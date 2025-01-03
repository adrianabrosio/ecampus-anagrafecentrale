package anagraficaCentrale.client.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Arrays;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.border.MatteBorder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import anagraficaCentrale.client.core.ConnectionManager;
import anagraficaCentrale.client.core.NotificationPollingManager;
import anagraficaCentrale.client.core.SideMenuPanelManager;
import anagraficaCentrale.client.exception.UnsupportedServiceException;
import anagraficaCentrale.client.gui.component.AcScrollPane;
import anagraficaCentrale.client.gui.resource.AbstractResourceElement;
import anagraficaCentrale.client.gui.resource.AdminSupportPanel;
import anagraficaCentrale.client.gui.resource.FilterableResourcePanel;
import anagraficaCentrale.client.gui.resource.NotificationElement;
import anagraficaCentrale.client.gui.resource.ReportElement;
import anagraficaCentrale.client.gui.resource.ServicePanel;
import anagraficaCentrale.client.gui.service.GenericService;
import anagraficaCentrale.client.gui.service.ServicePanelFactory;
import anagraficaCentrale.client.gui.service.ShowProfileService;
import anagraficaCentrale.utils.ClientServerConstants.PortalType;
import anagraficaCentrale.utils.ClientServerConstants.ServiceType;
import anagraficaCentrale.utils.PDFWriter;
import anagraficaCentrale.utils.ScriptUtils;

/**
 * class that displays the main operation panel. This class is the GUI controller. 
 * It coordinates the loading of the services on the left panel and the details on the right panel
 * @author Adriana Brosio
 */
public class OperationPanel {

	private JFrame frame;
	private MiniLoadingPanel miniLoadingPanel;
	private SideMenuPanelManager sideMenuPanelManager;
	private SideMenuPanel sideMenuPanel;
	private NotificationPollingManager notificationManager;
	private JSplitPane splitPane;
	private FilterableResourcePanel leftPanel;
	private GenericService rightPanel;
	private AcScrollPane rightPanelScrollPane;
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
	
	/**
	 * method that shows the panel
	 */
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

	/**
	 * method that sets the location of the panel
	 */
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
			frame.setIconImage(ImageIO.read(ScriptUtils.getResourceAsStream(getClass(), connectionManager.isAdmin()?"iconAdmin.png":"icon.png")));
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
		Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        int width = (int) (screenSize.getWidth() * 0.8);
        int height = (int) (screenSize.getHeight() * 0.8);
		frame.setSize(new Dimension(width, height));
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
			reportPanel.addResource(new ReportElement(this, "1", "demo report 1", "demo title", "demo content", null));
			reportPanel.addResource(new ReportElement(this, "2", "demo report 2", "demo title", "demo content", null));
			reportPanel.addResource(new ReportElement(this, "3", "test report 3", "demo title", "demo content", null));
			for(int i=1; i<100; i++)
				reportPanel.addResource(new ReportElement(this, ""+i+3, "multi report "+i, "demo title"+i, "demo content"+i, null));
		}
		for(Map<String,String> record : connectionManager.getReportList(portalType)){
			logger.debug("found report ["+record.get("id")+"], content:["+record.get("file_content")+"]");
			reportPanel.addResource(new ReportElement(this, record.get("id"), record.get("file_display_name"), record.get("file_title"), record.get("file_content"), record.get("file_path")));
		}
		return reportPanel;
	}
	
	/**
	 * method to initialize the profile panel
	 */
	private GenericService initProfilePanel() {
		return new ShowProfileService(this);
	}

	/**
	 * method to initialize the admin support panel
	 */
	protected FilterableResourcePanel initAdminSupportPanel() {
		FilterableResourcePanel adminSupportPanel = new AdminSupportPanel(this, portalType, getConnectionManager().isAdmin());
		return adminSupportPanel;
	}

	/**
	 * this method initialize the notification panel
	 */
	private FilterableResourcePanel initNotificationPanel() {
		FilterableResourcePanel notificationPanel = new FilterableResourcePanel();
		/* for future implementation
		ImageIcon ic = null;
		try {
			ic = new ImageIcon(ScriptUtils.getResource("delete0.png"));
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
		*/
		for(Map<String,String> record : connectionManager.getNewNotificationList(portalType)){
			notificationPanel.addResource(new NotificationElement(this, record));
		}
		return notificationPanel;
	}

	/**
	 * method to select an element of the left panel
	 */
	public void selectOperation(AbstractResourceElement abstractResourceElement) {
		leftPanel.setSelected(abstractResourceElement);
	}
	
	/**
	 * method to open a service
	 */
	public void openService(ServiceType serviceType) {
		openService(serviceType, null);
	}

	/**
	 * this method manage the loading of the services on the right panel
	 */
	public void openService(ServiceType serviceType, Map<String, String> serviceData){
		openLoading();
		// build right panel
		try {
			rightPanel = generatePanelByService(serviceType, serviceData);
		} catch (Exception e) {
			popupError(e);
		}
		rightPanelScrollPane = new AcScrollPane(rightPanel);
		splitPane.setRightComponent(rightPanelScrollPane);
		splitPane.setDividerLocation(0.27f);
		splitPane.setDividerSize(3);
		closeLoading();
	}
	
	/**
	 * method to scroll the right panel on top (useful for long panels)
	 */
	public void scrollOnTop() {
		if(rightPanelScrollPane != null) {
			SwingUtilities.invokeLater(new Runnable() {
			    @Override
			    public void run() {
			    	rightPanelScrollPane.getViewport().setViewPosition( new Point(0, 0) );
			    }
			});
		}
	}

	/**
	 * method to close the loading panel
	 */
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
	 * @param serviceData 
	 */
	private GenericService generatePanelByService(ServiceType serviceType, Map<String, String> serviceData) throws Exception {
		logger.debug("Generating panel "+ serviceType);
		if(serviceData != null && logger.isDebugEnabled())
			logger.debug("service data: "+ serviceData);
		switch(serviceType){
		case DUMMY:
			return ServicePanelFactory.generateDummyPanel(this);
		case APP_CI    :
			return ServicePanelFactory.generateCIAppointmentPanel(this);
		case CI_TEMP   :
			return ServicePanelFactory.generateCITempPanel(this);
		case CAM_RES   :
			return ServicePanelFactory.generateChangeResidencePanel(this);
		case CERT_NASC :
			return ServicePanelFactory.generateBirthCertPanel(this);
		case CERT_MATR :
			return ServicePanelFactory.generateMarriageCertPanel(this);
		case STAT_FAM  :
			return ServicePanelFactory.generateFamilyStatusPanel(this);
		case CAM_MED   :
			return ServicePanelFactory.generateDoctorChangePanel(this);
		case PREN_VIS  :
			return ServicePanelFactory.generateMedicalAppointmentPanel(this);
		case PAG_TICK  :
			return ServicePanelFactory.generateTicketPaymentPanel(this);
		case PAG_RET   :
			return ServicePanelFactory.generateSchoolFeePaymentPanel(this);
		case PAG_MEN   :
			return ServicePanelFactory.generateCanteenPaymentPanel(this);
		case ISCRIZ    :
			return ServicePanelFactory.generateSchoolRegistrationPanel(this);
		case COLL_INS  :
			return ServicePanelFactory.generateTeacherInterviewPanel(this);
		case ADM_CREAZ_USR:
			return ServicePanelFactory.generateUserCreationPanel(this);
		case ADM_MOD_USR:
			return ServicePanelFactory.generateUserEditPanel(this);
		case ADM_REQ_MNG:
			return ServicePanelFactory.generateAdminRequestManagementPanel(this, serviceData);
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
		String lableError = "Error: "+e.getClass()+": "+e.getMessage().replace("\n", " ");
		statusLabel.setText(lableError.length()>200?lableError.substring(0, 197)+"...":lableError);
		logger.error(e.getMessage(), e);
		JTextArea textArea = new JTextArea(e.getClass()+": ["+e.getMessage()+"]");
		textArea.setFont(new Font(textArea.getFont().getFamily(), Font.BOLD, textArea.getFont().getSize()));
		textArea.setBackground(GUIConstants.BACKGROUND_COLOR_2);
		textArea.setEditable(false);
		textArea.setBorder(null);
		textArea.setLineWrap(true);  
		textArea.setWrapStyleWord(true); 
		textArea.setSize( new Dimension( 500, 200 ) );
		JScrollPane scrollPane = new JScrollPane(textArea); 
		scrollPane.setBorder(null);
		scrollPane.setSize( new Dimension( 500, 200 ) );
		JOptionPane.showMessageDialog(this.frame, scrollPane, "Error", JOptionPane.ERROR_MESSAGE );
	}
	
	/**
	 * utility method to popup an WARN message at the center of the UI and log the same message in 
	 * the log file
	 */
	public void popupWarning(Exception e) {
		String lableError = "WARN: "+e.getClass()+": "+e.getMessage().replace("\n", " ");
		statusLabel.setText(lableError.length()>200?lableError.substring(0, 197)+"...":lableError);
		logger.warn(e.getMessage(), e);
		JTextArea textArea = new JTextArea(e.getClass()+": ["+e.getMessage()+"]");
		textArea.setFont(new Font(textArea.getFont().getFamily(), Font.BOLD, textArea.getFont().getSize()));
		textArea.setBackground(GUIConstants.BACKGROUND_COLOR_2);
		textArea.setEditable(false);
		textArea.setBorder(null);
		textArea.setLineWrap(true);  
		textArea.setWrapStyleWord(true); 
		textArea.setSize( new Dimension( 500, 200 ) );
		JScrollPane scrollPane = new JScrollPane(textArea); 
		scrollPane.setBorder(null);
		scrollPane.setSize( new Dimension( 500, 200 ) );
		JOptionPane.showMessageDialog(this.frame, scrollPane, "Warning", JOptionPane.WARNING_MESSAGE );
	}
	
	/**
	 * utility method to popup an info message at the center of the UI and log the same message in 
	 * the log file
	 */
	public void popupInfo(String msg) {
		String lableError = "INFO: "+msg;
		statusLabel.setText(lableError.length()>200?lableError.substring(0, 197)+"...":lableError);
		logger.info(msg);
		JOptionPane.showMessageDialog(this.frame, msg, "Info", JOptionPane.INFORMATION_MESSAGE );
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
				subMenuLabel.setText(GUIConstants.LANG.lblNotication);

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
	 * method to open the admin support panel
	 */
	public void openAdminSupportPanelAction() {
		new SwingWorker<Integer, Integer>() {

			@Override
			protected Integer doInBackground() throws Exception {
				openLoading();
				closeService();
				leftPanel = initAdminSupportPanel();
				splitPane.setLeftComponent(leftPanel);
				subMenuLabel.setText(GUIConstants.LANG.lblAdminSupport);

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
		int result = fileChooser.showSaveDialog(this.frame);

		if (result == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();

			logger.debug("Save location: "+file.getAbsolutePath());
			try{
				PDFWriter pdfWriter = new PDFWriter(file.getAbsolutePath());
				pdfWriter.createPdfFile();
				BufferedImage image = ImageIO.read(ScriptUtils.getResourceAsStream(getClass(), "CityLogo.png"));
				pdfWriter.addPage(reportTitle, new StringBuffer(reportContent), "", Arrays.asList(image));
				String fullPathFileName = pdfWriter.saveAndClose();
				popupInfo(GUIConstants.LANG.msgPDFFileGenerated + fullPathFileName);
			}catch(Exception e){
				popupError(e);
			}
		}
	}

	/**
	 * method to download a file located on the server 
	 */
	public void downloadFile(String fileName) {
		throw new UnsupportedOperationException("downloadFile is not implemented yet");
	}

	/**
	 * method to get the size of the right panel
	 */
	public Dimension getRightPanelSize() {
		Dimension d = new Dimension(400,400);
		try {
			d = splitPane.getRightComponent().getSize();
		} catch(Exception e) {}
		return d;
	}

}
