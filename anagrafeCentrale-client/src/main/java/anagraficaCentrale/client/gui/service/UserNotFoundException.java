package anagraficaCentrale.client.gui.service;

import anagraficaCentrale.client.gui.GUIConstants;

public class UserNotFoundException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public UserNotFoundException() {
		super(GUIConstants.LANG.errUserInvalid);
	}

}
