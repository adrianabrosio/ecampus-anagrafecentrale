package anagraficaCentrale.client.gui.resource;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import anagraficaCentrale.client.gui.GUIConstants;
import anagraficaCentrale.client.gui.OperationPanel;
import anagraficaCentrale.client.gui.component.AcIconButton;
import anagraficaCentrale.utils.ScriptUtils;

public abstract class AbstractResourceElement extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	final static Logger logger = LogManager.getRootLogger();
	
	private static final Border DEFAULT_BORDER = BorderFactory.createLineBorder(Color.LIGHT_GRAY, 1);
	private static final Border HOVER_BORDER = BorderFactory.createBevelBorder(BevelBorder.RAISED);
	private Border clickedBorder;

	public static final Dimension MINIMUM_SIZE = new Dimension(200, 0);
	public static final Dimension MAXIMUM_SIZE = new Dimension(500, 40);
	public static final Dimension PREFERRED_SIZE = new Dimension(200, 40);
	
	protected OperationPanel operationPanel;
	
	private JLabel descLabel;
	private AcIconButton actionButton;

	private boolean isSelected;
	
	protected AbstractResourceElement(OperationPanel op, String description) {
		super();
		this.operationPanel = op;
		setSelected(false);
		setLayout(new BorderLayout());
		setBackground(GUIConstants.RESOURCE_CARD_BACKGROUND);
		descLabel = new JLabel(description);
		add(descLabel,BorderLayout.CENTER);
		
		clickedBorder = BorderFactory.createLineBorder(op.guiBackgroundColor, 2);
		
		ImageIcon buttonIcon = null;
		try {
			BufferedImage bi = ImageIO.read(ScriptUtils.getResourceAsStream(getClass(), getButtonIconName()));
			buttonIcon = new ImageIcon(bi.getScaledInstance(40, 40, Image.SCALE_DEFAULT));
		} catch (Exception e1) {
			logger.error("Unable to load \""+getButtonIconName()+"\" from resources", e1);
		}
		
		actionButton = new AcIconButton();
		addMouseListener(attachMouseListener());
		actionButton.setIcon(buttonIcon);
		add(actionButton, BorderLayout.EAST);
		setMinimumSize(MINIMUM_SIZE);
		setPreferredSize(PREFERRED_SIZE);
		setMaximumSize(MAXIMUM_SIZE);
		setBorder(DEFAULT_BORDER);//BorderFactory.createBevelBorder(BevelBorder.RAISED)
	}
	
	private MouseListener attachMouseListener() {
		return new MouseListener(){

			@Override
			public void mouseClicked(MouseEvent arg0) {
				//rimosso da qui in quanto a volte i bottoni davano l'impressione di essere poco reattivi
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				if(!isSelected())
					setBorder(HOVER_BORDER);
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				if(!isSelected())
					setBorder(DEFAULT_BORDER);
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				operationPanel.selectOperation(AbstractResourceElement.this);
				arg0.consume();
			}

			@Override
			public void mouseReleased(MouseEvent arg0) {}
			
		};
	}

	public void setDescription(String description){
		descLabel.setText(description);
	}
	

	protected void setButtonIconImage() {
		ImageIcon buttonIcon = null;
		try {
			BufferedImage bi = ImageIO.read(ScriptUtils.getResourceAsStream(getClass(), getButtonIconName()));
			buttonIcon = new ImageIcon(bi.getScaledInstance(40, 40, Image.SCALE_DEFAULT));
		} catch (Exception e1) {
			logger.error("Unable to load \""+getButtonIconName()+"\" from resources", e1);
		}
		actionButton.setIcon(buttonIcon);
	}
	
	public String getDescription() {
		return descLabel.getText();
	}
	
	protected abstract void executeAction();
	protected abstract void executePostAction();
	protected abstract String getButtonIconName();

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
		if(isSelected){
			setBorder(clickedBorder);
			new SwingWorker<Integer, Integer>() {

				@Override
				protected Integer doInBackground() throws Exception {
					AbstractResourceElement.this.executeAction();
					return 100;
				}

				@Override
				protected void done() {
					SwingUtilities.invokeLater(new Runnable() {
						@Override
						public void run() {
							AbstractResourceElement.this.executePostAction();
						}
					});
				}
			}.execute();
		} else {
			setBorder(DEFAULT_BORDER);
		}
	}

	

}
