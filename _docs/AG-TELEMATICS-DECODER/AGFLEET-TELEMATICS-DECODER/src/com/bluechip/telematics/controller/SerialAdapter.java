package com.bluechip.telematics.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;

import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;
import jssc.SerialPortTimeoutException;

/**
 * 
 * Classe contem todos os metodos de recepcao e envio de dados pela serial 
 * @author Caio Jose Carriel
 * @version 2.0
 * @company BLUECHIP Electronics
 * @since 30/05/2020
 *
 */

public class SerialAdapter extends Observable implements SerialPortEventListener {
	private SerialPort serialPort;

	private String readed;
	private Integer index = 0;
	private JTextPane textPane;
	private File fileFirmware;
	private Integer indexFrameParte = 0;
	private List<String> data;
	
	public SerialAdapter(Observer obs) {
		this.readed = "";
		this.addObserver(obs);
	}

	@Override
	public void serialEvent(SerialPortEvent evt) {
		if (evt.isRXCHAR()) {
			try {
				while (serialPort.getInputBufferBytesCount() > 0) {
					byte temp[] = serialPort.readBytes(1);
					if (temp[0] != '\n' && temp[0] != '\r') {
						this.readed = this.readed.concat(String.valueOf((char) temp[0]));
						continue;
					}
					
					setChanged();
					notifyObservers();
					readed = "";
					serialPort.purgePort(SerialPort.PURGE_TXCLEAR | SerialPort.PURGE_RXCLEAR);
					return;
				}
			} catch (SerialPortException e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(null, "Ocorreu um erro no sistema, entre em contato com a engenharia!","Mensagem de erro!", JOptionPane.ERROR_MESSAGE);
				System.exit(0);
			}

		}
	}
	
	public void cmdSendSerial(String val){
		try {
			serialPort.writeBytes(val.getBytes());
		} catch (SerialPortException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, "Ocorreu um erro ao tentar enviar valores pela USB" + e.getMessage());
			e.printStackTrace();
		}
	}
	
	public void cmdSendBytes(byte[] val) {
		try {
			serialPort.writeBytes(val);
		} catch (SerialPortException e) {
			// TODO Auto-generated catch block
			JOptionPane.showMessageDialog(null, "Ocorreu um erro ao tentar enviar valores pela USB" + e.getMessage());
			e.printStackTrace();
		}
	}
	
	public int sendFileData(){
		try {
			int qtdLines = 0;
			BufferedReader reader = new BufferedReader(new FileReader(fileFirmware));
			List<String> tmp = new ArrayList<String>();
			for(String line; (line = reader.readLine()) != null; ) {
				qtdLines++;
				if(line.contains(":")) {
					tmp.add(line.replace(":", ""));
				}else {
					tmp.add(line);
				}
				
			}
			setData(tmp);
			return qtdLines;
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Erro ao converter o arquivo em lista! " + e.getMessage());
			return -1;
		}
	}
	
	public List<String> getData() {
		return data;
	}

	public void setData(List<String> data) {
		this.data = data;
	}

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

	public JTextPane getTextPane() {
		return textPane;
	}

	public void setTextPane(JTextPane textPane) {
		this.textPane = textPane;
	}

	public File getFileFirmware() {
		return fileFirmware;
	}

	public void setFileFirmware(File fileFirmware) {
		this.fileFirmware = fileFirmware;
	}

	public Integer getIndexFrameParte() {
		return indexFrameParte;
	}

	public void setIndexFrameParte(Integer indexFrameParte) {
		this.indexFrameParte = indexFrameParte;
	}

	public SerialPort getSerialPort() {
		return serialPort;
	}

	public void setSerialPort(SerialPort serialPort) {
		this.serialPort = serialPort;
	}

	public String getReaded() {
		return this.readed;
	}
	
	public void setReaded(String readed) {
		this.readed = readed;
	}
}

