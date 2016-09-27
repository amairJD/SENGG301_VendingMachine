package seng301.assn1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.lsmr.vending.frontend1.Coin;
import org.lsmr.vending.frontend1.Deliverable;
import org.lsmr.vending.frontend1.Pop;

public class VendingMachine {	
	
	private Map<SelectionButton, ArrayList<Pop>> buttonGrid;
	// Each selection button stores an index and price, and corresponds with a number/type of pops
	
	private Map<Integer, ArrayList<Coin>> treasury;
	// Total cash is kept in the 'treasury'. Represented as (K, V), where each (K)ey is the coin kind, 
	// and it's (V)alue is a List containing the coins.
	
	private List<Integer> coinKinds;
	
	private ArrayList<Coin> creditChute;
	// Inputed coins are stored here and stay here unless a purchase is made

	private int index;
	
	private ArrayList<Deliverable> deliveryChute;

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
		coinKinds = in_coinKinds;
		
		creditChute = new ArrayList<Coin>();
		index = indexCounter;
		
		deliveryChute = new ArrayList<Deliverable>();
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
		creditChute.add(coin);
	}
	
	public void loadCoin_Treasury(int coinKindIndex, Coin coin){
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

	public void pressButton(int value) {
		Integer inputMoney = countMoney();
		if (value < buttonGrid.size()){
			for (SelectionButton selButton: buttonGrid.keySet()){
				if (selButton.getIndex() == value){
					if (inputMoney < selButton.getPrice()){
						// Not enough cash
						return;
					}else {
						deliverPop(selButton);
						Integer outputMoney = inputMoney - selButton.getPrice(); // Calculate change
						// Now change must be withdrawn in respect to the coins in the treasury
						changeMaker(outputMoney);
					}
					break;
				}
			}
		}
	}

	
	// Delivers a pop to the deliveryChute from the corresponding selection button (if it's in stock)
	private void deliverPop(SelectionButton selButton) {
		if (buttonGrid.get(selButton) == null){
			// All pops are sold out
			return; 
		}
		ArrayList<Pop> popStash = buttonGrid.get(selButton);
		deliveryChute.add(popStash.get(0)); // Move a pop from the inventory to the chute
		buttonGrid.get(selButton).remove(0); // Make sure the pop is removed from the inventory
		
	}

	// This method creates change out of the total value needing to be outputed and delivers the coins to the deliveryChute
	// Will try to give the least number of coins for total value
	private void changeMaker(Integer outputMoney) {
		creditChute.clear();
		List<Integer> tmp_coinKinds = coinKinds;
		int numberOfCoin;
		
		while (tmp_coinKinds != null) {
			int maxType = Collections.max(tmp_coinKinds); 	//Get the largest coinKind, 
			numberOfCoin = outputMoney/maxType;  			// and calculate how many of those can be delivered
			while (numberOfCoin != 0) {
				if (treasury.get(maxType) != null){ 		//Check if machine has any coins stocked of MaxType
					Coin returnCoin = new Coin(maxType);
					deliveryChute.add(returnCoin);   		// Add the coin to the DeliveryChute
					treasury.get(maxType).remove(0); 		// Remove the coin from the Treasury
					outputMoney -= maxType;					// Update remaining outputMoney
					numberOfCoin--;							// Need one less coin of that type now
				} else
					numberOfCoin = 0;						// If treasury has no more of that coinType, reset numberOfCoin and stop the loop
			}
			tmp_coinKinds.remove(maxType);
		}
		if (outputMoney != 0){
			// Sorry, not enough change! Ask to contact customer service here (future assignment?)
			return;
		}
	}

	private Integer countMoney() {
		int inputMoney = 0;
		for (Coin coin: creditChute){
			inputMoney += coin.getValue();
		}
		return inputMoney;
	}

	public List<Deliverable> emptyDeliveryChute() {
		ArrayList<Deliverable> toDeliver = deliveryChute;
		deliveryChute.clear();
		return toDeliver;
	}

	public List<List<?>> unload() {
		
		List<Coin> treasuryCoins = new ArrayList<Coin>();
		List<Coin> creditCoins   = new ArrayList<Coin>();
		List<Pop>  popInventory  = new ArrayList<Pop>();
		
		for (int coinKind: treasury.keySet()){
			treasuryCoins.addAll(treasury.get(coinKind));
			treasury.get(coinKind).clear();
		}
		
		creditCoins = creditChute;
		creditChute.clear();
		
		for (SelectionButton popButton: buttonGrid.keySet()){
			popInventory.addAll(buttonGrid.get(popButton));
			buttonGrid.get(popButton).clear();
		}
		
		
		// TODO
		//List<List<?>> allLists = new ArrayList< List<Deliverable> >;
		
		return null;
	}
	
	
	
	
}
