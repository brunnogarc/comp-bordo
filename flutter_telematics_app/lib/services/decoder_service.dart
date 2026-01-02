import 'logger_service.dart';

class DecoderService {
  final _logger = LoggerService();
  
  // Decodifica velocidade (bytes 92-95 no AgDecodeTelematics)
  String decodeVelocidade(String data, int startByte) {
    if (data.length < startByte + 4) {
      _logger.warning('DECODER', 'Dados insuficientes para velocidade');
      return "N/A";
    }
    
    String result = data.substring(startByte, startByte + 4);
    if (result == "FFFF") {
      print('  🚗 Velocidade: hex=$result → N/A');
      return "0.0";
    }
    int hexValue = int.parse(result, radix: 16);
    double tmp = hexValue * 0.1;
    print('  🚗 Velocidade: hex=$result ($hexValue) → ${tmp.toStringAsFixed(1)} km/h');
    return tmp.toStringAsFixed(1);
  }

  // Decodifica bateria (bytes 64-67)
  String decodeBateria(String data, int startByte) {
    if (data.length < startByte + 4) return "N/A";
    
    String result = data.substring(startByte, startByte + 4);
    if (result == "FFFF") {
      print('  🔋 Bateria: hex=$result → N/A');
      return "0.0";
    }
    int hexValue = int.parse(result, radix: 16);
    double tmp = hexValue * 0.05;
    print('  🔋 Bateria: hex=$result ($hexValue) → ${tmp.toStringAsFixed(2)} V');
    return tmp.toStringAsFixed(2);
  }

  // Decodifica RPM (bytes 96-99)
  String decodeRPM(String data, int startByte) {
    if (data.length < startByte + 4) return "N/A";
    
    String result = data.substring(startByte, startByte + 4);
    if (result == "FFFF") {
      print('  ⚙️ RPM: hex=$result → N/A');
      return "0.0";
    }
    int hexValue = int.parse(result, radix: 16);
    double tmp = hexValue * 0.125;
    print('  ⚙️ RPM: hex=$result ($hexValue) → ${tmp.toStringAsFixed(1)} rpm');
    return tmp.toStringAsFixed(1);
  }

  // Decodifica odômetro (bytes 84-91)
  String decodeOdometro(String data, int startByte) {
    if (data.length < startByte + 8) return "N/A";
    
    String result = data.substring(startByte, startByte + 8);
    if (result == "FFFFFFFF") {
      print('  📏 Odômetro: hex=$result → N/A');
      return "0.0";
    }
    int hexValue = int.parse(result, radix: 16);
    double tmp = hexValue * 0.125;
    print('  📏 Odômetro: hex=$result ($hexValue) → ${tmp.toStringAsFixed(2)} km');
    return tmp.toStringAsFixed(2);
  }

  // Decodifica horímetro (bytes 68-75)
  String decodeHorimetro(String data, int startByte) {
    if (data.length < startByte + 8) return "N/A";
    
    String result = data.substring(startByte, startByte + 8);
    
    // Log detalhado para debug
    print('  ⏱️ HORIMETRO DEBUG:');
    print('     Posição no hex: $startByte (byte 68)');
    print('     8 caracteres lidos: $result');
    
    if (result == "FFFFFFFF") {
      print('     Valor: N/A (todos FF)');
      return "N/A";
    }
    
    int hexValue = int.parse(result, radix: 16);
    double tmp = hexValue * 0.05;
    print('     Decimal: $hexValue');
    print('     Cálculo: $hexValue × 0.05 = $tmp h');
    
    return tmp.toStringAsFixed(2);
  }

  // Decodifica nível de combustível (bytes 138-139)
  String decodeNivelCombustivel(String data, int startByte) {
    if (data.length < startByte + 2) return "N/A";
    
    String result = data.substring(startByte, startByte + 2);
    if (result == "FF") {
      print('  ⛽ Nível Combustível: hex=$result → N/A');
      return "N/A";
    }
    int hexValue = int.parse(result, radix: 16);
    double tmp = hexValue * 0.4;
    print('  ⛽ Nível Combustível: hex=$result ($hexValue) → ${tmp.toStringAsFixed(1)}%');
    return tmp.toStringAsFixed(1);
  }

  // Decodifica torque do motor (bytes 102-103)
  String decodeTorqueMotor(String data, int startByte) {
    if (data.length < startByte + 2) return "N/A";
    
    String result = data.substring(startByte, startByte + 2);
    if (result == "FF") {
      print('  ⚙️ Torque Motor: hex=$result → N/A');
      return "N/A";
    }
    int hexValue = int.parse(result, radix: 16);
    double tmp = hexValue - 125.0;
    print('  ⚙️ Torque Motor: hex=$result ($hexValue) → ${tmp.toStringAsFixed(1)}%');
    return tmp.toStringAsFixed(1);
  }

  // Decodifica temperatura da água (bytes 120-121)
  String decodeTemperaturaMotor(String data, int startByte) {
    if (data.length < startByte + 2) return "N/A";
    
    String result = data.substring(startByte, startByte + 2);
    if (result == "FF") {
      print('  🌡️ Temperatura Motor: hex=$result → N/A');
      return "N/A";
    }
    int hexValue = int.parse(result, radix: 16);
    double tmp = hexValue - 40.0;
    print('  🌡️ Temperatura Motor: hex=$result ($hexValue) → ${tmp.toStringAsFixed(1)}°C');
    return tmp.toStringAsFixed(1);
  }

  // Decodifica latitude (bytes 32-39)
  String decodeLatitude(String data, int startByte) {
    if (data.length < startByte + 8) return "N/A";
    
    String result = data.substring(startByte, startByte + 8);
    if (result == "FFFFFFFF") {
      print('  🌍 Latitude: hex=$result → N/A');
      return "N/A";
    }
    
    // Exatamente como Java: SEMPRE subtrai 4294967296
    int recLat = int.parse(result, radix: 16);
    double latFinal = (recLat - 4294967296) / 10000000.0;
    
    // Validar range de latitude (-90 a +90)
    if (latFinal < -90.0 || latFinal > 90.0) {
      print('  🌍 Latitude: hex=$result → ${latFinal.toStringAsFixed(7)}° [INVÁLIDO]');
      return "Aguardando GPS";
    }
    
    print('  🌍 Latitude: hex=$result → ${latFinal.toStringAsFixed(7)}°');
    return latFinal.toStringAsFixed(7);
  }

  // Decodifica longitude (bytes 40-47)
  String decodeLongitude(String data, int startByte) {
    if (data.length < startByte + 8) return "N/A";
    
    String result = data.substring(startByte, startByte + 8);
    if (result == "FFFFFFFF") {
      print('  🌎 Longitude: hex=$result → N/A');
      return "N/A";
    }
    
    // Exatamente como Java: SEMPRE subtrai 4294967296
    int recLon = int.parse(result, radix: 16);
    double lonFinal = (recLon - 4294967296) / 10000000.0;
    
    // Validar range de longitude (-180 a +180)
    if (lonFinal < -180.0 || lonFinal > 180.0) {
      print('  🌎 Longitude: hex=$result → ${lonFinal.toStringAsFixed(7)}° [INVÁLIDO]');
      return "Aguardando GPS";
    }
    
    print('  🌎 Longitude: hex=$result → ${lonFinal.toStringAsFixed(7)}°');
    return lonFinal.toStringAsFixed(7);
  }

  // Decodifica bússola (bytes 56-59) e converte para direção cardinal
  String decodeBussola(String data, int startByte) {
    if (data.length < startByte + 4) return "N/A";
    
    String result = data.substring(startByte, startByte + 4);
    if (result == "FFFF") {
      print('  🧭 Bússola: hex=$result → N/A');
      return "N/A";
    }
    int hexValue = int.parse(result, radix: 16);
    double graus = hexValue.toDouble();
    String direcao = _grausParaDirecao(graus);
    print('  🧭 Bússola: hex=$result ($hexValue) → ${graus.toStringAsFixed(1)}° ($direcao)');
    return "${graus.toStringAsFixed(1)}° ($direcao)";
  }
  
  // Converte graus em direção cardinal
  String _grausParaDirecao(double graus) {
    if (graus >= 337.5 || graus < 22.5) return "N";
    if (graus >= 22.5 && graus < 67.5) return "NE";
    if (graus >= 67.5 && graus < 112.5) return "E";
    if (graus >= 112.5 && graus < 157.5) return "SE";
    if (graus >= 157.5 && graus < 202.5) return "S";
    if (graus >= 202.5 && graus < 247.5) return "SW";
    if (graus >= 247.5 && graus < 292.5) return "W";
    if (graus >= 292.5 && graus < 337.5) return "NW";
    return "N";
  }

  // Decodifica status da ignição (byte 11, posição 22, bit 0)
  String decodeIgnicao(String data, int startByte) {
    if (data.length < startByte + 2) return "Não avaliado";
    
    String result = data.substring(startByte, startByte + 2);
    int byte1 = int.parse(result, radix: 16);
    int tmp2 = byte1 & 0x01;
    String status = tmp2 == 1 ? "Ligada" : "Desligada";
    print('  🔴 Ignição: hex=$result (byte=$byte1, bit0=$tmp2) → $status');
    return status;
  }

  // Decodifica byte de status (byte 7: bit0=pós-chave, bit1=motor, bit2=gps)
  Map<String, bool> decodeStatusFlags(String data) {
    if (data.length < 16) return {};
    
    String statusByte = data.substring(14, 16); // Byte 7 na posição 14-15
    int status = int.parse(statusByte, radix: 16);
    
    return {
      'posChave': (status & 0x01) != 0,
      'motor': (status & 0x02) != 0,
      'gpsValido': (status & 0x04) != 0,
    };
  }

  // Decodifica dados completos do AT+BT_DATA=
  Map<String, String> decodeFullTelemetry(String hexData) {
    Map<String, String> result = {};
    
    try {
      // Remove espaços e converte para maiúsculas
      hexData = hexData.replaceAll(' ', '').toUpperCase();
      
      // Decodificar flags de status (byte 7)
      Map<String, bool> statusFlags = decodeStatusFlags(hexData);
      print('  🚩 Status: Pós-chave=${statusFlags['posChave']}, Motor=${statusFlags['motor']}, GPS=${statusFlags['gpsValido']}');
      
      // Posições conforme AgDecodeTelematics.java (índices da string, não bytes × 2)
      result['velocidade'] = decodeVelocidade(hexData, 92); // substring(92, 96)
      result['bateria'] = decodeBateria(hexData, 64); // substring(64, 68)
      result['rpm'] = decodeRPM(hexData, 96); // substring(96, 100)
      result['odometro'] = decodeOdometro(hexData, 84); // substring(84, 92)
      result['horimetro'] = decodeHorimetro(hexData, 68); // substring(68, 76)
      result['nivelCombustivel'] = decodeNivelCombustivel(hexData, 138); // substring(138, 140)
      result['torqueMotor'] = decodeTorqueMotor(hexData, 102); // substring(102, 104)
      result['temperaturaMotor'] = decodeTemperaturaMotor(hexData, 120); // substring(120, 122)
      result['latitude'] = decodeLatitude(hexData, 32); // substring(32, 40)
      result['longitude'] = decodeLongitude(hexData, 40); // substring(40, 48)
      result['bussola'] = decodeBussola(hexData, 56); // substring(56, 60)
      result['ignicao'] = decodeIgnicao(hexData, 22); // Byte 11 (posição 22)
    } catch (e) {
      print('Erro ao decodificar: $e');
    }
    
    return result;
  }

  // Converte hex string para bytes
  List<int> hexStringToBytes(String hex) {
    List<int> bytes = [];
    for (int i = 0; i < hex.length; i += 2) {
      String hexByte = hex.substring(i, i + 2);
      bytes.add(int.parse(hexByte, radix: 16));
    }
    return bytes;
  }

  // Converte bytes para hex string
  String bytesToHexString(List<int> bytes) {
    return bytes.map((b) => b.toRadixString(16).padLeft(2, '0')).join('').toUpperCase();
  }
}
