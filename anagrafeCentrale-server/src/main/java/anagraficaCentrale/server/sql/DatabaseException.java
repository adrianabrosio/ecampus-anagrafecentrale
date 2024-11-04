package anagraficaCentrale.server.sql;

public class DatabaseException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DatabaseException() {
		super("Unable to connect to the database");
	}
	
	
}
