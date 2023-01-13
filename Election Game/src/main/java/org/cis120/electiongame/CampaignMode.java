package org.cis120.electiongame;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class CampaignMode implements Runnable {
	private List<President> presidentCollection;
	private List<President> activeDeck;
	private int money;
	private String name;
	private int cardSize = 225;
	
	/***
	 * Main Game Action Here
	 */
	public void run() {
		// Top-level frame in which game components live
		final JFrame frame = new JFrame("Election Game - Campaign Mode");
		frame.setLocation(0, 0);
		
		// Game Info
		final JPanel info = new JPanel();
		info.setPreferredSize(new Dimension(175, 200));

		frame.add(info, BorderLayout.WEST);
		final JLabel moneyCount = new JLabel(String.valueOf(money), SwingConstants.CENTER);
		moneyCount.repaint();
		info.add(moneyCount);
		
		// Game Buttons
		final JPanel buttons = new JPanel();
		buttons.setPreferredSize(new Dimension(175, 200));

		frame.add(buttons, BorderLayout.EAST);
		final JLabel nothing = new JLabel(String.valueOf(money), SwingConstants.CENTER);
		nothing.repaint();
		buttons.add(nothing);
		
		// TBD
		final JPanel center = new JPanel();
		frame.add(center, BorderLayout.CENTER);
		center.setPreferredSize(new Dimension(1000, 1000));
		
		// Set up frame
		frame.pack();
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		
		// Populate all the cards into the game
		List<President> presidents = CardData.getPresidents("expanded", null, 0.0);
		/*
		 * Set up the game if the user just started and has no cards
		 */
		if(true) {
			JOptionPane.showMessageDialog(null,
					"Welcome to Election Game Campaign Mode! It seems like you have not yet started with this account.\n"
					+ "Let's setup your deck with the first opening packs and 100 Money!");
			money=100;
			List<President> presidentCollection = new ArrayList<President>();
			moneyCount.repaint();
			info.repaint();
			// Packs should be something visual
			JOptionPane.showMessageDialog(null,
					"For your first pack, you'll get one 5.0+ rated President. Click OK to open!");
			// Starter pack of good president
			Pack starterElitePack = new Pack(1, 0, 5, 10, presidents, null);
			President starterElite = starterElitePack.pull().get(0);
			presidentCollection.add(starterElite);
			
			ImageIcon starterEliteCard = new ImageIcon(new ImageIcon(starterElite.getImageURL()).getImage().getScaledInstance(cardSize,
					(int) Math.round(1.32713755 * cardSize), Image.SCALE_SMOOTH));
			final JLabel starterEliteCardButton = new JLabel(starterEliteCard);
			
			center.add(starterEliteCardButton);
			frame.revalidate();
			frame.repaint();
			
			// Next pack, 3 mid presidents
			JOptionPane.showMessageDialog(null,
					"Congrats on your " + starterElite.toString() + "! Hopefully they can lead you to glory!\n"
							+ "Next, let's get 3 mid-tier cards to round out your deck!");

			Pack starterMidPack = new Pack(3, 0, 2.5, 4.0, presidents, null);
			List<President>starterMid = starterMidPack.pull();
			presidentCollection.addAll(starterMid);
			
			JPanel midCards = new JPanel();
			for(President curr: starterMid) {
				ImageIcon img = new ImageIcon(new ImageIcon(curr.getImageURL()).getImage().getScaledInstance(cardSize,
						(int) Math.round(1.32713755 * cardSize), Image.SCALE_SMOOTH));
				final JLabel midCard = new JLabel(img);
				midCards.add(midCard);
			}
			center.remove(starterEliteCardButton);
			center.add(midCards);
			frame.revalidate();
			frame.repaint();
			
			// Finally, 6 starter presidents
			JOptionPane.showMessageDialog(null,
					"You're beginning to build out a squad! Here's the last starter pack to fill out your deck:");

			Pack starterPack = new Pack(6, 0, 0, 2.5, presidents, null);
			List<President>starter = starterPack.pull();
			presidentCollection.addAll(starter);
			
			center.remove(midCards);
			center.setLayout(new GridLayout(1, 5));
			JPanel starterCards = new JPanel();
			for(President curr: starter) {
				ImageIcon img = new ImageIcon(new ImageIcon(curr.getImageURL()).getImage().getScaledInstance(cardSize,
						(int) Math.round(1.32713755 * cardSize), Image.SCALE_SMOOTH));
				final JLabel starterCard = new JLabel(img);
				starterCards.add(starterCard);
			}
			center.add(starterCards);
			frame.revalidate();
			frame.repaint();
			
			//Finalize?
			JOptionPane.showMessageDialog(null,
					"Nice! Here's your full beginner deck:");
			JPanel currentCards = new JPanel();
			for(President curr: presidentCollection) {
				ImageIcon img = new ImageIcon(new ImageIcon(curr.getImageURL()).getImage().getScaledInstance(cardSize,
						(int) Math.round(1.32713755 * cardSize), Image.SCALE_SMOOTH));
				final JLabel currCard = new JLabel(img);
				currentCards.add(currCard);
			}
			center.remove(starterCards);
			center.setLayout(new GridLayout(1, 5));
			center.add(currentCards);
			frame.revalidate();
			frame.repaint();
		}
	}
	
	/*
	// Need something better to initialize game
	public CampaignMode(String name) {
		this.presidentCollection = new ArrayList<President>();
		this.money = 100;
		this.name = name;
	}
	*/
	
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
