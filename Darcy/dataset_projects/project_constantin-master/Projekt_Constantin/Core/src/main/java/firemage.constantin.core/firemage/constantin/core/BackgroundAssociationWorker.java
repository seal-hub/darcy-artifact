package firemage.constantin.core;

import java.util.Vector;

public class BackgroundAssociationWorker implements Runnable {
	
	private Mind mind;
	private AssociationFinder finder;
	
	public BackgroundAssociationWorker() {
		mind = Mind.getInstance();
		finder = new AssociationFinder();
	}

	@Override
	public void run() {
		while(!Thread.currentThread().isInterrupted()) {
			safeSleep(10);
			
			incrementTimeInMinds();
			
			addNewAssocitaions();
			
			calcNewImportance();
			
			removeUnusedAssociations();
			
		}

	}
	
	private void incrementTimeInMinds() {
		mind.getAssociations().forEach(a -> a.incrementTimeInMind());
	}
	
	private void addNewAssocitaions() {
		Vector<Association> newAssociations = new Vector<>();
		for(Association association : mind.getAssociations()) {
			newAssociations.addAll(finder.findAssociations(association));
		}
	}
	
	private void calcNewImportance() {
		
	}

	private void removeUnusedAssociations() {
		
	}
	
	private void safeSleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch(InterruptedException ex) {
			Thread.currentThread().interrupt();
		}
	}
}
