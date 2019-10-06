package readAndWrite;

import java.util.List;

import utils.ComboItem;
import utils.kinect.KinectReadInterface;

/**
 * Contient les méthodes à implémenter par KinectFrame pour être utilisé par ShowSkeletonReadAndWrite
 */
public interface KinectReadAndWriteInterface extends KinectReadInterface {
	public boolean getSaving();
	public int getCountFramesSaved();
	public void setCountFramesSaved(int countFramesSaved);
	public int getCountFrames();
	public void incrementCountFrames();
	public List<ComboItem> getPositionsAndStatusSaving();
	
	public void unsetSaving();
	
	public void stop();
	
	public void beginSaving();
}