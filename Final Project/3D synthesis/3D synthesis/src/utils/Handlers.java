package utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import buttons.MoveFileInterface;

public final class Handlers {

	public static List<ComboItem>[] makeMovesList(final List<? extends MoveFileInterface> moves) {
		final int len = moves.size() - 1;
		final List<ComboItem>[] movesList = new ArrayList[len];
		for(int i = 0; i<len; i++) {
			movesList[i] = new ArrayList<ComboItem>();
		}

		for(int i = 0; i<len; i++) {
			final Scanner sc = new Scanner(moves.get(i).getFileOpen());
			sc.nextLine();

			final List<ComboItem> positions = movesList[i];

			while(sc.hasNextLine()) {
				final ComboItem pos = new ComboItem(decodeLine(sc.nextLine()),(byte[])null);
				pos.setJointStatus(decodeLine(sc.nextLine()));
				positions.add(pos);
			}
			sc.close();
		}
		return movesList;
	}

	public static float[] decodeLine(final String positions) {
		final String build = positions.substring(1,positions.length()-1);
		final String[] array = build.split(",");

		final int len = array.length;
		if(len == 60 || len == 75) {
			final float[] arrayFloat = new float[60];
			for(int i = 0; i<60; i++) {
				arrayFloat[i] = Float.parseFloat(array[i]);
			}
			return arrayFloat;
		} else {
			if(len == 20) {
				final float[] arrayFloat = new float[20];
				for(int i = 0; i<20; i++) {
					arrayFloat[i] = Float.parseFloat(array[i]);
				}
				return arrayFloat;
			} else {
				return null;
			}
		}
	}

	public static void copyFile(final InputStream is, final File dest) throws IOException {
		OutputStream os = null;
		try {
			os = new FileOutputStream(dest);
			final byte[] buffer = new byte[1024];
			int length;
			while((length = is.read(buffer)) > 0) {
				os.write(buffer, 0, length);
			}
		} finally {
			if(os != null) {
				os.close();
			}
		}
	}

	public static void appendToFile(final InputStream is, final OutputStream os) throws IOException {
		try {
			final byte[] buffer = new byte[1024];
			int length;
			while((length = is.read(buffer)) > 0) {
				os.write(buffer,0,length);
			}
		} finally {
			if(is != null) {
				is.close();
			}
		}
	}
}