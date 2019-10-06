package utils.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import edu.ufl.digitalworlds.j4k.Skeleton;
import utils.Constants;
import utils.kinect.ShowSkeletonInterface;
import viewer.Cylinder;
import viewer.Linef;
import viewer.Sphere;
import viewer.Vertexf3D;
import viewer.ViewerInterface;

public abstract class EventListenerAbstract {

	private final ViewerInterface viewer;
	
	public EventListenerAbstract(final ViewerInterface viewer) {
		this.viewer = viewer;
	}
	
	/**
	 * Dessine le segment entre 2 points du squelette sur la toile OpenGL. (avec zoomFactor)
	 */
	private final void drawLine(final int point1, final int point2, final Vertexf3D[] positions) {
		viewer.addDraws(new Linef(positions[point1],positions[point2]));
	}

	/**
	 * Dessine une sphere de centre center et dont point appartient � la sph�re. (avec zoomFactor)
	 */
	private final void drawSphere(final int center, final int point, final Vertexf3D[] positions) {
		viewer.addDraws(new Sphere(positions[center],(2f/3f)*norme3D(positions[center], positions[point])));
	}

	/**
	 * Dessine un cylindre dont top et base sont les milieux des 2 bases, de rayon radius. (avec zoomFactor)
	 */
	private final void drawCylinder(final int base, final int top, final Vertexf3D[] positions, final float radius) {
		viewer.addDraws(new Cylinder(positions[base],positions[top],radius));
	}

	public void display() {}
	
	/**
	 * Met � jour le squelette affich�.
	 * <p>
	 * Effectue les �ventuels changements de vue du squelette.
	 * <p>
	 * Trace tous les segments liants les points du squelette.
	 */
	public final void displayConstruct(final ShowSkeletonInterface showSkeletonInterface) {
		viewer.flush();
		
		final float[] positions = showSkeletonInterface.getKinectInterface().getPositionsAndStatus().getPositions();
		final Vertexf3D[] positionsVertex = new Vertexf3D[20];
		for(int i = 0; i<20; i++) {
			positionsVertex[i] = new Vertexf3D(positions[3*i], positions[3*i+1], positions[3*i+2]);
		}

		int point1;
		int point2;

		/*for(final int[] limb : Constants.limbs) {
			point1 = limb[0];
			point2 = limb[1];
			drawLine(point1, point2, positionsVertex);
		}*/

		for(final int[] limb : Constants.limbsSphere) {
			point1 = limb[0];
			point2 = limb[1];
			drawSphere(point1, point2, positionsVertex);
		}

		for(final int[] limb : Constants.limbsCylinder0) {
			point1 = limb[0];
			point2 = limb[1];
			drawCylinder(point1, point2, positionsVertex, 0.1f);
		}

		for(final int[] limb : Constants.limbsCylinder1) {
			point1 = limb[0];
			point2 = limb[1];
			drawCylinder(point1, point2, positionsVertex, 0.05f);
		}

		for(final int[] limb : Constants.limbsCylinder2) {
			point1 = limb[0];
			point2 = limb[1];
			drawCylinder(point1, point2, positionsVertex, 0.02f);
		}
		
		viewer.updateDisplay(positionsVertex[Skeleton.SPINE_BASE]);
	}
	
	private static final float norme3D(final Vertexf3D vertex1, final Vertexf3D vertex2) {
		return (float)Math.sqrt((vertex2.getX() - vertex1.getX())*(vertex2.getX() - vertex1.getX()) + (vertex2.getY() - vertex1.getY())*(vertex2.getY() - vertex1.getY()) + (vertex2.getZ() - vertex1.getZ())*(vertex2.getZ() - vertex1.getZ()));
	}
	
	public class TimerListener implements ActionListener {
		
		private final EventListenerAbstract eventListener;
		private Timer timer;
		
		public TimerListener(final EventListenerAbstract eventListener) {
			this.eventListener = eventListener;
		}
		
		public void setTimer(final Timer timer) {
			this.timer = timer;
			timer.setRepeats(false);
			timer.start();
		}
		
		public void actionPerformed(final ActionEvent e) {
			eventListener.display();
			if(timer != null) {
				timer.start();
			}
		}
	}
}