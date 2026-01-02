package com.bluechip.telematics.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.bluechip.telematics.model.SerialConfiguration;

import javax.swing.JTextField;

import jssc.SerialPortList;

/**
 * 
 * Classe cria a tela de selacao de serial e velodade 
 * @author Caio Jose Carriel
 * @version 2.0
 * @company BLUECHIP Electronics
 * @since 30/05/2020
 *
 */
public class SerialConfigWindow extends JDialog implements ActionListener {

	private final JPanel contentPanel = new JPanel();
	private JTextField tfBaudRate;
	private	JComboBox jCboSerial;
	private String[] portNames;
	
	
	private SerialConfiguration serialConfiguration;
	/**
	 * Cria a tela de selecao de serial
	 */
	public SerialConfigWindow() {
		setTitle("Configura\u00E7\u00F5es da Porta Serial");
		setModal(true);
		setBounds(100, 100, 300, 200);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			JLabel lblNewLabel = new JLabel("Portas Seriais dispon\u00EDveis:");
			lblNewLabel.setBounds(10, 11, 155, 14);
			contentPanel.add(lblNewLabel);
		}
		
		jCboSerial = new JComboBox();
		jCboSerial.setBounds(10, 28, 222, 20);
		contentPanel.add(jCboSerial);
		
		JLabel lblBaudRate = new JLabel("Baud Rate:");
		lblBaudRate.setBounds(10, 59, 222, 14);
		contentPanel.add(lblBaudRate);
		
		tfBaudRate = new JTextField();
		tfBaudRate.setText("115200");
		tfBaudRate.setBounds(10, 74, 222, 20);
		contentPanel.add(tfBaudRate);
		tfBaudRate.setColumns(10);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				okButton.addActionListener(this);
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancelar");
				cancelButton.setActionCommand("Cancel");
				cancelButton.addActionListener(this);
				buttonPane.add(cancelButton);
			}
		}
		
		portNames = SerialPortList.getPortNames();
		DefaultComboBoxModel<String> model = new DefaultComboBoxModel<String>();
		for(int i = 0; i < portNames.length; i++){
			model.addElement(portNames[i]);
		}
		model.addElement("/dev/ttyLP1");
		jCboSerial.setModel(model);
		
		setLocationRelativeTo(null);
	}
	
	@Override
	public void actionPerformed(ActionEvent evt) {
		if(evt.getActionCommand().equals("OK")){
			serialConfiguration = new SerialConfiguration();
			serialConfiguration.setBaudRate(Integer.parseInt(tfBaudRate.getText()));
			serialConfiguration.setSerialName((String) jCboSerial.getSelectedItem());
			dispose();
		}else{
			dispose();		
		}
			
		
	}

	public SerialConfiguration getSerialConfiguration() {
		return serialConfiguration;
	}

	public void setSerialConfiguration(SerialConfiguration serialConfiguration) {
		this.serialConfiguration = serialConfiguration;
	}
}
