package anagraficaCentrale.client.gui.resource;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.Document;

import anagraficaCentrale.client.gui.GUIConstants;
import anagraficaCentrale.client.gui.component.AcIconButton;

/**
 * class that represent a panel that can be filtered.
 * the panel is composed by a list of resource elements and a filter text box
 * it updates the list of resource elements when the filter text box is changed in real time
 * 
 * It is a generic resource element collector that can be used as is or specialized in case of need
 *
 * @author Adriana Brosio
 */
public class FilterableResourcePanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private ArrayList<AbstractResourceElement> resourceList;

	private PlaceholderTextField filterTextBox;

	private JPanel resourcePanel, filterPanel, actionPanel;

	public FilterableResourcePanel() {
		this(new ArrayList<AbstractResourceElement>());
	}

	public FilterableResourcePanel(ArrayList<AbstractResourceElement> resourceList) {
		super();
		this.resourceList = resourceList;
		setLayout(new BorderLayout());
		setBackground(GUIConstants.OPERATION_PANEL_BACKGROUND);
		setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		JPanel headerPanel = new JPanel();
		headerPanel.setLayout(new GridLayout(0,1));
		filterPanel = new JPanel();
		filterPanel.setBackground(GUIConstants.OPERATION_PANEL_BACKGROUND);
		filterPanel.setLayout(new FlowLayout(FlowLayout.TRAILING));
		filterPanel.add(new JLabel(GUIConstants.LANG.lblCerca));
		filterTextBox = new PlaceholderTextField();

		// Aggiungi un listener al text box
		filterTextBox.addCaretListener(new CaretListener() {
			@Override
			public void caretUpdate(CaretEvent arg0) {
				applyFilter(filterTextBox.getText());
			}
		});
		filterPanel.add(filterTextBox);
		headerPanel.add(filterPanel);
		
		actionPanel = new JPanel();
		actionPanel.setBackground(GUIConstants.OPERATION_PANEL_BACKGROUND);
		actionPanel.setLayout(new FlowLayout(FlowLayout.TRAILING));
		headerPanel.add(actionPanel);
		add(headerPanel, BorderLayout.NORTH);

		resourcePanel = new JPanel();
		resourcePanel.setBackground(GUIConstants.OPERATION_PANEL_BACKGROUND);
		resourcePanel.setLayout(new BoxLayout(resourcePanel, BoxLayout.Y_AXIS));
		applyFilter("");

		JScrollPane jsp = new JScrollPane(resourcePanel);
		jsp.setBorder(BorderFactory.createEmptyBorder());
		jsp.setBackground(GUIConstants.OPERATION_PANEL_BACKGROUND);
		jsp.getVerticalScrollBar().setUnitIncrement(16);
		this.add(jsp, BorderLayout.CENTER);
	}

	private void applyFilter(String filter) {
		if(filter == null) 
			return;
		this.resourcePanel.removeAll();
		boolean elementAdded = false;
		for (AbstractResourceElement resource : resourceList) {
			if ( (resource.getName()!=null && resource.getName().toLowerCase().contains(filter.toLowerCase()))
					|| (resource.getDescription()!=null && resource.getDescription().toLowerCase().contains(filter.toLowerCase())) ) {
				elementAdded = true;
				resourcePanel.add(resource);
			}
		}
		if(!elementAdded)
			resourcePanel.add(new JLabel(GUIConstants.LANG.lblApplyFilterNoResults));
		resourcePanel.updateUI();
	}

	public void addResource(AbstractResourceElement resource) {
		resource.setAlignmentX(Component.LEFT_ALIGNMENT);
		resourceList.add(resource);
		applyFilter(filterTextBox.getText());
	}

	public void removeResource(AbstractResourceElement resource) {
		resourceList.remove(resource);
		applyFilter(filterTextBox.getText());
	}

	public void setSelected(AbstractResourceElement abstractResourceElement) {
		for (AbstractResourceElement resource : resourceList) {
			if (resource.equals(abstractResourceElement))
				resource.setSelected(true);
			else if (resource.isSelected())
				resource.setSelected(false);
		}
	}
	
	public void addActionButton(AcIconButton deleteReadNotification) {
		actionPanel.add(deleteReadNotification);
	}

	public class PlaceholderTextField extends JTextField {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private String placeholder;

		public PlaceholderTextField() {
			super();
			setPlaceholder("Inserisci testo");
			Font f = getFont();
			setFont(new Font(f.getName(), Font.ITALIC, 14));
			setMinimumSize(new Dimension(200, 0));
			setPreferredSize(new Dimension(200, 30));
		}

		public PlaceholderTextField(final Document pDoc, final String pText, final int pColumns) {
			super(pDoc, pText, pColumns);
		}

		public PlaceholderTextField(final int pColumns) {
			super(pColumns);
		}

		public PlaceholderTextField(final String pText) {
			super(pText);
		}

		public PlaceholderTextField(final String pText, final int pColumns) {
			super(pText, pColumns);
		}

		public String getPlaceholder() {
			return placeholder;
		}

		@Override
		protected void paintComponent(final Graphics pG) {
			super.paintComponent(pG);

			if (placeholder == null || placeholder.length() == 0 || getText().length() > 0) {
				return;
			}

			final Graphics2D g = (Graphics2D) pG;
			g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g.setColor(getDisabledTextColor());
			g.drawString(placeholder, getInsets().left + 2, pG.getFontMetrics().getMaxAscent() + getInsets().top + 4);
		}

		public void setPlaceholder(final String s) {
			placeholder = s;
		}

	}

}
