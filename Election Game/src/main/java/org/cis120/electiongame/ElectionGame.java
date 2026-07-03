package org.cis120.electiongame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

/*
 * This is the model of my game
 */
public class ElectionGame {

    private Deck presidentDeck = new Deck();
    private Deck singlePresidentDeck = new Deck();

    // v06 bridge support: Campaign Mode needs exact player/CPU President decks.
    // Classic mode can still use presidentDeck as the old shared President deck.
    private Deck player1PresidentDeck = new Deck();
    private Deck player2PresidentDeck = new Deck();

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

    // v06 bridge support. These fields are only set while a Campaign Mode
    // playable match is using this ElectionGame instance.
    private boolean campaignMatchMode = false;
    private CampaignMatchConfig campaignMatchConfig = null;
    
    public int p1score = 0;
    public int p2score = 0;
    public int p1w = 0;
    public int p2w = 0;
    public int rounds = 0;
    public boolean statTesting = false;
    public boolean lastwinner = true;
    public boolean winner = true;
    
    public String deckPreset = "standard";
    public String achievement = null;
    public String aiDifficulty = "";
    
    public String hint = "";
    public int hintIndex = 1;
    
    public String getHint() {
    	switch (hintIndex) {
		case 1:
			hint += "Hint 1: One attribute is " + current.getAttrOne().toUpperCase() + ".";
			hintIndex++;
			break;
		case 2:
			hint += "\nHint 2: One policy set is " + current.getSide1()+ ".";
			hintIndex++;
			break;
		case 3:
			hint += "\nHint 3: Another attribute is " + current.getAttrTwo().toUpperCase() + ".";
			hintIndex++;
			break;
		case 4:
			hint += "\nHint 4: Another policy set is " + current.getSide2()+ ".";
			hintIndex++;
			break;
		case 5:
			hint += "\nHint 5: The main policy is " + current.getMain()+ ".";
			hintIndex++;
			break;
		default:
			return hint;
    	}
    	return hint;
    }

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
    
    public boolean getWinner() {
    	return winner;
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

    /**
     * v06 campaign bridge getter. In normal Classic Mode this returns the same
     * shared President deck as getPres(). In Campaign Match Mode it returns the
     * exact player-side deck supplied by CampaignMode.
     */
    public Deck getPlayer1PresidentDeck() {
        if (campaignMatchMode) {
            return player1PresidentDeck;
        }
        return presidentDeck;
    }

    /**
     * v06 campaign bridge getter. In normal Classic Mode this returns the same
     * shared President deck as getPres(). In Campaign Match Mode it returns the
     * exact CPU-side deck supplied by CampaignMode.
     */
    public Deck getPlayer2PresidentDeck() {
        if (campaignMatchMode) {
            return player2PresidentDeck;
        }
        return presidentDeck;
    }

    public Deck getActivePresidentDeck() {
        if (turn) {
            return getPlayer1PresidentDeck();
        }
        return getPlayer2PresidentDeck();
    }

    public boolean isCampaignMatchMode() {
        return campaignMatchMode;
    }

    public CampaignMatchConfig getCampaignMatchConfig() {
        return campaignMatchConfig;
    }

    public int getPlayer1RoundsWon() {
        return player1.getWins();
    }

    public int getPlayer2RoundsWon() {
        return player2.getWins();
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
    	campaignMatchMode = false;
    	campaignMatchConfig = null;
    	hint = "";
    	hintIndex = 1;
    	
        player1play = null;
        player2play = null;

        pinnedPolicies1.clear();
        pinnedPolicies2.clear();

        pinnedPolicies1.add(null);
        pinnedPolicies1.add(null);
        pinnedPolicies2.add(null);
        pinnedPolicies2.add(null);

        presidentDeck.clear();
        singlePresidentDeck.clear();
        player1PresidentDeck.clear();
        player2PresidentDeck.clear();
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
    
    
    /**
     * This is the correct method to use
     */
    public void reset(String preset, String[] tags, double minRate, int presCount) {
    	campaignMatchMode = false;
    	campaignMatchConfig = null;
    	hint = "";
    	hintIndex = 1;
    	
        player1play = null;
        player2play = null;

        pinnedPolicies1.clear();
        pinnedPolicies2.clear();

        pinnedPolicies1.add(null);
        pinnedPolicies1.add(null);
        pinnedPolicies2.add(null);
        pinnedPolicies2.add(null);

        presidentDeck.clear();
        singlePresidentDeck.clear();
        player1PresidentDeck.clear();
        player2PresidentDeck.clear();
        electionDeck.clear();
        policyDeck.clear();

        presidentDeck.fillPres(
                CardData.getPresidents(
                        preset, tags, minRate
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

    /**
     * v06 Campaign Mode bridge reset.
     *
     * This prepares the normal ElectionGame model for a playable Campaign Mode
     * match using exact President decks supplied by CampaignMode. It does not
     * apply rewards or update campaign progress; it only sets up the playable
     * match state. RunElectionGameCombined will later call this before showing
     * the board, and then return a CampaignMatchResult when the match ends.
     */
    public void resetForCampaignMatch(CampaignMatchConfig config) {
        if (config == null) {
            throw new IllegalArgumentException("Campaign match config cannot be null.");
        }

        String validationError = config.getValidationError();
        if (validationError != null && !validationError.isEmpty()) {
            throw new IllegalArgumentException(validationError);
        }

        resetWithCustomPresidentDecks(
                config.getPlayerPresidentDeck(),
                config.getCpuPresidentDeck(),
                config.getStartingHandSize(),
                config
        );

        campaignMatchMode = true;
        campaignMatchConfig = config;
        deckPreset = "campaign";

        namePlayer(config.getPlayerName());
        namePlayer2(config.getCpuName());
        setAIDifficulty(config.getDifficulty());

        message = "Starting " + config.getMatchTitle() + ".";
    }

    /**
     * Low-level exact-deck reset for future bridge/testing uses.
     *
     * Normal reset(String, ...) is still the right method for Classic Mode. This
     * one exists so Campaign Mode can say: "use exactly these 10 player cards"
     * and "use exactly this CPU pool" without going through preset/tag filters.
     */
    public void resetWithCustomPresidentDecks(
            List<President> player1Presidents, List<President> player2Presidents,
            int presCount
    ) {
        resetWithCustomPresidentDecks(player1Presidents, player2Presidents, presCount, null);
    }

    private void resetWithCustomPresidentDecks(
            List<President> player1Presidents, List<President> player2Presidents,
            int presCount, CampaignMatchConfig config
    ) {
        validateCustomPresidentDecks(player1Presidents, player2Presidents, presCount);

        hint = "";
        hintIndex = 1;

        player1play = null;
        player2play = null;

        pinnedPolicies1.clear();
        pinnedPolicies2.clear();

        pinnedPolicies1.add(null);
        pinnedPolicies1.add(null);
        pinnedPolicies2.add(null);
        pinnedPolicies2.add(null);

        presidentDeck.clear();
        singlePresidentDeck.clear();
        player1PresidentDeck.clear();
        player2PresidentDeck.clear();
        electionDeck.clear();
        policyDeck.clear();

        // Legacy fallback deck: future v06 RunElectionGameCombined should use
        // getPlayer1PresidentDeck() and getPlayer2PresidentDeck(), but keeping a
        // merged shared deck here makes old/debug callers less fragile.
        presidentDeck.fillPres(buildMergedPresidentList(player1Presidents, player2Presidents));
        player1PresidentDeck.fillPres(new ArrayList<President>(player1Presidents));
        player2PresidentDeck.fillPres(new ArrayList<President>(player2Presidents));

        electionDeck.fillElec(getCampaignElectionDeckForConfig(config));
        policyDeck.fillPol(CardData.getPolicies());

        presidentDeck.shuffle();
        player1PresidentDeck.shuffle();
        player2PresidentDeck.shuffle();
        electionDeck.shuffle();
        policyDeck.shuffle();

        player1.reset();
        player2.reset();

        player1.drawInit(player1PresidentDeck, policyDeck, presCount);
        player2.drawInit(player2PresidentDeck, policyDeck, presCount);

        current = (Election) electionDeck.draw();

        message = "Starting.";
        activePlayer = player1;
        turn = true;

        campaignMatchMode = false;
        campaignMatchConfig = null;
    }


    private List<Election> getCampaignElectionDeckForConfig(CampaignMatchConfig config) {
        ArrayList<Election> allElections = new ArrayList<Election>(CardData.getElections());
        if (config == null || !config.shouldUseEraElectionDeck()) {
            return allElections;
        }

        ArrayList<Election> filteredElections = new ArrayList<Election>();
        int startYear = config.getElectionStartYear();
        int endYear = config.getElectionEndYear();

        for (Election election : allElections) {
            if (election == null) {
                continue;
            }
            int year = election.getYear();
            if (year >= startYear && year <= endYear) {
                filteredElections.add(election);
            }
        }

        // Safety fallback: if a thin era does not have enough election cards yet,
        // use the normal election deck rather than risking an empty/too-small game.
        int minimumUsableElections = Math.max(5, config.getStartingHandSize());
        if (filteredElections.size() < minimumUsableElections) {
            return allElections;
        }
        return filteredElections;
    }

    private void validateCustomPresidentDecks(
            List<President> player1Presidents, List<President> player2Presidents,
            int presCount
    ) {
        if (presCount <= 0) {
            throw new IllegalArgumentException("Starting hand size must be positive.");
        }
        if (player1Presidents == null || player1Presidents.size() < presCount) {
            int size = player1Presidents == null ? 0 : player1Presidents.size();
            throw new IllegalArgumentException("Player deck has only " + size
                    + " cards; at least " + presCount + " are required.");
        }
        if (player2Presidents == null || player2Presidents.size() < presCount) {
            int size = player2Presidents == null ? 0 : player2Presidents.size();
            throw new IllegalArgumentException("CPU deck has only " + size
                    + " cards; at least " + presCount + " are required.");
        }
    }

    private List<President> buildMergedPresidentList(
            List<President> player1Presidents, List<President> player2Presidents
    ) {
        ArrayList<President> merged = new ArrayList<President>();
        addUniquePresidents(merged, player1Presidents);
        addUniquePresidents(merged, player2Presidents);
        return merged;
    }

    private void addUniquePresidents(List<President> target, List<President> source) {
        if (target == null || source == null) {
            return;
        }

        for (President president : source) {
            if (president == null || containsPresidentByName(target, president)) {
                continue;
            }
            target.add(president);
        }
    }

    private boolean containsPresidentByName(List<President> presidents, President target) {
        if (presidents == null || target == null || target.getName() == null) {
            return false;
        }

        for (President president : presidents) {
            if (president != null && president.getName() != null
                    && president.getName().equalsIgnoreCase(target.getName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Helper for the future RunElectionGameCombined bridge. Classic mode does not
     * need this, but Campaign Mode can use it to turn the current ElectionGame
     * winner state into the standard bridge result object.
     */
    public CampaignMatchResult buildCampaignMatchResult(boolean resigned) {
        if (campaignMatchConfig == null) {
            return CampaignMatchResult.error(null, "No campaign match config is active.");
        }

        int playerRoundsWon = player1.getWins();
        int cpuRoundsWon = player2.getWins();

        if (resigned) {
            return CampaignMatchResult.resigned(
                    campaignMatchConfig,
                    playerRoundsWon,
                    cpuRoundsWon,
                    finalScore(false)
            );
        }

        int winnerId = checkWinner();
        boolean playerWon = winnerId == 1;
        return CampaignMatchResult.completed(
                campaignMatchConfig,
                playerWon,
                playerRoundsWon,
                cpuRoundsWon,
                finalScore(playerWon)
        );
    }

    // This is the exact same as reset, except it doesn't shuffle, so I can use this
    // for testing.
    public void riggedReset() {
        campaignMatchMode = false;
        campaignMatchConfig = null;

        player1play = null;
        player2play = null;

        pinnedPolicies1.clear();
        pinnedPolicies2.clear();

        pinnedPolicies1.add(null);
        pinnedPolicies1.add(null);
        pinnedPolicies2.add(null);
        pinnedPolicies2.add(null);

        presidentDeck.clear();
        singlePresidentDeck.clear();
        player1PresidentDeck.clear();
        player2PresidentDeck.clear();
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
    
    public void nameActivePlayer(String s) {
    	activePlayer.setName(s);
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
        return player1.getName() + ": " + "<br>" + player1.getWins() + " Wins";
    }
    
    public String p2Score() {
        return player2.getName() + ": " + "<br>" + player2.getWins() + " Wins";
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
        reset(deckPreset, null, 0.0, 5);
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
    
    public void rigElection(Election year) {
    	current = year;
    }

    public void playRound(President c1, ArrayList<Policy> p1, President c2, ArrayList<Policy> p2) {

        if (c1 == null || p1.get(0) == null || p1.get(1) == null || p2.get(0) == null
                || p2.get(1) == null || c2 == null) {
            throw new IllegalArgumentException();
        }
        
        hint = "";
        hintIndex = 1;
        
        p1score += current.getScore(c1, p1.get(0), p1.get(1));
        p2score += current.getScore(c2, p2.get(0), p2.get(1));
        
		double p1pct = (double)(Math.round(((float) (current.getScore(c1, p1.get(0), p1.get(1))) * 100.0)
				/ (current.getScore(c2, p2.get(0), p2.get(1)) + current.getScore(c1, p1.get(0), p1.get(1)))*100))/100.0;
		double p2pct = 100 - p1pct;
		rounds++;     
        
		winner = current.getWinner(c1, p1.get(0), p1.get(1), c2, p2.get(0), p2.get(1));
		lastwinner = winner;
		if (winner) {
			message = player1.getName() + "'s " + c1.toString() + " beat " + player2.getName() + "'s " + c2.toString()
					+ "! \n\n" + player1.getName() + " played the policies " + p1.get(0).toString() + " and " + p1.get(1)
					+ " while " + player2.getName() + " played " + p2.get(0) + " and " + p2.get(1) + "."
					+ "\nRound score: " + current.getScore(c1, p1.get(0), p1.get(1)) + " to "
					+ current.getScore(c2, p2.get(0), p2.get(1)) + "\n\nKey Attributes were: "
					+ current.getAttrOne().toUpperCase() + ", " + current.getAttrTwo().toUpperCase() + "."
					+ "\nKey Issues were: " + current.getMain() + " (Main), " + current.getSide1() + ", and "
					+ current.getSide2() + ".";
			player1.winRound();
		} else {
			message = player2.getName() + "'s " + c2.toString() + " beat " + player1.getName() + "'s " + c1.toString()
					+ "! \n\n" + player2.getName() + " played the policies " + p2.get(0).toString() + " and " + p2.get(1)
					+ " while " + player1.getName() + " played " + p1.get(0) + " and " + p1.get(1) + "."
					+ "\nRound score: " + current.getScore(c2, p2.get(0), p2.get(1)) + " to "
					+ current.getScore(c1, p1.get(0), p1.get(1)) + "\n\nKey Attributes were: "
					+ current.getAttrOne().toUpperCase() + ", " + current.getAttrTwo().toUpperCase() + "."
					+ "\nKey Issues were: " + current.getMain() + " (Main), " + current.getSide1() + ", and "
					+ current.getSide2() + ".";
			;

			player2.winRound();
		}
		
		/*
		 * Final vote count code if I want to bring it back:
		  "\nFinal vote count: "
					+ c1.toString().replaceAll("Jr.", "").replaceAll("\\d", "").replaceAll("^.*?(\\w+)\\W*$", "$1")
					+ " " + p1pct + "% to "
					+ c2.toString().replaceAll("Jr.", "").replaceAll("\\d", "").replaceAll("^.*?(\\w+)\\W*$", "$1")
					+ " " + p2pct + "%.";
		 * 
		 */
        
		/*
        if (winner&&c1.getName().equals("George W. Bush")&&c2.getName().equals("Al Gore")) {
            achievement = "ACHIEVEMENT: Win Florida (Defeat Al Gore with George W. Bush)";
            //System.out.println(achievement);
        } else if (winner&&c1.getName().equals("Al Gore")&&c2.getName().equals("George W. Bush")) {
            achievement = "ACHIEVEMENT: Avenge 2000 (Defeat George W. Bush with Al Gore)";
            //System.out.println(achievement);
        } else if (!winner&&c1.getName().equals("Donald Trump")&&c2.getName().equals("Joe Biden")) {
            achievement = "ACHIEVEMENT: STOP THE STEAL! (Lose to Joe Biden with Donald Trump)";
            //System.out.println(achievement);
        } else if (winner&&c1.getName().equals("Donald Trump")&&c2.getName().equals("Joe Biden")) {
            achievement = "ACHIEVEMENT: Steal stopped (Defeat Joe Biden with Donald Trump)";
            //System.out.println(achievement);
        } else if (winner&&(c1.getName().equals("George W. Bush")&&c2.getName().equals("George H.W. Bush"))
                ||(c1.getName().equals("John Quincy Adams")&&c2.getName().equals("John Adams"))
                ||(c1.getName().equals("Robert A. Taft")&&c2.getName().equals("William Howard Taft"))) {
            achievement = "ACHIEVEMENT: Take that, Dad! (Defeat a father with his son)";
            //System.out.println(achievement);
        } else if (!winner&&(c1.getName().equals("George H.W. Bush")&&c2.getName().equals("George W. Bush"))
                ||(c1.getName().equals("John Quincy Adams")&&c2.getName().equals("John Adams"))
                ||(c1.getName().equals("Robert A. Taft")&&c2.getName().equals("William Howard Taft"))) {
            achievement = "ACHIEVEMENT: Respect your parents (Lose to a father with his son)";
            //System.out.println(achievement);
        } else if (winner&&(c1.getName().equals("Hillary Clinton")||c1.getName().equals("Betty Ford")
                ||c1.getName().equals("Shirley Chisholm")||c1.getName().equals("Susan B. Anthony")
                ||c1.getName().equals("Sarah Palin"))) {
            achievement = "ACHIEVEMENT: Girlboss (Win with a female candidate)";
            //System.out.println(achievement);
        } else if ((c1.getName().equals("William McKinley")&&c2.getName().equals("William Jennings Bryan"))
                ||(c2.getName().equals("William McKinley")&&c1.getName().equals("William Jennings Bryan"))) {
            achievement = "ACHIEVEMENT: Run it back (Have William McKinley and William Jennings Bryan run against each other";
            //System.out.println(achievement);
        } else if (!winner&&c1.getName().equals("Andrew Jackson")&&(c2.getName().equals("Henry Clay")
                ||c2.getName().equals("John C. Calhoun"))) {
            achievement = "ACHIEVEMENT: 'I regret I was unable to shoot Henry Clay or to hangs John C. Calhoun "
                    + "(Lose to Henry Clay or John C. Calhoun with Andrew Jackson)";
            //System.out.println(achievement);
        } else if (winner&&c1.getName().equals("Andrew Jackson")&&(p1.get(0).toString()=="Anti-Central Bank"
                ||p1.get(1).toString()=="Anti-Central Bank")) {
            achievement = "ACHIEVEMENT: I killed the Bank (Win using Andrew Jackson with Anti-Central Bank)";
            //System.out.println(achievement);
        } else if (winner&&c1.getName().equals("William McKinley")&&(p1.get(0).toString()=="Tariffs"
                ||p1.get(1).toString()=="Tariffs")) {
            achievement = "ACHIEVEMENT: A tariff man through and through! (Win using William McKinley with Tariffs)";
            //System.out.println(achievement);
        } else if (winner&&c1.getName().equals("Donald Trump")&&(p1.get(0).toString()=="Closed Borders"
                ||p1.get(1).toString()=="Closed Borders")) {
            achievement = "ACHIEVEMENT: Build the wall! (Win using Donald Trump with Closed Borders)";
            //System.out.println(achievement);
        } else if (winner&&c1.getName().equals("John F. Kennedy")&&current.getYear()==1964) {
            achievement = "ACHIEVEMENT: Camelot lives on (Win re-election using JFK in 1964)";
            //System.out.println(achievement);
        } else if (winner&&c1.getName().equals("Richard Nixon")&&current.getYear()==1976) {
            achievement = "ACHIEVEMENT: Tricky Dick (Win a third term after Watergate with Nixon in 1976)";
            //System.out.println(achievement);
        } else if (winner&&c1.getName().equals("Ulysses S. Grant")&&current.getYear()==1788) {
            achievement = "ACHIEVEMENT: Wrong general! (Win with Ulysses S. Grant in America's first election)";
            //System.out.println(achievement);
        } else if (winner&&c1.getName().equals("Grover Clevelan")&&current.getYear()==1888) {
            achievement = "ACHIEVEMENT: Consecutive terms (Win 1888 with Grover Cleveland)";
            //System.out.println(achievement);
        } else if (winner&&c1.getName().equals("Franklin D. Roosevelt")&&current.getYear()==1948) {
            achievement = "ACHIEVEMENT: The New Deal is getting Old (Win a fifth term as FDR)";
            //System.out.println(achievement);
        } else if (winner&&c1.getName().equals("Thomas Dewey")&&c2.getName().equals("Harry Truman")) {
            achievement = "ACHIEVEMENT: Dewey Defeats Truman (Defeat Harry Truman with Thomas Dewey)";
            //System.out.println(achievement);
        } else if (winner&&c1.getName().equals("Harry Truman")&&c2.getName().equals("Thomas Dewey")) {
            achievement = "ACHIEVEMENT: 'Dewey Defeats Truman' (Defeat Thomas Dewey with Harry Truman)";
            //System.out.println(achievement);
        } else if (winner&&c1.getName().equals("Ralph Nader")&&c2.getName().equals("Al Gore")) {
            achievement = "ACHIEVEMENT: Ultimate Spoiler (Defeat Al Gore with Ralph Nader)";
            //System.out.println(achievement);
        } else if (winner&&c1.getName().equals("Joe Biden")&&current.getYear()==1788) {
            achievement = "ACHIEVEMENT: Old Man Joe (Win in 1788 with Joe Biden)";
            //System.out.println(achievement);
        } else if (winner&&c1.getName().equals("William Jennings Bryan")&&(current.getYear()==1900
                ||current.getYear()==1908)) {
            achievement = "ACHIEVEMENT: If at first you don't succeed... (Win in 1900 or 1908 with William Jennings Bryan)";
            //System.out.println(achievement);
        } else if (winner&&c1.getName().equals("Theodore Roosevelt")&&current.getYear()==1912) {
            achievement = "ACHIEVEMENT: It takes more than that to kill a Bull Moose (Win in 1912 with Teddy Roosevelt)";
            //System.out.println(achievement);
        } else if (winner&&c1.getName().equals("Ulysses S. Grant")&&current.getYear()==1864) {
            achievement = "ACHIEVEMENT: Coup d'Etat (Win in 1864 with Ulysses S. Grant)";
            //System.out.println(achievement);
        } else if (winner&&c1.getName().equals("Dwight Eisenhower")&&current.getYear()==1944) {
            achievement = "ACHIEVEMENT: Coup d'Etat (Win in 1944 with Dwight Eisenhower)";
            //System.out.println(achievement);
        } else if (winner&&c2.getName().equals("Henry Clay")&&current.getYear()>1848) {
            achievement = "ACHIEVEMENT: How many times do we have to teach you this lesson old man! "
                    + "(Beat Henry Clay after his 5th presidential run in 1848)";
            //System.out.println(achievement);
        } else if (winner&&c1.getName().equals("Pat Buchanan")&&c2.getName().equals("George W. Bush")) {
            achievement = "ACHIEVEMENT: Down with King George! (Defeat George W. Bush with Pat Buchanan)";
            //System.out.println(achievement);
        } else if (winner&&c1.getName().equals("John C. Calhoun")&&c2.getName().equals("Andrew Jackson")) {
            achievement = "ACHIEVEMENT: Revenge for Nullifcation (Defeat Andrew Jackson with John C. Calhoun)";
            //System.out.println(achievement);
        } else if (winner&&c2.getName().equals("John C. Calhoun")&&c1.getName().equals("Andrew Jackson")) {
            achievement = "ACHIEVEMENT: Nullification Crisis (Defeat John C. Calhoun with Andrew Jackson)";
            //System.out.println(achievement);
        } else if (winner&&c1.getName().equals("Alexander Hamilton")&&c2.getName().equals("George Washington")) {
            achievement = "ACHIEVEMENT: Who's really in charge? (Defeat George Washington with Alexander Hamilton)";
            //System.out.println(achievement);
        } else if (winner&&c1.getName().equals("Abraham Lincoln")&&c2.getName().equals("Stephen A. Douglas")) {
            achievement = "ACHIEVEMENT: Third Time's The Charm (Defeat Stephen A. Douglas with Abraham Lincoln)";
            //System.out.println(achievement);
        } else if (winner&&c1.getName().equals("Dwight Eisenhower")) {
            achievement = "ACHIEVEMENT: I Like Ike (Win with Dwight Eisenhower)";
            //System.out.println(achievement);
        } else if (winner&&c1.getName().equals("Martin Luther King Jr.")&&current.getYear()>1967) {
            achievement = "ACHIEVEMENT: The Good Ending (Win with MLK after 1968)";
            //System.out.println(achievement);
        } else if (winner&&c1.getName().equals("Bill Clinton")&&c2.getName().equals("Hillary Clinton")) {
            achievement = "ACHIEVEMENT: Marriage Difficulty (Defeat Hillary Clinton with Bill Clinton)";
            //System.out.println(achievement);
        } else if (winner&&c2.getName().equals("Bill Clinton")&&c1.getName().equals("Hillary Clinton")) {
            achievement = "ACHIEVEMENT: Revenge for Monica (Defeat Bill Clinton with Hillary Clinton)";
            //System.out.println(achievement);
        } else if (winner&&c1.getName().equals("Betty Ford")&&c2.getName().equals("Gerald Ford")) {
            achievement = "ACHIEVEMENT: You're sleeping on the couch tonight (Defeat Gerald Ford with Betty Ford)";
            //System.out.println(achievement);
        } else if (winner&&c1.getName().equals("Betty Ford")&&c2.getName().equals("Gerald Ford")) {
            achievement = "ACHIEVEMENT: You're sleeping on the couch tonight (Defeat Gerald Ford with Betty Ford)";
            //System.out.println(achievement);
        }
        
        
        else {
            achievement = null;
        }
        */
    }
        
    // Make sure to always do this in playRound if testing in this file
    public void finishRound() {

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
        runCampaignMultiContextMarginalCardStudy();
    }

    /*** V10 ECONOMY / MULTI-CONTEXT MARGINAL CARD FULL-MATCH STUDY HELPERS ***/

    private static final int MULTI_CONTEXT_MATCHES_PER_ROW = 10000;
    private static final int MULTI_CONTEXT_DECK_SIZE = 10;
    private static final int MULTI_CONTEXT_BASELINE_CARDS = 9;
    private static final int MULTI_CONTEXT_STARTING_HAND_SIZE = 5;
    private static final double MULTI_CONTEXT_PLAYER_AI_DIFFICULTY = 0.65;

    private static final double EASY_CONTEXT_MIN_RATING = 1.0;
    private static final double MEDIUM_CONTEXT_MIN_RATING = 3.0;
    private static final double HARD_CONTEXT_MIN_RATING = 5.0;

    private static final double EASY_CONTEXT_CPU_AI_DIFFICULTY = 0.50;
    private static final double MEDIUM_CONTEXT_CPU_AI_DIFFICULTY = 0.65;
    private static final double HARD_CONTEXT_CPU_AI_DIFFICULTY = 0.80;

    /**
     * V10 economy test harness.
     *
     * Goal: estimate the full-match marginal value of adding one extra card of a
     * given rating bucket across three Campaign Mode contexts:
     *   Easy context   = baseline/CPU decks sampled from 1.0+ pool, CPU AI 0.50
     *   Medium context = baseline/CPU decks sampled from 3.0+ pool, CPU AI 0.65
     *   Hard context   = baseline/CPU decks sampled from 5.0+ pool, CPU AI 0.80
     *
     * The player pilot always uses 0.65 AI difficulty. Each context includes a
     * control row where both sides receive 10 random context-pool cards, then
     * bucket rows where the player receives 9 context-pool cards plus one marked
     * marginal card from the bucket.
     */
    private static void runCampaignMultiContextMarginalCardStudy() {
        long startMillis = System.currentTimeMillis();
        List<President> allPresidents = CardData.getPresidents("full", null, 0.0);
        List<RatingBucket> buckets = buildMarginalRatingBuckets();
        List<MarginalStudyContext> contexts = buildMarginalStudyContexts();
        ArrayList<MarginalCardStudyResult> allResults = new ArrayList<MarginalCardStudyResult>();

        System.out.println("CAMPAIGN CLASH V10 MULTI-CONTEXT MARGINAL CARD FULL-MATCH STUDY");
        System.out.println("Starting sim...");
        System.out.println("Matches per row: " + MULTI_CONTEXT_MATCHES_PER_ROW);
        System.out.println("Deck size per side: " + MULTI_CONTEXT_DECK_SIZE);
        System.out.println("Starting hand size: " + MULTI_CONTEXT_STARTING_HAND_SIZE);
        System.out.println("Player AI difficulty: " + MULTI_CONTEXT_PLAYER_AI_DIFFICULTY);
        System.out.println("Contexts: Easy 1.0+ / CPU AI 0.50, Medium 3.0+ / CPU AI 0.65, Hard 5.0+ / CPU AI 0.80");
        System.out.println("Bucket rows: 9 context-pool cards + 1 marked marginal card.");
        System.out.println("Control row per context: 10 context-pool cards vs 10 context-pool cards.");
        System.out.println("Buckets: 1.0-1.5, 1.5-2.0, ... , 5.5-6.0, 6.0-7.0 inclusive");
        System.out.println();
        System.out.flush();

        for (MarginalStudyContext context : contexts) {
            List<President> contextPool = CardData.getPresidents(
                    "full", null, context.minRating
            );
            int belowMinCount = countPresidentsBelowRating(contextPool, context.minRating);

            System.out.println("CONTEXT_START: " + context.contextName);
            System.out.println("  Difficulty label: " + context.difficultyLabel);
            System.out.println("  Context pool: CardData.getPresidents(\"full\", null, "
                    + formatDouble(context.minRating) + ") returned "
                    + contextPool.size() + " cards");
            System.out.println("  Cards below min rating in returned pool: " + belowMinCount
                    + (belowMinCount == 0 ? " (OK)" : " (WARNING - CardData minRate may still be broken)"));
            System.out.println("  Player AI: " + MULTI_CONTEXT_PLAYER_AI_DIFFICULTY
                    + " | CPU AI: " + context.cpuAiDifficulty);
            System.out.flush();

            if (contextPool.size() < MULTI_CONTEXT_DECK_SIZE) {
                System.out.println("  Skipping context: not enough cards to build 10-card decks.");
                System.out.println();
                continue;
            }

            MarginalCardStudyResult controlResult = runMarginalStudyRow(
                    context,
                    "CONTROL_" + context.contextName.toUpperCase() + "_RANDOM_10TH",
                    null,
                    contextPool,
                    contextPool,
                    true,
                    startMillis
            );
            controlResult.deltaWinPctVsControl = 0.0;
            controlResult.deltaEloPowerVsControl = 0.0;
            allResults.add(controlResult);

            for (RatingBucket bucket : buckets) {
                List<President> marginalPool = filterPresidentsByBucket(allPresidents, bucket);
                MarginalCardStudyResult rowResult = runMarginalStudyRow(
                        context,
                        bucket.label,
                        marginalPool,
                        contextPool,
                        contextPool,
                        false,
                        startMillis
                );
                rowResult.bucketMinRating = bucket.minRating;
                rowResult.bucketMaxRating = bucket.maxRating;
                rowResult.bucketInclusiveMax = bucket.inclusiveMax;
                rowResult.deltaWinPctVsControl = rowResult.getPlayerMatchWinRate()
                        - controlResult.getPlayerMatchWinRate();
                rowResult.deltaEloPowerVsControl = rowResult.getEloPower()
                        - controlResult.getEloPower();
                allResults.add(rowResult);
            }

            System.out.println("CONTEXT_DONE: " + context.contextName
                    + " | Elapsed: " + formatMillis(System.currentTimeMillis() - startMillis));
            System.out.println();
            System.out.flush();
        }

        System.out.println("All contexts finished. Printing final multi-context marginal-card CSV summary...");
        printMarginalStudySummary(allResults, startMillis);
        System.out.println();
        System.out.println("Runtime: " + formatMillis(System.currentTimeMillis() - startMillis));
        System.out.println("Done.");
    }

    private static MarginalCardStudyResult runMarginalStudyRow(
            MarginalStudyContext context,
            String rowLabel,
            List<President> marginalPool,
            List<President> basePool,
            List<President> cpuPool,
            boolean controlRow,
            long globalStartMillis
    ) {
        MarginalCardStudyResult result = new MarginalCardStudyResult(context, rowLabel, controlRow);
        result.basePoolSize = basePool == null ? 0 : basePool.size();
        result.cpuPoolSize = cpuPool == null ? 0 : cpuPool.size();
        result.marginalEligibleCards = marginalPool == null ? 0 : marginalPool.size();

        if (controlRow) {
            System.out.println("  ROW_START: " + rowLabel + " | control 10-card context deck");
        } else {
            System.out.println("  ROW_START: " + rowLabel + " | marginal eligible cards: "
                    + result.marginalEligibleCards);
        }
        System.out.flush();

        if (!controlRow && (marginalPool == null || marginalPool.isEmpty())) {
            System.out.println("  ROW_SKIP: " + rowLabel + " | no eligible marginal cards");
            return result;
        }
        if (basePool == null || basePool.size() < MULTI_CONTEXT_BASELINE_CARDS) {
            System.out.println("  ROW_SKIP: " + rowLabel + " | not enough baseline cards");
            return result;
        }
        if (cpuPool == null || cpuPool.size() < MULTI_CONTEXT_DECK_SIZE) {
            System.out.println("  ROW_SKIP: " + rowLabel + " | not enough CPU cards");
            return result;
        }

        long rowStartMillis = System.currentTimeMillis();

        for (int matchNumber = 1; matchNumber <= MULTI_CONTEXT_MATCHES_PER_ROW; matchNumber++) {
            President marginalCard = null;
            List<President> playerDeck;
            if (controlRow) {
                playerDeck = samplePresidents(basePool, MULTI_CONTEXT_DECK_SIZE);
            } else {
                marginalCard = sampleOnePresident(marginalPool);
                playerDeck = buildMarginalPlayerDeck(basePool, marginalCard);
            }
            List<President> cpuDeck = samplePresidents(cpuPool, MULTI_CONTEXT_DECK_SIZE);

            result.totalPlayerDeckRating += averagePresidentRating(playerDeck);
            result.totalPlayerTopFiveRating += averagePresidentRating(
                    rankPresidentsByRating(playerDeck).subList(0, Math.min(5, playerDeck.size()))
            );
            result.totalCpuDeckRating += averagePresidentRating(cpuDeck);
            result.totalCpuTopFiveRating += averagePresidentRating(
                    rankPresidentsByRating(cpuDeck).subList(0, Math.min(5, cpuDeck.size()))
            );

            if (marginalCard != null) {
                result.totalMarginalRating += marginalCard.getAv();
                result.totalMarginalCurrentValue += marginalCard.getValue();
                result.totalMarginalFullDeckRank += getRankInFullDeck(
                        rankPresidentsByRating(playerDeck), marginalCard
                );
            }

            CampaignMatchConfig config = CampaignMatchConfig.quickPlay(
                    "Player",
                    context.difficultyLabel,
                    playerDeck,
                    cpuDeck
            ).withExecutionMode(CampaignMatchConfig.EXECUTION_MODE_SIMULATED);

            ElectionGame game = new ElectionGame();
            game.resetForCampaignMatch(config);

            AI playerAI = (AI) game.getPlayer1();
            AI cpuAI = (AI) game.getPlayer2();
            playerAI.setDifficulty(MULTI_CONTEXT_PLAYER_AI_DIFFICULTY);
            cpuAI.setDifficulty(context.cpuAiDifficulty);

            if (marginalCard != null && handContainsPresident(playerAI.getHand(), marginalCard)) {
                result.marginalStartedInOpeningHand++;
            }

            boolean marginalAvailableThisMatch = false;
            boolean marginalPlayedThisMatch = false;
            int roundsThisMatch = 0;

            while (game.checkWinner() == 0) {
                roundsThisMatch++;

                Deck playerPresidentDeck = game.getPlayer1PresidentDeck();
                Deck cpuPresidentDeck = game.getPlayer2PresidentDeck();
                Deck policyDeck = game.getPol();

                if (marginalCard != null && handContainsPresident(playerAI.getHand(), marginalCard)) {
                    marginalAvailableThisMatch = true;
                    result.marginalAvailableBeforeRoundCount++;
                }

                President playerCard = playerAI.play(playerPresidentDeck, game.getElection());
                President cpuCard = cpuAI.play(cpuPresidentDeck, game.getElection());

                ArrayList<Policy> playerPolicies = playerAI.playPolicies(playerCard, policyDeck);
                ArrayList<Policy> cpuPolicies = cpuAI.playPolicies(cpuCard, policyDeck);

                int playerScore = game.getElection().getScore(
                        playerCard, playerPolicies.get(0), playerPolicies.get(1)
                );
                int cpuScore = game.getElection().getScore(
                        cpuCard, cpuPolicies.get(0), cpuPolicies.get(1)
                );

                boolean marginalWasPlayedThisRound = marginalCard != null
                        && samePresidentByName(playerCard, marginalCard);
                if (marginalWasPlayedThisRound) {
                    marginalPlayedThisMatch = true;
                    result.marginalPlayCount++;
                    result.totalMarginalScore += playerScore;
                }

                game.playRound(playerCard, playerPolicies, cpuCard, cpuPolicies);
                result.totalRounds++;
                result.totalPlayerScore += playerScore;
                result.totalCpuScore += cpuScore;
                result.totalScoreDiff += playerScore - cpuScore;

                if (game.getLastWinner()) {
                    result.playerRoundWins++;
                    if (marginalWasPlayedThisRound) {
                        result.marginalRoundWins++;
                    }
                } else {
                    result.cpuRoundWins++;
                }

                if (game.checkWinner() == 0) {
                    game.finishRound();
                }
            }

            result.matches++;
            result.totalRoundsPerMatch += roundsThisMatch;
            if (marginalAvailableThisMatch) {
                result.marginalAvailableInMatch++;
            }
            if (marginalPlayedThisMatch) {
                result.marginalPlayedInMatch++;
            }

            if (game.checkWinner() == 1) {
                result.playerMatchWins++;
                if (marginalPlayedThisMatch) {
                    result.matchesWonWhenMarginalPlayed++;
                }
            } else {
                result.cpuMatchWins++;
            }

            if (!marginalPlayedThisMatch && game.checkWinner() == 1) {
                result.matchesWonWhenMarginalNotPlayed++;
            }
            if (!marginalPlayedThisMatch) {
                result.matchesMarginalNotPlayed++;
            }
        }

        System.out.println("  ROW_DONE: " + rowLabel
                + " | Match win %: " + formatPercent(result.getPlayerMatchWinRate())
                + " | EloPower: " + formatDouble(result.getEloPower())
                + " | Marginal used: " + (controlRow ? "n/a" : formatPercent(result.getMarginalPlayedMatchShare()))
                + " | Avg score diff: " + formatDouble(result.getAverageScoreDiff())
                + " | Row elapsed: " + formatMillis(System.currentTimeMillis() - rowStartMillis)
                + " | Total elapsed: " + formatMillis(System.currentTimeMillis() - globalStartMillis));
        System.out.flush();
        return result;
    }

    private static List<MarginalStudyContext> buildMarginalStudyContexts() {
        ArrayList<MarginalStudyContext> contexts = new ArrayList<MarginalStudyContext>();
        contexts.add(new MarginalStudyContext(
                "EASY", "Easy", EASY_CONTEXT_MIN_RATING, EASY_CONTEXT_CPU_AI_DIFFICULTY
        ));
        contexts.add(new MarginalStudyContext(
                "MEDIUM", "Medium", MEDIUM_CONTEXT_MIN_RATING, MEDIUM_CONTEXT_CPU_AI_DIFFICULTY
        ));
        contexts.add(new MarginalStudyContext(
                "HARD", "Hard", HARD_CONTEXT_MIN_RATING, HARD_CONTEXT_CPU_AI_DIFFICULTY
        ));
        return contexts;
    }

    private static List<RatingBucket> buildMarginalRatingBuckets() {
        ArrayList<RatingBucket> buckets = new ArrayList<RatingBucket>();
        double min = 1.0;
        while (min < 6.0) {
            double max = min + 0.5;
            buckets.add(new RatingBucket(min, max, false));
            min = max;
        }
        buckets.add(new RatingBucket(6.0, 7.0, true));
        return buckets;
    }

    private static List<President> filterPresidentsByBucket(
            List<President> presidents, RatingBucket bucket
    ) {
        ArrayList<President> filtered = new ArrayList<President>();
        if (presidents == null || bucket == null) {
            return filtered;
        }
        for (President president : presidents) {
            if (president == null) {
                continue;
            }
            double rating = president.getAv();
            boolean inBucket = rating >= bucket.minRating
                    && (bucket.inclusiveMax ? rating <= bucket.maxRating : rating < bucket.maxRating);
            if (inBucket) {
                filtered.add(president);
            }
        }
        Collections.sort(filtered, presidentRatingComparator());
        return filtered;
    }

    private static President sampleOnePresident(List<President> pool) {
        if (pool == null || pool.isEmpty()) {
            return null;
        }
        int index = (int) (Math.random() * pool.size());
        if (index < 0) {
            index = 0;
        }
        if (index >= pool.size()) {
            index = pool.size() - 1;
        }
        return pool.get(index);
    }

    private static List<President> buildMarginalPlayerDeck(List<President> basePool, President marginalCard) {
        ArrayList<President> availableBaseCards = new ArrayList<President>();
        if (basePool != null) {
            for (President president : basePool) {
                if (president != null && !samePresidentByName(president, marginalCard)) {
                    availableBaseCards.add(president);
                }
            }
        }
        List<President> baseNine = samplePresidents(availableBaseCards, MULTI_CONTEXT_BASELINE_CARDS);
        ArrayList<President> deck = new ArrayList<President>(baseNine);
        if (marginalCard != null) {
            deck.add(marginalCard);
        }
        return deck;
    }

    private static List<President> samplePresidents(List<President> pool, int count) {
        ArrayList<President> copy = new ArrayList<President>();
        if (pool != null) {
            for (President president : pool) {
                if (president != null) {
                    copy.add(president);
                }
            }
        }
        Collections.shuffle(copy);
        int resultSize = Math.min(Math.max(0, count), copy.size());
        return new ArrayList<President>(copy.subList(0, resultSize));
    }

    private static List<President> rankPresidentsByRating(List<President> presidents) {
        ArrayList<President> ranked = new ArrayList<President>();
        if (presidents != null) {
            for (President president : presidents) {
                if (president != null) {
                    ranked.add(president);
                }
            }
        }
        Collections.sort(ranked, presidentRatingComparator());
        return ranked;
    }

    private static int getRankInFullDeck(List<President> rankedDeck, President target) {
        if (rankedDeck == null || target == null) {
            return 0;
        }
        for (int i = 0; i < rankedDeck.size(); i++) {
            if (samePresidentByName(rankedDeck.get(i), target)) {
                return i + 1;
            }
        }
        return 0;
    }

    private static boolean handContainsPresident(List<President> hand, President target) {
        if (hand == null || target == null) {
            return false;
        }
        for (President president : hand) {
            if (samePresidentByName(president, target)) {
                return true;
            }
        }
        return false;
    }

    private static boolean samePresidentByName(President p1, President p2) {
        if (p1 == null || p2 == null) {
            return false;
        }
        return p1.getName().equalsIgnoreCase(p2.getName());
    }

    private static int countPresidentsBelowRating(List<President> presidents, double minRating) {
        if (presidents == null) {
            return 0;
        }
        int count = 0;
        for (President president : presidents) {
            if (president != null && president.getAv() < minRating) {
                count++;
            }
        }
        return count;
    }

    private static void printMarginalStudySummary(
            List<MarginalCardStudyResult> results,
            long startMillis
    ) {
        if (results == null || results.isEmpty()) {
            System.out.println("No results to print.");
            return;
        }

        System.out.println("SUMMARY");
        System.out.println("Rows include one control row per context plus one row per marginal rating bucket.");
        System.out.println("Delta columns compare each bucket to the control row from the same context.");
        System.out.println("Use the Context column to blend Easy / Medium / Hard later with chosen weights.");
        System.out.println();

        System.out.println("CSV_START");
        System.out.println("Context,DifficultyLabel,ContextMinRating,PlayerAIDifficulty,CpuAIDifficulty,Row,MinRating,MaxRating,InclusiveMax,MarginalEligibleCards,BasePoolCards,CpuPoolCards,Matches,PlayerWins,MatchWinPct,EloPower,DeltaWinPctVsContextControl,DeltaEloVsContextControl,RoundWinPct,AvgRounds,AvgMarginalRating,AvgMarginalCurrentGetValuePC,AvgPlayerDeckRating,AvgPlayerTop5Rating,AvgCpuDeckRating,AvgCpuTop5Rating,AvgMarginalFullDeckRank,MarginalOpeningHandPct,MarginalAvailableMatchPct,MarginalAvailableBeforeRoundPct,MarginalPlayedMatchPct,MarginalPlayedWhenAvailablePct,MarginalPlayShareOfPlayerRounds,MarginalRoundWinPct,AvgMarginalScore,MatchWinPctWhenMarginalPlayed,MatchWinPctWhenMarginalNotPlayed,AvgPlayerScore,AvgCpuScore,AvgScoreDiff");
        for (MarginalCardStudyResult result : results) {
            System.out.println(result.toCsvLine());
        }
        System.out.println("CSV_END");
        System.out.println();

        System.out.println("MULTI_CONTEXT_MARGINAL_CARD_READ_ME");
        System.out.println("Each context has its own control row: player 10 context-pool cards vs CPU 10 context-pool cards.");
        System.out.println("Bucket rows: player 9 context-pool cards + 1 marked marginal card; CPU 10 context-pool cards.");
        System.out.println("Easy context uses 1.0+ decks and CPU AI 0.50; Medium uses 3.0+ decks and CPU AI 0.65; Hard uses 5.0+ decks and CPU AI 0.80.");
        System.out.println("Player AI stays 0.65 in every context.");
        System.out.println("DeltaWinPctVsContextControl and DeltaEloVsContextControl are the cleanest columns for marginal competitive value within each context.");
        System.out.println("Later, blend Easy/Medium/Hard context deltas with chosen weights to derive a candidate getValue curve.");
    }

    private static Comparator<President> presidentRatingComparator() {
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
                int ratingCompare = Double.compare(p2.getAv(), p1.getAv());
                if (ratingCompare != 0) {
                    return ratingCompare;
                }
                return p1.getName().compareToIgnoreCase(p2.getName());
            }
        };
    }

    private static double averagePresidentRating(List<President> presidents) {
        if (presidents == null || presidents.isEmpty()) {
            return 0.0;
        }
        double total = 0.0;
        int count = 0;
        for (President president : presidents) {
            if (president == null) {
                continue;
            }
            total += president.getAv();
            count++;
        }
        return count == 0 ? 0.0 : total / count;
    }

    private static double calculateEloPower(double winRate) {
        double clipped = Math.max(0.0001, Math.min(0.9999, winRate));
        return 400.0 * Math.log10(clipped / (1.0 - clipped));
    }

    private static String formatDouble(double value) {
        return String.format(Locale.US, "%.4f", value);
    }

    private static String formatPercent(double value) {
        return String.format(Locale.US, "%.2f%%", value * 100.0);
    }

    private static String formatIntRounded(double value) {
        return String.valueOf((int) Math.round(value));
    }

    private static String formatMillis(long millis) {
        long cleanMillis = Math.max(0L, millis);
        long seconds = cleanMillis / 1000L;
        long minutes = seconds / 60L;
        long remainingSeconds = seconds % 60L;
        if (minutes <= 0L) {
            return remainingSeconds + " sec";
        }
        return minutes + " min " + remainingSeconds + " sec";
    }

    private static class MarginalStudyContext {
        private String contextName;
        private String difficultyLabel;
        private double minRating;
        private double cpuAiDifficulty;

        private MarginalStudyContext(
                String contextName, String difficultyLabel, double minRating, double cpuAiDifficulty
        ) {
            this.contextName = contextName == null ? "" : contextName;
            this.difficultyLabel = difficultyLabel == null ? "Medium" : difficultyLabel;
            this.minRating = minRating;
            this.cpuAiDifficulty = cpuAiDifficulty;
        }
    }

    private static class RatingBucket {
        private double minRating;
        private double maxRating;
        private boolean inclusiveMax;
        private String label;

        private RatingBucket(double minRating, double maxRating, boolean inclusiveMax) {
            this.minRating = minRating;
            this.maxRating = maxRating;
            this.inclusiveMax = inclusiveMax;
            this.label = formatDouble(minRating) + "-" + formatDouble(maxRating)
                    + (inclusiveMax ? " inclusive" : "");
        }
    }

    private static class MarginalCardStudyResult {
        private MarginalStudyContext context;
        private String rowLabel;
        private boolean controlRow;
        private double bucketMinRating;
        private double bucketMaxRating;
        private boolean bucketInclusiveMax;
        private int marginalEligibleCards;
        private int basePoolSize;
        private int cpuPoolSize;
        private int matches;
        private int playerMatchWins;
        private int cpuMatchWins;
        private int playerRoundWins;
        private int cpuRoundWins;
        private int totalRounds;
        private int totalRoundsPerMatch;
        private int marginalStartedInOpeningHand;
        private int marginalAvailableInMatch;
        private int marginalAvailableBeforeRoundCount;
        private int marginalPlayedInMatch;
        private int marginalPlayCount;
        private int marginalRoundWins;
        private int matchesWonWhenMarginalPlayed;
        private int matchesMarginalNotPlayed;
        private int matchesWonWhenMarginalNotPlayed;
        private double totalMarginalRating;
        private double totalMarginalCurrentValue;
        private double totalMarginalFullDeckRank;
        private double totalMarginalScore;
        private double totalPlayerDeckRating;
        private double totalPlayerTopFiveRating;
        private double totalCpuDeckRating;
        private double totalCpuTopFiveRating;
        private double totalPlayerScore;
        private double totalCpuScore;
        private double totalScoreDiff;
        private double deltaWinPctVsControl;
        private double deltaEloPowerVsControl;

        private MarginalCardStudyResult(
                MarginalStudyContext context, String rowLabel, boolean controlRow
        ) {
            this.context = context;
            this.rowLabel = rowLabel == null ? "" : rowLabel;
            this.controlRow = controlRow;
        }

        private double getPlayerMatchWinRate() {
            return matches <= 0 ? 0.0 : playerMatchWins / (double) matches;
        }

        private double getEloPower() {
            return calculateEloPower(getPlayerMatchWinRate());
        }

        private double getPlayerRoundWinRate() {
            return totalRounds <= 0 ? 0.0 : playerRoundWins / (double) totalRounds;
        }

        private double getAverageRoundsPerMatch() {
            return matches <= 0 ? 0.0 : totalRoundsPerMatch / (double) matches;
        }

        private double getAverageMarginalRating() {
            return controlRow || matches <= 0 ? 0.0 : totalMarginalRating / matches;
        }

        private double getAverageMarginalCurrentValue() {
            return controlRow || matches <= 0 ? 0.0 : totalMarginalCurrentValue / matches;
        }

        private double getAverageMarginalFullDeckRank() {
            return controlRow || matches <= 0 ? 0.0 : totalMarginalFullDeckRank / matches;
        }

        private double getAveragePlayerDeckRating() {
            return matches <= 0 ? 0.0 : totalPlayerDeckRating / matches;
        }

        private double getAveragePlayerTopFiveRating() {
            return matches <= 0 ? 0.0 : totalPlayerTopFiveRating / matches;
        }

        private double getAverageCpuDeckRating() {
            return matches <= 0 ? 0.0 : totalCpuDeckRating / matches;
        }

        private double getAverageCpuTopFiveRating() {
            return matches <= 0 ? 0.0 : totalCpuTopFiveRating / matches;
        }

        private double getMarginalOpeningHandShare() {
            return controlRow || matches <= 0 ? 0.0 : marginalStartedInOpeningHand / (double) matches;
        }

        private double getMarginalAvailableMatchShare() {
            return controlRow || matches <= 0 ? 0.0 : marginalAvailableInMatch / (double) matches;
        }

        private double getMarginalAvailableBeforeRoundShare() {
            return controlRow || totalRounds <= 0 ? 0.0 : marginalAvailableBeforeRoundCount / (double) totalRounds;
        }

        private double getMarginalPlayedMatchShare() {
            return controlRow || matches <= 0 ? 0.0 : marginalPlayedInMatch / (double) matches;
        }

        private double getMarginalPlayedWhenAvailableShare() {
            return controlRow || marginalAvailableInMatch <= 0
                    ? 0.0
                    : marginalPlayedInMatch / (double) marginalAvailableInMatch;
        }

        private double getMarginalPlayShareOfPlayerRounds() {
            return controlRow || totalRounds <= 0 ? 0.0 : marginalPlayCount / (double) totalRounds;
        }

        private double getMarginalRoundWinRate() {
            return controlRow || marginalPlayCount <= 0 ? 0.0 : marginalRoundWins / (double) marginalPlayCount;
        }

        private double getAverageMarginalScore() {
            return controlRow || marginalPlayCount <= 0 ? 0.0 : totalMarginalScore / marginalPlayCount;
        }

        private double getMatchWinRateWhenMarginalPlayed() {
            return controlRow || marginalPlayedInMatch <= 0
                    ? 0.0
                    : matchesWonWhenMarginalPlayed / (double) marginalPlayedInMatch;
        }

        private double getMatchWinRateWhenMarginalNotPlayed() {
            return controlRow || matchesMarginalNotPlayed <= 0
                    ? 0.0
                    : matchesWonWhenMarginalNotPlayed / (double) matchesMarginalNotPlayed;
        }

        private double getAveragePlayerScore() {
            return totalRounds <= 0 ? 0.0 : totalPlayerScore / totalRounds;
        }

        private double getAverageCpuScore() {
            return totalRounds <= 0 ? 0.0 : totalCpuScore / totalRounds;
        }

        private double getAverageScoreDiff() {
            return totalRounds <= 0 ? 0.0 : totalScoreDiff / totalRounds;
        }

        private String toCsvLine() {
            return context.contextName + ","
                    + context.difficultyLabel + ","
                    + formatDouble(context.minRating) + ","
                    + formatDouble(MULTI_CONTEXT_PLAYER_AI_DIFFICULTY) + ","
                    + formatDouble(context.cpuAiDifficulty) + ","
                    + rowLabel + ","
                    + (controlRow ? "" : formatDouble(bucketMinRating)) + ","
                    + (controlRow ? "" : formatDouble(bucketMaxRating)) + ","
                    + (controlRow ? "" : String.valueOf(bucketInclusiveMax)) + ","
                    + marginalEligibleCards + ","
                    + basePoolSize + ","
                    + cpuPoolSize + ","
                    + matches + ","
                    + playerMatchWins + ","
                    + formatPercent(getPlayerMatchWinRate()) + ","
                    + formatDouble(getEloPower()) + ","
                    + formatPercent(deltaWinPctVsControl) + ","
                    + formatDouble(deltaEloPowerVsControl) + ","
                    + formatPercent(getPlayerRoundWinRate()) + ","
                    + formatDouble(getAverageRoundsPerMatch()) + ","
                    + (controlRow ? "" : formatDouble(getAverageMarginalRating())) + ","
                    + (controlRow ? "" : formatIntRounded(getAverageMarginalCurrentValue())) + ","
                    + formatDouble(getAveragePlayerDeckRating()) + ","
                    + formatDouble(getAveragePlayerTopFiveRating()) + ","
                    + formatDouble(getAverageCpuDeckRating()) + ","
                    + formatDouble(getAverageCpuTopFiveRating()) + ","
                    + (controlRow ? "" : formatDouble(getAverageMarginalFullDeckRank())) + ","
                    + (controlRow ? "" : formatPercent(getMarginalOpeningHandShare())) + ","
                    + (controlRow ? "" : formatPercent(getMarginalAvailableMatchShare())) + ","
                    + (controlRow ? "" : formatPercent(getMarginalAvailableBeforeRoundShare())) + ","
                    + (controlRow ? "" : formatPercent(getMarginalPlayedMatchShare())) + ","
                    + (controlRow ? "" : formatPercent(getMarginalPlayedWhenAvailableShare())) + ","
                    + (controlRow ? "" : formatPercent(getMarginalPlayShareOfPlayerRounds())) + ","
                    + (controlRow ? "" : formatPercent(getMarginalRoundWinRate())) + ","
                    + (controlRow ? "" : formatDouble(getAverageMarginalScore())) + ","
                    + (controlRow ? "" : formatPercent(getMatchWinRateWhenMarginalPlayed())) + ","
                    + (controlRow ? "" : formatPercent(getMatchWinRateWhenMarginalNotPlayed())) + ","
                    + formatDouble(getAveragePlayerScore()) + ","
                    + formatDouble(getAverageCpuScore()) + ","
                    + formatDouble(getAverageScoreDiff());
        }
    }
}
