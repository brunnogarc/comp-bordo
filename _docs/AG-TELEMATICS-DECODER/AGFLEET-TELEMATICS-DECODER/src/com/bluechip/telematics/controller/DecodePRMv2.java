package com.bluechip.telematics.controller;

import java.math.BigDecimal;

public class DecodePRMv2 {
	//versão do firmware da interface
	public String decodeVerFW(String data) {
		String result = data.substring(0, 2);
		int byte1 = Integer.parseInt(result,16);
		int tmp2 = (byte1 & 0b11110000) >> 4;
		result = tmp2 + "";
		return result;
	}
	//versão do protocolo da interface
	public String decodeVerPROT(String data) {
		String result = data.substring(0, 2);
		int byte1 = Integer.parseInt(result,16);
		int tmp2 = (byte1 & 0b00001111);
		result = tmp2 + "";
		return result;
	}
	
	//indica o status do motor do veiculo (ligado ou desligado)
	public String decodeEngON(String data) {
		String result = data.substring(2, 4);
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
		String result = data.substring(2, 4);
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
		String result = data.substring(2, 4);
		int byte1 = Integer.parseInt(result,16);
		int tmp2 = (byte1 & 0b00000100) >> 2;
		if(tmp2 == 1) {
			result = "Valido";
		}else{
			result = "Invalido";
		}
		return result;
	}
	//latitude do GPS
	public String decodeLatitude(String data) {
		String result = data.substring(4, 12);
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
		String result = data.substring(12, 20);
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
	//Parametro: bussola do GPS 
	//Unidade: Graus em rela��o ao norte
	public String decodeBussola(String data) {
		String result = data.substring(20, 24);
		if(result.equals("FFFF")) {
			result = "Não avaliado";
		}else {
			double tmp = Long.valueOf(result,16);
			result = String.format("%.1f", tmp).replace(",", ".");
		}
		return result;
	}
	//Parametro: Bateria do veiculo
	//Unidade: Volts (V)
	public String decodeBateria(String data) {
		String result = data.substring(24, 28);
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
		String result = data.substring(28, 36);
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
		String result = data.substring(36, 44);
		if(result.equals("FFFFFFFF")) {
			result = "Não avaliado";
		}else {
			double tmp = Long.valueOf(result,16);
			tmp = tmp * 0.05;
			result = String.format("%.2f", tmp).replace(",", ".");
		}
		return result;
	}
	//Parametro: Odometro do veiculo
	//Unidade: Kilometros (KM)
	public String decodeOdometro(String data) {
		String result = data.substring(44, 52);
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
		String result = data.substring(52, 56);
		if(result.equals("FFFF")) {
			result = "Não avaliado";
		}else {
			double tmp = Long.valueOf(result,16);
			result = String.format("%.1f", tmp).replace(",", ".");
		}
		return result;
	}
	//Parametro: Rota��o do motor do veiculo
	//Unidade: Rota��es (RPM)
	public String decodeRPM(String data) {
		String result = data.substring(56, 60);
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
		String result = data.substring(60, 62);
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
		String result = data.substring(62, 64);
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
		String result = data.substring(64, 66);
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
		String result = data.substring(66, 68);
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
		String result = data.substring(68, 70);
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
		String result = data.substring(70, 72);
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
		String result = data.substring(72, 74);
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
		String result = data.substring(74, 76);
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
		String result = data.substring(76, 80);
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
		String result = data.substring(80, 82);
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
		String result = data.substring(82, 84);
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
		String result = data.substring(84, 86);
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
		String result = data.substring(86, 90);
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
		String result = data.substring(90, 92);
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
		String result = data.substring(92, 94);
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
		String result = data.substring(94, 98);
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
		String result = data.substring(98, 100);
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
	//Parametro: Nivel do fluido hidraulico
	//Unidade: Porcentagem (%)
	public String decodeNivelFluidoHidra(String data) {
		String result = data.substring(102, 104);
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
		String result = data.substring(104, 106);
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
		String result = data.substring(104, 106);
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
		String result = data.substring(104, 106);
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
		String result = data.substring(104, 106);
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
	//Parametro: Velocidade da unidade de colheita
	//Unidade: Rota��o (RPM)
	public String decodeVelColheita(String data) {
		String result = data.substring(108, 110);
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
		String result = data.substring(110, 112);
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
		String result = data.substring(110, 112);
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
		String result = data.substring(110, 112);
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
		String result = data.substring(110, 112);
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
		String result = data.substring(112, 114);
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
		String result = data.substring(112, 114);
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
		String result = data.substring(112, 114);
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
		String result = data.substring(112, 114);
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
		String result = data.substring(114, 116);
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
		String result = data.substring(114, 116);
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
		String result = data.substring(114, 116);
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
		String result = data.substring(114, 116);
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
		String result = data.substring(116, 118);
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
		String result = data.substring(116, 118);
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
		String result = data.substring(116, 118);
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
		String result = data.substring(116, 118);
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
		String result = data.substring(118, 120);
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
		String result = data.substring(118, 120);
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
		String result = data.substring(118, 120);
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
	public String decodePortaDigital02(String data) {
		String result = data.substring(118, 120);
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
	//Parametro: Horimetro Esteira Elevador
	//Unidade: Horas
	public String decodeHorimetroElevador(String data) {
		String result = data.substring(120, 128);
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
		String result = data.substring(128, 136);
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
		String result = data.substring(136, 140);
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
		String result = data.substring(140, 144);
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
		String result = data.substring(144, 148);
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
		String result = data.substring(148, 152);
		if(result.equals("FFFF")) {
			result = "Não avaliado";
		}else {
			double tmp = Long.valueOf(result,16);
			tmp = tmp * 0.125;
			result = String.format("%.1f", tmp).replace(",", ".");
		}
		return result;
	}
	
	//Parametro: Bico direito 04 (pulverizador)
	//Unidade: Aberto ou fechado
	public String decodeBicoPulD4(String data) {
		String result = data.substring(152, 154);
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
	//Parametro: Bico esquerdo 04 (pulverizador)
	//Unidade: Aberto ou fechado
	public String decodeBicoPulE4(String data) {
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
}
