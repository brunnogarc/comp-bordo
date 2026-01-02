import 'package:flutter/material.dart';
import 'package:get/get.dart';
import 'package:flutter_blue_plus/flutter_blue_plus.dart';
import '../controllers/telemetry_controller.dart';
import '../models/unified_device.dart';
import 'package:permission_handler/permission_handler.dart';

class ScanScreen extends StatefulWidget {
  const ScanScreen({super.key});

  @override
  State<ScanScreen> createState() => _ScanScreenState();
}

class _ScanScreenState extends State<ScanScreen> {
  bool _showOnlyBonded = false;

  @override
  Widget build(BuildContext context) {
    final TelemetryController controller = Get.find();

    return Scaffold(
      appBar: AppBar(
        title: const Text('Buscar Dispositivos'),
        centerTitle: true,
      ),
      body: Column(
        children: [
          Padding(
            padding: const EdgeInsets.all(16),
            child: Column(
              children: [
                Obx(() {
                  final bondedDevices = controller.unifiedDevices.where((d) => d.isBonded).length;
                  final unbondedDevices = controller.unifiedDevices.where((d) => !d.isBonded).length;
                  
                  return Row(
                    mainAxisAlignment: MainAxisAlignment.spaceBetween,
                    children: [
                      Text(
                        controller.isScanning.value 
                          ? 'Escaneando...' 
                          : 'Pareados: $bondedDevices | Outros: $unbondedDevices',
                        style: const TextStyle(
                          fontSize: 16,
                          fontWeight: FontWeight.bold,
                        ),
                      ),
                      if (!controller.isScanning.value && unbondedDevices > 0)
                        InkWell(
                          onTap: () {
                            setState(() {
                              _showOnlyBonded = !_showOnlyBonded;
                            });
                          },
                          borderRadius: BorderRadius.circular(8),
                          child: Container(
                            padding: const EdgeInsets.symmetric(horizontal: 12, vertical: 6),
                            decoration: BoxDecoration(
                              border: Border.all(color: Colors.grey.shade400),
                              borderRadius: BorderRadius.circular(8),
                              color: _showOnlyBonded ? Colors.grey.shade100 : Colors.transparent,
                            ),
                            child: Row(
                              mainAxisSize: MainAxisSize.min,
                              children: [
                                Icon(
                                  _showOnlyBonded ? Icons.visibility_off : Icons.visibility,
                                  size: 16,
                                  color: Colors.grey.shade700,
                                ),
                                const SizedBox(width: 6),
                                SizedBox(
                                  width: 50,
                                  child: Text(
                                    _showOnlyBonded ? 'Todos' : 'Ocultar',
                                    style: TextStyle(
                                      fontSize: 11,
                                      color: Colors.grey.shade700,
                                    ),
                                    textAlign: TextAlign.center,
                                  ),
                                ),
                              ],
                            ),
                          ),
                        ),
                    ],
                  );
                }),
                const SizedBox(height: 10),
                SizedBox(
                  width: double.infinity,
                  child: ElevatedButton.icon(
                    onPressed: () async {
                      await _checkPermissions();
                      await controller.scanDevices();
                    },
                    icon: const Icon(Icons.search),
                    label: const Text('Procurar Dispositivos'),
                    style: ElevatedButton.styleFrom(
                      padding: const EdgeInsets.symmetric(vertical: 15),
                    ),
                  ),
                ),
              ],
            ),
          ),
          const Divider(height: 1),
          Expanded(
            child: Obx(() {
              if (controller.isScanning.value) {
                return const Center(
                  child: Column(
                    mainAxisAlignment: MainAxisAlignment.center,
                    children: [
                      CircularProgressIndicator(),
                      SizedBox(height: 20),
                      Text('Procurando dispositivos...'),
                    ],
                  ),
                );
              }

              if (controller.unifiedDevices.isEmpty) {
                return const Center(
                  child: Column(
                    mainAxisAlignment: MainAxisAlignment.center,
                    children: [
                      Icon(
                        Icons.bluetooth_searching,
                        size: 80,
                        color: Colors.grey,
                      ),
                      SizedBox(height: 20),
                      Text(
                        'Nenhum dispositivo encontrado',
                        style: TextStyle(fontSize: 16, color: Colors.grey),
                      ),
                      SizedBox(height: 10),
                      Text(
                        'Toque em "Escanear" para buscar',
                        style: TextStyle(fontSize: 14, color: Colors.grey),
                      ),
                    ],
                  ),
                );
              }

              // Separar dispositivos pareados e não pareados
              final bondedDevices = controller.unifiedDevices.where((d) => d.isBonded).toList();
              final unbondedDevices = controller.unifiedDevices.where((d) => !d.isBonded).toList();
              
              return ListView(
                children: [
                  // Card informativo
                  Container(
                    margin: const EdgeInsets.all(16),
                    child: Card(
                      color: Colors.blue.shade50,
                      child: Padding(
                        padding: const EdgeInsets.all(12),
                        child: Row(
                          children: [
                            Icon(
                              Icons.info_outline,
                              color: Colors.blue.shade700,
                              size: 24,
                            ),
                            const SizedBox(width: 12),
                            Expanded(
                              child: Text(
                                'Primeiro, pareie o dispositivo de rastreamento nas configurações Bluetooth do Android. Dispositivos pareados aparecerão como "Pareado".',
                                style: TextStyle(
                                  fontSize: 13,
                                  color: Colors.blue.shade900,
                                ),
                              ),
                            ),
                          ],
                        ),
                      ),
                    ),
                  ),
                  
                  // Seção: Dispositivos Pareados
                  if (bondedDevices.isNotEmpty) ...[
                    Padding(
                      padding: const EdgeInsets.fromLTRB(16, 8, 16, 8),
                      child: Row(
                        children: [
                          Icon(Icons.bluetooth_connected, size: 20, color: Colors.green.shade700),
                          const SizedBox(width: 8),
                          Text(
                            'Dispositivos Pareados',
                            style: TextStyle(
                              fontSize: 14,
                              fontWeight: FontWeight.bold,
                              color: Colors.grey.shade700,
                            ),
                          ),
                          const SizedBox(width: 8),
                          Expanded(
                            child: Container(
                              height: 1,
                              color: Colors.grey.shade300,
                            ),
                          ),
                        ],
                      ),
                    ),
                    ...bondedDevices.map((device) => _buildUnifiedDeviceCard(device, controller)),
                  ],
                  
                  // Seção: Dispositivos Não Pareados (só mostra se filtro estiver desligado)
                  if (!_showOnlyBonded && unbondedDevices.isNotEmpty) ...[
                    Padding(
                      padding: const EdgeInsets.fromLTRB(16, 16, 16, 8),
                      child: Row(
                        children: [
                          Icon(Icons.bluetooth_searching, size: 20, color: Colors.blue.shade700),
                          const SizedBox(width: 8),
                          Text(
                            'Outros Dispositivos',
                            style: TextStyle(
                              fontSize: 14,
                              fontWeight: FontWeight.bold,
                              color: Colors.grey.shade700,
                            ),
                          ),
                          const SizedBox(width: 8),
                          Expanded(
                            child: Container(
                              height: 1,
                              color: Colors.grey.shade300,
                            ),
                          ),
                        ],
                      ),
                    ),
                    ...unbondedDevices.map((device) => _buildUnifiedDeviceCard(device, controller)),
                  ],
                ],
              );
            }),
          ),
        ],
      ),
    );
  }

  Widget _buildUnifiedDeviceCard(UnifiedDevice device, TelemetryController controller) {
    // Ícone e cor: Verde para pareados, Azul para não pareados
    IconData icon = device.type == DeviceType.ble ? Icons.bluetooth : Icons.bluetooth_connected;
    Color iconColor = device.isBonded ? Colors.green.shade700 : Colors.blue.shade700;
    String typeLabel = device.isBonded ? 'Pareado' : 'Parear';

    return Card(
      margin: const EdgeInsets.symmetric(horizontal: 16, vertical: 8),
      child: ListTile(
        leading: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Icon(icon, color: iconColor, size: 30),
            Text(
              typeLabel,
              style: TextStyle(fontSize: 10, color: iconColor, fontWeight: FontWeight.bold),
            ),
          ],
        ),
        title: Row(
          children: [
            Expanded(
              child: Text(
                device.name,
                style: const TextStyle(fontWeight: FontWeight.bold),
                maxLines: 1,
                overflow: TextOverflow.ellipsis,
              ),
            ),
          ],
        ),
        subtitle: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            const SizedBox(height: 4),
            Text(
              device.address,
              style: const TextStyle(fontSize: 12, color: Colors.grey),
            ),
            if (device.type == DeviceType.ble && device.rssi != 0) ...[
              const SizedBox(height: 4),
              Row(
                children: [
                  Icon(
                    _getSignalIcon(device.rssi),
                    size: 16,
                    color: _getSignalColor(device.rssi),
                  ),
                  const SizedBox(width: 5),
                  Text(
                    '${device.rssi} dBm',
                    style: TextStyle(
                      fontSize: 12,
                      color: _getSignalColor(device.rssi),
                    ),
                  ),
                ],
              ),
            ],
          ],
        ),
        trailing: ElevatedButton(
          onPressed: () => controller.connectToUnifiedDevice(device),
          style: ElevatedButton.styleFrom(
            padding: const EdgeInsets.symmetric(horizontal: 12, vertical: 8),
          ),
          child: const Text('Conectar', style: TextStyle(fontSize: 12)),
        ),
      ),
    );
  }

  IconData _getSignalIcon(int rssi) {
    if (rssi > -60) return Icons.signal_cellular_4_bar;
    if (rssi > -70) return Icons.signal_cellular_alt_2_bar;
    return Icons.signal_cellular_alt_1_bar;
  }

  Color _getSignalColor(int rssi) {
    if (rssi > -60) return Colors.green;
    if (rssi > -70) return Colors.orange;
    return Colors.red;
  }

  Future<void> _checkPermissions() async {
    // Solicitar permissões necessárias
    Map<Permission, PermissionStatus> statuses = await [
      Permission.bluetooth,
      Permission.bluetoothScan,
      Permission.bluetoothConnect,
      Permission.location,
    ].request();

    bool allGranted = statuses.values.every((status) => status.isGranted);

    if (!allGranted) {
      Get.snackbar(
        'Permissões Necessárias',
        'Por favor, conceda as permissões de Bluetooth e Localização',
        snackPosition: SnackPosition.BOTTOM,
        duration: const Duration(seconds: 3),
      );
    }
  }
}
