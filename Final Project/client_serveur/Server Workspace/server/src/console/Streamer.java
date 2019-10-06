package console;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.InputStream;

import javax.swing.JTextArea;
import javax.swing.text.BadLocationException;
import javax.swing.text.Utilities;

import utils.UserErrorHandler;

/**
 * Permet de paramétrer la gestion de l'input dans le console pour le retranscrire pour System.in
 */
public final class Streamer extends InputStream implements KeyListener {

    private final JTextArea console;
    private String str;
    private int pos;
    
    /**
     * Association de la console à l'input stream
     */
    public Streamer(JTextArea console) {
    	this.console = console;
    }

    @Override
    public int read() {
        if(str != null && pos == str.length()) {
            str = null;
            return java.io.StreamTokenizer.TT_EOF;
        }

        while(str == null || pos >= str.length()) {
            try {
                synchronized(this) {
                    wait();
                }
            } catch(InterruptedException e) {
            	new UserErrorHandler("An error occured on the console");
            }
        }

        return str.charAt(pos++);
    }
    
    /**
     * Quand on appuie sur entrer, ajoute la dernière ligne à l'input stream
     */
	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_ENTER){
			try {
				int end = console.getDocument().getLength();
				int start;
				start = Utilities.getRowStart(console, end);
				String text = console.getText(start, end - start);
				str = text.isEmpty() ? "" : text + "\n";
		        pos = 0;
		        synchronized(this) {
		            notifyAll();
		        }
			} catch(BadLocationException e1) {}
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {}

	@Override
	public void keyTyped(KeyEvent arg0) {}
}