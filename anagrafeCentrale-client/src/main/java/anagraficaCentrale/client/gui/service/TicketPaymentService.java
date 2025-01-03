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
import anagraficaCentrale.client.gui.resource.FakeBankTransactionPanel;
import anagraficaCentrale.utils.ClientServerConstants.ServiceType;

/**
 * this class represent the service of the ticket payment.
 *
 * @author Adriana Brosio
 */
public class TicketPaymentService extends GenericService {
	private JEditorPane textField;
	private AcTextField firstNameText;
	private AcTextField surnameText;
	private AcTextField ticketNumberText;
	private AcTextField cardNumberText;

	private static final long serialVersionUID = 1L;

	public TicketPaymentService(OperationPanel op) {
		super(op, null);
		setTitle(GUIConstants.LANG.lbl_PAG_TICK_SrvTitle);
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

		operationPanel.getConnectionManager().refreshUserData();

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
		
		JLabel lblTicketNumber = new JLabel(GUIConstants.LANG.lblTicketPaymentNumber + "*");
		gbc.gridy++;
		gbc.gridx = 0;
		attrPanel.add(lblTicketNumber, gbc);
		
		ticketNumberText = new AcTextField(true);
		gbc.gridx = 1;
		attrPanel.add(ticketNumberText, gbc);
		ticketNumberText.setMaximumCharacterSize(15);
		ticketNumberText.setDigitOnly(true);
		
		JLabel lblCardNumber = new JLabel(GUIConstants.LANG.lblTicketPaymentCard + "*");
		gbc.gridy++;
		gbc.gridx = 0;
		attrPanel.add(lblCardNumber, gbc);
		
		cardNumberText = new AcTextField(true);
		gbc.gridx = 1;
		attrPanel.add(cardNumberText, gbc);
		cardNumberText.setMaximumCharacterSize(16);
		cardNumberText.setDigitOnly(true);

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
				userProps.add(new String[]{"ticketNumber", ticketNumberText.getText()});
				userProps.add(new String[]{"request_type", ""+ServiceType.PAG_TICK});

				try{
					new FakeBankTransactionPanel() {

						@Override
						public void callback() {
							operationPanel.getConnectionManager().createPaymentRequest(ServiceType.PAG_TICK, userProps);
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

		innerPanel.add(lowerPanel, BorderLayout.AFTER_LAST_LINE);
		return innerPanel;
	}

	private String getTextAreaContent() {
		return GUIConstants.LANG.lbl_PAG_TICK_SrvText;
	}

	protected void clearForm() {
		ticketNumberText.setText("");
		cardNumberText.setText("");
	}

	protected boolean isFormValid() {
		//validation
		boolean formIncomplete = false;

		if(!ticketNumberText.fieldIsValid()){
			formIncomplete = true;
		}
		
		if(!cardNumberText.fieldIsValid()){
			formIncomplete = true;
		}else if(cardNumberText.getText().length()<15){
			cardNumberText.setError(GUIConstants.LANG.errInvalidCardNumber);
			formIncomplete = true;
		}
		
		return !formIncomplete;
	}

}
