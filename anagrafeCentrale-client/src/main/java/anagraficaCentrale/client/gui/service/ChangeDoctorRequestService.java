package anagraficaCentrale.client.gui.service;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.MatteBorder;

import anagraficaCentrale.client.gui.GUIConstants;
import anagraficaCentrale.client.gui.OperationPanel;
import anagraficaCentrale.client.gui.component.AcServiceButton;
import anagraficaCentrale.client.gui.component.AcTextField;
import anagraficaCentrale.utils.ClientServerConstants.ServiceType;

public class ChangeDoctorRequestService extends GenericService {
	private JEditorPane textField;
	private AcTextField textFirstName;
	private AcTextField textSurname;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ChangeDoctorRequestService(OperationPanel op) {
		super(op, null);
		setTitle(GUIConstants.LANG.lbl_CAM_MED_SrvTitle);
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

		textField = new JEditorPane();
		textField.setContentType("text/html");
		textField.setText(getTextAreaContent());
		textField.setEditable(false);
		attrPanel.add(textField, gbc);


		gbc.gridheight = 1;
		gbc.gridwidth = 1;
		gbc.weightx = 1;
		gbc.weighty = 1;
		JLabel lblFirstName = new JLabel(GUIConstants.LANG.lbluserCreationFirstName + "*");
		gbc.gridy++;
		gbc.gridx = 0;
		attrPanel.add(lblFirstName, gbc);
		
		textFirstName = new AcTextField(true);
		gbc.gridx = 1;
		attrPanel.add(textFirstName, gbc);
		
		JLabel lblSurname = new JLabel(GUIConstants.LANG.lbluserCreationSurname + "*");
		gbc.gridy++;
		gbc.gridx = 0;
		attrPanel.add(lblSurname, gbc);
		
		textSurname = new AcTextField(true);
		gbc.gridx = 1;
		attrPanel.add(textSurname, gbc);

		AcServiceButton createUserButton = new AcServiceButton(GUIConstants.LANG.lblSimpleRequestCreateBtn);
		createUserButton.setBorder(new MatteBorder(2, 3, 2, 3, operationPanel.guiBackgroundColor));
		createUserButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {

				if(!isFormValid())
					return;

				List<String[]> userProps = new ArrayList<>();

				userProps.add(new String[]{"portal_type", ""+operationPanel.getPortalType().getValue()});
				userProps.add(new String[]{"creator_user_id", operationPanel.getConnectionManager().getUserAttribute("id")});
				userProps.add(new String[]{"medicFirstName", textFirstName.getText()});
				userProps.add(new String[]{"medicSurname", textSurname.getText()});
				userProps.add(new String[]{"request_type", ""+ServiceType.CAM_MED});

				try{
					operationPanel.getConnectionManager().createDoctorChangeRequest(ServiceType.CAM_MED, userProps);
				}catch(Exception e){
					operationPanel.popupError(e);
					return; 
				}
				operationPanel.popupInfo(GUIConstants.LANG.msgCAM_MEDSuccess);
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

	private String getTextAreaContent() {
		operationPanel.getConnectionManager().refreshUserData();
		String text = GUIConstants.LANG.lbl_CAM_MED_SrvText;
		String[] prorToReplace = new String[]{"first_name" , "surname", "birth_town", "birth_province", "birthdate", "tax_id_code", "address", "town", "province", "zip_code"};
		for(String attrName : prorToReplace)
			text = text.replaceAll("!"+attrName, operationPanel.getConnectionManager().getUserAttribute(attrName));
		return text;
	}

	protected void clearForm() {
		textFirstName.setText("");
		textSurname.setText("");
	}

	protected boolean isFormValid() {
		//validation
		boolean formIncomplete = false;

		if(!textFirstName.fieldIsValid()){
			formIncomplete = true;
		}
		
		if(!textSurname.fieldIsValid()){
			formIncomplete = true;
		}
		
		return !formIncomplete;
	}

}
