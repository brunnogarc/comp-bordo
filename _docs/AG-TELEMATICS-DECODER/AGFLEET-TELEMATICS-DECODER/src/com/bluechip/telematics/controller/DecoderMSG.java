package com.bluechip.telematics.controller;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * 
 * Classe de decodificacao
 * @author Caio Jose Carriel
 * @version 2.0
 * @company BLUECHIP Electronics
 * @since 30/05/2020
 *
 */

public class DecoderMSG {
	public String decodeSerial(String data) {
		String result = data.substring(0, 8);
		BigDecimal tmp = new BigDecimal(Long.valueOf(result,16));
		result = tmp + "";
		while(result.length() < 8) {
			result = "0" + result;
		}
		return result;
	}
	public String decodeVerFW(String data) {
		String result = data.substring(8, 10);
		int byte1 = Integer.parseInt(result,16);
		int tmp2 = (byte1 & 0b11110000) >> 4;
		result = tmp2 + "";
		return result;
	}
	public String decodeTypMSG(String data) {
		String result = data.substring(8, 10);
		int byte1 = Integer.parseInt(result,16);
		int tmp2 = (byte1 & 0b00001111);
		result = tmp2 + "";
		return result;
	}
	public String decodeQtdMsgFLASH(String data) {
		String result = data.substring(10, 14);
		BigDecimal tmp = new BigDecimal(Long.valueOf(result,16));
		result = tmp + "";
		return result;
	}
	public String decodeEngON(String data) {
		String result = data.substring(14, 16);
		int byte1 = Integer.parseInt(result,16);
		int tmp2 = (byte1 & 0b00000001);
		if(tmp2 == 1) {
			result = "Ligado";
		}else{
			result = "Desligado";
		}
		return result;
	}
	public String decodeIngON(String data) {
		String result = data.substring(14, 16);
		int byte1 = Integer.parseInt(result,16);
		int tmp2 = (byte1 & 0b00000010) >> 1;
		if(tmp2 == 1) {
			result = "Ligado";
		}else{
			result = "Desligado";
		}
		return result;
	}
	public String decodeStsGPS(String data) {
		String result = data.substring(14, 16);
		int byte1 = Integer.parseInt(result,16);
		int tmp2 = (byte1 & 0b00000100) >> 2;
		if(tmp2 == 1) {
			result = "Valido";
		}else{
			result = "Invalido";
		}
		return result;
	}
	public String decodeTimestamp(String data) {
		String result = data.substring(16, 24);
		result = convertTimeUTC(result);
		return result;
	}
	public String decodeHorimetro(String data) {
		String result = data.substring(24, 32);
		if(result.equals("FFFFFFFF")) {
			result = "Não avaliado";
		}else {
			double tmp = Long.valueOf(result,16);
			tmp = tmp * 0.05;
			result = String.format("%.2f", tmp).replace(",", ".");
		}
		return result;
	}
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
	public String decodeVelocidade(String data) {
		String result = data.substring(48, 50);
		if(result.equals("FF")) {
			result = "Não avaliado";
		}else {
			double tmp = Long.valueOf(result,16);
			result = String.format("%.1f", tmp).replace(",", ".");
		}
		return result;
	}
	public String decodeRPM(String data) {
		String result = data.substring(50, 54);
		if(result.equals("FFFF")) {
			result = "Não avaliado";
		}else {
			double tmp = Long.valueOf(result,16);
			tmp = tmp * 0.125;
			result = String.format("%.1f", tmp).replace(",", ".");
		}
		return result;
	}
	public String decodeBussola(String data, int protocol) {
		String result = "FFFF";
		if(protocol > 1) {
			result = data.substring(54, 58);
		}else {
			result = data.substring(50, 54);
		}
		if(result.equals("FFFF")) {
			result = "Não avaliado";
		}else {
			double tmp = Long.valueOf(result,16);
			result = String.format("%.1f", tmp).replace(",", ".");
		}
		return result;
	}
	
	public String decodeCodUser(String data, int protocol) {
		String result = "FFFFFFFFFFFFFFFF";
		if(protocol > 1) {
			result = data.substring(58, data.length() - 2);
		}else {
			result = data.substring(54, data.length() - 2);
		}
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
