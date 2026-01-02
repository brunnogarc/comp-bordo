# Aplicação Java - Configuração do Dispositivo BLUE_1487

Este documento descreve todas as funcionalidades da aplicação Java de configuração do dispositivo BLUE_1487 Telematics.

---

## Visão Geral

A aplicação Java serve para **configurar parâmetros** do dispositivo BLUE_1487, enquanto o app Flutter é usado para **visualizar telemetria em tempo real**. São ferramentas complementares:

- **App Java**: Configuração e parametrização do dispositivo
- **App Flutter**: Monitoramento e visualização de dados em tempo real

---

## ABA 1: COMANDOS - Configurações Gerais

### Sistema

**VER. FIRMWARE**
- Consulta a versão do firmware instalado no dispositivo
- Útil para verificar atualizações disponíveis

**FACTORY**
- Reset de fábrica (Factory Reset)
- Restaura todas as configurações para os valores padrão de fábrica
- ⚠️ **CUIDADO**: Apaga todas as configurações personalizadas

**FORMAT MEM.**
- Formata a memória interna do dispositivo
- Remove dados armazenados (logs, histórico, etc.)

---

### Time Mensagem (Intervalos de Envio)

**SET TIME REPORT / GET TIME REPORT**
- **Função**: Define/consulta o intervalo de envio de telemetria
- **Importância**: ⭐⭐⭐⭐⭐ Este é o parâmetro mais importante!
- **Uso**: Define de quanto em quanto tempo o dispositivo envia os pacotes `AT+BT_DATA=...`
- **Exemplos**:
  - 1 segundo = Telemetria em tempo real (mais bateria)
  - 5 segundos = Atualização moderada (economia de bateria)
  - 30 segundos = Apenas rastreamento básico

**SET REPORT SPEED / GET REPORT SPEED**
- Define/consulta a velocidade mínima para enviar relatórios
- Exemplo: Só enviar dados se velocidade > 5 km/h (economiza dados quando parado)

**SET TIME SEND MSG / GET TIME SEND MSG**
- Define/consulta intervalo de envio de mensagens específicas
- Usado para notificações ou alertas periódicos

**SET TIM REP IG OFF / GET TIM REP IG OFF**
- Define/consulta intervalo de reporte quando a ignição está **desligada**
- Permite configurar intervalos maiores quando o veículo está parado
- Exemplo: 1 seg com ignição ligada, 60 seg com ignição desligada

---

### Bluetooth

**SET TIME MAX AUTH / GET TIME MAX AUTH**
- Define/consulta tempo máximo para completar a autenticação
- Se a autenticação demorar mais que esse tempo, a conexão é rejeitada
- Segurança contra tentativas de conexão não autorizadas

**SET TIME RCV ACK / GET TIME RCV ACK**
- Define/consulta tempo de espera para receber ACK (confirmação)
- ACK = `AT+BT_DATA_OK` enviado pelo app após receber dados
- Se não receber o ACK nesse tempo, o dispositivo reenvia o pacote

**SET NAME BLE / GET NAME BLE**
- Configura/consulta o nome do dispositivo Bluetooth
- Nome que aparece na busca de dispositivos
- Útil para identificar múltiplos dispositivos (ex: "BLUE_TRATOR_01", "BLUE_CAMINHAO_02")

---

### Sensores

**SET RPM NOW / GET RPM NOW**
- Configura/lê o RPM atual do motor
- Usado para calibração inicial do sensor de rotação

**SET SPEED REPORT / GET SPEED REPORT**
- Configura parâmetros de reporte de velocidade
- Define limiares ou condições para envio

**QTD_MSG_FLASH**
- Consulta quantidade de mensagens armazenadas na memória flash
- Útil para saber quantos dados históricos estão salvos

---

### GPS

**SET ANG. COMPASS / GET ANG. COMPASS**
- Configura/consulta ângulo da bússola
- Usado para calibração do compass (ajustar offset magnético)
- Importante se o dispositivo estiver instalado em ângulo não-padrão

**SET CONF COMPASS / GET CONF COMPASS**
- Configura/consulta parâmetros gerais do compass
- Ajustes avançados de calibração magnética

---

## ABA 2: WIFI / SERVER - Conectividade Internet

### WiFi - Configuração de Redes

**Campos:**
- **SSID**: Nome da rede WiFi
- **PASS**: Senha da rede WiFi

**Botões:**
- **Listar**: Exibe as redes WiFi já configuradas no dispositivo
- **CARREGAR**: Carrega configurações salvas anteriormente
- **SALVAR**: Grava as 5 redes no dispositivo

**Como funciona:**
- O dispositivo pode armazenar até **5 redes WiFi**
- Tenta conectar em ordem de prioridade (01 → 02 → 03 → 04 → 05)
- Se perder conexão com uma rede, tenta a próxima automaticamente
- Útil para veículos que circulam entre diferentes locais (ex: garagem, pátio, oficina)

---

### SERVER - Servidores de Telemetria

**Campos:**
- **IP**: Endereço do servidor (IP ou domínio)
  - Exemplo IP: `192.168.1.100`
  - Exemplo domínio: `telemetria.minhaempresa.com.br`
- **PORTA**: Porta TCP/UDP do servidor
  - Exemplo: `8080`, `5000`, `3000`

**Botões:**
- **Listar**: Exibe os servidores já configurados
- **CARREGAR**: Carrega configurações salvas
- **SALVAR**: Grava os 5 servidores no dispositivo

**Como funciona:**
- O dispositivo pode enviar dados para até **5 servidores diferentes**
- Se conectado ao WiFi, envia telemetria via TCP/IP para esses servidores
- Permite redundância: se um servidor cair, usa o backup
- Útil para empresas que querem centralizar dados de toda a frota

---

### Ferramentas

**SET TIME CHECK CONF / GET TIME CHECK CONF**
- Define/consulta intervalo de verificação de conexão
- De quanto em quanto tempo o dispositivo verifica se WiFi/servidor ainda estão ativos
- Reconecta automaticamente se detectar queda

**Comando Livre**
- Campo para enviar comandos AT personalizados
- Para testes ou comandos avançados não disponíveis nos botões
- Exemplo: `AT+BT_GET_STATUS`

**Decode Frame**
- Ferramenta para decodificar frame de telemetria manualmente
- Útil para debug: copiar um frame hexadecimal e ver os valores decodificados
- Mesma decodificação que o app Flutter faz automaticamente

---

### Modos de Operação

O dispositivo BLUE_1487 pode funcionar em **2 modos simultâneos**:

#### 1. Modo Bluetooth (App Flutter)
- ✅ Conexão direta com smartphone via Bluetooth Classic
- ✅ Telemetria em tempo real na tela do app
- ✅ Ideal para: Operador no veículo, diagnóstico local
- ❌ Limitação: Apenas 1 dispositivo conectado por vez, alcance ~10 metros

#### 2. Modo WiFi + Servidor (Backend)
- ✅ Dispositivo conecta no WiFi local
- ✅ Envia telemetria para servidor na nuvem via TCP/IP
- ✅ Ideal para: Gestão de frota, múltiplos veículos, histórico centralizado
- ✅ Permite: Dashboards web, relatórios, alertas automáticos
- ❌ Requer: Infraestrutura de WiFi no local e servidor rodando

**Ambos podem funcionar ao mesmo tempo!**

---

## ABA 3: FERRAMENTAS - Firmware e Horímetros

### Atualização de Firmware

**Arquivo + Carregar arquivo**
- Seleciona arquivo do novo firmware (.bin ou .hex)
- Geralmente fornecido pelo fabricante

**Iniciar Atualização**
- Inicia o processo de gravação do novo firmware
- ⚠️ **NÃO DESCONECTAR** durante a atualização!
- Processo leva 2-5 minutos
- Dispositivo reinicia automaticamente após conclusão

**Quando atualizar:**
- Correção de bugs
- Novos recursos
- Melhorias de performance ou bateria
- Patches de segurança

---

### Horímetros (Contadores de Horas)

Os horímetros rastreiam **tempo de operação** de diferentes componentes. São essenciais para manutenção preventiva.

#### HORÍMETRO DO MOTOR

**GET HOURS NOW**
- Consulta horas **atuais** acumuladas do motor
- Exemplo: `2543.7 horas`

**SET HOURS INIT / GET HOURS INIT**
- Define/consulta valor **inicial** do horímetro
- Usado quando instala o dispositivo em veículo com uso anterior
- Exemplo: Motor já tem 1000h antes da instalação → SET HOURS INIT = 1000

**SET HOURS COUNT / GET HOURS COUNT**
- Define/consulta taxa de **incremento** de contagem
- Geralmente 1:1 (1 hora real = 1 hora no contador)
- Pode ajustar se o sensor tiver erro de calibração

**Como funciona:**
- Conta horas sempre que ignição está ligada E motor em funcionamento (RPM > 0)
- Valor aparece no app Flutter como `Horímetro`

---

#### HORÍMETRO ESTEIRA

**Para que serve:**
- Máquinas agrícolas/industriais com esteira transportadora
- Colheitadeiras, plantadeiras, tratores com implementos
- Esteira pode funcionar independente do deslocamento

**Mesmas funções do horímetro do motor:**
- GET HOURS NOW
- SET/GET HOURS INIT
- SET/GET HOURS COUNT

**Como funciona:**
- Conta horas quando a esteira está ativa
- Sensor específico detecta movimento/rotação da esteira

---

#### HORÍMETRO PILOTO

**Para que serve:**
- Rastreia tempo de trabalho do operador
- Usado para folha de pagamento (horas extras)
- Controle de jornada de trabalho
- Empresas de locação de equipamentos

**Mesmas funções dos outros horímetros:**
- GET HOURS NOW
- SET/GET HOURS INIT
- SET/GET HOURS COUNT

**Como funciona:**
- Pode contar de diferentes formas:
  - Sempre que ignição ligada (operador no veículo)
  - Por RFID/cartão de identificação
  - Por login manual no sistema

---

### Aplicações dos Horímetros

#### Manutenção Preventiva
```
- Trocar óleo: a cada 250 horas
- Filtro de ar: a cada 500 horas
- Revisão geral: a cada 1000 horas
```

#### Gestão de Frota
```
- Trator A: 2543h → Próxima revisão: 57h
- Trator B: 1892h → Próxima revisão: 108h
- Colheitadeira: 789h → OK
```

#### Cobrança por Uso
```
Aluguel de retroescavadeira:
- Valor/hora: R$ 180,00
- Início: 1250h
- Fim: 1267h
- Uso: 17 horas
- Total: R$ 3.060,00
```

---

## Fluxo de Configuração Recomendado

### Configuração Inicial do Dispositivo

1. **Identificação**
   - SET NAME BLE → Definir nome único (ex: "BLUE_TRATOR_01")

2. **Intervalos de Envio**
   - SET TIME REPORT → 1-5 segundos (conforme necessidade)
   - SET TIM REP IG OFF → 60 segundos (economizar bateria quando desligado)

3. **Horímetros**
   - SET HOURS INIT (motor) → Valor atual do hodômetro/horímetro do veículo
   - SET HOURS INIT (esteira) → Se aplicável
   - SET HOURS INIT (piloto) → Se aplicável

4. **Calibração (Opcional)**
   - SET ANG. COMPASS → Ajustar offset magnético se necessário
   - SET RPM NOW → Calibrar sensor de rotação

5. **WiFi/Servidor (Opcional)**
   - Configurar redes WiFi disponíveis
   - Configurar endereços de servidores backend

6. **Teste**
   - Conectar no app Flutter e verificar todos os parâmetros
   - Verificar se dados estão sendo enviados corretamente

---

## Diferenças: App Java vs App Flutter

| Característica | App Java (Configuração) | App Flutter (Monitoramento) |
|---------------|------------------------|----------------------------|
| **Plataforma** | Desktop (Windows/Mac/Linux) | Mobile (Android/iOS) |
| **Função Principal** | Configurar dispositivo | Visualizar telemetria |
| **Frequência de Uso** | Raramente (setup inicial) | Sempre (uso diário) |
| **Conectividade** | USB ou Bluetooth | Bluetooth apenas |
| **Interface** | Técnica (para instaladores) | Simples (para operadores) |
| **Requer Conhecimento** | Técnico avançado | Básico |

---

## Comandos AT Mencionados

Referência dos principais comandos AT usados pela aplicação Java:

### Sistema
```
AT+VER                    # Ver firmware
AT+FACTORY_RESET          # Reset de fábrica
AT+FORMAT_MEM             # Formatar memória
```

### Intervalos
```
AT+SET_TIME_REPORT=<seg>  # Definir intervalo de telemetria
AT+GET_TIME_REPORT        # Consultar intervalo
```

### Bluetooth
```
AT+SET_NAME_BLE=<nome>    # Definir nome Bluetooth
AT+GET_NAME_BLE           # Consultar nome
```

### Horímetro
```
AT+GET_HOURS_NOW          # Horas atuais
AT+SET_HOURS_INIT=<valor> # Definir valor inicial
AT+GET_HOURS_INIT         # Consultar valor inicial
```

### WiFi/Server
```
AT+SET_WIFI_SSID_01=<ssid>       # Definir SSID rede 1
AT+SET_WIFI_PASS_01=<senha>      # Definir senha rede 1
AT+SET_SERVER_IP_01=<ip>         # Definir IP servidor 1
AT+SET_SERVER_PORT_01=<porta>    # Definir porta servidor 1
```

---

## Segurança e Boas Práticas

### ⚠️ Cuidados ao Configurar

1. **Não fazer Factory Reset sem backup**: Todas as configurações serão perdidas
2. **Não desconectar durante atualização de firmware**: Pode danificar o dispositivo
3. **Anotar configurações**: Manter registro das configurações aplicadas
4. **Testar após configurar**: Sempre validar no app Flutter após alterações

### ✅ Recomendações

1. **Manter firmware atualizado**: Verificar atualizações periodicamente
2. **Nomear dispositivos**: Usar nomes descritivos (placa do veículo, código)
3. **Ajustar intervalos**: Balancear tempo real vs duração da bateria
4. **Configurar horímetros corretamente**: Essencial para manutenção preventiva

---

## Suporte e Troubleshooting

### Dispositivo não conecta
- Verificar se Bluetooth está ativado
- Verificar se dispositivo está pareado
- Tentar "Esquecê-lo" e parear novamente
- Verificar TIME MAX AUTH (pode estar muito curto)

### Dados não aparecem no app Flutter
- Verificar TIME REPORT (pode estar com intervalo muito longo)
- Verificar TIME RCV ACK (dispositivo pode estar esperando confirmação)
- Reconectar dispositivo
- Verificar logs no app Flutter

### Firmware não atualiza
- Verificar se arquivo está correto (.bin/.hex)
- Garantir conexão estável durante processo
- Não desconectar cabos ou fechar aplicação
- Se falhar, tentar novamente

### Horímetro zerou
- Pode ter ocorrido FORMAT MEM ou FACTORY RESET
- Reconfigurar com SET HOURS INIT
- Implementar backup periódico das configurações

---

## Conclusão

A aplicação Java é uma ferramenta **essencial para configuração inicial** do dispositivo BLUE_1487. Após a configuração correta, o **app Flutter** é usado no dia a dia para monitoramento em tempo real.

Para uso profissional (gestão de frota), considere também configurar o **modo WiFi + Servidor** para centralizar dados de múltiplos veículos em um dashboard web.
