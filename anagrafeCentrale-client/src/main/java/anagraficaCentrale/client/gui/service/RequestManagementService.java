package anagraficaCentrale.client.gui.service;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.border.MatteBorder;

import anagraficaCentrale.client.exception.UnsupportedServiceException;
import anagraficaCentrale.client.exception.UserNotFoundException;
import anagraficaCentrale.client.gui.GUIConstants;
import anagraficaCentrale.client.gui.OperationPanel;
import anagraficaCentrale.client.gui.component.AcServiceButton;
import anagraficaCentrale.utils.ClientServerConstants;
import anagraficaCentrale.utils.ClientServerConstants.ServiceType;
import anagraficaCentrale.utils.ScriptUtils;

/**
 * this class represent the service of the request management.
 * It is used to show both request management service for Admin and notification pane for User
 * @author Adriana Brosio
 */
public class RequestManagementService extends GenericService {

	private static final long serialVersionUID = 1L;
	private JEditorPane textField;
	private String title;

	public RequestManagementService(OperationPanel op, Map<String, String> serviceData) {
		super(op, serviceData);
		setTitle(title==null? GUIConstants.LANG.lblAdminRequestManagementSrvTitle : title);
	}

	@Override
	protected JPanel generateInnerPanel() {
		JPanel innerPanel = new JPanel(new BorderLayout());
		textField = new JEditorPane(); 
		textField.setContentType("text/html");
		textField.setText(getTextAreaContent(serviceData));
		textField.setEditable(false);
		innerPanel.add(textField, BorderLayout.CENTER);

		if(operationPanel.getConnectionManager().isAdmin()) {
			AcServiceButton acceptRequestButton = new AcServiceButton(GUIConstants.LANG.lblAcceptRequestBtn);
			acceptRequestButton.setBorder(new MatteBorder(2, 3, 2, 3, operationPanel.guiBackgroundColor));
			acceptRequestButton.addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent arg0) {
					try{
						operationPanel.getConnectionManager().manageRequest(serviceData, true);
						operationPanel.openAdminSupportPanelAction();
					}catch(Exception e){
						operationPanel.popupError(e);
						return;
					}
					operationPanel.popupInfo(GUIConstants.LANG.msgSimpleRequestSuccess);
				}

			});

			AcServiceButton rejectRequestButton = new AcServiceButton(GUIConstants.LANG.lblRejectRequestBtn);
			rejectRequestButton.setBorder(new MatteBorder(2, 3, 2, 3, operationPanel.guiBackgroundColor));
			rejectRequestButton.addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent arg0) {
					try{
						operationPanel.getConnectionManager().manageRequest(serviceData, false);
						operationPanel.openAdminSupportPanelAction();
					}catch(Exception e){
						operationPanel.popupError(e);
						return;
					}
					operationPanel.popupInfo(GUIConstants.LANG.msgSimpleRequestSuccess);
				}

			});


			JPanel lowerPanel = new JPanel();
			lowerPanel.setLayout(new GridLayout(0,1));
			lowerPanel.setBackground(GUIConstants.OPERATION_PANEL_BACKGROUND);
			lowerPanel.add(acceptRequestButton);
			lowerPanel.add(rejectRequestButton);
			//lowerPanel.add(new JLabel(""));

			innerPanel.add(lowerPanel, BorderLayout.AFTER_LAST_LINE);
		}
		return innerPanel;
	}

	protected String getTextAreaContent(Map<String, String> serviceData) {
		String text = getTextByServiceType(serviceData);
		if(text == null || text.equals("ERROR"))
			return "ERROR";
		//replace data about creator_user_id
		String requestUsername = serviceData.get("creator_user_id");
		Map<String, String> requestUserAttributes;
		try {
			requestUserAttributes = operationPanel.getConnectionManager().getUserData(requestUsername);
		} catch (UserNotFoundException e) {
			operationPanel.popupError(e);
			return "ERROR";
		}
		//String[] usrDataToReplace = new String[]{"first_name" , "surname", "birth_town", "birth_province", "birthdate", "tax_id_code", "address", "town", "province", "zip_code"};
		for(String attrName : requestUserAttributes.keySet().toArray(new String[0]))
			text = text.replaceAll("!"+attrName, requestUserAttributes.get(attrName));

		//replace data about request
		text = text.replaceAll("&requestType", serviceData.getOrDefault("request_type", ""));

		//replace data about request params
		Map<String, String> requestParamsAttributes = ScriptUtils.convertParamStringToMap(serviceData.get("request_parameters"), ClientServerConstants.PARAM_SEPARATOR);
		
		String status = requestParamsAttributes.getOrDefault("status", "");
		if(status.equalsIgnoreCase("true"))
			status = GUIConstants.LANG.lblRequestStatusAccepted;
		if(status.equalsIgnoreCase("false"))
			status = GUIConstants.LANG.lblRequestStatusRejected;
		if(status.equals("") && operationPanel.getConnectionManager().isAdmin())
			status = GUIConstants.LANG.lblRequestStatusToBeManaged;
		text = text.replaceAll("&status", status);
		
		for(String attrName : requestParamsAttributes.keySet().toArray(new String[0]))
			text = text.replaceAll("&"+attrName, requestParamsAttributes.get(attrName));
		return text;
	}

	protected String getTextByServiceType(Map<String, String> serviceData) {
		String requestType = serviceData.get("request_type");
		if(requestType == null || requestType.isEmpty()) {
			operationPanel.popupError(new UnsupportedServiceException(requestType));
			return "ERROR";
		}
		switch(ServiceType.valueOf(requestType)) {
		case ADM_CREAZ_USR:
			return ""; //do not create requests
		case ADM_MOD_USR:
			return ""; //do not create requests
		case APP_CI:
			title = GUIConstants.LANG.lblAdminRequestManagementSrvTitle + " - " + GUIConstants.LANG.lbl_APP_CI_SrvTitle;
			return GUIConstants.LANG.lbl_APP_CI_SrvAdminRequestText;
		case CAM_MED:
			title = GUIConstants.LANG.lblAdminRequestManagementSrvTitle + " - " + GUIConstants.LANG.lbl_CAM_MED_SrvTitle;
			return GUIConstants.LANG.lbl_CAM_MED_SrvAdminRequestText;
		case CAM_RES:
			title = GUIConstants.LANG.lblAdminRequestManagementSrvTitle + " - " + GUIConstants.LANG.lbl_CAM_RES_SrvTitle;
			return GUIConstants.LANG.lbl_CAM_RES_SrvAdminRequestText;
		case CERT_MATR:
			title = GUIConstants.LANG.lblAdminRequestManagementSrvTitle + " - " + GUIConstants.LANG.lbl_CERT_MATR_SrvTitle;
			return GUIConstants.LANG.lbl_CERT_MATR_SrvAdminRequestText;
		case CERT_NASC:
			title = GUIConstants.LANG.lblAdminRequestManagementSrvTitle + " - " + GUIConstants.LANG.lbl_CERT_NASC_SrvTitle;
			return GUIConstants.LANG.lbl_CERT_NASC_SrvAdminRequestText;
		case CI_TEMP:
			title = GUIConstants.LANG.lblAdminRequestManagementSrvTitle + " - " + GUIConstants.LANG.lbl_CI_TEMP_SrvTitle;
			return GUIConstants.LANG.lbl_CI_TEMP_SrvAdminRequestText;
		case COLL_INS:
			title = GUIConstants.LANG.lblAdminRequestManagementSrvTitle + " - " + GUIConstants.LANG.lbl_COLL_INS_SrvTitle;
			return GUIConstants.LANG.lbl_COLL_INS_SrvAdminRequestText;
		case DUMMY:
			return ""; //do not create requests
		case ISCRIZ:
			title = GUIConstants.LANG.lblAdminRequestManagementSrvTitle + " - " + GUIConstants.LANG.lbl_ISCRIZ_SrvTitle;
			return GUIConstants.LANG.lbl_ISCRIZ_SrvAdminRequestText;
		case PAG_MEN:
			title = GUIConstants.LANG.lblAdminRequestManagementSrvTitle + " - " + GUIConstants.LANG.lbl_PAG_MEN_SrvTitle;
			return GUIConstants.LANG.lbl_PAG_MEN_SrvAdminRequestText;
		case PAG_RET:
			title = GUIConstants.LANG.lblAdminRequestManagementSrvTitle + " - " + GUIConstants.LANG.lbl_PAG_RET_SrvTitle;
			return GUIConstants.LANG.lbl_PAG_RET_SrvAdminRequestText;
		case PAG_TICK:
			title = GUIConstants.LANG.lblAdminRequestManagementSrvTitle + " - " + GUIConstants.LANG.lbl_PAG_TICK_SrvTitle;
			return GUIConstants.LANG.lbl_PAG_TICK_SrvAdminRequestText;
		case PREN_VIS:
			title = GUIConstants.LANG.lblAdminRequestManagementSrvTitle + " - " + GUIConstants.LANG.lbl_PREN_VIS_SrvTitle;
			return GUIConstants.LANG.lbl_PREN_VIS_SrvAdminRequestText;
		case STAT_FAM:
			title = GUIConstants.LANG.lblAdminRequestManagementSrvTitle + " - " + GUIConstants.LANG.lbl_STAT_FAM_SrvTitle;
			return GUIConstants.LANG.lbl_STAT_FAM_SrvAdminRequestText;
		default:
			break;
		}

		return "";
	}

}
