package anagraficaCentrale.client.gui.service;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JComboBox;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.MatteBorder;

import anagraficaCentrale.client.gui.GUIConstants;
import anagraficaCentrale.client.gui.OperationPanel;
import anagraficaCentrale.client.gui.component.AcServiceButton;
import anagraficaCentrale.utils.ClientServerConstants.ServiceType;

public class SchoolRegistrationService extends GenericService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JEditorPane textField;
	private Map<String, Map<String,String>> usersList;
	private JComboBox<String> userList;
	private boolean panelIsInvalid;

	public SchoolRegistrationService(OperationPanel op) {
		super(op, null);
		setTitle(GUIConstants.LANG.lbl_ISCRIZ_SrvTitle);
	}

	@Override
	protected JPanel generateInnerPanel() {
		JPanel innerPanel = new JPanel(new BorderLayout());

		operationPanel.getConnectionManager().refreshUserData();
		usersList = new HashMap<>();
		usersList.putAll(operationPanel.getConnectionManager().getRelationsDataByTaxIdCode());

		String[] users = usersList.keySet().toArray(new String[0]);
		if(users != null && users.length > 0) {
			userList = new JComboBox<>(users);
			userList.setBackground(GUIConstants.OPERATION_PANEL_BACKGROUND);
			userList.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					String selection = (String) userList.getSelectedItem();
					textField.setText(getTextAreaContent(selection));
				}
			});
			innerPanel.add(userList, BorderLayout.NORTH);


			String selection = (String) userList.getSelectedItem();

			textField = new JEditorPane(); 
			/*{
			@Override
			public Dimension getPreferredSize() {
				return operationPanel.getRightPanelSize();
			}
		};*/
			textField.setContentType("text/html");
			textField.setText(getTextAreaContent(selection));
			textField.setEditable(false);
			innerPanel.add(textField, BorderLayout.CENTER);
			panelIsInvalid = false;
		} else {
			JLabel lblUserError = new JLabel(GUIConstants.LANG.errorNoRelationFound);
			lblUserError.setForeground(Color.RED);
			panelIsInvalid = true;
			innerPanel.add(lblUserError, BorderLayout.CENTER);
		}

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
				userProps.add(new String[]{"request_type", ""+getServiceType()});
				userProps.add(new String[]{"tax_id_code", (String) userList.getSelectedItem()});

				try{
					operationPanel.getConnectionManager().createSimpleRequest(getServiceType(), userProps);
				}catch(Exception e){
					operationPanel.popupError(e);
					return;
				}
				operationPanel.popupInfo(GUIConstants.LANG.msgSimpleRequestSuccess);
			}

		});

		innerPanel.add(createUserButton, BorderLayout.AFTER_LAST_LINE);
		return innerPanel;
	}

	protected String getTextAreaContent(String selection) {

		String text = GUIConstants.LANG.lbl_ISCRIZ_SrvText;
		String[] prorToReplace = new String[]{"first_name" , "surname", "birth_town", "birth_province", "birthdate", "tax_id_code", "address", "town", "province", "zip_code"};
		for(String attrName : prorToReplace)
			text = text.replaceAll("!"+attrName, operationPanel.getConnectionManager().getUserAttribute(attrName));

		String selectionUsername = operationPanel.getConnectionManager().getRelationUsernameFromAttribute("tax_id_code", selection);
		for(String attrName : prorToReplace)
			text = text.replaceAll("&"+attrName, operationPanel.getConnectionManager().getRelationAttribute(selectionUsername, attrName));
		return text;
	}

	protected ServiceType getServiceType() {
		return ServiceType.ISCRIZ;
	}

	protected boolean isFormValid() {
		//validation
		boolean formIncomplete = false;

		if(panelIsInvalid){
			operationPanel.popupInfo(GUIConstants.LANG.errorNoRelationFound);
			formIncomplete = true;
		}

		return !formIncomplete;
	}
}
