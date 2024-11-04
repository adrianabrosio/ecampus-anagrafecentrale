package anagraficaCentrale.client.gui.service;

import anagraficaCentrale.client.gui.GUIConstants;

public class RequestNotFoundException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public RequestNotFoundException() {
		super(GUIConstants.LANG.errRequestIdInvalid);
	}

}
