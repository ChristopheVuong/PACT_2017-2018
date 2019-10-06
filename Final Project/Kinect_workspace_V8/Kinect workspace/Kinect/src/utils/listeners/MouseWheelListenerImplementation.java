package utils.listeners;

import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import viewer.ModelView;
import viewer.ViewerListenerElements;
import utils.Constants;

public final class MouseWheelListenerImplementation implements MouseWheelListener {

	private final ViewerListenerElements viewer;
	
	public MouseWheelListenerImplementation(final ViewerListenerElements viewer) {
		this.viewer = viewer;
		viewer.getModelView().scale(Constants.initialZoomFactor);
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		if(e.getScrollType() == MouseWheelEvent.WHEEL_UNIT_SCROLL) {
			final int notches = e.getWheelRotation();
			final ModelView modelview = viewer.getModelView();
			if(notches < 0) {
				modelview.scale(Constants.zoomFactorMultiplier);
			} else {
				modelview.scale(1f/Constants.zoomFactorMultiplier);
			}
			
		}
	}
}