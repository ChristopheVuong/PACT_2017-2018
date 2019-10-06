package utils.kinect;

import java.awt.Scrollbar;

import current.KinectInterface;
import viewer.Viewer3D;
import utils.Constants;

public abstract class ShowSkeletonAbstract implements ShowSkeletonInterface {

	private final KinectInterface kinectInterface;
	private final Viewer3D viewer;
	private final ScrollbarSized scrollbar;

	/**
	 * Lance une toile OpenGL.
	 * <p>
	 * Ajoute les listeners pour dessiner sur la toile, et pour pouvoir faire pivoter le squelette.
	 */
	public ShowSkeletonAbstract(final KinectInterface kinectInterface) {
		this.kinectInterface = kinectInterface;
		viewer = new Viewer3D();
		viewer.getModelView().scale(Constants.initialZoomFactor);
		scrollbar = new ScrollbarSized(Scrollbar.HORIZONTAL,0,1,0,0,400,20);
	}

	@Override
	public final KinectInterface getKinectInterface() {
		return kinectInterface;
	}

	@Override
	public final Viewer3D getViewer() {
		return viewer;
	}
	
	@Override
	public final ScrollbarSized getScrollbar() {
		return scrollbar;
	}
}