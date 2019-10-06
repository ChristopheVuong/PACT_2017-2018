import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.PlainDocument;

import current.Kinect;
import miginfocom.swing.MigLayout;
import readAndWrite.KinectFrame;
import utils.PositiveIntegerFilter;
import utils.UserErrorHandler;

/**
 * Lance le programme, permet de choisir le mode de fonctionnement.
 */
public final class Main {
	
	public static void main(final String[] args) {
		final int mode = JOptionPane.showOptionDialog(null, null, "Choix du mode de fonctionnement", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, new ImageIcon(), new String[]{"Utiliser la Kinect", "Ne pas utiliser de Kinect"}, null);
		if(mode == 0) {
			String folderPath = null;
			boolean error = false;
			final int optionImage = JOptionPane.showOptionDialog(null, null, "Choix du mode de fonctionnement", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, new ImageIcon(), new String[]{"Sauvegarder des images aléatoirement lors de la session", "Ne pas sauvegarder d'images lors de la session"}, null);
			if(optionImage == 0) {
				final JFileChooser saveFolder = new JFileChooser();
				saveFolder.setDialogTitle("Choisissez le dossier où stocker vos images");
				saveFolder.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				saveFolder.setAcceptAllFileFilterUsed(false);
				final int retrival = saveFolder.showSaveDialog(null);
				if(retrival == JFileChooser.APPROVE_OPTION) {
					final File folderSave = saveFolder.getSelectedFile();
					folderPath = folderSave.getPath() + "\\";
				} else {
					error = true;
					new UserErrorHandler("Une erreur s'est produite lors de la sauvegarde, veuillez ressayer");
				}
			}
			
			if(!error) {
				final int option = JOptionPane.showOptionDialog(null, null, "Choix du mode de fonctionnement", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, new ImageIcon(), new String[]{"Affichage uniquement de la session en cours", "Lecture et enregistrement de fichier de squelette en mouvement"}, null);
				switch(option) {
					case 0:
						final JPanel panel = new JPanel(new MigLayout());
						final JLabel label = new JLabel("Entrez le temps de fonctionnement (en s) :");
						final JTextField textField = new JTextField("");
						((PlainDocument)textField.getDocument()).setDocumentFilter(new PositiveIntegerFilter());
						panel.add(label, "cell 0 0");
						panel.add(textField, "cell 0 1, grow, gapbottom 30");
						final int optionTime = JOptionPane.showOptionDialog(null, panel, "Temps de fonctionnement", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, new ImageIcon(), new String[]{"Ok"}, null);
						final String result = textField.getText();
						if(!result.isEmpty() && optionTime == 0) {
							new Kinect(Integer.parseInt(result), folderPath);
						}
						break;
					case 1:
						new KinectFrame(KinectFrame.KINECT, folderPath);
						break;
				}
			}
		}
		if(mode == 1) {
			new KinectFrame(KinectFrame.NOKINECT, null);
		}
	}
}