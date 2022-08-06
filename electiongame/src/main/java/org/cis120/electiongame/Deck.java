package org.cis120.electiongame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/*
 * This class is for the deck of cards you can draw from, from each category of
 * cards.
 * It mainly just has methods for shuffling and drawing cards from the deck.
 */
public class Deck {
    private List<Card> cards;
    private int size;

    public Deck() {
        this.cards = new ArrayList<Card>();
        this.size = 0;
    }

    public Deck(List<Card> cds) {
        this.cards.addAll(cds);
        this.size = cds.size();
    }

    public int getSize() {
        return size;
    }

    // Because the list is of type card, I had to make these methods for each card
    // type for it to compile.
    public void fillElec(List<Election> cds) {
        this.cards.addAll(cds);
        this.size = cds.size();
    }

    public void fillPres(List<President> pres) {
        this.cards.addAll(pres);
        this.size = pres.size();
    }

    public void fillPol(List<Policy> pols) {
        this.cards.addAll(pols);
        this.size = pols.size();
    }

    public void shuffle() {     
        for (int k = cards.size() - 1; k > 0; k--) {
            int howMany = k + 1;
            int start = 0;
            int randPos = (int) (Math.random() * howMany) + start;
            Card temp = cards.get(k);
            cards.set(k, cards.get(randPos));
            cards.set(randPos, temp);
            size = cards.size();
        }
        Collections.shuffle(cards);
    }

    public Card draw() {
        if (cards.size() == 0) {
            return null;
        }
        size--;
        Card c = cards.get(size);
        cards.remove(c);
        return c;
    }

    public void clear() {
        cards.clear();
        size = 0;
    }

}
