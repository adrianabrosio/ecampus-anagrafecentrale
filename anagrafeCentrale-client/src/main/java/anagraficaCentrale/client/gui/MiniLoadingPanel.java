package anagraficaCentrale.client.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MiniLoadingPanel extends JFrame {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel loadingPanel;
	
	final static Logger logger = LogManager.getRootLogger();
	
	private Timer tm;

    public MiniLoadingPanel() {
        super("Loading Panel");
        try {
			setIconImage(new ImageIcon(ClassLoader.getSystemResource("icon.png")).getImage());
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
			loadingImage = new ImageIcon(ClassLoader.getSystemResource("loading3.gif"));
		} catch (Exception e1) {
			logger.error("Unable to load \"loading.gif\" from resources", e1);
		}
        
        JLabel loadingLabel = new JLabel();
        loadingLabel.setIcon(loadingImage);
        loadingPanel.add(loadingLabel, BorderLayout.WEST);

        JLabel textMessage = new JLabel("  Caricamento in corso...");
        Font font = new Font("Adamina", Font.ITALIC, 30);
        textMessage.setFont(font);
        loadingPanel.add(textMessage, BorderLayout.CENTER);
        
        loadingPanel.setBackground(GUIConstants.BACKGROUND_COLOR_1);
        // Aggiungi il pannello di caricamento al frame
        add(loadingPanel);

        this.setUndecorated(true);
        this.setSize(600, 180);
		this.setLocationRelativeTo(null);
		this.setAlwaysOnTop(true);
		//this.setVisible(true);
		
		// Avvia un timer per chiudere il pannello di caricamento dopo 2 secondi
        /*tm = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	if(clientGui.isReady()){
            		tm.stop();
            		clientGui.setVisible(true);
            		MiniLoadingPanel.this.setVisible(false);
            	}
            }
        });
        tm.start();*/
    }
}