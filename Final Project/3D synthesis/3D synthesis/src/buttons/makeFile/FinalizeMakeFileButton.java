package buttons.makeFile;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.security.DigestOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import buttons.MakeFileButton;
import utils.ButtonSized;
import utils.ComboItem;
import utils.Constants;
import utils.DTWImplementation;
import utils.FileComboItem;
import utils.Handlers;
import utils.MessageComboItem;
import utils.PositiveIntegerFilter;
import utils.UserErrorHandler;
import utils.ValidationMethods;

public final class FinalizeMakeFileButton extends ButtonSized {

	private static final long serialVersionUID = 811739324946889431L;

	private final MakeFileButton makeFile;

	public FinalizeMakeFileButton(final MakeFileButton makeFile) {
		super("Synthétiser le fichier de mouvements",300,20);
		this.makeFile = makeFile;
		addActionListener(new Listener());
		makeFile.getFrame().getPanel().add(this, "cell 0 " + 30000 + ", span 3, align 50% 50%, gapbottom 25px");
		makeFile.getFrame().getPanel().revalidate();
		setVisible(false);
	}

	private void makeFile(final List<MoveFileMakeButton> moves, final File f) {
		final int leni = moves.size();
		OutputStream os = null;
		String MD5 = null;

		final List<ComboItem>[] movesList = Handlers.makeMovesList(moves);

		try {
			final MessageDigest md = MessageDigest.getInstance("MD5");
			final byte[] lineSeparator = System.lineSeparator().getBytes("UTF-8");
			os = new DigestOutputStream(new FileOutputStream(f.getPath()),md);
			for(int i = 0; i<leni; i++) {
				final MoveFileMakeButton move = moves.get(i);
				if(move.isFileChoosen()) {
					if(i > 0) {
						os.write(lineSeparator);
					}
					os.write(move.getMoveName().getText().getBytes("UTF-8"));
					os.write(lineSeparator);
					final List<MessageComboItem> messageList = move.getMessages();
					final int lenj = messageList.size();
					for(int j = 0; j<lenj; j++) {
						final MessageComboItem message = messageList.get(j);
						os.write(((PositiveIntegerFilter.testFinal(message.getMessageLevel().getText(),false) ? Integer.parseInt(message.getMessageLevel().getText()) : 0) + ":" + message.getMessageName().getText()).getBytes("UTF-8"));
						os.write(lineSeparator);
					}
					
					Handlers.appendToFile(moves.get(i).getFileCopy(),os);
				}
			}

			final byte[] bytes = md.digest();
			final StringBuilder sb = new StringBuilder();
			for(final byte b : bytes) {
				sb.append(String.format("%02x",b));
			}
			MD5 = sb.toString();
		} catch(final IOException e) {
			exitMakeFile(f);
			return;
		} catch(final NoSuchAlgorithmException e) {
			exitMakeFile(f);
			return;
		} finally {
			if(os != null) {
				try {
					os.close();
				} catch(final IOException e) {}
			}
		}

		for(int i = 0; i<leni; i++) {
			moves.get(i).closeStreams();
		}

		final String directoryPath = f.getAbsoluteFile().getParent();

		final File index = new File(directoryPath + "/index.moveroot");
		FileWriter fw = null;
		if(index.exists()) {
			Scanner sc = null;

			try {
				sc = new Scanner(index);
				int indexCount = 0;

				final List<FileComboItem> indexList = new ArrayList<FileComboItem>();

				while(sc.hasNextLine()) {
					final String str = sc.nextLine();
					if(ValidationMethods.isValidFile(str)) {
						final String[] indexFile = str.split(" => ",2);
						if(Integer.parseInt(indexFile[0]) == indexCount) {
							indexList.add(new FileComboItem(indexFile[1].split("&",2)));
							indexCount++;
						} else {
							exitMakeFile(f);
							return;
						}
					} else {
						exitMakeFile(f);
						return;
					}
				}

				insertIn(MD5,f.getName(),indexList);

				final StringBuilder build = new StringBuilder();
				final int len = indexList.size();
				for(int i = 0; i<len; i++) {
					build.append(encodeFileToIndex(i,indexList.get(i)));
				}

				fw = new FileWriter(index.getPath());
				fw.write(build.toString());
			} catch(final IOException e) {
				exitMakeFile(f);
				return;
			} finally {
				if(fw != null) {
					try {
						fw.close();
					} catch(final IOException e) {}
				}
				if(sc != null) {
					sc.close();
				}
			}
		} else {
			try {
				fw = new FileWriter(index.getPath());
				fw.write(encodeFileToIndex(0,MD5,f.getName()));
			} catch(final IOException e) {
				exitMakeFile(f);
				return;
			} finally {
				if(fw != null) {
					try {
						fw.close();
					} catch(final IOException e) {}
				}
			}
		}
	}

	private static void exitMakeFile(final File file) {
		file.delete();
		new UserErrorHandler("Une erreur s'est produite lors de la sauvegarde, veuillez réessayer");
	}

	private static void insertIn(final String MD5, final String filename, final List<FileComboItem> indexList) {
		final int len = indexList.size();
		final int i = 0;
		while(i<len) {
			final int compare = indexList.get(i).getFileName().compareTo(filename);
			if(compare == 0) {
				indexList.get(i).setFileName(filename);
				indexList.get(i).setMD5(MD5);
				return;
			} else {
				if(compare>0) {
					indexList.add(i,new FileComboItem(MD5,filename));
					return;
				}
			}
		}
	}

	private static String encodeFileToIndex(final int index, final String MD5, final String fileName) {
		return Integer.toString(index) + " => " + MD5 + "&" + fileName;
	}

	private static String encodeFileToIndex(final int index, final FileComboItem MD5AndFileName) {
		return encodeFileToIndex(index,MD5AndFileName.getMD5(),MD5AndFileName.getFileName());
	}

	private final class Listener implements ActionListener {

		@Override
		public void actionPerformed(final ActionEvent e) {
			final JFileChooser saveFile = new JFileChooser(makeFile.getFrame().getSaveAndOpenPath());
			final int retrival = saveFile.showSaveDialog(null);
			if(retrival == JFileChooser.APPROVE_OPTION) {
				final File fileSave = saveFile.getSelectedFile();
				makeFile.getFrame().setSaveAndOpenPath(fileSave.getAbsoluteFile().getParent());
				String filePath = fileSave.getPath();
				final String filename = fileSave.getName();

				if(filename.length()>4) {
					if(!filename.substring(filename.length() - 4).equals(".txt")) {
						filePath = filePath + ".txt";
					}
				} else {
					if(filename.equals(".txt")) {
						filePath = filePath.substring(0, filePath.length() - 4) + "Mouvement Kinect.txt";
					} else {
						filePath = filePath + ".txt";
					}
				}

				final File f = new File(filePath);
				boolean valid = false;
				if(f.exists()) {
					final int result = JOptionPane.showOptionDialog(null, "Un fichier avec ce nom existe déjà. Voulez-vous le remplacer?", null, -1, JOptionPane.QUESTION_MESSAGE, new ImageIcon(), new String[]{"Oui", "Non"}, null);
					if(result == 0) {
						valid = true;
					}
				} else {
					valid = true;
				}
				if(valid) {
					makeFile(makeFile.getMoves(),f);
					makeFile.returnToMain();
				}
			}
		}
	}
}