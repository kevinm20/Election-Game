package org.cis120.electiongame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class Pack {
	private List<President> presidents;
	private int cost;
	private int count;
	private double minRate;
	private double maxRate;
	private List<String> tags;
	// Find some way to make weighted odds

	public Pack(int count, int cost, double minRate, double maxRate, List<President> pres, List<String> tags) {
		// Setup the variables of the pack
		this.cost = cost;
		this.count = count;
		this.minRate = minRate;
		this.maxRate = maxRate;
		this.tags = new ArrayList<String>();
		if (tags != null) {
			this.tags.addAll(tags);
		} else {
			this.tags = null;
		}
		this.presidents = new ArrayList<President>();
		this.presidents.addAll(pres);
		
		
		// Filter out presidents by min and max rating and tags. Iterate through and
		// remove if necessary.
		Iterator<President> it = presidents.iterator();
		while (it.hasNext()) {
			President curr = it.next();

			// Filter for tags. If tags is null then any tag applies and just filter by
			// rating.
			boolean valid = false;
			if (tags != null) {
				for (String customTag : tags) {
					for (String presTag : curr.getTags()) {
						if (customTag.equals(presTag)) {
							valid = true;
						}
					}
				}
			} else {
				valid = true;
			}

			// Filter by weighted average.
			if (curr.getWeightedAve() < minRate || curr.getWeightedAve() > maxRate) {
				valid = false;
			}
			if (!valid) {
				it.remove();
			}
		}
	}

	public List<President> pull() {
		Collections.shuffle(presidents);
		List<President> pulled = new ArrayList<President>();
		for (int i = 0; i < count; i++) {
			pulled.add(presidents.get(i));
		}
		return pulled;
	}

	public int getCost() {
		return cost;
	}
}
