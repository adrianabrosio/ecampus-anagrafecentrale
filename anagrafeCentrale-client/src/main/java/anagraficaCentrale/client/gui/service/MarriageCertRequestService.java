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
		operationPanel.getConnectionManager().refreshUserData();
		String text = GUIConstants.LANG.lbl_CERT_MATR_SrvText;
		String[] prorToReplace = new String[]{"first_name" , "surname", "birth_town", "birth_province", "birthdate", "tax_id_code", "address", "town", "province", "zip_code"};
		for(String attrName : prorToReplace)
			text = text.replaceAll("!"+attrName, operationPanel.getConnectionManager().getUserAttribute(attrName));
		return text;
	}

	@Override
	protected ServiceType getServiceType() {
		return ServiceType.CERT_MATR;
	}


}
