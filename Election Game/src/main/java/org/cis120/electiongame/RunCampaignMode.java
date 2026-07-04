package org.cis120.electiongame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;

/**
 * Swing UI for Campaign Mode.
 *
 * v13.3 polishes League Mode after the first playable UI pass:
 * league seasons now start only when Game 1 is actually launched, League records
 * are more trophy-like, and the Trophy Cabinet moves into the Collection hub as
 * a third roster tab beside Active Deck and Collection.
 */
public class RunCampaignMode implements Runnable {

    private static final String PREFIX = RunElectionGameCombined.prefix;
    private static final Pattern NEW_CARD_PATTERN = Pattern.compile("^\\s*New card:\\s+(.+?)\\s+\\d+\\.\\d{2}\\s*$");
    private static final Pattern DUPLICATE_CARD_PATTERN = Pattern.compile("^\\s*Duplicate:\\s+(.+?)\\s+auto-sold.*$");

    private static final String COLLECTION_ERA_ALL = "All Eras";
    private static final String COLLECTION_TAG_ALL = "All Tags";
    private static final String COLLECTION_PARTY_ALL = "All Parties";
    private static final String COLLECTION_STATE_ALL = "All States";
    private static final String COLLECTION_REGION_ALL = "All Regions";
    private static final String COLLECTION_IDEOLOGY_ALL = "All Ideologies";
    private static final String COLLECTION_SCOPE_ALL = "All Cards";
    private static final String COLLECTION_SCOPE_ERA = "Era";
    private static final String COLLECTION_SCOPE_TAG = "Tag";
    private static final String COLLECTION_SCOPE_FILTERS = "Full Filters";
    private static final String COLLECTION_SCOPE_PROGRESS = "Progress Dashboard";
    private static final String COLLECTION_OWNERSHIP_OWNED = "Owned";
    private static final String COLLECTION_OWNERSHIP_MISSING = "Missing";
    private static final String COLLECTION_OWNERSHIP_ALL = "All Cards";
    private static final int TAG_TOURNAMENT_REQUIRED_CARDS = 10;
    private static final String PROGRESS_SORT_ELIGIBLE_CLOSE = "Eligible / Close";
    private static final String PROGRESS_SORT_CLOSEST_LOCKED = "Closest Locked";
    private static final String PROGRESS_SORT_PERCENT_OWNED = "% Owned";
    private static final String PROGRESS_SORT_OWNED_COUNT = "Owned Count";
    private static final String PROGRESS_SORT_DECK_STRENGTH = "Deck Strength";
    private static final String PROGRESS_SORT_ALPHABETICAL = "Alphabetical";
    private static final String PROGRESS_SORT_CHRONOLOGICAL = "Chronological Era";
    private static final String PROGRESS_TYPE_BOTH = "Eras + Tags";
    private static final String PROGRESS_TYPE_ERAS = "Eras Only";
    private static final String PROGRESS_TYPE_TAGS = "Tags Only";

    private static final String TROPHY_TYPE_ALL = "All Trophies";
    private static final String TROPHY_TYPE_TOURNAMENTS = "Eras + Tags";
    private static final String TROPHY_TYPE_ERAS = "Eras Only";
    private static final String TROPHY_TYPE_TAGS = "Tags Only";
    private static final String TROPHY_TYPE_LEAGUES = "Leagues Only";
    private static final String TROPHY_STATUS_ALL = "All";
    private static final String TROPHY_STATUS_WON = "Won";
    private static final String TROPHY_STATUS_ENTERED = "Entered";
    private static final String TROPHY_STATUS_ELIGIBLE = "Eligible";
    private static final String TROPHY_STATUS_LOCKED = "Locked";
    private static final String TROPHY_SORT_DEFAULT = "Default";
    private static final String TROPHY_SORT_TITLES = "Titles";
    private static final String TROPHY_SORT_BEST_FINISH = "Best Finish";
    private static final String TROPHY_SORT_ELIGIBLE = "Eligibility";
    private static final String TROPHY_SORT_ATTEMPTS = "Attempts";
    private static final String TROPHY_SORT_ALPHABETICAL = "Alphabetical";

    private JFrame frame;
    private JPanel rootPanel;
    private boolean embeddedMode = false;
    private boolean ownsFrame = true;
    private CampaignNavigationHost navigationHost;
    private CampaignMode campaign;
    private SoundtrackPlayer soundtrackPlayer;

    private int cardSize = 275;

    private JLabel nameLabel;
    private JLabel pcLabel;
    private JLabel recordLabel;
    private JLabel collectionLabel;
    private JLabel activeDeckLabel;
    private JLabel deckStrengthLabel;
    private Timer playTimeUiTimer;

    private JPanel displayPanel;
    private JLabel displayTitleLabel;
    private JScrollPane displayScrollPane;
    private boolean showingActiveDeckCards = false;
    private boolean showingCollectionCards = false;
    private boolean activeDeckEditMode = false;

    private java.util.List<JButton> actionButtons;

    private ImageIcon buttonIcon;
    private ImageIcon buttonHoverIcon;
    private ImageIcon buttonClickedIcon;

    // Scaled card images are expensive to load/resize. Cache by path + size so
    // collection/active-deck/store result views can reuse already-scaled icons.
    private final Map<String, ImageIcon> scaledIconCache = new HashMap<String, ImageIcon>();
    private SwingWorker<List<CardIconData>, Integer> collectionLoadWorker;
    private long collectionLoadRequestId = 0;

    private String collectionScopeMode = COLLECTION_SCOPE_PROGRESS;
    private String collectionEraFilterId = COLLECTION_ERA_ALL;
    private String collectionTagFilter = COLLECTION_TAG_ALL;
    private String collectionOwnershipFilter = COLLECTION_OWNERSHIP_OWNED;
    private String collectionFullEraFilter = COLLECTION_ERA_ALL;
    private String collectionFullTagFilter = COLLECTION_TAG_ALL;
    private String collectionFullPartyFilter = COLLECTION_PARTY_ALL;
    private String collectionFullStateFilter = COLLECTION_STATE_ALL;
    private String collectionFullRegionFilter = COLLECTION_REGION_ALL;
    private String collectionFullIdeologyFilter = COLLECTION_IDEOLOGY_ALL;
    private String collectionProgressSortMode = PROGRESS_SORT_ELIGIBLE_CLOSE;
    private String collectionProgressTypeFilter = PROGRESS_TYPE_BOTH;
    private String trophyTypeFilter = TROPHY_TYPE_ALL;
    private String trophyStatusFilter = TROPHY_STATUS_ALL;
    private String trophySortMode = TROPHY_SORT_DEFAULT;

    private java.util.List<President> activeDeckDraft = new ArrayList<President>();

    private CampaignTournament activeTournament;
    private List<President> activeTournamentDeck;
    private int activeTournamentNextRound = 1;

    private CampaignMatchLauncher campaignMatchLauncher;

    // Normal Campaign Mode should be played manually through the board. This
    // testing switch is kept for economy/balance work and is exposed only from
    // the Campaign Menu, not on the main match/tournament screens.
    private boolean simTestMode = false;

    public RunCampaignMode() {
        this(null, null);
    }

    public RunCampaignMode(CampaignMatchLauncher campaignMatchLauncher) {
        this(campaignMatchLauncher, null);
    }

    public RunCampaignMode(
            CampaignMatchLauncher campaignMatchLauncher, CampaignNavigationHost navigationHost
    ) {
        this.campaignMatchLauncher = campaignMatchLauncher;
        this.navigationHost = navigationHost;
    }

    public void setCampaignMatchLauncher(CampaignMatchLauncher campaignMatchLauncher) {
        this.campaignMatchLauncher = campaignMatchLauncher;
    }

    public void setCampaignNavigationHost(CampaignNavigationHost navigationHost) {
        this.navigationHost = navigationHost;
    }

    /**
     * Builds Campaign Mode as a JPanel for the main one-window Campaign Clash
     * shell. The supplied host frame is used only as the parent for dialogs;
     * this class does not own or dispose it in embedded mode.
     */
    public JPanel buildEmbeddedPanel(JFrame hostFrame) {
        embeddedMode = true;
        ownsFrame = false;
        frame = hostFrame;

        campaign = new CampaignMode();
        initializeUiResources();
        rootPanel = buildRootPanel();
        return rootPanel;
    }

    /**
     * Called by the host after the embedded panel is visible. Returns false if
     * the player chose to quit from the Campaign Mode start dialog.
     */
    public boolean startEmbeddedCampaignSession() {
        if (!embeddedMode) {
            return false;
        }

        boolean started = showStartDialog();
        if (!started) {
            return false;
        }

        refreshDashboard();
        startPlayTimeUiTimer();
        displayActiveDeckCards();
        return true;
    }

    public JPanel getRootPanel() {
        return rootPanel;
    }

    @Override
    public void run() {
        embeddedMode = false;
        ownsFrame = true;
        campaign = new CampaignMode();
        initializeUiResources();
        buildFrame();
        frame.setVisible(true);

        boolean started = showStartDialog();
        if (!started) {
            frame.dispose();
            return;
        }

        refreshDashboard();
        startPlayTimeUiTimer();
        displayActiveDeckCards();
    }

    /**
     * Sets up shared scale, audio hook, CustomDialog images, and button art.
     */
    private void initializeUiResources() {
        Dimension fullScreenSize = Toolkit.getDefaultToolkit().getScreenSize();
        cardSize = (int) (fullScreenSize.width / 1920.0 * 275.0);
        if (cardSize <= 0) {
            cardSize = 275;
        }

        soundtrackPlayer = new SoundtrackPlayer(buildSoundtrackTracks());

        CustomDialog.setGlobalScale(cardSize / 275.0);
        CustomDialog.setGlobalPlayer(soundtrackPlayer);
        CustomDialog.setGlobalFontSize((int) (16.0 * (cardSize / 225.0)));
        CustomDialog.preloadImages();

        int buttonWidth = (int) (260 * (cardSize / 275.0));
        int buttonHeight = (int) (80 * (cardSize / 275.0));
        buttonIcon = loadScaledIcon("files/blankbutton.PNG", buttonWidth, buttonHeight);
        buttonHoverIcon = loadScaledIcon("files/blankbuttonhover.PNG", buttonWidth, buttonHeight);
        buttonClickedIcon = loadScaledIcon("files/blankbuttonclicked.PNG", buttonWidth, buttonHeight);
    }

    private List<String> buildSoundtrackTracks() {
        return Arrays.asList(
                PREFIX + "files/americathebeautiful.MP3",
                PREFIX + "files/battlehymnoftherepublic.MP3",
                PREFIX + "files/columbiathegemoftheocean.MP3",
                PREFIX + "files/dixie.MP3",
                PREFIX + "files/mycountrytisofthee.MP3",
                PREFIX + "files/starsandstripesforever.MP3",
                PREFIX + "files/starspangledbanner.MP3",
                PREFIX + "files/whenjohnnycomesmarchinghome.MP3",
                PREFIX + "files/yankeedoodle.MP3",
                PREFIX + "files/youreagrandoldflag.MP3"
        );
    }

    private void buildFrame() {
        frame = new JFrame("Campaign Clash - Campaign Mode");
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setMinimumSize(new Dimension(900, 650));
        frame.setLayout(new BorderLayout(10, 10));
        installFrameIconAndCursor();

        rootPanel = buildRootPanel();
        frame.setContentPane(rootPanel);

        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                confirmSaveAndQuit();
            }
        });

        applyFullScreenSizing();
    }

    private JPanel buildRootPanel() {
        JPanel root = new JPanel(new BorderLayout(10, 10));
        root.setBorder(new EmptyBorder(16, 16, 16, 16));
        root.setBackground(new Color(244, 239, 225));

        root.add(buildHeaderPanel(), BorderLayout.NORTH);

        JPanel center = new JPanel(new BorderLayout(10, 10));
        center.setOpaque(false);
        center.add(buildStatusPanel(), BorderLayout.NORTH);
        center.add(buildDisplayPanel(), BorderLayout.CENTER);
        root.add(center, BorderLayout.CENTER);

        root.add(buildButtonPanel(), BorderLayout.SOUTH);
        return root;
    }

    private JPanel buildHeaderPanel() {
        JPanel header = new JPanel(new BorderLayout(12, 0));
        header.setOpaque(false);

        JLabel title = new JLabel("Campaign Mode", SwingConstants.CENTER);
        title.setFont(new Font("Dialog", Font.BOLD, Math.max(28, (int) (34 * (cardSize / 275.0)))));
        title.setForeground(new Color(20, 35, 65));

        JLabel subtitle = new JLabel("Build your active deck, play matches, and spend Political Capital in the Store", SwingConstants.CENTER);
        subtitle.setFont(new Font("Dialog", Font.PLAIN, Math.max(14, (int) (16 * (cardSize / 275.0)))));
        subtitle.setForeground(new Color(70, 70, 70));

        JPanel titleStack = new JPanel(new GridLayout(2, 1));
        titleStack.setOpaque(false);
        titleStack.add(title);
        titleStack.add(subtitle);

        JLabel logo = buildLogoLabel();
        header.add(logo, BorderLayout.WEST);
        header.add(titleStack, BorderLayout.CENTER);
        header.add(BoxLikeSpacer.logoWidthSpacer(logo), BorderLayout.EAST);

        return header;
    }

    private JLabel buildLogoLabel() {
        ImageIcon logoIcon = loadScaledIcon("files/logo.PNG",
                Math.max(80, (int) (100 * (cardSize / 275.0))),
                Math.max(80, (int) (100 * (cardSize / 275.0))));
        JLabel logoLabel = new JLabel();
        if (logoIcon != null) {
            logoLabel.setIcon(logoIcon);
        }
        logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        logoLabel.setPreferredSize(new Dimension(Math.max(100, (int) (120 * (cardSize / 275.0))),
                Math.max(80, (int) (100 * (cardSize / 275.0)))));
        return logoLabel;
    }

    private JPanel buildStatusPanel() {
        JPanel statusPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        statusPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 160, 120), 2),
                new EmptyBorder(12, 12, 12, 12)
        ));
        statusPanel.setBackground(new Color(255, 250, 235));

        nameLabel = makeStatusLabel("Name: ");
        pcLabel = makeStatusLabel("Political Capital: ");
        recordLabel = makeStatusLabel("Record: ");
        collectionLabel = makeStatusLabel("Collection: ");
        activeDeckLabel = makeStatusLabel("Time Played: ");
        deckStrengthLabel = makeStatusLabel("Active Deck Strength: ");

        statusPanel.add(nameLabel);
        statusPanel.add(pcLabel);
        statusPanel.add(recordLabel);
        statusPanel.add(collectionLabel);
        statusPanel.add(activeDeckLabel);
        statusPanel.add(deckStrengthLabel);

        return statusPanel;
    }

    private JLabel makeStatusLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Dialog", Font.BOLD, Math.max(14, (int) (16 * (cardSize / 275.0)))));
        label.setForeground(new Color(35, 35, 35));
        return label;
    }

    private JPanel buildDisplayPanel() {
        JPanel outer = new JPanel(new BorderLayout(10, 10));
        outer.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 160, 120), 2),
                new EmptyBorder(12, 12, 12, 12)
        ));
        outer.setBackground(new Color(255, 253, 245));

        displayTitleLabel = new JLabel("Campaign Dashboard", SwingConstants.CENTER);
        displayTitleLabel.setFont(new Font("Dialog", Font.BOLD, Math.max(20, (int) (24 * (cardSize / 275.0)))));
        displayTitleLabel.setForeground(new Color(20, 35, 65));
        outer.add(displayTitleLabel, BorderLayout.NORTH);

        displayPanel = new JPanel(new BorderLayout());
        displayPanel.setBackground(new Color(255, 253, 245));
        JLabel placeholder = new JLabel("", SwingConstants.CENTER);
        placeholder.setFont(new Font("Dialog", Font.PLAIN, Math.max(18, (int) (22 * (cardSize / 275.0)))));
        placeholder.setForeground(new Color(90, 80, 65));
        displayPanel.add(placeholder, BorderLayout.CENTER);

        displayScrollPane = new JScrollPane(displayPanel);
        displayScrollPane.setBorder(BorderFactory.createEmptyBorder());
        displayScrollPane.getVerticalScrollBar().setUnitIncrement(24);
        displayScrollPane.getHorizontalScrollBar().setUnitIncrement(24);
        displayScrollPane.getViewport().setBackground(new Color(255, 253, 245));
        outer.add(displayScrollPane, BorderLayout.CENTER);

        return outer;
    }

    private JPanel buildButtonPanel() {
        actionButtons = new java.util.ArrayList<JButton>();

        JPanel buttonPanel = new JPanel(new GridLayout(1, 6, 10, 10));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(new EmptyBorder(8, 0, 0, 0));

        JButton playMatchButton = makeButton("Quick Play");
        playMatchButton.addActionListener(e -> displayMatchOptionsDashboard());

        JButton leagueButton = makeButton("League");
        leagueButton.addActionListener(e -> displayLeagueDashboard());

        JButton tournamentsButton = makeButton("Tournaments");
        tournamentsButton.addActionListener(e -> displayTournamentsDashboard());

        JButton packsButton = makeButton("Store");
        packsButton.addActionListener(e -> displayStoreDashboard());

        JButton collectionButton = makeButton("Collection");
        collectionButton.addActionListener(e -> displayCollectionCards());

        JButton quitButton = makeButton(embeddedMode ? "Campaign Menu" : "Save and Quit");
        quitButton.addActionListener(e -> confirmSaveAndQuit());

        buttonPanel.add(playMatchButton);
        buttonPanel.add(leagueButton);
        buttonPanel.add(tournamentsButton);
        buttonPanel.add(packsButton);
        buttonPanel.add(collectionButton);
        buttonPanel.add(quitButton);

        return buttonPanel;
    }

    private JButton makeButton(String text) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setOpaque(false);
        button.setForeground(new Color(222, 162, 6));
        button.setFont(new Font("Dialog", Font.BOLD, Math.max(14, (int) (18 * (cardSize / 275.0)))));
        button.setHorizontalTextPosition(SwingConstants.CENTER);
        button.setPreferredSize(new Dimension(
                Math.max(220, (int) (260 * (cardSize / 275.0))),
                Math.max(65, (int) (80 * (cardSize / 275.0)))
        ));

        if (buttonIcon != null) {
            button.setIcon(buttonIcon);
            button.setRolloverIcon(buttonHoverIcon);
            button.setPressedIcon(buttonClickedIcon);
        }

        button.addActionListener(e -> playButtonSound());
        actionButtons.add(button);
        return button;
    }

    private boolean showStartDialog() {
        while (true) {
            campaign.migrateLegacySingleSaveIfNeeded();

            if (campaign.hasAnySaveSlot()) {
                final int defaultSlot = campaign.getDefaultContinueSlot();
                CampaignMode.SaveSlotSummary defaultSummary = campaign.getSaveSlotSummary(defaultSlot);
                String welcomeName = defaultSummary == null || !defaultSummary.exists()
                        ? "Campaign Manager"
                        : defaultSummary.getPlayerName();

                int choice = showChoiceDialog(
                        "Welcome back, " + welcomeName + "!\n\n"
                                + "Continue your active campaign, load another save slot, "
                                + "or start a new campaign.\n\n"
                                + "Current campaign:\n"
                                + (defaultSummary == null
                                        ? "Slot " + defaultSlot
                                        : defaultSummary.getOneLineSummary()),
                        "files/setuptitle.PNG",
                        new String[] { "Continue Campaign", "Load Campaign", "New Campaign", "Quit" },
                        "settings",
                        1.2,
                        1.55
                );

                if (choice == 0) {
                    OperationResult result = captureBooleanOutput(() -> campaign.loadCampaignFromSlot(defaultSlot));
                    appendLog(result.output);
                    if (result.success) {
                        return true;
                    }
                    showShortDialog("Load Failed", "Load failed. You can load another save, start a new campaign, or quit.", true);
                } else if (choice == 1) {
                    if (showLoadCampaignDialog(true)) {
                        return true;
                    }
                } else if (choice == 2) {
                    if (showStartNewCampaignSlotDialog()) {
                        return true;
                    }
                } else {
                    return false;
                }
            } else {
                int choice = showChoiceDialog(
                        "Welcome to Campaign Mode!\n\nBuild your presidential collection, play matches, "
                                + "win era tournaments, and spend Political Capital in the Store.\n\n"
                                + "Ready to start a new campaign?",
                        "files/setuptitle.PNG",
                        new String[] { "Start New Campaign", "Quit" },
                        "settings",
                        1.15,
                        1.35
                );

                if (choice == 0) {
                    campaign.setActiveSaveSlot(1);
                    return startNewCampaignFromDialog();
                }
                return false;
            }
        }
    }

    private boolean showStartNewCampaignSlotDialog() {
        List<CampaignMode.SaveSlotSummary> summaries = campaign.getSaveSlotSummaries();
        String[] options = new String[summaries.size() + 1];
        for (int i = 0; i < summaries.size(); i++) {
            options[i] = summaries.get(i).getOneLineSummary();
        }
        options[summaries.size()] = "Back";

        int choice = showChoiceDialog(
                "Choose a save slot for the new campaign.\n\n"
                        + "Starting a new campaign in an occupied slot will overwrite that slot.",
                "files/setuptitle.PNG",
                options,
                "settings",
                1.15,
                1.85
        );

        if (choice < 0 || choice >= summaries.size()) {
            return false;
        }

        CampaignMode.SaveSlotSummary selected = summaries.get(choice);
        int slot = selected.getSlotNumber();
        if (selected.exists()) {
            int confirm = showChoiceDialog(
                    selected.getDetailedSummary()
                            + "\n\nThis will permanently overwrite Save Slot " + slot + ". Continue?",
                    "files/settingstitle.PNG",
                    new String[] { "Yes, Overwrite", "No" },
                    "settings",
                    1.15,
                    1.45
            );
            if (confirm != 0) {
                return false;
            }
        }

        campaign.setActiveSaveSlot(slot);
        return startNewCampaignFromDialog();
    }

    private boolean showLoadCampaignDialog(boolean startupMode) {
        while (true) {
            List<CampaignMode.SaveSlotSummary> summaries = campaign.getSaveSlotSummaries();
            String[] options = new String[summaries.size() + 1];
            for (int i = 0; i < summaries.size(); i++) {
                options[i] = summaries.get(i).getOneLineSummary();
            }
            options[summaries.size()] = startupMode ? "Back" : "Cancel";

            int choice = showChoiceDialog(
                    "Load Campaign\n\nChoose one of your Campaign Mode save slots.",
                    "files/setuptitle.PNG",
                    options,
                    "settings",
                    1.2,
                    1.95
            );

            if (choice < 0 || choice >= summaries.size()) {
                return false;
            }

            CampaignMode.SaveSlotSummary selected = summaries.get(choice);
            int slot = selected.getSlotNumber();

            if (!selected.exists()) {
                int emptyChoice = showChoiceDialog(
                        "Save Slot " + slot + " is empty.\n\nStart a new campaign in this slot?",
                        "files/setuptitle.PNG",
                        new String[] { "Start New Campaign", "Back" },
                        "settings",
                        0.95,
                        1.25
                );
                if (emptyChoice == 0) {
                    campaign.setActiveSaveSlot(slot);
                    return startNewCampaignFromDialog();
                }
                continue;
            }

            int action = showChoiceDialog(
                    selected.getDetailedSummary()
                            + (selected.hasPendingPlayableMatch()
                                    ? "\n\nWarning: this save has an abandoned playable match. Continuing will count it as a forfeit loss."
                                    : ""),
                    "files/setuptitle.PNG",
                    new String[] { "Continue", "Restart Slot", "Rename Slot", "Duplicate Slot", "Delete Slot", "Back" },
                    "settings",
                    1.25,
                    1.7
            );

            if (action == 0) {
                OperationResult result = captureBooleanOutput(() -> campaign.loadCampaignFromSlot(slot));
                appendLog(result.output);
                if (result.success) {
                    refreshDashboard();
                    displayActiveDeckCards();
                    return true;
                }
                showShortDialog("Load Failed", "Could not load Save Slot " + slot + ".", true);
            } else if (action == 1) {
                int confirm = showChoiceDialog(
                        "Restarting Save Slot " + slot + " will permanently overwrite this campaign.\n\nAre you absolutely sure?",
                        "files/settingstitle.PNG",
                        new String[] { "Yes, Restart", "No" },
                        "settings",
                        1.0,
                        1.35
                );
                if (confirm == 0) {
                    campaign.setActiveSaveSlot(slot);
                    return startNewCampaignFromDialog();
                }
            } else if (action == 2) {
                renameSaveSlotFromMenu(slot, selected);
            } else if (action == 3) {
                duplicateSaveSlotFromMenu(slot);
            } else if (action == 4) {
                int confirm = showChoiceDialog(
                        "Delete Save Slot " + slot + "?\n\nThis cannot be undone.",
                        "files/errortitle.PNG",
                        new String[] { "Yes, Delete", "No" },
                        "error",
                        0.9,
                        1.2
                );
                if (confirm == 0) {
                    OperationResult result = captureBooleanOutput(() -> campaign.deleteSaveSlot(slot));
                    appendLog(result.output);
                    showShortDialog(
                            result.success ? "Save Deleted" : "Delete Failed",
                            result.success
                                    ? "Save Slot " + slot + " deleted."
                                    : "Could not delete Save Slot " + slot + ".",
                            !result.success
                    );
                }
            }
        }
    }

    private void renameSaveSlotFromMenu(final int slot, CampaignMode.SaveSlotSummary summary) {
        if (summary == null || !summary.exists()) {
            showShortDialog("Rename Failed", "That save slot is empty.", true);
            return;
        }

        String currentName = summary.getPlayerName();
        String newName = CustomDialog.showInputDialog(
                frame,
                "Rename Save Slot " + slot + ".\n\nCurrent name: " + currentName
                        + "\n\nEnter the new campaign name:",
                "files/settingstitle.PNG"
        );

        if (newName == null) {
            return;
        }
        newName = newName.trim();
        if (newName.isEmpty()) {
            showShortDialog("Rename Failed", "Campaign name cannot be blank.", true);
            return;
        }

        final String cleanName = newName;
        OperationResult result = captureBooleanOutput(() -> campaign.renameSaveSlot(slot, cleanName));
        appendLog(result.output);
        if (result.success) {
            refreshDashboard();
        }
        showShortDialog(
                result.success ? "Save Renamed" : "Rename Failed",
                result.success
                        ? "Save Slot " + slot + " renamed to " + cleanName + "."
                        : "Could not rename Save Slot " + slot + ".",
                !result.success
        );
    }

    private void duplicateSaveSlotFromMenu(final int sourceSlot) {
        List<CampaignMode.SaveSlotSummary> summaries = campaign.getSaveSlotSummaries();
        ArrayList<CampaignMode.SaveSlotSummary> destinations = new ArrayList<CampaignMode.SaveSlotSummary>();
        for (CampaignMode.SaveSlotSummary summary : summaries) {
            if (summary != null && summary.getSlotNumber() != sourceSlot) {
                destinations.add(summary);
            }
        }

        if (destinations.isEmpty()) {
            showShortDialog("Duplicate Failed", "There is no other save slot available.", true);
            return;
        }

        String[] options = new String[destinations.size() + 1];
        for (int i = 0; i < destinations.size(); i++) {
            options[i] = destinations.get(i).getOneLineSummary();
        }
        options[destinations.size()] = "Back";

        int choice = showChoiceDialog(
                "Duplicate Save Slot " + sourceSlot + "\n\nChoose a destination slot.",
                "files/setuptitle.PNG",
                options,
                "settings",
                1.1,
                1.85
        );

        if (choice < 0 || choice >= destinations.size()) {
            return;
        }

        CampaignMode.SaveSlotSummary destination = destinations.get(choice);
        final int destinationSlot = destination.getSlotNumber();

        if (destination.exists()) {
            int confirm = showChoiceDialog(
                    destination.getDetailedSummary()
                            + "\n\nDuplicating Save Slot " + sourceSlot
                            + " into Save Slot " + destinationSlot
                            + " will permanently overwrite this destination slot. Continue?",
                    "files/settingstitle.PNG",
                    new String[] { "Yes, Overwrite", "No" },
                    "settings",
                    1.2,
                    1.55
            );
            if (confirm != 0) {
                return;
            }
        }

        OperationResult result = captureBooleanOutput(
                () -> campaign.duplicateSaveSlot(sourceSlot, destinationSlot)
        );
        appendLog(result.output);
        showShortDialog(
                result.success ? "Save Duplicated" : "Duplicate Failed",
                result.success
                        ? "Save Slot " + sourceSlot + " duplicated into Save Slot "
                                + destinationSlot + "."
                        : "Could not duplicate Save Slot " + sourceSlot + ".",
                !result.success
        );
    }

    private boolean startNewCampaignFromDialog() {
        String name = CustomDialog.showInputDialog(
                frame,
                "What should we call your campaign manager? Leave blank for Player:",
                "files/setuptitle.PNG"
        );

        if (name == null) {
            name = "";
        }
        name = name.trim();
        if (name.isEmpty()) {
            name = "Player";
        }

        int selectedSlot = campaign == null ? 1 : campaign.getActiveSaveSlot();
        campaign = new CampaignMode(name);
        campaign.setActiveSaveSlot(selectedSlot);
        OperationResult result = captureVoidOutput(() -> campaign.startNewCampaign());
        appendLog(result.output);
        showLongTextDialog("New Campaign Started", result.output);
        refreshDashboard();
        return true;
    }

    private void displayMatchOptionsDashboard() {
        cancelCollectionLoadWorker();
        showingActiveDeckCards = false;
        showingCollectionCards = false;

        if (displayPanel == null) {
            return;
        }

        displayTitleLabel.setText("Choose Quick Play Difficulty");
        displayPanel.removeAll();
        displayPanel.setLayout(new BorderLayout(14, 14));
        displayPanel.setBackground(new Color(255, 253, 245));

        JPanel wrapper = new JPanel(new BorderLayout(16, 16));
        wrapper.setOpaque(false);
        wrapper.setBorder(new EmptyBorder(30, 30, 30, 30));

        JLabel intro = new JLabel(
                "<html><center>Pick a quick play match. "
                        + getMatchModeHelpText()
                        + "</center></html>",
                SwingConstants.CENTER
        );
        intro.setFont(new Font("Dialog", Font.BOLD, Math.max(16, (int) (18 * (cardSize / 275.0)))));
        intro.setForeground(new Color(35, 35, 35));
        wrapper.add(intro, BorderLayout.NORTH);

        JPanel choices = new JPanel(new GridLayout(1, 3, 24, 24));
        choices.setOpaque(false);
        choices.add(buildMatchChoicePanel("Easy", "Easy AI", "Full card pool"));
        choices.add(buildMatchChoicePanel("Medium", "Medium AI", "3.0+ card pool"));
        choices.add(buildMatchChoicePanel("Hard", "Hard AI", "5.0+ card pool"));

        wrapper.add(choices, BorderLayout.CENTER);
        displayPanel.add(wrapper, BorderLayout.NORTH);
        updateDisplayPanel();
    }

    private JPanel buildMatchChoicePanel(String difficulty, String cpuAiText, String cpuDeckText) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(new Color(255, 250, 235));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 160, 120), 2),
                new EmptyBorder(16, 16, 16, 16)
        ));

        JLabel details = new JLabel(
                "<html><center>"
                        + "<b>" + difficulty + " Match</b><br><br>"
                        + "CPU: " + cpuAiText + "<br>"
                        + "Deck: " + cpuDeckText + "<br><br>"
                        + "Win: " + CampaignMode.formatPC(campaign.getMatchWinRewardPC(difficulty)) + "<br>"
                        + "Loss: " + CampaignMode.formatPC(campaign.getMatchLossRewardPC(difficulty))
                        + "</center></html>",
                SwingConstants.CENTER
        );
        details.setFont(new Font("Dialog", Font.BOLD, Math.max(14, (int) (17 * (cardSize / 275.0)))));
        details.setForeground(new Color(20, 35, 65));
        panel.add(details, BorderLayout.CENTER);

        JButton playButton = makeDashboardButton(getMatchActionVerb() + " " + difficulty);
        playButton.addActionListener(e -> playCampaignMatchFromUI(difficulty));
        panel.add(playButton, BorderLayout.SOUTH);

        return panel;
    }

    private boolean shouldUsePlayableMatches() {
        return campaignMatchLauncher != null && !simTestMode;
    }

    private String getMatchActionVerb() {
        return shouldUsePlayableMatches() ? "Play" : "Sim";
    }

    private String getMatchModeHelpText() {
        if (shouldUsePlayableMatches()) {
            return "Matches launch the real Campaign Clash board with your active deck.";
        }
        if (simTestMode) {
            return "Sim/Test Mode is ON, so matches use the AI-vs-AI simulation engine.";
        }
        return "Playable matches are available when Campaign Mode is launched from the main Campaign Clash home screen.";
    }

    private String getSimTestMenuLabel() {
        return simTestMode ? "Disable Sim/Test Mode" : "Enable Sim/Test Mode";
    }

    private void toggleSimTestModeFromMenu() {
        simTestMode = !simTestMode;
        String modeText = simTestMode
                ? "Sim/Test Mode is now ON. Match and tournament buttons will simulate results for testing."
                : "Sim/Test Mode is now OFF. Campaign Mode will launch playable matches from the Campaign Clash board.";
        showShortDialog("Campaign Mode", modeText, false);

        if (displayTitleLabel != null && "Choose Quick Play Difficulty".equals(displayTitleLabel.getText())) {
            displayMatchOptionsDashboard();
        }
    }

    private JButton makeDashboardButton(String text) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setOpaque(false);
        button.setForeground(new Color(222, 162, 6));
        button.setFont(new Font("Dialog", Font.BOLD, Math.max(14, (int) (16 * (cardSize / 275.0)))));
        button.setHorizontalTextPosition(SwingConstants.CENTER);
        button.setPreferredSize(new Dimension(
                Math.max(190, (int) (220 * (cardSize / 275.0))),
                Math.max(58, (int) (70 * (cardSize / 275.0)))
        ));

        if (buttonIcon != null) {
            button.setIcon(buttonIcon);
            button.setRolloverIcon(buttonHoverIcon);
            button.setPressedIcon(buttonClickedIcon);
        }

        button.addActionListener(e -> playButtonSound());
        return button;
    }

    private void hideCampaignWindowForPlayableMatch() {
        if (embeddedMode) {
            if (rootPanel != null) {
                rootPanel.setVisible(false);
            }
            return;
        }

        if (frame == null) {
            return;
        }

        frame.setVisible(false);
    }

    private void restoreCampaignWindowAfterPlayableMatch() {
        if (embeddedMode) {
            if (rootPanel != null) {
                rootPanel.setVisible(true);
            }
            if (navigationHost != null) {
                navigationHost.showCampaignModeScreen();
            }
            return;
        }

        if (frame == null) {
            return;
        }

        if (!frame.isVisible()) {
            frame.setVisible(true);
        }

        applyFullScreenSizing();
        frame.toFront();
        frame.requestFocus();
        frame.requestFocusInWindow();
    }

    private void showPlayableLaunchFailure(String title, String message, Runnable fallback) {
        restoreCampaignWindowAfterPlayableMatch();
        setActionButtonsEnabled(true);
        showShortDialog(title, message, true);
        if (fallback != null) {
            fallback.run();
        }
    }

    private void playCampaignMatchFromUI(String difficulty) {
        if (shouldUsePlayableMatches()) {
            playPlayableMatchFromUI(difficulty);
        } else {
            playSimulatedMatchFromUI(difficulty);
        }
    }

    private void playPlayableMatchFromUI(final String difficulty) {
        if (campaign == null || campaign.getPlayer() == null) {
            showShortDialog("No Campaign Loaded", "Start or continue a campaign first.", true);
            return;
        }
        if (campaignMatchLauncher == null) {
            showShortDialog(
                    "Playable Match Unavailable",
                    "Playable matches require Campaign Mode to be launched from the main Campaign Clash shell.\n"
                            + "Use Sim/Test Mode for now, or launch Campaign Mode from the main Campaign Clash home screen.",
                    true
            );
            return;
        }

        if (campaign.getActiveDeckSize() < campaign.getActiveDeckMaxSize()) {
            showShortDialog(
                    "Active Deck Not Ready",
                    "Your active deck needs " + campaign.getActiveDeckMaxSize()
                            + " Presidents before you can play a match.\nCurrent active deck: "
                            + campaign.getActiveDeckSize() + "/" + campaign.getActiveDeckMaxSize()
                            + "\nOpen more packs first or complete your custom deck.",
                    true
            );
            return;
        }

        final CampaignMatchConfig config = campaign.buildQuickMatchConfig(difficulty, true);
        String validationError = config.getValidationError();
        if (validationError != null && !validationError.isEmpty()) {
            showShortDialog("Playable Match Error", validationError, true);
            return;
        }

        setActionButtonsEnabled(false);

        OperationResult pendingResult = captureBooleanOutput(() -> campaign.beginPendingPlayableMatch(config));
        appendLog(pendingResult.output);
        if (!pendingResult.success) {
            setActionButtonsEnabled(true);
            displayMatchOptionsDashboard();
            showShortDialog(
                    "Playable Match Error",
                    "Could not save the pending playable match. The match was not launched.",
                    true
            );
            return;
        }

        appendLog("\nLaunching playable " + difficulty + " campaign match...\n");
        displayPlayableMatchInProgress(config);
        hideCampaignWindowForPlayableMatch();

        try {
            campaignMatchLauncher.launchCampaignMatch(config, new CampaignMatchCallback() {
                @Override
                public void onCampaignMatchComplete(final CampaignMatchResult result) {
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            restoreCampaignWindowAfterPlayableMatch();
                            handlePlayableQuickMatchResult(difficulty, result);
                        }
                    });
                }
            });
        } catch (Exception ex) {
            OperationResult clearResult = captureBooleanOutput(() -> campaign.clearPendingPlayableMatch());
            appendLog(clearResult.output);
            showPlayableLaunchFailure(
                    "Playable Match Error",
                    "Could not launch playable match:\n" + ex.getMessage(),
                    new Runnable() {
                        @Override
                        public void run() {
                            displayMatchOptionsDashboard();
                        }
                    }
            );
        }
    }

    private void handlePlayableQuickMatchResult(String difficulty, CampaignMatchResult result) {
        OperationResult applyResult = captureBooleanOutput(() -> campaign.applyQuickMatchResult(difficulty, result));
        appendLog(applyResult.output);
        refreshDashboard();
        displayMatchResult(difficulty, applyResult.output);
        setActionButtonsEnabled(true);
    }

    private void playSimulatedMatchFromUI(String difficulty) {
        if (campaign == null || campaign.getPlayer() == null) {
            showShortDialog("No Campaign Loaded", "Start or continue a campaign first.", true);
            return;
        }

        if (campaign.getActiveDeckSize() < campaign.getActiveDeckMaxSize()) {
            showShortDialog(
                    "Active Deck Not Ready",
                    "Your active deck needs " + campaign.getActiveDeckMaxSize()
                            + " Presidents before you can play a match.\nCurrent active deck: "
                            + campaign.getActiveDeckSize() + "/" + campaign.getActiveDeckMaxSize()
                            + "\nOpen more packs first or complete your custom deck.",
                    true
            );
            return;
        }

        setActionButtonsEnabled(false);
        appendLog("\nSimulating " + difficulty + " campaign match...\n");
        displayMatchInProgress(difficulty);

        SwingWorker<OperationResult, Void> worker = new SwingWorker<OperationResult, Void>() {
            @Override
            protected OperationResult doInBackground() {
                return captureVoidOutput(() -> campaign.playSimulatedMatch(difficulty));
            }

            @Override
            protected void done() {
                try {
                    OperationResult result = get();
                    appendLog(result.output);
                    refreshDashboard();
                    displayMatchResult(difficulty, result.output);
                } catch (Exception ex) {
                    showShortDialog("Simulation Error", "Could not simulate match:\n" + ex.getMessage(), true);
                } finally {
                    setActionButtonsEnabled(true);
                }
            }
        };
        worker.execute();
    }

    private void displayPlayableMatchInProgress(CampaignMatchConfig config) {
        cancelCollectionLoadWorker();
        showingActiveDeckCards = false;
        showingCollectionCards = false;

        if (displayPanel == null) {
            return;
        }

        String title = config == null ? "Playable Match" : config.getMatchTitle();
        displayTitleLabel.setText(title);
        displayPanel.removeAll();
        displayPanel.setLayout(new BorderLayout());
        displayPanel.setBackground(new Color(255, 253, 245));
        displayPanel.add(makeCenteredDisplayMessage(
                "Launching playable match...\nFinish the match on the Campaign Clash board to return here."
        ), BorderLayout.CENTER);
        updateDisplayPanel();
    }

    private void displayMatchInProgress(String difficulty) {
        cancelCollectionLoadWorker();
        showingActiveDeckCards = false;
        showingCollectionCards = false;

        if (displayPanel == null) {
            return;
        }

        displayTitleLabel.setText(difficulty + " Match");
        displayPanel.removeAll();
        displayPanel.setLayout(new BorderLayout());
        displayPanel.setBackground(new Color(255, 253, 245));
        displayPanel.add(makeCenteredDisplayMessage("Simulating " + difficulty + " match..."), BorderLayout.CENTER);
        updateDisplayPanel();
    }

    private void displayMatchResult(String difficulty, String output) {
        cancelCollectionLoadWorker();
        showingActiveDeckCards = false;
        showingCollectionCards = false;

        if (displayPanel == null) {
            return;
        }

        displayTitleLabel.setText(difficulty + " Match Result");
        displayPanel.removeAll();
        displayPanel.setLayout(new BorderLayout(12, 12));
        displayPanel.setBackground(new Color(255, 253, 245));

        JTextArea resultArea = new JTextArea(output == null || output.trim().isEmpty()
                ? "Match simulated."
                : output.trim());
        resultArea.setEditable(false);
        resultArea.setLineWrap(true);
        resultArea.setWrapStyleWord(true);
        resultArea.setFont(new Font("Monospaced", Font.PLAIN,
                Math.max(13, (int) (15 * (cardSize / 275.0)))));
        resultArea.setBackground(new Color(255, 253, 245));
        resultArea.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 160, 120), 1),
                new EmptyBorder(14, 14, 14, 14)
        ));

        displayPanel.add(resultArea, BorderLayout.CENTER);
        updateDisplayPanel();
    }


    private void displayLeagueDashboard() {
        cancelCollectionLoadWorker();
        showingActiveDeckCards = false;
        showingCollectionCards = false;

        if (displayPanel == null) {
            return;
        }

        displayTitleLabel.setText("League Mode");
        displayPanel.removeAll();
        displayPanel.setLayout(new BorderLayout(14, 14));
        displayPanel.setBackground(new Color(255, 253, 245));

        JPanel wrapper = new JPanel(new BorderLayout(18, 18));
        wrapper.setOpaque(false);
        wrapper.setBorder(new EmptyBorder(26, 28, 28, 28));

        CampaignPlayer player = campaign == null ? null : campaign.getPlayer();
        CampaignLeagueDivision currentDivision = campaign == null ? null : campaign.getCurrentLeagueDivision();
        CampaignLeagueDivision nextDivision = campaign == null ? null : campaign.getNextLeagueDivision();
        CampaignLeagueSeason season = campaign == null ? null : campaign.getActiveLeagueSeason();
        CampaignLeagueDivision seasonDivision = campaign == null ? null : campaign.getActiveLeagueSeasonDivision();

        JLabel intro = new JLabel(
                "<html><center><b>League Mode</b><br>"
                        + "A 10-game playable season using your full owned collection. "
                        + "You are placed in the highest league your collection qualifies for."
                        + "<br>CPU decks use the broad 1.0+ pool; division difficulty controls the challenge."
                        + "</center></html>",
                SwingConstants.CENTER
        );
        intro.setFont(new Font("Dialog", Font.BOLD, Math.max(16, (int) (18 * (cardSize / 275.0)))));
        intro.setForeground(new Color(20, 35, 65));
        wrapper.add(intro, BorderLayout.NORTH);

        JPanel center = new JPanel(new GridLayout(1, 2, 22, 0));
        center.setOpaque(false);
        center.add(buildLeagueStatusPanel(player, currentDivision, nextDivision));
        center.add(buildLeagueSeasonPanel(season, seasonDivision));
        wrapper.add(center, BorderLayout.CENTER);

        JPanel buttons = new JPanel(new GridLayout(1, 4, 16, 0));
        buttons.setOpaque(false);
        buttons.setBorder(new EmptyBorder(16, 0, 0, 0));

        JButton primaryButton;
        if (season != null && season.isCompleted()) {
            primaryButton = makeDashboardButton(season.isRewardClaimed() ? "Season Complete" : "Claim Reward");
            primaryButton.setEnabled(!season.isRewardClaimed());
            primaryButton.addActionListener(e -> claimLeagueRewardFromUI());
        } else if (season != null) {
            primaryButton = makeDashboardButton(getMatchActionVerb() + " Next Game");
            primaryButton.addActionListener(e -> playLeagueGameFromUI());
        } else {
            primaryButton = makeDashboardButton(getMatchActionVerb() + " Game 1");
            primaryButton.setEnabled(currentDivision != null);
            primaryButton.addActionListener(e -> playLeagueGameFromUI());
        }

        JButton divisionsButton = makeDashboardButton("Divisions");
        divisionsButton.addActionListener(e -> displayLeagueDivisions());
        JButton recordsButton = makeDashboardButton("Records");
        recordsButton.addActionListener(e -> displayLeagueRecords());
        JButton collectionButton = makeDashboardButton("Collection");
        collectionButton.addActionListener(e -> displayCollectionCards());

        buttons.add(primaryButton);
        buttons.add(divisionsButton);
        buttons.add(recordsButton);
        buttons.add(collectionButton);
        wrapper.add(buttons, BorderLayout.SOUTH);

        displayPanel.add(wrapper, BorderLayout.NORTH);
        updateDisplayPanel();
    }

    private JPanel buildLeagueStatusPanel(
            CampaignPlayer player, CampaignLeagueDivision currentDivision, CampaignLeagueDivision nextDivision
    ) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(new Color(255, 250, 235));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 160, 120), 2),
                new EmptyBorder(18, 18, 18, 18)
        ));

        int collectionSize = player == null ? 0 : player.getCollectionSize();
        String currentText = currentDivision == null
                ? "Locked"
                : currentDivision.getDisplayName();
        String nextText;
        if (nextDivision == null) {
            nextText = currentDivision == null ? "Need 25 cards to unlock Local League" : "Top league reached";
        } else {
            int needed = Math.max(0, nextDivision.getMinimumCollectionSize() - collectionSize);
            nextText = nextDivision.getDisplayName() + " at " + nextDivision.getMinimumCollectionSize()
                    + " cards (need " + needed + ")";
        }

        JLabel details = new JLabel(
                "<html><center>"
                        + "<b>Your League Placement</b><br><br>"
                        + "Collection: <b>" + collectionSize + " cards</b><br>"
                        + "Current League: <b>" + escapeHtml(currentText) + "</b><br>"
                        + "Next League: " + escapeHtml(nextText) + "<br><br>"
                        + "You cannot farm lower leagues. Once your collection grows, "
                        + "your next season moves up automatically."
                        + "</center></html>",
                SwingConstants.CENTER
        );
        details.setFont(new Font("Dialog", Font.BOLD, Math.max(14, (int) (16 * (cardSize / 275.0)))));
        details.setForeground(new Color(20, 35, 65));
        panel.add(details, BorderLayout.CENTER);
        return panel;
    }

    private JPanel buildLeagueSeasonPanel(CampaignLeagueSeason season, CampaignLeagueDivision division) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(new Color(255, 250, 235));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 160, 120), 2),
                new EmptyBorder(18, 18, 18, 18)
        ));

        String html;
        if (season == null || division == null) {
            html = "<html><center>"
                    + "<b>No Active Season</b><br><br>"
                    + "Click " + escapeHtml(getMatchActionVerb()) + " Game 1 to start a 10-game League season.<br>"
                    + "The season division locks when the first game launches.<br>"
                    + "Final reward packs unlock at 3, 6, and 9 wins."
                    + "</center></html>";
        } else {
            CampaignLeagueDivision.RewardTier currentTier = campaign.getLeagueSeasonRewardTier(season, division);
            CampaignLeagueDivision.RewardTier nextTier = division.getNextRewardTierForWins(season.getWins());
            String currentReward = currentTier == null
                    ? "No final pack yet"
                    : currentTier.getPackName(division);
            String nextReward = nextTier == null
                    ? "Top reward tier reached"
                    : nextTier.getWinsRequired() + "+ wins: " + nextTier.getPackName(division);
            String status = season.isCompleted()
                    ? (season.isRewardClaimed() ? "Completed / Reward Claimed" : "Completed / Reward Ready")
                    : "In Progress";

            html = "<html><center>"
                    + "<b>Active Season</b><br><br>"
                    + escapeHtml(division.getDisplayName()) + "<br>"
                    + "Status: <b>" + escapeHtml(status) + "</b><br>"
                    + "Record: <b>" + season.getRecordText() + "</b> | Games: "
                    + season.getProgressText() + "<br>"
                    + "Difficulty: " + escapeHtml(division.getDifficulty())
                    + " | CPU: " + escapeHtml(campaign.getLeagueCpuDeckDescription(division)) + "<br>"
                    + "Game Rewards: " + CampaignMode.formatPC(division.getWinRewardPC()) + " win / "
                    + CampaignMode.formatPC(division.getLossRewardPC()) + " loss<br><br>"
                    + "Current Final Reward: <b>" + escapeHtml(currentReward) + "</b><br>"
                    + "Next Reward: " + escapeHtml(nextReward) + "<br>"
                    + "Last Result: " + escapeHtml(season.getLastResult())
                    + "</center></html>";
        }

        JLabel details = new JLabel(html, SwingConstants.CENTER);
        details.setFont(new Font("Dialog", Font.BOLD, Math.max(14, (int) (16 * (cardSize / 275.0)))));
        details.setForeground(new Color(20, 35, 65));
        panel.add(details, BorderLayout.CENTER);
        return panel;
    }

    private void startLeagueSeasonFromUI() {
        if (campaign == null || campaign.getPlayer() == null) {
            showShortDialog("No Campaign Loaded", "Start or continue a campaign first.", true);
            return;
        }
        if (!campaign.canStartLeagueSeason()) {
            showShortDialog("League Unavailable", campaign.getLeagueStatusText(), true);
            return;
        }

        OperationResult result = captureBooleanOutput(() -> campaign.startLeagueSeason());
        appendLog(result.output);
        refreshDashboard();
        if (!result.success) {
            showShortDialog("League Error", result.output == null ? "Could not start season." : result.output.trim(), true);
            return;
        }
        displayLeagueDashboard();
    }

    private boolean ensureLeagueSeasonStartedForNextGame() {
        CampaignLeagueSeason season = campaign == null ? null : campaign.getActiveLeagueSeason();
        if (season != null) {
            return true;
        }

        if (campaign == null || campaign.getPlayer() == null) {
            showShortDialog("No Campaign Loaded", "Start or continue a campaign first.", true);
            return false;
        }
        if (!campaign.canStartLeagueSeason()) {
            showShortDialog("League Unavailable", campaign.getLeagueStatusText(), true);
            return false;
        }

        OperationResult result = captureBooleanOutput(() -> campaign.startLeagueSeason());
        appendLog(result.output);
        refreshDashboard();
        if (!result.success) {
            showShortDialog(
                    "League Error",
                    result.output == null || result.output.trim().isEmpty()
                            ? "Could not start League season."
                            : result.output.trim(),
                    true
            );
            return false;
        }
        return true;
    }

    private void playLeagueGameFromUI() {
        if (shouldUsePlayableMatches()) {
            playPlayableLeagueGameFromUI();
        } else {
            playSimulatedLeagueGameFromUI();
        }
    }

    private void playPlayableLeagueGameFromUI() {
        if (campaign == null || campaign.getPlayer() == null) {
            showShortDialog("No Campaign Loaded", "Start or continue a campaign first.", true);
            return;
        }
        if (campaignMatchLauncher == null) {
            showShortDialog(
                    "Playable Match Unavailable",
                    "Playable league games require Campaign Mode to be launched from the main Campaign Clash shell.\n"
                            + "Use Sim/Test Mode for now, or launch Campaign Mode from the main Campaign Clash home screen.",
                    true
            );
            return;
        }
        if (!ensureLeagueSeasonStartedForNextGame()) {
            return;
        }
        if (campaign.getActiveLeagueSeason().isCompleted()) {
            showShortDialog("Season Complete", "Claim your League reward before starting another season.", false);
            return;
        }

        final CampaignMatchConfig config;
        try {
            config = campaign.buildLeagueMatchConfig(true);
        } catch (Exception ex) {
            showShortDialog("League Error", "Could not build league game:\n" + ex.getMessage(), true);
            return;
        }

        String validationError = config.getValidationError();
        if (validationError != null && !validationError.isEmpty()) {
            showShortDialog("Playable League Error", validationError, true);
            return;
        }

        setActionButtonsEnabled(false);
        OperationResult pendingResult = captureBooleanOutput(() -> campaign.beginPendingPlayableMatch(config));
        appendLog(pendingResult.output);
        if (!pendingResult.success) {
            setActionButtonsEnabled(true);
            displayLeagueDashboard();
            showShortDialog(
                    "Playable League Error",
                    "Could not save the pending league game. The game was not launched.",
                    true
            );
            return;
        }

        appendLog("\nLaunching playable " + config.getMatchTitle() + "...\n");
        displayPlayableLeagueGameInProgress(config);
        hideCampaignWindowForPlayableMatch();

        try {
            campaignMatchLauncher.launchCampaignMatch(config, new CampaignMatchCallback() {
                @Override
                public void onCampaignMatchComplete(final CampaignMatchResult result) {
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            restoreCampaignWindowAfterPlayableMatch();
                            handlePlayableLeagueGameResult(result);
                        }
                    });
                }
            });
        } catch (Exception ex) {
            OperationResult clearResult = captureBooleanOutput(() -> campaign.clearPendingPlayableMatch());
            appendLog(clearResult.output);
            showPlayableLaunchFailure(
                    "Playable League Error",
                    "Could not launch playable league game:\n" + ex.getMessage(),
                    new Runnable() {
                        @Override
                        public void run() {
                            displayLeagueDashboard();
                        }
                    }
            );
        }
    }

    private void handlePlayableLeagueGameResult(CampaignMatchResult result) {
        OperationResult applyResult = captureBooleanOutput(() -> campaign.applyLeagueMatchResult(result));
        appendLog(applyResult.output);
        refreshDashboard();
        displayLeagueGameResult(applyResult.output);
        setActionButtonsEnabled(true);
    }

    private void playSimulatedLeagueGameFromUI() {
        if (campaign == null || campaign.getPlayer() == null) {
            showShortDialog("No Campaign Loaded", "Start or continue a campaign first.", true);
            return;
        }
        if (!ensureLeagueSeasonStartedForNextGame()) {
            return;
        }
        if (campaign.getActiveLeagueSeason().isCompleted()) {
            showShortDialog("Season Complete", "Claim your League reward before starting another season.", false);
            return;
        }

        setActionButtonsEnabled(false);
        appendLog("\nSimulating League game...\n");
        displayLeagueGameInProgress();

        SwingWorker<OperationResult, Void> worker = new SwingWorker<OperationResult, Void>() {
            @Override
            protected OperationResult doInBackground() {
                return captureBooleanOutput(() -> campaign.playLeagueGame(false));
            }

            @Override
            protected void done() {
                try {
                    OperationResult result = get();
                    appendLog(result.output);
                    refreshDashboard();
                    displayLeagueGameResult(result.output);
                } catch (Exception ex) {
                    showShortDialog("League Error", "Could not run League game:\n" + ex.getMessage(), true);
                } finally {
                    setActionButtonsEnabled(true);
                }
            }
        };
        worker.execute();
    }

    private void displayPlayableLeagueGameInProgress(CampaignMatchConfig config) {
        cancelCollectionLoadWorker();
        showingActiveDeckCards = false;
        showingCollectionCards = false;

        if (displayPanel == null) {
            return;
        }

        String title = config == null ? "Playable League Game" : config.getMatchTitle();
        displayTitleLabel.setText(title);
        displayPanel.removeAll();
        displayPanel.setLayout(new BorderLayout());
        displayPanel.setBackground(new Color(255, 253, 245));
        displayPanel.add(makeCenteredDisplayMessage(
                "Launching playable League game...\nFinish the match on the Campaign Clash board to return here."
        ), BorderLayout.CENTER);
        updateDisplayPanel();
    }

    private void displayLeagueGameInProgress() {
        cancelCollectionLoadWorker();
        showingActiveDeckCards = false;
        showingCollectionCards = false;

        if (displayPanel == null) {
            return;
        }

        displayTitleLabel.setText("League Game");
        displayPanel.removeAll();
        displayPanel.setLayout(new BorderLayout());
        displayPanel.setBackground(new Color(255, 253, 245));
        displayPanel.add(makeCenteredDisplayMessage("Playing League game..."), BorderLayout.CENTER);
        updateDisplayPanel();
    }

    private void displayLeagueGameResult(String output) {
        cancelCollectionLoadWorker();
        showingActiveDeckCards = false;
        showingCollectionCards = false;

        if (displayPanel == null) {
            return;
        }

        displayTitleLabel.setText("League Game Result");
        displayPanel.removeAll();
        displayPanel.setLayout(new BorderLayout(12, 12));
        displayPanel.setBackground(new Color(255, 253, 245));

        JPanel resultPanel = new JPanel(new BorderLayout(12, 12));
        resultPanel.setBackground(new Color(255, 253, 245));
        resultPanel.setBorder(new EmptyBorder(18, 18, 18, 18));

        JTextArea resultArea = new JTextArea(output == null || output.trim().isEmpty()
                ? "League game completed."
                : output.trim());
        resultArea.setEditable(false);
        resultArea.setLineWrap(true);
        resultArea.setWrapStyleWord(true);
        resultArea.setFont(new Font("Monospaced", Font.PLAIN,
                Math.max(13, (int) (15 * (cardSize / 275.0)))));
        resultArea.setBackground(new Color(255, 253, 245));
        resultArea.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 160, 120), 1),
                new EmptyBorder(14, 14, 14, 14)
        ));
        resultPanel.add(resultArea, BorderLayout.CENTER);

        CampaignLeagueSeason season = campaign == null ? null : campaign.getActiveLeagueSeason();
        boolean completed = season != null && season.isCompleted();

        JPanel followUpButtons = new JPanel(new GridLayout(1, 2, 18, 0));
        followUpButtons.setOpaque(false);
        followUpButtons.setBorder(new EmptyBorder(4, 0, 0, 0));

        JButton primaryButton = makeDashboardButton(completed ? "Claim Reward" : getMatchActionVerb() + " Next Game");
        primaryButton.addActionListener(e -> {
            CampaignLeagueSeason activeSeason = campaign == null ? null : campaign.getActiveLeagueSeason();
            if (activeSeason != null && activeSeason.isCompleted()) {
                claimLeagueRewardFromUI();
            } else {
                playLeagueGameFromUI();
            }
        });
        JButton backButton = makeDashboardButton("Back to League");
        backButton.addActionListener(e -> displayLeagueDashboard());
        followUpButtons.add(primaryButton);
        followUpButtons.add(backButton);
        resultPanel.add(followUpButtons, BorderLayout.SOUTH);

        displayPanel.add(resultPanel, BorderLayout.CENTER);
        updateDisplayPanel();
    }

    private void claimLeagueRewardFromUI() {
        if (campaign == null || campaign.getPlayer() == null) {
            showShortDialog("No Campaign Loaded", "Start or continue a campaign first.", true);
            return;
        }
        CampaignLeagueSeason season = campaign.getActiveLeagueSeason();
        if (season == null) {
            showShortDialog("No Active Season", "There is no completed League season to claim.", true);
            return;
        }
        if (!season.isCompleted()) {
            showShortDialog("Season Incomplete", "Finish all " + season.getSeasonLength()
                    + " League games before claiming your final reward.", true);
            return;
        }

        OperationResult result = captureBooleanOutput(() -> campaign.claimLeagueSeasonReward());
        appendLog(result.output);
        refreshDashboard();
        if (!result.success) {
            showShortDialog("League Reward Error", result.output == null ? "Could not claim reward." : result.output.trim(), true);
            return;
        }
        displayLeagueRewardResult(result.output);
    }

    private void displayLeagueRewardResult(String output) {
        cancelCollectionLoadWorker();
        showingActiveDeckCards = false;
        showingCollectionCards = false;

        if (displayPanel == null) {
            return;
        }

        displayTitleLabel.setText("League Season Reward");
        displayPanel.removeAll();
        displayPanel.setLayout(new BorderLayout(12, 12));
        displayPanel.setBackground(new Color(255, 253, 245));

        JPanel resultPanel = new JPanel(new BorderLayout(12, 12));
        resultPanel.setBackground(new Color(255, 253, 245));
        resultPanel.setBorder(new EmptyBorder(18, 18, 18, 18));

        List<President> pulledCards = extractPresidentsFromPackOutput(output);
        if (!pulledCards.isEmpty()) {
            int imageWidth = getPackResultCardImageWidth();
            int columns = getPackResultColumnCount(imageWidth, pulledCards.size());
            JPanel cardGrid = new JPanel(new GridLayout(0, columns, 20, 20));
            cardGrid.setBackground(new Color(255, 253, 245));
            cardGrid.setBorder(new EmptyBorder(8, 0, 10, 0));

            for (President president : pulledCards) {
                cardGrid.add(buildPresidentCardPanel(president, imageWidth));
            }
            resultPanel.add(cardGrid, BorderLayout.NORTH);
        }

        JTextArea resultArea = new JTextArea(output == null || output.trim().isEmpty()
                ? "League reward claimed."
                : output.trim());
        resultArea.setEditable(false);
        resultArea.setLineWrap(true);
        resultArea.setWrapStyleWord(true);
        resultArea.setFont(new Font("Monospaced", Font.PLAIN,
                Math.max(13, (int) (15 * (cardSize / 275.0)))));
        resultArea.setBackground(new Color(255, 253, 245));
        resultArea.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 160, 120), 1),
                new EmptyBorder(14, 14, 14, 14)
        ));
        resultPanel.add(resultArea, BorderLayout.CENTER);

        JPanel followUpButtons = new JPanel(new GridLayout(1, 2, 18, 0));
        followUpButtons.setOpaque(false);
        followUpButtons.setBorder(new EmptyBorder(4, 0, 0, 0));
        JButton leagueButton = makeDashboardButton("Back to League");
        leagueButton.addActionListener(e -> displayLeagueDashboard());
        JButton collectionButton = makeDashboardButton("Collection");
        collectionButton.addActionListener(e -> displayCollectionCards());
        followUpButtons.add(leagueButton);
        followUpButtons.add(collectionButton);
        resultPanel.add(followUpButtons, BorderLayout.SOUTH);

        displayPanel.add(resultPanel, BorderLayout.CENTER);
        updateDisplayPanel();
    }

    private void displayLeagueDivisions() {
        cancelCollectionLoadWorker();
        showingActiveDeckCards = false;
        showingCollectionCards = false;

        if (displayPanel == null) {
            return;
        }

        displayTitleLabel.setText("League Divisions");
        displayPanel.removeAll();
        displayPanel.setLayout(new BorderLayout(14, 14));
        displayPanel.setBackground(new Color(255, 253, 245));

        JPanel wrapper = new JPanel(new BorderLayout(16, 16));
        wrapper.setOpaque(false);
        wrapper.setBorder(new EmptyBorder(24, 24, 24, 24));

        JLabel intro = new JLabel(
                "<html><center><b>League Divisions</b><br>"
                        + "Your current league is always the highest division your collection size unlocks."
                        + "</center></html>",
                SwingConstants.CENTER
        );
        intro.setFont(new Font("Dialog", Font.BOLD, Math.max(16, (int) (18 * (cardSize / 275.0)))));
        intro.setForeground(new Color(20, 35, 65));
        wrapper.add(intro, BorderLayout.NORTH);

        JPanel grid = new JPanel(new GridLayout(0, 1, 0, 14));
        grid.setOpaque(false);
        List<CampaignLeagueDivision> divisions = campaign == null
                ? new ArrayList<CampaignLeagueDivision>()
                : campaign.getLeagueDivisions();
        Collections.sort(divisions, new Comparator<CampaignLeagueDivision>() {
            @Override
            public int compare(CampaignLeagueDivision a, CampaignLeagueDivision b) {
                return b.getDivisionNumber() - a.getDivisionNumber();
            }
        });
        for (CampaignLeagueDivision division : divisions) {
            grid.add(buildLeagueDivisionPanel(division));
        }
        wrapper.add(grid, BorderLayout.CENTER);

        JPanel buttons = new JPanel(new GridLayout(1, 2, 18, 0));
        buttons.setOpaque(false);
        buttons.setBorder(new EmptyBorder(16, 0, 0, 0));
        JButton backButton = makeDashboardButton("Back to League");
        backButton.addActionListener(e -> displayLeagueDashboard());
        JButton recordsButton = makeDashboardButton("Records");
        recordsButton.addActionListener(e -> displayLeagueRecords());
        buttons.add(backButton);
        buttons.add(recordsButton);
        wrapper.add(buttons, BorderLayout.SOUTH);

        displayPanel.add(wrapper, BorderLayout.NORTH);
        updateDisplayPanel();
    }

    private JPanel buildLeagueDivisionPanel(CampaignLeagueDivision division) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        CampaignPlayer player = campaign == null ? null : campaign.getPlayer();
        int collectionSize = player == null ? 0 : player.getCollectionSize();
        CampaignLeagueDivision current = campaign == null ? null : campaign.getCurrentLeagueDivision();
        boolean unlocked = division != null && division.isUnlockedByCollectionSize(collectionSize);
        boolean currentDivision = division != null && division.equals(current);

        panel.setBackground(currentDivision ? new Color(255, 248, 220) : new Color(255, 250, 235));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(currentDivision ? new Color(190, 150, 50) : new Color(180, 160, 120), 2),
                new EmptyBorder(14, 16, 14, 16)
        ));

        CampaignLeagueRecord record = campaign == null || division == null
                ? new CampaignLeagueRecord("")
                : campaign.getLeagueRecord(division);
        String status = currentDivision
                ? "Current League"
                : (unlocked ? "Unlocked but below current league" : "Locked");
        String neededText = unlocked
                ? "Unlocked"
                : "Need " + Math.max(0, division.getMinimumCollectionSize() - collectionSize) + " more cards";

        JLabel details = new JLabel(
                "<html><center>"
                        + "<b>" + escapeHtml(division.getDisplayName()) + "</b><br>"
                        + escapeHtml(status) + " | " + escapeHtml(neededText) + "<br>"
                        + "Collection Gate: " + division.getMinimumCollectionSize() + "+ cards"
                        + " | Difficulty: " + escapeHtml(division.getDifficulty())
                        + " | CPU: " + String.format("%.1f+", division.getCpuMinimumRating()) + "<br>"
                        + "Per Game: " + CampaignMode.formatPC(division.getWinRewardPC()) + " win / "
                        + CampaignMode.formatPC(division.getLossRewardPC()) + " loss<br>"
                        + "Final Rewards: " + escapeHtml(division.getRewardSummaryText()) + "<br>"
                        + "History: " + record.getSeasonsPlayed() + " seasons | Best: "
                        + record.getBestWins() + "-" + (division.getSeasonLength() - record.getBestWins())
                        + " | Total: " + escapeHtml(record.getTotalRecordText())
                        + "</center></html>",
                SwingConstants.CENTER
        );
        details.setFont(new Font("Dialog", Font.BOLD, Math.max(13, (int) (15 * (cardSize / 275.0)))));
        details.setForeground(new Color(20, 35, 65));
        panel.add(details, BorderLayout.CENTER);
        return panel;
    }

    private void displayLeagueRecords() {
        cancelCollectionLoadWorker();
        showingActiveDeckCards = false;
        showingCollectionCards = false;

        if (displayPanel == null) {
            return;
        }

        displayTitleLabel.setText("League Records");
        displayPanel.removeAll();
        displayPanel.setLayout(new BorderLayout(14, 14));
        displayPanel.setBackground(new Color(255, 253, 245));

        JPanel wrapper = new JPanel(new BorderLayout(16, 16));
        wrapper.setOpaque(false);
        wrapper.setBorder(new EmptyBorder(24, 24, 24, 24));

        JLabel intro = new JLabel(
                "<html><center><b>League Records</b><br>"
                        + "Permanent history for completed League seasons. A League title means a 9+ win season."
                        + "</center></html>",
                SwingConstants.CENTER
        );
        intro.setFont(new Font("Dialog", Font.BOLD, Math.max(16, (int) (18 * (cardSize / 275.0)))));
        intro.setForeground(new Color(20, 35, 65));
        wrapper.add(intro, BorderLayout.NORTH);

        JPanel grid = new JPanel(new GridLayout(0, 1, 0, 14));
        grid.setOpaque(false);
        List<CampaignLeagueDivision> divisions = campaign == null
                ? new ArrayList<CampaignLeagueDivision>()
                : campaign.getLeagueDivisions();
        Collections.sort(divisions, new Comparator<CampaignLeagueDivision>() {
            @Override
            public int compare(CampaignLeagueDivision a, CampaignLeagueDivision b) {
                return b.getDivisionNumber() - a.getDivisionNumber();
            }
        });
        for (CampaignLeagueDivision division : divisions) {
            grid.add(buildLeagueRecordPanel(division));
        }
        wrapper.add(grid, BorderLayout.CENTER);

        JPanel buttons = new JPanel(new GridLayout(1, 2, 18, 0));
        buttons.setOpaque(false);
        buttons.setBorder(new EmptyBorder(16, 0, 0, 0));
        JButton backButton = makeDashboardButton("Back to League");
        backButton.addActionListener(e -> displayLeagueDashboard());
        JButton divisionsButton = makeDashboardButton("Divisions");
        divisionsButton.addActionListener(e -> displayLeagueDivisions());
        buttons.add(backButton);
        buttons.add(divisionsButton);
        wrapper.add(buttons, BorderLayout.SOUTH);

        displayPanel.add(wrapper, BorderLayout.NORTH);
        updateDisplayPanel();
    }

    private JPanel buildLeagueRecordPanel(CampaignLeagueDivision division) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        CampaignPlayer player = campaign == null ? null : campaign.getPlayer();
        int collectionSize = player == null ? 0 : player.getCollectionSize();
        boolean eligible = division != null && division.isUnlockedByCollectionSize(collectionSize);
        CampaignLeagueRecord record = campaign == null || division == null
                ? new CampaignLeagueRecord("")
                : campaign.getLeagueRecord(division);
        boolean titleWon = hasLeagueTitle(record);

        panel.setBackground(titleWon ? new Color(255, 248, 220) : new Color(255, 250, 235));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(titleWon ? new Color(190, 150, 50) : new Color(180, 160, 120), 2),
                new EmptyBorder(14, 16, 14, 16)
        ));

        String winPct = String.format("%.1f%%", record.getWinPercentage());
        String leagueStatus = titleWon
                ? "League Title Won"
                : (eligible ? "Eligible" : "Locked");
        String eligibleText = eligible
                ? "Collection Gate Met"
                : "Need " + Math.max(0, division.getMinimumCollectionSize() - collectionSize) + " more cards";
        CampaignLeagueDivision.RewardTier bestTier = division.getRewardTierForWins(record.getBestWins());
        String bestReward = bestTier == null
                ? "No final pack earned yet"
                : bestTier.getPackName(division);

        JLabel details = new JLabel(
                "<html><center>"
                        + "<b>" + escapeHtml(division.getDisplayName()) + "</b><br>"
                        + "<b>" + escapeHtml(leagueStatus) + "</b> | "
                        + escapeHtml(eligibleText) + "<br>"
                        + "Seasons Played: " + record.getSeasonsPlayed() + "<br>"
                        + "Best Season: " + record.getBestWins() + "-"
                        + (division.getSeasonLength() - record.getBestWins()) + "<br>"
                        + "Best Reward: " + escapeHtml(bestReward) + "<br>"
                        + "Perfect Seasons: " + record.getPerfectSeasons() + "<br>"
                        + "Total Record: " + escapeHtml(record.getTotalRecordText())
                        + " (" + winPct + ")<br>"
                        + "Last Season: " + escapeHtml(record.getLastResult())
                        + "</center></html>",
                SwingConstants.CENTER
        );
        details.setFont(new Font("Dialog", Font.BOLD, Math.max(13, (int) (15 * (cardSize / 275.0)))));
        details.setForeground(new Color(20, 35, 65));
        panel.add(details, BorderLayout.CENTER);
        return panel;
    }


    private String getTournamentScopeLabel(CampaignTournament tournament) {
        if (tournament == null) {
            return "Scope";
        }
        if (tournament.isTagTournament()) {
            return "Tag";
        }
        if (tournament.isEraTournament()) {
            return "Era";
        }
        return "Scope";
    }

    private void displayTournamentsDashboard() {
        cancelCollectionLoadWorker();
        showingActiveDeckCards = false;
        showingCollectionCards = false;

        if (displayPanel == null) {
            return;
        }

        displayTitleLabel.setText("Tournaments");
        displayPanel.removeAll();
        displayPanel.setLayout(new BorderLayout(14, 14));
        displayPanel.setBackground(new Color(255, 253, 245));

        JPanel wrapper = new JPanel(new BorderLayout(16, 16));
        wrapper.setOpaque(false);
        wrapper.setBorder(new EmptyBorder(20, 24, 24, 24));

        String activeEras = campaign == null ? "" : campaign.getActiveEraNamesText();
        String activeTags = campaign == null ? "" : campaign.getActiveTagNamesText();
        String nextRotation = campaign == null ? "" : campaign.getTournamentRotationCountdownText();
        JLabel intro = new JLabel(
                "<html><center>Three era tournaments and three tag tournaments are active at a time. "
                        + "Tournament decks are auto-built from your best 10 owned cards in that scope."
                        + "<br><br><b>Active eras:</b> " + escapeHtml(activeEras)
                        + "<br><b>Active tags:</b> " + escapeHtml(activeTags)
                        + "<br><b>Next rotation:</b> " + escapeHtml(nextRotation)
                        + "</center></html>",
                SwingConstants.CENTER
        );
        intro.setFont(new Font("Dialog", Font.BOLD, Math.max(15, (int) (17 * (cardSize / 275.0)))));
        intro.setForeground(new Color(35, 35, 35));
        wrapper.add(intro, BorderLayout.NORTH);

        java.util.List<CampaignTournament> tournaments = campaign.getActiveCampaignTournaments();
        JPanel choices = new JPanel(new GridLayout(0, 2, 22, 22));
        choices.setOpaque(false);

        if (tournaments == null || tournaments.isEmpty()) {
            JLabel emptyLabel = new JLabel(
                    "<html><center>No active tournaments are available right now.</center></html>",
                    SwingConstants.CENTER
            );
            emptyLabel.setFont(new Font("Dialog", Font.BOLD, Math.max(15, (int) (17 * (cardSize / 275.0)))));
            choices.add(emptyLabel);
        } else {
            for (CampaignTournament tournament : tournaments) {
                choices.add(buildTournamentChoicePanel(tournament));
            }
        }

        boolean hasCurrentTournamentRun = activeTournament != null && activeTournamentDeck != null
                && !activeTournamentDeck.isEmpty();
        int bottomButtonCount = hasCurrentTournamentRun ? 2 : 1;
        JPanel bottomButtons = new JPanel(new GridLayout(1, bottomButtonCount, 18, 0));
        bottomButtons.setOpaque(false);
        bottomButtons.setBorder(new EmptyBorder(18, 0, 0, 0));

        if (hasCurrentTournamentRun) {
            JButton continueRunButton = makeDashboardButton("Continue Current Run");
            continueRunButton.addActionListener(e -> displayTournamentRunDashboard(
                    activeTournament, Math.max(1, Math.min(3, activeTournamentNextRound))
            ));
            bottomButtons.add(continueRunButton);
        }

        JButton refreshButton = makeDashboardButton("Refresh Rotation");
        refreshButton.addActionListener(e -> displayTournamentsDashboard());
        bottomButtons.add(refreshButton);

        wrapper.add(choices, BorderLayout.CENTER);
        wrapper.add(bottomButtons, BorderLayout.SOUTH);
        displayPanel.add(wrapper, BorderLayout.NORTH);
        updateDisplayPanel();
    }

    private JPanel buildTournamentChoicePanel(CampaignTournament tournament) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        boolean active = campaign.isTournamentActive(tournament);
        panel.setBackground(active ? new Color(255, 250, 235) : new Color(242, 238, 226));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(active ? new Color(180, 160, 120) : new Color(150, 145, 130), 2),
                new EmptyBorder(14, 14, 14, 14)
        ));

        int owned = campaign.getOwnedCountForTournament(tournament);
        int required = tournament.getRequiredCards();
        boolean eligible = campaign.canEnterTournament(tournament);
        boolean ready = campaign.canStartTournament(tournament);
        boolean hasCurrentRun = activeTournament != null
                && activeTournamentDeck != null
                && !activeTournamentDeck.isEmpty();
        boolean currentRunForThisTournament = hasCurrentRun && activeTournament.equals(tournament);
        boolean currentRunForOtherTournament = hasCurrentRun && !activeTournament.equals(tournament);
        CampaignTournamentRecord record = campaign.getTournamentRecord(tournament);
        String activeText = currentRunForThisTournament
                ? "Current run"
                : (active ? "Active now" : "Rotated out");
        String readinessText = eligible
                ? "Eligible: " + owned + "/" + required + " owned"
                : "Locked: " + owned + "/" + required + " owned";
        String deckStrengthText = eligible
                ? "<br>Deck Strength: " + String.format("%.2f", campaign.getTournamentDeckStrength(tournament))
                : "<br>Need " + (required - owned) + " more " + tournament.getScopeName() + " cards";

        JLabel details = new JLabel(
                "<html><center>"
                        + "<b>" + escapeHtml(tournament.getTournamentName()) + "</b><br>"
                        + escapeHtml(getTournamentScopeLabel(tournament)) + ": "
                        + escapeHtml(tournament.getScopeName())
                        + " | Elections " + escapeHtml(tournament.getElectionYearRangeText())
                        + "<br><b>" + escapeHtml(activeText) + "</b><br><br>"
                        + escapeHtml(readinessText)
                        + deckStrengthText
                        + "<br>Best Finish: " + escapeHtml(record.getBestFinishLabel())
                        + " | Titles: " + record.getTitles()
                        + "<br><br>Round 1: Easy / " + CampaignMode.formatPC(tournament.getRoundWinPC(1))
                        + " + Souvenir Pack"
                        + "<br>Round 2: Medium / " + CampaignMode.formatPC(tournament.getRoundWinPC(2))
                        + " + Souvenir Pack"
                        + "<br>Final: Hard / " + CampaignMode.formatPC(tournament.getRoundWinPC(3))
                        + " + Souvenir + Champion Pack"
                        + "<br>First Title: +" + CampaignMode.formatPC(campaign.getFirstTournamentTitleBonusPC())
                        + " + extra Champion Pack"
                        + "<br><br>" + escapeHtml(tournament.getDescription())
                        + "</center></html>",
                SwingConstants.CENTER
        );
        details.setFont(new Font("Dialog", Font.BOLD, Math.max(13, (int) (15 * (cardSize / 275.0)))));
        details.setForeground(active ? new Color(20, 35, 65) : new Color(85, 85, 85));
        panel.add(details, BorderLayout.CENTER);

        String buttonText;
        boolean buttonEnabled;
        if (currentRunForThisTournament) {
            buttonText = "Continue Run";
            buttonEnabled = true;
        } else if (currentRunForOtherTournament) {
            buttonText = "Current Run Active";
            buttonEnabled = false;
        } else {
            buttonText = ready ? "Enter" : (active ? "Locked" : "Rotated Out");
            buttonEnabled = ready;
        }

        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        buttonPanel.setOpaque(false);

        JButton enterButton = makeDashboardButton(buttonText);
        enterButton.setEnabled(buttonEnabled);
        enterButton.addActionListener(e -> playTournamentFromUI(tournament));
        buttonPanel.add(enterButton);

        JButton collectionButton = makeDashboardButton("View Cards");
        collectionButton.addActionListener(e -> displayTournamentCollectionView(tournament));
        buttonPanel.add(collectionButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void displayTournamentCollectionView(CampaignTournament tournament) {
        if (campaign == null || tournament == null) {
            return;
        }

        collectionOwnershipFilter = COLLECTION_OWNERSHIP_OWNED;
        if (tournament.isEraTournament()) {
            collectionScopeMode = COLLECTION_SCOPE_ERA;
            collectionEraFilterId = tournament.getId();
        } else if (tournament.isTagTournament()) {
            collectionScopeMode = COLLECTION_SCOPE_TAG;
            collectionTagFilter = tournament.getScopeName();
        } else {
            collectionScopeMode = COLLECTION_SCOPE_ALL;
        }
        displayCollectionCards();
    }

    private void playTournamentFromUI(CampaignTournament tournament) {
        if (campaign == null || campaign.getPlayer() == null) {
            showShortDialog("No Campaign Loaded", "Start or continue a campaign first.", true);
            return;
        }
        if (tournament == null) {
            showShortDialog("Tournament Error", "That tournament does not exist.", true);
            return;
        }

        if (activeTournament != null
                && activeTournamentDeck != null
                && !activeTournamentDeck.isEmpty()) {
            if (activeTournament.equals(tournament)) {
                displayTournamentRunDashboard(
                        activeTournament,
                        Math.max(1, Math.min(3, activeTournamentNextRound))
                );
            } else {
                showShortDialog(
                        "Current Run Active",
                        "Finish your current " + activeTournament.getTournamentName()
                                + " run before starting another tournament.",
                        true
                );
            }
            return;
        }

        if (!campaign.isTournamentActive(tournament)) {
            showShortDialog(
                    "Tournament Rotated Out",
                    tournament.getTournamentName() + " is not active right now.\n\n"
                            + "Active eras: " + campaign.getActiveEraNamesText()
                            + "\nNext rotation: " + campaign.getTournamentRotationCountdownText(),
                    true
            );
            return;
        }
        if (!campaign.canEnterTournament(tournament)) {
            showShortDialog(
                    "Tournament Locked",
                    campaign.getTournamentEligibilityText(tournament),
                    true
            );
            return;
        }

        if (campaign.isTournamentRotationClose()) {
            showShortDialog(
                    "Rotation Soon",
                    "The tournament rotation changes in " + campaign.getTournamentRotationCountdownText()
                            + ".\n\nStarting now is safe: this tournament run will stay locked in until you finish, lose, or forfeit it.",
                    false
            );
        }

        // Entering the tournament screen is now only a preview. The run is not
        // locked, and the attempt is not recorded, until the player actually
        // clicks Play/Sim for Round 1.
        displayTournamentRunDashboard(tournament, 1);
    }

    private void displayTournamentRunDashboard(CampaignTournament tournament, int nextRound) {
        cancelCollectionLoadWorker();
        showingActiveDeckCards = false;
        showingCollectionCards = false;

        if (displayPanel == null) {
            return;
        }

        displayTitleLabel.setText(tournament.getTournamentName());
        displayPanel.removeAll();
        displayPanel.setLayout(new BorderLayout(14, 14));
        displayPanel.setBackground(new Color(255, 253, 245));

        JPanel wrapper = new JPanel(new BorderLayout(16, 16));
        wrapper.setOpaque(false);
        wrapper.setBorder(new EmptyBorder(28, 28, 28, 28));

        JLabel details = new JLabel(
                "<html><center>"
                        + "<b>" + escapeHtml(tournament.getTournamentName()) + "</b><br>"
                        + escapeHtml(getTournamentScopeLabel(tournament)) + ": "
                        + escapeHtml(tournament.getScopeName())
                        + " | Elections " + escapeHtml(tournament.getElectionYearRangeText())
                        + "<br><br>"
                        + getTournamentDeckStatusText(tournament) + ": "
                        + getTournamentDeckSizeForDisplay(tournament) + "/" + tournament.getRequiredCards() + " cards"
                        + "<br>Deck Strength: " + String.format("%.2f", getTournamentDeckStrengthForDisplay(tournament))
                        + "<br><br>"
                        + "Round 1: Easy / " + CampaignMode.formatPC(tournament.getRoundWinPC(1))
                        + " + Souvenir Pack<br>"
                        + "Round 2: Medium / " + CampaignMode.formatPC(tournament.getRoundWinPC(2))
                        + " + Souvenir Pack<br>"
                        + "Final: Hard / " + CampaignMode.formatPC(tournament.getRoundWinPC(3))
                        + " + Souvenir + Champion Pack<br>"
                        + "First Title: +" + CampaignMode.formatPC(campaign.getFirstTournamentTitleBonusPC())
                        + " + extra Champion Pack<br><br>"
                        + "Next match: Round " + nextRound + " ("
                        + escapeHtml(tournament.getRoundDifficulty(nextRound)) + ")"
                        + "</center></html>",
                SwingConstants.CENTER
        );
        details.setFont(new Font("Dialog", Font.BOLD, Math.max(15, (int) (17 * (cardSize / 275.0)))));
        details.setForeground(new Color(20, 35, 65));
        wrapper.add(details, BorderLayout.NORTH);

        wrapper.add(buildActiveTournamentDeckPanel(tournament), BorderLayout.CENTER);

        JPanel buttons = new JPanel(new GridLayout(1, 2, 18, 0));
        buttons.setOpaque(false);
        JButton playButton = makeDashboardButton(getMatchActionVerb() + " Round " + nextRound);
        playButton.addActionListener(e -> playTournamentRoundFromUI(tournament, nextRound));
        JButton backButton = makeDashboardButton("Back to Tournaments");
        backButton.addActionListener(e -> displayTournamentsDashboard());
        buttons.add(playButton);
        buttons.add(backButton);
        wrapper.add(buttons, BorderLayout.SOUTH);

        displayPanel.add(wrapper, BorderLayout.CENTER);
        updateDisplayPanel();
    }

    private boolean hasStartedTournamentRun(CampaignTournament tournament) {
        return tournament != null
                && activeTournament != null
                && activeTournament.equals(tournament)
                && activeTournamentDeck != null
                && !activeTournamentDeck.isEmpty();
    }

    private String getTournamentDeckStatusText(CampaignTournament tournament) {
        return hasStartedTournamentRun(tournament)
                ? "Tournament deck locked for this run"
                : "Tournament deck preview; locks when Round 1 starts";
    }

    private List<President> getTournamentDeckForDisplay(CampaignTournament tournament) {
        if (hasStartedTournamentRun(tournament)) {
            return new ArrayList<President>(activeTournamentDeck);
        }
        return campaign == null || tournament == null
                ? new ArrayList<President>()
                : campaign.getTournamentActiveDeck(tournament);
    }

    private int getTournamentDeckSizeForDisplay(CampaignTournament tournament) {
        return getTournamentDeckForDisplay(tournament).size();
    }

    private double getTournamentDeckStrengthForDisplay(CampaignTournament tournament) {
        List<President> deck = getTournamentDeckForDisplay(tournament);
        if (deck.isEmpty()) {
            return 0.0;
        }

        List<President> sortedDeck = new ArrayList<President>(deck);
        Collections.sort(sortedDeck, presidentAverageComparator());

        int topCount = Math.min(5, sortedDeck.size());
        double topFiveTotal = 0.0;
        for (int i = 0; i < topCount; i++) {
            topFiveTotal += sortedDeck.get(i).getAv();
        }

        double fullDeckTotal = 0.0;
        for (President president : sortedDeck) {
            if (president != null) {
                fullDeckTotal += president.getAv();
            }
        }

        double topFiveAverage = topCount == 0 ? 0.0 : topFiveTotal / topCount;
        double fullDeckAverage = sortedDeck.isEmpty() ? 0.0 : fullDeckTotal / sortedDeck.size();
        return (0.30 * topFiveAverage) + (0.70 * fullDeckAverage);
    }

    private boolean startTournamentRunIfNeeded(CampaignTournament tournament) {
        if (tournament == null || campaign == null) {
            return false;
        }

        if (hasStartedTournamentRun(tournament)) {
            return true;
        }

        if (activeTournament != null
                && activeTournamentDeck != null
                && !activeTournamentDeck.isEmpty()
                && !activeTournament.equals(tournament)) {
            showShortDialog(
                    "Current Run Active",
                    "Finish your current " + activeTournament.getTournamentName()
                            + " run before starting another tournament.",
                    true
            );
            return false;
        }

        List<President> lockedDeck = campaign.getTournamentActiveDeck(tournament);
        if (lockedDeck.size() < tournament.getRequiredCards()) {
            showShortDialog("Tournament Locked", campaign.getTournamentEligibilityText(tournament), true);
            return false;
        }

        activeTournament = tournament;
        activeTournamentDeck = new ArrayList<President>(lockedDeck);
        activeTournamentNextRound = 1;
        campaign.recordTournamentAttemptStarted(tournament);
        return true;
    }

    private String buildActiveTournamentDeckText() {
        List<President> deck = getTournamentDeckForDisplay(activeTournament);
        if (deck.isEmpty()) {
            return "Tournament deck not loaded.";
        }

        List<President> sortedDeck = new ArrayList<President>(deck);
        Collections.sort(sortedDeck, presidentAverageComparator());

        StringBuilder deckText = new StringBuilder();
        deckText.append("Tournament Deck\n");
        deckText.append("------------------------------");
        for (President president : sortedDeck) {
            deckText.append("\n- ").append(president.toString())
                    .append(" | Avg: ")
                    .append(String.format("%.2f", president.getAv()));
        }
        return deckText.toString();
    }

    private JPanel buildActiveTournamentDeckPanel(CampaignTournament tournament) {
        JPanel outer = new JPanel(new BorderLayout(10, 10));
        outer.setOpaque(false);
        outer.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 160, 120), 1),
                new EmptyBorder(14, 14, 14, 14)
        ));

        JLabel title = new JLabel(
                "<html><center><b>Tournament Deck</b><br>"
                        + escapeHtml(getTournamentDeckStatusText(tournament)) + ".</center></html>",
                SwingConstants.CENTER
        );
        title.setFont(new Font("Dialog", Font.BOLD, Math.max(13, (int) (15 * (cardSize / 275.0)))));
        title.setForeground(new Color(20, 35, 65));
        outer.add(title, BorderLayout.NORTH);

        List<President> displayDeck = getTournamentDeckForDisplay(tournament);
        if (displayDeck.isEmpty()) {
            outer.add(makeCenteredDisplayMessage("Tournament deck not loaded."), BorderLayout.CENTER);
            return outer;
        }

        List<President> sortedDeck = new ArrayList<President>(displayDeck);
        Collections.sort(sortedDeck, presidentAverageComparator());

        int imageWidth = getTournamentDeckCardImageWidth();
        JPanel cardGrid = new JPanel(new GridLayout(0, 5, 18, 18));
        cardGrid.setBackground(new Color(255, 253, 245));
        cardGrid.setBorder(new EmptyBorder(6, 6, 6, 6));

        for (President president : sortedDeck) {
            cardGrid.add(buildPresidentCardPanel(president, imageWidth));
        }

        outer.add(cardGrid, BorderLayout.CENTER);
        return outer;
    }

    private int getTournamentDeckCardImageWidth() {
        int frameWidth = frame == null ? 1600 : frame.getWidth();
        int availableWidth = Math.max(700, frameWidth - 160);

        // Tournament decks show 10 cards in a 5x2 grid. Keep them smaller than
        // the full Active Deck view so the round controls stay visible.
        int widthFromScreen = (availableWidth - 4 * 18 - 36) / 5;
        int widthFromScale = (int) (155 * (cardSize / 275.0));
        return Math.max(120, Math.min(widthFromScreen, Math.max(150, widthFromScale)));
    }


    private void playTournamentRoundFromUI(CampaignTournament tournament, int roundNumber) {
        if (shouldUsePlayableMatches()) {
            playPlayableTournamentRoundFromUI(tournament, roundNumber);
        } else {
            playSimulatedTournamentRoundFromUI(tournament, roundNumber);
        }
    }

    private void playPlayableTournamentRoundFromUI(final CampaignTournament tournament, final int roundNumber) {
        if (campaign == null || campaign.getPlayer() == null) {
            showShortDialog("No Campaign Loaded", "Start or continue a campaign first.", true);
            return;
        }
        if (campaignMatchLauncher == null) {
            showShortDialog(
                    "Playable Match Unavailable",
                    "Playable tournament rounds require Campaign Mode to be launched from the main Campaign Clash shell.\n"
                            + "Use Sim/Test Mode for now, or launch Campaign Mode from the main Campaign Clash home screen.",
                    true
            );
            return;
        }
        if (tournament == null) {
            showShortDialog("Tournament Error", "That tournament does not exist.", true);
            return;
        }
        if (!campaign.canEnterTournament(tournament)) {
            showShortDialog("Tournament Locked", campaign.getTournamentEligibilityText(tournament), true);
            return;
        }
        if (!startTournamentRunIfNeeded(tournament)) {
            return;
        }

        final CampaignMatchConfig config;
        try {
            config = campaign.buildTournamentRoundConfig(tournament, roundNumber, activeTournamentDeck, true);
        } catch (Exception ex) {
            showShortDialog("Tournament Error", "Could not build playable round:\n" + ex.getMessage(), true);
            return;
        }

        String validationError = config.getValidationError();
        if (validationError != null && !validationError.isEmpty()) {
            showShortDialog("Playable Round Error", validationError, true);
            return;
        }

        setActionButtonsEnabled(false);

        OperationResult pendingResult = captureBooleanOutput(() -> campaign.beginPendingPlayableMatch(config));
        appendLog(pendingResult.output);
        if (!pendingResult.success) {
            setActionButtonsEnabled(true);
            displayTournamentRunDashboard(tournament, roundNumber);
            showShortDialog(
                    "Playable Round Error",
                    "Could not save the pending playable round. The round was not launched.",
                    true
            );
            return;
        }

        appendLog("\nLaunching playable " + tournament.getTournamentName()
                + " Round " + roundNumber + "...\n");
        displayPlayableTournamentRoundInProgress(config);
        hideCampaignWindowForPlayableMatch();

        try {
            campaignMatchLauncher.launchCampaignMatch(config, new CampaignMatchCallback() {
                @Override
                public void onCampaignMatchComplete(final CampaignMatchResult result) {
                    SwingUtilities.invokeLater(new Runnable() {
                        @Override
                        public void run() {
                            restoreCampaignWindowAfterPlayableMatch();
                            handlePlayableTournamentRoundResult(tournament, roundNumber, result);
                        }
                    });
                }
            });
        } catch (Exception ex) {
            OperationResult clearResult = captureBooleanOutput(() -> campaign.clearPendingPlayableMatch());
            appendLog(clearResult.output);
            showPlayableLaunchFailure(
                    "Playable Round Error",
                    "Could not launch playable round:\n" + ex.getMessage(),
                    new Runnable() {
                        @Override
                        public void run() {
                            displayTournamentRunDashboard(tournament, roundNumber);
                        }
                    }
            );
        }
    }

    private void handlePlayableTournamentRoundResult(
            CampaignTournament tournament, int roundNumber, CampaignMatchResult result
    ) {
        OperationResult applyResult = captureBooleanOutput(
                () -> campaign.applyTournamentRoundResult(tournament, roundNumber, result)
        );
        appendLog(applyResult.output);
        refreshDashboard();
        displayTournamentRoundResult(tournament, roundNumber, applyResult.output, applyResult.success);
        setActionButtonsEnabled(true);
    }

    private void playSimulatedTournamentRoundFromUI(CampaignTournament tournament, int roundNumber) {
        if (campaign == null || campaign.getPlayer() == null) {
            showShortDialog("No Campaign Loaded", "Start or continue a campaign first.", true);
            return;
        }
        if (tournament == null) {
            showShortDialog("Tournament Error", "That tournament does not exist.", true);
            return;
        }
        if (!campaign.canEnterTournament(tournament)) {
            showShortDialog("Tournament Locked", campaign.getTournamentEligibilityText(tournament), true);
            return;
        }
        if (!startTournamentRunIfNeeded(tournament)) {
            return;
        }

        setActionButtonsEnabled(false);
        appendLog("\nSimulating " + tournament.getTournamentName() + " Round " + roundNumber + "...\n");
        displayTournamentRoundInProgress(tournament, roundNumber);

        SwingWorker<OperationResult, Void> worker = new SwingWorker<OperationResult, Void>() {
            @Override
            protected OperationResult doInBackground() {
                return captureBooleanOutput(() -> campaign.playTournamentRound(
                        tournament, roundNumber, activeTournamentDeck
                ));
            }

            @Override
            protected void done() {
                try {
                    OperationResult result = get();
                    appendLog(result.output);
                    refreshDashboard();
                    displayTournamentRoundResult(tournament, roundNumber, result.output, result.success);
                } catch (Exception ex) {
                    showShortDialog("Tournament Error", "Could not run tournament round:\n" + ex.getMessage(), true);
                } finally {
                    setActionButtonsEnabled(true);
                }
            }
        };
        worker.execute();
    }

    private void displayPlayableTournamentRoundInProgress(CampaignMatchConfig config) {
        cancelCollectionLoadWorker();
        showingActiveDeckCards = false;
        showingCollectionCards = false;

        if (displayPanel == null) {
            return;
        }

        String title = config == null ? "Playable Tournament Round" : config.getMatchTitle();
        displayTitleLabel.setText(title);
        displayPanel.removeAll();
        displayPanel.setLayout(new BorderLayout());
        displayPanel.setBackground(new Color(255, 253, 245));
        displayPanel.add(makeCenteredDisplayMessage(
                "Launching playable tournament round...\nFinish the match on the Campaign Clash board to return here."
        ), BorderLayout.CENTER);
        updateDisplayPanel();
    }

    private void displayTournamentRoundInProgress(CampaignTournament tournament, int roundNumber) {
        cancelCollectionLoadWorker();
        showingActiveDeckCards = false;
        showingCollectionCards = false;

        if (displayPanel == null) {
            return;
        }

        displayTitleLabel.setText(tournament.getTournamentName() + " - Round " + roundNumber);
        displayPanel.removeAll();
        displayPanel.setLayout(new BorderLayout());
        displayPanel.setBackground(new Color(255, 253, 245));
        displayPanel.add(makeCenteredDisplayMessage(
                "Playing Round " + roundNumber + " of " + tournament.getTournamentName() + "..."
        ), BorderLayout.CENTER);
        updateDisplayPanel();
    }

    private void displayTournamentRoundResult(
            CampaignTournament tournament, int roundNumber, String output, boolean roundWon
    ) {
        showingActiveDeckCards = false;
        showingCollectionCards = false;

        if (displayPanel == null) {
            return;
        }

        displayTitleLabel.setText(tournament.getTournamentName() + " - Round " + roundNumber + " Result");
        displayPanel.removeAll();
        displayPanel.setLayout(new BorderLayout(12, 12));
        displayPanel.setBackground(new Color(255, 253, 245));

        JPanel resultPanel = new JPanel(new BorderLayout(12, 12));
        resultPanel.setBackground(new Color(255, 253, 245));
        resultPanel.setBorder(new EmptyBorder(18, 18, 18, 18));

        List<President> pulledCards = extractPresidentsFromPackOutput(output);
        if (!pulledCards.isEmpty()) {
            int imageWidth = getPackResultCardImageWidth();
            int columns = getPackResultColumnCount(imageWidth, pulledCards.size());
            JPanel cardGrid = new JPanel(new GridLayout(0, columns, 20, 20));
            cardGrid.setBackground(new Color(255, 253, 245));
            cardGrid.setBorder(new EmptyBorder(0, 0, 10, 0));

            for (President president : pulledCards) {
                cardGrid.add(buildPresidentCardPanel(president, imageWidth));
            }
            resultPanel.add(cardGrid, BorderLayout.NORTH);
        }

        JTextArea resultArea = new JTextArea(output == null || output.trim().isEmpty()
                ? "Tournament round completed."
                : output.trim());
        resultArea.setEditable(false);
        resultArea.setLineWrap(true);
        resultArea.setWrapStyleWord(true);
        resultArea.setFont(new Font("Monospaced", Font.PLAIN,
                Math.max(13, (int) (15 * (cardSize / 275.0)))));
        resultArea.setBackground(new Color(255, 253, 245));
        resultArea.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 160, 120), 1),
                new EmptyBorder(14, 14, 14, 14)
        ));
        resultPanel.add(resultArea, BorderLayout.CENTER);

        if (roundWon && roundNumber < 3) {
            activeTournament = tournament;
            activeTournamentNextRound = roundNumber + 1;
        } else {
            activeTournament = null;
            activeTournamentDeck = null;
            activeTournamentNextRound = 1;
        }

        JPanel followUpButtons = new JPanel(new GridLayout(1, 2, 18, 0));
        followUpButtons.setOpaque(false);
        followUpButtons.setBorder(new EmptyBorder(4, 0, 0, 0));

        if (roundWon && roundNumber < 3) {
            JButton nextRoundButton = makeDashboardButton("Play Round " + (roundNumber + 1));
            nextRoundButton.addActionListener(e -> playTournamentRoundFromUI(tournament, roundNumber + 1));
            JButton backButton = makeDashboardButton("Back to Tournaments");
            backButton.addActionListener(e -> displayTournamentsDashboard());
            followUpButtons.add(nextRoundButton);
            followUpButtons.add(backButton);
        } else {
            JButton backButton = makeDashboardButton("Back to Tournaments");
            backButton.addActionListener(e -> displayTournamentsDashboard());
            JButton viewCollectionButton = makeDashboardButton("View Collection");
            viewCollectionButton.addActionListener(e -> displayCollectionCards());
            followUpButtons.add(backButton);
            followUpButtons.add(viewCollectionButton);
        }

        resultPanel.add(followUpButtons, BorderLayout.SOUTH);
        displayPanel.add(resultPanel, BorderLayout.CENTER);
        updateDisplayPanel();
    }


    private void displayTrophyCabinet() {
        cancelCollectionLoadWorker();
        showingActiveDeckCards = false;
        showingCollectionCards = false;

        if (displayPanel == null) {
            return;
        }

        displayTitleLabel.setText("Trophy Cabinet");
        displayPanel.removeAll();
        displayPanel.setLayout(new BorderLayout());
        displayPanel.setBackground(new Color(255, 253, 245));

        JPanel wrapper = new JPanel(new BorderLayout(16, 16));
        wrapper.setOpaque(false);
        wrapper.add(buildRosterTabPanel("Trophy Cabinet"), BorderLayout.NORTH);

        JPanel body = new JPanel(new BorderLayout(16, 16));
        body.setOpaque(false);
        body.setBorder(new EmptyBorder(18, 24, 24, 24));

        int completedEras = campaign.getCompletedTournamentEraCount();
        int completedTags = campaign.getCompletedTournamentTagCount();
        int totalEras = campaign.getEraTournaments().size();
        int totalTags = campaign.getTagTournaments().size();
        int leagueTitles = getLeagueTitleCount();
        int perfectLeagueSeasons = getPerfectLeagueSeasonCount();

        JLabel summary = new JLabel(
                "<html><center><b>Trophy Cabinet</b><br>"
                        + "Completed Eras: " + completedEras + "/" + totalEras
                        + " | Completed Tags: " + completedTags + "/" + totalTags
                        + " | Tournament Titles: " + campaign.getTotalTournamentTitles()
                        + " | League Titles: " + leagueTitles
                        + " | Perfect League Seasons: " + perfectLeagueSeasons
                        + "<br>First championship in each tournament awards +"
                        + CampaignMode.formatPC(campaign.getFirstTournamentTitleBonusPC())
                        + " and an extra Champion Pack."
                        + "</center></html>",
                SwingConstants.CENTER
        );
        summary.setFont(new Font("Dialog", Font.BOLD, Math.max(16, (int) (18 * (cardSize / 275.0)))));
        summary.setForeground(new Color(20, 35, 65));

        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setOpaque(false);
        topPanel.add(summary, BorderLayout.NORTH);
        topPanel.add(buildTrophyFilterPanel(), BorderLayout.SOUTH);
        body.add(topPanel, BorderLayout.NORTH);

        JPanel recordsPanel = new JPanel(new BorderLayout(16, 16));
        recordsPanel.setOpaque(false);

        boolean showTournaments = shouldShowTournamentTrophies();
        boolean showLeagues = shouldShowLeagueTrophies();

        if (showTournaments) {
            List<CampaignTournament> visibleTournaments = getVisibleTrophyTournaments();
            recordsPanel.add(buildTournamentTrophySection(visibleTournaments), BorderLayout.NORTH);
        }
        if (showLeagues) {
            recordsPanel.add(buildLeagueTrophySection(), showTournaments ? BorderLayout.CENTER : BorderLayout.NORTH);
        }
        if (!showTournaments && !showLeagues) {
            recordsPanel.add(makeCenteredDisplayMessage("No trophy section matches those filters."), BorderLayout.CENTER);
        }

        body.add(recordsPanel, BorderLayout.CENTER);

        JPanel buttons = new JPanel(new GridLayout(1, 2, 18, 0));
        buttons.setOpaque(false);
        buttons.setBorder(new EmptyBorder(16, 0, 0, 0));
        JButton collectionButton = makeDashboardButton("Collection");
        collectionButton.addActionListener(e -> displayCollectionCards());
        JButton storeButton = makeDashboardButton("Store");
        storeButton.addActionListener(e -> displayStoreDashboard());
        buttons.add(collectionButton);
        buttons.add(storeButton);
        body.add(buttons, BorderLayout.SOUTH);

        wrapper.add(body, BorderLayout.CENTER);
        displayPanel.add(wrapper, BorderLayout.NORTH);
        updateDisplayPanel();
    }

    private JPanel buildTournamentTrophySection(List<CampaignTournament> visibleTournaments) {
        JPanel section = new JPanel(new BorderLayout(10, 10));
        section.setOpaque(false);

        JLabel title = new JLabel("Tournament Trophies", SwingConstants.CENTER);
        title.setFont(new Font("Dialog", Font.BOLD, Math.max(15, (int) (17 * (cardSize / 275.0)))));
        title.setForeground(new Color(20, 35, 65));
        section.add(title, BorderLayout.NORTH);

        if (visibleTournaments == null || visibleTournaments.isEmpty()) {
            JLabel emptyLabel = new JLabel(
                    "<html><center><b>No tournaments match those filters.</b><br>"
                            + "Try changing the type or status filter."
                            + "</center></html>",
                    SwingConstants.CENTER
            );
            emptyLabel.setFont(new Font("Dialog", Font.BOLD, Math.max(15, (int) (17 * (cardSize / 275.0)))));
            emptyLabel.setForeground(new Color(20, 35, 65));
            JPanel emptyPanel = new JPanel(new BorderLayout());
            emptyPanel.setOpaque(false);
            emptyPanel.setBorder(new EmptyBorder(24, 10, 24, 10));
            emptyPanel.add(emptyLabel, BorderLayout.CENTER);
            section.add(emptyPanel, BorderLayout.CENTER);
            return section;
        }

        JPanel grid = new JPanel(new GridLayout(0, 2, 20, 20));
        grid.setOpaque(false);
        for (CampaignTournament tournament : visibleTournaments) {
            grid.add(buildTrophyRecordPanel(tournament));
        }
        section.add(grid, BorderLayout.CENTER);
        return section;
    }

    private JPanel buildLeagueTrophySection() {
        JPanel section = new JPanel(new BorderLayout(10, 10));
        section.setOpaque(false);
        section.setBorder(new EmptyBorder(18, 0, 0, 0));

        JLabel title = new JLabel("League Trophies", SwingConstants.CENTER);
        title.setFont(new Font("Dialog", Font.BOLD, Math.max(15, (int) (17 * (cardSize / 275.0)))));
        title.setForeground(new Color(20, 35, 65));
        section.add(title, BorderLayout.NORTH);

        List<CampaignLeagueDivision> divisions = getVisibleLeagueTrophyDivisions();
        if (divisions.isEmpty()) {
            section.add(makeCenteredDisplayMessage("No League records match those filters."), BorderLayout.CENTER);
            return section;
        }

        JPanel grid = new JPanel(new GridLayout(0, 1, 0, 14));
        grid.setOpaque(false);
        for (CampaignLeagueDivision division : divisions) {
            grid.add(buildLeagueRecordPanel(division));
        }
        section.add(grid, BorderLayout.CENTER);
        return section;
    }

    private JPanel buildTrophyFilterPanel() {
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 4));
        filterPanel.setOpaque(false);

        JLabel showLabel = new JLabel("Show:");
        showLabel.setFont(new Font("Dialog", Font.BOLD, Math.max(12, (int) (14 * (cardSize / 275.0)))));
        showLabel.setForeground(new Color(20, 35, 65));
        filterPanel.add(showLabel);

        JComboBox<String> typeBox = new JComboBox<String>(new String[] {
                TROPHY_TYPE_ALL,
                TROPHY_TYPE_TOURNAMENTS,
                TROPHY_TYPE_ERAS,
                TROPHY_TYPE_TAGS,
                TROPHY_TYPE_LEAGUES
        });
        typeBox.setSelectedItem(trophyTypeFilter);
        typeBox.addActionListener(e -> {
            Object selected = typeBox.getSelectedItem();
            trophyTypeFilter = selected == null ? TROPHY_TYPE_ALL : selected.toString();
            displayTrophyCabinet();
        });
        filterPanel.add(typeBox);

        JLabel statusLabel = new JLabel("Status:");
        statusLabel.setFont(new Font("Dialog", Font.BOLD, Math.max(12, (int) (14 * (cardSize / 275.0)))));
        statusLabel.setForeground(new Color(20, 35, 65));
        filterPanel.add(statusLabel);

        JComboBox<String> statusBox = new JComboBox<String>(new String[] {
                TROPHY_STATUS_ALL,
                TROPHY_STATUS_WON,
                TROPHY_STATUS_ENTERED,
                TROPHY_STATUS_ELIGIBLE,
                TROPHY_STATUS_LOCKED
        });
        statusBox.setSelectedItem(trophyStatusFilter);
        statusBox.addActionListener(e -> {
            Object selected = statusBox.getSelectedItem();
            trophyStatusFilter = selected == null ? TROPHY_STATUS_ALL : selected.toString();
            displayTrophyCabinet();
        });
        filterPanel.add(statusBox);

        JLabel sortLabel = new JLabel("Sort:");
        sortLabel.setFont(new Font("Dialog", Font.BOLD, Math.max(12, (int) (14 * (cardSize / 275.0)))));
        sortLabel.setForeground(new Color(20, 35, 65));
        filterPanel.add(sortLabel);

        JComboBox<String> sortBox = new JComboBox<String>(new String[] {
                TROPHY_SORT_DEFAULT,
                TROPHY_SORT_TITLES,
                TROPHY_SORT_BEST_FINISH,
                TROPHY_SORT_ELIGIBLE,
                TROPHY_SORT_ATTEMPTS,
                TROPHY_SORT_ALPHABETICAL
        });
        sortBox.setSelectedItem(trophySortMode);
        sortBox.addActionListener(e -> {
            Object selected = sortBox.getSelectedItem();
            trophySortMode = selected == null ? TROPHY_SORT_DEFAULT : selected.toString();
            displayTrophyCabinet();
        });
        filterPanel.add(sortBox);

        JButton resetButton = makeDashboardButton("Reset");
        resetButton.addActionListener(e -> {
            trophyTypeFilter = TROPHY_TYPE_ALL;
            trophyStatusFilter = TROPHY_STATUS_ALL;
            trophySortMode = TROPHY_SORT_DEFAULT;
            displayTrophyCabinet();
        });
        filterPanel.add(resetButton);

        return filterPanel;
    }

    private boolean shouldShowTournamentTrophies() {
        return !TROPHY_TYPE_LEAGUES.equals(trophyTypeFilter);
    }

    private boolean shouldShowLeagueTrophies() {
        return TROPHY_TYPE_ALL.equals(trophyTypeFilter) || TROPHY_TYPE_LEAGUES.equals(trophyTypeFilter);
    }

    private List<CampaignLeagueDivision> getVisibleLeagueTrophyDivisions() {
        ArrayList<CampaignLeagueDivision> divisions = new ArrayList<CampaignLeagueDivision>();
        if (campaign == null) {
            return divisions;
        }

        for (CampaignLeagueDivision division : campaign.getLeagueDivisions()) {
            if (matchesLeagueTrophyStatusFilter(division)) {
                divisions.add(division);
            }
        }
        sortLeagueTrophyDivisions(divisions);
        return divisions;
    }

    private boolean matchesLeagueTrophyStatusFilter(CampaignLeagueDivision division) {
        if (division == null || TROPHY_STATUS_ALL.equals(trophyStatusFilter)) {
            return true;
        }

        CampaignLeagueRecord record = campaign.getLeagueRecord(division);
        boolean titleWon = hasLeagueTitle(record);
        boolean entered = record.getSeasonsPlayed() > 0;
        CampaignPlayer player = campaign.getPlayer();
        int collectionSize = player == null ? 0 : player.getCollectionSize();
        boolean eligible = division.isUnlockedByCollectionSize(collectionSize);

        if (TROPHY_STATUS_WON.equals(trophyStatusFilter)) {
            return titleWon;
        }
        if (TROPHY_STATUS_ENTERED.equals(trophyStatusFilter)) {
            return entered;
        }
        if (TROPHY_STATUS_ELIGIBLE.equals(trophyStatusFilter)) {
            return eligible;
        }
        if (TROPHY_STATUS_LOCKED.equals(trophyStatusFilter)) {
            return !eligible;
        }
        return true;
    }

    private void sortLeagueTrophyDivisions(List<CampaignLeagueDivision> divisions) {
        if (divisions == null || divisions.size() <= 1) {
            return;
        }

        if (TROPHY_SORT_TITLES.equals(trophySortMode)) {
            Collections.sort(divisions, new Comparator<CampaignLeagueDivision>() {
                @Override
                public int compare(CampaignLeagueDivision a, CampaignLeagueDivision b) {
                    int cmp = boolScore(hasLeagueTitle(campaign.getLeagueRecord(b)))
                            - boolScore(hasLeagueTitle(campaign.getLeagueRecord(a)));
                    if (cmp != 0) {
                        return cmp;
                    }
                    return a.getDivisionNumber() - b.getDivisionNumber();
                }
            });
        } else if (TROPHY_SORT_BEST_FINISH.equals(trophySortMode)) {
            Collections.sort(divisions, new Comparator<CampaignLeagueDivision>() {
                @Override
                public int compare(CampaignLeagueDivision a, CampaignLeagueDivision b) {
                    int cmp = campaign.getLeagueRecord(b).getBestWins()
                            - campaign.getLeagueRecord(a).getBestWins();
                    if (cmp != 0) {
                        return cmp;
                    }
                    return a.getDivisionNumber() - b.getDivisionNumber();
                }
            });
        } else if (TROPHY_SORT_ATTEMPTS.equals(trophySortMode)) {
            Collections.sort(divisions, new Comparator<CampaignLeagueDivision>() {
                @Override
                public int compare(CampaignLeagueDivision a, CampaignLeagueDivision b) {
                    int cmp = campaign.getLeagueRecord(b).getSeasonsPlayed()
                            - campaign.getLeagueRecord(a).getSeasonsPlayed();
                    if (cmp != 0) {
                        return cmp;
                    }
                    return a.getDivisionNumber() - b.getDivisionNumber();
                }
            });
        } else if (TROPHY_SORT_ELIGIBLE.equals(trophySortMode)) {
            Collections.sort(divisions, new Comparator<CampaignLeagueDivision>() {
                @Override
                public int compare(CampaignLeagueDivision a, CampaignLeagueDivision b) {
                    int cmp = getLeagueTrophyEligibilityRank(a) - getLeagueTrophyEligibilityRank(b);
                    if (cmp != 0) {
                        return cmp;
                    }
                    cmp = getLeagueCardsNeeded(a) - getLeagueCardsNeeded(b);
                    if (cmp != 0) {
                        return cmp;
                    }
                    return a.getDivisionNumber() - b.getDivisionNumber();
                }
            });
        } else if (TROPHY_SORT_ALPHABETICAL.equals(trophySortMode)) {
            Collections.sort(divisions, new Comparator<CampaignLeagueDivision>() {
                @Override
                public int compare(CampaignLeagueDivision a, CampaignLeagueDivision b) {
                    return a.getDisplayName().compareToIgnoreCase(b.getDisplayName());
                }
            });
        } else {
            Collections.sort(divisions, new Comparator<CampaignLeagueDivision>() {
                @Override
                public int compare(CampaignLeagueDivision a, CampaignLeagueDivision b) {
                    return a.getDivisionNumber() - b.getDivisionNumber();
                }
            });
        }
    }

    private int boolScore(boolean value) {
        return value ? 1 : 0;
    }

    private int getLeagueTrophyEligibilityRank(CampaignLeagueDivision division) {
        CampaignLeagueRecord record = campaign.getLeagueRecord(division);
        if (hasLeagueTitle(record)) {
            return 0;
        }
        CampaignPlayer player = campaign.getPlayer();
        int collectionSize = player == null ? 0 : player.getCollectionSize();
        if (division.isUnlockedByCollectionSize(collectionSize)) {
            return 1;
        }
        return 2;
    }

    private int getLeagueCardsNeeded(CampaignLeagueDivision division) {
        if (division == null || campaign == null || campaign.getPlayer() == null) {
            return Integer.MAX_VALUE;
        }
        return Math.max(0, division.getMinimumCollectionSize() - campaign.getPlayer().getCollectionSize());
    }

    private int getLeagueTitleCount() {
        if (campaign == null) {
            return 0;
        }
        int count = 0;
        for (CampaignLeagueDivision division : campaign.getLeagueDivisions()) {
            if (hasLeagueTitle(campaign.getLeagueRecord(division))) {
                count++;
            }
        }
        return count;
    }

    private int getPerfectLeagueSeasonCount() {
        if (campaign == null) {
            return 0;
        }
        int count = 0;
        for (CampaignLeagueDivision division : campaign.getLeagueDivisions()) {
            count += campaign.getLeagueRecord(division).getPerfectSeasons();
        }
        return count;
    }

    private boolean hasLeagueTitle(CampaignLeagueRecord record) {
        return record != null
                && record.getBestWins() >= CampaignLeagueDivision.THIRD_REWARD_WIN_THRESHOLD;
    }

    private List<CampaignTournament> getVisibleTrophyTournaments() {
        ArrayList<CampaignTournament> tournaments = new ArrayList<CampaignTournament>();

        if (TROPHY_TYPE_LEAGUES.equals(trophyTypeFilter)) {
            return tournaments;
        } else if (TROPHY_TYPE_ERAS.equals(trophyTypeFilter)) {
            tournaments.addAll(campaign.getEraTournaments());
        } else if (TROPHY_TYPE_TAGS.equals(trophyTypeFilter)) {
            tournaments.addAll(campaign.getTagTournaments());
        } else {
            tournaments.addAll(campaign.getEraTournaments());
            tournaments.addAll(campaign.getTagTournaments());
        }

        ArrayList<CampaignTournament> filtered = new ArrayList<CampaignTournament>();
        for (CampaignTournament tournament : tournaments) {
            if (matchesTrophyStatusFilter(tournament)) {
                filtered.add(tournament);
            }
        }

        sortTrophyTournaments(filtered);
        return filtered;
    }

    private boolean matchesTrophyStatusFilter(CampaignTournament tournament) {
        if (tournament == null || TROPHY_STATUS_ALL.equals(trophyStatusFilter)) {
            return true;
        }

        CampaignTournamentRecord record = campaign.getTournamentRecord(tournament);
        int owned = campaign.getOwnedCountForTournament(tournament);
        int required = tournament.getRequiredCards();

        if (TROPHY_STATUS_WON.equals(trophyStatusFilter)) {
            return record.hasWonTitle();
        }
        if (TROPHY_STATUS_ENTERED.equals(trophyStatusFilter)) {
            return record.getAttempts() > 0;
        }
        if (TROPHY_STATUS_ELIGIBLE.equals(trophyStatusFilter)) {
            return owned >= required;
        }
        if (TROPHY_STATUS_LOCKED.equals(trophyStatusFilter)) {
            return owned < required;
        }
        return true;
    }

    private void sortTrophyTournaments(List<CampaignTournament> tournaments) {
        if (tournaments == null || tournaments.size() <= 1) {
            return;
        }

        if (TROPHY_SORT_TITLES.equals(trophySortMode)) {
            Collections.sort(tournaments, new Comparator<CampaignTournament>() {
                @Override
                public int compare(CampaignTournament a, CampaignTournament b) {
                    int cmp = campaign.getTournamentRecord(b).getTitles()
                            - campaign.getTournamentRecord(a).getTitles();
                    if (cmp != 0) {
                        return cmp;
                    }
                    return compareTrophyDefault(a, b);
                }
            });
        } else if (TROPHY_SORT_BEST_FINISH.equals(trophySortMode)) {
            Collections.sort(tournaments, new Comparator<CampaignTournament>() {
                @Override
                public int compare(CampaignTournament a, CampaignTournament b) {
                    int cmp = campaign.getTournamentRecord(b).getBestFinish()
                            - campaign.getTournamentRecord(a).getBestFinish();
                    if (cmp != 0) {
                        return cmp;
                    }
                    return compareTrophyDefault(a, b);
                }
            });
        } else if (TROPHY_SORT_ELIGIBLE.equals(trophySortMode)) {
            Collections.sort(tournaments, new Comparator<CampaignTournament>() {
                @Override
                public int compare(CampaignTournament a, CampaignTournament b) {
                    int cmp = getTrophyEligibilityRank(a) - getTrophyEligibilityRank(b);
                    if (cmp != 0) {
                        return cmp;
                    }
                    cmp = getTrophyCardsNeeded(a) - getTrophyCardsNeeded(b);
                    if (cmp != 0) {
                        return cmp;
                    }
                    return compareTrophyDefault(a, b);
                }
            });
        } else if (TROPHY_SORT_ATTEMPTS.equals(trophySortMode)) {
            Collections.sort(tournaments, new Comparator<CampaignTournament>() {
                @Override
                public int compare(CampaignTournament a, CampaignTournament b) {
                    int cmp = campaign.getTournamentRecord(b).getAttempts()
                            - campaign.getTournamentRecord(a).getAttempts();
                    if (cmp != 0) {
                        return cmp;
                    }
                    return compareTrophyDefault(a, b);
                }
            });
        } else if (TROPHY_SORT_ALPHABETICAL.equals(trophySortMode)) {
            Collections.sort(tournaments, new Comparator<CampaignTournament>() {
                @Override
                public int compare(CampaignTournament a, CampaignTournament b) {
                    String aName = a == null ? "" : a.getTournamentName();
                    String bName = b == null ? "" : b.getTournamentName();
                    return aName.compareToIgnoreCase(bName);
                }
            });
        } else {
            Collections.sort(tournaments, new Comparator<CampaignTournament>() {
                @Override
                public int compare(CampaignTournament a, CampaignTournament b) {
                    return compareTrophyDefault(a, b);
                }
            });
        }
    }

    private int compareTrophyDefault(CampaignTournament a, CampaignTournament b) {
        boolean aEra = a != null && a.isEraTournament();
        boolean bEra = b != null && b.isEraTournament();
        if (aEra && !bEra) {
            return -1;
        }
        if (!aEra && bEra) {
            return 1;
        }

        if (aEra && bEra) {
            return getEraChronologyIndexByName(a.getScopeName()) - getEraChronologyIndexByName(b.getScopeName());
        }

        String aName = a == null ? "" : a.getTournamentName();
        String bName = b == null ? "" : b.getTournamentName();
        return aName.compareToIgnoreCase(bName);
    }

    private int getEraChronologyIndexByName(String eraName) {
        if (eraName == null || campaign == null) {
            return Integer.MAX_VALUE - 1;
        }
        List<CampaignTournament> tournaments = campaign.getEraTournaments();
        for (int i = 0; i < tournaments.size(); i++) {
            CampaignTournament tournament = tournaments.get(i);
            if (tournament != null && safeEqualsIgnoreCase(eraName, tournament.getScopeName())) {
                return i;
            }
        }
        return Integer.MAX_VALUE - 1;
    }

    private int getTrophyEligibilityRank(CampaignTournament tournament) {
        CampaignTournamentRecord record = campaign.getTournamentRecord(tournament);
        if (record.hasWonTitle()) {
            return 0;
        }
        if (campaign.getOwnedCountForTournament(tournament) >= tournament.getRequiredCards()) {
            return 1;
        }
        return 2;
    }

    private int getTrophyCardsNeeded(CampaignTournament tournament) {
        if (tournament == null) {
            return Integer.MAX_VALUE;
        }
        int owned = campaign.getOwnedCountForTournament(tournament);
        int required = tournament.getRequiredCards();
        return Math.max(0, required - owned);
    }

    private JPanel buildTrophyRecordPanel(CampaignTournament tournament) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        boolean active = campaign.isTournamentActive(tournament);
        CampaignTournamentRecord record = campaign.getTournamentRecord(tournament);
        boolean champion = record.hasWonTitle();
        panel.setBackground(champion ? new Color(255, 248, 220) : new Color(255, 250, 235));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(champion ? new Color(190, 150, 50) : new Color(180, 160, 120), 2),
                new EmptyBorder(14, 14, 14, 14)
        ));

        int owned = campaign.getOwnedCountForTournament(tournament);
        int required = tournament.getRequiredCards();
        String entryStatus = owned >= required
                ? "Entry: Eligible (" + owned + "/" + required + ")"
                : "Entry: Need " + (required - owned) + " more (" + owned + "/" + required + ")";
        String activeStatus = active ? "Active Now" : "Rotated Out";
        JLabel details = new JLabel(
                "<html><center>"
                        + "<b>" + escapeHtml(tournament.getTournamentName()) + "</b><br>"
                        + escapeHtml(getTournamentScopeLabel(tournament)) + ": "
                        + escapeHtml(tournament.getScopeName()) + "<br>"
                        + escapeHtml(activeStatus) + "<br>"
                        + escapeHtml(entryStatus)
                        + "<br><br>Best Finish: <b>" + escapeHtml(record.getBestFinishLabel()) + "</b><br>"
                        + "Titles: " + record.getTitles() + "<br>"
                        + "Attempts: " + record.getAttempts() + "<br>"
                        + "Last Result: " + escapeHtml(record.getLastResult())
                        + "</center></html>",
                SwingConstants.CENTER
        );
        details.setFont(new Font("Dialog", Font.BOLD, Math.max(13, (int) (15 * (cardSize / 275.0)))));
        details.setForeground(new Color(20, 35, 65));
        panel.add(details, BorderLayout.CENTER);
        return panel;
    }

    private void displayStoreDashboard() {
        cancelCollectionLoadWorker();
        showingActiveDeckCards = false;
        showingCollectionCards = false;

        if (displayPanel == null) {
            return;
        }

        displayTitleLabel.setText("Store");
        displayPanel.removeAll();
        displayPanel.setLayout(new BorderLayout(14, 14));
        displayPanel.setBackground(new Color(255, 253, 245));

        JPanel wrapper = new JPanel(new BorderLayout(16, 16));
        wrapper.setOpaque(false);
        wrapper.setBorder(new EmptyBorder(30, 30, 30, 30));

        JLabel intro = new JLabel(
                "<html><center>Open packs to expand your collection and improve your active deck. "
                        + "Duplicate cards are automatically sold for Political Capital.</center></html>",
                SwingConstants.CENTER
        );
        intro.setFont(new Font("Dialog", Font.BOLD, Math.max(16, (int) (18 * (cardSize / 275.0)))));
        intro.setForeground(new Color(35, 35, 35));
        wrapper.add(intro, BorderLayout.NORTH);

        JPanel choices = new JPanel(new GridLayout(0, 3, 24, 24));
        choices.setOpaque(false);
        choices.add(buildPackChoicePanel("Campaign Trail Pack", campaign.getCampaignTrailPack(),
                "1 Candidate", "0.0-10.0 rating",
                "Cheap chaos reroll. Any card can show up."));
        choices.add(buildPackChoicePanel("Filler Pack", campaign.getFillerPack(),
                "5 Candidates", "0.0-3.0 rating",
                "Cheap depth and early collection filler."));
        choices.add(buildPackChoicePanel("Star Pack", campaign.getStarPack(),
                "1 Candidate", "3.5-10.0 rating",
                "Single-card gamble with a stronger floor."));
        choices.add(buildPackChoicePanel("Standard Pack", campaign.getStandardPack(),
                "5 Candidates", "2.0-10.0 rating",
                "Balanced collection growth and active-deck upgrades."));
        choices.add(buildPackChoicePanel("Elite Pack", campaign.getElitePack(),
                "3 Candidates", "4.0-10.0 rating",
                "Premium high-end hunting for your active deck."));
        choices.add(buildEraProspectStorePanel());
        choices.add(buildTagProspectStorePanel());
        choices.add(buildStateCampaignTrailStorePanel());
        choices.add(buildIdeologyStarStorePanel());

        wrapper.add(choices, BorderLayout.CENTER);
        displayPanel.add(wrapper, BorderLayout.NORTH);
        updateDisplayPanel();
    }


    private JPanel buildEraProspectStorePanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(new Color(255, 250, 235));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 160, 120), 2),
                new EmptyBorder(16, 16, 16, 16)
        ));

        int cost = campaign.getEraProspectPackCost();
        String affordText = "";
        if (campaign != null && campaign.getPlayer() != null) {
            if (campaign.getPlayer().getPC() >= cost) {
                affordText = "<br><br>You can afford this pack.";
            } else {
                int needed = cost - campaign.getPlayer().getPC();
                affordText = "<br><br>Need " + CampaignMode.formatPC(needed) + " more.";
            }
        }

        JLabel details = new JLabel(
                "<html><center>"
                        + "<b>Era Prospect Pack</b><br><br>"
                        + "5 Candidates<br>"
                        + "Active era only, 0.0-4.0 rating<br>"
                        + "Cost: " + CampaignMode.formatPC(cost) + "<br><br>"
                        + "Targeted progress for currently active tournaments."
                        + affordText
                        + "</center></html>",
                SwingConstants.CENTER
        );
        details.setFont(new Font("Dialog", Font.BOLD, Math.max(14, (int) (17 * (cardSize / 275.0)))));
        details.setForeground(new Color(20, 35, 65));
        panel.add(details, BorderLayout.CENTER);

        JButton chooseButton = makeDashboardButton("Choose Era");
        chooseButton.addActionListener(e -> displayEraProspectDashboard());
        panel.add(chooseButton, BorderLayout.SOUTH);

        return panel;
    }

    private void displayEraProspectDashboard() {
        cancelCollectionLoadWorker();
        showingActiveDeckCards = false;
        showingCollectionCards = false;

        if (displayPanel == null) {
            return;
        }

        displayTitleLabel.setText("Era Prospect Packs");
        displayPanel.removeAll();
        displayPanel.setLayout(new BorderLayout(14, 14));
        displayPanel.setBackground(new Color(255, 253, 245));

        JPanel wrapper = new JPanel(new BorderLayout(16, 16));
        wrapper.setOpaque(false);
        wrapper.setBorder(new EmptyBorder(24, 24, 24, 24));

        JLabel intro = new JLabel(
                "<html><center>Choose an active era. Era Prospect Packs give 5 cards from that era, "
                        + "rated 0.0-4.0, and rotate with tournaments every 3 real-world hours."
                        + "<br>Active now: " + escapeHtml(campaign.getActiveEraNamesText())
                        + " | Next rotation: " + escapeHtml(campaign.getTournamentRotationCountdownText())
                        + "</center></html>",
                SwingConstants.CENTER
        );
        intro.setFont(new Font("Dialog", Font.BOLD, Math.max(15, (int) (17 * (cardSize / 275.0)))));
        intro.setForeground(new Color(35, 35, 35));
        wrapper.add(intro, BorderLayout.NORTH);

        JPanel choices = new JPanel(new GridLayout(0, 2, 22, 22));
        choices.setOpaque(false);
        for (CampaignTournament tournament : campaign.getActiveEraTournaments()) {
            choices.add(buildEraProspectChoicePanel(tournament));
        }

        wrapper.add(choices, BorderLayout.CENTER);
        displayPanel.add(wrapper, BorderLayout.NORTH);
        updateDisplayPanel();
    }

    private JPanel buildEraProspectChoicePanel(CampaignTournament tournament) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(new Color(255, 250, 235));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 160, 120), 2),
                new EmptyBorder(14, 14, 14, 14)
        ));

        int cost = campaign.getEraProspectPackCost();
        int owned = campaign.getOwnedCountForTournament(tournament);
        int required = tournament.getRequiredCards();
        String affordText = campaign.getPlayer().getPC() >= cost
                ? "You can afford this pack."
                : "Need " + CampaignMode.formatPC(cost - campaign.getPlayer().getPC()) + " more.";

        JLabel details = new JLabel(
                "<html><center>"
                        + "<b>" + escapeHtml(campaign.getEraProspectPackName(tournament)) + "</b><br>"
                        + escapeHtml(tournament.getTournamentName()) + "<br><br>"
                        + "Owned: " + owned + "/" + required + " for tournament entry<br>"
                        + "5 cards, 0.0-4.0 rating<br>"
                        + "Cost: " + CampaignMode.formatPC(cost) + "<br><br>"
                        + escapeHtml(affordText)
                        + "</center></html>",
                SwingConstants.CENTER
        );
        details.setFont(new Font("Dialog", Font.BOLD, Math.max(13, (int) (15 * (cardSize / 275.0)))));
        details.setForeground(new Color(20, 35, 65));
        panel.add(details, BorderLayout.CENTER);

        JButton openButton = makeDashboardButton("Open");
        openButton.addActionListener(e -> openEraProspectPackFromUI(tournament));
        panel.add(openButton, BorderLayout.SOUTH);

        return panel;
    }

    private void openEraProspectPackFromUI(CampaignTournament tournament) {
        if (campaign == null || campaign.getPlayer() == null) {
            showShortDialog("No Campaign Loaded", "Start or continue a campaign first.", true);
            return;
        }
        if (tournament == null) {
            showShortDialog("Pack Error", "That era does not exist.", true);
            return;
        }

        int cost = campaign.getEraProspectPackCost();
        if (campaign.getPlayer().getPC() < cost) {
            int needed = cost - campaign.getPlayer().getPC();
            showShortDialog(
                    "Not Enough Political Capital",
                    "You need " + CampaignMode.formatPC(needed) + " more to open "
                            + campaign.getEraProspectPackName(tournament) + ".\nCost: "
                            + CampaignMode.formatPC(cost) + "\nCurrent balance: "
                            + CampaignMode.formatPC(campaign.getPlayer().getPC()),
                    true
            );
            return;
        }

        OperationResult result = captureBooleanOutput(() -> campaign.buyEraProspectPack(tournament));
        appendLog(result.output);
        refreshDashboard();

        if (!result.success) {
            showShortDialog(
                    "Pack Error",
                    result.output == null || result.output.trim().isEmpty()
                            ? "Could not open " + campaign.getEraProspectPackName(tournament) + "."
                            : result.output.trim(),
                    true
            );
            return;
        }

        displayPackResult(campaign.getEraProspectPackName(tournament), result.output);
    }


    private JPanel buildTagProspectStorePanel() {
        JPanel panel = buildTargetedStorePanelBase();

        int cost = campaign.getTagProspectPackCost();
        String affordText = buildAffordText(cost);
        JLabel details = new JLabel(
                "<html><center>"
                        + "<b>Tag Prospect Pack</b><br><br>"
                        + "5 Candidates<br>"
                        + "Active tag only, 0.0-4.0 rating<br>"
                        + "Cost: " + CampaignMode.formatPC(cost) + "<br><br>"
                        + "Targeted progress for currently active tag tournaments."
                        + affordText
                        + "</center></html>",
                SwingConstants.CENTER
        );
        details.setFont(new Font("Dialog", Font.BOLD, Math.max(14, (int) (17 * (cardSize / 275.0)))));
        details.setForeground(new Color(20, 35, 65));
        panel.add(details, BorderLayout.CENTER);

        JButton chooseButton = makeDashboardButton("Choose Tag");
        chooseButton.addActionListener(e -> displayTagProspectDashboard());
        panel.add(chooseButton, BorderLayout.SOUTH);
        return panel;
    }

    private void displayTagProspectDashboard() {
        cancelCollectionLoadWorker();
        showingActiveDeckCards = false;
        showingCollectionCards = false;

        if (displayPanel == null) {
            return;
        }

        displayTitleLabel.setText("Tag Prospect Packs");
        displayPanel.removeAll();
        displayPanel.setLayout(new BorderLayout(14, 14));
        displayPanel.setBackground(new Color(255, 253, 245));

        JPanel wrapper = new JPanel(new BorderLayout(16, 16));
        wrapper.setOpaque(false);
        wrapper.setBorder(new EmptyBorder(24, 24, 24, 24));

        JLabel intro = new JLabel(
                "<html><center>Choose an active tag. Tag Prospect Packs give 5 cards from that tag, "
                        + "rated 0.0-4.0, and rotate with tag tournaments every 3 real-world hours."
                        + "<br>Active now: " + escapeHtml(campaign.getActiveTagNamesText())
                        + " | Next rotation: " + escapeHtml(campaign.getTournamentRotationCountdownText())
                        + "</center></html>",
                SwingConstants.CENTER
        );
        intro.setFont(new Font("Dialog", Font.BOLD, Math.max(15, (int) (17 * (cardSize / 275.0)))));
        intro.setForeground(new Color(35, 35, 35));
        wrapper.add(intro, BorderLayout.NORTH);

        JPanel choices = new JPanel(new GridLayout(0, 2, 22, 22));
        choices.setOpaque(false);
        for (CampaignTournament tournament : campaign.getActiveTagTournaments()) {
            choices.add(buildTagProspectChoicePanel(tournament));
        }

        wrapper.add(choices, BorderLayout.CENTER);
        displayPanel.add(wrapper, BorderLayout.NORTH);
        updateDisplayPanel();
    }

    private JPanel buildTagProspectChoicePanel(CampaignTournament tournament) {
        JPanel panel = buildTargetedStorePanelBase();

        int cost = campaign.getTagProspectPackCost();
        int owned = campaign.getOwnedCountForTournament(tournament);
        int required = tournament.getRequiredCards();
        String affordText = campaign.getPlayer().getPC() >= cost
                ? "You can afford this pack."
                : "Need " + CampaignMode.formatPC(cost - campaign.getPlayer().getPC()) + " more.";

        JLabel details = new JLabel(
                "<html><center>"
                        + "<b>" + escapeHtml(campaign.getTagProspectPackName(tournament)) + "</b><br>"
                        + escapeHtml(tournament.getTournamentName()) + "<br><br>"
                        + "Owned: " + owned + "/" + required + " for tournament entry<br>"
                        + "5 cards, 0.0-4.0 rating<br>"
                        + "Cost: " + CampaignMode.formatPC(cost) + "<br><br>"
                        + escapeHtml(affordText)
                        + "</center></html>",
                SwingConstants.CENTER
        );
        details.setFont(new Font("Dialog", Font.BOLD, Math.max(13, (int) (15 * (cardSize / 275.0)))));
        details.setForeground(new Color(20, 35, 65));
        panel.add(details, BorderLayout.CENTER);

        JButton openButton = makeDashboardButton("Open");
        openButton.addActionListener(e -> openTagProspectPackFromUI(tournament));
        panel.add(openButton, BorderLayout.SOUTH);
        return panel;
    }

    private void openTagProspectPackFromUI(CampaignTournament tournament) {
        if (campaign == null || campaign.getPlayer() == null) {
            showShortDialog("No Campaign Loaded", "Start or continue a campaign first.", true);
            return;
        }
        if (tournament == null || !tournament.isTagTournament()) {
            showShortDialog("Pack Error", "That tag does not exist.", true);
            return;
        }

        int cost = campaign.getTagProspectPackCost();
        if (campaign.getPlayer().getPC() < cost) {
            int needed = cost - campaign.getPlayer().getPC();
            showShortDialog(
                    "Not Enough Political Capital",
                    "You need " + CampaignMode.formatPC(needed) + " more to open "
                            + campaign.getTagProspectPackName(tournament) + ".\nCost: "
                            + CampaignMode.formatPC(cost) + "\nCurrent balance: "
                            + CampaignMode.formatPC(campaign.getPlayer().getPC()),
                    true
            );
            return;
        }

        OperationResult result = captureBooleanOutput(() -> campaign.buyTagProspectPack(tournament));
        appendLog(result.output);
        refreshDashboard();

        if (!result.success) {
            showShortDialog(
                    "Pack Error",
                    result.output == null || result.output.trim().isEmpty()
                            ? "Could not open " + campaign.getTagProspectPackName(tournament) + "."
                            : result.output.trim(),
                    true
            );
            return;
        }

        displayPackResult(campaign.getTagProspectPackName(tournament), result.output);
    }

    private JPanel buildStateCampaignTrailStorePanel() {
        JPanel panel = buildTargetedStorePanelBase();
        int cost = campaign.getStateCampaignTrailPackCost();
        JLabel details = new JLabel(
                "<html><center>"
                        + "<b>State Campaign Trail Packs</b><br><br>"
                        + "1 Candidate<br>"
                        + "Chosen state, 0.0-10.0 rating<br>"
                        + "Cost: " + CampaignMode.formatPC(cost) + "<br><br>"
                        + "A slightly pricier targeted Campaign Trail Pack."
                        + buildAffordText(cost)
                        + "</center></html>",
                SwingConstants.CENTER
        );
        details.setFont(new Font("Dialog", Font.BOLD, Math.max(14, (int) (17 * (cardSize / 275.0)))));
        details.setForeground(new Color(20, 35, 65));
        panel.add(details, BorderLayout.CENTER);

        JButton chooseButton = makeDashboardButton("Choose State");
        chooseButton.addActionListener(e -> displayStateCampaignTrailDashboard());
        panel.add(chooseButton, BorderLayout.SOUTH);
        return panel;
    }

    private void displayStateCampaignTrailDashboard() {
        displayTargetedSinglePickerDashboard(
                "State Campaign Trail Packs",
                "Choose a state. State Campaign Trail Packs give 1 card from that state, rated 0.0-10.0.",
                campaign.getAvailableStatePackStates(),
                campaign.getStateCampaignTrailPackCost(),
                "State",
                selectedState -> openStateCampaignTrailPackFromUI(selectedState)
        );
    }

    private void openStateCampaignTrailPackFromUI(String state) {
        openNamedTargetedPackFromUI(
                campaign.getStateCampaignTrailPackName(state),
                campaign.getStateCampaignTrailPack(state),
                () -> campaign.buyStateCampaignTrailPack(state)
        );
    }

    private JPanel buildIdeologyStarStorePanel() {
        JPanel panel = buildTargetedStorePanelBase();
        int cost = campaign.getIdeologyStarPackCost();
        JLabel details = new JLabel(
                "<html><center>"
                        + "<b>Ideology Star Packs</b><br><br>"
                        + "1 Candidate<br>"
                        + "Chosen ideology, 3.5-10.0 rating<br>"
                        + "Cost: " + CampaignMode.formatPC(cost) + "<br><br>"
                        + "A slightly pricier targeted Star Pack."
                        + buildAffordText(cost)
                        + "</center></html>",
                SwingConstants.CENTER
        );
        details.setFont(new Font("Dialog", Font.BOLD, Math.max(14, (int) (17 * (cardSize / 275.0)))));
        details.setForeground(new Color(20, 35, 65));
        panel.add(details, BorderLayout.CENTER);

        JButton chooseButton = makeDashboardButton("Choose Ideology");
        chooseButton.addActionListener(e -> displayIdeologyStarDashboard());
        panel.add(chooseButton, BorderLayout.SOUTH);
        return panel;
    }

    private void displayIdeologyStarDashboard() {
        displayTargetedSinglePickerDashboard(
                "Ideology Star Packs",
                "Choose an ideology. Ideology Star Packs give 1 card from that ideology, rated 3.5-10.0.",
                campaign.getAvailableIdeologyPackIdeologies(),
                campaign.getIdeologyStarPackCost(),
                "Ideology",
                selectedIdeology -> openIdeologyStarPackFromUI(selectedIdeology)
        );
    }

    private void openIdeologyStarPackFromUI(String ideology) {
        openNamedTargetedPackFromUI(
                campaign.getIdeologyStarPackName(ideology),
                campaign.getIdeologyStarPack(ideology),
                () -> campaign.buyIdeologyStarPack(ideology)
        );
    }

    private void displayTargetedSinglePickerDashboard(
            String title, String introText, List<String> options, int cost, String labelText,
            TargetedPackOpener opener
    ) {
        cancelCollectionLoadWorker();
        showingActiveDeckCards = false;
        showingCollectionCards = false;

        if (displayPanel == null) {
            return;
        }

        displayTitleLabel.setText(title);
        displayPanel.removeAll();
        displayPanel.setLayout(new BorderLayout(14, 14));
        displayPanel.setBackground(new Color(255, 253, 245));

        JPanel wrapper = new JPanel(new BorderLayout(16, 16));
        wrapper.setOpaque(false);
        wrapper.setBorder(new EmptyBorder(28, 28, 28, 28));

        JLabel intro = new JLabel("<html><center>" + escapeHtml(introText)
                + "<br>Cost: " + CampaignMode.formatPC(cost) + buildAffordText(cost)
                + "</center></html>", SwingConstants.CENTER);
        intro.setFont(new Font("Dialog", Font.BOLD, Math.max(15, (int) (17 * (cardSize / 275.0)))));
        intro.setForeground(new Color(35, 35, 35));
        wrapper.add(intro, BorderLayout.NORTH);

        if (options == null || options.isEmpty()) {
            JLabel empty = new JLabel("No eligible options found.", SwingConstants.CENTER);
            empty.setFont(new Font("Dialog", Font.BOLD, Math.max(15, (int) (17 * (cardSize / 275.0)))));
            empty.setForeground(new Color(90, 70, 55));
            wrapper.add(empty, BorderLayout.CENTER);
        } else {
            JPanel picker = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 12));
            picker.setOpaque(false);
            JLabel label = new JLabel(labelText + ":");
            label.setFont(new Font("Dialog", Font.BOLD, Math.max(14, (int) (16 * (cardSize / 275.0)))));
            label.setForeground(new Color(35, 35, 35));
            picker.add(label);

            JComboBox<String> comboBox = new JComboBox<String>(options.toArray(new String[0]));
            comboBox.setFont(new Font("Dialog", Font.BOLD, Math.max(14, (int) (16 * (cardSize / 275.0)))));
            comboBox.setPreferredSize(new Dimension(Math.max(240, (int) (260 * (cardSize / 275.0))), 36));
            picker.add(comboBox);

            JButton openButton = makeDashboardButton("Open Pack");
            openButton.addActionListener(e -> {
                Object selected = comboBox.getSelectedItem();
                if (selected != null) {
                    opener.open(selected.toString());
                }
            });
            picker.add(openButton);
            wrapper.add(picker, BorderLayout.CENTER);
        }

        JPanel bottomButtons = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 8));
        bottomButtons.setOpaque(false);
        JButton backButton = makeDashboardButton("Back to Store");
        backButton.addActionListener(e -> displayStoreDashboard());
        bottomButtons.add(backButton);
        wrapper.add(bottomButtons, BorderLayout.SOUTH);

        displayPanel.add(wrapper, BorderLayout.NORTH);
        updateDisplayPanel();
    }

    private void openNamedTargetedPackFromUI(String packName, CampaignPack pack, PackPurchase purchase) {
        if (campaign == null || campaign.getPlayer() == null) {
            showShortDialog("No Campaign Loaded", "Start or continue a campaign first.", true);
            return;
        }
        if (pack == null) {
            showShortDialog("Pack Error", "That pack does not exist.", true);
            return;
        }
        if (campaign.getPlayer().getPC() < pack.getCost()) {
            int needed = pack.getCost() - campaign.getPlayer().getPC();
            showShortDialog(
                    "Not Enough Political Capital",
                    "You need " + CampaignMode.formatPC(needed) + " more to open "
                            + packName + ".\nCost: " + CampaignMode.formatPC(pack.getCost())
                            + "\nCurrent balance: " + CampaignMode.formatPC(campaign.getPlayer().getPC()),
                    true
            );
            return;
        }

        OperationResult result = captureBooleanOutput(() -> purchase.buy());
        appendLog(result.output);
        refreshDashboard();

        if (!result.success) {
            showShortDialog(
                    "Pack Error",
                    result.output == null || result.output.trim().isEmpty()
                            ? "Could not open " + packName + "."
                            : result.output.trim(),
                    true
            );
            return;
        }

        displayPackResult(packName, result.output);
    }

    private JPanel buildTargetedStorePanelBase() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(new Color(255, 250, 235));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 160, 120), 2),
                new EmptyBorder(16, 16, 16, 16)
        ));
        return panel;
    }

    private String buildAffordText(int cost) {
        if (campaign != null && campaign.getPlayer() != null) {
            if (campaign.getPlayer().getPC() >= cost) {
                return "<br><br>You can afford this pack.";
            }
            int needed = cost - campaign.getPlayer().getPC();
            return "<br><br>Need " + CampaignMode.formatPC(needed) + " more.";
        }
        return "";
    }

    private interface TargetedPackOpener {
        void open(String selectedValue);
    }

    private interface PackPurchase {
        boolean buy();
    }

    private JPanel buildPackChoicePanel(
            String packName, CampaignPack pack, String cardCountText,
            String ratingText, String flavorText
    ) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(new Color(255, 250, 235));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 160, 120), 2),
                new EmptyBorder(16, 16, 16, 16)
        ));

        String affordText = "";
        if (campaign != null && campaign.getPlayer() != null && pack != null) {
            if (campaign.getPlayer().getPC() >= pack.getCost()) {
                affordText = "<br><br>You can afford this pack.";
            } else {
                int needed = pack.getCost() - campaign.getPlayer().getPC();
                affordText = "<br><br>Need " + CampaignMode.formatPC(needed) + " more.";
            }
        }

        JLabel details = new JLabel(
                "<html><center>"
                        + "<b>" + escapeHtml(packName) + "</b><br><br>"
                        + escapeHtml(cardCountText) + "<br>"
                        + escapeHtml(ratingText) + "<br>"
                        + "Cost: " + CampaignMode.formatPC(pack.getCost()) + "<br><br>"
                        + escapeHtml(flavorText)
                        + affordText
                        + "</center></html>",
                SwingConstants.CENTER
        );
        details.setFont(new Font("Dialog", Font.BOLD, Math.max(14, (int) (17 * (cardSize / 275.0)))));
        details.setForeground(new Color(20, 35, 65));
        panel.add(details, BorderLayout.CENTER);

        JButton openButton = makeDashboardButton("Open");
        openButton.addActionListener(e -> openPackFromUI(packName, pack));
        panel.add(openButton, BorderLayout.SOUTH);

        return panel;
    }

    private void openPackFromUI(String packName, CampaignPack pack) {
        if (campaign == null || campaign.getPlayer() == null) {
            showShortDialog("No Campaign Loaded", "Start or continue a campaign first.", true);
            return;
        }
        if (pack == null) {
            showShortDialog("Pack Error", "That pack does not exist.", true);
            return;
        }
        if (campaign.getPlayer().getPC() < pack.getCost()) {
            int needed = pack.getCost() - campaign.getPlayer().getPC();
            showShortDialog(
                    "Not Enough Political Capital",
                    "You need " + CampaignMode.formatPC(needed) + " more to open "
                            + packName + ".\nCost: " + CampaignMode.formatPC(pack.getCost())
                            + "\nCurrent balance: " + CampaignMode.formatPC(campaign.getPlayer().getPC()),
                    true
            );
            return;
        }

        OperationResult result = captureBooleanOutput(() -> campaign.buyPack(packName, pack));
        appendLog(result.output);
        refreshDashboard();

        if (!result.success) {
            showShortDialog(
                    "Pack Error",
                    result.output == null || result.output.trim().isEmpty()
                            ? "Could not open " + packName + "."
                            : result.output.trim(),
                    true
            );
            return;
        }

        displayPackResult(packName, result.output);
    }

    private void displayPackResult(String packName, String output) {
        cancelCollectionLoadWorker();
        showingActiveDeckCards = false;
        showingCollectionCards = false;

        if (displayPanel == null) {
            return;
        }

        displayTitleLabel.setText(packName + " Result");
        displayPanel.removeAll();
        displayPanel.setLayout(new BorderLayout(12, 12));
        displayPanel.setBackground(new Color(255, 253, 245));

        JPanel resultPanel = new JPanel(new BorderLayout(12, 12));
        resultPanel.setBackground(new Color(255, 253, 245));
        resultPanel.setBorder(new EmptyBorder(18, 18, 18, 18));

        JPanel topPanel = new JPanel(new BorderLayout(12, 12));
        topPanel.setOpaque(false);

        JLabel headline = new JLabel(
                "<html><center><b>Opened " + escapeHtml(packName) + "</b><br>"
                        + escapeHtml(buildPackResultSummary(output))
                        + "</center></html>",
                SwingConstants.CENTER
        );
        headline.setFont(new Font("Dialog", Font.BOLD, Math.max(16, (int) (19 * (cardSize / 275.0)))));
        headline.setForeground(new Color(20, 35, 65));
        topPanel.add(headline, BorderLayout.NORTH);

        List<President> pulledCards = extractPresidentsFromPackOutput(output);
        if (!pulledCards.isEmpty()) {
            int imageWidth = getPackResultCardImageWidth();
            int columns = getPackResultColumnCount(imageWidth, pulledCards.size());
            JPanel cardGrid = new JPanel(new GridLayout(0, columns, 20, 20));
            cardGrid.setBackground(new Color(255, 253, 245));
            cardGrid.setBorder(new EmptyBorder(8, 0, 10, 0));

            for (President president : pulledCards) {
                cardGrid.add(buildPresidentCardPanel(president, imageWidth));
            }
            topPanel.add(cardGrid, BorderLayout.CENTER);
        }

        resultPanel.add(topPanel, BorderLayout.NORTH);

        JTextArea summaryArea = new JTextArea(output == null || output.trim().isEmpty()
                ? "Pack opened."
                : output.trim());
        summaryArea.setEditable(false);
        summaryArea.setLineWrap(true);
        summaryArea.setWrapStyleWord(true);
        summaryArea.setFont(new Font("Monospaced", Font.PLAIN,
                Math.max(13, (int) (15 * (cardSize / 275.0)))));
        summaryArea.setBackground(new Color(255, 253, 245));
        summaryArea.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 160, 120), 1),
                new EmptyBorder(10, 10, 10, 10)
        ));
        resultPanel.add(summaryArea, BorderLayout.CENTER);

        JPanel followUpButtons = new JPanel(new GridLayout(1, 2, 18, 0));
        followUpButtons.setOpaque(false);
        followUpButtons.setBorder(new EmptyBorder(4, 0, 0, 0));

        JButton backToStoreButton = makeDashboardButton("Back to Store");
        backToStoreButton.addActionListener(e -> displayStoreDashboard());
        JButton viewDeckButton = makeDashboardButton("Active Deck");
        viewDeckButton.addActionListener(e -> displayActiveDeckCards());

        followUpButtons.add(backToStoreButton);
        followUpButtons.add(viewDeckButton);
        resultPanel.add(followUpButtons, BorderLayout.SOUTH);

        displayPanel.add(resultPanel, BorderLayout.CENTER);
        updateDisplayPanel();
    }

    private String buildPackResultSummary(String output) {
        if (output == null || output.trim().isEmpty()) {
            return "Pack opened.";
        }

        String[] lines = output.split("\\r?\\n");
        for (String line : lines) {
            String trimmed = line.trim();
            if (trimmed.startsWith("Summary:")) {
                return trimmed;
            }
        }
        return "Pack opened.";
    }

    private List<President> extractPresidentsFromPackOutput(String output) {
        java.util.List<President> cards = new java.util.ArrayList<President>();
        if (output == null || output.trim().isEmpty() || campaign == null) {
            return cards;
        }

        String[] lines = output.split("\\r?\\n");
        for (String line : lines) {
            String cardName = extractCardNameFromPackLine(line);
            if (cardName == null || cardName.trim().isEmpty()) {
                continue;
            }

            President president = findPresidentByName(cardName.trim());
            if (president != null) {
                cards.add(president);
            }
        }
        return cards;
    }

    private String extractCardNameFromPackLine(String line) {
        if (line == null) {
            return null;
        }

        Matcher newCardMatcher = NEW_CARD_PATTERN.matcher(line);
        if (newCardMatcher.matches()) {
            return newCardMatcher.group(1);
        }

        Matcher duplicateMatcher = DUPLICATE_CARD_PATTERN.matcher(line);
        if (duplicateMatcher.matches()) {
            return duplicateMatcher.group(1);
        }

        return null;
    }

    private President findPresidentByName(String name) {
        if (name == null || campaign == null || campaign.getAllPresidents() == null) {
            return null;
        }

        for (President president : campaign.getAllPresidents()) {
            if (president != null && name.equalsIgnoreCase(president.getName())) {
                return president;
            }
        }
        return null;
    }

    private void showCollection() {
        CampaignPlayer player = campaign.getPlayer();
        String text = player == null ? "No campaign loaded." : player.getCollectionSummary();
        showLongTextDialog("Collection", text);
    }

    private void showActiveDeck() {
        CampaignPlayer player = campaign.getPlayer();
        String text = player == null ? "No campaign loaded." : player.getActiveDeckSummary();
        showLongTextDialog("Active Deck", text);
    }

    private void displayActiveDeckCards() {
        cancelCollectionLoadWorker();
        showingActiveDeckCards = true;
        showingCollectionCards = false;
        activeDeckEditMode = false;
        refreshActiveDeckCards();
    }

    private void displayActiveDeckEditorCards() {
        cancelCollectionLoadWorker();
        showingActiveDeckCards = true;
        showingCollectionCards = false;
        activeDeckEditMode = true;
        resetActiveDeckDraftFromCampaign();
        refreshActiveDeckCards();
    }

    private void displayCollectionCards() {
        showingActiveDeckCards = false;
        showingCollectionCards = true;
        refreshCollectionCards();
    }

    private void refreshCollectionCards() {
        if (displayPanel == null) {
            return;
        }

        cancelCollectionLoadWorker();

        displayPanel.removeAll();
        displayPanel.setLayout(new BorderLayout());
        displayPanel.setBackground(new Color(255, 253, 245));

        if (campaign == null || campaign.getPlayer() == null) {
            displayTitleLabel.setText("Collection");
            displayPanel.add(makeCenteredDisplayMessage("No campaign loaded."), BorderLayout.CENTER);
            updateDisplayPanel();
            return;
        }

        CampaignPlayer player = campaign.getPlayer();
        if (COLLECTION_SCOPE_PROGRESS.equals(collectionScopeMode)) {
            displayCollectionProgressDashboard(player);
            return;
        }

        CollectionScope scope = getSelectedCollectionScope();
        List<CollectionCardItem> collectionCards = buildFilteredCollectionCards(player, scope);

        int totalInScope = getCollectionScopeTotal(scope);
        int ownedInScope = getCollectionScopeOwnedCount(player, scope);
        int missingInScope = Math.max(0, totalInScope - ownedInScope);
        String titleText = buildCollectionTitleText(scope, ownedInScope, totalInScope);
        displayTitleLabel.setText(titleText);

        int imageWidth = getCollectionCardImageWidth();
        int imageHeight = getCardImageHeight(imageWidth);
        int columns = getCollectionColumnCount(imageWidth);
        long requestId = ++collectionLoadRequestId;

        final int totalCardsToLoad = collectionCards.size();
        final JLabel loadingLabel = makeCenteredDisplayMessage(
                buildLoadingImagesMessage("Loading collection images", 0, totalCardsToLoad)
        );
        displayPanel.add(loadingLabel, BorderLayout.CENTER);
        updateDisplayPanel();

        if (collectionCards.isEmpty()) {
            displayLoadedCollectionCards(
                    new ArrayList<CardIconData>(), columns, ownedInScope, totalInScope, missingInScope, scope
            );
            return;
        }

        collectionLoadWorker = new SwingWorker<List<CardIconData>, Integer>() {
            @Override
            protected List<CardIconData> doInBackground() {
                List<CardIconData> loadedCards = new ArrayList<CardIconData>();
                int loadedCount = 0;
                for (CollectionCardItem cardItem : collectionCards) {
                    if (isCancelled()) {
                        return loadedCards;
                    }

                    President president = cardItem.getPresident();
                    ImageIcon icon = cardItem.isOwned()
                            ? loadScaledIcon(president.getImageURL(), imageWidth, imageHeight)
                            : loadMissingScaledIcon(president.getImageURL(), imageWidth, imageHeight);
                    loadedCards.add(new CardIconData(president, imageWidth, icon, cardItem.isOwned()));
                    loadedCount++;
                    publish(loadedCount);
                }
                return loadedCards;
            }

            @Override
            protected void process(List<Integer> chunks) {
                if (chunks == null || chunks.isEmpty() || isCancelled()
                        || requestId != collectionLoadRequestId || !showingCollectionCards) {
                    return;
                }
                int loadedCount = chunks.get(chunks.size() - 1);
                setCenteredDisplayMessage(
                        loadingLabel,
                        buildLoadingImagesMessage("Loading collection images", loadedCount, totalCardsToLoad)
                );
            }

            @Override
            protected void done() {
                if (isCancelled() || requestId != collectionLoadRequestId || !showingCollectionCards) {
                    return;
                }

                try {
                    displayLoadedCollectionCards(get(), columns, ownedInScope, totalInScope, missingInScope, scope);
                } catch (Exception e) {
                    displayPanel.removeAll();
                    displayPanel.setLayout(new BorderLayout());
                    displayPanel.add(makeCenteredDisplayMessage(
                            "Could not load collection images. Try reopening the collection."
                    ), BorderLayout.CENTER);
                    updateDisplayPanel();
                }
            }
        };
        collectionLoadWorker.execute();
    }

    private void displayCollectionProgressDashboard(CampaignPlayer player) {
        if (displayPanel == null) {
            return;
        }

        displayTitleLabel.setText("Collection Progress Dashboard");
        displayPanel.removeAll();
        displayPanel.setLayout(new BorderLayout());
        displayPanel.setBackground(new Color(255, 253, 245));

        JPanel wrapper = new JPanel(new BorderLayout(10, 10));
        wrapper.setOpaque(false);
        wrapper.add(buildRosterTabPanel("Collection"), BorderLayout.NORTH);

        JPanel body = new JPanel(new BorderLayout(10, 10));
        body.setOpaque(false);
        body.setBorder(new EmptyBorder(4, 12, 12, 12));
        body.add(buildCollectionProgressTopPanel(), BorderLayout.NORTH);

        List<CollectionProgressRow> rows = buildCollectionProgressRows(player);
        sortCollectionProgressRows(rows);

        if (rows.isEmpty()) {
            body.add(makeCenteredDisplayMessage("No era or tag progress rows available yet."), BorderLayout.CENTER);
        } else {
            body.add(buildCollectionProgressRowsPanel(rows), BorderLayout.CENTER);
        }

        wrapper.add(body, BorderLayout.CENTER);
        displayPanel.add(wrapper, BorderLayout.NORTH);
        updateDisplayPanel();
    }

    private JPanel buildCollectionProgressTopPanel() {
        JPanel topPanel = new JPanel(new BorderLayout(8, 8));
        topPanel.setOpaque(false);

        JLabel guidance = new JLabel(
                "<html><center><b>Collection Progress</b><br>"
                        + "Track era and tag tournament readiness. Tags shown here have "
                        + TAG_TOURNAMENT_REQUIRED_CARDS + "+ total cards, matching the future tag-tournament rule."
                        + "</center></html>",
                SwingConstants.CENTER
        );
        guidance.setFont(new Font("Dialog", Font.PLAIN, Math.max(14, (int) (16 * (cardSize / 275.0)))));
        guidance.setForeground(new Color(70, 70, 70));
        topPanel.add(guidance, BorderLayout.NORTH);

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.CENTER, 14, 4));
        controls.setOpaque(false);

        JLabel viewLabel = new JLabel("View:");
        viewLabel.setFont(new Font("Dialog", Font.BOLD, Math.max(13, (int) (15 * (cardSize / 275.0)))));
        controls.add(viewLabel);

        JComboBox<String> scopeCombo = new JComboBox<String>(new String[] {
                COLLECTION_SCOPE_PROGRESS,
                COLLECTION_SCOPE_ALL,
                COLLECTION_SCOPE_ERA,
                COLLECTION_SCOPE_TAG,
                COLLECTION_SCOPE_FILTERS
        });
        scopeCombo.setSelectedItem(collectionScopeMode);
        scopeCombo.setFont(new Font("Dialog", Font.PLAIN, Math.max(13, (int) (15 * (cardSize / 275.0)))));
        scopeCombo.addActionListener(e -> {
            collectionScopeMode = (String) scopeCombo.getSelectedItem();
            displayCollectionCards();
        });
        controls.add(scopeCombo);

        JLabel showLabel = new JLabel("Show:");
        showLabel.setFont(new Font("Dialog", Font.BOLD, Math.max(13, (int) (15 * (cardSize / 275.0)))));
        controls.add(showLabel);

        JComboBox<String> typeCombo = new JComboBox<String>(new String[] {
                PROGRESS_TYPE_BOTH,
                PROGRESS_TYPE_ERAS,
                PROGRESS_TYPE_TAGS
        });
        typeCombo.setSelectedItem(collectionProgressTypeFilter);
        typeCombo.setFont(new Font("Dialog", Font.PLAIN, Math.max(13, (int) (15 * (cardSize / 275.0)))));
        typeCombo.addActionListener(e -> {
            collectionProgressTypeFilter = (String) typeCombo.getSelectedItem();
            displayCollectionCards();
        });
        controls.add(typeCombo);

        JLabel sortLabel = new JLabel("Sort:");
        sortLabel.setFont(new Font("Dialog", Font.BOLD, Math.max(13, (int) (15 * (cardSize / 275.0)))));
        controls.add(sortLabel);

        JComboBox<String> sortCombo = new JComboBox<String>(new String[] {
                PROGRESS_SORT_ELIGIBLE_CLOSE,
                PROGRESS_SORT_CLOSEST_LOCKED,
                PROGRESS_SORT_PERCENT_OWNED,
                PROGRESS_SORT_OWNED_COUNT,
                PROGRESS_SORT_DECK_STRENGTH,
                PROGRESS_SORT_ALPHABETICAL,
                PROGRESS_SORT_CHRONOLOGICAL
        });
        sortCombo.setSelectedItem(collectionProgressSortMode);
        sortCombo.setFont(new Font("Dialog", Font.PLAIN, Math.max(13, (int) (15 * (cardSize / 275.0)))));
        sortCombo.addActionListener(e -> {
            collectionProgressSortMode = (String) sortCombo.getSelectedItem();
            displayCollectionCards();
        });
        controls.add(sortCombo);

        topPanel.add(controls, BorderLayout.SOUTH);
        return topPanel;
    }

    private List<CollectionProgressRow> buildCollectionProgressRows(CampaignPlayer player) {
        ArrayList<CollectionProgressRow> rows = new ArrayList<CollectionProgressRow>();
        if (campaign == null || player == null) {
            return rows;
        }

        if (shouldShowEraProgressRows()) {
            for (CampaignTournament tournament : campaign.getEraTournaments()) {
                if (tournament == null) {
                    continue;
                }
                List<President> candidates = campaign.getPresidentsForTournament(tournament);
                int owned = getOwnedCountForCandidates(player, candidates);
                int required = tournament.getRequiredCards();
                double strength = owned >= required
                        ? calculateScopedDeckStrength(player, candidates, required)
                        : 0.0;
                rows.add(new CollectionProgressRow(
                        COLLECTION_SCOPE_ERA,
                        tournament.getScopeName(),
                        tournament.getId(),
                        owned,
                        candidates.size(),
                        required,
                        strength
                ));
            }
        }

        if (shouldShowTagProgressRows()) {
            for (String tag : getProgressDashboardTags()) {
                List<President> candidates = getCandidatesForTag(tag);
                int owned = getOwnedCountForCandidates(player, candidates);
                double strength = owned >= TAG_TOURNAMENT_REQUIRED_CARDS
                        ? calculateScopedDeckStrength(player, candidates, TAG_TOURNAMENT_REQUIRED_CARDS)
                        : 0.0;
                rows.add(new CollectionProgressRow(
                        COLLECTION_SCOPE_TAG,
                        tag,
                        tag,
                        owned,
                        candidates.size(),
                        TAG_TOURNAMENT_REQUIRED_CARDS,
                        strength
                ));
            }
        }

        return rows;
    }

    private boolean shouldShowEraProgressRows() {
        return PROGRESS_TYPE_BOTH.equals(collectionProgressTypeFilter)
                || PROGRESS_TYPE_ERAS.equals(collectionProgressTypeFilter);
    }

    private boolean shouldShowTagProgressRows() {
        return PROGRESS_TYPE_BOTH.equals(collectionProgressTypeFilter)
                || PROGRESS_TYPE_TAGS.equals(collectionProgressTypeFilter);
    }

    private JPanel buildCollectionProgressRowsPanel(List<CollectionProgressRow> rows) {
        JPanel outer = new JPanel(new BorderLayout(0, 8));
        outer.setOpaque(false);

        JLabel summary = new JLabel(buildCollectionProgressSummaryText(rows), SwingConstants.CENTER);
        summary.setFont(new Font("Dialog", Font.BOLD, Math.max(14, (int) (16 * (cardSize / 275.0)))));
        summary.setForeground(new Color(20, 35, 65));
        outer.add(summary, BorderLayout.NORTH);

        JPanel table = new JPanel(new GridLayout(0, 1, 0, 4));
        table.setOpaque(false);
        table.add(buildCollectionProgressHeaderRow());

        for (CollectionProgressRow row : rows) {
            table.add(buildCollectionProgressDataRow(row));
        }

        outer.add(table, BorderLayout.NORTH);
        return outer;
    }

    private String buildCollectionProgressSummaryText(List<CollectionProgressRow> rows) {
        int eligible = 0;
        int close = 0;
        for (CollectionProgressRow row : rows) {
            if (row.isEligible()) {
                eligible++;
            } else if (row.getNeeded() <= 3) {
                close++;
            }
        }
        return "Eligible: " + eligible + " | Within 3 cards: " + close + " | Total scopes: " + rows.size();
    }

    private JPanel buildCollectionProgressHeaderRow() {
        JPanel row = buildCollectionProgressRowShell(new Color(235, 225, 205));
        row.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 160, 120), 1),
                new EmptyBorder(5, 7, 5, 7)
        ));
        addProgressCell(row, "Type", true);
        addProgressCell(row, "Era / Tag", true);
        addProgressCell(row, "Owned", true);
        addProgressCell(row, "Total", true);
        addProgressCell(row, "Entry", true);
        addProgressCell(row, "Status", true);
        addProgressCell(row, "Strength", true);
        addProgressCell(row, "Open", true);
        return row;
    }

    private JPanel buildCollectionProgressDataRow(CollectionProgressRow progressRow) {
        Color bg = progressRow.isEligible()
                ? new Color(232, 246, 226)
                : progressRow.getNeeded() <= 3
                        ? new Color(255, 246, 218)
                        : new Color(255, 253, 245);
        JPanel row = buildCollectionProgressRowShell(bg);
        row.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(215, 200, 165), 1),
                new EmptyBorder(5, 7, 5, 7)
        ));

        addProgressCell(row, progressRow.getTypeLabel(), false);
        addProgressCell(row, progressRow.getDisplayName(), false);
        addProgressCell(row, String.valueOf(progressRow.getOwned()), false);
        addProgressCell(row, String.valueOf(progressRow.getTotal()), false);
        addProgressCell(row, String.valueOf(progressRow.getRequired()), false);
        addProgressCell(row, progressRow.getStatusText(), false);
        addProgressCell(row, progressRow.getStrengthText(), false);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        buttonPanel.setOpaque(false);
        JButton openButton = makeDashboardButton("View");
        openButton.setFont(new Font("Dialog", Font.BOLD, Math.max(11, (int) (12 * (cardSize / 275.0)))));
        openButton.addActionListener(e -> openCollectionProgressRow(progressRow));
        buttonPanel.add(openButton);
        row.add(buttonPanel);
        return row;
    }

    private JPanel buildCollectionProgressRowShell(Color backgroundColor) {
        JPanel row = new JPanel(new GridLayout(1, 8, 6, 0));
        row.setBackground(backgroundColor);
        row.setOpaque(true);
        return row;
    }

    private void addProgressCell(JPanel row, String text, boolean header) {
        JLabel label = new JLabel(text == null ? "" : text, SwingConstants.CENTER);
        label.setFont(new Font(
                "Dialog",
                header ? Font.BOLD : Font.PLAIN,
                Math.max(12, (int) (14 * (cardSize / 275.0)))
        ));
        label.setForeground(header ? new Color(20, 35, 65) : new Color(55, 55, 55));
        row.add(label);
    }

    private void openCollectionProgressRow(CollectionProgressRow progressRow) {
        if (progressRow == null) {
            return;
        }

        collectionOwnershipFilter = COLLECTION_OWNERSHIP_OWNED;
        if (COLLECTION_SCOPE_ERA.equals(progressRow.getScopeType())) {
            collectionScopeMode = COLLECTION_SCOPE_ERA;
            collectionEraFilterId = progressRow.getScopeKey();
        } else if (COLLECTION_SCOPE_TAG.equals(progressRow.getScopeType())) {
            collectionScopeMode = COLLECTION_SCOPE_TAG;
            collectionTagFilter = progressRow.getScopeKey();
        }
        displayCollectionCards();
    }

    private List<String> getProgressDashboardTags() {
        ArrayList<String> tags = new ArrayList<String>();
        if (campaign == null) {
            return tags;
        }

        for (String tag : CardData.getAllTags()) {
            if (tag == null || tag.trim().isEmpty() || isEraTag(tag)) {
                continue;
            }
            if (getCandidateCountForTag(tag) >= TAG_TOURNAMENT_REQUIRED_CARDS) {
                tags.add(tag);
            }
        }
        Collections.sort(tags);
        return tags;
    }

    private List<President> getCandidatesForTag(String tag) {
        ArrayList<President> candidates = new ArrayList<President>();
        if (campaign == null || tag == null || tag.trim().isEmpty()) {
            return candidates;
        }
        for (President president : campaign.getAllPresidents()) {
            if (president != null && president.hasTag(tag)) {
                candidates.add(president);
            }
        }
        Collections.sort(candidates, presidentAverageComparator());
        return candidates;
    }

    private int getOwnedCountForCandidates(CampaignPlayer player, List<President> candidates) {
        if (player == null || candidates == null || candidates.isEmpty()) {
            return 0;
        }
        Set<String> ownedNames = getOwnedPresidentNames(player);
        int owned = 0;
        for (President president : candidates) {
            if (president != null && ownedNames.contains(president.getName())) {
                owned++;
            }
        }
        return owned;
    }

    private double calculateScopedDeckStrength(
            CampaignPlayer player, List<President> candidates, int requiredCards
    ) {
        if (player == null || candidates == null || requiredCards <= 0) {
            return 0.0;
        }

        Set<String> ownedNames = getOwnedPresidentNames(player);
        ArrayList<President> ownedCandidates = new ArrayList<President>();
        for (President president : candidates) {
            if (president != null && ownedNames.contains(president.getName())) {
                ownedCandidates.add(president);
            }
        }
        Collections.sort(ownedCandidates, presidentAverageComparator());
        if (ownedCandidates.size() < requiredCards) {
            return 0.0;
        }
        return calculateDeckStrength(new ArrayList<President>(ownedCandidates.subList(0, requiredCards)));
    }

    private void sortCollectionProgressRows(List<CollectionProgressRow> rows) {
        if (rows == null) {
            return;
        }
        Collections.sort(rows, new Comparator<CollectionProgressRow>() {
            @Override
            public int compare(CollectionProgressRow r1, CollectionProgressRow r2) {
                if (r1 == null && r2 == null) {
                    return 0;
                }
                if (r1 == null) {
                    return 1;
                }
                if (r2 == null) {
                    return -1;
                }

                if (PROGRESS_SORT_CLOSEST_LOCKED.equals(collectionProgressSortMode)) {
                    int lockedCompare = Boolean.compare(r1.isEligible(), r2.isEligible());
                    if (lockedCompare != 0) {
                        return lockedCompare;
                    }
                    int neededCompare = Integer.compare(r1.getNeeded(), r2.getNeeded());
                    if (neededCompare != 0) {
                        return neededCompare;
                    }
                    return r1.getDisplayName().compareToIgnoreCase(r2.getDisplayName());
                }

                if (PROGRESS_SORT_PERCENT_OWNED.equals(collectionProgressSortMode)) {
                    int pctCompare = Double.compare(r2.getOwnedPct(), r1.getOwnedPct());
                    if (pctCompare != 0) {
                        return pctCompare;
                    }
                    return r1.getDisplayName().compareToIgnoreCase(r2.getDisplayName());
                }

                if (PROGRESS_SORT_OWNED_COUNT.equals(collectionProgressSortMode)) {
                    int ownedCompare = Integer.compare(r2.getOwned(), r1.getOwned());
                    if (ownedCompare != 0) {
                        return ownedCompare;
                    }
                    return r1.getDisplayName().compareToIgnoreCase(r2.getDisplayName());
                }

                if (PROGRESS_SORT_DECK_STRENGTH.equals(collectionProgressSortMode)) {
                    int strengthCompare = Double.compare(r2.getDeckStrength(), r1.getDeckStrength());
                    if (strengthCompare != 0) {
                        return strengthCompare;
                    }
                    return r1.getDisplayName().compareToIgnoreCase(r2.getDisplayName());
                }

                if (PROGRESS_SORT_ALPHABETICAL.equals(collectionProgressSortMode)) {
                    int typeCompare = r1.getTypeLabel().compareToIgnoreCase(r2.getTypeLabel());
                    if (typeCompare != 0) {
                        return typeCompare;
                    }
                    return r1.getDisplayName().compareToIgnoreCase(r2.getDisplayName());
                }

                if (PROGRESS_SORT_CHRONOLOGICAL.equals(collectionProgressSortMode)) {
                    int eraCompare = Integer.compare(getEraChronologyIndex(r1), getEraChronologyIndex(r2));
                    if (eraCompare != 0) {
                        return eraCompare;
                    }
                    int typeCompare = r1.getTypeLabel().compareToIgnoreCase(r2.getTypeLabel());
                    if (typeCompare != 0) {
                        return typeCompare;
                    }
                    return r1.getDisplayName().compareToIgnoreCase(r2.getDisplayName());
                }

                int eligibleCompare = Boolean.compare(r2.isEligible(), r1.isEligible());
                if (eligibleCompare != 0) {
                    return eligibleCompare;
                }
                if (r1.isEligible() && r2.isEligible()) {
                    int strengthCompare = Double.compare(r2.getDeckStrength(), r1.getDeckStrength());
                    if (strengthCompare != 0) {
                        return strengthCompare;
                    }
                }
                int neededCompare = Integer.compare(r1.getNeeded(), r2.getNeeded());
                if (neededCompare != 0) {
                    return neededCompare;
                }
                return r1.getDisplayName().compareToIgnoreCase(r2.getDisplayName());
            }
        });
    }

    private int getEraChronologyIndex(CollectionProgressRow row) {
        if (row == null || !COLLECTION_SCOPE_ERA.equals(row.getScopeType())) {
            return Integer.MAX_VALUE;
        }
        if (campaign == null) {
            return Integer.MAX_VALUE - 1;
        }

        List<CampaignTournament> tournaments = campaign.getEraTournaments();
        for (int i = 0; i < tournaments.size(); i++) {
            CampaignTournament tournament = tournaments.get(i);
            if (tournament == null) {
                continue;
            }
            if (safeEqualsIgnoreCase(row.getScopeKey(), tournament.getId())
                    || safeEqualsIgnoreCase(row.getDisplayName(), tournament.getScopeName())) {
                return i;
            }
        }
        return Integer.MAX_VALUE - 1;
    }

    private boolean safeEqualsIgnoreCase(String a, String b) {
        if (a == null || b == null) {
            return false;
        }
        return a.equalsIgnoreCase(b);
    }

    private List<CollectionCardItem> buildFilteredCollectionCards(
            CampaignPlayer player, CollectionScope scope
    ) {
        ArrayList<CollectionCardItem> filteredCards = new ArrayList<CollectionCardItem>();
        if (player == null || campaign == null || scope == null) {
            return filteredCards;
        }

        Set<String> ownedNames = getOwnedPresidentNames(player);
        List<President> candidates = scope.getCandidates();

        for (President president : candidates) {
            if (president == null) {
                continue;
            }

            boolean owned = ownedNames.contains(president.getName());
            if (COLLECTION_OWNERSHIP_OWNED.equals(collectionOwnershipFilter) && !owned) {
                continue;
            }
            if (COLLECTION_OWNERSHIP_MISSING.equals(collectionOwnershipFilter) && owned) {
                continue;
            }
            filteredCards.add(new CollectionCardItem(president, owned));
        }

        Collections.sort(filteredCards, collectionCardItemAverageComparator());
        return filteredCards;
    }

    private Comparator<CollectionCardItem> collectionCardItemAverageComparator() {
        return new Comparator<CollectionCardItem>() {
            @Override
            public int compare(CollectionCardItem c1, CollectionCardItem c2) {
                President p1 = c1 == null ? null : c1.getPresident();
                President p2 = c2 == null ? null : c2.getPresident();
                return presidentAverageComparator().compare(p1, p2);
            }
        };
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

    private Set<String> getOwnedPresidentNames(CampaignPlayer player) {
        HashSet<String> ownedNames = new HashSet<String>();
        if (player == null) {
            return ownedNames;
        }

        for (President president : player.getPresidentCollection()) {
            if (president != null && president.getName() != null) {
                ownedNames.add(president.getName());
            }
        }
        return ownedNames;
    }

    private CollectionScope getSelectedCollectionScope() {
        if (campaign == null) {
            return new CollectionScope(COLLECTION_SCOPE_ALL, COLLECTION_SCOPE_ALL, new ArrayList<President>());
        }

        if (COLLECTION_SCOPE_ERA.equals(collectionScopeMode)) {
            CampaignTournament tournament = getSelectedCollectionEraTournament();
            if (tournament != null) {
                return new CollectionScope(
                        COLLECTION_SCOPE_ERA,
                        tournament.getScopeName(),
                        campaign.getPresidentsForTournament(tournament),
                        tournament
                );
            }
        }

        if (COLLECTION_SCOPE_TAG.equals(collectionScopeMode)) {
            String tag = collectionTagFilter;
            if (tag != null && !tag.trim().isEmpty() && !COLLECTION_TAG_ALL.equals(tag)) {
                ArrayList<President> taggedCards = new ArrayList<President>();
                for (President president : campaign.getAllPresidents()) {
                    if (president != null && president.hasTag(tag)) {
                        taggedCards.add(president);
                    }
                }
                Collections.sort(taggedCards, presidentAverageComparator());
                return new CollectionScope(COLLECTION_SCOPE_TAG, tag, taggedCards);
            }
            return new CollectionScope(COLLECTION_SCOPE_TAG, COLLECTION_TAG_ALL, campaign.getAllPresidents());
        }

        if (COLLECTION_SCOPE_FILTERS.equals(collectionScopeMode)) {
            ArrayList<President> filteredCards = new ArrayList<President>();
            for (President president : campaign.getAllPresidents()) {
                if (matchesFullCollectionFilters(president)) {
                    filteredCards.add(president);
                }
            }
            Collections.sort(filteredCards, presidentAverageComparator());
            return new CollectionScope(
                    COLLECTION_SCOPE_FILTERS, buildFullFilterDisplayName(), filteredCards
            );
        }

        return new CollectionScope(COLLECTION_SCOPE_ALL, COLLECTION_SCOPE_ALL, campaign.getAllPresidents());
    }

    private boolean matchesFullCollectionFilters(President president) {
        if (president == null) {
            return false;
        }

        if (!matchesCollectionTextFilter(president.getEra(), collectionFullEraFilter, COLLECTION_ERA_ALL)) {
            return false;
        }
        if (!COLLECTION_TAG_ALL.equals(collectionFullTagFilter) && !president.hasTag(collectionFullTagFilter)) {
            return false;
        }
        if (!matchesCollectionTextFilter(president.getParty(), collectionFullPartyFilter, COLLECTION_PARTY_ALL)) {
            return false;
        }
        if (!matchesCollectionTextFilter(president.getState(), collectionFullStateFilter, COLLECTION_STATE_ALL)) {
            return false;
        }
        if (!matchesCollectionTextFilter(president.getRegion(), collectionFullRegionFilter, COLLECTION_REGION_ALL)) {
            return false;
        }
        if (!matchesCollectionTextFilter(
                president.getIdeology(), collectionFullIdeologyFilter, COLLECTION_IDEOLOGY_ALL
        )) {
            return false;
        }
        return true;
    }

    private boolean matchesCollectionTextFilter(String value, String selectedFilter, String allFilter) {
        if (selectedFilter == null || selectedFilter.trim().isEmpty() || allFilter.equals(selectedFilter)) {
            return true;
        }
        return value != null && selectedFilter.equalsIgnoreCase(value.trim());
    }

    private String buildFullFilterDisplayName() {
        ArrayList<String> parts = new ArrayList<String>();
        addFilterDisplayPart(parts, "Era", collectionFullEraFilter, COLLECTION_ERA_ALL);
        addFilterDisplayPart(parts, "Tag", collectionFullTagFilter, COLLECTION_TAG_ALL);
        addFilterDisplayPart(parts, "Party", collectionFullPartyFilter, COLLECTION_PARTY_ALL);
        addFilterDisplayPart(parts, "State", collectionFullStateFilter, COLLECTION_STATE_ALL);
        addFilterDisplayPart(parts, "Region", collectionFullRegionFilter, COLLECTION_REGION_ALL);
        addFilterDisplayPart(parts, "Ideology", collectionFullIdeologyFilter, COLLECTION_IDEOLOGY_ALL);

        if (parts.isEmpty()) {
            return "Full Filters";
        }
        return "Filters: " + joinStrings(parts, ", ");
    }

    private String joinStrings(List<String> values, String separator) {
        if (values == null || values.isEmpty()) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < values.size(); i++) {
            if (i > 0) {
                builder.append(separator);
            }
            builder.append(values.get(i));
        }
        return builder.toString();
    }

    private void addFilterDisplayPart(ArrayList<String> parts, String label, String value, String allValue) {
        if (value != null && !value.trim().isEmpty() && !allValue.equals(value)) {
            parts.add(label + "=" + value);
        }
    }

    private String[] getFullFilterEraLabels() {
        return getCandidateFieldFilterLabels(COLLECTION_ERA_ALL, "era");
    }

    private String[] getFullFilterTagLabels() {
        return getCollectionTagFilterLabels();
    }

    private String[] getCandidateFieldFilterLabels(String allLabel, String fieldName) {
        ArrayList<String> labels = new ArrayList<String>();
        labels.add(allLabel);

        ArrayList<String> values = new ArrayList<String>();
        HashSet<String> seen = new HashSet<String>();
        if (campaign != null) {
            for (President president : campaign.getAllPresidents()) {
                String value = getCandidateFieldValue(president, fieldName);
                if (value == null || value.trim().isEmpty()) {
                    continue;
                }
                String cleaned = value.trim();
                String key = cleaned.toLowerCase();
                if (!seen.contains(key)) {
                    values.add(cleaned);
                    seen.add(key);
                }
            }
        }

        Collections.sort(values, String.CASE_INSENSITIVE_ORDER);
        labels.addAll(values);
        return labels.toArray(new String[labels.size()]);
    }

    private String getCandidateFieldValue(President president, String fieldName) {
        if (president == null || fieldName == null) {
            return "";
        }
        if ("era".equals(fieldName)) {
            return president.getEra();
        }
        if ("party".equals(fieldName)) {
            return president.getParty();
        }
        if ("state".equals(fieldName)) {
            return president.getState();
        }
        if ("region".equals(fieldName)) {
            return president.getRegion();
        }
        if ("ideology".equals(fieldName)) {
            return president.getIdeology();
        }
        return "";
    }

    private int getCollectionScopeTotal(CollectionScope scope) {
        if (scope == null) {
            return 0;
        }
        return scope.getCandidates().size();
    }

    private int getCollectionScopeOwnedCount(CampaignPlayer player, CollectionScope scope) {
        if (player == null || scope == null) {
            return 0;
        }

        Set<String> ownedNames = getOwnedPresidentNames(player);
        int ownedCount = 0;
        for (President president : scope.getCandidates()) {
            if (president != null && ownedNames.contains(president.getName())) {
                ownedCount++;
            }
        }
        return ownedCount;
    }

    private String buildCollectionTitleText(CollectionScope scope, int owned, int total) {
        double pct = total == 0 ? 0.0 : owned * 100.0 / total;
        if (scope == null || COLLECTION_SCOPE_ALL.equals(scope.getScopeType())) {
            return "Collection - " + owned + "/" + total + " (" + String.format("%.1f", pct) + "%)";
        }
        return "Collection - " + scope.getDisplayName() + " - " + owned + "/" + total
                + " (" + String.format("%.1f", pct) + "%)";
    }

    private void displayLoadedCollectionCards(
            List<CardIconData> loadedCards, int columns, int ownedInScope,
            int totalInScope, int missingInScope, CollectionScope scope
    ) {
        if (displayPanel == null || !showingCollectionCards) {
            return;
        }

        displayPanel.removeAll();
        displayPanel.setLayout(new BorderLayout());
        displayPanel.setBackground(new Color(255, 253, 245));

        JPanel wrapper = new JPanel(new BorderLayout(10, 10));
        wrapper.setOpaque(false);

        wrapper.add(buildRosterTabPanel("Collection"), BorderLayout.NORTH);

        JPanel body = new JPanel(new BorderLayout(10, 10));
        body.setOpaque(false);
        body.add(buildCollectionTopPanel(ownedInScope, totalInScope, missingInScope, scope), BorderLayout.NORTH);

        if (loadedCards.isEmpty()) {
            body.add(makeCenteredDisplayMessage("No cards match these filters."), BorderLayout.CENTER);
        } else {
            JPanel cardGrid = new JPanel(new GridLayout(0, columns, 20, 20));
            cardGrid.setBackground(new Color(255, 253, 245));
            cardGrid.setBorder(new EmptyBorder(8, 14, 8, 14));

            for (CardIconData cardData : loadedCards) {
                cardGrid.add(buildPresidentCardPanel(
                        cardData.getPresident(), cardData.getImageWidth(), cardData.getIcon(), cardData.isOwned()
                ));
            }
            body.add(cardGrid, BorderLayout.CENTER);
        }

        wrapper.add(body, BorderLayout.CENTER);
        displayPanel.add(wrapper, BorderLayout.NORTH);
        updateDisplayPanel();
    }

    private JPanel buildCollectionTopPanel(
            int ownedInScope, int totalInScope, int missingInScope, CollectionScope scope
    ) {
        JPanel topPanel = new JPanel(new BorderLayout(8, 8));
        topPanel.setOpaque(false);

        String scopeDescription = scope == null ? "All Cards" : scope.getDisplayName();
        String eligibilityText = buildCollectionEligibilityText(scope, ownedInScope, totalInScope);

        JLabel guidance = new JLabel(
                "<html><center>Browse your collection by all cards, era, tag, or full filters. Missing cards are shown dimmed in grayscale. Right-click a card to zoom.<br>"
                        + "Scope: " + escapeHtml(scopeDescription)
                        + " | Owned: " + ownedInScope + "/" + totalInScope
                        + " | Missing: " + missingInScope
                        + eligibilityText
                        + " | Showing: " + escapeHtml(collectionOwnershipFilter)
                        + "</center></html>",
                SwingConstants.CENTER
        );
        guidance.setFont(new Font("Dialog", Font.PLAIN, Math.max(14, (int) (16 * (cardSize / 275.0)))));
        guidance.setForeground(new Color(70, 70, 70));
        topPanel.add(guidance, BorderLayout.NORTH);

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.CENTER, 14, 4));
        controls.setOpaque(false);

        JLabel viewLabel = new JLabel("View:");
        viewLabel.setFont(new Font("Dialog", Font.BOLD, Math.max(13, (int) (15 * (cardSize / 275.0)))));
        controls.add(viewLabel);

        JComboBox<String> scopeCombo = new JComboBox<String>(new String[] {
                COLLECTION_SCOPE_PROGRESS,
                COLLECTION_SCOPE_ALL,
                COLLECTION_SCOPE_ERA,
                COLLECTION_SCOPE_TAG,
                COLLECTION_SCOPE_FILTERS
        });
        scopeCombo.setSelectedItem(collectionScopeMode);
        scopeCombo.setFont(new Font("Dialog", Font.PLAIN, Math.max(13, (int) (15 * (cardSize / 275.0)))));
        scopeCombo.addActionListener(e -> {
            collectionScopeMode = (String) scopeCombo.getSelectedItem();
            displayCollectionCards();
        });
        controls.add(scopeCombo);

        if (COLLECTION_SCOPE_ERA.equals(collectionScopeMode)) {
            JLabel eraLabel = new JLabel("Era:");
            eraLabel.setFont(new Font("Dialog", Font.BOLD, Math.max(13, (int) (15 * (cardSize / 275.0)))));
            controls.add(eraLabel);

            JComboBox<String> eraCombo = new JComboBox<String>(getCollectionEraFilterLabels());
            eraCombo.setSelectedItem(getCollectionEraFilterLabel());
            eraCombo.setFont(new Font("Dialog", Font.PLAIN, Math.max(13, (int) (15 * (cardSize / 275.0)))));
            eraCombo.addActionListener(e -> {
                collectionEraFilterId = getCollectionEraIdForLabel((String) eraCombo.getSelectedItem());
                displayCollectionCards();
            });
            controls.add(eraCombo);
        }

        if (COLLECTION_SCOPE_TAG.equals(collectionScopeMode)) {
            JLabel tagLabel = new JLabel("Tag:");
            tagLabel.setFont(new Font("Dialog", Font.BOLD, Math.max(13, (int) (15 * (cardSize / 275.0)))));
            controls.add(tagLabel);

            JComboBox<String> tagCombo = new JComboBox<String>(getCollectionTagFilterLabels());
            tagCombo.setSelectedItem(collectionTagFilter);
            tagCombo.setFont(new Font("Dialog", Font.PLAIN, Math.max(13, (int) (15 * (cardSize / 275.0)))));
            tagCombo.addActionListener(e -> {
                collectionTagFilter = (String) tagCombo.getSelectedItem();
                displayCollectionCards();
            });
            controls.add(tagCombo);
        }

        JLabel ownershipLabel = new JLabel("Cards:");
        ownershipLabel.setFont(new Font("Dialog", Font.BOLD, Math.max(13, (int) (15 * (cardSize / 275.0)))));
        controls.add(ownershipLabel);

        JComboBox<String> ownershipCombo = new JComboBox<String>(new String[] {
                COLLECTION_OWNERSHIP_OWNED,
                COLLECTION_OWNERSHIP_MISSING,
                COLLECTION_OWNERSHIP_ALL
        });
        ownershipCombo.setSelectedItem(collectionOwnershipFilter);
        ownershipCombo.setFont(new Font("Dialog", Font.PLAIN, Math.max(13, (int) (15 * (cardSize / 275.0)))));
        ownershipCombo.addActionListener(e -> {
            collectionOwnershipFilter = (String) ownershipCombo.getSelectedItem();
            displayCollectionCards();
        });
        controls.add(ownershipCombo);

        JPanel controlRows = new JPanel(new GridLayout(0, 1, 0, 3));
        controlRows.setOpaque(false);
        controlRows.add(controls);
        if (COLLECTION_SCOPE_FILTERS.equals(collectionScopeMode)) {
            controlRows.add(buildCollectionFullFilterPanel());
        }

        topPanel.add(controlRows, BorderLayout.SOUTH);
        return topPanel;
    }

    private JPanel buildCollectionFullFilterPanel() {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 2));
        row.setOpaque(false);

        addFullFilterCombo(row, "Era:", getFullFilterEraLabels(), collectionFullEraFilter, value -> {
            collectionFullEraFilter = value;
            displayCollectionCards();
        });

        addFullFilterCombo(row, "Tag:", getFullFilterTagLabels(), collectionFullTagFilter, value -> {
            collectionFullTagFilter = value;
            displayCollectionCards();
        });

        addFullFilterCombo(row, "Party:", getCandidateFieldFilterLabels(COLLECTION_PARTY_ALL, "party"),
                collectionFullPartyFilter, value -> {
                    collectionFullPartyFilter = value;
                    displayCollectionCards();
                });

        addFullFilterCombo(row, "State:", getCandidateFieldFilterLabels(COLLECTION_STATE_ALL, "state"),
                collectionFullStateFilter, value -> {
                    collectionFullStateFilter = value;
                    displayCollectionCards();
                });

        addFullFilterCombo(row, "Region:", getCandidateFieldFilterLabels(COLLECTION_REGION_ALL, "region"),
                collectionFullRegionFilter, value -> {
                    collectionFullRegionFilter = value;
                    displayCollectionCards();
                });

        addFullFilterCombo(row, "Ideology:", getCandidateFieldFilterLabels(COLLECTION_IDEOLOGY_ALL, "ideology"),
                collectionFullIdeologyFilter, value -> {
                    collectionFullIdeologyFilter = value;
                    displayCollectionCards();
                });

        JButton clearButton = makeDashboardButton("Clear Filters");
        clearButton.setFont(new Font("Dialog", Font.BOLD, Math.max(10, (int) (11 * (cardSize / 275.0)))));
        clearButton.addActionListener(e -> {
            resetFullCollectionFilters();
            displayCollectionCards();
        });
        row.add(clearButton);

        return row;
    }

    private void addFullFilterCombo(
            JPanel row, String labelText, String[] values, String selectedValue, FilterSelectionHandler handler
    ) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Dialog", Font.BOLD, Math.max(12, (int) (14 * (cardSize / 275.0)))));
        row.add(label);

        JComboBox<String> combo = new JComboBox<String>(values);
        combo.setSelectedItem(selectedValue);
        combo.setFont(new Font("Dialog", Font.PLAIN, Math.max(11, (int) (12 * (cardSize / 275.0)))));
        Dimension preferredSize = combo.getPreferredSize();
        combo.setPreferredSize(new Dimension(getFullFilterComboWidth(labelText), preferredSize.height));
        combo.addActionListener(e -> {
            Object selected = combo.getSelectedItem();
            handler.onSelect(selected == null ? "" : selected.toString());
        });
        row.add(combo);
    }

    private int getFullFilterComboWidth(String labelText) {
        double scale = Math.max(0.85, cardSize / 275.0);
        int baseWidth;
        if ("Tag:".equals(labelText)) {
            baseWidth = 145;
        } else if ("Era:".equals(labelText)) {
            baseWidth = 125;
        } else if ("State:".equals(labelText)) {
            baseWidth = 80;
        } else if ("Region:".equals(labelText)) {
            baseWidth = 105;
        } else {
            baseWidth = 115;
        }
        return Math.max(70, (int) Math.round(baseWidth * scale));
    }

    private void resetFullCollectionFilters() {
        collectionFullEraFilter = COLLECTION_ERA_ALL;
        collectionFullTagFilter = COLLECTION_TAG_ALL;
        collectionFullPartyFilter = COLLECTION_PARTY_ALL;
        collectionFullStateFilter = COLLECTION_STATE_ALL;
        collectionFullRegionFilter = COLLECTION_REGION_ALL;
        collectionFullIdeologyFilter = COLLECTION_IDEOLOGY_ALL;
    }

    private String buildCollectionEligibilityText(CollectionScope scope, int ownedInScope, int totalInScope) {
        if (scope == null) {
            return "";
        }

        if (COLLECTION_SCOPE_ERA.equals(scope.getScopeType()) && scope.getTournament() != null) {
            int required = scope.getTournament().getRequiredEraCards();
            if (ownedInScope >= required) {
                return " | Era deck strength: "
                        + String.format("%.2f", getScopedOwnedDeckStrength(scope, required));
            }
            return " | Need " + (required - ownedInScope) + " more for era tournament entry";
        }

        if (COLLECTION_SCOPE_TAG.equals(scope.getScopeType()) && !COLLECTION_TAG_ALL.equals(scope.getDisplayName())) {
            if (totalInScope < TAG_TOURNAMENT_REQUIRED_CARDS) {
                return " | Below future tag tournament size";
            }
            if (ownedInScope >= TAG_TOURNAMENT_REQUIRED_CARDS) {
                return " | Tag deck strength: "
                        + String.format("%.2f", getScopedOwnedDeckStrength(scope, TAG_TOURNAMENT_REQUIRED_CARDS));
            }
            return " | Need " + (TAG_TOURNAMENT_REQUIRED_CARDS - ownedInScope)
                    + " more for future tag tournament entry";
        }

        return "";
    }

    private double getScopedOwnedDeckStrength(CollectionScope scope, int requiredCards) {
        if (campaign == null || campaign.getPlayer() == null || scope == null || requiredCards <= 0) {
            return 0.0;
        }

        Set<String> ownedNames = getOwnedPresidentNames(campaign.getPlayer());
        ArrayList<President> eligibleOwnedCards = new ArrayList<President>();
        for (President president : scope.getCandidates()) {
            if (president != null && ownedNames.contains(president.getName())) {
                eligibleOwnedCards.add(president);
            }
        }

        Collections.sort(eligibleOwnedCards, presidentAverageComparator());
        if (eligibleOwnedCards.size() < requiredCards) {
            return 0.0;
        }

        return calculateDeckStrength(new ArrayList<President>(eligibleOwnedCards.subList(0, requiredCards)));
    }

    private String[] getCollectionEraFilterLabels() {
        ArrayList<String> labels = new ArrayList<String>();
        labels.add(COLLECTION_ERA_ALL);
        if (campaign != null) {
            for (CampaignTournament tournament : campaign.getEraTournaments()) {
                labels.add(tournament.getScopeName());
            }
        }
        return labels.toArray(new String[labels.size()]);
    }

    private String[] getCollectionTagFilterLabels() {
        ArrayList<String> labels = new ArrayList<String>();
        labels.add(COLLECTION_TAG_ALL);
        if (campaign != null) {
            labels.addAll(getEligibleCollectionTags());
        }
        return labels.toArray(new String[labels.size()]);
    }

    private List<String> getEligibleCollectionTags() {
        ArrayList<String> tags = new ArrayList<String>();
        if (campaign == null) {
            return tags;
        }

        for (String tag : CardData.getAllTags()) {
            if (tag == null || tag.trim().isEmpty() || isEraTag(tag)) {
                continue;
            }
            int count = getCandidateCountForTag(tag);
            if (count > 0) {
                tags.add(tag);
            }
        }
        Collections.sort(tags);
        return tags;
    }

    private boolean isEraTag(String tag) {
        if (tag == null) {
            return false;
        }

        String cleaned = tag.trim();
        if (cleaned.endsWith(" Era")) {
            return true;
        }

        if (campaign != null) {
            for (CampaignTournament tournament : campaign.getEraTournaments()) {
                if (tournament != null && cleaned.equalsIgnoreCase(tournament.getScopeName())) {
                    return true;
                }
            }
        }
        return false;
    }

    private int getCandidateCountForTag(String tag) {
        if (campaign == null || tag == null || tag.trim().isEmpty()) {
            return 0;
        }
        int count = 0;
        for (President president : campaign.getAllPresidents()) {
            if (president != null && president.hasTag(tag)) {
                count++;
            }
        }
        return count;
    }

    private String getCollectionEraFilterLabel() {
        if (COLLECTION_ERA_ALL.equals(collectionEraFilterId) || campaign == null) {
            return COLLECTION_ERA_ALL;
        }

        CampaignTournament tournament = campaign.getTournamentById(collectionEraFilterId);
        return tournament == null ? COLLECTION_ERA_ALL : tournament.getScopeName();
    }

    private String getCollectionEraIdForLabel(String label) {
        if (label == null || COLLECTION_ERA_ALL.equals(label) || campaign == null) {
            return COLLECTION_ERA_ALL;
        }

        for (CampaignTournament tournament : campaign.getEraTournaments()) {
            if (label.equals(tournament.getScopeName())) {
                return tournament.getId();
            }
        }
        return COLLECTION_ERA_ALL;
    }

    private CampaignTournament getSelectedCollectionEraTournament() {
        if (campaign == null || COLLECTION_ERA_ALL.equals(collectionEraFilterId)) {
            return null;
        }
        CampaignTournament tournament = campaign.getTournamentById(collectionEraFilterId);
        if (tournament == null) {
            collectionEraFilterId = COLLECTION_ERA_ALL;
        }
        return tournament;
    }

    private void cancelCollectionLoadWorker() {
        collectionLoadRequestId++;
        if (collectionLoadWorker != null && !collectionLoadWorker.isDone()) {
            collectionLoadWorker.cancel(true);
        }
        collectionLoadWorker = null;
    }

    private JPanel buildRosterTabPanel(String selectedTab) {
        JPanel tabPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 0));
        tabPanel.setOpaque(false);
        tabPanel.setBorder(new EmptyBorder(0, 0, 8, 0));

        JButton activeDeckTab = makeDashboardButton("Active Deck");
        JButton collectionTab = makeDashboardButton("Collection");
        JButton trophyTab = makeDashboardButton("Trophy Cabinet");

        boolean activeSelected = "Active Deck".equals(selectedTab);
        boolean collectionSelected = "Collection".equals(selectedTab);
        boolean trophySelected = "Trophy Cabinet".equals(selectedTab);

        styleRosterTabButton(activeDeckTab, activeSelected);
        styleRosterTabButton(collectionTab, collectionSelected);
        styleRosterTabButton(trophyTab, trophySelected);

        activeDeckTab.addActionListener(e -> displayActiveDeckCards());
        collectionTab.addActionListener(e -> displayCollectionCards());
        trophyTab.addActionListener(e -> displayTrophyCabinet());

        tabPanel.add(activeDeckTab);
        tabPanel.add(collectionTab);
        tabPanel.add(trophyTab);
        return tabPanel;
    }

    private void styleRosterTabButton(JButton button, boolean selected) {
        if (button == null) {
            return;
        }
        button.setEnabled(!selected);
        if (selected) {
            button.setForeground(new Color(20, 35, 65));
            button.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(222, 162, 6), 2),
                    new EmptyBorder(2, 8, 2, 8)
            ));
            button.setBorderPainted(true);
        }
    }

    private void refreshActiveDeckCards() {
        if (activeDeckEditMode) {
            refreshActiveDeckEditorCards();
        } else {
            refreshActiveDeckViewCards();
        }
    }

    private void refreshActiveDeckViewCards() {
        if (displayPanel == null) {
            return;
        }

        cancelCollectionLoadWorker();

        displayPanel.removeAll();
        displayPanel.setLayout(new BorderLayout());
        displayPanel.setBackground(new Color(255, 253, 245));

        if (campaign == null || campaign.getPlayer() == null) {
            displayTitleLabel.setText("Active Deck");
            displayPanel.add(makeCenteredDisplayMessage("No campaign loaded."), BorderLayout.CENTER);
            updateDisplayPanel();
            return;
        }

        List<President> activeDeck = campaign.getActiveDeck();
        displayTitleLabel.setText("Active Deck - " + campaign.getActiveDeckModeLabel()
                + " | " + campaign.getActiveDeckSize() + "/" + campaign.getActiveDeckMaxSize()
                + " | Strength: " + String.format("%.2f", campaign.getActiveDeckStrength()));

        JPanel wrapper = new JPanel(new BorderLayout(12, 12));
        wrapper.setOpaque(false);
        wrapper.setBorder(new EmptyBorder(10, 14, 12, 14));
        wrapper.add(buildRosterTabPanel("Active Deck"), BorderLayout.NORTH);

        JPanel body = new JPanel(new BorderLayout(12, 12));
        body.setOpaque(false);
        body.add(buildActiveDeckViewTopPanel(), BorderLayout.NORTH);

        if (activeDeck == null || activeDeck.isEmpty()) {
            body.add(makeCenteredDisplayMessage("No active deck cards yet. Open starter packs first."), BorderLayout.CENTER);
            wrapper.add(body, BorderLayout.CENTER);
            displayPanel.add(wrapper, BorderLayout.NORTH);
            updateDisplayPanel();
            return;
        }

        JPanel cardGrid = new JPanel(new GridLayout(0, 5, 18, 14));
        cardGrid.setBackground(new Color(255, 253, 245));
        cardGrid.setBorder(new EmptyBorder(8, 14, 8, 14));

        int imageWidth = getLargeCardImageWidth();
        for (President president : activeDeck) {
            cardGrid.add(buildPresidentCardPanel(president, imageWidth));
        }

        body.add(cardGrid, BorderLayout.CENTER);
        wrapper.add(body, BorderLayout.CENTER);
        displayPanel.add(wrapper, BorderLayout.NORTH);
        updateDisplayPanel();
    }

    private JPanel buildActiveDeckViewTopPanel() {
        JPanel topPanel = new JPanel(new BorderLayout(18, 0));
        topPanel.setOpaque(false);
        topPanel.setBorder(new EmptyBorder(0, 8, 4, 8));

        JLabel guidance = new JLabel(
                "<html><b>Quick Match Lineup</b><br>"
                        + "Mode: " + escapeHtml(campaign.getActiveDeckModeLabel())
                        + " | Cards: " + campaign.getActiveDeckSize() + "/" + campaign.getActiveDeckMaxSize()
                        + " | Strength: " + String.format("%.2f", campaign.getActiveDeckStrength())
                        + "</html>",
                SwingConstants.LEFT
        );
        guidance.setFont(new Font("Dialog", Font.PLAIN, Math.max(14, (int) (16 * (cardSize / 275.0)))));
        guidance.setForeground(new Color(70, 70, 70));
        topPanel.add(guidance, BorderLayout.CENTER);

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        controls.setOpaque(false);

        JButton editButton = makeDashboardButton("Edit");
        editButton.addActionListener(e -> displayActiveDeckEditorCards());
        controls.add(editButton);

        topPanel.add(controls, BorderLayout.EAST);
        return topPanel;
    }

    private void refreshActiveDeckEditorCards() {
        if (displayPanel == null) {
            return;
        }

        cancelCollectionLoadWorker();

        displayPanel.removeAll();
        displayPanel.setLayout(new BorderLayout());
        displayPanel.setBackground(new Color(255, 253, 245));

        if (campaign == null || campaign.getPlayer() == null) {
            displayTitleLabel.setText("Active Deck Editor");
            displayPanel.add(makeCenteredDisplayMessage("No campaign loaded."), BorderLayout.CENTER);
            updateDisplayPanel();
            return;
        }

        CampaignPlayer player = campaign.getPlayer();
        ensureActiveDeckDraftInitialized();

        displayTitleLabel.setText("Active Deck Editor - " + campaign.getActiveDeckModeLabel()
                + " | " + campaign.getActiveDeckSize() + "/" + campaign.getActiveDeckMaxSize()
                + " | Strength: " + String.format("%.2f", campaign.getActiveDeckStrength()));

        List<President> ownedCards = player.getPresidentCollection();
        Collections.sort(ownedCards, presidentAverageComparator());

        final int ownedImageWidth = getActiveDeckManagerCollectionCardImageWidth();
        final int ownedImageHeight = getCardImageHeight(ownedImageWidth);
        final int columns = getActiveDeckManagerCollectionColumnCount(ownedImageWidth);
        final long requestId = ++collectionLoadRequestId;

        final int totalOwnedCardsToLoad = ownedCards.size();
        final JLabel loadingLabel = makeCenteredDisplayMessage(
                buildLoadingImagesMessage("Loading Active Deck editor images", 0, totalOwnedCardsToLoad)
        );
        displayPanel.add(loadingLabel, BorderLayout.CENTER);
        updateDisplayPanel();

        collectionLoadWorker = new SwingWorker<List<CardIconData>, Integer>() {
            @Override
            protected List<CardIconData> doInBackground() {
                List<CardIconData> loadedCards = new ArrayList<CardIconData>();
                int loadedCount = 0;
                for (President president : ownedCards) {
                    if (isCancelled()) {
                        return loadedCards;
                    }

                    ImageIcon icon = loadScaledIcon(president.getImageURL(), ownedImageWidth, ownedImageHeight);
                    loadedCards.add(new CardIconData(president, ownedImageWidth, icon, true));
                    loadedCount++;
                    publish(loadedCount);
                }
                return loadedCards;
            }

            @Override
            protected void process(List<Integer> chunks) {
                if (chunks == null || chunks.isEmpty() || isCancelled()
                        || requestId != collectionLoadRequestId || !showingActiveDeckCards) {
                    return;
                }
                int loadedCount = chunks.get(chunks.size() - 1);
                setCenteredDisplayMessage(
                        loadingLabel,
                        buildLoadingImagesMessage(
                                "Loading Active Deck editor images", loadedCount, totalOwnedCardsToLoad
                        )
                );
            }

            @Override
            protected void done() {
                if (isCancelled() || requestId != collectionLoadRequestId || !showingActiveDeckCards) {
                    return;
                }

                try {
                    displayLoadedActiveDeckManager(get(), columns);
                } catch (Exception e) {
                    displayPanel.removeAll();
                    displayPanel.setLayout(new BorderLayout());
                    displayPanel.add(makeCenteredDisplayMessage(
                            "Could not load Active Deck editor images. Try reopening Active Deck."
                    ), BorderLayout.CENTER);
                    updateDisplayPanel();
                }
            }
        };
        collectionLoadWorker.execute();
    }

    private void displayLoadedActiveDeckManager(List<CardIconData> loadedOwnedCards, int collectionColumns) {
        if (displayPanel == null || !showingActiveDeckCards) {
            return;
        }

        ensureActiveDeckDraftInitialized();

        displayPanel.removeAll();
        displayPanel.setLayout(new BorderLayout());
        displayPanel.setBackground(new Color(255, 253, 245));

        JPanel wrapper = new JPanel(new BorderLayout(14, 14));
        wrapper.setOpaque(false);
        wrapper.setBorder(new EmptyBorder(18, 18, 18, 18));

        wrapper.add(buildRosterTabPanel("Active Deck"), BorderLayout.NORTH);

        JPanel body = new JPanel(new BorderLayout(14, 14));
        body.setOpaque(false);
        body.add(buildActiveDeckManagerTopPanel(), BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout(18, 18));
        centerPanel.setOpaque(false);
        centerPanel.add(buildActiveDeckDraftPanel(), BorderLayout.NORTH);
        centerPanel.add(buildActiveDeckOwnedCollectionPanel(loadedOwnedCards, collectionColumns), BorderLayout.CENTER);
        body.add(centerPanel, BorderLayout.CENTER);
        wrapper.add(body, BorderLayout.CENTER);

        displayPanel.add(wrapper, BorderLayout.NORTH);
        updateDisplayPanel();
    }

    private JPanel buildActiveDeckManagerTopPanel() {
        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setOpaque(false);

        int maxSize = campaign.getActiveDeckMaxSize();
        int draftSize = activeDeckDraft == null ? 0 : activeDeckDraft.size();
        double draftStrength = calculateDeckStrength(activeDeckDraft);

        String statusText = "<html><center><b>Active Deck Editor</b><br>"
                + "Current Mode: " + escapeHtml(campaign.getActiveDeckModeLabel())
                + " | Draft: " + draftSize + "/" + maxSize
                + " | Draft Strength: " + String.format("%.2f", draftStrength)
                + "<br>Click cards below to add/remove them. Save Custom Deck requires exactly "
                + maxSize + " cards. Era tournament decks are still auto-built by era."
                + "</center></html>";

        JLabel guidance = new JLabel(statusText, SwingConstants.CENTER);
        guidance.setFont(new Font("Dialog", Font.PLAIN, Math.max(14, (int) (16 * (cardSize / 275.0)))));
        guidance.setForeground(new Color(70, 70, 70));
        topPanel.add(guidance, BorderLayout.NORTH);

        JPanel controls = new JPanel(new FlowLayout(FlowLayout.CENTER, 14, 4));
        controls.setOpaque(false);

        JButton viewButton = makeDashboardButton("View Active Deck");
        viewButton.addActionListener(e -> displayActiveDeckCards());
        controls.add(viewButton);

        JButton autoModeButton = makeDashboardButton("Use Auto-Best");
        autoModeButton.addActionListener(e -> setAutoBestActiveDeckFromUI());
        controls.add(autoModeButton);

        JButton buildBestButton = makeDashboardButton("Draft Best 10");
        buildBestButton.addActionListener(e -> autoBuildBestActiveDeckDraft());
        controls.add(buildBestButton);

        JButton clearButton = makeDashboardButton("Clear Draft");
        clearButton.addActionListener(e -> clearActiveDeckDraft());
        controls.add(clearButton);

        JButton saveCustomButton = makeDashboardButton("Save Custom");
        saveCustomButton.addActionListener(e -> saveCustomActiveDeckDraft());
        controls.add(saveCustomButton);

        topPanel.add(controls, BorderLayout.SOUTH);
        return topPanel;
    }

    private JPanel buildActiveDeckDraftPanel() {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setOpaque(false);

        JLabel title = new JLabel("Selected Active Deck Draft", SwingConstants.CENTER);
        title.setFont(new Font("Dialog", Font.BOLD, Math.max(15, (int) (18 * (cardSize / 275.0)))));
        title.setForeground(new Color(20, 35, 65));
        panel.add(title, BorderLayout.NORTH);

        JPanel cardGrid = new JPanel(new GridLayout(0, 5, 18, 18));
        cardGrid.setBackground(new Color(255, 253, 245));
        cardGrid.setBorder(new EmptyBorder(8, 8, 8, 8));

        int imageWidth = getActiveDeckManagerSelectedCardImageWidth();
        int maxSize = campaign.getActiveDeckMaxSize();
        for (int i = 0; i < maxSize; i++) {
            if (activeDeckDraft != null && i < activeDeckDraft.size()) {
                President president = activeDeckDraft.get(i);
                ImageIcon icon = loadScaledIcon(president.getImageURL(), imageWidth, getCardImageHeight(imageWidth));
                cardGrid.add(buildActiveDeckDraftCardButton(president, imageWidth, icon));
            } else {
                cardGrid.add(buildActiveDeckEmptySlotPanel(i + 1, imageWidth));
            }
        }

        panel.add(cardGrid, BorderLayout.CENTER);
        return panel;
    }

    private JPanel buildActiveDeckOwnedCollectionPanel(List<CardIconData> loadedOwnedCards, int columns) {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setOpaque(false);

        JLabel title = new JLabel("Owned Collection - Click Image to Add/Remove", SwingConstants.CENTER);
        title.setFont(new Font("Dialog", Font.BOLD, Math.max(15, (int) (18 * (cardSize / 275.0)))));
        title.setForeground(new Color(20, 35, 65));
        panel.add(title, BorderLayout.NORTH);

        if (loadedOwnedCards == null || loadedOwnedCards.isEmpty()) {
            panel.add(makeCenteredDisplayMessage("No owned cards yet. Open packs first."), BorderLayout.CENTER);
            return panel;
        }

        JPanel cardGrid = new JPanel(new GridLayout(0, columns, 20, 20));
        cardGrid.setBackground(new Color(255, 253, 245));
        cardGrid.setBorder(new EmptyBorder(8, 8, 8, 8));

        for (CardIconData cardData : loadedOwnedCards) {
            cardGrid.add(buildActiveDeckCollectionCardButton(cardData));
        }

        panel.add(cardGrid, BorderLayout.CENTER);
        return panel;
    }

    private String buildPresidentRatingToolTip(President president) {
        if (president == null) {
            return "";
        }
        return president.getName() + " | Avg: " + String.format("%.2f", president.getAv());
    }

    private JPanel buildActiveDeckDraftCardButton(President president, int imageWidth, ImageIcon icon) {
        JPanel cardPanel = new JPanel(new BorderLayout(0, 4));
        cardPanel.setOpaque(false);

        JButton imageButton = buildCardImageButton(president, imageWidth, icon);
        imageButton.setToolTipText(buildPresidentRatingToolTip(president));
        imageButton.addActionListener(e -> removePresidentFromActiveDeckDraft(president));
        cardPanel.add(imageButton, BorderLayout.CENTER);

        JLabel actionLabel = new JLabel("Remove", SwingConstants.CENTER);
        actionLabel.setFont(new Font("Dialog", Font.BOLD, Math.max(11, (int) (13 * (cardSize / 275.0)))));
        actionLabel.setForeground(new Color(110, 50, 45));
        cardPanel.add(actionLabel, BorderLayout.SOUTH);

        return cardPanel;
    }

    private JPanel buildActiveDeckCollectionCardButton(CardIconData cardData) {
        President president = cardData.getPresident();
        boolean inDraft = isPresidentInActiveDeckDraft(president);

        JPanel cardPanel = new JPanel(new BorderLayout(0, 4));
        cardPanel.setOpaque(false);
        cardPanel.setBorder(BorderFactory.createLineBorder(
                inDraft ? new Color(30, 95, 45) : new Color(180, 160, 120),
                inDraft ? 3 : 1
        ));

        JButton imageButton = buildCardImageButton(president, cardData.getImageWidth(), cardData.getIcon());
        imageButton.setToolTipText(buildPresidentRatingToolTip(president));
        imageButton.addActionListener(e -> togglePresidentInActiveDeckDraft(president));
        cardPanel.add(imageButton, BorderLayout.CENTER);

        JLabel actionLabel = new JLabel(inDraft ? "In Deck" : "Add", SwingConstants.CENTER);
        actionLabel.setFont(new Font("Dialog", Font.BOLD, Math.max(11, (int) (13 * (cardSize / 275.0)))));
        actionLabel.setForeground(inDraft ? new Color(30, 95, 45) : new Color(20, 35, 65));
        cardPanel.add(actionLabel, BorderLayout.SOUTH);

        return cardPanel;
    }

    private JButton buildCardImageButton(President president, int imageWidth, ImageIcon icon) {
        int imageHeight = getCardImageHeight(imageWidth);
        JButton imageButton = new JButton();
        imageButton.setFocusPainted(false);
        imageButton.setContentAreaFilled(false);
        imageButton.setOpaque(false);
        imageButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        imageButton.setPreferredSize(new Dimension(imageWidth + 2, imageHeight + 2));
        imageButton.setBorder(BorderFactory.createLineBorder(new Color(120, 100, 70), 1));

        if (icon != null) {
            imageButton.setIcon(icon);
        } else {
            imageButton.setText("<html><center>Image not found<br>" + escapeHtml(president.getName()) + "</center></html>");
            imageButton.setFont(new Font("Dialog", Font.BOLD, Math.max(10, (int) (12 * (cardSize / 275.0)))));
        }
        return imageButton;
    }

    private JPanel buildActiveDeckEmptySlotPanel(int slotNumber, int imageWidth) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        panel.setPreferredSize(new Dimension(imageWidth + 4, getCardImageHeight(imageWidth) + 4));
        panel.setBorder(BorderFactory.createDashedBorder(new Color(170, 150, 115), 3, 4));

        JLabel label = new JLabel("<html><center>Empty Slot<br>" + slotNumber + "</center></html>", SwingConstants.CENTER);
        label.setFont(new Font("Dialog", Font.BOLD, Math.max(12, (int) (14 * (cardSize / 275.0)))));
        label.setForeground(new Color(110, 95, 70));
        panel.add(label, BorderLayout.CENTER);
        return panel;
    }

    private void resetActiveDeckDraftFromCampaign() {
        if (campaign == null) {
            activeDeckDraft = new ArrayList<President>();
            return;
        }
        activeDeckDraft = new ArrayList<President>(campaign.getActiveDeck());
    }

    private void ensureActiveDeckDraftInitialized() {
        if (activeDeckDraft == null) {
            activeDeckDraft = new ArrayList<President>();
        }
    }

    private boolean isPresidentInActiveDeckDraft(President president) {
        if (president == null || activeDeckDraft == null) {
            return false;
        }
        for (President draftCard : activeDeckDraft) {
            if (draftCard != null && draftCard.getName().equalsIgnoreCase(president.getName())) {
                return true;
            }
        }
        return false;
    }

    private void togglePresidentInActiveDeckDraft(President president) {
        if (isPresidentInActiveDeckDraft(president)) {
            removePresidentFromActiveDeckDraft(president);
        } else {
            addPresidentToActiveDeckDraft(president);
        }
    }

    private void addPresidentToActiveDeckDraft(President president) {
        if (president == null || campaign == null) {
            return;
        }
        ensureActiveDeckDraftInitialized();
        if (isPresidentInActiveDeckDraft(president)) {
            return;
        }
        if (activeDeckDraft.size() >= campaign.getActiveDeckMaxSize()) {
            showShortDialog(
                    "Deck Full",
                    "Your custom Active Deck draft already has " + campaign.getActiveDeckMaxSize()
                            + " Presidents. Remove someone first.",
                    true
            );
            return;
        }
        activeDeckDraft.add(president);
        refreshActiveDeckCards();
    }

    private void removePresidentFromActiveDeckDraft(President president) {
        if (president == null || activeDeckDraft == null) {
            return;
        }
        for (int i = activeDeckDraft.size() - 1; i >= 0; i--) {
            President draftCard = activeDeckDraft.get(i);
            if (draftCard != null && draftCard.getName().equalsIgnoreCase(president.getName())) {
                activeDeckDraft.remove(i);
            }
        }
        refreshActiveDeckCards();
    }

    private void autoBuildBestActiveDeckDraft() {
        if (campaign == null) {
            return;
        }
        activeDeckDraft = new ArrayList<President>(campaign.getBestOwnedPresidents(campaign.getActiveDeckMaxSize()));
        refreshActiveDeckCards();
    }

    private void clearActiveDeckDraft() {
        activeDeckDraft = new ArrayList<President>();
        refreshActiveDeckCards();
    }

    private void setAutoBestActiveDeckFromUI() {
        if (campaign == null) {
            return;
        }
        OperationResult result = captureBooleanOutput(() -> campaign.setActiveDeckAutoMode());
        appendLog(result.output);
        refreshDashboard();
        resetActiveDeckDraftFromCampaign();
        refreshActiveDeckCards();
        showShortDialog(
                result.success ? "Auto-Best Enabled" : "Deck Save Failed",
                result.success
                        ? "Quick matches will use your best 10 owned Presidents automatically."
                        : "Could not save Auto-Best deck mode.",
                !result.success
        );
    }

    private void saveCustomActiveDeckDraft() {
        if (campaign == null) {
            return;
        }
        ensureActiveDeckDraftInitialized();
        if (activeDeckDraft.size() != campaign.getActiveDeckMaxSize()) {
            showShortDialog(
                    "Custom Deck Incomplete",
                    "A custom Active Deck needs exactly " + campaign.getActiveDeckMaxSize()
                            + " Presidents. Current draft: " + activeDeckDraft.size()
                            + "/" + campaign.getActiveDeckMaxSize() + ".",
                    true
            );
            return;
        }

        final List<President> savedDraft = new ArrayList<President>(activeDeckDraft);
        OperationResult result = captureBooleanOutput(() -> campaign.setCustomActiveDeck(savedDraft));
        appendLog(result.output);
        refreshDashboard();
        resetActiveDeckDraftFromCampaign();
        refreshActiveDeckCards();
        showShortDialog(
                result.success ? "Custom Deck Saved" : "Deck Save Failed",
                result.success
                        ? "Quick matches will use this custom Active Deck. Tournament decks remain auto-built by era."
                        : "Could not save that custom Active Deck.",
                !result.success
        );
    }

    private double calculateDeckStrength(List<President> deck) {
        if (deck == null || deck.isEmpty()) {
            return 0.0;
        }

        List<President> sortedDeck = new ArrayList<President>(deck);
        Collections.sort(sortedDeck, presidentAverageComparator());

        int topCount = Math.min(5, sortedDeck.size());
        double topFiveTotal = 0.0;
        for (int i = 0; i < topCount; i++) {
            topFiveTotal += sortedDeck.get(i).getAv();
        }

        double fullTotal = 0.0;
        for (President president : sortedDeck) {
            if (president != null) {
                fullTotal += president.getAv();
            }
        }

        double topFiveAverage = topCount == 0 ? 0.0 : topFiveTotal / topCount;
        double fullAverage = sortedDeck.isEmpty() ? 0.0 : fullTotal / sortedDeck.size();
        return (0.30 * topFiveAverage) + (0.70 * fullAverage);
    }

    private int getActiveDeckManagerSelectedCardImageWidth() {
        int frameWidth = frame == null ? 1600 : frame.getWidth();
        int availableWidth = Math.max(900, frameWidth - 160);
        int widthFromScreen = (availableWidth - 4 * 18 - 36) / 5;
        int widthFromScale = (int) (176 * (cardSize / 275.0));
        return Math.max(126, Math.min(widthFromScreen, Math.max(162, widthFromScale)));
    }

    private int getActiveDeckManagerCollectionCardImageWidth() {
        int frameWidth = frame == null ? 1600 : frame.getWidth();
        int availableWidth = Math.max(700, frameWidth - 120);
        int widthFromScreen = (availableWidth - 5 * 20 - 36) / 6;
        int widthFromScale = (int) (205 * (cardSize / 275.0));
        return Math.max(145, Math.min(widthFromScreen, Math.max(195, widthFromScale)));
    }

    private int getActiveDeckManagerCollectionColumnCount(int imageWidth) {
        int frameWidth = frame == null ? 1600 : frame.getWidth();
        int availableWidth = Math.max(700, frameWidth - 120);
        int columns = availableWidth / Math.max(1, imageWidth + 24);
        return Math.max(3, Math.min(6, columns));
    }

    private JLabel makeCenteredDisplayMessage(String message) {
        JLabel label = new JLabel("", SwingConstants.CENTER);
        setCenteredDisplayMessage(label, message);
        label.setFont(new Font("Dialog", Font.BOLD, Math.max(18, (int) (22 * (cardSize / 275.0)))));
        label.setForeground(new Color(90, 80, 65));
        return label;
    }

    private void setCenteredDisplayMessage(JLabel label, String message) {
        if (label == null) {
            return;
        }
        String safeMessage = escapeHtml(message == null ? "" : message).replace("\n", "<br>");
        label.setText("<html><center>" + safeMessage + "</center></html>");
    }

    private String buildLoadingImagesMessage(String label, int loaded, int total) {
        int safeTotal = Math.max(0, total);
        int safeLoaded = Math.max(0, Math.min(loaded, safeTotal));
        int pct = safeTotal == 0 ? 100 : (int) Math.round(safeLoaded * 100.0 / safeTotal);
        return label + "... " + safeLoaded + "/" + safeTotal + " (" + pct + "%)";
    }

    private int getCardImageHeight(int imageWidth) {
        return (int) Math.round(1.32713755 * imageWidth);
    }

    private int getLargeCardImageWidth() {
        int frameWidth = frame == null ? 1600 : frame.getWidth();
        int availableWidth = Math.max(900, frameWidth - 120);

        // Active deck is 5 columns. Use the available width, but cap it so the
        // cards are big without becoming cartoonishly huge on ultrawide screens.
        int widthFromScreen = (availableWidth - 4 * 18 - 28) / 5;
        int widthFromScale = (int) (238 * (cardSize / 275.0));
        return Math.max(168, Math.min(widthFromScreen, Math.max(236, widthFromScale)));
    }

    private int getCollectionCardImageWidth() {
        int frameWidth = frame == null ? 1600 : frame.getWidth();
        int availableWidth = Math.max(700, frameWidth - 120);

        // Collection is capped at 6 columns so cards stay large enough to read.
        // Use the screen width as a soft cap, then scale with the same global
        // cardSize factor used elsewhere.
        int widthFromScreen = (availableWidth - 5 * 20 - 36) / 6;
        int widthFromScale = (int) (225 * (cardSize / 275.0));
        return Math.max(155, Math.min(widthFromScreen, Math.max(215, widthFromScale)));
    }

    private int getPackResultCardImageWidth() {
        int widthFromScale = (int) (185 * (cardSize / 275.0));
        return Math.max(135, Math.min(225, widthFromScale));
    }

    private int getPackResultColumnCount(int imageWidth, int cardCount) {
        int frameWidth = frame == null ? 1600 : frame.getWidth();
        int availableWidth = Math.max(700, frameWidth - 120);
        int columns = availableWidth / Math.max(1, imageWidth + 24);
        columns = Math.max(1, Math.min(5, columns));
        return Math.max(1, Math.min(cardCount, columns));
    }

    private int getCollectionColumnCount(int imageWidth) {
        int frameWidth = frame == null ? 1600 : frame.getWidth();
        int availableWidth = Math.max(700, frameWidth - 120);
        int columns = availableWidth / Math.max(1, imageWidth + 24);
        return Math.max(3, Math.min(6, columns));
    }

    private JPanel buildPresidentCardPanel(President president, int imageWidth) {
        return buildPresidentCardPanel(president, imageWidth, null, true);
    }

    private JPanel buildPresidentCardPanel(President president, int imageWidth, ImageIcon preloadedIcon) {
        return buildPresidentCardPanel(president, imageWidth, preloadedIcon, true);
    }

    private JPanel buildPresidentCardPanel(
            President president, int imageWidth, ImageIcon preloadedIcon, boolean owned
    ) {
        JPanel cardPanel = new JPanel(new BorderLayout(0, 4));
        cardPanel.setOpaque(false);

        int imageHeight = getCardImageHeight(imageWidth);

        JLabel imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setVerticalAlignment(SwingConstants.CENTER);
        imageLabel.setToolTipText((owned ? "" : "Missing | ")
                + president.getName() + " | Avg: " + String.format("%.2f", president.getAv()));

        ImageIcon presidentIcon = preloadedIcon;
        if (presidentIcon == null) {
            presidentIcon = owned
                    ? loadScaledIcon(president.getImageURL(), imageWidth, imageHeight)
                    : loadMissingScaledIcon(president.getImageURL(), imageWidth, imageHeight);
        }

        if (presidentIcon != null) {
            imageLabel.setIcon(presidentIcon);
            if (!owned) {
                imageLabel.setBorder(BorderFactory.createLineBorder(new Color(115, 115, 115), 2));
            }
        } else {
            imageLabel.setText("<html><center>Image not found<br>" + escapeHtml(president.getName()) + "</center></html>");
            imageLabel.setPreferredSize(new Dimension(imageWidth, imageHeight));
            imageLabel.setBorder(BorderFactory.createLineBorder(
                    owned ? new Color(120, 100, 70) : new Color(115, 115, 115), 1
            ));
            imageLabel.setFont(new Font("Dialog", Font.BOLD, Math.max(10, (int) (12 * (cardSize / 275.0)))));
        }

        addCollectionCardZoomListener(imageLabel, president, owned);
        cardPanel.add(imageLabel, BorderLayout.CENTER);
        if (!owned) {
            JLabel missingLabel = new JLabel("Missing", SwingConstants.CENTER);
            missingLabel.setFont(new Font("Dialog", Font.BOLD, Math.max(11, (int) (13 * (cardSize / 275.0)))));
            missingLabel.setForeground(new Color(95, 95, 95));
            cardPanel.add(missingLabel, BorderLayout.SOUTH);
        }
        return cardPanel;
    }

    private void addCollectionCardZoomListener(JLabel imageLabel, President president, boolean owned) {
        if (imageLabel == null || president == null) {
            return;
        }

        imageLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        imageLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e) && showingCollectionCards) {
                    displayCollectionCardZoom(president, owned);
                }
            }
        });
    }

    private void displayCollectionCardZoom(President president, boolean owned) {
        if (displayPanel == null || president == null) {
            return;
        }

        cancelCollectionLoadWorker();
        showingActiveDeckCards = false;
        showingCollectionCards = true;
        displayTitleLabel.setText("Card Detail - " + president.getName());

        displayPanel.removeAll();
        displayPanel.setLayout(new BorderLayout());
        displayPanel.setBackground(new Color(255, 253, 245));

        JPanel wrapper = new JPanel(new BorderLayout(12, 12));
        wrapper.setOpaque(false);
        wrapper.setBorder(new EmptyBorder(10, 14, 12, 14));
        wrapper.add(buildRosterTabPanel("Collection"), BorderLayout.NORTH);

        JPanel topPanel = new JPanel(new BorderLayout(8, 8));
        topPanel.setOpaque(false);

        JLabel titleLabel = new JLabel(
                "<html><center><b>" + escapeHtml(president.getName()) + "</b><br>"
                        + (owned ? "Owned Card" : "Missing Card")
                        + "</center></html>",
                SwingConstants.CENTER
        );
        titleLabel.setFont(new Font("Dialog", Font.BOLD, Math.max(18, (int) (22 * (cardSize / 275.0)))));
        titleLabel.setForeground(new Color(20, 35, 65));
        topPanel.add(titleLabel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        buttonPanel.setOpaque(false);
        JButton backButton = makeDashboardButton("Back");
        backButton.addActionListener(e -> displayCollectionCards());
        buttonPanel.add(backButton);
        topPanel.add(buttonPanel, BorderLayout.EAST);

        wrapper.add(topPanel, BorderLayout.NORTH);

        JPanel detailPanel = new JPanel(new BorderLayout(18, 18));
        detailPanel.setOpaque(false);
        detailPanel.setBorder(new EmptyBorder(8, 24, 24, 24));

        int imageWidth = getZoomCardImageWidth();
        int imageHeight = getCardImageHeight(imageWidth);
        ImageIcon icon = owned
                ? loadScaledIcon(president.getImageURL(), imageWidth, imageHeight)
                : loadMissingScaledIcon(president.getImageURL(), imageWidth, imageHeight);

        JLabel imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        imageLabel.setVerticalAlignment(SwingConstants.CENTER);
        if (icon != null) {
            imageLabel.setIcon(icon);
        } else {
            imageLabel.setText("<html><center>Image not found<br>" + escapeHtml(president.getName()) + "</center></html>");
            imageLabel.setPreferredSize(new Dimension(imageWidth, imageHeight));
            imageLabel.setBorder(BorderFactory.createLineBorder(new Color(120, 100, 70), 1));
            imageLabel.setFont(new Font("Dialog", Font.BOLD, Math.max(14, (int) (16 * (cardSize / 275.0)))));
        }
        detailPanel.add(imageLabel, BorderLayout.WEST);

        JTextArea details = new JTextArea(buildCardDetailText(president, owned));
        details.setEditable(false);
        details.setLineWrap(true);
        details.setWrapStyleWord(true);
        details.setOpaque(false);
        details.setFont(new Font("Dialog", Font.PLAIN, Math.max(16, (int) (18 * (cardSize / 275.0)))));
        details.setForeground(new Color(45, 45, 45));
        details.setBorder(new EmptyBorder(4, 8, 4, 8));
        detailPanel.add(details, BorderLayout.CENTER);

        wrapper.add(detailPanel, BorderLayout.CENTER);
        displayPanel.add(wrapper, BorderLayout.NORTH);
        updateDisplayPanel();
    }

    private String buildCardDetailText(President president, boolean owned) {
        StringBuilder text = new StringBuilder();
        text.append(owned ? "Owned" : "Missing").append("\n\n");
        text.append("Average: ").append(String.format("%.2f", president.getAv())).append("\n");
        text.append("Value: ").append(String.format("%.0f", president.getValue())).append(" PC").append("\n\n");
        text.append("EXP: ").append(president.getExp()).append("    ");
        text.append("INF: ").append(president.getInfl()).append("\n");
        text.append("POL: ").append(president.getPol()).append("    ");
        text.append("NAM: ").append(president.getNam()).append("\n\n");
        text.append("Region: ").append(blankToDash(president.getRegion())).append("\n");
        text.append("Era: ").append(blankToDash(president.getEra())).append("\n");
        text.append("Party: ").append(blankToDash(president.getParty())).append("\n");
        text.append("State: ").append(blankToDash(president.getState())).append("\n");
        text.append("Ideology: ").append(blankToDash(president.getIdeology())).append("\n");
        text.append("Marquee: ").append(blankToDash(president.getMarquee())).append("\n\n");
        text.append("Tags: ").append(buildTagsText(president)).append("\n\n");
        text.append(president.getCardInfo());
        return text.toString();
    }

    private String blankToDash(String text) {
        if (text == null || text.trim().isEmpty()) {
            return "—";
        }
        return text.trim();
    }

    private String buildTagsText(President president) {
        if (president == null || president.getTags() == null || president.getTags().length == 0) {
            return "—";
        }
        return String.join(", ", president.getTags());
    }

    private int getZoomCardImageWidth() {
        int frameHeight = frame == null ? 900 : frame.getHeight();
        int frameWidth = frame == null ? 1600 : frame.getWidth();
        int heightBasedWidth = (int) ((Math.max(500, frameHeight - 260)) / 1.32713755);
        int widthBasedWidth = Math.max(260, (frameWidth - 260) / 3);
        int widthFromScale = (int) (360 * (cardSize / 275.0));
        return Math.max(260, Math.min(Math.min(heightBasedWidth, widthBasedWidth), Math.max(330, widthFromScale)));
    }

    private void updateDisplayPanel() {
        displayPanel.revalidate();
        displayPanel.repaint();
        if (displayScrollPane != null) {
            SwingUtilities.invokeLater(() -> displayScrollPane.getVerticalScrollBar().setValue(0));
        }
    }

    private String escapeHtml(String text) {
        if (text == null) {
            return "";
        }
        return text.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;");
    }

    private void saveCampaignFromUI() {
        OperationResult result = captureBooleanOutput(() -> campaign.saveCampaign());
        appendLog(result.output);
        refreshDashboard();
        showShortDialog(result.success ? "Campaign Saved" : "Save Failed",
                result.output == null || result.output.trim().isEmpty()
                        ? "Campaign saved."
                        : result.output.trim(),
                !result.success);
    }

    private void changeCampaignPlayerNameFromUI() {
        if (campaign == null || campaign.getPlayer() == null) {
            return;
        }

        String currentName = campaign.getPlayer().getName();
        String newName = CustomDialog.showInputDialog(
                frame,
                "Current campaign name: " + currentName + "\n\nEnter the new campaign name:",
                "files/settingstitle.PNG"
        );

        if (newName == null) {
            return;
        }
        newName = newName.trim();
        if (newName.isEmpty()) {
            showShortDialog("Name Not Changed", "Campaign name cannot be blank.", true);
            return;
        }

        campaign.getPlayer().setName(newName);
        OperationResult result = captureBooleanOutput(() -> campaign.saveCampaign());
        appendLog(result.output);
        refreshDashboard();
        showShortDialog(
                result.success ? "Name Changed" : "Name Changed, Save Failed",
                result.success
                        ? "Campaign name changed to " + newName + "."
                        : "Campaign name changed to " + newName + ", but the save failed.",
                !result.success
        );
    }

    private void restartCampaignFromUI() {
        int confirm = showChoiceDialog(
                "Restarting will permanently overwrite your current Campaign Mode save.\n\nAre you absolutely sure?",
                "files/settingstitle.PNG",
                new String[] { "Yes, Restart", "No" },
                "settings",
                1.0,
                1.2
        );
        if (confirm != 0) {
            return;
        }

        boolean started = startNewCampaignFromDialog();
        if (started) {
            refreshDashboard();
            displayActiveDeckCards();
        }
    }

    private void confirmSaveAndQuit() {
        if (navigationHost != null) {
            confirmEmbeddedSaveAndQuit();
            return;
        }

        int choice = showChoiceDialog(
                "What would you like to do?",
                "files/settingstitle.PNG",
                new String[] { "Save", "Change Name", "Load Campaign", "Save and Quit", getSimTestMenuLabel(), "Restart Campaign", "Cancel" },
                "settings",
                1.0,
                1.85
        );

        if (choice == 6 || choice == -1) {
            return;
        }

        if (choice == 0) {
            saveCampaignFromUI();
            return;
        }

        if (choice == 1) {
            changeCampaignPlayerNameFromUI();
            return;
        }

        if (choice == 2) {
            loadCampaignFromMenu();
            return;
        }

        if (choice == 4) {
            toggleSimTestModeFromMenu();
            return;
        }

        if (choice == 5) {
            restartCampaignFromUI();
            return;
        }

        if (choice == 3) {
            if (!saveCampaignBeforeNavigation()) {
                return;
            }
        }

        if (ownsFrame && frame != null) {
            frame.dispose();
        }
    }

    private void confirmEmbeddedSaveAndQuit() {
        int choice = showChoiceDialog(
                "Campaign Mode Menu",
                "files/settingstitle.PNG",
                new String[] {
                        "Save",
                        "Change Name",
                        "Return Home",
                        "Save + Quit",
                        getSimTestMenuLabel(),
                        "Load Campaign",
                        "Restart",
                        "Cancel"
                },
                "settings",
                1.0,
                1.75
        );

        if (choice == 7 || choice == -1) {
            return;
        }

        if (choice == 0) {
            saveCampaignFromUI();
            return;
        }

        if (choice == 1) {
            changeCampaignPlayerNameFromUI();
            return;
        }

        if (choice == 4) {
            toggleSimTestModeFromMenu();
            return;
        }

        if (choice == 5) {
            loadCampaignFromMenu();
            return;
        }

        if (choice == 6) {
            restartCampaignFromUI();
            return;
        }

        // Return Home and Quit both save first by design.
        if (choice == 2 || choice == 3) {
            if (!saveCampaignBeforeNavigation()) {
                return;
            }
        }

        if (choice == 2) {
            returnToMainHomeScreen();
        } else if (choice == 3) {
            exitFullApplication();
        }
    }


    private void loadCampaignFromMenu() {
        if (!saveCampaignBeforeNavigation()) {
            return;
        }

        boolean loaded = showLoadCampaignDialog(false);
        if (loaded) {
            refreshDashboard();
            displayActiveDeckCards();
        }
    }

    private boolean saveCampaignBeforeNavigation() {
        OperationResult result = captureBooleanOutput(() -> campaign.saveCampaign());
        appendLog(result.output);
        if (result.success) {
            return true;
        }

        int continueAnyway = showChoiceDialog(
                "Save failed. Continue anyway?",
                "files/errortitle.PNG",
                new String[] { "Yes", "No" },
                "error",
                0.85,
                1.0
        );
        return continueAnyway == 0;
    }

    private boolean confirmNavigationWithoutSaving(boolean returningHome) {
        String destination = returningHome ? "return to the Campaign Clash home screen" : "quit the game";
        int confirm = showChoiceDialog(
                "Continue without saving and " + destination + "?",
                "files/settingstitle.PNG",
                new String[] { "Yes", "No" },
                "settings",
                0.9,
                1.2
        );
        return confirm == 0;
    }

    private void returnToMainHomeScreen() {
        stopPlayTimeUiTimer(true);
        if (navigationHost != null) {
            navigationHost.showMainHomeScreen();
            return;
        }
        if (ownsFrame && frame != null) {
            frame.dispose();
        }
    }

    private void exitFullApplication() {
        stopPlayTimeUiTimer(true);
        if (navigationHost != null) {
            navigationHost.exitApplication();
            return;
        }
        if (ownsFrame && frame != null) {
            frame.dispose();
        }
    }

    private void startPlayTimeUiTimer() {
        stopPlayTimeUiTimer(false);
        playTimeUiTimer = new Timer(60000, e -> {
            if (campaign != null) {
                campaign.flushPlayTimeToPlayer();
            }
            refreshPlayTimeLabel();
        });
        playTimeUiTimer.setInitialDelay(60000);
        playTimeUiTimer.start();
        refreshPlayTimeLabel();
    }

    private void stopPlayTimeUiTimer(boolean flushTime) {
        if (flushTime && campaign != null) {
            campaign.flushPlayTimeToPlayer();
        }
        if (playTimeUiTimer != null) {
            playTimeUiTimer.stop();
            playTimeUiTimer = null;
        }
    }

    private void refreshPlayTimeLabel() {
        if (activeDeckLabel == null) {
            return;
        }
        if (campaign == null || campaign.getPlayer() == null) {
            activeDeckLabel.setText("Time Played: ");
            return;
        }
        activeDeckLabel.setText("Time Played: " + campaign.getFormattedPlayTime());
    }

    private void refreshDashboard() {
        if (campaign == null || campaign.getPlayer() == null) {
            nameLabel.setText("Name: ");
            pcLabel.setText("Political Capital: ");
            recordLabel.setText("Record: ");
            collectionLabel.setText("Collection: ");
            activeDeckLabel.setText("Time Played: ");
            deckStrengthLabel.setText("Active Deck Strength: ");
            return;
        }

        CampaignPlayer player = campaign.getPlayer();
        int totalPresidents = campaign.getTotalPresidentCount();
        int ownedPresidents = player.getCollectionSize();
        double completionPct = totalPresidents == 0 ? 0.0 : ownedPresidents * 100.0 / totalPresidents;

        nameLabel.setText("Name: " + player.getName());
        pcLabel.setText("Political Capital: " + CampaignMode.formatPC(player.getPC()));
        recordLabel.setText("Record: " + player.getRecordSummary());
        collectionLabel.setText("Collection: " + ownedPresidents + "/" + totalPresidents
                + " (" + String.format("%.1f", completionPct) + "%)");
        refreshPlayTimeLabel();
        deckStrengthLabel.setText("Active Deck Strength: "
                + String.format("%.2f", campaign.getActiveDeckStrength()));

        if (showingActiveDeckCards) {
            refreshActiveDeckCards();
        } else if (showingCollectionCards) {
            refreshCollectionCards();
        }
    }

    private String buildStatusText() {
        if (campaign == null || campaign.getPlayer() == null) {
            return "No campaign loaded.";
        }

        CampaignPlayer player = campaign.getPlayer();
        int totalPresidents = campaign.getTotalPresidentCount();
        int ownedPresidents = player.getCollectionSize();
        double completionPct = totalPresidents == 0 ? 0.0 : ownedPresidents * 100.0 / totalPresidents;

        return "Status\n"
                + "------------------------------\n"
                + "Name: " + player.getName() + "\n"
                + "Political Capital: " + CampaignMode.formatPC(player.getPC()) + "\n"
                + "Record: " + player.getRecordSummary() + "\n"
                + "Time Played: " + campaign.getFormattedPlayTime() + "\n"
                + "Presidents owned: " + ownedPresidents + "/" + totalPresidents
                + " (" + String.format("%.1f", completionPct) + "%)\n"
                + "Active Deck: " + campaign.getActiveDeckModeLabel() + " " + campaign.getActiveDeckSize() + "/"
                + campaign.getActiveDeckMaxSize() + "\n"
                + "Active Deck Strength: "
                + String.format("%.2f", campaign.getActiveDeckStrength()) + "\n"
                + "Easy rewards: " + CampaignMode.formatPC(campaign.getEasyWinRewardPC())
                + " win / " + CampaignMode.formatPC(campaign.getEasyLossRewardPC()) + " loss\n"
                + "Medium rewards: " + CampaignMode.formatPC(campaign.getMediumWinRewardPC())
                + " win / " + CampaignMode.formatPC(campaign.getMediumLossRewardPC()) + " loss\n"
                + "Hard rewards: " + CampaignMode.formatPC(campaign.getHardWinRewardPC())
                + " win / " + CampaignMode.formatPC(campaign.getHardLossRewardPC()) + " loss\n"
                + "Active Save Slot: " + campaign.getActiveSaveSlot()
                + " (" + campaign.getActiveSaveFilePath() + ")";
    }

    /**
     * Scrollable text windows stay as simple JDialogs because CustomDialog's
     * message label is not meant for huge collection/deck lists.
     */
    private void showLongTextDialog(String title, String text) {
        JDialog dialog = new JDialog(frame, title, true);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.setSize(Math.max(760, (int) (850 * (cardSize / 275.0))),
                Math.max(520, (int) (600 * (cardSize / 275.0))));
        dialog.setLocationRelativeTo(frame);

        JTextArea textArea = new JTextArea(text == null || text.isEmpty() ? "(No output)" : text);
        textArea.setEditable(false);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, Math.max(13, (int) (14 * (cardSize / 275.0)))));
        textArea.setBackground(new Color(255, 253, 245));
        textArea.setBorder(new EmptyBorder(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(180, 160, 120), 2));
        dialog.add(scrollPane, BorderLayout.CENTER);

        JButton okButton = makeDialogCloseButton("Ok");
        okButton.addActionListener(e -> dialog.dispose());
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(244, 239, 225));
        buttonPanel.add(okButton);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        installDialogIcon(dialog);
        dialog.setVisible(true);
    }

    private JButton makeDialogCloseButton(String text) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setFont(new Font("Dialog", Font.BOLD, Math.max(14, (int) (16 * (cardSize / 275.0)))));
        button.setPreferredSize(new Dimension(130, 42));
        return button;
    }

    private void showShortDialog(String title, String message, boolean error) {
        CustomDialog.showCustomDialog(
                frame,
                message,
                error ? "files/errortitle.PNG" : "files/settingstitle.PNG",
                new String[] { "Ok" },
                error ? "error" : "settings",
                0.85,
                1.15
        );
    }

    private int showChoiceDialog(
            String message, String titlePath, String[] options, String type,
            double heightRatio, double widthRatio
    ) {
        return CustomDialog.showCustomDialog(
                frame,
                message,
                titlePath,
                options,
                type,
                heightRatio,
                widthRatio
        );
    }

    private void appendLog(String text) {
        // RunCampaignMode no longer mirrors captured engine output to the console.
        // User-facing results are shown through dialogs and dashboard panels instead.
    }

    private OperationResult captureVoidOutput(VoidOperation operation) {
        return captureOutput(() -> {
            operation.run();
            return true;
        });
    }

    private OperationResult captureBooleanOutput(BooleanOperation operation) {
        return captureOutput(operation);
    }

    private OperationResult captureOutput(BooleanOperation operation) {
        PrintStream originalOut = System.out;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        PrintStream capture = new PrintStream(buffer);

        boolean success = false;
        try {
            System.setOut(capture);
            success = operation.run();
        } catch (RuntimeException e) {
            e.printStackTrace(capture);
            success = false;
        } finally {
            capture.flush();
            System.setOut(originalOut);
        }

        return new OperationResult(success, buffer.toString());
    }

    private void setActionButtonsEnabled(boolean enabled) {
        for (JButton button : actionButtons) {
            button.setEnabled(enabled);
        }
    }

    private boolean saveFileExists() {
        return campaign != null && campaign.hasAnySaveSlot();
    }

    private void installFrameIconAndCursor() {
        installDialogIcon(frame);

        try {
            URL cursorUrl = getClass().getClassLoader().getResource(PREFIX + "files/mouse.PNG");
            if (cursorUrl != null) {
                Image cursorImage = Toolkit.getDefaultToolkit().getImage(cursorUrl);
                Image scaledCursorImage = cursorImage.getScaledInstance(
                        Math.max(48, (int) (80 * (cardSize / 275.0))),
                        Math.max(48, (int) (80 * (cardSize / 275.0))),
                        Image.SCALE_SMOOTH
                );
                Cursor customCursor = Toolkit.getDefaultToolkit().createCustomCursor(
                        scaledCursorImage,
                        new Point(0, 0),
                        "Campaign Cursor"
                );
                frame.setCursor(customCursor);
            }
        } catch (Exception ignored) {
            // Cursor is cosmetic; keep running if it fails.
        }
    }

    private void installDialogIcon(java.awt.Window window) {
        try {
            URL logoUrl = getClass().getClassLoader().getResource(PREFIX + "files/logo.PNG");
            if (logoUrl != null) {
                Image logoImage = new ImageIcon(logoUrl).getImage();
                if (window instanceof JFrame) {
                    ((JFrame) window).setIconImage(logoImage);
                } else if (window instanceof JDialog) {
                    ((JDialog) window).setIconImage(logoImage);
                }
            }
        } catch (Exception ignored) {
            // Icon is cosmetic; keep running if it fails.
        }
    }

    private void applyFullScreenSizing() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setSize(screenSize.width, screenSize.height);
        frame.setLocationRelativeTo(null);

        if (!System.getProperty("os.name").toLowerCase().contains("mac")) {
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        }
    }

    private ImageIcon loadScaledIcon(String path, int width, int height) {
        if (path == null || path.trim().isEmpty() || width <= 0 || height <= 0) {
            return null;
        }

        String cacheKey = buildIconCacheKey(path, width, height);
        synchronized (scaledIconCache) {
            ImageIcon cachedIcon = scaledIconCache.get(cacheKey);
            if (cachedIcon != null) {
                return cachedIcon;
            }
        }

        ImageIcon loadedIcon = loadScaledIconUncached(path, width, height);
        if (loadedIcon != null) {
            synchronized (scaledIconCache) {
                scaledIconCache.put(cacheKey, loadedIcon);
            }
        }
        return loadedIcon;
    }

    private String buildIconCacheKey(String path, int width, int height) {
        return path + "|" + width + "x" + height;
    }

    private ImageIcon loadScaledIconUncached(String path, int width, int height) {
        try {
            URL url = findResource(path);
            if (url == null) {
                return null;
            }

            Image originalImage = new ImageIcon(url).getImage();
            BufferedImage scaledImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            Graphics2D graphics = scaledImage.createGraphics();
            graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            graphics.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            graphics.drawImage(originalImage, 0, 0, width, height, null);
            graphics.dispose();
            return new ImageIcon(scaledImage);
        } catch (Exception e) {
            return null;
        }
    }

    private ImageIcon loadMissingScaledIcon(String path, int width, int height) {
        if (path == null || path.trim().isEmpty() || width <= 0 || height <= 0) {
            return null;
        }

        String cacheKey = buildIconCacheKey(path, width, height) + "|missing";
        synchronized (scaledIconCache) {
            ImageIcon cachedIcon = scaledIconCache.get(cacheKey);
            if (cachedIcon != null) {
                return cachedIcon;
            }
        }

        ImageIcon baseIcon = loadScaledIcon(path, width, height);
        if (baseIcon == null) {
            return null;
        }

        BufferedImage missingImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = missingImage.createGraphics();
        graphics.drawImage(baseIcon.getImage(), 0, 0, width, height, null);
        graphics.dispose();

        for (int y = 0; y < missingImage.getHeight(); y++) {
            for (int x = 0; x < missingImage.getWidth(); x++) {
                int argb = missingImage.getRGB(x, y);
                int alpha = (argb >> 24) & 0xff;
                if (alpha == 0) {
                    continue;
                }
                int red = (argb >> 16) & 0xff;
                int green = (argb >> 8) & 0xff;
                int blue = argb & 0xff;
                int gray = (red + green + blue) / 3;

                // Dim and flatten the image, but keep it readable enough that
                // the player can still recognize the missing card art.
                gray = Math.max(25, Math.min(210, (int) (gray * 0.58 + 35)));
                alpha = Math.max(90, (int) (alpha * 0.68));
                int newArgb = (alpha << 24) | (gray << 16) | (gray << 8) | gray;
                missingImage.setRGB(x, y, newArgb);
            }
        }

        ImageIcon missingIcon = new ImageIcon(missingImage);
        synchronized (scaledIconCache) {
            scaledIconCache.put(cacheKey, missingIcon);
        }
        return missingIcon;
    }

    private URL findResource(String path) {
        if (path == null || path.trim().isEmpty()) {
            return null;
        }

        String cleanPath = path.startsWith("/") ? path.substring(1) : path;
        String prefixedPath = PREFIX + cleanPath;

        URL url = getClass().getClassLoader().getResource(prefixedPath);
        if (url != null) {
            return url;
        }

        url = getClass().getClassLoader().getResource(cleanPath);
        if (url != null) {
            return url;
        }

        return getClass().getResource("/" + cleanPath);
    }

    private void playButtonSound() {
        if (soundtrackPlayer != null) {
            soundtrackPlayer.playSoundEffect(PREFIX + "files/playbutton.MP3");
        }
    }

    private interface FilterSelectionHandler {
        void onSelect(String value);
    }

    private interface VoidOperation {
        void run();
    }

    private interface BooleanOperation {
        boolean run();
    }

    private static class OperationResult {
        private final boolean success;
        private final String output;

        private OperationResult(boolean success, String output) {
            this.success = success;
            this.output = output == null ? "" : output;
        }
    }

    private static class CollectionProgressRow {
        private final String scopeType;
        private final String displayName;
        private final String scopeKey;
        private final int owned;
        private final int total;
        private final int required;
        private final double deckStrength;

        private CollectionProgressRow(
                String scopeType, String displayName, String scopeKey, int owned, int total,
                int required, double deckStrength
        ) {
            this.scopeType = scopeType == null ? "" : scopeType;
            this.displayName = displayName == null ? "" : displayName;
            this.scopeKey = scopeKey == null ? this.displayName : scopeKey;
            this.owned = Math.max(0, owned);
            this.total = Math.max(0, total);
            this.required = Math.max(0, required);
            this.deckStrength = Math.max(0.0, deckStrength);
        }

        private String getScopeType() {
            return scopeType;
        }

        private String getScopeKey() {
            return scopeKey;
        }

        private String getDisplayName() {
            return displayName;
        }

        private int getOwned() {
            return owned;
        }

        private int getTotal() {
            return total;
        }

        private int getRequired() {
            return required;
        }

        private double getDeckStrength() {
            return deckStrength;
        }

        private int getNeeded() {
            return Math.max(0, required - owned);
        }

        private boolean isEligible() {
            return owned >= required && required > 0;
        }

        private double getOwnedPct() {
            return total == 0 ? 0.0 : owned * 100.0 / total;
        }

        private String getTypeLabel() {
            if (COLLECTION_SCOPE_ERA.equals(scopeType)) {
                return "Era";
            }
            if (COLLECTION_SCOPE_TAG.equals(scopeType)) {
                return "Tag";
            }
            return scopeType;
        }

        private String getStatusText() {
            if (isEligible()) {
                return "Eligible";
            }
            return "Need " + getNeeded();
        }

        private String getStrengthText() {
            if (!isEligible()) {
                return "—";
            }
            return String.format("%.2f", deckStrength);
        }
    }

    private static class CollectionScope {
        private final String scopeType;
        private final String displayName;
        private final List<President> candidates;
        private final CampaignTournament tournament;

        private CollectionScope(String scopeType, String displayName, List<President> candidates) {
            this(scopeType, displayName, candidates, null);
        }

        private CollectionScope(
                String scopeType, String displayName, List<President> candidates, CampaignTournament tournament
        ) {
            this.scopeType = scopeType == null ? COLLECTION_SCOPE_ALL : scopeType;
            this.displayName = displayName == null ? COLLECTION_SCOPE_ALL : displayName;
            this.candidates = candidates == null
                    ? new ArrayList<President>()
                    : new ArrayList<President>(candidates);
            this.tournament = tournament;
        }

        private String getScopeType() {
            return scopeType;
        }

        private String getDisplayName() {
            return displayName;
        }

        private List<President> getCandidates() {
            return new ArrayList<President>(candidates);
        }

        private CampaignTournament getTournament() {
            return tournament;
        }
    }

    private static class CollectionCardItem {
        private final President president;
        private final boolean owned;

        private CollectionCardItem(President president, boolean owned) {
            this.president = president;
            this.owned = owned;
        }

        private President getPresident() {
            return president;
        }

        private boolean isOwned() {
            return owned;
        }
    }

    private static class CardIconData {
        private final President president;
        private final int imageWidth;
        private final ImageIcon icon;
        private final boolean owned;

        private CardIconData(President president, int imageWidth, ImageIcon icon) {
            this(president, imageWidth, icon, true);
        }

        private CardIconData(President president, int imageWidth, ImageIcon icon, boolean owned) {
            this.president = president;
            this.imageWidth = imageWidth;
            this.icon = icon;
            this.owned = owned;
        }

        private President getPresident() {
            return president;
        }

        private int getImageWidth() {
            return imageWidth;
        }

        private ImageIcon getIcon() {
            return icon;
        }

        private boolean isOwned() {
            return owned;
        }
    }

    /**
     * Tiny spacer helper so the title stays centered opposite the logo.
     */
    private static class BoxLikeSpacer extends JLabel {
        private static final long serialVersionUID = 1L;

        private BoxLikeSpacer(Dimension size) {
            setPreferredSize(size);
        }

        private static Component logoWidthSpacer(JLabel logo) {
            return new BoxLikeSpacer(logo.getPreferredSize());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new RunCampaignMode());
    }
}
