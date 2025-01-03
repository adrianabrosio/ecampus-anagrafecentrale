package anagraficaCentrale.client.gui.service;

import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import anagraficaCentrale.client.gui.OperationPanel;

/**
 * this class represent the factory of the service panels.
 * It is used to generate the panels for the services, managing the parameters transformation if needed.
 * 
 * @author Adriana Brosio
 */
public class ServicePanelFactory {

	public static GenericService generateDummyPanel(OperationPanel operationPanel){
		return new GenericService(operationPanel, null){
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
		return new CITempRequestService(operationPanel);
	}

	public static GenericService generateChangeResidencePanel(OperationPanel operationPanel) {
		return new ResidenceChangeRequestService(operationPanel);
	}
	
	public static GenericService generateBirthCertPanel(OperationPanel operationPanel) {
		return new BirthCertRequestService(operationPanel);
	}

	public static GenericService generateMarriageCertPanel(OperationPanel operationPanel) {
		return new MarriageCertRequestService(operationPanel);
	}

	public static GenericService generateFamilyStatusPanel(OperationPanel operationPanel) {
		return new FamilyStatusRequestService(operationPanel);
	}

	public static GenericService generateDoctorChangePanel(OperationPanel operationPanel) {
		return new ChangeDoctorRequestService(operationPanel);
	}

	public static GenericService generateMedicalAppointmentPanel(OperationPanel operationPanel) {
		return new MedicalAppointmentService(operationPanel);
	}

	public static GenericService generateTicketPaymentPanel(OperationPanel operationPanel) {
		return new TicketPaymentService(operationPanel);	
	}

	public static GenericService generateSchoolFeePaymentPanel(OperationPanel operationPanel) {
		return new SchoolFeePaymentService(operationPanel);
	}

	public static GenericService generateCanteenPaymentPanel(OperationPanel operationPanel) {
		return new CanteenPaymentService(operationPanel);
	}

	public static GenericService generateSchoolRegistrationPanel(OperationPanel operationPanel) {
		return new SchoolRegistrationService(operationPanel);
	}

	public static GenericService generateTeacherInterviewPanel(OperationPanel operationPanel) {
		return new TeacherInterviewAppointmentService(operationPanel);
	}

	public static GenericService generateUserEditPanel(OperationPanel operationPanel) {
		return new UserCreationService(operationPanel, true);
	}

	public static GenericService generateAdminRequestManagementPanel(OperationPanel operationPanel, Map<String, String> requestData) {
		return new RequestManagementService(operationPanel, requestData);
	}
}
