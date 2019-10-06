package buttons;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import appInterface.MainFrame;
import buttons.synthesis.FinalizeSynthesisButton;
import buttons.synthesis.MoveFileButton;
import buttons.synthesis.PreviousButton;
import utils.ButtonSized;

public final class SynthesisButton extends ButtonSized {

	private static final long serialVersionUID = 6856283059243916410L;

	private final MainFrame frame;
	private PreviousButton previous;
	private List<MoveFileButton> moves;
	private FinalizeSynthesisButton finalize;
	private int counter;

	public SynthesisButton(final MainFrame frame) {
		super("Synthèse d'un mouvement",300,20);
		this.frame = frame;
		moves = new ArrayList<MoveFileButton>();
		addActionListener(new Listener());
	}

	public MainFrame getFrame() {
		return frame;
	}

	public List<MoveFileButton> getMoves() {
		return moves;
	}

	public void addMove(final MoveFileButton move) {
		moves.add(move);
		counter++;
	}

	public FinalizeSynthesisButton getFinalizeSynthesis() {
		return finalize;
	}

	public int getCounter() {
		return counter;
	}

	public void remake(final int index) {
		if(index >= 0 && index < moves.size()) {
			frame.getPanel().remove(moves.get(index));
			frame.getPanel().remove(moves.get(index).getDelete());
			moves.remove(index);
			frame.getPanel().revalidate();
			frame.getPanel().repaint();
			if(moves.size() < 2 && finalize != null) {
				finalize.setVisible(false);
			}
		}
	}

	public void reset() {
		if(previous != null) {
			frame.getPanel().remove(previous);
		}

		final int len = moves.size();
		for(int i = 0; i<len-1; i++) {
			frame.getPanel().remove(moves.get(i));
			frame.getPanel().remove(moves.get(i).getDelete());
		}
		frame.getPanel().remove(moves.get(len-1));

		if(finalize != null) {
			frame.getPanel().remove(finalize);
		}

		moves = new ArrayList<MoveFileButton>();
		frame.getPanel().revalidate();
		frame.getPanel().repaint();
	}

	public void returnToMain() {
		reset();
		setVisible(true);
		frame.getMakeFile().setVisible(true);
		frame.getEditFile().setVisible(true);
	}

	private final class Listener implements ActionListener {
		@Override
		public void actionPerformed(final ActionEvent e) {
			setVisible(false);
			frame.getMakeFile().setVisible(false);
			frame.getEditFile().setVisible(false);
			previous = new PreviousButton(SynthesisButton.this);
			addMove(new MoveFileButton(SynthesisButton.this));
			finalize = new FinalizeSynthesisButton(SynthesisButton.this);
		}
	}
}