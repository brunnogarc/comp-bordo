import 'package:flutter_blue_plus/flutter_blue_plus.dart';
import 'package:flutter_bluetooth_serial/flutter_bluetooth_serial.dart' as classic;

enum DeviceType { ble, classic }

class UnifiedDevice {
  final String name;
  final String address;
  final int rssi;
  final DeviceType type;
  final BluetoothDevice? bleDevice;
  final classic.BluetoothDevice? classicDevice;
  final bool isBonded;

  UnifiedDevice({
    required this.name,
    required this.address,
    required this.rssi,
    required this.type,
    this.bleDevice,
    this.classicDevice,
    this.isBonded = false,
  });

  // Criar a partir de dispositivo BLE
  factory UnifiedDevice.fromBLE(ScanResult result) {
    String name = 'Dispositivo BLE';
    
    if (result.device.advName.isNotEmpty) {
      name = result.device.advName;
    } else if (result.device.platformName.isNotEmpty) {
      name = result.device.platformName;
    } else if (result.advertisementData.advName.isNotEmpty) {
      name = result.advertisementData.advName;
    } else if (result.advertisementData.localName.isNotEmpty) {
      name = result.advertisementData.localName;
    }

    return UnifiedDevice(
      name: name,
      address: result.device.remoteId.toString(),
      rssi: result.rssi,
      type: DeviceType.ble,
      bleDevice: result.device,
      isBonded: result.device.bondState == BluetoothBondState.bonded,
    );
  }

  // Criar a partir de dispositivo Bluetooth Classic
  factory UnifiedDevice.fromClassic(classic.BluetoothDevice device) {
    return UnifiedDevice(
      name: device.name ?? 'Dispositivo Classic',
      address: device.address,
      rssi: 0, // Bluetooth Classic não fornece RSSI no scan
      type: DeviceType.classic,
      classicDevice: device,
      isBonded: device.isBonded,
    );
  }

  bool get hasName => name != 'Dispositivo BLE' && name != 'Dispositivo Classic';
}
