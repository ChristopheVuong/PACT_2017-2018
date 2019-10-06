package current;

import utils.ComboItem;

/**
 * Contient les m�thodes � impl�menter par Kinect pour �tre utilis� pour montrer le squelette
 */
public interface KinectInterface {
	
	/**
	 * Permet d'obtenir le tableau des positions du squelette ainsi que de leur status (traqu�es ou calcul�es)
	 */
	public ComboItem getPositionsAndStatus();
}