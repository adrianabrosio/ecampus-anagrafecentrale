package anagraficaCentrale.client.gui.service;

import java.util.Map;

import anagraficaCentrale.client.exception.UserNotFoundException;
import anagraficaCentrale.client.gui.GUIConstants;
import anagraficaCentrale.client.gui.OperationPanel;
import anagraficaCentrale.utils.ClientServerConstants.ServiceType;

/**
 * this class represent the family status request service. It specializes the abstract certificate request service
 * @author Adriana Brosio
 */
public class FamilyStatusRequestService extends CertificateRequestService {

	private static final long serialVersionUID = 1L;

	public FamilyStatusRequestService(OperationPanel op) {
		super(op);
		setTitle(GUIConstants.LANG.lbl_STAT_FAM_SrvTitle);
	}

	@Override
	protected String getTextAreaContent() {
		operationPanel.getConnectionManager().refreshUserData();
		String text = GUIConstants.LANG.lbl_STAT_FAM_SrvText;
		String[] prorToReplace = new String[]{"first_name" , "surname", "birth_town", "birth_province", "birthdate", "tax_id_code", "address", "town", "province", "zip_code"};
		for(String attrName : prorToReplace)
			text = text.replaceAll("!"+attrName, operationPanel.getConnectionManager().getUserAttribute(attrName));
		return text;
	}

	@Override
	protected ServiceType getServiceType() {
		return ServiceType.STAT_FAM;
	}

	@Override
	protected String getFileDisplayName() {
		return GUIConstants.LANG.lbl_STAT_FAM_ReportDisplayName;
	}

	@Override
	protected String getFileTitle() {
		return GUIConstants.LANG.lbl_STAT_FAM_ReportTitle;
	}

	@Override
	protected String getFileContent() {
		String fileContent = GUIConstants.LANG.lbl_STAT_FAM_ReportContent;
		Map<String,String> userProps = null;
		try {
			userProps = operationPanel.getConnectionManager().getUserData(operationPanel.getConnectionManager().getUserAttribute("id"));
		} catch (UserNotFoundException e) {
			operationPanel.popupError(e);
			return "ERROR";
		}
		for(String attrName : userProps.keySet())
			fileContent = fileContent.replaceAll("!"+attrName, userProps.get(attrName));
		
		//build family members report
		Map<String, Map<String,String>> familyMap = operationPanel.getConnectionManager().getRelationsData();
		for(String member : familyMap.keySet()) {
			String familyMemberLine = GUIConstants.LANG.lbl_STAT_FAM_ReportSingleComponentString;
			Map<String,String> familyMemberMap = familyMap.get(member);
			for(String attrName : familyMemberMap.keySet())
				familyMemberLine = familyMemberLine.replaceAll("!"+attrName, familyMemberMap.get(attrName));
			fileContent += familyMemberLine;
		}
				
		return fileContent;
	}

}
