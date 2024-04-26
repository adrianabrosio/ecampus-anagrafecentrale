package anagraficaCentrale.client.gui.service;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.MatteBorder;

import anagraficaCentrale.client.gui.GUIConstants;
import anagraficaCentrale.client.gui.OperationPanel;
import anagraficaCentrale.client.gui.component.AcServiceButton;

public abstract class GenericService extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected OperationPanel operationPanel;
	
	protected JLabel serviceTitle;
	protected JPanel innerPanel;

	public GenericService(OperationPanel op) {
		this.operationPanel = op;
		setBackground(GUIConstants.OPERATION_PANEL_BACKGROUND);
		setBorder(BorderFactory.createLineBorder(op.guiBackgroundColor, 2));
		setLayout(new BorderLayout());
		serviceTitle = new JLabel("Generic Service");
		Font serviceTitleFont = new Font(serviceTitle.getFont().getName(), Font.BOLD, 22);
		serviceTitle.setFont(serviceTitleFont);
		serviceTitle.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		add(serviceTitle, BorderLayout.NORTH);
		
		AcServiceButton closeButton = new AcServiceButton(GUIConstants.LANG.lblCloseBtn);
		closeButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				operationPanel.closeService();
			}
			
		});
		closeButton.setBorder(new MatteBorder(2, 3, 2, 3, op.guiBackgroundColor));
		
		add(closeButton, BorderLayout.AFTER_LAST_LINE);
		
		innerPanel = generateInnerPanel();
		innerPanel.setBackground(GUIConstants.OPERATION_PANEL_BACKGROUND);
		add(innerPanel, BorderLayout.CENTER);
		
		
	}
	
	protected abstract JPanel generateInnerPanel();

	public void setTitle(String title){
		serviceTitle.setText(title);
	}
}
