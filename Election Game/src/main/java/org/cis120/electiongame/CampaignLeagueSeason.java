package org.cis120.electiongame;

/**
 * Mutable state for the player's current League Mode season.
 *
 * A season locks its division at start. If the player opens packs mid-season
 * and grows into a higher division, the current season stays in the old division
 * and the next season will place them in the new one.
 */
public class CampaignLeagueSeason {

    private final String divisionId;
    private final int seasonLength;
    private int gamesPlayed;
    private int wins;
    private int losses;
    private boolean rewardClaimed;
    private boolean historyRecorded;
    private String lastResult;

    public CampaignLeagueSeason(String divisionId, int seasonLength) {
        this(divisionId, seasonLength, 0, 0, 0, false, false, "Not Started");
    }

    public CampaignLeagueSeason(
            String divisionId, int seasonLength, int gamesPlayed, int wins, int losses,
            boolean rewardClaimed, boolean historyRecorded, String lastResult
    ) {
        this.divisionId = CampaignLeagueDivision.cleanId(divisionId);
        this.seasonLength = Math.max(1, seasonLength);
        this.gamesPlayed = clamp(gamesPlayed, 0, this.seasonLength);
        this.wins = Math.max(0, wins);
        this.losses = Math.max(0, losses);
        sanitizeRecordCounts();
        this.rewardClaimed = rewardClaimed;
        this.historyRecorded = historyRecorded;
        this.lastResult = cleanLastResult(lastResult);
    }

    public String getDivisionId() {
        return divisionId;
    }

    public int getSeasonLength() {
        return seasonLength;
    }

    public int getGamesPlayed() {
        return gamesPlayed;
    }

    public int getWins() {
        return wins;
    }

    public int getLosses() {
        return losses;
    }

    public int getGamesRemaining() {
        return Math.max(0, seasonLength - gamesPlayed);
    }

    public boolean isCompleted() {
        return gamesPlayed >= seasonLength;
    }

    public boolean canPlayNextGame() {
        return !isCompleted();
    }

    public boolean isRewardClaimed() {
        return rewardClaimed;
    }

    public void setRewardClaimed(boolean rewardClaimed) {
        this.rewardClaimed = rewardClaimed;
    }

    public boolean isHistoryRecorded() {
        return historyRecorded;
    }

    public void setHistoryRecorded(boolean historyRecorded) {
        this.historyRecorded = historyRecorded;
    }

    public String getLastResult() {
        return lastResult;
    }

    public int getNextGameNumber() {
        return isCompleted() ? seasonLength : gamesPlayed + 1;
    }

    public void recordGame(boolean won, boolean forfeited) {
        if (isCompleted()) {
            return;
        }

        gamesPlayed++;
        if (won) {
            wins++;
            lastResult = "Won Game " + gamesPlayed;
        } else {
            losses++;
            lastResult = (forfeited ? "Forfeited" : "Lost") + " Game " + gamesPlayed;
        }
    }

    public String getRecordText() {
        return wins + "-" + losses;
    }

    public String getProgressText() {
        return gamesPlayed + "/" + seasonLength;
    }

    public String toSaveString() {
        return divisionId + "|" + seasonLength + "|" + gamesPlayed + "|" + wins + "|" + losses
                + "|" + rewardClaimed + "|" + historyRecorded + "|" + sanitizeSaveText(lastResult);
    }

    public static CampaignLeagueSeason fromSaveString(String saveString) {
        if (saveString == null || saveString.trim().isEmpty()) {
            return null;
        }
        String[] parts = saveString.split("\\|", -1);
        if (parts.length < 1) {
            return null;
        }

        String divisionId = CampaignLeagueDivision.cleanId(parts[0]);
        if (divisionId.isEmpty()) {
            return null;
        }

        int seasonLength = parts.length > 1 ? parseNonNegativeInt(parts[1], 10) : 10;
        int gamesPlayed = parts.length > 2 ? parseNonNegativeInt(parts[2], 0) : 0;
        int wins = parts.length > 3 ? parseNonNegativeInt(parts[3], 0) : 0;
        int losses = parts.length > 4 ? parseNonNegativeInt(parts[4], 0) : 0;
        boolean rewardClaimed = parts.length > 5 && parseBoolean(parts[5]);
        boolean historyRecorded = parts.length > 6 && parseBoolean(parts[6]);
        String lastResult = parts.length > 7 ? parts[7] : "Not Started";

        return new CampaignLeagueSeason(
                divisionId, seasonLength, gamesPlayed, wins, losses,
                rewardClaimed, historyRecorded, lastResult
        );
    }

    private void sanitizeRecordCounts() {
        int total = wins + losses;
        if (total <= gamesPlayed) {
            return;
        }
        losses = Math.max(0, gamesPlayed - wins);
        if (wins > gamesPlayed) {
            wins = gamesPlayed;
            losses = 0;
        }
    }

    private static int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }

    private static String cleanLastResult(String text) {
        if (text == null || text.trim().isEmpty()) {
            return "Not Started";
        }
        return text.trim();
    }

    private static int parseNonNegativeInt(String text, int defaultValue) {
        if (text == null || text.trim().isEmpty()) {
            return defaultValue;
        }
        try {
            int value = Integer.parseInt(text.trim());
            return value < 0 ? defaultValue : value;
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    private static boolean parseBoolean(String text) {
        return text != null && text.trim().equalsIgnoreCase("true");
    }

    private static String sanitizeSaveText(String text) {
        if (text == null) {
            return "";
        }
        return text.replace("|", " ").replace(";", " ").replace("\n", " ").replace("\r", " ").trim();
    }
}
