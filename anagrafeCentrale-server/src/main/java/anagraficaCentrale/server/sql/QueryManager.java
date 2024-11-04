package anagraficaCentrale.server.sql;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SuppressWarnings("unused")
public class QueryManager implements QueryManagerIF{

	final static Logger logger = LogManager.getRootLogger();

	private String dbUrl, dbName, dbUser, dbPw;

	private boolean verbose = false;
	private boolean enableUpdate = false;
	private boolean header = false;
	private boolean plain = false;
	private int fetchSize = 50;
	private int batchSize = 0;
	private int totalRecordToProcess = 0;
	private int totalRecordWithError = 0;
	private String fileSeparator = ",";
	private boolean isTrimRequired = false;

	private Statement statement;

	public QueryManager() throws IOException, DatabaseException {

		Properties prop = new Properties();
		try (InputStream resourceStream = ClassLoader.getSystemResourceAsStream("database.properties")) {
			prop.load(resourceStream);
		}

		dbUrl = prop.getProperty("db.url");
		dbName = prop.getProperty("db.name");
		dbUser = prop.getProperty("db.user");
		dbPw = prop.getProperty("db.password");
		logger.debug("sto accedendo al db con questi dati: " + dbUrl + ", user:" + dbUser + ", pw=****");

		ResultSet rs;
		try {
			rs = getStatement().executeQuery("select 1 + 2 as three;");
			if (rs.first())
				System.out.println(rs.getString(1));
			else
				logger.error("Unable to connect to Database");
		} catch (SQLException e) {
			logger.error(e);
		}

	}

	@Override
	public Statement getStatement() throws DatabaseException {
		try {
			if (statement == null || statement.isClosed()){
				Class.forName("com.mysql.cj.jdbc.Driver");
				Connection con = DriverManager.getConnection(dbUrl + dbName, dbUser, dbPw);
				int resultsetType;
				if (enableUpdate)
					resultsetType = ResultSet.CONCUR_UPDATABLE;
				else
					resultsetType = ResultSet.CONCUR_READ_ONLY;
				statement = con.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, resultsetType);
				return statement;
				// con.close();
			}
		} catch (Exception e) {
			logger.error(e);
			throw new DatabaseException();
		}
		return statement;
	}

	@Override
	public void installDatabase() throws SQLException, IOException, DatabaseException {
		// leggi script SQL dal file nelle risorse
		logger.info("Installing database..");
		BufferedReader reader = new BufferedReader(
				new InputStreamReader(ClassLoader.getSystemResource("db_installation.sql").openStream()));
		logger.info("script db_installation.sql loaded. Dropping all tables..");
		dropAlltables();
		logger.info("drop completed. Executing db_installation.sql script..");
		// eseguire lo script SQL con attenzione agli statement multiriga.
		// Scartare il ";" e i commenti
		StringBuilder statement = new StringBuilder();
		String line;
		while ((line = reader.readLine()) != null) {
			if (line.trim().startsWith("--"))
				continue; // salto commenti
			statement.append(line+' ');
			if (line.endsWith(";")) {
				Statement stmt = getStatement();
				logger.debug("Executing query: " + (statement.toString().substring(0, statement.length() - 1)));
				stmt.execute(statement.toString().substring(0, statement.length() - 1));
				statement = new StringBuilder();
			}
		}

		// warning se presenti altre linee non eseguite
		if (statement.length() > 0) {
			logger.warn("found line not executeble at EOF (missing semicolon[';']?): "+statement.toString());
		}
		logger.info("script executed successfully");
		reader.close();
	}

	public void populateDatabase() throws SQLException, IOException, DatabaseException {
		// leggi script SQL dal file nelle risorse
		logger.info("populating DB tables..");
		BufferedReader reader = new BufferedReader(
				new InputStreamReader(ClassLoader.getSystemResource("db_prepopulation.sql").openStream()));
		// eseguire lo script SQL con attenzione agli statement multiriga.
		// Scartare il ";" e i commenti
		StringBuilder statement = new StringBuilder();
		String line;
		while ((line = reader.readLine()) != null) {
			if (line.trim().startsWith("--"))
				continue; // salto commenti
			statement.append(line+' ');
			if (line.endsWith(";")) {
				Statement stmt = getStatement();
				logger.debug("Executing query: " + (statement.toString().substring(0, statement.length() - 1)));
				stmt.execute(statement.toString().substring(0, statement.length() - 1));
				statement = new StringBuilder();
			}
		}

		// warning se presenti altre linee non eseguite
		if (statement.length() > 0) {
			logger.warn("found line not executeble at EOF (missing semicolon[';']?): "+statement.toString());
		}
		logger.info("script executed successfully");
		reader.close();
	}

	@Override
	public void dropAlltables() throws DatabaseException {
		String queryModel = "SELECT GROUP_CONCAT('DROP TABLE IF EXISTS ', table_name) as query FROM information_schema.tables WHERE table_schema = '"
				+ dbName + "'";

		Statement stmt = getStatement();
		logger.debug("Executing query: " + queryModel);

		ResultSet rs;
		String[] dropList = null;
		try {
			rs = stmt.executeQuery(queryModel);
			if (rs.first())
				dropList = rs.getString(1).split(",");
			else
				logger.error("Unable to connect to Database");
		} catch (SQLException e) {
			logger.error(e);
		}

		for(String query: dropList){
			try {
				logger.debug("Executing query: "+query);
				stmt.execute(query);
			} catch (SQLException e) {
				logger.error(e);
			}
		}

	}

}
