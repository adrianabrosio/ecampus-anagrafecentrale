package anagraficaCentrale.client.gui.service;

import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import anagraficaCentrale.client.core.ConnectionManager;
import anagraficaCentrale.client.gui.GUIConstants;
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
	private JTextField textField_6;
	private JTextField textField_6_1;
	private JTextField textField_7;
	private JTextField textField_8;
	private JTextField textField_9;
	private JTextField textField_10;
	private JTextField textField_11;
	private JTextField textField_12;
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
		
		JLabel lblId = new JLabel(GUIConstants.LANG.lbluserCreationUsername);
		innerPanel.add(lblId);
		
		textField = new JTextField(cm.getUserAttribute("id"));
		textField.setEditable(false);
		innerPanel.add(textField);
		
		JLabel lblFirstName = new JLabel(GUIConstants.LANG.lbluserCreationFirstName);
		innerPanel.add(lblFirstName);
		
		textField_1 = new JTextField(cm.getUserAttribute("first_name"));
		textField_1.setEditable(false);
		innerPanel.add(textField_1);
		
		JLabel lblSurname = new JLabel(GUIConstants.LANG.lbluserCreationSurname);
		innerPanel.add(lblSurname);
		
		textField_2 = new JTextField(cm.getUserAttribute("surname"));
		textField_2.setEditable(false);
		innerPanel.add(textField_2);
		
		JLabel lblBirthDate = new JLabel(GUIConstants.LANG.lbluserCreationBirthDate);
		innerPanel.add(lblBirthDate);
		
		textField_3 = new JTextField(cm.getUserAttribute("birthdate"));
		textField_3.setEditable(false);
		innerPanel.add(textField_3);
		
		JLabel lblGender = new JLabel(GUIConstants.LANG.lbluserCreationGender);
		innerPanel.add(lblGender);
		
		textField_4 = new JTextField(cm.getUserAttribute("gender"));
		textField_4.setEditable(false);
		innerPanel.add(textField_4);
		
		JLabel lblTaxIdCode = new JLabel(GUIConstants.LANG.lbluserCreationTaxIDCode);
		innerPanel.add(lblTaxIdCode);
		
		textField_5 = new JTextField(cm.getUserAttribute("tax_id_code"));
		textField_5.setEditable(false);
		innerPanel.add(textField_5);
		
		JLabel lblBirthTown = new JLabel(GUIConstants.LANG.lbluserCreationBirthTown);
		innerPanel.add(lblBirthTown);
		
		textField_6 = new JTextField(cm.getUserAttribute("birth_town"));
		textField_6.setEditable(false);
		innerPanel.add(textField_6);
		
		JLabel lblBirthProvince = new JLabel(GUIConstants.LANG.lbluserCreationBirthProvince);
		innerPanel.add(lblBirthProvince);
		
		textField_6_1 = new JTextField(cm.getUserAttribute("birth_province"));
		textField_6_1.setEditable(false);
		innerPanel.add(textField_6_1);
		
		JLabel lblBirthState = new JLabel(GUIConstants.LANG.lbluserCreationBirthState);
		innerPanel.add(lblBirthState);
		
		textField_7 = new JTextField(cm.getUserAttribute("birth_state"));
		textField_7.setEditable(false);
		innerPanel.add(textField_7);
		
		JLabel lblAddress = new JLabel(GUIConstants.LANG.lbluserCreationAddress);
		innerPanel.add(lblAddress);
		
		textField_8 = new JTextField(cm.getUserAttribute("address"));
		textField_8.setEditable(false);
		innerPanel.add(textField_8);
		
		JLabel lblTown = new JLabel(GUIConstants.LANG.lbluserCreationTown);
		innerPanel.add(lblTown);
		
		textField_9 = new JTextField(cm.getUserAttribute("town"));
		textField_9.setEditable(false);
		innerPanel.add(textField_9);
		
		JLabel lblProvince = new JLabel(GUIConstants.LANG.lbluserCreationProvince);
		innerPanel.add(lblProvince);
		
		textField_10 = new JTextField(cm.getUserAttribute("province"));
		textField_10.setEditable(false);
		innerPanel.add(textField_10);
		
		JLabel lblState = new JLabel(GUIConstants.LANG.lbluserCreationState);
		innerPanel.add(lblState);
		
		textField_11 = new JTextField(cm.getUserAttribute("state"));
		textField_11.setEditable(false);
		innerPanel.add(textField_11);
		
		JLabel lblZipCode = new JLabel(GUIConstants.LANG.lbluserCreationZipCode);
		innerPanel.add(lblZipCode);
		
		textField_12 = new JTextField(cm.getUserAttribute("zip_code"));
		textField_12.setEditable(false);
		innerPanel.add(textField_12);

		return innerPanel;
	}

}
