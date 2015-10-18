package treesim;

public class TextNode {
	private String parentElementNodeHavingText;
	private int parentElementFrequency = 0;
	
	public String getParentElementNodeHavingText() {
		return parentElementNodeHavingText;
	}
	public void setParentElementNodeHavingText(String parentElementNodeHavingText) {
		this.parentElementNodeHavingText = parentElementNodeHavingText;
	}
	public int getParentElementFrequency() {
		return parentElementFrequency;
	}
	public void setParentElementFrequency(int parentElementFrequency) {
		this.parentElementFrequency = parentElementFrequency;
	}
}
