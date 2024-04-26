package anagraficaCentrale.client.gui.component;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import anagraficaCentrale.client.gui.GUIConstants;

public class AcTextField extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	final static Logger logger = LogManager.getRootLogger();

	private JTextField text;
	private JLabel error;
	private boolean isMandatory;
	private Border textDefaultBorder;

	public AcTextField(){
		this(false);
	}

	public AcTextField(boolean isMandatory){
		super();
		this.isMandatory = isMandatory;
		setBackground(GUIConstants.OPERATION_PANEL_BACKGROUND);
		setLayout(new GridLayout(0, 1, 0, 5));
		text = new JTextField();
		textDefaultBorder = text.getBorder();
		add(text);
		error = new JLabel("");
		error.setForeground(Color.RED);
		add(error);

		text.addFocusListener(new FocusListener() {

			@Override
			public void focusLost(FocusEvent e) {
				logger.debug("focus lost");
				fieldValidation();
			}

			@Override
			public void focusGained(FocusEvent e) {
				logger.debug("focus gained");
				setError(null);
			}
		});
	}

	public void fieldValidation() {
		if(isMandatory){
			if(getText().equals("")){
				setError(GUIConstants.LANG.lblErrorMandatoryField);
			}
		}
	}

	public void setText(String s){
		text.setText(s);
	}

	public String getText(){
		return text.getText().trim();
	}

	public void setMaximumCharacterSize(int limit){
		text.setDocument(new PlainDocument(){
			private static final long serialVersionUID = 1L;

			public void insertString( int offset, String  str, AttributeSet attr ) throws BadLocationException {
				if (str == null) return;

				if ((getLength() + str.length()) <= limit) {
					super.insertString(offset, str, attr);
				}
			}
		});

	}

	public void setError(String s){
		if(s != null){
			error.setText(s);
			text.setBorder(new LineBorder(Color.red, 2));
		}else{
			error.setText("");
			text.setBorder(textDefaultBorder);
		}
	}

	public boolean isMandatory(){
		return isMandatory;
	}

	public void setMandatory(boolean isMandatory){
		this.isMandatory = isMandatory;
	}

	public void setEditable(boolean b) {
		text.setEditable(b);
	}
}
