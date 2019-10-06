package chat;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.concurrent.TimeUnit;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.text.PlainDocument;

import console.Console;
import utils.PositiveIntegerFilter;

/**
 * Lance la communication et permet d'en choisir la dur�e
 */
public final class Main {
	
	public static final Console console = new Console();
	
	public static void main(final String[] args) throws InterruptedException {
		final JPanel panel = new JPanel(new GridBagLayout());
		final GridBagConstraints constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.NONE;
		constraints.gridx = 0;

		final JLabel label = new JLabel("Choose the number of seconds during which the communication whille be available at the maximum (enter end_stream in the console if you want to end it before that)");
		constraints.gridy = 0;
		panel.add(label, constraints);
		final JTextField textField = new JTextField();
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.gridy = 1;
		panel.add(textField, constraints);
		((PlainDocument)textField.getDocument()).setDocumentFilter(new PositiveIntegerFilter());
		final int option = JOptionPane.showOptionDialog(null, panel, "Settings", -1, JOptionPane.QUESTION_MESSAGE, new ImageIcon(), new String[]{"Ok"}, null);
		final String result = textField.getText();
		if(option == 0) {
			if(PositiveIntegerFilter.testFinal(result)) {
				final int resultInt = Integer.parseInt(result);
				
				console.initConsole();
				
				final Server server = new Server();
				TimeUnit.SECONDS.sleep(resultInt); //temps en secondes durant lequel le programme est actif
				server.endStreams();
			}
		}
	}
}