package org.cis120.electiongame;

/*
 * Elections are a pretty complicated class, with a bunch of features
 */
public class Election extends Card {
    private int year;
    private String region;
    private String att1;
    private String att2;
    private String mainIssue;
    private String side1;
    private String side2;

    public Election(
            int year, String att1, String att2, String region, String main, String side1,
            String side2
    ) {
        this.year = year;
        this.region = region;
        this.att1 = att1;
        this.att2 = att2;
        super.setName(String.valueOf(year));
        this.mainIssue = main;
        this.side1 = side1;
        this.side2 = side2;
    }

    int getYear() {
        return year;
    }

    String getRegion() {
        return region;
    }

    // Helper methods and then methods to get the score of an election based on what
    // the player played
    int getPolMatch(Policy p) {
        if (mainIssue.equals(p.getCategory())) {
            return 3;
        } else if (side1.equals(p.getCategory()) || side2.equals(p.getCategory())) {
            return 2;
        }
        return 0;
    }

    int getScore(President pres) {
        return pres.checkScore(att1, att2, region);
    }

    int getScore(President pres, Policy p1, Policy p2) {
        int score = pres.checkScore(this.att1, this.att2, this.region);
        score = score + pres.checkMatch(p1) + pres.checkMatch(p2) + getPolMatch(p1)
                + getPolMatch(p2);
        return score;
    }

    /*
     * Because this method below is only used for the AI, I gave a 1 point boost for
     * ideology match.
     * Since the AI "knows" its guesses are imperfect, it ended up being to have it
     * weight ideology matches over side issues.
     */
    int getScore(President pres, Policy p1) {
        int score = pres.checkScore(this.att1, this.att2, this.region);
        score = score + pres.checkMatch(p1) + getPolMatch(p1);
        if (pres.checkMatch(p1) > 0) {
            score++;
        }
        return score;
    }

    boolean getWinner(President user, Policy p1, Policy p2, President cpu, Policy p3, Policy p4) {
        if (user == null || cpu == null) {
            throw new IllegalArgumentException();
        }

        int userScore = getScore(user, p1, p2);
        int cpuScore = getScore(cpu, p3, p4);

        if (userScore > cpuScore) {
            return true;
        } else if (userScore < cpuScore) {
            return false;
        } else if (user.getTieBreaker() > cpu.getTieBreaker()) {
            return true;
        } else if (user.getTieBreaker() < cpu.getTieBreaker()) {
            return false;
        } else {
            return Math.random() > .5;
        }
    }

    // I kept this method in for my earlier tests before I implemented policies, I
    // don't want to delete all those tests so I kept this in.
    boolean getWinner(President user, President cpu) {
        if (user == null || cpu == null) {
            throw new IllegalArgumentException();
        }

        int userScore = user.checkScore(this.att1, this.att2, this.region);
        int cpuScore = cpu.checkScore(this.att1, this.att2, this.region);

        if (userScore > cpuScore) {
            return true;
        } else if (userScore < cpuScore) {
            return false;
        } else if (user.getTieBreaker() > cpu.getTieBreaker()) {
            return true;
        } else if (user.getTieBreaker() < cpu.getTieBreaker()) {
            return false;
        } else {
            return Math.random() > .5;
        }
    }

    public String getMain() {
        return mainIssue;
    }

    public String getSide1() {
        return side1;
    }

    public String getSide2() {
        return side2;
    }

    String getAttrOne() {
        String s = att1;
        return s;
    }

    String getAttrTwo() {
        String s = att2;
        return s;
    }

    public String getImageURL() {
        return "files/" + String.valueOf(year) + ".png";
    }

    @Override
    public String toString() {
        return "Election Year: " + year;
    }
}
