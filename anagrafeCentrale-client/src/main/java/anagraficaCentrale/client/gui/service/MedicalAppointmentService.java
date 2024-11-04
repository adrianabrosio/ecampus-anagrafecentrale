package anagraficaCentrale.client.gui.service;

import java.awt.BorderLayout;
import java.awt.Font;
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
import anagraficaCentrale.client.gui.component.AcTextField;
import anagraficaCentrale.utils.ClientServerConstants.ServiceType;

public class MedicalAppointmentService extends GenericService {
	private JTextArea textField;
	private AcTextField visitText;
	private DatePicker datePicker1;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MedicalAppointmentService(OperationPanel op) {
		super(op, null);
		setTitle(GUIConstants.LANG.lbl_PREN_VIS_SrvTitle);
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
		gbc.gridheight = 1;
		gbc.gridwidth = 2;

		textField = new JTextArea(GUIConstants.LANG.lbl_PREN_VIS_SrvText);
		Font textAreaFont = new Font(textField.getFont().getFontName(), textField.getFont().getStyle(), 16);
		textField.setFont(textAreaFont);
		attrPanel.add(textField, gbc);

		//lista persone per appuntamento
		Map<String, Map<String,String>> usersList = new HashMap<>();

		Map<String,String> tmpMap = new HashMap<>();
		tmpMap.put("first_name", operationPanel.getConnectionManager().getUserAttribute("first_name"));
		tmpMap.put("surname", operationPanel.getConnectionManager().getUserAttribute("surname"));
		usersList.put(operationPanel.getConnectionManager().getUserAttribute("tax_id_code"), tmpMap);
		usersList.putAll(operationPanel.getConnectionManager().getRelationsData());

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

		JComboBox<String> userList = new JComboBox<>(usersList.keySet().toArray(new String[0]));
		userList.setBackground(GUIConstants.OPERATION_PANEL_BACKGROUND);
		userList.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
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

		//empty
		gbc.gridy++;
		attrPanel.add(new JLabel(), gbc);
		
		JLabel lblVisit = new JLabel(GUIConstants.LANG.lblPREN_VIS_visit + "*");
		gbc.gridy++;
		gbc.gridx = 0;
		attrPanel.add(lblVisit, gbc);
		
		visitText = new AcTextField(true);
		gbc.gridx = 1;
		attrPanel.add(visitText, gbc);
		
		//data appuntamento
		JLabel lblDate = new JLabel(GUIConstants.LANG.lblAppointmentDate + "*");
		gbc.gridy++;
		gbc.gridx = 0;
		attrPanel.add(lblDate, gbc);

		// Create a date picker with some custom settings.
		DatePickerSettings dateSettings = new DatePickerSettings();
		dateSettings.setFirstDayOfWeek(DayOfWeek.MONDAY);
		dateSettings.setLocale(Locale.ITALIAN);
		dateSettings.setAllowEmptyDates(false);
		dateSettings.setColor(DateArea.CalendarBackgroundSelectedDate, GUIConstants.BACKGROUND_COLOR_1);
		dateSettings.setFormatForDatesCommonEra("dd/MM/yyyy");
		datePicker1 = new DatePicker(dateSettings);
		datePicker1.getComponentDateTextField().setEditable(false);

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
				userProps.add(new String[]{"tax_id_code", (String) userList.getSelectedItem()});
				userProps.add(new String[]{"medical_examination", visitText.getText()});
				String sqlDateFormated = datePicker1.getDate().format(DateTimeFormatter.ISO_DATE);
				userProps.add(new String[]{"appointment_date", sqlDateFormated});
				userProps.add(new String[]{"request_type", ""+ServiceType.PREN_VIS});

				try{
					operationPanel.getConnectionManager().createAppointment(ServiceType.PREN_VIS, userProps);
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
		
		if(!visitText.fieldIsValid())
			formIncomplete = true;

		if(datePicker1.getText() == null || datePicker1.getText().equals("") || !datePicker1.isTextFieldValid() || datePicker1.getDate().isBefore(LocalDate.now())){
			formIncomplete = true;
		}
		return !formIncomplete;
	}

}
