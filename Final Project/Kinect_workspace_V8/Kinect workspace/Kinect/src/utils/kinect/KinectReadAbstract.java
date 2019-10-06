package utils.kinect;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.Timer;

import readAndWrite.KinectFrameInterface;
import utils.ComboItem;
import utils.UserErrorHandler;
import viewer.Viewer3D;

public abstract class KinectReadAbstract implements KinectReadInterface {

	private final KinectFrameInterface kinectFrameInterface;

	private ComboItem positionsAndStatus;
	private Viewer3D viewer;
	private ScrollbarSized scrollbar;

	private int len;
	private int counter;
	private int counterCurrent;
	private Timer timer;
	private boolean showing;
	private boolean pause;
	private int resave;
	private List<ComboItem> positionsOpen;
	
	/**
	 * Permet de créer l'interface pour sauvegarder ou lire des fichiers de squelettes issus de la Kinect, en mouvement.
	 */
	public KinectReadAbstract(final KinectFrameInterface kinectFrameInterface) {
		float[] positions = new float[60];
		byte[] jointStatus = new byte[20];
		for(int i = 0; i<60; i++) {
			positions[i] = 0.0f;
		}
		for(int i = 0; i<20; i++) {
			jointStatus[i] = 0;
		}
		positionsAndStatus = new ComboItem(positions,jointStatus);
		this.kinectFrameInterface = kinectFrameInterface;
	}

	@Override
	public KinectFrameInterface getKinectFrameInterface() {
		return kinectFrameInterface;
	}

	@Override
	public ComboItem getPositionsAndStatus() {
		return positionsAndStatus;
	}

	public Viewer3D getViewer() {
		return viewer;
	}

	@Override
	public boolean getShowing() {
		return showing;
	}
	
	@Override
	public boolean getPause() {
		return pause;
	}
	
	@Override
	public void setPause(boolean pause) {
		this.pause = pause;
	}
	
	@Override
	public void changePause() {
		setPause(getPause()^true);
	}
	
	@Override
	public int getResave() {
		return resave;
	}
	
	@Override
	public void changeResave() {
		resave++;
		if(resave>3) {
			resave = 0;
		}
	}

	@Override
	public int getCounterCurrent() {
		return counterCurrent;
	}

	@Override
	public void setCounterCurrent(final int counterCurrent) {
		this.counterCurrent = counterCurrent;
	}

	@Override
	public int getCounter() {
		return counter;
	}
	
	@Override
	public void setCounter(final int counter) {
		this.counter = counter;
	}

	@Override
	public Timer getTimer() {
		return timer;
	}

	/**
	 * Permet d'obtenir le tableau de toutes les frames en cours de lecture du fichier en cours de lecture.
	 */
	@Override
	public List<ComboItem> getPositionsOpen() {
		return positionsOpen;
	};
	
	@Override
	public void setPositionsOpen(final List<ComboItem> positionsOpen) {
		this.positionsOpen = positionsOpen;
	};

	@Override
	public void setPositions(final float[] positions) {
		positionsAndStatus.setPositions(positions);
	}

	/**
	 * Termine la lecture du fichier.
	 */
	@Override
	public void unsetShowing() {
		showing = false;
	}

	/**
	 * Rend invisible la toile OpenGL.
	 */
	@Override
	public void unsetVisible() {
		viewer.setVisible(false);
		scrollbar.setVisible(false);
	}

	/**
	 * Permet de lancer une lecture de fichier de squelette en mouvement ou de relancer la lecture.
	 * <p>
	 * On initialise positionsOpen, on démarre la lecture puis on affiche la toile OpenGL.
	 */
	@Override
	public void beginShowing(final int timeBetweenFrames, final List<ComboItem> positionsOpen) {
		if(showing) {
			new UserErrorHandler("Vous ne pouvez pas relancer la lecture pendant qu'elle est en train de tourner");
		} else {
			this.positionsOpen = positionsOpen;
			len = positionsOpen.size();
			counter = 0;
			scrollbar.setMaximum(len);
			scrollbar.setValue(counter);
			counterCurrent = -1;
			timer = new Timer(timeBetweenFrames, new ActionListener() {
				@Override
				public void actionPerformed(final ActionEvent e) {
					if(!pause) {
						if(counter<len-1) {
							counter++;
							scrollbar.setValue(counter);
						} else {
							kinectFrameInterface.getOpenPause().doClick();
						}
					}
				}
			});
			timer.setRepeats(false);
			showing = true;
			if(pause) {
				kinectFrameInterface.getOpenPause().doClick();
			}
			viewer.setVisible(true);
			scrollbar.setVisible(true);
		}
	}
	
	public void setSkeleton(final ShowSkeletonAbstract showSkeleton) {
		viewer = showSkeleton.getViewer();
		scrollbar = showSkeleton.getScrollbar();
		scrollbar.setKinectRead(this);
	}
}