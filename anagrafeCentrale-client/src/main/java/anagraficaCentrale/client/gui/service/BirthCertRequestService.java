package anagraficaCentrale.client.gui.service;

import anagraficaCentrale.client.gui.GUIConstants;
import anagraficaCentrale.client.gui.OperationPanel;
import anagraficaCentrale.utils.ClientServerConstants.ServiceType;

public class BirthCertRequestService extends SimpleRequestService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BirthCertRequestService(OperationPanel op) {
		super(op);
		setTitle(GUIConstants.LANG.lbl_CERT_NASC_SrvTitle);
	}

	@Override
	protected String getTextAreaContent() {
		return GUIConstants.LANG.lbl_CERT_NASC_SrvText;
	}

	@Override
	protected ServiceType getServiceType() {
		return ServiceType.CERT_NASC;
	}

}
