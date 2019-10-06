package utils;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JTextField;

public final class TextListener implements FocusListener {

	private final String text;

	public TextListener(final String text) {
		this.text = text;
	}

	@Override
	public void focusGained(final FocusEvent e) {
		final JTextField source = (JTextField)e.getSource();
		if(source.getText().equals(text)) {
			source.setText("");
		}
	}

	@Override
	public void focusLost(final FocusEvent e) {
		final JTextField source = (JTextField)e.getSource();
		if(source.getText().isEmpty()) {
			source.setText(text);
		}
	}
}