package org.cis120.electiongame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Stores the player's persistent Campaign Mode progress.
 *
 * This class should stay UI-free: no Swing, no console input, no pack logic.
 * CampaignMode can use this object to ask, "what does the player have?" and
 * to make controlled changes like adding PC or adding a President card.
 */
public class CampaignPlayer {

    private static final int ACTIVE_DECK_SIZE = 10;
    private static final int STARTING_HAND_SIZE = 5;

    private String name;
    private int pc;
    private int wins;
    private int losses;
    private List<President> presidentCollection;
    private List<President> activeDeck;

    /**
     * Creates a campaign player with 0 starting PC.
     */
    public CampaignPlayer(String name) {
        this(name, 0);
    }

    /**
     * Creates a campaign player with the requested starting PC.
     */
    public CampaignPlayer(String name, int startingPC) {
        this.name = cleanName(name);
        this.pc = Math.max(0, startingPC);
        this.wins = 0;
        this.losses = 0;
        this.presidentCollection = new ArrayList<President>();
        this.activeDeck = new ArrayList<President>();
    }

    /*** BASIC GETTERS / SETTERS ***/

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = cleanName(name);
    }

    public int getPC() {
        return pc;
    }

    /**
     * Backward-compatible alias for older CampaignMode versions.
     */
    public int getMoney() {
        return getPC();
    }

    /**
     * Returns a defensive copy so outside classes cannot accidentally mutate the
     * collection without going through CampaignPlayer methods.
     */
    public List<President> getPresidentCollection() {
        return new ArrayList<President>(presidentCollection);
    }

    public int getCollectionSize() {
        return presidentCollection.size();
    }

    /*** PC METHODS ***/

    public void addPC(int amount) {
        if (amount <= 0) {
            return;
        }
        pc += amount;
    }

    /**
     * Backward-compatible alias for older CampaignMode versions.
     */
    public void addMoney(int amount) {
        addPC(amount);
    }

    /**
     * Attempts to spend PC. Returns true if successful and false if the player
     * cannot afford it.
     */
    public boolean spendPC(int amount) {
        if (amount < 0) {
            return false;
        }
        if (pc < amount) {
            return false;
        }
        pc -= amount;
        return true;
    }

    /**
     * Backward-compatible alias for older CampaignMode versions.
     */
    public boolean spendMoney(int amount) {
        return spendPC(amount);
    }

    /**
     * Useful for loading save files or test setup.
     */
    public void setPC(int pc) {
        this.pc = Math.max(0, pc);
    }

    /**
     * Backward-compatible alias for older CampaignMode versions.
     */
    public void setMoney(int money) {
        setPC(money);
    }


    /*** RECORD / MATCH HISTORY METHODS ***/

    public int getWins() {
        return wins;
    }

    public int getLosses() {
        return losses;
    }

    public int getTotalMatches() {
        return wins + losses;
    }

    public double getWinPercentage() {
        int totalMatches = getTotalMatches();
        if (totalMatches == 0) {
            return 0.0;
        }
        return (wins * 100.0) / totalMatches;
    }

    public void recordWin() {
        wins++;
    }

    public void recordLoss() {
        losses++;
    }

    public void recordMatch(boolean won) {
        if (won) {
            recordWin();
        } else {
            recordLoss();
        }
    }

    /**
     * Useful for loading save files or resetting campaign test state.
     */
    public void setRecord(int wins, int losses) {
        this.wins = Math.max(0, wins);
        this.losses = Math.max(0, losses);
    }

    public String getRecordSummary() {
        return wins + "-" + losses + " (" + String.format("%.1f", getWinPercentage()) + "%)";
    }

    /*** PRESIDENT COLLECTION METHODS ***/

    /**
     * Adds a President to the collection.
     *
     * Returns true if the card was new. Returns false if the player already owned
     * the card or if the card was invalid.
     */
    public boolean addPresident(President president) {
        if (president == null) {
            return false;
        }
        if (ownsPresident(president)) {
            return false;
        }

        presidentCollection.add(president);
        sortPresidentCollectionByAverage();
        updateActiveDeck();
        return true;
    }

    /**
     * Adds several Presidents and returns how many were actually new.
     */
    public int addPresidents(List<President> presidents) {
        if (presidents == null) {
            return 0;
        }

        int newCards = 0;
        for (President president : presidents) {
            if (addPresident(president)) {
                newCards++;
            }
        }
        return newCards;
    }

    public boolean ownsPresident(President president) {
        if (president == null) {
            return false;
        }

        for (President owned : presidentCollection) {
            if (samePresident(owned, president)) {
                return true;
            }
        }
        return false;
    }

    public boolean ownsPresident(String presidentName) {
        if (presidentName == null) {
            return false;
        }

        for (President owned : presidentCollection) {
            if (owned != null && presidentName.equalsIgnoreCase(owned.getName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Default Campaign Mode collection order: strongest weighted-average cards first.
     *
     * This mutates the internal collection order on purpose. That way every later
     * collection display, save file, or deck-building screen sees the same stable
     * rating-sorted order without needing to re-sort every time.
     */
    public void sortPresidentCollectionByAverage() {
        Collections.sort(presidentCollection, presidentAverageComparator());
    }

    /**
     * Backward-compatible alias. PC value is currently a monotonic function of
     * weighted average, but the gameplay-facing default sort is now weighted average.
     */
    public void sortPresidentCollectionByValue() {
        sortPresidentCollectionByAverage();
    }

    public void sortPresidentCollectionByName() {
        Collections.sort(presidentCollection, new Comparator<President>() {
            @Override
            public int compare(President p1, President p2) {
                String name1 = p1 == null ? "" : p1.getName();
                String name2 = p2 == null ? "" : p2.getName();
                return name1.compareToIgnoreCase(name2);
            }
        });
        updateActiveDeck();
    }

    /**
     * Simple console-friendly collection display for CampaignMode.main testing.
     */
    public String getCollectionSummary() {
        StringBuilder summary = new StringBuilder();
        summary.append(name).append("'s President Collection (")
                .append(presidentCollection.size()).append(" cards):");

        if (presidentCollection.isEmpty()) {
            summary.append("\n  [empty]");
            return summary.toString();
        }

        for (President president : presidentCollection) {
            summary.append("\n  - ").append(president.toString())
                    .append(" | Avg: ")
                    .append(String.format("%.2f", president.getAv()));
        }
        return summary.toString();
    }

    public void printCollection() {
        System.out.println(getCollectionSummary());
    }

    /**
     * Clears the collection. Mostly for tests / resetting campaign state.
     */
    public void clearPresidentCollection() {
        presidentCollection.clear();
        updateActiveDeck();
    }

    /*** ACTIVE DECK METHODS ***/

    /**
     * For now, the active deck is auto-built from the player's best 10 Presidents.
     * Later, this can become manually editable once Campaign Mode has deck-building.
     */
    private void updateActiveDeck() {
        activeDeck.clear();

        List<President> bestPresidents = getBestPresidents(ACTIVE_DECK_SIZE);
        activeDeck.addAll(bestPresidents);
    }

    /**
     * Returns a defensive copy of the current active deck.
     */
    public List<President> getActiveDeck() {
        return new ArrayList<President>(activeDeck);
    }

    public int getActiveDeckSize() {
        return activeDeck.size();
    }

    public int getActiveDeckMaxSize() {
        return ACTIVE_DECK_SIZE;
    }

    /**
     * Returns the best N Presidents owned, sorted by weighted average descending.
     */
    public List<President> getBestPresidents(int count) {
        List<President> sorted = new ArrayList<President>(presidentCollection);
        Collections.sort(sorted, presidentAverageComparator());

        int resultSize = Math.min(Math.max(0, count), sorted.size());
        return new ArrayList<President>(sorted.subList(0, resultSize));
    }

    /**
     * First-pass Campaign Mode deck strength.
     *
     * This makes stars matter most, but also gives value to depth:
     *   30% = average of best 5 cards
     *   70% = average of full active deck, up to 10 cards
     */
    public double getActiveDeckStrength() {
        if (activeDeck.isEmpty()) {
            return 0.0;
        }

        double topFiveAverage = averagePresidentRating(activeDeck, Math.min(STARTING_HAND_SIZE, activeDeck.size()));
        double fullDeckAverage = averagePresidentRating(activeDeck, activeDeck.size());

        return (0.30 * topFiveAverage) + (0.70 * fullDeckAverage);
    }

    /**
     * Shorter alias for future CampaignMode W/L code.
     */
    public double getDeckStrength() {
        return getActiveDeckStrength();
    }

    public String getActiveDeckSummary() {
        StringBuilder summary = new StringBuilder();
        summary.append(name).append("'s Active Deck (")
                .append(activeDeck.size()).append("/")
                .append(ACTIVE_DECK_SIZE).append("):");

        if (activeDeck.isEmpty()) {
            summary.append("\n  [empty]");
            return summary.toString();
        }

        for (President president : activeDeck) {
            summary.append("\n  - ").append(president.toString())
                    .append(" | Avg: ")
                    .append(String.format("%.2f", president.getAv()));
        }

        summary.append("\nDeck Strength: ")
                .append(String.format("%.2f", getActiveDeckStrength()));

        return summary.toString();
    }

    public void printActiveDeck() {
        System.out.println(getActiveDeckSummary());
    }

    @Override
    public String toString() {
        return "CampaignPlayer{" +
                "name='" + name + '\'' +
                ", pc=" + pc +
                ", presidentCollection=" + presidentCollection.size() + " cards" +
                ", activeDeck=" + activeDeck.size() + "/" + ACTIVE_DECK_SIZE +
                ", record=" + wins + "-" + losses +
                '}';
    }

    /*** PRIVATE HELPERS ***/

    private String cleanName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return "Player";
        }
        return name.trim();
    }

    /**
     * Avoid relying on object identity. This will still work later if cards are
     * rebuilt from CSV/save files as fresh President objects.
     */
    private boolean samePresident(President p1, President p2) {
        if (p1 == null || p2 == null) {
            return false;
        }
        return p1.getName().equalsIgnoreCase(p2.getName());
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
}
