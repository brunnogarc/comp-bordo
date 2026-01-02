class TelemetryData {
  String velocidade;
  String bateria;
  String rpm;
  String odometro;
  String horimetro;
  String nivelCombustivel;
  String torqueMotor;
  String temperaturaMotor;
  String latitude;
  String longitude;
  String bussola;
  String ignicao;
  DateTime? timestamp;

  TelemetryData({
    this.velocidade = 'N/A',
    this.bateria = 'N/A',
    this.rpm = 'N/A',
    this.odometro = 'N/A',
    this.horimetro = 'N/A',
    this.nivelCombustivel = 'N/A',
    this.torqueMotor = 'N/A',
    this.temperaturaMotor = 'N/A',
    this.latitude = 'N/A',
    this.longitude = 'N/A',
    this.bussola = 'N/A',
    this.ignicao = 'N/A',
    this.timestamp,
  });

  Map<String, dynamic> toJson() {
    return {
      'velocidade': velocidade,
      'bateria': bateria,
      'rpm': rpm,
      'odometro': odometro,
      'horimetro': horimetro,
      'nivelCombustivel': nivelCombustivel,
      'torqueMotor': torqueMotor,
      'temperaturaMotor': temperaturaMotor,
      'latitude': latitude,
      'longitude': longitude,
      'bussola': bussola,
      'ignicao': ignicao,
      'timestamp': timestamp?.toIso8601String(),
    };
  }
}
