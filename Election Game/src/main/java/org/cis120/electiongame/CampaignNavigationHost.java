package org.cis120.electiongame;

/**
 * Small navigation contract used by embedded Campaign Mode screens.
 *
 * RunCampaignMode should not need to know how the main Campaign Clash shell
 * stores its JFrame, home screen, or screen-switching layout. It can simply ask
 * the host to show the main home screen, show Campaign Mode again, or exit the
 * application.
 */
public interface CampaignNavigationHost {

    /**
     * Return to the main Campaign Clash home/loading screen.
     */
    void showMainHomeScreen();

    /**
     * Show the Campaign Mode screen inside the host application shell.
     */
    void showCampaignModeScreen();

    /**
     * Exit the full Campaign Clash application.
     */
    void exitApplication();
}
