package org.cis120.electiongame;

/**
 * Small bridge interface between Campaign Mode and the playable match UI.
 *
 * RunCampaignMode should depend on this interface rather than directly knowing
 * how RunElectionGameCombined builds its board, buttons, decks, and dialogs.
 * RunElectionGameCombined can implement this interface in v06 once it supports
 * campaign-launched playable matches.
 */
public interface CampaignMatchLauncher {

    void launchCampaignMatch(CampaignMatchConfig config, CampaignMatchCallback callback);
}
