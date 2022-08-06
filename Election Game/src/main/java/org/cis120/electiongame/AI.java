package org.cis120.electiongame;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
/*
 * The AI was definitely the most difficult part of this game, so I'll try to
 * explain my process
 */

public class AI extends Player {
    // This map is used for the AI to get its best play, and maps a score to each
    // president
    private Map<President, Integer> pointMap;
    // These are the AI's 'guesses' of the key attributes and policies
    private String guessOne = "exp";
    private String guessTwo = "exp";
    private String guessMain = "";
    private String guessSide1 = "";
    private String guessSide2 = "";
    // This maps each president to a pair of policies
    private Map<President, ArrayList<Policy>> policyMap;
    private int gamesPlayed = 0;
    // I have no idea why eclipse is giving me a warning, I clearly use this
    // variable...
    private double difficulty;
    private double presAttributes = 0;

    public AI(String name) {
        super(name);
        this.pointMap = new TreeMap<President, Integer>();
        this.policyMap = new TreeMap<President, ArrayList<Policy>>();
        this.difficulty = .65;
        // initializes the point map with scores of 0, this was helpful for testing
        for (President curr : super.getHand()) {
            this.pointMap.put(curr, 0);
        }
    }

    // This is my example of dynamic dispatch from the rubric
    public void reset() {
        super.reset();
        pointMap.clear();
        policyMap.clear();
        gamesPlayed = 0;
    }

    public void setDifficulty(double d) {
        difficulty = d;
    }

    /*
     * Both of these methods work nearly the same way. Given the difficulty (prob)
     * of the AI,
     * it 'guesses' the attributes and policies of the election. I just used some
     * statistics
     * and a random number generation to have the AI 'guess' these desired traits,
     * and use it as the
     * basis for their mapping of scores to presidents. I commented out those
     * System.out.printlns, but
     * left them there to show what I did to test this
     */
    public void guessAttributes(Election e) {
        String real1 = e.getAttrOne();
        String real2 = e.getAttrTwo();

        double threshold2 = difficulty * difficulty;
        double threshold1 = 2 * (1 - difficulty) * difficulty + threshold2;

        double random = Math.random();

        ArrayList<String> attrs = new ArrayList<String>();

        attrs.add("exp");
        attrs.add("infl");
        attrs.add("pol");
        attrs.add("nam");

        if (random < threshold2) {
            guessOne = real1;
            guessTwo = real2;
            // System.out.println("Got both real attributes: " + guessOne + " " + guessTwo);
        } else if (random < threshold1) {
            guessOne = real1;
            attrs.remove(real1);
            int index = (int) Math.round(2 * Math.random());
            guessTwo = attrs.get(index);
            // System.out.println("Got one real attributes: " + guessOne + " " + guessTwo);
        } else {
            attrs.remove(real1);
            attrs.remove(real2);
            guessOne = attrs.get(0);
            guessTwo = attrs.get(1);
            // System.out.println("Got zero real attributes: " + guessOne + " " + guessTwo);
        }
    }

    public void guessPolicies(Election e) {
        String main = e.getMain();
        String side1 = e.getSide1();
        String side2 = e.getSide2();

        double threshold0 = difficulty * difficulty * difficulty;
        double threshold1 = 3 * (1 - difficulty) * difficulty * difficulty + threshold0;
        double threshold2 = 3 * (1 - difficulty) * (1 - difficulty) * difficulty + threshold1;

        double random = Math.random();

        ArrayList<String> categories = CardData.getCategories();

        if (random < threshold0) {
            guessMain = main;
            guessSide1 = side1;
            guessSide2 = side2;
            /*
             * System.out.println(
             * "Got all real policies: " + guessMain + " " + guessSide1 + " " + guessSide2
             * );
             */
        } else if (random < threshold1) {
            guessMain = main;
            guessSide1 = side1;
            categories.remove(main);
            categories.remove(side1);
            categories.remove(side2);
            int index = (int) Math.round((categories.size() - 1) * Math.random());
            guessSide2 = categories.get(index);
            /*
             * /*System.out.println(
             * "Got two real policies: " + guessMain + " " + guessSide1 + " " + guessSide2
             * + ". Side 2 was really " + side2
             * );
             */
        } else if (random < threshold2) {
            guessMain = main;
            categories.remove(main);
            categories.remove(side1);
            categories.remove(side2);
            int index1 = (int) Math.round((categories.size() - 1) * Math.random());
            guessSide1 = categories.get(index1);
            categories.remove(guessSide1);
            int index2 = (int) Math.round((categories.size() - 1) * Math.random());
            guessSide2 = categories.get(index2);
            /*
             * System.out.println(
             * "Got one real policy: " + guessMain + " " + guessSide1 + " " + guessSide2
             * + ". Side 1 was really " + side1 + " and Side 2 was really " + side2
             * );
             */
        } else {
            categories.remove(main);
            categories.remove(side1);
            categories.remove(side2);
            int index1 = (int) Math.round((categories.size() - 1) * Math.random());
            guessMain = categories.get(index1);
            categories.remove(guessMain);
            int index2 = (int) Math.round((categories.size() - 1) * Math.random());
            guessSide1 = categories.get(index2);
            categories.remove(guessSide1);
            int index3 = (int) Math.round((categories.size() - 1) * Math.random());
            guessSide2 = categories.get(index3);
            /*
             * System.out.println(
             * "Got no real policies: " + guessMain + " " + guessSide1 + " " + guessSide2
             * + ". Main was really " + main + " and Side 1 was really " + side1
             * + " and Side 2 was really " + side2
             * );
             */
        }
    }

    /*
     * Now for the difficult... these methods determine how the CPU picks its card
     */

    public President play(Deck d, Election e) {
        // first guess the attributes, and then construct an "idea" of what the AI
        // thinks the election is
        gamesPlayed++;
        guessAttributes(e);
        guessPolicies(e);
        Election idea = new Election(
                e.getYear(), guessOne, guessTwo, e.getRegion(), guessMain, guessSide1, guessSide2
        );

        // Now, it calls the helper function to map the best possible score for each
        // president
        mapScores(idea);

        // Iterate through the map, and get the highest score to play.
        President max = super.getHand().get(0);
        int maxScore = 0;
        for (Map.Entry<President, Integer> entry : pointMap.entrySet()) {
            if (entry.getValue() > maxScore) {
                max = entry.getKey();
                maxScore = entry.getValue();
            }
        }
        pointMap.remove(max);
        presAttributes = e.getScore(max);
        return super.play(max, d);
    }

    public double getPresAttributes() {
    	return presAttributes;
    }
    // This helper function is a little more complicated.
    public void mapScores(Election e) {
        // Track how many wins the opponent has
        int opponentWins = gamesPlayed - super.getWins();
        for (President curr : super.getHand()) {
            // The "potential points" is a reasonable highest point a president can get
            // normally (not for this specific election).
            int potentialPoints = curr.getBestAttributes() + 9;

            int high1 = 0;
            int high2 = 0;
            Policy p1 = null;
            Policy p2 = null;

            // iterate through all policies, and find which ones would give the highest
            // score to AI
            for (Policy pol : super.getPolicies()) {
                int score = e.getScore(curr, pol);
                if (score > high1) {
                    // The same category makes sure the AI doesn't play the same/opposite policies
                    if (pol.sameCategory(p1)) {
                        high1 = score;
                        p1 = pol;
                    } else {
                        high2 = high1;
                        high1 = score;
                        p2 = p1;
                        p1 = pol;
                    }
                } else if (score > high2 && !pol.sameCategory(p1)) {
                    high2 = score;
                    p2 = pol;
                }
            }

            /*
             * This part makes the AI more interesting. It will give it a tendency to "save"
             * better cards for later if the card is below it's "potential". For example,
             * it would give a penalty to George Washington if he might be their best play,
             * but would "waste" him in the first round if the election doesn't have good
             * desired attributes for him.
             */
            int potentialGap = potentialPoints - e.getScore(curr, p1, p2);

            int potentialPenalty = (int) Math.round((1 - (double) opponentWins / 2) * potentialGap);

            // For testing
            // System.out.println(
            // "Current max: " + e.getScore(curr, p1, p2) + " Gap: " + potentialGap
            // + " Penalty: " + potentialPenalty
            // );

            pointMap.put(curr, e.getScore(curr, p1, p2) - potentialPenalty);

            ArrayList<Policy> list = new ArrayList<Policy>();
            if (p1 == null || p2 == null) {
                // For testing
                System.out.println("FATAL ERROR: POLICY WAS NULL");
            }
            list.add(p1);
            list.add(p2);
            policyMap.put(curr, list);
            // Finally, map everything and add the policies to the list
        }
    }

    public ArrayList<Policy> playPolicies(President pres, Deck p) {
        ArrayList<Policy> ret = policyMap.get(pres);
        policyMap.remove(pres);
        return super.playPolicies(ret, p);
    }

}
