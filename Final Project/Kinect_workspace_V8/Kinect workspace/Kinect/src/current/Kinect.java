package current;

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
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import edu.ufl.digitalworlds.j4k.J4KSDK;
import edu.ufl.digitalworlds.j4k.Skeleton;
import utils.ComboItem;
import utils.UserErrorHandler;

/**
 * Permet de lancer une session de Kinect et de voir en direct le squelette de l'utilisateur.
 */
public final class Kinect extends J4KSDK implements KinectInterface {

	private boolean showSetup = true;
	private ComboItem positionsAndStatus;
	private final String imagePath;
	private int counterImage;
	private final Random random;

	/**
	 * @param timeOut un entier positif qui représente le temps de la session 
	 */
	public Kinect(final int timeOut, final String imagePath) {
		super();
		this.imagePath = imagePath;
		random =  new Random();
		float[] positions = new float[60];
		byte[] jointStatus = new byte[20];
		for(int i = 0; i<60; i++) {
			positions[i] = 0.0f;
		}
		for(int i = 0; i<20; i++) {
			jointStatus[i] = 0;
		}
		positionsAndStatus = new ComboItem(positions,jointStatus);
		start(J4KSDK.SKELETON|J4KSDK.COLOR);
		try {
			TimeUnit.SECONDS.sleep(timeOut);
		} catch(final InterruptedException e) {
			new UserErrorHandler("Une erreur est survenue lors du fonctionnement de la Kinect, veuillez relancer le programme");
			System.exit(0);
		} finally {
			stop();
		}
	}

	@Override
	public ComboItem getPositionsAndStatus() {
		return positionsAndStatus;
	}

	/**
	 * Méthode héritée de la librairie J4K, déclanchée à chaque réception d'une frame de squelettes par l'ordinateur depuis la Kinect.
	 * <p>
	 * Cherche parmi les squelettes possibles sur quel tableau il y a effectivement un squelette.
	 * <p>
	 * Mets à jour positions avec les nouvelles données du squelette.
	 * <p>
	 * Lance l'affichage s'il s'agit de la première frame récupérée.
	 * @param skeletonTracked tableau de booléens contenant les informations sur les squelettes tracker
	 * @param positions tableau de flottants contenant les positions de tous les squelettes (6), dont on peut extraire les squelettes trackés avec skeletonTracked
	 */
	@Override
	public void onSkeletonFrameEvent(final boolean[] skeletonTracked, final float[] positions, final float[] orientations, final byte[] jointStatus) {
		for(int i = 0; i < getMaxNumberOfSkeletons(); i++) {
			if(skeletonTracked[i]) {
				final Skeleton skeleton = Skeleton.getSkeleton(i, skeletonTracked, positions, orientations, jointStatus, this);
				positionsAndStatus.setPositions(skeleton.getJointPositions());
				positionsAndStatus.setJointStatus(skeleton.getJointTrackingStates());
			}
		}

		if(showSetup) {
			new ShowSkeleton(this);
			showSetup = false;
		}
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
