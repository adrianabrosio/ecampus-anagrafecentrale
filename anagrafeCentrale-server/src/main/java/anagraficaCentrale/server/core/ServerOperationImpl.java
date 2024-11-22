package anagraficaCentrale.server.core;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.util.Strings;

import anagraficaCentrale.server.sql.QueryManagerIF;
import anagraficaCentrale.utils.ClientServerConstants;
import anagraficaCentrale.utils.ClientServerConstants.PortalType;

public class ServerOperationImpl implements ServerOperationIF {
	@SuppressWarnings("unused")
	private Properties prop;
	private QueryManagerIF qm;

	final static Logger logger = LogManager.getRootLogger();

	public ServerOperationImpl(Properties prop, QueryManagerIF qm) {
		this.prop=prop;
		this.qm=qm;
	}

	@Override
	public String[] loginOperation(String[] commArgs) {
		try {
			//input
			String username = commArgs[1];
			String password = commArgs[2];
			PortalType portalType = PortalType.castIntToEnum(Integer.parseInt(commArgs[3]));
			//output
			//verify if user exists
			ResultSet rs = qm.getStatement().executeQuery("SELECT * FROM User WHERE id='" + username + "'");
			if(!rs.first()){
				return new String[]{ClientServerConstants.SERVER_RESP_ERROR, ServerConstants.LANG.msgLoginUserNotExists, "false"};
			}
			//verify password
			if(!password.equals(rs.getString("password"))){
				return new String[]{ClientServerConstants.SERVER_RESP_ERROR, ServerConstants.LANG.msgWrongPassword, "false"};
			}

			//verify active user
			if(!rs.getBoolean("active")){
				return new String[]{ClientServerConstants.SERVER_RESP_ERROR, ServerConstants.LANG.msgUserInactive, "false"};
			}

			//verify role
			String auth = rs.getString("authorization");
			logger.debug("Auth string ["+auth+"], portal type ["+portalType.getValue()+"]");
			int startIndex = portalType.getValue()*2;
			String currentAuth = auth.substring(startIndex, startIndex+2);
			logger.debug("Current Auth ["+currentAuth+"]");
			if(currentAuth.equals("00")){
				return new String[]{ClientServerConstants.SERVER_RESP_ERROR, ServerConstants.LANG.msgUserUnauthorized, "false"};
			}

			String isAdmin = currentAuth.charAt(0) == '1'?"true":"false";

			return new String[]{ClientServerConstants.SERVER_RESP_OK, ServerConstants.LANG.msgLoginSuccessful, isAdmin};


		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return new String[]{ClientServerConstants.SERVER_RESP_ERROR, "Database error:\n" + e.toString()};
		}
	}

	@Override
	public String[] logoutOperation(String[] commArgs) {
		logger.info("Exiting from program");
		return new String[]{"EXIT"};
	}

	@Override
	public String[] createAccountOperation(String[] commArgs) {
		try {
			//input
			String username = commArgs.length>=2? commArgs[1]:null; //could be useful for future logging system
			boolean isAdmin = commArgs.length>=3 && commArgs[2].equalsIgnoreCase("true");
			Map<String,String> userProps = parseUserProps(commArgs.length>=4? commArgs[3] : null);

			//check admin
			if(!isAdmin)
				return new String[]{ClientServerConstants.SERVER_RESP_ERROR, ServerConstants.LANG.msgCommandForAdminOnly};

			//verify if user exists
			ResultSet rs = qm.getStatement().executeQuery("SELECT * FROM User WHERE id='" + userProps.get("id") + "'");
			if(rs.first()){
				return new String[]{ClientServerConstants.SERVER_RESP_ERROR, ServerConstants.LANG.msgUserAlreadyExists};
			}

			//verify if tax id code already used
			rs = qm.getStatement().executeQuery("SELECT * FROM User WHERE tax_id_code='" + userProps.get("tax_id_code") + "'");
			if(rs.first()){
				return new String[]{ClientServerConstants.SERVER_RESP_ERROR, ServerConstants.LANG.msgTaxIdCodeAlreadyExists};
			}

			String tableName = "User";
			String colString = Strings.join(userProps.keySet(), ',');
			String paramString = Strings.join(userProps.values(), ',');
			paramString = "'" + paramString.replace(",", "','") + "'";

			String sql = String.format("INSERT INTO %s (%s) VALUES (%s)", tableName, colString, paramString);

			logger.debug("create user query: ["+sql+"]");
			qm.getStatement().execute(sql);

			return new String[]{ClientServerConstants.SERVER_RESP_OK, ServerConstants.LANG.msgCreateUserSuccessful};


		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return new String[]{ClientServerConstants.SERVER_RESP_ERROR, "Database error:\n" + ServerConstants.LANG.msgCreateUserUnsuccessful + '\n'+ e.toString()};
		}
	}

	@Override
	public String[] markNotificationAsReadOperation(String[] commArgs) {
		logger.info("marking notification as read..");
		String notificationID = commArgs.length>=2? commArgs[1]:null;
		ResultSet rs;
		try {
			rs = qm.getStatement().executeQuery("SELECT * FROM Notification WHERE id='" + notificationID + "'");
			if(!rs.first()){
				logger.info("Notification with id " + notificationID + " not found");
			} else {
				qm.getStatement().executeUpdate("UPDATE Notification SET unread=false WHERE id='" + notificationID + "'");
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return new String[]{ClientServerConstants.SERVER_RESP_ERROR, "Database error:\n" + e.toString()};
		}
		return new String[]{ClientServerConstants.SERVER_RESP_OK};
	}

	@Override
	public String[] deleteNotificationOperation(String[] commArgs) {
		logger.info("deleting notification..");
		String notificationID = commArgs.length>=2? commArgs[1]:null;
		ResultSet rs;
		try {
			rs = qm.getStatement().executeQuery("SELECT * FROM Notification WHERE id='" + notificationID + "'");
			if(!rs.first()){
				logger.info("Notification with id " + notificationID + " not found");
			} else {
				qm.getStatement().executeUpdate("DELETE FROM Notification WHERE id='" + notificationID + "'");
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return new String[]{ClientServerConstants.SERVER_RESP_ERROR, "Database error:\n" + e.toString()};
		}
		return new String[]{ClientServerConstants.SERVER_RESP_OK};
	}

	@Override
	public String[] downloadFileOperation(String[] commArgs) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] createNewRequestOperation(String[] commArgs) {
		logger.info("creating new request..");
		String username = commArgs.length>=2? commArgs[1]:null;
		boolean isAdmin = commArgs.length>=3 && commArgs[2].equalsIgnoreCase("true");
		String serviceType = commArgs.length>=4? commArgs[3]:null;
		Map<String,String> userProps = parseUserProps(commArgs.length>=5? commArgs[4] : null);
		logger.debug("Request params:\nusername: " + username + "\nisAdmin: " + isAdmin + "\nserviceType: " + serviceType + "\nuserProps: " + userProps);

		if(username == null || serviceType == null) {
			return new String[]{ClientServerConstants.SERVER_RESP_ERROR, };
		}

		//TODO finire di implementare
		//record: id, portal_type, creator_user_id, manager_user_id, request_type, request_name, request_description, request_parameters

		String tableName = "Request";
		//String colString = Strings.join(userProps.keySet(), ',');
		//String paramString = Strings.join(userProps.values(), ',');
		//paramString = "'" + paramString.replace(",", "','") + "'";
		StringBuilder colStringBuilder = new StringBuilder();
		StringBuilder valueStringBuilder = new StringBuilder();
		StringBuilder paramStringBuilder = new StringBuilder();
		for (String key : userProps.keySet()) {
			if(		   key.equals("portal_type") 
					|| key.equals("creator_user_id") 
					|| key.equals("request_type")
					|| key.equals("request_name")
					|| key.equals("request_description")
					) {
				colStringBuilder.append(key).append(",");
				valueStringBuilder.append(userProps.get(key)).append("','");
			}
			else {
				if(paramStringBuilder.length()>0) paramStringBuilder.append(ClientServerConstants.PARAM_SEPARATOR);
				paramStringBuilder.append(key + "=" + userProps.get(key));
			}
		}

		if(!colStringBuilder.toString().contains("request_name")) {
			colStringBuilder.append("request_name").append(",");
			valueStringBuilder.append("','");
		}

		if(!colStringBuilder.toString().contains("request_description")) {
			colStringBuilder.append("request_description").append(",");
			valueStringBuilder.append("','");
		}

		colStringBuilder.append("request_parameters");
		valueStringBuilder.append(paramStringBuilder.toString());

		String colString = colStringBuilder.toString();
		String valueString = "'" + valueStringBuilder.toString() + "'";
		String sql = String.format("INSERT INTO %s (%s) VALUES (%s)", tableName, colString, valueString);
		try {
			logger.debug("create request query: ["+sql+"]");
			qm.getStatement().execute(sql);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return new String[]{ClientServerConstants.SERVER_RESP_ERROR, "Database error:\n" + e.toString()};
		}

		return new String[]{ClientServerConstants.SERVER_RESP_OK};
	}

	private Map<String, String> parseUserProps(String toBeParsed) {
		if(toBeParsed == null)
			return new HashMap<String,String>();
		HashMap<String,String> userParams = new HashMap<>();
		for(String token : toBeParsed.split(ClientServerConstants.COMM_MILTIVALUE_FIELD_SEPARATOR)){
			String[] param = token.split("=");
			if(param.length != 2){
				logger.warn("param skipped: "+token);
				continue;
			}
			userParams.put(param[0], param[1]);
		}
		return userParams;
	}

	@Override
	public String[] createNewReportOperation(String[] commArgs) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getRequestDataOperation(String[] commArgs) {
		return getGenericDataOperation(commArgs, "Request");
	}
	
	@Override
	public String[] getReportDataOperation(String[] commArgs) {
		return getGenericDataOperation(commArgs, "Report");
	}
	
	public String[] getGenericDataOperation(String[] commArgs, String tableName) {
		try {
			@SuppressWarnings("unused")
			String username = commArgs[1];
			@SuppressWarnings("unused")
			boolean isAdmin = commArgs.length>=3 && commArgs[2].equalsIgnoreCase("true");
			String id = commArgs.length>=4 ? commArgs[3]:null;
			
			//getRequestData
			ResultSet rs = qm.getStatement().executeQuery("SELECT * FROM " + tableName + " WHERE id='" + id + "'");
			if(!rs.first()){
				return new String[]{ClientServerConstants.SERVER_RESP_ERROR, ServerConstants.LANG.msgElementNotExists};
			}
			
			ArrayList<String> attrList = new ArrayList<>();
			attrList.add(ClientServerConstants.SERVER_RESP_OK); //op return value
			
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnsNumber = rsmd.getColumnCount();
			rs.beforeFirst();
			while(rs.next()){
				StringBuilder sb = new StringBuilder();
				for (int i = 1; i <= columnsNumber; i++) {
					String columnValue = rs.getString(i);
					sb.append(rsmd.getColumnName(i) + "=" + columnValue);
					if(i < columnsNumber)
						sb.append(ClientServerConstants.COMM_MILTIVALUE_FIELD_SEPARATOR);
				}
				attrList.add(sb.toString());
			}

			return attrList.toArray(new String[0]);
			
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return new String[]{ClientServerConstants.SERVER_RESP_ERROR, "Database error:\n" + e.toString()};
		}
	}

	@Override
	public String[] getUserDataOperation(String[] commArgs) {
		try {
			//input
			String username = commArgs[1];
			//output
			//verify if user exists
			ResultSet rs = qm.getStatement().executeQuery("SELECT * FROM User WHERE id='" + username + "' OR tax_id_code='" + username + "'");
			if(!rs.first()){
				return new String[]{ClientServerConstants.SERVER_RESP_ERROR, ServerConstants.LANG.msgLoginUserNotExists, "false"};
			}
			ArrayList<String> attrList = new ArrayList<>();
			attrList.add(ClientServerConstants.SERVER_RESP_OK); //op return value

			ResultSetMetaData rsmd = rs.getMetaData();
			int columnsNumber = rsmd.getColumnCount();
			for (int i = 1; i <= columnsNumber; i++) {
				String columnValue = rs.getString(i);
				attrList.add(rsmd.getColumnName(i) + "=" + columnValue);
			}

			return attrList.toArray(new String[0]);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return new String[]{ClientServerConstants.SERVER_RESP_ERROR, "Database error:\n" + e.toString()};
		}
	}

	@Override
	public String[] getRelationsDataOperation(String[] commArgs) {
		try {
			//input
			String username = commArgs[1];
			//output
			//verify if user exists
			ResultSet rs = qm.getStatement().executeQuery("SELECT * FROM Relationship WHERE `primary`='" + username + "' AND degree IN ('madre', 'padre')");
			//fix - a user may not have relations
			//if(!rs.first()){
			//	return new String[]{ClientServerConstants.SERVER_RESP_ERROR, ServerConstants.LANG.msgLoginUserNotExists, "false"};
			//}
			ArrayList<String> attrList = new ArrayList<>();
			attrList.add(ClientServerConstants.SERVER_RESP_OK); //op return value

			rs.beforeFirst();
			while(rs.next()){
				attrList.add(rs.getString("secondary"));
			}

			return attrList.toArray(new String[0]);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return new String[]{ClientServerConstants.SERVER_RESP_ERROR, "Database error:\n" + e.toString()};
		}
	}

	@Override
	public String[] getNotificationListOperation(String[] commArgs) {
		try {
			//input
			String username = commArgs.length>=2? commArgs[1]:null;
			boolean isAdmin = commArgs.length>=3 && commArgs[2].equalsIgnoreCase("true");
			String portalType = commArgs.length>=4 ? commArgs[3]:null;

			String tmpQuery;
			if(!isAdmin){
				tmpQuery = 
						"SELECT * FROM (SELECT n.* "
								+ "FROM User u INNER JOIN Request req on req.creator_user_id=u.id "
								+ "INNER JOIN Notification n on n.request_id=req.id "
								+ "WHERE u.id='!userid' AND req.portal_type='!portal'"
								+ "UNION ALL  "
								+ "SELECT n.* "
								+ "FROM User u INNER JOIN Report rep on rep.user_id=u.id "
								+ "INNER JOIN Notification n on n.report_id=rep.id "
								+ "WHERE u.id='!userid' AND rep.portal_type='!portal') a "
								+ "ORDER BY a.id DESC";
			} else {
				tmpQuery = 
						"SELECT * FROM (SELECT n.* "
								+ "FROM User u INNER JOIN Request req on req.creator_user_id=u.id "
								+ "INNER JOIN Notification n on n.request_id=req.id "
								+ "WHERE u.id='!userid' AND req.portal_type='!portal'"
								+ "UNION ALL  "
								+ "SELECT n.* "
								+ "FROM User u INNER JOIN Report rep on rep.user_id=u.id "
								+ "INNER JOIN Notification n on n.report_id=rep.id "
								+ "WHERE u.id='!userid' AND rep.portal_type='!portal'"
								+ "UNION ALL  "
								+ "SELECT n.* "
								+ "FROM Request req "
								+ "INNER JOIN Notification n on n.report_id=req.id "
								+ "WHERE req.manager_user_id IS NULL AND req.portal_type='!portal') a "
								+ "ORDER BY a.id DESC";
			}
			ResultSet rs = qm.getStatement().executeQuery(tmpQuery.replaceAll("!userid", username).replaceAll("!portal", portalType));
			ArrayList<String> attrList = new ArrayList<>();
			attrList.add(ClientServerConstants.SERVER_RESP_OK); //op return value

			ResultSetMetaData rsmd = rs.getMetaData();
			int columnsNumber = rsmd.getColumnCount();
			rs.beforeFirst();
			while(rs.next()){
				StringBuilder sb = new StringBuilder();
				for (int i = 1; i <= columnsNumber; i++) {
					String columnValue = rs.getString(i);
					sb.append(rsmd.getColumnName(i) + "=" + columnValue);
					if(i < columnsNumber)
						sb.append(ClientServerConstants.COMM_MILTIVALUE_FIELD_SEPARATOR);
				}
				attrList.add(sb.toString());
			}

			return attrList.toArray(new String[0]);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return new String[]{ClientServerConstants.SERVER_RESP_ERROR, "Database error:\n" + e.toString()};
		}
	}

	@Override
	public String[] getRequestListOperation(String[] commArgs) {
		try {
			//input
			String username = commArgs.length>=2? commArgs[1]:null;
			@SuppressWarnings("unused")
			boolean isAdmin = commArgs.length>=3 && commArgs[2].equalsIgnoreCase("true");
			String portalType = commArgs.length>=4 ? commArgs[3]:null;

			String tmpQuery;

			tmpQuery = 
					"SELECT * "
							+ "FROM Request req "
							+ "WHERE req.manager_user_id IS NULL AND req.portal_type='!portal'";

			ResultSet rs = qm.getStatement().executeQuery(tmpQuery.replaceAll("!portal", portalType));
			ArrayList<String> attrList = new ArrayList<>();
			attrList.add(ClientServerConstants.SERVER_RESP_OK); //op return value

			ResultSetMetaData rsmd = rs.getMetaData();
			int columnsNumber = rsmd.getColumnCount();
			rs.beforeFirst();
			while(rs.next()){
				StringBuilder sb = new StringBuilder();
				for (int i = 1; i <= columnsNumber; i++) {
					String columnValue = rs.getString(i);
					sb.append(rsmd.getColumnName(i) + "=" + columnValue);
					if(i < columnsNumber)
						sb.append(ClientServerConstants.COMM_MILTIVALUE_FIELD_SEPARATOR);
				}
				attrList.add(sb.toString());
			}

			return attrList.toArray(new String[0]);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return new String[]{ClientServerConstants.SERVER_RESP_ERROR, "Database error:\n" + e.toString()};
		}
	}

	@Override
	public String[] getReportListOperation(String[] commArgs) {
		try {
			//input
			String username = commArgs[1];
			@SuppressWarnings("unused")
			boolean isAdmin = commArgs.length>=3 && commArgs[2].equalsIgnoreCase("true");
			String portalType = commArgs.length>=4 ? commArgs[3]:null;

			String tmpQuery;

			tmpQuery = 
					"SELECT * "
							+ "FROM Report rep "
							+ "WHERE rep.user_id='!userid' AND rep.portal_type='!portal'";

			ResultSet rs = qm.getStatement().executeQuery(tmpQuery.replaceAll("!userid", username).replaceAll("!portal", portalType));
			ArrayList<String> attrList = new ArrayList<>();
			attrList.add(ClientServerConstants.SERVER_RESP_OK); //op return value

			ResultSetMetaData rsmd = rs.getMetaData();
			int columnsNumber = rsmd.getColumnCount();
			rs.beforeFirst();
			while(rs.next()){
				StringBuilder sb = new StringBuilder();
				for (int i = 1; i <= columnsNumber; i++) {
					String columnValue = rs.getString(i);
					sb.append(rsmd.getColumnName(i) + "=" + columnValue);
					if(i < columnsNumber)
						sb.append(ClientServerConstants.COMM_MILTIVALUE_FIELD_SEPARATOR);
				}
				attrList.add(sb.toString());
			}

			return attrList.toArray(new String[0]);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return new String[]{ClientServerConstants.SERVER_RESP_ERROR, "Database error:\n" + e.toString()};
		}
	}

	@Override
	public String[] checkNewNotificationOperation(String[] commArgs) {
		try {
			//input
			String username = commArgs[1];
			boolean isAdmin = commArgs.length>=3 && commArgs[2].equalsIgnoreCase("true");
			String portalType = commArgs.length>=4 ? commArgs[3]:null;
			String tmpQuery;
			if(!isAdmin){
				tmpQuery = 
						"SELECT 1 "
								+ "FROM User u INNER JOIN Request req on req.creator_user_id=u.id "
								+ "INNER JOIN Notification n on n.request_id=req.id "
								+ "WHERE u.id='!userid' AND n.unread=true AND req.portal_type='!portal' "
								+ "UNION ALL  "
								+ "SELECT 1 "
								+ "FROM User u INNER JOIN Report rep on rep.user_id=u.id "
								+ "INNER JOIN Notification n on n.report_id=rep.id "
								+ "WHERE u.id='!userid' AND n.unread=true AND rep.portal_type='!portal' ";
			} else {
				tmpQuery = 
						"SELECT 1 "
								+ "FROM User u INNER JOIN Request req on req.creator_user_id=u.id "
								+ "INNER JOIN Notification n on n.request_id=req.id "
								+ "WHERE u.id='!userid' AND n.unread=true AND req.portal_type='!portal' "
								+ "UNION ALL  "
								+ "SELECT 1 "
								+ "FROM User u INNER JOIN Report rep on rep.user_id=u.id "
								+ "INNER JOIN Notification n on n.report_id=rep.id "
								+ "WHERE u.id='!userid' AND n.unread=true AND rep.portal_type='!portal' "
								+ "UNION ALL  "
								+ "SELECT 1 "
								+ "FROM Request req "
								+ "INNER JOIN Notification n on n.report_id=req.id "
								+ "WHERE req.manager_user_id IS NULL AND n.unread=true AND req.portal_type='!portal' ";
			}
			ResultSet rs = qm.getStatement().executeQuery(tmpQuery.replaceAll("!userid", username).replaceAll("!portal", portalType));
			boolean notificationFound = rs.first();
			return new String[]{ClientServerConstants.SERVER_RESP_OK, ""+notificationFound};
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return new String[]{ClientServerConstants.SERVER_RESP_ERROR, "Database error:\n" + e.toString()};
		}
	}

	@Override
	public String[] editAccountOperation(String[] commArgs) {
		try {
			//input
			@SuppressWarnings("unused")
			String username = commArgs.length>=2? commArgs[1]:null; //could be useful for future logging system
			boolean isAdmin = commArgs.length>=3 && commArgs[2].equalsIgnoreCase("true");
			Map<String,String> userProps = parseUserProps(commArgs.length>=4? commArgs[3] : null);
			String updateParams = commArgs.length>=4? commArgs[3] : null;

			//check admin
			if(!isAdmin)
				return new String[]{ClientServerConstants.SERVER_RESP_ERROR, ServerConstants.LANG.msgCommandForAdminOnly};

			//verify if user exists
			ResultSet rs = qm.getStatement().executeQuery("SELECT * FROM User WHERE id='" + userProps.get("id") + "'");
			if(!rs.first()){
				return new String[]{ClientServerConstants.SERVER_RESP_ERROR, ServerConstants.LANG.msgUserNotExists};
			}

			if(updateParams == null) {
				return new String[]{ClientServerConstants.SERVER_RESP_ERROR, ServerConstants.LANG.msgInvalidUpdateParams};
			}

			String tableName = "User";
			//String colString = Strings.join(userProps.keySet(), ',');
			//String paramString = Strings.join(userProps.values(), ',');
			//paramString = "'" + paramString.replace(",", "','") + "'";
			
			updateParams = "'" + updateParams.replace(ClientServerConstants.COMM_MILTIVALUE_FIELD_SEPARATOR, "','") + "'";

			String sql = String.format("UPDATE %s SET (%s) WHERE id='" + userProps.get("id") + "'", tableName, updateParams);

			logger.debug("update user query: ["+sql+"]");
			qm.getStatement().execute(sql);

			return new String[]{ClientServerConstants.SERVER_RESP_OK, ServerConstants.LANG.msgUpdateUserSuccessful};


		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return new String[]{ClientServerConstants.SERVER_RESP_ERROR, "Database error:\n" + ServerConstants.LANG.msgInvalidUpdateParams + '\n'+ e.toString()};
		}
	}

	@Override
	public String[] getAllAdminSupportRequestsOperation(String[] commArgs) {
		try {
			//input
			String username = commArgs[1];
			boolean isAdmin = commArgs.length>=3 && commArgs[2].equalsIgnoreCase("true");
			String portalType = commArgs.length>=4 ? commArgs[3]:null;

			if(!isAdmin) {
				logger.warn("\"getAllAdminSupportRequestsOperation\" request sent from user without administration permission. Possible programming error.");
				return new String[]{ClientServerConstants.SERVER_RESP_ERROR, "User unauthorized"};
			}

			String tmpQuery =  "SELECT * "
					+ "FROM Request req "
					+ "WHERE req.manager_user_id IS NULL AND req.portal_type='!portal' "; //req.manager_user_id IS NULL means that request is not managed

			ResultSet rs = qm.getStatement().executeQuery(tmpQuery.replaceAll("!userid", username).replaceAll("!portal", portalType));
			ArrayList<String> attrList = new ArrayList<>();
			attrList.add(ClientServerConstants.SERVER_RESP_OK); //op return value

			ResultSetMetaData rsmd = rs.getMetaData();
			int columnsNumber = rsmd.getColumnCount();
			rs.beforeFirst();
			while(rs.next()){
				StringBuilder sb = new StringBuilder();
				for (int i = 1; i <= columnsNumber; i++) {
					String columnValue = rs.getString(i);
					sb.append(rsmd.getColumnName(i) + "=" + columnValue);
					if(i < columnsNumber)
						sb.append(ClientServerConstants.COMM_MILTIVALUE_FIELD_SEPARATOR);
				}
				attrList.add(sb.toString());
			}

			return attrList.toArray(new String[0]);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return new String[]{ClientServerConstants.SERVER_RESP_ERROR, "Database error:\n" + e.toString()};
		}
	}

	@Override
	public String[] manageRequestOperation(String[] commArgs) {
		try {
			String username = commArgs[1];
			boolean isAdmin = commArgs.length>=3 && commArgs[2].equalsIgnoreCase("true");
			String requestID = commArgs.length>=4 ? commArgs[3]:null;
			boolean acceptRequest = commArgs.length>=5 && commArgs[4].equalsIgnoreCase("true");
			
			//check admin
			if(!isAdmin)
				return new String[]{ClientServerConstants.SERVER_RESP_ERROR, ServerConstants.LANG.msgCommandForAdminOnly};

			//verify if user exists
			ResultSet rs = qm.getStatement().executeQuery("SELECT * FROM Request WHERE id='" + requestID + "'");
			if(!rs.first()){
				return new String[]{ClientServerConstants.SERVER_RESP_ERROR, ServerConstants.LANG.msgUserNotExists};
			}
			String requestId = rs.getString("id");
			String requestType = rs.getString("request_type");
			String portalType = rs.getString("portal_type");
			String requestParams = rs.getString("request_parameters");

			//update request
			String sql = "UPDATE Request SET manager_user_id='" + username + "', "
					                      + "request_parameters='"+requestParams + ClientServerConstants.PARAM_SEPARATOR + "status=" + acceptRequest +"' "
					                      + "WHERE id='" + requestID + "'";

			logger.debug("update user query: ["+sql+"]");
			qm.getStatement().execute(sql);
			
			//send notification to the user
			String tableName = "Notification";
			String colString = "portal_type, request_id, notification_type, notification_name, notification_description";
			String paramString = portalType + ',' + requestId + ',' + requestType + ',' + "" + ',' + "accepted=" + (acceptRequest?"Y":"N");
			paramString = "'" + paramString.replace(",", "','") + "'";

			String sql2 = String.format("INSERT INTO %s (%s) VALUES (%s)", tableName, colString, paramString);

			logger.debug("create user query: ["+sql2+"]");
			qm.getStatement().execute(sql2);
			
			return new String[]{ClientServerConstants.SERVER_RESP_OK};
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return new String[]{ClientServerConstants.SERVER_RESP_ERROR, "Database error:\n" + e.toString()};
		}
	}

	@Override
	public String[] passwordResetOperation(String[] commArgs) {
		try {
			//input
			String username = commArgs[1];
			String oldPassword = commArgs.length>=3 ? commArgs[2]:null;
			String newPassword = commArgs.length>=4 ? commArgs[3]:null;

			//verify if user exists
			ResultSet rs = qm.getStatement().executeQuery("SELECT * FROM User WHERE id='" + username + "'");
			if(!rs.first()){
				return new String[]{ClientServerConstants.SERVER_RESP_ERROR, ServerConstants.LANG.msgUserNotExists};
			}
			
			//check old password
			if(!oldPassword.equals(rs.getString("password"))){
				return new String[]{ClientServerConstants.SERVER_RESP_ERROR, ServerConstants.LANG.msgWrongPassword};
			}
			
			//update user
			String sql = "UPDATE User SET password='" + newPassword + "' WHERE id='" + username + "'";

			logger.debug("update user query: ["+sql+"]");
			qm.getStatement().execute(sql);
			
			return new String[]{ClientServerConstants.SERVER_RESP_OK};
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return new String[]{ClientServerConstants.SERVER_RESP_ERROR, "Database error:\n" + e.toString()};
		}
	}
}
