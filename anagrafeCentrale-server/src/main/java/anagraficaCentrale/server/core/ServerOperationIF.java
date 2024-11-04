package anagraficaCentrale.server.core;

/**
 * 
 * @author Adriana Brosio
 *
 */
public interface ServerOperationIF {

	/**LOGOUT
	 * no params
	 */
	public String[] logoutOperation(String[] commArgs);
	
	/**LOGIN
	 * request: username, password
	 * response: result[OK|KO], message, isAdmin[true,false]
	 */
	public String[] loginOperation(String[] commArgs);
	
	/**CREATE_ACCOUNT
	 * request: username, password
	 * response: result[OK|KO], message
	 */
	public String[] createAccountOperation(String[] commArgs);
	
	/**TODO MARK_NOTIFICATION_AS_READ
	 * request: username
	 * response: result[OK|KO], message
	 */
	public String[] markNotificationAsReadOperation(String[] commArgs);
	
	/**TODO DOWNLOAD_FILE
	 * request: username
	 * response: result[OK|KO], message
	 */
	public String[] downloadFileOperation(String[] commArgs);
	
	/**TODO CREATE_NEW_REQUEST
	 * request: username
	 * response: result[OK|KO], message
	 */
	public String[] createNewRequestOperation(String[] commArgs);
	
	/**TODO CREATE_NEW_REPORT
	 * request: username
	 * response: result[OK|KO], message
	 */
	public String[] createNewReportOperation(String[] commArgs);
	
	/**TODO GET_REQUEST_FORM
	 * request: username
	 * response: result[OK|KO], message
	 */
	public String[] getRequestDataOperation(String[] commArgs);
	
	/**GET_USER_DATA - refresh the user data from the database
	 * request: username
	 * response: result[OK|KO], [list of the attribute in format <attrName=attrValue>]
	 */
	public String[] getUserDataOperation(String[] commArgs);
	
	/**GET_NOTIFICATION_LIST
	 * request: username
	 * response: result[OK|KO], 
	 */
	public String[] getNotificationListOperation(String[] commArgs);
	
	/**TODO GET_REQUEST_LIST
	 * request: username
	 * response: result[OK|KO], 
	 */
	public String[] getRequestListOperation(String[] commArgs);
	
	/**TODO GET_REPORT_LIST
	 * request: username
	 * response: result[OK|KO], 
	 */
	public String[] getReportListOperation(String[] commArgs);

	/**CHECK_NEW_NOTIFICATION - look for new notification on the server
	 * request: username
	 * response: result[OK|KO], new notification[true|false]
	 */
	public String[] checkNewNotificationOperation(String[] commArgs);
	
	/**TODO DELETE_NOTIFICATION - delete a notification from the server
	 * request: username
	 * response: result[OK|KO]
	 */
	public String[] deleteNotificationOperation(String[] commArgs);

	/**TODO RELATIONS_DATA - get user relations list
	 * request: username
	 * response: result[OK|KO], <usernames of related users>
	 */
	public String[] getRelationsDataOperation(String[] commArgs);
	
	/**EDIT_ACCOUNT
	 * request: username, password
	 * response: result[OK|KO], message
	 */
	public String[] editAccountOperation(String[] commArgs);
	
	/**GET_ALL_ADM_VAL_REQ
	 * request: username
	 * response: result[OK|KO], <list of records related to admin support requests>
	 */
	public String[] getAllAdminSupportRequestsOperation(String[] commArgs);

	/**ADM_MNG_REQ
	 * request: username, isAdmin, id, acceptRequest
	 * response: result[OK|KO], message
	 */
	public String[] manageRequestOperation(String[] commArgs);
}
