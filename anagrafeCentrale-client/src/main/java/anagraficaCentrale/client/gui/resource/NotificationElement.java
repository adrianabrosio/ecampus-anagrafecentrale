package anagraficaCentrale.client.gui.resource;

import java.util.Map;

import anagraficaCentrale.client.core.ConnectionManager;
import anagraficaCentrale.client.exception.UnsupportedServiceException;
import anagraficaCentrale.client.gui.GUIConstants;
import anagraficaCentrale.client.gui.OperationPanel;
import anagraficaCentrale.utils.ClientServerConstants.ServiceType;

public class NotificationElement extends AbstractResourceElement {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private ConnectionManager cm;
	private String iconImageName = "notificationOnIcon.png";
	private String id, name, description;
	private Map<String, String> record;

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

	public NotificationElement(OperationPanel op, Map<String, String> record) {
		super(op, "");
		this.cm = op.getConnectionManager();
		this.record = record;
		this.id = record.get("id");
		this.name = record.get("notification_name");
		//this.description = record.get("notification_description");
		this.unread = "1".equalsIgnoreCase(record.get("unread"));
		
		iconImageName = unread? "notificationOnIcon.png" : "notificationIcon.png";
		this.setButtonIconImage();
		
		boolean requestAccepted = record.getOrDefault("notification_description", "").contains("accepted=Y");
		this.description = "["+id+"]"+" "+getTextByServiceType(record)+": "+(requestAccepted?GUIConstants.LANG.lblRequestAccepted : GUIConstants.LANG.lblRequestDeclined);
		this.setDescription(this.description);
	}

	@Override
	protected void executeAction() {
		if(unread){
			this.unread = false;
			this.cm.markNotificationAsRead(this.id);
			iconImageName = "notificationIcon.png";
			this.setButtonIconImage();
		}
		if(record.containsKey("request_id")) {
			try {
				Map<String, String> requestData = operationPanel.getConnectionManager().getRequestData(record.get("request_id"));
				operationPanel.openService(ServiceType.ADM_REQ_MNG, requestData);
			} catch (Exception e) {
				operationPanel.popupError(e);
			}
		}
	}

	@Override
	protected void executePostAction() {
		/*NO-OP*/
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

	protected String getTextByServiceType(Map<String, String> record) {
		String requestType = record.get("notification_type");
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
			return GUIConstants.LANG.lbl_APP_CI_SrvNotificationText;
		case CAM_MED:
			return GUIConstants.LANG.lbl_CAM_MED_SrvNotificationText;
		case CAM_RES:
			return GUIConstants.LANG.lbl_CAM_RES_SrvNotificationText;
		case CERT_MATR:
			return GUIConstants.LANG.lbl_CERT_MATR_SrvNotificationText;
		case CERT_NASC:
			return GUIConstants.LANG.lbl_CERT_NASC_SrvNotificationText;
		case CI_TEMP:
			return GUIConstants.LANG.lbl_CI_TEMP_SrvNotificationText;
		case COLL_INS:
			return GUIConstants.LANG.lbl_COLL_INS_SrvNotificationText;
		case DUMMY:
			return ""; //do not create requests
		case ISCRIZ:
			return GUIConstants.LANG.lbl_ISCRIZ_SrvNotificationText;
		case PAG_MEN:
			return GUIConstants.LANG.lbl_PAG_MEN_SrvNotificationText;
		case PAG_RET:
			return GUIConstants.LANG.lbl_PAG_RET_SrvNotificationText;
		case PAG_TICK:
			return GUIConstants.LANG.lbl_PAG_TICK_SrvNotificationText;
		case PREN_VIS:
			return GUIConstants.LANG.lbl_PREN_VIS_SrvNotificationText;
		case STAT_FAM:
			return GUIConstants.LANG.lbl_STAT_FAM_SrvNotificationText;
		default:
			break;
		}
			
		return "";
	}
}
