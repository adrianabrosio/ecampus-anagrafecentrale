package anagraficaCentrale.client.gui.service;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import anagraficaCentrale.client.gui.GUIConstants;
import anagraficaCentrale.client.gui.OperationPanel;
import anagraficaCentrale.utils.ClientServerConstants.ServiceType;

public class ServicePanelFactory {

	public static GenericService generateDummyPanel(OperationPanel operationPanel){
		return new GenericService(operationPanel){
			private static final long serialVersionUID = 1L;

			@Override
			protected JPanel generateInnerPanel() {
				JLabel serviceDescription = new JLabel("Generic Description");
				serviceDescription.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
				JPanel retPanel = new JPanel();
				retPanel.add(serviceDescription);
				return retPanel;
			}
			
		};
	}
	
	public static GenericService generateUserCreationPanel(OperationPanel operationPanel){
		return new UserCreationService(operationPanel);
	}

	public static GenericService generateCIAppointmentPanel(OperationPanel operationPanel) {
		return new CIAppointmentService(operationPanel);
	}

	public static GenericService generateCITempPanel(OperationPanel operationPanel) {
		// TODO Auto-generated method stub
		return null;
	}

	public static GenericService generateChangeDomicilePanel(OperationPanel operationPanel) {
		// TODO Auto-generated method stub
		return null;
	}

	public static GenericService generateChangeResidencePanel(OperationPanel operationPanel) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public static GenericService generateBirthCertPanel(OperationPanel operationPanel) {
		// TODO Auto-generated method stub
		return null;
	}

	public static GenericService generateMarriageCertPanel(OperationPanel operationPanel) {
		// TODO Auto-generated method stub
		return null;
	}

	public static GenericService generateFamilyStatusPanel(OperationPanel operationPanel) {
		// TODO Auto-generated method stub
		return null;
	}

	public static GenericService generateDoctorChangePanel(OperationPanel operationPanel) {
		// TODO Auto-generated method stub
		return null;
	}

	public static GenericService generateMedicalAppointmentPanel(OperationPanel operationPanel) {
		// TODO Auto-generated method stub
		return null;
	}

	public static GenericService generateTicketPaymentPanel(OperationPanel operationPanel) {
		// TODO Auto-generated method stub
		return null;
	}

	public static GenericService generateSchoolFeePaymentPanel(OperationPanel operationPanel) {
		// TODO Auto-generated method stub
		return null;
	}

	public static GenericService generateCanteenPaymentPanel(OperationPanel operationPanel) {
		// TODO Auto-generated method stub
		return null;
	}

	public static GenericService generateSchoolRegistrationPanel(OperationPanel operationPanel) {
		// TODO Auto-generated method stub
		return null;
	}

	public static GenericService generateTeacherInterviewPanel(OperationPanel operationPanel) {
		return new TeacherInterviewAppointmentService(operationPanel);
	}
}
