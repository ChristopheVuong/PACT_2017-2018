package utils;

import java.util.regex.Pattern;

public final class ValidationMethods {

	private static final Pattern IS_VALID_FILE = Pattern.compile("[0-9]+ => [0-9a-f]{32}&[A-Za-z0-9\\._-]+\\.txt");
	private static final Pattern IS_MESSAGE = Pattern.compile("[0-9]+:.+");
	private static final Pattern IS_DTW = Pattern.compile("[0-9]+=>[0-9]+(\\.[0-9]+)?");
	
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
}