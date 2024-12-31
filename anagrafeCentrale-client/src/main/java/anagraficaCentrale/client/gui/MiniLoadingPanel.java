package anagraficaCentrale.client.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import anagraficaCentrale.utils.ScriptUtils;

/**
 * class that displays a loading panel
 * @author Adriana Brosio
 */
public class MiniLoadingPanel extends JFrame {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel loadingPanel;
	
	final static Logger logger = LogManager.getRootLogger();
	
	//private Timer tm;

	/**
	 * constructor
	 */
    public MiniLoadingPanel() {
        super("Loading Panel");
        try {
			setIconImage(ImageIO.read(ScriptUtils.getResourceAsStream(getClass(), "icon.png")));
		} catch (Exception e1) {
			logger.error("Unable to load \"icon.png\" from resources", e1);
		}

        // create loading panel
        loadingPanel = new JPanel();
        loadingPanel.setLayout(new BorderLayout());
        loadingPanel.setBackground(Color.WHITE);

        // add a loading image
        ImageIcon loadingImage = null;
		try {
			loadingImage = new ImageIcon(ScriptUtils.getResource(getClass(), "loading3.gif"));
		} catch (Exception e1) {
			logger.error("Unable to load \"loading.gif\" from resources", e1);
		}
        
        JLabel loadingLabel = new JLabel();
        loadingLabel.setIcon(loadingImage);
        loadingPanel.add(loadingLabel, BorderLayout.WEST);

        JLabel textMessage = new JLabel("  "+ GUIConstants.LANG.lblLoadingMessage);
        Font font = new Font("Adamina", Font.ITALIC, 30);
        textMessage.setFont(font);
        loadingPanel.add(textMessage, BorderLayout.CENTER);
        
        loadingPanel.setBackground(GUIConstants.BACKGROUND_COLOR_1);
        // add the loading panel to the frame
        add(loadingPanel);

        this.setUndecorated(true);
        this.setSize(600, 180);
		this.setLocationRelativeTo(null);
		this.setAlwaysOnTop(true);
		//this.setVisible(true);
		
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