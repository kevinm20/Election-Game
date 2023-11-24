package org.cis120.electiongame;

import java.util.ArrayList;
import java.util.Collections;

/*
 * These are the most important cards, representing the presidents. They have a
 * lot of important features.
 */

public class President extends Card {
    /*
     * This string represents the home region of the president
     */
    private final String region;

    /*
     * These ints represent the attributes of the president
     */
    private final int experience;
    private final int influence;
    private final int policy;
    private final int nameRec;

    /*
     * These strings are for policy matching
     */

    private final String ideology;
    private final String marquee;
    
    // This is for an extra description
    private String cardInfo = "No card info available";
    
    // This is for tags
    private String[] tags;

    // Constructor
    public President(
            String name, String region, int exp, int infl, int pol, int nam, String id, String marq
    ) {
        super.setName(name);
        this.region = region;
        this.experience = exp;
        this.influence = infl;
        this.policy = pol;
        this.nameRec = nam;
        this.ideology = id;
        this.marquee = marq;
    }
    
    // Constructor with description
    public President(
            String name, String region, int exp, int infl, int pol, int nam, String id, String marq, String[] tags, String desc
    ) {
        this(name, region, exp, infl, pol, nam, id, marq);
        this.cardInfo = desc;
        this.tags = tags;
    }
    
    // Constructor with tags
    public President(
            String name, String region, int exp, int infl, int pol, int nam, String id, String marq, String[] tags
    ) {
        this(name, region, exp, infl, pol, nam, id, marq);
        this.tags = tags;
    }

    String getRegion() {
        return region;
    }
    
    String getIdeology() {
        return ideology;
    }
    
    String[] getTags() {
    	return tags;
    }

    int getExp() {
        return experience;
    }

    int getInfl() {
        return influence;
    }

    int getPol() {
        return policy;
    }

    int getNam() {
        return nameRec;
    }
    
    double getWeightedAve() {
    	return (double)(.15*experience)+(double)(.35*influence)+(double)(.25*policy)+(double)(.25*nameRec);
    }
    
    String getCardInfo() {
    	return cardInfo;
    }

    // This is used for the AI
    int getBestAttributes() {
        ArrayList<Integer> attrs = new ArrayList<Integer>();
        attrs.add(experience);
        attrs.add(influence);
        attrs.add(policy);
        attrs.add(nameRec);
        Collections.sort(attrs);
        int max = attrs.get(3);
        int max2 = attrs.get(2);
        return max + max2;
    }

    // This is for elections
    int getAttributePair(String att1, String att2) {

        int a1 = 0;
        int a2 = 0;

        if (att1 == null || att2 == null) {
            throw new IllegalArgumentException();
        }

        switch (att1) {
            case "exp":
                a1 += experience;
                break;
            case "infl":
                a1 += influence;
                break;
            case "pol":
                a1 += policy;
                break;
            case "nam":
                a1 += nameRec;
                break;
            default:
                break;
        }

        switch (att2) {
            case "exp":
                a2 += experience;
                break;
            case "infl":
                a2 += influence;
                break;
            case "pol":
                a2 += policy;
                break;
            case "nam":
                a2 += nameRec;
                break;
            default:
                break;
        }

        /* If the string wasn't one of the switch cases
        if (a1 == 0 || a2 == 0) {
            throw new IllegalArgumentException();
        }
        */

        return a1 + a2;
    }

    // These are all mainly for election scoring
    int getTieBreaker() {
        return experience + influence + policy + nameRec;
    }
    
    boolean isMarquee(Policy p) {
        return marquee.equals(p.getName());
    }

    int checkScore(String att1, String att2, String reg) {
        int score = 0;

        if (reg.equals(region)) {
            score += 3;
        }

        score += getAttributePair(att1, att2);

        return score;
    }

    int checkMatch(Policy p) {
        if (marquee.equals(p.getName())) {
            return 5;
        } else if (p.checkIdeology(ideology)) {
            return 2;
        }
        return 0;
    }
    
    double getValue() {
    	double av = (.15*experience+.35*influence+.25*policy+.25*nameRec);
    	return (1.2868*(av*av)-0.6574*av-6.3713)*1000;
    }

    // I used this to easily get the file location of all the president cards
    public String getImageURL() {
        String target = super.getName().replaceAll("\\s", "").replace(".", "").replace("'", "");
        target = target.toLowerCase();
        return "files/" + target + ".PNG";
    }

    @Override
    public String toString() {
        return super.getName();
    }

}
