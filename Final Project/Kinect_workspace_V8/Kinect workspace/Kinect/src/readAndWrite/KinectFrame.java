package readAndWrite;

import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

import buttons.OpenButton;
import buttons.OpenPauseButton;
import buttons.OpenResaveButton;
import buttons.OpenValidateButton;
import buttons.SaveButton;
import buttons.SaveValidateButton;
import miginfocom.swing.MigLayout;
import utils.ComboItem;
import utils.PanelSized;
import utils.kinect.KinectReadInterface;

/**
 * Permet de créer l'interface pour sauvegarder ou lire des fichiers de squelettes issus de la Kinect, en mouvement.
 */
public final class KinectFrame extends JFrame implements KinectFrameInterface {

	private static final long serialVersionUID = 1143592698481014691L;
	
	public static final int KINECT = 0;
	public static final int NOKINECT = 1;

	private final PanelSized panel;
	
	private final int mode;

	private List<ComboItem> positionsOpen;
	private KinectReadInterface kinectRead;
	private KinectReadAndWriteInterface kinectReadAndWrite;
	private int timeBetweenFrames;
	private OpenButton open;
	private OpenPauseButton openPause;
	private OpenResaveButton openResave;
	private OpenValidateButton openValidate;
	private SaveButton save;
	private SaveValidateButton saveValidate;
	
	private String saveAndOpenPath;

	/**
	 * Permet de créer l'interface pour sauvegarder ou lire des fichiers de squelettes issus de la Kinect en mouvement.
	 * <p>
	 * Paramètre la taille initial de l'interface, ajoute un scroller à l'intérieur qui permet à l'interface de toujours garder la même taille de composants.
	 * <p>
	 * Paramètre les boutons qui composent l'interface, puis les y ajoute.
	 * <p>
	 * Créé l'interface graphique pour afficher le squelette et créer un objet qui gère le fonctionnement de la Kinect en appelant KinectSave.
	 */
	public KinectFrame(final int mode, final String imagePath) {
		super();
		this.mode = mode;
		
		setTitle("Interface graphique pour le squelette de la Kinect");

		setSize(1200,800);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		final Box box = new Box(BoxLayout.Y_AXIS);
		box.setAlignmentX(JComponent.CENTER_ALIGNMENT);
		panel = new PanelSized(new MigLayout("gap 0"),1160,740);
		final JScrollPane scrollPanel = new JScrollPane(box, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		add(scrollPanel);
		box.add(panel);
		
		switch(mode) {
			case KINECT:
				open = new OpenButton(this);
				openPause = new OpenPauseButton(this);
				openResave = new OpenResaveButton(this);
				openValidate = new OpenValidateButton(this);
				save = new SaveButton(this);
				saveValidate = new SaveValidateButton(this,this);

				panel.add(new JLabel(), "cell 0 0, w 290!");
				panel.add(new JLabel(), "cell 1 0, w 290!");
				panel.add(new JLabel(), "cell 2 0, w 290!");
				panel.add(new JLabel(), "cell 3 0, w 290!");
				panel.add(open, "cell 0 1, span 2, align 67% 33%, hidemode 2");
				panel.add(save, "cell 2 1, span 2, align 33% 67%, hidemode 2");
				panel.add(openPause, "cell 0 2, align 67% 33%, hidemode 2");
				panel.add(openResave, "cell 1 2, span 2, align 50% 50%, hidemode 2");
				panel.add(openValidate, "cell 3 2, align 33% 67%, hidemode 2");
				panel.add(saveValidate, "cell 0 3, span 4, align 50% 50%, hidemode 2");
				
				kinectReadAndWrite = new KinectReadAndWrite(this, imagePath);
				kinectRead = kinectReadAndWrite;
				break;
			case NOKINECT:
				open = new OpenButton(this);
				openPause = new OpenPauseButton(this);
				openResave = new OpenResaveButton(this);
				openValidate = new OpenValidateButton(this);

				panel.add(new JLabel(), "cell 0 0, w 290!");
				panel.add(new JLabel(), "cell 1 0, w 290!");
				panel.add(new JLabel(), "cell 2 0, w 290!");
				panel.add(new JLabel(), "cell 3 0, w 290!");
				panel.add(open, "cell 0 1, span 4, align 50% 50%, hidemode 2");
				panel.add(openPause, "cell 0 2, align 67% 33%, hidemode 2");
				panel.add(openResave, "cell 1 2, span 2, align 50% 50%, hidemode 2");
				panel.add(openValidate, "cell 3 2, align 33% 67%, hidemode 2");
				
				kinectRead = new KinectRead(this);
				break;
		}
		
		setVisible(true);
	}

	@Override
	public int getMode() {
		return mode;
	}
	
	@Override
	public PanelSized getPanel() {
		return panel;
	}

	@Override
	public void setTimeBetweenFrames(final int timeBetweenFrames) {
		this.timeBetweenFrames = timeBetweenFrames;
	}

	@Override
	public int getTimeBetweenFrames() {
		return timeBetweenFrames;
	}

	@Override
	public void setPositionsOpen(final List<ComboItem> positionsOpen) {
		this.positionsOpen = positionsOpen;
	}

	@Override
	public List<ComboItem> getPositionsOpen() {
		return positionsOpen;
	}

	@Override
	public OpenButton getOpen() {
		return open;
	}

	@Override
	public OpenPauseButton getOpenPause() {
		return openPause;
	}
	
	@Override
	public OpenResaveButton getOpenResave() {
		return openResave;
	}

	@Override
	public OpenValidateButton getOpenValidate() {
		return openValidate;
	}

	@Override
	public SaveButton getSave() {
		return save;
	}

	@Override
	public SaveValidateButton getSaveValidate() {
		return saveValidate;
	}
	
	@Override
	public String getSaveAndOpenPath() {
		return saveAndOpenPath;
	}
	
	@Override
	public void setSaveAndOpenPath(String saveAndOpenPath) {
		this.saveAndOpenPath = saveAndOpenPath;
	}

	@Override
	public KinectReadInterface getKinectRead() {
		return kinectRead;
	}
	
	@Override
	public KinectReadAndWriteInterface getKinectReadAndWrite() {
		return kinectReadAndWrite;
	}
}
