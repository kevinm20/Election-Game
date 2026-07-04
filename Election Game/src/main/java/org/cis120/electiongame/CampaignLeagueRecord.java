package org.cis120.electiongame;

/**
 * Permanent history for one League Mode division.
 *
 * CampaignLeagueSeason is the current run. CampaignLeagueRecord is the historical
 * summary shown later in League Records / Trophy-style screens.
 */
public class CampaignLeagueRecord {

    private final String divisionId;
    private int seasonsPlayed;
    private int totalWins;
    private int totalLosses;
    private int bestWins;
    private int perfectSeasons;
    private String lastResult;

    public CampaignLeagueRecord(String divisionId) {
        this(divisionId, 0, 0, 0, 0, 0, "No seasons played");
    }

    public CampaignLeagueRecord(
            String divisionId, int seasonsPlayed, int totalWins, int totalLosses,
            int bestWins, int perfectSeasons, String lastResult
    ) {
        this.divisionId = CampaignLeagueDivision.cleanId(divisionId);
        this.seasonsPlayed = Math.max(0, seasonsPlayed);
        this.totalWins = Math.max(0, totalWins);
        this.totalLosses = Math.max(0, totalLosses);
        this.bestWins = Math.max(0, bestWins);
        this.perfectSeasons = Math.max(0, perfectSeasons);
        this.lastResult = cleanLastResult(lastResult);
    }

    public String getDivisionId() {
        return divisionId;
    }

    public int getSeasonsPlayed() {
        return seasonsPlayed;
    }

    public int getTotalWins() {
        return totalWins;
    }

    public int getTotalLosses() {
        return totalLosses;
    }

    public int getBestWins() {
        return bestWins;
    }

    public int getPerfectSeasons() {
        return perfectSeasons;
    }

    public String getLastResult() {
        return lastResult;
    }

    public String getTotalRecordText() {
        return totalWins + "-" + totalLosses;
    }

    public double getWinPercentage() {
        int totalGames = totalWins + totalLosses;
        if (totalGames <= 0) {
            return 0.0;
        }
        return totalWins * 100.0 / totalGames;
    }

    public void recordCompletedSeason(CampaignLeagueSeason season) {
        if (season == null || !divisionId.equalsIgnoreCase(season.getDivisionId())) {
            return;
        }

        seasonsPlayed++;
        totalWins += season.getWins();
        totalLosses += season.getLosses();
        bestWins = Math.max(bestWins, season.getWins());
        if (season.getWins() >= season.getSeasonLength()) {
            perfectSeasons++;
        }
        lastResult = season.getWins() + "-" + season.getLosses();
    }

    public String toSaveString() {
        return divisionId + "|" + seasonsPlayed + "|" + totalWins + "|" + totalLosses
                + "|" + bestWins + "|" + perfectSeasons + "|" + sanitizeSaveText(lastResult);
    }

    public static CampaignLeagueRecord fromSaveString(String saveString) {
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

        int seasonsPlayed = parts.length > 1 ? parseNonNegativeInt(parts[1], 0) : 0;
        int totalWins = parts.length > 2 ? parseNonNegativeInt(parts[2], 0) : 0;
        int totalLosses = parts.length > 3 ? parseNonNegativeInt(parts[3], 0) : 0;
        int bestWins = parts.length > 4 ? parseNonNegativeInt(parts[4], 0) : 0;
        int perfectSeasons = parts.length > 5 ? parseNonNegativeInt(parts[5], 0) : 0;
        String lastResult = parts.length > 6 ? parts[6] : "No seasons played";

        return new CampaignLeagueRecord(
                divisionId, seasonsPlayed, totalWins, totalLosses,
                bestWins, perfectSeasons, lastResult
        );
    }

    private static String cleanLastResult(String text) {
        if (text == null || text.trim().isEmpty()) {
            return "No seasons played";
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

    private static String sanitizeSaveText(String text) {
        if (text == null) {
            return "";
        }
        return text.replace("|", " ").replace(";", " ").replace("\n", " ").replace("\r", " ").trim();
    }
}
