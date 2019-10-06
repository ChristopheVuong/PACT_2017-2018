package utils;

import java.awt.Dimension;
import java.awt.LayoutManager;

import javax.swing.JPanel;

public class PanelSizedX extends JPanel {

	private static final long serialVersionUID = 6943193250939856586L;

	private int dimensionX;

	public PanelSizedX(final int dimensionX) {
		super();
		initPanelSized(dimensionX);
	}

	public PanelSizedX(final LayoutManager layout, final int dimensionX) {
		super(layout);
		initPanelSized(dimensionX);
	}

	private void initPanelSized(final int dimensionX) {
		this.dimensionX = dimensionX;
	}

	@Override
	public Dimension getMinimumSize() {
		return new Dimension(dimensionX,(int)super.getMinimumSize().getHeight());
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(dimensionX,(int)super.getPreferredSize().getHeight());
	}

	@Override
	public Dimension getMaximumSize() {
		return new Dimension(dimensionX,(int)super.getMaximumSize().getHeight());
	}
}