package buttons.makeFile;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTextField;

import utils.ButtonSized;
import utils.MessageComboItem;

public final class AddMessageButton extends ButtonSized {

	private static final long serialVersionUID = 2561570461401500095L;

	private final MoveFileMakeButton moveFile;

	public AddMessageButton(final MoveFileMakeButton moveFile) {
		super("Ajouter un message",300,20);
		this.moveFile = moveFile;
		addActionListener(new Listener());
		moveFile.getPanel().add(this, "cell 0 " + 30000 + ", align 50% 50%, gapbottom 40px");
		moveFile.getPanel().revalidate();
	}

	private final class Listener implements ActionListener {
		@Override
		public void actionPerformed(final ActionEvent e) {
			if(moveFile.getMessages().size() > 0) {
				moveFile.getMessages().get(moveFile.getMessages().size()-1).getDeleteMessageButton().setVisible(true);
			}
			final JTextField messageLevel = new MessageLevel();
			final JTextField messageName = new MessageName();
			final DeleteMessageButton deleteMessage = new DeleteMessageButton(moveFile);
			final MessageComboItem message = new MessageComboItem(messageLevel,messageName,deleteMessage);
			deleteMessage.init(message);
			moveFile.getMessages().add(message);
			final int temp = 2*(moveFile.getCounter()+1);
			moveFile.getPanel().add(messageLevel, "cell 0 " + temp + ", align 50% 50%");
			moveFile.getPanel().add(deleteMessage, "cell 0 " + temp + ", align 50% 50%, gapbottom 10px, hidemode 2");
			moveFile.getPanel().add(messageName, "cell 0 " + (temp+1) + ", align 50% 50%, gapbottom 20px");
			moveFile.incCounter();
			moveFile.getPanel().revalidate();
		}
	}
}