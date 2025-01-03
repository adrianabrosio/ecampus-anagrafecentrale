package anagraficaCentrale.client.gui.component;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.Scrollable;

import anagraficaCentrale.client.gui.GUIConstants;

/**
 * class that manage graphical representation of a group of JCheckBox
 * it gives the possibility to select/unselect all of them
 * @author Adriana Brosio
 */
public class AcCheckBoxGroup extends JPanel {

	private static final long serialVersionUID = 1L;
	private JCheckBox all;
    private List<JCheckBox> checkBoxes;

    /**
     * constructor
     * @param options list of checkbox entries
     */
    public AcCheckBoxGroup(String... options) {
        checkBoxes = new ArrayList<>();
        setLayout(new BorderLayout());
        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT, 1, 1));
        all = new JCheckBox("Select All...");
        all.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (JCheckBox cb : checkBoxes) {
                    cb.setSelected(all.isSelected());
                }
            }
        });
        header.add(all);
        add(header, BorderLayout.NORTH);

        JPanel content = new ScrollablePane(new GridBagLayout());
        content.setBackground(GUIConstants.OPERATION_PANEL_BACKGROUND);
        if (options.length > 0) {

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridwidth = GridBagConstraints.REMAINDER;
            gbc.anchor = GridBagConstraints.NORTHWEST;
            gbc.weightx = 1;
            for (int index = 0; index < options.length - 1; index++) {
                JCheckBox cb = new JCheckBox(options[index]);
                cb.setOpaque(false);
                checkBoxes.add(cb);
                content.add(cb, gbc);
            }

            JCheckBox cb = new JCheckBox(options[options.length - 1]);
            cb.setOpaque(false);
            checkBoxes.add(cb);
            gbc.weighty = 1;
            content.add(cb, gbc);

        }

        add(new JScrollPane(content));
    }
    
    /**
     * it builds a string that represent the selected check boxes for authorization string
     * note: it does not manage the administrative permissions
     * @return a string that represent the selected check boxes
     */
    public String getCheckBoxString(){
    	StringBuilder checkboxString = new StringBuilder();
    	for(JCheckBox jcb : checkBoxes){
    		checkboxString.append('0');
    		checkboxString.append(jcb.isSelected()?"1":"0");
    	}
    	return checkboxString.toString();
    }
    
    /**
     * it sets check boxes based on the authorization string provided in input
     * @param checkboxString authorization string
     * @return	authorization string
     */
    public String setCheckBoxString(String checkboxString){
    	for(int i = 0; i < checkBoxes.size(); i++) {
    		try {
    			boolean selected = checkboxString.charAt(i*2+1) == '1';
    			checkBoxes.get(i).setSelected(selected);
    		}catch(Exception e) {}
    	}
    	return checkboxString.toString();
    }

    /**
     * it clears all the check boxes
     */
	public void clear() {
		for(JCheckBox jcb : checkBoxes){
			jcb.setSelected(false);
    	}
	}
	
	/**
	 * it enables/disables all the check boxes
	 */
	@Override
	public void setEnabled(boolean status) {
		all.setEnabled(status);
		for(JCheckBox jcb : checkBoxes){
			jcb.setEnabled(status);
    	}
	}

	/**
	 * inner class that implements Scrollable for long check box list
	 */
    public class ScrollablePane extends JPanel implements Scrollable {

        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public ScrollablePane(LayoutManager layout) {
            super(layout);
        }

        public ScrollablePane() {
        }

        @Override
        public Dimension getPreferredScrollableViewportSize() {
            return new Dimension(100, 100);
        }

        @Override
        public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
            return 32;
        }

        @Override
        public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
            return 32;
        }

        @Override
        public boolean getScrollableTracksViewportWidth() {
            boolean track = false;
            Container parent = getParent();
            if (parent instanceof JViewport) {
                JViewport vp = (JViewport) parent;
                track = vp.getWidth() > getPreferredSize().width;
            }
            return track;
        }

        @Override
        public boolean getScrollableTracksViewportHeight() {
            boolean track = false;
            Container parent = getParent();
            if (parent instanceof JViewport) {
                JViewport vp = (JViewport) parent;
                track = vp.getHeight() > getPreferredSize().height;
            }
            return track;
        }

    }


}
