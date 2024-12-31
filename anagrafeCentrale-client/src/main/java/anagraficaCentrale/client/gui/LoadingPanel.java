package anagraficaCentrale.client.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import anagraficaCentrale.utils.ScriptUtils;

/**
 * class that displays a loading panel
 * @author Adriana Brosio
 */
public class LoadingPanel extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel loadingPanel;
	
	final static Logger logger = LogManager.getRootLogger();
	
	private Timer tm;

	/* 
	 * Constructor
	 */
    public LoadingPanel(ClientGui clientGui) {
        super("Loading Panel Example");
        try {
			setIconImage(ImageIO.read(ScriptUtils.getResourceAsStream(getClass(), "icon.png")));
		} catch (Exception e1) {
			logger.error("Unable to load \"icon.png\" from resources", e1);
		}

        // Crea il pannello di caricamento
        loadingPanel = new JPanel();
        loadingPanel.setLayout(new BorderLayout());
        loadingPanel.setBackground(Color.WHITE);

        // Aggiungi un'immagine di caricamento
        ImageIcon loadingImage = null;
		try {
			loadingImage = new ImageIcon(ScriptUtils.getResource(getClass(), "loading3.gif"));
		} catch (Exception e1) {
			logger.error("Unable to load \"loading.gif\" from resources", e1);
		}
        
        JLabel loadingLabel = new JLabel();
        loadingLabel.setIcon(loadingImage);
        loadingPanel.add(loadingLabel, BorderLayout.WEST);

        // Aggiungi un messaggio di caricamento
        JLabel loadingMessage = new JLabel(GUIConstants.LANG.lblLoadingMessage);
        loadingPanel.add(loadingMessage, BorderLayout.SOUTH);

        JLabel textMessage = new JLabel(GUIConstants.LANG.lblLoadingTitle);
        Font font = new Font("Adamina", Font.ITALIC, 40);
        textMessage.setFont(font);
        loadingPanel.add(textMessage, BorderLayout.CENTER);
        
        loadingPanel.setBackground(GUIConstants.BACKGROUND_COLOR_1);
        // Aggiungi il pannello di caricamento al frame
        add(loadingPanel);

        this.setUndecorated(true);
        this.setSize(600, 400);
		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// Posiziona il JFrame al centro dello schermo
		this.setLocationRelativeTo(null);
		this.setAlwaysOnTop(true);
		this.setVisible(true);
		
		// Avvia un timer per chiudere il pannello di caricamento dopo 2 secondi
        tm = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	if(clientGui.isReady()){
            		tm.stop();
            		clientGui.setVisible(true);
            		LoadingPanel.this.setVisible(false);
            	}
            }
        });
        tm.start();
    }
}