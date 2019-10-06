package utils;

import java.awt.event.MouseEvent;

import edu.ufl.digitalworlds.j4k.Skeleton;

/**
 * D�fini quelques constantes utiles pour le projet.
 */
public final class Constants {
	public static final int leftClick = MouseEvent.BUTTON1_DOWN_MASK;
	public static final int rightClick = MouseEvent.BUTTON3_DOWN_MASK;
	public static final int leftButton = MouseEvent.BUTTON1;
	public static final int rightButton = MouseEvent.BUTTON3;
	public static final float initialZoomFactor = 3.5f;
	public static final float zoomFactorMultiplier = 1.1f;
	public static final float initialTranslationFactor = 0f;
	public static final int[][] limbs = new int[][] {
		{Skeleton.SPINE_BASE,Skeleton.SPINE_MID},
		{Skeleton.SPINE_MID,Skeleton.NECK},
		{Skeleton.NECK,Skeleton.HEAD},
		{Skeleton.NECK,Skeleton.SHOULDER_LEFT},
		{Skeleton.NECK,Skeleton.SHOULDER_RIGHT},
		{Skeleton.SHOULDER_LEFT,Skeleton.ELBOW_LEFT},
		{Skeleton.SHOULDER_RIGHT,Skeleton.ELBOW_RIGHT},
		{Skeleton.ELBOW_LEFT,Skeleton.WRIST_LEFT},
		{Skeleton.ELBOW_RIGHT,Skeleton.WRIST_RIGHT},
		{Skeleton.WRIST_LEFT,Skeleton.HAND_LEFT},
		{Skeleton.WRIST_RIGHT,Skeleton.HAND_RIGHT},
		{Skeleton.SPINE_BASE,Skeleton.HIP_LEFT},
		{Skeleton.SPINE_BASE,Skeleton.HIP_RIGHT},
		{Skeleton.HIP_LEFT,Skeleton.KNEE_LEFT},
		{Skeleton.HIP_RIGHT,Skeleton.KNEE_RIGHT},
		{Skeleton.KNEE_LEFT,Skeleton.ANKLE_LEFT},
		{Skeleton.KNEE_RIGHT,Skeleton.ANKLE_RIGHT},
		{Skeleton.ANKLE_LEFT,Skeleton.FOOT_LEFT},
		{Skeleton.ANKLE_RIGHT,Skeleton.FOOT_RIGHT}
	};
	public static final int[][] limbsSphere = new int[][] {
		{Skeleton.HEAD,Skeleton.NECK}
	};
	public static final int[][] limbsCylinder0 = new int[][] {
		{Skeleton.SPINE_BASE,Skeleton.SPINE_MID},
		{Skeleton.SPINE_MID,Skeleton.NECK},
		{Skeleton.NECK,Skeleton.SHOULDER_LEFT},
		{Skeleton.NECK,Skeleton.SHOULDER_RIGHT},
		{Skeleton.SPINE_BASE,Skeleton.HIP_LEFT},
		{Skeleton.SPINE_BASE,Skeleton.HIP_RIGHT}
	};
	public static final int[][] limbsCylinder1 = new int[][] {
		{Skeleton.SHOULDER_LEFT,Skeleton.ELBOW_LEFT},
		{Skeleton.SHOULDER_RIGHT,Skeleton.ELBOW_RIGHT},
		{Skeleton.ELBOW_LEFT,Skeleton.WRIST_LEFT},
		{Skeleton.ELBOW_RIGHT,Skeleton.WRIST_RIGHT},
		{Skeleton.HIP_LEFT,Skeleton.KNEE_LEFT},
		{Skeleton.HIP_RIGHT,Skeleton.KNEE_RIGHT},
		{Skeleton.KNEE_LEFT,Skeleton.ANKLE_LEFT},
		{Skeleton.KNEE_RIGHT,Skeleton.ANKLE_RIGHT}
	};
	public static final int[][] limbsCylinder2 = new int[][] {
		{Skeleton.WRIST_LEFT,Skeleton.HAND_LEFT},
		{Skeleton.WRIST_RIGHT,Skeleton.HAND_RIGHT},
		{Skeleton.ANKLE_LEFT,Skeleton.FOOT_LEFT},
		{Skeleton.ANKLE_RIGHT,Skeleton.FOOT_RIGHT}
	};
}