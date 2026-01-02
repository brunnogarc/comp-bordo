import 'package:flutter/material.dart';
import 'package:get/get.dart';
import '../controllers/telemetry_controller.dart';
import 'scan_screen.dart';
import 'initial_screen.dart';

class HomeScreen extends StatelessWidget {
  const HomeScreen({super.key});

  @override
  Widget build(BuildContext context) {
    final TelemetryController controller = Get.find();

    return Scaffold(
      appBar: AppBar(
        title: const Text('Blue Telematics'),
        centerTitle: true,
        leading: Obx(() => !controller.isConnected.value
            ? IconButton(
                icon: const Icon(Icons.arrow_back),
                onPressed: () => Get.off(() => const InitialScreen()),
              )
            : const SizedBox.shrink()),
        actions: [
          Obx(() => IconButton(
            icon: Icon(
              controller.isConnected.value 
                ? Icons.bluetooth_connected 
                : Icons.bluetooth_disabled,
              color: Colors.red,
            ),
            onPressed: () {
              if (controller.isConnected.value) {
                _showDisconnectDialog(context, controller);
              } else {
                Get.to(() => const ScanScreen());
              }
            },
          )),
        ],
      ),
      body: Obx(() {
        if (!controller.isConnected.value) {
          return _buildDisconnectedView(controller);
        }
        return _buildTelemetryView(controller);
      }),
    );
  }

  Widget _buildDisconnectedView(TelemetryController controller) {
    return Center(
      child: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
          const Icon(
            Icons.bluetooth_disabled,
            size: 80,
            color: Colors.grey,
          ),
          const SizedBox(height: 20),
          const Text(
            'Dispositivo não conectado',
            style: TextStyle(fontSize: 20, fontWeight: FontWeight.bold),
          ),
          const SizedBox(height: 10),
          const Text(
            'Toque no ícone Bluetooth para conectar',
            style: TextStyle(color: Colors.grey),
          ),
          const SizedBox(height: 30),
          ElevatedButton.icon(
            onPressed: () => Get.to(() => const ScanScreen()),
            icon: const Icon(Icons.search),
            label: const Text('Buscar Dispositivos'),
            style: ElevatedButton.styleFrom(
              padding: const EdgeInsets.symmetric(horizontal: 30, vertical: 15),
            ),
          ),
          const SizedBox(height: 15),
          OutlinedButton.icon(
            onPressed: () => Get.off(() => const InitialScreen()),
            icon: const Icon(Icons.home),
            label: const Text('Voltar ao Menu Inicial'),
            style: OutlinedButton.styleFrom(
              padding: const EdgeInsets.symmetric(horizontal: 30, vertical: 15),
            ),
          ),
        ],
      ),
    );
  }

  Widget _buildTelemetryView(TelemetryController controller) {
    return RefreshIndicator(
      onRefresh: () async {
        await controller.requestHours();
      },
      child: SingleChildScrollView(
        physics: const AlwaysScrollableScrollPhysics(),
        padding: const EdgeInsets.all(8),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            // Status Card
            Card(
              color: Colors.blue.shade50,
              child: Padding(
                padding: const EdgeInsets.all(10),
                child: Row(
                  children: [
                    const Icon(Icons.info_outline, color: Colors.blue),
                    const SizedBox(width: 10),
                    Expanded(
                      child: Column(
                        crossAxisAlignment: CrossAxisAlignment.start,
                        children: [
                          Text(
                            'Conectado a: ${controller.deviceName.value}',
                            style: const TextStyle(fontWeight: FontWeight.bold),
                          ),
                          Obx(() => Text(
                            'Última atualização: ${controller.telemetryData.value.timestamp != null ? _formatDateTime(controller.telemetryData.value.timestamp!) : "N/A"}',
                            style: const TextStyle(fontSize: 12, color: Colors.grey),
                          )),
                        ],
                      ),
                    ),
                  ],
                ),
              ),
            ),
            const SizedBox(height: 8),

            // Velocidade e RPM
            Row(
              children: [
                Expanded(
                  child: _buildDataCard(
                    'Velocidade',
                    controller.telemetryData.value.velocidade,
                    'km/h',
                    Icons.speed,
                    Colors.blue,
                  ),
                ),
                const SizedBox(width: 6),
                Expanded(
                  child: _buildDataCard(
                    'RPM',
                    controller.telemetryData.value.rpm,
                    'rpm',
                    Icons.rotate_right,
                    Colors.orange,
                  ),
                ),
              ],
            ),
            const SizedBox(height: 6),

            // Bateria e Ignição
            Row(
              children: [
                Expanded(
                  child: _buildDataCard(
                    'Bateria',
                    controller.telemetryData.value.bateria,
                    'V',
                    Icons.battery_charging_full,
                    Colors.green,
                  ),
                ),
                const SizedBox(width: 6),
                Expanded(
                  child: _buildStatusCard(
                    'Ignição',
                    controller.telemetryData.value.ignicao,
                    Icons.power_settings_new,
                    controller.telemetryData.value.ignicao == 'Ligada' 
                      ? Colors.green 
                      : Colors.red,
                  ),
                ),
              ],
            ),
            const SizedBox(height: 6),

            // Odômetro e Horímetro
            Row(
              children: [
                Expanded(
                  child: _buildDataCard(
                    'Odômetro',
                    controller.telemetryData.value.odometro,
                    'km',
                    Icons.route,
                    Colors.purple,
                  ),
                ),
                const SizedBox(width: 6),
                Expanded(
                  child: _buildDataCard(
                    'Horímetro',
                    controller.telemetryData.value.horimetro,
                    'h',
                    Icons.access_time,
                    Colors.indigo,
                  ),
                ),
              ],
            ),
            const SizedBox(height: 6),

            // Combustível e Torque
            Row(
              children: [
                Expanded(
                  child: _buildDataCard(
                    'Combustível',
                    controller.telemetryData.value.nivelCombustivel,
                    '%',
                    Icons.local_gas_station,
                    Colors.amber,
                  ),
                ),
                const SizedBox(width: 6),
                Expanded(
                  child: _buildDataCard(
                    'Torque',
                    controller.telemetryData.value.torqueMotor,
                    '%',
                    Icons.settings,
                    Colors.teal,
                  ),
                ),
              ],
            ),
            const SizedBox(height: 6),

            // Temperatura e Bússola
            Row(
              children: [
                Expanded(
                  child: _buildDataCard(
                    'Temperatura',
                    controller.telemetryData.value.temperaturaMotor,
                    '°C',
                    Icons.thermostat,
                    Colors.red,
                  ),
                ),
                const SizedBox(width: 6),
                Expanded(
                  child: _buildDataCard(
                    'Bússola',
                    controller.telemetryData.value.bussola,
                    '°',
                    Icons.explore,
                    Colors.cyan,
                  ),
                ),
              ],
            ),
            const SizedBox(height: 6),

            // GPS (Latitude e Longitude)
            Card(
              child: Padding(
                padding: const EdgeInsets.all(10),
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Row(
                      children: [
                        Icon(Icons.location_on, color: Colors.red.shade700, size: 16),
                        const SizedBox(width: 6),
                        const Text(
                          'Localização GPS',
                          style: TextStyle(
                            fontSize: 13,
                            fontWeight: FontWeight.bold,
                          ),
                        ),
                      ],
                    ),
                    const SizedBox(height: 6),
                    Row(
                      children: [
                        Expanded(
                          child: Column(
                            crossAxisAlignment: CrossAxisAlignment.start,
                            children: [
                              const Text(
                                'Latitude',
                                style: TextStyle(
                                  fontSize: 10,
                                  color: Colors.grey,
                                ),
                              ),
                              Obx(() => Text(
                                controller.telemetryData.value.latitude,
                                style: const TextStyle(
                                  fontSize: 13,
                                  fontWeight: FontWeight.bold,
                                ),
                              )),
                            ],
                          ),
                        ),
                        Expanded(
                          child: Column(
                            crossAxisAlignment: CrossAxisAlignment.start,
                            children: [
                              const Text(
                                'Longitude',
                                style: TextStyle(
                                  fontSize: 10,
                                  color: Colors.grey,
                                ),
                              ),
                              Obx(() => Text(
                                controller.telemetryData.value.longitude,
                                style: const TextStyle(
                                  fontSize: 13,
                                  fontWeight: FontWeight.bold,
                                ),
                              )),
                            ],
                          ),
                        ),
                      ],
                    ),
                  ],
                ),
              ),
            ),
          ],
        ),
      ),
    );
  }

  Widget _buildDataCard(
    String label,
    String value,
    String unit,
    IconData icon,
    Color color,
  ) {
    return Card(
      child: Padding(
        padding: const EdgeInsets.all(10),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Row(
              children: [
                Icon(icon, color: color, size: 16),
                const SizedBox(width: 6),
                Flexible(
                  child: Text(
                    label,
                    style: const TextStyle(
                      fontSize: 12,
                      fontWeight: FontWeight.w500,
                    ),
                  ),
                ),
              ],
            ),
            const SizedBox(height: 6),
            Row(
              crossAxisAlignment: CrossAxisAlignment.end,
              children: [
                Flexible(
                  child: Text(
                    value,
                    style: const TextStyle(
                      fontSize: 18,
                      fontWeight: FontWeight.bold,
                    ),
                  ),
                ),
                const SizedBox(width: 4),
                Padding(
                  padding: const EdgeInsets.only(bottom: 1),
                  child: Text(
                    unit,
                    style: TextStyle(
                      fontSize: 11,
                      color: Colors.grey.shade600,
                    ),
                  ),
                ),
              ],
            ),
          ],
        ),
      ),
    );
  }

  Widget _buildStatusCard(
    String label,
    String status,
    IconData icon,
    Color color,
  ) {
    return Card(
      child: Padding(
        padding: const EdgeInsets.all(10),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Row(
              children: [
                Icon(icon, color: color, size: 16),
                const SizedBox(width: 6),
                Text(
                  label,
                  style: const TextStyle(
                    fontSize: 12,
                    fontWeight: FontWeight.w500,
                  ),
                ),
              ],
            ),
            const SizedBox(height: 6),
            Text(
              status,
              style: TextStyle(
                fontSize: 18,
                fontWeight: FontWeight.bold,
                color: color,
              ),
            ),
          ],
        ),
      ),
    );
  }

  String _formatDateTime(DateTime dateTime) {
    return '${dateTime.hour.toString().padLeft(2, '0')}:${dateTime.minute.toString().padLeft(2, '0')}:${dateTime.second.toString().padLeft(2, '0')}';
  }

  void _showDisconnectDialog(BuildContext context, TelemetryController controller) {
    showDialog(
      context: context,
      builder: (context) => AlertDialog(
        title: const Text('Desconectar'),
        content: const Text('Deseja desconectar do dispositivo?'),
        actions: [
          TextButton(
            onPressed: () => Navigator.pop(context),
            child: const Text('Cancelar'),
          ),
          TextButton(
            onPressed: () {
              controller.disconnect();
              Navigator.pop(context);
            },
            child: const Text('Desconectar'),
          ),
        ],
      ),
    );
  }
}
