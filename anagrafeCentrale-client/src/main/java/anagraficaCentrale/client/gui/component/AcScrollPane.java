package anagraficaCentrale.client.gui.component;

import java.awt.Component;

import javax.swing.JScrollPane;

import anagraficaCentrale.client.gui.GUIConstants;

public class AcScrollPane extends JScrollPane {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AcScrollPane(Component c){
		super(c);
		setBackground(GUIConstants.OPERATION_PANEL_BACKGROUND);
		getVerticalScrollBar().setUnitIncrement(10);
	}
}
