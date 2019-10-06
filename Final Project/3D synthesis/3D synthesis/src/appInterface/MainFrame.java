package appInterface;

import java.awt.Component;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import buttons.EditFileButton;
import buttons.MakeFileButton;
import buttons.SynthesisButton;
import miginfocom.swing.MigLayout;
import utils.PanelSizedX;

public final class MainFrame extends JFrame {

	private static final long serialVersionUID = -877349367296034667L;

	private final JPanel panel;

	private final SynthesisButton synthesis;
	private final MakeFileButton makeFile;
	private final EditFileButton editFile;

	private String saveAndOpenPath;

	public MainFrame() {
		super();

		setTitle("Interface graphique pour synthétiser les mouvements");

		setSize(1200,800);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		final Box box = new Box(BoxLayout.Y_AXIS);
		box.setAlignmentX(Component.CENTER_ALIGNMENT);
		panel = new PanelSizedX(new MigLayout("gap 0"),1160);
		final JScrollPane scrollPanel = new JScrollPane(box, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		add(scrollPanel);
		box.add(panel);

		synthesis = new SynthesisButton(this);
		makeFile = new MakeFileButton(this);
		editFile = new EditFileButton(this);

		panel.add(new JLabel(), "cell 0 0, w 385!");
		panel.add(new JLabel(), "cell 1 0, w 385!");
		panel.add(new JLabel(), "cell 2 0, w 385!");
		panel.add(synthesis, "cell 0 1, align 67% 33%, hidemode 2");
		panel.add(makeFile, "cell 1 1, align 50% 50%, hidemode 2");
		panel.add(editFile, "cell 2 1, align 33% 67%, hidemode 2");

		setVisible(true);
	}

	public JPanel getPanel() {
		return panel;
	}

	public String getSaveAndOpenPath() {
		return saveAndOpenPath;
	}

	public SynthesisButton getSynthesis() {
		return synthesis;
	}

	public MakeFileButton getMakeFile() {
		return makeFile;
	}

	public EditFileButton getEditFile() {
		return editFile;
	}

	public void setSaveAndOpenPath(final String saveAndOpenPath) {
		this.saveAndOpenPath = saveAndOpenPath;
	}
}