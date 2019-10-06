package utils.kinect;

import java.util.List;

import javax.swing.Timer;

import current.KinectInterface;
import readAndWrite.KinectFrameInterface;
import utils.ComboItem;

/**
 * Contient les méthodes à implémenter par KinectFrame pour être utilisé par ShowSkeletonRead
 */
public interface KinectReadInterface extends KinectInterface {
	public KinectFrameInterface getKinectFrameInterface();

	public boolean getShowing();
	public boolean getPause();
	public void setPause(boolean pause);
	public void changePause();
	public int getResave();
	public void changeResave();
	public int getCounterCurrent();
	public void setCounterCurrent(int counterCurrent);
	public int getCounter();
	public void setCounter(int counter);
	public Timer getTimer();
	public List<ComboItem> getPositionsOpen();
	public void setPositionsOpen(List<ComboItem> positionsOpen);
	public void setPositions(float[] positions);
	
	public void unsetShowing();
	public void unsetVisible();
	
	public void beginShowing(final int timeBetweenFrames, final List<ComboItem> positionsOpen);
}