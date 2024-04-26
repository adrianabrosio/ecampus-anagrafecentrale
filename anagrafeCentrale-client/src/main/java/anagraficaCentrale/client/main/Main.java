package anagraficaCentrale.client.main;

import java.util.Arrays;

import javax.swing.JOptionPane;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import anagraficaCentrale.client.core.ConnectionManager;
import anagraficaCentrale.client.gui.ClientGui;

public class Main {
	final static Logger logger = LogManager.getRootLogger();

	public static void main(String[] args) {
		long startTime = System.currentTimeMillis();
		args = initialization(args, startTime);
		ConnectionManager connectionManager = null;
		try {
			connectionManager = new ConnectionManager(args);
		} catch (Exception e) {
			logger.error(e);
			JOptionPane.showConfirmDialog(null, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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
