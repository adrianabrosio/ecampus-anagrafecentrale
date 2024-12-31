package anagraficaCentrale.client.gui.resource;

import java.util.Map;

import anagraficaCentrale.client.gui.OperationPanel;
import anagraficaCentrale.utils.ClientServerConstants.ServiceType;

/**
 * this class represents the admin support element. It specializes the abstract resource element
 * @author Adriana Brosio
 */
public class AdminSupportElement extends AbstractResourceElement {

	private static final long serialVersionUID = 1L;

	private ServiceType service;
	
	private Map<String, String> serviceData;
	
	public AdminSupportElement(OperationPanel op, String description, Map<String, String> serviceData) {
		super(op, description);
		this.service = ServiceType.ADM_REQ_MNG;
		this.serviceData = serviceData;
	}

	@Override
	protected void executeAction() {
		operationPanel.openService(this.service, serviceData);
	}

	@Override
	protected void executePostAction() {
		/*no-op*/
	}

	@Override
	protected String getButtonIconName() {
		return "serviceButtonIcon.png";
	}



}
