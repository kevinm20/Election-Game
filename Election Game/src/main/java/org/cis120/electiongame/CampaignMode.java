package org.cis120.electiongame;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Campaign Mode engine / console testbed.
 *
 * This file is intentionally UI-free for now. The goal is to make the campaign
 * loop work in the console first, then eventually build a RunCampaignMode Swing
 * interface on top of the same logic.
 */
public class CampaignMode implements Runnable {

    private static final int STARTING_POLITICAL_CAPITAL = 100000;

    private static final int ERA_PROSPECT_PACK_CARD_COUNT = 5;
    private static final int ERA_PROSPECT_PACK_COST = 225000;
    private static final double ERA_PROSPECT_PACK_MIN_RATING = 0.0;
    private static final double ERA_PROSPECT_PACK_MAX_RATING = 3.99;

    private static final int EASY_WIN_REWARD_PC = 65000;
    private static final int EASY_LOSS_REWARD_PC = 25000;

    private static final int MEDIUM_WIN_REWARD_PC = 95000;
    private static final int MEDIUM_LOSS_REWARD_PC = 30000;

    private static final int HARD_WIN_REWARD_PC = 185000;
    private static final int HARD_LOSS_REWARD_PC = 35000;

    // The simulated human/player side should play competently but not perfectly.
    private static final double PLAYER_SIM_AI_DIFFICULTY = 0.65;

    private static final double EASY_CPU_AI_DIFFICULTY = 0.50;
    private static final double MEDIUM_CPU_AI_DIFFICULTY = 0.65;
    private static final double HARD_CPU_AI_DIFFICULTY = 0.80;

    private static final double EASY_CPU_MIN_RATING = 0.0;
    private static final double MEDIUM_CPU_MIN_RATING = 3.0;
    private static final double HARD_CPU_MIN_RATING = 5.0;

    private static final double DUPLICATE_SALE_PERCENT = 0.25;

    private static final String SAVE_VERSION = "CAMPAIGN_CLASH_SAVE_V3";
    private static final String SAVE_VERSION_V2 = "CAMPAIGN_CLASH_SAVE_V2";
    private static final String SAVE_VERSION_V1 = "CAMPAIGN_CLASH_SAVE_V1";
    private static final String SAVE_FILE_PATH = "files/campaignsave.TXT";

    private CampaignPlayer player;
    private List<President> allPresidents;
    private List<CampaignTournament> eraTournaments;

    private CampaignMatchConfig pendingPlayableMatchConfig;
    private long pendingPlayableMatchStartedAtMillis;

    private CampaignPack starterElitePack;
    private CampaignPack starterMidPack;
    private CampaignPack starterPack;

    private CampaignPack campaignTrailPack;
    private CampaignPack fillerPack;
    private CampaignPack starPack;
    private CampaignPack standardPack;
    private CampaignPack elitePack;

    /**
     * Creates a campaign with a default player name.
     */
    public CampaignMode() {
        this("Player");
    }

    /**
     * Creates a campaign for the given player.
     */
    public CampaignMode(String playerName) {
        player = new CampaignPlayer(playerName, 0);
        allPresidents = CardData.getPresidents("full", null, 0.0);
        eraTournaments = CampaignTournament.buildDefaultEraTournaments();
        buildPackPresets();
    }

    /**
     * Runnable hook, mostly for consistency with the old CampaignMode shape.
     */
    @Override
    public void run() {
        runConsoleGame();
    }

    /**
     * Defines the current hardcoded pack presets.
     *
     * Later, if we want, these can become data-driven pack definitions instead
     * of hardcoded Java objects.
     */
    private void buildPackPresets() {
        starterElitePack = new CampaignPack(1, 0, 5.0, 10.0, allPresidents, null);
        starterMidPack = new CampaignPack(3, 0, 2.5, 4.0, allPresidents, null);
        starterPack = new CampaignPack(6, 0, 0.0, 2.5, allPresidents, null);

        campaignTrailPack = new CampaignPack(1, 90000, 0.0, 10.0, allPresidents, null);
        fillerPack = new CampaignPack(5, 100000, 0.0, 3.0, allPresidents, null);
        starPack = new CampaignPack(1, 200000, 3.5, 10.0, allPresidents, null);
        standardPack = new CampaignPack(5, 400000, 2.0, 10.0, allPresidents, null);
        elitePack = new CampaignPack(3, 700000, 4.0, 10.0, allPresidents, null);
    }

    /**
     * Public accessors for the Swing campaign UI.
     *
     * CampaignMode owns the pack definitions so RunCampaignMode does not need
     * duplicate hardcoded pack prices/ranges.
     */
    public CampaignPack getStarterElitePack() {
        return starterElitePack;
    }

    public CampaignPack getStarterMidPack() {
        return starterMidPack;
    }

    public CampaignPack getStarterPack() {
        return starterPack;
    }

    public CampaignPack getCampaignTrailPack() {
        return campaignTrailPack;
    }

    public CampaignPack getFillerPack() {
        return fillerPack;
    }

    public CampaignPack getStarPack() {
        return starPack;
    }

    public CampaignPack getStandardPack() {
        return standardPack;
    }

    public CampaignPack getElitePack() {
        return elitePack;
    }

    public List<President> getAllPresidents() {
        return new ArrayList<President>(allPresidents);
    }

    public int getTotalPresidentCount() {
        return allPresidents == null ? 0 : allPresidents.size();
    }

    /**
     * Backward-compatible reward getters. The old one-match setup is now treated
     * as the Medium match.
     */
    public int getWinRewardPC() {
        return getMediumWinRewardPC();
    }

    public int getLossRewardPC() {
        return getMediumLossRewardPC();
    }

    public int getEasyWinRewardPC() {
        return EASY_WIN_REWARD_PC;
    }

    public int getEasyLossRewardPC() {
        return EASY_LOSS_REWARD_PC;
    }

    public int getMediumWinRewardPC() {
        return MEDIUM_WIN_REWARD_PC;
    }

    public int getMediumLossRewardPC() {
        return MEDIUM_LOSS_REWARD_PC;
    }

    public int getHardWinRewardPC() {
        return HARD_WIN_REWARD_PC;
    }

    public int getHardLossRewardPC() {
        return HARD_LOSS_REWARD_PC;
    }

    public String[] getMatchDifficultyNames() {
        return new String[] { "Easy", "Medium", "Hard" };
    }

    public int getMatchWinRewardPC(String matchDifficulty) {
        String difficulty = normalizeMatchDifficulty(matchDifficulty);
        if ("Easy".equals(difficulty)) {
            return EASY_WIN_REWARD_PC;
        } else if ("Hard".equals(difficulty)) {
            return HARD_WIN_REWARD_PC;
        } else {
            return MEDIUM_WIN_REWARD_PC;
        }
    }

    public int getMatchLossRewardPC(String matchDifficulty) {
        String difficulty = normalizeMatchDifficulty(matchDifficulty);
        if ("Easy".equals(difficulty)) {
            return EASY_LOSS_REWARD_PC;
        } else if ("Hard".equals(difficulty)) {
            return HARD_LOSS_REWARD_PC;
        } else {
            return MEDIUM_LOSS_REWARD_PC;
        }
    }

    public String getMatchDescription(String matchDifficulty) {
        String difficulty = normalizeMatchDifficulty(matchDifficulty);
        return difficulty + " Match: CPU " + difficulty + " AI, "
                + getCpuDeckDescription(difficulty)
                + ", win " + formatPC(getMatchWinRewardPC(difficulty))
                + ", loss " + formatPC(getMatchLossRewardPC(difficulty));
    }

    /*** V06 CAMPAIGN MATCH BRIDGE METHODS ***/

    /*** V06.8 PENDING PLAYABLE MATCH / ANTI-CHEESE METHODS ***/

    /**
     * Locks in a playable campaign match before the playable board opens.
     *
     * If the app is closed, killed, or crashes before the match result is
     * returned, the next campaign load will see this pending match and count it
     * as a forfeit loss with no reward. This prevents re-rolling tournament
     * rounds by quitting mid-match.
     */
    public boolean beginPendingPlayableMatch(CampaignMatchConfig config) {
        if (config == null) {
            System.out.println("Could not start playable match: missing match config.");
            return false;
        }

        String validationError = config.getValidationError();
        if (validationError != null && !validationError.isEmpty()) {
            System.out.println("Could not start playable match: " + validationError);
            return false;
        }

        if (!config.isPlayableExecutionMode()) {
            System.out.println("Could not start playable match: config was not marked playable.");
            return false;
        }

        pendingPlayableMatchConfig = config;
        pendingPlayableMatchStartedAtMillis = System.currentTimeMillis();

        boolean saved = autosaveCampaign();
        if (saved) {
            System.out.println("Playable match locked in: " + config.getMatchTitle() + ".");
            System.out.println("If the app closes before this match finishes, it will count as a forfeit loss.");
        } else {
            clearPendingPlayableMatchInMemory();
            System.out.println("Could not save the pending playable match. Match launch cancelled.");
        }
        return saved;
    }

    /**
     * Clears an unlaunched pending match, mostly for launch failures before the
     * playable board actually appears. Normal completed playable matches are
     * cleared automatically when their result is applied.
     */
    public boolean clearPendingPlayableMatch() {
        if (!hasPendingPlayableMatch()) {
            return true;
        }
        clearPendingPlayableMatchInMemory();
        boolean saved = autosaveCampaign();
        if (saved) {
            System.out.println("Pending playable match cleared.");
        }
        return saved;
    }

    public boolean hasPendingPlayableMatch() {
        return pendingPlayableMatchConfig != null;
    }

    public String getPendingPlayableMatchSummary() {
        if (pendingPlayableMatchConfig == null) {
            return "No pending playable match.";
        }
        return pendingPlayableMatchConfig.getMatchTitle();
    }

    private void clearPendingPlayableMatchInMemory() {
        pendingPlayableMatchConfig = null;
        pendingPlayableMatchStartedAtMillis = 0L;
    }

    /**
     * Builds the standard CampaignMatchConfig for a quick-play campaign match.
     *
     * This is the key bridge object for v06. Sim mode and playable mode should
     * both start from the same config, which keeps deck construction, CPU pools,
     * names, and difficulty rules from drifting apart.
     */
    public CampaignMatchConfig buildQuickMatchConfig(String matchDifficulty, boolean playable) {
        String difficulty = normalizeMatchDifficulty(matchDifficulty);
        String playerName = player == null ? "Player" : player.getName();
        List<President> playerDeck = player == null
                ? new ArrayList<President>()
                : player.getActiveDeck();
        List<President> cpuDeck = getCpuPresidentsForQuickMatch(difficulty);

        CampaignMatchConfig config = CampaignMatchConfig.quickPlay(
                playerName,
                difficulty,
                playerDeck,
                cpuDeck
        );
        return config.withExecutionMode(playable
                ? CampaignMatchConfig.EXECUTION_MODE_PLAYABLE
                : CampaignMatchConfig.EXECUTION_MODE_SIMULATED);
    }

    /**
     * Builds the CampaignMatchConfig for one specific tournament round.
     *
     * RunCampaignMode should pass the locked tournament-entry deck here. If the
     * deck is null, CampaignMode falls back to the current best 10 era cards.
     */
    public CampaignMatchConfig buildTournamentRoundConfig(
            CampaignTournament tournament, int roundNumber,
            List<President> lockedTournamentDeck, boolean playable
    ) {
        if (tournament == null) {
            throw new IllegalArgumentException("Tournament cannot be null.");
        }

        List<President> playerDeck = lockedTournamentDeck == null
                ? getTournamentActiveDeck(tournament)
                : new ArrayList<President>(lockedTournamentDeck);
        List<President> cpuDeck = getPresidentsForTournament(tournament);
        String playerName = player == null ? "Player" : player.getName();

        CampaignMatchConfig config = CampaignMatchConfig.tournamentRound(
                playerName,
                tournament,
                roundNumber,
                playerDeck,
                cpuDeck
        );
        return config.withExecutionMode(playable
                ? CampaignMatchConfig.EXECUTION_MODE_PLAYABLE
                : CampaignMatchConfig.EXECUTION_MODE_SIMULATED);
    }

    public List<President> getCpuPresidentsForQuickMatch(String matchDifficulty) {
        String difficulty = normalizeMatchDifficulty(matchDifficulty);
        List<President> cpuPresidents = CardData.getPresidents("full", null, getCpuMinRating(difficulty));
        Collections.sort(cpuPresidents, presidentAverageComparator());
        return cpuPresidents;
    }

    public String getCpuNameForDifficulty(String matchDifficulty) {
        return normalizeMatchDifficulty(matchDifficulty) + " CPU";
    }

    /**
     * Runs an AI-vs-AI simulation from the same CampaignMatchConfig that a
     * playable match would use.
     *
     * This keeps v06 safe: the GUI can offer both Sim and Play, but both paths
     * use the same player deck, CPU deck, difficulty, and match metadata.
     */
    public CampaignMatchResult simulateCampaignMatchConfig(CampaignMatchConfig config) {
        if (config == null) {
            return CampaignMatchResult.error(null, "Campaign match config was missing.");
        }

        String validationError = config.getValidationError();
        if (validationError != null && !validationError.isEmpty()) {
            System.out.println(validationError);
            return CampaignMatchResult.error(config, validationError);
        }

        CampaignMatchConfig simulatedConfig = config.withExecutionMode(
                CampaignMatchConfig.EXECUTION_MODE_SIMULATED
        );

        MatchResult result = simulateAIMatch(
                simulatedConfig,
                getPlayerDeckDescriptionForConfig(simulatedConfig),
                getCpuDeckDescriptionForConfig(simulatedConfig),
                calculateDeckStrength(simulatedConfig.getPlayerPresidentDeck())
        );

        String finalScoreText = "Final score: " + simulatedConfig.getPlayerName() + " "
                + result.playerRoundsWon + " - " + simulatedConfig.getCpuName() + " "
                + result.cpuRoundsWon;

        return CampaignMatchResult.builder(simulatedConfig)
                .resultType(CampaignMatchResult.RESULT_COMPLETED)
                .playerWon(result.playerWon)
                .playerRoundsWon(result.playerRoundsWon)
                .cpuRoundsWon(result.cpuRoundsWon)
                .finalScoreText(finalScoreText)
                .message(result.playerWon ? "Campaign match won." : "Campaign match lost.")
                .build();
    }

    /**
     * Applies quick-play rewards from either a simulated result or a future
     * playable result returned by RunElectionGameCombined.
     */
    public boolean applyQuickMatchResult(String matchDifficulty, CampaignMatchResult result) {
        if (result == null) {
            System.out.println("No campaign match result was returned.");
            return false;
        }
        if (!result.shouldApplyCampaignResult()) {
            System.out.println("Campaign match did not finish: " + result.getMessage());
            clearPendingPlayableMatchInMemory();
            autosaveCampaign();
            return false;
        }

        String difficulty = normalizeMatchDifficulty(matchDifficulty);
        if (result.getConfig() != null) {
            difficulty = normalizeMatchDifficulty(result.getConfig().getDifficulty());
        }

        int reward;
        if (result.wasResigned()) {
            // Forfeits/interrupted playable matches count as losses, but do not
            // pay the normal quick-match consolation reward.
            reward = 0;
        } else {
            reward = result.didPlayerWin()
                    ? getMatchWinRewardPC(difficulty)
                    : getMatchLossRewardPC(difficulty);
        }

        player.recordMatch(result.didPlayerWin());
        player.addPC(reward);

        System.out.println();
        if (result.didPlayerWin()) {
            System.out.println("You won the " + difficulty + " campaign match!");
        } else if (result.wasResigned()) {
            System.out.println("You resigned the " + difficulty + " campaign match.");
        } else {
            System.out.println("You lost the " + difficulty + " campaign match.");
        }

        String finalScoreText = result.getFinalScoreText();
        if (finalScoreText == null || finalScoreText.trim().isEmpty()) {
            System.out.println("Final score: " + player.getName() + " "
                    + result.getPlayerRoundsWon() + " - " + getCpuNameForDifficulty(difficulty)
                    + " " + result.getCpuRoundsWon());
        } else {
            System.out.println(finalScoreText);
        }
        if (reward > 0) {
            System.out.println("You earned " + formatPC(reward) + ".");
        } else {
            System.out.println("No Political Capital earned for a forfeit.");
        }
        System.out.println("Campaign record: " + player.getRecordSummary());
        printStatus();
        clearPendingPlayableMatchInMemory();
        autosaveCampaign();
        return result.didPlayerWin();
    }

    /**
     * Applies one tournament-round result from either sim mode or playable mode.
     */
    public boolean applyTournamentRoundResult(
            CampaignTournament tournament, int roundNumber, CampaignMatchResult result
    ) {
        if (tournament == null) {
            System.out.println("That tournament does not exist.");
            return false;
        }
        if (roundNumber < 1 || roundNumber > 3) {
            System.out.println("Tournament round must be 1, 2, or 3.");
            return false;
        }
        if (result == null) {
            System.out.println("No tournament match result was returned.");
            return false;
        }
        if (!result.shouldApplyCampaignResult()) {
            System.out.println("Tournament match did not finish: " + result.getMessage());
            clearPendingPlayableMatchInMemory();
            autosaveCampaign();
            return false;
        }

        String difficulty = tournament.getRoundDifficulty(roundNumber);
        player.recordMatch(result.didPlayerWin());

        System.out.println();
        if (result.didPlayerWin()) {
            int pcReward = tournament.getRoundWinPC(roundNumber);
            player.addPC(pcReward);
            System.out.println("You won Round " + roundNumber + " of "
                    + tournament.getTournamentName() + "!");
            printResultFinalScore(result, difficulty);
            System.out.println("Round " + roundNumber + " reward: " + formatPC(pcReward)
                    + " + " + tournament.getSouvenirPackName() + ".");

            awardTournamentSouvenirPack(tournament);

            if (roundNumber == 3) {
                System.out.println("Tournament clear bonus: "
                        + tournament.getChampionPackName() + ".");
                awardTournamentChampionPack(tournament);
                System.out.println("You cleared " + tournament.getTournamentName() + "!");
            } else {
                System.out.println("You advance to Round " + (roundNumber + 1) + ".");
            }
        } else {
            if (result.wasResigned()) {
                System.out.println("You resigned Round " + roundNumber + " of "
                        + tournament.getTournamentName() + ".");
            } else {
                System.out.println("You lost Round " + roundNumber + " of "
                        + tournament.getTournamentName() + ".");
            }
            printResultFinalScore(result, difficulty);
            System.out.println("Tournament ended. No reward earned for this round.");
        }

        System.out.println("Campaign record: " + player.getRecordSummary());
        printStatus();
        clearPendingPlayableMatchInMemory();
        autosaveCampaign();
        return result.didPlayerWin();
    }

    private void printResultFinalScore(CampaignMatchResult result, String difficulty) {
        String finalScoreText = result.getFinalScoreText();
        if (finalScoreText == null || finalScoreText.trim().isEmpty()) {
            System.out.println("Final score: " + player.getName() + " "
                    + result.getPlayerRoundsWon() + " - " + difficulty + " CPU "
                    + result.getCpuRoundsWon());
        } else {
            System.out.println(finalScoreText);
        }
    }

    private String getPlayerDeckDescriptionForConfig(CampaignMatchConfig config) {
        if (config != null && config.isTournamentMatch()) {
            String eraName = config.getTournamentEraName().isEmpty()
                    ? "era"
                    : config.getTournamentEraName();
            return "Player sim: Medium AI using tournament-entry " + eraName + " deck";
        }
        return "Player sim: Medium AI using Active Deck";
    }

    private String getCpuDeckDescriptionForConfig(CampaignMatchConfig config) {
        if (config != null && config.isTournamentMatch()) {
            String eraName = config.getTournamentEraName().isEmpty()
                    ? "era"
                    : config.getTournamentEraName();
            return "CPU sim: " + config.getDifficulty() + " AI using full "
                    + eraName + " card pool";
        }
        String difficulty = config == null ? "Medium" : normalizeMatchDifficulty(config.getDifficulty());
        return "CPU sim: " + difficulty + " AI using " + getCpuDeckDescription(difficulty);
    }

    public static String getSaveFilePath() {
        return SAVE_FILE_PATH;
    }

    /**
     * Starts a brand-new campaign with the original starter-pack flow.
     */
    public void startNewCampaign() {
        String playerName = player == null ? "Player" : player.getName();
        player = new CampaignPlayer(playerName, 0);

        System.out.println("Welcome to Election Game Campaign Mode! It seems like you have not yet started with this account.");
        System.out.println("Let's setup your deck with the first opening packs and " + formatPC(STARTING_POLITICAL_CAPITAL) + "!");
        System.out.println();

        player.addPC(STARTING_POLITICAL_CAPITAL);
        System.out.println("You received " + formatPC(STARTING_POLITICAL_CAPITAL) + ".");
        printStatus();
        System.out.println();

        System.out.println("For your first pack, you'll get one 5.0+ rated President. Opening now!");
        buyPack("Starter Elite Pack", starterElitePack);

        System.out.println("Hopefully that card can lead you to glory!");
        System.out.println("Next, let's get 3 mid-tier cards to round out your deck!");
        buyPack("Starter Mid Pack", starterMidPack);

        System.out.println("You're beginning to build out a squad! Here's the last starter pack to fill out your deck:");
        buyPack("Starter Pack", starterPack);

        System.out.println("Nice! Here's your full beginner deck:");
        player.printCollection();
        printStatus();
        autosaveCampaign();
    }

    /**
     * Buys/opens a pack. This handles both paid packs and free packs.
     *
     * If pack.getCost() is 0, this functions exactly like a normal free reward
     * pack because player.spendPC(0) succeeds and subtracts nothing.
     */
    public boolean buyPack(String packName, CampaignPack pack) {
        return openPackInternal(
                packName, pack, true, DUPLICATE_SALE_PERCENT, true, true
        );
    }

    /**
     * Opens a pack using custom duplicate handling.
     *
     * Normal store packs charge their listed cost and use the standard 25%
     * duplicate refund. Tournament reward packs are free and use the v5
     * full-value duplicate refund so era farming does not feel punishing.
     */
    private boolean openPackInternal(
            String packName, CampaignPack pack, boolean chargeCost,
            double duplicateSalePercent, boolean printStatusAfter,
            boolean autosaveAfter
    ) {
        if (pack == null) {
            System.out.println("That pack does not exist.");
            return false;
        }

        if (chargeCost && !player.spendPC(pack.getCost())) {
            System.out.println("Not enough Political Capital for " + packName + ".");
            System.out.println("  Cost: " + formatPC(pack.getCost()));
            System.out.println("  Current balance: " + formatPC(player.getPC()));
            return false;
        }

        System.out.println();
        System.out.println("Opened " + packName + "!");

        List<President> pulls = pack.pull();
        if (pulls.isEmpty()) {
            System.out.println("  No eligible cards were found for this pack.");
            if (printStatusAfter) {
                printStatus();
            }
            System.out.println();
            if (autosaveAfter) {
                autosaveCampaign();
            }
            return true;
        }

        int newCards = 0;
        int duplicates = 0;
        int duplicatePC = 0;

        for (President president : pulls) {
            if (player.addPresident(president)) {
                newCards++;
                System.out.println("  New card: " + president.toString() + " "
                        + String.format("%.2f", president.getAv()));
            } else {
                duplicates++;
                int duplicateValue = getDuplicateSaleValue(president, duplicateSalePercent);
                duplicatePC += duplicateValue;
                player.addPC(duplicateValue);
                System.out.println("  Duplicate: " + president.toString()
                        + " auto-sold for " + formatPC(duplicateValue) + ".");
            }
        }

        System.out.println("  Summary: " + newCards + " new, " + duplicates
                + " duplicates, " + formatPC(duplicatePC) + " from duplicates.");
        if (printStatusAfter) {
            printStatus();
        }
        System.out.println();
        if (autosaveAfter) {
            autosaveCampaign();
        }
        return true;
    }

    /*** ERA TOURNAMENT METHODS ***/

    public List<CampaignTournament> getEraTournaments() {
        return new ArrayList<CampaignTournament>(eraTournaments);
    }

    public CampaignTournament getTournamentById(String tournamentId) {
        if (tournamentId == null) {
            return null;
        }

        for (CampaignTournament tournament : eraTournaments) {
            if (tournament != null && tournamentId.equalsIgnoreCase(tournament.getId())) {
                return tournament;
            }
        }
        return null;
    }

    public int getEraProspectPackCost() {
        return ERA_PROSPECT_PACK_COST;
    }

    public String getEraProspectPackName(CampaignTournament tournament) {
        if (tournament == null) {
            return "Era Prospect Pack";
        }
        return tournament.getEraName() + " Prospect Pack";
    }

    public CampaignPack getEraProspectPack(CampaignTournament tournament) {
        if (tournament == null) {
            return null;
        }
        return new CampaignPack(
                ERA_PROSPECT_PACK_CARD_COUNT,
                ERA_PROSPECT_PACK_COST,
                ERA_PROSPECT_PACK_MIN_RATING,
                ERA_PROSPECT_PACK_MAX_RATING,
                getPresidentsForTournament(tournament),
                null
        );
    }

    public boolean buyEraProspectPack(CampaignTournament tournament) {
        if (tournament == null) {
            System.out.println("That era does not exist.");
            return false;
        }
        return buyPack(getEraProspectPackName(tournament), getEraProspectPack(tournament));
    }

    public List<President> getPresidentsForTournament(CampaignTournament tournament) {
        ArrayList<President> eligiblePresidents = new ArrayList<President>();
        if (tournament == null || allPresidents == null) {
            return eligiblePresidents;
        }

        for (President president : allPresidents) {
            if (tournament.matchesPresident(president)) {
                eligiblePresidents.add(president);
            }
        }
        Collections.sort(eligiblePresidents, presidentAverageComparator());
        return eligiblePresidents;
    }

    public List<President> getOwnedPresidentsForTournament(CampaignTournament tournament) {
        ArrayList<President> ownedPresidents = new ArrayList<President>();
        if (tournament == null || player == null) {
            return ownedPresidents;
        }

        for (President president : player.getPresidentCollection()) {
            if (tournament.matchesPresident(president)) {
                ownedPresidents.add(president);
            }
        }
        Collections.sort(ownedPresidents, presidentAverageComparator());
        return ownedPresidents;
    }

    public int getOwnedCountForTournament(CampaignTournament tournament) {
        return getOwnedPresidentsForTournament(tournament).size();
    }

    public boolean canEnterTournament(CampaignTournament tournament) {
        return getOwnedCountForTournament(tournament) >= CampaignTournament.REQUIRED_ERA_CARDS;
    }

    public List<President> getTournamentActiveDeck(CampaignTournament tournament) {
        List<President> ownedPresidents = getOwnedPresidentsForTournament(tournament);
        int deckSize = Math.min(CampaignTournament.REQUIRED_ERA_CARDS, ownedPresidents.size());
        return new ArrayList<President>(ownedPresidents.subList(0, deckSize));
    }

    public double getTournamentDeckStrength(CampaignTournament tournament) {
        return calculateDeckStrength(getTournamentActiveDeck(tournament));
    }

    public String getTournamentEligibilityText(CampaignTournament tournament) {
        if (tournament == null) {
            return "Tournament not found.";
        }

        int owned = getOwnedCountForTournament(tournament);
        int required = CampaignTournament.REQUIRED_ERA_CARDS;
        if (owned >= required) {
            return "Ready: " + owned + "/" + required + " " + tournament.getEraName()
                    + " cards owned | Deck Strength: "
                    + String.format("%.2f", getTournamentDeckStrength(tournament));
        }
        return "Locked: " + owned + "/" + required + " " + tournament.getEraName()
                + " cards owned | Need " + (required - owned) + " more.";
    }

    /**
     * Plays one round of an era tournament through the v06 config/result bridge.
     *
     * The overload without a supplied deck auto-builds the current best 10-card
     * era deck. The overload with a supplied deck lets the GUI freeze the deck
     * at tournament entry, so reward cards pulled after Round 1 do not quietly
     * upgrade the Round 2/Final deck inside the same tournament run.
     */
    public boolean playTournamentRound(CampaignTournament tournament, int roundNumber) {
        return playTournamentRound(tournament, roundNumber, getTournamentActiveDeck(tournament));
    }

    public boolean playTournamentRound(
            CampaignTournament tournament, int roundNumber, List<President> tournamentDeck
    ) {
        if (tournament == null) {
            System.out.println("That tournament does not exist.");
            return false;
        }

        if (roundNumber < 1 || roundNumber > 3) {
            System.out.println("Tournament round must be 1, 2, or 3.");
            return false;
        }

        if (!canEnterTournament(tournament)) {
            System.out.println(getTournamentEligibilityText(tournament));
            return false;
        }

        List<President> usableTournamentDeck = tournamentDeck == null
                ? getTournamentActiveDeck(tournament)
                : new ArrayList<President>(tournamentDeck);
        List<President> cpuEraPool = getPresidentsForTournament(tournament);

        System.out.println();
        System.out.println(tournament.getTournamentName() + " | Round " + roundNumber);
        System.out.println("Era: " + tournament.getEraName());
        System.out.println("Eligibility: " + getTournamentEligibilityText(tournament));

        if (usableTournamentDeck.size() < CampaignTournament.REQUIRED_ERA_CARDS) {
            System.out.println("You need at least " + CampaignTournament.REQUIRED_ERA_CARDS
                    + " cards from the " + tournament.getEraName() + " to enter.");
            return false;
        }

        if (cpuEraPool.size() < CampaignMatchConfig.DEFAULT_STARTING_HAND_SIZE) {
            System.out.println("Not enough eligible CPU cards exist for this tournament yet.");
            System.out.println("Eligible CPU cards found: " + cpuEraPool.size());
            return false;
        }

        String difficulty = tournament.getRoundDifficulty(roundNumber);
        System.out.println("Round " + roundNumber + " opponent: " + difficulty
                + " AI using full " + tournament.getEraName() + " card pool.");
        System.out.println("Tournament Deck Strength: "
                + String.format("%.2f", calculateDeckStrength(usableTournamentDeck)));

        CampaignMatchConfig config = buildTournamentRoundConfig(
                tournament,
                roundNumber,
                usableTournamentDeck,
                false
        );
        CampaignMatchResult result = simulateCampaignMatchConfig(config);
        return applyTournamentRoundResult(tournament, roundNumber, result);
    }

    /**
     * Plays a full v5 era tournament in console/sim mode.
     *
     * The GUI uses playTournamentRound(...) directly so it can show a dashboard
     * between rounds. This console helper now uses the same round-by-round bridge
     * path, which keeps tournament rewards consistent with future playable mode.
     */
    public void playTournament(CampaignTournament tournament) {
        if (tournament == null) {
            System.out.println("That tournament does not exist.");
            return;
        }

        List<President> tournamentDeck = getTournamentActiveDeck(tournament);

        System.out.println();
        System.out.println("Entering " + tournament.getTournamentName());
        System.out.println("Era: " + tournament.getEraName());
        if (!tournament.getDescription().isEmpty()) {
            System.out.println(tournament.getDescription());
        }
        System.out.println("Eligibility: " + getTournamentEligibilityText(tournament));

        if (tournamentDeck.size() < CampaignTournament.REQUIRED_ERA_CARDS) {
            System.out.println("You need at least " + CampaignTournament.REQUIRED_ERA_CARDS
                    + " cards from the " + tournament.getEraName() + " to enter.");
            return;
        }

        System.out.println("Tournament Deck:");
        for (President president : tournamentDeck) {
            System.out.println("  - " + president.toString() + " "
                    + String.format("%.2f", president.getAv()));
        }
        System.out.println("Tournament Deck Strength: "
                + String.format("%.2f", calculateDeckStrength(tournamentDeck)));

        int roundsWon = 0;
        for (int round = 1; round <= 3; round++) {
            boolean wonRound = playTournamentRound(tournament, round, tournamentDeck);
            if (!wonRound) {
                break;
            }
            roundsWon++;
        }

        System.out.println();
        System.out.println(tournament.getTournamentName() + " Result: " + roundsWon + "/3 rounds won.");
        System.out.println("Campaign record: " + player.getRecordSummary());
        printStatus();
    }

    private void awardTournamentSouvenirPack(CampaignTournament tournament) {
        CampaignPack pack = new CampaignPack(
                1,
                0,
                CampaignTournament.SOUVENIR_MIN_RATING,
                CampaignTournament.SOUVENIR_MAX_RATING,
                getPresidentsForTournament(tournament),
                null
        );
        openPackInternal(
                tournament.getSouvenirPackName(),
                pack,
                false,
                CampaignTournament.TOURNAMENT_DUPLICATE_SALE_PERCENT,
                false,
                false
        );
    }

    private void awardTournamentChampionPack(CampaignTournament tournament) {
        CampaignPack pack = new CampaignPack(
                1,
                0,
                CampaignTournament.CHAMPION_MIN_RATING,
                CampaignTournament.CHAMPION_MAX_RATING,
                getPresidentsForTournament(tournament),
                null
        );
        openPackInternal(
                tournament.getChampionPackName(),
                pack,
                false,
                CampaignTournament.TOURNAMENT_DUPLICATE_SALE_PERCENT,
                false,
                false
        );
    }

    /**
     * Simulates one full best-of-five ElectionGame match.
     *
     * Backward-compatible default: play a Medium match.
     */
    public void playSimulatedMatch() {
        playSimulatedMatch("Medium");
    }

    /**
     * Simulates one full best-of-five ElectionGame match at the chosen campaign
     * difficulty.
     *
     * Player side: Medium AI using the campaign player's active deck.
     * CPU side:
     *   Easy   = Easy AI + full card pool
     *   Medium = Medium AI + 3.0+ card pool
     *   Hard   = Hard AI + 5.0+ card pool
     */
    public void playSimulatedMatch(String matchDifficulty) {
        String difficulty = normalizeMatchDifficulty(matchDifficulty);

        if (player.getActiveDeckSize() < player.getActiveDeckMaxSize()) {
            System.out.println("Your Active Deck is not ready yet.");
            System.out.println("  Current Active Deck: " + player.getActiveDeckSize()
                    + "/" + player.getActiveDeckMaxSize() + " Presidents");
            System.out.println("  Open more packs until you have a full Active Deck.");
            return;
        }

        CampaignMatchConfig config = buildQuickMatchConfig(difficulty, false);
        CampaignMatchResult result = simulateCampaignMatchConfig(config);
        applyQuickMatchResult(difficulty, result);
    }

    /**
     * Runs the actual ElectionGame model with AI-vs-AI decisions.
     *
     * Backward-compatible helper retained for older console/debug callers. New
     * v06 code should prefer simulateCampaignMatchConfig(...).
     */
    private MatchResult simulateAIMatch(String difficulty) {
        CampaignMatchConfig config = buildQuickMatchConfig(difficulty, false);
        CampaignMatchResult result = simulateCampaignMatchConfig(config);
        return new MatchResult(
                result.didPlayerWin(),
                result.getPlayerRoundsWon(),
                result.getCpuRoundsWon()
        );
    }

    /**
     * Shared AI-vs-AI runner for configs. This mirrors the future playable path:
     * exact President decks come from CampaignMatchConfig, then ElectionGame does
     * the normal best-of-five match work.
     */
    private MatchResult simulateAIMatch(
            CampaignMatchConfig config, String playerDeckDescription,
            String cpuDeckDescription, double displayedDeckStrength
    ) {
        String difficulty = normalizeMatchDifficulty(config.getDifficulty());

        ElectionGame match = new ElectionGame();
        match.resetForCampaignMatch(config);
        match.setAIDifficulty(difficulty);

        AI playerAI = (AI) match.getPlayer1();
        AI cpuAI = (AI) match.getPlayer2();
        playerAI.setDifficulty(PLAYER_SIM_AI_DIFFICULTY);
        cpuAI.setDifficulty(getCpuAIDifficulty(difficulty));

        Deck playerPresidentDeck = match.getPlayer1PresidentDeck();
        Deck cpuPresidentDeck = match.getPlayer2PresidentDeck();
        Deck policyDeck = match.getPol();

        System.out.println();
        System.out.println("Simulating " + config.getMatchTitle());
        System.out.println("  " + playerDeckDescription);
        System.out.println("  " + cpuDeckDescription);
        System.out.println("Deck Strength: " + String.format("%.2f", displayedDeckStrength));

        int roundNumber = 1;
        int playerRoundsWon = 0;
        int cpuRoundsWon = 0;

        while (match.checkWinner() == 0) {
            President playerCard = playerAI.play(playerPresidentDeck, match.getElection());
            President cpuCard = cpuAI.play(cpuPresidentDeck, match.getElection());

            ArrayList<Policy> playerPolicies = playerAI.playPolicies(playerCard, policyDeck);
            ArrayList<Policy> cpuPolicies = cpuAI.playPolicies(cpuCard, policyDeck);

            int playerRoundScore = match.getElection().getScore(
                    playerCard, playerPolicies.get(0), playerPolicies.get(1)
            );
            int cpuRoundScore = match.getElection().getScore(
                    cpuCard, cpuPolicies.get(0), cpuPolicies.get(1)
            );

            match.playRound(playerCard, playerPolicies, cpuCard, cpuPolicies);

            boolean playerWonRound = match.getLastWinner();
            if (playerWonRound) {
                playerRoundsWon++;
            } else {
                cpuRoundsWon++;
            }

            System.out.println("  Round " + roundNumber + ": "
                    + (playerWonRound ? "W" : "L")
                    + " | " + playerCard.toString() + " (" + playerRoundScore + ")"
                    + " vs " + cpuCard.toString() + " (" + cpuRoundScore + ")");

            if (match.checkWinner() == 0) {
                match.finishRound();
            }
            roundNumber++;
        }

        return new MatchResult(match.checkWinner() == 1, playerRoundsWon, cpuRoundsWon);
    }

    private String normalizeMatchDifficulty(String matchDifficulty) {
        if (matchDifficulty == null) {
            return "Medium";
        }

        String trimmed = matchDifficulty.trim();
        if (trimmed.equalsIgnoreCase("Easy")) {
            return "Easy";
        } else if (trimmed.equalsIgnoreCase("Hard")) {
            return "Hard";
        } else {
            return "Medium";
        }
    }

    private double getCpuAIDifficulty(String difficulty) {
        if ("Easy".equals(difficulty)) {
            return EASY_CPU_AI_DIFFICULTY;
        } else if ("Hard".equals(difficulty)) {
            return HARD_CPU_AI_DIFFICULTY;
        } else {
            return MEDIUM_CPU_AI_DIFFICULTY;
        }
    }

    private double getCpuMinRating(String difficulty) {
        if ("Easy".equals(difficulty)) {
            return EASY_CPU_MIN_RATING;
        } else if ("Hard".equals(difficulty)) {
            return HARD_CPU_MIN_RATING;
        } else {
            return MEDIUM_CPU_MIN_RATING;
        }
    }

    private String getCpuDeckDescription(String difficulty) {
        if ("Easy".equals(difficulty)) {
            return "full CPU card pool";
        } else if ("Hard".equals(difficulty)) {
            return "5.0+ rated CPU card pool";
        } else {
            return "3.0+ rated CPU card pool";
        }
    }

    private Deck buildPresidentDeck(List<President> presidents) {

        Deck deck = new Deck();
        deck.fillPres(presidents);
        deck.shuffle();
        return deck;
    }

    private Deck buildPolicyDeck() {
        Deck deck = new Deck();
        deck.fillPol(CardData.getPolicies());
        deck.shuffle();
        return deck;
    }

    private Comparator<President> presidentAverageComparator() {
        return new Comparator<President>() {
            @Override
            public int compare(President p1, President p2) {
                if (p1 == null && p2 == null) {
                    return 0;
                }
                if (p1 == null) {
                    return 1;
                }
                if (p2 == null) {
                    return -1;
                }

                int averageCompare = Double.compare(p2.getAv(), p1.getAv());
                if (averageCompare != 0) {
                    return averageCompare;
                }

                int valueCompare = Double.compare(p2.getValue(), p1.getValue());
                if (valueCompare != 0) {
                    return valueCompare;
                }

                return p1.getName().compareToIgnoreCase(p2.getName());
            }
        };
    }

    /**
     * Tournament deck strength mirrors the flatter v4 active-deck philosophy:
     * stars still matter, but full 10-card depth matters more.
     */
    private double calculateDeckStrength(List<President> presidents) {
        if (presidents == null || presidents.isEmpty()) {
            return 0.0;
        }

        List<President> sorted = new ArrayList<President>(presidents);
        Collections.sort(sorted, presidentAverageComparator());

        double topFiveAverage = averagePresidentRating(sorted, Math.min(5, sorted.size()));
        double fullDeckAverage = averagePresidentRating(sorted, sorted.size());
        return (0.30 * topFiveAverage) + (0.70 * fullDeckAverage);
    }

    private double averagePresidentRating(List<President> presidents, int count) {
        if (presidents == null || presidents.isEmpty() || count <= 0) {
            return 0.0;
        }

        int usableCount = Math.min(count, presidents.size());
        double total = 0.0;
        for (int i = 0; i < usableCount; i++) {
            total += presidents.get(i).getAv();
        }
        return total / usableCount;
    }

    private static class MatchResult {
        private boolean playerWon;
        private int playerRoundsWon;
        private int cpuRoundsWon;

        private MatchResult(boolean playerWon, int playerRoundsWon, int cpuRoundsWon) {
            this.playerWon = playerWon;
            this.playerRoundsWon = playerRoundsWon;
            this.cpuRoundsWon = cpuRoundsWon;
        }
    }

    /**
     * Saves the current campaign to a simple text file.
     *
     * Current format:
     *   line 1 = save version
     *   later lines = KEY=value pairs
     *   CARDS is one CSV row of owned President names
     */
    public boolean saveCampaign() {
        return saveCampaign(true);
    }

    private boolean autosaveCampaign() {
        return saveCampaign(false);
    }

    private boolean saveCampaign(boolean printMessage) {
        File saveFile = new File(SAVE_FILE_PATH);
        File parent = saveFile.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }

        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(saveFile));
            writer.write(SAVE_VERSION);
            writer.newLine();
            writer.write("NAME=" + player.getName());
            writer.newLine();
            writer.write("PC=" + player.getPC());
            writer.newLine();
            writer.write("WINS=" + player.getWins());
            writer.newLine();
            writer.write("LOSSES=" + player.getLosses());
            writer.newLine();
            writer.write("CARDS=");
            writeCardCsvRow(writer, player.getPresidentCollection());
            writer.newLine();
            writePendingPlayableMatchFields(writer);

            if (printMessage) {
                System.out.println("Campaign saved to " + SAVE_FILE_PATH + ".");
            }
            return true;
        } catch (IOException e) {
            System.out.println("Could not save campaign to " + SAVE_FILE_PATH + ".");
            return false;
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                System.out.println("Could not close save file cleanly.");
            }
        }
    }

    private void writeCardCsvRow(BufferedWriter writer, List<President> cards) throws IOException {
        if (cards == null) {
            return;
        }

        boolean firstCard = true;
        for (President president : cards) {
            if (president == null) {
                continue;
            }
            if (!firstCard) {
                writer.write(",");
            }
            writer.write(president.getName());
            firstCard = false;
        }
    }

    private void writePendingPlayableMatchFields(BufferedWriter writer) throws IOException {
        if (pendingPlayableMatchConfig == null) {
            writer.write("PENDING_PLAYABLE_MATCH=false");
            writer.newLine();
            return;
        }

        writer.write("PENDING_PLAYABLE_MATCH=true");
        writer.newLine();
        writer.write("PENDING_MATCH_ID=" + pendingPlayableMatchConfig.getMatchId());
        writer.newLine();
        writer.write("PENDING_MATCH_TITLE=" + pendingPlayableMatchConfig.getMatchTitle());
        writer.newLine();
        writer.write("PENDING_MATCH_TYPE=" + pendingPlayableMatchConfig.getMatchType());
        writer.newLine();
        writer.write("PENDING_DIFFICULTY=" + pendingPlayableMatchConfig.getDifficulty());
        writer.newLine();
        writer.write("PENDING_TOURNAMENT_ID=" + pendingPlayableMatchConfig.getTournamentId());
        writer.newLine();
        writer.write("PENDING_TOURNAMENT_NAME=" + pendingPlayableMatchConfig.getTournamentName());
        writer.newLine();
        writer.write("PENDING_TOURNAMENT_ROUND=" + pendingPlayableMatchConfig.getTournamentRound());
        writer.newLine();
        writer.write("PENDING_STARTED_AT=" + pendingPlayableMatchStartedAtMillis);
        writer.newLine();
    }

    /**
     * Loads campaign progress from the save file, if one exists.
     */
    public boolean loadCampaign() {
        File saveFile = new File(SAVE_FILE_PATH);
        if (!saveFile.exists()) {
            System.out.println("No campaign save file found at " + SAVE_FILE_PATH + ".");
            return false;
        }

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(saveFile));
            String version = reader.readLine();

            if (SAVE_VERSION.equals(version) || SAVE_VERSION_V2.equals(version)) {
                return loadCurrentSaveFormat(reader);
            } else if (SAVE_VERSION_V1.equals(version)) {
                return loadLegacyV1SaveFormat(reader);
            } else {
                System.out.println("Save file version was not recognized.");
                return false;
            }
        } catch (IOException e) {
            System.out.println("Could not load campaign from " + SAVE_FILE_PATH + ".");
            return false;
        } catch (IllegalArgumentException e) {
            System.out.println("Save file could not be loaded: " + e.getMessage());
            return false;
        } finally {
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                System.out.println("Could not close save file cleanly.");
            }
        }
    }

    private boolean loadCurrentSaveFormat(BufferedReader reader) throws IOException {
        Map<String, String> fields = new HashMap<String, String>();
        String line = reader.readLine();
        while (line != null) {
            int equalsIndex = line.indexOf('=');
            if (equalsIndex > 0) {
                String key = line.substring(0, equalsIndex).trim().toUpperCase();
                String value = line.substring(equalsIndex + 1);
                fields.put(key, value);
            }
            line = reader.readLine();
        }

        String savedName = getRequiredSaveField(fields, "NAME");
        int savedPC = parseNonNegativeInt(getRequiredSaveField(fields, "PC"), "PC");
        int savedWins = parseNonNegativeInt(fields.containsKey("WINS") ? fields.get("WINS") : "0", "WINS");
        int savedLosses = parseNonNegativeInt(fields.containsKey("LOSSES") ? fields.get("LOSSES") : "0", "LOSSES");
        String cardCsvRow = fields.containsKey("CARDS") ? fields.get("CARDS") : "";

        boolean loaded = applyLoadedCampaign(savedName, savedPC, savedWins, savedLosses, cardCsvRow, false);
        if (loaded) {
            applyAbandonedPendingPlayableMatchIfNeeded(fields);
        }
        return loaded;
    }

    private void applyAbandonedPendingPlayableMatchIfNeeded(Map<String, String> fields) {
        if (fields == null || !parseSaveBoolean(fields.get("PENDING_PLAYABLE_MATCH"))) {
            clearPendingPlayableMatchInMemory();
            return;
        }

        String matchTitle = cleanSaveField(fields.get("PENDING_MATCH_TITLE"), "Playable Campaign Match");
        String matchType = cleanSaveField(fields.get("PENDING_MATCH_TYPE"), "");
        String difficulty = normalizeMatchDifficulty(fields.get("PENDING_DIFFICULTY"));
        String tournamentName = cleanSaveField(fields.get("PENDING_TOURNAMENT_NAME"), "Era Tournament");
        int roundNumber = parseOptionalPositiveInt(fields.get("PENDING_TOURNAMENT_ROUND"), 0);

        System.out.println();
        System.out.println("Previous playable match was abandoned: " + matchTitle + ".");
        System.out.println("It has been counted as a forfeit loss with no reward.");

        player.recordMatch(false);

        if (CampaignMatchConfig.MATCH_TYPE_TOURNAMENT.equals(matchType)) {
            System.out.println("Tournament result: " + tournamentName
                    + (roundNumber > 0 ? " Round " + roundNumber : "")
                    + " ended by forfeit. No tournament reward earned.");
        } else {
            System.out.println("Quick match result: " + difficulty
                    + " campaign match forfeited. No Political Capital earned.");
        }

        clearPendingPlayableMatchInMemory();
        printStatus();
        autosaveCampaign();
    }

    private boolean parseSaveBoolean(String text) {
        return text != null && text.trim().equalsIgnoreCase("true");
    }

    private int parseOptionalPositiveInt(String text, int defaultValue) {
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

    private String cleanSaveField(String text, String defaultValue) {
        if (text == null || text.trim().isEmpty()) {
            return defaultValue;
        }
        return text.trim();
    }

    private boolean loadLegacyV1SaveFormat(BufferedReader reader) throws IOException {
        String savedName = reader.readLine();
        String savedPCText = reader.readLine();
        String cardCsvRow = reader.readLine();

        if (savedName == null || savedPCText == null) {
            throw new IllegalArgumentException("legacy save file is missing required lines");
        }

        int savedPC = parseNonNegativeInt(savedPCText, "PC");
        return applyLoadedCampaign(savedName, savedPC, 0, 0, cardCsvRow, true);
    }

    private boolean applyLoadedCampaign(
            String savedName, int savedPC, int savedWins, int savedLosses,
            String cardCsvRow, boolean legacySave
    ) {
        CampaignPlayer loadedPlayer = new CampaignPlayer(savedName, 0);
        loadedPlayer.setPC(savedPC);
        loadedPlayer.setRecord(savedWins, savedLosses);

        int loadedCards = loadCardsFromCsvRow(loadedPlayer, cardCsvRow);
        player = loadedPlayer;

        if (legacySave) {
            System.out.println("Loaded legacy campaign save from " + SAVE_FILE_PATH + ".");
            System.out.println("This save will be upgraded the next time it autosaves.");
        } else {
            System.out.println("Loaded campaign from " + SAVE_FILE_PATH + ".");
        }
        System.out.println("Loaded " + loadedCards + " cards.");
        printStatus();
        return true;
    }

    private String getRequiredSaveField(Map<String, String> fields, String key) {
        if (!fields.containsKey(key)) {
            throw new IllegalArgumentException("missing " + key + " field");
        }
        return fields.get(key);
    }

    private int parseNonNegativeInt(String text, String fieldName) {
        try {
            int value = Integer.parseInt(text.trim());
            if (value < 0) {
                throw new IllegalArgumentException(fieldName + " cannot be negative");
            }
            return value;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(fieldName + " is not a valid number");
        }
    }

    private int loadCardsFromCsvRow(CampaignPlayer loadedPlayer, String cardCsvRow) {
        if (loadedPlayer == null || cardCsvRow == null || cardCsvRow.trim().isEmpty()) {
            return 0;
        }

        int loadedCards = 0;
        String[] cardNames = cardCsvRow.split(",");
        for (String cardName : cardNames) {
            President president = findPresidentByName(cardName.trim());
            if (president != null && loadedPlayer.addPresident(president)) {
                loadedCards++;
            } else if (president == null && !cardName.trim().isEmpty()) {
                System.out.println("Warning: save file card not found: " + cardName.trim());
            }
        }
        return loadedCards;
    }

    private President findPresidentByName(String presidentName) {
        if (presidentName == null) {
            return null;
        }

        for (President president : allPresidents) {
            if (president != null && presidentName.equalsIgnoreCase(president.getName())) {
                return president;
            }
        }
        return null;
    }

    private boolean saveFileExists() {
        return new File(SAVE_FILE_PATH).exists();
    }

    /**
     * Duplicate cards auto-sell for 25% of their card PC value in normal store
     * packs. Tournament reward packs can pass a higher duplicate sale percent.
     */
    private int getDuplicateSaleValue(President president) {
        return getDuplicateSaleValue(president, DUPLICATE_SALE_PERCENT);
    }

    private int getDuplicateSaleValue(President president, double duplicateSalePercent) {
        if (president == null) {
            return 0;
        }

        double cleanSalePercent = Math.max(0.0, duplicateSalePercent);
        int rawValue = (int) Math.max(0, president.getValue() * cleanSalePercent);
        if (rawValue == 0) {
            return 0;
        }

        // Keep tiny duplicate refunds from displaying as 0 PC after rounding.
        return Math.max(1000, roundToNearest(rawValue, 1000));
    }

    private int roundToNearest(int value, int nearest) {
        if (nearest <= 0) {
            return value;
        }
        return (int) Math.round(value / (double) nearest) * nearest;
    }

    public static String formatPC(int amount) {
        return String.format("%,d PC", amount);
    }

    public CampaignPlayer getPlayer() {
        return player;
    }

    public void printStatus() {
        int totalPresidents = allPresidents == null ? 0 : allPresidents.size();
        int ownedPresidents = player.getCollectionSize();
        double completionPct = totalPresidents == 0
                ? 0.0
                : (ownedPresidents * 100.0) / totalPresidents;

        System.out.println("Status: " + player.getName()
                + " | Political Capital: " + formatPC(player.getPC())
                + " | Presidents owned: " + ownedPresidents
                + "/" + totalPresidents
                + " (" + String.format("%.1f", completionPct) + "%)"
                + " | Active Deck: " + player.getActiveDeckSize()
                + "/" + player.getActiveDeckMaxSize()
                + " | Strength: " + String.format("%.2f", player.getActiveDeckStrength())
                + " | Record: " + player.getRecordSummary());
    }

    /**
     * Console loop for testing the basic campaign flow.
     */
    public void runConsoleGame() {
        runConsoleGame(new Scanner(System.in));
    }

    /**
     * Console loop using a supplied Scanner. Keeping one Scanner for System.in
     * avoids input-buffer weirdness when main() already read the player name.
     */
    public void runConsoleGame(Scanner scanner) {
        if (!runStartMenu(scanner)) {
            System.out.println("Goodbye.");
            return;
        }

        boolean keepPlaying = true;

        while (keepPlaying) {
            System.out.println();
            System.out.println("What would you like to do?");
            System.out.println("  1 - Play AI-Simulated Match");
            System.out.println("  2 - Buy/Open Packs");
            System.out.println("  3 - Play Era Tournament");
            System.out.println("  4 - View Collection");
            System.out.println("  5 - View Active Deck");
            System.out.println("  6 - View Status");
            System.out.println("  7 - Save Campaign");
            System.out.println("  0 - Quit");
            System.out.print("> ");

            String input = scanner.nextLine().trim();

            if (input.equals("1")) {
                runMatchMenu(scanner);
            } else if (input.equals("2")) {
                runPackMenu(scanner);
            } else if (input.equals("3")) {
                runTournamentMenu(scanner);
            } else if (input.equals("4")) {
                player.printCollection();
            } else if (input.equals("5")) {
                player.printActiveDeck();
            } else if (input.equals("6")) {
                printStatus();
            } else if (input.equals("7")) {
                saveCampaign();
            } else if (input.equals("0") || input.equalsIgnoreCase("q") || input.equalsIgnoreCase("quit")) {
                keepPlaying = false;
            } else {
                System.out.println("Invalid option. Try again.");
            }
        }

        autosaveCampaign();
        System.out.println();
        System.out.println("Thanks for playing Campaign Mode test mode.");
        printStatus();
    }

    private boolean runStartMenu(Scanner scanner) {
        while (true) {
            System.out.println("Campaign Clash - Campaign Mode Console Test");
            System.out.println("Save file: " + SAVE_FILE_PATH);
            System.out.println();

            if (saveFileExists()) {
                System.out.println("  1 - Continue Campaign");
                System.out.println("  2 - Restart Campaign");
                System.out.println("  0 - Quit");
            } else {
                System.out.println("  1 - Start New Campaign");
                System.out.println("  0 - Quit");
            }
            System.out.print("> ");

            String input = scanner.nextLine().trim();

            if (saveFileExists()) {
                if (input.equals("1")) {
                    if (loadCampaign()) {
                        return true;
                    }
                    System.out.println("Load failed. You can restart or quit.");
                    System.out.println();
                } else if (input.equals("2")) {
                    if (confirmRestart(scanner)) {
                        player = new CampaignPlayer(askPlayerName(scanner), 0);
                        startNewCampaign();
                        return true;
                    }
                } else if (input.equals("0") || input.equalsIgnoreCase("q")
                        || input.equalsIgnoreCase("quit")) {
                    return false;
                } else {
                    System.out.println("Invalid option. Try again.");
                    System.out.println();
                }
            } else {
                if (input.equals("1")) {
                    player = new CampaignPlayer(askPlayerName(scanner), 0);
                    startNewCampaign();
                    return true;
                } else if (input.equals("0") || input.equalsIgnoreCase("q")
                        || input.equalsIgnoreCase("quit")) {
                    return false;
                } else {
                    System.out.println("Invalid option. Try again.");
                    System.out.println();
                }
            }
        }
    }

    private String askPlayerName(Scanner scanner) {
        System.out.print("Enter player name (blank for Player): ");
        String name = scanner.nextLine().trim();
        if (name.isEmpty()) {
            return "Player";
        }
        return name;
    }

    private boolean confirmRestart(Scanner scanner) {
        System.out.println("Restarting will overwrite your current Campaign Mode save.");
        System.out.print("Are you sure? (y/n): ");
        String input = scanner.nextLine().trim();
        return input.equalsIgnoreCase("y") || input.equalsIgnoreCase("yes");
    }

    private void runMatchMenu(Scanner scanner) {
        boolean inMatchMenu = true;

        while (inMatchMenu) {
            System.out.println();
            System.out.println("Choose a match difficulty:");
            System.out.println("  1 - " + getMatchDescription("Easy"));
            System.out.println("  2 - " + getMatchDescription("Medium"));
            System.out.println("  3 - " + getMatchDescription("Hard"));
            System.out.println("  0 - Back");
            System.out.print("> ");

            String input = scanner.nextLine().trim();

            if (input.equals("1")) {
                playSimulatedMatch("Easy");
            } else if (input.equals("2")) {
                playSimulatedMatch("Medium");
            } else if (input.equals("3")) {
                playSimulatedMatch("Hard");
            } else if (input.equals("0") || input.equalsIgnoreCase("b") || input.equalsIgnoreCase("back")) {
                inMatchMenu = false;
            } else {
                System.out.println("Invalid option. Try again.");
            }
        }
    }

    private void runTournamentMenu(Scanner scanner) {
        boolean inTournamentMenu = true;

        while (inTournamentMenu) {
            CampaignTournament tournament = chooseTournamentFromMenu(scanner, "Choose an era tournament:");
            if (tournament == null) {
                inTournamentMenu = false;
            } else {
                playTournament(tournament);
            }
        }
    }

    private CampaignTournament chooseTournamentFromMenu(Scanner scanner, String header) {
        while (true) {
            System.out.println();
            System.out.println(header);

            for (int i = 0; i < eraTournaments.size(); i++) {
                CampaignTournament tournament = eraTournaments.get(i);
                System.out.println("  " + (i + 1) + " - "
                        + tournament.getTournamentName() + " ("
                        + tournament.getEraName() + ") | "
                        + getTournamentEligibilityText(tournament));
            }
            System.out.println("  0 - Back");
            System.out.print("> ");

            String input = scanner.nextLine().trim();
            if (input.equals("0") || input.equalsIgnoreCase("b") || input.equalsIgnoreCase("back")) {
                return null;
            }

            try {
                int choice = Integer.parseInt(input);
                if (choice >= 1 && choice <= eraTournaments.size()) {
                    return eraTournaments.get(choice - 1);
                }
            } catch (NumberFormatException e) {
                // Fall through to invalid-option message.
            }

            System.out.println("Invalid option. Try again.");
        }
    }

    private void runPackMenu(Scanner scanner) {
        boolean inPackMenu = true;

        while (inPackMenu) {
            System.out.println();
            System.out.println("Choose a pack:");
            System.out.println("  1 - Campaign Trail Pack (1 President, rating 0.0-10.0, cost "
                    + formatPC(campaignTrailPack.getCost()) + ")");
            System.out.println("  2 - Filler Pack (5 Presidents, rating 0.0-3.0, cost "
                    + formatPC(fillerPack.getCost()) + ")");
            System.out.println("  3 - Star Pack (1 President, rating 3.5-10.0, cost "
                    + formatPC(starPack.getCost()) + ")");
            System.out.println("  4 - Standard Pack (5 Presidents, rating 2.0-10.0, cost "
                    + formatPC(standardPack.getCost()) + ")");
            System.out.println("  5 - Elite Pack (3 Presidents, rating 4.0-10.0, cost "
                    + formatPC(elitePack.getCost()) + ")");
            System.out.println("  6 - Era Prospect Pack (5 same-era Presidents, rating 0.0-3.99, cost "
                    + formatPC(ERA_PROSPECT_PACK_COST) + ")");
            System.out.println("  0 - Back");
            System.out.print("> ");

            String input = scanner.nextLine().trim();

            if (input.equals("1")) {
                openPackFromMenu("Campaign Trail Pack", campaignTrailPack);
            } else if (input.equals("2")) {
                openPackFromMenu("Filler Pack", fillerPack);
            } else if (input.equals("3")) {
                openPackFromMenu("Star Pack", starPack);
            } else if (input.equals("4")) {
                openPackFromMenu("Standard Pack", standardPack);
            } else if (input.equals("5")) {
                openPackFromMenu("Elite Pack", elitePack);
            } else if (input.equals("6")) {
                CampaignTournament tournament = chooseTournamentFromMenu(scanner, "Choose an era for the Era Prospect Pack:");
                if (tournament != null) {
                    buyEraProspectPack(tournament);
                }
            } else if (input.equals("0") || input.equalsIgnoreCase("b") || input.equalsIgnoreCase("back")) {
                inPackMenu = false;
            } else {
                System.out.println("Invalid option. Try again.");
            }
        }
    }

    private void openPackFromMenu(String packName, CampaignPack pack) {
        buyPack(packName, pack);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        CampaignMode campaign = new CampaignMode();
        campaign.runConsoleGame(scanner);
    }
}
