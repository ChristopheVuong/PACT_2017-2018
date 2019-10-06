package utils.kinect;

import current.KinectInterface;
import viewer.Viewer3D;

/**
 * Contient les méthodes à implémenter par ShowSkeleton pour être utilisé pour montrer le squelette
 */
public interface ShowSkeletonInterface {
	public KinectInterface getKinectInterface();
	public Viewer3D getViewer();
	public ScrollbarSized getScrollbar();
}