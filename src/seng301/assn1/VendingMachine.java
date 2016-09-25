package seng301.assn1;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class VendingMachine {
	private ArrayList<Pop>  selectButtons; 
	private List<Integer> coinKinds;
	private Integer treasury; // Total cash is kept here
	private Integer change; // Store temporary change here that may be returned
	private int index;

	// Default Constructor, DO NOT use!
	// Use second constructor and pass in ID from Factory
	VendingMachine(){
	
	};
	
	
	// Pass in ID from Factory to this constructor
	VendingMachine(List<Integer> in_coinKinds, int selectionButtonCount, int indexCounter){
		selectButtons = new ArrayList<Pop>(selectionButtonCount);
		coinKinds = in_coinKinds;
		index = indexCounter;
	}


	public void configurePops(List<String> popNames, List<Integer> popCosts) {
		
		selectButtons.clear();
		
		Iterator<String> popNames_Iterator = popNames.iterator();
		Iterator<Integer> popCosts_Iterator = popCosts.iterator();		
		
		while (popNames_Iterator.hasNext() && popCosts_Iterator.hasNext()) {
			selectButtons.add(new Pop(popNames_Iterator.next(), popCosts_Iterator.next()));
		}
		
	}


	public void setIndex(int vmIndex) {
		index = vmIndex;
	};
	
	public int getIndex(){
		return index;
	}
	
	
	
	
}
