package anagraficaCentrale.server.core;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
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
			//String username = commArgs[1];
			boolean isAdmin = commArgs.length>=3 && commArgs[2].equalsIgnoreCase("true");
			String paramListString = commArgs.length>=4? commArgs[3] : "";
			
			//check admin
			if(!isAdmin)
				return new String[]{ClientServerConstants.SERVER_RESP_ERROR, ServerConstants.LANG.msgCommandForAdminOnly};
			//extract param list

			Map<String,String> userProps = new HashMap<>();
			for(String token : paramListString.split(ClientServerConstants.COMM_MILTIVALUE_FIELD_SEPARATOR)){
				String[] param = token.split("=");
				if(param.length != 2){
					logger.warn("param skipped: "+token);
					continue;
				}
				userProps.put(param[0], param[1]);
			}
			
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
		String notificationID = commArgs[1];
		ResultSet rs;
		try {
			rs = qm.getStatement().executeQuery("SELECT * FROM Notification WHERE id='" + notificationID + "'");
			if(!rs.first()){
				logger.info("Notification with id " + notificationID + " not found");
			} else {
				qm.getStatement().executeUpdate("UPDATE Notification SET unread=false WHERE id='" + notificationID + "'");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new String[]{ClientServerConstants.SERVER_RESP_OK};
	}

	@Override
	public String[] deleteNotificationOperation(String[] commArgs) {
		logger.info("deleting notification..");
		String notificationID = commArgs[1];
		ResultSet rs;
		try {
			rs = qm.getStatement().executeQuery("SELECT * FROM Notification WHERE id='" + notificationID + "'");
			if(!rs.first()){
				logger.info("Notification with id " + notificationID + " not found");
			} else {
				qm.getStatement().executeUpdate("DELETE FROM Notification WHERE id='" + notificationID + "'");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
		String notificationID = commArgs[1];
		ResultSet rs;
		try {
			rs = qm.getStatement().executeQuery("SELECT * FROM Notification WHERE id='" + notificationID + "'");
			if(!rs.first()){
				logger.info("Notification with id " + notificationID + " not found");
			} else {
				qm.getStatement().executeUpdate("UPDATE Notification SET unread=false WHERE id='" + notificationID + "'");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new String[]{ClientServerConstants.SERVER_RESP_OK};
	}

	@Override
	public String[] createNewReportOperation(String[] commArgs) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getRequestDataOperation(String[] commArgs) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getUserDataOperation(String[] commArgs) {
		try {
			//input
			String username = commArgs[1];
			//output
			//verify if user exists
			ResultSet rs = qm.getStatement().executeQuery("SELECT * FROM User WHERE id='" + username + "'");
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
			if(!rs.first()){
				return new String[]{ClientServerConstants.SERVER_RESP_ERROR, ServerConstants.LANG.msgLoginUserNotExists, "false"};
			}
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
			String username = commArgs[1];
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
			String username = commArgs[1];
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
}
