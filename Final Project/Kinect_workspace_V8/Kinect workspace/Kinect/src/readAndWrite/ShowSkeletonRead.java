package readAndWrite;

import javax.swing.Timer;

import utils.kinect.KinectReadInterface;
import utils.kinect.ShowSkeletonAbstract;
import utils.listeners.EventListenerAbstract;
import viewer.Viewer3D;

/**
 * Initialise l'affichage du squelette et le maintient � jour � l'aide des nouvelles donn�es de positions.
 */
public final class ShowSkeletonRead extends ShowSkeletonAbstract {

	private final KinectReadInterface kinectReadInterface;

	/**
	 * Lance une toile OpenGL.
	 * <p>
	 * Ajoute les listeners pour dessiner sur la toile, et pour pouvoir faire pivoter le squelette.
	 */
	public ShowSkeletonRead(final KinectReadInterface kinectReadInterface) {
		super(kinectReadInterface);

		this.kinectReadInterface = kinectReadInterface;

		new EventListener(getViewer());
		
		getViewer().init(kinectReadInterface.getKinectFrameInterface().getPanel(), "cell 0 4, span 4, align 50% 50%, gaptop 100");
		kinectReadInterface.getKinectFrameInterface().getPanel().add(getScrollbar(), "cell 0 5, span 4, align 50% 50%");
		
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
		 * Permet d'afficher le contenu de la toile, qui gr�ce au Timer varie dynamiquement avec les variations du tableau positions.
		 */
		@Override
		public void display() {
			if(kinectReadInterface.getShowing()) {
				kinectReadInterface.setPositions(kinectReadInterface.getPositionsOpen().get(kinectReadInterface.getCounter()).getPositions());
				displayConstruct(ShowSkeletonRead.this);

				if(kinectReadInterface.getCounterCurrent() != kinectReadInterface.getCounter()) {
					kinectReadInterface.setCounterCurrent(kinectReadInterface.getCounter());
					kinectReadInterface.getTimer().start();
				}
			}
		}
	}
}