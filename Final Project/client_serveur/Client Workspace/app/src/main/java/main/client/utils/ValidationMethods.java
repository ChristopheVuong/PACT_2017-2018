package main.client.utils;

import java.util.List;
import java.util.regex.Pattern;

public final class ValidationMethods {

    private static final Pattern IS_VALID_FILE = Pattern.compile("[0-9]+");
    private static final Pattern IS_MESSAGE = Pattern.compile("[0-9]+:.+");
    private static final Pattern IS_DTW = Pattern.compile("[0-9]+=>[0-9]+(\\.[0-9]+)?");
    private static final Pattern IS_CLASSIF = Pattern.compile("classif=>[0-9]+&[0-9]+");

    public static boolean isValidFile(final String str) {
        return IS_VALID_FILE.matcher(str).matches();
    }

    public static boolean isMoveName(final String str) {
        return !str.isEmpty();
    }

    public static boolean isMessage(final String str) {
        return IS_MESSAGE.matcher(str).matches();
    }

    public static boolean isDTW(final String str) {
        return IS_DTW.matcher(str).matches();
    }

    public static int checkLine(final String positions) {
        final int len = positions.substring(1,positions.length()-1).split(",").length;
        return (len == 60 || len == 75) ? 60 : len == 20 ? 20 : 0;
    }

    public static boolean isClassif(final String str) {
        return IS_CLASSIF.matcher(str).matches();
    }

    public static boolean isIndexInitializer(final String str) {
        return str.equals("file=>indexInit");
    }

    public static boolean isIndexEnd(final String str) {
        return str.equals("file=>indexEnd");
    }

    public static boolean isIndexValid(final List<FileComboItem> indexList) {
        final int len = indexList.size();
        for(int i = 0; i<len-1; i++) {
            if(indexList.get(i+1).getFileName().compareTo(indexList.get(i).getFileName()) < 0) {
                return false;
            }
        }
        return true;
    }

    public static boolean isMoveInitializer(final String str) {
        return str.equals("file=>moveInit");
    }

    public static boolean isMoveEnd(final String str) {
        return str.equals("file=>moveEnd");
    }
}