package anagraficaCentrale.client.gui;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class DraggableWindowMouseListener implements MouseMotionListener {
	private int  oldX = 0, oldY = 0;
	private ClientGui clientGui;
	
	public DraggableWindowMouseListener(ClientGui clientGui) {
		this.clientGui = clientGui;
	}
	@Override
	public void mouseMoved(MouseEvent e) {
		oldX = e.getXOnScreen();
		oldY = e.getYOnScreen();
	}

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
