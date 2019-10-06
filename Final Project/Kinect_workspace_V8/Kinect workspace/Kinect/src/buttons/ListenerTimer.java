package buttons;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;
import javax.swing.JProgressBar;
import javax.swing.Timer;

import readAndWrite.KinectFrameInterface;

/**
 * Action Listener pour apporter les modifications souhaitées à la fin de la sauvegarde
 */
public final class ListenerTimer implements ActionListener {
	
	private final KinectFrameInterface kinectFrameInterface;
	private final Timer timer;
	private final SavingCurrent savingCurrent;
	private final JDialog dialog;
	private final JProgressBar progressBar;
	private final boolean visibility;
	
	public ListenerTimer(final KinectFrameInterface kinectFrameInterface, final SavingCurrent savingCurrent, final Timer timer, final JDialog dialog, final JProgressBar progressBar, final boolean visibility) {
		this.kinectFrameInterface = kinectFrameInterface;
		this.savingCurrent = savingCurrent;
		this.timer = timer;
		this.dialog = dialog;
		this.progressBar = progressBar;
		this.visibility = visibility;
	}
	
	public ListenerTimer(final KinectFrameInterface kinectFrameInterface, final SavingCurrent savingCurrent, final Timer timer, final JDialog dialog, final JProgressBar progressBar) {
		this(kinectFrameInterface, savingCurrent, timer, dialog, progressBar, true);
	}

	@Override
	public void actionPerformed(final ActionEvent e) {
		if(savingCurrent.isDone()) {
			if(visibility) {
				kinectFrameInterface.getOpen().setVisible(true);
				kinectFrameInterface.getSave().setVisible(true);
			}
			dialog.setVisible(false);
			progressBar.setValue(0);
			timer.stop();
		}
	}
}