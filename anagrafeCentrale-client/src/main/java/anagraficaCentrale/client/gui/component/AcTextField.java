package anagraficaCentrale.client.gui.component;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyListener;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.event.CaretListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import anagraficaCentrale.client.gui.GUIConstants;

/**
 * class that represents a text field with a placeholder
 * it manages:
 * - mandatory fields
 * - digit only
 * - fixed size of the text
 * It manages error messages related to the field validity
 * @author Adriana Brosio
 */
public class AcTextField extends JPanel {

	private static final long serialVersionUID = 1L;
	final static Logger logger = LogManager.getRootLogger();

	private PlaceholderTextField text;
	private JLabel error;
	private boolean isMandatory;
	private Border textDefaultBorder;
	private boolean isDigitOnly;

	public AcTextField(){
		this(false);
	}

	public AcTextField(boolean isMandatory){
		super();
		this.isMandatory = isMandatory;
		this.isDigitOnly = false;
		setBackground(GUIConstants.OPERATION_PANEL_BACKGROUND);
		setLayout(new GridLayout(0, 1, 0, 5));
		text = new PlaceholderTextField();
		textDefaultBorder = text.getBorder();
		add(text);
		error = new JLabel("");
		error.setForeground(Color.RED);
		add(error);

		text.addFocusListener(new FocusListener() {

			@Override
			public void focusLost(FocusEvent e) {
				logger.debug("focus lost");
				fieldIsValid();
			}

			@Override
			public void focusGained(FocusEvent e) {
				logger.debug("focus gained");
				setError(null);
			}
		});
	}

	public boolean fieldIsValid() {
		if(isMandatory){
			if(getText().equals("")){
				setError(GUIConstants.LANG.lblErrorMandatoryField);
				return false;
			}
		}
		if(isDigitOnly){
			String digitRegex = "\\d*";
			Pattern digitPattern = Pattern.compile(digitRegex);
			Matcher matcher = digitPattern.matcher(getText());
			if(!matcher.matches()){
				setError(GUIConstants.LANG.lblErrorOnlyDigit);
				return false;
			}
		}
		return true;
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

	@Override
	public void setEnabled(boolean b) {
		text.setEnabled(b);
	}

	public void setDigitOnly(boolean b) {
		isDigitOnly  = b;
	}

	public void addKeyListenerToTextArea(KeyListener kl) {
		text.addKeyListener(kl);
	}

	public void setPlaceholder(String placeholder) {
		text.setPlaceholder(placeholder);
	}

	public void addCaretListener(CaretListener caretListener) {
		text.addCaretListener(caretListener);
	}

	@Override
	public void setFont(Font f) {
		super.setFont(f);
		if(text != null) {
			text.setFont(f);
		}

	}

	@Override
	public void setMinimumSize(Dimension size) {
		super.setMinimumSize(size);
		if(text != null) {
			text.setMinimumSize(size);
		}
	}

	@Override
	public void setPreferredSize(Dimension size) {
		super.setPreferredSize(size);
		if(text != null) {
			text.setPreferredSize(size);
		}
	}

	@Override
	public void setBorder(Border border) {
		super.setBorder(border);
		if(text != null) {
			text.setBorder(border);
		}
	}

	public class PlaceholderTextField extends JTextField {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private String placeholder;

		public PlaceholderTextField() {
			super();
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
			g.drawString(placeholder, getInsets().left + 2, pG.getFontMetrics().getMaxAscent() + getInsets().top );//+ 4
		}

		public void setPlaceholder(final String s) {
			placeholder = s;
		}

	}


}
