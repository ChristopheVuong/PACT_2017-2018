package readAndWrite;

import utils.kinect.KinectReadAbstract;
import utils.kinect.KinectReadInterface;

/**
 * Permet de créer l'interface pour sauvegarder ou lire des fichiers de squelettes issus de la Kinect, en mouvement.
 */
public final class KinectRead extends KinectReadAbstract implements KinectReadInterface {

	/**
	 * Permet de créer l'interface pour sauvegarder ou lire des fichiers de squelettes issus de la Kinect, en mouvement.
	 */
	public KinectRead(final KinectFrameInterface kinectFrameInterface) {
		super(kinectFrameInterface);
		setSkeleton(new ShowSkeletonRead(this));
	}
}