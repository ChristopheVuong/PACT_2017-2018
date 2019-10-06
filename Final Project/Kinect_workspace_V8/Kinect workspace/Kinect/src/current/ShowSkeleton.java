package current;

import javax.swing.JFrame;
import javax.swing.Timer;

import utils.kinect.ShowSkeletonAbstract;
import utils.listeners.EventListenerAbstract;
import viewer.Viewer3D;

/**
 * Initialise l'affichage du squelette et le maintient � jour � l'aide des nouvelles donn�es de positions.
 */
public final class ShowSkeleton extends ShowSkeletonAbstract {

	/**
	 * Lance une toile OpenGL.
	 * <p>
	 * Ajoute les listeners pour dessiner sur la toile, et pour pouvoir faire pivoter le squelette.
	 */
	public ShowSkeleton(final KinectInterface kinectInterface) {
		super(kinectInterface);

		new EventListener(getViewer());

		final JFrame frame = new JFrame("Show Skeleton");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getViewer().init(frame.getContentPane());
		frame.setSize(frame.getContentPane().getPreferredSize());
		frame.setVisible(true);
	}

	private class EventListener extends EventListenerAbstract {

		public EventListener(final Viewer3D viewer) {
			super(viewer);
			TimerListener timerListener = new TimerListener(this);
			Timer timer = new Timer(25,timerListener);
			timerListener.setTimer(timer);
		}

		/**
		 * Permet d'afficher le contenu de la toile, qui gr�ce � l'Animator varie dynamiquement avec les variations du tableau positions.
		 * <p>
		 * Effectue les �ventuels changements de vue du squelette.
		 * <p>
		 * Trace tous les segments liants les points du squelette.
		 */
		@Override
		public void display() {
			displayConstruct(ShowSkeleton.this);
		}
	}
}