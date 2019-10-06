package buttons;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import readAndWrite.KinectFrame;
import readAndWrite.KinectFrameInterface;
import utils.kinect.KinectReadInterface;

public final class OpenValidateButton extends JButton {

	private static final long serialVersionUID = 5420946146787630824L;
	
	private final KinectFrameInterface kinectFrameInterface;

	public OpenValidateButton(final KinectFrameInterface kinectFrameInterface) {
		super("Arr�ter de jouer ce fichier");
		this.kinectFrameInterface = kinectFrameInterface;
		setVisible(false);
		setMinimumSize(new Dimension(200, 20));
		setMaximumSize(new Dimension(200, 20));
		addActionListener(new Listener());
	}

	/**
	 * D�finie ce qu'il se passe lorsqu'on appuie sur le bouton "Arr�ter de jouer ce fichier" pour arr�ter de jouer un fichier de squelette.
	 * <p>
	 * On arr�te de jouer le fichier, on enl�ve l'affichage de la toile puis on remet en place les boutons initiaux.
	 */
	private final class Listener implements ActionListener {
		@Override
		public void actionPerformed(final ActionEvent e) {
			final KinectReadInterface kinectReadInterface = kinectFrameInterface.getKinectRead();
			kinectReadInterface.unsetShowing();
			kinectReadInterface.unsetVisible();
			if(kinectFrameInterface.getMode() == KinectFrame.KINECT) {
				kinectFrameInterface.getSave().setVisible(true);
			}
			kinectFrameInterface.getOpen().setVisible(true);
			kinectFrameInterface.getOpenPause().setVisible(false);
			kinectFrameInterface.getOpenResave().setVisible(false);
			kinectFrameInterface.getOpenValidate().setVisible(false);
		}
	}
}