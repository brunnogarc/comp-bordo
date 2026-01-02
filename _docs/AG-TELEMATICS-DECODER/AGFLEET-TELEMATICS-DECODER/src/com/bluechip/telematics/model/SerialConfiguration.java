package com.bluechip.telematics.model;

/**
 * 
 * Classe contem serial selecionada e velocidade 
 * @author Caio Jose Carriel
 * @version 2.0
 * @company BLUECHIP Electronics
 * @since 30/05/2020
 *
 */
public class SerialConfiguration {
	private String serialName;
	private Integer baudRate;
	
	public String getSerialName() {
		return serialName;
	}
	public void setSerialName(String serialName) {
		this.serialName = serialName;
	}
	public Integer getBaudRate() {
		return baudRate;
	}
	public void setBaudRate(Integer baudRate) {
		this.baudRate = baudRate;
	}
	
	
}
