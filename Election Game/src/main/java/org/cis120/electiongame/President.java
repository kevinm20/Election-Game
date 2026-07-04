package org.cis120.electiongame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

/*
 * Candidate card model used throughout Campaign Clash.
 *
 * The class name remains President because the rest of the project already uses
 * President as the candidate-card type.
 */
public class President extends Card {

    private static final double EXP_WEIGHT = 0.15;
    private static final double INFL_WEIGHT = 0.35;
    private static final double POL_WEIGHT = 0.25;
    private static final double NAM_WEIGHT = 0.25;

    private final String party;
    private final String state;
    private final String region;
    private final String era;
    private final String ideology;
    private final String marquee;

    private final int experience;
    private final int influence;
    private final int policy;
    private final int nameRec;

    private final String[] tags;
    private final Set<String> normalizedTags;

    private String cardInfo = "No card info available";

    public President(
            String name, String region, int exp, int infl, int pol, int nam,
            String ideology, String marquee
    ) {
        this(name, "", "", region, "", exp, infl, pol, nam, ideology, marquee, null, null);
    }

    public President(
            String name, String region, int exp, int infl, int pol, int nam,
            String ideology, String marquee, String[] tags
    ) {
        this(
                name, "", "", region, inferEraFromTags(tags), exp, infl, pol, nam,
                ideology, marquee, tags, null
        );
    }

    public President(
            String name, String region, int exp, int infl, int pol, int nam,
            String ideology, String marquee, String[] tags, String desc
    ) {
        this(
                name, "", "", region, inferEraFromTags(tags), exp, infl, pol, nam,
                ideology, marquee, tags, desc
        );
    }

    public President(
            String name, String party, String state, String region, String era, int exp,
            int infl, int pol, int nam, String ideology, String marquee, String[] tags
    ) {
        this(name, party, state, region, era, exp, infl, pol, nam, ideology, marquee, tags, null);
    }

    public President(
            String name, String party, String state, String region, String era, int exp,
            int infl, int pol, int nam, String ideology, String marquee, String[] tags,
            String desc
    ) {
        super.setName(cleanString(name));
        this.party = cleanString(party);
        this.state = cleanString(state);
        this.region = cleanString(region);
        this.era = cleanString(era);
        this.experience = exp;
        this.influence = infl;
        this.policy = pol;
        this.nameRec = nam;
        this.ideology = cleanString(ideology);
        this.marquee = cleanString(marquee);
        this.tags = cleanTags(tags);
        this.normalizedTags = buildNormalizedTags(this.tags);

        if (desc != null && !desc.trim().isEmpty()) {
            this.cardInfo = desc;
        }
    }

    String getParty() {
        return party;
    }

    String getState() {
        return state;
    }

    String getRegion() {
        return region;
    }

    String getEra() {
        return era;
    }

    String getIdeology() {
        return ideology;
    }

    String getMarquee() {
        return marquee;
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

    String[] getTags() {
        return Arrays.copyOf(tags, tags.length);
    }

    Set<String> getTagSet() {
        return Collections.unmodifiableSet(normalizedTags);
    }

    boolean hasTag(String tag) {
        return normalizedTags.contains(normalizeTag(tag));
    }

    boolean hasAnyTag(String[] tagsToCheck) {
        if (tagsToCheck == null || tagsToCheck.length == 0) {
            return false;
        }

        for (String tag : tagsToCheck) {
            if (hasTag(tag)) {
                return true;
            }
        }
        return false;
    }

    boolean hasAnyTag(Collection<String> tagsToCheck) {
        if (tagsToCheck == null || tagsToCheck.isEmpty()) {
            return false;
        }

        for (String tag : tagsToCheck) {
            if (hasTag(tag)) {
                return true;
            }
        }
        return false;
    }

    boolean hasAllTags(String[] tagsToCheck) {
        if (tagsToCheck == null || tagsToCheck.length == 0) {
            return true;
        }

        for (String tag : tagsToCheck) {
            if (!hasTag(tag)) {
                return false;
            }
        }
        return true;
    }

    boolean hasAllTags(Collection<String> tagsToCheck) {
        if (tagsToCheck == null || tagsToCheck.isEmpty()) {
            return true;
        }

        for (String tag : tagsToCheck) {
            if (!hasTag(tag)) {
                return false;
            }
        }
        return true;
    }

    String getCardInfo() {
        return cardInfo;
    }

    double getWeightedAve() {
        return getAv();
    }

    double getAv() {
        return EXP_WEIGHT * experience
                + INFL_WEIGHT * influence
                + POL_WEIGHT * policy
                + NAM_WEIGHT * nameRec;
    }

    double getValue() {
        double av = getAv();
        double value = 1356.44 * av * av + 1934.93 * av - 2291.37;
        return Math.max(1000.0, value);
    }

    int getBestAttributes() {
        int[] attrs = { experience, influence, policy, nameRec };
        Arrays.sort(attrs);
        return attrs[2] + attrs[3];
    }

    int getAttributePair(String att1, String att2) {
        if (att1 == null || att2 == null) {
            throw new IllegalArgumentException();
        }

        return getAttributeValue(att1) + getAttributeValue(att2);
    }

    int getTieBreaker() {
        return experience + influence + policy + nameRec;
    }

    boolean isMarquee(Policy p) {
        return p != null && marquee.equals(p.getName());
    }

    int checkScore(String att1, String att2, String reg) {
        int score = getAttributePair(att1, att2);

        if (reg != null && reg.equals(region)) {
            score += 3;
        }

        return score;
    }

    int checkMatch(Policy p) {
        if (p == null) {
            return 0;
        }

        boolean marqueeMatch = marquee.equals(p.getName());
        boolean ideologyMatch = p.checkIdeology(ideology);

        if (marqueeMatch && ideologyMatch) {
            return 5;
        } else if (marqueeMatch) {
            return 4;
        } else if (ideologyMatch) {
            return 2;
        }
        return 0;
    }

    @Override
    public String getImageURL() {
        String target = super.getName()
                .replaceAll("\\s", "")
                .replace(".", "")
                .replace("'", "")
                .replace("(", "")
                .replace(")", "")
                .toLowerCase(Locale.US);
        return "files/" + target + ".PNG";
    }

    @Override
    public String toString() {
        return super.getName();
    }

    private int getAttributeValue(String attribute) {
        switch (attribute) {
            case "exp":
                return experience;
            case "infl":
                return influence;
            case "pol":
                return policy;
            case "nam":
                return nameRec;
            default:
                return 0;
        }
    }

    private static String cleanString(String value) {
        if (value == null) {
            return "";
        }
        return value.trim();
    }

    private static String[] cleanTags(String[] rawTags) {
        if (rawTags == null || rawTags.length == 0) {
            return new String[0];
        }

        ArrayList<String> cleaned = new ArrayList<String>();
        HashSet<String> seen = new HashSet<String>();

        for (String rawTag : rawTags) {
            String tag = cleanString(rawTag);
            if (tag.isEmpty()) {
                continue;
            }

            String normalized = normalizeTag(tag);
            if (!seen.contains(normalized)) {
                cleaned.add(tag);
                seen.add(normalized);
            }
        }

        return cleaned.toArray(new String[0]);
    }

    private static Set<String> buildNormalizedTags(String[] displayTags) {
        HashSet<String> result = new HashSet<String>();
        if (displayTags == null) {
            return result;
        }

        for (String tag : displayTags) {
            String normalized = normalizeTag(tag);
            if (!normalized.isEmpty()) {
                result.add(normalized);
            }
        }
        return result;
    }

    private static String normalizeTag(String tag) {
        if (tag == null) {
            return "";
        }
        return tag.trim().toLowerCase(Locale.US);
    }

    private static String inferEraFromTags(String[] tags) {
        if (tags == null) {
            return "";
        }

        for (String tag : tags) {
            String cleaned = cleanString(tag);
            if (cleaned.endsWith(" Era")) {
                return cleaned;
            }
        }
        return "";
    }
}
