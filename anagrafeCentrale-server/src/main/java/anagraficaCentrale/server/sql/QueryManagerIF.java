package anagraficaCentrale.server.sql;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;

public interface QueryManagerIF {

	public Statement getStatement() throws DatabaseException;

	public void installDatabase() throws SQLException, IOException, DatabaseException;

	public void dropAlltables() throws DatabaseException;

}
