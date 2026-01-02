import 'package:shared_preferences/shared_preferences.dart';

class StorageService {
  static const String _keyLastDeviceName = 'last_device_name';
  static const String _keyLastDeviceAddress = 'last_device_address';
  static const String _keyUserCode = 'user_code';

  // Salvar último dispositivo conectado
  Future<void> saveLastDevice(String name, String address) async {
    final prefs = await SharedPreferences.getInstance();
    await prefs.setString(_keyLastDeviceName, name);
    await prefs.setString(_keyLastDeviceAddress, address);
  }

  // Carregar último dispositivo conectado
  Future<Map<String, String?>> getLastDevice() async {
    final prefs = await SharedPreferences.getInstance();
    return {
      'name': prefs.getString(_keyLastDeviceName),
      'address': prefs.getString(_keyLastDeviceAddress),
    };
  }

  // Salvar código de usuário
  Future<void> saveUserCode(String code) async {
    final prefs = await SharedPreferences.getInstance();
    await prefs.setString(_keyUserCode, code);
  }

  // Carregar código de usuário
  Future<String?> getUserCode() async {
    final prefs = await SharedPreferences.getInstance();
    return prefs.getString(_keyUserCode);
  }

  // Limpar dados armazenados
  Future<void> clearAll() async {
    final prefs = await SharedPreferences.getInstance();
    await prefs.clear();
  }
}
