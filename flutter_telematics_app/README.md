# Flutter Telematics App

Aplicativo Flutter para captura de dados de telemetria via Bluetooth de dispositivos Blue Telematics.

## Dados Capturados

O aplicativo captura os seguintes dados em tempo real:

- **Velocidade** (km/h)
- **Bateria** (V)
- **Rotação do Motor (RPM)**
- **Odômetro** (km)
- **Horímetro** (horas)
- **Nível de Combustível** (%)
- **Torque do Motor** (%)
- **Temperatura do Motor** (°C)
- **Localização GPS** (Latitude, Longitude)
- **Bússola** (graus)
- **Status da Ignição** (Ligada/Desligada)

## Recursos

- ✅ Conexão via Bluetooth Low Energy (BLE)
- ✅ Autenticação com dispositivo
- ✅ Decodificação automática de dados de telemetria
- ✅ Interface moderna e intuitiva
- ✅ Atualização em tempo real
- ✅ Indicadores visuais de status
- ✅ Suporte para múltiplos dispositivos

## Instalação

### Pré-requisitos

- Flutter SDK (>=3.0.0)
- Android Studio ou VS Code
- Dispositivo Android com Bluetooth

### Passos

1. Clone o repositório:
```bash
cd flutter_telematics_app
```

2. Instale as dependências:
```bash
flutter pub get
```

3. Execute o aplicativo:
```bash
flutter run
```

## Configuração

### Chave da Empresa

No arquivo `lib/controllers/telemetry_controller.dart`, configure a chave da empresa:

```dart
List<int> companyKey = [0x00, 0x00, 0x00, 0x00]; // Substitua pelos valores corretos
```

### Permissões

As permissões necessárias já estão configuradas no `AndroidManifest.xml`:

- Bluetooth
- Bluetooth Scan
- Bluetooth Connect
- Localização (necessária para scan BLE no Android)

## Uso

1. **Abra o aplicativo**
2. **Toque no ícone Bluetooth** ou no botão "Buscar Dispositivos"
3. **Toque em "Escanear"** para buscar dispositivos próximos
4. **Selecione o dispositivo** Blue Telematics
5. **Aguarde a conexão e autenticação**
6. **Visualize os dados** em tempo real na tela principal

## Estrutura do Projeto

```
lib/
├── main.dart                       # Ponto de entrada
├── models/
│   └── telemetry_data.dart        # Modelo de dados
├── services/
│   ├── bluetooth_service.dart     # Serviço BLE
│   └── decoder_service.dart       # Decodificação de dados
├── controllers/
│   └── telemetry_controller.dart  # Lógica de negócio
└── views/
    ├── home_screen.dart           # Tela principal
    └── scan_screen.dart           # Tela de scan
```

## Protocolo de Comunicação

O aplicativo implementa o protocolo Blue Telematics V3.0.0:

### Autenticação
1. Recebe `AT+BT_SEED=XXXXXXXXXXXXXXXX`
2. Calcula chave de autenticação
3. Envia `AT+BT_AUTH=XXXXXXXXXXXXXXXX`
4. Aguarda `AT+BT_AUTH_OK`

### Telemetria
1. Recebe `AT+BT_DATA=...` automaticamente
2. Decodifica dados hexadecimais
3. Atualiza interface
4. Confirma com `AT+BT_DATA_OK`

## Decodificação de Dados

Os dados são recebidos em formato hexadecimal e decodificados conforme o protocolo:

| Campo | Posição (bytes) | Fórmula | Unidade |
|-------|----------------|---------|---------|
| Velocidade | 92-95 | int * 0.1 | km/h |
| Bateria | 64-67 | int * 0.05 | V |
| RPM | 96-99 | int * 0.125 | rpm |
| Odômetro | 84-91 | int * 0.125 | km |
| Horímetro | 68-75 | int * 0.05 | h |
| Combustível | 138-139 | int * 0.4 | % |
| Torque | 102-103 | int - 125 | % |
| Temperatura | 120-121 | int - 40 | °C |
| Latitude | 32-39 | (int - 4294967296) / 10000000 | graus |
| Longitude | 40-47 | (int - 4294967296) / 10000000 | graus |
| Bússola | 56-59 | int | graus |
| Ignição | 22 (bit 0) | 0=OFF, 1=ON | - |

## Troubleshooting

### Dispositivo não encontrado
- Verifique se o Bluetooth está ligado
- Verifique se o dispositivo está próximo
- Conceda permissões de localização
- Reinicie o scan

### Falha na conexão
- Verifique se o dispositivo não está conectado a outro aparelho
- Reinicie o dispositivo Blue Telematics
- Reinicie o Bluetooth do celular

### Dados não atualizam
- Verifique a conexão Bluetooth
- Verifique se o dispositivo está enviando dados
- Tente desconectar e reconectar

## Tecnologias Utilizadas

- **Flutter** - Framework de desenvolvimento
- **GetX** - Gerenciamento de estado
- **flutter_blue_plus** - Comunicação Bluetooth
- **permission_handler** - Gerenciamento de permissões

## Compatibilidade

- **Android**: ✅ Testado (API 21+)
- **iOS**: ⚠️ Requer configuração adicional no Info.plist

## Licença

Este projeto é proprietário da BLUECHIP Electronics.

## Suporte

Para dúvidas ou problemas, consulte a documentação técnica do Blue Telematics ou entre em contato com o suporte.

## Versão

**1.0.0** - Versão inicial
- Conexão Bluetooth
- Autenticação
- Captura de 11 parâmetros de telemetria
- Interface intuitiva
