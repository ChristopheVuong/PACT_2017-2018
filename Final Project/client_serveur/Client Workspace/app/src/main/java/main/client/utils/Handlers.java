package main.client.utils;

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
}