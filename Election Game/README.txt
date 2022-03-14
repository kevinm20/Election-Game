=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=
CIS 120 Game Project README
PennKey: kevmani
=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=

===================
=: Core Concepts :=
===================

- List the four core concepts, the features they implement, and why each feature
  is an appropriate use of the concept. Incorporate the feedback you got after
  submitting your proposal.

  1. Collections: I used this feature extensively throughout the entire game. Collections represent a ton
 	of different things in my game between the decks of cards, and the hands of the user and CPU. They are
 	re-sized a ton throughout the game, as the user/cpu play cards and draw cards, removing one from their
 	collection and adding another. Collections were necessary because I needed all of the resizing for the
 	most part. I also used a bunch of for-each loops throughout.
 	
 	These are the collections I used:
 	
 	-ArrayList for decks, user hand, and CPU hand. ArrayLists were the best because I could easily get the position
 	of certain cards that needed to be played, were very efficient at finding that. I chose them mainly because it
 	provided the easiest implementation for shuffling the decks. Also used them a lot of times for helper methods
 	or other simple things throughout the code.
 	-TreeMaps for the AI. It mapped each president in the AI's hand to a score that the AI would use to play its
 	cards, and also map presidents to a pair of policies for the AI.

  2. Inheritance and Subtying: In order for my game to work, I needed a few examples of this. First, I made the superclass
     "Card" that was extended by President, Election, and Policy. This was necessary because my decks dealt in cards,
     and that having this one superclass for those three made making them all extend comparable very easy in case I had
     to do that anywhere in my ode.
     
     The next example I used was the superclass Player and the subclass AI. I wanted to make them two distinct classes because
     the AI was far more complicated than the Player (which is just supposed to be the user), and didn't want the user
     to have to use a ton of extra variables because the game would run slower. The AI inherited a bunch of useful methods from
     the user, and added a bunch more so that it functioned properly. I used dynamic dispatch on calling .reset() on the AI
     that was statically typed as a user, to help it clear its internal variables.
     
     Finally, I used inner classes in the RunElectionGame file. There were UserPolicies, UserDeck, and GameBoard. I tried
     having them be their own class at first, but decided to make them inner classes because I wanted them to all be
     able to modify the same game state from inside the main class. Each of them have different functions, and made
     my implementation so much easier and less redundant. These were part of the controller aspects for the game.

  3. JUnit Testing: I made multiple different testing for different classes in my game. It mainly focused on testing
  	 the individual little methods in the different card classes, to make sure they all worked properly together. I tested edge
  	 cases, but didn't test them for every single method, since a lot of them would have invariants like the input wouldn't be null
  	 (e.g. the election in each round can never be null as an invariant, so it would never be necessary to test for that). I did
  	 make a few tests be essentially duplicates of each other, such as testing multiple different election combinations, to make
  	 sure there weren't any issues I was missing (indeed, there were some tests that were basically the same that didn't work while
  	 others did, but I ended up fixing those errors). I also tested my game model as well.
  	 
  	 Note: ElectionTest and PolicyTest are flipped in their titled, I tried to change their names but it wasn't working for some
  	 reason.

  4. AI: The final concept I chose was making an AI. This was definitely the toughest part of the project, and I tried to make the AI
  	 a little creative so that it felt like playing a real player and not just an unstoppable machine. First, the AI "guesses" the
  	 different attributes or policies desired by the election. The AI has different difficulty levels, and harder AI is just more
  	 accurate in it's guess. Next, in short, the AI iterates through all of its cards and plays the best possible combination 
  	 based off its guess. To make the AI "think ahead" a little more, I also tracked something called "potential points" for each card,
  	 or how high the President card could be in a "good" election. Early in the game, when the user has 0/1 wins, the AI will
  	 have a tendency to "save" cards and lower the score of really good cards that perhaps wouldn't be played ideally in that round.
  	 However, with 1 win for the user the tendency is halved, and then gone at 0 wins, because the AI gets more "desperate" and can't
  	 "save" cards anymore. This tendency helped the CPU play better, because for example if it's the first round, the CPU shouldn't
  	 play George Washington if they don't have good policies for him or the election doesn't want his best attributes. The AI performs
  	 very well, at "Hard" difficulty which is what I played against because I obviously have a lot of experience with this game,
  	 my win rate was very close to 50/50.

=========================
=: Your Implementation :=
=========================

- Provide an overview of each of the classes in your code, and what their
  function is in the overall game.

	Deck: This class is a simple but important one, it serves as the decks for each type of card.
		  It allows the players to draw from it, and has a shuffle feature.
    Card: Decks are composed of cards, and card is the superclass for all the types of cards in my game.
          It is a simple class mainly there to help all the card types fit in a deck, and give them all
          a comparable implementation that was useful for testing and necessary for the AI since I used TreeMaps.
    Election, President, Policy: All 3 of these are different card types. They are all very unique and have their
    	  totally different functions for the game, as described in the implementation.
    CardData: This was the painful class where I made all the objects for every single card in the game, I used this
          for the game model so I could easily get all the card objects needed without taking up a ton of space in my file.
    ElectionGame: This is the model for my game. When Player1 is edited to become an AI, I had CPU vs. CPU games play out against
    	  each other for a very decent way of seeing that my features worked and the AI is acting the way I wanted to.
    RunElectionGame: This is the view/controller in my game, with all the swing components and letting the game work visually.
    Player: This class represents a player, and was primarily used for methods that the user could invoke as well as storing
    		their hand and other things.
    AI: An extension of the player, with the ability to pick cards to play by itself.

- Were there any significant stumbling blocks while you were implementing your
  game (related to your design, or otherwise)?
  
  	There were a lot of stumbling blocks throughout the way. Before understanding inner classes, making the game itself
  	was very tough to say the least. However, after discovering that it was much easier, that was a life saver.
  	
  	The AI was also very hard to make just because of how hard it was to test. I went around this by using a ton of 
  	printing to the console for each step in its logic to make sure that was working properly. In addition, when I tried
  	adding new refinements or improvements to the AI, I had a creative way of testing it. I used the game model to just
  	simulate like 50 games of the "New" AI versus the "old" one, and it was really effective in helping me see if my changes
  	actually made the AI better.


- Evaluate your design. Is there a good separation of functionality? How well is
  private state encapsulated? What would you refactor, if given the chance?
	
	I'm pretty happy with my design after all. There were a bunch of features I wanted to and kept adding to the game,
	originally I wasn't even going to have policies for example. However, I found good ways to do all of it in my opinion,
	and the game works exactly like I imagined and intended, and still challenges me against the AI, even though I've played
	this game in real life many times before and know a lot about it. I'm happy with my separation of functionality, since I
	have a lot of different buttons do different things, and found a creative way of letting the user see both their cards
	and policies easily and swap between the two. I think everything I did was encapsulated properly, and I only used methods
	to modify internal variables.
	
	There were definitely many things I could refactor, but I didn't really get the chance because I kept adding new features.
	I might have been able to make Deck extend collection for example, or could have made the Reset feature in RunElectionGame
	a helper method since I used it both when clicking resign, and when the game was over. Also, userdeck and userpolicies
	were pretty similar classes, and if I had the chance I could probably find some way to combine them.


========================
=: External Resources :=
========================

- Cite any external resources (libraries, images, tutorials, etc.) that you may
  have used while implementing your game.
  
    I used a billion images in my game obviously for each card, saved in the "files" folder. I learned about and
    used ImageIcons to do this easily and put the images in my game.
    I also spent a lot of time figuring out how to get JScrollPane to work, since the 15 policies wouldn't fit on the screen.
