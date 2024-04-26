package anagraficaCentrale.client.gui.resource;

import anagraficaCentrale.client.core.ConnectionManager;
import anagraficaCentrale.client.gui.OperationPanel;

public class NotificationElement extends AbstractResourceElement {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private ConnectionManager cm;
	private String iconImageName = "notificationOnIcon.png";
	private String id, name, description;

	private boolean unread;


	public NotificationElement(OperationPanel op, String id, String name, String description, boolean unread) {
		super(op, "["+id+"]"+" "+name+"-"+description);
		this.cm = op.getConnectionManager();
		this.id = id;
		this.name = name;
		this.description = description;
		this.unread = unread;
		
		iconImageName = unread? "notificationOnIcon.png" : "notificationIcon.png";
		this.setButtonIconImage();
	}

	@Override
	protected void executeAction() {
		if(unread){
			this.unread = false;
			this.cm.markNotificationAsRead(this.id);
			iconImageName = "notificationIcon.png";
			this.setButtonIconImage();
		}
	}

	@Override
	protected void executePostAction() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected String getButtonIconName() {
		return iconImageName!=null? iconImageName : "notificationOnIcon.png";
	}


	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public boolean isUnread() {
		return unread;
	}

}
