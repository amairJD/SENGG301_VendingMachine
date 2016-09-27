package seng301.assn1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.lsmr.vending.frontend1.Coin;
import org.lsmr.vending.frontend1.Pop;

public class VendingMachine {	
	
	private Map<SelectionButton, ArrayList<Pop>> buttonGrid;
	// Each selection button stores an index and price, and corresponds with a number/type of pops
	
	private Map<Integer, ArrayList<Coin>> treasury;
	// Total cash is kept in the 'treasury'. Represented as (K, V), where each (K)ey is the coin kind, 
	// and it's (V)alue is a List containing the coins.
	
	private ArrayList<Coin> changeBucket;
	// Change is stored here temporarily and stays unless a purchase is made

	private int index;

	// Pass in ID from Factory to this constructor
	VendingMachine(List<Integer> in_coinKinds, int selectionButtonCount, int indexCounter){
		
		buttonGrid = new HashMap<SelectionButton, ArrayList<Pop> >();
		for(int i = 0; i < selectionButtonCount; i++){
			buttonGrid.put(new SelectionButton(i, 0), new ArrayList<Pop>());
		}
		
		treasury = new HashMap<Integer, ArrayList<Coin> >();
		for (int coinKind: in_coinKinds){
			treasury.put(coinKind, new ArrayList<Coin>());
		}
		
		changeBucket = new ArrayList<Coin>();
		index = indexCounter;
	}
	
	// Basic Constructor, DO NOT use!
	VendingMachine(){
		
	};

	public void configurePops(List<String> popNames, List<Integer> popCosts) {
		buttonGrid.clear();
		
		if(popNames.size() <= buttonGrid.size() && popCosts.size() <= buttonGrid.size()){
			Iterator<String> popNames_Iterator = popNames.iterator();
			Iterator<Integer> popCosts_Iterator = popCosts.iterator();		
			while (popNames_Iterator.hasNext() && popCosts_Iterator.hasNext()) {
				for (SelectionButton selButton: buttonGrid.keySet()){
					selButton.setStoredPopName(popNames_Iterator.next());
					selButton.setPrice(popCosts_Iterator.next());
				}
			}
		}		
	}

	public void setIndex(int vmIndex) {
		index = vmIndex;
	};
	
	public int getIndex(){
		return index;
	}
	
	public void insertCoin_Purchase(Coin coin){
		changeBucket.add(coin);
	}
	
	public void insertCoin_Treasury(int coinKindIndex, Coin coin){
		if (treasury.get(coinKindIndex) != null)
			treasury.get(coinKindIndex).add(coin);
	}


	public void depositPops(int popIndex, Pop... pops) {
		for (SelectionButton selButton: buttonGrid.keySet()){
			if (selButton.getIndex() == popIndex){
				for(Pop pop: pops){
					buttonGrid.get(selButton).add(pop);
				}
				return;
			}
		}
	}
	
	
	
	
}
