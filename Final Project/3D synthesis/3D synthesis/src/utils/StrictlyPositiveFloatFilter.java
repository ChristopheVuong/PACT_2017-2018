package utils;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.DocumentFilter;

/**
 * Permet de vérifier que les données entrées par l'utilisateur sont bien des réels strictement positifs, et de lui notifier dans le cas contraire.
 */
public final class StrictlyPositiveFloatFilter extends DocumentFilter {

	private final String text;

	public StrictlyPositiveFloatFilter(final String text) {
		this.text = text;
	}

	public StrictlyPositiveFloatFilter() {
		this(null);
	}

	/**
	 * Méthode héritée de DocumentFilter, décrit ce qu'il se passe quand l'utilisateur entre quelque chose dans la zone d'input.
	 * <p>
	 * Si on n'a pas un réel strictement positif avec le nouvelle input, afficher une erreur et invalider la nouvelle entrée.
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
	 * Méthode héritée de DocumentFilter, décrit ce qu'il se passe quand l'utilisateur remplace quelque chose dans la zone d'input.
	 * <p>
	 * Si on n'a pas un réel strictement positif avec le nouvelle input, afficher une erreur et invalider le remplacement.
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
	 * Méthode héritée de DocumentFilter, décrit ce qu'il se passe quand l'utilisateur retire quelque chose dans la zone d'input.
	 * <p>
	 * Si on n'a pas un réel strictement positif avec le nouvelle input, afficher une erreure et invalider le retrait.
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
	 * Méthode permettant de vérifier que l'input est bien un réel strictement positif (sans empêcher l'utilisateur d'utiliser 0 pour y mettre une virgule après, cela sera vérifier à la validation).
	 */
	private boolean test(final String text) {
		if(text.isEmpty() || text.equals(this.text)) {
			return true;
		} else {
			try {
				final Float number = Float.parseFloat(text);
				return number >= 0f;
			} catch(final NumberFormatException e) {
				return false;
			}
		}
	}

	/**
	 * Méthode permettant de vérifier que l'input est bien un réel strictement positif.
	 */
	public static boolean testFinal(final String text, final boolean withMessage) {
		try {
			final Float number = Float.parseFloat(text);
			return number > 0f;
		} catch(final NumberFormatException e) {
			if(withMessage) {
				errorMessage();
			}
			return false;
		}
	}

	/**
	 * Méthode permettant de vérifier que l'input est bien un réel strictement positif.
	 */
	public static boolean testFinal(final String text) {
		return testFinal(text,true);
	}

	public static void errorMessage() {
		new UserErrorHandler("Vous devez entrer un réel strictement positif");
	}
}