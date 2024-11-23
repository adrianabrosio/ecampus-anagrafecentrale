package anagraficaCentrale.client.gui.service;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
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
import anagraficaCentrale.client.gui.component.AcTextField;
import anagraficaCentrale.client.gui.resource.FakeBankTransactionPanel;
import anagraficaCentrale.utils.ClientServerConstants.ServiceType;

public class CanteenPaymentService extends GenericService {
	private JEditorPane textField;
	private AcTextField firstNameText;
	private AcTextField surnameText;
	private JComboBox<String> monthText, userList;
	private AcTextField feeText;
	private boolean panelIsInvalid;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CanteenPaymentService(OperationPanel op) {
		super(op, null);
		setTitle(GUIConstants.LANG.lbl_PAG_MEN_SrvTitle);
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

		//lista persone per appuntamento
		Map<String, Map<String,String>> usersList = new HashMap<>();
		usersList.putAll(operationPanel.getConnectionManager().getRelationsDataByTaxIdCode());

		gbc.gridheight = 1;
		gbc.gridwidth = 1;
		gbc.weightx = 1;
		gbc.weighty = 1;
		JLabel lblFirstName = new JLabel(GUIConstants.LANG.lbluserCreationFirstName + "*");
		gbc.gridy++;
		gbc.gridx = 0;
		attrPanel.add(lblFirstName, gbc);

		firstNameText = new AcTextField(true);
		firstNameText.setEditable(false);
		firstNameText.setText(operationPanel.getConnectionManager().getUserAttribute("first_name"));
		gbc.gridx = 1;
		attrPanel.add(firstNameText, gbc);

		JLabel lblSurname = new JLabel(GUIConstants.LANG.lbluserCreationSurname + "*");
		gbc.gridy++;
		gbc.gridx = 0;
		attrPanel.add(lblSurname, gbc);

		surnameText = new AcTextField(true);
		surnameText.setEditable(false);
		surnameText.setText(operationPanel.getConnectionManager().getUserAttribute("surname"));
		gbc.gridx = 1;
		attrPanel.add(surnameText, gbc);

		JLabel lblUser = new JLabel(GUIConstants.LANG.lblTicketPaymentUser + "*");
		gbc.gridy++;
		gbc.gridx = 0;
		attrPanel.add(lblUser, gbc);

		String[] users = usersList.keySet().toArray(new String[0]);
		if(users != null && users.length > 0) {
			panelIsInvalid = false;
			userList = new JComboBox<>(users);
			userList.setBackground(GUIConstants.OPERATION_PANEL_BACKGROUND);
			userList.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					String selection = (String) userList.getSelectedItem();
					firstNameText.setText(usersList.get(selection).get("first_name"));
					surnameText.setText(usersList.get(selection).get("surname"));
				}
			});
			gbc.gridx = 1;
			attrPanel.add(userList, gbc);

			String selection = (String) userList.getSelectedItem();
			firstNameText.setText(usersList.get(selection).get("first_name"));
			surnameText.setText(usersList.get(selection).get("surname"));
		} else {
			JLabel lblUserError = new JLabel(GUIConstants.LANG.errorNoRelationFound);
			lblUserError.setForeground(Color.RED);
			panelIsInvalid = true;
			gbc.gridx = 1;
			attrPanel.add(lblUserError, gbc);
		}

		JLabel lblTicketNumber = new JLabel(GUIConstants.LANG.lblMonth + "*");
		gbc.gridy++;
		gbc.gridx = 0;
		attrPanel.add(lblTicketNumber, gbc);

		monthText = new JComboBox<>(GUIConstants.LANG.monthValues);
		gbc.gridx = 1;
		attrPanel.add(monthText, gbc);

		JLabel lblSchoolFee = new JLabel(GUIConstants.LANG.lblSchoolFee + "*");
		gbc.gridy++;
		gbc.gridx = 0;
		attrPanel.add(lblSchoolFee, gbc);

		feeText = new AcTextField(true);
		gbc.gridx = 1;
		attrPanel.add(feeText, gbc);
		feeText.setMaximumCharacterSize(16);
		feeText.setDigitOnly(true);

		AcServiceButton createUserButton = new AcServiceButton(GUIConstants.LANG.lblPayBtn);
		createUserButton.setBorder(new MatteBorder(2, 3, 2, 3, operationPanel.guiBackgroundColor));
		createUserButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {

				if(!isFormValid())
					return;

				List<String[]> userProps = new ArrayList<>();

				userProps.add(new String[]{"portal_type", ""+operationPanel.getPortalType().getValue()});
				userProps.add(new String[]{"creator_user_id", operationPanel.getConnectionManager().getUserAttribute("id")});
				userProps.add(new String[]{"month", (String) monthText.getSelectedItem()});
				userProps.add(new String[]{"fee", feeText.getText()});
				userProps.add(new String[]{"request_type", ""+ServiceType.PAG_MEN});


				try{
					new FakeBankTransactionPanel() {

						@Override
						public void callback() {
							operationPanel.getConnectionManager().createPaymentRequest(ServiceType.PAG_MEN, userProps);
							operationPanel.popupInfo(GUIConstants.LANG.msgTicketPaymentSuccess);
							clearForm();
						}

					};

				}catch(Exception e){
					operationPanel.popupError(e);
					return;
				}
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
		return GUIConstants.LANG.lbl_PAG_MEN_SrvText;
	}

	protected void clearForm() {
		monthText.setSelectedIndex(1);
		feeText.setText("");
	}

	protected boolean isFormValid() {
		//validation
		boolean formIncomplete = false;

		if(!feeText.fieldIsValid()){
			formIncomplete = true;
		}

		if(panelIsInvalid){
			operationPanel.popupInfo(GUIConstants.LANG.errorNoRelationFound);
			formIncomplete = true;
		}

		return !formIncomplete;
	}

}
