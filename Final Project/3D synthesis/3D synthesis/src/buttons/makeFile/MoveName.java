package buttons.makeFile;

import javax.swing.SwingConstants;

import utils.TextFieldSized;
import utils.TextListener;

public final class MoveName extends TextFieldSized {

	private static final long serialVersionUID = 4921847741594848374L;

	public MoveName() {
		super("Entrer le nom du mouvement",600,40);
		setHorizontalAlignment(SwingConstants.CENTER);
		addFocusListener(new TextListener("Entrer le nom du mouvement"));
	}
}