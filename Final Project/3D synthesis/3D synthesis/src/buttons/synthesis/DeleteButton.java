package buttons.synthesis;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import utils.JButtonResized;

public final class DeleteButton extends JButtonResized {

	private static final long serialVersionUID = 5939011112694153493L;

	private final MoveFileButton moveFile;

	public DeleteButton(final MoveFileButton moveFile) {
		super("Supprimer ce fichier");
		this.moveFile = moveFile;
		setIcon("cross.png");
		addActionListener(new Listener());
	}

	private final class Listener implements ActionListener {
		@Override
		public void actionPerformed(final ActionEvent e) {
			moveFile.getSynthesis().remake(moveFile.getSynthesis().getMoves().indexOf(moveFile));
		}
	}
}