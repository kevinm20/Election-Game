package org.cis120.electiongame;

import java.util.ArrayList;
import java.util.List;

public class CampaignUser {
	private List<President> deck;
	private int money;
	private String name;
	
	public CampaignUser(String name) {
		this.deck = new ArrayList<President>();
		this.money = 100;
		this.name = name;
	}
	
	public void buyPack(Pack pack) {
		if(money>=pack.getCost()) {
			money-=pack.getCost();
			List<President> pulls = pack.pull();
			for (President p: pulls) {
				if(deck.contains(p)) {
					System.out.println("Sold " + p.toString() + " for " + p.getValue() + ".");
					money+=p.getValue();
				} else {
					deck.add(p);
				}
			}
		} else {
			System.out.println("Too broke to afford");
		}
	}
	
	public int getMoney() {
		return money;
	}
	
	public void printDeck() {
		System.out.println(name + "'s Deck:");
		for (President pres: deck) {
			System.out.print(" " + pres.toString());
		}
	}
}
