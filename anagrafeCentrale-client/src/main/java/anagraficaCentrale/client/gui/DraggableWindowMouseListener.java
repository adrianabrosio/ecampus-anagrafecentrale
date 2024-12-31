package anagraficaCentrale.client.gui;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

/**
 * class to make the window draggable
 * @author Adriana Brosio
 */
public class DraggableWindowMouseListener implements MouseMotionListener {
	private int  oldX = 0, oldY = 0;
	private ClientGui clientGui;
	
	/**
	 * constructor of the class
	 */
	public DraggableWindowMouseListener(ClientGui clientGui) {
		this.clientGui = clientGui;
	}
	
	/**
	 * method to set the mouse start position
	 */
	@Override
	public void mouseMoved(MouseEvent e) {
		oldX = e.getXOnScreen();
		oldY = e.getYOnScreen();
	}

	/**
	 * method to change the window position based on mouse position
	 */
	@Override
	public void mouseDragged(MouseEvent e) {
		Point oldLocation = this.clientGui.getLocationOnScreen();
		int relativeX = e.getXOnScreen()-oldX;
		int relativeY = e.getYOnScreen()-oldY;
		this.clientGui.setLocation(oldLocation.x+relativeX, oldLocation.y+relativeY);
		oldX = e.getXOnScreen();
		oldY = e.getYOnScreen();
	}
}
