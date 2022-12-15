package org.cis120.electiongame;

import java.util.ArrayList;
import java.util.List;

/*
 * This is the model of my game
 */
public class ElectionGame {

    private Deck presidentDeck = new Deck();
    private Deck singlePresidentDeck = new Deck();
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
    public boolean statTesting = false;
    public boolean lastwinner = true;
    
    public String deckPreset = "standard";
    public String achievement = null;
    public String aiDifficulty = "";

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

    public boolean getLastWinner() {
    	return lastwinner;
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
    public Deck getSinglePres() {
        return singlePresidentDeck;
    }

    public Deck getPol() {
        return policyDeck;
    }

    public Election getElection() {
        return current;
    }
    
    public String getAchievement() {
        return achievement;
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
        if(statTesting) {
        	singlePresidentDeck.fillPres(CardData.getOnePresident());
        }
        electionDeck.fillElec(CardData.getElections());
        policyDeck.fillPol(CardData.getPolicies());

        presidentDeck.shuffle();
        electionDeck.shuffle();
        policyDeck.shuffle();

        player1.reset();
        player2.reset();
      
        if(!statTesting) {
        player1.drawInit(presidentDeck, policyDeck, 5);
        } else {
        	player1.drawInit(singlePresidentDeck, policyDeck, 5);
        }
        player2.drawInit(presidentDeck, policyDeck, 5);

        current = (Election) electionDeck.draw();

        message = "Starting.";

        activePlayer = player1;
        turn = true;
    }
    
    public void reset(String preset, int pres, boolean presSort, int nonpres, 
            boolean nonPresSort, int memeCards, boolean memesSort, int presCount) {
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
        if(statTesting) {
        	singlePresidentDeck.fillPres(CardData.getOnePresident());
        }
        electionDeck.fillElec(CardData.getElections());
        policyDeck.fillPol(CardData.getPolicies());

        presidentDeck.shuffle();
        electionDeck.shuffle();
        policyDeck.shuffle();

        player1.reset();
        player2.reset();

        if(!statTesting) {
            player1.drawInit(presidentDeck, policyDeck, presCount);
            } else {
            	player1.drawInit(singlePresidentDeck, policyDeck, presCount);
            }
        player2.drawInit(presidentDeck, policyDeck, presCount);

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

        player1.drawInit(presidentDeck, policyDeck, 5);
        player2.drawInit(presidentDeck, policyDeck, 5);

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
        aiDifficulty = s;
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
        reset(deckPreset, 0, true, 0, true, 0, true, 5);
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
        lastwinner = winner;
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
        
        if (winner&&c1.getName().equals("George W. Bush")&&c2.getName().equals("Al Gore")) {
            achievement = "ACHIEVEMENT: Win Florida (Defeat Al Gore with George W. Bush)";
            System.out.println(achievement);
        } else if (winner&&c1.getName().equals("Al Gore")&&c2.getName().equals("George W. Bush")) {
            achievement = "ACHIEVEMENT: Avenge 2000 (Defeat George W. Bush with Al Gore)";
            System.out.println(achievement);
        } else if (!winner&&c1.getName().equals("Donald Trump")&&c2.getName().equals("Joe Biden")) {
            achievement = "ACHIEVEMENT: STOP THE STEAL! (Lose to Joe Biden with Donald Trump)";
            System.out.println(achievement);
        } else if (winner&&c1.getName().equals("Donald Trump")&&c2.getName().equals("Joe Biden")) {
            achievement = "ACHIEVEMENT: Steal stopped (Defeat Joe Biden with Donald Trump)";
            System.out.println(achievement);
        } else if (winner&&(c1.getName().equals("George W. Bush")&&c2.getName().equals("George H.W. Bush"))
                ||(c1.getName().equals("John Quincy Adams")&&c2.getName().equals("John Adams"))
                ||(c1.getName().equals("Robert A. Taft")&&c2.getName().equals("William Howard Taft"))) {
            achievement = "ACHIEVEMENT: Take that, Dad! (Defeat a father with his son)";
            System.out.println(achievement);
        } else if (!winner&&(c1.getName().equals("George H.W. Bush")&&c2.getName().equals("George W. Bush"))
                ||(c1.getName().equals("John Quincy Adams")&&c2.getName().equals("John Adams"))
                ||(c1.getName().equals("Robert A. Taft")&&c2.getName().equals("William Howard Taft"))) {
            achievement = "ACHIEVEMENT: Respect your parents (Lose to a father with his son)";
            System.out.println(achievement);
        } else if (winner&&(c1.getName().equals("Hillary Clinton")||c1.getName().equals("Betty Ford")
                ||c1.getName().equals("Shirley Chisholm")||c1.getName().equals("Susan B. Anthony")
                ||c1.getName().equals("Sarah Palin"))) {
            achievement = "ACHIEVEMENT: Girlboss (Win with a female candidate)";
            System.out.println(achievement);
        } else if ((c1.getName().equals("William McKinley")&&c2.getName().equals("William Jennings Bryan"))
                ||(c2.getName().equals("William McKinley")&&c1.getName().equals("William Jennings Bryan"))) {
            achievement = "ACHIEVEMENT: Run it back (Have William McKinley and William Jennings Bryan run against each other";
            System.out.println(achievement);
        } else if (!winner&&c1.getName().equals("Andrew Jackson")&&(c2.getName().equals("Henry Clay")
                ||c2.getName().equals("John C. Calhoun"))) {
            achievement = "ACHIEVEMENT: 'I regret I was unable to shoot Henry Clay or to hangs John C. Calhoun "
                    + "(Lose to Henry Clay or John C. Calhoun with Andrew Jackson)";
            System.out.println(achievement);
        } else if (winner&&c1.getName().equals("Andrew Jackson")&&(p1.get(0).toString()=="Anti-Central Bank"
                ||p1.get(1).toString()=="Anti-Central Bank")) {
            achievement = "ACHIEVEMENT: I killed the Bank (Win using Andrew Jackson with Anti-Central Bank)";
            System.out.println(achievement);
        } else if (winner&&c1.getName().equals("William McKinley")&&(p1.get(0).toString()=="Tariffs"
                ||p1.get(1).toString()=="Tariffs")) {
            achievement = "ACHIEVEMENT: A tariff man through and through! (Win using William McKinley with Tariffs)";
            System.out.println(achievement);
        } else if (winner&&c1.getName().equals("Donald Trump")&&(p1.get(0).toString()=="Closed Borders"
                ||p1.get(1).toString()=="Closed Borders")) {
            achievement = "ACHIEVEMENT: Build the wall! (Win using Donald Trump with Closed Borders)";
            System.out.println(achievement);
        } else if (winner&&c1.getName().equals("John F. Kennedy")&&current.getYear()==1964) {
            achievement = "ACHIEVEMENT: Camelot lives on (Win re-election using JFK in 1964)";
            System.out.println(achievement);
        } else if (winner&&c1.getName().equals("Richard Nixon")&&current.getYear()==1976) {
            achievement = "ACHIEVEMENT: Tricky Dick (Win a third term after Watergate with Nixon in 1976)";
            System.out.println(achievement);
        } else if (winner&&c1.getName().equals("Ulysses S. Grant")&&current.getYear()==1788) {
            achievement = "ACHIEVEMENT: Wrong general! (Win with Ulysses S. Grant in America's first election)";
            System.out.println(achievement);
        } else if (winner&&c1.getName().equals("Grover Clevelan")&&current.getYear()==1888) {
            achievement = "ACHIEVEMENT: Consecutive terms (Win 1888 with Grover Cleveland)";
            System.out.println(achievement);
        } else if (winner&&c1.getName().equals("Franklin D. Roosevelt")&&current.getYear()==1948) {
            achievement = "ACHIEVEMENT: The New Deal is getting Old (Win a fifth term as FDR)";
            System.out.println(achievement);
        } else if (winner&&c1.getName().equals("Thomas Dewey")&&c2.getName().equals("Harry Truman")) {
            achievement = "ACHIEVEMENT: Dewey Defeats Truman (Defeat Harry Truman with Thomas Dewey)";
            System.out.println(achievement);
        } else if (winner&&c1.getName().equals("Harry Truman")&&c2.getName().equals("Thomas Dewey")) {
            achievement = "ACHIEVEMENT: 'Dewey Defeats Truman' (Defeat Thomas Dewey with Harry Truman)";
            System.out.println(achievement);
        } else if (winner&&c1.getName().equals("Ralph Nader")&&c2.getName().equals("Al Gore")) {
            achievement = "ACHIEVEMENT: Ultimate Spoiler (Defeat Al Gore with Ralph Nader)";
            System.out.println(achievement);
        } else if (winner&&c1.getName().equals("Joe Biden")&&current.getYear()==1788) {
            achievement = "ACHIEVEMENT: Old Man Joe (Win in 1788 with Joe Biden)";
            System.out.println(achievement);
        } else if (winner&&c1.getName().equals("William Jennings Bryan")&&(current.getYear()==1900
                ||current.getYear()==1908)) {
            achievement = "ACHIEVEMENT: If at first you don't succeed... (Win in 1900 or 1908 with William Jennings Bryan)";
            System.out.println(achievement);
        } else if (winner&&c1.getName().equals("Theodore Roosevelt")&&current.getYear()==1912) {
            achievement = "ACHIEVEMENT: It takes more than that to kill a Bull Moose (Win in 1912 with Teddy Roosevelt)";
            System.out.println(achievement);
        } else if (winner&&c1.getName().equals("Ulysses S. Grant")&&current.getYear()==1864) {
            achievement = "ACHIEVEMENT: Coup d'Etat (Win in 1864 with Ulysses S. Grant)";
            System.out.println(achievement);
        } else if (winner&&c1.getName().equals("Dwight Eisenhower")&&current.getYear()==1944) {
            achievement = "ACHIEVEMENT: Coup d'Etat (Win in 1944 with Dwight Eisenhower)";
            System.out.println(achievement);
        } else if (winner&&c2.getName().equals("Henry Clay")&&current.getYear()>1848) {
            achievement = "ACHIEVEMENT: How many times do we have to teach you this lesson old man! "
                    + "(Beat Henry Clay after his 5th presidential run in 1848)";
            System.out.println(achievement);
        } else if (winner&&c1.getName().equals("Pat Buchanan")&&c2.getName().equals("George W. Bush")) {
            achievement = "ACHIEVEMENT: Down with King George! (Defeat George W. Bush with Pat Buchanan)";
            System.out.println(achievement);
        } else if (winner&&c1.getName().equals("John C. Calhoun")&&c2.getName().equals("Andrew Jackson")) {
            achievement = "ACHIEVEMENT: Revenge for Nullifcation (Defeat Andrew Jackson with John C. Calhoun)";
            System.out.println(achievement);
        } else if (winner&&c2.getName().equals("John C. Calhoun")&&c1.getName().equals("Andrew Jackson")) {
            achievement = "ACHIEVEMENT: Nullification Crisis (Defeat John C. Calhoun with Andrew Jackson)";
            System.out.println(achievement);
        } else if (winner&&c1.getName().equals("Alexander Hamilton")&&c2.getName().equals("George Washington")) {
            achievement = "ACHIEVEMENT: Who's really in charge? (Defeat George Washington with Alexander Hamilton)";
            System.out.println(achievement);
        } else if (winner&&c1.getName().equals("Abraham Lincoln")&&c2.getName().equals("Stephen A. Douglas")) {
            achievement = "ACHIEVEMENT: Third Time's The Charm (Defeat Stephen A. Douglas with Abraham Lincoln)";
            System.out.println(achievement);
        } else if (winner&&c1.getName().equals("Dwight Eisenhower")) {
            achievement = "ACHIEVEMENT: I Like Ike (Win with Dwight Eisenhower)";
            System.out.println(achievement);
        } else if (winner&&c1.getName().equals("Martin Luther King Jr.")&&current.getYear()>1967) {
            achievement = "ACHIEVEMENT: The Good Ending (Win with MLK after 1968)";
            System.out.println(achievement);
        } else if (winner&&c1.getName().equals("Bill Clinton")&&c2.getName().equals("Hillary Clinton")) {
            achievement = "ACHIEVEMENT: Marriage Difficulty (Defeat Hillary Clinton with Bill Clinton)";
            System.out.println(achievement);
        } else if (winner&&c2.getName().equals("Bill Clinton")&&c1.getName().equals("Hillary Clinton")) {
            achievement = "ACHIEVEMENT: Revenge for Monica (Defeat Bill Clinton with Hillary Clinton)";
            System.out.println(achievement);
        } else if (winner&&c1.getName().equals("Betty Ford")&&c2.getName().equals("Gerald Ford")) {
            achievement = "ACHIEVEMENT: You're sleeping on the couch tonight (Defeat Gerald Ford with Betty Ford)";
            System.out.println(achievement);
        } else if (winner&&c1.getName().equals("Betty Ford")&&c2.getName().equals("Gerald Ford")) {
            achievement = "ACHIEVEMENT: You're sleeping on the couch tonight (Defeat Gerald Ford with Betty Ford)";
            System.out.println(achievement);
        }
        
        else {
            achievement = null;
        }

        nullifyUser();
        nullifyAI();

        //System.out.println(message);
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

    	Pack pack1 = new Pack(3, 50, CardData.calvinCoolidge,CardData.abrahamLincoln,
    			CardData.georgeWashington,CardData.georgeHWBush, CardData.alGore);
    	
    	CampaignUser kevin = new CampaignUser("Kevin");
    	System.out.println("Original Balance: " + kevin.getMoney());
    	System.out.println("BOUGHT PACK");
    	kevin.buyPack(pack1);
    	kevin.printDeck();
    	System.out.println();
    	System.out.println("New Balance: " + kevin.getMoney());
    	System.out.println("BOUGHT PACK");
    	kevin.buyPack(pack1);
    	kevin.printDeck();
    	System.out.println();
    	System.out.println("Final Balance: " + kevin.getMoney());
    	System.out.println("LAST PACK");
    	kevin.buyPack(pack1);
    	
    	/*
        // This simulates a CPU vs. CPU game
        double swing = 0;
        double con = 0;
        double prog = 0;
        double lib = 0;
        double pop = 0;
        double rounds = 0;
        double marquees = 0;
        double attributes = 0;
        int p1rdwins = 0;
        
        ElectionGame e = new ElectionGame();
        e.reset("expanded", 0, true, 0, true, 0, true);
        

        AI p1 = (AI) e.getPlayer1();
        AI p2 = (AI) e.getPlayer2();
        p1.setDifficulty(1);
        p2.setDifficulty(1);

        Deck prez = e.getPres();
        Deck singlePrez = e.getSinglePres();

        System.out.println("Policy deck size " + e.getPol().getSize());
        System.out.println(p1.toString());
        
        for (int i = 0; i < 5000; i++) {
            while (e.checkWinner() == 0) {
                //e.printGameState();
                rounds++;
                President c1 = p1.play(prez, e.getElection());
                attributes += p1.getPresAttributes();
                President c2 = p2.play(prez, e.getElection());
                if(c1.getRegion().equals(e.getElection().getRegion())) {
                    swing++;
                }
                if(c1.getIdeology().equals("Conservative")) {
                    con++;
                } else if(c1.getIdeology().equals("Libertarian")) {
                    lib++;
                } else if(c1.getIdeology().equals("Populist")) {
                    pop++;
                } else {
                    prog++;
                }
                ArrayList<Policy> pols = p1.playPolicies(c1, e.getPol());
                if(c1.isMarquee(pols.get(0))||c1.isMarquee(pols.get(1))) {
                    marquees++;
                }
                e.playRound(
                        c1, pols, c2, p2.playPolicies(c2, e.getPol())
                );
                if(e.getLastWinner()) {
                	p1rdwins++;
                }
                if (e.checkWinner() != 0) {
                    break;
                }
            }

            //System.out.println();
            if (e.checkWinner() == 1) {
                e.p1w++;                
            } else {
                e.p2w++;
            }
            
            e.reset("expanded", 0, true, 0, true, 0, true);
        }
        
        System.out.println();
        System.out.println();
        System.out.println("Win share: " + (double)p1rdwins/(double)e.rounds);
        System.out.println();
        System.out.println("Attributes Sum: " + attributes/(double)e.rounds);
        System.out.println();
        System.out.println("Player 1 av. score: " + (double)e.p1score/(double)e.rounds);
        System.out.println("Player 2 av. score: " + (double)e.p2score/(double)e.rounds);
        
        System.out.println();
        System.out.println("Player 1 wins: " + e.p1w);
        System.out.println("Player 2 wins: " + e.p2w);
        System.out.println("Swing uses: " + swing/rounds);
        System.out.println("Marquee uses: " + marquees/rounds);
        System.out.println("Conservatives: " + con/rounds);
        System.out.println("Progressives: " + prog/rounds);
        System.out.println("Libertarians: " + lib/rounds);
        System.out.println("Populists: " + pop/rounds);
        */
    }
}
