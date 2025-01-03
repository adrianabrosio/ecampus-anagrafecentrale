package anagraficaCentrale.client.gui.service;

import anagraficaCentrale.client.gui.GUIConstants;
import anagraficaCentrale.client.gui.OperationPanel;
import anagraficaCentrale.utils.ClientServerConstants.ServiceType;

/**
 * this class represent the service of the teacher interview. It specializes the abstract appointment service
 * @author Adriana Brosio
 */
public class TeacherInterviewAppointmentService extends AppointmentService {

	private static final long serialVersionUID = 1L;

	public TeacherInterviewAppointmentService(OperationPanel op) {
		super(op);
		setTitle(GUIConstants.LANG.lbl_COLL_INS_SrvTitle);
	}

	@Override
	protected int getUserListType() {
		return AppointmentService.RELATIONS_ONLY;
	}

	@Override
	protected String getTextAreaContent() {
		return GUIConstants.LANG.lbl_COLL_INS_SrvText;
	}

	@Override
	protected ServiceType getServiceType() {
		return ServiceType.COLL_INS;
	}

}
