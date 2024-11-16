package anagraficaCentrale.client.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.NoSuchAlgorithmException;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import anagraficaCentrale.client.core.ConnectionManager;
import anagraficaCentrale.client.exception.AcErrorDialog;
import anagraficaCentrale.client.gui.component.AcServiceButton;

public class PasswordResetDialog extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	final static Logger logger = LogManager.getRootLogger();
	private JPasswordField oldPasswordTextField, newPasswordTextField, confirmPasswordTextField;
	private JLabel lblError;
	private ConnectionManager connectionManager;
	private String username;
	private boolean passwordChanged;

	public PasswordResetDialog(Component clientGui, ConnectionManager connectionManager, String username) throws AcErrorDialog {
		super();
		this.connectionManager = connectionManager;
		this.username = username;
		this.passwordChanged = false;
		setModal(true);
		setModalityType(ModalityType.APPLICATION_MODAL);
		setTitle("Password Reset");
		setSize(400, 200);
		setLocationRelativeTo(clientGui);
		//setAlwaysOnTop(true);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setResizable(false);
		setContentPane(generateInnerPanel());
		setVisible(true);
	}

	private Container generateInnerPanel() throws AcErrorDialog{
		JPanel innerPanel = new JPanel();
		innerPanel.setLayout(new BorderLayout());
		JPanel attrPanel = new JPanel();
		//this.removeAll();
		//attrPanel.setLayout(new GridLayout(0, 2, 20, 20));
		attrPanel.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.anchor = GridBagConstraints.NORTH;
		//gbc.anchor = GridBagConstraints.NORTHWEST;
		gbc.insets = new Insets(5, 15, 5, 15);
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 0;
		
		JLabel lblId = new JLabel(GUIConstants.LANG.lblOldPassword + "*");
		attrPanel.add(lblId, gbc);

		oldPasswordTextField = new JPasswordField();
		oldPasswordTextField.setColumns(20);
		gbc.gridx = 1;
		gbc.weightx = 100;
		attrPanel.add(oldPasswordTextField, gbc);
		
		gbc.gridx = 0;
		gbc.weightx = 0;
		gbc.gridy++;
		lblId = new JLabel(GUIConstants.LANG.lblNewPassword + "*");
		attrPanel.add(lblId, gbc);

		newPasswordTextField = new JPasswordField();
		newPasswordTextField.setColumns(20);
		gbc.gridx = 1;
		gbc.weightx = 100;
		attrPanel.add(newPasswordTextField, gbc);
		
		gbc.gridx = 0;
		gbc.weightx = 0;
		gbc.gridy++;
		lblId = new JLabel(GUIConstants.LANG.lblConfirmPassword + "*");
		attrPanel.add(lblId, gbc);

		confirmPasswordTextField = new JPasswordField();
		confirmPasswordTextField.setColumns(20);
		gbc.gridx = 1;
		gbc.weightx = 100;
		attrPanel.add(confirmPasswordTextField, gbc);
		
		gbc.gridx = 0;
		gbc.weightx = 0;
		gbc.gridy++;
		attrPanel.add(new JLabel(""), gbc);
		
		lblError = new JLabel("");
		lblError.setForeground(Color.RED);
		
		AcServiceButton setPasswordButton = new AcServiceButton(GUIConstants.LANG.lblSaveBtn);
		setPasswordButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				lblError.setText("");
				
				if(oldPasswordTextField.getPassword().length == 0 || newPasswordTextField.getPassword().length == 0 || confirmPasswordTextField.getPassword().length == 0) {
					//errore, la password non può essere uguale all'username
					popupError(GUIConstants.LANG.errFillMandatoryFields);
					return;
				}
				
				if(username.equals(new String(newPasswordTextField.getPassword()))) {
					//errore, la password non può essere uguale all'username
					popupError(GUIConstants.LANG.errPasswordResetSameAsUsername);
					return;
				}
				
				if(!new String(confirmPasswordTextField.getPassword()).equals(new String(newPasswordTextField.getPassword()))) {
					//errore, la password non può essere uguale all'username
					popupError(GUIConstants.LANG.errPasswordResetDifferentPassword);
					return;
				}
				
				try {
					connectionManager.passwordReset(username, oldPasswordTextField.getPassword(), newPasswordTextField.getPassword());
					passwordChanged = true;
					PasswordResetDialog.this.dispose();
				} catch (Exception e) {
					logger.error(e);
					popupError(e.getMessage());
				}
			}

			
		});
		
		innerPanel.add(attrPanel, BorderLayout.CENTER);
		
		JPanel lowerPanel = new JPanel();
		lowerPanel.setLayout(new BorderLayout());
		lowerPanel.add(lblError, BorderLayout.NORTH);
		lowerPanel.add(setPasswordButton, BorderLayout.CENTER);
		
		innerPanel.add(lowerPanel, BorderLayout.SOUTH);
		return innerPanel;
	}
	
	private void popupError(String errMessage) {
		//JOptionPane.showMessageDialog(this, errMessage, "Error", JOptionPane.ERROR_MESSAGE);
		lblError.setText(errMessage);
	}
	
	public boolean isPasswordChanged() {
		return this.passwordChanged;
	}
}
