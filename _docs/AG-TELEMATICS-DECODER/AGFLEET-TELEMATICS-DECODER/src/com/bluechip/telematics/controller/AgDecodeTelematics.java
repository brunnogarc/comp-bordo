package com.bluechip.telematics.controller;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AgDecodeTelematics {
	//numero de serie da interface
	public String decodeSerial(String data) {
		String result = data.substring(0, 8);
		BigDecimal tmp = new BigDecimal(Long.valueOf(result,16));
		result = tmp + "";
		while(result.length() < 8) {
			result = "0" + result;
		}
		return result;
	}
	//vers�o do firmware da interface
	public String decodeVerFW(String data) {
		String result = data.substring(8, 10);
		int byte1 = Integer.parseInt(result,16);
		int tmp2 = (byte1 & 0b11110000) >> 4;
		result = tmp2 + "";
		return result;
	}
	//vers�o do protocolo da interface
	public String decodeVerPROT(String data) {
		String result = data.substring(8, 10);
		int byte1 = Integer.parseInt(result,16);
		int tmp2 = (byte1 & 0b00001111);
		result = tmp2 + "";
		return result;
	}
	//tipo de mensagem decodificado (ALERT ou REPORT)
	public String decodeTypMSG(String data) {
		String result = data.substring(10, 12);
		int byte1 = Integer.parseInt(result,16);
		int tmp2 = (byte1 & 0b11110000) >> 4;
		if(tmp2 == 1) {
			result = "ALERT";
		}else if(tmp2 == 2) {
			result = "REPORT";
		}else if(tmp2 == 3) {
			result = "KEEP";
		}else {
			result = "Não identificado";
		}
		return (tmp2 + " - " + result);		
	}
	//vers�o da carga da interface
	public String decodeVerCarga(String data) {
		String result = data.substring(10, 12);
		int byte1 = Integer.parseInt(result,16);
		int tmp2 = (byte1 & 0b00001111);
		result = tmp2 + "";
		return result;
	}
	//tipo da carga da interface
	public String decodeTypCarga(String data) {
		String result = data.substring(12, 14);
		int byte1 = Integer.parseInt(result,16);
		int tmp2 = (byte1 & 0b11000000) >> 6;
		result = tmp2 + "";
		return result;
	}
	//id de identifica��o da carga da interface
	public String decodeIdCarga(String data) {
		String result = data.substring(12, 14);
		int byte1 = Integer.parseInt(result,16);
		int tmp2 = (byte1 & 0b00111111);
		result = tmp2 + "";
		return result;
	}
	//informa o identificador do alerta que gerou a mensagem
	public String decodeIdAlerta(String data) {
		String result = data.substring(14, 16);
		int byte1 = Integer.parseInt(result,16);
		if(byte1 == 0) {
			result = "Não identificado";
		}else if(byte1 == 1) {
			result = "STS Bussola";
		}else if(byte1 == 2) {
			result = "STS pós-chave";
		}else if(byte1 == 3) {
			result = "STS Motor";
		}else if(byte1 == 4) {
			result = "STS Velocidade";
		}else if(byte1 == 5) {
			result = "Gerado tempo";
		}else if(byte1 == 255) {
			result = "CPU reset";
		}else {
			result = "Invalido";
		}
		return (byte1 + " - " + result);
	}
	//reservado 01
	public String decodeReservado01(String data) {
		String result = data.substring(16, 18);
		return result;
	}
	//quantidade de mensagens armazenada na memoria interna (flash) da interface
	public String decodeQtdMsgFLASH(String data) {
		String result = data.substring(18, 22);
		BigDecimal tmp = new BigDecimal(Long.valueOf(result,16));
		result = tmp + "";
		return result;
	}
	//indica o status do motor do veiculo (ligado ou desligado)
	public String decodeEngON(String data) {
		String result = data.substring(22, 24);
		int byte1 = Integer.parseInt(result,16);
		int tmp2 = (byte1 & 0b00000010) >> 1;
		if(tmp2 == 1) {
			result = "Ligado";
		}else{
			result = "Desligado";
		}
		return result;
	}
	//indica o status da igni��o do veiculo (ligado ou desligado)
	public String decodeIngON(String data) {
		String result = data.substring(22, 24);
		int byte1 = Integer.parseInt(result,16);
		int tmp2 = (byte1 & 0b00000001);
		if(tmp2 == 1) {
			result = "Ligado";
		}else{
			result = "Desligado";
		}
		return result;
	}
	//indica o status do sinal GPS da interface (Valido ou Invalido)
	public String decodeStsGPS(String data) {
		String result = data.substring(22, 24);
		int byte1 = Integer.parseInt(result,16);
		int tmp2 = (byte1 & 0b00000100) >> 2;
		if(tmp2 == 1) {
			result = "Valido";
		}else{
			result = "Invalido";
		}
		return result;
	}
	
	//hora e data no momento da gera��o da mensagem
	public String decodeTimestamp(String data) {
		String result = data.substring(24, 32);
		result = convertTimeUTC(result);
		return result;
	}
	//latitude do GPS
	public String decodeLatitude(String data) {
		String result = data.substring(32, 40);
		if(result.equals("FFFFFFFF")) {
			result = "Não avaliado";
		}else {
			long recLat = Long.parseLong(result, 16);
			BigDecimal latFinal = new  BigDecimal((recLat - Long.parseLong("4294967296")));
		    latFinal = latFinal.divide(new BigDecimal(10000000));
		    result = latFinal+"";
		}
		return result;
	}
	//longitude do GPS
	public String decodeLongitude(String data) {
		String result = data.substring(40, 48);
		if(result.equals("FFFFFFFF")) {
			result = "Não avaliado";
		}else {
			long recLon = Long.parseLong(result, 16);
			BigDecimal lonFinal = new  BigDecimal((recLon - Long.parseLong("4294967296")));
			lonFinal = lonFinal.divide(new BigDecimal(10000000));
		    result = lonFinal+"";
		}
		return result;
	}
	//tipo do fix
	public String decodeTipoFIX(String data) {
		String result = data.substring(48, 50);
		int byte1 = Integer.parseInt(result,16);
		if(byte1 == 0) {
			result = "NA";
		}else if(byte1 == 1) {
			result = "SEM FIX";
		}else if(byte1 == 2) {
			result = "2D";
		}else if(byte1 == 3) {
			result = "3D";
		}else {
			result = "NA";
		}
		return (byte1 + " - " + result);
	}
	//HDOP
	public String decodeHDOP(String data) {
		String result = data.substring(50, 54);
		if(result.equals("FFFF")) {
			result = "Não avaliado";
		}else {
			double tmp = Long.valueOf(result,16);
			tmp = tmp * 0.01;
			//1.5 é o fator para conversao em metros
			result = String.format("%.2f", tmp) + " - " + (String.format("%.2f", (tmp * 1.5)).replace(",", ".") + " mts");
		}
		return result;
	}
	//QTD SATELITE
	public String decodeQtdSAT(String data) {
		String result = data.substring(54, 56);
		int byte1 = Integer.parseInt(result,16);
		return byte1 + "";
	}
	//Parametro: bussola do GPS 
	//Unidade: Graus em rela��o ao norte
	public String decodeBussola(String data) {
		String result = "FFFF";
		       result = data.substring(56, 60);
		if(result.equals("FFFF")) {
			result = "Não avaliado";
		}else {
			double tmp = Long.valueOf(result,16);
			result = String.format("%.1f", tmp).replace(",", ".");
		}
		return result;
	}
	//tipo de conexao
	public String decodeConnTech(String data) {
		String result = data.substring(60, 62);
		if(result.equals("FF")) {
			result = "Não avaliado";
		}else {
			int byte1 = Integer.parseInt(result,16);
			if(byte1 == 0) {
				result = "Sem conexão";
			}else if(byte1 == 1) {
				result = "LTE";
			}else if(byte1 == 2) {
				result = "WIFI";
			}else {
				result = "Não avaliado";
			}			
		}
		return result;
	}
	//qualidade do sinal
	public String decodePowerSignal(String data) {
		String result = data.substring(62, 64);
		if(result.equals("FF")) {
			result = "Não avaliado";
		}else {
			int rssi_positivo = Integer.parseInt(result,16);
			if (rssi_positivo <= 50) {
				return "-"+rssi_positivo+"dBm - Excelente";
			}
		    if (rssi_positivo <= 60) {
		    	return "-"+rssi_positivo+"dBm - Muito bom";
		    }
		    if (rssi_positivo <= 70) {
		    	return "-"+rssi_positivo+"dBm - Bom";
		    }
		    if (rssi_positivo <= 80) {
		    	return "-"+rssi_positivo+"dBm - Fraco";
		    }
		    if (rssi_positivo <= 90) {
		    	return "-"+rssi_positivo+"dBm - Muito fraco";
		    }
		    return "-"+rssi_positivo+"dBm - Sem sinal";		
		}
		return result;
	}
	//Parametro: Bateria do veiculo
	//Unidade: Volts (V)
	public String decodeBateria(String data) {
		String result = data.substring(64, 68);
		if(result.equals("FFFFFFFF")) {
			result = "Não avaliado";
		}else {
			double tmp = Long.valueOf(result,16);
			tmp = tmp * 0.05;
			result = String.format("%.2f", tmp).replace(",", ".");
		}
		return result;
	}
	//Parametro: Horimetro do veiculo
	//Unidade: Horas
	public String decodeHorimetro(String data) {
		String result = data.substring(68, 76);
		if(result.equals("FFFFFFFF")) {
			result = "Não avaliado";
		}else {
			double tmp = Long.valueOf(result,16);
			tmp = tmp * 0.05;
			result = String.format("%.2f", tmp).replace(",", ".");
		}
		return result;
	}
	//Parametro: Total de Combustivel utilizado pelo motor do veiculo
	//Unidade: Litros (L)
	public String decodeTotCombustivel(String data) {
		String result = data.substring(76, 84);
		if(result.equals("FFFFFFFF")) {
			result = "Não avaliado";
		}else {
			double tmp = Long.valueOf(result,16);
			tmp = tmp * 0.5;
			result = String.format("%.2f", tmp).replace(",", ".");
		}
		return result;
	}
	//Parametro: Odometro do veiculo
	//Unidade: Kilometros (KM)
	public String decodeOdometro(String data) {
		String result = data.substring(84, 92);
		if(result.equals("FFFFFFFF")) {
			result = "Não avaliado";
		}else {
			double tmp = Long.valueOf(result,16);
			tmp = tmp * 0.125;
			result = String.format("%.2f", tmp).replace(",", ".");
		}
		return result;
	}
	//Parametro: Velocidade do veiculo
	//Unidade: Kilometros por hora (Km/h)
	public String decodeVelocidade(String data) {
		String result = data.substring(92, 96);
		if(result.equals("FFFF")) {
			result = "Não avaliado";
		}else {
			double tmp = Long.valueOf(result,16);
			tmp = tmp * 0.1;
			result = String.format("%.1f", tmp).replace(",", ".");
		}
		return result;
	}
	//Parametro: Rota��o do motor do veiculo
	//Unidade: Rota��es (RPM)
	public String decodeRPM(String data) {
		String result = data.substring(96, 100);
		if(result.equals("FFFF")) {
			result = "Não avaliado";
		}else {
			double tmp = Long.valueOf(result,16);
			tmp = tmp * 0.125;
			result = String.format("%.1f", tmp).replace(",", ".");
		}
		return result;
	}
	//Parametro: Pedal do Acelerador do veiculo
	//Unidade: Porcentagem (%)
	public String decodePedalAcelerador(String data) {
		String result = data.substring(100, 102);
		if(result.equals("FF")) {
			result = "Não avaliado";
		}else {
			double tmp = Long.valueOf(result,16);
			tmp = tmp * 0.4;
			result = String.format("%.1f", tmp).replace(",", ".");
		}
		return result;
	}
	//Parametro: Torque do motor do veiculo
	//Unidade: Porcentagem (%)
	public String decodeTorqueMotor(String data) {
		String result = data.substring(102, 104);
		if(result.equals("FF")) {
			result = "Não avaliado";
		}else {
			double tmp = Long.valueOf(result,16);
			tmp = tmp - 125;
			result = String.format("%.1f", tmp).replace(",", ".");
		}
		return result;
	}
	//Parametro: Carga do motor do veiculo
	//Unidade: Porcentagem (%)
	public String decodeCargaMotor(String data) {
		String result = data.substring(104, 106);
		if(result.equals("FF")) {
			result = "Não avaliado";
		}else {
			double tmp = Long.valueOf(result,16);
			tmp = tmp * 0.4;
			result = String.format("%.1f", tmp).replace(",", ".");
		}
		return result;
	}
	//Parametro: Press�o do turbo do veiculo
	//Unidade: PSI
	public String decodePressaoTurbo(String data) {
		String result = data.substring(106, 108);
		if(result.equals("FF")) {
			result = "Não avaliado";
		}else {
			double tmp = Long.valueOf(result,16);
			tmp = tmp * 0.05;
			result = String.format("%.1f", tmp).replace(",", ".");
		}
		return result;
	}
	//Parametro: Press�o do ar de admiss�o do motor
	//Unidade: PSI
	public String decodePressaoAdmisao(String data) {
		String result = data.substring(108, 110);
		if(result.equals("FF")) {
			result = "Não avaliado";
		}else {
			double tmp = Long.valueOf(result,16);
			tmp = tmp * 0.05;
			result = String.format("%.1f", tmp).replace(",", ".");
		}
		return result;
	}
	//Parametro: Press�o do oleo do motor
	//Unidade: KPA
	public String decodePressaoOleo(String data) {
		String result = data.substring(110, 112);
		if(result.equals("FF")) {
			result = "Não avaliado";
		}else {
			double tmp = Long.valueOf(result,16);
			tmp = tmp * 4;
			result = String.format("%.1f", tmp).replace(",", ".");
		}
		return result;
	}
	//Parametro: Press�o do oleo da transmiss�o do veiculo
	//Unidade: KPA
	public String decodePressaoTrans(String data) {
		String result = data.substring(112, 114);
		if(result.equals("FF")) {
			result = "Não avaliado";
		}else {
			double tmp = Long.valueOf(result,16);
			tmp = tmp * 16;
			result = String.format("%.1f", tmp).replace(",", ".");
		}
		return result;
	}
	//Parametro: Press�o do combustivel
	//Unidade: KPA
	public String decodePressaoCombustivel(String data) {
		String result = data.substring(114, 116);
		if(result.equals("FF")) {
			result = "Não avaliado";
		}else {
			double tmp = Long.valueOf(result,16);
			tmp = tmp * 4;
			result = String.format("%.1f", tmp).replace(",", ".");
		}
		return result;
	}
	//Parametro: Temperatura do oleo do motor
	//Unidade: Celsius (�C)
	public String decodeTempOleo(String data) {
		String result = data.substring(116, 120);
		if(result.equals("FFFF")) {
			result = "Não avaliado";
		}else {
			double tmp = Long.valueOf(result,16);
			tmp = tmp * 0.03125;
			result = String.format("%.1f", tmp).replace(",", ".");
		}
		return result;
	}
	//Parametro: Temperatura da agua do motor (liquido de arrefecimento)
	//Unidade: Celsius (�C)
	public String decodeTempAgua(String data) {
		String result = data.substring(120, 122);
		if(result.equals("FF")) {
			result = "Não avaliado";
		}else {
			double tmp = Long.valueOf(result,16);
			tmp = tmp - 40;
			result = String.format("%.1f", tmp).replace(",", ".");
		}
		return result;
	}
	//Parametro: Temperatura do ar de admiss�o do motor
	//Unidade: Celsius (�C)
	public String decodeTempAdmissao(String data) {
		String result = data.substring(122, 124);
		if(result.equals("FF")) {
			result = "Não avaliado";
		}else {
			double tmp = Long.valueOf(result,16);
			tmp = tmp - 40;
			result = String.format("%.1f", tmp).replace(",", ".");
		}
		return result;
	}
	//Parametro: Temperatura do ar ambiente
	//Unidade: Celsius (�C)
	public String decodeTempAmbiente(String data) {
		String result = data.substring(124, 126);
		if(result.equals("FF")) {
			result = "Não avaliado";
		}else {
			double tmp = Long.valueOf(result,16);
			tmp = tmp - 40;
			result = String.format("%.1f", tmp).replace(",", ".");
		}
		return result;
	}
	//Parametro: Temperatura do oleo da transmiss�o
	//Unidade: Celsius (�C)
	public String decodeTempTrans(String data) {
		String result = data.substring(126, 130);
		if(result.equals("FFFF")) {
			result = "Não avaliado";
		}else {
			double tmp = Long.valueOf(result,16);
			tmp = (tmp * 0.03125) - 273;
			result = String.format("%.1f", tmp).replace(",", ".");
		}
		return result;
	}
	//Parametro: Temperatura do fluido hidraulico
	//Unidade: Celsius (�C)
	public String decodeTempFluidoHidra(String data) {
		String result = data.substring(130, 132);
		if(result.equals("FF")) {
			result = "Não avaliado";
		}else {
			double tmp = Long.valueOf(result,16);
			tmp = tmp - 40;
			result = String.format("%.1f", tmp).replace(",", ".");
		}
		return result;
	}
	//Parametro: Temperatura do combustivel
	//Unidade: Celsius (�C)
	public String decodeTempCombustivel(String data) {
		String result = data.substring(132, 134);
		if(result.equals("FF")) {
			result = "Não avaliado";
		}else {
			double tmp = Long.valueOf(result,16);
			tmp = tmp - 40;
			result = String.format("%.1f", tmp).replace(",", ".");
		}
		return result;
	}
	//Parametro: Vaz�o de combustivel
	//Unidade: Litros por hora (L/h)
	public String decodeVazaoComb(String data) {
		String result = data.substring(134, 138);
		if(result.equals("FFFF")) {
			result = "Não avaliado";
		}else {
			double tmp = Long.valueOf(result,16);
			tmp = tmp * 0.05;
			result = String.format("%.1f", tmp).replace(",", ".");
		}
		return result;
	}
	//Parametro: Nivel de combustivel
	//Unidade: Porcentagem (%)
	public String decodeNivelComb(String data) {
		String result = data.substring(138, 140);
		if(result.equals("FF")) {
			result = "Não avaliado";
		}else {
			double tmp = Long.valueOf(result,16);
			tmp = tmp * 0.4;
			result = String.format("%.1f", tmp).replace(",", ".");
		}
		return result;
	}
	//Parametro: Nivel do oleo da transmiss�o
	//Unidade: Porcentagem (%)
	public String decodeNivelOleoTrans(String data) {
		String result = data.substring(140, 142);
		if(result.equals("FF")) {
			result = "Não avaliado";
		}else {
			double tmp = Long.valueOf(result,16);
			tmp = tmp * 0.4;
			result = String.format("%.1f", tmp).replace(",", ".");
		}
		return result;
	}
	//Parametro: Nivel do fluido hidraulico
	//Unidade: Porcentagem (%)
	public String decodeNivelFluidoHidra(String data) {
		String result = data.substring(142, 144);
		if(result.equals("FF")) {
			result = "Não avaliado";
		}else {
			double tmp = Long.valueOf(result,16);
			tmp = tmp * 0.4;
			result = String.format("%.1f", tmp).replace(",", ".");
		}
		return result;
	}
	//Parametro: Status do Codigo de falhas
	//Unidade: Ausente ou presente
	public String decodeDTC(String data) {
		String result = data.substring(144, 146);
		int byte1 = Integer.parseInt(result,16);
		int tmp2 = (byte1 & 0b00000011);
		if(tmp2 == 0) {
			result = "OFF";
		}else if(tmp2 == 1){
			result = "ON";
		}else{
			result = "NA";
		}
		return result;
	}
	//Parametro: Status da helice do radiador
	//Unidade: Ligado ou desligado
	public String decodeFUN(String data) {
		String result = data.substring(144, 146);
		int byte1 = Integer.parseInt(result,16);
		int tmp2 = (byte1 & 0b00001100) >> 2;
		if(tmp2 == 0) {
			result = "OFF";
		}else if(tmp2 == 1){
			result = "ON";
		}else{
			result = "NA";
		}
		return result;
	}
	//Parametro: Status da esteira do elevador (CANA)
	//Unidade: Ligado ou desligado
	public String decodeEsteiraElevador(String data) {
		String result = data.substring(144, 146);
		int byte1 = Integer.parseInt(result,16);
		int tmp2 = (byte1 & 0b00110000) >> 4;
		if(tmp2 == 0) {
			result = "OFF";
		}else if(tmp2 == 1){
			result = "ON";
		}else{
			result = "NA";
		}
		return result;
	}
	//Parametro: Status do corte de base (CANA)
	//Unidade: Ligado ou desligado
	public String decodeCorteBase(String data) {
		String result = data.substring(144, 146);
		int byte1 = Integer.parseInt(result,16);
		int tmp2 = (byte1 & 0b11000000) >> 6;
		if(tmp2 == 0) {
			result = "OFF";
		}else if(tmp2 == 1){
			result = "ON";
		}else{
			result = "NA";
		}
		return result;
	}
	//Parametro: Altura do implemento agricola
	//Unidade: Porcentagem (%)
	public String decodeAlturaImpl(String data) {
		String result = data.substring(146, 148);
		if(result.equals("FF")) {
			result = "Não avaliado";
		}else {
			double tmp = Long.valueOf(result,16);
			tmp = tmp * 0.05;
			result = String.format("%.1f", tmp).replace(",", ".");
		}
		return result;
	}
	//Parametro: Velocidade da unidade de colheita
	//Unidade: Rota��o (RPM)
	public String decodeVelColheita(String data) {
		String result = data.substring(148, 150);
		if(result.equals("FF")) {
			result = "Não avaliado";
		}else {
			double tmp = Long.valueOf(result,16);
			result = String.format("%.1f", tmp).replace(",", ".");
		}
		return result;
	}
	//Parametro: Status da tomada de for�a
	//Unidade: Ligado ou desligado
	public String decodeTomadaForca(String data) {
		String result = data.substring(150, 152);
		int byte1 = Integer.parseInt(result,16);
		int tmp2 = (byte1 & 0b00000011);
		if(tmp2 == 0) {
			result = "DESLIGADO";
		}else if(tmp2 == 1){
			result = "LIGADO";
		}else{
			result = "Não Avaliado";
		}
		return result;
	}
	//Parametro: Status do piloto automatico (RTK)
	//Unidade: Ligado ou desligado
	public String decodePilotoRTK(String data) {
		String result = data.substring(150, 152);
		int byte1 = Integer.parseInt(result,16);
		int tmp2 = (byte1 & 0b00001100) >> 2;
		if(tmp2 == 0) {
			result = "OFF";
		}else if(tmp2 == 1){
			result = "ON";
		}else{
			result = "NA";
		}
		return result;
	}
	//Parametro: Status da industria do veiculo
	//Unidade: Ligado ou desligado
	public String decodeIndustria(String data) {
		String result = data.substring(150, 152);
		int byte1 = Integer.parseInt(result,16);
		int tmp2 = (byte1 & 0b00110000) >> 4;
		if(tmp2 == 0) {
			result = "OFF";
		}else if(tmp2 == 1){
			result = "ON";
		}else{
			result = "NA";
		}
		return result;
	}
	//Parametro: Status da descarga de gr�os
	//Unidade: Ligado ou desligado
	public String decodeDescargaGrao(String data) {
		String result = data.substring(150, 152);
		int byte1 = Integer.parseInt(result,16);
		int tmp2 = (byte1 & 0b11000000) >> 6;
		if(tmp2 == 0) {
			result = "OFF";
		}else if(tmp2 == 1){
			result = "ON";
		}else{
			result = "NA";
		}
		return result;
	}
	//Paramtros: Status da unidade de colheita
	//Unidade: Ligado ou desligado
	public String decodeUnidadeColheita(String data) {
		String result = data.substring(152, 154);
		int byte1 = Integer.parseInt(result,16);
		int tmp2 = (byte1 & 0b00000011);
		if(tmp2 == 0) {
			result = "OFF";
		}else if(tmp2 == 1){
			result = "ON";
		}else{
			result = "NA";
		}
		return result;
	}
	//Parametro: Status da plataforma (descarga rolo de algd�o)
	//Unidade: Parado, abaixando ou subindo
	public String decodePlataforma(String data) {
		String result = data.substring(152, 154);
		int byte1 = Integer.parseInt(result,16);
		int tmp2 = (byte1 & 0b00001100) >> 2;
		if(tmp2 == 0) {
			result = "DESLIGADO";
		}else if(tmp2 == 1){
			result = "ABAIXANDO";
		}else if(tmp2 == 2){
			result = "SUBINDO";
		}else{
			result = "Não Avaliado";
		}
		return result;
	}
	//Parametro: Status do processo de embalar algod�o
	//Unidade: Ligado ou desligado
	public String decodeEmbalar(String data) {
		String result = data.substring(152, 154);
		int byte1 = Integer.parseInt(result,16);
		int tmp2 = (byte1 & 0b00110000) >> 4;
		if(tmp2 == 0) {
			result = "OFF";
		}else if(tmp2 == 1){
			result = "ON";
		}else{
			result = "NA";
		}
		return result;
	}
	//Parametro: Status da bomba dagua (pulverizadores)
	//Unidade: Ligado ou desligado
	public String decodeBombaDagua(String data) {
		String result = data.substring(152, 154);
		int byte1 = Integer.parseInt(result,16);
		int tmp2 = (byte1 & 0b11000000) >> 6;
		if(tmp2 == 0) {
			result = "OFF";
		}else if(tmp2 == 1){
			result = "ON";
		}else{
			result = "NA";
		}
		return result;
	}
	//Parametro: Taxa de aplica��o (pulverizador)
	//Unidade: Modo manual, taxa 1 e taxa 2
	public String decodeTaxaAplicacao(String data) {
		String result = data.substring(154, 156);
		int byte1 = Integer.parseInt(result,16);
		int tmp2 = (byte1 & 0b00000011);
		if(tmp2 == 0) {
			result = "MAN";
		}else if(tmp2 == 1){
			result = "T1";
		}else if(tmp2 == 2){
			result = "T2";
		}else{
			result = "NA";
		}
		return result;
	}
	//Parametro: Liberando Liquido (pulverizador)
	//Unidade: Aberto ou fechado
	public String decodeLiberandoLiquido(String data) {
		String result = data.substring(154, 156);
		int byte1 = Integer.parseInt(result,16);
		int tmp2 = (byte1 & 0b00001100) >> 2;
		if(tmp2 == 0) {
			result = "OFF";
		}else if(tmp2 == 1){
			result = "ON";
		}else{
			result = "NA";
		}
		return result;
	}
	//Parametro: Status Extrator Primario (CANA)
	//Unidade: Ligado ou Desligado
	public String decodeExtratorPrimario(String data) {
		String result = data.substring(154, 156);
		int byte1 = Integer.parseInt(result,16);
		int tmp2 = (byte1 & 0b00110000) >> 4;
		if(tmp2 == 0) {
			result = "OFF";
		}else if(tmp2 == 1){
			result = "ON";
		}else{
			result = "NA";
		}
		return result;
	}
	//Parametro: Status Extrator Secundario (CANA)
	//Unidade: Ligado ou Desligado
	public String decodeExtratorSecundario(String data) {
		String result = data.substring(154, 156);
		int byte1 = Integer.parseInt(result,16);
		int tmp2 = (byte1 & 0b11000000) >> 6;
		if(tmp2 == 0) {
			result = "OFF";
		}else if(tmp2 == 1){
			result = "ON";
		}else{
			result = "NA";
		}
		return result;
	}
	//Parametro: Bico central (pulverizador)
	//Unidade: Aberto ou fechado
	public String decodeBicoPulCT(String data) {
		String result = data.substring(156, 158);
		int byte1 = Integer.parseInt(result,16);
		int tmp2 = (byte1 & 0b00000011);
		if(tmp2 == 0) {
			result = "OFF";
		}else if(tmp2 == 1){
			result = "ON";
		}else{
			result = "NA";
		}
		return result;
	}
	//Parametro: Bico esquerdo 01 (pulverizador)
	//Unidade: Aberto ou fechado
	public String decodeBicoPulE1(String data) {
		String result = data.substring(156, 158);
		int byte1 = Integer.parseInt(result,16);
		int tmp2 = (byte1 & 0b00001100) >> 2;
		if(tmp2 == 0) {
			result = "OFF";
		}else if(tmp2 == 1){
			result = "ON";
		}else{
			result = "NA";
		}
		return result;
	}
	//Parametro: Bico esquerdo 02 (pulverizador)
	//Unidade: Aberto ou fechado
	public String decodeBicoPulE2(String data) {
		String result = data.substring(156, 158);
		int byte1 = Integer.parseInt(result,16);
		int tmp2 = (byte1 & 0b00110000) >> 4;
		if(tmp2 == 0) {
			result = "OFF";
		}else if(tmp2 == 1){
			result = "ON";
		}else{
			result = "NA";
		}
		return result;
	}
	//Parametro: Bico esquerdo 03 (pulverizador)
	//Unidade: Aberto ou fechado
	public String decodeBicoPulE3(String data) {
		String result = data.substring(156, 158);
		int byte1 = Integer.parseInt(result,16);
		int tmp2 = (byte1 & 0b11000000) >> 6;
		if(tmp2 == 0) {
			result = "OFF";
		}else if(tmp2 == 1){
			result = "ON";
		}else{
			result = "NA";
		}
		return result;
	}
	//Parametro: Bico direito 01 (pulverizador)
	//Unidade: Aberto ou fechado
	public String decodeBicoPulD1(String data) {
		String result = data.substring(158, 160);
		int byte1 = Integer.parseInt(result,16);
		int tmp2 = (byte1 & 0b00000011);
		if(tmp2 == 0) {
			result = "OFF";
		}else if(tmp2 == 1){
			result = "ON";
		}else{
			result = "NA";
		}
		return result;
	}
	//Parametro: Bico direito 02 (pulverizador)
	//Unidade: Aberto ou fechado
	public String decodeBicoPulD2(String data) {
		String result = data.substring(158, 160);
		int byte1 = Integer.parseInt(result,16);
		int tmp2 = (byte1 & 0b00001100) >> 2;
		if(tmp2 == 0) {
			result = "OFF";
		}else if(tmp2 == 1){
			result = "ON";
		}else{
			result = "NA";
		}
		return result;
	}
	//Parametro: Bico direito 03 (pulverizador)
	//Unidade: Aberto ou fechado
	public String decodeBicoPulD3(String data) {
		String result = data.substring(158, 160);
		int byte1 = Integer.parseInt(result,16);
		int tmp2 = (byte1 & 0b00110000) >> 4;
		if(tmp2 == 0) {
			result = "OFF";
		}else if(tmp2 == 1){
			result = "ON";
		}else{
			result = "NA";
		}
		return result;
	}
	//Parametro: Status da porta digital 02
	//Unidade: Ligado ou desligado
	public String decodePortaDigital(String data) {
		String result = data.substring(158, 160);
		int byte1 = Integer.parseInt(result,16);
		int tmp2 = (byte1 & 0b11000000) >> 6;
		if(tmp2 == 0) {
			result = "OFF";
		}else{
			result = "ON";
		}
		return result;
	}
	//Parametro: Horimetro Esteira Elevador
	//Unidade: Horas
	public String decodeHorimetroElevador(String data) {
		String result = data.substring(160, 168);
		if(result.equals("FFFFFFFF")) {
			result = "Não avaliado";
		}else {
			double tmp = Long.valueOf(result,16);
			tmp = tmp * 0.05;
			result = String.format("%.2f", tmp).replace(",", ".");
		}
		return result;
	}
	//Parametro: Horimetro Piloto Automatico
	//Unidade: Horas
	public String decodeHorimetroPiloto(String data) {
		String result = data.substring(168, 176);
		if(result.equals("FFFFFFFF")) {
			result = "Não avaliado";
		}else {
			double tmp = Long.valueOf(result,16);
			tmp = tmp * 0.05;
			result = String.format("%.2f", tmp).replace(",", ".");
		}
		return result;
	}
	//Parametro: Pressão Corte de Base (CANA)
	//Unidade: Bar
	public String decodePressaoCorteBase(String data) {
		String result = data.substring(176, 180);
		if(result.equals("FFFF")) {
			result = "Não avaliado";
		}else {
			double tmp = Long.valueOf(result,16);
			tmp = tmp * 0.05;
			result = String.format("%.1f", tmp).replace(",", ".");
		}
		return result;
	}
	//Parametro: Pressão do Picador (CANA)
	//Unidade: Bar
	public String decodePressaoPicador(String data) {
		String result = data.substring(180, 184);
		if(result.equals("FFFF")) {
			result = "Não avaliado";
		}else {
			double tmp = Long.valueOf(result,16);
			tmp = tmp * 0.05;
			result = String.format("%.1f", tmp).replace(",", ".");
		}
		return result;
	}
	//Parametro: Altura do Corte de Base (CANA)
	//Unidade: Altura
	public String decodeAlturaCorteBase(String data) {
		String result = data.substring(184, 188);
		if(result.equals("FFFF")) {
			result = "Não avaliado";
		}else {
			double tmp = Long.valueOf(result,16);
			tmp = tmp * 1.0;
			result = String.format("%.1f", tmp).replace(",", ".");
		}
		return result;
	}
	//Parametro: Velocidade do Extrator Primario (CANA)
	//Unidade: RPM
	public String decodeVelocidadeExtratorPrimario(String data) {
		String result = data.substring(188, 192);
		if(result.equals("FFFF")) {
			result = "Não avaliado";
		}else {
			double tmp = Long.valueOf(result,16);
			tmp = tmp * 0.125;
			result = String.format("%.1f", tmp).replace(",", ".");
		}
		return result;
	}
	//Parametro: Bico esquerdo 04 (pulverizador)
	//Unidade: Aberto ou fechado
	public String decodeBicoPulE4(String data) {
		String result = data.substring(192, 194);
		int byte1 = Integer.parseInt(result,16);
		int tmp2 = (byte1 & 0b00000011);
		if(tmp2 == 0) {
			result = "OFF";
		}else if(tmp2 == 1){
			result = "ON";
		}else{
			result = "NA";
		}
		return result;
	}
	//Parametro: Bico direito 04 (pulverizador)
	//Unidade: Aberto ou fechado
	public String decodeBicoPulD4(String data) {
		String result = data.substring(192, 194);
		int byte1 = Integer.parseInt(result,16);
		int tmp2 = (byte1 & 0b00001100) >> 2;
		if(tmp2 == 0) {
			result = "OFF";
		}else if(tmp2 == 1){
			result = "ON";
		}else{
			result = "NA";
		}
		return result;
	}
	//194 a 196 reservado
	//reservado 2
	public String decodeReservado02(String data) {
		String result = data.substring(196, 212);
		return result;
	}
	//Paramtro: Codigo do usuario informado durante o snapshot (PROTOCOLO V2)
	public String decodeCodUser(String data) {
		String result = "FFFFFFFFFFFFFFFF";
		result = data.substring(212, data.length() - 2);
		if(result.equals("FFFFFFFFFFFFFFFF")) {
			result = "Não informado";
		}else {
			double tmp = Long.valueOf(result,16);
			result = String.format("%.0f", tmp);
		}
		return result;
	}	

	public String convertTimeUTC(String time){
		long lonUpdateTime = Long.parseLong(time,16);
		//String utcFinal = "GMT+3";
		Date date = new Date(lonUpdateTime*1000L); // *1000 is to convert seconds to milliseconds
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // the format of your date
		//sdf.setTimeZone(TimeZone.getTimeZone(utcFinal)); // give a timezone reference for formating (see comment at the bottom
		String formattedDate = sdf.format(date);
		//System.out.println("Update_time: " + formattedDate);
		return formattedDate;
	}
}
