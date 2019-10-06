package utils;

import javax.swing.JTextField;

import buttons.makeFile.DeleteMessageButton;

public final class MessageComboItem {

	private final JTextField messageLevel;
	private final JTextField messageName;
	private final DeleteMessageButton deleteMessageButton;

	public MessageComboItem(final JTextField messageLevel, final JTextField messageName, final DeleteMessageButton deleteMessageButton) {
		this.messageLevel = messageLevel;
		this.messageName = messageName;
		this.deleteMessageButton = deleteMessageButton;
	}

	public JTextField getMessageLevel() {
		return messageLevel;
	}

	public JTextField getMessageName() {
		return messageName;
	}

	public DeleteMessageButton getDeleteMessageButton() {
		return deleteMessageButton;
	}
}