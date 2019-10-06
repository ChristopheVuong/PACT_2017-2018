package console;

import javax.swing.JTextArea;

/**
 * Permet de paramétrer la console
 */
public final class Console extends JTextArea {

	private static final long serialVersionUID = 3999275487935015073L;
	
	private final Streamer consoleStream;
	
	public Console() {
		this(false);
	}
	
	public Console(boolean initialize) {
		super();
		consoleStream = new Streamer(this);
		addKeyListener(consoleStream);
		System.setIn(consoleStream);
		if(initialize) {
			initConsole();
		}
	}
	
	/**
	 * Permet d'obtenir l'input stream de la console
	 */
	public Streamer getConsoleStream() {
		return consoleStream;
	}
	
	/**
	 * Permet d'imprimer un message dans la console
	 */
	public void print(final String str) {
		append(str);
		setCaretPosition(getDocument().getLength());
	}
	
	public void print(final float floatNumber) {
		println(Float.toString(floatNumber));
	}
	
	public void print(final double doubleNumber) {
		println(Double.toString(doubleNumber));
	}
	
	public void print(final int intNumber) {
		println(Integer.toString(intNumber));
	}
	
	/**
	 * Permet d'imprimer un message dans la console
	 */
	public void println(final String str) {
		print(str + "\n");
	}
	
	public void println(final float floatNumber) {
		println(Float.toString(floatNumber));
	}
	
	public void println(final double doubleNumber) {
		println(Double.toString(doubleNumber));
	}
	
	public void println(final int intNumber) {
		println(Integer.toString(intNumber));
	}
	
	/**
	 * Permet d'initialiser le wrapper de la console
	 */
	public void initConsole() {
		new ConsoleWrapper(this);
	}
}