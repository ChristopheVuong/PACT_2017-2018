package buttons;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.text.PlainDocument;

import miginfocom.swing.MigLayout;
import readAndWrite.KinectFrameInterface;
import utils.PositiveIntegerFilter;

public final class SaveButton extends JButton {

	private static final long serialVersionUID = -681900416773300992L;
	
	private final KinectFrameInterface kinectFrameInterface;

	public SaveButton(final KinectFrameInterface kinectFrameInterface) {
		super("Sauvegarder un fichier");
		this.kinectFrameInterface = kinectFrameInterface;
		addActionListener(new Listener());
		setMinimumSize(new Dimension(200, 20));
		setMaximumSize(new Dimension(200, 20));
	}

	/**
	 * Définie ce qu'il se passe lorsqu'on appuie sur le bouton "Sauvegarder un fichier" pour lancer l'enregistrement d'un fichier de squelette.
	 * <p>
	 * Demande à l'utilisateur d'entrer combien de temps il veut que l'enregistrement dur au maximum (en s).
	 * <p>
	 * Affiche le bouton pour arrêter l'enregistrement si l'utilisateur le souhaite.
	 * <p>
	 * Lance la sauvegarde du fichier et l'affichage du squelette en cours.
	 */
	private final class Listener implements ActionListener {
		@Override
		public void actionPerformed(final ActionEvent e) {
			final JPanel panel = new JPanel(new MigLayout());
			final JLabel label = new JLabel("Entrez le temps en s que vous voulez que l'enregistrement dur au maximum :");
			final JTextField textField = new JTextField("");
			((PlainDocument)textField.getDocument()).setDocumentFilter(new PositiveIntegerFilter());
			panel.add(label, "cell 0 0");
			panel.add(textField, "cell 0 1, grow, gapbottom 30");
			final int option = JOptionPane.showOptionDialog(null, panel, "Choix du temps maximal d'enregistrement", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, new ImageIcon(), new String[]{"Ok"}, null);
			final String result = textField.getText();
			if(PositiveIntegerFilter.testFinal(result) && option == 0) {
				final int timeOut = Integer.parseInt(result);
				kinectFrameInterface.getOpen().setVisible(false);
				kinectFrameInterface.getSave().setVisible(false);
				kinectFrameInterface.getSaveValidate().setVisible(true);
				kinectFrameInterface.getKinectReadAndWrite().beginSaving();
				final Timer timer = new Timer(timeOut*1000, new ActionListener() {
					@Override
					public void actionPerformed(final ActionEvent e) {
						kinectFrameInterface.getSaveValidate().doClick();
					}
				});
				timer.setRepeats(false);
				timer.start();
			}
		}
	}
}