package firemage.constantin.core;

import java.util.Hashtable;

/**
 * Represents the current emotional state
 * @author firemage
 * @see Emotion
 */
public class EmotionPool {

	private static EmotionPool ref;
	
	private Hashtable<Emotion, Integer> emotionMap = new Hashtable<>();
	
	
	
	private EmotionPool() {}
	
	/**
	 * 
	 * @return An instance of the EmotionPool class
	 */
	public EmotionPool getInstance() {
		if(ref == null)
			ref = new EmotionPool();
		
		return ref;
	}
	
	/**
	 * Adds the <code>value</code> to the <code>emotion</code>
	 * @param emotion The emotion to which should be added
	 * @param value The value to add
	 */
	public void addToEmotion(Emotion emotion, int value) {
		emotionMap.put(emotion, emotionMap.get(emotion) + value);
	}
	
	/**
	 * Removes the <code>value</code> from the <code>emotion</code>
	 * @param emotion The emotion from which should be removed
	 * @param value THe value to remove
	 */
	public void removeFromEmotion(Emotion emotion, int value) {
		emotionMap.put(emotion, emotionMap.get(emotion) - value);
	}
}
