package buttons;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import appInterface.MainFrame;
import buttons.makeFile.FinalizeMakeFileButton;
import buttons.makeFile.MoveFileMakeButton;
import buttons.makeFile.PreviousMakeButton;
import utils.ButtonSized;

public final class MakeFileButton extends ButtonSized {

	private static final long serialVersionUID = -500086565501448600L;

	private final MainFrame frame;
	private PreviousMakeButton previous;
	private List<MoveFileMakeButton> moves;
	private FinalizeMakeFileButton finalize;
	private int counter;

	public MakeFileButton(final MainFrame frame) {
		super("Créer un fichier de mouvement",300,20);
		this.frame = frame;
		moves = new ArrayList<MoveFileMakeButton>();
		addActionListener(new Listener());
	}

	public MainFrame getFrame() {
		return frame;
	}

	public List<MoveFileMakeButton> getMoves() {
		return moves;
	}

	public void addMove(final MoveFileMakeButton move) {
		moves.add(move);
		counter++;
	}

	public FinalizeMakeFileButton getFinalizeMakeFile() {
		return finalize;
	}

	public int getCounter() {
		return counter;
	}

	public void remake(final int index) {
		if(index >= 0 && index < moves.size()) {
			frame.getPanel().remove(moves.get(index).getPanel());
			moves.get(index).closeStreams();
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
		for(int i = 0; i<len; i++) {
			frame.getPanel().remove(moves.get(i).getPanel());
		}

		if(finalize != null) {
			frame.getPanel().remove(finalize);
		}

		moves = new ArrayList<MoveFileMakeButton>();
		frame.getPanel().revalidate();
		frame.getPanel().repaint();
	}

	public void returnToMain() {
		reset();
		frame.getSynthesis().setVisible(true);
		setVisible(true);
		frame.getEditFile().setVisible(true);
	}

	private final class Listener implements ActionListener {
		@Override
		public void actionPerformed(final ActionEvent e) {
			frame.getSynthesis().setVisible(false);
			setVisible(false);
			frame.getEditFile().setVisible(false);
			previous = new PreviousMakeButton(MakeFileButton.this);
			addMove(new MoveFileMakeButton(MakeFileButton.this));
			finalize = new FinalizeMakeFileButton(MakeFileButton.this);
		}
	}
}