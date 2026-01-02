import 'package:flutter_blue_plus/flutter_blue_plus.dart';
import 'package:flutter_bluetooth_serial/flutter_bluetooth_serial.dart' as classic;
import 'dart:async';
import 'dart:typed_data';
import 'logger_service.dart';
import '../models/unified_device.dart';

class TelematicsBluetoothService {
  final _logger = LoggerService();
  
  BluetoothDevice? connectedDevice;
  BluetoothCharacteristic? txCharacteristic;
  BluetoothCharacteristic? rxCharacteristic;
  
  // Para Bluetooth Classic
  classic.BluetoothConnection? classicConnection;
  final classic.FlutterBluetoothSerial _bluetoothClassic = classic.FlutterBluetoothSerial.instance;
  
  // UUIDs comuns para dispositivos seriais BLE
  final String serviceUUID = "0000ffe0-0000-1000-8000-00805f9b34fb";
  final String characteristicUUID = "0000ffe1-0000-1000-8000-00805f9b34fb";
  
  final StreamController<String> _dataStreamController = StreamController<String>.broadcast();
  Stream<String> get dataStream => _dataStreamController.stream;
  
  String _buffer = '';

  // Escanear TODOS os dispositivos (BLE + Classic)
  Future<List<UnifiedDevice>> scanAllDevices({Duration timeout = const Duration(seconds: 10)}) async {
    List<UnifiedDevice> allDevices = [];
    
    _logger.bluetooth('SCAN', 'Iniciando scan de dispositivos BLE e Classic...');
    
    try {
      // 1. Escanear BLE
      _logger.info('BLE_SCAN', 'Escaneando dispositivos BLE...');
      List<ScanResult> bleResults = await scanDevices(timeout: timeout);
      
      for (var result in bleResults) {
        allDevices.add(UnifiedDevice.fromBLE(result));
      }
      _logger.success('BLE_SCAN', '${bleResults.length} dispositivo(s) BLE encontrado(s)');
      
      // 2. Escanear Bluetooth Classic (dispositivos pareados)
      _logger.info('CLASSIC_SCAN', 'Buscando dispositivos Bluetooth Classic pareados...');
      List<classic.BluetoothDevice> bondedDevices = await _bluetoothClassic.getBondedDevices();
      
      for (var device in bondedDevices) {
        // Evitar duplicatas (se um dispositivo é dual mode)
        bool isDuplicate = allDevices.any((d) => d.address == device.address);
        if (!isDuplicate) {
          allDevices.add(UnifiedDevice.fromClassic(device));
          _logger.debug('CLASSIC_SCAN', 'Classic pareado: ${device.name} (${device.address})');
        }
      }
      _logger.success('CLASSIC_SCAN', '${bondedDevices.length} dispositivo(s) Classic pareado(s)');
      
      _logger.success('SCAN', 'Total: ${allDevices.length} dispositivo(s) encontrado(s)');
      
    } catch (e) {
      _logger.error('SCAN', 'Erro ao escanear', e.toString());
    }
    
    return allDevices;
  }

  // Escanear dispositivos BLE
  Future<List<ScanResult>> scanDevices({Duration timeout = const Duration(seconds: 10)}) async {
    List<ScanResult> results = [];
    
    _logger.bluetooth('BLE_SCAN', 'Iniciando scan de dispositivos...');
    
    try {
      // Verificar se Bluetooth está disponível
      if (await FlutterBluePlus.isSupported == false) {
        _logger.error('BLE_SCAN', 'Bluetooth não suportado neste dispositivo');
        throw Exception("Bluetooth não suportado neste dispositivo");
      }
      _logger.success('BLE_SCAN', 'Bluetooth suportado');

      // Verificar se está ligado
      var adapterState = await FlutterBluePlus.adapterState.first;
      _logger.info('BLE_SCAN', 'Estado do adapter', adapterState);
      
      if (adapterState != BluetoothAdapterState.on) {
        _logger.error('BLE_SCAN', 'Bluetooth está desligado');
        throw Exception("Bluetooth desligado");
      }

      // Limpar scan anterior
      await FlutterBluePlus.stopScan();
      _logger.debug('BLE_SCAN', 'Scan anterior parado');
      
      // Iniciar novo scan
      await FlutterBluePlus.startScan(timeout: timeout);
      _logger.info('BLE_SCAN', 'Scan iniciado com timeout de ${timeout.inSeconds}s');
      
      // Aguardar o timeout completo para coletar todos os dispositivos
      await Future.delayed(timeout);
      
      // Obter resultados
      results = FlutterBluePlus.lastScanResults;
      
      // Parar scan
      await FlutterBluePlus.stopScan();
      
      _logger.success('BLE_SCAN', 'Scan concluído - ${results.length} dispositivo(s) encontrado(s)');
      
      // Listar dispositivos encontrados
      for (var result in results) {
        String name = 'Sem nome';
        
        // Tentar obter nome de várias fontes
        if (result.device.advName.isNotEmpty) {
          name = result.device.advName;
        } else if (result.device.platformName.isNotEmpty) {
          name = result.device.platformName;
        } else if (result.advertisementData.advName.isNotEmpty) {
          name = result.advertisementData.advName;
        } else if (result.advertisementData.localName.isNotEmpty) {
          name = result.advertisementData.localName;
        }
        
        _logger.debug('BLE_SCAN', 'Dispositivo: $name (${result.device.remoteId}) - RSSI: ${result.rssi} dBm');
      }
    } catch (e) {
      _logger.error('BLE_SCAN', 'Erro ao escanear', e.toString());
      await FlutterBluePlus.stopScan();
    }
    
    return results;
  }

  // Conectar ao dispositivo
  Future<bool> connectToDevice(BluetoothDevice device) async {
    _logger.bluetooth('BLE_CONNECT', 'Tentando conectar ao dispositivo: ${device.remoteId}');
    
    try {
      // Verificar se o dispositivo já está pareado
      bool isBonded = device.bondState == BluetoothBondState.bonded;
      _logger.info('BLE_PAIR', 'Status de pareamento: ${isBonded ? "Pareado" : "Não pareado"}');
      
      // Se não estiver pareado, fazer o pareamento
      if (!isBonded) {
        _logger.bluetooth('BLE_PAIR', 'Iniciando pareamento...');
        try {
          await device.createBond();
          _logger.success('BLE_PAIR', 'Pareamento solicitado - aguardando confirmação do usuário');
          
          // Aguardar até 30 segundos para o pareamento ser concluído
          int attempts = 0;
          while (attempts < 30 && device.bondState != BluetoothBondState.bonded) {
            await Future.delayed(const Duration(seconds: 1));
            attempts++;
            if (attempts % 5 == 0) {
              _logger.info('BLE_PAIR', 'Aguardando pareamento... (${attempts}s)');
            }
          }
          
          if (device.bondState == BluetoothBondState.bonded) {
            _logger.success('BLE_PAIR', 'Dispositivo pareado com sucesso!');
          } else {
            _logger.error('BLE_PAIR', 'Timeout ao aguardar pareamento', 'Usuário pode ter cancelado');
            return false;
          }
        } catch (e) {
          _logger.error('BLE_PAIR', 'Erro ao parear dispositivo', e.toString());
          return false;
        }
      }
      
      // Conectar ao dispositivo
      _logger.bluetooth('BLE_CONNECT', 'Conectando...');
      await device.connect(timeout: const Duration(seconds: 15));
      connectedDevice = device;
      _logger.success('BLE_CONNECT', 'Conectado com sucesso');
      
      // Aguardar um momento para estabilizar
      await Future.delayed(const Duration(milliseconds: 500));
      
      // Descobrir serviços
      _logger.info('BLE_CONNECT', 'Descobrindo serviços...');
      List<BluetoothService> services = await device.discoverServices();
      _logger.success('BLE_CONNECT', '${services.length} serviço(s) encontrado(s)');
      
      for (BluetoothService service in services) {
        _logger.debug('BLE_CONNECT', 'Serviço: ${service.uuid}');
        
        for (BluetoothCharacteristic characteristic in service.characteristics) {
          _logger.debug('BLE_CONNECT', '  └─ Característica: ${characteristic.uuid}');
          _logger.debug('BLE_CONNECT', '     Properties: R:${characteristic.properties.read} W:${characteristic.properties.write} N:${characteristic.properties.notify}');
          
          // Tentar encontrar a característica de comunicação
          if (characteristic.properties.write || characteristic.properties.writeWithoutResponse) {
            txCharacteristic = characteristic;
            _logger.success('BLE_CONNECT', 'TX Característica encontrada: ${characteristic.uuid}');
          }
          if (characteristic.properties.notify || characteristic.properties.read) {
            rxCharacteristic = characteristic;
            _logger.success('BLE_CONNECT', 'RX Característica encontrada: ${characteristic.uuid}');
            
            // Habilitar notificações
            await characteristic.setNotifyValue(true);
            _logger.info('BLE_CONNECT', 'Notificações habilitadas');
            
            // Escutar dados recebidos
            characteristic.lastValueStream.listen((value) {
              if (value.isNotEmpty) {
                _logger.data('BLE_RX', 'Dados recebidos: ${value.length} bytes');
                _processReceivedData(value);
              }
            });
          }
        }
      }
      
      return true;
    } catch (e) {
      _logger.error('BLE_CONNECT', 'Erro ao conectar', e.toString());
      return false;
    }
  }

  // Conectar a dispositivo Bluetooth Classic
  Future<bool> connectToClassicDevice(classic.BluetoothDevice device) async {
    _logger.bluetooth('CLASSIC_CONNECT', 'Tentando conectar ao dispositivo Classic: ${device.address}');
    
    try {
      // Verificar se já está pareado
      if (!device.isBonded) {
        _logger.error('CLASSIC_CONNECT', 'Dispositivo não está pareado', 'Pareie o dispositivo nas configurações do Android');
        return false;
      }
      
      _logger.info('CLASSIC_CONNECT', 'Dispositivo pareado, conectando...');
      
      // Conectar via serial
      classicConnection = await classic.BluetoothConnection.toAddress(device.address);
      _logger.success('CLASSIC_CONNECT', 'Conectado com sucesso via Bluetooth Classic');
      
      // Escutar dados recebidos
      classicConnection!.input!.listen((data) {
        String received = String.fromCharCodes(data);
        _logger.data('CLASSIC_RX', 'Dados recebidos', received);
        _dataStreamController.add(received);
      }).onDone(() {
        _logger.info('CLASSIC_CONNECT', 'Conexão encerrada');
        classicConnection = null;
      });
      
      return true;
    } catch (e) {
      _logger.error('CLASSIC_CONNECT', 'Erro ao conectar', e.toString());
      return false;
    }
  }

  // Enviar comando
  Future<void> sendCommand(List<int> data) async {
    _logger.bluetooth('BLE_TX', 'Enviando comando: ${data.map((b) => b.toRadixString(16).padLeft(2, '0')).join(' ')}');
    
    if (txCharacteristic == null) {
      _logger.error('BLE_TX', 'Característica TX não encontrada');
      throw Exception("Característica TX não encontrada");
    }

    try {
      await txCharacteristic!.write(data, withoutResponse: false);
      _logger.success('BLE_TX', 'Comando enviado com sucesso');
    } catch (e) {
      _logger.error('BLE_TX', 'Erro ao enviar comando', e.toString());
      rethrow;
    }
  }

  // Enviar comando automaticamente (detecta BLE ou Classic)
  Future<bool> sendCommandAuto(String command) async {
    // Se tiver conexão Classic ativa, usar Classic
    if (classicConnection != null && classicConnection!.isConnected) {
      return await sendCommandClassic(command);
    }
    // Senão, tentar BLE
    else if (txCharacteristic != null) {
      try {
        await sendCommand(command.codeUnits);
        return true;
      } catch (e) {
        _logger.error('BLE_TX', 'Erro ao enviar via BLE', e.toString());
        return false;
      }
    } else {
      _logger.error('SEND', 'Nenhuma conexão ativa (BLE ou Classic)');
      return false;
    }
  }

  // Enviar comando via Bluetooth Classic
  Future<bool> sendCommandClassic(String command) async {
    _logger.bluetooth('CLASSIC_TX', 'Enviando comando: $command');
    
    try {
      if (classicConnection == null || !classicConnection!.isConnected) {
        _logger.error('CLASSIC_TX', 'Conexão Classic não está ativa');
        return false;
      }
      
      classicConnection!.output.add(Uint8List.fromList(command.codeUnits));
      await classicConnection!.output.allSent;
      _logger.success('CLASSIC_TX', 'Comando enviado via Classic');
      return true;
    } catch (e) {
      _logger.error('CLASSIC_TX', 'Erro ao enviar comando', e.toString());
      return false;
    }
  }

  // Processar dados recebidos
  void _processReceivedData(List<int> data) {
    _logger.data('BLE_RX_PROCESS', 'Processando ${data.length} bytes');
    
    // Converter bytes para string hexadecimal
    String hexString = data.map((b) => b.toRadixString(16).padLeft(2, '0')).join('');
    _buffer += hexString;
    
    _logger.debug('BLE_RX_PROCESS', 'Buffer atual: $_buffer (${_buffer.length} chars)');
    
    // Procurar por pacotes completos
    while (_buffer.contains('##') && _buffer.contains('**')) {
      int start = _buffer.indexOf('##');
      int end = _buffer.indexOf('**', start);
      
      if (end != -1) {
        String packet = _buffer.substring(start, end + 2);
        _logger.success('BLE_RX_PROCESS', 'Pacote completo encontrado: $packet');
        _dataStreamController.add(packet);
        _buffer = _buffer.substring(end + 2);
        _logger.debug('BLE_RX_PROCESS', 'Buffer após extração: $_buffer');
      } else {
        _logger.debug('BLE_RX_PROCESS', 'Aguardando fim do pacote...');
        break;
      }
    }
    
    // Limitar tamanho do buffer
    if (_buffer.length > 1000) {
      _logger.warning('BLE_RX_PROCESS', 'Buffer muito grande, limpando');
      _buffer = '';
    }
  }

  // Desconectar
  Future<void> disconnect() async {
    _logger.bluetooth('DISCONNECT', 'Desconectando...');
    
    // Desconectar Classic
    if (classicConnection != null) {
      try {
        await classicConnection!.close();
        _logger.success('CLASSIC_DISCONNECT', 'Bluetooth Classic desconectado com sucesso');
      } catch (e) {
        _logger.error('CLASSIC_DISCONNECT', 'Erro ao desconectar Classic', e.toString());
      }
      classicConnection = null;
    }
    
    // Desconectar BLE
    if (connectedDevice != null) {
      try {
        await connectedDevice!.disconnect();
        _logger.success('BLE_DISCONNECT', 'BLE desconectado com sucesso');
      } catch (e) {
        _logger.error('BLE_DISCONNECT', 'Erro ao desconectar BLE', e.toString());
      }
      connectedDevice = null;
      txCharacteristic = null;
      rxCharacteristic = null;
    }
  }

  // Limpar recursos
  void dispose() {
    _logger.info('BLE_DISPOSE', 'Limpando recursos do serviço Bluetooth');
    disconnect();
    _dataStreamController.close();
  }
}
