package buttons.makeFile;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import buttons.MakeFileButton;
import utils.JButtonResized;

public final class PreviousMakeButton extends JButtonResized {

	private static final long serialVersionUID = 2011969516175471488L;

	private final MakeFileButton makeFile;

	public PreviousMakeButton(final MakeFileButton makeFile) {
		super("Retourner à la page principale");
		this.makeFile = makeFile;
		setIcon("previous_page.png");
		addActionListener(new Listener());
		makeFile.getFrame().getPanel().add(this, "cell 0 2, align 0% 100%, gapbottom 25px");
		makeFile.getFrame().getPanel().revalidate();
	}

	private final class Listener implements ActionListener {
		@Override
		public void actionPerformed(final ActionEvent e) {
			makeFile.returnToMain();
		}
	}
}