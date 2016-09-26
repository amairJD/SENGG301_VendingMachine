package seng301.assn1;

import org.lsmr.vending.frontend1.Pop;

// Same as Pop, but can store the price of the pop as well
public class Better_Pop extends Pop{

	private int price;
	
	public Better_Pop(String name, int in_price) {
		super(name);
		price = in_price;
	}
	
	public void setPrice(int in_price){
		price = in_price;
	}
		
	public int getPrice(){
		return price;
	}
}
