package readAndWrite;

import java.util.List;

import buttons.OpenButton;
import buttons.OpenPauseButton;
import buttons.OpenResaveButton;
import buttons.OpenValidateButton;
import buttons.SaveButton;
import buttons.SaveValidateButton;
import utils.ComboItem;
import utils.PanelSized;
import utils.kinect.KinectReadInterface;

/**
 * Contient les méthodes à implémenter par KinectFrame pour être utilisé par les boutons et KinectReadAndWrite
 */
public interface KinectFrameInterface {
	public int getMode();
	public PanelSized getPanel();
	
	public void setTimeBetweenFrames(int timeBetweenFrames);
	public int getTimeBetweenFrames();
	public void setPositionsOpen(List<ComboItem> positionsOpen);
	public List<ComboItem> getPositionsOpen();
	
	public OpenButton getOpen();
	public OpenPauseButton getOpenPause();
	public OpenResaveButton getOpenResave();
	public OpenValidateButton getOpenValidate();
	public SaveButton getSave();
	public SaveValidateButton getSaveValidate();
	public KinectReadInterface getKinectRead();
	public KinectReadAndWriteInterface getKinectReadAndWrite();
	
	public String getSaveAndOpenPath();
	public void setSaveAndOpenPath(String saveAndOpenPath);
}