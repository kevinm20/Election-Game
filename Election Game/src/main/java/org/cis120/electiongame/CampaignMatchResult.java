package org.cis120.electiongame;

/**
 * Outcome object for a playable Campaign Mode match.
 *
 * RunElectionGameCombined should create one of these when a campaign-launched
 * match ends. CampaignMode / RunCampaignMode can then use it to award PC,
 * packs, tournament advancement, or a loss.
 */
public class CampaignMatchResult {

    public static final String RESULT_COMPLETED = "Completed";
    public static final String RESULT_RESIGNED = "Resigned";
    public static final String RESULT_CANCELLED = "Cancelled";
    public static final String RESULT_ERROR = "Error";

    private final CampaignMatchConfig config;
    private final String resultType;
    private final boolean playerWon;
    private final boolean resigned;
    private final boolean cancelled;

    private final int playerRoundsWon;
    private final int cpuRoundsWon;

    private final String finalScoreText;
    private final String matchLog;
    private final String message;
    private final long completedAtMillis;

    private CampaignMatchResult(Builder builder) {
        this.config = builder.config;
        this.resultType = cleanOrDefault(builder.resultType, RESULT_COMPLETED);
        this.playerWon = builder.playerWon;
        this.resigned = builder.resigned;
        this.cancelled = builder.cancelled;
        this.playerRoundsWon = Math.max(0, builder.playerRoundsWon);
        this.cpuRoundsWon = Math.max(0, builder.cpuRoundsWon);
        this.finalScoreText = cleanOrDefault(builder.finalScoreText, "");
        this.matchLog = cleanOrDefault(builder.matchLog, "");
        this.message = cleanOrDefault(builder.message, "");
        this.completedAtMillis = builder.completedAtMillis <= 0
                ? System.currentTimeMillis()
                : builder.completedAtMillis;
    }

    public static Builder builder(CampaignMatchConfig config) {
        return new Builder(config);
    }

    public static CampaignMatchResult completed(
            CampaignMatchConfig config, boolean playerWon,
            int playerRoundsWon, int cpuRoundsWon, String finalScoreText
    ) {
        return CampaignMatchResult.builder(config)
                .resultType(RESULT_COMPLETED)
                .playerWon(playerWon)
                .playerRoundsWon(playerRoundsWon)
                .cpuRoundsWon(cpuRoundsWon)
                .finalScoreText(finalScoreText)
                .message(playerWon ? "Campaign match won." : "Campaign match lost.")
                .build();
    }

    public static CampaignMatchResult resigned(
            CampaignMatchConfig config, int playerRoundsWon,
            int cpuRoundsWon, String finalScoreText
    ) {
        return CampaignMatchResult.builder(config)
                .resultType(RESULT_RESIGNED)
                .playerWon(false)
                .resigned(true)
                .playerRoundsWon(playerRoundsWon)
                .cpuRoundsWon(cpuRoundsWon)
                .finalScoreText(finalScoreText)
                .message("Campaign match resigned.")
                .build();
    }

    public static CampaignMatchResult cancelled(CampaignMatchConfig config, String message) {
        return CampaignMatchResult.builder(config)
                .resultType(RESULT_CANCELLED)
                .cancelled(true)
                .message(message)
                .build();
    }

    public static CampaignMatchResult error(CampaignMatchConfig config, String message) {
        return CampaignMatchResult.builder(config)
                .resultType(RESULT_ERROR)
                .cancelled(true)
                .message(message)
                .build();
    }

    public CampaignMatchConfig getConfig() {
        return config;
    }

    public String getResultType() {
        return resultType;
    }

    public boolean didPlayerWin() {
        return playerWon;
    }

    public boolean didPlayerLose() {
        return shouldApplyCampaignResult() && !playerWon;
    }

    public boolean wasResigned() {
        return resigned;
    }

    public boolean wasCancelled() {
        return cancelled;
    }

    public boolean wasError() {
        return RESULT_ERROR.equals(resultType);
    }

    public int getPlayerRoundsWon() {
        return playerRoundsWon;
    }

    public int getCpuRoundsWon() {
        return cpuRoundsWon;
    }

    public String getFinalScoreText() {
        return finalScoreText;
    }

    public String getMatchLog() {
        return matchLog;
    }

    public String getMessage() {
        return message;
    }

    public long getCompletedAtMillis() {
        return completedAtMillis;
    }

    /**
     * Campaign rewards should apply for completed matches and resignations.
     * They should not apply if a bridge fails or the launch is cancelled.
     */
    public boolean shouldApplyCampaignResult() {
        return RESULT_COMPLETED.equals(resultType) || RESULT_RESIGNED.equals(resultType);
    }

    public String getSummaryLine() {
        String title = config == null ? "Campaign Match" : config.getMatchTitle();
        String outcome;
        if (wasCancelled()) {
            outcome = resultType;
        } else if (playerWon) {
            outcome = "Win";
        } else {
            outcome = resigned ? "Loss by resignation" : "Loss";
        }
        return title + " - " + outcome + " (" + playerRoundsWon + "-" + cpuRoundsWon + ")";
    }

    @Override
    public String toString() {
        return getSummaryLine();
    }

    private static String cleanOrDefault(String text, String defaultValue) {
        if (text == null || text.trim().isEmpty()) {
            return defaultValue;
        }
        return text.trim();
    }

    public static class Builder {
        private final CampaignMatchConfig config;
        private String resultType = RESULT_COMPLETED;
        private boolean playerWon;
        private boolean resigned;
        private boolean cancelled;
        private int playerRoundsWon;
        private int cpuRoundsWon;
        private String finalScoreText;
        private String matchLog;
        private String message;
        private long completedAtMillis;

        private Builder(CampaignMatchConfig config) {
            this.config = config;
        }

        public Builder resultType(String resultType) {
            this.resultType = resultType;
            return this;
        }

        public Builder playerWon(boolean playerWon) {
            this.playerWon = playerWon;
            return this;
        }

        public Builder resigned(boolean resigned) {
            this.resigned = resigned;
            return this;
        }

        public Builder cancelled(boolean cancelled) {
            this.cancelled = cancelled;
            return this;
        }

        public Builder playerRoundsWon(int playerRoundsWon) {
            this.playerRoundsWon = playerRoundsWon;
            return this;
        }

        public Builder cpuRoundsWon(int cpuRoundsWon) {
            this.cpuRoundsWon = cpuRoundsWon;
            return this;
        }

        public Builder finalScoreText(String finalScoreText) {
            this.finalScoreText = finalScoreText;
            return this;
        }

        public Builder matchLog(String matchLog) {
            this.matchLog = matchLog;
            return this;
        }

        public Builder message(String message) {
            this.message = message;
            return this;
        }

        public Builder completedAtMillis(long completedAtMillis) {
            this.completedAtMillis = completedAtMillis;
            return this;
        }

        public CampaignMatchResult build() {
            return new CampaignMatchResult(this);
        }
    }
}
