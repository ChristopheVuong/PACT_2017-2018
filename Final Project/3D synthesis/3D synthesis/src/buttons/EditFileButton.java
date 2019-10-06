package buttons;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JProgressBar;
import javax.swing.Timer;
import javax.swing.filechooser.FileNameExtensionFilter;

import appInterface.MainFrame;
import utils.ButtonSized;

public final class EditFileButton extends ButtonSized {

	private static final long serialVersionUID = -4484546912688589675L;

	private final MainFrame frame;
	private final JProgressBar progressBar;
	private final JDialog dialog;

	public EditFileButton(final MainFrame frame) {
		super("Editer un fichier de mouvement",300,20);
		this.frame = frame;
		addActionListener(new Listener());
		progressBar = new JProgressBar(0,100);
		progressBar.setValue(0);
		progressBar.setStringPainted(true); 
		progressBar.setIndeterminate(true);
		dialog = new JDialog();
		dialog.setSize(new Dimension(600,200));
		dialog.setTitle("Opening");
		dialog.add(progressBar);
		dialog.setVisible(false);
	}

	public MainFrame getFrame() {
		return frame;
	}

	public void init(final File file) {
		dialog.setVisible(true);
		final OpeningCurrent openingCurrent = new OpeningCurrent(progressBar,file,this);
		final Timer timer = new Timer(1000,null);
		timer.addActionListener(new ListenerTimer(openingCurrent,timer,dialog,progressBar));
		timer.start();
		openingCurrent.execute();
	}

	private final class Listener implements ActionListener {
		@Override
		public void actionPerformed(final ActionEvent e) {
			final JFileChooser openFile = new JFileChooser(frame.getSaveAndOpenPath());
			final FileNameExtensionFilter filter = new FileNameExtensionFilter("Fichier au format txt", "txt", "text");
			openFile.setFileFilter(filter);
			final int retrival = openFile.showOpenDialog(null);
			if(retrival == JFileChooser.APPROVE_OPTION) {
				final File file = openFile.getSelectedFile();
				frame.setSaveAndOpenPath(file.getAbsoluteFile().getParent());
				frame.getMakeFile().doClick();
				init(file);
			}
		}
	}
}