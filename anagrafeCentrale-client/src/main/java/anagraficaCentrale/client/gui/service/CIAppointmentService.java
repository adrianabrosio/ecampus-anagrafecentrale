package anagraficaCentrale.client.gui.service;

import anagraficaCentrale.client.gui.GUIConstants;
import anagraficaCentrale.client.gui.OperationPanel;
import anagraficaCentrale.utils.ClientServerConstants.ServiceType;

/**
 * this class represent the service of the change doctor request. It specializes the abstract appointment service
 * @author Adriana Brosio
 */
public class CIAppointmentService extends AppointmentService {

	private static final long serialVersionUID = 1L;

	public CIAppointmentService(OperationPanel op) {
		super(op);
		setTitle(GUIConstants.LANG.lbl_APP_CI_SrvTitle);
	}

	@Override
	protected int getUserListType() {
		return AppointmentService.USER_AND_RELATIONS;
	}

	@Override
	protected String getTextAreaContent() {
		return GUIConstants.LANG.lbl_APP_CI_SrvText;
	}

	@Override
	protected ServiceType getServiceType() {
		return ServiceType.APP_CI;
	}

}
