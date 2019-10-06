package buttons.makeFile;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import utils.JButtonResized;

public final class DeleteMakeButton extends JButtonResized {

	private static final long serialVersionUID = 5939011112694153493L;

	private final MoveFileMakeButton moveFile;

	public DeleteMakeButton(final MoveFileMakeButton moveFile) {
		super("Supprimer ce fichier");
		this.moveFile = moveFile;
		setIcon("cross.png");
		addActionListener(new Listener());
	}

	private final class Listener implements ActionListener {
		@Override
		public void actionPerformed(final ActionEvent e) {
			moveFile.getMakeFile().remake(moveFile.getMakeFile().getMoves().indexOf(moveFile));
		}
	}
}