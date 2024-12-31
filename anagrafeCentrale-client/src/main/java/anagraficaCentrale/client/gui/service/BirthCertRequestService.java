package anagraficaCentrale.client.gui.service;

import java.util.Map;

import anagraficaCentrale.client.exception.UserNotFoundException;
import anagraficaCentrale.client.gui.GUIConstants;
import anagraficaCentrale.client.gui.OperationPanel;
import anagraficaCentrale.utils.ClientServerConstants.ServiceType;

/**
 * this class represents the birth certificate request service. It specializes the abstract certificate request service
 * @author Adriana Brosio
 */
public class BirthCertRequestService extends CertificateRequestService {

	private static final long serialVersionUID = 1L;

	public BirthCertRequestService(OperationPanel op) {
		super(op);
		setTitle(GUIConstants.LANG.lbl_CERT_NASC_SrvTitle);
	}

	@Override
	protected String getTextAreaContent() {
		operationPanel.getConnectionManager().refreshUserData();
		String text = GUIConstants.LANG.lbl_CERT_NASC_SrvText;
		String[] prorToReplace = new String[]{"first_name" , "surname", "birth_town", "birth_province", "birthdate", "tax_id_code", "address", "town", "province", "zip_code"};
		for(String attrName : prorToReplace)
			text = text.replaceAll("!"+attrName, operationPanel.getConnectionManager().getUserAttribute(attrName));
		return text;
	}

	@Override
	protected ServiceType getServiceType() {
		return ServiceType.CERT_NASC;
	}

	@Override
	protected String getFileDisplayName() {
		return GUIConstants.LANG.lbl_CERT_NASC_ReportDisplayName;
	}

	@Override
	protected String getFileTitle() {
		return GUIConstants.LANG.lbl_CERT_NASC_ReportTitle;
	}

	@Override
	protected String getFileContent() {
		String fileContent = GUIConstants.LANG.lbl_CERT_NASC_ReportContent;
		Map<String,String> userProps = null;
		try {
			userProps = operationPanel.getConnectionManager().getUserData(operationPanel.getConnectionManager().getUserAttribute("id"));
		} catch (UserNotFoundException e) {
			operationPanel.popupError(e);
			return "ERROR";
		}
		for(String attrName : userProps.keySet())
			fileContent = fileContent.replaceAll("!"+attrName, userProps.get(attrName));
		return fileContent;
	}

}
