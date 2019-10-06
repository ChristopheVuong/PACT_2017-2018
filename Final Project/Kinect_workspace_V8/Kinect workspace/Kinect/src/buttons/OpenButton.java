package buttons;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.PlainDocument;

import miginfocom.swing.MigLayout;
import readAndWrite.KinectFrame;
import readAndWrite.KinectFrameInterface;
import utils.ComboItem;
import utils.PositiveIntegerFilter;
import utils.UserErrorHandler;

public final class OpenButton extends JButton {

	private static final long serialVersionUID = -8919053683164064051L;
	
	private final KinectFrameInterface kinectFrameInterface;

	public OpenButton(final KinectFrameInterface kinectFrameInterface) {
		super("Ouvrir un fichier");
		this.kinectFrameInterface = kinectFrameInterface;
		setMinimumSize(new Dimension(200, 20));
		setMaximumSize(new Dimension(200, 20));
		addActionListener(new Listener());
	}

	/**
	 * Méthode permettant de décoder les squelettes récupérés en sauvegardant, pour les afficher dans l'interface (on ne garde que 20 points et pas les 5 derniers que l'on suppose vide dans cette application)
	 *
	 * @param positionsOpen chaîne de caractères contenant une frame (positions de l'ensemble des points du squelette à un instant t)
	 * @return arrayFloat tableau contenant les positions des points du squelette de la frame décodée
	 */
	private float[] decodeLine(final String positionsOpen) {
		final String build = positionsOpen.substring(1,positionsOpen.length()-1);
		final String[] array = build.split(",");

		final int len = array.length;
		if(len == 60 || len == 75) {
			final float[] arrayFloat = new float[60];
			for(int i = 0; i<60; i++) {
				arrayFloat[i] = Float.parseFloat(array[i]);
			}
			return arrayFloat;
		} else {
			if(len == 20) {
				final float[] arrayFloat = new float[20];
				for(int i = 0; i<20; i++) {
					arrayFloat[i] = Float.parseFloat(array[i]);
				}
				return arrayFloat;
			} else {
				return null;
			}
		}
	}

	/**
	 * Définie ce qu'il se passe lorsqu'on appuie sur le bouton "Ouvrir un fichier" pour ouvrir un fichier de squelette.
	 * <p>
	 * Demande à l'utilisateur d'entrer combien de temps il veut entre chaque frame (en ms).
	 * <p>
	 * Décode le fichier texte et s'il est valide lance alors l'affichage de ce dernier et donne à l'utilisateur la possibilité d'arrêter la lecture ou de la recommencer.
	 */
	private final class Listener implements ActionListener {
		@Override
		public void actionPerformed(final ActionEvent e) {
			final JFileChooser openFile = new JFileChooser((String)kinectFrameInterface.getSaveAndOpenPath());
			final FileNameExtensionFilter filter = new FileNameExtensionFilter("Fichier au format txt", "txt", "text");
			openFile.setFileFilter(filter);
			final int retrival = openFile.showOpenDialog(null);
			if(retrival == JFileChooser.APPROVE_OPTION) {
				Scanner sc = null;
				try {
					final File file = openFile.getSelectedFile();
					kinectFrameInterface.setSaveAndOpenPath(file.getAbsoluteFile().getParent());
					sc = new Scanner(file);
					if(sc.hasNextLine()) {
						String str = sc.nextLine();

						if(str.equals("Enregistrement d'un squelette Kinect")) {
							final JPanel panel = new JPanel(new MigLayout());
							final JLabel label = new JLabel("Entrez le temps que vous voulez entre chaque frame (en ms) :");
							final JTextField textField = new JTextField("");
							((PlainDocument)textField.getDocument()).setDocumentFilter(new PositiveIntegerFilter());
							panel.add(label, "cell 0 0");
							panel.add(textField, "cell 0 1, grow, gapbottom 30");
							final int option = JOptionPane.showOptionDialog(null, panel, "Choix du temps entre frames", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, new ImageIcon(), new String[]{"Ok"}, null);
							final String result = textField.getText();
							if(PositiveIntegerFilter.testFinal(result) && option == 0) {
								final int timeBetweenFrames = Integer.parseInt(result);
								kinectFrameInterface.setTimeBetweenFrames(timeBetweenFrames);
								final List<ComboItem> positionsOpen = new ArrayList<ComboItem>();
								ComboItem positionsTemp = null;
								boolean withStatus = false;
								boolean start = false;
								boolean validSqueleton =  true;

								while(sc.hasNextLine() && validSqueleton){
									str = sc.nextLine();
									final float[] arrayFloat = decodeLine(str);

									if(arrayFloat != null) {
										switch(arrayFloat.length) {
										case 20:
											if(start) {
												positionsTemp.setJointStatus(arrayFloat);
												withStatus = true;
												positionsTemp = null;
											} else {
												validSqueleton = false;
											}
											break;
										case 60:
											if(start) {
												if((withStatus && positionsTemp == null) || !withStatus) {
													positionsTemp = new ComboItem(arrayFloat, (byte[])null);
													positionsOpen.add(positionsTemp);
												}
											} else {
												start = true;
												positionsTemp = new ComboItem(arrayFloat, (byte[])null);
												positionsOpen.add(positionsTemp);
											}
											break;
										}
									} else {
										validSqueleton = false;
									}
								}
								if(validSqueleton && !positionsOpen.isEmpty()) {
									setVisible(false);
									kinectFrameInterface.setPositionsOpen(positionsOpen);
									if(kinectFrameInterface.getMode() == KinectFrame.KINECT) {
										kinectFrameInterface.getSave().setVisible(false);
									}
									kinectFrameInterface.getOpenPause().setVisible(true);
									kinectFrameInterface.getOpenResave().setVisible(true);
									kinectFrameInterface.getOpenValidate().setVisible(true);
									kinectFrameInterface.getKinectRead().beginShowing(timeBetweenFrames, positionsOpen);
								} else {
									new UserErrorHandler("Le fichier ne semble pas être au format d'enregistrement d'un squelette, veuillez en choisir un autre");
								}
							}
						} else {
							new UserErrorHandler("Le fichier ne semble pas être au format d'enregistrement d'un squelette, veuillez en choisir un autre");
						}
					} else {
						new UserErrorHandler("Le fichier ne semble pas être au format d'enregistrement d'un squelette, veuillez en choisir un autre");
					}
				} catch(final FileNotFoundException e1) {
					new UserErrorHandler("Le fichier n'a pas pu être trouvé");
				} finally {
					if(sc != null) {
						sc.close();
					}
				}
			}
		}
	}
}