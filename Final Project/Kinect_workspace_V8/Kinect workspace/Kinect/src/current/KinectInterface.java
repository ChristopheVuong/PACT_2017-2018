package current;

import utils.ComboItem;

/**
 * Contient les méthodes à implémenter par Kinect pour être utilisé pour montrer le squelette
 */
public interface KinectInterface {
	
	/**
	 * Permet d'obtenir le tableau des positions du squelette ainsi que de leur status (traquées ou calculées)
	 */
	public ComboItem getPositionsAndStatus();
}