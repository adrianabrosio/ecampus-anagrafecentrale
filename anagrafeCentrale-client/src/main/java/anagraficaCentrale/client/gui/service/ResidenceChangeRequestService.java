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

public class ResidenceChangeRequestService extends GenericService {
	private JEditorPane textField;
	private AcTextField newAddress;
	private AcTextField newTown;
	private AcTextField newProvince;
	private AcTextField newZipCode;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ResidenceChangeRequestService(OperationPanel op) {
		super(op, null);
		setTitle(GUIConstants.LANG.lbl_CAM_RES_SrvTitle);
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
		JLabel lblAddress = new JLabel(GUIConstants.LANG.lbluserCreationAddress + "*");
		gbc.gridy++;
		gbc.gridx = 0;
		attrPanel.add(lblAddress, gbc);
		
		newAddress = new AcTextField(true);
		gbc.gridx = 1;
		attrPanel.add(newAddress, gbc);
		
		JLabel lblTown = new JLabel(GUIConstants.LANG.lbluserCreationTown + "*");
		gbc.gridy++;
		gbc.gridx = 0;
		attrPanel.add(lblTown, gbc);
		
		newTown = new AcTextField(true);
		gbc.gridx = 1;
		attrPanel.add(newTown, gbc);
		
		JLabel lblProvince = new JLabel(GUIConstants.LANG.lbluserCreationProvince + "*");
		gbc.gridy++;
		gbc.gridx = 0;
		attrPanel.add(lblProvince, gbc);
		
		newProvince = new AcTextField(true);
		gbc.gridx = 1;
		attrPanel.add(newProvince, gbc);
		newProvince.setMaximumCharacterSize(2);
		
		JLabel lblZipCode = new JLabel(GUIConstants.LANG.lbluserCreationZipCode + "*");
		gbc.gridy++;
		gbc.gridx = 0;
		attrPanel.add(lblZipCode, gbc);
		
		newZipCode = new AcTextField(true);
		gbc.gridx = 1;
		attrPanel.add(newZipCode, gbc);
		newZipCode.setMaximumCharacterSize(5);
		newZipCode.setDigitOnly(true);

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
				userProps.add(new String[]{"newAddress", newAddress.getText()});
				userProps.add(new String[]{"newTown", newTown.getText()});
				userProps.add(new String[]{"newProvince", newProvince.getText()});
				userProps.add(new String[]{"newZipCode", newZipCode.getText()});
				userProps.add(new String[]{"request_type", ""+ServiceType.CAM_RES});

				try{
					operationPanel.getConnectionManager().createResidenceChangeRequest(ServiceType.CAM_RES, userProps);
				}catch(Exception e){
					operationPanel.popupError(e);
					return;
				}
				operationPanel.popupInfo(GUIConstants.LANG.msgResidenceChangeSuccess);
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
		String text = GUIConstants.LANG.lbl_CAM_RES_SrvText;
		String[] prorToReplace = new String[]{"first_name" , "surname", "birth_town", "birth_province", "birthdate", "tax_id_code", "address", "town", "province", "zip_code"};
		for(String attrName : prorToReplace)
			text = text.replaceAll("!"+attrName, operationPanel.getConnectionManager().getUserAttribute(attrName));
		return text;
	}

	protected void clearForm() {
		newAddress.setText("");
		newTown.setText("");
		newProvince.setText("");
		newZipCode.setText("");
	}

	protected boolean isFormValid() {
		//validation
		boolean formIncomplete = false;

		if(!newAddress.fieldIsValid()){
			formIncomplete = true;
		}
		
		if(!newTown.fieldIsValid()){
			formIncomplete = true;
		}
		
		if(!newProvince.fieldIsValid()){
			formIncomplete = true;
		}
		
		if(!newZipCode.fieldIsValid()){
			formIncomplete = true;
		}
		
		return !formIncomplete;
	}

}
