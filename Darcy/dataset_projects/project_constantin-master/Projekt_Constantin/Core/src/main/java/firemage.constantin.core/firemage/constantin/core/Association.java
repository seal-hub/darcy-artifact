package firemage.constantin.core;

public abstract class Association {

	private int relevance = 0;
	private int timeInMind = 0;
	
	
	public int getRelevance() {
		return relevance;
	}
	
	public void setRelevance(int relevance) {
		this.relevance = relevance;
	}
	
	public int getTimeInMind() {
		return timeInMind;
	}
	
	public void incrementTimeInMind() {
		timeInMind++;
	}
}
