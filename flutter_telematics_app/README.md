# Flutter Telematics App 🚜📡

Aplicativo Flutter para captura e visualização de telemetria veicular via Bluetooth Classic do dispositivo **BLUE_1487** (Blue Chip Telematics).

![Flutter](https://img.shields.io/badge/Flutter-3.24.5-02569B?logo=flutter)
![Dart](https://img.shields.io/badge/Dart-3.5.4-0175C2?logo=dart)
![Android](https://img.shields.io/badge/Android-16+-3DDC84?logo=android)
![License](https://img.shields.io/badge/License-MIT-green)

## 📋 Características

### ✅ Telemetria em Tempo Real
- **11 parâmetros** decodificados e exibidos simultaneamente
- Atualização automática a cada 3 minutos (configurável)
- Interface moderna com Material Design 3

### 📊 Parâmetros Monitorados

| Parâmetro | Unidade | Status |
|-----------|---------|--------|
| 🚗 Velocidade | km/h | ✅ |
| 🔋 Bateria | Volts | ✅ |
| ⚙️ RPM | rpm | ✅ |
| 📏 Odômetro | km | ✅ |
| ⏱️ Horímetro | horas | ✅ |
| ⛽ Combustível | % | ✅ |
| 🔧 Torque Motor | % | ✅ |
| 🌡️ Temperatura | °C | ✅ |
| 🌍 Latitude | graus | ✅ |
| 🌎 Longitude | graus | ✅ |
| 🧭 Bússola | N/S/E/W | ✅ |

### 🔐 Autenticação
- Sistema de autenticação via código de usuário
- Handshake com SEED + senha MD5
- Armazenamento seguro de credenciais
- Auto-reconexão automática

### 📡 Conectividade
- **Bluetooth Classic** (SPP - Serial Port Profile)
- Scan automático de dispositivos próximos
- Detecção automática de desconexão
- Retry automático em caso de falha

## 🛠️ Tecnologias Utilizadas

- **Flutter 3.24.5** - Framework multiplataforma
- **Dart 3.5.4** - Linguagem de programação
- **GetX 4.6.6** - Gerenciamento de estado e rotas
- **flutter_bluetooth_serial 0.4.0** - Bluetooth Classic
- **crypto 3.0.6** - Hash MD5 para autenticação
- **shared_preferences 2.3.4** - Armazenamento local

## 🚀 Como Executar

### Pré-requisitos

- Flutter SDK 3.24.5 ou superior
- Android Studio / VS Code
- Dispositivo Android com Bluetooth (API 16+)
- Dispositivo BLUE_1487 pareado

### Instalação

1. Clone o repositório:
```bash
git clone https://github.com/brunnogarc/comp-bordo.git
cd comp-bordo/flutter_telematics_app
```

2. Instale as dependências:
```bash
flutter pub get
```

3. Execute no dispositivo:
```bash
flutter run
```

### Configuração

1. **Primeiro Acesso:**
   - Digite o código de usuário (8 bytes em decimal)
   - Exemplo: `283686952306183`

2. **Parear Dispositivo:**
   - Vá para Configurações Bluetooth do Android
   - Pareie com `BLUE_1487`
   - PIN padrão: `1234` ou `0000`

3. **Conectar no App:**
   - Abra o app
   - Toque em "Scanear Dispositivos"
   - Selecione `BLUE_1487`
   - Aguarde autenticação

## 📱 Estrutura do Projeto

```
lib/
├── main.dart                          # Entry point
├── controllers/
│   └── telemetry_controller.dart     # Lógica de negócio e Bluetooth
├── models/
│   ├── telemetry_data.dart           # Modelo de dados
│   └── unified_device.dart           # Modelo de dispositivo BT
├── services/
│   ├── bluetooth_service.dart        # Comunicação Bluetooth
│   ├── decoder_service.dart          # Decodificação de telemetria
│   ├── logger_service.dart           # Sistema de logs
│   └── storage_service.dart          # Persistência de dados
└── views/
    ├── initial_screen.dart           # Tela de boas-vindas
    ├── scan_screen.dart              # Scan de dispositivos
    ├── home_screen.dart              # Dashboard principal
    └── settings_screen.dart          # Configurações
```

## 🔍 Como Funciona a Decodificação

### Protocolo Blue Telematics V3.0.0

O dispositivo envia frames hexadecimais no formato:
```
AT+BT_DATA=<232 caracteres hexadecimais>
```

### Mapeamento de Bytes

**IMPORTANTE:** Os índices referem-se à **posição dos caracteres na string hexadecimal**, não aos bytes. Cada byte = 2 caracteres hex.

| Parâmetro | Posição String | Tamanho | Fórmula |
|-----------|----------------|---------|---------|
| **Velocidade** | 92-95 | 4 chars (2 bytes) | `hex × 0.1` km/h |
| **Bateria** | 64-67 | 4 chars (2 bytes) | `hex × 0.05` V |
| **RPM** | 96-99 | 4 chars (2 bytes) | `hex × 0.125` rpm |
| **Odômetro** | 84-91 | 8 chars (4 bytes) | `hex × 0.125` km |
| **Horímetro** | 68-75 | 8 chars (4 bytes) | `hex × 0.05` horas |
| **Combustível** | 138-139 | 2 chars (1 byte) | `hex × 0.4` % |
| **Torque** | 102-103 | 2 chars (1 byte) | `(hex - 125)` % |
| **Temperatura** | 120-121 | 2 chars (1 byte) | `(hex - 40)` °C |
| **Latitude** | 32-39 | 8 chars (4 bytes) | `(hex - 2³²) ÷ 10⁷` graus |
| **Longitude** | 40-47 | 8 chars (4 bytes) | `(hex - 2³²) ÷ 10⁷` graus |
| **Bússola** | 56-59 | 4 chars (2 bytes) | `hex × 0.1` graus |

### Exemplo Prático de Decodificação

**Frame recebido:**
```
AT+BT_DATA=000005CF31214305FF000107695817DBF275DFD7E39C2991...
```

**Decodificando o Horímetro (Motor):**

1. **Extrair hex da posição 68-75:**
```dart
String hex = hexData.substring(68, 76);  // "00007567"
```

2. **Converter para decimal:**
```dart
int decimal = int.parse(hex, radix: 16);  // 30055
```

3. **Aplicar fórmula:**
```dart
double horas = decimal * 0.05;  // 1502.75 horas ✅
```

**Decodificando Coordenadas GPS:**

As coordenadas usam **complemento de dois** (signed integer 32 bits):

```dart
// Latitude na posição 32-39
String hexLat = hexData.substring(32, 40);  // "F275DFD7"

// Converter para inteiro com sinal
int rawLat = int.parse(hexLat, radix: 16);
if (rawLat > 0x7FFFFFFF) {
  rawLat = rawLat - 0x100000000;  // Ajustar para negativo
}

// Aplicar fórmula
double latitude = rawLat / 10000000.0;  // -22.7156009° ✅
```

**Tratamento de Valores Inválidos:**
- `FF` (1 byte) = Não avaliado → Retorna `"N/A"`
- `FFFF` (2 bytes) = Não avaliado → Retorna `"N/A"`
- `FFFFFFFF` (4 bytes) = Não avaliado → Retorna `"N/A"`

### Fluxo de Autenticação

```
1. App → Device: AT+BT_COD_USER=283686952306183
2. Device → App: AT+BT_AUTH_SEED=A1B2C3D4E5F6G7H8
3. App calcula: MD5(seed + "12345678")
4. App → Device: AT+BT_AUTH=<hash MD5>
5. Device → App: AT+BT_AUTH_OK
6. App → Device: AT_BT_DATA_START
7. Device → App: AT+BT_DATA=<telemetria> (a cada 3 min)
8. App → Device: AT+BT_DATA_OK
```

## 🐛 Troubleshooting

### Dispositivo não aparece no scan
- Certifique-se que o Bluetooth está ligado
- Verifique se o dispositivo está pareado nas configurações do Android
- Reinicie o Bluetooth e tente novamente

### Erro de autenticação
- Verifique se o código de usuário está correto (8 bytes em decimal)
- Apague os dados do app (Settings → Apps → Clear Data)
- Tente reconectar

### Valores aparecem como N/A
- Motor desligado (RPM, velocidade)
- Sensores não configurados no dispositivo
- Aguarde alguns segundos após conectar

### Coordenadas GPS incorretas
- Aguarde o GPS obter sinal (pode levar alguns minutos)
- Verifique se está em ambiente aberto (não indoor)
- HDOP < 2.0 indica boa precisão

### Hot reload não atualiza valores
- Mudanças em posições de substring requerem **Hot Restart** (Ctrl+Shift+F5)
- Ou pare e execute `flutter run` novamente

## 📚 Documentação Adicional

- [Protocolo Blue Telematics V3.0.0](../_docs/BLUE_TELEMATICS_WIFI_BLE_Protocolos_e_Comandos_V3.0.0.md)
- [Decodificação de Telemetria](../_docs/DECODIFICACAO_TELEMETRIA.md)
- [Guia da Aplicação Java](../_docs/APLICACAO_JAVA_CONFIGURACAO.md)

## 📝 Changelog

### v1.0.0 (Janeiro 2026)
- ✅ Implementação completa de 11 parâmetros de telemetria
- ✅ Autenticação via Bluetooth Classic
- ✅ GPS com precisão de 7 casas decimais
- ✅ Bússola com direções cardinais (N, S, E, W, NE, SE, SW, NW)
- ✅ Sistema de logs detalhado com emojis
- ✅ Auto-reconexão automática
- ✅ Interface Material Design 3
- ✅ Correção crítica: índices de substring corrigidos (eram dobrados incorretamente)

### Próximas Features
- 🔄 Tela de configurações do dispositivo
- 🔄 Histórico de telemetria
- 🔄 Gráficos de desempenho
- 🔄 Exportação de dados (CSV/JSON)
- 🔄 Notificações de alertas
- 🔄 Modo offline

## 🤝 Contribuindo

Contribuições são bem-vindas! Por favor:

1. Faça um Fork do projeto
2. Crie uma branch para sua feature (`git checkout -b feature/NovaFeature`)
3. Commit suas mudanças (`git commit -m 'Adiciona nova feature'`)
4. Push para a branch (`git push origin feature/NovaFeature`)
5. Abra um Pull Request

## 📄 Licença

Este projeto está sob a licença MIT.

## 👨‍💻 Autor

**Brunno Garcia**
- GitHub: [@brunnogarc](https://github.com/brunnogarc)
- Repositório: [comp-bordo](https://github.com/brunnogarc/comp-bordo)

---

**Nota:** Este aplicativo foi desenvolvido para comunicação com dispositivos Blue Chip Telematics modelo BLUE_1487. Certifique-se de ter o hardware compatível antes de usar.

**Status do Projeto:** ✅ Produção - Totalmente funcional

**Última atualização:** Janeiro 2026
