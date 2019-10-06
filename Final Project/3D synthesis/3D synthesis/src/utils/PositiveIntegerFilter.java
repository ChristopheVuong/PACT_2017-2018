package utils;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.DocumentFilter;

/**
 * Permet de v�rifier que les donn�es entr�es par l'utilisateur sont bien des entiers positifs, et de lui notifier dans le cas contraire.
 */
public final class PositiveIntegerFilter extends DocumentFilter {

	private final String text;

	public PositiveIntegerFilter(final String text) {
		this.text = text;
	}

	public PositiveIntegerFilter() {
		this(null);
	}

	/**
	 * M�thode h�rit�e de DocumentFilter, d�crit ce qu'il se passe quand l'utilisateur entre quelque chose dans la zone d'input.
	 * <p>
	 * Si on n'a pas un entier positif avec le nouvelle input, afficher une erreur et invalider la nouvelle entr�e.
	 */
	@Override
	public void insertString(final FilterBypass fb, final int offset, final String string, final AttributeSet attr) throws BadLocationException {
		final Document doc = fb.getDocument();
		final StringBuilder sb = new StringBuilder();
		sb.append(doc.getText(0, doc.getLength()));
		sb.insert(offset, string);

		if(test(sb.toString())) {
			super.insertString(fb, offset, string, attr);
		} else {
			errorMessage();
		}
	}

	/**
	 * M�thode h�rit�e de DocumentFilter, d�crit ce qu'il se passe quand l'utilisateur remplace quelque chose dans la zone d'input.
	 * <p>
	 * Si on n'a pas un entier positif avec le nouvelle input, afficher une erreur et invalider le remplacement.
	 */
	@Override
	public void replace(final FilterBypass fb, final int offset, final int length, final String text, final AttributeSet attrs) throws BadLocationException {
		final Document doc = fb.getDocument();
		final StringBuilder sb = new StringBuilder();
		sb.append(doc.getText(0, doc.getLength()));
		sb.replace(offset, offset + length, text);

		if(test(sb.toString())) {
			super.replace(fb, offset, length, text, attrs);
		} else {
			errorMessage();
		}
	}

	/**
	 * M�thode h�rit�e de DocumentFilter, d�crit ce qu'il se passe quand l'utilisateur retire quelque chose dans la zone d'input.
	 * <p>
	 * Si on n'a pas un entier positif avec le nouvelle input, afficher une erreure et invalider le retrait.
	 */
	@Override
	public void remove(final FilterBypass fb, final int offset, final int length) throws BadLocationException {
		final Document doc = fb.getDocument();
		final StringBuilder sb = new StringBuilder();
		sb.append(doc.getText(0, doc.getLength()));
		sb.delete(offset, offset + length);

		if(test(sb.toString())) {
			super.remove(fb, offset, length);
		} else {
			errorMessage();
		}

	}

	/**
	 * M�thode permettant de v�rifier que l'input est bien un entier positif.
	 */
	private boolean test(final String text) {
		if(text.isEmpty() || text.equals(this.text)) {
			return true;
		} else {
			try {
				final int number = Integer.parseInt(text);
				return number >= 0;
			} catch(final NumberFormatException e) {
				return false;
			}
		}
	}

	/**
	 * M�thode permettant de v�rifier que l'input est bien un entier positif.
	 */
	public static boolean testFinal(final String text, final boolean withMessage) {
		try {
			final int number = Integer.parseInt(text);
			return number >= 0;
		} catch(final NumberFormatException e) {
			if(withMessage) {
				errorMessage();
			}
			return false;
		}
	}

	/**
	 * M�thode permettant de v�rifier que l'input est bien un entier positif.
	 */
	public static boolean testFinal(final String text) {
		return testFinal(text,true);
	}

	public static void errorMessage() {
		new UserErrorHandler("Vous devez entrer un entier positif");
	}
}
