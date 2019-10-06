package buttons;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.Timer;

import readAndWrite.KinectFrameInterface;
import utils.UserErrorHandler;

public final class OpenResaveButton extends JButton {

	private static final long serialVersionUID = -1456426836039419007L;
	
	private final KinectFrameInterface kinectFrameInterface;
	
	private int frame1;
	private int frame2;
	

	public OpenResaveButton(final KinectFrameInterface kinectFrameInterface) {
		super("Sauvegarder une partie du fichier (entre 2 frames)");
		this.kinectFrameInterface = kinectFrameInterface;
		setVisible(false);
		setMinimumSize(new Dimension(400, 20));
		setMaximumSize(new Dimension(400, 20));
		addActionListener(new Listener());
	}

	/**
	 * Définie ce qu'il se passe lorsqu'on appuie sur le bouton "Rejour le fichier" pour rejouer un fichier de squelette.
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
			kinectFrameInterface.getKinectRead().changeResave();
			if(kinectFrameInterface.getKinectRead().getResave() == 1) {
				if(!kinectFrameInterface.getKinectRead().getPause()) {
					kinectFrameInterface.getOpenPause().doClick();
				}
				OpenResaveButton.this.setText("Choisissez une première frame");
			}
			if(kinectFrameInterface.getKinectRead().getResave() == 2) {
				if(!kinectFrameInterface.getKinectRead().getPause()) {
					kinectFrameInterface.getOpenPause().doClick();
				}
				OpenResaveButton.this.setText("Choisissez une deuxième frame");
				frame1 = kinectFrameInterface.getKinectRead().getCounter();
			}
			if(kinectFrameInterface.getKinectRead().getResave() == 3) {
				OpenResaveButton.this.setText("Lancer la sauvegarde");
				frame2 = kinectFrameInterface.getKinectRead().getCounter();
			}
			if(kinectFrameInterface.getKinectRead().getResave() == 0) {
				OpenResaveButton.this.setText("Sauvegarder une partie du fichier (entre 2 frames)");
				if(frame2<frame1) {
					final int temp = frame1;
					frame1 = frame2;
					frame2 = temp;
				}
				
				boolean saving = false;
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
							final SavingCurrent savingCurrent = new SavingCurrent(progressBar, kinectFrameInterface.getPositionsOpen().subList(frame1,frame2+1), fw);
							final Timer timer = new Timer(1000, null);
							timer.addActionListener(new ListenerTimer(kinectFrameInterface, savingCurrent, timer, dialog, progressBar, false));
							timer.start();
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
		}
	}
}