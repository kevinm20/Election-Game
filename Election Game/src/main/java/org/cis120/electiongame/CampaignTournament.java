package org.cis120.electiongame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Definition object for Campaign Mode era tournaments.
 *
 * This class intentionally stores tournament metadata and helper rules only.
 * CampaignMode should own the actual tournament simulation/reward flow, while
 * RunCampaignMode should own the Swing display.
 */
public class CampaignTournament {

    public static final int REQUIRED_ERA_CARDS = 10;

    public static final int ROUND_1_WIN_PC = 40000;
    public static final int ROUND_2_WIN_PC = 75000;
    public static final int ROUND_3_WIN_PC = 200000;

    public static final double SOUVENIR_MIN_RATING = 0.0;
    public static final double SOUVENIR_MAX_RATING = 3.99;

    public static final double CHAMPION_MIN_RATING = 4.0;
    public static final double CHAMPION_MAX_RATING = 10.0;

    // Tournament reward packs use full-value duplicate refunds in v5.
    public static final double TOURNAMENT_DUPLICATE_SALE_PERCENT = 1.0;

    public static final String ROUND_1_DIFFICULTY = "Easy";
    public static final String ROUND_2_DIFFICULTY = "Medium";
    public static final String ROUND_3_DIFFICULTY = "Hard";

    private final String id;
    private final String tournamentName;
    private final String eraName;
    private final String primaryEraTag;
    private final List<String> eraTags;
    private final String description;

    public CampaignTournament(String id, String tournamentName, String eraName, String eraTag) {
        this(id, tournamentName, eraName, eraTag, null, new String[0]);
    }

    public CampaignTournament(
            String id, String tournamentName, String eraName,
            String eraTag, String description, String... alternateEraTags
    ) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Tournament id cannot be blank.");
        }
        if (tournamentName == null || tournamentName.trim().isEmpty()) {
            throw new IllegalArgumentException("Tournament name cannot be blank.");
        }
        if (eraName == null || eraName.trim().isEmpty()) {
            throw new IllegalArgumentException("Era name cannot be blank.");
        }
        if (eraTag == null || eraTag.trim().isEmpty()) {
            throw new IllegalArgumentException("Era tag cannot be blank.");
        }

        this.id = id.trim();
        this.tournamentName = tournamentName.trim();
        this.eraName = eraName.trim();
        this.primaryEraTag = eraTag.trim();
        this.description = description == null ? "" : description.trim();

        ArrayList<String> tags = new ArrayList<String>();
        addUniqueTag(tags, this.primaryEraTag);
        if (alternateEraTags != null) {
            for (String tag : alternateEraTags) {
                addUniqueTag(tags, tag);
            }
        }
        this.eraTags = Collections.unmodifiableList(tags);
    }

    private static void addUniqueTag(List<String> tags, String tag) {
        if (tag == null || tag.trim().isEmpty()) {
            return;
        }

        String cleanTag = tag.trim();
        for (String existingTag : tags) {
            if (existingTag.equalsIgnoreCase(cleanTag)) {
                return;
            }
        }
        tags.add(cleanTag);
    }

    public String getId() {
        return id;
    }

    public String getTournamentName() {
        return tournamentName;
    }

    public String getEraName() {
        return eraName;
    }

    public String getPrimaryEraTag() {
        return primaryEraTag;
    }

    public List<String> getEraTags() {
        return new ArrayList<String>(eraTags);
    }

    public String getDescription() {
        return description;
    }

    public int getRequiredEraCards() {
        return REQUIRED_ERA_CARDS;
    }

    public boolean matchesPresident(President president) {
        if (president == null) {
            return false;
        }
        return matchesAnyEraTag(president.getTags());
    }

    public boolean matchesAnyEraTag(String[] tags) {
        if (tags == null) {
            return false;
        }

        for (String tag : tags) {
            if (matchesEraTag(tag)) {
                return true;
            }
        }
        return false;
    }

    public boolean matchesEraTag(String tag) {
        if (tag == null) {
            return false;
        }

        String cleanTag = tag.trim();
        for (String eraTag : eraTags) {
            if (eraTag.equalsIgnoreCase(cleanTag)) {
                return true;
            }
        }
        return false;
    }

    public String getRoundDifficulty(int roundNumber) {
        if (roundNumber == 1) {
            return ROUND_1_DIFFICULTY;
        } else if (roundNumber == 2) {
            return ROUND_2_DIFFICULTY;
        } else if (roundNumber == 3) {
            return ROUND_3_DIFFICULTY;
        }
        throw new IllegalArgumentException("Tournament round must be 1, 2, or 3.");
    }

    public int getRoundWinPC(int roundNumber) {
        if (roundNumber == 1) {
            return ROUND_1_WIN_PC;
        } else if (roundNumber == 2) {
            return ROUND_2_WIN_PC;
        } else if (roundNumber == 3) {
            return ROUND_3_WIN_PC;
        }
        throw new IllegalArgumentException("Tournament round must be 1, 2, or 3.");
    }

    public String getSouvenirPackName() {
        return eraName + " Souvenir Pack";
    }

    public String getChampionPackName() {
        return eraName + " Champion Pack";
    }

    /**
     * Default v5 era tournament slate.
     *
     * Era matching is based on President tags, not year ranges. Most era names
     * are identical to their data tags. Today's Trophy uses "Trump Era" as its
     * primary data tag while also accepting "Present Era" as a future alias.
     */
    public static List<CampaignTournament> buildDefaultEraTournaments() {
        ArrayList<CampaignTournament> tournaments = new ArrayList<CampaignTournament>();

        tournaments.add(new CampaignTournament(
                "FOUNDERS_GAUNTLET",
                "Founders' Gauntlet",
                "Founding Era",
                "Founding Era",
                "Build a founding-era coalition and survive the birth of the republic."
        ));
        tournaments.add(new CampaignTournament(
                "OLD_HICKORY_OPEN",
                "Old Hickory Open",
                "Jacksonian Era",
                "Jacksonian Era",
                "Mass democracy, party machines, and the rough edge of Jacksonian politics."
        ));
        tournaments.add(new CampaignTournament(
                "HOUSE_DIVIDED_CUP",
                "House Divided Cup",
                "Civil War Era",
                "Civil War Era",
                "A brutal era tournament built around crisis, union, secession, and survival."
        ));
        tournaments.add(new CampaignTournament(
                "GILDED_AGE_INVITATIONAL",
                "Gilded Age Invitational",
                "Reconstruction Era",
                "Reconstruction Era",
                "Reconstruction, industrial power, patronage politics, and the Gilded Age."
        ));
        tournaments.add(new CampaignTournament(
                "BULL_MOOSE_CUP",
                "Bull Moose Cup",
                "Progressive Era",
                "Progressive Era",
                "Reformers, trust-busters, modern campaigns, and Progressive Era combat."
        ));
        tournaments.add(new CampaignTournament(
                "FIRESIDE_CLASSIC",
                "Fireside Classic",
                "New Deal Era",
                "New Deal Era",
                "Depression, war, radio politics, and the rise of the New Deal state."
        ));
        tournaments.add(new CampaignTournament(
                "CONTAINMENT_CLASSIC",
                "Containment Classic",
                "Cold War Era",
                "Cold War Era",
                "Cold War consensus, civil rights, television politics, Vietnam, and detente."
        ));
        tournaments.add(new CampaignTournament(
                "MORNING_IN_AMERICA_BOWL",
                "Morning in America Bowl",
                "Reagan Era",
                "Reagan Era",
                "Conservatism, optimism, deregulation, and late-Cold-War political combat."
        ));
        tournaments.add(new CampaignTournament(
                "NEW_MILLENNIUM_CUP",
                "New Millennium Cup",
                "Modern Era",
                "Modern Era",
                "Post-Cold-War politics, polarization, terror, recession, and digital campaigns."
        ));
        tournaments.add(new CampaignTournament(
                "TODAYS_TROPHY",
                "Today's Trophy",
                "Trump Era",
                "Trump Era",
                "Populism, disruption, media saturation, and the politics of right now.",
                "Present Era"
        ));

        return Collections.unmodifiableList(tournaments);
    }

    @Override
    public String toString() {
        return tournamentName + " (" + eraName + ")";
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof CampaignTournament)) {
            return false;
        }
        CampaignTournament that = (CampaignTournament) other;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
