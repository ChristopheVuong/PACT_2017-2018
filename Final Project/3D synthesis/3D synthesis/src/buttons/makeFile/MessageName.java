package buttons.makeFile;

import javax.swing.SwingConstants;

import utils.TextFieldSized;
import utils.TextListener;

public final class MessageName extends TextFieldSized {

	private static final long serialVersionUID = 9192589528905955378L;

	public MessageName() {
		super("Entrer le message",600,40);
		setHorizontalAlignment(SwingConstants.CENTER);
		addFocusListener(new TextListener("Entrer le message"));
	}
}