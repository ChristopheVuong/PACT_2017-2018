package buttons;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import readAndWrite.KinectFrameInterface;

public final class OpenPauseButton extends JButton {

	private static final long serialVersionUID = -1456426836039419007L;
	
	private final KinectFrameInterface kinectFrameInterface;

	public OpenPauseButton(final KinectFrameInterface kinectFrameInterface) {
		super("Pause");
		this.kinectFrameInterface = kinectFrameInterface;
		setVisible(false);
		setMinimumSize(new Dimension(200, 20));
		setMaximumSize(new Dimension(200, 20));
		addActionListener(new Listener());
	}

	/**
	 * Définie ce qu'il se passe lorsqu'on appuie sur le bouton "Pause" ou "Reprendre".
	 */
	private final class Listener implements ActionListener {
		@Override
		public void actionPerformed(final ActionEvent e) {
			kinectFrameInterface.getKinectRead().changePause();
			if(kinectFrameInterface.getKinectRead().getPause()) {
				OpenPauseButton.this.setText("Reprendre");
			} else {
				OpenPauseButton.this.setText("Pause");
				kinectFrameInterface.getKinectRead().getTimer().start();
			}
		}
	}
}