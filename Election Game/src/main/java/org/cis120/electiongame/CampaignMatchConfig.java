package org.cis120.electiongame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Immutable setup object for a Campaign Mode match that will be played through
 * the normal Campaign Clash match UI.
 *
 * CampaignMode / RunCampaignMode should build one of these whenever the player
 * chooses to manually play a quick match or tournament round. RunElectionGameCombined
 * can then read this config, load the exact President decks, play the match, and
 * send a CampaignMatchResult back through a CampaignMatchCallback.
 */
public class CampaignMatchConfig {

    public static final String MATCH_TYPE_QUICK_PLAY = "Quick Play";
    public static final String MATCH_TYPE_TOURNAMENT = "Tournament";

    public static final String EXECUTION_MODE_PLAYABLE = "Playable";
    public static final String EXECUTION_MODE_SIMULATED = "Simulated";

    public static final int DEFAULT_STARTING_HAND_SIZE = 5;

    private final String matchId;
    private final String matchTitle;
    private final String matchType;
    private final String executionMode;

    private final String playerName;
    private final String cpuName;
    private final String difficulty;

    private final List<President> playerPresidentDeck;
    private final List<President> cpuPresidentDeck;
    private final int startingHandSize;

    private final String tournamentId;
    private final String tournamentName;
    private final String tournamentEraName;
    private final int tournamentRound;

    private final boolean returnToCampaignModeAfterMatch;
    private final boolean resignCountsAsLoss;

    private CampaignMatchConfig(Builder builder) {
        this.matchId = cleanOrDefault(builder.matchId, buildDefaultMatchId(builder.matchType));
        this.matchTitle = cleanOrDefault(builder.matchTitle, "Campaign Match");
        this.matchType = cleanOrDefault(builder.matchType, MATCH_TYPE_QUICK_PLAY);
        this.executionMode = cleanOrDefault(builder.executionMode, EXECUTION_MODE_PLAYABLE);

        this.playerName = cleanOrDefault(builder.playerName, "Player");
        this.cpuName = cleanOrDefault(builder.cpuName, "CPU");
        this.difficulty = normalizeDifficulty(builder.difficulty);

        this.playerPresidentDeck = immutableCopy(builder.playerPresidentDeck);
        this.cpuPresidentDeck = immutableCopy(builder.cpuPresidentDeck);
        this.startingHandSize = builder.startingHandSize <= 0
                ? DEFAULT_STARTING_HAND_SIZE
                : builder.startingHandSize;

        this.tournamentId = cleanOrDefault(builder.tournamentId, "");
        this.tournamentName = cleanOrDefault(builder.tournamentName, "");
        this.tournamentEraName = cleanOrDefault(builder.tournamentEraName, "");
        this.tournamentRound = builder.tournamentRound;

        this.returnToCampaignModeAfterMatch = builder.returnToCampaignModeAfterMatch;
        this.resignCountsAsLoss = builder.resignCountsAsLoss;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static CampaignMatchConfig quickPlay(
            String playerName, String difficulty,
            List<President> playerPresidentDeck, List<President> cpuPresidentDeck
    ) {
        String cleanDifficulty = normalizeDifficulty(difficulty);
        return CampaignMatchConfig.builder()
                .matchId("QUICK_PLAY_" + cleanDifficulty.toUpperCase())
                .matchTitle(cleanDifficulty + " Campaign Match")
                .matchType(MATCH_TYPE_QUICK_PLAY)
                .executionMode(EXECUTION_MODE_PLAYABLE)
                .playerName(playerName)
                .cpuName(cleanDifficulty + " CPU")
                .difficulty(cleanDifficulty)
                .playerPresidentDeck(playerPresidentDeck)
                .cpuPresidentDeck(cpuPresidentDeck)
                .startingHandSize(DEFAULT_STARTING_HAND_SIZE)
                .returnToCampaignModeAfterMatch(true)
                .resignCountsAsLoss(true)
                .build();
    }

    public static CampaignMatchConfig tournamentRound(
            String playerName, CampaignTournament tournament, int roundNumber,
            List<President> playerPresidentDeck, List<President> cpuPresidentDeck
    ) {
        if (tournament == null) {
            throw new IllegalArgumentException("Tournament cannot be null.");
        }

        String difficulty = tournament.getRoundDifficulty(roundNumber);
        return CampaignMatchConfig.builder()
                .matchId(tournament.getId() + "_ROUND_" + roundNumber)
                .matchTitle(tournament.getTournamentName() + " - Round " + roundNumber)
                .matchType(MATCH_TYPE_TOURNAMENT)
                .executionMode(EXECUTION_MODE_PLAYABLE)
                .playerName(playerName)
                .cpuName(difficulty + " CPU")
                .difficulty(difficulty)
                .playerPresidentDeck(playerPresidentDeck)
                .cpuPresidentDeck(cpuPresidentDeck)
                .startingHandSize(DEFAULT_STARTING_HAND_SIZE)
                .tournamentId(tournament.getId())
                .tournamentName(tournament.getTournamentName())
                .tournamentEraName(tournament.getEraName())
                .tournamentRound(roundNumber)
                .returnToCampaignModeAfterMatch(true)
                .resignCountsAsLoss(true)
                .build();
    }

    public String getMatchId() {
        return matchId;
    }

    public String getMatchTitle() {
        return matchTitle;
    }

    public String getMatchType() {
        return matchType;
    }

    public String getExecutionMode() {
        return executionMode;
    }

    public String getPlayerName() {
        return playerName;
    }

    public String getCpuName() {
        return cpuName;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public List<President> getPlayerPresidentDeck() {
        return new ArrayList<President>(playerPresidentDeck);
    }

    public List<President> getCpuPresidentDeck() {
        return new ArrayList<President>(cpuPresidentDeck);
    }

    public int getStartingHandSize() {
        return startingHandSize;
    }

    public String getTournamentId() {
        return tournamentId;
    }

    public String getTournamentName() {
        return tournamentName;
    }

    public String getTournamentEraName() {
        return tournamentEraName;
    }

    public int getTournamentRound() {
        return tournamentRound;
    }

    public boolean isTournamentMatch() {
        return MATCH_TYPE_TOURNAMENT.equals(matchType);
    }

    public boolean isQuickPlayMatch() {
        return MATCH_TYPE_QUICK_PLAY.equals(matchType);
    }

    public boolean isPlayableExecutionMode() {
        return EXECUTION_MODE_PLAYABLE.equals(executionMode);
    }

    public boolean isSimulatedExecutionMode() {
        return EXECUTION_MODE_SIMULATED.equals(executionMode);
    }

    public boolean shouldReturnToCampaignModeAfterMatch() {
        return returnToCampaignModeAfterMatch;
    }

    public boolean doesResignCountAsLoss() {
        return resignCountsAsLoss;
    }

    public boolean isPlayableConfig() {
        return getValidationError().isEmpty();
    }

    public String getValidationError() {
        if (playerPresidentDeck.size() < startingHandSize) {
            return "Player deck has only " + playerPresidentDeck.size()
                    + " cards; at least " + startingHandSize + " are required.";
        }
        if (cpuPresidentDeck.size() < startingHandSize) {
            return "CPU deck has only " + cpuPresidentDeck.size()
                    + " cards; at least " + startingHandSize + " are required.";
        }
        if (isTournamentMatch() && tournamentRound < 1) {
            return "Tournament match is missing a valid round number.";
        }
        return "";
    }

    public CampaignMatchConfig withExecutionMode(String newExecutionMode) {
        return CampaignMatchConfig.builder()
                .matchId(matchId)
                .matchTitle(matchTitle)
                .matchType(matchType)
                .executionMode(newExecutionMode)
                .playerName(playerName)
                .cpuName(cpuName)
                .difficulty(difficulty)
                .playerPresidentDeck(playerPresidentDeck)
                .cpuPresidentDeck(cpuPresidentDeck)
                .startingHandSize(startingHandSize)
                .tournamentId(tournamentId)
                .tournamentName(tournamentName)
                .tournamentEraName(tournamentEraName)
                .tournamentRound(tournamentRound)
                .returnToCampaignModeAfterMatch(returnToCampaignModeAfterMatch)
                .resignCountsAsLoss(resignCountsAsLoss)
                .build();
    }

    @Override
    public String toString() {
        return matchTitle + " [" + matchType + ", " + difficulty + ", "
                + executionMode + "]";
    }

    private static String cleanOrDefault(String text, String defaultValue) {
        if (text == null || text.trim().isEmpty()) {
            return defaultValue;
        }
        return text.trim();
    }

    private static String normalizeDifficulty(String difficulty) {
        if (difficulty == null) {
            return "Medium";
        }

        String trimmed = difficulty.trim();
        if (trimmed.equalsIgnoreCase("Easy")) {
            return "Easy";
        } else if (trimmed.equalsIgnoreCase("Hard")) {
            return "Hard";
        } else if (trimmed.equalsIgnoreCase("Impossible")) {
            return "Impossible";
        }
        return "Medium";
    }

    private static String buildDefaultMatchId(String matchType) {
        String cleanType = cleanOrDefault(matchType, MATCH_TYPE_QUICK_PLAY)
                .toUpperCase()
                .replace(' ', '_');
        return cleanType + "_MATCH";
    }

    private static List<President> immutableCopy(List<President> presidents) {
        ArrayList<President> copy = new ArrayList<President>();
        if (presidents != null) {
            for (President president : presidents) {
                if (president != null) {
                    copy.add(president);
                }
            }
        }
        return Collections.unmodifiableList(copy);
    }

    public static class Builder {
        private String matchId;
        private String matchTitle;
        private String matchType = MATCH_TYPE_QUICK_PLAY;
        private String executionMode = EXECUTION_MODE_PLAYABLE;

        private String playerName;
        private String cpuName;
        private String difficulty = "Medium";

        private List<President> playerPresidentDeck = new ArrayList<President>();
        private List<President> cpuPresidentDeck = new ArrayList<President>();
        private int startingHandSize = DEFAULT_STARTING_HAND_SIZE;

        private String tournamentId;
        private String tournamentName;
        private String tournamentEraName;
        private int tournamentRound;

        private boolean returnToCampaignModeAfterMatch = true;
        private boolean resignCountsAsLoss = true;

        public Builder matchId(String matchId) {
            this.matchId = matchId;
            return this;
        }

        public Builder matchTitle(String matchTitle) {
            this.matchTitle = matchTitle;
            return this;
        }

        public Builder matchType(String matchType) {
            this.matchType = matchType;
            return this;
        }

        public Builder executionMode(String executionMode) {
            this.executionMode = executionMode;
            return this;
        }

        public Builder playerName(String playerName) {
            this.playerName = playerName;
            return this;
        }

        public Builder cpuName(String cpuName) {
            this.cpuName = cpuName;
            return this;
        }

        public Builder difficulty(String difficulty) {
            this.difficulty = difficulty;
            return this;
        }

        public Builder playerPresidentDeck(List<President> playerPresidentDeck) {
            this.playerPresidentDeck = playerPresidentDeck;
            return this;
        }

        public Builder cpuPresidentDeck(List<President> cpuPresidentDeck) {
            this.cpuPresidentDeck = cpuPresidentDeck;
            return this;
        }

        public Builder startingHandSize(int startingHandSize) {
            this.startingHandSize = startingHandSize;
            return this;
        }

        public Builder tournamentId(String tournamentId) {
            this.tournamentId = tournamentId;
            return this;
        }

        public Builder tournamentName(String tournamentName) {
            this.tournamentName = tournamentName;
            return this;
        }

        public Builder tournamentEraName(String tournamentEraName) {
            this.tournamentEraName = tournamentEraName;
            return this;
        }

        public Builder tournamentRound(int tournamentRound) {
            this.tournamentRound = tournamentRound;
            return this;
        }

        public Builder returnToCampaignModeAfterMatch(boolean returnToCampaignModeAfterMatch) {
            this.returnToCampaignModeAfterMatch = returnToCampaignModeAfterMatch;
            return this;
        }

        public Builder resignCountsAsLoss(boolean resignCountsAsLoss) {
            this.resignCountsAsLoss = resignCountsAsLoss;
            return this;
        }

        public CampaignMatchConfig build() {
            return new CampaignMatchConfig(this);
        }
    }
}
