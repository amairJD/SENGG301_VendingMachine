package seng301.assn1;

public class SelectionButton {
	private int selIndex;
	private Integer price;
	private String storedPopName;
	
	public SelectionButton() {
		selIndex = 0;
		price = 0;
	}
	
	public SelectionButton(int in_index, int in_price) {
		selIndex = in_index;
		price = in_price;
		storedPopName = "";
	}
	
	public void setStoredPopName(String popName){
		storedPopName = popName;
	}
	
	public void setPrice(Integer in_price){
		price = in_price;
	}
	
	public int getIndex(){
		return selIndex;
	}

	public Integer getPrice() {
		return price;
	}
	
	public String getName() {
		return storedPopName;
	}

}
