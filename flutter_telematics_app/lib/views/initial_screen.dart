import 'package:flutter/material.dart';
import 'package:get/get.dart';
import '../controllers/telemetry_controller.dart';
import 'home_screen.dart';
import 'scan_screen.dart';

class InitialScreen extends StatefulWidget {
  const InitialScreen({super.key});

  @override
  State<InitialScreen> createState() => _InitialScreenState();
}

class _InitialScreenState extends State<InitialScreen> {
  final TelemetryController controller = Get.find();
  final TextEditingController _userCodeController = TextEditingController();

  @override
  void initState() {
    super.initState();
    _userCodeController.text = '0001020304050607'; // Código padrão
    _loadStoredData();
  }

  Future<void> _loadStoredData() async {
    await controller.loadStoredData();
    // Se houver código armazenado, sobrescreve o padrão
    if (controller.storedUserCode.value.isNotEmpty) {
      _userCodeController.text = controller.storedUserCode.value;
    }
  }

  @override
  void dispose() {
    _userCodeController.dispose();
    super.dispose();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Blue Telematics'),
        centerTitle: true,
      ),
      body: SingleChildScrollView(
        padding: const EdgeInsets.all(24),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.stretch,
          children: [
            const SizedBox(height: 40),

            // Logo/Ícone
            const Icon(
              Icons.bluetooth_audio,
              size: 100,
              color: Colors.blue,
            ),
            const SizedBox(height: 20),

            const Text(
              'Bem-vindo',
              style: TextStyle(
                fontSize: 32,
                fontWeight: FontWeight.bold,
              ),
              textAlign: TextAlign.center,
            ),
            const SizedBox(height: 8),
            const Text(
              'Sistema de Telemetria Veicular',
              style: TextStyle(
                fontSize: 16,
                color: Colors.grey,
              ),
              textAlign: TextAlign.center,
            ),
            const SizedBox(height: 40),

            // Último dispositivo conectado
            Card(
              elevation: 2,
              child: InkWell(
                onTap: () {
                  // Salvar código antes de ir para scan
                  if (_userCodeController.text.isNotEmpty) {
                    controller.saveUserCode(_userCodeController.text);
                  }
                  Get.to(() => const ScanScreen());
                },
                borderRadius: BorderRadius.circular(12),
                child: Padding(
                  padding: const EdgeInsets.all(16),
                  child: Column(
                    crossAxisAlignment: CrossAxisAlignment.start,
                    children: [
                      Row(
                        children: [
                          Icon(Icons.history, color: Colors.blue.shade700),
                          const SizedBox(width: 10),
                          const Text(
                            'Último Dispositivo',
                            style: TextStyle(
                              fontSize: 16,
                              fontWeight: FontWeight.bold,
                            ),
                          ),
                          const Spacer(),
                          Icon(Icons.chevron_right,
                              color: Colors.grey.shade400),
                        ],
                      ),
                      const SizedBox(height: 12),
                      Obx(() {
                        if (controller.lastDeviceName.value.isEmpty) {
                          return const Text(
                            'Nenhum dispositivo conectado anteriormente\nToque para buscar dispositivos',
                            style: TextStyle(color: Colors.grey),
                          );
                        }
                        return Column(
                          crossAxisAlignment: CrossAxisAlignment.start,
                          children: [
                            Text(
                              controller.lastDeviceName.value,
                              style: const TextStyle(
                                fontSize: 18,
                                fontWeight: FontWeight.w600,
                              ),
                            ),
                            const SizedBox(height: 4),
                            Text(
                              controller.lastDeviceAddress.value,
                              style: TextStyle(
                                fontSize: 14,
                                color: Colors.grey.shade600,
                              ),
                            ),
                          ],
                        );
                      }),
                    ],
                  ),
                ),
              ),
            ),
            const SizedBox(height: 16),

            // Código de Usuário
            Card(
              elevation: 2,
              child: Padding(
                padding: const EdgeInsets.all(16),
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Row(
                      children: [
                        Icon(Icons.key, color: Colors.blue.shade700),
                        const SizedBox(width: 10),
                        const Text(
                          'Código de Usuário',
                          style: TextStyle(
                            fontSize: 16,
                            fontWeight: FontWeight.bold,
                          ),
                        ),
                      ],
                    ),
                    const SizedBox(height: 12),
                    TextField(
                      controller: _userCodeController,
                      decoration: InputDecoration(
                        hintText: 'Digite o código (ex: 0001020304050607)',
                        border: OutlineInputBorder(
                          borderRadius: BorderRadius.circular(8),
                        ),
                        prefixIcon: const Icon(Icons.vpn_key),
                        suffixIcon: IconButton(
                          icon: const Icon(Icons.save),
                          onPressed: () {
                            controller.saveUserCode(_userCodeController.text);
                            Get.snackbar(
                              'Salvo',
                              'Código de usuário salvo com sucesso',
                              snackPosition: SnackPosition.BOTTOM,
                              backgroundColor: Colors.green,
                              colorText: Colors.white,
                            );
                          },
                        ),
                      ),
                      maxLength: 16,
                      keyboardType: TextInputType.text,
                    ),
                    const SizedBox(height: 8),
                    Text(
                      'Este código será usado automaticamente na autenticação',
                      style: TextStyle(
                        fontSize: 12,
                        color: Colors.grey.shade600,
                      ),
                    ),
                  ],
                ),
              ),
            ),
            const SizedBox(height: 24),

            // Botão Continuar
            Obx(() => ElevatedButton.icon(
                  onPressed: () async {
                    // Salvar código se preenchido
                    if (_userCodeController.text.isNotEmpty) {
                      controller.saveUserCode(_userCodeController.text);
                    }

                    // Se houver último dispositivo, tentar conectar primeiro
                    if (controller.lastDeviceAddress.value.isNotEmpty) {
                      // Mostrar dialog de loading
                      Get.dialog(
                        WillPopScope(
                          onWillPop: () async => false,
                          child: Center(
                            child: Card(
                              child: Padding(
                                padding: const EdgeInsets.all(24),
                                child: Column(
                                  mainAxisSize: MainAxisSize.min,
                                  children: [
                                    const CircularProgressIndicator(),
                                    const SizedBox(height: 16),
                                    Text(
                                      'Conectando a ${controller.lastDeviceName.value}...',
                                      style: const TextStyle(fontSize: 16),
                                    ),
                                  ],
                                ),
                              ),
                            ),
                          ),
                        ),
                        barrierDismissible: false,
                      );

                      // Tentar reconectar ao último dispositivo
                      await controller.reconnectToLastDevice();

                      // Fechar dialog
                      Get.back();

                      // Verificar se conectou
                      if (controller.isConnected.value) {
                        // Se conectou, ir para home
                        Get.off(() => const HomeScreen());
                      } else {
                        // Se não conectou, mostrar erro
                        Get.snackbar(
                          'Erro',
                          'Não foi possível conectar. Tente novamente ou busque dispositivos.',
                          snackPosition: SnackPosition.BOTTOM,
                          duration: const Duration(seconds: 3),
                        );
                      }
                    } else {
                      // Se não houver dispositivo, vai direto para home
                      Get.off(() => const HomeScreen());
                    }
                  },
                  icon: const Icon(Icons.arrow_forward),
                  label: Text(
                    controller.lastDeviceAddress.value.isNotEmpty
                        ? 'Conectar e Continuar'
                        : 'Continuar',
                    style: const TextStyle(fontSize: 18),
                  ),
                  style: ElevatedButton.styleFrom(
                    padding: const EdgeInsets.symmetric(vertical: 16),
                    shape: RoundedRectangleBorder(
                      borderRadius: BorderRadius.circular(8),
                    ),
                  ),
                )),
            const SizedBox(height: 40),
          ],
        ),
      ),
    );
  }
}
