package buttons;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import javax.swing.JProgressBar;
import javax.swing.SwingWorker;

import utils.ComboItem;
import utils.UserErrorHandler;

/**
 * Permet de faire évoluer la barre de sauvegarde tout en effectuant la sauvegarde dans un Thread séparé
 */
public final class SavingCurrent extends SwingWorker<Void,Void> implements PropertyChangeListener {
	
	private final JProgressBar progressBar;
	private final List<ComboItem> positionsAndStatusSaving;
	private final FileWriter fw;
	
	public SavingCurrent(final JProgressBar progressBar, final List<ComboItem> positionsAndStatusSaving, final FileWriter fw) {
		super();
		this.progressBar = progressBar;
		this.positionsAndStatusSaving = positionsAndStatusSaving;
		this.fw = fw;
		addPropertyChangeListener(this);
	}
	
	@Override
	protected Void doInBackground() {
        int progress = 0;
        setProgress(0);
        try {
            Thread.sleep(100);
        } catch(InterruptedException e) {}
        String encodedArray = "";
        while(progress < positionsAndStatusSaving.size() - 1) {
        	encodedArray += encodeArray(positionsAndStatusSaving.get(progress));
            progress += 1;
            setProgress((int)(100*(double)progress/(double)positionsAndStatusSaving.size()));
        }
        encodedArray += encodeArray(positionsAndStatusSaving.get(progress));
        try {
        	progressBar.setIndeterminate(true);
			fw.write(encodedArray);
		} catch(IOException e) {
			new UserErrorHandler("Une erreur s'est produite lors de la sauvegarde, veuillez ressayer");
		} finally {
			if(fw != null) {
				try {
					fw.close();
				} catch(final IOException e1) {}
			}
		}
        setProgress(100);
        return null;
	}

	@Override
	public void propertyChange(PropertyChangeEvent e) {
		if("progress" == e.getPropertyName()) {
            int progress = (Integer)e.getNewValue();
            progressBar.setIndeterminate(false);
            progressBar.setValue(progress);
        }
	}
	
	/**
	 * Méthode permettant d'encoder le squelette récupéré en sauvegardant
	 * 
	 * @param positionsAndStatusSavingItem ComboItem contenant l'ensemble une frame (positions de l'ensemble des points du squelette à un instant t et des status) à enregistrer
	 * @return build chaîne de caractères contenant l'ensemble contenant positionsAndStatusSavingItem encodé, mais qui reste toujours compréhensible pour un utilisateur qui lira le fichier texte, où chaque ligne correspond à un instant t avec le listing des positions ou des status
	 */
	private String encodeArray(final ComboItem positionsAndStatusSavingItem) {
		String build = "";

		final float[] positions = positionsAndStatusSavingItem.getPositions();
		final int lenj = positions.length;
		build += "[";
		build += positions[0];
		for(int j = 1; j<60 && j<lenj; j++) {
			build += ",";
			build += positions[j];
		}
		build += "]";
		build += System.lineSeparator();

		final byte[] jointStatus = positionsAndStatusSavingItem.getJointStatus();
		if(jointStatus != null) {
			final int lenk = jointStatus.length;
			build += "[";
			build += jointStatus[0];
			for(int k = 1; k<20 && k<lenk; k++) {
				build += ",";
				build += jointStatus[k];
			}
			build += "]";
			build += System.lineSeparator();
		}

		return build;
	}
}