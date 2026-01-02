import 'package:flutter/foundation.dart';
import 'package:intl/intl.dart';

class LoggerService {
  static final LoggerService _instance = LoggerService._internal();
  factory LoggerService() => _instance;
  LoggerService._internal();

  final List<String> _logs = [];
  final int maxLogs = 500; // Máximo de logs armazenados
  
  // Níveis de log
  static const String _info = '📘 INFO';
  static const String _warning = '⚠️ WARNING';
  static const String _error = '❌ ERROR';
  static const String _success = '✅ SUCCESS';
  static const String _bluetooth = '📡 BLUETOOTH';
  static const String _data = '📊 DATA';
  static const String _debug = '🐛 DEBUG';

  String _getTimestamp() {
    return DateFormat('HH:mm:ss.SSS').format(DateTime.now());
  }

  void _log(String level, String tag, String message, [dynamic data]) {
    if (!kDebugMode) return; // Só registra em modo debug

    final timestamp = _getTimestamp();
    final logMessage = '[$timestamp] $level [$tag] $message';
    
    // Adicionar aos logs internos
    _logs.add(logMessage);
    if (_logs.length > maxLogs) {
      _logs.removeAt(0);
    }

    // Imprimir no console
    if (kDebugMode) {
      print(logMessage);
      if (data != null) {
        print('  └─ Data: $data');
      }
    }
  }

  // Métodos públicos
  void info(String tag, String message, [dynamic data]) {
    _log(_info, tag, message, data);
  }

  void warning(String tag, String message, [dynamic data]) {
    _log(_warning, tag, message, data);
  }

  void error(String tag, String message, [dynamic data]) {
    _log(_error, tag, message, data);
  }

  void success(String tag, String message, [dynamic data]) {
    _log(_success, tag, message, data);
  }

  void bluetooth(String tag, String message, [dynamic data]) {
    _log(_bluetooth, tag, message, data);
  }

  void data(String tag, String message, [dynamic data]) {
    _log(_data, tag, message, data);
  }

  void debug(String tag, String message, [dynamic data]) {
    _log(_debug, tag, message, data);
  }

  // Obter todos os logs
  List<String> getLogs() {
    return List.from(_logs);
  }

  // Limpar logs
  void clear() {
    _logs.clear();
    info('Logger', 'Logs limpos');
  }

  // Exportar logs como string
  String exportLogs() {
    return _logs.join('\n');
  }
}
