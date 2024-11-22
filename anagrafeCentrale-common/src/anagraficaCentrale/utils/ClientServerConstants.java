package anagraficaCentrale.utils;

import java.util.HashMap;
import java.util.Map;

public class ClientServerConstants {
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
	 * 	<tr><th>Portal</th>	<th>Servizio</th><th>Tecnico</th></tr>
	 * 	<tr><td>Comune</td>		<td>Appuntamento Carta d'Identità/td>	<td>APP_CI</td></tr>
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
	public enum ServiceType {DUMMY, APP_CI, CI_TEMP, CAM_RES, CERT_NASC, CERT_MATR, STAT_FAM, CAM_MED, PREN_VIS, PAG_TICK, PAG_RET, PAG_MEN, ISCRIZ, COLL_INS, ADM_CREAZ_USR, ADM_MOD_USR, ADM_REQ_MNG};

	public enum ServerAction {
		/**LOGOUT
		 * no params
		 */
		LOGOUT(1),
		
		/**LOGIN
		 * request: username, password
		 * response: result[OK|KO], message, isAdmin[true,false]
		 */
		LOGIN(2),
		
		/**CREATE_ACCOUNT
		 * request: username, <user data>
		 * response: result[OK|KO], message
		 */
		CREATE_ACCOUNT(3),
		
		/**MARK_NOTIFICATION_AS_READ
		 * request: Notification.ID
		 * response: result[OK|KO], message
		 */
		MARK_NOTIFICATION_AS_READ(4),
		
		/**DOWNLOAD_FILE
		 * request: username
		 * response: result[OK|KO], message
		 */
		DOWNLOAD_FILE(5),
		
		/**CREATE_NEW_REQUEST
		 * request: username
		 * response: result[OK|KO], message
		 */
		CREATE_NEW_REQUEST(6),
		
		/**TODO CREATE_NEW_REPORT
		 * request: username
		 * response: result[OK|KO], message
		 */
		CREATE_NEW_REPORT(7),
		
		/**GET_REQUEST_DATA
		 * request: username
		 * response: result[OK|KO], message
		 */
		GET_REQUEST_DATA(8),
		
		/**GET_USER_DATA - refresh the user data from the database
		 * request: username
		 * response: result[OK|KO], [list of the attribute in format <attrName=attrValue>]
		 */
		GET_USER_DATA(9),
		
		/**GET_NOTIFICATION_LIST
		 * request: username
		 * response: result[OK|KO], 
		 */
		GET_NOTIFICATION_LIST(10),
		
		/** GET_REQUEST_LIST
		 * request: username
		 * response: result[OK|KO], 
		 */
		GET_REQUEST_LIST(11),
		
		/** GET_REPORT_LIST
		 * request: username
		 * response: result[OK|KO], 
		 */
		GET_REPORT_LIST(12), 
		
		/** CHECK_NEW_NOTIFICATION - look for new notification on the server
		 * request: 
		 * response: result[OK|KO], new notification[true|false]
		 */
		CHECK_NEW_NOTIFICATION(13),
		/**DELETE_NOTIFICATION
		 * request: Notification.ID
		 * response: result[OK|KO], message
		 */
		DELETE_NOTIFICATION(14), 
		/**GET_RELATIONS
		 * request: username
		 * response: result[OK|KO], <relations list>
		 */
		GET_RELATIONS(15), 
		/**EDIT_ACCOUNT
		 * request: username, <user data>
		 * response: result[OK|KO], message
		 */
		EDIT_ACCOUNT(16),
		/**GET_ALL_ADM_VAL_REQ
		 * request: username, isAdmin, portalType
		 * response: result[OK|KO], <list of user request to evaluate>
		 */
		GET_ALL_ADM_MNG_REQ(17), 
		/**ADM_MNG_REQ
		 * request: username, isAdmin, id, acceptRequest
		 * response: result[OK|KO], message
		 */
		ADM_MNG_REQ(18), 
		/**USR_CHANGE_PASS
		 * request: username, oldPassword, newPassword
		 * response: result[OK|KO], message
		 */
		USR_CHANGE_PASS(19)
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
	public static final String PARAM_SEPARATOR = ",";

	
}
