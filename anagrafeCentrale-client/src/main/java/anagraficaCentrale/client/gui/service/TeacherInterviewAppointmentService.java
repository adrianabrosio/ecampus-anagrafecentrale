package anagraficaCentrale.client.gui.service;

import anagraficaCentrale.client.gui.GUIConstants;
import anagraficaCentrale.client.gui.OperationPanel;

public class TeacherInterviewAppointmentService extends AppointmentService {

	/**
	 * 
	 */
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

}
