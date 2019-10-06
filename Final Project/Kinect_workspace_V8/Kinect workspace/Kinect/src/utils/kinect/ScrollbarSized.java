package utils.kinect;

import java.awt.Dimension;
import java.awt.Scrollbar;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;

public final class ScrollbarSized extends Scrollbar {

	private static final long serialVersionUID = 6997977400888743782L;

	private Dimension dimension;
	private KinectReadInterface kinectRead;

	public ScrollbarSized(final int orientation, final int value, final int visible, final int minimum, final int maximum, final int dimensionX, final int dimensionY) {
		super(orientation, value, visible, minimum, maximum);
		initScrollbarSized(dimensionX,dimensionY);
		addAdjustmentListener(new Listener());
	}
	
	public void setKinectRead(KinectReadInterface kinectRead) {
		this.kinectRead = kinectRead;
	}

	private void initScrollbarSized(final int dimensionX, final int dimensionY) {
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
	
	private final class Listener implements AdjustmentListener {

		@Override
		public void adjustmentValueChanged(AdjustmentEvent e) {
			if(kinectRead != null) {
				kinectRead.setCounter(e.getValue());
			}
		}
	}
}