package com.bluechip.telematics.model;

/**
 * 
 * Classe do dispositivo 
 * @author Caio Jose Carriel
 * @version 2.0
 * @company BLUECHIP Electronics
 * @since 30/05/2020
 *
 */

public class Device {
	private String data;
	private boolean ignicao;
	private boolean motor;
	private boolean stsGPS;	
	private String latitude;
	private String longitude;
	private String velocidade;
	private String horimetro;
	private String compass;
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public boolean getIgnicao() {
		return ignicao;
	}
	public void setIgnicao(boolean ignicao) {
		this.ignicao = ignicao;
	}
	public boolean getMotor() {
		return motor;
	}
	public void setMotor(boolean motor) {
		this.motor = motor;
	}
	public String getVelocidade() {
		return velocidade;
	}
	public void setVelocidade(String velocidade) {
		this.velocidade = velocidade;
	}
	public String getHorimetro() {
		return horimetro;
	}
	public void setHorimetro(String horimetro) {
		this.horimetro = horimetro;
	}
	public String getCompass() {
		return compass;
	}
	public void setCompass(String compass) {
		this.compass = compass;
	}
	public boolean isStsGPS() {
		return stsGPS;
	}
	public void setStsGPS(boolean stsGPS) {
		this.stsGPS = stsGPS;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}	
}
