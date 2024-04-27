package anagraficaCentrale.client.main;

import java.util.Arrays;

import javax.swing.JOptionPane;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import anagraficaCentrale.client.core.ConnectionManager;
import anagraficaCentrale.client.gui.ClientGui;
import anagraficaCentrale.client.gui.GUIConstants;

public class Main {
	final static Logger logger = LogManager.getRootLogger();

	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();
		args = initialization(args, startTime);
		ConnectionManager connectionManager = null;
		do{
			try {
				connectionManager = new ConnectionManager(args);
			} catch (Exception e) {
				logger.error(e);
				int ret = JOptionPane.showConfirmDialog(null, GUIConstants.LANG.lblErrorUnableToCreateConnection+"\n\n["+e.getMessage()+"]", "Errore", JOptionPane.ERROR_MESSAGE);
				if(ret == JOptionPane.NO_OPTION)
					break;
			} 
		}while(connectionManager==null);
		
		if(connectionManager == null){
			logger.error("Unable to connect. Exiting...");
			return;
		}

		new ClientGui(args, connectionManager);
		logger.info("Gui started in " + (System.currentTimeMillis()-startTime) + "ms");
	}

	private static String[] initialization(String[] args, long startTime) {
		args = Arrays.copyOf(args, args.length + 1);
		args[args.length-1] = "-startTime="+startTime;
		return args;
	}
}
