package buttons.makeFile;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import buttons.MakeFileButton;
import buttons.MoveFileInterface;
import miginfocom.swing.MigLayout;
import utils.ButtonSized;
import utils.MessageComboItem;
import utils.UserErrorHandler;
import utils.ValidationMethods;

public final class MoveFileMakeButton extends ButtonSized implements MoveFileInterface {

	private static final long serialVersionUID = 3166337025576944551L;
	private static final int OFFSET = 3;

	private final MakeFileButton makeFile;
	private DeleteMakeButton delete;
	private InputStream fileOpen;
	private InputStream fileCopy;
	private boolean fileChoosen;
	private final JPanel panel;
	private JTextField moveName;
	private List<MessageComboItem> messageList;
	private AddMessageButton addMessage;
	private int counter;

	public MoveFileMakeButton(final MakeFileButton makeFile, final int y) {
		super("Ajouter un (ou plusieurs) fichier(s) de mouvement",600,30);
		this.makeFile = makeFile;
		addActionListener(new Listener());
		panel = new JPanel(new MigLayout());
		panel.add(this, "cell 0 0, align 50% 50%, gapbottom 20px");
		makeFile.getFrame().getPanel().add(panel, "cell 0 " + (y + OFFSET) + ", span 3, align 50% 50%, gapbottom 40px");
		makeFile.getFrame().getPanel().revalidate();
	}

	public MoveFileMakeButton(final MakeFileButton makeFile) {
		this(makeFile,makeFile.getCounter());
	}

	public MakeFileButton getMakeFile() {
		return makeFile;
	}

	public DeleteMakeButton getDelete() {
		return delete;
	}

	@Override
	public InputStream getFileOpen() {
		return fileOpen;
	}

	public void setFileOpen(final InputStream fileOpen) {
		this.fileOpen = fileOpen;
	}

	public InputStream getFileCopy() {
		return fileCopy;
	}

	public void setFileCopy(final InputStream fileCopy) {
		this.fileCopy = fileCopy;
	}

	public boolean isFileChoosen() {
		return fileChoosen;
	}

	public JPanel getPanel() {
		return panel;
	}

	public JTextField getMoveName() {
		return moveName;
	}

	public List<MessageComboItem> getMessages() {
		return messageList;
	}

	public void addMessage(final MessageComboItem message) {
		messageList.add(message);
		counter++;
	}

	public AddMessageButton getAddMessage() {
		return addMessage;
	}

	public int getCounter() {
		return counter;
	}

	public void incCounter() {
		counter++;
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

	public void setContent(final File file, final boolean edit) {
		try {
			if(file == null && edit) {
				if(!fileChoosen) {
					fileChoosen = true;
					panel.add(delete = new DeleteMakeButton(this), "cell 0 0, align 50% 50%, gapbottom 20px");
					panel.add(moveName = new MoveName(), "cell 0 1, align 50% 50%, gapbottom 20px");

					messageList = new ArrayList<MessageComboItem>();
					(addMessage = new AddMessageButton(this)).doClick();

					makeFile.addMove(new MoveFileMakeButton(makeFile));
				}
				setText("");
			} else {
				if(file != null) {
					fileOpen = new FileInputStream(file);
					fileCopy = new FileInputStream(file);
					if(!fileChoosen) {
						fileChoosen = true;
						panel.add(delete = new DeleteMakeButton(this), "cell 0 0, align 50% 50%, gapbottom 20px");
						panel.add(moveName = new MoveName(), "cell 0 1, align 50% 50%, gapbottom 20px");

						messageList = new ArrayList<MessageComboItem>();
						(addMessage = new AddMessageButton(this)).doClick();

						makeFile.addMove(new MoveFileMakeButton(makeFile));
					}
					setText(file.getName());
				}
			}
		} catch(final FileNotFoundException e) {}
	}

	public void setContent(final File file) {
		setContent(file,false);
	}

	private final class Listener implements ActionListener {
		@Override
		public void actionPerformed(final ActionEvent e) {
			final JFileChooser openFile = new JFileChooser(makeFile.getFrame().getSaveAndOpenPath());
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
						makeFile.getFrame().setSaveAndOpenPath(file.getAbsoluteFile().getParent());
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
										makeFile.getFinalizeMakeFile().setVisible(true);
										setContent(file);
									} else {
										makeFile.getMoves().get(makeFile.getMoves().size()-1).setContent(file);
									}
								} else {
									new UserErrorHandler("Le fichier " + file.getName() + " ne semble pas �tre au format d'enregistrement d'un squelette, veuillez en choisir un autre");
								}
							} else {
								new UserErrorHandler("Le fichier " + file.getName() + " ne semble pas �tre au format d'enregistrement d'un squelette, veuillez en choisir un autre");
							}
						} else {
							new UserErrorHandler("Le fichier " + file.getName() + " ne semble pas �tre au format d'enregistrement d'un squelette, veuillez en choisir un autre");
						}
					} catch(final FileNotFoundException e1) {
						new UserErrorHandler("Le fichier n�" + i + " n'a pas pu �tre trouv�");
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