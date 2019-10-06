package utils;

import java.awt.Dimension;

import javax.swing.JTextField;

public class TextFieldSized extends JTextField {

	private static final long serialVersionUID = -3895683779190864914L;

	private Dimension dimension;

	public TextFieldSized(final String text, final int dimensionX, final int dimensionY) {
		super(text);
		initTextFieldSized(dimensionX,dimensionY);
	}

	public TextFieldSized(final int dimensionX, final int dimensionY) {
		this(null,dimensionX,dimensionY);
	}

	private void initTextFieldSized(final int dimensionX, final int dimensionY) {
		dimension = new Dimension(dimensionX,dimensionY);
	}

	@Override
	public Dimension getMinimumSize() {
		return dimension;
	}

	@Override
	public Dimension getPreferredSize() {
		return dimension;
	}

	@Override
	public Dimension getMaximumSize() {
		return dimension;
	}
}