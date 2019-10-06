package utils;

import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;

public abstract class JButtonResized extends JButton {

	private static final long serialVersionUID = -5696564594502185637L;

	public JButtonResized() {
		super();
	}

	public JButtonResized(final String text) {
		this();
		setToolTipText(text);
	}

	public final void setIcon(final String imgFile, final int size) {
		try {
			final Image img = ImageIO.read(new File(utils.Constants.IMAGE_PATH + imgFile));
			final Image resizedimg = img.getScaledInstance(size, size, 1);
			final ImageIcon icon = new ImageIcon(resizedimg);
			setIcon(icon);
		} catch(final IOException e) {
			new UserErrorHandler("Image non trouv�e");
		}
	}

	public final void setIcon(final String imgFile) {
		setIcon(imgFile,20);
	}
}