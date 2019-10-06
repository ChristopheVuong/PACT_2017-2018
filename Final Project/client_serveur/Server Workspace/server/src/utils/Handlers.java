package utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public final class Handlers {
	
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
	
	public static List<FileComboItem> makeIndex() {
		final List<FileComboItem> indexList;
		final File index = new File(Constants.BASE_MOVE + "index.moveroot");
		if(index.exists()) {
			Scanner sc = null;
			
			try {
				sc = new Scanner(index);
				int indexCount = 0;
				
				indexList = new ArrayList<FileComboItem>();
				
				while(sc.hasNextLine()) {
					final String str = sc.nextLine();
					if(ValidationMethods.isValidFile(str)) {
						final String[] indexFile = str.split(" => ",2);
						if(Integer.parseInt(indexFile[0]) == indexCount) {
							indexList.add(new FileComboItem(indexFile[1].split("&",2)));
							indexCount++;
						} else {
							return null;
						}
					} else {
						return null;
					}
				}
			} catch(IOException e) {
				return null;
			} finally {
				if(sc != null) {
					sc.close();
				}
		    }
			return indexList;
		}
		return null;
	}
	
	public static List<Integer> compareIndex(final List<FileComboItem> indexList, final List<FileComboItem> indexListClient) {
		if(indexList != null && indexListClient != null) {
			final List<Integer> compareIndex = new ArrayList<Integer>();
			final int lenClient = indexListClient.size();
			final int len = indexList.size();
			final boolean[] indexCheck = new boolean[len];
			for(int i = 0; i<lenClient; i++) {
				final FileComboItem indexItem = indexListClient.get(i);
				final int index = indexOfFileName(indexItem.getFileName(),indexList);
				if(index == -1 || !indexItem.getMD5().equals(indexList.get(index).getMD5())) {
					compareIndex.add(i);
				} else {
					indexCheck[index] = true;
				}
			}
			for(int i = 0; i<len; i++) {
				if(!indexCheck[i]) {
					new File(Constants.BASE_MOVE + indexList.get(i).getFileName()).delete();
				}
			}
			return compareIndex;
		}
		return null;
	}
	
	private static int indexOfFileName(final String fileName, final List<FileComboItem> indexList) {
		final int len = indexList.size();
		for(int i = 0; i<len; i++) {
			if(indexList.get(i).getFileName().equals(fileName)) {
				return i;
			}
		}
		return -1;
	}
}