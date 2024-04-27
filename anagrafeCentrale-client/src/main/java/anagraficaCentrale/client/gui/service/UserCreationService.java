package anagraficaCentrale.client.gui.service;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.NoSuchAlgorithmException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.MatteBorder;

import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;
import com.github.lgooddatepicker.components.DatePickerSettings.DateArea;

import anagraficaCentrale.client.core.ConnectionManager;
import anagraficaCentrale.client.gui.GUIConstants;
import anagraficaCentrale.client.gui.OperationPanel;
import anagraficaCentrale.client.gui.component.AcCheckBoxGroup;
import anagraficaCentrale.client.gui.component.AcServiceButton;
import anagraficaCentrale.client.gui.component.AcTextField;
import anagraficaCentrale.exception.AcServerRuntimeException;
import anagraficaCentrale.utils.ScriptUtils;

public class UserCreationService extends GenericService {
	private AcTextField textField;
	private AcTextField textField_1;
	private AcTextField textField_2;
	private AcTextField textField_5;
	private AcTextField textField_6;
	private AcTextField textField_7;
	private AcTextField textField_8;
	private AcTextField textField_9;
	private AcTextField textField_10;
	private AcTextField textField_11;
	private AcTextField textField_12;
	private DatePicker datePicker1;
	private JComboBox<String> genderList;
	private AcCheckBoxGroup authGroup;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UserCreationService(OperationPanel op) {
		super(op);
		setTitle(GUIConstants.LANG.lblUserCreationSrvTitle);
	}

	@Override
	protected JPanel generateInnerPanel() {
		JPanel attrPanel = new JPanel();
		//this.removeAll();
		//attrPanel.setLayout(new GridLayout(0, 2, 20, 20));
		attrPanel.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.NORTH;
        //gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.insets = new Insets(5, 15, 5, 15);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;

		JLabel lblId = new JLabel(GUIConstants.LANG.lbluserCreationUsername + "*");
		attrPanel.add(lblId, gbc);

		textField = new AcTextField(true);
		gbc.gridx = 1;
		attrPanel.add(textField, gbc);
		textField.setMaximumCharacterSize(20);

		JLabel lblFirstName = new JLabel(GUIConstants.LANG.lbluserCreationFirstName + "*");
		gbc.gridy++;
		gbc.gridx = 0;
		attrPanel.add(lblFirstName, gbc);

		textField_1 = new AcTextField(true);
		gbc.gridx = 1;
		attrPanel.add(textField_1, gbc);
		textField_1.setMaximumCharacterSize(50);

		JLabel lblSurname = new JLabel(GUIConstants.LANG.lbluserCreationSurname + "*");
		gbc.gridy++;
		gbc.gridx = 0;
		attrPanel.add(lblSurname, gbc);

		textField_2 = new AcTextField(true);
		gbc.gridx = 1;
		attrPanel.add(textField_2, gbc);
		textField_2.setMaximumCharacterSize(40);

		JLabel lblBirthDate = new JLabel(GUIConstants.LANG.lbluserCreationBirthDate + "*");
		gbc.gridy++;
		gbc.gridx = 0;
		attrPanel.add(lblBirthDate, gbc);

		// Create a date picker with some custom settings.
		DatePickerSettings dateSettings = new DatePickerSettings();
	    dateSettings.setFirstDayOfWeek(DayOfWeek.MONDAY);
	    dateSettings.setLocale(Locale.ITALIAN);
	    dateSettings.setAllowEmptyDates(false);
	    dateSettings.setColor(DateArea.CalendarBackgroundSelectedDate, GUIConstants.BACKGROUND_COLOR_1);
	    dateSettings.setFormatForDatesCommonEra("dd/MM/yyyy");
		datePicker1 = new DatePicker(dateSettings);
	    final LocalDate today = LocalDate.now();
	    dateSettings.setDateRangeLimits(LocalDate.MIN, today);

		gbc.gridx = 1;
		attrPanel.add(datePicker1, gbc);
		
		gbc.gridy++;
		attrPanel.add(new JLabel(), gbc);//empty space

		JLabel lblGender = new JLabel(GUIConstants.LANG.lbluserCreationGender + "*");
		gbc.gridy++;
		gbc.gridx = 0;
		attrPanel.add(lblGender, gbc);

		genderList = new JComboBox<>(new String[]{"M", "F"});
		//genderList.setSelectedIndex(0);
		genderList.setBackground(GUIConstants.OPERATION_PANEL_BACKGROUND);
		gbc.gridx = 1;
		attrPanel.add(genderList, gbc);
		
		gbc.gridy++;
		attrPanel.add(new JLabel(), gbc);//empty space

		JLabel lblTaxIdCode = new JLabel(GUIConstants.LANG.lbluserCreationTaxIDCode + "*");
		gbc.gridy++;
		gbc.gridx = 0;
		attrPanel.add(lblTaxIdCode, gbc);

		textField_5 = new AcTextField(true);
		gbc.gridx = 1;
		attrPanel.add(textField_5, gbc);
		textField_5.setMaximumCharacterSize(16);
		
		JLabel lblBirthTown = new JLabel(GUIConstants.LANG.lbluserCreationBirthTown + "*");
		gbc.gridy++;
		gbc.gridx = 0;
		attrPanel.add(lblBirthTown, gbc);
		
		textField_6 = new AcTextField(true);
		gbc.gridx = 1;
		attrPanel.add(textField_6, gbc);
		
		JLabel lblBirthState = new JLabel(GUIConstants.LANG.lbluserCreationBirthState + "*");
		gbc.gridy++;
		gbc.gridx = 0;
		attrPanel.add(lblBirthState, gbc);
		
		textField_7 = new AcTextField(true);
		gbc.gridx = 1;
		attrPanel.add(textField_7, gbc);
		
		JLabel lblAddress = new JLabel(GUIConstants.LANG.lbluserCreationAddress + "*");
		gbc.gridy++;
		gbc.gridx = 0;
		attrPanel.add(lblAddress, gbc);
		
		textField_8 = new AcTextField(true);
		gbc.gridx = 1;
		attrPanel.add(textField_8, gbc);
		
		JLabel lblTown = new JLabel(GUIConstants.LANG.lbluserCreationTown + "*");
		gbc.gridy++;
		gbc.gridx = 0;
		attrPanel.add(lblTown, gbc);
		
		textField_9 = new AcTextField(true);
		gbc.gridx = 1;
		attrPanel.add(textField_9, gbc);
		
		JLabel lblProvince = new JLabel(GUIConstants.LANG.lbluserCreationProvince + "*");
		gbc.gridy++;
		gbc.gridx = 0;
		attrPanel.add(lblProvince, gbc);
		
		textField_10 = new AcTextField(true);
		gbc.gridx = 1;
		attrPanel.add(textField_10, gbc);
		textField_10.setMaximumCharacterSize(2);
		
		JLabel lblState = new JLabel(GUIConstants.LANG.lbluserCreationState + "*");
		gbc.gridy++;
		gbc.gridx = 0;
		attrPanel.add(lblState, gbc);
		
		textField_11 = new AcTextField(true);
		gbc.gridx = 1;
		attrPanel.add(textField_11, gbc);
		
		JLabel lblZipCode = new JLabel(GUIConstants.LANG.lbluserCreationZipCode + "*");
		gbc.gridy++;
		gbc.gridx = 0;
		attrPanel.add(lblZipCode, gbc);
		
		textField_12 = new AcTextField(true);
		gbc.gridx = 1;
		attrPanel.add(textField_12, gbc);
		textField_12.setMaximumCharacterSize(5);
		textField_12.setDigitOnly(true);
		
		gbc.gridy++;
		gbc.gridx = 0;
		attrPanel.add(new JLabel(GUIConstants.LANG.lbluserCreationAuthorization + "*") , gbc);
		gbc.gridx = 1;
		authGroup = new AcCheckBoxGroup(new String[]{GUIConstants.LANG.lblComuneTitle, GUIConstants.LANG.lblOspedaleTitle, GUIConstants.LANG.lblScuolaTitle}) ;
		attrPanel.add(authGroup, gbc);

		AcServiceButton createUserButton = new AcServiceButton(GUIConstants.LANG.lbluserCreationCreateUserBtn);
		createUserButton.setBorder(new MatteBorder(2, 3, 2, 3, operationPanel.guiBackgroundColor));
		createUserButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				if(!isFormValid())
					return;
				
				int choice = JOptionPane.showConfirmDialog(UserCreationService.this, GUIConstants.LANG.userCreateConfirmMsg,  GUIConstants.LANG.userCreateConfirmTitle, JOptionPane.WARNING_MESSAGE);
				if(choice != JOptionPane.OK_OPTION)
					return;
				
				String hashedPassword = null;
				try {
					hashedPassword = ConnectionManager.hash(textField.getText().toCharArray());
				} catch (NoSuchAlgorithmException e1) {
					operationPanel.popupError(e1);
					return;
				}
				
				List<String[]> userProps = new ArrayList<>();
				userProps.add(new String[]{"id", textField.getText()});
				userProps.add(new String[]{"password", hashedPassword});
				userProps.add(new String[]{"first_name", textField_1.getText()});
				userProps.add(new String[]{"surname", textField_2.getText()});
				String sqlDateFormated = datePicker1.getDate().format(DateTimeFormatter.ISO_DATE);
				userProps.add(new String[]{"birthdate", sqlDateFormated});
				userProps.add(new String[]{"gender", (String) genderList.getSelectedItem()});
				userProps.add(new String[]{"tax_id_code", textField_5.getText()});
				userProps.add(new String[]{"birth_town", textField_6.getText()});
				userProps.add(new String[]{"birth_state", textField_7.getText()});
				userProps.add(new String[]{"address", textField_8.getText()});
				userProps.add(new String[]{"town", textField_9.getText()});
				userProps.add(new String[]{"province", textField_10.getText()});
				userProps.add(new String[]{"state", textField_11.getText()});
				userProps.add(new String[]{"zip_code", textField_12.getText()});
				userProps.add(new String[]{"authorization", authGroup.getCheckBoxString()});

				try{
					operationPanel.getConnectionManager().createUser(userProps);
				}catch(AcServerRuntimeException e){
					operationPanel.popupError(e);
					return;
				}
				operationPanel.popupInfo(GUIConstants.LANG.msgUserCreateSuccess);
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
		textField.setText("");
		textField_1.setText("");
		textField_2.setText("");
		textField_5.setText("");
		textField_6.setText("");
		textField_7.setText("");
		textField_8.setText("");
		textField_9.setText("");
		textField_10.setText("");
		textField_11.setText("");
		textField_12.setText("");
		datePicker1.setDateToToday();
		genderList.setSelectedIndex(0);
		authGroup.clear();
	}

	protected boolean isFormValid() {
		//validation
		boolean formIncomplete = false;
		if(textField.fieldValidation()){
			formIncomplete = true;
		}
		
		if(textField_1.fieldValidation()){
			formIncomplete = true;
		}
		
		if(textField_2.fieldValidation()){
			formIncomplete = true;
		}
		
		if(datePicker1.getText() == null || datePicker1.getText().equals("") || !datePicker1.isTextFieldValid()){
			formIncomplete = true;
		}
		
		if(textField_5.fieldValidation()){
			formIncomplete = true;
		} else {
			//CF validation
			textField_5.setText(textField_5.getText().toUpperCase());
			if(!ScriptUtils.isValidTaxId(textField_5.getText())){
				formIncomplete = true;
				textField_5.setError(GUIConstants.LANG.lblTaxIdWrongSize);
			}
			
		}
		
		if(textField_6.fieldValidation()){
			formIncomplete = true;
		}
		
		if(textField_7.fieldValidation()){
			formIncomplete = true;
		}
		
		if(textField_8.fieldValidation()){
			formIncomplete = true;
		}
		
		if(textField_9.fieldValidation()){
			formIncomplete = true;
		}
		
		if(textField_10.fieldValidation()){
			formIncomplete = true;
		}
		
		if(textField_11.fieldValidation()){
			formIncomplete = true;
		}
		
		if(textField_12.fieldValidation()){
			formIncomplete = true;
		}
		return !formIncomplete;
	}

}
