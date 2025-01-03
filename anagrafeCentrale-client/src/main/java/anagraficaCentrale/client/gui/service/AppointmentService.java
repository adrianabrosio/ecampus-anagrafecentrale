package anagraficaCentrale.client.gui.service;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.MatteBorder;

import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;
import com.github.lgooddatepicker.components.DatePickerSettings.DateArea;

import anagraficaCentrale.client.gui.GUIConstants;
import anagraficaCentrale.client.gui.OperationPanel;
import anagraficaCentrale.client.gui.component.AcServiceButton;
import anagraficaCentrale.client.gui.component.AcTextField;
import anagraficaCentrale.utils.ClientServerConstants.ServiceType;

/**
 * this class represent a service that can be used to create an appointment
 * it is abstract because it can be used by different type of appointment services
 * 
 * @author Adriana Brosio
 */
public abstract class AppointmentService extends GenericService {
	private JEditorPane textField;
	private DatePicker datePicker1;
	private boolean panelIsInvalid;
	private JComboBox<String> userList;

	private int userListType;
	public static final int USER_ONLY=0;
	public static final int RELATIONS_ONLY=1;
	public static final int USER_AND_RELATIONS=2;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AppointmentService(OperationPanel op) {
		super(op, null);
	}

	@Override
	protected JPanel generateInnerPanel() {
		this.userListType = getUserListType();
		JPanel attrPanel = new JPanel();
		attrPanel.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.CENTER;
		gbc.insets = new Insets(5, 15, 5, 15);
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridheight = 1;
		gbc.gridwidth = 2;

		textField = new JEditorPane();
		textField.setContentType("text/html");
		textField.setText(getTextAreaContent());
		textField.setEditable(false);
		attrPanel.add(textField, gbc);

		//list of user for the appointment
		Map<String, Map<String,String>> usersList = new HashMap<>();

		if(this.userListType == USER_ONLY || this.userListType == USER_AND_RELATIONS){
			Map<String,String> tmpMap = new HashMap<>();
			tmpMap.put("first_name", operationPanel.getConnectionManager().getUserAttribute("first_name"));
			tmpMap.put("surname", operationPanel.getConnectionManager().getUserAttribute("surname"));
			usersList.put(operationPanel.getConnectionManager().getUserAttribute("tax_id_code"), tmpMap);
		}
		if(this.userListType == RELATIONS_ONLY || this.userListType == USER_AND_RELATIONS){
			usersList.putAll(operationPanel.getConnectionManager().getRelationsDataByTaxIdCode());
		}

		gbc.gridheight = 1;
		gbc.gridwidth = 1;
		gbc.weightx = 1;
		gbc.weighty = 1;
		JLabel lblFirstName = new JLabel(GUIConstants.LANG.lbluserCreationFirstName);
		gbc.gridy++;
		gbc.gridx = 0;
		attrPanel.add(lblFirstName, gbc);

		AcTextField textFirstName = new AcTextField();
		gbc.gridx = 1;
		attrPanel.add(textFirstName, gbc);
		textFirstName.setEditable(false);

		JLabel lblSurname = new JLabel(GUIConstants.LANG.lbluserCreationSurname);
		gbc.gridy++;
		gbc.gridx = 0;
		attrPanel.add(lblSurname, gbc);

		AcTextField textSurname = new AcTextField();
		gbc.gridx = 1;
		attrPanel.add(textSurname, gbc);
		textSurname.setEditable(false);

		JLabel lblUser = new JLabel(GUIConstants.LANG.lblAppointmentListUser + "*");
		gbc.gridy++;
		gbc.gridx = 0;
		attrPanel.add(lblUser, gbc);

		String[] users = usersList.keySet().toArray(new String[0]);
		if(users != null && users.length > 0) {
			userList = new JComboBox<>(users);
			userList.setBackground(GUIConstants.OPERATION_PANEL_BACKGROUND);
			userList.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					//if selection is changed, update the first name and surname
					String selection = (String) userList.getSelectedItem();
					textFirstName.setText(usersList.get(selection).get("first_name"));
					textSurname.setText(usersList.get(selection).get("surname"));
				}
			});
			gbc.gridx = 1;
			attrPanel.add(userList, gbc);


			String selection = (String) userList.getSelectedItem();
			textFirstName.setText(usersList.get(selection).get("first_name"));
			textSurname.setText(usersList.get(selection).get("surname"));
			panelIsInvalid = false;
		} else {
			JLabel lblUserError = new JLabel(GUIConstants.LANG.errorNoRelationFound);
			lblUserError.setForeground(Color.RED);
			panelIsInvalid = true;
			gbc.gridx = 1;
			attrPanel.add(lblUserError, gbc);
		}

		//appointment date
		JLabel lblDate = new JLabel(GUIConstants.LANG.lblAppointmentDate + "*");
		gbc.gridy = 4;
		gbc.gridx = 0;
		attrPanel.add(lblDate, gbc);

		// Create a date picker with some custom settings.
		DatePickerSettings dateSettings = new DatePickerSettings();
		//Italian settings
		dateSettings.setFirstDayOfWeek(DayOfWeek.MONDAY);
		dateSettings.setLocale(Locale.ITALIAN);
		dateSettings.setAllowEmptyDates(false);
		dateSettings.setColor(DateArea.CalendarBackgroundSelectedDate, GUIConstants.BACKGROUND_COLOR_1);
		dateSettings.setFormatForDatesCommonEra("dd/MM/yyyy");
		datePicker1 = new DatePicker(dateSettings);
		datePicker1.getComponentDateTextField().setEditable(false);

		//you cannot make appointments in the past and more than a year into the future
		try{LocalDate today = LocalDate.now();
		dateSettings.setDateRangeLimits(today, today.plusYears(1));
		}catch(Exception e){
			operationPanel.popupError(e);
		}

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

				userProps.add(new String[]{"portal_type", ""+operationPanel.getPortalType().getValue()});
				userProps.add(new String[]{"creator_user_id", operationPanel.getConnectionManager().getUserAttribute("id")});
				userProps.add(new String[]{"request_type", ""+getServiceType()});
				userProps.add(new String[]{"tax_id_code", (String) userList.getSelectedItem()});
				String sqlDateFormated = datePicker1.getDate().format(DateTimeFormatter.ISO_DATE);
				userProps.add(new String[]{"appointment_date", sqlDateFormated});

				try{
					operationPanel.getConnectionManager().createAppointment(getServiceType(), userProps);
				}catch(Exception e){
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

		innerPanel.add(lowerPanel, BorderLayout.AFTER_LAST_LINE);
		return innerPanel;
	}

	protected abstract int getUserListType();

	protected abstract String getTextAreaContent();

	protected abstract ServiceType getServiceType();

	protected void clearForm() {
		datePicker1.setDateToToday();
	}

	protected boolean isFormValid() {
		//validation
		boolean formIncomplete = false;

		if(datePicker1.getText() == null || datePicker1.getText().equals("") || !datePicker1.isTextFieldValid() || datePicker1.getDate().isBefore(LocalDate.now())){
			formIncomplete = true;
		}

		if(panelIsInvalid){
			operationPanel.popupInfo(GUIConstants.LANG.errorNoRelationFound);
			formIncomplete = true;
		}

		return !formIncomplete;
	}

}
