package org.cis120.electiongame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

/**
 * Definition object for Campaign Mode tournaments.
 *
 * Era tournaments and tag tournaments intentionally share the same backend shape:
 * a named scope, a set of matching card tags, a 10-card entry requirement, the
 * same three-round reward structure, and the same persistent record behavior.
 */
public class CampaignTournament {

    public static final String SCOPE_ERA = "ERA";
    public static final String SCOPE_TAG = "TAG";

    public static final int REQUIRED_TOURNAMENT_CARDS = 10;
    public static final int REQUIRED_ERA_CARDS = REQUIRED_TOURNAMENT_CARDS;
    public static final int REQUIRED_TAG_CARDS = REQUIRED_TOURNAMENT_CARDS;

    public static final int ACTIVE_ERA_TOURNAMENT_COUNT = 3;
    public static final int ACTIVE_TAG_TOURNAMENT_COUNT = 3;

    public static final int ROUND_1_WIN_PC = 10000;
    public static final int ROUND_2_WIN_PC = 20000;
    public static final int ROUND_3_WIN_PC = 55000;

    public static final double SOUVENIR_MIN_RATING = 0.0;
    public static final double SOUVENIR_MAX_RATING = 3.99;

    public static final double CHAMPION_MIN_RATING = 4.0;
    public static final double CHAMPION_MAX_RATING = 10.0;

    public static final double TOURNAMENT_DUPLICATE_SALE_PERCENT = 1.0;

    public static final String ROUND_1_DIFFICULTY = "Easy";
    public static final String ROUND_2_DIFFICULTY = "Medium";
    public static final String ROUND_3_DIFFICULTY = "Hard";

    private static final String[] ERA_TAG_NAMES = {
        "Founding Era",
        "Jacksonian Era",
        "Civil War Era",
        "Reconstruction Era",
        "Progressive Era",
        "New Deal Era",
        "Cold War Era",
        "Reagan Era",
        "Modern Era",
        "Trump Era",
        "Present Era"
    };

    private final String id;
    private final String tournamentName;
    private final String scopeType;
    private final String scopeName;
    private final String primaryScopeTag;
    private final List<String> scopeTags;
    private final String description;
    private final int electionStartYear;
    private final int electionEndYear;

    public CampaignTournament(String id, String tournamentName, String eraName, String eraTag) {
        this(
                SCOPE_ERA,
                id,
                tournamentName,
                eraName,
                eraTag,
                null,
                0,
                0,
                new String[0]
        );
    }

    public CampaignTournament(
            String id, String tournamentName, String eraName,
            String eraTag, String description, String... alternateEraTags
    ) {
        this(
                SCOPE_ERA,
                id,
                tournamentName,
                eraName,
                eraTag,
                description,
                0,
                0,
                alternateEraTags
        );
    }

    public CampaignTournament(
            String id, String tournamentName, String eraName,
            String eraTag, String description, int electionStartYear, int electionEndYear,
            String... alternateEraTags
    ) {
        this(
                SCOPE_ERA,
                id,
                tournamentName,
                eraName,
                eraTag,
                description,
                electionStartYear,
                electionEndYear,
                alternateEraTags
        );
    }

    private CampaignTournament(
            String scopeType, String id, String tournamentName, String scopeName,
            String primaryScopeTag, String description, int electionStartYear,
            int electionEndYear, String... alternateScopeTags
    ) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Tournament id cannot be blank.");
        }
        if (tournamentName == null || tournamentName.trim().isEmpty()) {
            throw new IllegalArgumentException("Tournament name cannot be blank.");
        }
        if (scopeName == null || scopeName.trim().isEmpty()) {
            throw new IllegalArgumentException("Tournament scope name cannot be blank.");
        }
        if (primaryScopeTag == null || primaryScopeTag.trim().isEmpty()) {
            throw new IllegalArgumentException("Tournament scope tag cannot be blank.");
        }

        String cleanScopeType = cleanScopeType(scopeType);
        this.id = id.trim().toUpperCase();
        this.tournamentName = tournamentName.trim();
        this.scopeType = cleanScopeType;
        this.scopeName = scopeName.trim();
        this.primaryScopeTag = primaryScopeTag.trim();
        this.description = description == null ? "" : description.trim();
        this.electionStartYear = electionStartYear;
        this.electionEndYear = electionEndYear;

        ArrayList<String> tags = new ArrayList<String>();
        addUniqueTag(tags, this.primaryScopeTag);
        if (alternateScopeTags != null) {
            for (String tag : alternateScopeTags) {
                addUniqueTag(tags, tag);
            }
        }
        this.scopeTags = Collections.unmodifiableList(tags);
    }

    public static CampaignTournament buildTagTournament(String tagName) {
        String cleanTag = cleanText(tagName);
        if (cleanTag.isEmpty()) {
            throw new IllegalArgumentException("Tag tournament name cannot be blank.");
        }

        String id = "TAG_" + sanitizeIdPart(cleanTag) + "_CUP";
        String tournamentName = cleanTag + " Cup";
        String description = "Build a 10-card " + cleanTag
                + " lineup and survive a three-round campaign gauntlet.";

        return new CampaignTournament(
                SCOPE_TAG,
                id,
                tournamentName,
                cleanTag,
                cleanTag,
                description,
                0,
                0,
                new String[0]
        );
    }

    public static List<CampaignTournament> buildDefaultTagTournaments() {
        return buildDefaultTagTournaments(CardData.getPresidents("full", null, 0.0));
    }

    public static List<CampaignTournament> buildDefaultTagTournaments(List<President> candidates) {
        TreeMap<String, Integer> tagCounts = new TreeMap<String, Integer>(String.CASE_INSENSITIVE_ORDER);

        if (candidates != null) {
            for (President candidate : candidates) {
                if (candidate == null) {
                    continue;
                }

                String[] tags = candidate.getTags();
                if (tags == null) {
                    continue;
                }

                HashSet<String> countedForCandidate = new HashSet<String>();
                for (String tag : tags) {
                    String cleanTag = cleanText(tag);
                    if (cleanTag.isEmpty() || isEraTag(cleanTag)) {
                        continue;
                    }

                    String key = cleanTag.toLowerCase();
                    if (countedForCandidate.contains(key)) {
                        continue;
                    }
                    countedForCandidate.add(key);

                    Integer oldCount = tagCounts.get(cleanTag);
                    tagCounts.put(cleanTag, oldCount == null ? 1 : oldCount + 1);
                }
            }
        }

        ArrayList<CampaignTournament> tournaments = new ArrayList<CampaignTournament>();
        for (Map.Entry<String, Integer> entry : tagCounts.entrySet()) {
            if (entry.getValue() >= REQUIRED_TAG_CARDS) {
                tournaments.add(buildTagTournament(entry.getKey()));
            }
        }
        return Collections.unmodifiableList(tournaments);
    }

    public static List<CampaignTournament> buildActiveTagTournaments() {
        return buildActiveTagTournaments(ACTIVE_TAG_TOURNAMENT_COUNT);
    }

    public static List<CampaignTournament> buildActiveTagTournaments(int activeCount) {
        return pickRandomTournaments(buildDefaultTagTournaments(), activeCount);
    }

    public static List<CampaignTournament> buildActiveEraTournaments() {
        return buildActiveEraTournaments(ACTIVE_ERA_TOURNAMENT_COUNT);
    }

    public static List<CampaignTournament> buildActiveEraTournaments(int activeCount) {
        return pickRandomTournaments(buildDefaultEraTournaments(), activeCount);
    }

    public static List<CampaignTournament> pickRandomTournaments(
            List<CampaignTournament> tournaments, int activeCount
    ) {
        if (tournaments == null || tournaments.isEmpty() || activeCount <= 0) {
            return Collections.emptyList();
        }

        ArrayList<CampaignTournament> shuffled = new ArrayList<CampaignTournament>(tournaments);
        Collections.shuffle(shuffled);

        int count = Math.min(activeCount, shuffled.size());
        return Collections.unmodifiableList(new ArrayList<CampaignTournament>(shuffled.subList(0, count)));
    }

    public static boolean isEraTag(String tagName) {
        String cleanTag = cleanText(tagName);
        if (cleanTag.isEmpty()) {
            return false;
        }

        for (String eraTag : ERA_TAG_NAMES) {
            if (eraTag.equalsIgnoreCase(cleanTag)) {
                return true;
            }
        }
        return false;
    }

    public static List<String> getDefaultEraTagNames() {
        ArrayList<String> tags = new ArrayList<String>();
        for (String tag : ERA_TAG_NAMES) {
            tags.add(tag);
        }
        return Collections.unmodifiableList(tags);
    }

    private static void addUniqueTag(List<String> tags, String tag) {
        String cleanTag = cleanText(tag);
        if (cleanTag.isEmpty()) {
            return;
        }

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

    public String getScopeType() {
        return scopeType;
    }

    public boolean isEraTournament() {
        return SCOPE_ERA.equals(scopeType);
    }

    public boolean isTagTournament() {
        return SCOPE_TAG.equals(scopeType);
    }

    public String getScopeName() {
        return scopeName;
    }

    public String getPrimaryScopeTag() {
        return primaryScopeTag;
    }

    public List<String> getScopeTags() {
        return new ArrayList<String>(scopeTags);
    }

    public String getEraName() {
        return scopeName;
    }

    public String getPrimaryEraTag() {
        return primaryScopeTag;
    }

    public List<String> getEraTags() {
        return getScopeTags();
    }

    public String getDescription() {
        return description;
    }

    public int getElectionStartYear() {
        return electionStartYear;
    }

    public int getElectionEndYear() {
        return electionEndYear;
    }

    public boolean hasElectionYearRange() {
        return electionStartYear > 0 && electionEndYear >= electionStartYear;
    }

    public String getElectionYearRangeText() {
        if (!hasElectionYearRange()) {
            return "All elections";
        }
        return electionStartYear + "-" + electionEndYear;
    }

    public int getRequiredCards() {
        return REQUIRED_TOURNAMENT_CARDS;
    }

    public int getRequiredEraCards() {
        return REQUIRED_ERA_CARDS;
    }

    public int getRequiredTagCards() {
        return REQUIRED_TAG_CARDS;
    }

    public boolean matchesPresident(President president) {
        if (president == null) {
            return false;
        }
        return matchesAnyScopeTag(president.getTags());
    }

    public boolean matchesAnyScopeTag(String[] tags) {
        if (tags == null) {
            return false;
        }

        for (String tag : tags) {
            if (matchesScopeTag(tag)) {
                return true;
            }
        }
        return false;
    }

    public boolean matchesScopeTag(String tag) {
        String cleanTag = cleanText(tag);
        if (cleanTag.isEmpty()) {
            return false;
        }

        for (String scopeTag : scopeTags) {
            if (scopeTag.equalsIgnoreCase(cleanTag)) {
                return true;
            }
        }
        return false;
    }

    public boolean matchesAnyEraTag(String[] tags) {
        return matchesAnyScopeTag(tags);
    }

    public boolean matchesEraTag(String tag) {
        return matchesScopeTag(tag);
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
        return scopeName + " Souvenir Pack";
    }

    public String getChampionPackName() {
        return scopeName + " Champion Pack";
    }

    public static List<CampaignTournament> buildDefaultEraTournaments() {
        ArrayList<CampaignTournament> tournaments = new ArrayList<CampaignTournament>();

        tournaments.add(new CampaignTournament(
                "FOUNDERS_GAUNTLET",
                "Founders' Gauntlet",
                "Founding Era",
                "Founding Era",
                "Build a founding-era coalition and survive the birth of the republic.",
                1780,
                1819
        ));
        tournaments.add(new CampaignTournament(
                "OLD_HICKORY_OPEN",
                "Old Hickory Open",
                "Jacksonian Era",
                "Jacksonian Era",
                "Mass democracy, party machines, and the rough edge of Jacksonian politics.",
                1820,
                1849
        ));
        tournaments.add(new CampaignTournament(
                "HOUSE_DIVIDED_CUP",
                "House Divided Cup",
                "Civil War Era",
                "Civil War Era",
                "A brutal era tournament built around crisis, union, secession, and survival.",
                1850,
                1869
        ));
        tournaments.add(new CampaignTournament(
                "GILDED_AGE_INVITATIONAL",
                "Gilded Age Invitational",
                "Reconstruction Era",
                "Reconstruction Era",
                "Reconstruction, industrial power, patronage politics, and the Gilded Age.",
                1870,
                1899
        ));
        tournaments.add(new CampaignTournament(
                "BULL_MOOSE_CUP",
                "Bull Moose Cup",
                "Progressive Era",
                "Progressive Era",
                "Reformers, trust-busters, modern campaigns, and Progressive Era combat.",
                1900,
                1929
        ));
        tournaments.add(new CampaignTournament(
                "FIRESIDE_CLASSIC",
                "Fireside Classic",
                "New Deal Era",
                "New Deal Era",
                "Depression, war, radio politics, and the rise of the New Deal state.",
                1930,
                1949
        ));
        tournaments.add(new CampaignTournament(
                "CONTAINMENT_CLASSIC",
                "Containment Classic",
                "Cold War Era",
                "Cold War Era",
                "Cold War consensus, civil rights, television politics, Vietnam, and detente.",
                1950,
                1979
        ));
        tournaments.add(new CampaignTournament(
                "MORNING_IN_AMERICA_BOWL",
                "Morning in America Bowl",
                "Reagan Era",
                "Reagan Era",
                "Conservatism, optimism, deregulation, and late-Cold-War political combat.",
                1980,
                1999
        ));
        tournaments.add(new CampaignTournament(
                "NEW_MILLENNIUM_CUP",
                "New Millennium Cup",
                "Modern Era",
                "Modern Era",
                "Post-Cold-War politics, polarization, terror, recession, and digital campaigns.",
                2000,
                2019
        ));
        tournaments.add(new CampaignTournament(
                "TODAYS_TROPHY",
                "Today's Trophy",
                "Trump Era",
                "Trump Era",
                "Populism, disruption, media saturation, and the politics of right now.",
                2020,
                2039,
                "Present Era"
        ));

        return Collections.unmodifiableList(tournaments);
    }

    @Override
    public String toString() {
        return tournamentName + " (" + scopeName + ")";
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

    private static String cleanScopeType(String text) {
        String clean = cleanText(text).toUpperCase();
        if (SCOPE_TAG.equals(clean)) {
            return SCOPE_TAG;
        }
        return SCOPE_ERA;
    }

    private static String cleanText(String text) {
        return text == null ? "" : text.trim();
    }

    private static String sanitizeIdPart(String text) {
        String clean = cleanText(text).toUpperCase();
        StringBuilder builder = new StringBuilder();
        boolean previousUnderscore = false;

        for (int i = 0; i < clean.length(); i++) {
            char ch = clean.charAt(i);
            boolean valid = (ch >= 'A' && ch <= 'Z') || (ch >= '0' && ch <= '9');
            if (valid) {
                builder.append(ch);
                previousUnderscore = false;
            } else if (!previousUnderscore) {
                builder.append('_');
                previousUnderscore = true;
            }
        }

        String result = builder.toString();
        while (result.startsWith("_")) {
            result = result.substring(1);
        }
        while (result.endsWith("_")) {
            result = result.substring(0, result.length() - 1);
        }
        return result.isEmpty() ? "TAG" : result;
    }
}
