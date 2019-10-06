package utils;

import java.awt.Dimension;
import java.awt.LayoutManager;

import javax.swing.JPanel;

public final class PanelSized extends JPanel {

	private static final long serialVersionUID = 6943193250939856586L;

	private Dimension dimension;

	public PanelSized(final int dimensionX, final int dimensionY) {
		super();
		initPanelSized(dimensionX,dimensionY);
	}

	public PanelSized(final LayoutManager layout, final int dimensionX, final int dimensionY) {
		super(layout);
		initPanelSized(dimensionX,dimensionY);
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