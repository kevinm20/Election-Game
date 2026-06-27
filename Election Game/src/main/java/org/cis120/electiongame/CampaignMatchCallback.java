package org.cis120.electiongame;

/**
 * Callback used by a playable Campaign Mode match.
 *
 * RunCampaignMode will pass one of these when launching a playable match. When
 * RunElectionGameCombined reaches a win/loss/resign outcome, it should call this
 * callback with a CampaignMatchResult so Campaign Mode can apply rewards and
 * return to the correct dashboard.
 */
public interface CampaignMatchCallback {

    void onCampaignMatchComplete(CampaignMatchResult result);
}
