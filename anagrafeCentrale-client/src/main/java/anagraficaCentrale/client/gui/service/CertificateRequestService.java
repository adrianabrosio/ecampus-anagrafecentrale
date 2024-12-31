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

/**
 * this class represent the service of the certificate request
 * @author Adriana Brosio
 */
public abstract class CertificateRequestService extends GenericService {

	private static final long serialVersionUID = 1L;
	private JEditorPane textField;

	public CertificateRequestService(OperationPanel op) {
		super(op, null);
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

				userProps.add(new String[]{"user_id", operationPanel.getConnectionManager().getUserAttribute("id")});
				userProps.add(new String[]{"portal_type", ""+operationPanel.getPortalType().getValue()});
				//userProps.add(new String[]{"request_type", ""+getServiceType()});
				userProps.add(new String[]{"file_display_name", ""+getFileDisplayName()});
				userProps.add(new String[]{"file_title", ""+getFileTitle()});
				userProps.add(new String[]{"file_content", ""+getFileContent()});

				try{
					operationPanel.getConnectionManager().createCertificateRequest(getServiceType(), userProps);
				}catch(Exception e){
					operationPanel.popupError(e);
					return;
				}
				operationPanel.popupInfo(GUIConstants.LANG.msgCertificateRequestSuccess);
			}

		});

		innerPanel.add(createUserButton, BorderLayout.AFTER_LAST_LINE);
		return innerPanel;
	}

	protected abstract String getTextAreaContent();

	protected abstract ServiceType getServiceType();
		
	protected abstract String getFileDisplayName();
	
	protected abstract String getFileTitle();
	
	protected abstract String getFileContent();

}
