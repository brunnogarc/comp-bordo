# Decodificação de Telemetria - Blue Telematics

## Visão Geral

Este documento explica como funciona a decodificação dos dados de telemetria recebidos via Bluetooth Classic do dispositivo BLUE_1487, baseado no protocolo Blue Telematics V3.0.0 e na implementação de referência em Java (AgDecodeTelematics.java).

## Estrutura do Frame AT+BT_DATA

Os dados chegam no formato:
```
AT+BT_DATA=<hexadecimal_string>
```

Exemplo real:
```
AT+BT_DATA=000005CF31214305FF000105695427DBF275E141E39C2CB20300910B00D800FF00DD00007558FFFFFFFFFFFFFFFF...
```

Após remover o prefixo `AT+BT_DATA=`, temos uma string hexadecimal onde:
- **Cada 2 caracteres** = 1 byte
- **Posições são contadas em caracteres** (não em bytes)

## Mapeamento de Bytes

| Parâmetro | Posição String | Bytes | Fórmula/Conversão |
|-----------|----------------|-------|-------------------|
| Velocidade | 184-187 | 92-93 | hex × 0.1 km/h |
| Bateria | 128-131 | 64-65 | hex × 0.05 V |
| RPM | 192-195 | 96-97 | hex × 0.125 rpm |
| Odômetro | 168-175 | 84-87 | hex × 0.125 km |
| Horímetro | 136-143 | 68-71 | hex × 0.05 h |
| Combustível | 276-277 | 138 | hex × 0.4 % |
| Torque Motor | 204-205 | 102 | hex - 125 % |
| Temperatura | 240-241 | 120 | hex - 40 °C |
| **Latitude** | **32-39** | **16-19** | **(hex - 2³²) / 10⁷** |
| **Longitude** | **40-47** | **20-23** | **(hex - 2³²) / 10⁷** |
| Tipo FIX GPS | 48-49 | 24 | 0=NA, 1=SEM FIX, 2=2D, 3=3D |
| HDOP | 50-53 | 25-26 | hex × 0.01 |
| Qtd Satélites | 54-55 | 27 | decimal direto |
| **Bússola** | **56-59** | **28-29** | **hex = graus (0-360°)** |
| Ignição | 22 | 11 | bit 0 (0x01) |
| Motor | 22 | 11 | bit 1 (0x02) |
| GPS Válido | 22 | 11 | bit 2 (0x04) |

## Decodificação Padrão (Maioria dos Parâmetros)

### Velocidade
```dart
String velocidade = frame.substring(184, 188); // "FFFF" ou valor hex
if (velocidade == "FFFF") return "N/A";
int valor = int.parse(velocidade, radix: 16);
double resultado = valor * 0.1;
return resultado.toStringAsFixed(1); // Ex: "65.3 km/h"
```

**Exemplo:**
- Hex: `028F` = 655 decimal
- Cálculo: 655 × 0.1 = **65.5 km/h**

### Bateria
```dart
String bateria = frame.substring(128, 132); // "FFFF" ou valor hex
if (bateria == "FFFF") return "N/A";
int valor = int.parse(bateria, radix: 16);
double resultado = valor * 0.05;
return resultado.toStringAsFixed(2); // Ex: "12.50 V"
```

**Exemplo:**
- Hex: `00FA` = 250 decimal
- Cálculo: 250 × 0.05 = **12.50 V**

### Temperatura do Motor
```dart
String temp = frame.substring(240, 242); // "FF" ou valor hex
if (temp == "FF") return "N/A";
int valor = int.parse(temp, radix: 16);
double resultado = valor - 40.0;
return resultado.toStringAsFixed(1); // Ex: "85.0°C"
```

**Exemplo:**
- Hex: `7D` = 125 decimal
- Cálculo: 125 - 40 = **85°C**

**Nota:** O offset de -40 permite representar temperaturas negativas (0x00 = -40°C).

## ⭐ LATITUDE E LONGITUDE (Mais Complexas)

### Por que são complexas?

1. **Valores Signed 32-bit**: Coordenadas podem ser negativas (hemisfério Sul/Oeste)
2. **Precisão alta**: 7 casas decimais (resolução de ~11 metros)
3. **Conversão especial**: Necessário subtrair 2³² para valores positivos virarem negativos

### Estrutura do Dado GPS

Cada coordenada ocupa **8 caracteres hex** (4 bytes = 32 bits):

```
Latitude:  substring(32, 40)  → 8 chars → 32 bits
Longitude: substring(40, 48)  → 8 chars → 32 bits
```

### Algoritmo de Decodificação

```dart
String decodeLatitude(String frame, int startPos) {
  String hex = frame.substring(startPos, startPos + 8);
  
  // Caso especial: FFFFFFFF = GPS inválido
  if (hex == "FFFFFFFF") return "N/A";
  
  // Passo 1: Converter hex para inteiro decimal
  int valor = int.parse(hex, radix: 16);
  
  // Passo 2: Converter para signed 32-bit subtraindo 2^32
  double coordenada = (valor - 4294967296) / 10000000.0;
  
  // Passo 3: Validar range geográfico
  if (coordenada < -90.0 || coordenada > 90.0) {
    return "Aguardando GPS"; // GPS ligado mas sem fix
  }
  
  return coordenada.toStringAsFixed(7);
}
```

### Exemplo Real - Latitude

**Frame recebido:**
```
F275E141
```

**Passo a passo:**

1. **Hex → Decimal:**
   ```
   F275E141₁₆ = 4.067.811.649₁₀
   ```

2. **Aplicar offset (2³²):**
   ```
   4.067.811.649 - 4.294.967.296 = -227.155.647
   ```

3. **Dividir por 10⁷:**
   ```
   -227.155.647 / 10.000.000 = -22.7155647°
   ```

4. **Validar range (-90 a +90):**
   ```
   -90 ≤ -22.7155647 ≤ +90 ✅ VÁLIDO
   ```

**Resultado:** `-22.7155647°` (Sul de São Paulo, Brasil)

### Exemplo Real - Longitude

**Frame recebido:**
```
E39C2CB2
```

**Passo a passo:**

1. **Hex → Decimal:**
   ```
   E39C2CB2₁₆ = 3.818.663.090₁₀
   ```

2. **Aplicar offset (2³²):**
   ```
   3.818.663.090 - 4.294.967.296 = -476.304.206
   ```

3. **Dividir por 10⁷:**
   ```
   -476.304.206 / 10.000.000 = -47.6304206°
   ```

4. **Validar range (-180 a +180):**
   ```
   -180 ≤ -47.6304206 ≤ +180 ✅ VÁLIDO
   ```

**Resultado:** `-47.6304206°` (Oeste de São Paulo, Brasil)

### Por que subtrair 2³² (4.294.967.296)?

Coordenadas GPS precisam ser **negativas** para hemisfério Sul e Oeste:

- **Sem subtração:** Todos os valores seriam positivos (0 a 4.294.967.295)
- **Com subtração:** 
  - Valores < 2.147.483.648 → Negativos (após subtração)
  - Valores ≥ 2.147.483.648 → Positivos (após subtração)

**Exemplo prático:**

| Hex | Decimal | Após - 2³² | / 10⁷ | Resultado |
|-----|---------|------------|-------|-----------|
| `00DD0000` | 14.483.456 | -4.280.483.840 | -428.048° | ❌ Inválido |
| `F275E141` | 4.067.811.649 | -227.155.647 | -22.716° | ✅ SP, Brasil |
| `7FFFFFFF` | 2.147.483.647 | -2.147.483.649 | -214.748° | ❌ Inválido |

### Validação de Range

**Por que validar?**

Quando o GPS está ligado (bit2=1) mas ainda **não obteve fix de satélites**, o dispositivo envia valores temporários inválidos:

```dart
if (latitude < -90.0 || latitude > 90.0) {
  return "Aguardando GPS";
}

if (longitude < -180.0 || longitude > 180.0) {
  return "Aguardando GPS";
}
```

**Ranges geográficos válidos:**
- Latitude: **-90° a +90°**
  - -90° = Polo Sul
  - 0° = Equador
  - +90° = Polo Norte
  
- Longitude: **-180° a +180°**
  - -180°/+180° = Linha de Data Internacional
  - 0° = Meridiano de Greenwich

## ⭐ BÚSSOLA (Direção Cardinal)

### Conceito

A bússola retorna um ângulo de **0 a 360 graus** em relação ao **Norte Magnético**.

### Estrutura do Dado

```
Posição: substring(56, 60) → 4 caracteres hex (2 bytes)
```

### Decodificação

```dart
String decodeBussola(String frame, int startPos) {
  String hex = frame.substring(startPos, startPos + 4);
  
  if (hex == "FFFF") return "N/A";
  
  // Conversão direta: hex → graus
  int valor = int.parse(hex, radix: 16);
  double graus = valor.toDouble();
  
  // Converter para direção cardinal
  String direcao = converterParaDirecao(graus);
  
  return "${graus.toStringAsFixed(1)}° ($direcao)";
}
```

### Exemplo Real

**Frame recebido:**
```
00D8
```

**Cálculo:**
```
00D8₁₆ = 216₁₀ = 216.0°
```

**Conversão para direção cardinal:**

| Range | Direção | Nome |
|-------|---------|------|
| 337.5° - 22.5° | N | Norte |
| 22.5° - 67.5° | NE | Nordeste |
| 67.5° - 112.5° | E | Leste |
| 112.5° - 157.5° | SE | Sudeste |
| 157.5° - 202.5° | S | Sul |
| 202.5° - 247.5° | SW | Sudoeste |
| 247.5° - 292.5° | W | Oeste |
| 292.5° - 337.5° | NW | Noroeste |

**216° está no range 202.5° - 247.5°** → **SW (Sudoeste)**

**Resultado:** `216.0° (SW)`

### Implementação Completa

```dart
String _grausParaDirecao(double graus) {
  if (graus >= 337.5 || graus < 22.5) return "N";
  if (graus >= 22.5 && graus < 67.5) return "NE";
  if (graus >= 67.5 && graus < 112.5) return "E";
  if (graus >= 112.5 && graus < 157.5) return "SE";
  if (graus >= 157.5 && graus < 202.5) return "S";
  if (graus >= 202.5 && graus < 247.5) return "SW";
  if (graus >= 247.5 && graus < 292.5) return "W";
  if (graus >= 292.5 && graus < 337.5) return "NW";
  return "N"; // Fallback
}
```

## Flags de Status (Byte 11)

O byte na posição 22 (byte 11) contém 3 flags importantes:

```dart
int statusByte = int.parse(frame.substring(22, 24), radix: 16);

bool posChave = (statusByte & 0x01) != 0;  // bit 0
bool motor    = (statusByte & 0x02) != 0;  // bit 1
bool gpsValido = (statusByte & 0x04) != 0; // bit 2
```

**Exemplo:**
```
Hex: 05₁₆ = 00000101₂
         └─ bit0=1 → Pós-chave ligado
          └ bit1=0 → Motor desligado
           └bit2=1 → GPS válido
```

## Casos Especiais - Valor "N/A"

Cada parâmetro tem um valor especial que indica **"Não Avaliado"**:

| Tamanho | Valor Hex | Significado |
|---------|-----------|-------------|
| 1 byte | `FF` | Sensor não disponível |
| 2 bytes | `FFFF` | Sensor não disponível |
| 4 bytes | `FFFFFFFF` | Sensor não disponível |

**Exemplo:**
```dart
if (temperatura == "FF") return "N/A";
if (velocidade == "FFFF") return "N/A";
if (odometro == "FFFFFFFF") return "N/A";
```

## Precisão Numérica

Diferentes parâmetros usam diferentes precisões:

```dart
velocidade.toStringAsFixed(1);     // 1 casa: 65.3 km/h
bateria.toStringAsFixed(2);        // 2 casas: 12.50 V
latitude.toStringAsFixed(7);       // 7 casas: -22.7155647°
```

**Por que 7 casas decimais para GPS?**

- 1° latitude ≈ 111 km
- 0.0000001° ≈ **1.1 centímetros** de precisão
- Suficiente para navegação precisa

## Resumo de Dificuldades Encontradas

### 1. Latitude/Longitude

**Problema inicial:** Valores mostrando -428° (fora do range geográfico)

**Causa:** Estava usando posições erradas no frame (bytes 32-35 em vez de 16-19)

**Solução:** 
- Corrigir posições: substring(32, 40) e substring(40, 48)
- Adicionar validação de range geográfico
- Retornar "Aguardando GPS" se inválido

### 2. Bússola

**Problema inicial:** Conversão não mostrava direção cardinal

**Solução:** 
- Adicionar função `_grausParaDirecao()`
- Implementar ranges de 45° para cada direção cardinal
- Formato final: "216.0° (SW)"

### 3. Ignição Oscilando

**Problema inicial:** Status alternando entre Ligada/Desligada a cada frame

**Causa:** Usando byte errado (posição 44 em vez de 22)

**Solução:** 
- Corrigir para posição 22 (byte 11)
- Ler bit 0 corretamente

## Referências

- **Protocolo:** Blue Telematics V3.0.0
- **Implementação Java:** AgDecodeTelematics.java
- **Biblioteca Flutter BT Classic:** flutter_bluetooth_serial 0.4.0
- **Documentação:** BLUE_TELEMATICS_WIFI_BLE_Protocolos_e_Comandos_V3.0.0.md

## Exemplo Completo - Frame Real Decodificado

```
Frame: 000005CF31214305FF000105695427DBF275E141E39C2CB20300910B00D8...

Decodificação:
├─ Status (pos 22): 05 → Pós-chave=ON, Motor=OFF, GPS=ON
├─ Latitude (pos 32-39): F275E141 → -22.7155647°
├─ Longitude (pos 40-47): E39C2CB2 → -47.6304206°
├─ Tipo FIX (pos 48-49): 03 → 3D (GPS com altitude)
├─ HDOP (pos 50-53): 0091 → 1.45 (boa precisão)
├─ Satélites (pos 54-55): 0B → 11 satélites
├─ Bússola (pos 56-59): 00D8 → 216.0° (SW)
├─ Velocidade (pos 184-187): FFFF → N/A
├─ Bateria (pos 128-131): FFFF → N/A
├─ RPM (pos 192-195): FFFF → N/A
├─ Odômetro (pos 168-175): 00000000 → 0.00 km
├─ Horímetro (pos 136-143): FFFFFFFF → N/A
├─ Combustível (pos 276-277): FF → N/A
├─ Torque (pos 204-205): FF → N/A
└─ Temperatura (pos 240-241): FF → N/A
```

**Localização identificada:** São Paulo, Brasil 🇧🇷

---

**Documento criado em:** 30/12/2025  
**Versão:** 1.0  
**Autor:** Sistema de Desenvolvimento Blue Telematics
