package anagraficaCentrale.server.core;

/**
 * 
 * @author Adriana Brosio
 *
 */
public interface ServerOperationIF {

	/**<b>LOGOUT</b> <br/>
	 * no params<br/>
	 */
	public String[] logoutOperation(String[] commArgs);
	
	/**<b>LOGIN</b> <br/>
	 * request: username, password<br/>
	 * response: result[OK|KO], message, isAdmin[true,false]<br/>
	 */
	public String[] loginOperation(String[] commArgs);
	
	/**<b>CREATE_ACCOUNT</b> - create a new account<br/>
	 * request: username, isAdmin[true,false], [user properties]<br/>
	 * response: result[OK|KO], message<br/>
	 */
	public String[] createAccountOperation(String[] commArgs);
	
	/**<b>MARK_NOTIFICATION_AS_READ</b> - mark a notification as read<br/>
	 * request: notificationId <br/>
	 * response: result[OK|KO], message<br/>
	 */
	public String[] markNotificationAsReadOperation(String[] commArgs);
	
	/**TODO <b>DOWNLOAD_FILE</b> - download a specific report file<br/>
	 * request: reportId<br/>
	 * response: result[OK|KO], message<br/>
	 */
	public String[] downloadFileOperation(String[] commArgs);
	
	/**<b> CREATE_NEW_REQUEST</b> - create a new request to be managed<br/>
	 * request: username, isAdmin[true|false], service_type, [request data]<br/>
	 * response: result[OK|KO], message<br/>
	 */
	public String[] createNewRequestOperation(String[] commArgs);
	
	/**<b> CREATE_NEW_REPORT</b> - create a new report<br/>
	 * request: username, isAdmin[true|false], service_type, [report data]<br/>
	 * response: result[OK|KO], message<br/>
	 */
	public String[] createNewReportOperation(String[] commArgs);
	
	/**<b> GET_REQUEST_DATA</b> - get specific request data from the database <br/>
	 * request: username, isAdmin[true|false], requestId<br/>
	 * response: result[OK|KO], message<br/>
	 */
	public String[] getRequestDataOperation(String[] commArgs);

	/**<b>GET_REPORT_DATA</b> - get specific report data from the database <<br/>
	  * request: username, isAdmin[true|false], reportId<br/>
	 * response: result[OK|KO], message<br/>
	 */
	public String[] getReportDataOperation(String[] commArgs);
	
	/**<b>GET_USER_DATA</b> - get user data from the database <br/>
	 * request: username<br/>
	 * response: result[OK|KO], [list of the attribute in format <i>attrName=attrValue</i>]<br/>
	 */
	public String[] getUserDataOperation(String[] commArgs);
	
	/**<b>GET_NOTIFICATION_LIST</b> - get the list of all notifications<br/>
	 * request: username, isAdmin[true|false], portal_type<br/>
	 * response: result[OK|KO], [notifications list]<br/>
	 */
	public String[] getNotificationListOperation(String[] commArgs);
	
	/**<b> GET_REQUEST_LIST</b> - get the list of all requests <br/>
	 * request: username, isAdmin[true,false], portal_type<br/>
	 * response: result[OK|KO], [request list]<br/>
	 */
	public String[] getRequestListOperation(String[] commArgs);
	
	/**<b> GET_REPORT_LIST</b> - recover the list of the reports attached to the logon user for the specific portal<br/>
	 * request: username, isAdmin[true,false], portal_type<br/>
	 * response: result[OK|KO], [reports list]<br/>
	 */
	public String[] getReportListOperation(String[] commArgs);

	/**<b>CHECK_NEW_NOTIFICATION</b> - look for new notification on the server<br/>
	 * request: username, isAdmin[true,false], portal_type<br/>
	 * response: result[OK|KO], new_notification[true|false]<br/>
	 */
	public String[] checkNewNotificationOperation(String[] commArgs);
	
	/**TODO <b>DELETE_NOTIFICATION</b> - delete a notification from the server <br/>
	 * request: notification_id<br/>
	 * response: result[OK|KO]<br/>
	 */
	public String[] deleteNotificationOperation(String[] commArgs);

	/**<b> GET_RELATIONS</b> - get user relations list <br/>
	 * request: username<br/>
	 * response: result[OK|KO], <usernames of related users><br/>
	 */
	public String[] getRelationsDataOperation(String[] commArgs);
	
	/**<b>EDIT_ACCOUNT</b> - request to edit information related an existing user<br/>
	 * request: username, isAdmin[true,false], [user properties]<br/>
	 * response: result[OK|KO], message<br/>
	 */
	public String[] editAccountOperation(String[] commArgs);
	
	/**<b>GET_ALL_ADM_VAL_REQ</b> - request all open request to be managed by administrators<br/>
	 * request: username, isAdmin[true,false], portal_type
	 * response: result[OK|KO], <list of records related to admin support requests>
	 */
	public String[] getAllAdminSupportRequestsOperation(String[] commArgs);

	/**<b>ADM_MNG_REQ</b> - manage a single request<br/>
	 * request: username, isAdmin[true,false], id, acceptRequest[true,false]<br/>
	 * response: result[OK|KO], message<br/>
	 */
	public String[] manageRequestOperation(String[] commArgs);
	
	/**<b>USR_CHANGE_PASS</b> - password reset request<br/>
	 * request: username, oldPassword, newPassword<br/>
	 * response: result[OK|KO], message<br/>
	 */
	public String[] passwordResetOperation(String[] commArgs);
}
