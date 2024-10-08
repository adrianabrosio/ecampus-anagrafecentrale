/**
 * 
 */
package anagraficaCentrale.client.gui.service;

import java.util.List;
import java.util.Map;

import anagraficaCentrale.client.gui.GUIConstants;
import anagraficaCentrale.client.gui.OperationPanel;
import anagraficaCentrale.client.gui.resource.FilterableResourcePanel;
import anagraficaCentrale.client.gui.resource.ServiceElement;
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

			AdminSupportElement se = new AdminSupportElement(op, record.get("description"), ServiceType.ADM_VAL_REQ);
		}

	}


}
