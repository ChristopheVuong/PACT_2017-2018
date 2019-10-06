package console;

import javax.swing.JFrame;
import javax.swing.JScrollPane;

public final class ConsoleWrapper {
	
	public ConsoleWrapper(Console console) {
		final JScrollPane consoleScroll = new JScrollPane(console, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		final JFrame frame = new JFrame("Console");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(800, 300);
		frame.add(consoleScroll);
		frame.setVisible(true);
	}
}