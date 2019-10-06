package readAndWrite;

import java.awt.Transparency;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

import edu.ufl.digitalworlds.j4k.J4KSDK;
import edu.ufl.digitalworlds.j4k.Skeleton;
import utils.UserErrorHandler;

public final class KinectImplementation extends J4KSDK {
	
	private final KinectReadAndWrite kinectReadAndWrite;
	private final String imagePath;
	private int counterImage;
	private final Random random;

	
	public KinectImplementation(KinectReadAndWrite kinectReadAndWrite, final String imagePath) {
		super();
		this.kinectReadAndWrite = kinectReadAndWrite;
		this.imagePath = imagePath;
		random = new Random();
	}
	
	/**
	 * Méthode héritée de librairie J4K, déclenchée à chaque réception d'une frame de squelettes par l'ordinateur depuis la Kinect lors de la sauvegarde.
	 * <p>
	 * Cherche parmi les squelettes possibles sur quel tableau il y a effectivement un squelette.
	 * <p>
	 * Mets à jour positions avec les nouvelles données du squelette.
	 * <p>
	 * Lance l'affichage s'il s'agit de la première frame récupérée.
	 * @param skeletonTracked tableau de booléens contenant les informations sur les squelettes tracker
	 * @param positions tableau de flottant contenant les positions de tous les squelettes (6), dont on peut extraire les squelettes trackés avec skeletonTracked
	 */
	@Override
	public void onSkeletonFrameEvent(final boolean[] skeletonTracked, final float[] positions, final float[] orientations, final byte[] jointStatus) {
		for(int i = 0; i < getMaxNumberOfSkeletons(); i++) {
			if(skeletonTracked[i]) {
				final Skeleton skeleton = Skeleton.getSkeleton(i, skeletonTracked, positions, orientations, jointStatus, this);
				final float[] jointPositions = skeleton.getJointPositions();
				kinectReadAndWrite.getPositionsAndStatus().setPositions(jointPositions);
				kinectReadAndWrite.getPositionsAndStatus().setJointStatus(skeleton.getJointTrackingStates());
				kinectReadAndWrite.incrementCountFrames();
				if (isHandsClapped(jointPositions)) {
					System.out.println("Stop clapping your hands");
				}
			}
		}
	}
	
	public boolean isHandsClapped(float[] jointPositions) {
		float distancec = (jointPositions[3*Skeleton.HAND_RIGHT] - jointPositions[3*Skeleton.HAND_LEFT])* (jointPositions[3*Skeleton.HAND_RIGHT] - jointPositions[3*Skeleton.HAND_LEFT])
		+ (jointPositions[3*Skeleton.HAND_RIGHT+1] - jointPositions[3*Skeleton.HAND_LEFT+1])*(jointPositions[3*Skeleton.HAND_RIGHT+1] - jointPositions[3*Skeleton.HAND_LEFT+1]) 
		+ (jointPositions[3*Skeleton.HAND_RIGHT+2] - jointPositions[3*Skeleton.HAND_LEFT+2])*(jointPositions[3*Skeleton.HAND_RIGHT+2] - jointPositions[3*Skeleton.HAND_LEFT+2]);
		return (distancec < 0.005f);
	}

	/**
	 * Enregistre des images aléatoirement lorsque l'utilisateur enclenche cette fonctionnalité
	 * @param colorFrame Tableau de bytes au format BGRA (640 × 480 pixels)
	 */
	@Override
	public void onColorFrameEvent(final byte[] colorFrame) {
		if(random.nextInt(100) == 0) {
			if(imagePath != null) {
				final int width = 640;
				final int height = 480;
				
				final byte[] colorFrameNoA = new byte[(int)((double)colorFrame.length*0.75)];
				for(int i = 0; i<colorFrame.length; i++) {
					if(i%4 != 3) {
						colorFrameNoA[i - (int)((double)i/(double)4)] = colorFrame[i];
					}
				}
				
				final int samplesPerPixel = 3;
				final int[] bandOffsets = {2,1,0};
				
				final DataBuffer buffer = new DataBufferByte(colorFrameNoA, colorFrameNoA.length);
				final WritableRaster raster = Raster.createInterleavedRaster(buffer, width, height, samplesPerPixel*width, samplesPerPixel, bandOffsets, null);
				final ColorModel colorModel = new ComponentColorModel(ColorSpace.getInstance(ColorSpace.CS_sRGB), new int[]{8,8,8}, false, false, Transparency.OPAQUE, DataBuffer.TYPE_BYTE);
				
				try {
					File f;
					while((f = new File(imagePath + "image_SportBot" + counterImage + ".jpg")).exists()) {
						counterImage++;
					}
					final BufferedImage image = new BufferedImage(colorModel, raster, false, null);
					ImageIO.write(image, "jpg", f);
					counterImage++;
				} catch(final IOException e) {
					new UserErrorHandler("Une erreur s'est produite lors de la sauvegarde, veuillez ressayer");
				}
			}
		}
	}

	@Override
	public void onDepthFrameEvent(final short[] depthFrame, final byte[] bodyIndex, final float[] xyz, final float[] uv) {}
}