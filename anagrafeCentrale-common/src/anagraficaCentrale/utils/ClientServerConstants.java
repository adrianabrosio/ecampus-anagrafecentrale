package anagraficaCentrale.utils;

import java.util.HashMap;
import java.util.Map;

public class ClientServerConstants {
	/**
	 * Questa enum descrive i tipi di portale disponibili<br/><br/>
	 * <table border="1">
	 * 	<tr><th>Portal</th><th>Descrizione</th></tr>
	 * 	<tr><td>Comune</td><td>Anagrafe - gestione pratiche comunali</td></tr>
	 * 	<tr><td>Ospedale</td><td>Ospedale - gestione pratiche ospedaliere</td></tr>
	 * 	<tr><td>Scuola</td><td>Scuola - gestione pratiche scolastiche</td></tr>
	 * </table>
	 */
	public enum PortalType {
		COMUNE(0), 
		OSPEDALE(1), 
		SCUOLA(2);
		
		private static Map<Integer, PortalType> internalMap = new HashMap<>();
		static {
		    for (PortalType action : PortalType.values()) {
		    	internalMap.put(action.getValue(), action);
		    }
		}

		private int value;
		PortalType(int value) {
			this.value = value;
		}
		public int getValue() {
			return this.value;
		}
		public static PortalType castIntToEnum(int value) {
		    return internalMap.get(value);
		}
	};

	/**
	 * Questa enum descrive i tipi di servizi disponibili e la relativa codifica
	 * <table border="1">
	 * 	<tr><th>Portal</th>	<th>Servizio</th><th>Codice tecnico</th></tr>
	 * 	<tr><td>Comune</td>		<td>Appuntamento Carta d'Identità</td>	<td>APP_CI</td></tr>
	 * 	<tr><td>Comune</td>		<td>Carta d'Identità temporanea</td>	<td>CI_TEMP</td></tr>
	 * 	<tr><td>Comune</td>		<td>Cambio residenza</td> 				<td>CAM_RES</td></tr>
	 * 	<tr><td>Comune</td>		<td>Certificato di Nascita</td> 		<td>CERT_NASC</td></tr>
	 * 	<tr><td>Comune</td>		<td>Certificato di matrimonio</td> 		<td>CERT_MATR</td></tr>
	 * 	<tr><td>Comune</td>		<td>Stato di famiglia</td> 				<td>STAT_FAM</td></tr>
	 * 	<tr><td>Ospedale</td>	<td>Cambio medico</td> 					<td>CAM_MED</td></tr>
	 * 	<tr><td>Ospedale</td>	<td>Prenotazione visita</td> 			<td>PREN_VIS</td></tr>
	 * 	<tr><td>Ospedale</td>	<td>Pagamento Ticket</td> 				<td>PAG_TICK</td></tr>
	 * 	<tr><td>Scuola</td>		<td>Pagamento Retta</td> 				<td>PAG_RET</td></tr>
	 * 	<tr><td>Scuola</td>		<td>Pagamento servizio mensa</td> 		<td>PAG_MEN</td></tr>
	 * 	<tr><td>Scuola</td>		<td>Iscrizione</td>						<td>ISCRIZ</td></tr>
	 * 	<tr><td>Scuola</td>		<td>Colloquio Insegnanti</td>			<td>COLL_INS</td></tr>
	 * 	<tr><td>Comune</td>		<td>Admin: creazione utente</td>		<td>ADM_CREAZ_USR</td></tr>
	 * 	<tr><td>Comune</td>		<td>Admin: modifica utente</td>			<td>ADM_MOD_USR</td></tr>
	 *  <tr><td>ALL</td>		<td>Admin: gestione richieste</td>		<td>ADM_MNG_REQ</td></tr>
	 * </table>
	 *
	 */
	public enum ServiceType {
		DUMMY, 
		APP_CI, 
		CI_TEMP, 
		CAM_RES, 
		CERT_NASC, 
		CERT_MATR, 
		STAT_FAM, 
		CAM_MED, 
		PREN_VIS, 
		PAG_TICK, 
		PAG_RET, 
		PAG_MEN, 
		ISCRIZ, 
		COLL_INS, 
		ADM_CREAZ_USR, 
		ADM_MOD_USR, 
		ADM_REQ_MNG};

	public enum ServerAction {
		/**<b>LOGOUT</b> <br/>
		 * no params<br/>
		 */
		LOGOUT(1),
		
		/**<b>LOGIN</b> <br/>
		 * request: username, password<br/>
		 * response: result[OK|KO], message, isAdmin[true,false]<br/>
		 */
		LOGIN(2),
		
		/**<b>CREATE_ACCOUNT</b> - create a new account<br/>
		 * request: username, isAdmin[true,false], [user properties]<br/>
		 * response: result[OK|KO], message<br/>
		 */
		CREATE_ACCOUNT(3),
		
		/**<b>MARK_NOTIFICATION_AS_READ</b> - mark a notification as read<br/>
		 * request: notificationId <br/>
		 * response: result[OK|KO], message<br/>
		 */
		MARK_NOTIFICATION_AS_READ(4),
		
		/**TODO <b>DOWNLOAD_FILE</b> - download a specific report file<br/>
		 * request: reportId<br/>
		 * response: result[OK|KO], message<br/>
		 */
		DOWNLOAD_FILE(5),
		
		/**<b> CREATE_NEW_REQUEST</b> - create a new request to be managed<br/>
		 * request: username, isAdmin[true|false], service_type, [request data]<br/>
		 * response: result[OK|KO], message<br/>
		 */
		CREATE_NEW_REQUEST(6),
		
		/**<b> CREATE_NEW_REPORT</b> - create a new report<br/>
		 * request: username, isAdmin[true|false], service_type, [report data]<br/>
		 * response: result[OK|KO], message<br/>
		 */
		CREATE_NEW_REPORT(7),
		
		/**<b> GET_REQUEST_DATA</b> - get specific request data from the database <br/>
		 * request: username, isAdmin[true|false], requestId<br/>
		 * response: result[OK|KO], message<br/>
		 */
		GET_REQUEST_DATA(8),
		
		/**<b>GET_REPORT_DATA</b> - get specific report data from the database <<br/>
		  * request: username, isAdmin[true|false], reportId<br/>
		 * response: result[OK|KO], message<br/>
		 */
		GET_REPORT_DATA(9),
		
		/**<b>GET_USER_DATA</b> - get user data from the database <br/>
		 * request: username<br/>
		 * response: result[OK|KO], [list of the attribute in format <i>attrName=attrValue</i>]<br/>
		 */
		GET_USER_DATA(10),
		
		/**<b>GET_NOTIFICATION_LIST</b> - get the list of all notifications<br/>
		 * request: username, isAdmin[true|false], portal_type<br/>
		 * response: result[OK|KO], [notifications list]<br/>
		 */
		GET_NOTIFICATION_LIST(11),
		
		/**<b> GET_REQUEST_LIST</b> - get the list of all requests <br/>
		 * request: username, isAdmin[true,false], portal_type<br/>
		 * response: result[OK|KO], [request list]<br/>
		 */
		GET_REQUEST_LIST(12),
		
		/**<b> GET_REPORT_LIST</b> - recover the list of the reports attached to the logon user for the specific portal<br/>
		 * request: username, isAdmin[true,false], portal_type<br/>
		 * response: result[OK|KO], [reports list]<br/>
		 */
		GET_REPORT_LIST(13), 
		
		/**<b>CHECK_NEW_NOTIFICATION</b> - look for new notification on the server<br/>
		 * request: username, isAdmin[true,false], portal_type<br/>
		 * response: result[OK|KO], new_notification[true|false]<br/>
		 */
		CHECK_NEW_NOTIFICATION(14),
		
		/**TODO <b>DELETE_NOTIFICATION</b> - delete a notification from the server <br/>
		 * request: notification_id<br/>
		 * response: result[OK|KO]<br/>
		 */
		DELETE_NOTIFICATION(15), 
		
		/**<b> GET_RELATIONS</b> - get user relations list <br/>
		 * request: username<br/>
		 * response: result[OK|KO], <usernames of related users><br/>
		 */
		GET_RELATIONS(16), 

		/**<b>EDIT_ACCOUNT</b> - request to edit information related an existing user<br/>
		 * request: username, isAdmin[true,false], [user properties]<br/>
		 * response: result[OK|KO], message<br/>
		 */
		EDIT_ACCOUNT(17),

		/**<b>GET_ALL_ADM_VAL_REQ</b> - request all open request to be managed by administrators<br/>
		 * request: username, isAdmin[true,false], portal_type
		 * response: result[OK|KO], <list of records related to admin support requests>
		 */
		GET_ALL_ADM_MNG_REQ(18), 

		/**<b>ADM_MNG_REQ</b> - manage a single request<br/>
		 * request: username, isAdmin[true,false], id, acceptRequest[true,false]<br/>
		 * response: result[OK|KO], message<br/>
		 */
		ADM_MNG_REQ(19), 

		/**<b>USR_CHANGE_PASS</b> - password reset request<br/>
		 * request: username, oldPassword, newPassword<br/>
		 * response: result[OK|KO], message<br/>
		 */
		USR_CHANGE_PASS(20)
		;

		private static Map<Integer, ServerAction> internalMap = new HashMap<>();
		static {
		    for (ServerAction action : ServerAction.values()) {
		    	internalMap.put(action.getValue(), action);
		    }
		}

		private int value;
		ServerAction(int value) {
			this.value = value;
		}
		public int getValue() {
			return this.value;
		}
		public static ServerAction castIntToEnum(int value) {
		    return internalMap.get(value);
		}
	};

	public static final String COMM_SEPARATOR = "_0Y0_";
	public static final String COMM_MILTIVALUE_FIELD_SEPARATOR = "_0P0_";
	public static final String COMM_EOL = "_0E0_";
	
	public static final String SERVER_RESP_OK = "OK";
	public static final String SERVER_RESP_ERROR = "KO";
	public static final String PARAM_SEPARATOR = "_0S0_";

	
}
