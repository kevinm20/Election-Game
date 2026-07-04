package org.cis120.electiongame;

/**
 * Persistent record for one Campaign Mode tournament.
 *
 * Records are keyed by tournament id, so era tournaments and tag tournaments can
 * share the same save format without requiring a migration.
 */
public class CampaignTournamentRecord {

    public static final int FINISH_NOT_ENTERED = 0;
    public static final int FINISH_ROUND_1 = 1;
    public static final int FINISH_ROUND_2 = 2;
    public static final int FINISH_FINALIST = 3;
    public static final int FINISH_CHAMPION = 4;

    private final String tournamentId;
    private int attempts;
    private int titles;
    private int bestFinish;
    private String lastResult;

    public CampaignTournamentRecord(String tournamentId) {
        this(tournamentId, 0, 0, FINISH_NOT_ENTERED, "Not Entered");
    }

    public CampaignTournamentRecord(
            String tournamentId, int attempts, int titles, int bestFinish, String lastResult
    ) {
        this.tournamentId = cleanTournamentId(tournamentId);
        this.attempts = Math.max(0, attempts);
        this.titles = Math.max(0, titles);
        this.bestFinish = sanitizeFinish(bestFinish);
        this.lastResult = cleanLastResult(lastResult);
    }

    public String getTournamentId() {
        return tournamentId;
    }

    public int getAttempts() {
        return attempts;
    }

    public int getTitles() {
        return titles;
    }

    public int getBestFinish() {
        return bestFinish;
    }

    public String getLastResult() {
        return lastResult;
    }

    public boolean hasWonTitle() {
        return titles > 0 || bestFinish >= FINISH_CHAMPION;
    }

    public void recordAttemptStarted() {
        attempts++;
        updateBestFinish(FINISH_ROUND_1);
        lastResult = "In Progress";
    }

    public void recordRoundWin(int roundNumber) {
        if (roundNumber <= 1) {
            updateBestFinish(FINISH_ROUND_2);
            lastResult = "Won Round 1";
        } else if (roundNumber == 2) {
            updateBestFinish(FINISH_FINALIST);
            lastResult = "Reached Final";
        } else {
            titles++;
            updateBestFinish(FINISH_CHAMPION);
            lastResult = "Champion";
        }
    }

    public void recordRoundLoss(int roundNumber, boolean forfeited) {
        if (roundNumber <= 1) {
            updateBestFinish(FINISH_ROUND_1);
        } else if (roundNumber == 2) {
            updateBestFinish(FINISH_ROUND_2);
        } else {
            updateBestFinish(FINISH_FINALIST);
        }

        String prefix = forfeited ? "Forfeited" : "Lost";
        if (roundNumber <= 1) {
            lastResult = prefix + " in Round 1";
        } else if (roundNumber == 2) {
            lastResult = prefix + " in Round 2";
        } else {
            lastResult = prefix + " in Final";
        }
    }

    public String getBestFinishLabel() {
        return getFinishLabel(bestFinish);
    }

    public static String getFinishLabel(int finish) {
        if (finish >= FINISH_CHAMPION) {
            return "Champion";
        } else if (finish == FINISH_FINALIST) {
            return "Finalist";
        } else if (finish == FINISH_ROUND_2) {
            return "Round 2";
        } else if (finish == FINISH_ROUND_1) {
            return "Round 1";
        }
        return "Not Entered";
    }

    public String toSaveString() {
        return tournamentId + "|" + attempts + "|" + titles + "|" + bestFinish + "|"
                + sanitizeSaveText(lastResult);
    }

    public static CampaignTournamentRecord fromSaveString(String saveString) {
        if (saveString == null || saveString.trim().isEmpty()) {
            return null;
        }

        String[] parts = saveString.split("\\|", -1);
        if (parts.length < 1) {
            return null;
        }

        String id = cleanTournamentId(parts[0]);
        if (id.isEmpty()) {
            return null;
        }

        int attempts = parts.length > 1 ? parseNonNegativeInt(parts[1], 0) : 0;
        int titles = parts.length > 2 ? parseNonNegativeInt(parts[2], 0) : 0;
        int bestFinish = parts.length > 3 ? sanitizeFinish(parseNonNegativeInt(parts[3], 0)) : 0;
        String lastResult = parts.length > 4 ? parts[4] : "Not Entered";
        return new CampaignTournamentRecord(id, attempts, titles, bestFinish, lastResult);
    }

    private void updateBestFinish(int finish) {
        int cleanFinish = sanitizeFinish(finish);
        if (cleanFinish > bestFinish) {
            bestFinish = cleanFinish;
        }
    }

    private static String cleanTournamentId(String text) {
        return text == null ? "" : text.trim().toUpperCase();
    }

    private static String cleanLastResult(String text) {
        if (text == null || text.trim().isEmpty()) {
            return "Not Entered";
        }
        return text.trim();
    }

    private static int sanitizeFinish(int finish) {
        if (finish < FINISH_NOT_ENTERED) {
            return FINISH_NOT_ENTERED;
        }
        if (finish > FINISH_CHAMPION) {
            return FINISH_CHAMPION;
        }
        return finish;
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
