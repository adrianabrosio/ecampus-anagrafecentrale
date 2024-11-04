/**
 * 
 */
package anagraficaCentrale.client.gui.service;

import java.util.List;
import java.util.Map;

import anagraficaCentrale.client.gui.GUIConstants;
import anagraficaCentrale.client.gui.OperationPanel;
import anagraficaCentrale.client.gui.resource.AdminSupportElement;
import anagraficaCentrale.client.gui.resource.FilterableResourcePanel;
import anagraficaCentrale.utils.ClientServerConstants.PortalType;
import anagraficaCentrale.utils.ClientServerConstants.ServiceType;

/**
 * 
 *
 */
public class AdminSupportPanel extends FilterableResourcePanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	public AdminSupportPanel(OperationPanel op,PortalType portalType, boolean isAdmin) {
		super();
		List<Map<String, String>> list = op.getConnectionManager().getAdminSupportRequestsList(portalType);

		for (Map<String, String> record : list) {
			StringBuilder elementDescription = new StringBuilder();
			elementDescription.append(getRequestName(record.get("request_name"), record.get("request_type")));
			if(record.get("request_description") != null && !record.get("request_description").isEmpty()) {
				elementDescription.append(": ");
				elementDescription.append(record.get("request_description"));
			}
			
			addResource(new AdminSupportElement(op, elementDescription.toString(), record));
		}

	}

	private Object getRequestName(String requestName, String requestType) {
		if(requestName != null && !requestName.isEmpty()) {
			return requestName;
		}
		switch(ServiceType.valueOf(requestType)) {
		case ADM_CREAZ_USR:
			return ""; //do not create requests
		case ADM_MOD_USR:
			return ""; //do not create requests
		case APP_CI:
			return GUIConstants.LANG.lbl_APP_CI_SrvTitle;
		case CAM_MED:
			return GUIConstants.LANG.lbl_CAM_MED_SrvTitle;
		case CAM_RES:
			return GUIConstants.LANG.lbl_CAM_RES_SrvTitle;
		case CERT_MATR:
			return GUIConstants.LANG.lbl_CERT_MATR_SrvTitle;
		case CERT_NASC:
			return GUIConstants.LANG.lbl_CERT_NASC_SrvTitle;
		case CI_TEMP:
			return GUIConstants.LANG.lbl_CI_TEMP_SrvTitle;
		case COLL_INS:
			return GUIConstants.LANG.lbl_COLL_INS_SrvTitle;
		case DUMMY:
			return ""; //do not create requests
		case ISCRIZ:
			return GUIConstants.LANG.lbl_ISCRIZ_SrvTitle;
		case PAG_MEN:
			return GUIConstants.LANG.lbl_PAG_MEN_SrvTitle;
		case PAG_RET:
			return GUIConstants.LANG.lbl_PAG_RET_SrvTitle;
		case PAG_TICK:
			return GUIConstants.LANG.lbl_PAG_TICK_SrvTitle;
		case PREN_VIS:
			return GUIConstants.LANG.lbl_PREN_VIS_SrvTitle;
		case STAT_FAM:
			return GUIConstants.LANG.lbl_STAT_FAM_SrvTitle;
		default:
			break;
		}
		return null;
	}


}
