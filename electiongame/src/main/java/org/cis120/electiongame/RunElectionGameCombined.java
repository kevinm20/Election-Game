package org.cis120.electiongame;

/**
 * CIS 120 HW09 - TicTacToe Demo
 * (c) University of Pennsylvania
 * Created by Bayley Tuch, Sabrina Green, and Nicolas Corona in Fall 2020.
 */

import java.awt.*;
import java.awt.event.*;
import java.util.List;
import javax.swing.*;

/**
 * This class sets up the top-level frame and widgets for the GUI.
 */
public class RunElectionGameCombined implements Runnable {

    private ElectionGame election = new ElectionGame();
    private boolean starting = true;
    final GameBoard board = new GameBoard();
    private UserDeck user_cards;
    private UserPolicies up;
    private boolean mode = false;
    private String deckSet = "standard";

    // Change this if the screen is wacky
    int cardSize = 225;

    public void run() {

        // Top-level frame in which game components live
        final JFrame frame = new JFrame("Election Game");
        frame.setLocation(0, 0);

        // User Policy Deck, I added a scrollbar since you get 15 policies
        up = new UserPolicies();
        JScrollPane userPolicies = new JScrollPane(
                up, ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS
        );
        userPolicies.getHorizontalScrollBar().setUnitIncrement(100);

        userPolicies.setPreferredSize(new Dimension(600, 325));

        // User Card Deck
        // final UserDeck user_cards = new UserDeck();
        user_cards = new UserDeck();
        user_cards.setPreferredSize(new Dimension(600, 325));
        frame.add(user_cards, BorderLayout.SOUTH);

        // Game board/Election Area
        frame.add(board, BorderLayout.CENTER);
        board.setLayout(new GridLayout(1, 7));

        // AI Card Deck, just for visuals
        final JPanel ai_cards = new JPanel();
        frame.add(ai_cards, BorderLayout.NORTH);
        
        for (int i = 0; i < 5; i++) {
            ImageIcon img = new ImageIcon(
                    new ImageIcon("files/aicard.png").getImage()
                            .getScaledInstance(60, 79, Image.SCALE_SMOOTH)
            );
            final JLabel aicardlabel = new JLabel(img);
            ai_cards.add(aicardlabel);
        }

        // Deck Area and status, for visuals
        final JPanel decks = new JPanel();
        decks.setPreferredSize(new Dimension(175, 200));

        frame.add(decks, BorderLayout.WEST);
        final JLabel status = new JLabel(election.currentScore(), SwingConstants.CENTER);
        setLabel(status);
        decks.add(status);

        final JLabel elecdeck = new JLabel(
                new ImageIcon(
                        new ImageIcon("files/electionsdeck.png").getImage()
                                .getScaledInstance(85, 65, Image.SCALE_SMOOTH)
                )
        );
        decks.add(elecdeck);

        final JLabel prezdeck = new JLabel(
                new ImageIcon(
                        new ImageIcon("files/presidentdeck.png").getImage()
                                .getScaledInstance(85, 65, Image.SCALE_SMOOTH)
                )
        );
        decks.add(prezdeck);

        final JLabel poldeck = new JLabel(
                new ImageIcon(
                        new ImageIcon("files/policydeck.png").getImage()
                                .getScaledInstance(85, 65, Image.SCALE_SMOOTH)
                )
        );
        decks.add(poldeck);

        decks.setLayout(new GridLayout(4, 1));

        // Control panel
        final JPanel control_panel = new JPanel();
        frame.add(control_panel, BorderLayout.EAST);

        /*
         * Play button: Where most of the real action happens
         */
        final JButton play = new JButton("Play");
        play.setBackground(new Color(47, 84, 150));
        play.setForeground(Color.YELLOW);
        play.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                if (election.getActivePinCard() == null
                        || election.getActivePinnedPolicies().get(0) == null
                        || election.getActivePinnedPolicies().get(1) == null) {
                    JOptionPane.showMessageDialog(
                            null, "Error: you haven't played all cards!", "Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                } else {
                    if (!mode) {
                        election.printGameState();

                        election.getPlayer1().draw(election.getPres());
                        election.getPlayer1().drawPols(election.getPol());

                        AI ai = (AI) election.getPlayer2();

                        President cpuplay = ai.play(election.getPres(), election.getElection());

                        election.player2PlayCard(cpuplay);
                        election.pinAIPolicies(ai.playPolicies(cpuplay, election.getPol()));

                        board.flipCPU();
                        board.draw();

                        election.playRound(
                                election.getPlayer1Card(), election.getPinned1(),
                                cpuplay, election.getPinned2()
                        );

                    } else {
                        if (election.getTurn()) {
                            election.flipPlayer();
                            user_cards.hideCards(4);
                            up.hideCards();
                            board.draw();
                            JOptionPane
                                    .showMessageDialog(
                                            null, "Please pass the computer to Player 2."
                                    );
                            user_cards.paintCards();
                            up.paintCards();
                            board.draw();
                        } else {
                            user_cards.hideCards(4);
                            up.hideCards();
                            board.flipUser();
                            board.draw();
                            JOptionPane
                                    .showMessageDialog(
                                            null, "Click 'OK' when both players are ready!"
                                    );
                            board.flipUser();
                            election.printGameState();

                            election.getPlayer2().draw(election.getPres());
                            election.getPlayer2().drawPols(election.getPol());

                            election.getPlayer1().draw(election.getPres());
                            election.getPlayer1().drawPols(election.getPol());

                            board.flipCPU();
                            board.draw();

                            election.playRound(
                                    election.getPlayer1Card(), election.getPinned1(),
                                    election.getPlayer2Card(), election.getPinned2()
                            );
                            election.flipPlayer();
                        }
                    }

                    if (!mode || election.getTurn()) {
                        setLabel(status);
                        status.repaint();
                        JOptionPane.showMessageDialog(null, election.getMessage());

                        if (election.checkWinner() == 1) {
                            JOptionPane.showMessageDialog(
                                    null,
                                    election.getPlayer1().getName() + " has won!!! "
                                            + election.finalScore(true)
                            );
                            if (mode) {
                                JOptionPane.showMessageDialog(
                                        null, "Please pass the computer to Player 1."
                                );
                            }
                            election.reset(deckSet, 0, true, 0, true, 0, true);
                            user_cards.reset();
                            up.reset();
                            board.reset();
                            setLabel(status);
                            status.repaint();
                        } else if (election.checkWinner() == 2) {
                            JOptionPane.showMessageDialog(
                                    null,
                                    election.getPlayer2().getName() + " has won!!! "
                                            + election.finalScore(false)
                            );
                            if (mode) {
                                JOptionPane.showMessageDialog(
                                        null, "Please pass the computer to Player 1."
                                );
                            }
                            election.reset(deckSet, 0, true, 0, true, 0, true);
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
                                JOptionPane.showMessageDialog(
                                        null, "Please pass the computer to Player 1."
                                );
                            }
                            user_cards.paintCards();
                            up.paintCards();
                        }
                    }
                }
            }

        });

        final JButton undo = new JButton("Undo Cards");
        undo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                if (election.getActivePinCard() == null
                        && election.getActivePinnedPolicies().get(0) == null
                        && election.getActivePinnedPolicies().get(1) == null) {
                    JOptionPane.showMessageDialog(
                            null, "Error: you haven't played a card", "Error",
                            JOptionPane.ERROR_MESSAGE
                    );
                } else {

                    if (election.getActivePinCard() != null) {
                        election.getActivePlayer().add(election.getActivePinCard());
                    }

                    if (election.getActivePinnedPolicies().get(0) != null) {
                        election.getActivePlayer().add(election.getActivePinnedPolicies().get(0));
                    }

                    if (election.getActivePinnedPolicies().get(1) != null) {
                        election.getActivePlayer().add(election.getActivePinnedPolicies().get(1));
                    }

                    user_cards.paintCards();

                    election.activePinCard(null);

                    election.nullifyActive();

                    up.paintCards();

                    board.draw();

                }
            }

        });

        final JButton reset = new JButton("Resign");
        reset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(
                        null, "You have resigned. " + election.finalScore(false)
                );
                election.reset(deckSet, 0, true, 0, true, 0, true);
                setLabel(status);
                status.repaint();
                user_cards.reset();
                up.reset();
                board.reset();
            }
        });

        final JButton help = new JButton("Help");
        help.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(
                        null,
                        "Welcome to Election Game! First, I will explain how to play the game in s"
                                + "hort, and then we'll dive into the details of each card. \n"
                                +
                                "Simply put, each round, there is a certain election chosen. You d"
                                + "raw 5 Candidate cards and 15 Policy cards, \n"
                                +
                                "and based on traits or policies desired by the election, you have"
                                + " to play your best combo of these cards. A new election is chos"
                                + "en each round\n"
                                +
                                " and the game is best-of-five.\n" +
                                "Now, we'll look at each type of card."
                );
                JOptionPane.showMessageDialog(
                        null,
                        "The first type of card is an ELECTION CARD. There is one for each US elec"
                                + "tion historically. In the 'Context' section, the election will"
                                + " imply wh"
                                + "at attributes\n"
                                +
                                "to focus on, or what policies are important that year. Each elect"
                                + "ion has 2 'desired attributes' from the President cards,\n"
                                +
                                "1 'main' policy issue, and two 'side' policy issues. The details"
                                + " of scoring will be explained last.\n"
                                +
                                "Finally, each election has a 'swing region', and if your Preside"
                                + "nt is from the same region, you receive a bonus."
                );
                JOptionPane.showMessageDialog(
                        null,
                        "The next type of card is a PRESIDENT CARD. They are a combination of all"
                                + " US presidents, and other prominent political figures. These ca"
                                + "rds have"
                                + " quite a decent amount of information.\n"
                                +
                                "First, each president has 4 attributes, EXP, INFL, POL, and NAM. "
                                + "Each election year will desire 2 of these attributes, \n"
                                +
                                "and the attributes are rated 1-10, with 5 being average. Here's w"
                                + "hat they mean: \n"
                                +
                                "Experience (EXP): How long the candidate has been involved in pol"
                                + "itics, and their highest office.\n"
                                +
                                "Influence (INFL): How well a candidate can convince and sway peop"
                                + "le, and other personality aspects like leadership, charisma, "
                                + "etc.\n"
                                +
                                "Policy (POL): How well-versed the candidate is in ideas and polic"
                                + "y, and their knowledge of the issues.\n"
                                +
                                "Name Recogntion (NAM): How well-known the candidate is to the gen"
                                + "eral public, including outside of political life.\n"
                                +
                                "The cards have a little more information. One of them is their ho"
                                + "me region, giving a bonus if this matches the election's swing "
                                + "region.\n"
                                +
                                "Also, the cards show the candidates ideology. These are used to "
                                + "get bonuses if the policies played 'match' the candidates ideo"
                                + "logy.\n"
                                +
                                "Finally, each candidate also has a 'Marquee Policy', which always"
                                + " gives them a bonus if paired with the candidate."
                );
                JOptionPane.showMessageDialog(
                        null,
                        "Last, here is the specific breakdown of the scoring for each round: \n" +
                                "First, add the two key attributes of the president card, based "
                                + "on the desired attributes of the election.\n"
                                +
                                "You get 3 points if one of your policies is the main issue, and"
                                + " 2 for either of the side issues.\n"
                                +
                                "For each policy that matches the president's ideology, you get"
                                + " 2 points.\n"
                                +
                                "If you played the president's Marquee Policy, you get 3 points"
                                + " (and the ideology match always).\n"
                                +
                                "Finally, you get 3 points if your candidates' region is the same"
                                + " as the swing region.\n"
                                +
                                "Whichever player has the highest round score wins, and "
                                + "best-of-five wins it all.\n"
                                +
                                "In case of a tie, the first tiebreaker is the sum of the "
                                + "candidate's attributes, then just chosen randomly.\n"
                                +
                                "Good luck!"
                );
            }
        });

        // Swaps between policies and presidents in view
        final JButton swap = new JButton("Policies ->");
        swap.addActionListener(new ActionListener() {
            boolean mode = true;

            public void actionPerformed(ActionEvent e) {
                if (!mode) {
                    swap.setText("Policies ->");
                    swap.repaint();
                    frame.add(user_cards, BorderLayout.SOUTH);
                    frame.remove(userPolicies);
                    user_cards.paintCards();
                    up.paintCards();
                    mode = !mode;
                } else {
                    swap.setText("<- Presidents");
                    swap.repaint();
                    frame.add(userPolicies, BorderLayout.SOUTH);
                    frame.remove(user_cards);
                    user_cards.paintCards();
                    up.paintCards();
                    mode = !mode;
                }
            }
        });

        control_panel.add(play);
        control_panel.add(undo);
        control_panel.add(reset);
        control_panel.add(help);
        control_panel.add(swap);
        control_panel.setPreferredSize(new Dimension(175, 200));
        control_panel.setLayout(new GridLayout(5, 1));

        // Put the frame on the screen
        frame.pack();
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        user_cards.hideCards(5);

        /*
         * Here, the game is started with all necessary inputs
         */
        String[] names = { "dababy", "amogus", "thurday", "sus", "bartholomew", "Kevin" };

        Object[] options = { "1-Player", "2-Player", "Quick 1-Player" };
        int gameMode = JOptionPane.showOptionDialog(
                null, "Welcome to Election Game! Please select your game mode:",
                "Select Game Mode", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null,
                options, options[0]
        );

        if (gameMode == -1 || gameMode == 0 || gameMode == 2) {            
            mode = false;
        } else {
            mode = true;
        }

        if (gameMode == 2) {
            int ran = (int) Math.round(Math.random()*names.length);
            CardData.flipExpanded();
            election.namePlayer(names[ran]);
            election.namePlayer2("CPU");
            election.setAIDifficulty("Hard");
            election.reset("standard", 0, true, 0, true, 0, true);
            setLabel(status);
        } else {
            // Name Player 1
            String playername = "Player 1";
            if (mode) {
                playername = JOptionPane.showInputDialog(
                        null,
                        "Hello and welcome to the Election Game! Player 1, please enter your name: "
                );
            } else {
                playername = JOptionPane.showInputDialog(
                        null, "Hello and welcome to the Election Game! Please enter your name: "
                );
                election.namePlayer2("CPU");
            }
            election.namePlayer(playername);
            setLabel(status);
            status.repaint();

            // Name Player 2 or set AI difficulty
            if (mode) {
                String player2name = JOptionPane.showInputDialog(
                        null, "Player 2, please enter your name: "
                );
                election.namePlayer2(player2name);
                setLabel(status);
                status.repaint();
            } else {
                Object[] diffs = { "Easy", "Medium", "Hard", "Impossible" };
                int diff = JOptionPane.showOptionDialog(
                        null, "Choose the difficulty of the AI:",
                        "Select Difficulty", JOptionPane.DEFAULT_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        diffs, diffs[1]
                );
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

            Object[] cardDecks = { "Presidents Only", "Standard", "Expanded", "Memes" };
            int deck = JOptionPane.showOptionDialog(
                    null, "Choose the game deck:",
                    "Select Deck", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
                    null,
                    cardDecks, cardDecks[1]
            );

            if (deck == 0) {
                election.reset("presidentsonly", 0, true, 0, true, 0, true);
                deckSet = "presidentsonly";
            }
            if (deck == 1) {
                election.reset("standard", 0, true, 0, true, 0, true);
                deckSet = "standard";
            }
            if (deck == 2) {
                election.reset("expanded", 0, true, 0, true, 0, true);
                deckSet = "expanded";
            }
            if (deck == 3) {
                election.reset("memes", 0, true, 0, true, 0, true);
                deckSet = "memes";
            }

            // Final message to start the game
            JOptionPane.showMessageDialog(
                    null,
                    "To begin, please set your screen to fullscreen, and click the \"Help\" "
                            + "\nbutton for the instructions if this is your first time playing!"
            );
        }
        user_cards.paintCards();
        starting = false;
        board.draw();
    }

    /*
     * This is my first inner class used to represent the deck of the user.
     * It does things like draw the user's cards, and lets the user click on them
     * to place them.
     */
    @SuppressWarnings("serial")
    public class UserDeck extends JPanel {

        private List<President> hand;

        public static final int BOARD_WIDTH = 600;
        public static final int BOARD_HEIGHT = 325;

        public UserDeck() {

            setFocusable(true);

            this.hand = election.getActivePlayer().getHand();
            paintCards();

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
            this.removeAll();
            this.updateUI();
            this.hand = election.getActivePlayer().getHand();

            for (President curr : hand) {
                ImageIcon img = new ImageIcon(
                        new ImageIcon(curr.getImageURL()).getImage()
                                .getScaledInstance(
                                        cardSize, (int) Math.round(1.32713755 * cardSize),
                                        Image.SCALE_SMOOTH
                                )
                );
                final JButton usercd = new JButton(img);
                this.add(usercd);
                usercd.setPreferredSize(
                        new Dimension(cardSize, (int) Math.round(1.32713755 * cardSize))
                );

                // This makes all of the cards buttons that play the card if clicked
                usercd.addMouseListener(new MouseAdapter() {
                    public void mouseReleased(MouseEvent e) {
                        if (election.getActivePinCard() != null) {
                            election.getActivePlayer().add(election.getActivePinCard());
                        }
                        election.activePinCard(curr);
                        election.getActivePlayer().place(curr);
                        board.draw();
                        paintCards();
                        repaint();
                    }
                });

            }

            repaint();
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
                ImageIcon img = new ImageIcon(
                        new ImageIcon("files/aicard.png").getImage()
                                .getScaledInstance(
                                        cardSize, (int) Math.round(1.32713755 * cardSize),
                                        Image.SCALE_SMOOTH
                                )
                );
                final JButton usercd = new JButton(img);
                this.add(usercd);
                usercd.setPreferredSize(
                        new Dimension(cardSize, (int) Math.round(1.32713755 * cardSize))
                );
            }
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);

        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(BOARD_WIDTH, BOARD_HEIGHT);
        }
    }

    // This is very similar to the UserDeck, but with some changes geared towards
    // policies
    @SuppressWarnings("serial")
    public class UserPolicies extends JPanel {

        private List<Policy> hand;

        public static final int BOARD_WIDTH = 3600;
        public static final int BOARD_HEIGHT = 325;

        public UserPolicies() {

            setFocusable(true);

            this.hand = election.getActivePlayer().getPolicies();
            paintCards();

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseReleased(MouseEvent e) {

                    board.draw();
                    paintCards();
                    repaint();
                }
            });
        }

        public void paintCards() {
            this.removeAll();
            this.updateUI();

            this.hand = election.getActivePlayer().getPolicies();

            for (Policy curr : hand) {

                ImageIcon img = new ImageIcon(
                        new ImageIcon(curr.getImageURL()).getImage()
                                .getScaledInstance(
                                        cardSize, (int) Math.round(1.32713755 * cardSize),
                                        Image.SCALE_SMOOTH
                                )
                );
                final JButton usercd = new JButton(img);
                this.add(usercd);
                usercd.setPreferredSize(
                        new Dimension(cardSize, (int) Math.round(1.32713755 * cardSize))
                );

                usercd.addMouseListener(new MouseAdapter() {
                    public void mouseReleased(MouseEvent e) {
                        // Maintains the invariant that you can't play the same/opposite policies
                        if ((election.getActivePinnedPolicies().get(0) != null
                                && election.getActivePinnedPolicies().get(0).sameCategory(curr))
                                || election.getActivePinnedPolicies().get(0) == null
                                        && election.getActivePinnedPolicies().get(1) != null
                                        && election.getActivePinnedPolicies().get(1)
                                                .sameCategory(curr)) {
                            JOptionPane.showMessageDialog(
                                    board,
                                    "Error: you can't play the same or opposite policy together!",
                                    "Error",
                                    JOptionPane.ERROR_MESSAGE
                            );
                        } else if (election.getActivePinnedPolicies().get(0) == null) {
                            election.pinActivePolicy(election.getActivePlayer().place(curr), 0);
                            board.draw();
                            paintCards();
                        } else if (election.getActivePinnedPolicies().get(1) == null) {
                            election.pinActivePolicy(election.getActivePlayer().place(curr), 1);
                            board.draw();
                            paintCards();
                        } else {
                            Policy pol = election
                                    .pinActivePolicy(election.getActivePlayer().place(curr));
                            if (pol != null) {
                                election.getActivePlayer().add(pol);
                            }
                            board.draw();
                            paintCards();
                        }
                    }
                });

            }

            repaint();
        }

        public void hideCards() {
            this.removeAll();
            this.updateUI();

            for (int i = 0; i < 13; i++) {
                ImageIcon img = new ImageIcon(
                        new ImageIcon("files/aicard.png").getImage()
                                .getScaledInstance(
                                        cardSize, (int) Math.round(1.32713755 * cardSize),
                                        Image.SCALE_SMOOTH
                                )
                );
                final JButton usercd = new JButton(img);
                this.add(usercd);
                usercd.setPreferredSize(
                        new Dimension(cardSize, (int) Math.round(1.32713755 * cardSize))
                );
            }
        }

        public void reset() {
            this.removeAll();
            this.updateUI();
            hand = election.getPlayer1().getPolicies();
            paintCards();

            requestFocusInWindow();
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);

        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(BOARD_WIDTH, BOARD_HEIGHT);
        }
    }

    /// This class represents the game board
    @SuppressWarnings("serial")
    public class GameBoard extends JPanel {
        private boolean cpuPlayed;
        private boolean hideUsers;

        // Game constants
        public static final int BOARD_WIDTH = 600;
        public static final int BOARD_HEIGHT = 250;

        /**
         * Initializes the game board.
         */
        public GameBoard() {
            cpuPlayed = false;

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

            for (int i = 0; i < 2; i++) {
                if (hideUsers && mode) {
                    ImageIcon usercard = new ImageIcon(
                            new ImageIcon("files/aicard.png").getImage()
                                    .getScaledInstance(80, 106, Image.SCALE_SMOOTH)
                    );
                    final JLabel label = new JLabel(usercard);
                    userpin.add(label);
                } else {
                    int pos = i;
                    Policy curr = election.getActivePinnedPolicies().get(i);
                    if (curr != null) {
                        ImageIcon usercard = new ImageIcon(
                                new ImageIcon(curr.getImageURL()).getImage()
                                        .getScaledInstance(80, 106, Image.SCALE_SMOOTH)
                        );
                        final JLabel label = new JLabel(usercard);
                        label.addMouseListener(new MouseAdapter() {
                            public void mouseReleased(MouseEvent e) {
                                election.getActivePlayer().add(election.pinActivePolicy(null, pos));
                                draw();
                                up.paintCards();
                            }
                        });
                        userpin.add(label);
                    } else {
                        ImageIcon usercard = new ImageIcon(
                                new ImageIcon("files/placeholder.png").getImage()
                                        .getScaledInstance(80, 106, Image.SCALE_SMOOTH)
                        );
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
                ImageIcon aicard = new ImageIcon(
                        new ImageIcon("files/aicard.png").getImage()
                                .getScaledInstance(150, 199, Image.SCALE_SMOOTH)
                );
                final JLabel label = new JLabel(aicard);
                this.add(label);
            } else if (election.getActivePinCard() != null) {
                ImageIcon usercard = new ImageIcon(
                        new ImageIcon(election.getActivePinCard().getImageURL()).getImage()
                                .getScaledInstance(150, 199, Image.SCALE_SMOOTH)
                );
                final JLabel butt = new JLabel(usercard);
                this.add(butt);
                butt.addMouseListener(new MouseAdapter() {

                    public void mouseReleased(MouseEvent e) {
                        election.getActivePlayer().add(election.getActivePinCard());
                        election.activePinCard(null);
                        draw();
                        user_cards.paintCards();
                    }
                });
            } else {
                ImageIcon usercard = new ImageIcon(
                        new ImageIcon("files/placeholder.png").getImage()
                                .getScaledInstance(150, 199, Image.SCALE_SMOOTH)
                );
                final JLabel label = new JLabel(usercard);
                this.add(label);
            }

            /*
             * Current Election Card
             */

            if (starting) {
                ImageIcon currelection = new ImageIcon(
                        new ImageIcon("files/placeholder.png").getImage()
                                .getScaledInstance(200, 265, Image.SCALE_SMOOTH)
                );
                final JLabel elec = new JLabel(currelection);
                this.add(elec);
            } else {
                ImageIcon currelection = new ImageIcon(
                        new ImageIcon(election.getElection().getImageURL()).getImage()
                                .getScaledInstance(200, 265, Image.SCALE_SMOOTH)
                );
                final JLabel elec = new JLabel(currelection);
                this.add(elec);
            }

            /*
             * CPU Pinned Cards
             */
            if (starting) {
                ImageIcon aicard = new ImageIcon(
                        new ImageIcon("files/placeholder.png").getImage()
                                .getScaledInstance(150, 199, Image.SCALE_SMOOTH)
                );
                final JLabel label = new JLabel(aicard);
                this.add(label);

                JPanel cpupin = new JPanel();
                for (int i = 0; i < 2; i++) {
                    ImageIcon place = new ImageIcon(
                            new ImageIcon("files/placeholder.png").getImage()
                                    .getScaledInstance(80, 106, Image.SCALE_SMOOTH)
                    );
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
                    ImageIcon aicard = new ImageIcon(
                            new ImageIcon("files/aicard.png").getImage()
                                    .getScaledInstance(150, 199, Image.SCALE_SMOOTH)
                    );
                    final JLabel label = new JLabel(aicard);
                    this.add(label);
                } else {
                    ImageIcon aicard = new ImageIcon(
                            new ImageIcon(election.getPlayer2Card().getImageURL()).getImage()
                                    .getScaledInstance(150, 199, Image.SCALE_SMOOTH)
                    );
                    final JLabel label = new JLabel(aicard);
                    this.add(label);
                }

                JPanel cpupin = new JPanel();

                for (Policy curr : election.getPinned2()) {
                    if (!cpuPlayed || curr == null) {
                        ImageIcon aicard = new ImageIcon(
                                new ImageIcon("files/aicard.png").getImage()
                                        .getScaledInstance(80, 106, Image.SCALE_SMOOTH)
                        );
                        final JLabel label = new JLabel(aicard);
                        cpupin.add(label);
                    } else {
                        ImageIcon aicard = new ImageIcon(
                                new ImageIcon(curr.getImageURL()).getImage()
                                        .getScaledInstance(80, 106, Image.SCALE_SMOOTH)
                        );
                        final JLabel label = new JLabel(aicard);
                        cpupin.add(label);
                    }
                }

                cpupin.setLayout(new GridLayout(2, 1));
                this.add(cpupin);
            } else {
                String cd = "files/placeholder.png";
                if (!election.getTurn()) {
                    cd = "files/aicard.png";
                }
                if (!cpuPlayed || election.getPlayer1Card() == null) {
                    ImageIcon aicard = new ImageIcon(
                            new ImageIcon(cd).getImage()
                                    .getScaledInstance(150, 199, Image.SCALE_SMOOTH)
                    );
                    final JLabel label = new JLabel(aicard);
                    this.add(label);
                } else {
                    ImageIcon aicard = new ImageIcon(
                            new ImageIcon(election.getPlayer1Card().getImageURL()).getImage()
                                    .getScaledInstance(150, 199, Image.SCALE_SMOOTH)
                    );
                    final JLabel label = new JLabel(aicard);
                    this.add(label);
                }

                JPanel cpupin = new JPanel();

                for (Policy curr : election.getPinned1()) {
                    if (!cpuPlayed || curr == null) {
                        ImageIcon aicard = new ImageIcon(
                                new ImageIcon(cd).getImage()
                                        .getScaledInstance(80, 106, Image.SCALE_SMOOTH)
                        );
                        final JLabel label = new JLabel(aicard);
                        cpupin.add(label);
                    } else {
                        ImageIcon aicard = new ImageIcon(
                                new ImageIcon(curr.getImageURL()).getImage()
                                        .getScaledInstance(80, 106, Image.SCALE_SMOOTH)
                        );
                        final JLabel label = new JLabel(aicard);
                        cpupin.add(label);
                    }
                }

                cpupin.setLayout(new GridLayout(2, 1));
                this.add(cpupin);
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
        j.setText(
                "<html><body>" + election.p1Score() + "<br>" + election.p2Score() + "</body></html>"
        );
        j.repaint();
    }
}