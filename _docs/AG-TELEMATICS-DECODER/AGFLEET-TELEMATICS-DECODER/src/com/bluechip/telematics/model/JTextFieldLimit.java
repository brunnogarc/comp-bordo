package com.bluechip.telematics.model;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 * 
 * classe de mascaras
 * @author Caio Jose Carriel
 * @version 2.0
 * @company BLUECHIP Electronics
 * @since 30/05/2020
 *
 */

public class JTextFieldLimit extends PlainDocument{
	private int limit;
	private boolean hex;
	   public JTextFieldLimit(int limit) {
	      super();
	      this.limit = limit;
	   }
	   public JTextFieldLimit(int limit, boolean hex) {
	      super();
	      this.limit = limit;
	      this.hex = hex;
	   }
	   public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
	      if (str == null)
	         return;
	      if ((getLength() + str.length()) <= limit) {
	    	  if(hex) {
		    	  StringBuilder buffer = new StringBuilder(str.length());
		    	  for (int i = 0; i < str.length(); i++) {
	    	        char ch = str.charAt(i);
	    	        if (ch == '0' || ch == '1' || ch == '2' || ch == '3' || ch == '4' || ch == '5' || ch == '6' || ch == '7' || ch == '8' || ch == '9' || ch == 'A' || ch == 'a' || ch == 'B' || ch == 'b' || ch == 'C' || ch == 'c' || ch == 'D' || ch == 'd' || ch == 'E' || ch == 'e' || ch == 'F' || ch == 'f') {
	    	            buffer.append(ch);
	    	        }
		    	  }
		    	  super.insertString(offset, buffer.toString().toUpperCase(), attr);
	    	  }else {
	    		  super.insertString(offset, str.toUpperCase(), attr);
	    	  }
	    	}
	   }
}
