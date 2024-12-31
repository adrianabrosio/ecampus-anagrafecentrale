package anagraficaCentrale.client.gui.component;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;

import anagraficaCentrale.client.gui.GUIConstants;

/**
 * class that represents a button to select a service.
 * The button is highlighted when the mouse is over it.
 * This represent the basic component of left panel
 * @author Adriana Brosio
 */
public class AcServiceButton extends JPanel {

	private static final long serialVersionUID = 1L;

	private JButton button;
	private Border selectedBorder, unselectedBorder;
	public AcServiceButton() {
		super();
		this.button = new JButton();
		init();
	}
	
	public AcServiceButton(String text){
		super();
		this.button = new JButton(text);
		init();
	}
	
	public AcServiceButton(Icon ic){
		super();
		this.button = new JButton(ic);
		init();
	}
	
	private void init(){
		setBackground(GUIConstants.OPERATION_PANEL_BACKGROUND);
		setLayout(new FlowLayout(FlowLayout.CENTER));
		
		unselectedBorder = new MatteBorder(2, 3, 2, 3, GUIConstants.BACKGROUND_COLOR_1);
		
		button.setBackground(null);
		button.setOpaque(false);
		button.setFocusPainted(false);
		button.setContentAreaFilled(false);
		button.setForeground(Color.BLACK);
		button.setBorder(unselectedBorder);
		Font f = button.getFont();
		button.setFont(new Font(f.getFontName(), Font.PLAIN, 16));
		
		button.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent arg0) {}
			@Override
			public void mouseEntered(MouseEvent arg0) {
				button.setBorder(selectedBorder);
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				button.setBorder(unselectedBorder);
			}

			@Override
			public void mousePressed(MouseEvent arg0) {}

			@Override
			public void mouseReleased(MouseEvent arg0) {}
		});
		
		Box b = Box.createHorizontalBox();
		b.add(button);
		add(button);
	}

	public void addActionListener(ActionListener actionListener) {
		button.addActionListener(actionListener);
	}
	
	@Override
	public void setBorder(Border border) {
		if(button != null)
			selectedBorder=border;//button.setBorder(border);
		else
			super.setBorder(border);
	}
}
