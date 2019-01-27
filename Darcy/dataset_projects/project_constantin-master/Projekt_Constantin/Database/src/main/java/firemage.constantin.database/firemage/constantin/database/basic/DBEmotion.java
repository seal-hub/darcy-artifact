package firemage.constantin.database.basic;

public class DBEmotion {

	private String emotionName;
	private int emotionAmount;
	
	public DBEmotion(String name, int amount) {
		this.emotionName = name;
		this.emotionAmount = amount;
	}

	public String getEmotionName() {
		return emotionName;
	}

	public void setEmotionName(String emotionName) {
		this.emotionName = emotionName;
	}

	public int getEmotionAmount() {
		return emotionAmount;
	}

	public void setEmotionAmount(int emotionAmount) {
		this.emotionAmount = emotionAmount;
	}
}
