package org.cis120.electiongame;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class PresidentTest {
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

    /*
     * President testing
     */
    @Test
    public void testGetBestAttributes() {
        assertEquals(19, abeLincoln.getBestAttributes());
    }

    @Test
    public void testGetURL() {
        assertEquals("files/georgewashington.png", georgeWash.getImageURL());
    }

    @Test
    public void testGetURLPeriods() {
        assertEquals("files/franklindroosevelt.png", fdr.getImageURL());
    }

    @Test
    public void testGetName() {
        assertEquals("George Washington", georgeWash.getName());
    }

    @Test
    public void testGetRegion() {
        assertEquals("South", georgeWash.getRegion());
    }

    @Test
    public void testGetExp() {
        assertEquals(3, georgeWash.getExp());
    }

    @Test
    public void testGetInfl() {
        assertEquals(10, georgeWash.getInfl());
    }

    @Test
    public void testGetPol() {
        assertEquals(6, georgeWash.getPol());
    }

    @Test
    public void testGetNam() {
        assertEquals(10, georgeWash.getNam());
    }

    @Test
    public void testGetAttributePair() {
        int res = georgeWash.getAttributePair("exp", "infl");
        assertEquals(13, res);
    }

    @Test
    public void testGetAttributePairIllegalArg() {
        assertThrows(IllegalArgumentException.class, () -> {
            georgeWash.getAttributePair("hello", "infl");
        });
    }

    @Test
    public void testGetAttributePairNullInput() {
        assertThrows(IllegalArgumentException.class, () -> {
            georgeWash.getAttributePair(null, "infl");
        });
    }

    @Test
    public void testTieBreaker() {
        assertEquals(29, georgeWash.getTieBreaker());
    }

    @Test
    public void testCheckScore() {
        assertEquals(16, georgeWash.checkScore("exp", "infl", "South"));
    }

    @Test
    public void testCheckScoreNoSwing() {
        assertEquals(13, georgeWash.checkScore("exp", "infl", "North"));
    }

    @Test
    public void testCheckScoreIllegalArg() {
        assertThrows(IllegalArgumentException.class, () -> {
            georgeWash.checkScore("clownery", "infl", "North");
        });
    }
}
