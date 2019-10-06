package readAndWrite;

import java.util.ArrayList;
import java.util.List;

import edu.ufl.digitalworlds.j4k.J4KSDK;
import utils.ComboItem;
import utils.UserErrorHandler;
import utils.kinect.KinectReadAbstract;

/**
 * Permet de cr�er l'interface pour sauvegarder ou lire des fichiers de squelettes issus de la Kinect, en mouvement.
 */
public final class KinectReadAndWrite extends KinectReadAbstract implements KinectReadAndWriteInterface {

	private final KinectImplementation kinect;

	private int countFramesSaved;
	private int countFrames;
	private boolean saving;
	private List<ComboItem> positionsAndStatusSaving;

	/**
	 * Permet de cr�er l'interface pour sauvegarder ou lire des fichiers de squelettes issus de la Kinect, en mouvement.
	 */
	public KinectReadAndWrite(final KinectFrameInterface kinectFrameInterface, final String imagePath) {
		super(kinectFrameInterface);
		kinect = new KinectImplementation(this, imagePath);
		if(kinect.getDeviceType() == 0) {
			new UserErrorHandler("Il semblerait qu'aucune Kinect n'est reconnue par votre ordinateur, veuillez corriger ce probl�me avant de recommencer");
			System.exit(0);
		} else {
			setSkeleton(new ShowSkeletonReadAndWrite(this));
		}
	}

	@Override
	public boolean getSaving() {
		return saving;
	}

	@Override
	public int getCountFramesSaved() {
		return countFramesSaved;
	}

	@Override
	public void setCountFramesSaved(final int countFramesSaved) {
		this.countFramesSaved = countFramesSaved;
	}

	@Override
	public int getCountFrames() {
		return countFrames;
	}
	
	@Override
	public void incrementCountFrames() {
		countFrames++;
	}

	/**
	 * Permet d'obtenir le tableau de toutes les frames en cours de sauvegarde du fichier en cours d'�criture.
	 */
	@Override
	public List<ComboItem> getPositionsAndStatusSaving() {
		return positionsAndStatusSaving;
	};

	/**
	 * Termine la sauvegarde.
	 */
	@Override
	public void unsetSaving() {
		saving = false;
	}

	@Override
	public void stop() {
		kinect.stop();
	}

	/**
	 * Permet de lancer une sauvegarde de fichier de squelette en mouvement.
	 * <p>
	 * On initialise positionsSaving, on affiche la toile OpenGL puis on d�marre la Kinect.
	 */
	@Override
	public void beginSaving() {
		saving = true;
		setPositionsOpen(null);
		positionsAndStatusSaving = new ArrayList<ComboItem>();
		getViewer().setVisible(true);
		kinect.start(J4KSDK.SKELETON);
	}
}