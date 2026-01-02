# Guia de Desenvolvimento - Aplicação Flutter para Blue Telematics

## Visão Geral

Este documento descreve como desenvolver uma aplicação Flutter que replique as funcionalidades de leitura (GET) da aplicação Java AG-TELEMATICS-DECODER para comunicação com dispositivos Blue Telematics via BLE (Bluetooth Low Energy) e WiFi.

---

## 1. Arquitetura da Aplicação

### 1.1 Estrutura do Projeto Flutter

```
lib/
├── main.dart
├── models/
│   ├── device_model.dart
│   ├── telemetry_data.dart
│   ├── prm_data.dart
│   └── serial_configuration.dart
├── services/
│   ├── bluetooth_service.dart
│   ├── decoder_service.dart
│   ├── ag_decoder_service.dart
│   ├── prm_decoder_service.dart
│   └── prm_v2_decoder_service.dart
├── controllers/
│   ├── connection_controller.dart
│   └── telemetry_controller.dart
└── views/
    ├── home_screen.dart
    ├── connection_screen.dart
    ├── decoder_screen.dart
    └── settings_screen.dart
```

---

## 2. Dependências Necessárias

Adicione no arquivo `pubspec.yaml`:

```yaml
dependencies:
  flutter:
    sdk: flutter
  
  # Bluetooth
  flutter_blue_plus: ^1.31.0
  
  # Permissões
  permission_handler: ^11.0.1
  
  # State Management
  provider: ^6.1.1
  get: ^4.6.6
  
  # Utilities
  intl: ^0.18.1
  convert: ^3.1.1
```

---

## 3. Protocolo de Comunicação

### 3.1 Comandos AT disponíveis

Baseado no código Java, a aplicação utiliza comandos AT para comunicação serial/BLE:

#### **Comandos GET (Leitura)**

| Comando | Descrição | Resposta Esperada |
|---------|-----------|-------------------|
| `AT+BT_GET_SERIAL?` | Obter número de série | `AT+BT_SERIAL=XXXXXXXX` |
| `AT+BT_VERSION?` | Obter versão do firmware | `AT+BT_VERSION=X.X` |
| `AT+BT_REPORT_CYCLE?` | Obter ciclo de report | `AT+BT_REPORT_CYCLE=XXXX` |
| `AT+BT_REPORT_CYCLE_SPEED?` | Obter ciclo de report por velocidade | `AT+BT_REPORT_CYCLE_SPEED=XXXX` |
| `AT+BT_TIME_MIN_SEND_MSG?` | Obter tempo mínimo entre mensagens | `AT+BT_TIME_MIN_SEND_MSG=XXX` |
| `AT+BT_MAX_TIME_AUTH?` | Obter tempo máximo de autenticação | `AT+BT_MAX_TIME_AUTH=XXX` |
| `AT+BT_MAX_TIME_RCV_ACK?` | Obter tempo máximo para receber ACK | `AT+BT_MAX_TIME_RCV_ACK=XXX` |
| `AT+BT_NAME_DEVICE?` | Obter nome do dispositivo BLE | `AT+BT_NAME_DEVICE=XXXXX` |
| `AT+BT_SPEED_CHANGE_CYCLE_REPORT?` | Obter velocidade de mudança de ciclo | `AT+BT_SPEED_CHANGE_CYCLE_REPORT=XX` |
| `AT+BT_HOURS_INIT?` | Obter horímetro inicial | `AT+BT_HOURS_INIT=XXXXXXXX` |
| `AT+BT_HOURS_COUNT?` | Obter contador de horas | `AT+BT_HOURS_COUNT=XXXXXXXX` |
| `AT+BT_HOURS_NOW?` | Obter horímetro atual | `AT+BT_HOURS_NOW=XXXXXXXX` |
| `AT+BT_ANG_COMPASS?` | Obter ângulo da bússola | `AT+BT_ANG_COMPASS=XXX` |
| `AT+BT_CONF_COMPASS?` | Obter configuração da bússola | `AT+BT_CONF_COMPASS=X` |
| `AT+BT_RPM_NOW?` | Obter RPM atual | `AT+BT_RPM_NOW=XXXX` |
| `AT+BT_REPORT_CYCLE_IG_OFF?` | Obter ciclo de report com ignição OFF | `AT+BT_REPORT_CYCLE_IG_OFF=XXXX` |
| `AT+BT_CHECK_CONFIG_SERVER?` | Verificar tempo de checagem do servidor | `AT+BT_CHECK_CONFIG_SERVER=XXX` |
| `AT+BT_STATUS_WIFI?` | Obter status do WiFi | `AT+BT_STATUS_WIFI=X` |
| `AT+BT_STATUS_CONN?` | Obter status da conexão | `AT+BT_STATUS_CONN=X` |
| `AT+BT_GET_WIFI_BUF=X` | Obter configuração WiFi (1-5) | `AT+BT_GET_WIFI_BUF=X,SSID,PASSWORD` |
| `AT+BT_GET_SERVER_BUF=X` | Obter configuração de servidor (1-5) | `AT+BT_GET_SERVER_BUF=X,IP,PORT` |
| `AT+BT_HOURS_ESTEIRA_NOW?` | Obter horas da esteira atual | `AT+BT_HOURS_ESTEIRA_NOW=XXXX` |
| `AT+BT_HOURS_ESTEIRA_INIT?` | Obter horas da esteira inicial | `AT+BT_HOURS_ESTEIRA_INIT=XXXX` |
| `AT+BT_HOURS_ESTEIRA_COUNT?` | Obter contador de horas da esteira | `AT+BT_HOURS_ESTEIRA_COUNT=XXXX` |
| `AT+BT_HOURS_PILOTO_NOW?` | Obter horas do piloto atual | `AT+BT_HOURS_PILOTO_NOW=XXXX` |
| `AT+BT_HOURS_PILOTO_INIT?` | Obter horas do piloto inicial | `AT+BT_HOURS_PILOTO_INIT=XXXX` |
| `AT+BT_HOURS_PILOTO_COUNT?` | Obter contador de horas do piloto | `AT+BT_HOURS_PILOTO_COUNT=XXXX` |

### 3.2 Fluxo de Autenticação

1. **Conexão inicial**: Conectar ao dispositivo via BLE
2. **Receber SEED**: Aguardar `AT+BT_SEED=XXXXXXXXXXXXXXXX`
3. **Calcular AUTH**: Calcular chave de autenticação usando a fórmula:
```
btc_keyAUTH[0] = btc_seedAUTH[4] ^ btc_KeyCompany[3]
btc_keyAUTH[1] = btc_KeyCompany[1]
btc_keyAUTH[2] = (btc_keyAUTH[0] + btc_KeyCompany[2]) & 0xFF
btc_keyAUTH[3] = (btc_keyAUTH[1] + btc_seedAUTH[0]) & 0xFF
btc_keyAUTH[4] = btc_keyAUTH[2] ^ btc_KeyCompany[0]
btc_keyAUTH[5] = btc_seedAUTH[5] ^ btc_keyAUTH[3]
btc_keyAUTH[6] = btc_seedAUTH[7] & btc_keyAUTH[2]
btc_keyAUTH[7] = btc_seedAUTH[3] ^ btc_keyAUTH[6]
```
4. **Enviar AUTH**: `AT+BT_AUTH=XXXXXXXXXXXXXXXX`
5. **Confirmar**: Aguardar `AT+BT_AUTH_OK`

### 3.3 Fluxo de Dados de Telemetria

O dispositivo envia dados de duas formas:

1. **AT+BT_DATA=** - Dados de telemetria completos
2. **AT+BT_PRM=** - Parâmetros do veículo

Ambos requerem validação CRC antes de confirmar com `AT+BT_DATA_OK`.

---

## 4. Implementação dos Decodificadores

### 4.1 DecoderMSG (Mensagem Simples)

Esta classe decodifica mensagens básicas de telemetria.

```dart
class DecoderMSG {
  // Decodifica número de série (bytes 0-7)
  String decodeSerial(String data) {
    String result = data.substring(0, 8);
    int tmp = int.parse(result, radix: 16);
    result = tmp.toString().padLeft(8, '0');
    return result;
  }

  // Decodifica versão do firmware (byte 8, bits 4-7)
  String decodeVerFW(String data) {
    String result = data.substring(8, 10);
    int byte1 = int.parse(result, radix: 16);
    int tmp2 = (byte1 & 0xF0) >> 4;
    return tmp2.toString();
  }

  // Decodifica tipo de mensagem (byte 8, bits 0-3)
  String decodeTypMSG(String data) {
    String result = data.substring(8, 10);
    int byte1 = int.parse(result, radix: 16);
    int tmp2 = byte1 & 0x0F;
    return tmp2.toString();
  }

  // Decodifica quantidade de mensagens na flash (bytes 10-13)
  String decodeQtdMsgFLASH(String data) {
    String result = data.substring(10, 14);
    int tmp = int.parse(result, radix: 16);
    return tmp.toString();
  }

  // Decodifica status do motor (byte 14, bit 0)
  String decodeEngON(String data) {
    String result = data.substring(14, 16);
    int byte1 = int.parse(result, radix: 16);
    int tmp2 = byte1 & 0x01;
    return tmp2 == 1 ? "Ligado" : "Desligado";
  }

  // Decodifica status da ignição (byte 14, bit 1)
  String decodeIngON(String data) {
    String result = data.substring(14, 16);
    int byte1 = int.parse(result, radix: 16);
    int tmp2 = (byte1 & 0x02) >> 1;
    return tmp2 == 1 ? "Ligado" : "Desligado";
  }

  // Decodifica status do GPS (byte 14, bit 2)
  String decodeStsGPS(String data) {
    String result = data.substring(14, 16);
    int byte1 = int.parse(result, radix: 16);
    int tmp2 = (byte1 & 0x04) >> 2;
    return tmp2 == 1 ? "Valido" : "Invalido";
  }

  // Decodifica timestamp (bytes 16-23)
  String decodeTimestamp(String data) {
    String result = data.substring(16, 24);
    return convertTimeUTC(result);
  }

  // Decodifica horímetro (bytes 24-31)
  String decodeHorimetro(String data) {
    String result = data.substring(24, 32);
    if (result == "FFFFFFFF") {
      return "Não avaliado";
    }
    double tmp = int.parse(result, radix: 16).toDouble();
    tmp = tmp * 0.05;
    return tmp.toStringAsFixed(2);
  }

  // Decodifica latitude (bytes 32-39)
  String decodeLatitude(String data) {
    String result = data.substring(32, 40);
    if (result == "FFFFFFFF") {
      return "Não avaliado";
    }
    int recLat = int.parse(result, radix: 16);
    double latFinal = (recLat - 4294967296) / 10000000.0;
    return latFinal.toString();
  }

  // Decodifica longitude (bytes 40-47)
  String decodeLongitude(String data) {
    String result = data.substring(40, 48);
    if (result == "FFFFFFFF") {
      return "Não avaliado";
    }
    int recLon = int.parse(result, radix: 16);
    double lonFinal = (recLon - 4294967296) / 10000000.0;
    return lonFinal.toString();
  }

  // Decodifica velocidade (byte 48)
  String decodeVelocidade(String data) {
    String result = data.substring(48, 50);
    if (result == "FF") {
      return "Não avaliado";
    }
    double tmp = int.parse(result, radix: 16).toDouble();
    return tmp.toStringAsFixed(1);
  }

  // Decodifica RPM (bytes 50-53)
  String decodeRPM(String data) {
    String result = data.substring(50, 54);
    if (result == "FFFF") {
      return "Não avaliado";
    }
    double tmp = int.parse(result, radix: 16).toDouble();
    tmp = tmp * 0.125;
    return tmp.toStringAsFixed(1);
  }

  // Decodifica bússola
  String decodeBussola(String data, int protocol) {
    String result = "FFFF";
    if (protocol > 1) {
      result = data.substring(54, 58);
    } else {
      result = data.substring(50, 54);
    }
    if (result == "FFFF") {
      return "Não avaliado";
    }
    double tmp = int.parse(result, radix: 16).toDouble();
    return tmp.toStringAsFixed(1);
  }

  // Decodifica código do usuário
  String decodeCodUser(String data, int protocol) {
    String result = "FFFFFFFFFFFFFFFF";
    if (protocol > 1) {
      result = data.substring(58, data.length - 2);
    } else {
      result = data.substring(54, data.length - 2);
    }
    if (result == "FFFFFFFFFFFFFFFF") {
      return "Não informado";
    }
    double tmp = int.parse(result, radix: 16).toDouble();
    return tmp.toStringAsFixed(0);
  }

  // Converte timestamp UTC
  String convertTimeUTC(String time) {
    int lonUpdateTime = int.parse(time, radix: 16);
    DateTime date = DateTime.fromMillisecondsSinceEpoch(lonUpdateTime * 1000);
    return date.toString();
  }
}
```

### 4.2 DecodeTelematics (Telemetria Completa)

Esta classe decodifica dados completos de telemetria do veículo.

```dart
class DecodeTelematics {
  // Reutilizar os métodos básicos do DecoderMSG
  
  // Decodifica odômetro (bytes 72-79)
  String decodeOdometro(String data) {
    String result = data.substring(72, 80);
    if (result == "FFFFFFFF") {
      return "Não avaliado";
    }
    double tmp = int.parse(result, radix: 16).toDouble();
    tmp = tmp * 0.125;
    return tmp.toStringAsFixed(2);
  }

  // Decodifica velocidade (bytes 80-83)
  String decodeVelocidade(String data) {
    String result = data.substring(80, 84);
    if (result == "FFFF") {
      return "Não avaliado";
    }
    double tmp = int.parse(result, radix: 16).toDouble();
    return tmp.toStringAsFixed(1);
  }

  // Decodifica pedal do acelerador (bytes 88-89)
  String decodePedalAcelerador(String data) {
    String result = data.substring(88, 90);
    if (result == "FF") {
      return "Não avaliado";
    }
    double tmp = int.parse(result, radix: 16).toDouble();
    tmp = tmp * 0.4;
    return tmp.toStringAsFixed(1);
  }

  // Decodifica torque do motor (bytes 90-91)
  String decodeTorqueMotor(String data) {
    String result = data.substring(90, 92);
    if (result == "FF") {
      return "Não avaliado";
    }
    double tmp = int.parse(result, radix: 16).toDouble();
    tmp = tmp - 125;
    return tmp.toStringAsFixed(1);
  }

  // Decodifica carga do motor (bytes 92-93)
  String decodeCargaMotor(String data) {
    String result = data.substring(92, 94);
    if (result == "FF") {
      return "Não avaliado";
    }
    double tmp = int.parse(result, radix: 16).toDouble();
    tmp = tmp * 0.4;
    return tmp.toStringAsFixed(1);
  }

  // Decodifica pressão do turbo (bytes 94-95)
  String decodePressaoTurbo(String data) {
    String result = data.substring(94, 96);
    if (result == "FF") {
      return "Não avaliado";
    }
    double tmp = int.parse(result, radix: 16).toDouble();
    tmp = tmp * 0.05;
    return tmp.toStringAsFixed(1);
  }

  // Decodifica temperatura da água (bytes 108-109)
  String decodeTempAgua(String data) {
    String result = data.substring(108, 110);
    if (result == "FF") {
      return "Não avaliado";
    }
    double tmp = int.parse(result, radix: 16).toDouble();
    tmp = tmp - 40;
    return tmp.toStringAsFixed(1);
  }

  // Decodifica nível de combustível (bytes 126-127)
  String decodeNivelComb(String data) {
    String result = data.substring(126, 128);
    if (result == "FF") {
      return "Não avaliado";
    }
    double tmp = int.parse(result, radix: 16).toDouble();
    tmp = tmp * 0.4;
    return tmp.toStringAsFixed(1);
  }

  // Decodifica status DTC (bytes 132-133, bits 0-1)
  String decodeDTC(String data) {
    String result = data.substring(132, 134);
    int byte1 = int.parse(result, radix: 16);
    int tmp2 = byte1 & 0x03;
    if (tmp2 == 0) return "OFF";
    if (tmp2 == 1) return "ON";
    return "NA";
  }

  // Decodifica status da tomada de força (bytes 138-139, bits 0-1)
  String decodeTomadaForca(String data) {
    String result = data.substring(138, 140);
    int byte1 = int.parse(result, radix: 16);
    int tmp2 = byte1 & 0x03;
    if (tmp2 == 0) return "DESLIGADO";
    if (tmp2 == 1) return "LIGADO";
    return "Não Avaliado";
  }
}
```

### 4.3 AgDecodeTelematics (Telemetria Agrícola)

Esta classe decodifica dados específicos de equipamentos agrícolas.

```dart
class AgDecodeTelematics {
  // Inclui todos os métodos do DecodeTelematics, mais:
  
  // Decodifica tipo de mensagem (byte 10, bits 4-7)
  String decodeTypMSG(String data) {
    String result = data.substring(10, 12);
    int byte1 = int.parse(result, radix: 16);
    int tmp2 = (byte1 & 0xF0) >> 4;
    String tipo = "Não identificado";
    if (tmp2 == 1) tipo = "ALERT";
    if (tmp2 == 2) tipo = "REPORT";
    if (tmp2 == 3) tipo = "KEEP";
    return "$tmp2 - $tipo";
  }

  // Decodifica ID do alerta (byte 14)
  String decodeIdAlerta(String data) {
    String result = data.substring(14, 16);
    int byte1 = int.parse(result, radix: 16);
    String descricao = "Não identificado";
    
    switch (byte1) {
      case 0: descricao = "Não identificado"; break;
      case 1: descricao = "STS Bussola"; break;
      case 2: descricao = "STS pós-chave"; break;
      case 3: descricao = "STS Motor"; break;
      case 4: descricao = "STS Velocidade"; break;
      case 5: descricao = "Gerado tempo"; break;
      case 255: descricao = "CPU reset"; break;
      default: descricao = "Invalido";
    }
    
    return "$byte1 - $descricao";
  }

  // Decodifica tipo de FIX GPS (byte 48)
  String decodeTipoFIX(String data) {
    String result = data.substring(48, 50);
    int byte1 = int.parse(result, radix: 16);
    String tipo = "NA";
    
    if (byte1 == 0) tipo = "NA";
    if (byte1 == 1) tipo = "SEM FIX";
    if (byte1 == 2) tipo = "2D";
    if (byte1 == 3) tipo = "3D";
    
    return "$byte1 - $tipo";
  }

  // Decodifica HDOP (bytes 50-53)
  String decodeHDOP(String data) {
    String result = data.substring(50, 54);
    if (result == "FFFF") {
      return "Não avaliado";
    }
    double tmp = int.parse(result, radix: 16).toDouble();
    tmp = tmp * 0.01;
    double metros = tmp * 1.5;
    return "${tmp.toStringAsFixed(2)} - ${metros.toStringAsFixed(2)} mts";
  }

  // Decodifica quantidade de satélites (byte 54-55)
  String decodeQtdSAT(String data) {
    String result = data.substring(54, 56);
    int byte1 = int.parse(result, radix: 16);
    return byte1.toString();
  }

  // Decodifica tipo de conexão (byte 60-61)
  String decodeConnTech(String data) {
    String result = data.substring(60, 62);
    if (result == "FF") {
      return "Não avaliado";
    }
    int byte1 = int.parse(result, radix: 16);
    if (byte1 == 0) return "Sem conexão";
    if (byte1 == 1) return "LTE";
    if (byte1 == 2) return "WIFI";
    return "Não avaliado";
  }

  // Decodifica potência do sinal (byte 62-63)
  String decodePowerSignal(String data) {
    String result = data.substring(62, 64);
    if (result == "FF") {
      return "Não avaliado";
    }
    int rssi = int.parse(result, radix: 16);
    String qualidade = "Sem sinal";
    
    if (rssi <= 50) qualidade = "Excelente";
    else if (rssi <= 60) qualidade = "Muito bom";
    else if (rssi <= 70) qualidade = "Bom";
    else if (rssi <= 80) qualidade = "Fraco";
    else if (rssi <= 90) qualidade = "Muito fraco";
    
    return "-${rssi}dBm - $qualidade";
  }
}
```

### 4.4 DecodePRM e DecodePRMv2 (Parâmetros do Veículo)

```dart
class DecodePRM {
  // Versão do firmware (byte 0, bits 4-7)
  String decodeVerFW(String data) {
    String result = data.substring(0, 2);
    int byte1 = int.parse(result, radix: 16);
    int tmp2 = (byte1 & 0xF0) >> 4;
    return tmp2.toString();
  }

  // Versão do protocolo (byte 0, bits 0-3)
  String decodeVerPROT(String data) {
    String result = data.substring(0, 2);
    int byte1 = int.parse(result, radix: 16);
    int tmp2 = byte1 & 0x0F;
    return tmp2.toString();
  }

  // Os demais métodos seguem a mesma lógica do DecodeTelematics
  // mas com posições diferentes dos bytes
}

class DecodePRMv2 {
  // Similar ao DecodePRM mas com estrutura ligeiramente diferente
  // conforme versão 2 do protocolo
}
```

---

## 5. Implementação do Serviço Bluetooth

### 5.1 BluetoothService

```dart
import 'package:flutter_blue_plus/flutter_blue_plus.dart';

class BluetoothService {
  BluetoothDevice? connectedDevice;
  BluetoothCharacteristic? txCharacteristic;
  BluetoothCharacteristic? rxCharacteristic;
  
  // UUIDs do serviço BLE (verificar no dispositivo)
  final String serviceUUID = "0000ffe0-0000-1000-8000-00805f9b34fb";
  final String txCharacteristicUUID = "0000ffe1-0000-1000-8000-00805f9b34fb";
  final String rxCharacteristicUUID = "0000ffe1-0000-1000-8000-00805f9b34fb";
  
  Stream<String> receivedDataStream = const Stream.empty();
  
  // Escanear dispositivos
  Future<List<ScanResult>> scanDevices({Duration timeout = const Duration(seconds: 4)}) async {
    List<ScanResult> scanResults = [];
    
    await FlutterBluePlus.startScan(timeout: timeout);
    
    FlutterBluePlus.scanResults.listen((results) {
      scanResults = results;
    });
    
    await Future.delayed(timeout);
    await FlutterBluePlus.stopScan();
    
    return scanResults;
  }
  
  // Conectar ao dispositivo
  Future<bool> connectToDevice(BluetoothDevice device) async {
    try {
      await device.connect(timeout: const Duration(seconds: 10));
      connectedDevice = device;
      
      // Descobrir serviços
      List<BluetoothService> services = await device.discoverServices();
      
      for (BluetoothService service in services) {
        if (service.uuid.toString() == serviceUUID) {
          for (BluetoothCharacteristic characteristic in service.characteristics) {
            if (characteristic.uuid.toString() == txCharacteristicUUID) {
              txCharacteristic = characteristic;
            }
            if (characteristic.uuid.toString() == rxCharacteristicUUID) {
              rxCharacteristic = characteristic;
              // Habilitar notificações
              await characteristic.setNotifyValue(true);
              _listenToCharacteristic();
            }
          }
        }
      }
      
      return true;
    } catch (e) {
      print('Erro ao conectar: $e');
      return false;
    }
  }
  
  // Escutar dados recebidos
  void _listenToCharacteristic() {
    if (rxCharacteristic == null) return;
    
    receivedDataStream = rxCharacteristic!.value.map((value) {
      return String.fromCharCodes(value);
    });
  }
  
  // Enviar comando
  Future<void> sendCommand(String command) async {
    if (txCharacteristic == null) {
      throw Exception('Dispositivo não conectado');
    }
    
    List<int> bytes = command.codeUnits;
    await txCharacteristic!.write(bytes, withoutResponse: false);
  }
  
  // Desconectar
  Future<void> disconnect() async {
    if (connectedDevice != null) {
      await connectedDevice!.disconnect();
      connectedDevice = null;
      txCharacteristic = null;
      rxCharacteristic = null;
    }
  }
}
```

---

## 6. Controller de Telemetria

```dart
import 'package:get/get.dart';

class TelemetryController extends GetxController {
  final BluetoothService _bluetoothService = BluetoothService();
  final DecoderMSG _decoderMSG = DecoderMSG();
  final DecodeTelematics _decodeTelematics = DecodeTelematics();
  final AgDecodeTelematics _agDecodeTelematics = AgDecodeTelematics();
  
  // Observables
  var isConnected = false.obs;
  var isAuthenticated = false.obs;
  var lastReceivedData = ''.obs;
  
  // Dados decodificados
  var serialNumber = ''.obs;
  var firmwareVersion = ''.obs;
  var latitude = ''.obs;
  var longitude = ''.obs;
  var speed = ''.obs;
  var rpm = ''.obs;
  var engineOn = ''.obs;
  var ignitionOn = ''.obs;
  var gpsStatus = ''.obs;
  
  @override
  void onInit() {
    super.onInit();
    _listenToBluetoothData();
  }
  
  // Escutar dados do Bluetooth
  void _listenToBluetoothData() {
    _bluetoothService.receivedDataStream.listen((data) {
      lastReceivedData.value = data;
      _processReceivedData(data);
    });
  }
  
  // Processar dados recebidos
  void _processReceivedData(String data) {
    if (data.contains('AT+BT_SEED=')) {
      String seed = data.substring(11).toUpperCase();
      _calculateAuth(seed);
    } else if (data.contains('AT+BT_AUTH_OK')) {
      isAuthenticated.value = true;
      Get.snackbar('Sucesso', 'Dispositivo autenticado!');
    } else if (data.contains('AT+BT_DATA=')) {
      String telemetryData = data.substring(11).toUpperCase();
      _decodeTelemetryData(telemetryData);
    } else if (data.contains('AT+BT_PRM=')) {
      String prmData = data.substring(10).toUpperCase();
      _decodePRMData(prmData);
    }
  }
  
  // Calcular autenticação
  void _calculateAuth(String seed) {
    // Implementar algoritmo de autenticação
    List<int> seedBytes = _hexStringToBytes(seed);
    List<int> keyCompany = [0x00, 0x00, 0x00, 0x00]; // Chave da empresa
    
    List<int> keyAuth = List.filled(8, 0);
    keyAuth[0] = seedBytes[4] ^ keyCompany[3];
    keyAuth[1] = keyCompany[1];
    keyAuth[2] = (keyAuth[0] + keyCompany[2]) & 0xFF;
    keyAuth[3] = (keyAuth[1] + seedBytes[0]) & 0xFF;
    keyAuth[4] = keyAuth[2] ^ keyCompany[0];
    keyAuth[5] = seedBytes[5] ^ keyAuth[3];
    keyAuth[6] = seedBytes[7] & keyAuth[2];
    keyAuth[7] = seedBytes[3] ^ keyAuth[6];
    
    String authHex = _bytesToHexString(keyAuth);
    sendCommand('AT+BT_AUTH=$authHex\r\n');
  }
  
  // Decodificar dados de telemetria
  void _decodeTelemetryData(String data) {
    serialNumber.value = _agDecodeTelematics.decodeSerial(data);
    firmwareVersion.value = _agDecodeTelematics.decodeVerFW(data);
    latitude.value = _agDecodeTelematics.decodeLatitude(data);
    longitude.value = _agDecodeTelematics.decodeLongitude(data);
    speed.value = _agDecodeTelematics.decodeVelocidade(data);
    rpm.value = _agDecodeTelematics.decodeRPM(data);
    engineOn.value = _agDecodeTelematics.decodeEngON(data);
    ignitionOn.value = _agDecodeTelematics.decodeIngON(data);
    gpsStatus.value = _agDecodeTelematics.decodeStsGPS(data);
    
    // Confirmar recebimento
    sendCommand('AT+BT_DATA_OK\r\n');
  }
  
  // Funções GET
  Future<void> getSerial() async {
    await sendCommand('AT+BT_GET_SERIAL?\r\n');
  }
  
  Future<void> getVersion() async {
    await sendCommand('AT+BT_VERSION?\r\n');
  }
  
  Future<void> getReportCycle() async {
    await sendCommand('AT+BT_REPORT_CYCLE?\r\n');
  }
  
  Future<void> getHoursCount() async {
    await sendCommand('AT+BT_HOURS_COUNT?\r\n');
  }
  
  Future<void> getStatusWifi() async {
    await sendCommand('AT+BT_STATUS_WIFI?\r\n');
  }
  
  Future<void> getStatusConnection() async {
    await sendCommand('AT+BT_STATUS_CONN?\r\n');
  }
  
  // Enviar comando genérico
  Future<void> sendCommand(String command) async {
    await _bluetoothService.sendCommand(command);
  }
  
  // Utilitários
  List<int> _hexStringToBytes(String hex) {
    List<int> bytes = [];
    for (int i = 0; i < hex.length; i += 2) {
      String hexByte = hex.substring(i, i + 2);
      bytes.add(int.parse(hexByte, radix: 16));
    }
    return bytes;
  }
  
  String _bytesToHexString(List<int> bytes) {
    return bytes.map((b) => b.toRadixString(16).padLeft(2, '0')).join('');
  }
}
```

---

## 7. Interface de Usuário

### 7.1 Tela de Conexão

```dart
import 'package:flutter/material.dart';
import 'package:get/get.dart';

class ConnectionScreen extends StatelessWidget {
  final TelemetryController controller = Get.find();
  
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Conectar Dispositivo'),
      ),
      body: Column(
        children: [
          Obx(() => Text(
            controller.isConnected.value 
              ? 'Conectado' 
              : 'Desconectado',
            style: TextStyle(
              fontSize: 20,
              color: controller.isConnected.value 
                ? Colors.green 
                : Colors.red,
            ),
          )),
          ElevatedButton(
            onPressed: () => _scanAndConnect(),
            child: Text('Escanear Dispositivos'),
          ),
        ],
      ),
    );
  }
  
  Future<void> _scanAndConnect() async {
    // Implementar lógica de scan e conexão
  }
}
```

### 7.2 Tela de Telemetria

```dart
class TelemetryScreen extends StatelessWidget {
  final TelemetryController controller = Get.find();
  
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Telemetria'),
      ),
      body: SingleChildScrollView(
        padding: EdgeInsets.all(16),
        child: Column(
          children: [
            _buildDataCard('Número de Série', controller.serialNumber),
            _buildDataCard('Versão Firmware', controller.firmwareVersion),
            _buildDataCard('Latitude', controller.latitude),
            _buildDataCard('Longitude', controller.longitude),
            _buildDataCard('Velocidade', controller.speed),
            _buildDataCard('RPM', controller.rpm),
            _buildDataCard('Motor', controller.engineOn),
            _buildDataCard('Ignição', controller.ignitionOn),
            _buildDataCard('GPS', controller.gpsStatus),
            
            SizedBox(height: 20),
            
            // Botões GET
            Wrap(
              spacing: 10,
              runSpacing: 10,
              children: [
                ElevatedButton(
                  onPressed: () => controller.getSerial(),
                  child: Text('GET Serial'),
                ),
                ElevatedButton(
                  onPressed: () => controller.getVersion(),
                  child: Text('GET Versão'),
                ),
                ElevatedButton(
                  onPressed: () => controller.getReportCycle(),
                  child: Text('GET Ciclo Report'),
                ),
                ElevatedButton(
                  onPressed: () => controller.getHoursCount(),
                  child: Text('GET Horímetro'),
                ),
                ElevatedButton(
                  onPressed: () => controller.getStatusWifi(),
                  child: Text('GET Status WiFi'),
                ),
                ElevatedButton(
                  onPressed: () => controller.getStatusConnection(),
                  child: Text('GET Status Conexão'),
                ),
              ],
            ),
          ],
        ),
      ),
    );
  }
  
  Widget _buildDataCard(String label, RxString value) {
    return Card(
      child: ListTile(
        title: Text(label),
        subtitle: Obx(() => Text(value.value)),
      ),
    );
  }
}
```

---

## 8. Permissões Necessárias

### 8.1 Android (android/app/src/main/AndroidManifest.xml)

```xml
<manifest>
    <uses-permission android:name="android.permission.BLUETOOTH" />
    <uses-permission android:name="android.permission.BLUETOOTH_ADMIN" />
    <uses-permission android:name="android.permission.BLUETOOTH_SCAN" />
    <uses-permission android:name="android.permission.BLUETOOTH_CONNECT" />
    <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
    <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
    
    <application>
        ...
    </application>
</manifest>
```

### 8.2 iOS (ios/Runner/Info.plist)

```xml
<key>NSBluetoothAlwaysUsageDescription</key>
<string>Este app precisa de Bluetooth para conectar ao dispositivo telemático</string>
<key>NSBluetoothPeripheralUsageDescription</key>
<string>Este app precisa de Bluetooth para conectar ao dispositivo telemático</string>
<key>NSLocationWhenInUseUsageDescription</key>
<string>Este app precisa de localização para funcionalidades de GPS</string>
```

---

## 9. Fluxo de Uso da Aplicação

1. **Inicializar App**: Solicitar permissões necessárias
2. **Escanear**: Buscar dispositivos BLE disponíveis
3. **Conectar**: Conectar ao dispositivo Blue Telematics
4. **Autenticar**: Receber SEED, calcular e enviar AUTH
5. **Aguardar Confirmação**: `AT+BT_AUTH_OK`
6. **Executar GETs**: Enviar comandos GET conforme necessário
7. **Receber Dados**: Processar respostas e dados de telemetria
8. **Decodificar**: Usar classes de decodificação apropriadas
9. **Exibir**: Mostrar dados na interface

---

## 10. Mapeamento de Bytes dos Protocolos

### 10.1 Protocolo AgDecodeTelematics (Mensagens ALERT/REPORT)

| Bytes | Campo | Fórmula de Conversão | Observações |
|-------|-------|---------------------|-------------|
| 0-7 | Serial | Hex para Decimal, padding 8 | Número de série |
| 8 (bits 4-7) | Versão FW | (byte & 0xF0) >> 4 | |
| 8 (bits 0-3) | Versão Protocolo | byte & 0x0F | |
| 10 (bits 4-7) | Tipo Mensagem | (byte & 0xF0) >> 4 | 1=ALERT, 2=REPORT, 3=KEEP |
| 10 (bits 0-3) | Versão Carga | byte & 0x0F | |
| 12 (bits 6-7) | Tipo Carga | (byte & 0xC0) >> 6 | |
| 12 (bits 0-5) | ID Carga | byte & 0x3F | |
| 14 | ID Alerta | byte | Ver tabela de alertas |
| 16 | Reservado | - | |
| 18-21 | Qtd Msg Flash | Hex para Int | |
| 22 (bit 1) | Motor ON | (byte & 0x02) >> 1 | 0=OFF, 1=ON |
| 22 (bit 0) | Ignição ON | byte & 0x01 | 0=OFF, 1=ON |
| 22 (bit 2) | GPS Status | (byte & 0x04) >> 2 | 0=Inválido, 1=Válido |
| 24-31 | Timestamp | Unix Timestamp | Segundos desde epoch |
| 32-39 | Latitude | (int - 4294967296) / 10000000 | Graus decimais |
| 40-47 | Longitude | (int - 4294967296) / 10000000 | Graus decimais |
| 48 | Tipo FIX | byte | 0=NA, 1=SEM, 2=2D, 3=3D |
| 50-53 | HDOP | int * 0.01 | Precisão horizontal |
| 54-55 | Qtd Satélites | byte | |
| 56-59 | Bússola | int | Graus (0-360) |
| 60-61 | Tipo Conexão | byte | 0=Sem, 1=LTE, 2=WIFI |
| 62-63 | Potência Sinal | byte | RSSI em dBm |
| 64-67 | Bateria | int * 0.05 | Volts |
| 68-75 | Horímetro | int * 0.05 | Horas |
| 76-83 | Total Combustível | int * 0.5 | Litros |
| 84-91 | Odômetro | int * 0.125 | Quilômetros |
| 92-95 | Velocidade | int * 0.1 | Km/h |
| 96-99 | RPM | int * 0.125 | Rotações por minuto |
| 100-101 | Pedal Acelerador | int * 0.4 | Porcentagem |
| 102-103 | Torque Motor | int - 125 | Porcentagem |
| 104-105 | Carga Motor | int * 0.4 | Porcentagem |
| 106-107 | Pressão Turbo | int * 0.05 | PSI |
| 108-109 | Pressão Admissão | int * 0.05 | PSI |
| 110-111 | Pressão Óleo | int * 4 | KPA |
| 112-113 | Pressão Transmissão | int * 16 | KPA |
| 114-115 | Pressão Combustível | int * 4 | KPA |
| 116-119 | Temp. Óleo | int * 0.03125 | Celsius |
| 120-121 | Temp. Água | int - 40 | Celsius |
| 122-123 | Temp. Admissão | int - 40 | Celsius |
| 124-125 | Temp. Ambiente | int - 40 | Celsius |
| 126-129 | Temp. Transmissão | (int * 0.03125) - 273 | Celsius |
| 130-131 | Temp. Fluido Hidráulico | int - 40 | Celsius |
| 132-133 | Temp. Combustível | int - 40 | Celsius |
| 134-137 | Vazão Combustível | int * 0.05 | Litros/hora |
| 138-139 | Nível Combustível | int * 0.4 | Porcentagem |
| 140-141 | Nível Óleo Transmissão | int * 0.4 | Porcentagem |
| 142-143 | Nível Fluido Hidráulico | int * 0.4 | Porcentagem |
| 144-145 (bits 0-1) | DTC Status | byte & 0x03 | 0=OFF, 1=ON |
| 144-145 (bits 2-3) | Ventilador | (byte & 0x0C) >> 2 | 0=OFF, 1=ON |
| 144-145 (bits 4-5) | Esteira Elevador | (byte & 0x30) >> 4 | 0=OFF, 1=ON |
| 144-145 (bits 6-7) | Corte Base | (byte & 0xC0) >> 6 | 0=OFF, 1=ON |

### 10.2 Valor 0xFF e 0xFFFF

- **0xFF** (byte único) = "Não avaliado"
- **0xFFFF** (2 bytes) = "Não avaliado"
- **0xFFFFFFFF** (4 bytes) = "Não avaliado"

---

## 11. Exemplo Completo de Uso

```dart
void main() async {
  WidgetsFlutterBinding.ensureInitialized();
  
  // Inicializar controller
  Get.put(TelemetryController());
  
  runApp(MyApp());
}

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return GetMaterialApp(
      title: 'Blue Telematics',
      theme: ThemeData(
        primarySwatch: Colors.blue,
      ),
      home: HomeScreen(),
    );
  }
}

class HomeScreen extends StatelessWidget {
  final TelemetryController controller = Get.find();
  
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Blue Telematics'),
      ),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Obx(() => Text(
              controller.isConnected.value 
                ? 'Conectado e Autenticado' 
                : 'Não Conectado',
              style: TextStyle(fontSize: 24),
            )),
            SizedBox(height: 20),
            ElevatedButton(
              onPressed: () => Get.to(() => ConnectionScreen()),
              child: Text('Conectar'),
            ),
            SizedBox(height: 10),
            ElevatedButton(
              onPressed: () => Get.to(() => TelemetryScreen()),
              child: Text('Ver Telemetria'),
            ),
          ],
        ),
      ),
    );
  }
}
```

---

## 12. Considerações de Segurança

1. **Chave de Licença**: Armazenar a chave da empresa (keyCompany) de forma segura
2. **Validação CRC**: Sempre validar CRC antes de processar dados
3. **Timeout**: Implementar timeouts para comandos que não recebem resposta
4. **Reconexão Automática**: Implementar lógica de reconexão em caso de perda de conexão
5. **Tratamento de Erros**: Capturar e tratar todos os erros de comunicação

---

## 13. Testes Recomendados

1. **Teste de Conexão**: Verificar conexão BLE com diferentes dispositivos
2. **Teste de Autenticação**: Validar algoritmo de cálculo de AUTH
3. **Teste de Decodificação**: Validar cada método de decodificação com dados reais
4. **Teste de Performance**: Avaliar latência de comandos GET
5. **Teste de Reconexão**: Simular perda de conexão e reconexão
6. **Teste de Bateria**: Avaliar impacto no consumo de bateria

---

## 14. Próximos Passos

1. Implementar comandos SET (configuração)
2. Implementar atualização OTA de firmware
3. Adicionar persistência local de dados (SQLite)
4. Implementar sincronização com servidor
5. Adicionar gráficos e análises de dados
6. Implementar notificações de alertas
7. Adicionar suporte a múltiplos dispositivos
8. Implementar modo offline

---

## 15. Referências

- **Código Java Original**: AG-TELEMATICS-DECODER
- **Documentação**: BLUE TELEMATICS - WIFI e BLE - Protocolo e Comandos [V3.0.0].pdf
- **Flutter Blue Plus**: https://pub.dev/packages/flutter_blue_plus
- **GetX**: https://pub.dev/packages/get

---

## 16. Contato e Suporte

Para dúvidas sobre implementação ou protocolo, consulte:
- Documentação técnica do Blue Telematics
- Código fonte original em Java
- BLUECHIP Electronics

---

**Versão do Documento**: 1.0  
**Data**: 29 de Dezembro de 2025  
**Autor**: Baseado no código AG-TELEMATICS-DECODER
