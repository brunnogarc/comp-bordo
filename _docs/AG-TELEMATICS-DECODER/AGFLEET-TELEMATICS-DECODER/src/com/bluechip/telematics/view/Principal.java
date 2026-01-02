package com.bluechip.telematics.view;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.bluechip.telematics.controller.AgDecodeTelematics;
import com.bluechip.telematics.controller.DecodePRM;
import com.bluechip.telematics.controller.DecodePRMv2;
import com.bluechip.telematics.controller.DecodeTelematics;
import com.bluechip.telematics.controller.DecoderMSG;
import com.bluechip.telematics.controller.SerialAdapter;
import com.bluechip.telematics.controller.Utilitarios;
import com.bluechip.telematics.model.ConsolePrinter;
import com.bluechip.telematics.model.Device;
import com.bluechip.telematics.model.JTextFieldLimit;

import jssc.SerialPort;
import jssc.SerialPortException;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.Connection;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.Timer;
import javax.swing.border.TitledBorder;
import javax.swing.UIManager;
import javax.swing.JTextField;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.JSeparator;

/**
 * 
 * Classe principal do projeto
 * @author Caio Jose Carriel
 * @version 2.0
 * @company BLUECHIP Electronics
 * @since 30/05/2020
 *
 */

public class Principal extends JFrame implements ActionListener, Observer{

	private SerialAdapter serialAdapter;
	private static Connection conn;
	private boolean confgPortSerial = false;
	private boolean BT_AUTENTICADO = false;
	private boolean SERVER_CONECTED = false;
	private JLabel lblStatus, txtAtualizacaoLoadFile;
	
	private JButton btnDesconectar, btnConfigSerial, btnInitUpdate;
	
	private boolean initUpdateOTA = false;
	private int SIZE_PACKET_BT = 1024;
	private int SIZE_PACKET_UART = 110; //110
	
	//private int TIME_ENTRY_PKT_BT = 350;
	//private int TIME_ENTRY_PKT_UART = 150;
	
	private int SIZE_PACKET_USAGE = 1024;
	//private int TIME_ENTRY_PKT_USAGE = 150;
	
	private JTextPane textPaneAtulizacao;
	
	private String bt_seed = "";
	private String bt_data = "";
	private String bt_prm = "";
	private JTextField txtSerial;
	private JTextField txtVerFW;
	private JTextField txtVerProtocol;
	private JTextField txtQtdMsgFLASH;
	private JTextField txtMotorON;
	private JTextField txtIgnicaoON;
	private JTextField txtGpsON;
	private JTextField txtTimestamp;
	private JTextField txtHorimetro;
	private JTextField txtLatitude;
	private JTextField txtLongitude;
	private JTextField txtVelocidade;
	private JTextField txtBussola;
	private JTextField txtRotacaoAtual;
	private JTextField txtRPM;
	private JTextField txtCmdLivre;
	
	private DecoderMSG dc;
	
	private boolean saveSERVER = false, saveWifi = false;
	private boolean arquivoCarregado = false;
	private File inputFileCargaUpdate;
	
	long totalFileSize = 0;
	long totalSendFile = 0;
	private JProgressBar progressBar;
	private JTextField txtHoursCount;
	private JTextField txtReportCycle;
	private JTextField txtReportCycleSpeed;
	private JTextField txtSendEntryMSG;
	private JTextField txtTimeMaxAUTH;
	private JTextField txtTimeRcvACK;
	private JTextField txtNameBLE;
	private JTextField txtSpeedReport;
	private JTextField txtAngCOMPASS;
	private JTextField txtConfCOMPASS;
	private JTextField txtFConfCOMPASS;
	private JTextField txtCodUSER;
	private JTextField txtSetCodUSER;
	private JTextField keyB1;
	private JTextField keyB2;
	private JTextField keyB3;
	private JTextField keyB4;
	
	private String k1 = null;
	private String k2 = null;
	private String k3 = null;
	private String k4 = null;
	private JTextField txtSSID01;
	private JTextField txtPASS01;
	private JTextField txtSSID02;
	private JTextField txtSSID03;
	private JTextField txtSSID04;
	private JTextField txtSSID05;
	private JTextField txtPASS02;
	private JTextField txtPASS03;
	private JTextField txtPASS04;
	private JTextField txtPASS05;
	private JTextField txtIP01;
	private JTextField txtIP02;
	private JTextField txtIP03;
	private JTextField txtIP04;
	private JTextField txtIP05;
	private JTextField txtPORTA04;
	private JTextField txtPORTA03;
	private JTextField txtPORTA02;
	private JTextField txtPORTA01;
	private JTextField txtPORTA05;
	private JTextField txtReportCycleIgOFF;
	private JTextField txtTimeCheckSERVER;
	
	private JTextField txtBateria;
	private JTextField txtVerCarga;
	private JTextField txtTipoMSG;
	private JTextField txtTipoCarga;
	private JTextField txtIdCarga;
	private JTextField txtIdAlerta;
	private JTextField txtTotComb;
	private JTextField txtOdometro;
	private JTextField txtPedalAcelerador;
	private JTextField txtTorqueMotor;
	private JTextField txtCargaMotor;
	private JTextField txtPressaoTurbo;
	private JTextField txtPressaoAdmissao;
	private JTextField txtPressaoOleo;
	private JTextField txtPressaoTrans;
	private JTextField txtPressaoCombustivel;
	private JTextField txtTempOleo;
	private JTextField txtTempAgua;
	private JTextField txtTempAdmissao;
	private JTextField txtTempAmbiente;
	private JTextField txtTempTrans;
	private JTextField txtTempFluidoHidra;
	private JTextField txtTempCombustivel;
	private JTextField txtVazaoComb;
	private JTextField txtNivelComb;
	private JTextField txtNivelOleoTrans;
	private JTextField txtNivelFluidoHidra;
	private JTextField txtDTC;
	private JTextField txtAlturaImpl;
	private JTextField txtVelColheita;
	private JTextField txtTomadaForca;
	private JTextField txtPiloto;
	private JTextField txtDescargaGrao;
	private JTextField txtFUN;
	private JTextField txtIndustria;
	private JTextField txtStsColheita;
	private JTextField txtPlataforma;
	private JTextField txtEmbalar;
	private JTextField txtBombaAgua;
	private JTextField txtTaxaAplic;
	private JTextField txtLibLiquido;
	private JTextField txtBicoPulvE3;
	private JTextField txtBicoPulvE2;
	private JTextField txtBicoPulvE1;
	private JTextField txtBicoPulvCT;
	private JTextField txtBicoPulvD1;
	private JTextField txtBicoPulvD2;
	private JTextField txtBicoPulvD3;
	private JTextField txtDecodeFrame;
	private JTextField txtCorteBase;
	private JTextField txtExtratorPrimario;
	private JTextField txtExtratorSecundario;
	private JTextField txtEsteiraElevador;
	private JTextField txtHorimetroElvador;
	private JTextField txtHorimetroPiloto;
	private JTextField txtPressaoCorteBase;
	private JTextField txtPressaoPicador;
	private JTextField txtVelExtratorPrimario;
	private JTextField txtAlturaCorteBase;
	
	/**
	 * Create the frame.
	 */
	public Principal() {
		setTitle("AG - BLUE Telematics - V3.0.0");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1100, 775);
		getContentPane().setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Status:");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblNewLabel.setForeground(new Color(0, 0, 0));
		lblNewLabel.setBounds(12, 13, 56, 16);
		getContentPane().add(lblNewLabel);
		
		lblStatus = new JLabel("Serial n\u00E3o configurada!");
		lblStatus.setForeground(Color.RED);
		lblStatus.setBounds(66, 13, 279, 16);
		getContentPane().add(lblStatus);
		
		btnConfigSerial = new JButton("Conectar");
		btnConfigSerial.setBounds(947, 13, 135, 25);
		btnConfigSerial.setActionCommand("config-Serial");
		btnConfigSerial.addActionListener(this);
		getContentPane().add(btnConfigSerial);
		
		btnDesconectar = new JButton("Desconectar");
		btnDesconectar.setEnabled(false);
		btnDesconectar.setForeground(Color.RED);
		btnDesconectar.setBounds(947, 51, 135, 25);
		btnDesconectar.setActionCommand("desconectar-serial");
		btnDesconectar.addActionListener(this);
		getContentPane().add(btnDesconectar);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(8, 50, 633, 270);
		getContentPane().add(scrollPane);
		
		textPaneAtulizacao = new JTextPane();
		scrollPane.setViewportView(textPaneAtulizacao);
		
		JButton btnLimpar = new JButton("Limpar");
		btnLimpar.setToolTipText("LIMPA A JANELA DE LOG");
		btnLimpar.setActionCommand("limpar");
		btnLimpar.setBounds(505, 9, 135, 25);
		btnLimpar.addActionListener(this);
		getContentPane().add(btnLimpar);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(645, 89, 437, 639);
		getContentPane().add(tabbedPane);
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Decodificado", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		tabbedPane.addTab("DECODER", null, panel, null);
		panel.setLayout(null);
		
		JLabel lblNewLabel_1 = new JLabel("Serial");
		lblNewLabel_1.setBounds(12, 20, 56, 16);
		panel.add(lblNewLabel_1);
		
		txtSerial = new JTextField();
		txtSerial.setBounds(12, 37, 182, 22);
		panel.add(txtSerial);
		txtSerial.setColumns(10);
		
		JLabel lblNewLabel_1_1 = new JLabel("FW");
		lblNewLabel_1_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1_1.setBounds(12, 68, 50, 16);
		panel.add(lblNewLabel_1_1);
		
		txtVerFW = new JTextField();
		txtVerFW.setColumns(10);
		txtVerFW.setBounds(12, 85, 50, 20);
		panel.add(txtVerFW);
		
		txtVerProtocol = new JTextField();
		txtVerProtocol.setColumns(10);
		txtVerProtocol.setBounds(70, 85, 50, 20);
		panel.add(txtVerProtocol);
		
		JLabel lblNewLabel_1_1_1 = new JLabel("PROT");
		lblNewLabel_1_1_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1_1_1.setBounds(70, 68, 50, 16);
		panel.add(lblNewLabel_1_1_1);
		
		JLabel lblNewLabel_1_1_2 = new JLabel("QTD. MSG FLASH");
		lblNewLabel_1_1_2.setBounds(12, 153, 124, 16);
		panel.add(lblNewLabel_1_1_2);
		
		txtQtdMsgFLASH = new JTextField();
		txtQtdMsgFLASH.setColumns(10);
		txtQtdMsgFLASH.setBounds(12, 173, 182, 20);
		panel.add(txtQtdMsgFLASH);
		
		JLabel lblNewLabel_1_1_2_1 = new JLabel("ENG_ON");
		lblNewLabel_1_1_2_1.setBounds(12, 207, 56, 16);
		panel.add(lblNewLabel_1_1_2_1);
		
		JLabel lblNewLabel_1_1_2_1_1 = new JLabel("IGN_ON");
		lblNewLabel_1_1_2_1_1.setBounds(12, 234, 56, 16);
		panel.add(lblNewLabel_1_1_2_1_1);
		
		txtMotorON = new JTextField();
		txtMotorON.setColumns(10);
		txtMotorON.setBounds(80, 205, 114, 20);
		panel.add(txtMotorON);
		
		txtIgnicaoON = new JTextField();
		txtIgnicaoON.setColumns(10);
		txtIgnicaoON.setBounds(80, 232, 114, 20);
		panel.add(txtIgnicaoON);
		
		JLabel lblNewLabel_1_1_2_1_1_1 = new JLabel("STS_GPS");
		lblNewLabel_1_1_2_1_1_1.setBounds(12, 262, 56, 16);
		panel.add(lblNewLabel_1_1_2_1_1_1);
		
		txtGpsON = new JTextField();
		txtGpsON.setColumns(10);
		txtGpsON.setBounds(80, 260, 114, 20);
		panel.add(txtGpsON);
		
		JLabel lblNewLabel_1_1_2_2 = new JLabel("TIMESTAMP");
		lblNewLabel_1_1_2_2.setBounds(12, 288, 124, 16);
		panel.add(lblNewLabel_1_1_2_2);
		
		txtTimestamp = new JTextField();
		txtTimestamp.setColumns(10);
		txtTimestamp.setBounds(12, 308, 182, 22);
		panel.add(txtTimestamp);
		
		JLabel lblNewLabel_1_1_2_3 = new JLabel("HORIMETRO MOTOR (3 min.)");
		lblNewLabel_1_1_2_3.setBounds(12, 343, 182, 16);
		panel.add(lblNewLabel_1_1_2_3);
		
		txtHorimetro = new JTextField();
		txtHorimetro.setColumns(10);
		txtHorimetro.setBounds(12, 363, 182, 22);
		panel.add(txtHorimetro);
		
		JLabel lblNewLabel_1_1_2_4 = new JLabel("LATITUDE");
		lblNewLabel_1_1_2_4.setBounds(12, 395, 124, 16);
		panel.add(lblNewLabel_1_1_2_4);
		
		txtLatitude = new JTextField();
		txtLatitude.setColumns(10);
		txtLatitude.setBounds(12, 415, 182, 22);
		panel.add(txtLatitude);
		
		JLabel lblNewLabel_1_1_2_5 = new JLabel("LONGITUDE");
		lblNewLabel_1_1_2_5.setBounds(12, 450, 124, 16);
		panel.add(lblNewLabel_1_1_2_5);
		
		txtLongitude = new JTextField();
		txtLongitude.setColumns(10);
		txtLongitude.setBounds(12, 470, 182, 22);
		panel.add(txtLongitude);
		
		JLabel lblNewLabel_1_1_3 = new JLabel("SPEED");
		lblNewLabel_1_1_3.setBounds(12, 507, 56, 16);
		panel.add(lblNewLabel_1_1_3);
		
		txtVelocidade = new JTextField();
		txtVelocidade.setColumns(10);
		txtVelocidade.setBounds(12, 523, 80, 22);
		panel.add(txtVelocidade);
		
		JLabel lblNewLabel_1_1_1_1 = new JLabel("COMPASS");
		lblNewLabel_1_1_1_1.setBounds(230, 450, 66, 16);
		panel.add(lblNewLabel_1_1_1_1);
		
		txtBussola = new JTextField();
		txtBussola.setColumns(10);
		txtBussola.setBounds(230, 470, 80, 22);
		panel.add(txtBussola);
		
		JLabel lblNewLabel_1_1_1_1_1 = new JLabel("ID USER");
		lblNewLabel_1_1_1_1_1.setBounds(230, 507, 96, 16);
		panel.add(lblNewLabel_1_1_1_1_1);
		
		txtCodUSER = new JTextField();
		txtCodUSER.setBounds(230, 523, 190, 22);
		panel.add(txtCodUSER);
		txtCodUSER.setColumns(10);
		
		txtSetCodUSER = new JTextField();
		txtSetCodUSER.setBounds(230, 37, 190, 22);
		panel.add(txtSetCodUSER);
		txtSetCodUSER.setColumns(10);
		txtSetCodUSER.setDocument(new JTextFieldLimit(16, true));
		txtSetCodUSER.setText("0001020304050607");
		
		JLabel lblNewLabel_2 = new JLabel("C\u00D3DIGO USU\u00C1RIO - ID (8 bytes)");
		lblNewLabel_2.setBounds(230, 20, 190, 16);
		panel.add(lblNewLabel_2);
		
		JButton btnSetCodUSER = new JButton("Informar");
		btnSetCodUSER.setActionCommand("CMD_SET_COD_USER");
		btnSetCodUSER.addActionListener(this);
		btnSetCodUSER.setBounds(305, 65, 97, 25);
		panel.add(btnSetCodUSER);
		
		JButton btnStartREPORT = new JButton("Iniciar REPORT");
		btnStartREPORT.setBounds(300, 210, 120, 25);
		btnStartREPORT.setActionCommand("CMD_SET_START_REPORT");
		btnStartREPORT.addActionListener(this);
		panel.add(btnStartREPORT);
		
		JButton btnStopREPORT = new JButton("Parar REPORT");
		btnStopREPORT.setBounds(300, 240, 120, 25);
		btnStopREPORT.setActionCommand("CMD_SET_STOP_REPORT");
		btnStopREPORT.addActionListener(this);
		panel.add(btnStopREPORT);
		
		JButton btnStartPRM = new JButton("Iniciar PRM");
		btnStartPRM.setBounds(300, 278, 120, 25);
		btnStartPRM.setActionCommand("CMD_SET_START_PRM");
		btnStartPRM.addActionListener(this);
		panel.add(btnStartPRM);
		
		JButton btnStopPRM = new JButton("Parar PRM");
		btnStopPRM.setBounds(300, 308, 120, 25);
		btnStopPRM.setActionCommand("CMD_SET_STOP_PRM");
		btnStopPRM.addActionListener(this);
		panel.add(btnStopPRM);
		
		JButton btnRpmON = new JButton("RPM ON");
		btnRpmON.setBounds(334, 362, 85, 25);
		btnRpmON.setActionCommand("CMD_SET_RPM_ON");
		btnRpmON.addActionListener(this);
		panel.add(btnRpmON);
		
		JButton btnRpmOFF = new JButton("RPM OFF");
		btnRpmOFF.setBounds(334, 391, 85, 25);
		btnRpmOFF.setActionCommand("CMD_SET_RPM_OFF");
		btnRpmOFF.addActionListener(this);
		panel.add(btnRpmOFF);
		
		JButton btnSerial = new JButton("SERIAL");
		btnSerial.setToolTipText("RETORNA O NUMERO DE SERIE DA INTERFACE");
		btnSerial.setActionCommand("CMD_GET_SERIAL");
		btnSerial.addActionListener(this);
		btnSerial.setBounds(300, 160, 120, 25);
		panel.add(btnSerial);
		
		JButton btnNewButton = new JButton("RESET");
		btnNewButton.setToolTipText("REINICIA A INTERFACE");
		btnNewButton.setActionCommand("CMD_RST");
		btnNewButton.addActionListener(this);
		btnNewButton.setBounds(300, 131, 120, 25);
		panel.add(btnNewButton);
		
		JLabel lblNewLabel_1_1_3_1 = new JLabel("RPM");
		lblNewLabel_1_1_3_1.setBounds(114, 507, 56, 16);
		panel.add(lblNewLabel_1_1_3_1);
		
		txtRPM = new JTextField();
		txtRPM.setColumns(10);
		txtRPM.setBounds(114, 523, 80, 22);
		panel.add(txtRPM);
		
		txtBateria = new JTextField();
		txtBateria.setBounds(334, 471, 86, 20);
		panel.add(txtBateria);
		txtBateria.setColumns(10);
		
		JLabel lblNewLabel_5 = new JLabel("BATERIA (VOLTS)");
		lblNewLabel_5.setBounds(334, 451, 85, 14);
		panel.add(lblNewLabel_5);
		
		txtVerCarga = new JTextField();
		txtVerCarga.setBounds(130, 127, 50, 20);
		panel.add(txtVerCarga);
		txtVerCarga.setColumns(10);
		
		txtTipoMSG = new JTextField();
		txtTipoMSG.setBounds(130, 85, 70, 20);
		panel.add(txtTipoMSG);
		txtTipoMSG.setColumns(10);
		
		JLabel lblNewLabel_6 = new JLabel("TIPO MSG");
		lblNewLabel_6.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_6.setBounds(130, 69, 70, 14);
		panel.add(lblNewLabel_6);
		
		JLabel lblNewLabel_7 = new JLabel("CARGA");
		lblNewLabel_7.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_7.setBounds(133, 113, 50, 14);
		panel.add(lblNewLabel_7);
		
		txtTipoCarga = new JTextField();
		txtTipoCarga.setBounds(12, 127, 50, 20);
		panel.add(txtTipoCarga);
		txtTipoCarga.setColumns(10);
		
		JLabel lblNewLabel_8 = new JLabel("TP CARGA");
		lblNewLabel_8.setBounds(12, 113, 50, 14);
		panel.add(lblNewLabel_8);
		
		txtIdCarga = new JTextField();
		txtIdCarga.setBounds(70, 127, 50, 20);
		panel.add(txtIdCarga);
		txtIdCarga.setColumns(10);
		
		JLabel lblNewLabel_9 = new JLabel("ID CARGA");
		lblNewLabel_9.setBounds(70, 113, 50, 14);
		panel.add(lblNewLabel_9);
		
		txtIdAlerta = new JTextField();
		txtIdAlerta.setBounds(205, 85, 90, 20);
		panel.add(txtIdAlerta);
		txtIdAlerta.setColumns(10);
		
		JLabel lblNewLabel_10 = new JLabel("ID ALERT");
		lblNewLabel_10.setBounds(205, 69, 70, 14);
		panel.add(lblNewLabel_10);
		
		JButton btnDemoOn = new JButton("DEMO ON");
		btnDemoOn.setActionCommand("CMD_SET_DEMO_ON");
		btnDemoOn.setBounds(230, 363, 85, 25);
		btnDemoOn.addActionListener(this);
		panel.add(btnDemoOn);
		
		JButton btnDemoOff = new JButton("DEMO OFF");
		btnDemoOff.setActionCommand("CMD_SET_DEMO_OFF");
		btnDemoOff.setBounds(230, 392, 85, 25);
		btnDemoOff.addActionListener(this);
		panel.add(btnDemoOff);
		
		JLabel lblNewLabel_11_1_7_1_3_2 = new JLabel("HORIMETRO ELEVADOR");
		lblNewLabel_11_1_7_1_3_2.setBounds(12, 566, 120, 14);
		panel.add(lblNewLabel_11_1_7_1_3_2);
		
		txtHorimetroElvador = new JTextField();
		txtHorimetroElvador.setBounds(133, 563, 83, 20);
		panel.add(txtHorimetroElvador);
		txtHorimetroElvador.setColumns(10);
		
		JLabel lblNewLabel_11_1_7_1_3_2_1 = new JLabel("HORIMETRO PILOTO");
		lblNewLabel_11_1_7_1_3_2_1.setBounds(227, 566, 110, 14);
		panel.add(lblNewLabel_11_1_7_1_3_2_1);
		
		txtHorimetroPiloto = new JTextField();
		txtHorimetroPiloto.setBounds(335, 563, 83, 20);
		panel.add(txtHorimetroPiloto);
		txtHorimetroPiloto.setColumns(10);
		
		JLabel lblNewLabel_1_1_2_1_2 = new JLabel("CONEXAO");
		lblNewLabel_1_1_2_1_2.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1_1_2_1_2.setBounds(201, 115, 95, 16);
		panel.add(lblNewLabel_1_1_2_1_2);
		
		txtConexao = new JTextField();
		txtConexao.setColumns(10);
		txtConexao.setBounds(201, 134, 95, 20);
		panel.add(txtConexao);
		
		txtSinalConexao = new JTextField();
		txtSinalConexao.setColumns(10);
		txtSinalConexao.setBounds(201, 180, 95, 20);
		panel.add(txtSinalConexao);
		
		JLabel lblNewLabel_1_1_2_1_2_1 = new JLabel("SINAL CONEXAO");
		lblNewLabel_1_1_2_1_2_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1_1_2_1_2_1.setBounds(201, 160, 95, 16);
		panel.add(lblNewLabel_1_1_2_1_2_1);
		
		JLabel lblNewLabel_1_1_2_1_2_2 = new JLabel("FIX GPS");
		lblNewLabel_1_1_2_1_2_2.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1_1_2_1_2_2.setBounds(201, 205, 95, 16);
		panel.add(lblNewLabel_1_1_2_1_2_2);
		
		txtFixGPS = new JTextField();
		txtFixGPS.setColumns(10);
		txtFixGPS.setBounds(201, 220, 95, 20);
		panel.add(txtFixGPS);
		
		JLabel lblNewLabel_1_1_2_1_2_3 = new JLabel("HDOP");
		lblNewLabel_1_1_2_1_2_3.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1_1_2_1_2_3.setBounds(201, 245, 95, 16);
		panel.add(lblNewLabel_1_1_2_1_2_3);
		
		txtHDOP = new JTextField();
		txtHDOP.setColumns(10);
		txtHDOP.setBounds(201, 261, 95, 20);
		panel.add(txtHDOP);
		
		JLabel lblNewLabel_1_1_2_1_2_4 = new JLabel("QTD. SAT");
		lblNewLabel_1_1_2_1_2_4.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1_1_2_1_2_4.setBounds(201, 288, 95, 16);
		panel.add(lblNewLabel_1_1_2_1_2_4);
		
		txtQtdSat = new JTextField();
		txtQtdSat.setColumns(10);
		txtQtdSat.setBounds(201, 307, 95, 20);
		panel.add(txtQtdSat);
				
		JPanel panel_2 = new JPanel();
		tabbedPane.addTab("COMANDOS", null, panel_2, null);
		panel_2.setLayout(null);
		
		JPanel panel_1 = new JPanel();
		tabbedPane.addTab("WIFI / SERVER", null, panel_1, null);
		panel_1.setLayout(null);
		
		JPanel panel_6 = new JPanel();
		panel_6.setBorder(new TitledBorder(null, "WIFI", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_6.setBounds(12, 51, 408, 188);
		panel_1.add(panel_6);
		panel_6.setLayout(null);
		
		JButton btnLoadListWIFI = new JButton("CARREGAR");
		btnLoadListWIFI.setBounds(299, 62, 97, 25);
		btnLoadListWIFI.setActionCommand("CMD_LOAD_ALL_WIFI");
		btnLoadListWIFI.addActionListener(this);
		panel_6.add(btnLoadListWIFI);
		
		JButton btnSaveListWIFI = new JButton("SALVAR");
		btnSaveListWIFI.setBounds(299, 149, 97, 25);
		btnSaveListWIFI.setActionCommand("CMD_SAVE_ALL_WIFI");
		btnSaveListWIFI.addActionListener(this);
		panel_6.add(btnSaveListWIFI);
		
		JLabel lblNewLabel_3 = new JLabel("01");
		lblNewLabel_3.setBounds(12, 37, 16, 16);
		panel_6.add(lblNewLabel_3);
		
		JLabel lblNewLabel_3_1 = new JLabel("02");
		lblNewLabel_3_1.setBounds(12, 66, 16, 16);
		panel_6.add(lblNewLabel_3_1);
		
		JLabel lblNewLabel_3_1_1 = new JLabel("03");
		lblNewLabel_3_1_1.setBounds(12, 95, 16, 16);
		panel_6.add(lblNewLabel_3_1_1);
		
		JLabel lblNewLabel_3_1_2 = new JLabel("04");
		lblNewLabel_3_1_2.setBounds(12, 124, 16, 16);
		panel_6.add(lblNewLabel_3_1_2);
		
		JLabel lblNewLabel_3_1_3 = new JLabel("05");
		lblNewLabel_3_1_3.setBounds(12, 153, 16, 16);
		panel_6.add(lblNewLabel_3_1_3);
		
		txtSSID01 = new JTextField();
		txtSSID01.setBounds(36, 34, 116, 22);
		panel_6.add(txtSSID01);
		txtSSID01.setColumns(10);
		
		txtPASS01 = new JTextField();
		txtPASS01.setColumns(10);
		txtPASS01.setBounds(164, 34, 116, 22);
		panel_6.add(txtPASS01);
		
		txtSSID02 = new JTextField();
		txtSSID02.setColumns(10);
		txtSSID02.setBounds(36, 63, 116, 22);
		panel_6.add(txtSSID02);
		
		txtSSID03 = new JTextField();
		txtSSID03.setColumns(10);
		txtSSID03.setBounds(36, 92, 116, 22);
		panel_6.add(txtSSID03);
		
		txtSSID04 = new JTextField();
		txtSSID04.setColumns(10);
		txtSSID04.setBounds(36, 121, 116, 22);
		panel_6.add(txtSSID04);
		
		txtSSID05 = new JTextField();
		txtSSID05.setColumns(10);
		txtSSID05.setBounds(36, 150, 116, 22);
		panel_6.add(txtSSID05);
		
		txtPASS02 = new JTextField();
		txtPASS02.setColumns(10);
		txtPASS02.setBounds(164, 63, 116, 22);
		panel_6.add(txtPASS02);
		
		txtPASS03 = new JTextField();
		txtPASS03.setColumns(10);
		txtPASS03.setBounds(164, 92, 116, 22);
		panel_6.add(txtPASS03);
		
		txtPASS04 = new JTextField();
		txtPASS04.setColumns(10);
		txtPASS04.setBounds(164, 121, 116, 22);
		panel_6.add(txtPASS04);
		
		txtPASS05 = new JTextField();
		txtPASS05.setColumns(10);
		txtPASS05.setBounds(164, 150, 116, 22);
		panel_6.add(txtPASS05);
		
		JLabel lblNewLabel_4 = new JLabel("SSID");
		lblNewLabel_4.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblNewLabel_4.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_4.setBounds(67, 17, 56, 16);
		panel_6.add(lblNewLabel_4);
		
		JLabel lblNewLabel_4_1 = new JLabel("PASS");
		lblNewLabel_4_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_4_1.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblNewLabel_4_1.setBounds(191, 17, 56, 16);
		panel_6.add(lblNewLabel_4_1);
		
		JButton btnListWIFI = new JButton("Listar");
		btnListWIFI.setBounds(299, 33, 97, 25);
		btnListWIFI.setActionCommand("CMD_WIFI_LIST");
		btnListWIFI.addActionListener(this);
		panel_6.add(btnListWIFI);
		
		JPanel panel_6_1 = new JPanel();
		panel_6_1.setLayout(null);
		panel_6_1.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "SERVER", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel_6_1.setBounds(12, 245, 408, 188);
		panel_1.add(panel_6_1);
		
		JButton btnLoadListSERVER = new JButton("CARREGAR");
		btnLoadListSERVER.setBounds(299, 61, 97, 25);
		btnLoadListSERVER.setActionCommand("CMD_LOAD_ALL_SERVER");
		btnLoadListSERVER.addActionListener(this);
		panel_6_1.add(btnLoadListSERVER);
		
		JButton btnSaveListSERVER = new JButton("SALVAR");
		btnSaveListSERVER.setBounds(299, 148, 97, 25);
		btnSaveListSERVER.setActionCommand("CMD_SAVE_ALL_SERVER");
		btnSaveListSERVER.addActionListener(this);
		panel_6_1.add(btnSaveListSERVER);
		
		JLabel lblNewLabel_3_2 = new JLabel("01");
		lblNewLabel_3_2.setBounds(12, 36, 16, 16);
		panel_6_1.add(lblNewLabel_3_2);
		
		JLabel lblNewLabel_3_1_4 = new JLabel("02");
		lblNewLabel_3_1_4.setBounds(12, 65, 16, 16);
		panel_6_1.add(lblNewLabel_3_1_4);
		
		JLabel lblNewLabel_3_1_1_1 = new JLabel("03");
		lblNewLabel_3_1_1_1.setBounds(12, 94, 16, 16);
		panel_6_1.add(lblNewLabel_3_1_1_1);
		
		JLabel lblNewLabel_3_1_2_1 = new JLabel("04");
		lblNewLabel_3_1_2_1.setBounds(12, 123, 16, 16);
		panel_6_1.add(lblNewLabel_3_1_2_1);
		
		JLabel lblNewLabel_3_1_3_1 = new JLabel("05");
		lblNewLabel_3_1_3_1.setBounds(12, 152, 16, 16);
		panel_6_1.add(lblNewLabel_3_1_3_1);
		
		txtIP01 = new JTextField();
		txtIP01.setColumns(10);
		txtIP01.setBounds(36, 33, 116, 22);
		panel_6_1.add(txtIP01);
		
		txtIP02 = new JTextField();
		txtIP02.setColumns(10);
		txtIP02.setBounds(36, 62, 116, 22);
		panel_6_1.add(txtIP02);
		
		txtIP03 = new JTextField();
		txtIP03.setColumns(10);
		txtIP03.setBounds(36, 91, 116, 22);
		panel_6_1.add(txtIP03);
		
		txtIP04 = new JTextField();
		txtIP04.setColumns(10);
		txtIP04.setBounds(36, 120, 116, 22);
		panel_6_1.add(txtIP04);
		
		txtIP05 = new JTextField();
		txtIP05.setColumns(10);
		txtIP05.setBounds(36, 149, 116, 22);
		panel_6_1.add(txtIP05);
		
		JLabel lblNewLabel_4_2 = new JLabel("IP");
		lblNewLabel_4_2.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_4_2.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblNewLabel_4_2.setBounds(68, 13, 56, 16);
		panel_6_1.add(lblNewLabel_4_2);
		
		JLabel lblNewLabel_4_1_1 = new JLabel("PORTA");
		lblNewLabel_4_1_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_4_1_1.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblNewLabel_4_1_1.setBounds(192, 13, 56, 16);
		panel_6_1.add(lblNewLabel_4_1_1);
		
		txtPORTA04 = new JTextField();
		txtPORTA04.setBounds(164, 120, 116, 22);
		panel_6_1.add(txtPORTA04);
		txtPORTA04.setColumns(10);
		
		txtPORTA03 = new JTextField();
		txtPORTA03.setColumns(10);
		txtPORTA03.setBounds(164, 91, 116, 22);
		panel_6_1.add(txtPORTA03);
		
		txtPORTA02 = new JTextField();
		txtPORTA02.setColumns(10);
		txtPORTA02.setBounds(164, 62, 116, 22);
		panel_6_1.add(txtPORTA02);
		
		txtPORTA01 = new JTextField();
		txtPORTA01.setColumns(10);
		txtPORTA01.setBounds(164, 33, 116, 22);
		panel_6_1.add(txtPORTA01);
		
		txtPORTA05 = new JTextField();
		txtPORTA05.setColumns(10);
		txtPORTA05.setBounds(164, 149, 116, 22);
		panel_6_1.add(txtPORTA05);
		
		JButton btnListSERVER = new JButton("Listar");
		btnListSERVER.setBounds(299, 32, 97, 25);
		btnListSERVER.setActionCommand("CMD_SERVER_LIST");
		btnListSERVER.addActionListener(this);
		panel_6_1.add(btnListSERVER);
		
		JButton btnWifiON = new JButton("Ativar");
		btnWifiON.setForeground(new Color(0, 128, 0));
		btnWifiON.setFont(new Font("Tahoma", Font.BOLD, 13));
		btnWifiON.setActionCommand("CMD_WIFI_ON");
		btnWifiON.addActionListener(this);
		btnWifiON.setBounds(12, 13, 100, 25);
		panel_1.add(btnWifiON);
		
		JButton btnWifiOFF = new JButton("Desativar");
		btnWifiOFF.setFont(new Font("Tahoma", Font.BOLD, 13));
		btnWifiOFF.setForeground(Color.RED);
		btnWifiOFF.setActionCommand("CMD_WIFI_OFF");
		btnWifiOFF.addActionListener(this);
		btnWifiOFF.setBounds(122, 13, 100, 25);
		panel_1.add(btnWifiOFF);
		
		txtTimeCheckSERVER = new JTextField();
		txtTimeCheckSERVER.setBounds(12, 439, 70, 22);
		panel_1.add(txtTimeCheckSERVER);
		txtTimeCheckSERVER.setColumns(10);
		
		JButton btnSetCheckSERVER = new JButton("SET TIME CHECK CONF");
		btnSetCheckSERVER.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnSetCheckSERVER.setActionCommand("CMD_SET_CHECK_CONFIG_SERVER");
		btnSetCheckSERVER.addActionListener(this);
		btnSetCheckSERVER.setBounds(87, 438, 160, 25);
		panel_1.add(btnSetCheckSERVER);
		
		JButton btnGetCheckSERVER = new JButton("GET TIME CHECK CONF");
		btnGetCheckSERVER.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnGetCheckSERVER.setActionCommand("CMD_GET_CHECK_CONFIG_SERVER");
		btnGetCheckSERVER.addActionListener(this);
		btnGetCheckSERVER.setBounds(255, 438, 165, 25);
		panel_1.add(btnGetCheckSERVER);
		
		JButton btnStatusWIFI = new JButton("STS WIFI");
		btnStatusWIFI.setBounds(232, 14, 90, 25);
		btnStatusWIFI.setActionCommand("CMD_GET_STATUS_WIFI");
		btnStatusWIFI.addActionListener(this);
		panel_1.add(btnStatusWIFI);
		
		JButton btnCmdLivre = new JButton("Comando Livre");
		btnCmdLivre.setBounds(257, 540, 165, 23);
		btnCmdLivre.setActionCommand("CMD_LIVRE");
		btnCmdLivre.addActionListener(this);
		panel_1.add(btnCmdLivre);
		
		txtCmdLivre = new JTextField();
		txtCmdLivre.setBounds(14, 540, 230, 20);
		panel_1.add(txtCmdLivre);
		txtCmdLivre.setColumns(10);
		
		txtDecodeFrame = new JTextField();
		txtDecodeFrame.setColumns(10);
		txtDecodeFrame.setBounds(12, 578, 230, 20);
		panel_1.add(txtDecodeFrame);
		
		JButton btnDecodeFrame = new JButton("Decode Frame");
		btnDecodeFrame.setToolTipText("AT+BT_DATA=000000033111420001040000611f93e5f22bffe2e291b033ffff010d00000153ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff06");
		btnDecodeFrame.setActionCommand("CMD_DECODE_FRAME");
		btnDecodeFrame.addActionListener(this);
		btnDecodeFrame.setBounds(257, 577, 165, 23);
		panel_1.add(btnDecodeFrame);
		
		JButton btnStsConn = new JButton("STS CONN");
		btnStsConn.setActionCommand("CMD_GET_STATUS_CONN");
		btnStsConn.addActionListener(this);
		btnStsConn.setBounds(330, 15, 90, 25);
		panel_1.add(btnStsConn);
		
		JButton btnScanWifi = new JButton("SCAN WIFI");
		btnScanWifi.setActionCommand("CMD_SCAN_WIFI");
		btnScanWifi.addActionListener(this);
		btnScanWifi.setBounds(12, 472, 90, 25);
		panel_1.add(btnScanWifi);
		
		JPanel panel_4 = new JPanel();
		tabbedPane.addTab("FERRAMENTAS", null, panel_4, null);
		panel_4.setLayout(null);
		
		JPanel panel_5 = new JPanel();
		panel_5.setBounds(12, 13, 408, 112);
		panel_4.add(panel_5);
		panel_5.setLayout(null);
		
		JButton btnArquivo = new JButton("Arquivo");
		btnArquivo.setActionCommand("ATUALIZADOR-LOAD-FILE");
		btnArquivo.addActionListener(this);
		btnArquivo.setBounds(5, 10, 97, 25);
		panel_5.add(btnArquivo);
		
		txtAtualizacaoLoadFile = new JLabel("Carregar arquivo!");
		txtAtualizacaoLoadFile.setForeground(Color.RED);
		txtAtualizacaoLoadFile.setBounds(109, 13, 290, 16);
		panel_5.add(txtAtualizacaoLoadFile);
		
		btnInitUpdate = new JButton("Iniciar Atualiza\u00E7\u00E3o");
		btnInitUpdate.setEnabled(false);
		btnInitUpdate.setActionCommand("INIT-UPDATE");
		btnInitUpdate.addActionListener(this);
		btnInitUpdate.setBounds(130, 56, 174, 25);
		panel_5.add(btnInitUpdate);
		
		progressBar = new JProgressBar();
		progressBar.setBounds(6, 94, 395, 14);
		panel_5.add(progressBar);
		
		JPanel panel_8 = new JPanel();
		panel_8.setBorder(new TitledBorder(null, "Horimetro", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_8.setBounds(10, 128, 410, 310);
		panel_4.add(panel_8);
		panel_8.setLayout(null);
		
		txtHoursINIT = new JTextField();
		txtHoursINIT.setColumns(10);
		txtHoursINIT.setBounds(10, 38, 75, 22);
		panel_8.add(txtHoursINIT);
		
		JButton btnSetHoursINIT = new JButton("SET HOURS INIT");
		btnSetHoursINIT.setToolTipText("ALTERA O HORIMETRO INICIAL CONFIGURADO NA INTERFACE");
		btnSetHoursINIT.setActionCommand("CMD_SET_HOURS_INIT");
		btnSetHoursINIT.addActionListener(this);
		btnSetHoursINIT.setBounds(93, 37, 150, 25);
		panel_8.add(btnSetHoursINIT);
		
		JButton btnGetHoursINIT = new JButton("GET HOURS INIT");
		btnGetHoursINIT.setToolTipText("RETORNA O HORIMETRO INICIAL CONFIGURADO NA INTERFACE");
		btnGetHoursINIT.setActionCommand("CMD_GET_HOURS_INIT");
		btnGetHoursINIT.addActionListener(this);
		btnGetHoursINIT.setBounds(253, 37, 150, 25);
		panel_8.add(btnGetHoursINIT);
		
		txtHoursCOUNT = new JTextField();
		txtHoursCOUNT.setColumns(10);
		txtHoursCOUNT.setBounds(10, 68, 75, 22);
		panel_8.add(txtHoursCOUNT);
		
		JButton btnSetHoursCount = new JButton("SET HOURS COUNT");
		btnSetHoursCount.setToolTipText("ALTERA O HORIMETRO CONTABILIZADO PELA INTERFACE");
		btnSetHoursCount.setActionCommand("CMD_SET_HOURS_COUNT");
		btnSetHoursCount.addActionListener(this);
		btnSetHoursCount.setBounds(93, 67, 150, 25);
		panel_8.add(btnSetHoursCount);
		
		JButton btnGetHoursCount = new JButton("GET HOURS COUNT");
		btnGetHoursCount.setToolTipText("RETORNA O HORIMETRO CONTABILIZADO PELA INTERFACE");
		btnGetHoursCount.setActionCommand("CMD_GET_HOURS_COUNT");
		btnGetHoursCount.addActionListener(this);
		btnGetHoursCount.setBounds(253, 67, 150, 25);
		panel_8.add(btnGetHoursCount);
		
		JLabel lblNewLabel_11_8_1 = new JLabel("HORIMETRO DO MOTOR");
		lblNewLabel_11_8_1.setForeground(Color.RED);
		lblNewLabel_11_8_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_11_8_1.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblNewLabel_11_8_1.setBounds(10, 15, 233, 15);
		panel_8.add(lblNewLabel_11_8_1);
		
		JButton btnGetHoursNow = new JButton("GET HOURS NOW");
		btnGetHoursNow.setToolTipText("RETORNA O HORIMETRO CONTABILIZADO ATUAL (OFFSET + COUNT)");
		btnGetHoursNow.setActionCommand("CMD_GET_HOURS_NOW");
		btnGetHoursNow.addActionListener(this);
		btnGetHoursNow.setBounds(253, 8, 150, 25);
		panel_8.add(btnGetHoursNow);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(5, 100, 398, 2);
		panel_8.add(separator);
		
		JLabel lblNewLabel_11_8_1_1 = new JLabel("HORIMETRO ESTEIRA");
		lblNewLabel_11_8_1_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_11_8_1_1.setForeground(Color.RED);
		lblNewLabel_11_8_1_1.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblNewLabel_11_8_1_1.setBounds(10, 119, 233, 15);
		panel_8.add(lblNewLabel_11_8_1_1);
		
		txtHoursEsteiraINIT = new JTextField();
		txtHoursEsteiraINIT.setColumns(10);
		txtHoursEsteiraINIT.setBounds(10, 142, 75, 22);
		panel_8.add(txtHoursEsteiraINIT);
		
		txtHoursEsteiraCOUNT = new JTextField();
		txtHoursEsteiraCOUNT.setColumns(10);
		txtHoursEsteiraCOUNT.setBounds(10, 172, 75, 22);
		panel_8.add(txtHoursEsteiraCOUNT);
		
		JButton btnSetHoursEsteiraCount = new JButton("SET HOURS COUNT");
		btnSetHoursEsteiraCount.setToolTipText("ALTERA O HORIMETRO DO ELEVADOR CONTABILIZADO PELA INTERFACE");
		btnSetHoursEsteiraCount.setActionCommand("CMD_SET_HOURS_ESTEIRA_COUNT");
		btnSetHoursEsteiraCount.addActionListener(this);
		btnSetHoursEsteiraCount.setBounds(93, 171, 150, 25);
		panel_8.add(btnSetHoursEsteiraCount);
		
		JButton btnSetHoursEsteiraINIT = new JButton("SET HOURS INIT");
		btnSetHoursEsteiraINIT.setToolTipText("ALTERA O HORIMETRO DO ELEVADOR INICIAL CONFIGURADO NA INTERFACE");
		btnSetHoursEsteiraINIT.setActionCommand("CMD_SET_HOURS_ESTEIRA_INIT");
		btnSetHoursEsteiraINIT.addActionListener(this);
		btnSetHoursEsteiraINIT.setBounds(93, 141, 150, 25);
		panel_8.add(btnSetHoursEsteiraINIT);
		
		JButton btnGetHoursEsteiraNow = new JButton("GET HOURS NOW");
		btnGetHoursEsteiraNow.setToolTipText("RETORNA O HORIMETRO DO ELEVADOR CONTABILIZADO ATUAL (OFFSET + COUNT)");
		btnGetHoursEsteiraNow.setActionCommand("CMD_GET_HOURS_ESTEIRA_NOW");
		btnGetHoursEsteiraNow.addActionListener(this);
		btnGetHoursEsteiraNow.setBounds(253, 112, 150, 25);
		panel_8.add(btnGetHoursEsteiraNow);
		
		JButton btnGetHoursEsteiraINIT = new JButton("GET HOURS INIT");
		btnGetHoursEsteiraINIT.setToolTipText("RETORNA O HORIMETRO DO ELEVADOR INICIAL CONFIGURADO NA INTERFACE");
		btnGetHoursEsteiraINIT.setActionCommand("CMD_GET_HOURS_ESTEIRA_INIT");
		btnGetHoursEsteiraINIT.addActionListener(this);
		btnGetHoursEsteiraINIT.setBounds(253, 141, 150, 25);
		panel_8.add(btnGetHoursEsteiraINIT);
		
		JButton btnGetHoursEsteiraCount = new JButton("GET HOURS COUNT");
		btnGetHoursEsteiraCount.setToolTipText("RETORNA O HORIMETRO DO ELEVADOR CONTABILIZADO PELA INTERFACE");
		btnGetHoursEsteiraCount.setActionCommand("CMD_GET_HOURS_ESTEIRA_COUNT");
		btnGetHoursEsteiraCount.addActionListener(this);
		btnGetHoursEsteiraCount.setBounds(253, 171, 150, 25);
		panel_8.add(btnGetHoursEsteiraCount);
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(5, 204, 398, 2);
		panel_8.add(separator_1);
		
		JLabel lblNewLabel_11_8_1_2 = new JLabel("HORIMETRO PILOTO");
		lblNewLabel_11_8_1_2.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_11_8_1_2.setForeground(Color.RED);
		lblNewLabel_11_8_1_2.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblNewLabel_11_8_1_2.setBounds(10, 223, 233, 15);
		panel_8.add(lblNewLabel_11_8_1_2);
		
		txtHoursPilotoINIT = new JTextField();
		txtHoursPilotoINIT.setColumns(10);
		txtHoursPilotoINIT.setBounds(10, 246, 75, 22);
		panel_8.add(txtHoursPilotoINIT);
		
		txtHoursPilotoCOUNT = new JTextField();
		txtHoursPilotoCOUNT.setColumns(10);
		txtHoursPilotoCOUNT.setBounds(10, 276, 75, 22);
		panel_8.add(txtHoursPilotoCOUNT);
		
		JButton btnSetHoursPilotoCount = new JButton("SET HOURS COUNT");
		btnSetHoursPilotoCount.setToolTipText("ALTERA O HORIMETRO DO PILOTO CONTABILIZADO PELA INTERFACE");
		btnSetHoursPilotoCount.setActionCommand("CMD_SET_HOURS_PILOTO_COUNT");
		btnSetHoursPilotoCount.addActionListener(this);
		btnSetHoursPilotoCount.setBounds(93, 275, 150, 25);
		panel_8.add(btnSetHoursPilotoCount);
		
		JButton btnSetHoursPilotoINIT = new JButton("SET HOURS INIT");
		btnSetHoursPilotoINIT.setToolTipText("ALTERA O HORIMETRO DO PILOTO INICIAL CONFIGURADO NA INTERFACE");
		btnSetHoursPilotoINIT.setActionCommand("CMD_SET_HOURS_PILOTO_INIT");
		btnSetHoursPilotoINIT.addActionListener(this);
		btnSetHoursPilotoINIT.setBounds(93, 245, 150, 25);
		panel_8.add(btnSetHoursPilotoINIT);
		
		JButton btnGetHoursPilotoNow = new JButton("GET HOURS NOW");
		btnGetHoursPilotoNow.setToolTipText("RETORNA O HORIMETRO DO PILOTO CONTABILIZADO ATUAL (OFFSET + COUNT)");
		btnGetHoursPilotoNow.setActionCommand("CMD_GET_HOURS_PILOTO_NOW");
		btnGetHoursPilotoNow.addActionListener(this);
		btnGetHoursPilotoNow.setBounds(253, 216, 150, 25);
		panel_8.add(btnGetHoursPilotoNow);
		
		JButton btnGetHoursPilotoINIT = new JButton("GET HOURS INIT");
		btnGetHoursPilotoINIT.setToolTipText("RETORNA O HORIMETRO DO PILOTO INICIAL CONFIGURADO NA INTERFACE");
		btnGetHoursPilotoINIT.setActionCommand("CMD_GET_HOURS_PILOTO_INIT");
		btnGetHoursPilotoINIT.addActionListener(this);
		btnGetHoursPilotoINIT.setBounds(253, 245, 150, 25);
		panel_8.add(btnGetHoursPilotoINIT);
		
		JButton btnGetHoursPilotoCount = new JButton("GET HOURS COUNT");
		btnGetHoursPilotoCount.setToolTipText("RETORNA O HORIMETRO DO PILOTO CONTABILIZADO PELA INTERFACE");
		btnGetHoursPilotoCount.setActionCommand("CMD_GET_HOURS_PILOTO_COUNT");
		btnGetHoursPilotoCount.addActionListener(this);
		btnGetHoursPilotoCount.setBounds(253, 275, 150, 25);
		panel_8.add(btnGetHoursPilotoCount);
		
		JPanel panel_3 = new JPanel();
		panel_3.setBorder(new TitledBorder(null, "Sistema", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_3.setBounds(9, 0, 415, 50);
		panel_2.add(panel_3);
		panel_3.setLayout(null);
		
		JButton btnVerFirmware = new JButton("VER. FIRMWARE");
		btnVerFirmware.setToolTipText("RETORNA A VERSAO DO FIRMWARE DA INTERFACE");
		btnVerFirmware.setActionCommand("CMD_GET_VER_FW");
		btnVerFirmware.addActionListener(this);
		btnVerFirmware.setBounds(10, 15, 120, 25);
		panel_3.add(btnVerFirmware);
		
		JButton btnFactory = new JButton("FACTORY");
		btnFactory.setToolTipText("RESTAURA A INTERFACE PARA CONFIGURA\u00C7\u00C3O DE FABRICA");
		btnFactory.setActionCommand("CMD_FACTORY");
		btnFactory.addActionListener(this);
		btnFactory.setBounds(148, 15, 120, 25);
		panel_3.add(btnFactory);
		
		JButton btnFormatMem = new JButton("FORMAT MEM.");
		btnFormatMem.setToolTipText("FORMATA A MEMORIA INTERNA DA INTERFACE");
		btnFormatMem.setActionCommand("CMD_FORMAT_MEM");
		btnFormatMem.addActionListener(this);
		btnFormatMem.setBounds(285, 15, 120, 25);
		panel_3.add(btnFormatMem);
		
		JPanel panel_3_1 = new JPanel();
		panel_3_1.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Time Mensagem", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel_3_1.setBounds(9, 54, 415, 135);
		panel_2.add(panel_3_1);
		panel_3_1.setLayout(null);
		
		JButton btnGetTimeReport = new JButton("GET TIME REPORT");
		btnGetTimeReport.setToolTipText("RETORNA O CICLO DE CRIA\u00C7\u00C3O DE NOVA MENSAGEM PADR\u00C3O");
		btnGetTimeReport.setActionCommand("CMD_GET_TIME_CYCLE_REPORT");
		btnGetTimeReport.addActionListener(this);
		btnGetTimeReport.setBounds(255, 14, 155, 25);
		panel_3_1.add(btnGetTimeReport);
		
		JButton btnSetRepotCycle = new JButton("SET TIME REPORT");
		btnSetRepotCycle.setToolTipText("ALTERA O CICLO DE CRIA\u00C7\u00C3O DE NOVA MENSAGEM PADR\u00C3O");
		btnSetRepotCycle.setBounds(95, 14, 155, 25);
		btnSetRepotCycle.setActionCommand("CMD_SET_TIME_CYCLE_REPORT");
		btnSetRepotCycle.addActionListener(this);
		panel_3_1.add(btnSetRepotCycle);
		
		JButton btnGetReportSpeed = new JButton("GET REPORT SPEED");
		btnGetReportSpeed.setToolTipText("RETORNA O CICLO DE TEMPO CONFIGURADO QUANDO A VELOCIDADE MONITORADA ATINGE");
		btnGetReportSpeed.setBounds(255, 43, 155, 25);
		btnGetReportSpeed.setActionCommand("CMD_GET_TIME_CYCLE_REPORT_SPEED");
		btnGetReportSpeed.addActionListener(this);
		panel_3_1.add(btnGetReportSpeed);
		
		JButton btnGetTimeSend = new JButton("GET TIME SEND MSG");
		btnGetTimeSend.setToolTipText("RETORNA O INTERVALO DE TEMPO ENTRE O ENVIO DE MENSAGENS VIA BLUETOOTH");
		btnGetTimeSend.setBounds(255, 72, 155, 25);
		btnGetTimeSend.setActionCommand("CMD_GET_TIME_SEND");
		btnGetTimeSend.addActionListener(this);
		panel_3_1.add(btnGetTimeSend);
		
		JButton btnSetTimeSend = new JButton("SET TIME SEND MSG");
		btnSetTimeSend.setToolTipText("ALTERA O INTERVALO DE TEMPO ENTRE O ENVIO DE MENSAGENS VIA BLUETOOTH");
		btnSetTimeSend.setBounds(95, 72, 155, 25);
		btnSetTimeSend.setActionCommand("CMD_SET_TIME_SEND");
		btnSetTimeSend.addActionListener(this);
		panel_3_1.add(btnSetTimeSend);
		
		JButton btnSetReportSpeed = new JButton("SET REPORT SPEED");
		btnSetReportSpeed.setToolTipText("ALTERA O CICLO DE TEMPO CONFIGURADO QUANDO A VELOCIDADE MONITORADA ATINGE");
		btnSetReportSpeed.setBounds(95, 43, 155, 25);
		btnSetReportSpeed.setActionCommand("CMD_SET_TIME_CYCLE_REPORT_SPEED");
		btnSetReportSpeed.addActionListener(this);
		panel_3_1.add(btnSetReportSpeed);
		
		txtReportCycle = new JTextField();
		txtReportCycle.setBounds(12, 15, 75, 22);
		panel_3_1.add(txtReportCycle);
		txtReportCycle.setColumns(10);
		
		txtReportCycleSpeed = new JTextField();
		txtReportCycleSpeed.setColumns(10);
		txtReportCycleSpeed.setBounds(12, 44, 75, 22);
		panel_3_1.add(txtReportCycleSpeed);
		
		txtSendEntryMSG = new JTextField();
		txtSendEntryMSG.setColumns(10);
		txtSendEntryMSG.setBounds(12, 73, 75, 22);
		panel_3_1.add(txtSendEntryMSG);
		
		txtReportCycleIgOFF = new JTextField();
		txtReportCycleIgOFF.setBounds(12, 101, 75, 22);
		panel_3_1.add(txtReportCycleIgOFF);
		txtReportCycleIgOFF.setColumns(10);
		
		JButton btnSetReportCycleIgOFF = new JButton("SET TIM REP IG OFF");
		btnSetReportCycleIgOFF.setBounds(95, 100, 155, 25);
		btnSetReportCycleIgOFF.setActionCommand("CMD_SET_TIME_CYCLE_REPORT_IG_OFF");
		btnSetReportCycleIgOFF.addActionListener(this);
		panel_3_1.add(btnSetReportCycleIgOFF);
		
		JButton btnGetReportCycleIgOFF = new JButton("GET TIM REP IG OFF");
		btnGetReportCycleIgOFF.setBounds(255, 100, 155, 25);
		btnGetReportCycleIgOFF.setActionCommand("CMD_GET_TIME_CYCLE_REPORT_IG_OFF");
		btnGetReportCycleIgOFF.addActionListener(this);
		panel_3_1.add(btnGetReportCycleIgOFF);
		
		JPanel panel_3_2 = new JPanel();
		panel_3_2.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Bluetooth", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel_3_2.setBounds(9, 193, 415, 110);
		panel_2.add(panel_3_2);
		panel_3_2.setLayout(null);
		
		txtTimeMaxAUTH = new JTextField();
		txtTimeMaxAUTH.setColumns(10);
		txtTimeMaxAUTH.setBounds(12, 15, 75, 22);
		panel_3_2.add(txtTimeMaxAUTH);
		
		txtTimeRcvACK = new JTextField();
		txtTimeRcvACK.setColumns(10);
		txtTimeRcvACK.setBounds(12, 45, 75, 22);
		panel_3_2.add(txtTimeRcvACK);
		
		txtNameBLE = new JTextField();
		txtNameBLE.setColumns(10);
		txtNameBLE.setBounds(12, 75, 75, 22);
		panel_3_2.add(txtNameBLE);
		
		JButton btnSetTimeMax = new JButton("SET TIME MAX AUTH");
		btnSetTimeMax.setToolTipText("ALTERA O TEMPO MAX QUE O DISPOSITIVO TEM PARA AUTENTICAR NA INTERFACE DEPOIS DE PAREAR");
		btnSetTimeMax.setBounds(95, 14, 155, 25);
		btnSetTimeMax.setActionCommand("CMD_SET_TIME_AUTH");
		btnSetTimeMax.addActionListener(this);
		panel_3_2.add(btnSetTimeMax);
		
		JButton btnGetTimeMax = new JButton("GET TIME MAX AUTH");
		btnGetTimeMax.setToolTipText("RETORNA O TEMPO MAX QUE O DISPOSITIVO TEM PARA AUTENTICAR NA INTERFACE DEPOIS DE PAREAR");
		btnGetTimeMax.setBounds(255, 14, 155, 25);
		btnGetTimeMax.setActionCommand("CMD_GET_TIME_AUTH");
		btnGetTimeMax.addActionListener(this);
		panel_3_2.add(btnGetTimeMax);
		
		JButton btnSetTimeRcvACK = new JButton("SET TIME RCV ACK");
		btnSetTimeRcvACK.setToolTipText("ALTERA O TEMPO QUE A INTERFACE DEVE AGUARDAR UMA CONFIRMA\u00C7\u00C3O DE MENSAGEM ENTREGUE (ACK DO DISPOSITIVO)");
		btnSetTimeRcvACK.setBounds(95, 44, 155, 25);
		btnSetTimeRcvACK.setActionCommand("CMD_SET_TIME_RCV_ACK");
		btnSetTimeRcvACK.addActionListener(this);
		panel_3_2.add(btnSetTimeRcvACK);
		
		JButton btnSetNameBLE = new JButton("SET NAME BLE");
		btnSetNameBLE.setToolTipText("ALTERA O NOME DO BLUETOOTH");
		btnSetNameBLE.setBounds(95, 74, 155, 25);
		btnSetNameBLE.setActionCommand("CMD_SET_NAME_BLE");
		btnSetNameBLE.addActionListener(this);
		panel_3_2.add(btnSetNameBLE);
		
		JButton btnGetTimeRcvACK = new JButton("GET TIME RCV ACK");
		btnGetTimeRcvACK.setToolTipText("RETONAR O TEMPO QUE A INTERFACE DEVE AGUARDAR UMA CONFIRMA\u00C7\u00C3O DE MENSAGEM ENTREGUE (ACK DO DISPOSITIVO)");
		btnGetTimeRcvACK.setBounds(255, 44, 155, 25);
		btnGetTimeRcvACK.setActionCommand("CMD_GET_TIME_RCV_ACK");
		btnGetTimeRcvACK.addActionListener(this);
		panel_3_2.add(btnGetTimeRcvACK);
		
		JButton btnGetNameBLE = new JButton("GET NAME BLE");
		btnGetNameBLE.setToolTipText("RETORNA O NOME DO BLUETOOTH");
		btnGetNameBLE.setBounds(255, 74, 155, 25);
		btnGetNameBLE.setActionCommand("CMD_GET_NAME_BLE");
		btnGetNameBLE.addActionListener(this);
		panel_3_2.add(btnGetNameBLE);
		
		JPanel panel_3_2_1 = new JPanel();
		panel_3_2_1.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Sensores", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel_3_2_1.setBounds(9, 307, 415, 103);
		panel_2.add(panel_3_2_1);
		panel_3_2_1.setLayout(null);
		
		txtSpeedReport = new JTextField();
		txtSpeedReport.setColumns(10);
		txtSpeedReport.setBounds(10, 43, 75, 22);
		panel_3_2_1.add(txtSpeedReport);
		
		JButton btnSetSpeedReport = new JButton("SET SPEED REPORT");
		btnSetSpeedReport.setToolTipText("ALTERA A VELOCIDADE MONITORADA PARA ALTERAR O TEMPO DE CRIA\u00C7\u00C3O DE MENSAGEM");
		btnSetSpeedReport.setBounds(93, 42, 155, 25);
		btnSetSpeedReport.setActionCommand("CMD_SET_SPEED_REPORT");
		btnSetSpeedReport.addActionListener(this);
		panel_3_2_1.add(btnSetSpeedReport);
		
		JButton btnGetSpeedReport = new JButton("GET SPEED REPORT");
		btnGetSpeedReport.setToolTipText("RETORNA A VELOCIDADE MONITORADA PARA ALTERAR O TEMPO DE CRIA\u00C7\u00C3O DE MENSAGEM");
		btnGetSpeedReport.setBounds(253, 42, 155, 25);
		btnGetSpeedReport.setActionCommand("CMD_GET_SPEED_REPORT");
		btnGetSpeedReport.addActionListener(this);
		panel_3_2_1.add(btnGetSpeedReport);
		
		JButton btnQtdMsgFLASH = new JButton("QTD_MSG_FLASH");
		btnQtdMsgFLASH.setBounds(253, 72, 155, 25);
		btnQtdMsgFLASH.setActionCommand("CMD_QTD_MSG_FLASH");
		btnQtdMsgFLASH.addActionListener(this);
		panel_3_2_1.add(btnQtdMsgFLASH);
		
		txtRotacaoAtual = new JTextField();
		txtRotacaoAtual.setToolTipText("Informar a rota\u00E7\u00E3o atual do motor");
		txtRotacaoAtual.setColumns(10);
		txtRotacaoAtual.setBounds(10, 15, 75, 22);
		panel_3_2_1.add(txtRotacaoAtual);
		
		JButton btnSetRpmNow = new JButton("SET RPM NOW");
		btnSetRpmNow.setToolTipText("INFORMAR A ROTA\u00C7\u00C3O ATUAL DO MOTOR");
		btnSetRpmNow.setActionCommand("CMD_SET_RPM_NOW");
		btnSetRpmNow.addActionListener(this);
		btnSetRpmNow.setBounds(93, 13, 155, 25);
		panel_3_2_1.add(btnSetRpmNow);
		
		JButton btnGetRpmNow = new JButton("GET RPM NOW");
		btnGetRpmNow.setToolTipText("SOLICITA A ROTA\u00C7\u00C3O ATUAL DO MOTOR");
		btnGetRpmNow.setActionCommand("CMD_GET_RPM_NOW");
		btnGetRpmNow.addActionListener(this);
		btnGetRpmNow.setBounds(253, 13, 155, 25);
		panel_3_2_1.add(btnGetRpmNow);
		
		JPanel panel_3_2_2 = new JPanel();
		panel_3_2_2.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "GPS", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel_3_2_2.setBounds(9, 420, 415, 83);
		panel_2.add(panel_3_2_2);
		panel_3_2_2.setLayout(null);
		
		txtAngCOMPASS = new JTextField();
		txtAngCOMPASS.setColumns(10);
		txtAngCOMPASS.setBounds(12, 18, 75, 22);
		panel_3_2_2.add(txtAngCOMPASS);
		
		txtConfCOMPASS = new JTextField();
		txtConfCOMPASS.setColumns(10);
		txtConfCOMPASS.setBounds(12, 75, 75, 22);
		JButton btnSetAngBussola = new JButton("SET ANG. COMPASS");
		btnSetAngBussola.setToolTipText("ALTERA O ANGULO QUE DEVE SER MONITORADO PARA GERAR UMA NOVA MENSAGEM");
		btnSetAngBussola.setBounds(95, 17, 155, 25);
		btnSetAngBussola.setActionCommand("CMD_SET_ANG_COMPASS");
		btnSetAngBussola.addActionListener(this);
		panel_3_2_2.add(btnSetAngBussola);
		
		JButton btnGetAngBussola = new JButton("GET ANG COMPASS");
		btnGetAngBussola.setToolTipText("RETORNA O ANGULO QUE DEVE SER MONITORADO PARA GERAR UMA NOVA MENSAGEM");
		btnGetAngBussola.setBounds(255, 17, 155, 25);
		btnGetAngBussola.setActionCommand("CMD_GET_ANG_COMPASS");
		btnGetAngBussola.addActionListener(this);
		panel_3_2_2.add(btnGetAngBussola);
		
		JButton btnGetConfCOMPASS = new JButton("GET CONF COMPASS");
		btnGetConfCOMPASS.setToolTipText("RETORNA O TEMPO QUE A BUSSOLA DEVE CONFIRMAR O SEU VALOR");
		btnGetConfCOMPASS.setBounds(255, 51, 155, 25);
		btnGetConfCOMPASS.setActionCommand("CMD_GET_CONF_COMPASS");
		btnGetConfCOMPASS.addActionListener(this);
		panel_3_2_2.add(btnGetConfCOMPASS);
		
		JButton btnSetConfCOMPASS = new JButton("SET CONF COMPASS");
		btnSetConfCOMPASS.setToolTipText("ALTERA O TEMPO QUE A BUSSOLA DEVE CONFIRMAR O SEU VALOR");
		btnSetConfCOMPASS.setBounds(95, 51, 155, 25);
		btnSetConfCOMPASS.setActionCommand("CMD_SET_CONF_COMPASS");
		btnSetConfCOMPASS.addActionListener(this);
		panel_3_2_2.add(btnSetConfCOMPASS);
		
			panel_3_2_2.setLayout(null);
			
			txtFConfCOMPASS = new JTextField();
			txtFConfCOMPASS.setColumns(10);
			txtFConfCOMPASS.setBounds(12, 52, 75, 22);
			panel_3_2_2.add(txtFConfCOMPASS);
			
			JLabel lblChaveLicena = new JLabel("Chave Licen\u00E7a");
			lblChaveLicena.setForeground(Color.BLACK);
			lblChaveLicena.setFont(new Font("Tahoma", Font.BOLD, 13));
			lblChaveLicena.setBounds(752, 13, 101, 16);
			getContentPane().add(lblChaveLicena);
			
			keyB1 = new JTextField();
			keyB1.setBounds(696, 54, 30, 22);
			getContentPane().add(keyB1);
			keyB1.setColumns(2);
			keyB1.setDocument(new JTextFieldLimit(2, true));
			
			
			JLabel lblNewLabel_1_2 = new JLabel("0x");
			lblNewLabel_1_2.setBounds(678, 57, 19, 16);
			getContentPane().add(lblNewLabel_1_2);
			
			JLabel lblNewLabel_1_2_1 = new JLabel("0x");
			lblNewLabel_1_2_1.setBounds(738, 56, 19, 16);
			getContentPane().add(lblNewLabel_1_2_1);
			
			keyB2 = new JTextField();
			keyB2.setColumns(2);
			keyB2.setBounds(756, 53, 30, 22);
			getContentPane().add(keyB2);
			keyB2.setDocument(new JTextFieldLimit(2, true));
			
			JLabel lblNewLabel_1_2_2 = new JLabel("0x");
			lblNewLabel_1_2_2.setBounds(795, 56, 19, 16);
			getContentPane().add(lblNewLabel_1_2_2);
			
			keyB3 = new JTextField();
			keyB3.setColumns(2);
			keyB3.setBounds(813, 53, 30, 22);
			getContentPane().add(keyB3);
			keyB3.setDocument(new JTextFieldLimit(2, true));
			
			JLabel lblNewLabel_1_2_3 = new JLabel("0x");
			lblNewLabel_1_2_3.setBounds(855, 56, 19, 16);
			getContentPane().add(lblNewLabel_1_2_3);
			
			keyB4 = new JTextField();
			keyB4.setColumns(2);
			keyB4.setBounds(873, 53, 30, 22);
			getContentPane().add(keyB4);
			keyB4.setDocument(new JTextFieldLimit(2, true));
			
			JLabel lblNewLabel_1_2_4 = new JLabel("B1");
			lblNewLabel_1_2_4.setFont(new Font("Tahoma", Font.BOLD, 13));
			lblNewLabel_1_2_4.setBounds(703, 36, 19, 16);
			getContentPane().add(lblNewLabel_1_2_4);
			
			JLabel lblNewLabel_1_2_4_1 = new JLabel("B2");
			lblNewLabel_1_2_4_1.setFont(new Font("Tahoma", Font.BOLD, 13));
			lblNewLabel_1_2_4_1.setBounds(764, 36, 19, 16);
			getContentPane().add(lblNewLabel_1_2_4_1);
			
			JLabel lblNewLabel_1_2_4_2 = new JLabel("B3");
			lblNewLabel_1_2_4_2.setFont(new Font("Tahoma", Font.BOLD, 13));
			lblNewLabel_1_2_4_2.setBounds(820, 36, 19, 16);
			getContentPane().add(lblNewLabel_1_2_4_2);
			
			JLabel lblNewLabel_1_2_4_3 = new JLabel("B4");
			lblNewLabel_1_2_4_3.setFont(new Font("Tahoma", Font.BOLD, 13));
			lblNewLabel_1_2_4_3.setBounds(882, 36, 19, 16);
			getContentPane().add(lblNewLabel_1_2_4_3);
			
			JPanel panel_7 = new JPanel();
			panel_7.setBorder(new TitledBorder(null, "Dados Telemetria", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			panel_7.setBounds(8, 328, 635, 400);
			getContentPane().add(panel_7);
			panel_7.setLayout(null);
			
			JLabel lblNewLabel_11 = new JLabel("TOT. COMB. UTILIZADO");
			lblNewLabel_11.setBounds(10, 20, 120, 14);
			panel_7.add(lblNewLabel_11);
			
			txtTotComb = new JTextField();
			txtTotComb.setBounds(131, 17, 75, 20);
			panel_7.add(txtTotComb);
			txtTotComb.setColumns(10);
			
			JLabel lblNewLabel_11_1 = new JLabel("ODOMETRO");
			lblNewLabel_11_1.setBounds(10, 45, 120, 14);
			panel_7.add(lblNewLabel_11_1);
			
			txtOdometro = new JTextField();
			txtOdometro.setBounds(131, 42, 75, 20);
			panel_7.add(txtOdometro);
			txtOdometro.setColumns(10);
			
			JLabel lblNewLabel_11_1_1 = new JLabel("PEDAL ACELERADOR");
			lblNewLabel_11_1_1.setBounds(10, 73, 120, 14);
			panel_7.add(lblNewLabel_11_1_1);
			
			txtPedalAcelerador = new JTextField();
			txtPedalAcelerador.setColumns(10);
			txtPedalAcelerador.setBounds(131, 70, 75, 20);
			panel_7.add(txtPedalAcelerador);
			
			JLabel lblNewLabel_11_1_2 = new JLabel("TORQUE MOTOR");
			lblNewLabel_11_1_2.setBounds(10, 101, 120, 14);
			panel_7.add(lblNewLabel_11_1_2);
			
			txtTorqueMotor = new JTextField();
			txtTorqueMotor.setColumns(10);
			txtTorqueMotor.setBounds(131, 98, 75, 20);
			panel_7.add(txtTorqueMotor);
			
			JLabel lblNewLabel_11_1_3 = new JLabel("CARGA MOTOR");
			lblNewLabel_11_1_3.setBounds(10, 129, 120, 14);
			panel_7.add(lblNewLabel_11_1_3);
			
			txtCargaMotor = new JTextField();
			txtCargaMotor.setColumns(10);
			txtCargaMotor.setBounds(131, 126, 75, 20);
			panel_7.add(txtCargaMotor);
			
			JLabel lblNewLabel_11_1_4 = new JLabel("PRESS\u00C3O TURBO");
			lblNewLabel_11_1_4.setBounds(10, 157, 120, 14);
			panel_7.add(lblNewLabel_11_1_4);
			
			txtPressaoTurbo = new JTextField();
			txtPressaoTurbo.setColumns(10);
			txtPressaoTurbo.setBounds(131, 154, 75, 20);
			panel_7.add(txtPressaoTurbo);
			
			JLabel lblNewLabel_11_1_5 = new JLabel("PRESS\u00C3O AR ADMISS\u00C3O");
			lblNewLabel_11_1_5.setBounds(10, 185, 120, 14);
			panel_7.add(lblNewLabel_11_1_5);
			
			txtPressaoAdmissao = new JTextField();
			txtPressaoAdmissao.setColumns(10);
			txtPressaoAdmissao.setBounds(131, 182, 75, 20);
			panel_7.add(txtPressaoAdmissao);
			
			JLabel lblNewLabel_11_1_6 = new JLabel("PRESS\u00C3O OLEO MOTOR");
			lblNewLabel_11_1_6.setBounds(10, 213, 120, 14);
			panel_7.add(lblNewLabel_11_1_6);
			
			txtPressaoOleo = new JTextField();
			txtPressaoOleo.setColumns(10);
			txtPressaoOleo.setBounds(131, 210, 75, 20);
			panel_7.add(txtPressaoOleo);
			
			JLabel lblNewLabel_11_1_7 = new JLabel("PRESS\u00C3O OLEO TRANS.");
			lblNewLabel_11_1_7.setBounds(10, 241, 120, 14);
			panel_7.add(lblNewLabel_11_1_7);
			
			txtPressaoTrans = new JTextField();
			txtPressaoTrans.setColumns(10);
			txtPressaoTrans.setBounds(131, 238, 75, 20);
			panel_7.add(txtPressaoTrans);
			
			JLabel lblNewLabel_11_2 = new JLabel("PRESS\u00C3O COMBUSTIVEL");
			lblNewLabel_11_2.setBounds(215, 20, 120, 14);
			panel_7.add(lblNewLabel_11_2);
			
			txtPressaoCombustivel = new JTextField();
			txtPressaoCombustivel.setColumns(10);
			txtPressaoCombustivel.setBounds(337, 17, 86, 20);
			panel_7.add(txtPressaoCombustivel);
			
			JLabel lblNewLabel_11_3 = new JLabel("TEMP. OLEO MOTOR");
			lblNewLabel_11_3.setBounds(215, 45, 120, 14);
			panel_7.add(lblNewLabel_11_3);
			
			txtTempOleo = new JTextField();
			txtTempOleo.setColumns(10);
			txtTempOleo.setBounds(337, 42, 86, 20);
			panel_7.add(txtTempOleo);
			
			JLabel lblNewLabel_11_4 = new JLabel("TEMP. \u00C1GUA MOTOR");
			lblNewLabel_11_4.setBounds(215, 73, 120, 14);
			panel_7.add(lblNewLabel_11_4);
			
			txtTempAgua = new JTextField();
			txtTempAgua.setColumns(10);
			txtTempAgua.setBounds(337, 70, 86, 20);
			panel_7.add(txtTempAgua);
			
			JLabel lblNewLabel_11_5 = new JLabel("TEMP. AR ADMISS\u00C3O");
			lblNewLabel_11_5.setBounds(215, 101, 120, 14);
			panel_7.add(lblNewLabel_11_5);
			
			txtTempAdmissao = new JTextField();
			txtTempAdmissao.setColumns(10);
			txtTempAdmissao.setBounds(337, 98, 86, 20);
			panel_7.add(txtTempAdmissao);
			
			JLabel lblNewLabel_11_6 = new JLabel("TEMP. AR AMBIENTE");
			lblNewLabel_11_6.setBounds(215, 129, 120, 14);
			panel_7.add(lblNewLabel_11_6);
			
			txtTempAmbiente = new JTextField();
			txtTempAmbiente.setColumns(10);
			txtTempAmbiente.setBounds(337, 126, 86, 20);
			panel_7.add(txtTempAmbiente);
			
			JLabel lblNewLabel_11_7 = new JLabel("TEMP. OLEO TRANS.");
			lblNewLabel_11_7.setBounds(215, 157, 120, 14);
			panel_7.add(lblNewLabel_11_7);
			
			txtTempTrans = new JTextField();
			txtTempTrans.setColumns(10);
			txtTempTrans.setBounds(337, 154, 86, 20);
			panel_7.add(txtTempTrans);
			
			JLabel lblNewLabel_11_8 = new JLabel("TEMP. FLUIDO HIDRA.");
			lblNewLabel_11_8.setBounds(215, 185, 120, 14);
			panel_7.add(lblNewLabel_11_8);
			
			txtTempFluidoHidra = new JTextField();
			txtTempFluidoHidra.setColumns(10);
			txtTempFluidoHidra.setBounds(337, 182, 86, 20);
			panel_7.add(txtTempFluidoHidra);
			
			JLabel lblNewLabel_11_9 = new JLabel("TEMP. COMBUSTIVEL");
			lblNewLabel_11_9.setBounds(215, 213, 120, 14);
			panel_7.add(lblNewLabel_11_9);
			
			txtTempCombustivel = new JTextField();
			txtTempCombustivel.setColumns(10);
			txtTempCombustivel.setBounds(337, 210, 86, 20);
			panel_7.add(txtTempCombustivel);
			
			JLabel lblNewLabel_11_10 = new JLabel("VAZ\u00C3O COMBUSTIVEL");
			lblNewLabel_11_10.setBounds(215, 241, 120, 14);
			panel_7.add(lblNewLabel_11_10);
			
			txtVazaoComb = new JTextField();
			txtVazaoComb.setColumns(10);
			txtVazaoComb.setBounds(337, 238, 86, 20);
			panel_7.add(txtVazaoComb);
			
			JLabel lblNewLabel_11_2_1 = new JLabel("NIVEL COMBUSTIVEL");
			lblNewLabel_11_2_1.setBounds(429, 20, 120, 14);
			panel_7.add(lblNewLabel_11_2_1);
			
			txtNivelComb = new JTextField();
			txtNivelComb.setColumns(10);
			txtNivelComb.setBounds(550, 17, 75, 20);
			panel_7.add(txtNivelComb);
			
			JLabel lblNewLabel_11_2_1_1 = new JLabel("NIVEL OLEO TRANS.");
			lblNewLabel_11_2_1_1.setBounds(429, 45, 120, 14);
			panel_7.add(lblNewLabel_11_2_1_1);
			
			txtNivelOleoTrans = new JTextField();
			txtNivelOleoTrans.setColumns(10);
			txtNivelOleoTrans.setBounds(550, 42, 75, 20);
			panel_7.add(txtNivelOleoTrans);
			
			JLabel lblNewLabel_11_2_1_2 = new JLabel("NIVEL FLUIDO HIDRA.");
			lblNewLabel_11_2_1_2.setBounds(429, 73, 120, 14);
			panel_7.add(lblNewLabel_11_2_1_2);
			
			txtNivelFluidoHidra = new JTextField();
			txtNivelFluidoHidra.setColumns(10);
			txtNivelFluidoHidra.setBounds(550, 70, 75, 20);
			panel_7.add(txtNivelFluidoHidra);
			
			JLabel lblNewLabel_11_2_1_3 = new JLabel("STS DTC");
			lblNewLabel_11_2_1_3.setBounds(444, 101, 50, 14);
			panel_7.add(lblNewLabel_11_2_1_3);
			
			txtDTC = new JTextField();
			txtDTC.setColumns(10);
			txtDTC.setBounds(489, 98, 30, 20);
			panel_7.add(txtDTC);
			
			JLabel lblNewLabel_11_2_1_4 = new JLabel("ALTURA IMPLEMENTO");
			lblNewLabel_11_2_1_4.setBounds(429, 126, 120, 14);
			panel_7.add(lblNewLabel_11_2_1_4);
			
			txtAlturaImpl = new JTextField();
			txtAlturaImpl.setColumns(10);
			txtAlturaImpl.setBounds(550, 123, 75, 20);
			panel_7.add(txtAlturaImpl);
			
			JLabel lblNewLabel_11_2_1_5 = new JLabel("VEL. UND. COLHEITA");
			lblNewLabel_11_2_1_5.setBounds(429, 154, 120, 14);
			panel_7.add(lblNewLabel_11_2_1_5);
			
			txtVelColheita = new JTextField();
			txtVelColheita.setColumns(10);
			txtVelColheita.setBounds(550, 151, 75, 20);
			panel_7.add(txtVelColheita);
			
			JLabel lblNewLabel_11_2_1_6 = new JLabel("TOMADA FOR\u00C7A");
			lblNewLabel_11_2_1_6.setBounds(429, 182, 120, 14);
			panel_7.add(lblNewLabel_11_2_1_6);
			
			txtTomadaForca = new JTextField();
			txtTomadaForca.setColumns(10);
			txtTomadaForca.setBounds(550, 179, 75, 20);
			panel_7.add(txtTomadaForca);
			
			JLabel lblNewLabel_11_2_1_7 = new JLabel("STS PILOTO");
			lblNewLabel_11_2_1_7.setBounds(429, 213, 65, 14);
			panel_7.add(lblNewLabel_11_2_1_7);
			
			txtPiloto = new JTextField();
			txtPiloto.setColumns(10);
			txtPiloto.setBounds(493, 210, 30, 20);
			panel_7.add(txtPiloto);
			
			JLabel lblNewLabel_11_2_1_8 = new JLabel("DESC. GR\u00C3O");
			lblNewLabel_11_2_1_8.setBounds(429, 238, 65, 14);
			panel_7.add(lblNewLabel_11_2_1_8);
			
			txtDescargaGrao = new JTextField();
			txtDescargaGrao.setColumns(10);
			txtDescargaGrao.setBounds(493, 235, 30, 20);
			panel_7.add(txtDescargaGrao);
			
			JLabel lblNewLabel_11_2_1_3_1 = new JLabel("STS FUN");
			lblNewLabel_11_2_1_3_1.setBounds(527, 101, 50, 14);
			panel_7.add(lblNewLabel_11_2_1_3_1);
			
			txtFUN = new JTextField();
			txtFUN.setColumns(10);
			txtFUN.setBounds(575, 98, 30, 20);
			panel_7.add(txtFUN);
			
			JLabel lblNewLabel_11_2_1_7_1 = new JLabel("INDUSTRIA");
			lblNewLabel_11_2_1_7_1.setBounds(533, 213, 60, 14);
			panel_7.add(lblNewLabel_11_2_1_7_1);
			
			txtIndustria = new JTextField();
			txtIndustria.setColumns(10);
			txtIndustria.setBounds(595, 210, 30, 20);
			panel_7.add(txtIndustria);
			
			JLabel lblNewLabel_11_2_1_7_1_1 = new JLabel("COLHEITA");
			lblNewLabel_11_2_1_7_1_1.setBounds(533, 238, 60, 14);
			panel_7.add(lblNewLabel_11_2_1_7_1_1);
			
			txtStsColheita = new JTextField();
			txtStsColheita.setColumns(10);
			txtStsColheita.setBounds(595, 235, 30, 20);
			panel_7.add(txtStsColheita);
			
			JLabel lblNewLabel_11_1_7_1 = new JLabel("PLATAFORMA");
			lblNewLabel_11_1_7_1.setBounds(10, 269, 80, 14);
			panel_7.add(lblNewLabel_11_1_7_1);
			
			txtPlataforma = new JTextField();
			txtPlataforma.setColumns(10);
			txtPlataforma.setBounds(85, 266, 70, 20);
			panel_7.add(txtPlataforma);
			
			JLabel lblNewLabel_11_1_7_1_1 = new JLabel("STS EMBALAR");
			lblNewLabel_11_1_7_1_1.setBounds(165, 269, 80, 14);
			panel_7.add(lblNewLabel_11_1_7_1_1);
			
			txtEmbalar = new JTextField();
			txtEmbalar.setColumns(10);
			txtEmbalar.setBounds(240, 266, 30, 20);
			panel_7.add(txtEmbalar);
			
			JLabel lblNewLabel_11_2_1_7_1_1_1 = new JLabel("BOMBA DAGUA");
			lblNewLabel_11_2_1_7_1_1_1.setBounds(280, 269, 80, 14);
			panel_7.add(lblNewLabel_11_2_1_7_1_1_1);
			
			txtBombaAgua = new JTextField();
			txtBombaAgua.setColumns(10);
			txtBombaAgua.setBounds(358, 266, 30, 20);
			panel_7.add(txtBombaAgua);
			
			JLabel lblNewLabel_11_2_1_7_1_1_1_1 = new JLabel("TAXA APLIC.");
			lblNewLabel_11_2_1_7_1_1_1_1.setBounds(398, 269, 65, 14);
			panel_7.add(lblNewLabel_11_2_1_7_1_1_1_1);
			
			txtTaxaAplic = new JTextField();
			txtTaxaAplic.setColumns(10);
			txtTaxaAplic.setBounds(462, 266, 30, 20);
			panel_7.add(txtTaxaAplic);
			
			JLabel lblNewLabel_11_2_1_7_1_1_1_1_1 = new JLabel("LIB. LIQUIDO");
			lblNewLabel_11_2_1_7_1_1_1_1_1.setBounds(503, 269, 75, 14);
			panel_7.add(lblNewLabel_11_2_1_7_1_1_1_1_1);
			
			txtLibLiquido = new JTextField();
			txtLibLiquido.setColumns(10);
			txtLibLiquido.setBounds(575, 266, 50, 20);
			panel_7.add(txtLibLiquido);
			
			JLabel lblNewLabel_11_1_7_1_2 = new JLabel("BICO PULVERIZADOR:");
			lblNewLabel_11_1_7_1_2.setBounds(10, 294, 120, 14);
			panel_7.add(lblNewLabel_11_1_7_1_2);
			
			txtBicoPulvE3 = new JTextField();
			txtBicoPulvE3.setColumns(10);
			txtBicoPulvE3.setBounds(190, 291, 30, 20);
			panel_7.add(txtBicoPulvE3);
			
			JLabel lblNewLabel_11_2_1_7_1_1_1_1_2_4_1 = new JLabel("E3");
			lblNewLabel_11_2_1_7_1_1_1_1_2_4_1.setBounds(175, 294, 20, 14);
			panel_7.add(lblNewLabel_11_2_1_7_1_1_1_1_2_4_1);
			
			JLabel lblNewLabel_11_2_1_7_1_1_1_1_2_4_1_1 = new JLabel("E2");
			lblNewLabel_11_2_1_7_1_1_1_1_2_4_1_1.setBounds(230, 294, 20, 14);
			panel_7.add(lblNewLabel_11_2_1_7_1_1_1_1_2_4_1_1);
			
			txtBicoPulvE2 = new JTextField();
			txtBicoPulvE2.setColumns(10);
			txtBicoPulvE2.setBounds(250, 291, 30, 20);
			panel_7.add(txtBicoPulvE2);
			
			JLabel lblNewLabel_11_2_1_7_1_1_1_1_2_4_1_2 = new JLabel("E1");
			lblNewLabel_11_2_1_7_1_1_1_1_2_4_1_2.setBounds(290, 293, 20, 14);
			panel_7.add(lblNewLabel_11_2_1_7_1_1_1_1_2_4_1_2);
			
			txtBicoPulvE1 = new JTextField();
			txtBicoPulvE1.setColumns(10);
			txtBicoPulvE1.setBounds(310, 291, 30, 20);
			panel_7.add(txtBicoPulvE1);
			
			JLabel lblNewLabel_11_2_1_7_1_1_1_1_2_4_1_3 = new JLabel("CT");
			lblNewLabel_11_2_1_7_1_1_1_1_2_4_1_3.setBounds(345, 294, 20, 14);
			panel_7.add(lblNewLabel_11_2_1_7_1_1_1_1_2_4_1_3);
			
			txtBicoPulvCT = new JTextField();
			txtBicoPulvCT.setColumns(10);
			txtBicoPulvCT.setBounds(365, 291, 30, 20);
			panel_7.add(txtBicoPulvCT);
			
			JLabel lblNewLabel_11_2_1_7_1_1_1_1_2_4_1_4 = new JLabel("D1");
			lblNewLabel_11_2_1_7_1_1_1_1_2_4_1_4.setBounds(400, 294, 20, 14);
			panel_7.add(lblNewLabel_11_2_1_7_1_1_1_1_2_4_1_4);
			
			txtBicoPulvD1 = new JTextField();
			txtBicoPulvD1.setColumns(10);
			txtBicoPulvD1.setBounds(420, 291, 30, 20);
			panel_7.add(txtBicoPulvD1);
			
			JLabel lblNewLabel_11_2_1_7_1_1_1_1_2_4_1_5 = new JLabel("D2");
			lblNewLabel_11_2_1_7_1_1_1_1_2_4_1_5.setBounds(460, 294, 20, 14);
			panel_7.add(lblNewLabel_11_2_1_7_1_1_1_1_2_4_1_5);
			
			txtBicoPulvD2 = new JTextField();
			txtBicoPulvD2.setColumns(10);
			txtBicoPulvD2.setBounds(480, 291, 30, 20);
			panel_7.add(txtBicoPulvD2);
			
			JLabel lblNewLabel_11_2_1_7_1_1_1_1_2_4_1_6 = new JLabel("D3");
			lblNewLabel_11_2_1_7_1_1_1_1_2_4_1_6.setBounds(520, 294, 20, 14);
			panel_7.add(lblNewLabel_11_2_1_7_1_1_1_1_2_4_1_6);
			
			txtBicoPulvD3 = new JTextField();
			txtBicoPulvD3.setColumns(10);
			txtBicoPulvD3.setBounds(540, 291, 30, 20);
			panel_7.add(txtBicoPulvD3);
			
			JLabel lblNewLabel_11_1_7_1_3 = new JLabel("CORTE BASE");
			lblNewLabel_11_1_7_1_3.setBounds(10, 319, 75, 14);
			panel_7.add(lblNewLabel_11_1_7_1_3);
			
			txtCorteBase = new JTextField();
			txtCorteBase.setBounds(85, 316, 30, 20);
			panel_7.add(txtCorteBase);
			txtCorteBase.setColumns(10);
			
			JLabel lblNewLabel_11_1_7_1_3_1 = new JLabel("EXTRATOR PRIMARIO");
			lblNewLabel_11_1_7_1_3_1.setBounds(130, 319, 110, 14);
			panel_7.add(lblNewLabel_11_1_7_1_3_1);
			
			txtExtratorPrimario = new JTextField();
			txtExtratorPrimario.setColumns(10);
			txtExtratorPrimario.setBounds(245, 316, 30, 20);
			panel_7.add(txtExtratorPrimario);
			
			JLabel lblNewLabel_11_1_7_1_3_1_1 = new JLabel("EXTRATOR SECUNDARIO");
			lblNewLabel_11_1_7_1_3_1_1.setBounds(295, 319, 125, 14);
			panel_7.add(lblNewLabel_11_1_7_1_3_1_1);
			
			txtExtratorSecundario = new JTextField();
			txtExtratorSecundario.setColumns(10);
			txtExtratorSecundario.setBounds(430, 316, 30, 20);
			panel_7.add(txtExtratorSecundario);
			
			JLabel lblNewLabel_11_1_7_1_3_1_2 = new JLabel("ESTEIRA ELEVADOR");
			lblNewLabel_11_1_7_1_3_1_2.setBounds(485, 319, 105, 14);
			panel_7.add(lblNewLabel_11_1_7_1_3_1_2);
			
			txtEsteiraElevador = new JTextField();
			txtEsteiraElevador.setColumns(10);
			txtEsteiraElevador.setBounds(595, 316, 30, 20);
			panel_7.add(txtEsteiraElevador);
			
			JLabel lblNewLabel_11_1_7_1_4 = new JLabel("PRESSÃO CORTE BASE");
			lblNewLabel_11_1_7_1_4.setBounds(10, 346, 120, 14);
			panel_7.add(lblNewLabel_11_1_7_1_4);
			
			txtPressaoCorteBase = new JTextField();
			txtPressaoCorteBase.setColumns(10);
			txtPressaoCorteBase.setBounds(124, 344, 75, 20);
			panel_7.add(txtPressaoCorteBase);
			
			JLabel lblNewLabel_11_1_7_1_4_1 = new JLabel("PRESSÃO PICADOR");
			lblNewLabel_11_1_7_1_4_1.setBounds(209, 346, 100, 14);
			panel_7.add(lblNewLabel_11_1_7_1_4_1);
			
			txtPressaoPicador = new JTextField();
			txtPressaoPicador.setColumns(10);
			txtPressaoPicador.setBounds(308, 344, 75, 20);
			panel_7.add(txtPressaoPicador);
			
			JLabel lblNewLabel_11_1_7_1_4_2 = new JLabel("VEL. EXTRATOR PRIMARIO");
			lblNewLabel_11_1_7_1_4_2.setBounds(418, 346, 131, 14);
			panel_7.add(lblNewLabel_11_1_7_1_4_2);
			
			txtVelExtratorPrimario = new JTextField();
			txtVelExtratorPrimario.setColumns(10);
			txtVelExtratorPrimario.setBounds(550, 344, 75, 20);
			panel_7.add(txtVelExtratorPrimario);
			
			JLabel lblNewLabel_11_1_7_1_4_3 = new JLabel("ALTURA CORTE BASE");
			lblNewLabel_11_1_7_1_4_3.setBounds(10, 372, 120, 14);
			panel_7.add(lblNewLabel_11_1_7_1_4_3);
			
			txtAlturaCorteBase = new JTextField();
			txtAlturaCorteBase.setColumns(10);
			txtAlturaCorteBase.setBounds(124, 370, 75, 20);
			panel_7.add(txtAlturaCorteBase);
			
			JLabel lblNewLabel_11_1_7_1_3_1_2_1 = new JLabel("PORTA DIGITAL 02");
			lblNewLabel_11_1_7_1_3_1_2_1.setBounds(207, 373, 105, 14);
			panel_7.add(lblNewLabel_11_1_7_1_3_1_2_1);
			
			txtPortaDigital02 = new JTextField();
			txtPortaDigital02.setColumns(10);
			txtPortaDigital02.setBounds(308, 371, 30, 20);
			panel_7.add(txtPortaDigital02);
			
			JLabel lblNewLabel_11_2_1_7_1_1_1_1_2_4_1_7 = new JLabel("E4");
			lblNewLabel_11_2_1_7_1_1_1_1_2_4_1_7.setBounds(120, 294, 20, 14);
			panel_7.add(lblNewLabel_11_2_1_7_1_1_1_1_2_4_1_7);
			
			txtBicoPulvE4 = new JTextField();
			txtBicoPulvE4.setColumns(10);
			txtBicoPulvE4.setBounds(135, 291, 30, 20);
			panel_7.add(txtBicoPulvE4);
			
			JLabel lblNewLabel_11_2_1_7_1_1_1_1_2_4_1_6_1 = new JLabel("D4");
			lblNewLabel_11_2_1_7_1_1_1_1_2_4_1_6_1.setBounds(575, 294, 20, 14);
			panel_7.add(lblNewLabel_11_2_1_7_1_1_1_1_2_4_1_6_1);
			
			txtBicoPulvD4 = new JTextField();
			txtBicoPulvD4.setColumns(10);
			txtBicoPulvD4.setBounds(595, 291, 30, 20);
			panel_7.add(txtBicoPulvD4);
			
			JLabel lblNewLabel_11_1_7_1_3_1_2_1_1 = new JLabel("(FIO VERDE)");
			lblNewLabel_11_1_7_1_3_1_2_1_1.setBounds(345, 374, 105, 14);
			panel_7.add(lblNewLabel_11_1_7_1_3_1_2_1_1);
		
		txtHoursCount = new JTextField();
		txtHoursCount.setColumns(10);
		txtHoursCount.setBounds(12, 489, 120, 22);
		
		dc = new DecoderMSG();
	}
	
	private short calculateLineChecksumCustom(String line) {
		short cks = 0;
		for (int i = 0; i < line.length(); i += 2) {
			cks += Integer.parseInt(line.substring(i, i + 2), 16);
		}

		String tmp = Integer.toHexString(cks);
		return cks;
	}
	
	public void clearConsole() {
		textPaneAtulizacao.setText("");
		
		txtSerial.setText("");
		txtVerFW.setText("");
		txtVerProtocol.setText("");
		txtTipoMSG.setText("");
		txtVerCarga.setText("");
		txtTipoCarga.setText("");
		txtIdCarga.setText("");
		txtQtdMsgFLASH.setText("");
		txtMotorON.setText("");
		txtIgnicaoON.setText("");
		txtGpsON.setText("");
		txtIdAlerta.setText("");
		txtTimestamp.setText("");
		txtLatitude.setText("");
		txtLongitude.setText("");
		txtBussola.setText("");
		txtBateria.setText("");
		txtHorimetro.setText("");
		txtFixGPS.setText("");
		txtHDOP.setText("");
		txtQtdSat.setText("");
		txtConexao.setText("");
		txtSinalConexao.setText("");
		
		txtTotComb.setText("");
		txtOdometro.setText("");
		txtVelocidade.setText("");
		txtRPM.setText("");
		txtPedalAcelerador.setText("");
		txtTorqueMotor.setText("");
		txtCargaMotor.setText("");
		txtPressaoTurbo.setText("");
		txtPressaoAdmissao.setText("");
		txtPressaoOleo.setText("");
		txtPressaoTrans.setText("");
		
		txtPressaoCombustivel.setText("");
		txtTempOleo.setText("");
		txtTempAgua.setText("");
		txtTempAdmissao.setText("");
		txtTempAmbiente.setText("");
		txtTempTrans.setText("");
		txtTempFluidoHidra.setText("");
		txtTempCombustivel.setText("");
		txtVazaoComb.setText("");
		
		txtNivelComb.setText("");
		txtNivelOleoTrans.setText("");
		txtNivelFluidoHidra.setText("");
		txtDTC.setText("");
		txtFUN.setText("");
		txtAlturaImpl.setText("");
		txtVelColheita.setText("");
		txtTomadaForca.setText("");
		txtPiloto.setText("");
		txtIndustria.setText("");
		txtDescargaGrao.setText("");
		txtStsColheita.setText("");
		txtPlataforma.setText("");
		txtEmbalar.setText("");
		txtBombaAgua.setText("");
		txtTaxaAplic.setText("");
		txtLibLiquido.setText("");
		
		txtBicoPulvCT.setText("");
		txtBicoPulvE1.setText("");
		txtBicoPulvE2.setText("");
		txtBicoPulvE3.setText("");
		txtBicoPulvD1.setText("");
		txtBicoPulvD2.setText("");
		txtBicoPulvD3.setText("");
		
		txtCorteBase.setText("");
		txtExtratorPrimario.setText("");
		txtExtratorSecundario.setText("");
		txtEsteiraElevador.setText("");
		
		txtHorimetroElvador.setText("");
		txtHorimetroPiloto.setText("");
		txtPressaoCorteBase.setText("");
		txtPressaoPicador.setText("");
		txtAlturaCorteBase.setText("");
		txtVelExtratorPrimario.setText("");
		
		txtPortaDigital02.setText("");
		
		txtCodUSER.setText("");
	}
	
	public void decodeFrame() {		
		int len = bt_data.length(); // 230
		if(len >= 230) {
			AgDecodeTelematics dct = new AgDecodeTelematics();
			txtSerial.setText(dct.decodeSerial(bt_data));
			txtVerFW.setText(dct.decodeVerFW(bt_data));
			txtVerProtocol.setText(dct.decodeVerPROT(bt_data));
			txtTipoMSG.setText(dct.decodeTypMSG(bt_data));
			txtVerCarga.setText(dct.decodeVerCarga(bt_data));
			txtTipoCarga.setText(dct.decodeTypCarga(bt_data));
			txtIdCarga.setText(dct.decodeIdCarga(bt_data));
			txtIdAlerta.setText(dct.decodeIdAlerta(bt_data));
			txtQtdMsgFLASH.setText(dct.decodeQtdMsgFLASH(bt_data));
			
			String motor = dct.decodeEngON(bt_data);
			txtMotorON.setText(motor);
			String ign = dct.decodeIngON(bt_data);
			txtIgnicaoON.setText(ign);
			String gps = dct.decodeStsGPS(bt_data);
			txtGpsON.setText(gps);
			
			txtTimestamp.setText(dct.decodeTimestamp(bt_data));
			txtLatitude.setText(dct.decodeLatitude(bt_data));
			txtLongitude.setText(dct.decodeLongitude(bt_data));
			txtFixGPS.setText(dct.decodeTipoFIX(bt_data));
			txtHDOP.setText(dct.decodeHDOP(bt_data));
			txtQtdSat.setText(dct.decodeQtdSAT(bt_data));
			txtBussola.setText(dct.decodeBussola(bt_data));
			
			txtConexao.setText(dct.decodeConnTech(bt_data));
			txtSinalConexao.setText(dct.decodePowerSignal(bt_data));
			
			txtBateria.setText(dct.decodeBateria(bt_data));
			txtHorimetro.setText(dct.decodeHorimetro(bt_data));
			txtTotComb.setText(dct.decodeTotCombustivel(bt_data));
			txtOdometro.setText(dct.decodeOdometro(bt_data));
			txtVelocidade.setText(dct.decodeVelocidade(bt_data));
			txtRPM.setText(dct.decodeRPM(bt_data));
			txtPedalAcelerador.setText(dct.decodePedalAcelerador(bt_data));
			txtTorqueMotor.setText(dct.decodeTorqueMotor(bt_data));
			txtCargaMotor.setText(dct.decodeCargaMotor(bt_data));
			txtPressaoTurbo.setText(dct.decodePressaoTurbo(bt_data));
			txtPressaoAdmissao.setText(dct.decodePressaoAdmisao(bt_data));
			txtPressaoOleo.setText(dct.decodePressaoOleo(bt_data));
			txtPressaoTrans.setText(dct.decodePressaoTrans(bt_data));			
			txtPressaoCombustivel.setText(dct.decodePressaoCombustivel(bt_data));
			
			txtTempOleo.setText(dct.decodeTempOleo(bt_data));
			txtTempAgua.setText(dct.decodeTempAgua(bt_data));
			txtTempAdmissao.setText(dct.decodeTempAdmissao(bt_data));
			txtTempAmbiente.setText(dct.decodeTempAmbiente(bt_data));
			txtTempTrans.setText(dct.decodeTempTrans(bt_data));
			txtTempFluidoHidra.setText(dct.decodeTempFluidoHidra(bt_data));
			txtTempCombustivel.setText(dct.decodeTempCombustivel(bt_data));
			
			txtVazaoComb.setText(dct.decodeVazaoComb(bt_data));
			txtNivelComb.setText(dct.decodeNivelComb(bt_data));
			txtNivelOleoTrans.setText(dct.decodeNivelOleoTrans(bt_data));
			txtNivelFluidoHidra.setText(dct.decodeNivelFluidoHidra(bt_data));
			
			txtDTC.setText(dct.decodeDTC(bt_data));
			txtFUN.setText(dct.decodeFUN(bt_data));
			txtEsteiraElevador.setText(dct.decodeEsteiraElevador(bt_data));
			txtCorteBase.setText(dct.decodeCorteBase(bt_data));
			
			txtAlturaImpl.setText(dct.decodeAlturaImpl(bt_data));
			txtVelColheita.setText(dct.decodeVelColheita(bt_data));
			
			txtTomadaForca.setText(dct.decodeTomadaForca(bt_data));
			txtPiloto.setText(dct.decodePilotoRTK(bt_data));
			txtIndustria.setText(dct.decodeIndustria(bt_data));
			txtDescargaGrao.setText(dct.decodeDescargaGrao(bt_data));
			
			txtStsColheita.setText(dct.decodeUnidadeColheita(bt_data));
			txtPlataforma.setText(dct.decodePlataforma(bt_data));
			txtEmbalar.setText(dct.decodeEmbalar(bt_data));
			txtBombaAgua.setText(dct.decodeBombaDagua(bt_data));
			
			txtTaxaAplic.setText(dct.decodeTaxaAplicacao(bt_data));
			txtLibLiquido.setText(dct.decodeLiberandoLiquido(bt_data));
			txtExtratorPrimario.setText(dct.decodeExtratorPrimario(bt_data));
			txtExtratorSecundario.setText(dct.decodeExtratorSecundario(bt_data));
			
			txtBicoPulvCT.setText(dct.decodeBicoPulCT(bt_data));
			txtBicoPulvE1.setText(dct.decodeBicoPulE1(bt_data));
			txtBicoPulvE2.setText(dct.decodeBicoPulE2(bt_data));
			txtBicoPulvE3.setText(dct.decodeBicoPulE3(bt_data));
			
			txtBicoPulvD1.setText(dct.decodeBicoPulD1(bt_data));
			txtBicoPulvD2.setText(dct.decodeBicoPulD2(bt_data));
			txtBicoPulvD3.setText(dct.decodeBicoPulD3(bt_data));
			txtPortaDigital02.setText(dct.decodePortaDigital(bt_data));
			
			txtHorimetroElvador.setText(dct.decodeHorimetroElevador(bt_data));
			txtHorimetroPiloto.setText(dct.decodeHorimetroPiloto(bt_data));
			txtPressaoCorteBase.setText(dct.decodePressaoCorteBase(bt_data));			
			txtPressaoPicador.setText(dct.decodePressaoPicador(bt_data));
			txtAlturaCorteBase.setText(dct.decodeAlturaCorteBase(bt_data));
			txtVelExtratorPrimario.setText(dct.decodeVelocidadeExtratorPrimario(bt_data));
			
			txtBicoPulvE4.setText(dct.decodeBicoPulE4(bt_data));
			txtBicoPulvD4.setText(dct.decodeBicoPulD4(bt_data));
			
			txtCodUSER.setText(dct.decodeCodUser(bt_data));		
		}
	}
	
public void decodeFramePRM() {
	int len = bt_prm.length();
	if(len >= 190) {
		txtSerial.setText("");
		txtTipoMSG.setText("");
		txtVerCarga.setText("");
		txtTipoCarga.setText("");
		txtIdCarga.setText("");
		txtQtdMsgFLASH.setText("");
		txtIdAlerta.setText("");
		txtTimestamp.setText("");
		txtCodUSER.setText("");
		
		DecodePRM dctV2 = new DecodePRM();
		
		txtVerFW.setText(dctV2.decodeVerFW(bt_prm));
		txtVerProtocol.setText(dctV2.decodeVerPROT(bt_prm));
		
		String reservado01 = dctV2.decodeReservado01(bt_prm); //RESERVADO 01
		
		String motor = dctV2.decodeEngON(bt_prm);
		txtMotorON.setText(motor);
		String ign = dctV2.decodeIngON(bt_prm);
		txtIgnicaoON.setText(ign);
		String gps = dctV2.decodeStsGPS(bt_prm);
		txtGpsON.setText(gps);
		
		String reservado02 = dctV2.decodeReservado02(bt_prm); //RESERVADO 02
		
		txtLatitude.setText(dctV2.decodeLatitude(bt_prm));
		txtLongitude.setText(dctV2.decodeLongitude(bt_prm));
		txtFixGPS.setText(dctV2.decodeTipoFIX(bt_prm));
		txtHDOP.setText(dctV2.decodeHDOP(bt_prm));
		txtQtdSat.setText(dctV2.decodeQtdSAT(bt_prm));
		txtBussola.setText(dctV2.decodeBussola(bt_prm));
		
		txtConexao.setText(dctV2.decodeConnTech(bt_prm));
		txtSinalConexao.setText(dctV2.decodePowerSignal(bt_prm));
		
		txtBateria.setText(dctV2.decodeBateria(bt_prm));
		txtHorimetro.setText(dctV2.decodeHorimetro(bt_prm));			
		txtTotComb.setText(dctV2.decodeTotCombustivel(bt_prm));
		txtOdometro.setText(dctV2.decodeOdometro(bt_prm));		
		
		txtVelocidade.setText(dctV2.decodeVelocidade(bt_prm));
		txtRPM.setText(dctV2.decodeRPM(bt_prm));
		txtPedalAcelerador.setText(dctV2.decodePedalAcelerador(bt_prm));
		
		txtTorqueMotor.setText(dctV2.decodeTorqueMotor(bt_prm));
		txtCargaMotor.setText(dctV2.decodeCargaMotor(bt_prm));
		txtPressaoTurbo.setText(dctV2.decodePressaoTurbo(bt_prm));
		txtPressaoAdmissao.setText(dctV2.decodePressaoAdmisao(bt_prm));
		txtPressaoOleo.setText(dctV2.decodePressaoOleo(bt_prm));
		txtPressaoTrans.setText(dctV2.decodePressaoTrans(bt_prm));			
		txtPressaoCombustivel.setText(dctV2.decodePressaoCombustivel(bt_prm));
		
		txtTempOleo.setText(dctV2.decodeTempOleo(bt_prm));
		txtTempAgua.setText(dctV2.decodeTempAgua(bt_prm));
		txtTempAdmissao.setText(dctV2.decodeTempAdmissao(bt_prm));
		txtTempAmbiente.setText(dctV2.decodeTempAmbiente(bt_prm));
		txtTempTrans.setText(dctV2.decodeTempTrans(bt_prm));
		txtTempFluidoHidra.setText(dctV2.decodeTempFluidoHidra(bt_prm));
		txtTempCombustivel.setText(dctV2.decodeTempCombustivel(bt_prm));
		
		txtVazaoComb.setText(dctV2.decodeVazaoComb(bt_prm));
		
		txtNivelComb.setText(dctV2.decodeNivelComb(bt_prm));
		txtNivelOleoTrans.setText(dctV2.decodeNivelOleoTrans(bt_prm));
		txtNivelFluidoHidra.setText(dctV2.decodeNivelFluidoHidra(bt_prm));
		
		txtDTC.setText(dctV2.decodeDTC(bt_prm));
		txtFUN.setText(dctV2.decodeFUN(bt_prm));
		txtEsteiraElevador.setText(dctV2.decodeEsteiraElevador(bt_prm));
		txtCorteBase.setText(dctV2.decodeCorteBase(bt_prm));
		
		txtAlturaImpl.setText(dctV2.decodeAlturaImpl(bt_prm));
		txtVelColheita.setText(dctV2.decodeVelColheita(bt_prm));
		
		txtTomadaForca.setText(dctV2.decodeTomadaForca(bt_prm));
		txtPiloto.setText(dctV2.decodePilotoRTK(bt_prm));
		txtIndustria.setText(dctV2.decodeIndustria(bt_prm));
		txtDescargaGrao.setText(dctV2.decodeDescargaGrao(bt_prm));
		
		txtStsColheita.setText(dctV2.decodeUnidadeColheita(bt_prm));
		txtPlataforma.setText(dctV2.decodePlataforma(bt_prm));
		txtEmbalar.setText(dctV2.decodeEmbalar(bt_prm));
		txtBombaAgua.setText(dctV2.decodeBombaDagua(bt_prm));
		
		txtTaxaAplic.setText(dctV2.decodeTaxaAplicacao(bt_prm));
		txtLibLiquido.setText(dctV2.decodeLiberandoLiquido(bt_prm));
		txtExtratorPrimario.setText(dctV2.decodeExtratorPrimario(bt_prm));
		txtExtratorSecundario.setText(dctV2.decodeExtratorSecundario(bt_prm));
		
		txtBicoPulvCT.setText(dctV2.decodeBicoPulCT(bt_prm));
		txtBicoPulvE1.setText(dctV2.decodeBicoPulE1(bt_prm));
		txtBicoPulvE2.setText(dctV2.decodeBicoPulE2(bt_prm));
		txtBicoPulvE3.setText(dctV2.decodeBicoPulE3(bt_prm));
		txtBicoPulvD1.setText(dctV2.decodeBicoPulD1(bt_prm));
		txtBicoPulvD2.setText(dctV2.decodeBicoPulD2(bt_prm));
		txtBicoPulvD3.setText(dctV2.decodeBicoPulD3(bt_prm));
		txtPortaDigital02.setText(dctV2.decodePortaDigital(bt_prm));
		
		txtHorimetroElvador.setText(dctV2.decodeHorimetroElevador(bt_prm));
		txtHorimetroPiloto.setText(dctV2.decodeHorimetroPiloto(bt_prm));
		txtPressaoCorteBase.setText(dctV2.decodePressaoCorteBase(bt_prm));
		txtPressaoPicador.setText(dctV2.decodePressaoPicador(bt_prm));
		txtAlturaCorteBase.setText(dctV2.decodeAlturaCorteBase(bt_prm));
		txtVelExtratorPrimario.setText(dctV2.decodeVelocidadeExtratorPrimario(bt_prm));
		
		txtBicoPulvD4.setText(dctV2.decodeBicoPulD4(bt_prm));
		txtBicoPulvE4.setText(dctV2.decodeBicoPulE4(bt_prm));
		
		String reservado03 = dctV2.decodeReservado03(bt_prm); //RESERVADO 03
	}	
}
	
	public void calcCRC(boolean frameReport, boolean sendResp) {
		byte[] data = Utilitarios.hexStringToByteArray(bt_data);
		byte checksum = 0;
		int dataLen = (data.length - 1);
		for(int x = 0; x < dataLen; x++) {
			int tempCalc = data[x];
			checksum = (byte) ((checksum + tempCalc) & 0xFF);
		}
		
		if(frameReport) {
			if(checksum == data[dataLen]) {
				decodeFrame();
				ConsolePrinter.consolePrinter(textPaneAtulizacao, ">> AT+BT_DATA_OK");				
				if(sendResp) {
					serialAdapter.cmdSendSerial("AT+BT_DATA_OK\r\n");
				}
			}else {
				ConsolePrinter.consolePrinter(textPaneAtulizacao, "********* AT+BT_DATA_ERROR **********");
			}
		}else { //frame de tempo real
			decodeFramePRM();
		}
	}
	
	public void calcAuth() {
		
		byte[] btc_seedAUTH = Utilitarios.hexStringToByteArray(bt_seed);
		
		byte[] btc_keyAUTH = new byte[8];
		
		byte[] btc_KeyCompany = new byte[4];
			
			while(k1.length() < 2) {
				k1 = "0" + k1;
			}
			
			while(k2.length() < 2) {
				k2 = "0" + k2;
			}
			
			while(k3.length() < 2) {
				k3 = "0" + k3;
			}
			
			while(k4.length() < 2) {
				k4 = "0" + k4;
			}
			
			keyB1.setText(k1);
			keyB2.setText(k2);
			keyB3.setText(k3);
			keyB4.setText(k4);
		
			btc_KeyCompany[0] = (byte) Integer.parseInt(k1, 16);
			btc_KeyCompany[1] = (byte) Integer.parseInt(k2, 16);
			btc_KeyCompany[2] = (byte) Integer.parseInt(k3, 16);
			btc_KeyCompany[3] = (byte) Integer.parseInt(k4, 16);
			   
		btc_keyAUTH[0] = (byte) (btc_seedAUTH[4] ^ btc_KeyCompany[3]);
	    btc_keyAUTH[1] = btc_KeyCompany[1];
	    btc_keyAUTH[2] = (byte) ((btc_keyAUTH[0] + btc_KeyCompany[2]) & 0xFF);
	    btc_keyAUTH[3] = (byte) ((btc_keyAUTH[1] + btc_seedAUTH[0]) & 0xFF);
	    btc_keyAUTH[4] = (byte)  (btc_keyAUTH[2] ^ btc_KeyCompany[0]);
	    btc_keyAUTH[5] = (byte) (btc_seedAUTH[5] ^ btc_keyAUTH[3]);
	    btc_keyAUTH[6] = (byte) (btc_seedAUTH[7] & btc_keyAUTH[2]);
	    btc_keyAUTH[7] = (byte) (btc_seedAUTH[3] ^ btc_keyAUTH[6]);
	    
	    String result = Utilitarios.ByteArrayToString(btc_keyAUTH);
	    
	    while(result.length() < 16) {
	    	result = "0" + result;
	    }
	    
	    ConsolePrinter.consolePrinter(textPaneAtulizacao, ">> AT+BT_AUTH=" + result);
	    serialAdapter.cmdSendSerial("AT+BT_AUTH="+result+"\r\n");
	}
	
	/*
	 * METODO RESPONSAVEL POR CARREGAR O ARQUIVO A SER ENVIADO PARA ATUALIZACAO
	 */
	public void loadFileCargaAtivacao() {
		JFileChooser openFile = new JFileChooser();
		
		FileNameExtensionFilter speFilter = new FileNameExtensionFilter("Arquivos BIN (*.bin)", "bin");
		openFile.addChoosableFileFilter(speFilter);
		openFile.setFileFilter(speFilter);
		
		int result = openFile.showOpenDialog(null);
		if (result == JFileChooser.CANCEL_OPTION) {
			return;
		}
		
		inputFileCargaUpdate = openFile.getSelectedFile();
		if(inputFileCargaUpdate.length() < 1024) {
			JOptionPane.showMessageDialog(null, "Erro, tamanho do arquivo invalido!");
			arquivoCarregado = false;
			btnInitUpdate.setEnabled(false);
			return;
		}
		
		String url = inputFileCargaUpdate.getAbsolutePath();
		txtAtualizacaoLoadFile.setText(url);
		txtAtualizacaoLoadFile.setForeground(new Color(0, 128, 0));// verde
		btnInitUpdate.setEnabled(true);
		arquivoCarregado = true;
	}
	
	byte[] bytesFile;
	private JTextField txtHoursINIT;
	private JTextField txtHoursCOUNT;
	private JTextField txtHoursEsteiraINIT;
	private JTextField txtHoursEsteiraCOUNT;
	private JTextField txtHoursPilotoINIT;
	private JTextField txtHoursPilotoCOUNT;
	private JTextField txtPortaDigital02;
	private JTextField txtBicoPulvE4;
	private JTextField txtBicoPulvD4;
	private JTextField txtConexao;
	private JTextField txtSinalConexao;
	private JTextField txtFixGPS;
	private JTextField txtHDOP;
	private JTextField txtQtdSat;
	public void sendFileUpdate() {
		try {
			
			if(initUpdateOTA) {
				totalFileSize = inputFileCargaUpdate.length();
				totalSendFile = 0;
				progressBar.setMaximum((int)totalFileSize);
				
				bytesFile = Files.readAllBytes(inputFileCargaUpdate.toPath());
				
				if(BT_AUTENTICADO) {
					SIZE_PACKET_USAGE = SIZE_PACKET_BT;
				}else {
					SIZE_PACKET_USAGE = SIZE_PACKET_UART;
				}
				
				initUpdateOTA = false;
			}
			
			if(totalFileSize > totalSendFile) {
				long difSize = totalFileSize - totalSendFile;
				if(difSize > SIZE_PACKET_USAGE) {
					byte[] sendBytes = new byte[SIZE_PACKET_USAGE];
					System.arraycopy(bytesFile, (int) totalSendFile, sendBytes, 0, SIZE_PACKET_USAGE);
					System.out.println("DATA SEND: " + Utilitarios.ByteArrayToString(sendBytes));
					totalSendFile += SIZE_PACKET_USAGE;
					System.out.println("TOTAL ENVIADO= " + totalSendFile);
					serialAdapter.cmdSendBytes(sendBytes);
				}else {
					byte[] sendBytes = new byte[(int) difSize];
					System.arraycopy(bytesFile, (int) totalSendFile, sendBytes, 0, (int) difSize);
					System.out.println("DATA SEND: " + Utilitarios.ByteArrayToString(sendBytes));
					totalSendFile += difSize;
					System.out.println("TOTAL ENVIADO= " + totalSendFile);
					serialAdapter.cmdSendBytes(sendBytes);
				}
				progressBar.setValue((int)totalSendFile);
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/*
	 * METODO RESPONSAVEL POR SOLICITAR QUE O HARDWARE ENTRE EM MODO UPDATE
	 */
	public void entryModeUpdate() {
		if(!confgPortSerial){
			JOptionPane.showMessageDialog(null, "Porta serial Não esta configurada!");
			return;
		}
		if(!arquivoCarregado){
			JOptionPane.showMessageDialog(null, "Carregue o arquivo de atualiza��o!");
			return;
		}
		
		ConsolePrinter.consolePrinter(textPaneAtulizacao, "--------------------------------------");
		ConsolePrinter.consolePrinter(textPaneAtulizacao, "        ATUALIZANDO DISPOSITIVO!      ");
		ConsolePrinter.consolePrinter(textPaneAtulizacao, "--------------------------------------");
		
		progressBar.setValue(0);
		long sizeFile = inputFileCargaUpdate.length();
		initUpdateOTA = true;
		ConsolePrinter.consolePrinter(textPaneAtulizacao, "TAMANHO DO ARQUIVO= " + sizeFile);
		ConsolePrinter.consolePrinter(textPaneAtulizacao, ">> AT+BT_OTA_START_LEN=" + sizeFile);
		serialAdapter.cmdSendSerial("AT+BT_OTA_START_LEN="+sizeFile+"\r\n");
	}
	
		@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub
		if (arg0 instanceof SerialAdapter) {
			SerialAdapter serialAdapter = (SerialAdapter) arg0;
			String respTemp = serialAdapter.getReaded();
			System.out.println(respTemp);
			if(!respTemp.contains("AT+BT_OTA_NEXT_PKT")) {
				ConsolePrinter.consolePrinter(textPaneAtulizacao, "<< " + respTemp);
			}
			if(respTemp.contains("AT+BT_SEED=")) {
				bt_seed = respTemp.substring(11, respTemp.length()).toUpperCase();
				calcAuth();
			}else if(respTemp.contains("AT+BT_AUTH_OK")) {
				ConsolePrinter.consolePrinter(textPaneAtulizacao, "--------------------------------------");
				ConsolePrinter.consolePrinter(textPaneAtulizacao, "        DISPOSITIVO AUTENTICADO!      ");
				ConsolePrinter.consolePrinter(textPaneAtulizacao, "--------------------------------------");
				lblStatus.setText("DISPOSITIVO AUTENTICADO!");
				lblStatus.setForeground(new Color(0, 100, 0));
				BT_AUTENTICADO = true;
			}else if(respTemp.contains("AT+BT_AUTH_LEN_FAIL")) {
				ConsolePrinter.consolePrinter(textPaneAtulizacao, "--------------------------------------");
				ConsolePrinter.consolePrinter(textPaneAtulizacao, "         TAMANHO CHAVE INVALIDA!      ");
				ConsolePrinter.consolePrinter(textPaneAtulizacao, "--------------------------------------");
				lblStatus.setText("TAMANHO CHAVE INVALIDA!");
				lblStatus.setForeground(Color.RED);
				BT_AUTENTICADO = false;
			}else if(respTemp.contains("AT+BT_AUTH_DATA_FAIL")) {
				try {
					ConsolePrinter.consolePrinter(textPaneAtulizacao, "--------------------------------------");
					ConsolePrinter.consolePrinter(textPaneAtulizacao, "              CHAVE INVALIDA!         ");
					ConsolePrinter.consolePrinter(textPaneAtulizacao, "--------------------------------------");
					lblStatus.setText("CHAVE INVALIDA!");
					lblStatus.setForeground(Color.RED);
					BT_AUTENTICADO = false;
					serialAdapter.getSerialPort().closePort();
					serialAdapter = null;
					confgPortSerial = false;
					lblStatus.setText("Serial Não configurada!");
					lblStatus.setForeground(Color.RED);
					btnConfigSerial.setEnabled(true);
					btnDesconectar.setEnabled(false);
				} catch (SerialPortException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else if(respTemp.contains("AT+BT_PRM=")) {
				bt_prm = respTemp.substring(10, respTemp.length()).toUpperCase();
				calcCRC(false, false);
			}else if(respTemp.contains("AT+BT_DATA=")) {
				bt_data = respTemp.substring(11, respTemp.length()).toUpperCase();
				calcCRC(true, true);
			}else if(respTemp.contains("AT+BT_OTA_START_OK") || respTemp.contains("AT+BT_OTA_NEXT_PKT")) {
				//TODO AQUI DEVE CHAMAR FUNCAO DE ENVIAR ARQUIVO
				sendFileUpdate();
				//initSendFileUpdate();
			}else if(respTemp.contains("AT+BT_OTA_COMPLETE")) {
				btnInitUpdate.setEnabled(true);
			}else if(respTemp.contains("AT+BT_GET_WIFI_BUF=")) {
				String data_tmp = respTemp.substring(19, respTemp.length());
				int result = decodeBufWIFI(data_tmp);
				if(result != -1) {
					loadAllWIFI(result);
				}
			}else if(respTemp.contains("AT+BT_GET_SERVER_BUF=")) {
				String data_tmp = respTemp.substring(21, respTemp.length());
				int result = decodeBufSERVER(data_tmp);
				if(result != -1) {
					loadAllSERVER(result);
				}
			}
			
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if(e.getActionCommand().equals("config-Serial")) {
			
			k1 = keyB1.getText().trim();
			k2 = keyB2.getText().trim();
			k3 = keyB3.getText().trim();
			k4 = keyB4.getText().trim();
			
			if(k1.equals(null) || k1.equals("") || k2.equals(null) || k2.equals("") || k3.equals(null) || k3.equals("") || k4.equals(null) || k4.equals("")) {
				JOptionPane.showMessageDialog(null, "Chave de licen�a invalida!");
				return;
			}
			
			SerialConfigWindow serialConfigWindow = new SerialConfigWindow();
			serialConfigWindow.setVisible(true);
			if(serialConfigWindow.getSerialConfiguration() != null){
				
				serialAdapter = new SerialAdapter(this);
				serialAdapter.setSerialPort(new SerialPort(serialConfigWindow.getSerialConfiguration().getSerialName()));
				try {
					serialAdapter.getSerialPort().openPort();
					serialAdapter.getSerialPort().setParams(serialConfigWindow.getSerialConfiguration().getBaudRate(),8, 1, 0);
					serialAdapter.getSerialPort().addEventListener(serialAdapter);
					confgPortSerial = true;
					lblStatus.setText("Serial configurada!");
					lblStatus.setForeground(new Color(0, 100, 0));
					btnDesconectar.setEnabled(true);
					btnConfigSerial.setEnabled(false);
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(null, "Erro ao configurar Porta Serial. " + e1.getMessage());
				}
				
			}
		}else if(e.getActionCommand().equals("desconectar-serial")) {
			try {
				serialAdapter.getSerialPort().closePort();
				serialAdapter = null;
				confgPortSerial = false;
				lblStatus.setText("Serial Não configurada!");
				lblStatus.setForeground(Color.RED);
				btnConfigSerial.setEnabled(true);
				btnDesconectar.setEnabled(false);
			} catch (SerialPortException e1) {
				e1.printStackTrace();
			}			
		}else if(e.getActionCommand().equals("CMD_DECODE_FRAME")) {
			String data_temp = txtDecodeFrame.getText();
			if(data_temp.contains("AT+BT_DATA=")) {
				bt_data = data_temp.substring(11, data_temp.length()).toUpperCase();
				calcCRC(true, false);
			}else if(data_temp.contains("AT+BT_PRM=")) {
				bt_prm = data_temp.substring(10, data_temp.length()).toUpperCase();
				calcCRC(false, false);
			}
		}else if(e.getActionCommand().equals("limpar")) {
			clearConsole();
		}else if(e.getActionCommand().equals("ATUALIZADOR-LOAD-FILE")) {
			loadFileCargaAtivacao();
		}else if(e.getActionCommand().equals("INIT-UPDATE")) {
			entryModeUpdate();
		}else if(e.getActionCommand().equals("CMD_LOAD_ALL_WIFI")) {
			loadAllWIFI(1);
		}else if(e.getActionCommand().equals("CMD_LOAD_ALL_SERVER")) {
			loadAllSERVER(1);
		}else if(e.getActionCommand().equals("CMD_SAVE_ALL_WIFI")) {
			try {
				if(!saveWifi) {
					saveWifi = true;
					saveAllWIFI();
					loadAllWIFI(1);
					saveWifi = false;
				}
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}else if(e.getActionCommand().equals("CMD_SAVE_ALL_SERVER")) {
			try {
				if(!saveSERVER) {
					saveSERVER = true;
					saveAllSERVER();
					loadAllSERVER(1);
					saveSERVER = false;
				}
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			
		}else if(e.getActionCommand().equals("CMD_GET_SERIAL")) {
			sendCmdBT(0);
		}else if(e.getActionCommand().equals("CMD_GET_VER_FW")) {
			sendCmdBT(1);
		}else if(e.getActionCommand().equals("CMD_RST")) {
			sendCmdBT(2);
		}else if(e.getActionCommand().equals("CMD_FACTORY")) {
			sendCmdBT(3);
		}else if(e.getActionCommand().equals("CMD_FORMAT_MEM")) {
			sendCmdBT(4);
		
		}else if(e.getActionCommand().equals("CMD_GET_TIME_CYCLE_REPORT")) {
			sendCmdBT(5);
		}else if(e.getActionCommand().equals("CMD_SET_TIME_CYCLE_REPORT")) {
			sendCmdBT(6);
		}else if(e.getActionCommand().equals("CMD_GET_TIME_CYCLE_REPORT_SPEED")) {
			sendCmdBT(7);
		}else if(e.getActionCommand().equals("CMD_SET_TIME_CYCLE_REPORT_SPEED")) {
			sendCmdBT(8);
		}else if(e.getActionCommand().equals("CMD_GET_TIME_SEND")) {
			sendCmdBT(9);
		}else if(e.getActionCommand().equals("CMD_SET_TIME_SEND")) {
			sendCmdBT(10);
		
		}else if(e.getActionCommand().equals("CMD_GET_TIME_AUTH")) {
			sendCmdBT(11);
		}else if(e.getActionCommand().equals("CMD_SET_TIME_AUTH")) {
			sendCmdBT(12);
		}else if(e.getActionCommand().equals("CMD_GET_TIME_RCV_ACK")) {
			sendCmdBT(13);
		}else if(e.getActionCommand().equals("CMD_SET_TIME_RCV_ACK")) {
			sendCmdBT(14);
		}else if(e.getActionCommand().equals("CMD_GET_NAME_BLE")) {
			sendCmdBT(15);
		}else if(e.getActionCommand().equals("CMD_SET_NAME_BLE")) {
			sendCmdBT(16);
		
		}else if(e.getActionCommand().equals("CMD_GET_SPEED_REPORT")) {
			sendCmdBT(17);
		}else if(e.getActionCommand().equals("CMD_SET_SPEED_REPORT")) {
			sendCmdBT(18);
		}else if(e.getActionCommand().equals("CMD_GET_HOURS_INIT")) {
			sendCmdBT(19);
		}else if(e.getActionCommand().equals("CMD_SET_HOURS_INIT")) {
			sendCmdBT(20);
		}else if(e.getActionCommand().equals("CMD_GET_HOURS_COUNT")) {
			sendCmdBT(21);
		}else if(e.getActionCommand().equals("CMD_SET_HOURS_COUNT")) {
			sendCmdBT(22);		
		}else if(e.getActionCommand().equals("CMD_GET_ANG_COMPASS")) {
			sendCmdBT(23);
		}else if(e.getActionCommand().equals("CMD_SET_ANG_COMPASS")) {
			sendCmdBT(24);
		}else if(e.getActionCommand().equals("CMD_GET_CONF_COMPASS")) {
			sendCmdBT(25);
		}else if(e.getActionCommand().equals("CMD_SET_CONF_COMPASS")) {
			sendCmdBT(26);
		}else if(e.getActionCommand().equals("CMD_DEBUG_ON")) {
			sendCmdBT(27);
		}else if(e.getActionCommand().equals("CMD_DEBUG_OFF")) {
			sendCmdBT(28);
		}else if(e.getActionCommand().equals("CMD_SET_COD_USER")) {
			sendCmdBT(29);
		}else if(e.getActionCommand().equals("CMD_GET_HOURS_NOW")) {
			sendCmdBT(30);
		}else if(e.getActionCommand().equals("CMD_SET_START_REPORT")) {
			sendCmdBT(31);
		}else if(e.getActionCommand().equals("CMD_SET_STOP_REPORT")) {
			sendCmdBT(32);
		}else if(e.getActionCommand().equals("CMD_SET_START_PRM")) {
			sendCmdBT(33);
		}else if(e.getActionCommand().equals("CMD_SET_STOP_PRM")) {
			sendCmdBT(34);
		}else if(e.getActionCommand().equals("CMD_QTD_MSG_FLASH")) {
			sendCmdBT(35);
		}else if(e.getActionCommand().equals("CMD_WIFI_ON")) {
			sendCmdBT(36);
		}else if(e.getActionCommand().equals("CMD_WIFI_OFF")) {
			sendCmdBT(37);
		}else if(e.getActionCommand().equals("CMD_WIFI_LIST")) {
			sendCmdBT(38);
		}else if(e.getActionCommand().equals("CMD_SERVER_LIST")) {
			sendCmdBT(39);
		}else if(e.getActionCommand().equals("CMD_SET_TIME_CYCLE_REPORT_IG_OFF")) {
			sendCmdBT(40);
		}else if(e.getActionCommand().equals("CMD_GET_TIME_CYCLE_REPORT_IG_OFF")) {
			sendCmdBT(41);
		}else if(e.getActionCommand().equals("CMD_SET_CHECK_CONFIG_SERVER")) {
			sendCmdBT(42);
		}else if(e.getActionCommand().equals("CMD_GET_CHECK_CONFIG_SERVER")) {
			sendCmdBT(43);
		}else if(e.getActionCommand().equals("CMD_GET_STATUS_WIFI")) {
			sendCmdBT(44);
		}else if(e.getActionCommand().equals("CMD_SET_RPM_ON")) {
			sendCmdBT(45);
		}else if(e.getActionCommand().equals("CMD_SET_RPM_OFF")) {
			sendCmdBT(46);
		}else if(e.getActionCommand().equals("CMD_SET_RPM_NOW")) {
			sendCmdBT(47);
		}else if(e.getActionCommand().equals("CMD_GET_RPM_NOW")) {
			sendCmdBT(48);
		}else if(e.getActionCommand().equals("CMD_LIVRE")) {
			sendCmdBT(49);
		}else if(e.getActionCommand().equals("CMD_SET_DEMO_ON")) {
			sendCmdBT(50);
		}else if(e.getActionCommand().equals("CMD_SET_DEMO_OFF")) {
			sendCmdBT(51);
			
		}else if(e.getActionCommand().equals("CMD_GET_HOURS_ESTEIRA_NOW")) {
			sendCmdBT(52);
		}else if(e.getActionCommand().equals("CMD_GET_HOURS_ESTEIRA_INIT")) {
			sendCmdBT(53);
		}else if(e.getActionCommand().equals("CMD_SET_HOURS_ESTEIRA_INIT")) {
			sendCmdBT(54);
		}else if(e.getActionCommand().equals("CMD_GET_HOURS_ESTEIRA_COUNT")) {
			sendCmdBT(55);
		}else if(e.getActionCommand().equals("CMD_SET_HOURS_ESTEIRA_COUNT")) {
			sendCmdBT(56);
			
		}else if(e.getActionCommand().equals("CMD_GET_HOURS_PILOTO_NOW")) {
			sendCmdBT(57);
		}else if(e.getActionCommand().equals("CMD_GET_HOURS_PILOTO_INIT")) {
			sendCmdBT(58);
		}else if(e.getActionCommand().equals("CMD_SET_HOURS_PILOTO_INIT")) {
			sendCmdBT(59);
		}else if(e.getActionCommand().equals("CMD_GET_HOURS_PILOTO_COUNT")) {
			sendCmdBT(60);
		}else if(e.getActionCommand().equals("CMD_SET_HOURS_PILOTO_COUNT")) {
			sendCmdBT(61);
		}else if(e.getActionCommand().equals("CMD_GET_STATUS_CONN")) {
			sendCmdBT(62);
		}else if(e.getActionCommand().equals("CMD_SCAN_WIFI")) {
			sendCmdBT(63);
		}		
	}
	
	public void loadAllWIFI(int idx) {
		ConsolePrinter.consolePrinter(textPaneAtulizacao, ">> AT+BT_GET_WIFI_BUF="+idx);
		serialAdapter.cmdSendSerial("AT+BT_GET_WIFI_BUF="+idx+"\r\n");
	}
	
	public void loadAllSERVER(int idx) {
		ConsolePrinter.consolePrinter(textPaneAtulizacao, ">> AT+BT_GET_SERVER_BUF="+idx);
		serialAdapter.cmdSendSerial("AT+BT_GET_SERVER_BUF="+idx+"\r\n");
	}
	
	public void saveAllWIFI() throws InterruptedException {
		for(int x = 1; x <= 5; x++) {
			Thread.sleep(200);
			String ssid = "";
			String pass = "";
			if(x == 1) {
				ssid = txtSSID01.getText();
				pass = txtPASS01.getText();
			}else if(x == 2) {
				ssid = txtSSID02.getText();
				pass = txtPASS02.getText();
			}else if(x == 3) {
				ssid = txtSSID03.getText();
				pass = txtPASS03.getText();
			}else if(x == 4) {
				ssid = txtSSID04.getText();
				pass = txtPASS04.getText();
			}else if(x == 5) {
				ssid = txtSSID05.getText();
				pass = txtPASS05.getText();
			}
			ConsolePrinter.consolePrinter(textPaneAtulizacao, ">> AT+BT_SET_WIFI_BUF="+x+","+ssid+","+pass);
			serialAdapter.cmdSendSerial("AT+BT_SET_WIFI_BUF="+x+","+ssid+","+pass+"\r\n");
		}
	}
	
	public void saveAllSERVER() throws InterruptedException {
		for(int x = 1; x <= 5; x++) {
			Thread.sleep(200);
			String ip = "";
			String port = "";
			if(x == 1) {
				ip = txtIP01.getText();
				port = txtPORTA01.getText();
			}else if(x == 2) {
				ip = txtIP02.getText();
				port = txtPORTA02.getText();
			}else if(x == 3) {
				ip = txtIP03.getText();
				port = txtPORTA03.getText();
			}else if(x == 4) {
				ip = txtIP04.getText();
				port = txtPORTA04.getText();
			}else if(x == 5) {
				ip = txtIP05.getText();
				port = txtPORTA05.getText();
			}
			ConsolePrinter.consolePrinter(textPaneAtulizacao, ">> AT+BT_SET_SERVER_BUF="+x+","+ip+","+port);
			serialAdapter.cmdSendSerial("AT+BT_SET_SERVER_BUF="+x+","+ip+","+port+"\r\n");
		}
	}
	
	public int decodeBufWIFI(String dado) {
		String array[] = dado.split(",");
		int size = array.length;
	    String buf = array[0];
	    String ssid = "";
	    String pass = "";
	    if(size > 1) {
		    ssid = array[1];
		    pass = array[2];
	    }
	    
	    int numBuf = Integer.parseInt(buf);
	    if(numBuf == 1) {
	    	txtSSID01.setText(ssid);
	    	txtPASS01.setText(pass);
	    	return 2;
	    }else if(numBuf == 2) {
	    	txtSSID02.setText(ssid);
	    	txtPASS02.setText(pass);
	    	return 3;
	    }else if(numBuf == 3) {
	    	txtSSID03.setText(ssid);
	    	txtPASS03.setText(pass);
	    	return 4;
	    }else if(numBuf == 4) {
	    	txtSSID04.setText(ssid);
	    	txtPASS04.setText(pass);
	    	return 5;
	    }else if(numBuf == 5) {
	    	txtSSID05.setText(ssid);
	    	txtPASS05.setText(pass);
	    	return -1;
	    }
		return -1;
	}
	
	public int decodeBufSERVER(String dado) {
		String array[] = dado.split(",");
		int size = array.length;
		String buf = array[0];
		String ip = "";
		String port = "";
		if(size > 1) {
		    ip = array[1];
		    port = array[2];
		    
		    if(port.equals("0")) {
	    		port = "";
	    	}
		}
		
	    int numBuf = Integer.parseInt(buf);
	    if(numBuf == 1) {
	    	txtIP01.setText(ip);
	    	txtPORTA01.setText(port);
	    	return 2;
	    }else if(numBuf == 2) {
	    	txtIP02.setText(ip);
	    	txtPORTA02.setText(port);
	    	return 3;
	    }else if(numBuf == 3) {
	    	txtIP03.setText(ip);
	    	txtPORTA03.setText(port);
	    	return 4;
	    }else if(numBuf == 4) {
	    	txtIP04.setText(ip);
	    	txtPORTA04.setText(port);
	    	return 5;
	    }else if(numBuf == 5) {
	    	txtIP05.setText(ip);
	    	txtPORTA05.setText(port);
	    	return -1;
	    }
	    
		return -1;
	}
	
	public void sendCmdBT(int cmd) {
		if(!confgPortSerial){
			JOptionPane.showMessageDialog(null, "Porta serial Não esta configurada!");
			return;
		}
		if(cmd == 0) {
			ConsolePrinter.consolePrinter(textPaneAtulizacao, ">> AT+BT_GET_SERIAL?");
		    serialAdapter.cmdSendSerial("AT+BT_GET_SERIAL?\r\n");
		}else if(cmd == 1) {
			ConsolePrinter.consolePrinter(textPaneAtulizacao, ">> AT+BT_VERSION?");
		    serialAdapter.cmdSendSerial("AT+BT_VERSION?\r\n");
		}else if(cmd == 2) {
			ConsolePrinter.consolePrinter(textPaneAtulizacao, ">> AT+BT_RST");
		    serialAdapter.cmdSendSerial("AT+BT_RST\r\n");
		}else if(cmd == 3) {
			ConsolePrinter.consolePrinter(textPaneAtulizacao, ">> AT+BT_VALUES_FACTORY");
		    serialAdapter.cmdSendSerial("AT+BT_VALUES_FACTORY\r\n");
		}else if(cmd == 4) {
			ConsolePrinter.consolePrinter(textPaneAtulizacao, ">> AT+BT_FORMAT_STORAGE");
		    serialAdapter.cmdSendSerial("AT+BT_FORMAT_STORAGE\r\n");
		}else if(cmd == 5) {
			ConsolePrinter.consolePrinter(textPaneAtulizacao, ">> AT+BT_REPORT_CYCLE?");
		    serialAdapter.cmdSendSerial("AT+BT_REPORT_CYCLE?\r\n");
		}else if(cmd == 6) {
			String tmp = txtReportCycle.getText();
			if(tmp.equals("") || tmp.equals(null)) {
				JOptionPane.showMessageDialog(null, "Informa um valor!");
				return;
			}
			ConsolePrinter.consolePrinter(textPaneAtulizacao, ">> AT+BT_REPORT_CYCLE="+tmp);
		    serialAdapter.cmdSendSerial("AT+BT_REPORT_CYCLE="+tmp+"\r\n");
		}else if(cmd == 7) {
			ConsolePrinter.consolePrinter(textPaneAtulizacao, ">> AT+BT_REPORT_CYCLE_SPEED?");
		    serialAdapter.cmdSendSerial("AT+BT_REPORT_CYCLE_SPEED?\r\n");
		}else if(cmd == 8) {
			String tmp = txtReportCycleSpeed.getText();
			if(tmp.equals("") || tmp.equals(null)) {
				JOptionPane.showMessageDialog(null, "Informa um valor!");
				return;
			}
			ConsolePrinter.consolePrinter(textPaneAtulizacao, ">> AT+BT_REPORT_CYCLE_SPEED="+tmp);
		    serialAdapter.cmdSendSerial("AT+BT_REPORT_CYCLE_SPEED="+tmp+"\r\n");
		}else if(cmd == 9) {
			ConsolePrinter.consolePrinter(textPaneAtulizacao, ">> AT+BT_TIME_MIN_SEND_MSG?");
		    serialAdapter.cmdSendSerial("AT+BT_TIME_MIN_SEND_MSG?\r\n");
		}else if(cmd == 10) {
			String tmp = txtSendEntryMSG.getText();
			if(tmp.equals("") || tmp.equals(null)) {
				JOptionPane.showMessageDialog(null, "Informa um valor!");
				return;
			}
			ConsolePrinter.consolePrinter(textPaneAtulizacao, ">> AT+BT_TIME_MIN_SEND_MSG="+tmp);
		    serialAdapter.cmdSendSerial("AT+BT_TIME_MIN_SEND_MSG="+tmp+"\r\n");
		}else if(cmd == 11) {
			ConsolePrinter.consolePrinter(textPaneAtulizacao, ">> AT+BT_MAX_TIME_AUTH?");
		    serialAdapter.cmdSendSerial("AT+BT_MAX_TIME_AUTH?\r\n");
		}else if(cmd == 12) {
			String tmp = txtTimeMaxAUTH.getText();
			if(tmp.equals("") || tmp.equals(null)) {
				JOptionPane.showMessageDialog(null, "Informa um valor!");
				return;
			}
			ConsolePrinter.consolePrinter(textPaneAtulizacao, ">> AT+BT_MAX_TIME_AUTH="+tmp);
		    serialAdapter.cmdSendSerial("AT+BT_MAX_TIME_AUTH="+tmp+"\r\n");
		}else if(cmd == 13) {
			ConsolePrinter.consolePrinter(textPaneAtulizacao, ">> AT+BT_MAX_TIME_RCV_ACK?");
		    serialAdapter.cmdSendSerial("AT+BT_MAX_TIME_RCV_ACK?\r\n");
		}else if(cmd == 14) {
			String tmp = txtTimeRcvACK.getText();
			if(tmp.equals("") || tmp.equals(null)) {
				JOptionPane.showMessageDialog(null, "Informa um valor!");
				return;
			}
			ConsolePrinter.consolePrinter(textPaneAtulizacao, ">> AT+BT_MAX_TIME_RCV_ACK="+tmp);
		    serialAdapter.cmdSendSerial("AT+BT_MAX_TIME_RCV_ACK="+tmp+"\r\n");
		}else if(cmd == 15) {
			ConsolePrinter.consolePrinter(textPaneAtulizacao, ">> AT+BT_NAME_DEVICE?");
		    serialAdapter.cmdSendSerial("AT+BT_NAME_DEVICE?\r\n");
		}else if(cmd == 16) {
			String tmp = txtNameBLE.getText();
			if(tmp.equals("") || tmp.equals(null)) {
				JOptionPane.showMessageDialog(null, "Informa um nome!");
				return;
			}
			ConsolePrinter.consolePrinter(textPaneAtulizacao, ">> AT+BT_NAME_DEVICE="+tmp);
		    serialAdapter.cmdSendSerial("AT+BT_NAME_DEVICE="+tmp+"\r\n");
		}else if(cmd == 17) {
			ConsolePrinter.consolePrinter(textPaneAtulizacao, ">> AT+BT_SPEED_CHANGE_CYCLE_REPORT?");
		    serialAdapter.cmdSendSerial("AT+BT_SPEED_CHANGE_CYCLE_REPORT?\r\n");
		}else if(cmd == 18) {
			String tmp = txtSpeedReport.getText();
			if(tmp.equals("") || tmp.equals(null)) {
				JOptionPane.showMessageDialog(null, "Informa um valor!");
				return;
			}
			ConsolePrinter.consolePrinter(textPaneAtulizacao, ">> AT+BT_SPEED_CHANGE_CYCLE_REPORT="+tmp);
		    serialAdapter.cmdSendSerial("AT+BT_SPEED_CHANGE_CYCLE_REPORT="+tmp+"\r\n");
		}else if(cmd == 19) {
			ConsolePrinter.consolePrinter(textPaneAtulizacao, ">> AT+BT_ENG_HOUR_INIT?");
		    serialAdapter.cmdSendSerial("AT+BT_ENG_HOUR_INIT?\r\n");
		}else if(cmd == 20) {
			String tmp = txtHoursINIT.getText();
			if(tmp.equals("") || tmp.equals(null)) {
				JOptionPane.showMessageDialog(null, "Informa um valor!");
				return;
			}
			tmp = tmp.replaceAll(",", ".");
			txtHoursINIT.setText(tmp);
			ConsolePrinter.consolePrinter(textPaneAtulizacao, ">> AT+BT_ENG_HOUR_INIT="+tmp);
		    serialAdapter.cmdSendSerial("AT+BT_ENG_HOUR_INIT="+tmp+"\r\n");
		}else if(cmd == 21) {
			ConsolePrinter.consolePrinter(textPaneAtulizacao, ">> AT+BT_ENG_HOUR_COUNT?");
		    serialAdapter.cmdSendSerial("AT+BT_ENG_HOUR_COUNT?\r\n");
		}else if(cmd == 22) {
			String tmp = txtHoursCOUNT.getText();
			if(tmp.equals("") || tmp.equals(null)) {
				JOptionPane.showMessageDialog(null, "Informa um valor!");
				return;
			}
			tmp = tmp.replaceAll(",", ".");
			txtHoursCOUNT.setText(tmp);
			ConsolePrinter.consolePrinter(textPaneAtulizacao, ">> AT+BT_ENG_HOUR_COUNT="+tmp);
		    serialAdapter.cmdSendSerial("AT+BT_ENG_HOUR_COUNT="+tmp+"\r\n");
		}else if(cmd == 23) {
			ConsolePrinter.consolePrinter(textPaneAtulizacao, ">> AT+BT_COMPASS?");
		    serialAdapter.cmdSendSerial("AT+BT_COMPASS?\r\n");
		}else if(cmd == 24) {
			String tmp = txtAngCOMPASS.getText();
			if(tmp.equals("") || tmp.equals(null)) {
				JOptionPane.showMessageDialog(null, "Informa um valor!");
				return;
			}
			ConsolePrinter.consolePrinter(textPaneAtulizacao, ">> AT+BT_COMPASS="+tmp);
		    serialAdapter.cmdSendSerial("AT+BT_COMPASS="+tmp+"\r\n");
		}else if(cmd == 25) {
			ConsolePrinter.consolePrinter(textPaneAtulizacao, ">> AT+BT_TIME_CHECK_COMPASS?");
		    serialAdapter.cmdSendSerial("AT+BT_TIME_CHECK_COMPASS?\r\n");
		}else if(cmd == 26) {
			String tmp = txtFConfCOMPASS.getText();
			if(tmp.equals("") || tmp.equals(null)) {
				JOptionPane.showMessageDialog(null, "Informa um valor!");
				return;
			}
			ConsolePrinter.consolePrinter(textPaneAtulizacao, ">> AT+BT_TIME_CHECK_COMPASS="+tmp);
		    serialAdapter.cmdSendSerial("AT+BT_TIME_CHECK_COMPASS="+tmp+"\r\n");
		}else if(cmd == 27) {
			ConsolePrinter.consolePrinter(textPaneAtulizacao, ">> AT+BT_DEBUG_ON");
		    serialAdapter.cmdSendSerial("AT+BT_DEBUG_ON\r\n");
		}else if(cmd == 28) {
			ConsolePrinter.consolePrinter(textPaneAtulizacao, ">> AT+BT_DEBUG_OFF");
		    serialAdapter.cmdSendSerial("AT+BT_DEBUG_OFF\r\n");
		}else if(cmd == 29) {
			String codUser = txtSetCodUSER.getText();
			ConsolePrinter.consolePrinter(textPaneAtulizacao, ">> AT+BT_COD_USER="+codUser);
		    serialAdapter.cmdSendSerial("AT+BT_COD_USER="+codUser+"\r\n");
		}else if(cmd == 30) {
			ConsolePrinter.consolePrinter(textPaneAtulizacao, ">> AT+BT_ENG_HOUR_NOW?");
		    serialAdapter.cmdSendSerial("AT+BT_ENG_HOUR_NOW?\r\n");
		}else if(cmd == 31) {
			ConsolePrinter.consolePrinter(textPaneAtulizacao, ">> AT_BT_DATA_START");
		    serialAdapter.cmdSendSerial("AT_BT_DATA_START\r\n");
		}else if(cmd == 32) {
			ConsolePrinter.consolePrinter(textPaneAtulizacao, ">> AT_BT_DATA_STOP");
		    serialAdapter.cmdSendSerial("AT_BT_DATA_STOP\r\n");
		}else if(cmd == 33) {
			ConsolePrinter.consolePrinter(textPaneAtulizacao, ">> AT_BT_PRM_START");
		    serialAdapter.cmdSendSerial("AT_BT_PRM_START\r\n");
		}else if(cmd == 34) {
			ConsolePrinter.consolePrinter(textPaneAtulizacao, ">> AT_BT_PRM_STOP");
		    serialAdapter.cmdSendSerial("AT_BT_PRM_STOP\r\n");
		}else if(cmd == 35) {
			ConsolePrinter.consolePrinter(textPaneAtulizacao, ">> AT+BT_QTD_MSG_FLASH?");
		    serialAdapter.cmdSendSerial("AT+BT_QTD_MSG_FLASH?\r\n");
		}else if(cmd == 36) {
			ConsolePrinter.consolePrinter(textPaneAtulizacao, ">> AT+BT_WIFI_ON");
		    serialAdapter.cmdSendSerial("AT+BT_WIFI_ON\r\n");
		}else if(cmd == 37) {
			ConsolePrinter.consolePrinter(textPaneAtulizacao, ">> AT+BT_WIFI_OFF");
		    serialAdapter.cmdSendSerial("AT+BT_WIFI_OFF\r\n");
		}else if(cmd == 38) {
			ConsolePrinter.consolePrinter(textPaneAtulizacao, ">> AT+BT_WIFI_LIST");
		    serialAdapter.cmdSendSerial("AT+BT_WIFI_LIST\r\n");
		}else if(cmd == 39) {
			ConsolePrinter.consolePrinter(textPaneAtulizacao, ">> AT+BT_SERVER_LIST");
		    serialAdapter.cmdSendSerial("AT+BT_SERVER_LIST\r\n");
		}else if(cmd == 40) {
			String tmp = txtReportCycleIgOFF.getText();
			ConsolePrinter.consolePrinter(textPaneAtulizacao, ">> AT+BT_REPORT_CYCLE_IG_OFF="+tmp);
		    serialAdapter.cmdSendSerial("AT+BT_REPORT_CYCLE_IG_OFF="+tmp+"\r\n");
		}else if(cmd == 41) {
			ConsolePrinter.consolePrinter(textPaneAtulizacao, ">> AT+BT_REPORT_CYCLE_IG_OFF?");
		    serialAdapter.cmdSendSerial("AT+BT_REPORT_CYCLE_IG_OFF?\r\n");
		}else if(cmd == 42) {
			String tmp = txtTimeCheckSERVER.getText();
			ConsolePrinter.consolePrinter(textPaneAtulizacao, ">> AT+BT_TIME_CHECK_CONFIG_SERVER="+tmp);
		    serialAdapter.cmdSendSerial("AT+BT_TIME_CHECK_CONFIG_SERVER="+tmp+"\r\n");
		}else if(cmd == 43) {
			ConsolePrinter.consolePrinter(textPaneAtulizacao, ">> AT+BT_TIME_CHECK_CONFIG_SERVER?");
		    serialAdapter.cmdSendSerial("AT+BT_TIME_CHECK_CONFIG_SERVER?\r\n");
		}else if(cmd == 44) {
			ConsolePrinter.consolePrinter(textPaneAtulizacao, ">> AT+BT_STATUS_WIFI?");
		    serialAdapter.cmdSendSerial("AT+BT_STATUS_WIFI?\r\n");
		}else if(cmd == 45) {
			ConsolePrinter.consolePrinter(textPaneAtulizacao, ">> AT+BT_SENSOR_RPM_ON");
		    serialAdapter.cmdSendSerial("AT+BT_SENSOR_RPM_ON\r\n");
		}else if(cmd == 46) {
			ConsolePrinter.consolePrinter(textPaneAtulizacao, ">> AT+BT_SENSOR_RPM_OFF");
		    serialAdapter.cmdSendSerial("AT+BT_SENSOR_RPM_OFF\r\n");
		}else if(cmd == 47) {
			String tmp = txtRotacaoAtual.getText();
			if(tmp.equals("") || tmp.equals(null)) {
				JOptionPane.showMessageDialog(null, "Informa um valor!");
				return;
			}
			
			tmp = tmp.replaceAll(",", ".");
			txtRotacaoAtual.setText(tmp);
			ConsolePrinter.consolePrinter(textPaneAtulizacao, ">> AT+BT_REAL_RPM="+tmp);
		    serialAdapter.cmdSendSerial("AT+BT_REAL_RPM="+tmp+"\r\n");
		}else if(cmd == 48) {
			ConsolePrinter.consolePrinter(textPaneAtulizacao, ">> AT+BT_RPM_NOW?");
		    serialAdapter.cmdSendSerial("AT+BT_RPM_NOW?\r\n");
		}else if(cmd == 49) {
			String tmp = txtCmdLivre.getText();
			if(tmp.equals("") || tmp.equals(null)) {
				JOptionPane.showMessageDialog(null, "Informa um COMANDO!");
				return;
			}
			ConsolePrinter.consolePrinter(textPaneAtulizacao, ">> "+tmp);
		    serialAdapter.cmdSendSerial(tmp+"\r\n");
		}else if(cmd == 50) {
			ConsolePrinter.consolePrinter(textPaneAtulizacao, ">> AT+BT_SIMULATED_FRAME_ON");
		    serialAdapter.cmdSendSerial("AT+BT_SIMULATED_FRAME_ON\r\n");
		}else if(cmd == 51) {
			ConsolePrinter.consolePrinter(textPaneAtulizacao, ">> AT+BT_SIMULATED_FRAME_OFF");
		    serialAdapter.cmdSendSerial("AT+BT_SIMULATED_FRAME_OFF\r\n");
		    
		    
		}else if(cmd == 52) {
			ConsolePrinter.consolePrinter(textPaneAtulizacao, ">> AT+BT_ELEVADOR_HOUR_NOW?");
		    serialAdapter.cmdSendSerial("AT+BT_ELEVADOR_HOUR_NOW?\r\n");
		}else if(cmd == 53) {
			ConsolePrinter.consolePrinter(textPaneAtulizacao, ">> AT+BT_ELEVADOR_HOUR_INIT?");
		    serialAdapter.cmdSendSerial("AT+BT_ELEVADOR_HOUR_INIT?\r\n");
		}else if(cmd == 54) {
			String tmp = txtHoursEsteiraINIT.getText();
			if(tmp.equals("") || tmp.equals(null)) {
				JOptionPane.showMessageDialog(null, "Informa um valor!");
				return;
			}
			tmp = tmp.replaceAll(",", ".");
			txtHoursEsteiraINIT.setText(tmp);
			ConsolePrinter.consolePrinter(textPaneAtulizacao, ">> AT+BT_ELEVADOR_HOUR_INIT="+tmp);
		    serialAdapter.cmdSendSerial("AT+BT_ELEVADOR_HOUR_INIT="+tmp+"\r\n");
		}else if(cmd == 55) {
			ConsolePrinter.consolePrinter(textPaneAtulizacao, ">> AT+BT_ELEVADOR_HOUR_COUNT?");
		    serialAdapter.cmdSendSerial("AT+BT_ELEVADOR_HOUR_COUNT?\r\n");
		}else if(cmd == 56) {
			String tmp = txtHoursEsteiraCOUNT.getText();
			if(tmp.equals("") || tmp.equals(null)) {
				JOptionPane.showMessageDialog(null, "Informa um valor!");
				return;
			}
			tmp = tmp.replaceAll(",", ".");
			txtHoursEsteiraCOUNT.setText(tmp);
			ConsolePrinter.consolePrinter(textPaneAtulizacao, ">> AT+BT_ELEVADOR_HOUR_COUNT="+tmp);
		    serialAdapter.cmdSendSerial("AT+BT_ELEVADOR_HOUR_COUNT="+tmp+"\r\n");
		    
	    
		}else if(cmd == 57) {
			ConsolePrinter.consolePrinter(textPaneAtulizacao, ">> AT+BT_PILOTO_HOUR_NOW?");
		    serialAdapter.cmdSendSerial("AT+BT_PILOTO_HOUR_NOW?\r\n");
		}else if(cmd == 58) {
			ConsolePrinter.consolePrinter(textPaneAtulizacao, ">> AT+BT_PILOTO_HOUR_INIT?");
		    serialAdapter.cmdSendSerial("AT+BT_PILOTO_HOUR_INIT?\r\n");
		}else if(cmd == 59) {
			String tmp = txtHoursPilotoINIT.getText();
			if(tmp.equals("") || tmp.equals(null)) {
				JOptionPane.showMessageDialog(null, "Informa um valor!");
				return;
			}
			tmp = tmp.replaceAll(",", ".");
			txtHoursPilotoINIT.setText(tmp);
			ConsolePrinter.consolePrinter(textPaneAtulizacao, ">> AT+BT_PILOTO_HOUR_INIT="+tmp);
		    serialAdapter.cmdSendSerial("AT+BT_PILOTO_HOUR_INIT="+tmp+"\r\n");
		}else if(cmd == 60) {
			ConsolePrinter.consolePrinter(textPaneAtulizacao, ">> AT+BT_PILOTO_HOUR_COUNT?");
		    serialAdapter.cmdSendSerial("AT+BT_PILOTO_HOUR_COUNT?\r\n");
		}else if(cmd == 61) {
			String tmp = txtHoursPilotoCOUNT.getText();
			if(tmp.equals("") || tmp.equals(null)) {
				JOptionPane.showMessageDialog(null, "Informa um valor!");
				return;
			}
			tmp = tmp.replaceAll(",", ".");
			txtHoursPilotoCOUNT.setText(tmp);
			ConsolePrinter.consolePrinter(textPaneAtulizacao, ">> AT+BT_PILOTO_HOUR_COUNT="+tmp);
		    serialAdapter.cmdSendSerial("AT+BT_PILOTO_HOUR_COUNT="+tmp+"\r\n");
		}else if(cmd == 62) {
			ConsolePrinter.consolePrinter(textPaneAtulizacao, ">> AT+BT_STATUS_CONN_WIFI?");
		    serialAdapter.cmdSendSerial("AT+BT_STATUS_CONN_WIFI?\r\n");
		}else if(cmd == 63) {
			ConsolePrinter.consolePrinter(textPaneAtulizacao, ">> AT+BT_SCAN_WIFI");
		    serialAdapter.cmdSendSerial("AT+BT_SCAN_WIFI\r\n");
		}
		
	}
}
