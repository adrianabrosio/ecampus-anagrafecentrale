package anagraficaCentrale.client.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import anagraficaCentrale.client.gui.component.AcIconButton;
import anagraficaCentrale.utils.ScriptUtils;

public class SideMenuPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final static Logger logger = LogManager.getRootLogger();

	private ArrayList<JButton> sideButtonList;
	private HashMap<JButton, String> buttonNameMap;
	private JPanel mainPanel;
	private JButton notificationButton;

	private boolean debugButtonBorder = false;

	public SideMenuPanel(OperationPanel operationPanel) {
		setBackground(operationPanel.guiBackgroundColor);
		setLayout(new BorderLayout());
		sideButtonList = new ArrayList<>();
		buttonNameMap = new HashMap<>();

		mainPanel = new JPanel();
		mainPanel.setAlignmentX(LEFT_ALIGNMENT);
		mainPanel.setBackground(operationPanel.guiBackgroundColor);
		//mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		//mainPanel.setLayout(new GridLayout(10, 1));
		mainPanel.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.NONE;
		gbc.anchor = GridBagConstraints.WEST;
		gbc.gridwidth = GridBagConstraints.REMAINDER;
		gbc.ipady = 10;
		gbc.insets = new Insets(4, 4, 4, 4);
		gbc.weightx = 1;
		JButton accountButton = generateAccountButton(operationPanel);
		sideButtonList.add(accountButton);
		buttonNameMap.put(accountButton, GUIConstants.LANG.lblAccountBtnSide);
		mainPanel.add(accountButton, gbc);

		JButton serviceButton = generateServiceButton(operationPanel);
		sideButtonList.add(serviceButton);
		buttonNameMap.put(serviceButton, GUIConstants.LANG.lblServiceBtnSide);
		mainPanel.add(serviceButton, gbc);

		if(operationPanel.getConnectionManager().isAdmin()){
			JButton adminSupportButton = generateAdminSupportButton(operationPanel);
			sideButtonList.add(adminSupportButton);
			buttonNameMap.put(adminSupportButton, GUIConstants.LANG.lblAdminSupportButtonBtnSide);
			mainPanel.add(adminSupportButton, gbc);
		}

		notificationButton = generateNotificationButton(operationPanel);
		sideButtonList.add(notificationButton);
		buttonNameMap.put(notificationButton, GUIConstants.LANG.lblNotificationBtnSide);
		mainPanel.add(notificationButton, gbc);

		JButton reportButton = generateReportButton(operationPanel);
		sideButtonList.add(reportButton);
		buttonNameMap.put(reportButton, GUIConstants.LANG.lblReportBtnSide);
		mainPanel.add(reportButton, gbc);

		add(mainPanel, BorderLayout.NORTH);

		JPanel lowerPanel = new JPanel();
		lowerPanel.setLayout(new GridBagLayout());
		lowerPanel.setBackground(operationPanel.guiBackgroundColor);
		JButton expandButton = generateExpandButton(operationPanel);
		sideButtonList.add(expandButton);
		buttonNameMap.put(expandButton, GUIConstants.LANG.lblCollapseBtnSide);
		lowerPanel.add(expandButton, gbc);
		add(lowerPanel, BorderLayout.AFTER_LAST_LINE);
	}

	private JButton generateAdminSupportButton(OperationPanel operationPanel) {
		ImageIcon adminSupportIcon = null;
		try {
			BufferedImage bi = ImageIO.read(ScriptUtils.getResourceAsStream(getClass(), "adminsupport0.png"));
			adminSupportIcon = new ImageIcon(bi.getScaledInstance(40, 40, Image.SCALE_DEFAULT));
		} catch (Exception e1) {
			logger.error("Unable to load \"adminsupport0.png\" from resources", e1);
		}
		AcIconButton adminSupportButton = new AcIconButton(adminSupportIcon);
		if(debugButtonBorder)
			adminSupportButton.setBorder(BorderFactory.createLineBorder(Color.RED));
		adminSupportButton.setBorderPainted(true);
		adminSupportButton.setAlignmentX(Component.LEFT_ALIGNMENT);
		adminSupportButton.setToolTipText(GUIConstants.LANG.lblAdminSupportButtonBtnSide);
		adminSupportButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				closePanel(operationPanel.getSideMenuPanelManager().getMinWidth());
				operationPanel.getSideMenuPanelManager().closeMenu();
				operationPanel.openAdminSupportPanelAction();
			}
		});
		return adminSupportButton;
	}

	private JButton generateNotificationButton(OperationPanel operationPanel) {
		ImageIcon notificationIcon = null;
		try {
			BufferedImage bi = ImageIO.read(ScriptUtils.getResourceAsStream(getClass(), "notificationOff0.png"));
			notificationIcon = new ImageIcon(bi.getScaledInstance(40, 40, Image.SCALE_DEFAULT));
		} catch (Exception e1) {
			logger.error("Unable to load \"notificationOff0.png\" from resources", e1);
		}
		AcIconButton notificationButton = new AcIconButton(notificationIcon);
		if(debugButtonBorder)
			notificationButton.setBorder(BorderFactory.createLineBorder(Color.RED));
		notificationButton.setBorderPainted(true);
		notificationButton.setAlignmentX(Component.LEFT_ALIGNMENT);
		notificationButton.setToolTipText(GUIConstants.LANG.lblNotificationBtnSide);
		notificationButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				closePanel(operationPanel.getSideMenuPanelManager().getMinWidth());
				operationPanel.getSideMenuPanelManager().closeMenu();
				operationPanel.openNotificationPanelAction();
				//operationPanel.openCloseSideMenu();
			}
		});
		return notificationButton;
	}

	private JButton generateReportButton(OperationPanel operationPanel) {
		ImageIcon reportIcon = null;
		try {
			BufferedImage bi = ImageIO.read(ScriptUtils.getResourceAsStream(getClass(), "report0.png"));
			reportIcon = new ImageIcon(bi.getScaledInstance(40, 40, Image.SCALE_DEFAULT));
		} catch (Exception e1) {
			logger.error("Unable to load \"report0.png\" from resources", e1);
		}
		AcIconButton reportButton = new AcIconButton(reportIcon);
		if(debugButtonBorder)
			reportButton.setBorder(BorderFactory.createLineBorder(Color.RED));
		reportButton.setBorderPainted(true);
		reportButton.setAlignmentX(Component.LEFT_ALIGNMENT);
		reportButton.setToolTipText(GUIConstants.LANG.lblReportBtnSide);
		reportButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				closePanel(operationPanel.getSideMenuPanelManager().getMinWidth());
				operationPanel.getSideMenuPanelManager().closeMenu();
				operationPanel.openReportPanelAction();
				//operationPanel.openCloseSideMenu();
			}
		});
		return reportButton;
	}

	private JButton generateServiceButton(OperationPanel operationPanel) {
		ImageIcon serviceIcon = null;
		try {
			BufferedImage bi = ImageIO.read(ScriptUtils.getResourceAsStream(getClass(), "service0.png"));
			serviceIcon = new ImageIcon(bi.getScaledInstance(40, 40, Image.SCALE_DEFAULT));
		} catch (Exception e1) {
			logger.error("Unable to load \"service0.png\" from resources", e1);
		}
		AcIconButton serviceButton = new AcIconButton(serviceIcon);
		if(debugButtonBorder)
			serviceButton.setBorder(BorderFactory.createLineBorder(Color.RED));
		serviceButton.setBorderPainted(true);
		serviceButton.setAlignmentX(Component.LEFT_ALIGNMENT);
		serviceButton.setToolTipText(GUIConstants.LANG.lblServiceBtnSide);
		serviceButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				closePanel(operationPanel.getSideMenuPanelManager().getMinWidth());
				operationPanel.getSideMenuPanelManager().closeMenu();
				operationPanel.openServicePanelAction();
			}
		});
		return serviceButton;
	}

	private JButton generateAccountButton(OperationPanel operationPanel) {
		ImageIcon loginIcon = null;
		try {
			BufferedImage bi;
			if(operationPanel.getConnectionManager() == null || operationPanel.getConnectionManager().isAdmin())
				bi = ImageIO.read(ScriptUtils.getResourceAsStream(getClass(), "accountAdmin0.png"));
			else
				bi = ImageIO.read(ScriptUtils.getResourceAsStream(getClass(), "account0.png"));
			loginIcon = new ImageIcon(bi.getScaledInstance(40, 40, Image.SCALE_DEFAULT));
		} catch (Exception e1) {
			logger.error("Unable to load \"account0.png\" from resources", e1);
		}
		AcIconButton accountButton = new AcIconButton(loginIcon);
		if(debugButtonBorder)
			accountButton.setBorder(BorderFactory.createLineBorder(Color.RED));
		accountButton.setBorderPainted(true);
		accountButton.setAlignmentX(Component.LEFT_ALIGNMENT);
		accountButton.setToolTipText(GUIConstants.LANG.lblAccountBtnSide);
		// Aggiungi un listener al bottone
		accountButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				closePanel(operationPanel.getSideMenuPanelManager().getMinWidth());
				operationPanel.getSideMenuPanelManager().closeMenu();
				operationPanel.openProfilePanelAction();
			}
		});
		return accountButton;
	}

	private JButton generateExpandButton(OperationPanel operationPanel) {
		ImageIcon expandIcon = null;
		try {
			BufferedImage bi;
			bi = ImageIO.read(ScriptUtils.getResourceAsStream(getClass(), "expand0.png"));
			expandIcon = new ImageIcon(bi.getScaledInstance(40, 40, Image.SCALE_DEFAULT));
		} catch (Exception e1) {
			logger.error("Unable to load \"expand0.png\" from resources", e1);
		}
		AcIconButton accountButton = new AcIconButton(expandIcon);
		if(debugButtonBorder)
			accountButton.setBorder(BorderFactory.createLineBorder(Color.RED));
		accountButton.setBorderPainted(true);
		accountButton.setAlignmentX(Component.LEFT_ALIGNMENT);
		accountButton.setToolTipText(GUIConstants.LANG.lblExpandBtnSide);
		// Aggiungi un listener al bottone
		accountButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				operationPanel.openCloseSideMenu();
			}
		});
		return accountButton;
	}

	public void openPanel(int maxWidth){
		for(JButton btn : sideButtonList){
			btn.setText(buttonNameMap.get(btn));
		}
	}

	public void closePanel(int minWidth){
		for(JButton btn : sideButtonList){
			btn.setText("");
		}
	}

	public JButton getNotificationButton() {
		return notificationButton;
	}

	public void setNewNotificationIcon(boolean isThereAnyNotification) {
		String imgName = isThereAnyNotification? "notificationOn0.png" : "notificationOff0.png";
		ImageIcon notificationIcon = null;
		try {
			BufferedImage bi = ImageIO.read(ScriptUtils.getResourceAsStream(getClass(), imgName));
			notificationIcon = new ImageIcon(bi.getScaledInstance(40, 40, Image.SCALE_DEFAULT));
		} catch (Exception e1) {
			logger.error("Unable to load \""+imgName+"\" from resources", e1);
		}
		notificationButton.setIcon(notificationIcon);
	}
}
