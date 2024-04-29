package anagraficaCentrale.client.gui.service;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.border.MatteBorder;

import anagraficaCentrale.client.gui.GUIConstants;
import anagraficaCentrale.client.gui.OperationPanel;
import anagraficaCentrale.client.gui.component.AcServiceButton;
import anagraficaCentrale.utils.ClientServerConstants.ServiceType;

public abstract class SimpleRequestService extends GenericService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JEditorPane textField;

	public SimpleRequestService(OperationPanel op) {
		super(op);
	}

	@Override
	protected JPanel generateInnerPanel() {
		JPanel innerPanel = new JPanel(new BorderLayout());


		textField = new JEditorPane();
		textField.setContentType("text/html");
		textField.setText(getTextAreaContent());
		textField.setEditable(false);
		innerPanel.add(textField, BorderLayout.CENTER);

		AcServiceButton createUserButton = new AcServiceButton(GUIConstants.LANG.lblSimpleRequestCreateBtn);
		createUserButton.setBorder(new MatteBorder(2, 3, 2, 3, operationPanel.guiBackgroundColor));
		createUserButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {

				List<String[]> userProps = new ArrayList<>();

				userProps.add(new String[]{"portal_type", ""+operationPanel.getPortalType().getValue()});
				userProps.add(new String[]{"creator_user_id", operationPanel.getConnectionManager().getUserAttribute("id")});
				userProps.add(new String[]{"request_type", ""+getServiceType()});

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

	protected abstract String getTextAreaContent();

	protected abstract ServiceType getServiceType();

}
