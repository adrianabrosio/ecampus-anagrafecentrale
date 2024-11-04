package anagraficaCentrale.server.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import anagraficaCentrale.server.sql.QueryManager;

public class AcServer {
	final static Logger logger = LogManager.getRootLogger();

	public QueryManager queryManager;
	public Properties prop;
	public int port;
	public ServerSocket server;

	public AcServer(QueryManager qm) {
		new AcServer(qm, null);
	}

	public AcServer(QueryManager qm, String cfgFile) {
		this.queryManager = qm;
		initialize(cfgFile);
	}

	private void initialize(String cfgFile) {
		Socket socket;
		prop = new Properties();
		try {
			// load a properties file 
			if(cfgFile != null){
				String path = new File("").getAbsolutePath() + "\\" + cfgFile;
				InputStream configFile = new FileInputStream(path);
				prop.load(configFile);
			}else{
				InputStream resourceStream = ClassLoader.getSystemResourceAsStream("database.properties");
				prop.load(resourceStream);
			}

		} catch (IOException e) {
			logger.error("Missing configuration file.\n" + e);
			return;

		}
		port = Integer.parseInt(prop.getProperty("server.port", "5563"));
		logger.info("Server started on port " + port);
		// create the socket server object
		try {
			server = new ServerSocket(port);
			server.setReuseAddress(false);//force to open new socket on disconnect
		} catch (IOException e) {
			logger.error(e);
		}
		// keep listens indefinitely until receives 'exit' call or program terminates
		while (true) {
			try{
				logger.info("Waiting for the client request..");
				socket = null;
				socket = server.accept();
				logger.debug("new request received, starting new thread");
				ServerInstance serverThread = new ServerInstance(socket, prop, queryManager);
				// Starting thread
				serverThread.start();
				logger.debug("thread " + serverThread.getName() + " started");
			}catch(IOException e){
				logger.error(e);
			}
		}
	}

}
