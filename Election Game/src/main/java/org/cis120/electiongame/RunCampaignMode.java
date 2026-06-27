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
import javax.swing.border.EmptyBorder;

/**
 * Swing UI for Campaign Mode.
 *
 * v8.0 adds Campaign Mode save slots. The normal opening flow keeps Continue
 * Campaign as the default path, while Load Campaign exposes the three-slot
 * save manager for testing and alternate campaigns.
 */
public class RunCampaignMode implements Runnable {

    private static final String PREFIX = RunElectionGameCombined.prefix;
    private static final Pattern NEW_CARD_PATTERN = Pattern.compile("^\\s*New card:\\s+(.+?)\\s+\\d+\\.\\d{2}\\s*$");
    private static final Pattern DUPLICATE_CARD_PATTERN = Pattern.compile("^\\s*Duplicate:\\s+(.+?)\\s+auto-sold.*$");

    private static final String COLLECTION_ERA_ALL = "All Eras";
    private static final String COLLECTION_OWNERSHIP_OWNED = "Owned";
    private static final String COLLECTION_OWNERSHIP_MISSING = "Missing";
    private static final String COLLECTION_OWNERSHIP_ALL = "All Cards";

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

    private JPanel displayPanel;
    private JLabel displayTitleLabel;
    private JScrollPane displayScrollPane;
    private boolean showingActiveDeckCards = false;
    private boolean showingCollectionCards = false;

    private java.util.List<JButton> actionButtons;

    private ImageIcon buttonIcon;
    private ImageIcon buttonHoverIcon;
    private ImageIcon buttonClickedIcon;

    // Scaled card images are expensive to load/resize. Cache by path + size so
    // collection/active-deck/store result views can reuse already-scaled icons.
    private final Map<String, ImageIcon> scaledIconCache = new HashMap<String, ImageIcon>();
    private SwingWorker<List<CardIconData>, Void> collectionLoadWorker;
    private long collectionLoadRequestId = 0;

    private String collectionEraFilterId = COLLECTION_ERA_ALL;
    private String collectionOwnershipFilter = COLLECTION_OWNERSHIP_OWNED;

    private CampaignTournament activeTournament;
    private List<President> activeTournamentDeck;

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
        activeDeckLabel = makeStatusLabel("Active Deck: ");
        deckStrengthLabel = makeStatusLabel("Deck Strength: ");

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

        JButton playMatchButton = makeButton("Play Match");
        playMatchButton.addActionListener(e -> displayMatchOptionsDashboard());

        JButton tournamentsButton = makeButton("Tournaments");
        tournamentsButton.addActionListener(e -> displayTournamentsDashboard());

        JButton packsButton = makeButton("Store");
        packsButton.addActionListener(e -> displayStoreDashboard());

        JButton collectionButton = makeButton("View Collection");
        collectionButton.addActionListener(e -> displayCollectionCards());

        JButton activeDeckButton = makeButton("View Active Deck");
        activeDeckButton.addActionListener(e -> displayActiveDeckCards());

        JButton quitButton = makeButton(embeddedMode ? "Campaign Menu" : "Save and Quit");
        quitButton.addActionListener(e -> confirmSaveAndQuit());

        buttonPanel.add(playMatchButton);
        buttonPanel.add(tournamentsButton);
        buttonPanel.add(packsButton);
        buttonPanel.add(collectionButton);
        buttonPanel.add(activeDeckButton);
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
                        showLongTextDialog("Campaign Loaded", result.output);
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
                    showLongTextDialog("Campaign Loaded", result.output);
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

        displayTitleLabel.setText("Choose Match Difficulty");
        displayPanel.removeAll();
        displayPanel.setLayout(new BorderLayout(14, 14));
        displayPanel.setBackground(new Color(255, 253, 245));

        JPanel wrapper = new JPanel(new BorderLayout(16, 16));
        wrapper.setOpaque(false);
        wrapper.setBorder(new EmptyBorder(30, 30, 30, 30));

        JLabel intro = new JLabel(
                "<html><center>Pick a campaign match. "
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

        if (displayTitleLabel != null && "Choose Match Difficulty".equals(displayTitleLabel.getText())) {
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

        CampaignPlayer player = campaign.getPlayer();
        if (player.getActiveDeckSize() < player.getActiveDeckMaxSize()) {
            showShortDialog(
                    "Active Deck Not Ready",
                    "Your active deck needs " + player.getActiveDeckMaxSize()
                            + " Presidents before you can play a match.\nCurrent active deck: "
                            + player.getActiveDeckSize() + "/" + player.getActiveDeckMaxSize()
                            + "\nOpen more packs first.",
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

        CampaignPlayer player = campaign.getPlayer();
        if (player.getActiveDeckSize() < player.getActiveDeckMaxSize()) {
            showShortDialog(
                    "Active Deck Not Ready",
                    "Your active deck needs " + player.getActiveDeckMaxSize()
                            + " Presidents before you can play a match.\nCurrent active deck: "
                            + player.getActiveDeckSize() + "/" + player.getActiveDeckMaxSize()
                            + "\nOpen more packs first.",
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


    private void displayTournamentsDashboard() {
        cancelCollectionLoadWorker();
        showingActiveDeckCards = false;
        showingCollectionCards = false;

        if (displayPanel == null) {
            return;
        }

        displayTitleLabel.setText("Era Tournaments");
        displayPanel.removeAll();
        displayPanel.setLayout(new BorderLayout(14, 14));
        displayPanel.setBackground(new Color(255, 253, 245));

        JPanel wrapper = new JPanel(new BorderLayout(16, 16));
        wrapper.setOpaque(false);
        wrapper.setBorder(new EmptyBorder(24, 24, 24, 24));

        JLabel intro = new JLabel(
                "<html><center>Enter an era tournament with your best 10 owned cards from that era. "
                        + "Each tournament is a single-elimination Easy / Medium / Hard gauntlet. "
                        + "Click into a tournament first, then play each match one at a time.</center></html>",
                SwingConstants.CENTER
        );
        intro.setFont(new Font("Dialog", Font.BOLD, Math.max(15, (int) (17 * (cardSize / 275.0)))));
        intro.setForeground(new Color(35, 35, 35));
        wrapper.add(intro, BorderLayout.NORTH);

        java.util.List<CampaignTournament> tournaments = campaign.getEraTournaments();
        JPanel choices = new JPanel(new GridLayout(0, 2, 22, 22));
        choices.setOpaque(false);

        for (CampaignTournament tournament : tournaments) {
            choices.add(buildTournamentChoicePanel(tournament));
        }

        wrapper.add(choices, BorderLayout.CENTER);
        displayPanel.add(wrapper, BorderLayout.NORTH);
        updateDisplayPanel();
    }

    private JPanel buildTournamentChoicePanel(CampaignTournament tournament) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(new Color(255, 250, 235));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 160, 120), 2),
                new EmptyBorder(14, 14, 14, 14)
        ));

        int owned = campaign.getOwnedCountForTournament(tournament);
        int required = tournament.getRequiredEraCards();
        boolean ready = campaign.canEnterTournament(tournament);
        String readinessText = ready
                ? "Ready: " + owned + "/" + required + " owned"
                : "Locked: " + owned + "/" + required + " owned";
        String deckStrengthText = ready
                ? "<br>Deck Strength: " + String.format("%.2f", campaign.getTournamentDeckStrength(tournament))
                : "<br>Need " + (required - owned) + " more " + tournament.getEraName() + " cards";

        JLabel details = new JLabel(
                "<html><center>"
                        + "<b>" + escapeHtml(tournament.getTournamentName()) + "</b><br>"
                        + escapeHtml(tournament.getEraName()) + "<br><br>"
                        + escapeHtml(readinessText)
                        + deckStrengthText
                        + "<br><br>Round 1: Easy / 40k + Souvenir Pack"
                        + "<br>Round 2: Medium / 75k + Souvenir Pack"
                        + "<br>Final: Hard / 200k + Souvenir + Champion Pack"
                        + "<br><br>" + escapeHtml(tournament.getDescription())
                        + "</center></html>",
                SwingConstants.CENTER
        );
        details.setFont(new Font("Dialog", Font.BOLD, Math.max(13, (int) (15 * (cardSize / 275.0)))));
        details.setForeground(new Color(20, 35, 65));
        panel.add(details, BorderLayout.CENTER);

        JButton enterButton = makeDashboardButton(ready ? "Enter" : "Locked");
        enterButton.setEnabled(ready);
        enterButton.addActionListener(e -> playTournamentFromUI(tournament));
        panel.add(enterButton, BorderLayout.SOUTH);

        return panel;
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
        if (!campaign.canEnterTournament(tournament)) {
            showShortDialog(
                    "Tournament Locked",
                    campaign.getTournamentEligibilityText(tournament),
                    true
            );
            return;
        }

        activeTournament = tournament;
        activeTournamentDeck = campaign.getTournamentActiveDeck(tournament);
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
                        + escapeHtml(tournament.getEraName()) + "<br><br>"
                        + "Tournament deck locked at entry: "
                        + getActiveTournamentDeckSize() + "/" + tournament.getRequiredEraCards() + " cards"
                        + "<br>Deck Strength: " + String.format("%.2f", getActiveTournamentDeckStrength())
                        + "<br><br>"
                        + "Round 1: Easy / " + CampaignMode.formatPC(tournament.getRoundWinPC(1))
                        + " + Souvenir Pack<br>"
                        + "Round 2: Medium / " + CampaignMode.formatPC(tournament.getRoundWinPC(2))
                        + " + Souvenir Pack<br>"
                        + "Final: Hard / " + CampaignMode.formatPC(tournament.getRoundWinPC(3))
                        + " + Souvenir + Champion Pack<br><br>"
                        + "Next match: Round " + nextRound + " ("
                        + escapeHtml(tournament.getRoundDifficulty(nextRound)) + ")"
                        + "</center></html>",
                SwingConstants.CENTER
        );
        details.setFont(new Font("Dialog", Font.BOLD, Math.max(15, (int) (17 * (cardSize / 275.0)))));
        details.setForeground(new Color(20, 35, 65));
        wrapper.add(details, BorderLayout.NORTH);

        wrapper.add(buildActiveTournamentDeckPanel(), BorderLayout.CENTER);

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

    private int getActiveTournamentDeckSize() {
        return activeTournamentDeck == null ? 0 : activeTournamentDeck.size();
    }

    private double getActiveTournamentDeckStrength() {
        if (activeTournamentDeck == null || activeTournamentDeck.isEmpty()) {
            return 0.0;
        }

        List<President> sortedDeck = new ArrayList<President>(activeTournamentDeck);
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

    private String buildActiveTournamentDeckText() {
        if (activeTournamentDeck == null || activeTournamentDeck.isEmpty()) {
            return "Tournament deck not loaded.";
        }

        List<President> sortedDeck = new ArrayList<President>(activeTournamentDeck);
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

    private JPanel buildActiveTournamentDeckPanel() {
        JPanel outer = new JPanel(new BorderLayout(10, 10));
        outer.setOpaque(false);
        outer.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 160, 120), 1),
                new EmptyBorder(14, 14, 14, 14)
        ));

        JLabel title = new JLabel(
                "<html><center><b>Tournament Deck</b><br>"
                        + "Best 10 owned cards from this era, locked at entry.</center></html>",
                SwingConstants.CENTER
        );
        title.setFont(new Font("Dialog", Font.BOLD, Math.max(13, (int) (15 * (cardSize / 275.0)))));
        title.setForeground(new Color(20, 35, 65));
        outer.add(title, BorderLayout.NORTH);

        if (activeTournamentDeck == null || activeTournamentDeck.isEmpty()) {
            outer.add(makeCenteredDisplayMessage("Tournament deck not loaded."), BorderLayout.CENTER);
            return outer;
        }

        List<President> sortedDeck = new ArrayList<President>(activeTournamentDeck);
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
        if (activeTournament == null || !activeTournament.equals(tournament)
                || activeTournamentDeck == null || activeTournamentDeck.isEmpty()) {
            activeTournament = tournament;
            activeTournamentDeck = campaign.getTournamentActiveDeck(tournament);
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
        if (activeTournament == null || !activeTournament.equals(tournament)
                || activeTournamentDeck == null || activeTournamentDeck.isEmpty()) {
            activeTournament = tournament;
            activeTournamentDeck = campaign.getTournamentActiveDeck(tournament);
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
                "1 President", "0.0-10.0 rating",
                "Cheap chaos reroll. Any card can show up."));
        choices.add(buildPackChoicePanel("Filler Pack", campaign.getFillerPack(),
                "5 Presidents", "0.0-3.0 rating",
                "Cheap depth and early collection filler."));
        choices.add(buildPackChoicePanel("Star Pack", campaign.getStarPack(),
                "1 President", "3.5-10.0 rating",
                "Single-card gamble with a stronger floor."));
        choices.add(buildPackChoicePanel("Standard Pack", campaign.getStandardPack(),
                "5 Presidents", "2.0-10.0 rating",
                "Balanced collection growth and active-deck upgrades."));
        choices.add(buildPackChoicePanel("Elite Pack", campaign.getElitePack(),
                "3 Presidents", "4.0-10.0 rating",
                "Premium high-end hunting for your active deck."));
        choices.add(buildEraProspectStorePanel());

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
                        + "5 Presidents<br>"
                        + "Chosen era, 0.0-3.99 rating<br>"
                        + "Cost: " + CampaignMode.formatPC(cost) + "<br><br>"
                        + "Targeted era progress for unlocking tournaments."
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
                "<html><center>Choose an era. Era Prospect Packs give 5 cards from that era, "
                        + "rated 0.0-3.99, and are designed to build tournament eligibility.</center></html>",
                SwingConstants.CENTER
        );
        intro.setFont(new Font("Dialog", Font.BOLD, Math.max(15, (int) (17 * (cardSize / 275.0)))));
        intro.setForeground(new Color(35, 35, 35));
        wrapper.add(intro, BorderLayout.NORTH);

        JPanel choices = new JPanel(new GridLayout(0, 2, 22, 22));
        choices.setOpaque(false);
        for (CampaignTournament tournament : campaign.getEraTournaments()) {
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
        int required = tournament.getRequiredEraCards();
        String affordText = campaign.getPlayer().getPC() >= cost
                ? "You can afford this pack."
                : "Need " + CampaignMode.formatPC(cost - campaign.getPlayer().getPC()) + " more.";

        JLabel details = new JLabel(
                "<html><center>"
                        + "<b>" + escapeHtml(campaign.getEraProspectPackName(tournament)) + "</b><br>"
                        + escapeHtml(tournament.getTournamentName()) + "<br><br>"
                        + "Owned: " + owned + "/" + required + " for tournament entry<br>"
                        + "5 cards, 0.0-3.99 rating<br>"
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
        JButton viewDeckButton = makeDashboardButton("View Active Deck");
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
        CampaignTournament eraFilter = getSelectedCollectionEraTournament();
        List<CollectionCardItem> collectionCards = buildFilteredCollectionCards(player, eraFilter);

        int totalInScope = getCollectionScopeTotal(eraFilter);
        int ownedInScope = getCollectionScopeOwnedCount(player, eraFilter);
        int missingInScope = Math.max(0, totalInScope - ownedInScope);
        String titleText = buildCollectionTitleText(eraFilter, ownedInScope, totalInScope);
        displayTitleLabel.setText(titleText);

        int imageWidth = getCollectionCardImageWidth();
        int imageHeight = getCardImageHeight(imageWidth);
        int columns = getCollectionColumnCount(imageWidth);
        long requestId = ++collectionLoadRequestId;

        displayPanel.add(makeCenteredDisplayMessage(
                "Loading collection images... " + collectionCards.size() + " cards"
        ), BorderLayout.CENTER);
        updateDisplayPanel();

        if (collectionCards.isEmpty()) {
            displayLoadedCollectionCards(
                    new ArrayList<CardIconData>(), columns, ownedInScope, totalInScope, missingInScope, eraFilter
            );
            return;
        }

        collectionLoadWorker = new SwingWorker<List<CardIconData>, Void>() {
            @Override
            protected List<CardIconData> doInBackground() {
                List<CardIconData> loadedCards = new ArrayList<CardIconData>();
                for (CollectionCardItem cardItem : collectionCards) {
                    if (isCancelled()) {
                        return loadedCards;
                    }

                    President president = cardItem.getPresident();
                    ImageIcon icon = cardItem.isOwned()
                            ? loadScaledIcon(president.getImageURL(), imageWidth, imageHeight)
                            : loadMissingScaledIcon(president.getImageURL(), imageWidth, imageHeight);
                    loadedCards.add(new CardIconData(president, imageWidth, icon, cardItem.isOwned()));
                }
                return loadedCards;
            }

            @Override
            protected void done() {
                if (isCancelled() || requestId != collectionLoadRequestId || !showingCollectionCards) {
                    return;
                }

                try {
                    displayLoadedCollectionCards(get(), columns, ownedInScope, totalInScope, missingInScope, eraFilter);
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

    private List<CollectionCardItem> buildFilteredCollectionCards(
            CampaignPlayer player, CampaignTournament eraFilter
    ) {
        ArrayList<CollectionCardItem> filteredCards = new ArrayList<CollectionCardItem>();
        if (player == null || campaign == null) {
            return filteredCards;
        }

        Set<String> ownedNames = getOwnedPresidentNames(player);
        List<President> candidates = eraFilter == null
                ? campaign.getAllPresidents()
                : campaign.getPresidentsForTournament(eraFilter);

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

    private int getCollectionScopeTotal(CampaignTournament eraFilter) {
        if (campaign == null) {
            return 0;
        }
        if (eraFilter == null) {
            return campaign.getTotalPresidentCount();
        }
        return campaign.getPresidentsForTournament(eraFilter).size();
    }

    private int getCollectionScopeOwnedCount(CampaignPlayer player, CampaignTournament eraFilter) {
        if (player == null || campaign == null) {
            return 0;
        }
        if (eraFilter == null) {
            return player.getCollectionSize();
        }
        return campaign.getOwnedCountForTournament(eraFilter);
    }

    private String buildCollectionTitleText(CampaignTournament eraFilter, int owned, int total) {
        double pct = total == 0 ? 0.0 : owned * 100.0 / total;
        if (eraFilter == null) {
            return "Collection - " + owned + "/" + total + " (" + String.format("%.1f", pct) + "%)";
        }
        return "Collection - " + eraFilter.getEraName() + " - " + owned + "/" + total
                + " (" + String.format("%.1f", pct) + "%)";
    }

    private void displayLoadedCollectionCards(
            List<CardIconData> loadedCards, int columns, int ownedInScope,
            int totalInScope, int missingInScope, CampaignTournament eraFilter
    ) {
        if (displayPanel == null || !showingCollectionCards) {
            return;
        }

        displayPanel.removeAll();
        displayPanel.setLayout(new BorderLayout());
        displayPanel.setBackground(new Color(255, 253, 245));

        JPanel wrapper = new JPanel(new BorderLayout(10, 10));
        wrapper.setOpaque(false);

        wrapper.add(buildCollectionTopPanel(ownedInScope, totalInScope, missingInScope, eraFilter), BorderLayout.NORTH);

        if (loadedCards.isEmpty()) {
            wrapper.add(makeCenteredDisplayMessage("No cards match these filters."), BorderLayout.CENTER);
        } else {
            JPanel cardGrid = new JPanel(new GridLayout(0, columns, 20, 20));
            cardGrid.setBackground(new Color(255, 253, 245));
            cardGrid.setBorder(new EmptyBorder(18, 18, 18, 18));

            for (CardIconData cardData : loadedCards) {
                cardGrid.add(buildPresidentCardPanel(
                        cardData.getPresident(), cardData.getImageWidth(), cardData.getIcon(), cardData.isOwned()
                ));
            }
            wrapper.add(cardGrid, BorderLayout.CENTER);
        }

        displayPanel.add(wrapper, BorderLayout.NORTH);
        updateDisplayPanel();
    }

    private JPanel buildCollectionTopPanel(
            int ownedInScope, int totalInScope, int missingInScope, CampaignTournament eraFilter
    ) {
        JPanel topPanel = new JPanel(new BorderLayout(8, 8));
        topPanel.setOpaque(false);

        String eligibilityText = "";
        if (eraFilter != null) {
            int required = eraFilter.getRequiredEraCards();
            eligibilityText = ownedInScope >= required
                    ? " | Tournament eligible"
                    : " | Need " + (required - ownedInScope) + " more for tournament entry";
        }

        JLabel guidance = new JLabel(
                "<html><center>Filter your collection by era and ownership. Missing cards are shown dimmed in grayscale.<br>"
                        + "Owned: " + ownedInScope + "/" + totalInScope
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

        topPanel.add(controls, BorderLayout.SOUTH);
        return topPanel;
    }

    private String[] getCollectionEraFilterLabels() {
        ArrayList<String> labels = new ArrayList<String>();
        labels.add(COLLECTION_ERA_ALL);
        if (campaign != null) {
            for (CampaignTournament tournament : campaign.getEraTournaments()) {
                labels.add(tournament.getEraName());
            }
        }
        return labels.toArray(new String[labels.size()]);
    }

    private String getCollectionEraFilterLabel() {
        if (COLLECTION_ERA_ALL.equals(collectionEraFilterId) || campaign == null) {
            return COLLECTION_ERA_ALL;
        }

        CampaignTournament tournament = campaign.getTournamentById(collectionEraFilterId);
        return tournament == null ? COLLECTION_ERA_ALL : tournament.getEraName();
    }

    private String getCollectionEraIdForLabel(String label) {
        if (label == null || COLLECTION_ERA_ALL.equals(label) || campaign == null) {
            return COLLECTION_ERA_ALL;
        }

        for (CampaignTournament tournament : campaign.getEraTournaments()) {
            if (label.equals(tournament.getEraName())) {
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

    private void refreshActiveDeckCards() {
        if (displayPanel == null) {
            return;
        }

        displayPanel.removeAll();
        displayPanel.setLayout(new BorderLayout());
        displayPanel.setBackground(new Color(255, 253, 245));

        if (campaign == null || campaign.getPlayer() == null) {
            displayTitleLabel.setText("Active Deck");
            displayPanel.add(makeCenteredDisplayMessage("No campaign loaded."), BorderLayout.CENTER);
            updateDisplayPanel();
            return;
        }

        CampaignPlayer player = campaign.getPlayer();
        List<President> activeDeck = player.getActiveDeck();
        displayTitleLabel.setText("Active Deck - " + player.getActiveDeckSize()
                + "/" + player.getActiveDeckMaxSize()
                + " | Strength: " + String.format("%.2f", player.getActiveDeckStrength()));

        if (activeDeck == null || activeDeck.isEmpty()) {
            displayPanel.add(makeCenteredDisplayMessage("No active deck cards yet. Open starter packs first."), BorderLayout.CENTER);
            updateDisplayPanel();
            return;
        }

        JPanel wrapper = new JPanel(new BorderLayout(10, 10));
        wrapper.setOpaque(false);

        JLabel guidance = new JLabel(
                "<html><center>Your best 10 owned Presidents are automatically selected as your active deck.</center></html>",
                SwingConstants.CENTER
        );
        guidance.setFont(new Font("Dialog", Font.PLAIN, Math.max(14, (int) (16 * (cardSize / 275.0)))));
        guidance.setForeground(new Color(70, 70, 70));
        wrapper.add(guidance, BorderLayout.NORTH);

        JPanel cardGrid = new JPanel(new GridLayout(0, 5, 22, 22));
        cardGrid.setBackground(new Color(255, 253, 245));
        cardGrid.setBorder(new EmptyBorder(18, 18, 18, 18));

        int imageWidth = getLargeCardImageWidth();
        for (President president : activeDeck) {
            cardGrid.add(buildPresidentCardPanel(president, imageWidth));
        }

        wrapper.add(cardGrid, BorderLayout.CENTER);
        displayPanel.add(wrapper, BorderLayout.NORTH);
        updateDisplayPanel();
    }

    private JLabel makeCenteredDisplayMessage(String message) {
        String safeMessage = escapeHtml(message == null ? "" : message).replace("\n", "<br>");
        JLabel label = new JLabel("<html><center>" + safeMessage + "</center></html>", SwingConstants.CENTER);
        label.setFont(new Font("Dialog", Font.BOLD, Math.max(18, (int) (22 * (cardSize / 275.0)))));
        label.setForeground(new Color(90, 80, 65));
        return label;
    }

    private int getCardImageHeight(int imageWidth) {
        return (int) Math.round(1.32713755 * imageWidth);
    }

    private int getLargeCardImageWidth() {
        int frameWidth = frame == null ? 1600 : frame.getWidth();
        int availableWidth = Math.max(900, frameWidth - 120);

        // Active deck is 5 columns. Use the available width, but cap it so the
        // cards are big without becoming cartoonishly huge on ultrawide screens.
        int widthFromScreen = (availableWidth - 4 * 22 - 36) / 5;
        int widthFromScale = (int) (210 * (cardSize / 275.0));
        return Math.max(150, Math.min(widthFromScreen, Math.max(210, widthFromScale)));
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

        cardPanel.add(imageLabel, BorderLayout.CENTER);
        if (!owned) {
            JLabel missingLabel = new JLabel("Missing", SwingConstants.CENTER);
            missingLabel.setFont(new Font("Dialog", Font.BOLD, Math.max(11, (int) (13 * (cardSize / 275.0)))));
            missingLabel.setForeground(new Color(95, 95, 95));
            cardPanel.add(missingLabel, BorderLayout.SOUTH);
        }
        return cardPanel;
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
        if (navigationHost != null) {
            navigationHost.showMainHomeScreen();
            return;
        }
        if (ownsFrame && frame != null) {
            frame.dispose();
        }
    }

    private void exitFullApplication() {
        if (navigationHost != null) {
            navigationHost.exitApplication();
            return;
        }
        if (ownsFrame && frame != null) {
            frame.dispose();
        }
    }

    private void refreshDashboard() {
        if (campaign == null || campaign.getPlayer() == null) {
            nameLabel.setText("Name: ");
            pcLabel.setText("Political Capital: ");
            recordLabel.setText("Record: ");
            collectionLabel.setText("Collection: ");
            activeDeckLabel.setText("Active Deck: ");
            deckStrengthLabel.setText("Deck Strength: ");
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
        activeDeckLabel.setText("Active Deck: " + player.getActiveDeckSize()
                + "/" + player.getActiveDeckMaxSize());
        deckStrengthLabel.setText("Deck Strength: "
                + String.format("%.2f", player.getActiveDeckStrength()));

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
                + "Presidents owned: " + ownedPresidents + "/" + totalPresidents
                + " (" + String.format("%.1f", completionPct) + "%)\n"
                + "Active Deck: " + player.getActiveDeckSize() + "/"
                + player.getActiveDeckMaxSize() + "\n"
                + "Active Deck Strength: "
                + String.format("%.2f", player.getActiveDeckStrength()) + "\n"
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
        // v3.2 intentionally no longer keeps a full console log in the middle
        // of the screen. Short outputs are shown through CustomDialogs / text
        // dialogs, and the center display is now reserved for card views.
        if (text != null && !text.isEmpty()) {
            System.out.print(text);
            if (!text.endsWith("\n")) {
                System.out.println();
            }
        }
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
