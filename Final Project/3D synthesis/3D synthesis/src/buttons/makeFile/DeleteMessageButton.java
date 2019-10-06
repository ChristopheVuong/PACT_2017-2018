package buttons.makeFile;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import utils.JButtonResized;
import utils.MessageComboItem;

public final class DeleteMessageButton extends JButtonResized {

	private static final long serialVersionUID = 5939011112694153493L;

	private final MoveFileMakeButton moveFile;
	private MessageComboItem message;

	public DeleteMessageButton(final MoveFileMakeButton moveFile) {
		super("Supprimer ce message d'erreur");
		this.moveFile = moveFile;
		setIcon("cross.png");
		addActionListener(new Listener());
		setVisible(false);
	}

	public void init(final MessageComboItem message) {
		this.message = message;
	}

	private final class Listener implements ActionListener {
		@Override
		public void actionPerformed(final ActionEvent e) {
			if(message != null) {
				moveFile.getMessages().remove(message);
				moveFile.getPanel().remove(message.getMessageLevel());
				moveFile.getPanel().remove(message.getMessageName());
				moveFile.getPanel().remove(message.getDeleteMessageButton());
				moveFile.getPanel().revalidate();
			}
		}
	}
}