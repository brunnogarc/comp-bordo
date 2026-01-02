# BLUE TELEMATICS (WIFI e BLE): Protocolos e Comandos
**VERSÃO 3.0.0 – Piraju, 29 de Maio de 2025**

**Departamento de Engenharia**

## Sumário (por páginas)

- [Página 1](#página-01)
- [Página 2](#página-02)
- [Página 3](#página-03)
- [Página 4](#página-04)
- [Página 5](#página-05)
- [Página 6](#página-06)
- [Página 7](#página-07)
- [Página 8](#página-08)
- [Página 9](#página-09)
- [Página 10](#página-10)
- [Página 11](#página-11)
- [Página 12](#página-12)
- [Página 13](#página-13)
- [Página 14](#página-14)
- [Página 15](#página-15)
- [Página 16](#página-16)
- [Página 17](#página-17)
- [Página 18](#página-18)
- [Página 19](#página-19)
- [Página 20](#página-20)
- [Página 21](#página-21)
- [Página 22](#página-22)
- [Página 23](#página-23)
- [Página 24](#página-24)
- [Página 25](#página-25)
- [Página 26](#página-26)
- [Página 27](#página-27)

---

## Página 01

### Texto extraído

```text
(WIFI e BLE)
Protocolos e Comandos
```

### Imagem da página (layout original)

![Página 01](images/page_01.png)


---

## Página 02

### Texto extraído

```text
Sumário
1.
Introdução ...........................................................................................................................................3
2.
Descrição de ligação ............................................................................................................................3
3.
Descrição de LED .................................................................................................................................3
4.
Protocolo de comunicação (Bluetooth) ..............................................................................................4
5.
Lista de Comandos de Configuração da Interface (Bluetooth e WIFI)............................................ 18
6.
Atualização da interface (OTA - Bluetooth) .................................................................................... 25
7.
Histórico de Versionamento (Release) ............................................................................................ 26
```

### Imagem da página (layout original)

![Página 02](images/page_02.png)


---

## Página 03

### Texto extraído

```text
1. Introdução
O objetivo deste documento é descrever os parâmetros de configuração de aplicação, assim como
também a descrição dos protocolos de comunicação utilizado pelo mesmo para enviar e receber dados
através do rádio Bluetooth Classic com perfil SPP (Serial Port Profile).
2. Descrição de ligação
O equipamento utilizado, deve ser instalado afim de coletar informações de utilização do veículo,
levando em conta os sensores que o próprio equipamento possui.
Abaixo segue tabela de descrição de conexões:
Fio
Descrição
Vermelho
Alimentação Bateria (L30)
Branco
Alimentação pós-chave (L15)
Preto
Terra (GND)
Azul
Sensor RPM (Pino W alternador)
Equipamento com comunicação RS232
Fio
Descrição
Preto
Terra (GND)
Laranja
RX (Entrada de dados)
Roxo
TX (Saída de dados)
3. Descrição de LED
O LED da interface pode fornecer algumas informações sobre o comportamento da mesma de
maneira visual, onde sua descrição segue abaixo:
Cor
Status Interface
Descrição
Piscando – Vermelho (1Hz)
Stand-by
Mensagens armazenadas,
nenhum dispositivo pareado.
Piscando – Verde (1Hz)
Stand-by
Mensagens armazenadas,
dispositivo pareado
(Descarregando).
Desligado
Stand-by
Nenhuma mensagem
armazenada.
Fixo – Vermelho
Pós-chave ligado
Identificação de pós-chave
Piscando – Verde (1Hz)
Pós-chave ligado
Recebendo dados CAN
Fixo – Verde
Motor Ligado
Identificação de motor ligado
Piscando – Azul
Pós-chave/Motor
Geração de mensagem
Alternando – Vermelho, Verde,
Azul (1Hz)
Atualização
Interface está atualizando
```

### Imagem da página (layout original)

![Página 03](images/page_03.png)


---

## Página 04

### Texto extraído

```text
4. Protocolo de comunicação (Bluetooth)
Após dispositivo realizar o pareamento com a interface através do bluetooth, utilizando o perfil
SPP, o mesmo pode interagir com a interface através de comandos, assim como também receber
automaticamente os dados armazenados em memória, este último desde que ocorra a
identificação do usuário através de um código de 8 bytes e seja informado que o dispositivo deve
disparar as mensagens criadas ou armazenadas. No total a interface pode ter até 2 dispositivos
pareados e autenticados simultaneamente, porem somente 1 dispositivo pode identificar o código
de usuário por vez.
Obs.: TODOS OS COMANDOS NECESSITAM TER NO FINAL QUEBRA DE LINHA E RETORNO DE
CARRINHO (CR + LF “\r\n”).
4.1. Autenticação
Assim que o dispositivo realiza o pareamento com a interface, a mesma irá informar uma chave
aleatória e randômica de 8 bytes que deve ser utilizada como referência para a geração de uma contra
chave, usando um cálculo e chave de empresa especifico, onde sua descrição segue abaixo.
4.1.1. Comando
Comando
Condição
Descrição
AT+BT_SEED=<value>\r\n
Ao parear com dispositivo
Informa seed para calculo de contra
chave.
AT+BT_AUTH=<value>\r\n
Dispositivo deve enviar comando
Dispositivo pareado deve enviar
resposta para realizar autenticação
Exemplo:
AT+BT_SEED=8DDDD953E0626F92
AT+BT_AUTH=30469DD346B190C3
Respostas esperadas após envio do comando:
AT+BT_AUTH_OK
Mensagem informa que o dispositivo foi autenticado com sucesso.
AT+BT_AUTH_LEN_FAIL
Mensagem informa que a contra chave informada não possui o comprimento correto.
AT+BT_AUTH_DATA_FAIL
Mensagem informa que a contra chave informada está errada.
Caso o dispositivo não consiga se autenticar em tempo hábil ou com sucesso, o mesmo terá sua
conexão encerrada com a interface e o processo deve ser reiniciado.
4.1.1.1.
Calculo para autenticação
Segue descrição de cálculo para realização de autenticação:
CHAVE DA EMPRESA COMPOSTA POR 4 BYTES
```

### Imagem da página (layout original)

![Página 04](images/page_04.png)


---

## Página 05

### Texto extraído

```text
KEY_AUTH [Byte 0] = KEY_SEED[4] XOR KEY_COMPANY[3]
KEY_AUTH [Byte 1] = KEY_COMPANY[1]
KEY_AUTH [Byte 2] = (KEY_AUTH[0] + KEY_COMPANY[2]) AND 0xFF
KEY_AUTH [Byte 3] = (KEY_AUTH[1] + KEY_SEED[0]) AND 0xFF
KEY_AUTH [Byte 4] =  KEY_AUTH[2] XOR KEY_COMPANY[0]
KEY_AUTH [Byte 5] =   KEY_SEED[5] XOR KEY_AUTH[3]
KEY_AUTH [Byte 6] =   KEY_SEED[7] AND KEY_AUTH[2]
KEY_AUTH [Byte 7] =   KEY_SEED[3] XOR KEY_AUTH[6]
Exemplo em JAVA:
```

### Imagem da página (layout original)

![Página 05](images/page_05.png)


---

## Página 06

### Texto extraído

```text
4.2. Identificação Usuário
Após realizar pareamento e autenticação com a interface, o dispositivo deve informar um código de
usuário de até 8 Bytes, onde o mesmo passa a ser o dispositivo principal para recepção de frames de
dados armazenados ou gerados pela interface. Todos os novos frames que forem gerados pela
interface, devem conter o código de identificação do usuário informado, até que o dispositivo seja
desconectado.
4.2.1. Diagrama
```

### Imagem da página (layout original)

![Página 06](images/page_06.png)


---

## Página 07

### Texto extraído

```text
4.2.2. Comando
Comando
Condição
Descrição
AT+BT_COD_USER=<value>\r\n Dispositivo envia Codigo do
usuario em Hex
Informa a interface qual o código
do usuário autenticado e principal
para comunicação e geração de
frame de dados
Exemplo:
AT+BT_COD_USER =0000000000000001
Respostas esperadas após envio do comando:
AT+BT_COD_USER_OK=<value>
Mensagem relata que o usuário foi informado com sucesso e em “<value>” retorna o código
recebido e atribuído como principal (Nesse momento do dispositivo ainda nãe deve receber os
frames armazenados e gerados pela interface, pois é necessário enviar o comando
AT_BT_DATA_START).
AT+BT_COD_USER_LEN_FAIL
Mensagem informa que o código de identificação informado não possui o comprimento correto
(Total de 8 Bytes).
AT+BT_COD_USER_BUSY=<value>
Mensagem informa que já existe um outro dispositivo autenticado e com código de usuário
informado como principal, onde em “<value>” consta o código do usuário já informado de outro
dispositivo.
4.3. Comandos para recepção de frame de dados e real time (somente BLUETOOTH)
Após realizar a autenticação e se identificar pelo código de usuário, a interface necessita de 2
comandos para que possa disponibilizar as informações armazenadas e de tempo real.
4.3.1. Comando para recepção de frame de dados
Para que seja disponibilizado os frames de dados armazenados em memoria ou criados
recentemente, deve ser solicitado ao dispositivo que os dados sejam liberados através dos
comandos a seguir.
•
AT_BT_DATA_START
Comando enviado para a interface iniciar a externalização dos frames de dados
•
AT_BT_DATA_STOP
Comando enviado para a interface parar a externalização dos frames de dados
4.3.2. Comando para recepção de frame real time
Para que seja disponibilizado os dados de tempo real (Envio a cada 1 segundo), deve ser solicitado
ao dispositivo que os dados sejam liberados através dos comandos a seguir.
•
AT_BT_PRM_START
Comando enviado para a interface iniciar a externalização dos frames de dados
```

### Imagem da página (layout original)

![Página 07](images/page_07.png)


---

## Página 08

### Texto extraído

```text
•
AT_BT_PRM_STOP
Comando enviado para a interface parar a externalização dos frames de dados
4.4. Frame de dados
Depois que o dispositivo estiver pareado e autenticado na interface e ter enviado o comando para disparo dos frames de dados, o mesmo deve receber
automaticamente as informações armazenadas na memória ou se o mesmo estiver sincronizado deve receber as mensagens que forem criadas.
4.4.1. Interpretação Mensagem formato HEX
Os dados devem ser interpretados em formato HEX de acordo com informações abaixo, levando em consideração a versão do protocolo da
mensagem
4.4.1.1. VERSÃO DO PROTOCOLO 01 (Acima da versão de firmware V3.0.0)
EXEMPLO VERSÃO DO PROTOCOLO 02 E TIPO DE CARGA AGRO:
AT+BT_DATA= 00000001 12 11 11 0001 07 08 FF 5FB27B4F F22C0362 E291AEB5 015C 00F5 00000001 00000002 00000003 0004 1770 05 06 07 08 09 0A 0B 0C
000D 0E 0F 10 0011 12 13 0014 15 16 17 18 19 20 21 22 23 0024 00000025 00000026 0027 0028 0029 0030 0000000000000031
0000000000000001 AD
Frame
Descrição
Conversão
AT+BT_DATA=
IDENTIFICAÇÃO DE FRAME DE DADOS
00000001
SERIAL DA INTERFACE
CONVERTER PARA DECIMAL
12
11
TIPO MENSAGEM
TIPO MENSAGEM (0b11110000) -> 1- LAERTA 2 – REPORT 3 – KEEP ALIVE
11
TIPO CARGA
ID CARGA
TIPO CARGA (0b11000000) -> 1- AGRO 2- DIESEL 3- OUTROS
ID CARGA (0b00111111)
22
IDENTIFICADOR DO ALERTA
*DESCRIÇÃO 01
33
RESERVADO
RESERVADO
0001
QUANTIDADE DE DADOS ARMAZENADO
VALOR = QUANTIDADE DE DADOS ARMAZENADOS NA MEMORIA DA INTERFACE
07
ESTADO PÓS CHAVE (L15)
ESTADO MOTOR LIGADO
ESTADO GPS
PÓS-CHAVE (0b00000001) –> 1 - LIGADO | 0 - DESLIGADO
MOTOR (0b00000010) –> 1 - LIGADO | 0 – DESLIGADO
GPS (0b00000100) -> 1 – VALIDO | 0 - INVALIDO
5FB27B4F
TIMESTAMP
UTC UNIX (DESDE 01/01/1970) -> VALOR EM SEGUNDOS
```

### Imagem da página (layout original)

![Página 08](images/page_08.png)


---

## Página 09

### Texto extraído

```text
F22C0362
LATITUDE
(VALOR – 0xFFFFFFFF) * 10^-7 EX: F22C0362 -> -23.1996573
E291AEB5
LONGITUDE
(VALOR – 0xFFFFFFFF) * 10^-7 EX: E291AEB5 -> -49.3769035
05
TIPO DO FIX
TIPO: 0 – NA | 1 – SEM FIX | 2 – 2D | 3 – 3D
0006
HDOP
VALOR = (VALOR * 0.01)
07
QUANTIDADE SATÉLITES
VALOR = QUANTIDADE SATÉLITES EM USO
015C
BUSSOLA
VALOR = 348°GRAUS
08
CONEXAO
VALOR: 0 – NA | 1 – LTE | 2 – WIFI | 3 - LORA
09
QUALIDADE SINAL
VALOR = dBm (convertido para positivo)
00F5
TENSÃO BATERIA
VALOR * 0.05 = VOLTS -> EX: 00F5 -> 245 * 0.05 = 12,25V
00000001
TOTAL DE HORAS DE FUNCIONAMENTO
DO MOTOR
VALOR * 0.05 (EXIBIR EM HORAS)
RESOLUÇÃO -> 0.05 EQUIVALE A 3 MINUTOS
EX: 12.45 -> 12 HORAS E 27 MINUTOS
00000002
TOTAL DE COMBUSTIVEL UTILIZADO PELO
MOTOR
VALOR * 0.5 (EXIBIR EM LITROS)
00000003
ODOMETRO
VALOR * 0.125 = Km
0004
VELOCIDADE DO VEICULO
VALOR  * 0.1 = Km/h
1770
ROTAÇÃO DO MOTOR
VALOR * 0.125 = RPM
05
PEDAL ACELERADOR
VALOR * 0.4 = %
06
TORQUE DO MOTOR
VALOR – 125 = %
07
CARGA DO MOTOR
VALOR * 0.4 = %
08
PRESSÃO DO TURBO
VALOR * 0.05 = PSI
09
PRESSÃO AR ADMISSÃO
VALOR * 0.05 = PSI
0A
PRESSÃO OLEO MOTOR
VALOR * 4 = KPA
0B
PRESSÃO OLEO TRANSMISSÃO
VALOR * 16 = KPA
0C
PRESSÃO DO COMBUSTIVEL
VALOR * 4 = KPA
000D
TEMP. OLEO DO MOTOR
VALOR * 0.03125 = °C
0E
TEMP. ÁGUA DO MOTOR
VALOR – 40 = °C
0F
TEMP. AR ADMISSÃO
VALOR – 40 = °C
10
TEMP. AR AMBIENTE
VALOR – 40 = °C
0011
TEMP. OLEO TRANSMISSÃO
(VALOR * 0.03125) – 273 = °C
12
TEMP. FLUIDO HIDRAULICO
VALOR – 40 = °C
13
TEMP. COMBUSTIVEL
VALOR – 40 = °C
0014
VAZÃO COMBUSTIVEL
VALOR * 0.05 = L/h
15
NIVEL COMBUSTIVEL
VALOR * 0.4 = %
16
NIVEL OLEO TRANSMISSÃO
VALOR * 0.4 = %
17
NIVEL FLUIDO HIDRAULICO
VALOR * 0.4 = %
```

### Imagem da página (layout original)

![Página 09](images/page_09.png)


---

## Página 10

### Texto extraído

```text
18
STATUS CODIGO DE FALHA
STATUS HELICE RADIADOR
STATUS ESTEIRA ELEVADOR (CANA)
STATUS CORTE DE BASE (CANA)
(0b00000011) -> 00 AUSENTE 01 PRESENTE 10 ERRO 11 NÃO AVALIADO
(0b00001100) -> 00 DESLIGADO 01 LIGADO 10 ERRO 11 NÃO AVALIADO
(0b00110000) -> 00 DESLIGADO 01 LIGADO 10 ERRO 11 NÃO AVALIADO
(0b11000000) -> 00 DESLIGADO 01 LIGADO 10 ERRO 11 NÃO AVALIADO
19
ALTURA DO IMPLEMENTO
VALOR * 0.05 = %
20
VEL. UNIDADE COLHEITA
VALOR = RPM
21
STATUS TOMADA DE FORÇA
STATUS PILOTO AUTOMATICO
STATUS INDUSTRIA
STATUS DESCARGA GRÃOS
(0b00000011) -> 00 DESLIGADO 01 LIGADO 10 ERRO 11 NÃO AVALIADO
(0b00001100) -> 00 DESLIGADO 01 LIGADO 10 ERRO 11 NÃO AVALIADO
(0b00110000) -> 00 DESLIGADO 01 LIGADO 10 ERRO 11 NÃO AVALIADO
(0b11000000) -> 00 DESLIGADO 01 LIGADO 10 ERRO 11 NÃO AVALIADO
22
STATUS UNIDADE COLHEITA
STATUS PLATAFORMA (ALGODÃO)
STATUS EMBALAR (ALGODÃO)
STATUS BOMBA D’ÁGUA
(0b00000011) -> 00 DESLIGADO 01 LIGADO 10 ERRO 11 NÃO AVALIADO
(0b00001100) -> 00 DESLIGADO 01 ABAIXANDO 10 SUBINDO 11 NÃO AVALIADO
(0b00110000) -> 00 DESLIGADO 01 LIGADO 10 ERRO 11 NÃO AVALIADO
(0b11000000) -> 00 DESLIGADO 01 LIGADO 10 ERRO 11 NÃO AVALIADO
23
TAXA VOL. APLICAÇÃO POR HA
STATUS LIBERAÇÃO INSUMO
STATUS EXTRATOR PRIMARIO (CANA)
STATUS EXTRATOR SECUNDARIO (CANA)
(0b00000011) -> 00 MODO MANUAL | 01 TAXA1 | 10 TAXA2 | 11 NÃO AVALIADO
(0b00001100) -> 00 FECHADO 01 ABERTO 10 ERRO 11 NÃO AVALIADO
(0b00110000) -> 00 DESLIGADO 01 LIGADO 10 ERRO 11 NÃO AVALIADO
(0b11000000) -> 00 DESLIGADO 01 LIGADO 10 ERRO 11 NÃO AVALIADO
0102
STATUS SEÇÕES DE PULVERIZAÇÃO
*DESCRIÇÃO 03
00000025
HORIMETRO ESTEIRA ELEVADOR (CANA)
VALOR * 0.05 (EXIBIR EM HORAS)
RESOLUÇÃO -> 0.05 EQUIVALE A 3 MINUTOS
EX: 12.45 -> 12 HORAS E 27 MINUTOS
00000026
HORIMETRO PILOTO AUTOMATICO
VALOR * 0.05 (EXIBIR EM HORAS)
RESOLUÇÃO -> 0.05 EQUIVALE A 3 MINUTOS
EX: 12.45 -> 12 HORAS E 27 MINUTOS
0027
PRESSÃO CORTE DE BASE (CANA)
VALOR * 0.05 = BAR
0028
PRESSÃO DO PICADOR (CANA)
VALOR * 0.05 = BAR
0029
ALTURA CORTE DE BASE (CANA)
VALOR = ALTURA
0030
VELOCIDADE EXTRATOR PRIMARIO
VALOR * 0.125 = RPM
1122
EXPANSÃO SEÇÕES DE PULVERIZACAO
*DESCRIÇÃO 04
0000000000000031
RESERVADO PARA NOVOS DADOS
RESERVADO
0000000000000001
COD. USUÁRIO
VALOR EM HEX
AE
CHECKSUM
SOMATORIA DE TODOS OS BYTES DA MENSAGEM (0xFF)
Obs.: DESTAQUE EM VERMELHO, SIGINIFICA QUE A INFORMAÇÃO FOI ADICIONADA NESTA VERSÃO DO PROTOCOLO
```

### Imagem da página (layout original)

![Página 10](images/page_10.png)


---

## Página 11

### Texto extraído

```text
•
Descrição 01
Mensagens de alerta serão gerados, caso algum dos parâmetros da tabela abaixo alterar seu valor.
Seu identificador estará descriminado no frame de dados em seu campo em especifico.
Identificador
Nome do parâmetro
0
Não identificado
1
Status Bússola
2
Status pós-chave (L15)
3
Status Motor
4
Status Velocidade
5
Gerado por tempo
255
CPU reiniciado
•
Descrição 02
Descrição da qualidade do sinal de conexão
Valor
Interpretação
> 90 dBm
Sem Sinal
81 a 90 dBm
Muito Fraco
71 a 80 dBm
Fraco
61 a 70 dBm
Bom
51 a 60 dBm
Muito bom
≤ 50 dBm
Excelente
```

### Imagem da página (layout original)

![Página 11](images/page_11.png)


---

## Página 12

### Texto extraído

```text
•
Descrição 03
Byte 1 (01)
Byte 2 (02)
(0b00000011) -> BICO CENTRAL
00 FECHADO
01 ABERTO
10 ERRO
11 NÃO AVALIADO
(0b00000011) -> BICO DIREITO 01
00 FECHADO
01 ABERTO
10 ERRO
11 NÃO AVALIADO
(0b00001100) -> BICO ESQUERDO 01
00 FECHADO
01 ABERTO
10 ERRO
11 NÃO AVALIADO
(0b00001100) -> BICO DIREITO 02
00 FECHADO
01 ABERTO
10 ERRO
11 NÃO AVALIADO
(0b00110000) -> BICO ESQUERDO 02
00 FECHADO
01 ABERTO
10 ERRO
11 NÃO AVALIADO
(0b00110000) -> BICO DIREITO 03
00 FECHADO
01 ABERTO
10 ERRO
11 NÃO AVALIADO
(0b11000000) -> BICO ESQUERDO 03
00 FECHADO
01 ABERTO
10 ERRO
11 NÃO AVALIADO
(0b11000000) -> PORTA DIGITAL
2
00 FECHADO
01 ABERTO
10 ERRO
11 NÃO AVALIADO
•
Descrição 04
Byte 1 (01)
Byte 2 (02)
(0b00000011) -> BICO ESQUERDO 04
00 FECHADO
01 ABERTO
10 ERRO
11 NÃO AVALIADO
(0b00000011) -> RESERVADO
RESERVADO
(0b00001100) -> BICO DIREITO 04
00 FECHADO
01 ABERTO
10 ERRO
11 NÃO AVALIADO
(0b00001100) -> RESERVADO
RESERVADO
(0b00110000) -> RESERVADO
RESERVADO
(0b00110000) -> RESERVADO
RESERVADO
(0b11000000) -> RESERVADO
RESERVADO
(0b11000000) -> RESERVADO
RESERVADO
```

### Imagem da página (layout original)

![Página 12](images/page_12.png)


---

## Página 13

### Texto extraído

```text
Após a interface enviar um frame de dado, o dispositivo pareado deve enviar uma mensagem de confirmação de recebimento, caso o mesmo não
seja enviado, a mensagem continuará sendo armazenada na memória interna da interface.
Exemplo:
AT+BT_DATA=00000001111111000107FFFF5FB27B4FF22C0362E291AEB5015C00F5......21300415161718192021222300240000000000000001AD
AT+BT_DATA_OK (MENSAGEM DE CONFIRMAÇÃO ENVIADO DO DISPOSITVO PAREADO)
4.5. Frame de tempo real
Depois que o dispositivo estiver pareado e autenticado na interface e ter enviado o comando para disparo de frames de tempo real, o mesmo deve
receber automaticamente as informações.
4.5.1. Interpretação Mensagem formato HEX
Os dados devem ser interpretados em formato HEX de acordo com informações abaixo
4.5.1.1. VERSÃO DO PROTOCOLO 01 (Até versão de firmware V3.0.0)
EXEMPLO:
AT+BT_PRM= 12 07 F22C0362 E291AEB5 015C 00F5 00000001 00000002 00000003 0004 1770 05 06 07 08 09 0A 0B 0C 000D 0E 0F 10 0011 12 13 0014 15 16
17 18 19 20 21 22 23 0024 00000025 00000026 0027 0028 0029 0030 0000000000000031 AD
Frame
Descrição
Conversão
AT+BT_DATA=
IDENTIFICAÇÃO DE FRAME DE DADOS
12
33
RESERVADO
RESERVADO
07
ESTADO PÓS CHAVE (L15)
PÓS-CHAVE (0b00000001) –> 1 - LIGADO | 0 - DESLIGADO
```

### Imagem da página (layout original)

![Página 13](images/page_13.png)


---

## Página 14

### Texto extraído

```text
ESTADO MOTOR LIGADO
ESTADO GPS
MOTOR (0b00000010) –> 1 - LIGADO | 0 – DESLIGADO
GPS (0b00000100) -> 1 – VALIDO | 0 - INVALIDO
00000000
RESERVADO
RESERVADO
F22C0362
LATITUDE
(VALOR – 0xFFFFFFFF) * 10^-7 EX: F22C0362 -> -23.1996573
E291AEB5
LONGITUDE
(VALOR – 0xFFFFFFFF) * 10^-7 EX: E291AEB5 -> -49.3769035
05
TIPO DO FIX
TIPO: 0 – NA | 1 – SEM FIX | 2 – 2D | 3 – 3D
0006
HDOP
VALOR = (VALOR * 0.01)
07
QUANTIDADE SATÉLITES
VALOR = QUANTIDADE SATÉLITES EM USO
015C
BUSSOLA
VALOR = 348°GRAUS
08
CONEXAO
VALOR: 0 – NA | 1 – LTE | 2 – WIFI | 3 - LORA
09
QUALIDADE SINAL
VALOR = dBm (convertido para positivo)
00F5
TENSÃO BATERIA
VALOR * 0.05 = VOLTS -> EX: 00F5 -> 245 * 0.05 = 12,25V
00000001
TOTAL DE HORAS DE FUNCIONAMENTO
DO MOTOR
VALOR * 0.05 (EXIBIR EM HORAS)
RESOLUÇÃO -> 0.05 EQUIVALE A 3 MINUTOS
EX: 12.45 -> 12 HORAS E 27 MINUTOS
00000002
TOTAL DE COMBUSTIVEL UTILIZADO PELO
MOTOR
VALOR * 0.5 (EXIBIR EM LITROS)
00000003
ODOMETRO
VALOR * 0.125 = Km
0004
VELOCIDADE DO VEICULO
VALOR  * 0.1 = Km/h
1770
ROTAÇÃO DO MOTOR
VALOR * 0.125 = RPM
05
PEDAL ACELERADOR
VALOR * 0.4 = %
06
TORQUE DO MOTOR
VALOR – 125 = %
07
CARGA DO MOTOR
VALOR * 0.4 = %
08
PRESSÃO DO TURBO
VALOR * 0.05 = PSI
09
PRESSÃO AR ADMISSÃO
VALOR * 0.05 = PSI
0A
PRESSÃO OLEO MOTOR
VALOR * 4 = KPA
0B
PRESSÃO OLEO TRANSMISSÃO
VALOR * 16 = KPA
0C
PRESSÃO DO COMBUSTIVEL
VALOR * 4 = KPA
000D
TEMP. OLEO DO MOTOR
VALOR * 0.03125 = °C
0E
TEMP. ÁGUA DO MOTOR
VALOR – 40 = °C
0F
TEMP. AR ADMISSÃO
VALOR – 40 = °C
10
TEMP. AR AMBIENTE
VALOR – 40 = °C
0011
TEMP. OLEO TRANSMISSÃO
(VALOR * 0.03125) – 273 = °C
12
TEMP. FLUIDO HIDRAULICO
VALOR – 40 = °C
13
TEMP. COMBUSTIVEL
VALOR – 40 = °C
0014
VAZÃO COMBUSTIVEL
VALOR * 0.05 = L/h
```

### Imagem da página (layout original)

![Página 14](images/page_14.png)


---

## Página 15

### Texto extraído

```text
15
NIVEL COMBUSTIVEL
VALOR * 0.4 = %
16
NIVEL OLEO TRANSMISSÃO
VALOR * 0.4 = %
17
NIVEL FLUIDO HIDRAULICO
VALOR * 0.4 = %
18
STATUS CODIGO DE FALHA
STATUS HELICE RADIADOR
STATUS ESTEIRA ELEVADOR (CANA)
STATUS CORTE DE BASE (CANA)
(0b00000011) -> 00 AUSENTE 01 PRESENTE 10 ERRO 11 NÃO AVALIADO
(0b00001100) -> 00 DESLIGADO 01 LIGADO 10 ERRO 11 NÃO AVALIADO
(0b00110000) -> 00 DESLIGADO 01 LIGADO 10 ERRO 11 NÃO AVALIADO
(0b11000000) -> 00 DESLIGADO 01 LIGADO 10 ERRO 11 NÃO AVALIADO
19
ALTURA DO IMPLEMENTO
VALOR * 0.05 = %
20
VEL. UNIDADE COLHEITA
VALOR = RPM
21
STATUS TOMADA DE FORÇA
STATUS PILOTO AUTOMATICO
STATUS INDUSTRIA
STATUS DESCARGA GRÃOS
(0b00000011) -> 00 DESLIGADO 01 LIGADO 10 ERRO 11 NÃO AVALIADO
(0b00001100) -> 00 DESLIGADO 01 LIGADO 10 ERRO 11 NÃO AVALIADO
(0b00110000) -> 00 DESLIGADO 01 LIGADO 10 ERRO 11 NÃO AVALIADO
(0b11000000) -> 00 DESLIGADO 01 LIGADO 10 ERRO 11 NÃO AVALIADO
22
STATUS UNIDADE COLHEITA
STATUS PLATAFORMA (ALGODÃO)
STATUS EMBALAR (ALGODÃO)
STATUS BOMBA D’ÁGUA
(0b00000011) -> 00 DESLIGADO 01 LIGADO 10 ERRO 11 NÃO AVALIADO
(0b00001100) -> 00 DESLIGADO 01 ABAIXANDO 10 SUBINDO 11 NÃO AVALIADO
(0b00110000) -> 00 DESLIGADO 01 LIGADO 10 ERRO 11 NÃO AVALIADO
(0b11000000) -> 00 DESLIGADO 01 LIGADO 10 ERRO 11 NÃO AVALIADO
23
TAXA VOL. APLICAÇÃO POR HA
STATUS LIBERAÇÃO INSUMO
STATUS EXTRATOR PRIMARIO (CANA)
STATUS EXTRATOR SECUNDARIO (CANA)
(0b00000011) -> 00 MODO MANUAL | 01 TAXA1 | 10 TAXA2 | 11 NÃO AVALIADO
(0b00001100) -> 00 FECHADO 01 ABERTO 10 ERRO 11 NÃO AVALIADO
(0b00110000) -> 00 DESLIGADO 01 LIGADO 10 ERRO 11 NÃO AVALIADO
(0b11000000) -> 00 DESLIGADO 01 LIGADO 10 ERRO 11 NÃO AVALIADO
0102
STATUS SEÇÕES DE PULVERIZAÇÃO
*DESCRIÇÃO 02
00000025
HORIMETRO ESTEIRA ELEVADOR (CANA)
VALOR * 0.05 (EXIBIR EM HORAS)
RESOLUÇÃO -> 0.05 EQUIVALE A 3 MINUTOS
EX: 12.45 -> 12 HORAS E 27 MINUTOS
00000026
HORIMETRO PILOTO AUTOMATICO
VALOR * 0.05 (EXIBIR EM HORAS)
RESOLUÇÃO -> 0.05 EQUIVALE A 3 MINUTOS
EX: 12.45 -> 12 HORAS E 27 MINUTOS
0027
PRESSÃO CORTE DE BASE (CANA)
VALOR * 0.05 = BAR
0028
PRESSÃO DO PICADOR (CANA)
VALOR * 0.05 = BAR
0029
ALTURA CORTE DE BASE (CANA)
VALOR = ALTURA
0030
VELOCIDADE EXTRATOR PRIMARIO
VALOR * 0.125 = RPM
1122
EXPANSÃO SEÇÕES DE PULVERIZAÇÃO
*DESCRIÇÃO 03
0000000000000031
RESERVADO PARA NOVOS DADOS
RESERVADO
AE
CHECKSUM
SOMATORIA DE TODOS OS BYTES DA MENSAGEM (0xFF)
Obs.: DESTAQUE EM VERMELHO, SIGINIFICA QUE A INFORMAÇÃO FOI ADICIONADA NESTA VERSÃO DO PROTOCOLO
```

### Imagem da página (layout original)

![Página 15](images/page_15.png)


---

## Página 16

### Texto extraído

```text
•
Descrição 01
Descrição da qualidade do sinal de conexão
Valor
Interpretação
> 90 dBm
Sem Sinal
81 a 90 dBm
Muito Fraco
71 a 80 dBm
Fraco
61 a 70 dBm
Bom
51 a 60 dBm
Muito bom
≤ 50 dBm
Excelente
•
Descrição 02
Byte 1 (01)
Byte 2 (02)
(0b00000011) -> BICO CENTRAL
00 FECHADO
01 ABERTO
10 ERRO
11 NÃO AVALIADO
(0b00000011) -> BICO DIREITO 01
00 FECHADO
01 ABERTO
10 ERRO
11 NÃO AVALIADO
(0b00001100) -> BICO ESQUERDO 01
00 FECHADO
01 ABERTO
10 ERRO
11 NÃO AVALIADO
(0b00001100) -> BICO DIREITO 02
00 FECHADO
01 ABERTO
10 ERRO
11 NÃO AVALIADO
(0b00110000) -> BICO ESQUERDO 02
00 FECHADO
01 ABERTO
10 ERRO
11 NÃO AVALIADO
(0b00110000) -> BICO DIREITO 03
00 FECHADO
01 ABERTO
10 ERRO
11 NÃO AVALIADO
(0b11000000) -> BICO ESQUERDO 03
00 FECHADO
01 ABERTO
10 ERRO
11 NÃO AVALIADO
(0b11000000) -> PORTA DIGITAL
2
00 FECHADO
01 ABERTO
10 ERRO
11 NÃO AVALIADO
```

### Imagem da página (layout original)

![Página 16](images/page_16.png)


---

## Página 17

### Texto extraído

```text
•
Descrição 03
Byte 1 (01)
Byte 2 (02)
(0b00000011) -> BICO ESQUERDO 04
00 FECHADO
01 ABERTO
10 ERRO
11 NÃO AVALIADO
(0b00000011) -> RESERVADO
RESERVADO
(0b00001100) -> BICO DIREITO 04
00 FECHADO
01 ABERTO
10 ERRO
11 NÃO AVALIADO
(0b00001100) -> RESERVADO
RESERVADO
(0b00110000) -> RESERVADO
RESERVADO
(0b00110000) -> RESERVADO
RESERVADO
(0b11000000) -> RESERVADO
RESERVADO
(0b11000000) -> RESERVADO
RESERVADO
```

### Imagem da página (layout original)

![Página 17](images/page_17.png)


---

## Página 18

### Texto extraído

```text
5. Lista de Comandos de Configuração da Interface (Bluetooth e WIFI)
Os comandos só podem ser interpretados pela interface mediante autenticação no momento do
pareamento com o dispositivo
Obs.: TODOS OS COMANDOS NECESSITAM TER NO FINAL QUEBRA DE LINHA E RETORNO DE
CARRINHO (CR + LF “\r\n”).
•
AT+BT_SEED=<value>
Interface informa chave para cálculo de autenticação
•
AT+BT_AUTH=<value>
Dispositivo pareado informa chave de autenticação calculado
•
AT+BT_DATA=<value>
Interface envia frame de dados armazenados na memoria
•
AT+BT_DATA_OK
Dispositivo pareado informa que recebeu frame de dados com sucesso
•
AT+BT_CMD_ERROR
Informa que o equipamento não reconheceu o comando enviado
•
AT+BT_RST
Reinicia a interface
•
AT+BT_GET_SERIAL?
Retorna número de série da interface
•
AT+VERSION?
Retorna versão do firmware da interface
•
AT+BT_VALUES_FACTORY
Retorna todas as variáveis de configuração da interface para padrão de fabrica
•
AT+BT_FORMAT_STORAGE
Formata as mensagens salvas na memoria
•
AT+BT_SIMULATED_FRAME_ON
Coloca a interface em modo simulação, onde ao criar frame de dados os valores de parâmetros são
preenchidos para demonstração. (Obs.: Ao desligar a interface e alimentar novamente,
automaticamente a mesma sai do modo simulado).
•
AT+BT_SIMULATED_FRAME_OFF
Encerra o modo simulação da interface.
•
AT+BT_QTD_MSG_FLASH?
Retorna a quantidade de mensagens armazenadas na memória flash.
```

### Imagem da página (layout original)

![Página 18](images/page_18.png)


---

## Página 19

### Texto extraído

```text
•
AT+BT_REPORT_CYCLE=<value>
Modifica tempo PARA criação de nova mensagem de reporte quando o veículo estiver ligado (Ex.:
AT+REPORT_CYCLE=200) Segundos
•
AT+BT_REPORT_CYCLE?
Retorna o tempo para geração de mensagens de reporte.
•
AT+BT_REPORT_CYCLE_SPEED=<value>
Modifica tempo PARA criação de nova mensagem de reporte quando o veículo estiver ligado e
velocidade configurada atingida (Ex.: AT+REPORT_CYCLE_SPEED=200) Segundos
•
AT+BT_REPORT_CYCLE_SPEED?
Retorna o tempo para geração de mensagens de reporte quando velocidade é atingida.
•
AT+BT_TIME_MIN_SEND_MSG=<value>
Modifica
tempo
entre
envio
de
mensagem
de
dados
via
bluetooth
(Ex.:
AT+BT_TIME_MIN_SEND_MSG=1000) Milissegundos.
•
AT+BT_TIME_MIN_SEND_MSG?
Retorna o tempo configurado que deve ter entre o envio de mensagem de dados via bluetooth
•
AT+BT_MAX_TIME_AUTH=<value>
Modifica o tempo que a interface deve aguarda para o dispositivo pareado se autenticar.
•
AT+BT_MAX_TIME_AUTH?
Retorna o tempo que a interface deve aguarda para o dispositivo pareado se autenticar.
•
AT+BT_MAX_TIME_RCV_ACK=<value>
Modifica o tempo que a interface deve esperar reposta do dispositivo quando uma mensagem de
dados é enviada (Ex.: AT+BT_MAX_TIME_RCV_ACK=5000) Milissegundos.
•
AT+BT_MAX_TIME_RCV_ACK?
Retorna o tempo que a interface deve esperar reposta do dispositivo quando uma mensagem de
dados é enviada.
•
AT+BT_NAME_DEVICE=<value>
Modifica o nome atribuído ao bluetooth da interface (NECESSARIO REINICIAR A INTERFACE)
•
AT+BT_NAME_DEVICE?
Retorna o nome do atribuído ao bluetooth da interface.
•
AT+BT_ SPEED_CHANGE_CYCLE_REPORT=<value>
Modifica a velocidade a ser monitorado afim de trocar o tempo de ciclo de geração de nova
mensagem (Ex.: AT+BT_SPEED_CHANGE_CYCLE_REPORT=30) Km/h.
•
AT+BT_NAME_DEVICE?
```

### Imagem da página (layout original)

![Página 19](images/page_19.png)


---

## Página 20

### Texto extraído

```text
Retorna a velocidade a ser monitorado afim de trocar o tempo de ciclo de geração de nova
mensagem.
•
AT+BT_ENG_HOUR_INIT=<value>
Modifica o tempo de inicio de contabilização do Horímetro da interface (OFFSET) (Ex.:
AT+BT_ENG_HOUR_INIT=1000.20) Horas.
•
AT+BT_ENG_HOUR_INIT?
Retorna o tempo de início de contabilização do Horímetro da interface (OFFSET).
•
AT+BT_ENG_HOUR_COUNT=<value>
Modifica o tempo de contabilização do Horímetro da interface desde sua instalação (Ex.:
AT+BT_ENG_HOUR_COUNT=1000.10) Horas.
•
AT+BT_ENG_HOUR_COUNT?
Retorna o tempo de contabilização do Horímetro da interface desde sua instalação.
•
AT+BT_ENG_HOUR_NOW?
Retorna o Horímetro atual do motor (offset + tempo contabilizado)
•
AT+BT_ELEVADOR_HOUR_INIT=<value>
Modifica o tempo de inicio de contabilização do Horímetro do elevador da interface (OFFSET) (Ex.:
AT+BT_ELEVADOR_HOUR_INIT=1000.20) Horas.
•
AT+BT_ELEVADOR_HOUR_INIT?
Retorna o tempo de início de contabilização do Horímetro do elevador da interface (OFFSET).
•
AT+BT_ELEVADOR_HOUR_COUNT=<value>
Modifica o tempo de contabilização do Horímetro do elevador da interface desde sua instalação
(Ex.: AT+BT_ELEVADOR_HOUR_COUNT=1000.10) Horas.
•
AT+BT_ELEVADOR_HOUR_COUNT?
Retorna o tempo de contabilização do Horímetro do elevador da interface desde sua instalação.
•
AT+BT_ELEVADOR_HOUR_NOW?
Retorna o Horímetro atual do elevador (offset + tempo contabilizado)
•
AT+BT_PILOTO_HOUR_INIT=<value>
Modifica o tempo de inicio de contabilização do Horímetro do piloto automático da interface
(OFFSET) (Ex.: AT+BT_PILOTO_HOUR_INIT=1000.20) Horas.
•
AT+BT_PILOTO_HOUR_INIT?
Retorna o tempo de início de contabilização do Horímetro do piloto automático da interface
(OFFSET).
•
AT+BT_PILOTO_HOUR_COUNT=<value>
```

### Imagem da página (layout original)

![Página 20](images/page_20.png)


---

## Página 21

### Texto extraído

```text
Modifica o tempo de contabilização do Horímetro do piloto automático da interface desde sua
instalação (Ex.: AT+BT_PILOTO_HOUR_COUNT=1000.10) Horas.
•
AT+BT_PILOTO_HOUR_COUNT?
Retorna o tempo de contabilização do Horímetro do piloto automático da interface desde sua
instalação.
•
AT+BT_PILOTO_HOUR_NOW?
Retorna o Horímetro atual do piloto automático (offset + tempo contabilizado)
•
AT+BT_COMPASS=<value>
Modifica o ângulo da bussola a ser monitorado para geração de nova mensagem de reporte (Ex.:
AT+BT_COMPASS=45) Graus.
•
AT+BT_COMPASS?
Retorna o ângulo da bussola a ser monitorado para geração de nova mensagem de reporte.
•
AT+ BT_TIME_CHECK_COMPASS=<value>
Modifica o tempo de confirmação de identificação de trocar de orientação da bussola para geração
de nova mensagem de reporte (Ex.: AT+BT_TIME_CHECK_COMPASS=1) Segundos.
•
AT+ BT_TIME_CHECK_COMPASS=?
Retorna o tempo de confirmação de identificação de trocar de orientação da bussola para geração
de nova mensagem de reporte.
•
AT+ BT_SENSOR_RPM_ON
Comando informa a interface que deve ser utilizado a leitura do sinal de rotação do alternador
(pino W).
•
AT+ BT_SENSOR_RPM_OFF
Comando informa a interface que não deve ser utilizado a leitura do sinal de rotação do alternador
(pino W).
•
AT+BT_REAL_RPM=<value>
Informa a interface qual o valor da rotação atual do motor do veículo, este comando ajusta a
conversão para o valor lido pelo equipamento.
•
AT+BT_RPM_NOW?
Interface retorna a rotação atual do motor, onde a leitura foi realizada pela mesma.
•
AT_BT_DATA_START
Comando informe que a interface pode disparar as mensagens de frame de dados armazenadas ou
criadas recentemente.
•
AT_BT_DATA_STOP
Comando informe que a interface deve parar de disparar as mensagens de frame de dados
armazenadas ou criadas recentemente.
```

### Imagem da página (layout original)

![Página 21](images/page_21.png)


---

## Página 22

### Texto extraído

```text
•
AT_BT_PRM_START
Comando informa que a interface pode disparar mensagens de dados de tempo real.
•
AT_BT_PRM_STOP
Comando informa que a interface deve parar de disparar mensagens de dados de tempo real.
•
AT+BT_DISABLE_CREATE_REPORT
Comando configura a interface a não criar mais mensagens de report (se informado o equipamento
não irá mais criar mensagens de report e nem salvar na memória flash).
•
AT+ BT_ENABLE_CREATE_REPORT
Comando configura a interface a realizar a criação de mensagens de report e salvar na memoria
flash.
•
AT+BT_NO_COD_USER
Resposta informa que a mensagem não pode ser processada enquanto o não for informado o
código do usuário à interface.
•
AT+BT_QTD_MSG_FLASH?
Retorna a quantidade de mensagens que o dispositivo tem armazenado em sua memória.
•
AT+BT_REPORT_CYCLE_IG_OFF=<value>
Modifica tempo entre geração de mensagens quando a ignição do veículo estiver desligado (Ex.:
AT+BT_REPORT_CYCLE_IG_OFF=180) Segundos.
•
AT+BT_REPORT_CYCLE_IG_OFF?
Retorna o tempo entre geração de mensagens quando a ignição do veículo estiver desligado.
•
AT+BT_GET_BAUD_CAN
Comando retorna a configuração atual das porta de leitura CAN.
EX.: AT+BT_GET_BAUD_CAN: CAN1=2 | CAN2=2 (Ambas as portas estão na velocidade 250K)
•
AT+BT_SET_BAUD_CAN=<value1>,<value2>
Modifica a velocidade da porta CAN especificada
(EX.: AT+BT_SET_BAUD_CAN=1,2) -> Velocidade de 250Kbps na porta CAN 1
ID <value1>
Porta CAN
1
CAN1
2
CAN2
ID <value2>
Velocidade
1
125K
2
250K
3
500K
4
1M
```

### Imagem da página (layout original)

![Página 22](images/page_22.png)


---

## Página 23

### Texto extraído

```text
•
AT+BT_AUTO_BAUD_CAN
Comando solicita que o equipamento faça uma busca automática cada inicialização para tentar
encontrar a velocidade do barramento CAN instalado.
•
AT+BT_SEND_SERIAL=<value>
Envia os dados de <value>  do Bluetooth para a porta serial
•
AT+BT_SERIAL_SEND_OK
Dados bluetooth enviados a porta serial com sucesso
•
AT+BT_SERIAL_SEND_ERROR
Erro ao enviar dados do bluetooth para a porta serial
•
AT+BT_SEND_BLE=<value>
Solicita o envio de <value> para o dispositivo bluetooth conectado.
•
AT+BT_ERROR_SERIAL_SIZE
Informa que o tamanho dos dados enviados é maior que o esperado
•
AT+BT_NO_BLE_CONNECTED
Informa que não existe nenhum dispositivo conectado no bluetooth
•
AT+BT_RCV_SERIAL=<value>
Informa ao bluetooth que foi recebido dados da porta serial, onde <value> representa os dados
recebidos.
•
AT+BT_DISABLE_HOUR_CAN
Desabilita a coleta do horimetro do motor pela rede CAN
•
AT+BT_ENABLE_HOUR_CAN
Habilita a coleta do horimetro do motor pela rede CAN
•
AT+BT_SERIAL_BAUD?
Comando solicita que o equipamento retorne qual velocidade esta configurada a porta serial
•
AT+BT_SERIAL_BAUD=<value>
Comando altera a velocidade da porta serial do equipamento, após o envio do comando o
equipamento precisa ser reiniciado para aplicar as alterações
(Ex.: AT+BT_SERIAL_BAUD=115200)
•
AT+BT_STATUS_WIFI?
Retorna se comunicação WIFI está ativado ou desativado.
•
AT+BT_WIFI_ON
```

### Imagem da página (layout original)

![Página 23](images/page_23.png)


---

## Página 24

### Texto extraído

```text
Comando ativa a comunicação WIFI.
•
AT+BT_WIFI_OFF
Comando desativa a comunicação WIFI.
•
AT+BT_WIFI_LIST
Retorna a lista de todas as configurações WIFI.
•
AT+BT_SERVER_LIST
Retorna a lista de todas as configurações de comunicação com o servidor UDP
•
AT+BT_GET_WIFI_BUF=<value>
Retorna a configuração WIFI solicitada. <value> de 1 a 5. (Ex.: AT+BT_GET_WIFI_BUF=1)
•
AT+BT_GET_SERVER_BUF=<value>
Retorna a configuração de Servidor solicitada. <value> de 1 a 5. (Ex.: AT+BT_GET_SERVER_BUF=1)
•
AT+BT_SET_WIFI_BUF=<nbuf>,<ssid>,<pass>
Realiza a configuração do WIFI no buffer especificado.
(EX.: AT+BT_SET_WIFI_BUF=1,BLUECHIP, 12456789)
o <nbuf> Número do buffer que será armazenado a configuração (1 a 5).
o <ssid> SSID da rede WIFI (Max 64 caracteres).
o <pass> Senha da rede WIFI (Max 32 caracteres).
•
AT+BT_SET_SERVER_BUF=<nbuf>,<ip>,<porta>
Realiza a configuração do Servidor no buffer especificado.
(Ex.: AT+BT_SET_SERVER_BUF=1,192.168.3.101,20000)
o <nbuf> Número do buffer que será armazenado a configuração (1 a 5).
o <ip> Endereço IP do servidor. (IPV4).
o <porta> Porta de comunicação com o servidor. (Min. 1 e Max 65535)
•
AT+BT_DISCOVERY_NOW=<perfil>
Comando solicita ao equipamento que seja realizado a descoberta dos dispositivos BLUE-
TELEMATICS ao redor via Bluetooth, em <perfil> deve ser passado qual perfil de tempo deve ser
utilizado de 1 a 3.
Perfil
Tempo de pesquisa
1
20,48 segundos
2
40,96 segundos
3
61,44 segundos
•
AT+BT_DISCOVERY_WAIT
Resposta do comando AT+BT_DISCOVERY_NOW, informando que o equipamento está realizando
a descoberta e que deve ser aguardado (Tempo estimado para finalizar a descoberta é de acordo
com o perfil selecionado no momento da solicitação).
```

### Imagem da página (layout original)

![Página 24](images/page_24.png)


---

## Página 25

### Texto extraído

```text
•
A T+BT_DISCOVERY_LIST=<nome>:<RSSI>;<nome>:<RSSI>;...
Resposta do comando AT+BT_DISCOVERY_NOW, informando lista de até 10 dispositivos mais
próximos encontrados, separados por ponto e vírgula. O primeiro listado é o mais próximo do
equipamento que esta realizando a pesquisa e o ultimo seria o mais distante.
Ex.: AT+BT_DISCOVERY_LIST=BLUE_100;BLUE_110;BLUE_200
•
AT+BT_DISCOVERY_NO_FIND
Resposta do comando AT+BT_DISCOVERY_NOW, informando que nenhum equipamento próximo
foi encontrado via Bluetooth.
6. Atualização da interface (OTA - Bluetooth)
A interface pode ser atualizada através do bluetooth, para que a mesma possa ocorrer o veículo
deve estar como motor desligado e dispositivo pareado e autenticado.
O firmware de atualização será disponibilizado no formato Binary (.BIN), onde o mesmo deve ser
enviado a interface conforme descrito abaixo.
Aqui o dispositivo deve enviar o arquivo no formato Byte (dados do arquivo não é ASCII) e não deve
ter QUEBRA DE LINHA E RETORNO DE CARRINHO (CR + LF “\r\n”), fracionado em pacotes de até
1024 bytes.
Obs.: Os pacotes de até 1024 bytes.
Inicialização de atualização.
Comando:
•
AT+BT_OTA_START_LEN=<value>
Comando informa a interface que deve ocorrer a atualização de firmware e que o arquivo possui o
tamanho em Bytes (Ex.: AT+BT_OTA_START_LEN=235678) Bytes.
A interface deve responder enviando o seguinte comando.
•
AT+BT_OTA_START_OK
Neste momento a interface esta pronta para receber o arquivo e o primeiro pacote de até 1024
bytes deve ser enviado dentro de até 500 milissegundos.
Após o envio do primeiro pacote, deve aguardar que a interface solicite um novo pacote
•
AT+BT_OTA_NEXT_PKT
Ao receber este comando, o próximo pacote de até 1024 bytes deve ser enviado.
Após conclusão do envio total do arquivo a interface deve informar que a atualização foi completa
e reinicializar, ou então pode informar alguma exceção de erro no processo de atualização.
•
AT+BT_OTA_COMPLETE_WAIT
Comando informa que todos os dados esperados foram recebidos, atualização sendo finalizada
pelo processador.
```

### Imagem da página (layout original)

![Página 25](images/page_25.png)


---

## Página 26

### Texto extraído

```text
•
AT+BT_OTA_COMPLETE
Comando informa o dispositivo que a atualização foi realizada com sucesso.
Ex.:
AT+BT_OTA_START_LEN=30\r\n
AT+BT_OTA_START_OK\r\n
00102030405060708090.. 00102030405060708090
AT+BT_OTA_NEXT_PKT\r\n
00102030405060708090.. 00102030405060708090
AT+BT_OTA_NEXT_PKT\r\n
00102030405060708090.. 00102030405060708090
AT+BT_OTA_COMPLETE_WAIT\r\n
AT+BT_OTA_COMPLETE\r\n
7. Histórico de Versionamento (Release)
•
V3.6.9
Modificado recurso de Discovery, onde agora é possível selecionar o tempo em que o mesmo vai
ser executado pelo equipamento.
•
V3.6.8
Modificado a resposta do recurso de Discovery dos dispositivos BLUECHIP entorno do
equipamento principal, agora ele devolve o nome do equipamento e seu RSSI.
•
V3.6.1
Criação de recurso que sincroniza horimetro virtual com o horimetro encontrado na rede CAN;
Criação de recurso que minimiza a coleta de horimetro errado pela rede CAN;
Criação de comando para desabilitar e habilitar a coleta de horimetro pela rede CAN
AT+BT_DISABLE_HOUR_CAN         AT+BT_ENABLE_HOUR_CAN
•
V3.6.0
Acrescentado parâmetros destinados a implementos canavieiros.
Horímetro do Elevador, Horímetro do Piloto Automático, Pressão do Corte de Base, Pressão do
Picador, Altura do Corte de Base, Velocidade do Extrator Primário.
Foi acrescentado funcionalidade para ajustar Horímetro inicial.
•
V3.4.0
Implementada funcionalidade de descoberta de equipamento ao redor via Bluetooth, retorna
lista TOP 10 onde o primeiro está mais próximo e o ultimo mais distante.
```

### Imagem da página (layout original)

![Página 26](images/page_26.png)


---

## Página 27

### Texto extraído

```text
•
V3.3.0
Implementada funcionalidade de envia de mensagem Bluetooth para Serial e vice-versa
•
V3.2.2
Acrescentado identificadores de alerta de pós-chave e status do motor
•
V3.2.1
Acrescentado parâmetros destinados a implementos canavieiros.
Extrator Primário, Extrator Secundário, Corte de Base, Esteira do Elevador
```

### Imagem da página (layout original)

![Página 27](images/page_27.png)


---
