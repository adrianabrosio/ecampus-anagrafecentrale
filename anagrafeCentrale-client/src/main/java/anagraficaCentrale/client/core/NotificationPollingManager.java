package anagraficaCentrale.client.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import anagraficaCentrale.client.gui.OperationPanel;
import anagraficaCentrale.exception.AcServerRuntimeException;

public class NotificationPollingManager implements Runnable{

	final static Logger logger = LogManager.getRootLogger();

	private OperationPanel operationPanel;
	private Thread thisThread;
	private boolean isAlive;

	public static final int POLLING_TIME = 15000;

	public NotificationPollingManager(OperationPanel operationPanel) {
		this.operationPanel = operationPanel;
		thisThread = new Thread(this);
	}

	public void stop() {
		isAlive = false;
	}

	public void start() {

		isAlive = true;
		thisThread.start();
		logger.info("Notification polling started");
	}

	@Override
	public void run() {
		while(isAlive){
			//check for new notif
			try{
				boolean newNotification = operationPanel.getConnectionManager().checkNewNotification(operationPanel.getPortalType());
				logger.debug("Updating notification status: " + newNotification);
				operationPanel.setNewNotificationIcon(newNotification);
			} catch(AcServerRuntimeException e){
				logger.error(e);
			}
			
			try {
				Thread.sleep(POLLING_TIME);
			} catch (InterruptedException e) {
				logger.error(e);
			}

		}
	}

}
