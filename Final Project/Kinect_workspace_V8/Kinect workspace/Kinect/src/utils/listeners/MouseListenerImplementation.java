package utils.listeners;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import viewer.ViewerListenerElements;
import utils.Constants;

public final class MouseListenerImplementation implements MouseListener {
	
	private final ViewerListenerElements viewer;
	
	public MouseListenerImplementation(final ViewerListenerElements viewer) {
		this.viewer = viewer;
	}
	
	@Override
	public void mouseClicked(final MouseEvent e) {}

	@Override
	public void mouseEntered(final MouseEvent e) {}

	@Override
	public void mouseExited(final MouseEvent e) {}

	/**
	 * Permet d'initialiser la position de la souris lorsque le bouton gauche ou droite est cliqué afin de pouvoir ensuite en mesurer les variations pour les rotations du squelette
	 */
	@Override
	public void mousePressed(final MouseEvent e) {
		final int buttonPressed = e.getButton();
		if(buttonPressed == Constants.leftButton) {
			viewer.setLeftDragInitialized(false);
		} else if(buttonPressed == Constants.rightButton) {
			viewer.setRightDragInitialized(false);
		}
	}

	@Override
	public void mouseReleased(final MouseEvent e) {}
}