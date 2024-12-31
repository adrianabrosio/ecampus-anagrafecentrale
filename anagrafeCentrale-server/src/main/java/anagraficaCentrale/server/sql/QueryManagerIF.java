package anagraficaCentrale.server.sql;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;

public interface QueryManagerIF {

	/**
	 * this method returns a connection to the database
	 */
	public Statement getStatement() throws DatabaseException;

	/**
	 * this method install the database. The script executed must contains all the creation statements
	 * and the constraints
	 */
	public void installDatabase() throws SQLException, IOException, DatabaseException;

	/**
	 * delete all the table in the database (also temporary tables if present)
	 */
	public void dropAlltables() throws DatabaseException;

	/**
	 * this method populate the database. The script executed must contains sample data
	 */
	public void populateDatabase() throws SQLException, IOException, DatabaseException;

}
