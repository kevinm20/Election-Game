package org.cis120.electiongame;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class ElectionTest {
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
    Policy taxcuts = new Policy("Tax Cuts", "Conservative", "Libertarian", "Populist");
    Policy socialprograms = new Policy("Social Programs", "Progressive", "Populist");

    @Test
    public void getScoreOnePresAndPolicy() {
        int score = e1920.getScore(georgeWash, isolationism);
        assertEquals(23, score);
    }

    @Test
    public void testCheckMatchMarquee() {
        int score = fdr.checkMatch(socialprograms);
        assertEquals(5, score);
    }

    @Test
    public void testCheckMatchIdeology() {
        int score = georgeWash.checkMatch(taxcuts);
        assertEquals(2, score);
    }

    @Test
    public void testCheckMatchNoIdeology() {
        int score = georgeWash.checkMatch(socialprograms);
        assertEquals(0, score);
    }

    @Test
    public void testGetPolMatchMain() {
        int score = e1924.getPolMatch(socialprograms);
        assertEquals(3, score);
    }

    @Test
    public void testGetPolMatchSide1() {
        int score = e1920.getPolMatch(toughfp);
        assertEquals(2, score);
    }

    @Test
    public void testGetPolMatchSide2() {
        int score = e1920.getPolMatch(socialprograms);
        assertEquals(2, score);
    }

    @Test
    public void testGetPolMatchNone() {
        int score = e1924.getPolMatch(toughfp);
        assertEquals(0, score);
    }

    @Test
    public void testGetScore1() {
        int score = e1920.getScore(georgeWash, isolationism, taxcuts);
        assertEquals(27, score);
    }

    @Test
    public void testGetScore2() {
        int score = e1920.getScore(fdr, toughfp, socialprograms);
        assertEquals(30, score);
    }

    @Test
    public void testGetWinner() {
        assertFalse(
                e1920.getWinner(georgeWash, isolationism, taxcuts, fdr, toughfp, socialprograms)
        );
    }

    @Test
    public void testGetWinnerReverse() {
        assertTrue(
                e1920.getWinner(fdr, toughfp, socialprograms, georgeWash, isolationism, taxcuts)
        );
    }

    @Test
    public void testSameCategorySameCard() {
        assertTrue(toughfp.sameCategory(toughfp));
    }

    @Test
    public void testSameCategoryDiffCard() {
        assertTrue(toughfp.sameCategory(isolationism));
    }

    @Test
    public void testSameCategoryDiffCategory() {
        assertFalse(toughfp.sameCategory(taxcuts));
    }
}
