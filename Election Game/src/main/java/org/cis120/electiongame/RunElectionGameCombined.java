package org.cis120.electiongame;

/**
 * Kevin Mani - Election game
 */

import java.awt.*;
import java.awt.event.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Window;
import javazoom.jl.decoder.JavaLayerException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import javax.swing.border.LineBorder;
import java.awt.Color;



/**
 * This class sets up the top-level frame and widgets for the GUI.
 */
public class RunElectionGameCombined implements Runnable {

	private ElectionGame election = new ElectionGame();
	private boolean starting = true;
	GameBoard board = null;
	private UserDeck user_cards;
	private UserPolicies up;
	private boolean mode = false;
	private boolean swapmode = false;
	private String deckSet = "standard";
	private int cumulativeWins = 0;
	private int cumulativeGames = 0;
	private boolean cardInfoMode = false;
	private boolean askToResign = true;

	// Turn this to TRUE if trying to export to JAR file, FALSE otherwise. Make prefix "resources/" if exporting to JAR.

	private boolean jarmode = false;
	static String prefix = "";

	// Change this if the screen is wacky
	int cardSize = 275;

	public static InputStream loadImage(String path) {
		InputStream input = RunElectionGameCombined.class.getResourceAsStream(path);
		if (input == null) {
			input = RunElectionGameCombined.class.getResourceAsStream("/" + path);
		}
		return input;
	}
	
	// Helper to update all component fonts in a container recursively
	public void updateFonts(Component comp, Font font) {
	    if (comp instanceof JLabel || comp instanceof JButton || comp instanceof JTextField ||
	            comp instanceof JTextArea || comp instanceof JCheckBox || comp instanceof JRadioButton ||
	            comp instanceof JComboBox || comp instanceof JMenu || comp instanceof JMenuItem ||
	            comp instanceof JTabbedPane || comp instanceof JPanel || comp instanceof JScrollBar ||
	            comp instanceof JScrollPane) {
	        comp.setFont(font);
	    }
	    if (comp instanceof Container) {
	        for (Component child : ((Container) comp).getComponents()) {
	            updateFonts(child, font);
	        }
	    }
	}

	
	public void run() {
		// Used to track stats
		long startTime = System.nanoTime();
		
		/*
	    // Make a loading/starting screen with this info
		// Make a function here that gets full screen size
	    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	    int screenWidth = screenSize.width;
	    int screenHeight = screenSize.height;
	    
	    screenWidth = 1900;
	    screenHeight = 1200;
	    
	    // Figure out the actual math here
	    cardSize = 275;
	    
	    System.out.println("Card Size: " + cardSize);
	    */
	    

		// Top-level frame in which game components live
	    final JFrame frame = new JFrame("Election Game");
	    
	    // Create a loading screen panel with the desired background image
	    BackgroundPanel loadingScreen = new BackgroundPanel(prefix + "files/loadingscreen.PNG");
	    // Set up the layout and add the loading screen to the frame
	    frame.setLayout(new BorderLayout());
	    frame.add(loadingScreen, BorderLayout.CENTER);

		// Add the logo to the top left
		ImageIcon logoIcon = new ImageIcon(
				new ImageIcon(getClass().getClassLoader().getResource(prefix + "files/logo.PNG")).getImage()
						.getScaledInstance((int) (cardSize / 225.0 * 100), (int) (cardSize / 225.0 * 100),
								Image.SCALE_SMOOTH));
		// Set the icon image for the frame
		frame.setIconImage(logoIcon.getImage());
		
	    // Set the frame to be undecorated before making it displayable
	    frame.setUndecorated(true);

		// Pack and set the frame to full-screen mode
		frame.pack();
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.requestFocus();
		
		frame.revalidate();
		frame.repaint();
		
		// Get the frame stats
	 	    
		int frameWidth = frame.getWidth();
		int frameHeight = frame.getHeight();
		
		cardSize = (int)(frameWidth/1900.0*275.0);

	    
	    // Initialize soundtrack
	    List<String> tracks = Arrays.asList(
    		"src/main/resources/files/americathebeautiful.MP3",
            "src/main/resources/files/battlehymnoftherepublic.MP3",
            "src/main/resources/files/columbiathegemoftheocean.MP3",
            "src/main/resources/files/dixie.MP3",
            "src/main/resources/files/mycountrytisofthee.MP3",
            "src/main/resources/files/starsandstripesforever.MP3",
            "src/main/resources/files/starspangledbanner.MP3",
            "src/main/resources/files/whenjohnnycomesmarchinghome.MP3",
            "src/main/resources/files/yankeedoodle.MP3",
            "src/main/resources/files/youreagrandoldflag.MP3",
            "src/main/resources/files/camptownraces.MP3",
            "src/main/resources/files/itsalonglongwaytotipperary.MP3",
            "src/main/resources/files/justlikewashingtoncrossedthedelawaregeneralpershingwillcrosstherhine.MP3",
            "src/main/resources/files/myoldkentuckyhome.MP3",
            "src/main/resources/files/onthealamo.MP3",
            "src/main/resources/files/onthemississippi.MP3",
            "src/main/resources/files/packupyourtroublesinyouroldkitbagandsmilesmilesmile.MP3",
            "src/main/resources/files/ragtimecowboyjoe.MP3",
            "src/main/resources/files/swanee.MP3",
            "src/main/resources/files/temptationrag.MP3",
            "src/main/resources/files/thebattlecryoffreedom.MP3",
            "src/main/resources/files/tramptramptramp.MP3"
	    );

	    // Create the SoundtrackPlayer instance
	    SoundtrackPlayer player = new SoundtrackPlayer(tracks);

	    // Start playing the soundtrack in a new thread
	    new Thread(new Runnable() {
	        public void run() {
	            while (true) {
	                player.play();
	                // Since `play()` plays one track and then returns,
	                // we just loop and wait for it to return before playing the next track.
	            }
	        }
	    }).start();
	    
	    board = new GameBoard(player);
	    // User Policy Deck, I added a scrollbar since you get 18 policies
	    up = new UserPolicies(player);

	    JScrollPane userPolicies = new JScrollPane(up, ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER,
	            ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
	    userPolicies.getHorizontalScrollBar().setUnitIncrement(100);

	    // Set the background color to brown
	    Color brown = new Color(0, 0, 0); // RGB values for a brown color
	    userPolicies.getViewport().setBackground(brown);
	    userPolicies.setBackground(brown);
	    up.setBackground(brown);

	    // Set the preferred size of the JScrollPane
	    userPolicies.setPreferredSize(new Dimension((int)(cardSize/225.0*3600), (int)(cardSize/225.0*325)));

		
		// User Card Deck
		user_cards = new UserDeck(player);
		user_cards.setPreferredSize(new Dimension((int)(cardSize/225.0*600), (int)(cardSize/225.0*325)));
		frame.add(user_cards, BorderLayout.SOUTH);

		
		// Game board/Election Area
		frame.add(board, BorderLayout.CENTER);
		board.setLayout(new GridLayout(1, 7));

		// AI Card Deck, just for visuals
		double aiCardHeightRatio = 106.0 / 1200.0; // Proportionate height for the AI Cards section
		double aiCardWidthRatio = 1.0; // Use the full width of the image
		double aiCardStartXRatio = 0.0; // Start cropping from the very left of the image
		double aiCardStartYRatio = 0.0; // Start cropping from the very top of the image

		BackgroundPanel ai_cards = new BackgroundPanel(prefix + "files/backgroundfull.PNG", aiCardHeightRatio, aiCardStartYRatio, aiCardWidthRatio, aiCardStartXRatio);
		frame.add(ai_cards, BorderLayout.NORTH);

		for (int i = 0; i < 5; i++) {
		    URL imgURL = getClass().getClassLoader().getResource(prefix + "files/aicard.PNG");
		    ImageIcon icon = new ImageIcon(new ImageIcon(imgURL).getImage().getScaledInstance(
		            (int) (0.266666667 * cardSize), (int) (0.351111111 * cardSize), Image.SCALE_SMOOTH));
		    JLabel aicardlabel = new JLabel(icon);
		    ai_cards.add(aicardlabel);
		}

		// Height and width ratios for the decks area
		double deckHeightRatio = 697.0 / 1200.0;
		double deckWidthRatio = 211.0 / 1900.0;
		double deckStartYRatio = 106.0 / 1200.0;
		double deckStartXRatio = 0.0; // Start cropping from the left side of the image

		// Using the new constructor with cropping starting from a specific position
		BackgroundPanel decks = new BackgroundPanel(prefix + "files/backgroundfull.PNG", deckHeightRatio, deckStartYRatio, deckWidthRatio, deckStartXRatio);
		decks.setPreferredSize(new Dimension((int)(cardSize/225.0*175), (int)(cardSize/225.0*200)));
		deckWidthRatio = decks.getWidth()/frameWidth;
		decks.setBackgroundImage(prefix + "files/backgroundfull.PNG", deckHeightRatio, deckStartYRatio, deckWidthRatio, deckStartXRatio);

		JLabel logoLabel = new JLabel(logoIcon);
		decks.add(logoLabel);

		frame.add(decks, BorderLayout.WEST);
		final JLabel status = new JLabel(election.currentScore(), SwingConstants.CENTER);
		setLabel(status);
		decks.add(status);

		decks.setLayout(new GridLayout(5, 1));


		// Control panel with custom background
		double controlPanelHeightRatio = 697.0 / 1200.0; // Same height ratio as decks
		double controlPanelWidthRatio = 210.0 / 1900.0; // Width ratio for the control panel
		double controlPanelStartYRatio = 106.0 / 1200.0; // Start after AI cards area (same as decks)
		double controlPanelStartXRatio = 1.0 - controlPanelWidthRatio; // Start from the right side

		BackgroundPanel control_panel = new BackgroundPanel(
		    prefix + "files/backgroundfull.PNG", 
		    controlPanelHeightRatio, 
		    controlPanelStartYRatio, 
		    controlPanelWidthRatio, 
		    controlPanelStartXRatio
		);
		
		controlPanelWidthRatio = control_panel.getWidth()/frameWidth;
		controlPanelStartXRatio = 1.0 - controlPanelWidthRatio; // Start from the right side
		control_panel.setBackgroundImage(prefix + "files/backgroundfull.PNG", controlPanelHeightRatio, controlPanelStartYRatio, controlPanelWidthRatio, controlPanelStartXRatio);
		
		control_panel.setLayout(new GridBagLayout()); // Set layout after initializing the panel
		
		// GridBagConstraints setup
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		int insetSize = (int) (cardSize/275.0 * 10.0);
		gbc.insets = new Insets(insetSize, insetSize, insetSize, insetSize); // Padding around buttons

		// Play button: Where most of the real action happens
		final JButton play = new JButton();

		// Set the button preferences
		play.setContentAreaFilled(false);
		play.setPreferredSize(new Dimension((int)(cardSize / 275.0 * 193), (int)(cardSize / 275.0 * 110)));
		play.setBorderPainted(false);

		// Load the initial icons
		ImageIcon playIcon = new ImageIcon(getClass().getClassLoader().getResource(prefix + "files/play.PNG"));
		ImageIcon playHoverIcon = new ImageIcon(getClass().getClassLoader().getResource(prefix + "files/playhover.PNG"));
		ImageIcon playClickIcon = new ImageIcon(getClass().getClassLoader().getResource(prefix + "files/playclicked.PNG"));

		// Get the exact dimensions of the button
		int buttonWidth = play.getPreferredSize().width;
		int buttonHeight = play.getPreferredSize().height;
		
		// Scale the icons to fit exactly within the button dimensions
		play.setIcon(new ImageIcon(playIcon.getImage().getScaledInstance(buttonWidth, buttonHeight, Image.SCALE_SMOOTH)));
		play.setRolloverIcon(new ImageIcon(playHoverIcon.getImage().getScaledInstance(buttonWidth, buttonHeight, Image.SCALE_SMOOTH)));
		play.setPressedIcon(new ImageIcon(playClickIcon.getImage().getScaledInstance(buttonWidth, buttonHeight, Image.SCALE_SMOOTH)));

		// Revalidate and repaint to apply the changes
		play.revalidate();
		play.repaint();
		
		play.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// Play the button click sound
		        player.playSoundEffect("files/playbutton.MP3");
		        
				if (election.getActivePinCard() == null || election.getActivePinnedPolicies().get(0) == null
						|| election.getActivePinnedPolicies().get(1) == null) {
					JOptionPane.showMessageDialog(frame, "Error: you haven't played all cards!", "Error",
							JOptionPane.ERROR_MESSAGE);
				} else {
					if (!mode) {
						// election.printGameState();

						election.getPlayer1().draw(election.getPres());
						election.getPlayer1().drawPols(election.getPol());

						AI ai = (AI) election.getPlayer2();

						President cpuplay = ai.play(election.getPres(), election.getElection());

						election.player2PlayCard(cpuplay);
						election.pinAIPolicies(ai.playPolicies(cpuplay, election.getPol()));

						board.flipCPU();
						board.draw();

						election.playRound(election.getPlayer1Card(), election.getPinned1(), cpuplay,
								election.getPinned2());

					} else {
						if (election.getTurn()) {
							election.flipPlayer();
							user_cards.hideCards(4);
							up.hideCards();
							board.draw();
							JOptionPane.showMessageDialog(frame, "Please pass the computer to Player 2.");
							user_cards.paintCards();
							up.paintCards();
							board.draw();
						} else {
							user_cards.hideCards(4);
							up.hideCards();
							board.flipUser();
							board.draw();
							JOptionPane.showMessageDialog(frame, "Click 'OK' when both players are ready!");
							board.flipUser();
							// election.printGameState();

							election.getPlayer2().draw(election.getPres());
							election.getPlayer2().drawPols(election.getPol());

							election.getPlayer1().draw(election.getPres());
							election.getPlayer1().drawPols(election.getPol());

							board.flipCPU();
							board.draw();

							election.playRound(election.getPlayer1Card(), election.getPinned1(),
									election.getPlayer2Card(), election.getPinned2());
							election.flipPlayer();
						}
					}

					if (!mode || election.getTurn()) {
						setLabel(status);
						status.repaint();
						JOptionPane.showMessageDialog(frame, election.getMessage());
						if (election.getAchievement() != null) {
							JOptionPane.showMessageDialog(frame, election.getAchievement());
						}

						if (election.checkWinner() == 1) {
							JOptionPane.showMessageDialog(frame,
									election.getPlayer1().getName() + " has won!!! " + election.finalScore(true));
							if (mode) {
								JOptionPane.showMessageDialog(frame, "Please pass the computer to Player 1.");
							}
							cumulativeWins++;
							cumulativeGames++;
							election.reset(deckSet, null, 0.0, 5);
							user_cards.reset();
							up.reset();
							board.reset();
							setLabel(status);
							status.repaint();
						} else if (election.checkWinner() == 2) {
							JOptionPane.showMessageDialog(frame,
									election.getPlayer2().getName() + " has won!!! " + election.finalScore(false));
							if (mode) {
								JOptionPane.showMessageDialog(frame, "Please pass the computer to Player 1.");
							}
							cumulativeGames++;
							election.reset(deckSet, null, 0.0, 5);
							user_cards.reset();
							up.reset();
							board.reset();
							setLabel(status);
							status.repaint();
						} else {
							election.player1PlayCard(null);
							election.player2PlayCard(null);
							board.flipCPU();
							board.draw();
							if (mode) {
								JOptionPane.showMessageDialog(frame, "Please pass the computer to Player 1.");
							}
							user_cards.paintCards();
							up.paintCards();
						}
					}
				}
			}

		});
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 1.0;
		gbc.weighty = 1.0;
		control_panel.add(play, gbc);
		
		// Load the resign icon and hover icon
		ImageIcon resignIcon = new ImageIcon(getClass().getClassLoader().getResource(prefix + "files/resign.PNG"));
		ImageIcon resignHoverIcon = new ImageIcon(getClass().getClassLoader().getResource(prefix + "files/resignhover.PNG"));
		ImageIcon resignClickIcon = new ImageIcon(getClass().getClassLoader().getResource(prefix + "files/resignclicked.PNG"));

		// Create the reset button with the resign icon scaled to buttonWidth and buttonHeight
		final JButton reset = new JButton(new ImageIcon(resignIcon.getImage().getScaledInstance(buttonWidth, buttonHeight, Image.SCALE_SMOOTH)));

		// Set the rollover icon to the hover image, scaled to buttonWidth and buttonHeight
		reset.setRolloverIcon(new ImageIcon(resignHoverIcon.getImage().getScaledInstance(buttonWidth, buttonHeight, Image.SCALE_SMOOTH)));

		// Set the pressed icon to the clicked image, scaled to buttonWidth and buttonHeight
		reset.setPressedIcon(new ImageIcon(resignClickIcon.getImage().getScaledInstance(buttonWidth, buttonHeight, Image.SCALE_SMOOTH)));

		// Set button preferences
		reset.setContentAreaFilled(false); // Remove default button background
		reset.setBorderPainted(false);     // Remove button border

		// Set the preferred size of the button to buttonWidth and buttonHeight
		reset.setPreferredSize(new Dimension(buttonWidth, buttonHeight)); // Set preferred size

		
		reset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				player.playSoundEffect("files/playbutton.MP3");
				
				if (!askToResign) {
					JOptionPane.showMessageDialog(frame, "You have resigned. " + election.finalScore(false));
					cumulativeGames++;
					election.reset(deckSet, null, 0.0, 5);
					setLabel(status);
					status.repaint();
					user_cards.reset();
					up.reset();
					board.reset();
				} else {
					Object[] resignOptions = { "Yes and don't ask again", "Yes", "No" };
					int resignChoice = JOptionPane.showOptionDialog(frame, "Are you sure you would like to resign?",
							"Resign", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, resignOptions, 2);

					if (resignChoice == 0 || !askToResign || resignChoice == 1) {
						JOptionPane.showMessageDialog(frame, "You have resigned. " + election.finalScore(false));
						if (resignChoice == 0) {
							askToResign = !askToResign;
						}
						cumulativeGames++;
						election.reset(deckSet, null, 0.0, 5);
						setLabel(status);
						status.repaint();
						user_cards.reset();
						up.reset();
						board.reset();
					}
				}
			}
		});
		gbc.gridy = 3;
		control_panel.add(reset, gbc);

		// Load the help icon and hover icon
		ImageIcon helpIcon = new ImageIcon(getClass().getClassLoader().getResource(prefix + "files/help.PNG"));
		ImageIcon helpHoverIcon = new ImageIcon(getClass().getClassLoader().getResource(prefix + "files/helphover.PNG"));
		ImageIcon helpClickIcon = new ImageIcon(getClass().getClassLoader().getResource(prefix + "files/helpclicked.PNG"));

		// Create the help button with the help icon scaled to buttonWidth and buttonHeight
		final JButton help = new JButton(new ImageIcon(helpIcon.getImage().getScaledInstance(buttonWidth, buttonHeight, Image.SCALE_SMOOTH)));

		// Set the rollover icon to the hover image, scaled to buttonWidth and buttonHeight
		help.setRolloverIcon(new ImageIcon(helpHoverIcon.getImage().getScaledInstance(buttonWidth, buttonHeight, Image.SCALE_SMOOTH)));

		// Set the pressed icon to the clicked image, scaled to buttonWidth and buttonHeight
		help.setPressedIcon(new ImageIcon(helpClickIcon.getImage().getScaledInstance(buttonWidth, buttonHeight, Image.SCALE_SMOOTH)));

		// Set button preferences
		help.setContentAreaFilled(false); // Remove default button background
		help.setBorderPainted(false);     // Remove button border
		help.setPreferredSize(new Dimension(buttonWidth, buttonHeight)); // Set preferred size


		help.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				player.playSoundEffect("files/playbutton.MP3");

				Object[] settingOptions = { "Card Info", "Instructions" };
				int helpMode = JOptionPane.showOptionDialog(frame, "What would you like help with?", "Help",
						JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, settingOptions, -1);

				if (helpMode == 1) {
					JOptionPane.showMessageDialog(frame,
							"Welcome to Election Game! First, I will explain how to play the game in s"
									+ "hort, and then we'll dive into the details of each card. \n"
									+ "Simply put, each round, there is a certain election chosen. You d"
									+ "raw 5 Candidate cards and 15 Policy cards, \n"
									+ "and based on traits or policies desired by the election, you have"
									+ " to play your best combo of these cards. A new election is chos"
									+ "en each round\n" + " and the game is best-of-five.\n"
									+ "Now, we'll look at each type of card.");
					JOptionPane.showMessageDialog(frame,
							"The first type of card is an ELECTION CARD. There is one for each US elec"
									+ "tion historically. In the 'Context' section, the election will" + " imply wh"
									+ "at attributes\n"
									+ "to focus on, or what policies are important that year. Each elect"
									+ "ion has 2 'desired attributes' from the President cards,\n"
									+ "1 'main' policy issue, and two 'side' policy issues. The details"
									+ " of scoring will be explained last.\n"
									+ "Finally, each election has a 'swing region', and if your Preside"
									+ "nt is from the same region, you receive a bonus.");
					JOptionPane.showMessageDialog(frame,
							"The next type of card is a PRESIDENT CARD. They are a combination of all"
									+ " US presidents, and other prominent political figures. These ca" + "rds have"
									+ " quite a decent amount of information.\n"
									+ "First, each president has 4 attributes, EXP, INFL, POL, and NAM. "
									+ "Each election year will desire 2 of these attributes, \n"
									+ "and the attributes are rated 1-10, with 5 being average. Here's w"
									+ "hat they mean: \n"
									+ "Experience (EXP): How long the candidate has been involved in pol"
									+ "itics, and their highest office.\n"
									+ "Influence (INFL): How well a candidate can convince and sway peop"
									+ "le, and other personality aspects like leadership, charisma, " + "etc.\n"
									+ "Policy (POL): How well-versed the candidate is in ideas and polic"
									+ "y, and their knowledge of the issues.\n"
									+ "Name Recogntion (NAM): How well-known the candidate is to the gen"
									+ "eral public, including outside of political life.\n"
									+ "The cards have a little more information. One of them is their ho"
									+ "me region, giving a bonus if this matches the election's swing " + "region.\n"
									+ "Also, the cards show the candidates ideology. These are used to "
									+ "get bonuses if the policies played 'match' the candidates ideo" + "logy.\n"
									+ "Finally, each candidate also has a 'Marquee Policy', which always"
									+ " gives them a bonus if paired with the candidate.");
					JOptionPane.showMessageDialog(frame,
							"Last, here is the specific breakdown of the scoring for each round: \n"
									+ "First, add the two key attributes of the president card, based "
									+ "on the desired attributes of the election.\n"
									+ "You get 3 points if one of your policies is the main issue, and"
									+ " 2 for either of the side issues.\n"
									+ "For each policy that matches the president's ideology, you get" + " 2 points.\n"
									+ "If you played the president's Marquee Policy, you get 3 points"
									+ " (and the ideology match always).\n"
									+ "Finally, you get 3 points if your candidates' region is the same"
									+ " as the swing region.\n"
									+ "Whichever player has the highest round score wins, and "
									+ "best-of-five wins it all.\n"
									+ "In case of a tie, the first tiebreaker is the sum of the "
									+ "candidate's attributes, then just chosen randomly.\n" + "Good luck!");
				} else if (helpMode == 0) {
					cardInfoMode = !cardInfoMode;
					board.draw();
					user_cards.paintCards();
				}
			}
		});
		gbc.gridy = 1;
		control_panel.add(help, gbc);

		// Load the icons for both modes
		ImageIcon policiesIcon = new ImageIcon(getClass().getClassLoader().getResource(prefix + "files/policies.PNG"));
		ImageIcon policiesHoverIcon = new ImageIcon(getClass().getClassLoader().getResource(prefix + "files/policieshover.PNG"));
		ImageIcon presidentsIcon = new ImageIcon(getClass().getClassLoader().getResource(prefix + "files/presidents.PNG"));
		ImageIcon presidentsHoverIcon = new ImageIcon(getClass().getClassLoader().getResource(prefix + "files/presidentshover.PNG"));
		ImageIcon policiesClickIcon = new ImageIcon(getClass().getClassLoader().getResource(prefix + "files/policiesclicked.PNG"));
		ImageIcon presidentsClickIcon = new ImageIcon(getClass().getClassLoader().getResource(prefix + "files/presidentsclicked.PNG"));

		// Pre-scale all icons during initialization using buttonWidth and buttonHeight
		ImageIcon policiesIconScaled = new ImageIcon(policiesIcon.getImage().getScaledInstance(buttonWidth, buttonHeight, Image.SCALE_SMOOTH));
		ImageIcon policiesHoverIconScaled = new ImageIcon(policiesHoverIcon.getImage().getScaledInstance(buttonWidth, buttonHeight, Image.SCALE_SMOOTH));
		ImageIcon policiesClickIconScaled = new ImageIcon(policiesClickIcon.getImage().getScaledInstance(buttonWidth, buttonHeight, Image.SCALE_SMOOTH));
		ImageIcon presidentsIconScaled = new ImageIcon(presidentsIcon.getImage().getScaledInstance(buttonWidth, buttonHeight, Image.SCALE_SMOOTH));
		ImageIcon presidentsHoverIconScaled = new ImageIcon(presidentsHoverIcon.getImage().getScaledInstance(buttonWidth, buttonHeight, Image.SCALE_SMOOTH));
		ImageIcon presidentsClickIconScaled = new ImageIcon(presidentsClickIcon.getImage().getScaledInstance(buttonWidth, buttonHeight, Image.SCALE_SMOOTH));

		// Create the swap button with initial policies icon
		final JButton swap = new JButton();

		// Set button preferences
		swap.setContentAreaFilled(false); // Remove default button background
		swap.setBorderPainted(false); // Remove button border
		swap.setPreferredSize(new Dimension(buttonWidth, buttonHeight)); // Set preferred size

		// Initialize the button to start with the "policies" icon and add the initial component
		swap.setIcon(policiesIconScaled);
		swap.setRolloverIcon(policiesHoverIconScaled);
		swap.setPressedIcon(policiesClickIconScaled);


		// Initially display the president deck at the bottom
		frame.add(user_cards, BorderLayout.SOUTH);

		swap.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				player.playSoundEffect("files/playbutton.MP3");

				if (!swapmode) {
					// Switch to policies view
					swap.setIcon(presidentsIconScaled);
		            swap.setRolloverIcon(presidentsHoverIconScaled);
		            swap.setPressedIcon(presidentsClickIconScaled);
					frame.add(userPolicies, BorderLayout.SOUTH);
					frame.remove(user_cards);
					swapmode = true;
				} else {
					// Switch to presidents view
					swap.setIcon(policiesIconScaled);
		            swap.setRolloverIcon(policiesHoverIconScaled);
		            swap.setPressedIcon(policiesClickIconScaled);
					frame.add(user_cards, BorderLayout.SOUTH);
					frame.remove(userPolicies);
					swapmode = false;
				}
				user_cards.paintCards();
				up.paintCards();
				frame.revalidate();
				frame.repaint();
			}
		});




		gbc.gridy = 4;
		control_panel.add(swap, gbc);
		
		// Load the settings icon and hover icon
		ImageIcon settingsIcon = new ImageIcon(getClass().getClassLoader().getResource(prefix + "files/settings.PNG"));
		ImageIcon settingsHoverIcon = new ImageIcon(getClass().getClassLoader().getResource(prefix + "files/settingshover.PNG"));
		ImageIcon settingsClickedIcon = new ImageIcon(getClass().getClassLoader().getResource(prefix + "files/settingsclicked.PNG"));

		// Create the stats button with the settings icon
		final JButton stats = new JButton(new ImageIcon(settingsIcon.getImage().getScaledInstance(buttonWidth, buttonHeight, Image.SCALE_SMOOTH)));

		// Set the rollover icon to the hover image
		stats.setRolloverIcon(new ImageIcon(settingsHoverIcon.getImage().getScaledInstance(buttonWidth, buttonHeight, Image.SCALE_SMOOTH)));
		stats.setPressedIcon(new ImageIcon(settingsClickedIcon.getImage().getScaledInstance(buttonWidth, buttonHeight, Image.SCALE_SMOOTH)));

		// Set button preferences
		stats.setContentAreaFilled(false); // Remove default button background
		stats.setBorderPainted(false);     // Remove button border
		stats.setPreferredSize(new Dimension(buttonWidth, buttonHeight)); // Set preferred size


		stats.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				player.playSoundEffect("files/playbutton.MP3");

				long endTime = System.nanoTime();
				long totalTime = endTime - startTime;
				long totalTimeInSeconds = totalTime / 1000000000;
				long totalTimeMinutes = totalTimeInSeconds / 60;
				long totalTimeSeconds = totalTimeInSeconds % 60;

				int winPct;
				if (cumulativeGames == 0) {
					winPct = 0;
				} else {
					winPct = (int) (Math.round(((float) (cumulativeWins) * 100.0) / cumulativeGames));
				}

				Object[] settingOptions = { "Change Your Name", "Change AI Difficulty", "Change Deck Choice",
						"Exit Game" };
				int settingToChange = JOptionPane.showOptionDialog(frame,
						"Stats for " + election.getPlayer1().getName() + " this session: \n" + "Total play time: "
								+ totalTimeMinutes + " minutes, " + totalTimeSeconds + " seconds. \n" + "Total wins: "
								+ cumulativeWins + " wins out of " + cumulativeGames + " (" + winPct + "%). \n \n"
								+ "Select one of the settings below to change (may reset game):",
						"Settings", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null,
						settingOptions, -1);

				// Change Name
				if (settingToChange == 0) {
					String playername = election.getActivePlayer().getName();

					playername = JOptionPane.showInputDialog(frame, "What would you like to change your name to?");
					election.nameActivePlayer(playername);
					setLabel(status);
					status.repaint();
				}

				// Change Difficulty
				if (settingToChange == 1) {
					Object[] diffs = { "Easy", "Medium", "Hard", "Impossible" };
					int diff = JOptionPane.showOptionDialog(frame, "What would you like to change the AI difficulty to?",
							"Select Difficulty", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, diffs,
							election.aiDifficulty);
					String difficulty = "Medium";
					switch (diff) {
					case 0:
						difficulty = "Easy";
						break;
					case 1:
						difficulty = "Medium";
						break;
					case 2:
						difficulty = "Hard";
						break;
					case 3:
						difficulty = "Impossible";
						break;
					default:
						difficulty = election.aiDifficulty;
						break;
					}
					election.setAIDifficulty(difficulty);
				}

				// Change Deck
				if (settingToChange == 2) {
					// Add back memes?
					Object[] cardDecks = { "Presidents Only", "Standard", "Expanded", "Generational",
							"Custom" };
					int deck = JOptionPane.showOptionDialog(frame, "Choose the game deck:", "Select Deck",
							JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, cardDecks, cardDecks[1]);

					if (deck == 0) {
						election.reset("presidentsonly", null, 0.0, 5);
						deckSet = "presidentsonly";
					}
					if (deck == 1) {
						election.reset("standard", null, 0.0, 5);
						deckSet = "standard";
					}
					if (deck == 2) {
						election.reset("expanded", null, 0.0, 5);
						deckSet = "expanded";
					}
					if (deck == 3) {
						election.reset("generational", null, 0.0, 5);
						deckSet = "generational";
					}
					if (deck == 4) {
						// Find a way to screen this so it doesn't get broken. Also find ways to be more
						// creative with this, maybe
						// add generational cards, sort all presidents/nonpresidents and get the top 50
						// from there, etc.
						try {
							CardData.clearRemembered();

							String[] customTags = { "President", "Generational", "Founding Era",
							        "Jacksonian Era", "Civil War Era", "Reconstruction Era", "Progressive Era",
							        "New Deal Era", "Civil Rights Era", "Reagan Era", "Modern Era", "Present Era" };

							JPanel panel = new JPanel();
							panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
							JLabel message = new JLabel("Select all tags you would like to include in the deck:");
							panel.add(message);

							// Create the "Select All" checkbox
							JCheckBox selectAllCheckBox = new JCheckBox("Select All");
							panel.add(selectAllCheckBox);

							// Create the tag checkboxes
							List<JCheckBox> checkBoxes = new ArrayList<>();
							for (String tag : customTags) {
							    JCheckBox checkBox = new JCheckBox(tag);
							    checkBoxes.add(checkBox);
							    panel.add(checkBox);
							}

							// Add an action listener to the "Select All" checkbox
							selectAllCheckBox.addActionListener(new ActionListener() {
							    @Override
							    public void actionPerformed(ActionEvent e) {
							        boolean selectAll = selectAllCheckBox.isSelected();
							        for (JCheckBox checkBox : checkBoxes) {
							            checkBox.setSelected(selectAll);
							        }
							    }
							});

							int result = JOptionPane.showConfirmDialog(frame, panel, "Select tags",
							        JOptionPane.OK_CANCEL_OPTION);

							if (result == JOptionPane.OK_OPTION) {
							    ArrayList<String> tempSelectedTags = new ArrayList<>();
							    for (JCheckBox checkBox : checkBoxes) {
							        if (checkBox.isSelected()) {
							            tempSelectedTags.add(checkBox.getText());
							        }
							    }
							    String[] selectedTagsArray = new String[tempSelectedTags.size()];
							    selectedTagsArray = tempSelectedTags.toArray(selectedTagsArray);

							    double minRating = 0.0;
							    try {
							        String input = JOptionPane.showInputDialog(frame, "What would you like the minimum weighted average rating of a card to be?");
							        minRating = Double.parseDouble(input);
							    } catch (NumberFormatException er) {
							        minRating = 0.0;
							    }

							    deckSet = "custom";
							    election.reset("custom", selectedTagsArray, minRating, 5);
							} else {
							    JOptionPane.showMessageDialog(board,
							            "Error: One of your inputs was invalid. Standard deck will be used.", "Error",
							            JOptionPane.ERROR_MESSAGE);
							    election.reset("standard", null, 0.0, 5);
							    deckSet = "standard";
							}

						} catch (Exception ex) {
							JOptionPane.showMessageDialog(board,
									"Error: One of your inputs was invalid. Standard deck will be used.", "Error",
									JOptionPane.ERROR_MESSAGE);
							election.reset("standard", null, 0.0, 5);
							deckSet = "standard";
						}
					}
				}
				/* Change card size
				if (settingToChange == 3) {
				    try {
				        int newCardSize = Integer.parseInt(JOptionPane.showInputDialog(frame,
				                "What would you like the new card size to be (current is " + cardSize + ")?"));
				        if (newCardSize < 10) {
				            newCardSize = 10;
				        }
				        cardSize = newCardSize;

				        // Calculate new font size based on cardSize
				        int newFontSize = (int)(16 * (cardSize / 225.0));  // Example scaling, adjust as needed
				        Font newFont = new Font("Dialog", Font.PLAIN, newFontSize);

				        // Update JLabel components recursively within the frame
				        updateFonts(frame, newFont);

				        // Update JOptionPane font
				        UIManager.put("OptionPane.messageFont", newFont);
				        UIManager.put("OptionPane.buttonFont", newFont);

				        user_cards.paintCards();

				        // Update the JScrollPane size
				        userPolicies.setPreferredSize(new Dimension((int)(cardSize/225.0*3600), (int)(cardSize/225.0*325)));
				        userPolicies.revalidate();
				        userPolicies.repaint();

				        ai_cards.removeAll();  // Clear existing cards
				        for (int i = 0; i < 5; i++) {
				            URL imgURL = getClass().getClassLoader().getResource(prefix + "files/aicard.PNG");
				            ImageIcon icon = new ImageIcon(new ImageIcon(imgURL).getImage().getScaledInstance(
				                (int) (0.266666667 * cardSize), (int) (0.351111111 * cardSize), Image.SCALE_SMOOTH));
				            JLabel aicardlabel = new JLabel(icon);
				            ai_cards.add(aicardlabel);
				        }
				        ai_cards.revalidate();
				        ai_cards.repaint();

				        // Update deck images with new scaled instances
				        elecdeck.setIcon(new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource(prefix + "files/electionsdeck.PNG"))
				                .getImage().getScaledInstance((int)(cardSize/225.0*119), (int)(cardSize/225.0*91), Image.SCALE_SMOOTH)));
				        prezdeck.setIcon(new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource(prefix + "files/presidentdeck.PNG"))
				                .getImage().getScaledInstance((int)(cardSize/225.0*119), (int)(cardSize/225.0*91), Image.SCALE_SMOOTH)));
				        poldeck.setIcon(new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource(prefix + "files/policydeck.PNG"))
				                .getImage().getScaledInstance((int)(cardSize/225.0*119), (int)(cardSize/225.0*91), Image.SCALE_SMOOTH)));

				        // Redraw decks panel to reflect new sizes
				        decks.setPreferredSize(new Dimension((int)(cardSize/225.0*175), (int)(cardSize/225.0*200)));
				        
				        // Update control panel buttons' sizes and icons
				        for (Component comp : control_panel.getComponents()) {
				            if (comp instanceof JButton) {
				                JButton button = (JButton) comp;
				                button.setPreferredSize(new Dimension((int)(cardSize / 225.0 * 160), (int)(cardSize / 225.0 * 85)));  // Maintain proportional size

				                // Scale the icon based on the new size
				                ImageIcon icon = (ImageIcon) button.getIcon();
				                if (icon != null) {
				                    button.setIcon(new ImageIcon(icon.getImage().getScaledInstance(
				                            (int)(cardSize / 225.0 * 160), (int)(cardSize / 225.0 * 85), Image.SCALE_SMOOTH)));
				                }
				                
				                ImageIcon rolloverIcon = (ImageIcon) button.getRolloverIcon();
				                if (rolloverIcon != null) {
				                    button.setRolloverIcon(new ImageIcon(rolloverIcon.getImage().getScaledInstance(
				                            (int)(cardSize / 225.0 * 160), (int)(cardSize / 225.0 * 85), Image.SCALE_SMOOTH)));
				                }
				                
				                ImageIcon pressedIcon = (ImageIcon) button.getPressedIcon();
				                if (pressedIcon != null) {
				                    button.setPressedIcon(new ImageIcon(pressedIcon.getImage().getScaledInstance(
				                            (int)(cardSize / 225.0 * 160), (int)(cardSize / 225.0 * 85), Image.SCALE_SMOOTH)));
				                }
				            }
				        }
				        control_panel.revalidate();
				        control_panel.repaint();

				        board.draw();

				        // Force the frame to lay out its components again
				        frame.invalidate();  // Invalidate the layout
				        frame.validate();    // Validate the layout again to apply changes
				        frame.repaint();     // Repaint the whole frame to reflect changes
				    } catch (Exception ex) {
				        JOptionPane.showMessageDialog(board, "Error: Your input was invalid.", "Error",
				                JOptionPane.ERROR_MESSAGE);
				    }
				}*/
				/*
				// Music Settings
				if (settingToChange == 4) {
				    Object[] musicOptions = { "Stop", "Resume", "Next Track", "Previous Track", "Close" };

				    int musicChoice = JOptionPane.showOptionDialog(frame, "Current song: " + player.getCurrentTrackName(),
				            "Music Settings", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, musicOptions, musicOptions[4]);

				    switch (musicChoice) {
				        case 0: // Stop
				            player.stop();
				            break;
				        case 1: // Resume
				            player.play();
				            break;
				        case 2: // Next Track
				            player.playNext();
				            break;
				        case 3: // Previous Track
				            player.playPrevious();
				            break;
				        case 4: // Close
				            // Do nothing, just close the dialog
				            break;
				    }
				}
				*/

				
				// Exit Game
				if (settingToChange == 3) {
					frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
				}


				if (settingToChange == 2) {
					// This is not WAI
					if (election.p2w > 0) {
						cumulativeGames++;
					}
					election.reset(deckSet, null, 0.0, 5);
					setLabel(status);
					status.repaint();
					user_cards.reset();
					up.reset();
					board.reset();
				}

			}
		});
		gbc.gridy = 2;
		control_panel.add(stats, gbc);
		
		// Loop through all components in the control_panel
		for (Component comp : control_panel.getComponents()) {
		    if (comp instanceof JButton) {
		        JButton button = (JButton) comp;
		        button.setContentAreaFilled(false); // Remove default button background
		        button.setBorderPainted(false);     // Remove button border
		        button.setFocusPainted(false);      // Remove the focus highlight
		    }
		}

		
		control_panel.setPreferredSize(new Dimension((int)(cardSize/225.0*175), (int)(cardSize/225.0*200)));
		//control_panel.setLayout(new GridLayout(5, 1));
		frame.add(control_panel, BorderLayout.EAST);
		
		// Calculate initial font size based on the initial card size
		int initialFontSize = (int)(16 * (cardSize / 225.0));
		Font initialFont = new Font("Dialog", Font.PLAIN, initialFontSize);

		// Update JLabel components recursively within the frame
		updateFonts(frame, initialFont);

		// Update JOptionPane font
		UIManager.put("OptionPane.messageFont", initialFont);
		UIManager.put("OptionPane.buttonFont", initialFont);

	    user_cards.hideCards(5);
		
	    /*
	    System.out.println("AI Cards: " + ai_cards.getSize());
	    System.out.println("Decks : " + decks.getSize());
	    System.out.println("Board: " + board.getSize());
	    System.out.println("Control Panel: " + control_panel.getSize());
	    System.out.println("User Cards: " + user_cards.getSize());
	    System.out.println("Total Frame: " + frame.getSize());
	    */
	    

		/******************************************************
		 ********************** START GAME**********************
		 ******************************************************/

		
		/**********
		 * Login *
		 **********/
		
		

		// First get the active account. Have a check for if activeAccount ends up being
		// null.
		BufferedReader reader = null;
		String activeAccount = null;
		try {
			reader = new BufferedReader(new FileReader("files/logininfo.txt"));

			// Read the first line of the file
			activeAccount = reader.readLine();
		} catch (IOException e) {
			// Handle the exception
			e.printStackTrace();
		} finally {
			// Close the file
			try {
				if (reader != null) {
					reader.close();
				}
			} catch (IOException e) {
				// Handle the exception
				e.printStackTrace();
			}
		}

		// Next, get the settings for the active account. Include some sort of check for
		// if the account isn't found.
		BufferedReader reader2 = null;
		String accountSettings = null;
		try {
			reader2 = new BufferedReader(new FileReader("files/logininfo.txt"));
			String line;
			while ((line = reader2.readLine()) != null) {
				if (line.startsWith(activeAccount)) {
					accountSettings = line;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			accountSettings = null;
		} finally {
			try {
				if (reader2 != null) {
					reader2.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		String[] accountSettingsArray = accountSettings.split(",");

		// Now, open up the login menu
		Object[] loginOptions = { "Continue as " + activeAccount, "Change Account", "Continue as Guest" };
		int loginMode = JOptionPane.showOptionDialog(frame,
				"Welcome back to Election Game " + activeAccount + "! Login below:", "Login",
				JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, loginOptions, loginOptions[2]);

		// User continues as active account
		if (loginMode == 0) {
			mode = false;
			election.namePlayer(activeAccount);
			election.namePlayer2("CPU");
			election.setAIDifficulty(accountSettingsArray[1]);
			election.reset(accountSettingsArray[2], null, 0.0, 5);
			deckSet = accountSettingsArray[2];
			setLabel(status);
		}

		// User changes account. The following functions can be heavily refactored, and
		// needs to have way better error handling.
		else if (loginMode == 1) {
			mode = false;
			// First, get a list of the account names.
			BufferedReader reader3 = null;
			String[] accountList = null;
			try {
				reader3 = new BufferedReader(new FileReader("files/logininfo.txt"));
				List<String> entries = new ArrayList<>();
				String line;
				int lineNumber = 1;
				while ((line = reader3.readLine()) != null) {
					if (lineNumber >= 3) {
						String[] parts = line.split(",");
						entries.add(parts[0]);
					}
					lineNumber++;
				}
				entries.add(0, "Edit Accounts");
				accountList = entries.toArray(new String[0]);
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					if (reader3 != null) {
						reader3.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			int loginAccountOption = JOptionPane.showOptionDialog(frame,
					"Welcome back to Election Game " + activeAccount + "! Login below:", "Login",
					JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, accountList, accountList[0]);
			// Continue as the selected account
			if (loginAccountOption > 0) {
				// First, pull the account settings.
				activeAccount = accountList[loginAccountOption];
				reader2 = null;
				accountSettings = null;
				try {
					reader2 = new BufferedReader(new FileReader("files/logininfo.txt"));
					String line;
					while ((line = reader2.readLine()) != null) {
						if (line.startsWith(activeAccount)) {
							accountSettings = line;
						}
					}
				} catch (IOException e) {
					e.printStackTrace();
					accountSettings = null;
				} finally {
					try {
						if (reader2 != null) {
							reader2.close();
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				accountSettingsArray = accountSettings.split(",");
				// Next, change the active account.
				BufferedReader reader4 = null;
				BufferedWriter writer = null;
				try {
					reader4 = new BufferedReader(new FileReader("files/logininfo.txt"));
					List<String> lines = new ArrayList<>();
					String line;
					while ((line = reader4.readLine()) != null) {
						lines.add(line);
					}
					lines.set(0, activeAccount);

					writer = new BufferedWriter(new FileWriter("files/logininfo.txt"));
					for (String modifiedLine : lines) {
						writer.write(modifiedLine);
						writer.newLine();
					}
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					try {
						if (reader4 != null) {
							reader4.close();
						}
						if (writer != null) {
							writer.close();
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				// Finally, start the game.
				election.namePlayer(activeAccount);
				election.namePlayer2("CPU");
				election.setAIDifficulty(accountSettingsArray[1]);
				election.reset(accountSettingsArray[2], null, 0.0, 5);
				deckSet = accountSettingsArray[2];
				setLabel(status);
			}
			// Edit the accounts
			else {
				// First find what choice the user wants to make
				// WIP
				Object[] editOptions = { "Delete Account", "Edit Account", "New Account" };
				int editAccountOption = JOptionPane.showOptionDialog(frame, "How would you like to edit the accounts?",
						"Login", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, editOptions,
						editOptions[2]);
				// Delete Account
				if (editAccountOption == 0) {
					// First, choose which account to delete
					int deleteOption = JOptionPane.showOptionDialog(frame, "Which account would you like to delete?",
							"Login", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, accountList,
							accountList[0]);
					String acctToDelete = accountList[deleteOption];
					// Delete the account. This function doesn't work either, it deletes the first
					// line
					reader = null;
					BufferedWriter writer = null;
					try {
						reader = new BufferedReader(new FileReader("files/logininfo.txt"));
						List<String> lines = new ArrayList<>();
						String line;
						boolean deleted = false;
						while ((line = reader.readLine()) != null) {
							if (!line.startsWith(acctToDelete) || deleted) {
								lines.add(line);
							} else {
								deleted = true;
							}
						}

						writer = new BufferedWriter(new FileWriter("files/logininfo.txt"));
						for (String modifiedLine : lines) {
							writer.write(modifiedLine);
							writer.newLine();
						}
					} catch (IOException e) {
						e.printStackTrace();
					} finally {
						try {
							if (reader != null) {
								reader.close();
							}
							if (writer != null) {
								writer.close();
							}
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				} else if (editAccountOption == 1 || editAccountOption == 2) {
					// Choose account to edit
					String newAcctName = null;
					if (editAccountOption == 1) {
						int editOption = JOptionPane.showOptionDialog(frame, "Which account would you like to edit?",
								"Login", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, accountList,
								accountList[0]);
						newAcctName = accountList[editOption];
					} else {
						// Choose new settings
						// Name
						newAcctName = JOptionPane.showInputDialog(frame, "What would you like the account name to be?");
					}
					// AI difficulty
					Object[] diffs = { "Easy", "Medium", "Hard", "Impossible" };
					int diff = JOptionPane.showOptionDialog(frame, "Choose the difficulty of the AI:",
							"Select Difficulty", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, diffs,
							diffs[1]);
					String aiDifficulty = (String) diffs[diff];
					// Deck choice
					Object[] cardDecks = { "Presidents Only", "Standard", "Expanded", "Memes", "Generational",
							"Custom" };
					int deck = JOptionPane.showOptionDialog(frame, "Choose the game deck:", "Select Deck",
							JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, cardDecks, cardDecks[1]);
					String deckChoice = (String) cardDecks[deck];
					String acctLine = newAcctName + "," + aiDifficulty + "," + deckChoice;
					// Append the account to the file or edit the account. EDITING DOESNT WORK
					reader = null;
					BufferedWriter writer = null;
					try {
						reader = new BufferedReader(new FileReader("files/logininfo.txt"));
						List<String> lines = new ArrayList<>();
						String line;
						boolean replaced = false;
						while ((line = reader.readLine()) != null) {
							if (!line.startsWith(acctLine.split(",")[0]) || replaced) {
								lines.add(line);
							} else {
								lines.add(acctLine);
								replaced = true;
							}
						}

						if (!replaced) {
							lines.add(acctLine);
						}

						writer = new BufferedWriter(new FileWriter("files/logininfo.txt"));
						for (String modifiedLine : lines) {
							writer.write(modifiedLine);
							writer.newLine();
						}
					} catch (IOException e) {
						e.printStackTrace();
					} finally {
						try {
							if (reader != null) {
								reader.close();
							}
							if (writer != null) {
								writer.close();
							}
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					// Now, begin the game.
					JOptionPane.showMessageDialog(frame,
							"Account settings updated. You will now begin a new game with the updated account!");
					// First, change the active account.
					BufferedReader reader4 = null;
					writer = null;
					try {
						reader4 = new BufferedReader(new FileReader("files/logininfo.txt"));
						List<String> lines = new ArrayList<>();
						String line;
						while ((line = reader4.readLine()) != null) {
							lines.add(line);
						}
						lines.set(0, newAcctName);

						writer = new BufferedWriter(new FileWriter("files/logininfo.txt"));
						for (String modifiedLine : lines) {
							writer.write(modifiedLine);
							writer.newLine();
						}
					} catch (IOException e) {
						e.printStackTrace();
					} finally {
						try {
							if (reader4 != null) {
								reader4.close();
							}
							if (writer != null) {
								writer.close();
							}
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					// Finally, start the game.
					election.namePlayer(newAcctName);
					election.namePlayer2("CPU");
					election.setAIDifficulty(aiDifficulty);
					election.reset(deckChoice, null, 0.0, 5);
					deckSet = deckChoice;
					setLabel(status);
				}
			}
			
		
		
			
		
		} else {

			/*********************
			 * Normal Guest Login *
			 **********************/

			Object[] options = { "1-Player", "2-Player"};
			int gameMode = JOptionPane.showOptionDialog(frame, "Welcome to Election Game! Please select your game mode:",
					"Select Game Mode", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options,
					options[0]);

			if (gameMode == -1 || gameMode == 0 || gameMode == 2) {
				mode = false;
			} else {
				mode = true;
			}

				// Name Player 1
				String playername = "Player 1";
				if (mode) {
					playername = JOptionPane.showInputDialog(frame,
							"Hello and welcome to the Election Game! Player 1, please enter your name: ");
				} else {
					playername = JOptionPane.showInputDialog(frame,
							"Hello and welcome to the Election Game! Please enter your name: ");
					election.namePlayer2("CPU");
				}
				election.namePlayer(playername);
				setLabel(status);
				status.repaint();

				// Name Player 2 or set AI difficulty
				if (mode) {
					String player2name = JOptionPane.showInputDialog(frame, "Player 2, please enter your name: ");
					election.namePlayer2(player2name);
					setLabel(status);
					status.repaint();
				} else {
					Object[] diffs = { "Easy", "Medium", "Hard", "Impossible" };
					int diff = JOptionPane.showOptionDialog(frame, "Choose the difficulty of the AI:",
							"Select Difficulty", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, diffs,
							diffs[1]);
					String difficulty = "Medium";
					switch (diff) {
					case 0:
						difficulty = "Easy";
						break;
					case 1:
						difficulty = "Medium";
						break;
					case 2:
						difficulty = "Hard";
						break;
					case 3:
						difficulty = "Impossible";
						break;
					default:
						break;
					}
					election.setAIDifficulty(difficulty);
				}

				// Add back "memes"?
				Object[] cardDecks = { "Presidents Only", "Standard", "Expanded", "Generational", "Custom" };
				int deck = JOptionPane.showOptionDialog(frame, "Choose the game deck:", "Select Deck",
						JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, cardDecks, cardDecks[1]);

				if (deck == 0) {
					election.reset("presidentsonly", null, 0.0, 5);
					deckSet = "presidentsonly";
				}
				if (deck == 1) {
					election.reset("standard", null, 0.0, 5);
					deckSet = "standard";
				}
				if (deck == 2) {
					election.reset("expanded", null, 0.0, 5);
					deckSet = "expanded";
				}
				if (deck == 3) {
					election.reset("generational", null, 0.0, 5);
					deckSet = "generational";
				}
				if (deck == 4) {
					// Find a way to screen this so it doesn't get broken. Also find ways to be more
					// creative with this, maybe
					// add generational cards, sort all presidents/nonpresidents and get the top 50
					// from there, etc.
					try {
						// Add back Meme for non-JAR?
						String[] customTags = { "President", "Generational", "Founding Era", "Jacksonian Era",
								"Civil War Era", "Reconstruction Era", "Progressive Era", "New Deal Era",
								"Civil Rights Era", "Reagan Era", "Modern Era", "Present Era" };

						JPanel panel = new JPanel();
						panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
						JLabel message = new JLabel("Select the all tags you would like to include in the deck:");
						panel.add(message);

						for (String tag : customTags) {
							JCheckBox checkBox = new JCheckBox(tag);
							panel.add(checkBox);
						}

						int result = JOptionPane.showConfirmDialog(frame, panel, "Select tags",
								JOptionPane.OK_CANCEL_OPTION);

						if (result == JOptionPane.OK_OPTION) {
							ArrayList<String> tempSelectedTags = new ArrayList<String>();
							for (Component component : panel.getComponents()) {
								if (component instanceof JCheckBox) {
									JCheckBox checkBox = (JCheckBox) component;
									if (checkBox.isSelected()) {
										tempSelectedTags.add(checkBox.getText());
									}
								}
							}
							String[] selectedTagsArray = new String[tempSelectedTags.size()];
							selectedTagsArray = tempSelectedTags.toArray(selectedTagsArray);

							// This might crash stuff
							double minRating = Double.parseDouble(JOptionPane.showInputDialog(frame,
									"What would you like the minimum weighted average rating of a card to be?"));

							deckSet = "custom";
							election.reset("custom", selectedTagsArray, minRating, 5);
						} else {
							JOptionPane.showMessageDialog(board,
									"Error: One of your inputs was invalid. Standard deck will be used.", "Error",
									JOptionPane.ERROR_MESSAGE);
							election.reset("standard", null, 0.0, 5);
							deckSet = "standard";
						}
					} catch (Exception ex) {
						JOptionPane.showMessageDialog(board,
								"Error: One of your inputs was invalid. Standard deck will be used.", "Error",
								JOptionPane.ERROR_MESSAGE);
						election.reset("standard", null, 0.0, 5);
						deckSet = "standard";
					}
			}
		}
		user_cards.paintCards();
		starting = false;
		board.draw();
		
		frame.remove(loadingScreen);
		frame.revalidate();
	    frame.repaint();
	}

	/*
	 * This is my first inner class used to represent the deck of the user. It does
	 * things like draw the user's cards, and lets the user click on them to place
	 * them.
	 */
	@SuppressWarnings("serial")
	public class UserDeck extends JPanel {

	    private List<President> hand;
	    private Image backgroundImage; // To hold the background image
	    
	    private int scaledCardWidth;
	    private int scaledCardHeight;
	    private Map<String, ImageIcon> imageCache; // Move the cache outside of paintCards()

	    private SoundtrackPlayer player;

	    public int BOARD_WIDTH = 600;
	    public int BOARD_HEIGHT = (int)(cardSize/225.0*325);

	    public UserDeck(SoundtrackPlayer player) {
	        setFocusable(true);
	        this.hand = election.getActivePlayer().getHand();
	        this.player = player;
	        this.scaledCardWidth = cardSize;
	        this.scaledCardHeight = (int) Math.round(1.32713755 * cardSize);
	        this.imageCache = new HashMap<>(); // Initialize the cache
	        paintCards();

	        backgroundImage = new ImageIcon(getClass().getClassLoader().getResource(prefix + "files/backgroundfull.PNG")).getImage();

	        addMouseListener(new MouseAdapter() {
	            @Override
	            public void mouseReleased(MouseEvent e) {
	                board.draw();
	                paintCards();
	                repaint();
	            }
	        });
	    }

	    // This is the most important method. Loop through the hand, draw all the cards
	    public void paintCards() {
	            try {
	                // Remove only if necessary, and update the UI at the end
	                if (this.getComponentCount() > 0) {
	                    this.removeAll();
	                }

	                this.hand = election.getActivePlayer().getHand();

	                // Update the cache with any new cards
	                updateImageCache();

	                for (President curr : hand) {
	                    ImageIcon img = imageCache.get(curr.getImageURL());
	                    final JButton usercd = new JButton(img);
	                    if (cardInfoMode) {
	                        usercd.setIcon(null);
	                        usercd.setText("<html>" + curr.getCardInfo() + "</html>");
	                    }
	                    this.add(usercd);
	                    usercd.setPreferredSize(new Dimension(scaledCardWidth, scaledCardHeight));

	                    // This makes all of the cards buttons that play the card if clicked
	                    usercd.addMouseListener(new MouseAdapter() {
	                        public void mouseReleased(MouseEvent e) {
	                            player.playSoundEffect("files/cardplay.MP3");
	                            if (election.getActivePinCard() != null) {
	                                election.getActivePlayer().add(election.getActivePinCard());
	                            }
	                            election.activePinCard(curr);
	                            election.getActivePlayer().place(curr);
	                            board.draw();
	                            paintCards();
	                        }
	                    });
	                }

	                this.updateUI();  // Call updateUI only once at the end

	            } catch (NullPointerException e) {
	                StringBuilder message = new StringBuilder("One of the cards has an error, please exit the game. Here is the hand:");
	                for (President card : hand) {
	                    message.append("\n").append(card.toString());
	                }
	                JOptionPane.showMessageDialog(this, message.toString(), "Error", JOptionPane.ERROR_MESSAGE);
	            }
	        
	    }

	    // Method to update the image cache
	    private void updateImageCache() {
	    	List<String> currentImages = new ArrayList<>();
	    	for (President curr : hand) {
	    	    currentImages.add(curr.getImageURL());
	    	    if (!imageCache.containsKey(curr.getImageURL())) {
	    	        ImageIcon img = new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource(prefix + curr.getImageURL()))
	    	                .getImage().getScaledInstance(scaledCardWidth, scaledCardHeight, Image.SCALE_SMOOTH));
	    	        imageCache.put(curr.getImageURL(), img);
	    	    }
	    	}
	    	// Remove images that are no longer in use from the cache
	    	imageCache.keySet().retainAll(new HashSet<>(currentImages));
	    }

	    public void reset() {
	    	this.removeAll();
			this.updateUI();
			hand = election.userHand();
			paintCards();

			requestFocusInWindow();
	    }


	    public void hideCards(int cds) {
	        this.removeAll();
	        this.updateUI();

	        for (int i = 0; i < cds; i++) {
	            ImageIcon img = new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource(prefix + "files/aicard.PNG"))
	                    .getImage().getScaledInstance(cardSize, (int) Math.round(1.32713755 * cardSize), Image.SCALE_SMOOTH));
	            final JButton usercd = new JButton(img);
	            this.add(usercd);
	            usercd.setPreferredSize(new Dimension(cardSize, (int) Math.round(1.32713755 * cardSize)));
	        }
	    }

	    @Override
	    public void paintComponent(Graphics g) {
	        super.paintComponent(g);
	        if (backgroundImage != null) {
	            // Convert Image to BufferedImage
	            BufferedImage bufferedImage = new BufferedImage(
	                backgroundImage.getWidth(null), backgroundImage.getHeight(null), BufferedImage.TYPE_INT_ARGB);

	            Graphics2D bGr = bufferedImage.createGraphics();
	            bGr.drawImage(backgroundImage, 0, 0, null);
	            bGr.dispose();

	            // Get the size of the original background image
	            int imageWidth = bufferedImage.getWidth();
	            int imageHeight = bufferedImage.getHeight();

	            // Calculate the cropping parameters for the UserDeck
	            int cropStartX = 0; // Start X at 0 (full width)
	            int cropStartY = (int) (785.0 / 1200.0 * imageHeight); // Start Y at 803/1200 from the top
	            int cropWidth = imageWidth; // Full width
	            int cropHeight = (int) (415.0 / 1200.0 * imageHeight); // Height of 397/1200 of the image

	            // Crop the BufferedImage
	            BufferedImage croppedImage = bufferedImage.getSubimage(cropStartX, cropStartY, cropWidth, cropHeight);

	            // Draw the cropped image scaled to the UserDeck's size
	            g.drawImage(croppedImage, 0, 0, getWidth(), getHeight(), this);
	        }
	    }


	    @Override
	    public Dimension getPreferredSize() {
	        return new Dimension(BOARD_WIDTH, (int)(cardSize/225.0*325));
	    }
	}


	// This is very similar to the UserDeck, but with some changes geared towards
	// policies
	@SuppressWarnings("serial")
	public class UserPolicies extends JPanel {

	    private List<Policy> hand;
	    private Image backgroundImage; // To hold the background image
	    
	    private int scaledCardWidth;
	    private int scaledCardHeight;
	    private Map<String, ImageIcon> imageCache; // Move the cache outside of paintCards()

	    private SoundtrackPlayer player;

	    public int BOARD_WIDTH = 3600;
	    public int BOARD_HEIGHT = (int)(cardSize/225.0*325);

	    public UserPolicies(SoundtrackPlayer player) {
	        setFocusable(true);
	        this.hand = election.getActivePlayer().getPolicies();
	        this.player = player;
	        this.scaledCardWidth = cardSize;
	        this.scaledCardHeight = (int) Math.round(1.32713755 * cardSize);
	        this.imageCache = new HashMap<>(); // Initialize the cache
	        paintCards();

	        backgroundImage = new ImageIcon(getClass().getClassLoader().getResource(prefix + "files/backgroundfull.PNG")).getImage();

	        addMouseListener(new MouseAdapter() {
	            @Override
	            public void mouseReleased(MouseEvent e) {
	                board.draw();
	                paintCards();
	                repaint();
	            }
	        });
	    }

	    // This is the most important method. Loop through the hand, draw all the cards
	    public void paintCards() {
	        // Compare current hand with previous hand
	            try {
	                // Remove only if necessary, and update the UI at the end
	                if (this.getComponentCount() > 0) {
	                    this.removeAll();
	                }

	                this.hand = election.getActivePlayer().getPolicies();

	                // Update the cache with any new cards
	                updateImageCache();

	                for (Policy curr : hand) {
	                    ImageIcon img = imageCache.get(curr.getImageURL());
	                    final JButton usercd = new JButton(img);
	                    this.add(usercd);
	                    usercd.setPreferredSize(new Dimension(scaledCardWidth, scaledCardHeight));

	                    usercd.addMouseListener(new MouseAdapter() {
	                        public void mouseReleased(MouseEvent e) {
	                            player.playSoundEffect("files/cardplay.MP3");
	                            if ((election.getActivePinnedPolicies().get(0) != null
	                                    && election.getActivePinnedPolicies().get(0).sameCategory(curr))
	                                    || election.getActivePinnedPolicies().get(0) == null
	                                    && election.getActivePinnedPolicies().get(1) != null
	                                    && election.getActivePinnedPolicies().get(1).sameCategory(curr)) {
	                                JOptionPane.showMessageDialog(board,
	                                        "Error: you can't play the same or opposite policy together!", "Error",
	                                        JOptionPane.ERROR_MESSAGE);
	                            } else if (election.getActivePinnedPolicies().get(0) == null) {
	                                election.pinActivePolicy(election.getActivePlayer().place(curr), 0);
	                                board.draw();
	                                paintCards();
	                            } else if (election.getActivePinnedPolicies().get(1) == null) {
	                                election.pinActivePolicy(election.getActivePlayer().place(curr), 1);
	                                board.draw();
	                                paintCards();
	                            } else {
	                                Policy pol = election.pinActivePolicy(election.getActivePlayer().place(curr));
	                                if (pol != null) {
	                                    election.getActivePlayer().add(pol);
	                                }
	                                board.draw();
	                                paintCards();
	                            }
	                        }
	                    });
	                }

	                this.updateUI();  // Call updateUI only once at the end

	            } catch (NullPointerException e) {
	                StringBuilder message = new StringBuilder("One of the policies has an error, please exit the game. Here is the hand:");
	                for (Policy policy : hand) {
	                    message.append("\n").append(policy.toString());
	                }
	                JOptionPane.showMessageDialog(this, message.toString(), "Error", JOptionPane.ERROR_MESSAGE);
	            }
	        
	    }

	    // Method to update the image cache
	    private void updateImageCache() {
	        List<String> currentImages = new ArrayList<>();
	        for (Policy curr : hand) {
	            currentImages.add(curr.getImageURL());
	            if (!imageCache.containsKey(curr.getImageURL())) {
	                ImageIcon img = new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource(prefix + curr.getImageURL()))
	                        .getImage().getScaledInstance(scaledCardWidth, scaledCardHeight, Image.SCALE_SMOOTH));
	                imageCache.put(curr.getImageURL(), img);
	            }
	        }
	        // Remove images that are no longer in use from the cache
	        imageCache.keySet().retainAll(new HashSet<>(currentImages));
	    }

	    public void reset() {
	        this.removeAll();
	        this.updateUI();
	        hand = election.getPlayer1().getPolicies();
	        paintCards();

	        requestFocusInWindow();
	    }

	    public void hideCards() {
	        this.removeAll();
	        this.updateUI();

	        for (int i = 0; i < 13; i++) {
	            ImageIcon img = new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource(prefix + "files/aicard.PNG"))
	                    .getImage().getScaledInstance(cardSize, (int) Math.round(1.32713755 * cardSize), Image.SCALE_SMOOTH));
	            final JButton usercd = new JButton(img);
	            this.add(usercd);
	            usercd.setPreferredSize(new Dimension(cardSize, (int) Math.round(1.32713755 * cardSize)));
	        }
	    }

	    @Override
	    public void paintComponent(Graphics g) {
	        super.paintComponent(g);
	    }


	    @Override
	    public Dimension getPreferredSize() {
	        return new Dimension((int)(cardSize/225.0*3600), (int)(cardSize/225.0*325));
	    }
	}


	/// This class represents the main game board in the center of the screen.
	@SuppressWarnings("serial")
	public class GameBoard extends JPanel {
		private boolean cpuPlayed;
		private boolean hideUsers;
	    private boolean electionCardZoomed = false; // Track if the election card is zoomed in
	    private Image backgroundImage; // To hold the background image
	    private SoundtrackPlayer player;

		// Game constants
		public static final int BOARD_WIDTH = 600;
		public static final int BOARD_HEIGHT = 250;

		/**
		 * Initializes the game board.
		 */
		public GameBoard(SoundtrackPlayer player) {
			cpuPlayed = false;
			
			this.player = player;

			// Load the background image
	        backgroundImage = new ImageIcon(getClass().getClassLoader().getResource(prefix + "files/backgroundfull.PNG")).getImage();

			draw();

			addMouseListener(new MouseAdapter() {
				@Override
				public void mouseReleased(MouseEvent e) {

					repaint(); // repaints the game board
				}
			});
		}

		// Flip CPU cards when cards are played
		public void flipCPU() {
			cpuPlayed = !cpuPlayed;
		}

		public void flipUser() {
			hideUsers = !hideUsers;
		}

		// This method basically draws all the cards, the "pinned"
		// cards that the player is about to play along with others
		public void draw() {
			this.removeAll();
			this.updateUI();

			// Player 1 played policies
			JPanel userpin = new JPanel();
		    userpin.setOpaque(false); // Make the userpin panel transparent

			if (cardSize == 0) {
				cardSize = 225;
			}

			if (!electionCardZoomed) {
			for (int i = 0; i < 2; i++) {
				if (hideUsers && mode) {
					ImageIcon usercard = new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource(prefix + "files/aicard.PNG"))
							.getImage().getScaledInstance((int) (0.355555556 * cardSize), (int) (0.469026549 * cardSize), Image.SCALE_SMOOTH));
					final JLabel label = new JLabel(usercard);
					userpin.add(label);
				} else {
					int pos = i;
					Policy curr = election.getActivePinnedPolicies().get(i);
					if (curr != null) {
						ImageIcon usercard = new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource(prefix + curr.getImageURL()))
								.getImage().getScaledInstance((int) (0.355555556 * cardSize), (int) (0.469026549 * cardSize), Image.SCALE_SMOOTH));
						final JLabel label = new JLabel(usercard);
						label.addMouseListener(new MouseAdapter() {
							public void mouseReleased(MouseEvent e) {
								player.playSoundEffect("files/cardplay.MP3");
								election.getActivePlayer().add(election.pinActivePolicy(null, pos));
								draw();
								up.paintCards();
							}
						});
						userpin.add(label);
					} else {
						ImageIcon usercard = new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource(prefix + "files/placeholder.PNG"))
								.getImage().getScaledInstance((int) (0.355555556 * cardSize), (int) (0.469026549 * cardSize), Image.SCALE_SMOOTH));
						final JLabel label = new JLabel(usercard);
						userpin.add(label);
					}
				}
			}

			userpin.setLayout(new GridLayout(2, 1));
			this.add(userpin);

			/*
			 * Player 1 played president
			 */
			if (hideUsers && mode) {
				ImageIcon aicard = new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource(prefix + "files/aicard.PNG"))
						.getImage().getScaledInstance((int) (0.666666667 * cardSize), (int) (0.884444444 * cardSize), Image.SCALE_SMOOTH));
				final JLabel label = new JLabel(aicard);
				this.add(label);
			} else if (election.getActivePinCard() != null) {
				ImageIcon usercard = new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource(prefix + election.getActivePinCard().getImageURL()))
						.getImage().getScaledInstance((int) (0.666666667 * cardSize), (int) (0.884444444 * cardSize), Image.SCALE_SMOOTH));
				final JLabel butt = new JLabel(usercard);
				this.add(butt);
				butt.addMouseListener(new MouseAdapter() {

					public void mouseReleased(MouseEvent e) {
						player.playSoundEffect("files/cardplay.MP3");
						election.getActivePlayer().add(election.getActivePinCard());
						election.activePinCard(null);
						draw();
						user_cards.paintCards();
					}
				});
			} else {
				ImageIcon usercard = new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource(prefix + "files/placeholder.PNG"))
						.getImage().getScaledInstance((int) (0.666666667 * cardSize), (int) (0.884444444 * cardSize), Image.SCALE_SMOOTH));
				final JLabel label = new JLabel(usercard);
				this.add(label);
			}
			}
			/*
	         * Current Election Card
	         */
	        final int electionCardWidth = electionCardZoomed ? (int) (0.888888889 * cardSize * 1.75) : (int) (0.888888889 * cardSize);
	        final int electionCardHeight = electionCardZoomed ? (int) (1.17777778 * cardSize * 1.75) : (int) (1.17777778 * cardSize);

	        if (starting) {
	            ImageIcon currelection = new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource(prefix + "files/placeholder.PNG"))
	                    .getImage().getScaledInstance(electionCardWidth, electionCardHeight, Image.SCALE_SMOOTH));
	            final JLabel elec = new JLabel(currelection);
	            this.add(elec);
	        } else {
	            ImageIcon currelection = new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource(prefix + election.getElection().getImageURL()))
	                    .getImage().getScaledInstance(electionCardWidth, electionCardHeight, Image.SCALE_SMOOTH));
	            final JLabel elec = new JLabel(currelection);
	            this.add(elec);

	            // Add mouse listener to toggle zoom
	            elec.addMouseListener(new MouseAdapter() {
	                @Override
	                public void mouseReleased(MouseEvent e) {
	                	player.playSoundEffect("files/playbutton.MP3");
	                    electionCardZoomed = !electionCardZoomed; // Toggle the zoom state
	                    draw(); // Redraw the board to update the size of the election card
	                }
	            });
	        }
	        if (!electionCardZoomed) {
			/*
			 * CPU Pinned Cards
			 */
			JPanel cpupin = new JPanel();
		    cpupin.setOpaque(false); // Make the userpin panel transparent
			    
			if (starting) {
				ImageIcon aicard = new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource(prefix + "files/placeholder.PNG"))
						.getImage().getScaledInstance((int) (0.666666667 * cardSize), (int) (0.884444444 * cardSize), Image.SCALE_SMOOTH));
				final JLabel label = new JLabel(aicard);
				this.add(label);


				for (int i = 0; i < 2; i++) {
					ImageIcon place = new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource(prefix + "files/placeholder.PNG"))
							.getImage().getScaledInstance((int) (0.355555556 * cardSize), (int) (0.469026549 * cardSize), Image.SCALE_SMOOTH));
					final JLabel placeLab = new JLabel(place);
					cpupin.add(placeLab);
				}
				cpupin.setLayout(new GridLayout(2, 1));
				this.add(cpupin);

			} else if (!mode) {
				/*
				 * 1-Player Mode
				 */
				if (!cpuPlayed || election.getPlayer2Card() == null) {
					ImageIcon aicard = new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource(prefix + "files/aicard.PNG"))
							.getImage().getScaledInstance((int) (0.666666667 * cardSize), (int) (0.884444444 * cardSize), Image.SCALE_SMOOTH));
					final JLabel label = new JLabel(aicard);
					this.add(label);
				} else {
					ImageIcon aicard = new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource(prefix + election.getPlayer2Card().getImageURL()))
							.getImage().getScaledInstance((int) (0.666666667 * cardSize), (int) (0.884444444 * cardSize), Image.SCALE_SMOOTH));
					final JLabel label = new JLabel(aicard);
					this.add(label);
				}

				for (Policy curr : election.getPinned2()) {
					if (!cpuPlayed || curr == null) {
						ImageIcon aicard = new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource(prefix + "files/aicard.PNG"))
								.getImage().getScaledInstance((int) (0.355555556 * cardSize), (int) (0.469026549 * cardSize), Image.SCALE_SMOOTH));
						final JLabel label = new JLabel(aicard);
						cpupin.add(label);
					} else {
						ImageIcon aicard = new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource(prefix + curr.getImageURL()))
								.getImage().getScaledInstance((int) (0.355555556 * cardSize), (int) (0.469026549 * cardSize), Image.SCALE_SMOOTH));
						final JLabel label = new JLabel(aicard);
						cpupin.add(label);
					}
				}

				cpupin.setLayout(new GridLayout(2, 1));
				this.add(cpupin);
			} else {
				String cd = prefix + "files/placeholder.PNG";
				if (!election.getTurn()) {
					cd = prefix + "files/aicard.PNG";
				}
				if (!cpuPlayed || election.getPlayer1Card() == null) {
					ImageIcon aicard = new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource(cd))
							.getImage().getScaledInstance((int) (0.666666667 * cardSize), (int) (0.884444444 * cardSize), Image.SCALE_SMOOTH));
					final JLabel label = new JLabel(aicard);
					this.add(label);
				} else {
					ImageIcon aicard = new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource(prefix + election.getPlayer1Card().getImageURL()))
							.getImage().getScaledInstance((int) (0.666666667 * cardSize), (int) (0.884444444 * cardSize), Image.SCALE_SMOOTH));
					final JLabel label = new JLabel(aicard);
					this.add(label);
				}

				for (Policy curr : election.getPinned1()) {
					if (!cpuPlayed || curr == null) {
						ImageIcon aicard = new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource(cd))
								.getImage().getScaledInstance((int) (0.355555556 * cardSize), (int) (0.469026549 * cardSize), Image.SCALE_SMOOTH));
						final JLabel label = new JLabel(aicard);
						cpupin.add(label);
					} else {
						ImageIcon aicard = new ImageIcon(new ImageIcon(getClass().getClassLoader().getResource(prefix + curr.getImageURL()))
								.getImage().getScaledInstance((int) (0.355555556 * cardSize), (int) (0.469026549 * cardSize), Image.SCALE_SMOOTH));
						final JLabel label = new JLabel(aicard);
						cpupin.add(label);
					}
				}

				cpupin.setLayout(new GridLayout(2, 1));
				this.add(cpupin);
			}
	        }
			repaint();
		}

		public void reset() {
			cpuPlayed = false;
			draw();
			repaint();
		}

		@Override
		public void paintComponent(Graphics g) {
		    super.paintComponent(g);

		    if (backgroundImage != null) {
		        // Convert Image to BufferedImage
		        BufferedImage bufferedImage = new BufferedImage(
		            backgroundImage.getWidth(null), backgroundImage.getHeight(null), BufferedImage.TYPE_INT_ARGB);

		        Graphics2D bGr = bufferedImage.createGraphics();
		        bGr.drawImage(backgroundImage, 0, 0, null);
		        bGr.dispose();

		        // Get the size of the original background image
		        int imageWidth = bufferedImage.getWidth();
		        int imageHeight = bufferedImage.getHeight();

		        // Calculate the cropping parameters for the board
		        int cropStartX = (int) (211.5 / 1900.0 * imageWidth); // Start X at 213/1900 from the left
		        int cropStartY = (int) (106.0 / 1200.0 * imageHeight); // Start Y at 106/1200 from the top
		        int cropWidth = (int) ((imageWidth * (1 - 2 * 211 / 1900.0))); // Width from 213/1900 from the left to 213/1900 from the right
		        int cropHeight = (int) (679.0 / 1200.0 * imageHeight); // Height from 106/1200 to 679/1200

		        // Crop the BufferedImage
		        BufferedImage croppedImage = bufferedImage.getSubimage(cropStartX, cropStartY, cropWidth, cropHeight);

		        // Draw the cropped image scaled to the GameBoard's size
		        g.drawImage(croppedImage, 0, 0, getWidth(), getHeight(), this);
		    }
		}


		/**
		 * Returns the size of the game board.
		 */
		@Override
		public Dimension getPreferredSize() {
			return new Dimension(BOARD_WIDTH, BOARD_HEIGHT);
		}
	}

	private void setLabel(JLabel j) {
		j.setText("<html><body>" + election.p1Score() + "<br>" + election.p2Score() + "</body></html>");
		j.repaint();
	}
}
