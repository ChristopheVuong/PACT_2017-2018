package utils;

import java.awt.Dimension;

import javax.swing.JButton;

public class ButtonSized extends JButton {

	private static final long serialVersionUID = 6943193250939856586L;

	private Dimension dimension;

	public ButtonSized(final int dimensionX, final int dimensionY) {
		super();
		initPanelSized(dimensionX,dimensionY);
	}

	public ButtonSized(final String text, final int dimensionX, final int dimensionY) {
		this(dimensionX,dimensionY);
		setText(text);
	}

	private void initPanelSized(final int dimensionX, final int dimensionY) {
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