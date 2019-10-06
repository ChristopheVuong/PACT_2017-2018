package buttons.synthesis;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import buttons.SynthesisButton;
import utils.JButtonResized;

public final class PreviousButton extends JButtonResized {

	private static final long serialVersionUID = -5120787628532816862L;

	private final SynthesisButton synthesis;

	public PreviousButton(final SynthesisButton synthesis) {
		super("Retourner à la page principale");
		this.synthesis = synthesis;
		setIcon("previous_page.png");
		addActionListener(new Listener());
		synthesis.getFrame().getPanel().add(this, "cell 0 2, align 0% 100%, gapbottom 25px");
		synthesis.getFrame().getPanel().revalidate();
	}

	private final class Listener implements ActionListener {
		@Override
		public void actionPerformed(final ActionEvent e) {
			synthesis.returnToMain();
		}
	}
}