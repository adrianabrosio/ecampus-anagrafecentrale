package anagraficaCentrale.client.core;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.swing.JOptionPane;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import anagraficaCentrale.client.exception.InvalidCredentialException;
import anagraficaCentrale.client.exception.RequestNotFoundException;
import anagraficaCentrale.client.exception.ServerResponseException;
import anagraficaCentrale.client.exception.UnsupportedServiceException;
import anagraficaCentrale.client.exception.UserNotFoundException;
import anagraficaCentrale.exception.AcServerRuntimeException;
import anagraficaCentrale.utils.ClientServerConstants;
import anagraficaCentrale.utils.ClientServerConstants.PortalType;
import anagraficaCentrale.utils.ClientServerConstants.ServerAction;
import anagraficaCentrale.utils.ClientServerConstants.ServiceType;
import anagraficaCentrale.utils.ScriptUtils;

/**
 * The Connection Manager class is responsible for managing the exchange of information
 * between the client and the server
 * 
 * @author Adriana Brosio
 *
 */
public class ConnectionManager {

	final static Logger logger = LogManager.getRootLogger();

	private Socket socket;
	private boolean isAdmin;
	private String username;
	private Map<String, String> userAttributes;
	private Map<String, Map<String, String>> relationsAttributes;
	private ArrayList<Map<String,String>> notificationList, reportList;
	private Properties prop;

	private String lastError;

    /**
     * Constructor for ConnectionManager
     * 
     * @param args command-line arguments
     * @throws UnknownHostException if the host is unknown
     * @throws IOException if an I/O error occurs
     */
	public ConnectionManager(String[] args) throws UnknownHostException, IOException {
		String cfgFile = ScriptUtils.getParam(args, "-cfg=");
		this.socket = getConnection(cfgFile);
	}

    /**
     * Establish a connection to the server using the given configuration file
     * 
     * @param cfgFile the configuration file name
     * @return the socket connection
     * @throws UnknownHostException if the host is unknown
     * @throws IOException if an I/O error occurs
     */
	private Socket getConnection(String cfgFile) throws UnknownHostException, IOException {
		try {
			// load a properties file
			prop = new Properties();

			if(cfgFile != null){
				String path = new File("").getAbsolutePath() + "\\" + cfgFile;
				InputStream configFile = new FileInputStream(path);
				prop.load(configFile);
			}else{
				InputStream resourceStream = ScriptUtils.getResourceAsStream(getClass(), "client.properties");
				prop.load(resourceStream);
			}

		} catch (IOException e) {
			logger.error("Missing configuration file.\n" + e);
			return null;

		}
		String host = prop.getProperty("server.address");
		int port = Integer.parseInt(prop.getProperty("server.port", "5563"));
		logger.info("Connecting to server [" + host + ":" + port + "]");
		return new Socket(host, port);
	}

	/**
	 * serverCall method is used to send a command to the server. It is one of the most important methods of this class.<br>
	 * the first parameter in the communication is always the response code.<br>
	 *  - OK for success<br>
	 *  - KO for error<br>
	 *  In case of success, the following parameters are the response message (may be more than one, it depends from the call)<br>
	 *  In case of failure, the second parameter is the error message returned by the server<br>
	 * @param action is the action to perform
	 * @param comm_args list of arguments for the action
	 * @return array of response messages
	 */
	public String[] serverCall(ClientServerConstants.ServerAction action, String... comm_args){
		if("admin".equals(username)) // dummy user
			return new String[]{"OK"};
		try {
			String toEncrypt = "" + action.getValue() + ClientServerConstants.COMM_SEPARATOR + String.join(ClientServerConstants.COMM_SEPARATOR, comm_args);

			DataOutputStream send = new DataOutputStream(getSocket().getOutputStream());

			send.write((ScriptUtils.encrypt(toEncrypt)+ClientServerConstants.COMM_EOL).getBytes());
			send.flush();
			logger.debug("SENT: "+toEncrypt);
		} catch(Exception e) {
			logger.error(e);
			JOptionPane.showConfirmDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}

		// read the message with a buffer. Message end with EOL separator
		//there is a skeleton implementation for encryption. It is not implemented yet
		try {
			String strToDecrypt;
			InputStream is = socket.getInputStream();
			StringBuilder sb = new StringBuilder();
			byte[] buffer = new byte[1024];
			int read;
			while((read = is.read(buffer)) != -1) {
				sb.append(new String(buffer, 0, read));
				if(sb.indexOf(ClientServerConstants.COMM_EOL) != -1)
					break;
			}
			strToDecrypt = sb.toString();
			//remove EOL chars
			strToDecrypt = strToDecrypt.substring(0, strToDecrypt.length() - ClientServerConstants.COMM_EOL.length());
			logger.debug("RECV: "+ScriptUtils.decrypt(strToDecrypt));
			return ScriptUtils.decrypt(new String(strToDecrypt)).split(ClientServerConstants.COMM_SEPARATOR);
		} catch (Exception e1) {
			logger.error(e1);
		}
		return null;
	}

	private Socket getSocket() {
		if(socket == null || socket.isClosed())
			try {
				this.socket = getConnection(null);
			} catch (IOException e) {
				logger.error("Unable to get new socket", e);
			}
		return socket;
	}

	/**
	 * this method perform the user authentication
	 * @param username user id of the user
	 * @param pw password
	 * @param portalType portal type on which the user is logging in
	 * @throws InvalidCredentialException exception thrown in case of wrong credentials
	 * @throws ServerResponseException generic error thrown by the server
	 */
	public void login(String username, String pw, PortalType portalType) throws InvalidCredentialException, ServerResponseException {
		logger.debug("trying to log in as " + username);

		if (username.equals("pinco")) {
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				logger.error(e);
			}
			// ok
			this.username = "pinco";
			isAdmin = false;
			return;
		}
		if (username.equals("admin")) {
			// ok
			this.username = "admin";
			isAdmin = true;
			return;
		}

		String[] respComm = serverCall(ServerAction.LOGIN, username, pw, ""+portalType.getValue());

		if(respComm == null)
			throw new InvalidCredentialException();

		if(respComm[0].equalsIgnoreCase("KO"))
			throw new ServerResponseException(respComm[1]);

		isAdmin = respComm.length>2 && respComm[2].equalsIgnoreCase("true");
		this.username = username;
	}

	/**
	 * this method perform the user logout
	 */
	public void logout() {
		logger.debug("closing connection");
		serverCall(ServerAction.LOGOUT, this.username);
		logger.debug("connection closed!");
	}

	/**
	 * utility method to check if the user is an admin
	 * @return true if admin, false otherwise
	 */
	public boolean isAdmin() {
		return isAdmin;
	}

	/**
	 * utility method to parse a server response and check if it is an error
	 * @param callArgs
	 * @return
	 */
	private boolean checkIfErrorAndParse(String[] callArgs){
		if(callArgs==null || callArgs.length<1){
			lastError = "Invalid server response: no response or not enough arguments in the response";
			logger.error(lastError);
			return true;
		}

		if(!callArgs[0].equalsIgnoreCase(ClientServerConstants.SERVER_RESP_OK)){
			lastError = "Server error:";
			if(callArgs.length>1)
				lastError += callArgs[1];
			logger.error(lastError);
			return true;
		}

		return false;
	}
	
	/**
	 * recover user information from the server given the user id
	 * @param user user id of the user
	 * @return a map of user attributes related to the user
	 * @throws UserNotFoundException exception thrown in case of user not found
	 */
	public Map<String, String> getUserData(String user) throws UserNotFoundException{
		String[] respComm = serverCall(ServerAction.GET_USER_DATA, user);
		if(checkIfErrorAndParse(respComm)){
			logger.error(lastError);
			throw new UserNotFoundException();
		}
		HashMap<String,String> othersUserData = new HashMap<>();
		for(String ret : respComm){
			if(ret.contains("=")){
				String[] tokens = ret.split("=", 2);
				othersUserData.put(tokens[0], tokens[1]);
			}
		}
		return othersUserData;
	}

	/**
	 * utility method used to invalidate cached data in case of changes
	 */
	public void refreshUserData() {
		try {
			userAttributes = getUserData(username);
		} catch (UserNotFoundException e) {
			logger.error(e);//should never happen :)
		}
	}

	/**
	 * utility method to get a user attribute
	 * @param attrName attribute name
	 * @return the attribute value
	 */
	public String getUserAttribute(String attrName){
		if(userAttributes == null)
			refreshUserData();
		return userAttributes.get(attrName) != null? userAttributes.get(attrName) : "";
	}

	/**
	 * this method mark a notification as read
	 * @param id of the notification to be marked as read
	 */
	public void markNotificationAsRead(String id) {
		String[] respComm = serverCall(ServerAction.MARK_NOTIFICATION_AS_READ, id);
		if(checkIfErrorAndParse(respComm)){
			throw new AcServerRuntimeException(lastError);
		}
	}

	/**
	 * this method delete a notification
	 * @param id of the notification to be deleted
	 */
	public void deleteNotification(String id) {
		String[] respComm = serverCall(ServerAction.DELETE_NOTIFICATION, id);
		if(checkIfErrorAndParse(respComm)){
			throw new AcServerRuntimeException(lastError);
		}
	}

	/**
	 * this method check if there are new notifications.<br>
	 * this method is used by the polling system to manage the notification indicator.
	 * @return true if there are new notifications, false otherwise
	 */
	public boolean checkNewNotification(PortalType pt) {
		String[] respComm = serverCall(ServerAction.CHECK_NEW_NOTIFICATION, username, ""+isAdmin(), ""+pt.getValue());
		if(checkIfErrorAndParse(respComm)){
			throw new AcServerRuntimeException(lastError);
		}
		if(respComm.length>=2 && respComm[1].equalsIgnoreCase("true") ){
			return true;
		}
		return false;
	}

	/**
     * Get the notification list from the server filtered by portal type
     * 
     * @return the notification list
     */
	public ArrayList<Map<String,String>> getNewNotificationList(PortalType portalType) {
		String[] respComm = serverCall(ServerAction.GET_NOTIFICATION_LIST, username, ""+isAdmin(), ""+portalType.getValue());
		if(checkIfErrorAndParse(respComm)){
			throw new AcServerRuntimeException(lastError);
		}
		if(notificationList != null){
			notificationList.clear();
		} else {
			notificationList = new ArrayList<>();
		}
		for(String recString : respComm){
			if(!recString.contains("=")) 
				continue;
			String[] record = recString.split(ClientServerConstants.COMM_MILTIVALUE_FIELD_SEPARATOR);
			Map<String,String> recordMap = new HashMap<>();
			for(int i = 0; i < record.length; i++){
				if(record[i].contains("=")){
					String[] tokens = record[i].split("=", 2);
					recordMap.put(tokens[0], tokens[1]);
				}
			}
			notificationList.add(recordMap);
		}
		return notificationList;
	}

	/**
	 * create a new user based on the information filled in the UI
	 * @param userProps is the list of the user properties
	 */
	public void createUser(List<String[]> userProps) {
		List<String> args = new ArrayList<>();
		for(String[] prop : userProps)
			args.add(prop[0]+"="+prop[1]);

		String[] respComm = serverCall(ServerAction.CREATE_ACCOUNT, username, ""+isAdmin(), String.join(ClientServerConstants.COMM_MILTIVALUE_FIELD_SEPARATOR, args.toArray(new String[0])));
		if(checkIfErrorAndParse(respComm)){
			throw new AcServerRuntimeException(lastError);
		}
	}
	
	/**
	 * edit an existing user based on the information filled in the UI
	 * @param userProps is the list of the user properties
	 */
	public void editUser(List<String[]> userProps) {
		List<String> args = new ArrayList<>();
		for(String[] prop : userProps)
			args.add(prop[0]+"="+prop[1]);

		String[] respComm = serverCall(ServerAction.EDIT_ACCOUNT, username, ""+isAdmin(), String.join(ClientServerConstants.COMM_MILTIVALUE_FIELD_SEPARATOR, args.toArray(new String[0])));
		if(checkIfErrorAndParse(respComm)){
			throw new AcServerRuntimeException(lastError);
		}
	}
	
	/**
     * Get the report list from the server filtered by portal type
     * 
     * @return the report list
     */
	public ArrayList<Map<String,String>> getReportList(PortalType portalType) {
		String[] respComm = serverCall(ServerAction.GET_REPORT_LIST, username, ""+isAdmin(), ""+portalType.getValue());
		if(checkIfErrorAndParse(respComm)){
			throw new AcServerRuntimeException(lastError);
		}
		if(reportList != null){
			reportList.clear();
		} else {
			reportList = new ArrayList<>();
		}
		for(String recString : respComm){
			if(!recString.contains("=")) 
				continue;
			String[] record = recString.split(ClientServerConstants.COMM_MILTIVALUE_FIELD_SEPARATOR);
			Map<String,String> recordMap = new HashMap<>();
			for(int i = 0; i < record.length; i++){
				if(record[i].contains("=")){
					String[] tokens = record[i].split("=", 2);
					recordMap.put(tokens[0], tokens[1]);
				}
			}
			reportList.add(recordMap);
		}
		return reportList;
	}

	/**
	 * create an appointment
	 * @param serviceType is the input service
	 * @param userProps is the list of the properties related to the specific service
	 * @throws UnsupportedServiceException is thrown if the service is not supported
	 */
	public void createAppointment(ServiceType serviceType, List<String[]> userProps) throws UnsupportedServiceException {
		List<String> args = new ArrayList<>();
		for(String[] prop : userProps)
			args.add(prop[0]+"="+prop[1]);

		ServerAction sa;
		switch(serviceType){
		case APP_CI:
		case PREN_VIS:
		case COLL_INS:
			sa = ServerAction.CREATE_NEW_REQUEST;
			break;
		default:
			throw new UnsupportedServiceException(serviceType);
		}

		String[] respComm = serverCall(sa, username, ""+isAdmin(), ""+serviceType, String.join(ClientServerConstants.COMM_MILTIVALUE_FIELD_SEPARATOR, args.toArray(new String[0])));
		if(checkIfErrorAndParse(respComm)){
			throw new AcServerRuntimeException(lastError);
		}
	}

	/**
	 * utility method used to invalidate cached data in case of changes
	 */
	public void refreshRelationsData() {

		String[] respComm = serverCall(ServerAction.GET_RELATIONS, username);
		if(checkIfErrorAndParse(respComm)){
			throw new AcServerRuntimeException(lastError);
		}
		//the user may not have any relation. In any case, clear the relations attributes
		//append to relationList all the field of respComm except the first one
		String[] relationList = new String[respComm.length-1];
		for(int i = 1; i < respComm.length; i++){
			relationList[i-1] = respComm[i];
		}
		
		if(relationsAttributes != null){
			relationsAttributes.clear();
		} else {
			relationsAttributes = new HashMap<>();
		}

		for(String user : relationList){
			try {
				relationsAttributes.put(user, getUserData(user));
			} catch (UserNotFoundException e) {/*implicitly managed*/}
		}
	}

	/**
	 * get an attribute of a relation user
	 * @param username is the username of the related user
	 * @param attrName is the attribute name
	 * @return the attribute value
	 */
	public String getRelationAttribute(String username, String attrName){
		return relationsAttributes.get(username).get(attrName);
	}

	/**
	 * recover the username of a user based on an attribute.<br>
	 * this method works only if the attribute is unique (e.g. tax_id_code)
	 * @param attrName is the attribute name
	 * @param attrValue is the attribute value
	 * @return the username of the relation user
	 */
	public String getRelationUsernameFromAttribute(String attrName, String attrValue){
		for(String key : getRelationsList()) {
			if(attrValue.equals(getRelationAttribute(key, attrName)))
				return key;
		}
		return null;
	}

	/**
	 * get the list of the related users
	 * @return the list of the related users
	 */
	public String[] getRelationsList(){
		if(relationsAttributes == null)
			throw new RuntimeException("Programming error:you must refresh relation data before call this method");
		return relationsAttributes.keySet().toArray(new String[0]);
	}

	/**
	 * this method return a map that contains the attributes of the user and other users
	 * related to him.
	 * The map is composed using the tax_id_code as key and a sub Map for the users attributes.
	 * 
	 */
	public Map<String, Map<String,String>> getRelationsDataByTaxIdCode(){
		refreshRelationsData();
		String[] relations = getRelationsList();
		Map<String, Map<String,String>> taxIdList = new HashMap<>();
		for(String rel:relations){
			Map<String,String> tmpMap2 = new HashMap<>();
			tmpMap2.put("first_name", getRelationAttribute(rel, "first_name"));
			tmpMap2.put("surname", getRelationAttribute(rel, "surname"));
			taxIdList.put(getRelationAttribute(rel, "tax_id_code"), tmpMap2);
		}
		return taxIdList;
	}
	
	/**
	 * get the map of the attributes of the related users
	 * it refreshes cached data before return
	 * @return the map of the attributes of the related users
	 */
	public Map<String, Map<String,String>> getRelationsData(){
		refreshRelationsData();
		return relationsAttributes;
	}

	/**
	 * method used to create a new request
	 * @param serviceType is the service type
	 * @param userProps is the list of the attribute for the request
	 */
	public void createSimpleRequest(ServiceType serviceType, List<String[]> userProps) {
		List<String> args = new ArrayList<>();
		for(String[] prop : userProps)
			args.add(prop[0]+"="+prop[1]);

		String[] respComm = serverCall(ServerAction.CREATE_NEW_REQUEST, username, ""+isAdmin(), ""+serviceType, String.join(ClientServerConstants.COMM_MILTIVALUE_FIELD_SEPARATOR, args.toArray(new String[0])));
		if(checkIfErrorAndParse(respComm)){
			throw new AcServerRuntimeException(lastError);
		}
	}

	/**
	 * 
	 * @param camRes service type
	 * @param userProps is the list of the attribute for the request
	 */
	public void createResidenceChangeRequest(ServiceType camRes, List<String[]> userProps) {
		createSimpleRequest(camRes, userProps);
	}

	/**
	 * 
	 * @param camMed service type
	 * @param userProps is the list of the attribute for the request
	 */
	public void createDoctorChangeRequest(ServiceType camMed, List<String[]> userProps) {
		createSimpleRequest(camMed, userProps);
	}
	
	/**
	 * create a certificate request. It generate a report for the user.
	 * @param serviceType is the service type
	 * @param userProps is the list of the attribute for the request
	 */
	public void createCertificateRequest(ServiceType serviceType, List<String[]> userProps) {
		List<String> args = new ArrayList<>();
		for(String[] prop : userProps)
			args.add(prop[0]+"="+prop[1]);

		String[] respComm = serverCall(ServerAction.CREATE_NEW_REPORT, username, ""+isAdmin(), ""+serviceType, String.join(ClientServerConstants.COMM_MILTIVALUE_FIELD_SEPARATOR, args.toArray(new String[0])));
		if(checkIfErrorAndParse(respComm)){
			throw new AcServerRuntimeException(lastError);
		}
	}

	/**
	 * this is a placeholder for payment request
	 * @param pagTick is the service type
	 * @param userProps is the list of the attribute for the request
	 */
	public void createPaymentRequest(ServiceType pagTick, List<String[]> userProps) {
		/*FAKE OPERATION*/

	}

	/**
	 * this method return the list of the admin support requests for admin support request panel
	 * @param portalType is the portal type
	 * @return the list of the admin support requests
	 */
	public List<Map<String, String>> getAdminSupportRequestsList(PortalType portalType) {
		List<Map<String, String>> requestList = new ArrayList<>();
		String[] respComm = serverCall(ServerAction.GET_ALL_ADM_MNG_REQ, username, ""+isAdmin(), ""+portalType.getValue());
		if(checkIfErrorAndParse(respComm)){
			logger.error(lastError);
			throw new AcServerRuntimeException(lastError);
		}

		for(String recString : respComm){
			if(!recString.contains("=")) 
				continue;
			Map<String,String> recordMap = ScriptUtils.convertParamStringToMap(recString, ClientServerConstants.COMM_MILTIVALUE_FIELD_SEPARATOR);
			requestList.add(recordMap);
		}
		return requestList;
	}

	/**
	 * this method manage the admin support request
	 * @param serviceData is the service data
	 * @param acceptRequest is a boolean value that represent the request status (accepted or rejected)
	 */
	public void manageRequest(Map<String, String> serviceData, boolean acceptRequest) {
		String[] respComm = serverCall(ServerAction.ADM_MNG_REQ, username, ""+isAdmin(), ""+serviceData.get("id"), ""+acceptRequest);
		if(checkIfErrorAndParse(respComm)){
			logger.error(lastError);
			throw new AcServerRuntimeException(lastError);
		}
	}

	/**
	 * get the request data
	 * @param requestId request id
	 * @return the list of the attributes related to a specific request
	 * @throws RequestNotFoundException if the request is not found
	 */
	public Map<String, String> getRequestData(String requestId) throws RequestNotFoundException {
		String[] respComm = serverCall(ServerAction.GET_REQUEST_DATA, username, ""+isAdmin(), requestId);
		if(checkIfErrorAndParse(respComm)){
			logger.error(lastError);
			throw new RequestNotFoundException();
		}
		if(respComm.length >= 2){
			Map<String,String> requestData = ScriptUtils.convertParamStringToMap(respComm[1], ClientServerConstants.COMM_MILTIVALUE_FIELD_SEPARATOR);
			return requestData;
		}else{
			throw new RequestNotFoundException();
		}
		
	}

	/**
	 * method used to change the user password. User must exists.
	 * @param username is the username
	 * @param oldPassword is the old password
	 * @param newPassword is the new password
	 * @throws NoSuchAlgorithmException it the hash operation fails
	 * @throws AcServerRuntimeException in case of server error
	 */
	public void passwordReset(String username, char[] oldPassword, char[] newPassword) throws NoSuchAlgorithmException {
		String[] respComm = serverCall(ServerAction.USR_CHANGE_PASS, username, ScriptUtils.hash(oldPassword), ScriptUtils.hash(newPassword));
		
		if(checkIfErrorAndParse(respComm)){
			logger.error(lastError);
			throw new AcServerRuntimeException(lastError);
		}
	}
}
