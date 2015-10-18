package treesim;

public class AttributeNode {
	private String attributeName;
	private int attributeFrequency = 0;
	
	public String getAttributeName() {
		return attributeName;
	}
	public void setAttributeName(String attributeName) {
		this.attributeName = attributeName;
	}
	public int getAttributeFrequency() {
		return attributeFrequency;
	}
	public void setAttributeFrequency(int attributeFrequency) {
		this.attributeFrequency = attributeFrequency;
	}
}
