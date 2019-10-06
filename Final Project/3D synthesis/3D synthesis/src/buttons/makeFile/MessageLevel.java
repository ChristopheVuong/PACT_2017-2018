package buttons.makeFile;

import javax.swing.SwingConstants;
import javax.swing.text.PlainDocument;

import utils.PositiveIntegerFilter;
import utils.TextFieldSized;
import utils.TextListener;

public final class MessageLevel extends TextFieldSized {

	private static final long serialVersionUID = 6202837315534035716L;

	public MessageLevel() {
		super("Entrer le niveau du message (entier positif, 0 par défaut)",600,40);
		setHorizontalAlignment(SwingConstants.CENTER);
		addFocusListener(new TextListener("Entrer le niveau du message (entier positif, 0 par défaut)"));
		((PlainDocument)getDocument()).setDocumentFilter(new PositiveIntegerFilter("Entrer le niveau du message (entier positif, 0 par défaut)"));
	}	
}