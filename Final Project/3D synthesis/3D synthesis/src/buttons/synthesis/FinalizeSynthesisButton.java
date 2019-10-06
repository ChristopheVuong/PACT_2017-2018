package buttons.synthesis;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import buttons.SynthesisButton;
import utils.ButtonSized;
import utils.ComboItem;
import utils.DTWImplementation;
import utils.Handlers;
import utils.UserErrorHandler;

public final class FinalizeSynthesisButton extends ButtonSized {

	private static final long serialVersionUID = 3021880817462401566L;

	private final SynthesisButton synthesis;

	public FinalizeSynthesisButton(final SynthesisButton synthesis) {
		super("Synthétiser le mouvement",300,20);
		this.synthesis = synthesis;
		addActionListener(new Listener());
		synthesis.getFrame().getPanel().add(this, "cell 0 " + 30000 + ", span 3, align 50% 50%, gapbottom 25px");
		synthesis.getFrame().getPanel().revalidate();
		setVisible(false);
	}

	private void synthesis(final List<MoveFileButton> moves, final File f) {
		final int len = moves.size() - 1;
		final List<ComboItem>[] movesList = Handlers.makeMovesList(moves);

		int min = 0;
		float minDTW = 0f;
		for(int i = 0; i<len; i++) {
			if(!movesList[i].isEmpty()) {
				float DTW = 0f;
				for(int j = 0; j<len; j++) {
					if(i != j) {
						if(!movesList[j].isEmpty()) {
							DTW += DTWImplementation.sumDTW(movesList[i],movesList[j]);
						}
					}
				}
				if(minDTW > DTW) {
					minDTW = DTW;
					min = i;
				}
			}
		}

		try {
			Handlers.copyFile(moves.get(min).getFileCopy(),f);
		} catch(final IOException e) {
			f.delete();
			new UserErrorHandler("Une erreur s'est produite lors de la sauvegarde, veuillez réessayer");
		}
		for(int i = 0; i<len; i++) {
			moves.get(i).closeStreams();
		}
	}

	private final class Listener implements ActionListener {

		@Override
		public void actionPerformed(final ActionEvent e) {
			final JFileChooser saveFile = new JFileChooser(synthesis.getFrame().getSaveAndOpenPath());
			final int retrival = saveFile.showSaveDialog(null);
			if(retrival == JFileChooser.APPROVE_OPTION) {
				final File fileSave = saveFile.getSelectedFile();
				synthesis.getFrame().setSaveAndOpenPath(fileSave.getAbsoluteFile().getParent());
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
					synthesis(synthesis.getMoves(),f);
					synthesis.returnToMain();
				}
			}
		}
	}
}