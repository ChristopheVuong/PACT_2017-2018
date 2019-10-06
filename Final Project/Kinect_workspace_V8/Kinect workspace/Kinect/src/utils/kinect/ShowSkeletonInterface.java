package utils.kinect;

import current.KinectInterface;
import viewer.Viewer3D;

/**
 * Contient les m�thodes � impl�menter par ShowSkeleton pour �tre utilis� pour montrer le squelette
 */
public interface ShowSkeletonInterface {
	public KinectInterface getKinectInterface();
	public Viewer3D getViewer();
	public ScrollbarSized getScrollbar();
}