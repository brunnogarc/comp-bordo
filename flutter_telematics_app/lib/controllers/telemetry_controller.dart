import 'package:get/get.dart';
import 'package:flutter_blue_plus/flutter_blue_plus.dart';
import '../services/bluetooth_service.dart' as telematics_bt;
import '../services/decoder_service.dart';
import '../services/storage_service.dart';
import '../models/telemetry_data.dart';
import '../models/unified_device.dart';
import 'dart:async';

class TelemetryController extends GetxController {
  final telematics_bt.TelematicsBluetoothService _bluetoothService = telematics_bt.TelematicsBluetoothService();
  final DecoderService _decoderService = DecoderService();
  final StorageService _storageService = StorageService();
  
  // Observables
  var isConnected = false.obs;
  var isAuthenticated = false.obs;
  var isScanning = false.obs;
  var deviceName = ''.obs;
  var lastReceivedData = ''.obs;
  var connectionStatus = 'Desconectado'.obs;
  
  // Dados armazenados localmente
  var lastDeviceName = ''.obs;
  var lastDeviceAddress = ''.obs;
  var storedUserCode = '0001020304050607'.obs; // Código padrão
  
  // Lista de dispositivos encontrados (ambos BLE e Classic)
  var unifiedDevices = <UnifiedDevice>[].obs;
  
  // Dados de telemetria
  var telemetryData = TelemetryData().obs;
  
  // Chave da empresa (deve ser configurada)
  List<int> companyKey = [0x00, 0x00, 0x00, 0x00];
  
  StreamSubscription? _dataSubscription;
  Timer? _requestTimer;

  @override
  void onInit() {
    super.onInit();
    _listenToBluetoothData();
  }

  @override
  void onClose() {
    _dataSubscription?.cancel();
    _requestTimer?.cancel();
    _bluetoothService.dispose();
    super.onClose();
  }

  // Escanear dispositivos
  Future<void> scanDevices() async {
    try {
      isScanning.value = true;
      unifiedDevices.clear();
      
      List<UnifiedDevice> devices = await _bluetoothService.scanAllDevices(
        timeout: const Duration(seconds: 10),
      );
      
      // Ordenar: último dispositivo primeiro, depois por outras prioridades
      devices.sort((a, b) {
        // Último dispositivo conectado vem primeiro
        bool aIsLast = (a.type == DeviceType.ble && a.bleDevice?.remoteId.toString() == lastDeviceAddress.value) ||
                       (a.type == DeviceType.classic && a.classicDevice?.address == lastDeviceAddress.value);
        bool bIsLast = (b.type == DeviceType.ble && b.bleDevice?.remoteId.toString() == lastDeviceAddress.value) ||
                       (b.type == DeviceType.classic && b.classicDevice?.address == lastDeviceAddress.value);
        
        if (aIsLast && !bIsLast) return -1;
        if (!aIsLast && bIsLast) return 1;
        
        // Dispositivos com nome vêm primeiro
        if (a.hasName && !b.hasName) return -1;
        if (!a.hasName && b.hasName) return 1;
        
        // Dispositivos pareados vêm antes
        if (a.isBonded && !b.isBonded) return -1;
        if (!a.isBonded && b.isBonded) return 1;
        
        // Se ambos têm ou não têm nome, ordenar por RSSI (mais forte = número maior = menos negativo)
        return b.rssi.compareTo(a.rssi);
      });
      
      unifiedDevices.value = devices;
      isScanning.value = false;
      
      if (devices.isEmpty) {
        Get.snackbar(
          'Aviso',
          'Nenhum dispositivo encontrado. Verifique se o Bluetooth está ligado e dispositivos próximos.',
          snackPosition: SnackPosition.BOTTOM,
          duration: const Duration(seconds: 3),
        );
      } else {
        int bleCount = devices.where((d) => d.type == DeviceType.ble).length;
        int classicCount = devices.where((d) => d.type == DeviceType.classic).length;
        Get.snackbar(
          'Sucesso',
          '$bleCount BLE + $classicCount Classic = ${devices.length} total',
          snackPosition: SnackPosition.BOTTOM,
          duration: const Duration(seconds: 2),
        );
      }
    } catch (e) {
      isScanning.value = false;
      Get.snackbar(
        'Erro',
        'Erro ao escanear: $e',
        snackPosition: SnackPosition.BOTTOM,
      );
    }
  }

  // Conectar ao dispositivo unificado
  Future<void> connectToUnifiedDevice(UnifiedDevice device) async {
    try {
      connectionStatus.value = 'Conectando...';
      
      if (device.type == DeviceType.ble && device.bleDevice != null) {
        bool connected = await _bluetoothService.connectToDevice(device.bleDevice!);
        
        if (connected) {
          isConnected.value = true;
          deviceName.value = device.name;
          connectionStatus.value = 'Conectado (BLE)';
          
          // Salvar último dispositivo conectado
          await _saveLastDevice(device.name, device.bleDevice!.remoteId.toString());
          
          Get.back();
          Get.snackbar(
            'Sucesso',
            'Conectado ao dispositivo BLE',
            snackPosition: SnackPosition.BOTTOM,
          );
          
          await Future.delayed(const Duration(seconds: 1));
          // O fluxo de dados será iniciado automaticamente após autenticação
        } else {
          connectionStatus.value = 'Falha na conexão';
          Get.snackbar(
            'Erro',
            'Não foi possível conectar ao dispositivo',
            snackPosition: SnackPosition.BOTTOM,
          );
        }
      } else if (device.type == DeviceType.classic && device.classicDevice != null) {
        // Conectar via Bluetooth Classic
        bool connected = await _bluetoothService.connectToClassicDevice(device.classicDevice!);
        
        if (connected) {
          isConnected.value = true;
          deviceName.value = device.name;
          connectionStatus.value = 'Conectado (Classic)';
          
          // Salvar último dispositivo conectado
          await _saveLastDevice(device.name, device.classicDevice!.address);
          
          Get.back();
          Get.snackbar(
            'Sucesso',
            'Conectado ao dispositivo Bluetooth Classic',
            snackPosition: SnackPosition.BOTTOM,
          );
          
          await Future.delayed(const Duration(seconds: 1));
          // O fluxo de dados será iniciado automaticamente após autenticação
        } else {
          connectionStatus.value = 'Falha na conexão';
          Get.snackbar(
            'Erro',
            'Não foi possível conectar ao dispositivo Classic',
            snackPosition: SnackPosition.BOTTOM,
          );
        }
      }
    } catch (e) {
      connectionStatus.value = 'Erro';
      Get.snackbar(
        'Erro',
        'Erro ao conectar: $e',
        snackPosition: SnackPosition.BOTTOM,
      );
    }
  }

  // Conectar ao dispositivo BLE (mantido para compatibilidade)
  Future<void> connectToDevice(BluetoothDevice device) async {
    try {
      connectionStatus.value = 'Conectando...';
      
      bool connected = await _bluetoothService.connectToDevice(device);
      
      if (connected) {
        isConnected.value = true;
        deviceName.value = device.platformName.isNotEmpty 
          ? device.platformName 
          : device.remoteId.toString();
        connectionStatus.value = 'Conectado';
        
        Get.back(); // Fecha a tela de scan
        Get.snackbar(
          'Sucesso',
          'Conectado ao dispositivo',
          snackPosition: SnackPosition.BOTTOM,
        );
        
        // Aguardar estabilização da conexão
        await Future.delayed(const Duration(seconds: 1));
        
        // O fluxo de dados será iniciado automaticamente após autenticação
      } else {
        connectionStatus.value = 'Falha na conexão';
        Get.snackbar(
          'Erro',
          'Não foi possível conectar ao dispositivo',
          snackPosition: SnackPosition.BOTTOM,
        );
      }
    } catch (e) {
      connectionStatus.value = 'Erro';
      Get.snackbar(
        'Erro',
        'Erro ao conectar: $e',
        snackPosition: SnackPosition.BOTTOM,
      );
    }
  }

  // Desconectar
  Future<void> disconnect() async {
    await _bluetoothService.disconnect();
    _requestTimer?.cancel();
    isConnected.value = false;
    isAuthenticated.value = false;
    deviceName.value = '';
    connectionStatus.value = 'Desconectado';
    telemetryData.value = TelemetryData();
  }

  // Carregar dados armazenados
  Future<void> loadStoredData() async {
    try {
      // Carregar último dispositivo
      Map<String, String?> lastDevice = await _storageService.getLastDevice();
      lastDeviceName.value = lastDevice['name'] ?? '';
      lastDeviceAddress.value = lastDevice['address'] ?? '';
      
      // Carregar código de usuário (mantém padrão se não houver)
      String? userCode = await _storageService.getUserCode();
      if (userCode != null && userCode.isNotEmpty) {
        storedUserCode.value = userCode;
      }
      // Se não houver código salvo, mantém o padrão 0001020304050607
    } catch (e) {
      print('Erro ao carregar dados armazenados: $e');
    }
  }

  // Salvar código de usuário
  Future<void> saveUserCode(String code) async {
    try {
      await _storageService.saveUserCode(code);
      storedUserCode.value = code;
    } catch (e) {
      print('Erro ao salvar código de usuário: $e');
    }
  }

  // Salvar último dispositivo conectado
  Future<void> _saveLastDevice(String name, String address) async {
    try {
      await _storageService.saveLastDevice(name, address);
      lastDeviceName.value = name;
      lastDeviceAddress.value = address;
    } catch (e) {
      print('Erro ao salvar último dispositivo: $e');
    }
  }

  // Reconectar ao último dispositivo salvo
  Future<void> reconnectToLastDevice() async {
    if (lastDeviceAddress.value.isEmpty) {
      Get.snackbar(
        'Aviso',
        'Nenhum dispositivo salvo para reconectar',
        snackPosition: SnackPosition.BOTTOM,
      );
      return;
    }

    try {
      connectionStatus.value = 'Buscando dispositivo...';
      
      // Fazer scan rápido para encontrar o dispositivo
      List<UnifiedDevice> devices = await _bluetoothService.scanAllDevices(
        timeout: const Duration(seconds: 5),
      );
      
      // Procurar o dispositivo pelo endereço
      UnifiedDevice? targetDevice;
      for (var device in devices) {
        if ((device.type == DeviceType.ble && device.bleDevice?.remoteId.toString() == lastDeviceAddress.value) ||
            (device.type == DeviceType.classic && device.classicDevice?.address == lastDeviceAddress.value)) {
          targetDevice = device;
          break;
        }
      }
      
      if (targetDevice != null) {
        await connectToUnifiedDevice(targetDevice);
      } else {
        connectionStatus.value = 'Dispositivo não encontrado';
        Get.snackbar(
          'Erro',
          'Dispositivo ${lastDeviceName.value} não encontrado. Verifique se está ligado e próximo.',
          snackPosition: SnackPosition.BOTTOM,
          duration: const Duration(seconds: 3),
        );
      }
    } catch (e) {
      connectionStatus.value = 'Erro na reconexão';
      Get.snackbar(
        'Erro',
        'Erro ao tentar reconectar: $e',
        snackPosition: SnackPosition.BOTTOM,
      );
    }
  }

  // Escutar dados do Bluetooth
  void _listenToBluetoothData() {
    _dataSubscription = _bluetoothService.dataStream.listen((data) {
      lastReceivedData.value = data;
      _processReceivedData(data);
    });
  }

  // Processar dados recebidos
  void _processReceivedData(String data) {
    print('Recebido: $data');
    
    try {
      if (data.contains('AT+BT_SEED=')) {
        String seed = data.substring(11).replaceAll(RegExp(r'[\r\n]'), '').trim().toUpperCase();
        _calculateAuth(seed);
      } else if (data.contains('AT+BT_AUTH_OK')) {
        isAuthenticated.value = true;
        // Enviar código de usuário armazenado após autenticação
        sendCommand('AT+BT_COD_USER=${storedUserCode.value}\r\n');
      } else if (data.contains('AT+BT_COD_USER_OK')) {
        Get.snackbar(
          'Autenticado',
          'Código de usuário aceito - Dispositivo pronto',
          snackPosition: SnackPosition.BOTTOM,
        );
        // Enviar comando para iniciar stream de dados (apenas uma vez)
        Future.delayed(const Duration(milliseconds: 500), () {
          sendCommand('AT_BT_DATA_START\r\n');
        });
      } else if (data.contains('AT+BT_NO_COD_USER')) {
        // Dispositivo precisa de código de usuário - reenviar com código armazenado
        sendCommand('AT+BT_COD_USER=${storedUserCode.value}\r\n');
      } else if (data.contains('AT+BT_DATA=')) {
        String telemetryHex = data.substring(11).toUpperCase();
        _decodeTelemetryData(telemetryHex);
        _sendDataOk();
      } else if (data.contains('AT+BT_PRM=')) {
        String prmHex = data.substring(10).toUpperCase();
        _decodePRMData(prmHex);
      } else {
        // Processar outras respostas de comandos GET
        _processCommandResponse(data);
      }
    } catch (e) {
      print('Erro ao processar dados: $e');
    }
  }

  // Calcular autenticação
  void _calculateAuth(String seed) {
    try {
      List<int> seedBytes = _decoderService.hexStringToBytes(seed);
      
      if (seedBytes.length < 8) {
        print('SEED inválido');
        return;
      }
      
      List<int> keyAuth = List.filled(8, 0);
      keyAuth[0] = seedBytes[4] ^ companyKey[3];
      keyAuth[1] = companyKey[1];
      keyAuth[2] = (keyAuth[0] + companyKey[2]) & 0xFF;
      keyAuth[3] = (keyAuth[1] + seedBytes[0]) & 0xFF;
      keyAuth[4] = keyAuth[2] ^ companyKey[0];
      keyAuth[5] = seedBytes[5] ^ keyAuth[3];
      keyAuth[6] = seedBytes[7] & keyAuth[2];
      keyAuth[7] = seedBytes[3] ^ keyAuth[6];
      
      String authHex = _decoderService.bytesToHexString(keyAuth);
      sendCommand('AT+BT_AUTH=$authHex\r\n');
    } catch (e) {
      print('Erro ao calcular autenticação: $e');
    }
  }

  // Decodificar dados de telemetria
  void _decodeTelemetryData(String hexData) {
    print('🔍 Decodificando telemetria: ${hexData.substring(0, 40)}...');
    print('📏 Tamanho da string hex: ${hexData.length} caracteres');
    
    Map<String, String> decoded = _decoderService.decodeFullTelemetry(hexData);
    
    print('📊 Dados decodificados:');
    print('  Velocidade: ${decoded['velocidade']}');
    print('  Bateria: ${decoded['bateria']}');
    print('  RPM: ${decoded['rpm']}');
    print('  Latitude: ${decoded['latitude']}');
    print('  Longitude: ${decoded['longitude']}');
    print('  Ignição: ${decoded['ignicao']}');
    
    var data = telemetryData.value;
    data.velocidade = decoded['velocidade'] ?? 'N/A';
    data.bateria = decoded['bateria'] ?? 'N/A';
    data.rpm = decoded['rpm'] ?? 'N/A';
    data.odometro = decoded['odometro'] ?? 'N/A';
    data.horimetro = decoded['horimetro'] ?? 'N/A';
    data.nivelCombustivel = decoded['nivelCombustivel'] ?? 'N/A';
    data.torqueMotor = decoded['torqueMotor'] ?? 'N/A';
    data.temperaturaMotor = decoded['temperaturaMotor'] ?? 'N/A';
    data.latitude = decoded['latitude'] ?? 'N/A';
    data.longitude = decoded['longitude'] ?? 'N/A';
    data.bussola = decoded['bussola'] ?? 'N/A';
    data.ignicao = decoded['ignicao'] ?? 'N/A';
    data.timestamp = DateTime.now();
    
    telemetryData.value = data;
    telemetryData.refresh();
    
    print('✅ Telemetria atualizada na tela');
  }

  // Decodificar dados PRM
  void _decodePRMData(String hexData) {
    // Implementar se necessário
  }

  // Processar respostas de comandos
  void _processCommandResponse(String data) {
    // Implementar processamento de respostas específicas
  }

  // Enviar confirmação de recebimento
  void _sendDataOk() {
    sendCommand('AT+BT_DATA_OK\r\n');
  }

  // Enviar comando
  Future<void> sendCommand(String command) async {
    try {
      bool sent = await _bluetoothService.sendCommandAuto(command);
      if (sent) {
        print('Enviado: $command');
      } else {
        print('Falha ao enviar: $command');
      }
    } catch (e) {
      print('Erro ao enviar comando: $e');
    }
  }

  // Iniciar solicitações periódicas (DESABILITADO - não é necessário)
  // O dispositivo envia dados automaticamente após AT_BT_DATA_START
  void startPeriodicRequests() {
    // _requestTimer?.cancel();
    // 
    // // Solicitar dados a cada 2 segundos
    // _requestTimer = Timer.periodic(const Duration(seconds: 2), (timer) {
    //   if (isConnected.value) {
    //     // Solicitar início de report (comando correto sem +)
    //     sendCommand('AT_BT_DATA_START\r\n');
    //   } else {
    //     timer.cancel();
    //   }
    // });
  }

  // Comandos GET específicos
  Future<void> requestVersion() async {
    await sendCommand('AT+BT_VERSION?\r\n');
  }

  Future<void> requestSerial() async {
    await sendCommand('AT+BT_GET_SERIAL?\r\n');
  }

  Future<void> requestHours() async {
    await sendCommand('AT+BT_HOURS_NOW?\r\n');
  }
}
