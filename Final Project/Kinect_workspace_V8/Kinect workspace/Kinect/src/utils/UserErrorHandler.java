package utils;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public final class UserErrorHandler {

	public UserErrorHandler(final String string) {
		this(string,null);
	}

	public UserErrorHandler(final String string, final String arg) {
		if(arg == null || arg.isEmpty()) {
			buildDisplay(string);
		} else {
			buildDisplay(string + " : " + arg);
		}
	}

	private void buildDisplay(final String errorMessage) {
		JOptionPane.showOptionDialog(null, errorMessage, null, -1, JOptionPane.QUESTION_MESSAGE, new ImageIcon(), new String[]{"Ok"}, null);
	}
}