package anagraficaCentrale.client.gui.service;

import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import anagraficaCentrale.client.core.ConnectionManager;
import anagraficaCentrale.client.gui.OperationPanel;

public class ShowProfileService extends GenericService {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextField textField_3;
	private JTextField textField_4;
	private JTextField textField_5;
	/**
	 * Create the panel.
	 */
	public ShowProfileService(OperationPanel op) {
		super(op);
		this.setBorder(new EmptyBorder(20, 20, 20, 20));
	}
	@Override
	protected JPanel generateInnerPanel() {
		JPanel innerPanel = new JPanel();
		ConnectionManager cm = this.operationPanel.getConnectionManager();
		setTitle("");
		this.removeAll();
		innerPanel.setLayout(new GridLayout(0, 2, 20, 20));
		
		JLabel lblId = new JLabel("ID");
		innerPanel.add(lblId);
		
		textField = new JTextField(cm.getUserAttribute("id"));
		textField.setEditable(false);
		innerPanel.add(textField);
		textField.setColumns(10);
		
		JLabel lblFirstName = new JLabel("First Name");
		innerPanel.add(lblFirstName);
		
		textField_1 = new JTextField(cm.getUserAttribute("first_name"));
		textField_1.setEditable(false);
		innerPanel.add(textField_1);
		textField_1.setColumns(10);
		
		JLabel lblSurname = new JLabel("Surname");
		innerPanel.add(lblSurname);
		
		textField_2 = new JTextField(cm.getUserAttribute("surname"));
		textField_2.setEditable(false);
		innerPanel.add(textField_2);
		textField_2.setColumns(10);
		
		JLabel lblBirthDate = new JLabel("Birth Date");
		innerPanel.add(lblBirthDate);
		
		textField_3 = new JTextField(cm.getUserAttribute("birthdate"));
		textField_3.setEditable(false);
		innerPanel.add(textField_3);
		textField_3.setColumns(10);
		
		JLabel lblGender = new JLabel("Gender");
		innerPanel.add(lblGender);
		
		textField_4 = new JTextField(cm.getUserAttribute("gender"));
		textField_4.setEditable(false);
		innerPanel.add(textField_4);
		textField_4.setColumns(10);
		
		JLabel lblTaxIdCode = new JLabel("Tax ID code");
		innerPanel.add(lblTaxIdCode);
		
		textField_5 = new JTextField(cm.getUserAttribute("tax_id_code"));
		textField_5.setEditable(false);
		innerPanel.add(textField_5);
		textField_5.setColumns(10);

		return innerPanel;
	}

}
