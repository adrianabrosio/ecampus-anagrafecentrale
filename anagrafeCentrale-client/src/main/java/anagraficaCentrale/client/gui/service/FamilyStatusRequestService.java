package anagraficaCentrale.client.gui.service;

import anagraficaCentrale.client.gui.GUIConstants;
import anagraficaCentrale.client.gui.OperationPanel;
import anagraficaCentrale.utils.ClientServerConstants.ServiceType;

public class FamilyStatusRequestService extends SimpleRequestService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public FamilyStatusRequestService(OperationPanel op) {
		super(op);
		setTitle(GUIConstants.LANG.lbl_STAT_FAM_SrvTitle);
	}

	@Override
	protected String getTextAreaContent() {
		operationPanel.getConnectionManager().refreshUserData();
		String text = GUIConstants.LANG.lbl_STAT_FAM_SrvText;
		String[] prorToReplace = new String[]{"first_name" , "surname", "birth_town", "birth_province", "birthdate", "tax_id_code", "address", "town", "province", "zip_code"};
		for(String attrName : prorToReplace)
			text = text.replaceAll("!"+attrName, operationPanel.getConnectionManager().getUserAttribute(attrName));
		return text;
	}

	@Override
	protected ServiceType getServiceType() {
		return ServiceType.STAT_FAM;
	}

}
