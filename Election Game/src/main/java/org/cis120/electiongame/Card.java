package org.cis120.electiongame;

/*
 * The card class is the parent of presidents, policies, and elections.
 * It is mainly used for implementing comparable for all of the card types,
 * and letting them all used in decks.
 * 
 * Throughout all the classes, I had to make some methods visible to
 * package/public for testing purposes.
 */
public class Card implements Comparable<Card> {
    private String name = "";

    public Card() {

    }

    String getName() {
        return name;
    }

    public void setName(String nm) {
        name = nm;
    }

    @Override
    public int compareTo(Card o) {
        return (this.name).compareTo(o.getName());
    }

}
