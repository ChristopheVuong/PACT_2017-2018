package buttons.synthesis;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import buttons.MoveFileInterface;
import buttons.SynthesisButton;
import utils.ButtonSized;
import utils.UserErrorHandler;
import utils.ValidationMethods;

public final class MoveFileButton extends ButtonSized implements MoveFileInterface {

	private static final long serialVersionUID = 3166337025576944551L;
	private static final int OFFSET = 3;

	private final SynthesisButton synthesis;
	private final int y;
	private DeleteButton delete;
	private InputStream fileOpen;
	private InputStream fileCopy;
	private boolean fileChoosen;

	public MoveFileButton(final SynthesisButton synthesis, final int y) {
		super("Ajouter un (ou plusieurs) fichier(s) de mouvement",600,30);
		this.synthesis = synthesis;
		this.y = y;
		addActionListener(new Listener());
		synthesis.getFrame().getPanel().add(this, "cell 0 " + (y + OFFSET) + ", span 3, align 50% 50%, gapbottom 25px");
		synthesis.getFrame().getPanel().revalidate();
	}

	public MoveFileButton(final SynthesisButton synthesis) {
		this(synthesis,synthesis.getCounter());
	}

	public SynthesisButton getSynthesis() {
		return synthesis;
	}

	public DeleteButton getDelete() {
		return delete;
	}

	@Override
	public InputStream getFileOpen() {
		return fileOpen;
	}

	public InputStream getFileCopy() {
		return fileCopy;
	}

	public void closeStreams() {
		if(fileOpen != null) {
			try {
				fileOpen.close();
			} catch(final IOException e) {}
		}
		if(fileCopy != null) {
			try {
				fileCopy.close();
			} catch(final IOException e) {}
		}
	}

	private void setContent(final File file) {
		try {
			fileOpen = new FileInputStream(file);
			fileCopy = new FileInputStream(file);
			if(!fileChoosen) {
				fileChoosen = true;
				synthesis.getFrame().getPanel().add(delete = new DeleteButton(this), "cell 0 " + (y + OFFSET) + ", span 3, align 50% 50%, gapbottom 25px");
				synthesis.addMove(new MoveFileButton(synthesis));
			}
			setText(file.getName());
		} catch(final FileNotFoundException e) {}
	}

	private final class Listener implements ActionListener {
		@Override
		public void actionPerformed(final ActionEvent e) {
			final JFileChooser openFile = new JFileChooser(synthesis.getFrame().getSaveAndOpenPath());
			openFile.setMultiSelectionEnabled(true);
			final FileNameExtensionFilter filter = new FileNameExtensionFilter("Fichier au format txt", "txt", "text");
			openFile.setFileFilter(filter);
			final int retrival = openFile.showOpenDialog(null);
			if(retrival == JFileChooser.APPROVE_OPTION) {
				Scanner sc = null;
				final int lenFiles = openFile.getSelectedFiles().length;
				for(int i = 0; i<lenFiles; i++) {
					try {
						final File file = openFile.getSelectedFiles()[i];
						synthesis.getFrame().setSaveAndOpenPath(file.getAbsoluteFile().getParent());
						sc = new Scanner(file);

						if(sc.hasNextLine()) {
							String str = sc.nextLine();

							if(str.equals("Enregistrement d'un squelette Kinect")) {
								int lenTemp = 0;
								boolean withStatus = false;
								boolean start = false;
								boolean validSqueleton = true;

								while(sc.hasNextLine() && validSqueleton) {
									str = sc.nextLine();
									final int len = ValidationMethods.checkLine(str);

									switch(len) {
									case 0:
										validSqueleton = false;
										break;
									case 20:
										if(start && lenTemp == 60) {
											withStatus = true;
											lenTemp = 20;
										} else {
											validSqueleton = false;
										}
										break;
									case 60:
										if(start) {
											if((withStatus && lenTemp == 20) || !withStatus) {
												lenTemp = 60;
											}
										} else {
											start = true;
											lenTemp = 60;
										}
										break;
									}
								}
								if(validSqueleton) {
									if(i == 0) {
										synthesis.getFinalizeSynthesis().setVisible(true);
										setContent(file);
									} else {
										synthesis.getMoves().get(synthesis.getMoves().size()-1).setContent(file);
									}
								} else {
									new UserErrorHandler("Le fichier " + file.getName() + " ne semble pas être au format d'enregistrement d'un squelette, veuillez en choisir un autre");
								}
							} else {
								new UserErrorHandler("Le fichier " + file.getName() + " ne semble pas être au format d'enregistrement d'un squelette, veuillez en choisir un autre");
							}
						} else {
							new UserErrorHandler("Le fichier " + file.getName() + " ne semble pas être au format d'enregistrement d'un squelette, veuillez en choisir un autre");
						}
					} catch(final FileNotFoundException e1) {
						new UserErrorHandler("Le fichier n°" + i + " n'a pas pu être trouvé");
					} finally {
						if(sc != null) {
							sc.close();
						}
					}
				}
			}
		}
	}
}