package utils;

public final class Constants {
	public static final String BASE_MOVE = new String("");
	
	public static final int[][] LIMBS_ANGLES = new int[][] {
		{SkeletonPoints.SHOULDER_LEFT,SkeletonPoints.NECK,SkeletonPoints.SHOULDER_RIGHT},
		{SkeletonPoints.SHOULDER_LEFT,SkeletonPoints.ELBOW_LEFT, SkeletonPoints.WRIST_LEFT },
		{SkeletonPoints.SHOULDER_RIGHT,SkeletonPoints.ELBOW_RIGHT, SkeletonPoints.WRIST_RIGHT},
		{SkeletonPoints.HIP_LEFT,SkeletonPoints.SPINE_BASE,SkeletonPoints.SPINE_MID},
		{SkeletonPoints.HIP_RIGHT,SkeletonPoints.SPINE_BASE,SkeletonPoints.SPINE_MID},
		{SkeletonPoints.KNEE_LEFT,SkeletonPoints.SPINE_BASE,SkeletonPoints.KNEE_RIGHT},
		{SkeletonPoints.HIP_LEFT,SkeletonPoints.KNEE_LEFT,SkeletonPoints.ANKLE_LEFT},
		{SkeletonPoints.HIP_RIGHT,SkeletonPoints.KNEE_RIGHT,SkeletonPoints.ANKLE_RIGHT},
	};
}