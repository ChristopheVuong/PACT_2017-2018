package buttons;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Scanner;

import javax.swing.JProgressBar;
import javax.swing.SwingWorker;

import buttons.makeFile.MoveFileMakeButton;
import utils.MessageComboItem;
import utils.UserErrorHandler;
import utils.ValidationMethods;

/**
 * Permet de faire évoluer la barre de sauvegarde tout en effectuant la sauvegarde dans un Thread séparé
 */
public final class OpeningCurrent extends SwingWorker<Void,Void> implements PropertyChangeListener {

	private final JProgressBar progressBar;
	private final File file;
	private final EditFileButton editFile;
	private final double fileLen;
	private boolean isNotValid;

	public OpeningCurrent(final JProgressBar progressBar, final File file, final EditFileButton editFile) {
		super();
		this.progressBar = progressBar;
		this.file = file;
		this.editFile = editFile;
		fileLen = file.length();
		addPropertyChangeListener(this);
	}

	@Override
	protected Void doInBackground() {
		double progress = 0;
		final int currentPurcent = 0;
		setProgress(currentPurcent);
		try {
			Thread.sleep(100);
		} catch(final InterruptedException e) {}

		Scanner sc = null;
		exit:
			try {
				final long lineSeparatorLen = System.lineSeparator().getBytes("UTF-8").length;
				editFile.getFrame().setSaveAndOpenPath(file.getAbsoluteFile().getParent());
				sc = new Scanner(file);

				int numberOfMoves = 0;
				while(sc.hasNextLine()) {
					final MoveFileMakeButton move = editFile.getFrame().getMakeFile().getMoves().get(numberOfMoves);
					move.setContent(null,true);

					String str = sc.nextLine();
					progress += str.getBytes("UTF-8").length + lineSeparatorLen;
					if(currentPurcent != (int)(100*progress/fileLen)) {
						setProgress((int)(100*progress/fileLen));
					}

					if(ValidationMethods.isMoveName(str)) {
						move.setText(str.replaceAll(" ", "_") + ".txt");
						move.getMoveName().setText(str);
					} else {
						break;
					}
					int numberOfMessages = 0;
					while(sc.hasNextLine()) {
						str = sc.nextLine();
						progress += str.getBytes("UTF-8").length + lineSeparatorLen;
						if(ValidationMethods.isMessage(str)) {
							if(numberOfMessages > 0) {
								move.getAddMessage().doClick();
							}
							final String[] messageStr = str.split(":",2);
							final MessageComboItem message = move.getMessages().get(numberOfMessages);
							message.getMessageLevel().setText(messageStr[0]);
							message.getMessageName().setText(messageStr[1]);
							numberOfMessages++;
						} else {
							if(!ValidationMethods.isDTW(str)) {
								break;
							}
						}
					}
					if(numberOfMessages == 0 || !sc.hasNextLine()) {
						isNotValid = true;
						break exit;
					} else {
						final StringBuilder buffer = new StringBuilder(str);
						if(str.equals("Enregistrement d'un squelette Kinect")) {
							int lenTemp = 0;
							boolean withStatus = false;
							boolean start = false;
							boolean validSqueleton = true;

							while(sc.hasNextLine() && validSqueleton) {
								str = sc.nextLine();
								progress += str.getBytes("UTF-8").length + lineSeparatorLen;
								if(!str.isEmpty()) {
									buffer.append(str);
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
								} else {
									break;
								}
							}
							if(!validSqueleton) {
								isNotValid = true;
								break exit;
							} else {
								final byte[] encodedBuffer = Charset.forName("UTF-8").encode(buffer.toString()).array();
								move.setFileOpen(new ByteArrayInputStream(encodedBuffer));
								move.setFileCopy(new ByteArrayInputStream(encodedBuffer));
							}
						} else {
							isNotValid = true;
							break exit;
						}
					}
					numberOfMoves++;
				}
			} catch(final FileNotFoundException e) {
				isNotValid = true;
				break exit;
			} catch(final UnsupportedEncodingException e) {
				isNotValid = true;
				break exit;
			} finally {
				if(sc != null) {
					sc.close();
				}
			}
		if(isNotValid) {
			editFile.getFrame().getMakeFile().returnToMain();
			new UserErrorHandler("Une erreur s'est produite lors du chargement du fichier, il n'est peut-être pas à un format valide, veuillez réessayer");
		}
		return null;
	}

	@Override
	public void propertyChange(final PropertyChangeEvent e) {
		if("progress" == e.getPropertyName()) {
			final int progress = (Integer)e.getNewValue();
			progressBar.setIndeterminate(false);
			progressBar.setValue(progress);
		}
	}
}