package com.bluechip.telematics.controller;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.bluechip.telematics.view.Principal;

/**
 * 
 * Classe main
 * @author Caio Jose Carriel
 * @version 2.0
 * @company BLUECHIP Electronics
 * @since 30/05/2020
 *
 */

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Principal frame = new Principal();
		frame.setVisible(true);
	}

}
