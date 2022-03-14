package org.cis120.electiongame;

import java.util.ArrayList;
import java.util.List;

/*
 * This class mainly represents the user player. I made the AI an extension of
 * it because the user doesn't need those methods,
 * and it was easier to solve and trace my code/bugs to keep them as two
 * separate classes. So most of these methods are designed for the user.
 */
public class Player {
    private List<President> hand;
    private List<Policy> policies;
    private String name;
    private int wins;

    public Player(String name) {
        this.hand = new ArrayList<President>();
        this.policies = new ArrayList<Policy>();
        this.name = name;
        this.wins = 0;
    }

    public void reset() {
        hand.clear();
        policies.clear();
        wins = 0;
    }

    public String getName() {
        return name;
    }

    // Length is max'd at 7 for the status to fit on screen.
    public void setName(String s) {
        if (s != null) {
            name = s.substring(0, Math.min(20, s.length()));
        }
    }

    public President getCard(int index) {
        return hand.get(index);
    }

    public List<President> getHand() {
        return hand;
    }

    public Policy getPolicy(int index) {
        return policies.get(index);
    }

    public List<Policy> getPolicies() {
        return policies;
    }

    public int getPoliciesSize() {
        return policies.size();
    }

    // Draw the initial cards for the player
    public void drawInit(Deck d, Deck poldeck) {
        for (int i = 0; i < 5; i++) {
            Card c = d.draw();
            hand.add((President) c);
        }
        for (int i = 0; i < 15; i++) {
            Card p = poldeck.draw();
            policies.add((Policy) p);
        }
    }

    public President play(President c, Deck d) {
        hand.remove(c);
        Card n = d.draw();
        hand.add((President) n);
        return c;
    }

    public ArrayList<Policy> playPolicies(ArrayList<Policy> pols, Deck d) {
        policies.remove(pols.get(1));
        policies.remove(pols.get(0));
        Card p = d.draw();
        policies.add((Policy) p);
        Card p2 = d.draw();
        policies.add((Policy) p2);
        return pols;
    }

    // These two methods are for the user to place a card before playing it in-game.
    public President place(President c) {
        hand.remove(c);
        return c;
    }

    public Policy place(Policy c) {
        policies.remove(c);
        return c;
    }

    public void draw(Deck d) {
        Card n = d.draw();
        hand.add((President) n);
    }

    public void drawPols(Deck d) {
        Card p = d.draw();
        policies.add((Policy) p);
        Card p2 = d.draw();
        policies.add((Policy) p2);
    }

    public void add(President c) {
        hand.add(c);
    }

    public void add(Policy c) {
        policies.add(c);
    }

    public void winRound() {
        wins++;
    }

    public int getWins() {
        return wins;
    }

    // Kept this to make sure that the difficulty was being set on a CPU, not a user
    public void setDifficulty(double d) {
        System.out.println("Error: this is a user player, not CPU");
    }

    // This will return a string of all the player's cards
    @Override
    public String toString() {
        String target = name + "'s Cards: ";
        for (Card curr : hand) {
            target += curr.toString() + ", ";
        }
        target += "POLICIES: ";
        for (Card po : policies) {
            target += po.toString() + ", ";
        }
        target += " Wins: ";
        target += wins;
        return target;
    }

}
