package buttons;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.Timer;

import readAndWrite.KinectFrameInterface;
import readAndWrite.KinectReadAndWriteInterface;
import utils.ComboItem;
import utils.UserErrorHandler;

public final class SaveValidateButton extends JButton {

	private static final long serialVersionUID = -1708770906082475214L;

	private final KinectFrameInterface kinectFrameInterface;
	
	public SaveValidateButton(final KinectFrameInterface kinectFrameInterface, final JFrame kinectFrame) {
		super("Terminer la sauvegarde");
		this.kinectFrameInterface = kinectFrameInterface;
		setVisible(false);
		setMinimumSize(new Dimension(250, 20));
		setMaximumSize(new Dimension(250, 20));
		addActionListener(new Listener());
	}

	/**
	 * Définie ce qu'il se passe lorsqu'on appuie sur le bouton "Terminer la sauvegarde" pour arrêter de sauvegarder un fichier de squelette.
	 * <p>
	 * On arrête de sauvegarder, on enlève l'affichage de la toile, on arrête la Kinect, puis on propose à l'utilisateur de choisir un fichier où enregistrer la session.
	 */
	private final class Listener implements ActionListener {

		private final JProgressBar progressBar;
		private final JDialog dialog;

		public Listener() {
			progressBar = new JProgressBar(0,100);
			progressBar.setValue(0);
			progressBar.setStringPainted(true); 
			progressBar.setIndeterminate(true);
			dialog = new JDialog();
			dialog.setSize(new Dimension(600,200));
			dialog.setTitle("Saving");
			dialog.add(progressBar);
			dialog.setVisible(false);
		}

		@Override
		public void actionPerformed(final ActionEvent e) {
			boolean saving = false;
			final KinectReadAndWriteInterface kinect = kinectFrameInterface.getKinectReadAndWrite();
			kinect.unsetSaving();
			kinect.unsetVisible();
			kinect.stop();
			final List<ComboItem> positionsAndStatusSaving = kinect.getPositionsAndStatusSaving();
			if(positionsAndStatusSaving.isEmpty()) {
				new UserErrorHandler("Aucun mouvement à enregistrer n'a été détecté");
			} else {
				final JFileChooser saveFile = new JFileChooser((String)kinectFrameInterface.getSaveAndOpenPath());
				final int retrival = saveFile.showSaveDialog(null);
				if(retrival == JFileChooser.APPROVE_OPTION) {
					final File fileSave = saveFile.getSelectedFile();
					kinectFrameInterface.setSaveAndOpenPath(fileSave.getAbsoluteFile().getParent());
					String filePath = fileSave.getPath();
					final String filename = fileSave.getName();

					if(filename.length()>4) {
						if(!filename.substring(filename.length() - 4).equals(".txt")) {
							filePath = filePath + ".txt";
						}
					} else {
						if(filename.equals(".txt")) {
							filePath = filePath.substring(0, filePath.length() - 4) + "Squelette Kinect.txt";
						} else {
							filePath = filePath + ".txt";
						}
					}

					FileWriter fw = null;
					try {
						final File f = new File(filePath);
						boolean valid = false;
						if(f.exists()) {
							final int result = JOptionPane.showOptionDialog(null, "Un fichier avec ce nom existe déjà. Voulez-vous le remplacer?", null, -1, JOptionPane.QUESTION_MESSAGE, new ImageIcon(), new String[]{"Oui", "Non"}, null);
							if(result == 0) {
								valid = true;
							}
						} else {
							valid = true;
						}
						if(valid) {
							fw = new FileWriter(filePath);
							fw.write("Enregistrement d'un squelette Kinect" + System.lineSeparator());
							dialog.setVisible(true);
							final SavingCurrent savingCurrent = new SavingCurrent(progressBar, positionsAndStatusSaving, fw);
							final Timer timer = new Timer(1000, null);
							timer.addActionListener(new ListenerTimer(kinectFrameInterface, savingCurrent, timer, dialog, progressBar));
							timer.start();
							kinectFrameInterface.getSaveValidate().setVisible(false);
							saving = true;
							savingCurrent.execute();
						}
					} catch(final IOException e1) {
						new UserErrorHandler("Une erreur s'est produite lors de la sauvegarde, veuillez ressayer");
					} finally {
						if(fw != null && !saving) {
							try {
								fw.close();
							} catch(final IOException e2) {}
						}
					}
				}
			}
			if(!saving) {
				kinectFrameInterface.getOpen().setVisible(true);
				kinectFrameInterface.getSave().setVisible(true);
				kinectFrameInterface.getSaveValidate().setVisible(false);
			}
		}
	}
}