package org.cis120.electiongame;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class PolicyTest {
    /*
     * First, declare some presidents to use for testing
     */

    President georgeWash = new President(
            "George Washington", "South", 3, 10, 6, 10, "Libertarian", "Nationalism"
    );

    President abeLincoln = new President(
            "Abraham Lincoln", "Midwest", 3, 10, 9, 7, "Conservative", "Civil Rights"
    );

    President johnAdams = new President(
            "John Adams", "Northeast", 10, 3, 7, 8, "Conservative", "Social Hierarchy"
    );

    President fdr = new President(
            "Franklin D. Roosevelt", "Northeast", 5, 9, 10, 6, "Progressive", "Social Programs"
    );

    Election e1920 = new Election(
            1920, "infl", "pol", "South", "Active Executive vs. Limited Government",
            "Tough Foreign Policy vs. Isolationism", "Tax Cuts vs. Social Programs"
    );

    Election e1924 = new Election(
            1924, "infl", "pol", "West", "Tax Cuts vs. Social Programs",
            "Closed Borders vs. Open Borders", "Law and Order vs. Personal Freedom"
    );

    Election e1928 = new Election(
            1928, "infl", "nam", "South", "Traditional Morality vs. Social Liberalism",
            "Civil Rights vs. Racism", "Regulation vs. Deregulation"
    );

    Policy toughfp = new Policy("Tough Foreign Policy", "Progressive", "Conservative");
    Policy isolationism = new Policy(
            "Isolationism", "Progressive", "Libertarian", "Populist"
    );

    @Test
    public void testGetWinnerIllegalArg() {
        assertThrows(IllegalArgumentException.class, () -> {
            e1920.getWinner(georgeWash, null);
        });
    }

    @Test
    public void testGetScore() {
        int score = e1920.getScore(georgeWash);
        assertEquals(19, score);
    }

    @Test
    public void testGetTieBreaker() {
        boolean winner = e1920.getWinner(abeLincoln, fdr);
        assertFalse(winner);
    }

    @Test
    public void testGetWinner1() {
        boolean winner = e1920.getWinner(georgeWash, johnAdams);
        assertTrue(winner);
    }

    @Test
    public void testGetWinner2() {
        boolean winner = e1924.getWinner(johnAdams, fdr);
        assertFalse(winner);
    }

    @Test
    public void testGetWinner3() {
        boolean winner = e1920.getWinner(georgeWash, fdr);
        assertFalse(winner);
    }

    @Test
    public void testGetWinner4() {
        assertEquals(23, e1928.getScore(georgeWash));
        assertEquals(17, e1928.getScore(abeLincoln));
        boolean winner = e1928.getWinner(georgeWash, abeLincoln);
        assertTrue(winner);
    }
}
