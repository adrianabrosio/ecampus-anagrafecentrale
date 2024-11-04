package anagraficaCentrale.client.gui.service;

import anagraficaCentrale.utils.ClientServerConstants.ServiceType;

public class UnsupportedServiceException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UnsupportedServiceException(ServiceType serviceType) {
		super("Unsupported service type: \"" + serviceType + "\"");
	}

	public UnsupportedServiceException(ServiceType serviceType, String string) {
		super("Unsupported service type: \"" + serviceType + "\" "+string);
	}

	public UnsupportedServiceException(String serviceType) {
		super("Unsupported service type: \"" + serviceType + "\"");
	}

}
