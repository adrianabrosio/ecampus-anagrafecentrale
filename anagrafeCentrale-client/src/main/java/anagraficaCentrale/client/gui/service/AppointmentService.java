package anagraficaCentrale.client.gui.service;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.DayOfWeek;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.MatteBorder;

import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;
import com.github.lgooddatepicker.components.DatePickerSettings.DateArea;

import anagraficaCentrale.client.gui.GUIConstants;
import anagraficaCentrale.client.gui.OperationPanel;
import anagraficaCentrale.client.gui.component.AcServiceButton;
import anagraficaCentrale.exception.AcServerRuntimeException;
import anagraficaCentrale.utils.ClientServerConstants.ServiceType;

public class AppointmentService extends GenericService {
	private JTextArea textField;
	private DatePicker datePicker1;
	private ServiceType serviceType;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AppointmentService(OperationPanel op, ServiceType service) {
		super(op);
		setTitle(GUIConstants.LANG.lblUserCreationSrvTitle);
		this.serviceType = service;
	}

	@Override
	protected JPanel generateInnerPanel() {
		JPanel attrPanel = new JPanel();
		attrPanel.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(5, 15, 5, 15);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 2;
        gbc.weighty = 4;

		textField = new JTextArea();
		attrPanel.add(textField, gbc);

		
		gbc.weightx = 1;
		gbc.weighty = 1;
		JLabel lblDate = new JLabel(GUIConstants.LANG.lblAppointmentDate + "*");
		gbc.gridy = 4;
		gbc.gridx = 0;
		attrPanel.add(lblDate, gbc);

		// Create a date picker with some custom settings.
		DatePickerSettings dateSettings = new DatePickerSettings();
	    dateSettings.setFirstDayOfWeek(DayOfWeek.MONDAY);
	    dateSettings.setLocale(Locale.ITALIAN);
	    dateSettings.setAllowEmptyDates(false);
	    //final LocalDate today = LocalDate.now();
	    //dateSettings.setDateRangeLimits(today, today.plusYears(1));
	    dateSettings.setColor(DateArea.CalendarBackgroundSelectedDate, GUIConstants.BACKGROUND_COLOR_1);
	    dateSettings.setFormatForDatesCommonEra("dd/MM/yyyy");
		datePicker1 = new DatePicker(dateSettings);

		gbc.gridx = 1;
		attrPanel.add(datePicker1, gbc);

		AcServiceButton createUserButton = new AcServiceButton(GUIConstants.LANG.lblAppointmentCreateBtn);
		createUserButton.setBorder(new MatteBorder(2, 3, 2, 3, operationPanel.guiBackgroundColor));
		createUserButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				if(!isFormValid())
					return;
				
				List<String[]> userProps = new ArrayList<>();
				
				userProps.add(new String[]{"portal_type", operationPanel.getPortalType()});
				userProps.add(new String[]{"creator_user_id", operationPanel.getConnectionManager().getUserAttribute("id")});
				String sqlDateFormated = datePicker1.getDate().format(DateTimeFormatter.ISO_DATE);
				userProps.add(new String[]{"appointmentDate", sqlDateFormated});
				userProps.add(new String[]{"request_type", ""+serviceType});

				try{
					operationPanel.getConnectionManager().createAppointment(serviceType, userProps);
				}catch(AcServerRuntimeException | UnsupportedServiceException e){
					operationPanel.popupError(e);
					return;
				}
				operationPanel.popupInfo(GUIConstants.LANG.msgAppointmentSuccess);
				clearForm();
			}

		});
		
		attrPanel.setBackground(GUIConstants.OPERATION_PANEL_BACKGROUND);

		JPanel innerPanel = new JPanel();
		innerPanel.setLayout(new BorderLayout());
		innerPanel.add(attrPanel, BorderLayout.CENTER);
		
		
		JPanel lowerPanel = new JPanel();
		lowerPanel.setLayout(new GridLayout(0,1));
		lowerPanel.setBackground(GUIConstants.OPERATION_PANEL_BACKGROUND);
		lowerPanel.add(createUserButton);
		//lowerPanel.add(new JLabel(""));
		
		innerPanel.add(lowerPanel, BorderLayout.AFTER_LAST_LINE);
		return innerPanel;
	}

	protected void clearForm() {
		datePicker1.setDateToToday();
	}

	protected boolean isFormValid() {
		//validation
		boolean formIncomplete = false;
		
		if(datePicker1.getText() == null || datePicker1.getText().equals("") || !datePicker1.isTextFieldValid()){
			formIncomplete = true;
		}
		return !formIncomplete;
	}

}
