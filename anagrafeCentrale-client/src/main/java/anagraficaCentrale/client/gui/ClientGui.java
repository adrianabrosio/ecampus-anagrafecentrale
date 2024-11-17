package anagraficaCentrale.client.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.FocusTraversalPolicy;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import anagraficaCentrale.client.core.ConnectionManager;
import anagraficaCentrale.client.exception.AcErrorDialog;
import anagraficaCentrale.utils.ClientServerConstants.PortalType;
import anagraficaCentrale.utils.ScriptUtils;

public class ClientGui extends JFrame {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	final static Logger logger = LogManager.getRootLogger();
	private JPanel toolbarPanel;
	private JPanel headerButtonPanel;

	private JButton comuneButton, ospedaleButton, scuolaButton;
	//private JLabel titleLabel;
	private JPanel loginPanel;
	private JLabel usernameLabel;
	private JLabel passwordLabel;
	private JTextField usernameTextField;
	private JPasswordField passwordTextField;
	private JButton loginButton, exitButton;
	private JProgressBar progressBar;
	private JLabel errorMessageLabel;
	private JPanel framePanel;
	private MatteBorder loginPanelBorder;

	private long startTime;

	private String loginErrorMessage;

	private boolean isReady;

	private PortalType portalType;

	private ConnectionManager connectionManager;


	public ClientGui(String[] args, ConnectionManager connectionManager) {
		super("Login");
		this.startTime = Long.parseLong(ScriptUtils.getParam(args, "-startTime="));
		this.connectionManager = connectionManager;
		loginErrorMessage = "";
		new LoadingPanel(this);
		initAndShowLoginGui();

		/*new Timer(3000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ClientGui.this.setVisible(true);
			}
		}).start();*/

		isReady = true;
	}

	private void initAndShowLoginGui() {

		try {
			setIconImage(new ImageIcon(ClassLoader.getSystemResource("icon.png")).getImage());
		} catch (Exception e1) {
			logger.error("Unable to load \"icon.png\" from resources", e1);
		}

		framePanel = new JPanel();
		framePanel.setLayout(new BoxLayout(framePanel, BoxLayout.Y_AXIS));

		toolbarPanel = new JPanel();
		toolbarPanel.setLayout(new BorderLayout());
		toolbarPanel.setMaximumSize(new Dimension(1000,50));

		initHeaderButtonPanel();

		toolbarPanel.add(headerButtonPanel, BorderLayout.CENTER);
		portalType = PortalType.COMUNE;
		//titleLabel = new JLabel(GUIConstants.LANG.lblComuneTitle);
		//titleLabel.setBackground(GUIConstants.BACKGROUND_COLOR_1);

		loginPanel = new JPanel();
		loginPanel.setLayout(new GridBagLayout());
		loginPanel.setBackground(GUIConstants.BACKGROUND_COLOR_1);

		usernameLabel = new JLabel(GUIConstants.LANG.lblLoginUsername);
		passwordLabel = new JLabel(GUIConstants.LANG.lblLoginPassword);

		usernameTextField = new JTextField("federicaDP");
		Dimension textFieldMinDimension = new Dimension(200,25);
		Dimension textFieldMaxDimension = new Dimension(300,25);
		usernameTextField.setMinimumSize(textFieldMinDimension);
		usernameTextField.setPreferredSize(textFieldMinDimension);
		usernameTextField.setMaximumSize(textFieldMaxDimension);

		passwordTextField = new JPasswordField("federicaDP");
		passwordTextField.setMinimumSize(textFieldMinDimension);
		passwordTextField.setPreferredSize(textFieldMinDimension);
		passwordTextField.setMaximumSize(textFieldMaxDimension);
		passwordTextField.addKeyListener(new KeyListener(){
			@Override
			public void keyTyped(KeyEvent e) {
				if(e.getKeyChar()==KeyEvent.VK_ENTER){
					ClientGui.this.loginOperations();
				}
			}
			@Override
			public void keyPressed(KeyEvent e) {}
			@Override
			public void keyReleased(KeyEvent e) {}

		});
		
		loginButton = initLoginButton();
		exitButton = initExitButton();
		JPanel exitPanel = new JPanel();
		exitPanel.setBackground(GUIConstants.BACKGROUND_COLOR_1);
		exitPanel.setLayout(new BorderLayout());
		exitPanel.add(exitButton, BorderLayout.CENTER);
		exitPanel.add(new JLabel("  "), BorderLayout.EAST);
		toolbarPanel.add(exitPanel, BorderLayout.EAST);

		// Aggiunge una barra di avanzamento per indicare lo stato del login
		progressBar = new JProgressBar();
		progressBar.setIndeterminate(true);
		progressBar.setVisible(false);
		progressBar.setPreferredSize(textFieldMaxDimension);

		// Aggiunge un messaggio di errore se le credenziali di accesso non sono valide
		errorMessageLabel = new JLabel(" ");
		errorMessageLabel.setForeground(Color.RED);
		errorMessageLabel.setPreferredSize(textFieldMaxDimension);

		GridBagConstraints constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.insets = new Insets(10, 10, 10, 10);

		constraints.gridx = 0;
		constraints.gridy = 0;
		loginPanel.add(usernameLabel, constraints);

		constraints.gridx = 1;
		constraints.gridy = 0;
		loginPanel.add(usernameTextField, constraints);

		constraints.gridx = 0;
		constraints.gridy = 1;
		loginPanel.add(passwordLabel, constraints);

		constraints.gridx = 1;
		constraints.gridy = 1;
		loginPanel.add(passwordTextField, constraints);

		constraints.gridx = 3;
		constraints.gridy = 0;
		constraints.gridheight = 2;
		loginPanel.add(loginButton, constraints);
		
		JLabel pwResetLabel = new JLabel("Reset password?");
		pwResetLabel.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent arg0) {
				String username = usernameTextField.getText();
				errorMessageLabel.setText("");
				//if username is empty, popup insert username first
				if (username.isEmpty()) {
					JOptionPane.showMessageDialog(ClientGui.this, GUIConstants.LANG.msgUsernameEmpty, "", JOptionPane.WARNING_MESSAGE);
					return;
				}
				try {
					PasswordResetDialog dialog = new PasswordResetDialog(ClientGui.this, connectionManager, loginPanelBorder.getMatteColor(), usernameTextField.getText());
					if(dialog.isPasswordChanged()) {
						JOptionPane.showMessageDialog(ClientGui.this, GUIConstants.LANG.msgPasswordResetSuccess, "", JOptionPane.INFORMATION_MESSAGE);
					}
				} catch (AcErrorDialog e) {
					logger.error(e);
				}
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {}

			@Override
			public void mouseExited(MouseEvent arg0) {}

			@Override
			public void mousePressed(MouseEvent arg0) {}

			@Override
			public void mouseReleased(MouseEvent arg0) {}
			
		});
		constraints.gridx = 1;
		constraints.gridy = 2;
		constraints.gridheight = 1;
		loginPanel.add(pwResetLabel, constraints);

		constraints.gridx = 0;
		constraints.gridy = 3;
		constraints.gridheight = 1;
		constraints.gridwidth = 4;
		loginPanel.add(progressBar, constraints);
		loginPanel.add(errorMessageLabel, constraints);

		/*constraints.gridx = 4;
		constraints.gridy = 0;
		constraints.gridheight = 2;
		constraints.gridwidth = 1;
		loginPanel.add(exitButton, constraints);*/

		//loginPanel.setPreferredSize(new Dimension(580, 180));

		// Aggiungi un listener al pannello di contenuto
		loginPanel.addMouseMotionListener(new MouseMotionListener() {
			int  oldX = 0, oldY = 0;
			@Override
			public void mouseMoved(MouseEvent e) {
				oldX = e.getXOnScreen();
				oldY = e.getYOnScreen();
			}

			@Override
			public void mouseDragged(MouseEvent e) {
				Point oldLocation = ClientGui.this.getLocationOnScreen();
				int relativeX = e.getXOnScreen()-oldX;
				int relativeY = e.getYOnScreen()-oldY;
				ClientGui.this.setLocation(oldLocation.x+relativeX, oldLocation.y+relativeY);
				oldX = e.getXOnScreen();
				oldY = e.getYOnScreen();
			}
		});
		framePanel.add(toolbarPanel);
		//framePanel.add(titleLabel);
		framePanel.add(loginPanel);

		loginPanelBorder = new MatteBorder(8, 8, 8, 8, GUIConstants.COMUNE_COLOR);
		framePanel.setBorder(loginPanelBorder);
		framePanel.setBackground(GUIConstants.BACKGROUND_COLOR_1);

		this.add(framePanel);

		this.setFocusTraversalPolicy(new CustomFocusTraversalPolicy(Arrays.asList(usernameTextField, passwordTextField, loginButton, exitButton)));

		this.setUndecorated(true);
		this.setSize(600, 400);
		// Posiziona il JFrame al centro dello schermo
		this.setLocationRelativeTo(null);

	}

	private void initHeaderButtonPanel() {
		headerButtonPanel = new JPanel();
		headerButtonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
		headerButtonPanel.setBackground(GUIConstants.BACKGROUND_COLOR_1);

		// Aggiungi i pulsanti al pannello superiore
		Dimension headerButtonSize = new Dimension(100,40);
		comuneButton = new JButton(GUIConstants.LANG.lblComuneTitle);
		comuneButton.setPreferredSize(headerButtonSize);
		headerButtonPanel.add(comuneButton);

		ospedaleButton = new JButton(GUIConstants.LANG.lblOspedaleTitle);
		ospedaleButton.setPreferredSize(headerButtonSize);
		headerButtonPanel.add(ospedaleButton);

		scuolaButton = new JButton(GUIConstants.LANG.lblScuolaTitle);
		scuolaButton.setPreferredSize(headerButtonSize);
		headerButtonPanel.add(scuolaButton);

		// Imposta il bordo dei pulsanti come arrotondato
		Border roundedBorder = BorderFactory.createEtchedBorder(Color.BLUE, GUIConstants.BACKGROUND_COLOR_1);
		comuneButton.setBorder(roundedBorder);
		ospedaleButton.setBorder(roundedBorder);
		scuolaButton.setBorder(roundedBorder);

		comuneButton.setBackground(GUIConstants.COMUNE_COLOR);
		comuneButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// Accendi il pulsante Anagrafe
				portalType = PortalType.COMUNE;
				setLoginButtonIcon(portalType);
				comuneButton.setBackground(GUIConstants.COMUNE_COLOR);
				loginPanelBorder = new MatteBorder(8, 8, 8, 8, GUIConstants.COMUNE_COLOR);
				framePanel.setBorder(loginPanelBorder);
				// Spegni gli altri pulsanti
				ospedaleButton.setBackground(Color.WHITE);
				scuolaButton.setBackground(Color.WHITE);
			}
		});

		ospedaleButton.setBackground(null);
		ospedaleButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				portalType = PortalType.OSPEDALE;
				setLoginButtonIcon(portalType);
				ospedaleButton.setBackground(GUIConstants.OSPEDALE_COLOR);
				loginPanelBorder = new MatteBorder(8, 8, 8, 8, GUIConstants.OSPEDALE_COLOR);
				framePanel.setBorder(loginPanelBorder);
				// Spegni gli altri pulsanti
				comuneButton.setBackground(Color.WHITE);
				scuolaButton.setBackground(Color.WHITE);
			}
		});

		scuolaButton.setBackground(null);
		scuolaButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// Accendi il pulsante Scuola
				portalType = PortalType.SCUOLA;
				setLoginButtonIcon(portalType);
				scuolaButton.setBackground(GUIConstants.SCUOLA_COLOR);
				loginPanelBorder = new MatteBorder(8, 8, 8, 8, GUIConstants.SCUOLA_COLOR);
				framePanel.setBorder(loginPanelBorder);
				// Spegni gli altri pulsanti
				comuneButton.setBackground(Color.WHITE);
				ospedaleButton.setBackground(Color.WHITE);
			}
		});

	}

	private JButton initExitButton() {
		final JButton exitButton = new JButton();
		exitButton.setBounds(0, 0, 0, 0);
		//exitButton.set
		Font font = new Font(Font.SANS_SERIF,6,6);
		exitButton.setFont(font);
		Dimension btnSize = new Dimension(27,27);
		exitButton.setMinimumSize(btnSize);
		exitButton.setPreferredSize(btnSize);
		exitButton.setMaximumSize(btnSize);
		exitButton.setOpaque(true);
		exitButton.setBackground(GUIConstants.BACKGROUND_COLOR_1);
		exitButton.setBorder(null);
		//exitButton.setBackground(Color.RED);
		//exitButton.setForeground(Color.WHITE);
		try {
			exitButton.setIcon(new ImageIcon(ClassLoader.getSystemResource("exitIcon.png")));
		} catch (Exception e1) {
			logger.error("Unable to load \"exitIcon.png\" from resources", e1);
		}
		exitButton.setHorizontalAlignment(SwingConstants.CENTER);

		exitButton.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent arg0) {}
			@Override
			public void mouseEntered(MouseEvent arg0) {
				//exitButton.setBackground(Color.LIGHT_GRAY);
				exitButton.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1));
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				//exitButton.setBackground(GUIConstants.BACKGROUND_COLOR_1);
				exitButton.setBorder(null);
			}

			@Override
			public void mousePressed(MouseEvent arg0) {}

			@Override
			public void mouseReleased(MouseEvent arg0) {}
		});

		exitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ClientGui.this.closingOperations();
			}
		});

		return exitButton;
	}

	private JButton initLoginButton() {
		final JButton loginButton = new JButton("");
		loginButton.setBorder(null);
		// Aggiunge un'icona al pulsante di login
		try {
			BufferedImage bi = ImageIO.read(ClassLoader.getSystemResourceAsStream("login"+PortalType.COMUNE.getValue()+".png"));
			loginButton.setIcon(new ImageIcon(bi.getScaledInstance(160, 50, Image.SCALE_DEFAULT)));
		} catch (Exception e1) {
			logger.error("Unable to load \"login"+PortalType.COMUNE.getValue()+".png\" from resources", e1);
		}
		// Aggiunge un effetto al bottone di login
		loginButton.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent arg0) {}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				loginButton.setBackground(Color.YELLOW);
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				loginButton.setBackground(null);
			}

			@Override
			public void mousePressed(MouseEvent arg0) {}

			@Override
			public void mouseReleased(MouseEvent arg0) {}
		});

		loginButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ClientGui.this.loginOperations();
			}
		});

		return loginButton;
	}


	private void setLoginButtonIcon(PortalType pt) {
		try {
			BufferedImage bi = ImageIO.read(ClassLoader.getSystemResourceAsStream("login"+pt.getValue()+".png"));
			loginButton.setIcon(new ImageIcon(bi.getScaledInstance(160, 50, Image.SCALE_DEFAULT)));
		} catch (Exception e1) {
			logger.error("Unable to load \"login"+pt.getValue()+".png\" from resources", e1);
		}
	}

	protected void loginOperations() {
		logger.debug("logging in..");
		errorMessageLabel.setText(" ");
		errorMessageLabel.setVisible(false);
		progressBar.setVisible(true);
		// Crea un nuovo SwingWorker per sganciarsi dal thread principale
		new SwingWorker<Integer, Integer>() {

			@Override
			protected Integer doInBackground() throws Exception {
								
				try{
					connectionManager.login(usernameTextField.getText(), ScriptUtils.hash(passwordTextField.getPassword()), portalType);
					
					//check primo accesso - se l'hash dello user è uguale all'hash della pw, allora primo accesso
					if( ScriptUtils.hash(passwordTextField.getPassword()).equals(ScriptUtils.hash(usernameTextField.getText())) ) {
						try{
							PasswordResetDialog dialog = new PasswordResetDialog(ClientGui.this, connectionManager,  loginPanelBorder.getMatteColor(), usernameTextField.getText());
							if(dialog.isPasswordChanged()) {
								JOptionPane.showMessageDialog(ClientGui.this, GUIConstants.LANG.msgPasswordResetSuccess, "", JOptionPane.INFORMATION_MESSAGE);
								loginErrorMessage = GUIConstants.LANG.msgPasswordResetSuccess;
							} else {
								JOptionPane.showMessageDialog(ClientGui.this, GUIConstants.LANG.msgPasswordResetMandatory, "", JOptionPane.WARNING_MESSAGE);
								loginErrorMessage = GUIConstants.LANG.msgPasswordResetMandatory;
							}
							
							passwordTextField.setText("");
							return 100;
						}catch(Exception e){
							logger.warn("Login error: "+e.getMessage());
							loginErrorMessage = e.getMessage();
							passwordTextField.setText("");
							return 100;
						}
					}
					
					OperationPanel op = new OperationPanel(portalType, connectionManager);
					op.setLocationRelativeTo(ClientGui.this);
					op.show();
				}catch(Exception e){
					logger.warn("Login error: "+e.getMessage());
					loginErrorMessage = e.getMessage();
				}
				return 100;
			}

			@Override
			protected void done() {
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						if(loginErrorMessage != null && !loginErrorMessage.equals("")) {
							errorMessageLabel.setText(loginErrorMessage);
							loginErrorMessage = "";
						} else {

							ClientGui.this.setVisible(false);
							ClientGui.this.dispose();
						}
						progressBar.setVisible(false);
						errorMessageLabel.setVisible(true);
					}

				});
			}
		}.execute();
	}

	protected void closingOperations() {
		logger.info("Closing the application..");
		try{
			connectionManager.logout();
		}catch(Exception e){
			logger.error(e);
		}
		logger.info("Total session time: " + (System.currentTimeMillis()-startTime) + "ms");
		System.exit(0);
	}

	class CustomFocusTraversalPolicy extends FocusTraversalPolicy {
		private final List<Component> componentOrder = new ArrayList<>();

		public CustomFocusTraversalPolicy(final List<Component> componentOrder) {
			this.componentOrder.addAll(componentOrder);
		}
		@Override
		public Component getComponentAfter(final Container focusCycleRoot, final Component aComponent) {
			Component nextComponent = componentOrder.get((componentOrder.indexOf(aComponent) + 1) % componentOrder.size());
			applyCustomLogic(nextComponent);
			return nextComponent;
		}
		@Override
		public Component getComponentBefore(final Container focusCycleRoot, final Component aComponent) {
			final int currentIndex = componentOrder.indexOf(aComponent);
			Component nextComponent = componentOrder.get(currentIndex > 0 ? currentIndex - 1 : componentOrder.size() - 1);
			applyCustomLogic(nextComponent);
			return nextComponent;
		}
		@Override
		public Component getFirstComponent(final Container focusCycleRoot) {
			Component nextComponent = componentOrder.get(0);
			applyCustomLogic(nextComponent);
			return nextComponent;
		}
		@Override
		public Component getLastComponent(final Container focusCycleRoot) {
			Component nextComponent = componentOrder.get(componentOrder.size() - 1);
			applyCustomLogic(nextComponent);
			return nextComponent;
		}
		@Override
		public Component getDefaultComponent(final Container focusCycleRoot) {
			return getFirstComponent(focusCycleRoot);
		}

		private void applyCustomLogic(Component component) {
			if(component.equals(passwordTextField)){
				passwordTextField.selectAll();
			}
		}
	}

	public boolean isReady() {
		return isReady;
	}
}
