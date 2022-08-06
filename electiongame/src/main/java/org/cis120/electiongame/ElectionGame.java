package org.cis120.electiongame;

import java.util.ArrayList;
import java.util.List;

/*
 * This is the model of my game
 */
public class ElectionGame {

    private Deck presidentDeck = new Deck();
    private Deck electionDeck = new Deck();
    private Deck policyDeck = new Deck();

    // Usually when I play the game, player1 is supposed to be a Player not an AI.
    // However, to make sure the code for this file compiles, I'm leaving it at AI.
    private Player player1 = new AI("Player 1");
    private Player player2 = new AI("Player 2");
    private Election current;

    private President player1play;
    private President player2play;

    private ArrayList<Policy> pinnedPolicies1 = new ArrayList<Policy>();
    private ArrayList<Policy> pinnedPolicies2 = new ArrayList<Policy>();

    private String message;

    private Player activePlayer = player1;
    private boolean turn = true;
    
    public int p1score = 0;
    public int p2score = 0;
    public int p1w = 0;
    public int p2w = 0;
    public int rounds = 0;
    
    public String deckPreset = "standard";

    public void flipPlayer() {
        if (turn) {
            activePlayer = player2;
            turn = false;
        } else {
            activePlayer = player1;
            turn = true;
        }
    }

    public boolean getTurn() {
        return turn;
    }

    /* For testing */
    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public Deck getPres() {
        return presidentDeck;
    }

    public Deck getPol() {
        return policyDeck;
    }

    public Election getElection() {
        return current;
    }

    /* For drawing the user's deck */

    public Player getActivePlayer() {
        return activePlayer;
    }

    public List<President> userHand() {
        return player1.getHand();
    }

    public List<Policy> userPolicies() {
        return player1.getPolicies();
    }

    /**
     * reset (re-)sets the game state to start a new game.
     */
    public void reset() {
        player1play = null;
        player2play = null;

        pinnedPolicies1.clear();
        pinnedPolicies2.clear();

        pinnedPolicies1.add(null);
        pinnedPolicies1.add(null);
        pinnedPolicies2.add(null);
        pinnedPolicies2.add(null);

        presidentDeck.clear();
        electionDeck.clear();
        policyDeck.clear();

        presidentDeck.fillPres(CardData.getPresidents());
        electionDeck.fillElec(CardData.getElections());
        policyDeck.fillPol(CardData.getPolicies());

        presidentDeck.shuffle();
        electionDeck.shuffle();
        policyDeck.shuffle();

        player1.reset();
        player2.reset();

        player1.drawInit(presidentDeck, policyDeck);
        player2.drawInit(presidentDeck, policyDeck);

        current = (Election) electionDeck.draw();

        message = "Starting.";

        activePlayer = player1;
        turn = true;
    }
    
    public void reset(String preset, int pres, boolean presSort, int nonpres, 
            boolean nonPresSort, int memeCards, boolean memesSort) {
        player1play = null;
        player2play = null;

        pinnedPolicies1.clear();
        pinnedPolicies2.clear();

        pinnedPolicies1.add(null);
        pinnedPolicies1.add(null);
        pinnedPolicies2.add(null);
        pinnedPolicies2.add(null);

        presidentDeck.clear();
        electionDeck.clear();
        policyDeck.clear();

        presidentDeck.fillPres(
                CardData.getPresidents(
                        preset, pres, presSort, nonpres, nonPresSort, memeCards, memesSort
                )
        );
        electionDeck.fillElec(CardData.getElections());
        policyDeck.fillPol(CardData.getPolicies());

        presidentDeck.shuffle();
        electionDeck.shuffle();
        policyDeck.shuffle();

        player1.reset();
        player2.reset();

        player1.drawInit(presidentDeck, policyDeck);
        player2.drawInit(presidentDeck, policyDeck);

        current = (Election) electionDeck.draw();
        
        deckPreset = preset;
        message = "Starting.";

        activePlayer = player1;
        turn = true;
    }

    // This is the exact same as reset, except it doesn't shuffle, so I can use this
    // for testing.
    public void riggedReset() {
        player1play = null;
        player2play = null;

        pinnedPolicies1.clear();
        pinnedPolicies2.clear();

        pinnedPolicies1.add(null);
        pinnedPolicies1.add(null);
        pinnedPolicies2.add(null);
        pinnedPolicies2.add(null);

        presidentDeck.clear();
        electionDeck.clear();
        policyDeck.clear();

        presidentDeck.fillPres(CardData.getPresidents());
        electionDeck.fillElec(CardData.getElections());
        policyDeck.fillPol(CardData.getPolicies());

        player1.reset();
        player2.reset();

        player1.drawInit(presidentDeck, policyDeck);
        player2.drawInit(presidentDeck, policyDeck);

        current = (Election) electionDeck.draw();

        message = "Starting.";
    }

    public void namePlayer(String s) {
        player1.setName(s);
    }

    public void namePlayer2(String s) {
        player2.setName(s);
    }

    public void setAIDifficulty(String s) {
        if (s==null) {
            return;
        }
        switch (s) {
            case "Impossible":
                player2.setDifficulty(1.0);
                //System.out.println("Difficulty set: Impossible");
                break;
            case "Hard":
                player2.setDifficulty(.8);
                //System.out.println("Difficulty set: Hard");
                break;
            case "Medium":
                player2.setDifficulty(.65);
                //System.out.println("Difficulty set: Medium");
                break;
            case "Easy":
                player2.setDifficulty(.5);
                //System.out.println("Difficulty set: Easy");
                break;
            default:
                break;
        }
    }

    public String currentScore() {
        return player1.getName() + ": " + player1.getWins() + " Wins | " + player2.getName()
                + ": " + player2.getWins() + " Wins";
    }
    
    public String p1Score() {
        return player1.getName() + ": " + player1.getWins() + " Wins";
    }
    
    public String p2Score() {
        return player2.getName() + ": " + player2.getWins() + " Wins";
    }

    public String finalScore(boolean result) {
        if (result) {
            return "Final score: " + player1.getName() + ": "
                    + player1.getWins() + " Wins. " + player2.getName()
                    + ": " + player2.getWins() + " Wins";
        } else {
            return "Final score: " + player2.getName() + ": "
                    + player2.getWins() + " Wins. " + player1.getName()
                    + ": " + player1.getWins() + " Wins";
        }
    }

    /**
     * Constructor sets up game state.
     */
    public ElectionGame() {
        reset(deckPreset, 0, true, 0, true, 0, true);
    }

    // These 4 methods are about the card that the player/AI is "about" to play
    public void player1PlayCard(President c) {
        player1play = c;
    }

    public void player2PlayCard(President c) {
        player2play = c;
    }

    public void activePinCard(President c) {
        if (turn) {
            player1PlayCard(c);
        } else {
            player2PlayCard(c);
        }
    }

    public President getPlayer1Card() {
        return player1play;
    }

    public President getPlayer2Card() {
        return player2play;
    }

    public President getActivePinCard() {
        if (turn) {
            return getPlayer1Card();
        } else {
            return getPlayer2Card();
        }
    }

    public ArrayList<Policy> getPinned1() {
        return pinnedPolicies1;
    }

    public ArrayList<Policy> getPinned2() {
        return pinnedPolicies2;
    }

    public ArrayList<Policy> getActivePinnedPolicies() {
        if (turn) {
            return getPinned1();
        } else {
            return getPinned2();
        }
    }

    public Policy pinUserPolicy(Policy pol) {
        pinnedPolicies1.add(0, pol);
        Policy p = pinnedPolicies1.remove(2);
        return p;
    }
    
    public Policy pinUserPolicy(Policy pol, int pos) {
        Policy p = pinnedPolicies1.remove(pos);
        pinnedPolicies1.add(pos, pol);
        return p;
    }

    public void pinAIPolicies(ArrayList<Policy> pols) {
        pinnedPolicies2.clear();
        pinnedPolicies2.addAll(pols);
    }

    public Policy pinActivePolicy(Policy pol) {
        if (turn) {
            return pinUserPolicy(pol);
        } else {
            pinnedPolicies2.add(0, pol);
            Policy p = pinnedPolicies2.remove(2);
            return p;
        }
    }
    
    public Policy pinActivePolicy(Policy pol, int pos) {
        if (turn) {
            return pinUserPolicy(pol, pos);
        } else {
            Policy p = pinnedPolicies2.remove(pos);
            pinnedPolicies2.add(pos, pol);
            return p;
        }
    }

    public void nullifyUser() {
        pinnedPolicies1.clear();
        pinnedPolicies1.add(null);
        pinnedPolicies1.add(null);
    }

    public void nullifyAI() {
        pinnedPolicies2.clear();
        pinnedPolicies2.add(null);
        pinnedPolicies2.add(null);
    }

    public void nullifyActive() {
        if (turn) {
            nullifyUser();
        } else {
            nullifyAI();
        }
    }

    public String getMessage() {
        return message;
    }

    // Even though you also have to play policies, I left this method in for easy
    // testing.
    public void playRound(President c1, President c2) {

        if (c1 == null || c2 == null) {
            throw new IllegalArgumentException();
        }
        boolean winner = current.getWinner(c1, c2);
        if (winner) {
            message = player1.getName() + "'s " + c1.toString() + " beat " + player2.getName()
                    + "'s "
                    + c2.toString() + "! " + "Round score: " + current.getScore(c1) + " to "
                    + current.getScore(c2) + "\nKey Attributes were: "
                    + current.getAttrOne().toUpperCase()
                    + ", " + current.getAttrTwo().toUpperCase() + ".";
            player1.winRound();
        } else {
            message =

                    player2.getName() + "'s " + c2.toString() + " beat " + player1.getName() +
                            "'s "
                            + c1.toString() + "! " + "Round score: " + current.getScore(c2) + " to "
                            + current.getScore(c1) + "\nKey Attributes were: "
                            + current.getAttrOne().toUpperCase()
                            + ", " + current.getAttrTwo().toUpperCase() + ".";

            player2.winRound();
        }

        System.out.println(message);
        System.out.println();
        current = (Election) electionDeck.draw();
    }

    public void playRound(President c1, ArrayList<Policy> p1, President c2, ArrayList<Policy> p2) {

        if (c1 == null || p1.get(0) == null || p1.get(1) == null || p2.get(0) == null
                || p2.get(1) == null || c2 == null) {
            throw new IllegalArgumentException();
        }
        
        p1score += current.getScore(c1, p1.get(0), p1.get(1));
        p2score += current.getScore(c2, p2.get(0), p2.get(1));
        rounds++;

        boolean winner = current.getWinner(c1, p1.get(0), p1.get(1), c2, p2.get(0), p2.get(1));
        if (winner) {
            message = player1.getName() + "'s " + c1.toString() + " beat " + player2.getName()
                    + "'s "
                    + c2.toString() + "! \n" + player1.getName() + " played the policies "
                    + p1.get(0).toString() + " and " + p1.get(1) + " while " + player2.getName()
                    + " played " + p2.get(0) + " and " + p2.get(1) + "." + "\nRound score: "
                    + current.getScore(c1, p1.get(0), p1.get(1)) + " to "
                    + current.getScore(c2, p2.get(0), p2.get(1)) + "\nKey Attributes were: "
                    + current.getAttrOne().toUpperCase()
                    + ", " + current.getAttrTwo().toUpperCase() + "." + "\nKey Issues were: "
                    + current.getMain() + " (Main), " + current.getSide1() + ", and "
                    + current.getSide2() + ".";
            player1.winRound();
        } else {
            message = player2.getName() + "'s " + c2.toString() + " beat " + player1.getName()
                    + "'s "
                    + c1.toString() + "! \n" + player2.getName() + " played the policies "
                    + p2.get(0).toString() + " and " + p2.get(1) + " while " + player1.getName()
                    + " played " + p1.get(0) + " and " + p1.get(1) + "." + "\nRound score: "
                    + current.getScore(c2, p2.get(0), p2.get(1)) + " to "
                    + current.getScore(c1, p1.get(0), p1.get(1)) + "\nKey Attributes were: "
                    + current.getAttrOne().toUpperCase()
                    + ", " + current.getAttrTwo().toUpperCase() + "." + "\nKey Issues were: "
                    + current.getMain() + " (Main), " + current.getSide1() + ", and "
                    + current.getSide2() + ".";

            player2.winRound();
        }

        nullifyUser();
        nullifyAI();

        System.out.println(message);
        current = (Election) electionDeck.draw();
    }

    /**
     * checkWinner checks whether the game has reached a win condition.
     *
     * @return 0 if nobody has won yet, 1 if player 1 has won, and 2 if player 2
     *         has won.
     */
    public int checkWinner() {
        if (player1.getWins() >= 3) {
            return 1;
        } else if (player2.getWins() >= 3) {
            return 2;
        } else {
            return 0;
        }
    }

    /**
     * printGameState prints the current game state
     * for debugging. I also kept this in the real game just for fun
     * so I can look back at the results of the game
     * 
     * @return
     */
    public void printGameState() {
        System.out.println();
        System.out.println(current.toString());
        System.out.println(player1.toString());
        System.out.println(player2.toString());
    }

    public static void main(String[] args) {

        // This simulates a CPU vs. CPU game. I
        // made this simulate a ton of games before with new and old methods
        // to test the AI and see if my "new" implementations performed better
        // than my "old" implementations of the AI
        ElectionGame e = new ElectionGame();

        

        AI p1 = (AI) e.getPlayer1();
        AI p2 = (AI) e.getPlayer2();
        p1.setDifficulty(.8);
        p2.setDifficulty(.75);

        Deck prez = e.getPres();

        System.out.println("Policy deck size " + e.getPol().getSize());
        
        for (int i = 0; i < 1000; i++) {
            while (e.checkWinner() == 0) {
                //e.printGameState();
                President c1 = p1.play(prez, e.getElection());
                President c2 = p2.play(prez, e.getElection());
                e.playRound(
                        c1, p1.playPolicies(c1, e.getPol()), c2, p2.playPolicies(c2, e.getPol())
                );
                if (e.checkWinner() != 0) {
                    break;
                }
            }

            System.out.println();
            if (e.checkWinner() == 1) {
                /*
                System.out.println(
                        p1.getName() + " has won! Final score: " + p1.getWins() + " to "
                                + p2.getWins()
                );
                */
                e.p1w++;                
            } else {
                /*
                System.out.println(
                        p2.getName() + " has won! Final score: " + p2.getWins() + " to "
                                + p1.getWins()
                );
                */
                e.p2w++;
            }
            
           e.reset();
        }
        
        System.out.println();
        System.out.println("Player 1 av. score: " + (double)e.p1score/(double)e.rounds);
        System.out.println("Player 2 av. score: " + (double)e.p2score/(double)e.rounds);
        
        System.out.println();
        System.out.println("Player 1 wins: " + e.p1w);
        System.out.println("Player 2 wins: " + e.p2w);
    }
}
