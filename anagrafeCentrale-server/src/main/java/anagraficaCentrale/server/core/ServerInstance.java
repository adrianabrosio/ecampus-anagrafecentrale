package anagraficaCentrale.server.core;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import anagraficaCentrale.server.sql.QueryManagerIF;
import anagraficaCentrale.utils.ClientServerConstants;
import anagraficaCentrale.utils.ClientServerConstants.ServerAction;
import anagraficaCentrale.utils.ScriptUtils;

public class ServerInstance extends Thread {

	private Socket socket;
	private Properties prop;
	private QueryManagerIF qm;
	private ServerOperationIF serverOp;
	public boolean alive = true;

	char[] encryptkey = "1 chiave per criptare!".toCharArray();
	String strToDecrypt;
	char[] strDecrypted;

	final static Logger logger = LogManager.getRootLogger();

	public ServerInstance(Socket socket, Properties prop, QueryManagerIF qm) {
		this.socket = socket;
		this.prop = prop;
		this.qm = qm;
		initializeServerOperation();
	}

	/**
	 * Fare override di questo metodo per per cambiare ServerOperation
	 */
	protected void initializeServerOperation() {
		this.serverOp = new ServerOperationImpl(prop, qm);
	}

	public void run() {
		ServerAction action = null;
		String username = null;
		String[] resultList = null;
		String message = null;

		DataOutputStream send = null;

		boolean isAlive = true;
		while(isAlive){

			logger.debug("Waiting for input..");
			// read the message with a buffer. Message end with \n
			try {
				//-- need to change EOL separator in COMM_EOL
				//BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				//strToDecrypt = br.readLine();
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
				strToDecrypt = strToDecrypt.substring(0, strToDecrypt.length()-ClientServerConstants.COMM_EOL.length());
				//-- 
				// print message received
				logger.debug(this.getName() + " - Message received (encrypted): " + new String(strToDecrypt));
				message = ScriptUtils.decrypt(strToDecrypt);
				logger.debug(this.getName() + " - Message received (decrypted): " + message);
				// Split the message received
				String[] commArgs = message.split(ClientServerConstants.COMM_SEPARATOR);
				// read the value from the message
				action = ServerAction.castIntToEnum(Integer.parseInt(commArgs[0]));
				username = commArgs.length>=1? commArgs[1] : "NULL";

				logger.info(this.getName() + " - Action: " + action + ", username: " + username );
				// Do the action
				switch ( action ){
				case LOGIN:
					resultList = this.serverOp.loginOperation(commArgs);
					break;
				case CREATE_ACCOUNT:
					resultList = this.serverOp.createAccountOperation(commArgs);
					break;
				case LOGOUT:
					resultList = this.serverOp.logoutOperation(commArgs);
					isAlive = false;
					break;
				case MARK_NOTIFICATION_AS_READ:
					resultList = this.serverOp.markNotificationAsReadOperation(commArgs);
					break;
				case DELETE_NOTIFICATION:
					resultList = this.serverOp.markNotificationAsReadOperation(commArgs);
					break;
				case DOWNLOAD_FILE:
					resultList = this.serverOp.downloadFileOperation(commArgs);
					break;
				case CREATE_NEW_REQUEST:
					resultList = this.serverOp.createNewRequestOperation(commArgs);
					break;
				case CREATE_NEW_REPORT:
					resultList = this.serverOp.createNewReportOperation(commArgs);
					break;
				case GET_REQUEST_DATA:
					resultList = this.serverOp.getRequestDataOperation(commArgs);
					break;
				case GET_USER_DATA:
					resultList = this.serverOp.getUserDataOperation(commArgs);
					break;
				case GET_NOTIFICATION_LIST:
					resultList = this.serverOp.getNotificationListOperation(commArgs);
					break;
				case GET_REQUEST_LIST:
					resultList = this.serverOp.getRequestListOperation(commArgs);
					break;
				case GET_REPORT_LIST:
					resultList = this.serverOp.getReportListOperation(commArgs);
					break;
				case CHECK_NEW_NOTIFICATION:
					resultList = this.serverOp.checkNewNotificationOperation(commArgs);
					break;
				case GET_RELATIONS:
					resultList = this.serverOp.getRelationsDataOperation(commArgs);
					break;
				default : // Unsupported action
					logger.warn(this.getName() + " - Unsupported action ["+action+"]");
					resultList = new String[]{"ERROR"};
				}

			} catch (SocketException e1) {
				logger.warn("Client session closed without logout");
				return;
			} catch (Exception e1) {
				logger.error(e1);
			}

			try {
				send = new DataOutputStream(socket.getOutputStream());
				send.write((ScriptUtils.encrypt(String.join(ClientServerConstants.COMM_SEPARATOR, resultList))+ClientServerConstants.COMM_EOL).getBytes());
				send.flush();
				logger.debug(this.getName() + " - result");
			} catch (Exception e) {
				logger.error(this.getName() + " - " + e.getMessage(), e);
			} 
		}

	}
}
