package com.bluechip.telematics.controller;

/**
 * 
 * Classe de utilitarios do programa 
 * @author Caio Jose Carriel
 * @version 2.0
 * @company BLUECHIP Electronics
 * @since 30/05/2020
 *
 */

public class Utilitarios {
	public static byte[] hexStringToByteArray(String s) {
	    int len = s.length();
	    byte[] data = new byte[len / 2];
	    for (int i = 0; i < len; i += 2) {
	        data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
	                             + Character.digit(s.charAt(i+1), 16));
	    }
	    return data;
	}
	
	public static short[] hexStringToShortArray(String s) {
	    int len = s.length();
	    short[] data = new short[len / 2];
	    for (int i = 0; i < len; i += 2) {
	        data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
	                             + Character.digit(s.charAt(i+1), 16));
	    }
	    return data;
	}
	
	public static String calcCKS(byte[] line){
		short cks = 0;
		for (int i = 0; i < line.length; i++) {
			cks += line[i];
		}

		cks = (short) (cks & 0x00FF);

		cks = (short) ~(cks);
		
		cks = (short) (cks + 0x01);

		cks = (short) (cks & 0x00FF);

		return numToValidByteHex(cks);
	}
	
	public static String calcCKS(short[] line){
		short cks = 0;
		for (int i = 0; i < line.length; i++) {
			cks += line[i];
		}

		cks = (short) (cks & 0x00FF);

		cks = (short) ~(cks);
		
		cks = (short) (cks + 0x01);

		cks = (short) (cks & 0x00FF);

		return numToValidByteHex(cks);
	}
	
	private static String numToValidByteHex(short byteInput) {
		String temp = Integer.toHexString(byteInput);

		while (temp.length() < 2) {
			temp = "0" + temp;
		}

		if (temp.length() > 2) {
			temp = temp.substring(temp.length() - 2);
		}

		return temp.toUpperCase();
	}
	
	public static String ByteArrayToString(byte[] array)
	{
		String result = "";
		for (byte b : array) {
            result += String.format("%02X", b);
        }
		return result;
	}
}
