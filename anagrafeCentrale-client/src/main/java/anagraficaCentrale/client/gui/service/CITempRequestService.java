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

public class CITempRequestService extends GenericService {
	private JEditorPane textField;
	private AcTextField reasonText;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CITempRequestService(OperationPanel op) {
		super(op);
		setTitle(GUIConstants.LANG.lbl_CI_TEMP_SrvTitle);
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
		JLabel lblReason = new JLabel(GUIConstants.LANG.lblCI_TEMP_reason + "*");
		gbc.gridy++;
		gbc.gridx = 0;
		attrPanel.add(lblReason, gbc);
		
		reasonText = new AcTextField(true);
		gbc.gridx = 1;
		attrPanel.add(reasonText, gbc);

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
				userProps.add(new String[]{"reason", reasonText.getText()});
				userProps.add(new String[]{"request_type", ""+ServiceType.CI_TEMP});

				try{
					operationPanel.getConnectionManager().createResidenceChangeRequest(ServiceType.CI_TEMP, userProps);
				}catch(Exception e){
					operationPanel.popupError(e);
					return;
				}
				operationPanel.popupInfo(GUIConstants.LANG.msgCITempSuccess);
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
		String text = GUIConstants.LANG.lbl_CI_TEMP_SrvText;
		String[] prorToReplace = new String[]{"first_name" , "surname", "birth_town", "birth_province", "birthdate", "tax_id_code", "address", "town", "province", "zip_code"};
		for(String attrName : prorToReplace)
			text = text.replaceAll("!"+attrName, operationPanel.getConnectionManager().getUserAttribute(attrName));
		return text;
	}

	protected void clearForm() {
		reasonText.setText("");
	}

	protected boolean isFormValid() {
		//validation
		boolean formIncomplete = false;

		if(!reasonText.fieldIsValid()){
			formIncomplete = true;
		}
		
		return !formIncomplete;
	}

}
