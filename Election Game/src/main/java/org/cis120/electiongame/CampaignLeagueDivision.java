package org.cis120.electiongame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Static definition for one Campaign Mode league division.
 *
 * League Mode is intentionally different from Quick Play and tournaments:
 * it is a full-collection grind. The player does not build a special 10-card
 * league deck. Instead, the division they are assigned to depends on total
 * collection size, and each season uses the player's whole owned collection.
 *
 * This class stores the stable rules for a division: collection gate, season
 * length, rough CPU difficulty, per-game PC payouts, and the final reward-pack
 * tiers earned at 3+, 6+, and 9+ wins.
 */
public class CampaignLeagueDivision {

    public static final int DEFAULT_SEASON_LENGTH = 10;
    public static final double DEFAULT_CPU_MIN_RATING = 1.0;
    public static final int FIRST_REWARD_WIN_THRESHOLD = 3;
    public static final int SECOND_REWARD_WIN_THRESHOLD = 6;
    public static final int THIRD_REWARD_WIN_THRESHOLD = 9;

    private final String id;
    private final int divisionNumber;
    private final String name;
    private final int minimumCollectionSize;
    private final int seasonLength;
    private final String difficulty;
    private final double cpuMinimumRating;
    private final int winRewardPC;
    private final int lossRewardPC;
    private final List<RewardTier> rewardTiers;

    public CampaignLeagueDivision(
            String id, int divisionNumber, String name, int minimumCollectionSize,
            String difficulty, int winRewardPC, int lossRewardPC,
            RewardTier... rewardTiers
    ) {
        this(
                id, divisionNumber, name, minimumCollectionSize, DEFAULT_SEASON_LENGTH,
                difficulty, DEFAULT_CPU_MIN_RATING, winRewardPC, lossRewardPC, rewardTiers
        );
    }

    public CampaignLeagueDivision(
            String id, int divisionNumber, String name, int minimumCollectionSize,
            int seasonLength, String difficulty, double cpuMinimumRating,
            int winRewardPC, int lossRewardPC, RewardTier... rewardTiers
    ) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("League division id cannot be blank.");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("League division name cannot be blank.");
        }

        this.id = cleanId(id);
        this.divisionNumber = divisionNumber;
        this.name = name.trim();
        this.minimumCollectionSize = Math.max(0, minimumCollectionSize);
        this.seasonLength = Math.max(1, seasonLength);
        this.difficulty = normalizeDifficulty(difficulty);
        this.cpuMinimumRating = Math.max(0.0, cpuMinimumRating);
        this.winRewardPC = Math.max(0, winRewardPC);
        this.lossRewardPC = Math.max(0, lossRewardPC);

        ArrayList<RewardTier> tiers = new ArrayList<RewardTier>();
        if (rewardTiers != null) {
            for (RewardTier tier : rewardTiers) {
                if (tier != null) {
                    tiers.add(tier);
                }
            }
        }
        Collections.sort(tiers);
        this.rewardTiers = Collections.unmodifiableList(tiers);
    }

    public String getId() {
        return id;
    }

    public int getDivisionNumber() {
        return divisionNumber;
    }

    public String getName() {
        return name;
    }

    public int getMinimumCollectionSize() {
        return minimumCollectionSize;
    }

    public int getSeasonLength() {
        return seasonLength;
    }

    public String getDifficulty() {
        return difficulty;
    }

    /**
     * v13.0 design choice: every league CPU uses a broad 1.0+ card pool.
     * Division difficulty is mainly controlled by AI difficulty, not by making
     * higher divisions pull from dramatically stronger card pools.
     */
    public double getCpuMinimumRating() {
        return cpuMinimumRating;
    }

    public int getWinRewardPC() {
        return winRewardPC;
    }

    public int getLossRewardPC() {
        return lossRewardPC;
    }

    public List<RewardTier> getRewardTiers() {
        return new ArrayList<RewardTier>(rewardTiers);
    }

    public boolean isUnlockedByCollectionSize(int collectionSize) {
        return collectionSize >= minimumCollectionSize;
    }

    public RewardTier getRewardTierForWins(int wins) {
        RewardTier result = null;
        for (RewardTier tier : rewardTiers) {
            if (tier != null && wins >= tier.getWinsRequired()) {
                result = tier;
            }
        }
        return result;
    }

    public RewardTier getNextRewardTierForWins(int wins) {
        for (RewardTier tier : rewardTiers) {
            if (tier != null && wins < tier.getWinsRequired()) {
                return tier;
            }
        }
        return null;
    }

    public RewardTier getTopRewardTier() {
        if (rewardTiers.isEmpty()) {
            return null;
        }
        return rewardTiers.get(rewardTiers.size() - 1);
    }

    public String getDisplayName() {
        return "Division " + divisionNumber + " - " + name;
    }

    public String getRewardSummaryText() {
        if (rewardTiers.isEmpty()) {
            return "No final pack reward";
        }
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < rewardTiers.size(); i++) {
            if (i > 0) {
                builder.append(" | ");
            }
            RewardTier tier = rewardTiers.get(i);
            builder.append(tier.getWinsRequired()).append("+ wins: ")
                    .append(String.format("%.1f+", tier.getMinimumRating()))
                    .append(" card");
        }
        return builder.toString();
    }

    @Override
    public String toString() {
        return getDisplayName();
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof CampaignLeagueDivision)) {
            return false;
        }
        CampaignLeagueDivision that = (CampaignLeagueDivision) other;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public static List<CampaignLeagueDivision> buildDefaultDivisions() {
        ArrayList<CampaignLeagueDivision> divisions = new ArrayList<CampaignLeagueDivision>();

        divisions.add(new CampaignLeagueDivision(
                "DIVISION_5_LOCAL", 5, "Local League", 25,
                "Easy", 5000, 2000,
                new RewardTier(3, 2.0),
                new RewardTier(6, 2.5),
                new RewardTier(9, 3.0)
        ));
        divisions.add(new CampaignLeagueDivision(
                "DIVISION_4_STATE", 4, "State League", 75,
                "Easy", 8000, 3000,
                new RewardTier(3, 3.0),
                new RewardTier(6, 3.5),
                new RewardTier(9, 4.0)
        ));
        divisions.add(new CampaignLeagueDivision(
                "DIVISION_3_CONGRESSIONAL", 3, "Congressional League", 150,
                "Medium", 12000, 4000,
                new RewardTier(3, 4.0),
                new RewardTier(6, 4.5),
                new RewardTier(9, 5.0)
        ));
        divisions.add(new CampaignLeagueDivision(
                "DIVISION_2_NATIONAL", 2, "National League", 300,
                "Medium", 16000, 5000,
                new RewardTier(3, 5.0),
                new RewardTier(6, 5.5),
                new RewardTier(9, 6.0)
        ));
        divisions.add(new CampaignLeagueDivision(
                "DIVISION_1_PRESIDENTIAL", 1, "Presidential League", 500,
                "Hard", 22000, 6000,
                new RewardTier(3, 6.0),
                new RewardTier(6, 6.5),
                new RewardTier(9, 7.0)
        ));

        return Collections.unmodifiableList(divisions);
    }

    public static String cleanId(String text) {
        return text == null ? "" : text.trim().toUpperCase();
    }

    private static String normalizeDifficulty(String difficulty) {
        if (difficulty == null) {
            return "Medium";
        }
        String clean = difficulty.trim();
        if (clean.equalsIgnoreCase("Easy")) {
            return "Easy";
        }
        if (clean.equalsIgnoreCase("Hard")) {
            return "Hard";
        }
        return "Medium";
    }

    /**
     * One end-of-season reward tier. The pack itself is always one card from
     * the full candidate pool; wins only raise the minimum card rating.
     */
    public static class RewardTier implements Comparable<RewardTier> {
        private final int winsRequired;
        private final double minimumRating;

        public RewardTier(int winsRequired, double minimumRating) {
            this.winsRequired = Math.max(0, winsRequired);
            this.minimumRating = Math.max(0.0, minimumRating);
        }

        public int getWinsRequired() {
            return winsRequired;
        }

        public double getMinimumRating() {
            return minimumRating;
        }

        public String getPackName(CampaignLeagueDivision division) {
            String divisionName = division == null ? "League" : division.getName();
            return divisionName + " Reward Pack (" + String.format("%.1f+", minimumRating) + ")";
        }

        @Override
        public int compareTo(RewardTier other) {
            if (other == null) {
                return 1;
            }
            return Integer.compare(this.winsRequired, other.winsRequired);
        }
    }
}
