package utils.listeners;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import javax.imageio.IIOException;

import viewer.ViewerListenerElements;

public final class MouseMotionListenerImplementation implements MouseMotionListener {

	private final ViewerListenerElements viewer;
	
	private float currentX;
	private float currentY;
	private float currentAlpha;
	
	
	
	public MouseMotionListenerImplementation(final ViewerListenerElements viewer) {
		this.viewer = viewer;
	}

	
	
	/**
	 * Permet de faire pivoter le squelette.
	 * <p>
	 * Permet lorsque seulement le bouton gauche de la souris est appuy�:
	 * <p>
	 * - de faire tourner la toile selon l'axe (OX) (initialement horizontal orient� vers la droite) en bougeant la souris selon l'axe (OY)
	 * <p>
	 * - de faire tourner la toile selon l'axe (OY) (initialement vertical orient� vers le haut) en bougeant la souris selon l'axe (OX)
	 * <p>
	 * Permet lorsque seulement le bouton droite de la souris est appuy�:
	 * <p>
	 * - de faire tourner la toile selon l'axe (OZ) (tel que cet axe compl�te le rep�re direct correspondant (O,x,y,z)) en faisant pivoter la souris autour du centre O (la variation de l'angle du complexe associ� aux coordonn�es caract�rise cette rotation)
	 */
	@Override
	public void mouseDragged(final MouseEvent e) {
		final int buttonsPressed = e.getModifiersEx();
		final int leftClick = MouseEvent.BUTTON1_DOWN_MASK;
		final int rightClick = MouseEvent.BUTTON3_DOWN_MASK;
		if(buttonsPressed == leftClick) {
			if(viewer.getLeftDragInitialized()) {
				final float tempX = e.getX();
				final float tempY = e.getY();
				viewer.getModelView().rotateY(tempX-currentX);
				viewer.getModelView().rotateX(currentY-tempY);
				currentX = tempX;
				currentY = tempY;
				
			} else {
				currentX = e.getX();
				currentY = e.getY();
				viewer.setLeftDragInitialized(true);
			}
		}
		if(buttonsPressed == rightClick) {
			if(viewer.getRightDragInitialized()) {
				final float tempX = e.getX()-200;
				final float tempY = -(float)(e.getY()-200);
				final float tempAlpha = (float)Math.atan2(tempY,tempX);
				final float tempAlphaDiff = tempAlpha - currentAlpha;
				if(Math.abs(tempAlphaDiff) < Math.PI) {
					viewer.getModelView().rotateZ(tempAlphaDiff*200f/(float)Math.PI);
				} else {
					if(tempAlphaDiff < 0) {
						viewer.getModelView().rotateZ((2*(float)Math.PI + tempAlphaDiff)*200f/(float)Math.PI);
					} else {
						viewer.getModelView().rotateZ((-(float)2*(float)Math.PI + tempAlphaDiff)*200f/(float)Math.PI);
					}
				}
				currentAlpha = tempAlpha;
			} else {
				currentX = e.getX();
				currentY = e.getY();
				currentAlpha = (float)Math.atan2(currentY,currentX);
				viewer.setRightDragInitialized(true);
			}
		}
	}

	@Override
	public void mouseMoved(final MouseEvent e) {}
}
