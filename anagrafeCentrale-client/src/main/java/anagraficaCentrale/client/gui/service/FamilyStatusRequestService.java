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
		return GUIConstants.LANG.lbl_STAT_FAM_SrvText;
	}

	@Override
	protected ServiceType getServiceType() {
		return ServiceType.STAT_FAM;
	}

}
