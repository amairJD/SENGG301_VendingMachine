package seng301.assn1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.lsmr.vending.frontend1.Coin;
import org.lsmr.vending.frontend1.Deliverable;
import org.lsmr.vending.frontend1.Pop;

public class VendingMachine {	
	
	private Map<SelectionButton, ArrayList<Pop>> buttonGrid;
	// Each 'Selection Button' stores an index, price, and corresponds with a type of pop
	
	private Map<Integer, ArrayList<Coin>> treasury;
	// Total cash is kept in the 'treasury'. Represented as (K, V), where each Key is the coin kind, 
	// and it's Value is a List containing the coins.
	
	private List<Integer> coinKinds;
	// A List of coin types supported by the machine
	
	private ArrayList<Coin> creditChute;
	// Inputed coins are stored here and stay here unless a purchase is made
	
	private ArrayList<Coin> paymentContainer;
	// Money used for payment is stored here

	private int index;
	
	private ArrayList<Deliverable> deliveryChute;
	// Purchases and change are transferred here

	
	// Use this constructor
	VendingMachine(List<Integer> in_coinKinds, int selectionButtonCount, int indexCounter){
		
		buttonGrid = new LinkedHashMap<SelectionButton, ArrayList<Pop> >();
		for(int i = 0; i < selectionButtonCount; i++){
			buttonGrid.put(new SelectionButton(i, 0), new ArrayList<Pop>());
		}
		
		treasury = new LinkedHashMap<Integer, ArrayList<Coin> >();
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
		// Make sure all sections are empty before configuration
		for (SelectionButton sb: buttonGrid.keySet()){
			buttonGrid.get(sb).clear();
		}
		// Configure Pops
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
	    if (!coinKinds.contains(coin.getValue())){    // If an invalid coin type is inserted, 
	    	deliveryChute.add(coin);				  // move it directly to the delivery chute
	    }
	    else 
	    	creditChute.add(coin);					  // else, add it to the credit chute
	}
	
	public void loadCoin_Treasury(int coinKindIndex, Coin coin){
		int coinType = coinKinds.get(coinKindIndex);
		treasury.get(coinType).add(coin);
	}

	// Moves the coins in the credit chute to the payment Container
	@SuppressWarnings("unchecked")
	public void credit_to_paymentContainer(){
		paymentContainer = (ArrayList<Coin>) creditChute.clone();
		creditChute.clear();
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
		if (value < buttonGrid.size()){
			for (SelectionButton selButton: buttonGrid.keySet()){
				if (selButton.getIndex() == value){
					Integer inputMoney = countMoney();
					if (inputMoney < selButton.getPrice()){
						// Not enough cash
						return;
					}else {
						deliverPop(selButton);
						credit_to_paymentContainer(); // move payment $$ from credit to payment Container
						Integer outputMoney = inputMoney - selButton.getPrice(); // Calculate change value
						// Now change must be withdrawn in respect to the coins in the treasury
						changeMaker(outputMoney);
					}
					break;
				}
			}
		}
	}

	// Counts the total money in the credit chute and returns it as an Integer
	private Integer countMoney() {
		int inputMoney = 0;
		for (Coin coin: creditChute){
			inputMoney += coin.getValue();
		}
		return inputMoney;
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
	
	
	// Creates change out of the total value needing to be outputed, and delivers the coins to the deliveryChute
	// Will try to give the least number of coins possible for total value
	private void changeMaker(Integer outputMoney) {
		List<Integer> tmp_coinKinds = coinKinds;
		int numberOfCoin;
		
		while (!tmp_coinKinds.isEmpty()) {
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
			tmp_coinKinds.remove(Integer.valueOf(maxType));
		}
		if (outputMoney != 0){
			// Sorry, not enough change! Ask to contact customer service here (future assignment?)
			return;
		}
	}

	@SuppressWarnings("unchecked")
	public List<Deliverable> emptyDeliveryChute() {
		ArrayList<Deliverable> toDeliver = (ArrayList<Deliverable>) deliveryChute.clone();
		deliveryChute.clear();
		return toDeliver;
	}

	@SuppressWarnings("unchecked")
	public List<List<?>> unload() {
		
		List<Coin> treasuryCoins = new ArrayList<Coin>();
		List<Coin> creditCoins   = new ArrayList<Coin>();
		List<Pop>  popInventory  = new ArrayList<Pop>();
		
		for (int coinKind: treasury.keySet()){
			treasuryCoins.addAll(treasury.get(coinKind));
			treasury.get(coinKind).clear();
		}
		
		creditCoins = (List<Coin>) paymentContainer.clone();
		paymentContainer.clear();
		
		for (SelectionButton popButton: buttonGrid.keySet()){
			popInventory.addAll(buttonGrid.get(popButton));
			buttonGrid.get(popButton).clear();
		}
		
		List<List<?>> allLists = new ArrayList<List<?>>();
		allLists.add(treasuryCoins);
		allLists.add(creditCoins);
		allLists.add(popInventory);

		return allLists;
	}
	
}
