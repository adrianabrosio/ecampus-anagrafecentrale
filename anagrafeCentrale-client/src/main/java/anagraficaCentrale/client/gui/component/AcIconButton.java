package anagraficaCentrale.client.gui.component;

import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;

/**
 * class that manage graphical representation of a button.
 * This represent the basic button of the application.
 * @author Adriana Brosio
 */
public class AcIconButton extends JButton {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AcIconButton() {
		super();
		init();
	}
	
	public AcIconButton(String text){
		super(text);
		init();
	}
	
	public AcIconButton(Icon ic){
		super(ic);
		init();
	}
	
	private void init(){
		setBackground(null);
		setOpaque(false);
		setFocusPainted(false);
		setBorderPainted(false);
		setContentAreaFilled(false);
		setBorder(BorderFactory.createEmptyBorder(0,0,0,0)); 
		setForeground(Color.WHITE);
		Font f = getFont();
		setFont(new Font(f.getFontName(), Font.PLAIN, 16));
	}
}
