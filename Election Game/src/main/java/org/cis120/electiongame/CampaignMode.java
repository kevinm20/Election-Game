package org.cis120.electiongame;

import java.util.ArrayList;
import java.util.List;

public class CampaignMode {
	private List<President> presidentCollection;
	private List<President> activeDeck;
	private int money;
	private String name;
	
	// Need something better to initialize game
	public CampaignMode(String name) {
		this.presidentCollection = new ArrayList<President>();
		this.money = 100;
		this.name = name;
	}
	
	/***GETTER METHODS***/
	public int getMoney() {
		return money;
	}
	
	/***ACTION METHODS***/
	// Buys a pack
	public void buyPack(Pack pack) {
		if(money>=pack.getCost()) {
			money-=pack.getCost();
			List<President> pulls = pack.pull();
			for (President p: pulls) {
				if(presidentCollection.contains(p)) {
					System.out.println("Sold " + p.toString() + " for " + p.getValue() + ".");
					money+=p.getValue();
				} else {
					presidentCollection.add(p);
				}
			}
		} else {
			System.out.println("Too broke to afford");
		}
	}
	
	// Buys a president. Adds them to the presidentCollection. Check for if they're already in the deck, or other edge cases?
	public void buyPresident(President p) {
		presidentCollection.add(p);
	}
	
	// Changes the money by the amount
	public void changeMoney(int amount) {
		money+=amount;
	}
	
	public void printDeck() {
		System.out.println(name + "'s Deck:");
		for (President pres: presidentCollection) {
			System.out.print(" " + pres.toString());
		}
	}
}
