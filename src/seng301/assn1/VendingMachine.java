package seng301.assn1;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.lsmr.vending.frontend1.Coin;
import org.lsmr.vending.frontend1.Pop;

public class VendingMachine {
	private ArrayList<Better_Pop>  selectButtons; 
	
	// Total cash is kept in the 'treasury'. Represented as (K, V), where each key is the coin kind, 
	// and it's value is the number of those coins. I.e, (5, 3) means that there are 3 nickels
	private Map<Integer, Integer> treasury; 
	
	private Integer change; // Store temporary change here that may be returned
	private int index;

	// Default Constructor, DO NOT use!
	// Use second constructor and pass in ID from Factory
	VendingMachine(){
	
	};
	
	
	// Pass in ID from Factory to this constructor
	VendingMachine(List<Integer> in_coinKinds, int selectionButtonCount, int indexCounter){
		selectButtons = new ArrayList<Better_Pop>(selectionButtonCount);		
		for (int coinKind: in_coinKinds){
			treasury.put(coinKind, 0);
		}
		change = 0;
		index = indexCounter;
	}


	public void configurePops(List<String> popNames, List<Integer> popCosts) {
		selectButtons.clear();
		
		Iterator<String> popNames_Iterator = popNames.iterator();
		Iterator<Integer> popCosts_Iterator = popCosts.iterator();		
		while (popNames_Iterator.hasNext() && popCosts_Iterator.hasNext()) {
			selectButtons.add(new Better_Pop(popNames_Iterator.next(), popCosts_Iterator.next()));
		}
		
	}

	public void setIndex(int vmIndex) {
		index = vmIndex;
	};
	
	public int getIndex(){
		return index;
	}
	
	
	// >?????
	public void depositCoin(int coinKindIndex, Coin coin){
		int val = coin.getValue();
		treasury.put(coinKindIndex, 1);
	}


	public void depositPop(Pop pop) {
		
		
	}
	
	
	
	
}
