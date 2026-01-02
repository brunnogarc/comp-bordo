package com.bluechip.telematics.model;

import java.awt.Color;

import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

/**
 * 
 * Classe da janela do console 
 * @author Caio Jose Carriel
 * @version 2.0
 * @company BLUECHIP Electronics
 * @since 30/05/2020
 *
 */

public class ConsolePrinter {
	public static void consolePrinter(JTextPane console,String text){
		StyledDocument doc = console.getStyledDocument();
		SimpleAttributeSet keyWord = new SimpleAttributeSet();
		StyleConstants.setFontSize(keyWord, 15);
		StyleConstants.setForeground(keyWord, Color.BLACK);
		StyleConstants.setBackground(keyWord, Color.WHITE);
		try {
			doc.insertString(doc.getLength(), text+"\n",keyWord);
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
