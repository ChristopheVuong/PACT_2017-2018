package utils;

import java.util.ArrayList;
import java.util.List;

public final class DTWImplementation {

	private static final float AVERAGE_HEIGHT = 1.7f;

	public static float[] DTW(final List<ComboItem> s, final List<ComboItem> t) {
		final int slen = s.size();
		final int tlen = t.size();
		final float[][][] DTW = new float[20][slen][tlen];
		final float[] finalDTW = new float[20];
		for(int i = 0; i<20; i++) {
			DTW[i][0][0] = 0;
			for(int j = 1; j<slen; j++) {
				DTW[i][j][0] = Float.MAX_VALUE;
			}
			for(int j = 1; j<tlen; j++) {
				DTW[i][0][j] = Float.MAX_VALUE;
			}
			for(int j = 1; j<slen; j++) {
				final float[] sj = translate(s.get(j).getPositions());
				for(int k = 1; k<tlen; k++) {
					final float[] tk = translate(t.get(k).getPositions());
					DTW[i][j][k] = distanceD(i,sj,tk) + minimum(DTW[i][j-1][k],DTW[i][j][k-1],DTW[i][j-1][k-1]);
				}
			}
			finalDTW[i] = DTW[i][slen-1][tlen-1]/(slen+tlen);
		}
		return finalDTW;
	}

	private static float[] translate(final float[] positions) {
		final float[] positionsShift = new float[60];
		for(int i = 0; i<20; i++) {
			positionsShift[3*i] = positions[3*i] - positions[3*SkeletonPoints.SPINE_BASE];
			positionsShift[3*i+1] = positions[3*i+1] - positions[3*SkeletonPoints.SPINE_BASE+1];
			positionsShift[3*i+2] = positions[3*i+2] - positions[3*SkeletonPoints.SPINE_BASE+2];
		}
		return positionsShift;
	}

	public static float[] DTWAngles(final List<float[]> s, final List<float[]> t) {
		final int slen = s.size();
		final int tlen = t.size();
		final int len = Constants.LIMBS_ANGLES.length;
		final float[][][] DTW = new float[len][slen][tlen];
		final float[] finalDTW = new float[len];
		for(int i = 0; i<len; i++) {
			DTW[i][0][0] = 0;
			for(int j = 1; j<slen; j++) {
				DTW[i][j][0] = Float.MAX_VALUE;
			}
			for(int j = 1; j<tlen; j++) {
				DTW[i][0][j] = Float.MAX_VALUE;
			}
			for(int j = 1; j<slen; j++) {
				final float sj = s.get(j)[i];
				for(int k = 1; k<tlen; k++) {
					final float tk = t.get(k)[i];
					DTW[i][j][k] = distanceAngles(sj,tk) + minimum(DTW[i][j-1][k],DTW[i][j][k-1],DTW[i][j-1][k-1]);
				}
			}
			finalDTW[i] = DTW[i][slen-1][tlen-1]/(slen+tlen);
		}
		return finalDTW;
	}

	public static List<float[]> generateAngles(final List<ComboItem> positionsAndStatus) {
		final List<float[]> anglesList = new ArrayList<float[]>();
		for(final ComboItem positionAndStatus : positionsAndStatus) {
			anglesList.add(calculateAngles(positionAndStatus.getPositions()));
		}
		return anglesList;
	}

	private static float[] calculateAngles(final float[] positions) {
		final int len = Constants.LIMBS_ANGLES.length;
		final float[] angles = new float[len];
		for(int i = 0; i<len; i++) {
			angles[i] = calculateAngle(Constants.LIMBS_ANGLES[i],positions);
		}
		return angles;
	}

	private static float calculateAngle(final int[] points, final float[] positions) {
		return calculateAngle(points[0],points[1],points[2],positions);
	}

	private static float calculateAngle(final int point1, final int center, final int point2, final float[] positions) {
		final float[] vector1 = new float[] {positions[point1] - positions[center], positions[point1+1] - positions[center+1], positions[point1+2] - positions[center+2]};
		final float[] vector2 = new float[] {positions[point2] - positions[center], positions[point2+1] - positions[center+1], positions[point2+2] - positions[center+2]};
		return (float)Math.acos(scalar(vector1,vector2)/(norme(vector1)*norme(vector2)));
	}

	private static float scalar(final float[] vector1, final float[] vector2) {
		return vector1[0]*vector2[0]+vector1[1]*vector2[1]+vector1[2]*vector2[2];
	}

	private static float norme(final float[] vector) {
		return (float)Math.sqrt(vector[0]*vector[0]+vector[1]*vector[1]+vector[2]*vector[2]);
	}

	public static float sumDTW(final List<ComboItem> s, final List<ComboItem> t) {
		return sum(DTW(s,t));
	}

	public static float sumDTWAngles(final List<float[]> s, final List<float[]> t) {
		return sum(DTWAngles(s,t));
	}

	private static float distanceD(final int point, final float[] positions1, final float[] positions2) {
		final float dx = positions2[3*point]-positions1[3*point];
		final float dy = positions2[3*point+1]-positions1[3*point+1];
		final float dz = positions2[3*point+2]-positions1[3*point+2];
		return (float)(Math.sqrt(dx*dx + dy*dy + dz*dz) / AVERAGE_HEIGHT);
	}

	private static float distanceAngles(final float angle1, final float angle2) {
		return Math.abs(angle2-angle1);
	}

	private static float minimum(final float i, final float i1, final float i2) {
		return Math.min(Math.min(i,i1),i2);
	}

	public static float sum(final float[] array) {
		float temp = 0f;
		final float len = array.length;
		for(int i = 0; i<len; i++) {
			temp += array[i];
		}
		return temp;
	}

	public static float compare(final float[] DTW1, final float[] DTWAngles1, final float[] DTW2, final float[] DTWAngles2) {
		if(DTW1 != null && DTWAngles1 != null && DTW2 != null && DTWAngles2 != null && DTW1.length == DTW2.length && DTWAngles1.length == DTWAngles2.length) {
			return diff(DTW1,DTW2) + 3*diff(DTWAngles1,DTWAngles2);
		} else {
			return 0f;
		}
	}

	private static float diff(final float[] array1, final float[] array2) {
		float temp = 0f;
		final int len = array1.length;
		for(int i = 0; i<len; i++) {
			temp += Math.abs(array2[i]-array1[i]);
		}
		return temp;
	}
}