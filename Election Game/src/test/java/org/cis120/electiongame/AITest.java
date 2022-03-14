package org.cis120.electiongame;

import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

/*
 * 
 * Most of my testing for the AI was just done in the election game and printing
 * stuff out
 */
public class AITest {
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

    AI ai = new AI("ai");

    @Test
    public void testListRemoval() {
        ArrayList<String> attrs = new ArrayList<String>();
        attrs.add("exp");
        attrs.add("infl");
        attrs.add("pol");
        attrs.add("nam");

        String pol = "exp";
        attrs.remove(pol);
        assertFalse(attrs.contains("exp"));
    }

}
