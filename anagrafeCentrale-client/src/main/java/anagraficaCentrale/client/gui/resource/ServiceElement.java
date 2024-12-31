package anagraficaCentrale.client.gui.resource;

import anagraficaCentrale.client.gui.OperationPanel;
import anagraficaCentrale.utils.ClientServerConstants.ServiceType;

/**
 * this class represents the service element. It specializes the abstract resource element
 * @author Adriana Brosio
 */
public class ServiceElement extends AbstractResourceElement {

	private static final long serialVersionUID = 1L;

	private ServiceType service;

	public ServiceElement(OperationPanel op, String description) {
		super(op, description);
		this.service = ServiceType.DUMMY;
	}

	public ServiceElement(OperationPanel op, String description, ServiceType service) {
		super(op, description);
		this.service = service;
	}

	@Override
	protected void executeAction() {
		operationPanel.openService(this.service);
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
