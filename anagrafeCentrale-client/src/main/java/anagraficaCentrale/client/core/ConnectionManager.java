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
 * La classe connection manager si occupa di gestire lo scambio di informazioni
 * tra il client ed il server
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

	public ConnectionManager(String[] args) throws UnknownHostException, IOException {
		String cfgFile = ScriptUtils.getParam(args, "-cfg=");
		this.socket = getConnection(cfgFile);
	}

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

		// read the message with a buffer. Message end with \n
		try {
			String strToDecrypt;
			//-- need to change EOL separator in COMM_EOL
			//char[] strToDecrypt = (new BufferedReader(new InputStreamReader(getSocket().getInputStream()))).readLine().toCharArray();
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
			//-- 
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

	public void logout() {
		logger.debug("closing connection");
		serverCall(ServerAction.LOGOUT, this.username);
		logger.debug("connection closed!");
	}

	public boolean isAdmin() {
		return isAdmin;
	}

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

	public void refreshUserData() {
		try {
			userAttributes = getUserData(username);
		} catch (UserNotFoundException e) {
			logger.error(e);//should never happen :)
		}
	}

	public String getUserAttribute(String attrName){
		if(userAttributes == null)
			refreshUserData();
		return userAttributes.get(attrName) != null? userAttributes.get(attrName) : "";
	}

	public void markNotificationAsRead(String id) {
		String[] respComm = serverCall(ServerAction.MARK_NOTIFICATION_AS_READ, id);
		if(checkIfErrorAndParse(respComm)){
			throw new AcServerRuntimeException(lastError);
		}
	}

	public void deleteNotification(String id) {
		String[] respComm = serverCall(ServerAction.DELETE_NOTIFICATION, id);
		if(checkIfErrorAndParse(respComm)){
			throw new AcServerRuntimeException(lastError);
		}
	}

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

	public void createUser(List<String[]> userProps) {
		List<String> args = new ArrayList<>();
		for(String[] prop : userProps)
			args.add(prop[0]+"="+prop[1]);

		String[] respComm = serverCall(ServerAction.CREATE_ACCOUNT, username, ""+isAdmin(), String.join(ClientServerConstants.COMM_MILTIVALUE_FIELD_SEPARATOR, args.toArray(new String[0])));
		if(checkIfErrorAndParse(respComm)){
			throw new AcServerRuntimeException(lastError);
		}
	}
	
	public void editUser(List<String[]> userProps) {
		List<String> args = new ArrayList<>();
		for(String[] prop : userProps)
			args.add(prop[0]+"="+prop[1]);

		String[] respComm = serverCall(ServerAction.EDIT_ACCOUNT, username, ""+isAdmin(), String.join(ClientServerConstants.COMM_MILTIVALUE_FIELD_SEPARATOR, args.toArray(new String[0])));
		if(checkIfErrorAndParse(respComm)){
			throw new AcServerRuntimeException(lastError);
		}
	}

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

	public void refreshRelationsData() {

		String[] respComm = serverCall(ServerAction.GET_RELATIONS, username);
		if(checkIfErrorAndParse(respComm)){
			throw new AcServerRuntimeException(lastError);
		}
		//the user may not have any relation. In any case, clear the relations attributes 
		String[] relationList = respComm.length>=2? respComm[1].split(ClientServerConstants.COMM_MILTIVALUE_FIELD_SEPARATOR) : new String[0];
		if(relationsAttributes != null){
			relationsAttributes.clear();
		} else {
			relationsAttributes = new HashMap<>();
		}

		for(String user : relationList){
			/*Map<String, String> tmpMap = new HashMap<>();
			respComm = serverCall(ServerAction.GET_USER_DATA, user);
			if(checkIfErrorAndParse(respComm)){
				throw new AcServerRuntimeException(lastError);
			}
			for(String ret : respComm){
				if(ret.contains("=")){
					String[] tokens = ret.split("=", 2);
					tmpMap.put(tokens[0], tokens[1]);
				}
			}*/
			try {
				relationsAttributes.put(user, getUserData(user));
			} catch (UserNotFoundException e) {/*implicitly managed*/}
		}
	}

	public String getRelationAttribute(String username, String attrName){
		return relationsAttributes.get(username).get(attrName);
	}

	public String getRelationUsernameFromAttribute(String attrName, String attrValue){
		for(String key : getRelationsList()) {
			if(attrValue.equals(getRelationAttribute(key, attrName)))
				return key;
		}
		return null;
	}

	public String[] getRelationsList(){
		if(relationsAttributes == null)
			throw new RuntimeException("Proramming error:you must refresh relation data before call this method");
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
	
	public Map<String, Map<String,String>> getRelationsData(){
		refreshRelationsData();
		return relationsAttributes;
	}

	public void createSimpleRequest(ServiceType serviceType, List<String[]> userProps) {
		List<String> args = new ArrayList<>();
		for(String[] prop : userProps)
			args.add(prop[0]+"="+prop[1]);

		String[] respComm = serverCall(ServerAction.CREATE_NEW_REQUEST, username, ""+isAdmin(), ""+serviceType, String.join(ClientServerConstants.COMM_MILTIVALUE_FIELD_SEPARATOR, args.toArray(new String[0])));
		if(checkIfErrorAndParse(respComm)){
			throw new AcServerRuntimeException(lastError);
		}
	}

	public void createResidenceChangeRequest(ServiceType camRes, List<String[]> userProps) {
		createSimpleRequest(camRes, userProps);
	}

	public void createDoctorChangeRequest(ServiceType camMed, List<String[]> userProps) {
		createSimpleRequest(camMed, userProps);
	}
	
	public void createCertificateRequest(ServiceType serviceType, List<String[]> userProps) {
		List<String> args = new ArrayList<>();
		for(String[] prop : userProps)
			args.add(prop[0]+"="+prop[1]);

		String[] respComm = serverCall(ServerAction.CREATE_NEW_REPORT, username, ""+isAdmin(), ""+serviceType, String.join(ClientServerConstants.COMM_MILTIVALUE_FIELD_SEPARATOR, args.toArray(new String[0])));
		if(checkIfErrorAndParse(respComm)){
			throw new AcServerRuntimeException(lastError);
		}
	}

	public void createPaymentRequest(ServiceType pagTick, List<String[]> userProps) {
		/*FAKE OPERATION*/

	}

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

	public void manageRequest(Map<String, String> serviceData, boolean acceptRequest) {
		String[] respComm = serverCall(ServerAction.ADM_MNG_REQ, username, ""+isAdmin(), ""+serviceData.get("id"), ""+acceptRequest);
		if(checkIfErrorAndParse(respComm)){
			logger.error(lastError);
			throw new AcServerRuntimeException(lastError);
		}
	}

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

	public void passwordReset(String username, char[] oldPassword, char[] newPassword) throws NoSuchAlgorithmException {
		String[] respComm = serverCall(ServerAction.USR_CHANGE_PASS, username, ScriptUtils.hash(oldPassword), ScriptUtils.hash(newPassword));
		
		if(checkIfErrorAndParse(respComm)){
			logger.error(lastError);
			throw new AcServerRuntimeException(lastError);
		}
	}
}
