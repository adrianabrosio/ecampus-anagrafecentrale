package anagraficaCentrale.client.gui.service;

import anagraficaCentrale.client.gui.GUIConstants;
import anagraficaCentrale.client.gui.OperationPanel;
import anagraficaCentrale.utils.ClientServerConstants.ServiceType;

public class MarriageCertRequestService extends SimpleRequestService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MarriageCertRequestService(OperationPanel op) {
		super(op);
		setTitle(GUIConstants.LANG.lbl_CERT_MATR_SrvTitle);
	}

	@Override
	protected String getTextAreaContent() {
		return GUIConstants.LANG.lbl_CERT_MATR_SrvText;
	}

	@Override
	protected ServiceType getServiceType() {
		return ServiceType.CERT_MATR;
	}


}
