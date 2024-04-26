package anagraficaCentrale.client.gui.service;

import anagraficaCentrale.utils.ClientServerConstants.ServiceType;

public class UnsupportedServiceException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UnsupportedServiceException(ServiceType serviceType) {
		super("Service type unsupported: \"" + serviceType + "\"");
	}

}
