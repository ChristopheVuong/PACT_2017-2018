package readAndWrite;

import javax.swing.Timer;

import utils.kinect.ShowSkeletonAbstract;
import utils.listeners.EventListenerAbstract;
import viewer.Viewer3D;

/**
 * Initialise l'affichage du squelette et le maintient � jour � l'aide des nouvelles donn�es de positions et de si on est en enregistrement ou en lecture de fichier.
 */
public final class ShowSkeletonReadAndWrite extends ShowSkeletonAbstract {

	private final KinectReadAndWriteInterface kinectReadAndWriteInterface;

	/**
	 * Lance une toile OpenGL.
	 * <p>
	 * Ajoute les listeners pour dessiner sur la toile, et pour pouvoir faire pivoter le squelette.
	 */
	public ShowSkeletonReadAndWrite(final KinectReadAndWriteInterface kinectReadAndWriteInterface) {
		super(kinectReadAndWriteInterface);

		this.kinectReadAndWriteInterface = kinectReadAndWriteInterface;

		new EventListener(getViewer());

		getViewer().init(kinectReadAndWriteInterface.getKinectFrameInterface().getPanel(), "cell 0 4, span 4, align 50% 50%, gaptop 100");
		kinectReadAndWriteInterface.getKinectFrameInterface().getPanel().add(getScrollbar(), "cell 0 5, span 4, align 50% 50%");

		getViewer().setVisible(false);
		getScrollbar().setVisible(false);
	}

	private final class EventListener extends EventListenerAbstract {

		public EventListener(final Viewer3D viewer) {
			super(viewer);
			TimerListener timerListener = new TimerListener(this);
			Timer timer = new Timer(25,timerListener);
			timerListener.setTimer(timer);
		}

		/**
		 * Permet d'afficher le contenu de la toile, qui gr�ce au Timer varie dynamiquement avec les variations du tableau positions et le fait que l'on souhaite afficher un fichier enregistr� ou un fichier que l'on est en train d'enregistrer.
		 */
		@Override
		public void display() {
			if(kinectReadAndWriteInterface.getSaving()) {
				displayConstruct(ShowSkeletonReadAndWrite.this);

				if(kinectReadAndWriteInterface.getCountFramesSaved() < kinectReadAndWriteInterface.getCountFrames()) {
					kinectReadAndWriteInterface.getPositionsAndStatusSaving().add(kinectReadAndWriteInterface.getPositionsAndStatus().clone());
					kinectReadAndWriteInterface.setCountFramesSaved(kinectReadAndWriteInterface.getCountFrames());
				}
			}
			if(kinectReadAndWriteInterface.getShowing()) {
				kinectReadAndWriteInterface.setPositions(kinectReadAndWriteInterface.getPositionsOpen().get(kinectReadAndWriteInterface.getCounter()).getPositions());
				displayConstruct(ShowSkeletonReadAndWrite.this);

				if(kinectReadAndWriteInterface.getCounterCurrent() != kinectReadAndWriteInterface.getCounter()) {
					kinectReadAndWriteInterface.setCounterCurrent(kinectReadAndWriteInterface.getCounter());
					kinectReadAndWriteInterface.getTimer().start();
				}
			}
		}
	}
}