package org.cis120.electiongame;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * This tests the game state. I didn't have too many tests here, because the
 * game
 * state is mostly just composed of functions from other cards that were tested
 * elsewhere, or the functions were very simple getter methods.
 */

public class GameTest {

    @Test
    public void test() {
        assertNotEquals("CIS 120", "CIS 160");
    }

    ElectionGame e = new ElectionGame();

    @Test
    public void testDeckSizes() {
        e.riggedReset();

        assertEquals(e.getPres().getSize(), 42);
        assertEquals(e.getPol().getSize(), 18);
    }

    @Test
    public void testHandContains() {
        e.riggedReset();

        assertTrue(e.getPlayer1().getHand().contains(CardData.barryGoldwater));
        assertEquals(e.getPlayer1().getHand().size(), 5);
        assertEquals(e.getPlayer1().getPoliciesSize(), 15);
    }

    @Test
    public void testPlay() {
        e.riggedReset();
        e.getPlayer1().play(CardData.barryGoldwater, e.getPres());

        assertFalse(e.getPlayer1().getHand().contains(CardData.barryGoldwater));
        assertEquals(e.getPlayer1().getHand().size(), 5);
        assertEquals(e.getPlayer1().getPoliciesSize(), 15);
    }

    @Test
    public void testRound() {
        e.riggedReset();
        President play1 = e.getPlayer1().play(CardData.barryGoldwater, e.getPres());
        President play2 = e.getPlayer2().play(CardData.rutherfordBHayes, e.getPres());
        e.playRound(play1, play2);
        // The game state is correctly printed here
        assertEquals(e.getPlayer1().getWins(), 1);
        assertEquals(e.getPlayer2().getWins(), 0);
    }
}
