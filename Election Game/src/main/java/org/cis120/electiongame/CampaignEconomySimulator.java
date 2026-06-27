package org.cis120.electiongame;

import java.util.ArrayList;
import java.util.List;

/**
 * Console-only Campaign Mode economy simulator.
 *
 * v2.4: adds Campaign Trail and Star Pack strategies, while returning output
 * to one benchmark column for the selected benchmark mode.
 *
 * This does NOT save/load or mutate the real campaign save file. It creates fresh
 * CampaignPlayer objects in memory and runs headless simulated campaigns.
 */
public class CampaignEconomySimulator {

    private static final int STARTING_POLITICAL_CAPITAL = 100000;

    private static final double DUPLICATE_SALE_PERCENT = 0.25;

    private static final int SIMS_PER_STRATEGY = 250;
    private static final int MAX_GAMES = 200;
    private static final int[] CHECKPOINTS = { 10, 25, 50, 100, 150, 200 };

    // Benchmark matches do not award PC. They estimate win rate at each checkpoint.
    private static final int BENCHMARK_MATCHES_PER_CHECKPOINT = 50;

    // Estimated real play time for menu flow + match flow.
    private static final int MINUTES_PER_GAME = 15;

    // Campaign sim assumes the human is approximated by Medium AI.
    private static final double PLAYER_AI_DIFFICULTY = 0.65;

    // Escalation thresholds for normal/base-game play.
    private static final double EASY_UNTIL_DECK_STRENGTH = 4.0;
    private static final double MEDIUM_UNTIL_DECK_STRENGTH = 6.0;

    private enum Strategy {
        CAMPAIGN_TRAIL_ONLY("Always buy Campaign Trail Pack"),
        STAR_ONLY("Always buy Star Pack"),
        STANDARD_ONLY("Always buy Standard Pack"),
        ELITE_ONLY("Always buy Elite Pack");

        private String label;

        Strategy(String label) {
            this.label = label;
        }

        public String getLabel() {
            return label;
        }
    }

    private enum MatchTier {
        EASY("Easy", 0.50, 0.0, 65000, 25000),
        MEDIUM("Medium", 0.65, 3.0, 95000, 30000),
        HARD("Hard", 0.80, 5.0, 185000, 35000);

        private String label;
        private double cpuAIDifficulty;
        private double cpuMinRating;
        private int winRewardPC;
        private int lossRewardPC;

        MatchTier(
                String label, double cpuAIDifficulty, double cpuMinRating,
                int winRewardPC, int lossRewardPC
        ) {
            this.label = label;
            this.cpuAIDifficulty = cpuAIDifficulty;
            this.cpuMinRating = cpuMinRating;
            this.winRewardPC = winRewardPC;
            this.lossRewardPC = lossRewardPC;
        }

        public String getLabel() {
            return label;
        }

        public double getCpuAIDifficulty() {
            return cpuAIDifficulty;
        }

        public double getCpuMinRating() {
            return cpuMinRating;
        }

        public int getWinRewardPC() {
            return winRewardPC;
        }

        public int getLossRewardPC() {
            return lossRewardPC;
        }
    }

    public static void main(String[] args) {
        System.out.println("Campaign Clash - Economy Simulator");
        System.out.println("Sims per strategy: " + SIMS_PER_STRATEGY);
        System.out.println("Campaign games per sim: " + MAX_GAMES);
        System.out.println("Benchmark matches per checkpoint: " + BENCHMARK_MATCHES_PER_CHECKPOINT);
        System.out.println("Estimated play time: " + MINUTES_PER_GAME + " minutes per game");
        System.out.println("Player pilot: Medium AI (" + PLAYER_AI_DIFFICULTY + ")");
        System.out.println("Opponent escalation:");
        System.out.println("  Easy until deck strength < " + String.format("%.2f", EASY_UNTIL_DECK_STRENGTH)
                + " | CPU full pool | win " + formatPC(MatchTier.EASY.getWinRewardPC())
                + " / loss " + formatPC(MatchTier.EASY.getLossRewardPC()));
        System.out.println("  Medium until deck strength < " + String.format("%.2f", MEDIUM_UNTIL_DECK_STRENGTH)
                + " | CPU 3.0+ pool | win " + formatPC(MatchTier.MEDIUM.getWinRewardPC())
                + " / loss " + formatPC(MatchTier.MEDIUM.getLossRewardPC()));
        System.out.println("  Hard after that | CPU 5.0+ pool | win "
                + formatPC(MatchTier.HARD.getWinRewardPC())
                + " / loss " + formatPC(MatchTier.HARD.getLossRewardPC()));
        System.out.println();

        List<President> allPresidents = CardData.getPresidents("full", null, 0.0);

        runStrategy(Strategy.CAMPAIGN_TRAIL_ONLY, allPresidents);
        System.out.println();
        runStrategy(Strategy.STAR_ONLY, allPresidents);
        System.out.println();
        runStrategy(Strategy.STANDARD_ONLY, allPresidents);
        System.out.println();
        runStrategy(Strategy.ELITE_ONLY, allPresidents);
    }

    private static void runStrategy(Strategy strategy, List<President> allPresidents) {
        List<SimulationResult> results = new ArrayList<SimulationResult>();

        for (int i = 0; i < SIMS_PER_STRATEGY; i++) {
            results.add(runOneSimulation(strategy, allPresidents, i + 1));
        }

        printStrategySummary(strategy, results);
    }

    private static SimulationResult runOneSimulation(
            Strategy strategy, List<President> allPresidents, int simNumber
    ) {
        CampaignPlayer player = createFreshPlayer("Sim " + simNumber, allPresidents);
        CampaignPack targetPack = createTargetPack(strategy, allPresidents);

        SimulationResult result = new SimulationResult(strategy);

        int wins = 0;
        int losses = 0;
        int easyGames = 0;
        int mediumGames = 0;
        int hardGames = 0;
        int checkpointIndex = 0;

        buyTargetPackUntilUnaffordable(player, targetPack);

        for (int game = 1; game <= MAX_GAMES; game++) {
            buyTargetPackUntilUnaffordable(player, targetPack);

            MatchTier matchTier = chooseMatchTier(player.getActiveDeckStrength());
            if (matchTier == MatchTier.EASY) {
                easyGames++;
            } else if (matchTier == MatchTier.MEDIUM) {
                mediumGames++;
            } else {
                hardGames++;
            }

            boolean won = simulateCampaignMatch(player.getActiveDeck(), allPresidents, matchTier);
            if (won) {
                wins++;
                player.addPC(matchTier.getWinRewardPC());
            } else {
                losses++;
                player.addPC(matchTier.getLossRewardPC());
            }

            // "As soon as available" means spend new PC immediately after the match too.
            buyTargetPackUntilUnaffordable(player, targetPack);

            if (checkpointIndex < CHECKPOINTS.length && game == CHECKPOINTS[checkpointIndex]) {
                MatchTier checkpointTier = chooseMatchTier(player.getActiveDeckStrength());
                double actualWinPct = getWinPct(wins, losses);
                double benchmarkWinPct = runBenchmarkWinPct(
                        player.getActiveDeck(), allPresidents, checkpointTier
                );

                result.addCheckpoint(new CheckpointSnapshot(
                        game,
                        estimatedHours(game),
                        actualWinPct,
                        benchmarkWinPct,
                        player.getActiveDeckStrength(),
                        player.getCollectionSize(),
                        easyGames,
                        mediumGames,
                        hardGames,
                        checkpointTier
                ));

                checkpointIndex++;
            }
        }

        return result;
    }

    private static MatchTier chooseMatchTier(double activeDeckStrength) {
        if (activeDeckStrength < EASY_UNTIL_DECK_STRENGTH) {
            return MatchTier.EASY;
        }
        if (activeDeckStrength < MEDIUM_UNTIL_DECK_STRENGTH) {
            return MatchTier.MEDIUM;
        }
        return MatchTier.HARD;
    }

    private static int estimatedHours(int games) {
        return (int) Math.round((games * MINUTES_PER_GAME) / 60.0);
    }

    private static CampaignPlayer createFreshPlayer(String playerName, List<President> allPresidents) {
        CampaignPlayer player = new CampaignPlayer(playerName, 0);
        player.addPC(STARTING_POLITICAL_CAPITAL);

        CampaignPack starterElitePack = new CampaignPack(1, 0, 5.0, 10.0, allPresidents, null);
        CampaignPack starterMidPack = new CampaignPack(3, 0, 2.5, 4.0, allPresidents, null);
        CampaignPack starterPack = new CampaignPack(6, 0, 0.0, 2.5, allPresidents, null);

        openPackSilently(player, starterElitePack);
        openPackSilently(player, starterMidPack);
        openPackSilently(player, starterPack);

        return player;
    }

    private static CampaignPack createTargetPack(Strategy strategy, List<President> allPresidents) {
        if (strategy == Strategy.CAMPAIGN_TRAIL_ONLY) {
            // Campaign Trail Pack: 1 card, 90k PC, any rating.
            return new CampaignPack(1, 90000, 0.0, 10.0, allPresidents, null);
        }

        if (strategy == Strategy.STAR_ONLY) {
            // Star Pack: 1 card, 200k PC, 3.5+ rating.
            return new CampaignPack(1, 200000, 3.5, 10.0, allPresidents, null);
        }

        if (strategy == Strategy.STANDARD_ONLY) {
            // Standard Pack: 5 cards, 400k PC, 2.0+ rating.
            return new CampaignPack(5, 400000, 2.0, 10.0, allPresidents, null);
        }

        if (strategy == Strategy.ELITE_ONLY) {
            // Elite Pack: 3 cards, 700k PC, 4.0+ rating.
            return new CampaignPack(3, 700000, 4.0, 10.0, allPresidents, null);
        }

        throw new IllegalArgumentException("Unknown strategy: " + strategy);
    }

    private static void buyTargetPackUntilUnaffordable(CampaignPlayer player, CampaignPack pack) {
        while (player.getPC() >= pack.getCost()) {
            openPackSilently(player, pack);
        }
    }

    private static void openPackSilently(CampaignPlayer player, CampaignPack pack) {
        if (player == null || pack == null) {
            return;
        }
        if (!player.spendPC(pack.getCost())) {
            return;
        }

        List<President> pulls = pack.pull();
        for (President president : pulls) {
            if (!player.addPresident(president)) {
                player.addPC(getDuplicateSaleValue(president));
            }
        }
    }

    private static int getDuplicateSaleValue(President president) {
        if (president == null) {
            return 0;
        }

        int rawValue = (int) Math.max(0, president.getValue() * DUPLICATE_SALE_PERCENT);
        if (rawValue == 0) {
            return 0;
        }

        return Math.max(1000, roundToNearest(rawValue, 1000));
    }

    private static int roundToNearest(int value, int nearest) {
        if (nearest <= 0) {
            return value;
        }
        return (int) Math.round(value / (double) nearest) * nearest;
    }

    private static double getWinPct(int wins, int losses) {
        int total = wins + losses;
        if (total == 0) {
            return 0.0;
        }
        return (wins * 100.0) / total;
    }

    private static double runBenchmarkWinPct(
            List<President> activeDeck, List<President> allPresidents, MatchTier matchTier
    ) {
        int benchmarkWins = 0;

        for (int i = 0; i < BENCHMARK_MATCHES_PER_CHECKPOINT; i++) {
            if (simulateCampaignMatch(activeDeck, allPresidents, matchTier)) {
                benchmarkWins++;
            }
        }

        return (benchmarkWins * 100.0) / BENCHMARK_MATCHES_PER_CHECKPOINT;
    }

    /**
     * Runs one actual best-of-five ElectionGame match.
     *
     * Player side uses Medium AI and the current active deck. CPU side uses the
     * tier's AI difficulty and filtered CPU card pool.
     */
    private static boolean simulateCampaignMatch(
            List<President> activeDeck, List<President> allPresidents, MatchTier matchTier
    ) {
        ElectionGame match = new ElectionGame();
        match.reset("full", null, 0.0, 5);
        match.namePlayer("Campaign Player");
        match.namePlayer2(matchTier.getLabel() + " CPU");
        match.setAIDifficulty(matchTier.getLabel());

        AI playerAI = (AI) match.getPlayer1();
        AI cpuAI = (AI) match.getPlayer2();
        playerAI.setDifficulty(PLAYER_AI_DIFFICULTY);
        cpuAI.setDifficulty(matchTier.getCpuAIDifficulty());

        Deck playerPresidentDeck = buildPresidentDeck(activeDeck);
        Deck cpuPresidentDeck = buildPresidentDeck(
                filterPresidentsByMinRating(allPresidents, matchTier.getCpuMinRating())
        );
        Deck policyDeck = buildPolicyDeck();

        playerAI.reset();
        cpuAI.reset();
        playerAI.drawInit(playerPresidentDeck, policyDeck, 5);
        cpuAI.drawInit(cpuPresidentDeck, policyDeck, 5);

        while (match.checkWinner() == 0) {
            President playerCard = playerAI.play(playerPresidentDeck, match.getElection());
            President cpuCard = cpuAI.play(cpuPresidentDeck, match.getElection());

            ArrayList<Policy> playerPolicies = playerAI.playPolicies(playerCard, policyDeck);
            ArrayList<Policy> cpuPolicies = cpuAI.playPolicies(cpuCard, policyDeck);

            match.playRound(playerCard, playerPolicies, cpuCard, cpuPolicies);

            if (match.checkWinner() == 0) {
                match.finishRound();
            }
        }

        return match.checkWinner() == 1;
    }

    private static List<President> filterPresidentsByMinRating(
            List<President> allPresidents, double minRating
    ) {
        List<President> filtered = new ArrayList<President>();
        if (allPresidents == null) {
            return filtered;
        }

        for (President president : allPresidents) {
            if (president != null && president.getAv() >= minRating) {
                filtered.add(president);
            }
        }

        // Safety fallback so simulator never crashes from an accidentally empty CPU pool.
        if (filtered.isEmpty()) {
            filtered.addAll(allPresidents);
        }
        return filtered;
    }

    private static Deck buildPresidentDeck(List<President> presidents) {
        Deck deck = new Deck();
        deck.fillPres(presidents);
        deck.shuffle();
        return deck;
    }

    private static Deck buildPolicyDeck() {
        Deck deck = new Deck();
        deck.fillPol(CardData.getPolicies());
        deck.shuffle();
        return deck;
    }

    private static void printStrategySummary(Strategy strategy, List<SimulationResult> results) {
        System.out.println("Strategy: " + strategy.getLabel());
        System.out.println(
                "Games | Hours | E/M/H Games | Actual Win% | Benchmark Win% | "
                        + "Bench Mode | Deck Strength | Collection"
        );
        System.out.println(
                "------------------------------------------------------------------------------------------------"
        );

        for (int i = 0; i < CHECKPOINTS.length; i++) {
            double actualWinTotal = 0.0;
            double benchmarkWinTotal = 0.0;
            double deckStrengthTotal = 0.0;
            double collectionTotal = 0.0;
            double easyGamesTotal = 0.0;
            double mediumGamesTotal = 0.0;
            double hardGamesTotal = 0.0;
            int easyModeCount = 0;
            int mediumModeCount = 0;
            int hardModeCount = 0;
            int count = 0;

            int games = CHECKPOINTS[i];
            int hours = estimatedHours(games);

            for (SimulationResult result : results) {
                if (i < result.getCheckpoints().size()) {
                    CheckpointSnapshot snapshot = result.getCheckpoints().get(i);
                    actualWinTotal += snapshot.getActualWinPct();
                    benchmarkWinTotal += snapshot.getBenchmarkWinPct();
                    deckStrengthTotal += snapshot.getDeckStrength();
                    collectionTotal += snapshot.getCollectionSize();
                    easyGamesTotal += snapshot.getEasyGames();
                    mediumGamesTotal += snapshot.getMediumGames();
                    hardGamesTotal += snapshot.getHardGames();

                    if (snapshot.getCheckpointTier() == MatchTier.EASY) {
                        easyModeCount++;
                    } else if (snapshot.getCheckpointTier() == MatchTier.MEDIUM) {
                        mediumModeCount++;
                    } else {
                        hardModeCount++;
                    }
                    count++;
                }
            }

            if (count > 0) {
                System.out.println(String.format(
                        "%5d | %5d | %4.1f/%4.1f/%4.1f | %10.1f%% | %13.1f%% | %10s | %13.2f | %10.1f",
                        games,
                        hours,
                        easyGamesTotal / count,
                        mediumGamesTotal / count,
                        hardGamesTotal / count,
                        actualWinTotal / count,
                        benchmarkWinTotal / count,
                        majorityTierLabel(easyModeCount, mediumModeCount, hardModeCount),
                        deckStrengthTotal / count,
                        collectionTotal / count
                ));
            }
        }
    }

    private static String majorityTierLabel(int easyModeCount, int mediumModeCount, int hardModeCount) {
        if (hardModeCount >= mediumModeCount && hardModeCount >= easyModeCount) {
            return "Hard";
        }
        if (mediumModeCount >= easyModeCount && mediumModeCount >= hardModeCount) {
            return "Medium";
        }
        return "Easy";
    }

    private static String formatPC(int amount) {
        return String.format("%,d PC", amount);
    }

    private static class SimulationResult {
        private Strategy strategy;
        private List<CheckpointSnapshot> checkpoints;

        private SimulationResult(Strategy strategy) {
            this.strategy = strategy;
            this.checkpoints = new ArrayList<CheckpointSnapshot>();
        }

        private void addCheckpoint(CheckpointSnapshot snapshot) {
            checkpoints.add(snapshot);
        }

        private List<CheckpointSnapshot> getCheckpoints() {
            return checkpoints;
        }

        @SuppressWarnings("unused")
        private Strategy getStrategy() {
            return strategy;
        }
    }

    private static class CheckpointSnapshot {
        private int games;
        private int hours;
        private double actualWinPct;
        private double benchmarkWinPct;
        private double deckStrength;
        private int collectionSize;
        private int easyGames;
        private int mediumGames;
        private int hardGames;
        private MatchTier checkpointTier;

        private CheckpointSnapshot(
                int games,
                int hours,
                double actualWinPct,
                double benchmarkWinPct,
                double deckStrength,
                int collectionSize,
                int easyGames,
                int mediumGames,
                int hardGames,
                MatchTier checkpointTier
        ) {
            this.games = games;
            this.hours = hours;
            this.actualWinPct = actualWinPct;
            this.benchmarkWinPct = benchmarkWinPct;
            this.deckStrength = deckStrength;
            this.collectionSize = collectionSize;
            this.easyGames = easyGames;
            this.mediumGames = mediumGames;
            this.hardGames = hardGames;
            this.checkpointTier = checkpointTier;
        }

        @SuppressWarnings("unused")
        private int getGames() {
            return games;
        }

        @SuppressWarnings("unused")
        private int getHours() {
            return hours;
        }

        private double getActualWinPct() {
            return actualWinPct;
        }

        private double getBenchmarkWinPct() {
            return benchmarkWinPct;
        }

        private double getDeckStrength() {
            return deckStrength;
        }

        private int getCollectionSize() {
            return collectionSize;
        }

        private int getEasyGames() {
            return easyGames;
        }

        private int getMediumGames() {
            return mediumGames;
        }

        private int getHardGames() {
            return hardGames;
        }

        private MatchTier getCheckpointTier() {
            return checkpointTier;
        }
    }

}
