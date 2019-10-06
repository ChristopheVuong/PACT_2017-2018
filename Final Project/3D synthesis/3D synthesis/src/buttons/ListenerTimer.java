package buttons;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;
import javax.swing.JProgressBar;
import javax.swing.Timer;

public final class ListenerTimer implements ActionListener {

	private final Timer timer;
	private final OpeningCurrent openingCurrent;
	private final JDialog dialog;
	private final JProgressBar progressBar;

	public ListenerTimer(final OpeningCurrent openingCurrent, final Timer timer, final JDialog dialog, final JProgressBar progressBar) {
		this.openingCurrent = openingCurrent;
		this.timer = timer;
		this.dialog = dialog;
		this.progressBar = progressBar;
	}

	@Override
	public void actionPerformed(final ActionEvent e) {
		if(openingCurrent.isDone()) {
			dialog.setVisible(false);
			progressBar.setValue(0);
			timer.stop();
		}
	}
}