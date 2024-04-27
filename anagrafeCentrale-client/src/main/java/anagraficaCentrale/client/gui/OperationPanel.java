package anagraficaCentrale.client.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.border.MatteBorder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import anagraficaCentrale.client.core.ConnectionManager;
import anagraficaCentrale.client.core.NotificationPollingManager;
import anagraficaCentrale.client.core.SideMenuPanelManager;
import anagraficaCentrale.client.gui.component.AcIconButton;
import anagraficaCentrale.client.gui.component.AcScrollPane;
import anagraficaCentrale.client.gui.resource.AbstractResourceElement;
import anagraficaCentrale.client.gui.resource.FilterableResourcePanel;
import anagraficaCentrale.client.gui.resource.NotificationElement;
import anagraficaCentrale.client.gui.resource.ReportElement;
import anagraficaCentrale.client.gui.service.GenericService;
import anagraficaCentrale.client.gui.service.ServicePanel;
import anagraficaCentrale.client.gui.service.ServicePanelFactory;
import anagraficaCentrale.client.gui.service.ShowProfileService;
import anagraficaCentrale.client.gui.service.UnsupportedServiceException;
import anagraficaCentrale.client.utils.PDFWriter;
import anagraficaCentrale.utils.ClientServerConstants.PortalType;
import anagraficaCentrale.utils.ClientServerConstants.ServiceType;

public class OperationPanel {

	private JFrame frame;
	private MiniLoadingPanel miniLoadingPanel;
	private SideMenuPanelManager sideMenuPanelManager;
	private SideMenuPanel sideMenuPanel;
	private NotificationPollingManager notificationManager;
	private JSplitPane splitPane;
	private FilterableResourcePanel leftPanel;
	private GenericService rightPanel;
	private JLabel subMenuLabel, statusLabel; 

	public final Color guiBackgroundColor;
	private PortalType portalType;

	private ConnectionManager connectionManager;

	private boolean isServiceStillLoading;

	final static Logger logger = LogManager.getRootLogger();

	/**
	 * Launch the application.

	public static void main(String[] args) {
		new OperationPanel(PortalType.COMUNE, null).show();
	}*/

	public void show() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public void setLocationRelativeTo(ClientGui clientGui) {
		frame.setLocationRelativeTo(clientGui);
	}

	/**
	 * Create the application.
	 * @param connectionManager 
	 */
	public OperationPanel(PortalType pt, ConnectionManager connectionManager) {
		this.portalType = pt;
		this.connectionManager = connectionManager;
		this.guiBackgroundColor = getPortalBackgroundColor(pt);
		initialize();
	}


	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 1000, 600);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		WindowListener exitListener = new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				int confirm = JOptionPane.showOptionDialog(
						frame, GUIConstants.LANG.popupClosureDesc, 
						GUIConstants.LANG.popupClosureTitle, JOptionPane.YES_NO_OPTION, 
						JOptionPane.QUESTION_MESSAGE, null, null, null);
				if (confirm == 0) {
					try{
						notificationManager.stop();
						connectionManager.logout();
					}catch(Exception ex){
						logger.error(ex);
					}
					System.exit(0);
				}
			}
		};
		frame.addWindowListener(exitListener);
		frame.setMinimumSize(new Dimension(400, 400));

		try {
			frame.setIconImage(new ImageIcon(ClassLoader.getSystemResource(connectionManager.isAdmin()?"iconAdmin.png":"icon.png")).getImage());
		} catch (Exception e1) {
			logger.error("Unable to load \""+(connectionManager.isAdmin()?"iconAdmin.png":"icon.png")+"\" from resources", e1);
		}
		connectionManager.refreshUserData();
		frame.setTitle(GUIConstants.LANG.lblOperationPanelTitle + " - "+ getPortalTitle(portalType) +" - "+connectionManager.getUserAttribute("first_name")+" "+connectionManager.getUserAttribute("surname")+(connectionManager.isAdmin()?" [admin]":""));
		frame.getContentPane().setLayout(new BorderLayout());
		frame.getContentPane().setBackground(GUIConstants.OPERATION_PANEL_BACKGROUND);
		MatteBorder contentPaneBorder = new MatteBorder(8, 8, 8, 8, this.guiBackgroundColor);

		miniLoadingPanel = new MiniLoadingPanel();
		openLoading();

		((JPanel) frame.getContentPane()).setBorder(contentPaneBorder);

		JPanel centralPanel = new JPanel();
		centralPanel.setLayout(new BorderLayout());
		centralPanel.setBackground(GUIConstants.OPERATION_PANEL_BACKGROUND);
		subMenuLabel = new JLabel(GUIConstants.LANG.lblServizi, SwingConstants.LEFT);
		Font subMenuFont = new Font(subMenuLabel.getFont().getName(), Font.BOLD, 22);
		subMenuLabel.setFont(subMenuFont);
		subMenuLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 5));
		centralPanel.add(subMenuLabel, BorderLayout.NORTH);
		splitPane = new JSplitPane();
		splitPane.setBorder(BorderFactory.createEmptyBorder(2,2,2,2));
		splitPane.setOneTouchExpandable(true);
		//splitPane.setResizeWeight(1d);
		// build left panel
		leftPanel = initServicePanel();
		splitPane.setLeftComponent(leftPanel);
		// init right panel
		closeService();

		centralPanel.add(splitPane, BorderLayout.CENTER);
		frame.getContentPane().add(centralPanel, BorderLayout.CENTER);

		JPanel footerPanel = new JPanel();
		footerPanel.setLayout(new BorderLayout());
		footerPanel.setMaximumSize(new Dimension(2000, 40));
		footerPanel.setBorder(BorderFactory.createLineBorder(GUIConstants.OPERATION_PANEL_BACKGROUND, 5));
		statusLabel = new JLabel("Status");
		statusLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		statusLabel.setBackground(Color.WHITE);
		//statusLabel.setBorder(new MatteBorder(5, 5, 5, 5, this.guiBackgroundColor));
		statusLabel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		footerPanel.add(statusLabel, BorderLayout.CENTER);
		frame.getContentPane().add(footerPanel, BorderLayout.SOUTH);

		sideMenuPanelManager = new SideMenuPanelManager(frame);
		sideMenuPanelManager.setMain((JPanel) frame.getContentPane());
		sideMenuPanel = new SideMenuPanel(this);
		sideMenuPanelManager.setSide(sideMenuPanel);
		sideMenuPanelManager.setMainAnimation(true);
		sideMenuPanelManager.closeMenu();
		// frame.pack();
		frame.setSize(new Dimension(1200, 800));
		notificationManager = new NotificationPollingManager(this);
		notificationManager.start();
		closeLoading();
	}

	/**
	 * this method initialize the service panel
	 */
	private FilterableResourcePanel initServicePanel() {
		FilterableResourcePanel servicePanel = new ServicePanel(this, portalType, getConnectionManager().isAdmin());
		return servicePanel;
	}

	/**
	 * this method initialize the report panel
	 */
	private FilterableResourcePanel initReportPanel() {
		boolean demo=false;
		FilterableResourcePanel reportPanel = new FilterableResourcePanel();
		if(demo){
			reportPanel.addResource(new ReportElement(this, "1", "", "demo report 1", "demo title", "demo content"));
			reportPanel.addResource(new ReportElement(this, "2", "", "demo report 2", "demo title", "demo content"));
			reportPanel.addResource(new ReportElement(this, "3", "", "test report 3", "demo title", "demo content"));
			for(int i=1; i<100; i++)
				reportPanel.addResource(new ReportElement(this, ""+i+3, "", "multi report "+i, "demo title"+i, "demo content"+i));
		}
		for(Map<String,String> record : connectionManager.getReportList(portalType)){
			logger.debug("found report ["+record.get("id")+"], content:["+record.get("file_content")+"]");
			reportPanel.addResource(new ReportElement(this, record.get("id"), record.get("file_path"), record.get("file_display_name"), record.get("file_title"), record.get("file_content")));
		}
		return reportPanel;
	}

	private GenericService initProfilePanel() {
		return new ShowProfileService(this);
	}

	/**
	 * this method initialize the notification panel
	 */
	private FilterableResourcePanel initNotificationPanel() {
		FilterableResourcePanel notificationPanel = new FilterableResourcePanel();
		ImageIcon ic = null;
		try {
			ic = new ImageIcon(ClassLoader.getSystemResource("delete0.png"));
		} catch (Exception e1) {
			logger.error("Unable to load \"delete0.png\" from resources", e1);
		}
		AcIconButton deleteReadNotification = new AcIconButton(ic);
		deleteReadNotification.setText(GUIConstants.LANG.lblDeleteReadNotifBtn);
		deleteReadNotification.setForeground(Color.BLACK);
		Font f = deleteReadNotification.getFont();
		deleteReadNotification.setFont(new Font(f.getFontName(), Font.PLAIN, 12));
		deleteReadNotification.setToolTipText(GUIConstants.LANG.tooltipDeleteReadNotifBtn);
		deleteReadNotification.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				popupWarning(new UnsupportedOperationException("not implemented yet"));
			}
		});
		notificationPanel.addActionButton(deleteReadNotification);
		//notificationPanel.addResource(new NotificationElement(this, "1", "notif name", "demo desc 1"));
		for(Map<String,String> record : connectionManager.getNewNotificationList(portalType)){
			notificationPanel.addResource(new NotificationElement(this, record.get("id"), record.get("notification_name"), record.get("notification_description"), "1".equalsIgnoreCase(record.get("unread"))));
		}
		return notificationPanel;
	}

	public void selectOperation(AbstractResourceElement abstractResourceElement) {
		leftPanel.setSelected(abstractResourceElement);
	}

	/**
	 * this method manage the loading of the services on the right panel
	 */
	public void openService(ServiceType serviceType){
		openLoading();
		// build right panel
		try {
			rightPanel = generatePanelByService(serviceType);
		} catch (Exception e) {
			popupError(e);
		}
		splitPane.setRightComponent(new AcScrollPane(rightPanel));
		splitPane.setDividerSize(3);
		closeLoading();
	}


	private void closeLoading() {
		isServiceStillLoading = false;
		miniLoadingPanel.setVisible(false);
	}

	/**
	 * this method shows a loading panel if the load operations is taking too long
	 */
	private void openLoading() {
		isServiceStillLoading = true;
		new Thread(new Runnable(){
			@Override
			public void run(){
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					logger.error(e);
				}
				if(isServiceStillLoading){
					miniLoadingPanel.setVisible(true);
					miniLoadingPanel.setLocationRelativeTo(frame);
					miniLoadingPanel.setAlwaysOnTop(true);
					miniLoadingPanel.requestFocus();
				}
			}
		}).start();
	}

	/**
	 * this method closes the current opened service (right panel)
	 */
	public void closeService(){
		rightPanel = null;
		splitPane.setRightComponent(rightPanel);
		splitPane.setDividerSize(0);
	}

	/**
	 * this method invoke the ServicePanelFactory to manage the generation of the service panel
	 * based on the service called
	 */
	private GenericService generatePanelByService(ServiceType serviceType) throws UnsupportedServiceException {
		logger.debug("Generating panel "+ serviceType);
		switch(serviceType){
		case DUMMY:
			return ServicePanelFactory.generateDummyPanel(this);
		case APP_CI    :
			return ServicePanelFactory.generateCIAppointmentPanel(this);//TODO
		case CI_TEMP   :
			return ServicePanelFactory.generateCITempPanel(this);//TODO
		case CAM_DOM   :
			return ServicePanelFactory.generateChangeDomicilePanel(this);//TODO
		case CAM_RES   :
			return ServicePanelFactory.generateChangeResidencePanel(this);//TODO
		case CERT_NASC :
			return ServicePanelFactory.generateBirthCertPanel(this);//TODO
		case CERT_MATR :
			return ServicePanelFactory.generateMarriageCertPanel(this);//TODO
		case STAT_FAM  :
			return ServicePanelFactory.generateFamilyStatusPanel(this);//TODO
		case CAM_MED   :
			return ServicePanelFactory.generateDoctorChangePanel(this);//TODO
		case PREN_VIS  :
			return ServicePanelFactory.generateMedicalAppointmentPanel(this);//TODO
		case PAG_TICK  :
			return ServicePanelFactory.generateTicketPaymentPanel(this);//TODO
		case PAG_RET   :
			return ServicePanelFactory.generateSchoolFeePaymentPanel(this);//TODO
		case PAG_MEN   :
			return ServicePanelFactory.generateCanteenPaymentPanel(this);//TODO
		case ISCRIZ    :
			return ServicePanelFactory.generateSchoolRegistrationPanel(this);//TODO
		case COLL_INS  :
			return ServicePanelFactory.generateTeacherInterviewPanel(this);//TODO
		case ADM_CREAZ_USR:
			return ServicePanelFactory.generateUserCreationPanel(this);
		default:
			throw new UnsupportedServiceException(serviceType);
		}
	}

	/**
	 * returns a color based on the portal type passed as argument
	 */
	private Color getPortalBackgroundColor(PortalType portalType) {
		switch(portalType){
		case COMUNE:
			return GUIConstants.COMUNE_COLOR;
		case OSPEDALE:
			return GUIConstants.OSPEDALE_COLOR;
		case SCUOLA:
			return GUIConstants.SCUOLA_COLOR;
		default:
			throw new IllegalArgumentException("Invalid Portal type");
		}
	}

	/**
	 * returns a title based on the portal type passed as argument 
	 */
	private String getPortalTitle(PortalType portalType) {
		switch(portalType){
		case COMUNE:
			return GUIConstants.LANG.lblComuneTitle;
		case OSPEDALE:
			return GUIConstants.LANG.lblOspedaleTitle;
		case SCUOLA:
			return GUIConstants.LANG.lblScuolaTitle;
		default:
			throw new IllegalArgumentException("Invalid Portal type");
		}
	}

	/**
	 * utility method to popup an ERROR message at the center of the UI and log the same message in 
	 * the log file
	 */
	public void popupError(Exception e) {
		JOptionPane.showMessageDialog(this.frame, e.getClass()+": ["+e.getMessage()+"]", "Errore", JOptionPane.ERROR_MESSAGE );
		statusLabel.setText("Error: "+e.getClass()+": "+e.getMessage());
		logger.error(e.getMessage(), e);
	}
	
	/**
	 * utility method to popup an WARN message at the center of the UI and log the same message in 
	 * the log file
	 */
	public void popupWarning(Exception e) {
		JOptionPane.showMessageDialog(this.frame, e.getClass()+": ["+e.getMessage()+"]", "Attenzione", JOptionPane.WARNING_MESSAGE );
		statusLabel.setText("WARN: "+e.getClass()+": "+e.getMessage());
		logger.warn(e.getMessage(), e);
	}
	
	/**
	 * utility method to popup an info message at the center of the UI and log the same message in 
	 * the log file
	 */
	public void popupInfo(String msg) {
		JOptionPane.showMessageDialog(this.frame, msg, "Attenzione", JOptionPane.INFORMATION_MESSAGE );
		statusLabel.setText("INFO: "+msg);
		logger.info(msg);
	}

	/**
	 * UI action to toggle the side menu
	 */
	public void openCloseSideMenu() {
		sideMenuPanelManager.openCloseSideMenu();
	}

	/**
	 * getter for side menu panel manager
	 */
	public SideMenuPanelManager getSideMenuPanelManager(){
		return sideMenuPanelManager;
	}

	/**
	 * getter for connection manager
	 */
	public ConnectionManager getConnectionManager() {
		return connectionManager;
	}

	/**
	 * this method is attached to the left menu bar.
	 * It reset the UI closing any open panels and opens the notification panel
	 */
	public void openNotificationPanelAction(){

		new SwingWorker<Integer, Integer>() {

			@Override
			protected Integer doInBackground() throws Exception {
				openLoading();
				closeService();
				leftPanel = initNotificationPanel();
				splitPane.setLeftComponent(leftPanel);
				subMenuLabel.setText(GUIConstants.LANG.lblNotifiche);

				return 100;
			}

			@Override
			protected void done() {
				closeLoading();
			}
		}.execute();

	}

	/**
	 * this method is attached to the left menu bar.
	 * It reset the UI closing any open panels and opens the profile panel
	 */
	public void openProfilePanelAction() {
		new SwingWorker<Integer, Integer>() {

			@Override
			protected Integer doInBackground() throws Exception {
				openLoading();
				closeService();
				leftPanel = null;
				splitPane.setLeftComponent(leftPanel);
				rightPanel = initProfilePanel();
				splitPane.setRightComponent(new AcScrollPane(rightPanel));
				subMenuLabel.setText(GUIConstants.LANG.lblProfile);

				return 100;
			}

			@Override
			protected void done() {
				closeLoading();
			}
		}.execute();
	}

	/**
	 * this method is attached to the left menu bar.
	 * It reset the UI closing any open panels and opens the report panel
	 */
	public void openReportPanelAction() {
		new SwingWorker<Integer, Integer>() {

			@Override
			protected Integer doInBackground() throws Exception {
				openLoading();
				closeService();
				leftPanel = initReportPanel();
				splitPane.setLeftComponent(leftPanel);
				subMenuLabel.setText(GUIConstants.LANG.lblReport);

				return 100;
			}

			@Override
			protected void done() {
				closeLoading();
			}
		}.execute();
	}

	/**
	 * this method is attached to the left menu bar.
	 * It reset the UI closing any open panels and opens the service panel
	 */
	public void openServicePanelAction() {
		new SwingWorker<Integer, Integer>() {

			@Override
			protected Integer doInBackground() throws Exception {
				openLoading();
				closeService();
				leftPanel = initServicePanel();
				splitPane.setLeftComponent(leftPanel);
				subMenuLabel.setText(GUIConstants.LANG.lblService);

				return 100;
			}

			@Override
			protected void done() {
				closeLoading();
			}
		}.execute();
	}

	/**
	 * this method act on the UI. It changes the notification icon in case of new notifications
	 */
	public void setNewNotificationIcon(boolean isThereAnyNotification){

		new SwingWorker<Integer, Integer>() {

			@Override
			protected Integer doInBackground() throws Exception {
				sideMenuPanel.setNewNotificationIcon(isThereAnyNotification);
				return 100;
			}

			@Override
			protected void done() {

			}
		}.execute();

	}
	
	/**
	 * this method returns the portal type
	 */
	public PortalType getPortalType() {
		return portalType;
	}

	/**
	 * this method generate a PDF file starting from a title and a content.
	 * It uses PDFWriter class that is part of the Apache Pdfbox libs
	 */
	public void generateAndDownloadFile(String reportTitle, String reportContent) {
		//throw new UnsupportedOperationException("downloadFile is not implemented yet");
		JFileChooser fileChooser = new JFileChooser();
		int result = fileChooser.showSaveDialog(null);

		if (result == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();

			logger.debug("Save location: "+file.getAbsolutePath());
			try{
				PDFWriter pdfWriter = new PDFWriter(file.getAbsolutePath());
				pdfWriter.createPdfFile();
				pdfWriter.addPage(reportTitle, new StringBuffer(reportContent), null, null);
				pdfWriter.saveAndClose();
				JOptionPane.showMessageDialog(this.frame, "PDF generato in " + file.getAbsolutePath());
			}catch(Exception e){
				logger.error("Errore nella generazione del file", e);
				JOptionPane.showMessageDialog(this.frame, "Errore durante la generazione del file " + file.getAbsolutePath()+"\nErrore: " + e.getClass()+" - "+e.getMessage());
			}
		}
	}

	public void downloadFile(String fileName) {
		throw new UnsupportedOperationException("downloadFile is not implemented yet");
	}

}
