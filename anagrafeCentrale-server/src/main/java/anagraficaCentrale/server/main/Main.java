package anagraficaCentrale.server.main;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import anagraficaCentrale.server.core.AcServer;
import anagraficaCentrale.server.sql.QueryManager;
import anagraficaCentrale.utils.ScriptUtils;

public class Main {

	final static Logger logger = LogManager.getRootLogger();

	public static void main(String[] args) throws FileNotFoundException {

		if(args == null || args.length == 0){ 
			usage(); 
			return; 
		}

		boolean installDatabase = ScriptUtils.getParam(args, "-installdb") != null;
		boolean startServer = ScriptUtils.getParam(args, "-start") != null;
		String cfgFile = ScriptUtils.getParam(args, "-cfg=");
		QueryManager qm = null;
		try {
			qm = new QueryManager();
		} catch (IOException e) {
			logger.error(e);
			return;
		}

		if (installDatabase){
			try {
				qm.installDatabase();
				qm.populateDatabase();
			} catch (SQLException | IOException e) {
				logger.error("Error during DB installation", e);
			}
		}
		
		if(startServer)
			new AcServer(qm, cfgFile);
	}

	private static void usage() {
		System.out.println(
				"commands:\n -installDB: cancella tutte le tabelle e le riporta allo stato originale. Esegue gli script db_installation.sql e db_prepopulation.sql presenti tra le resources");

	}

}
