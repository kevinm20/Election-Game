package org.cis120.electiongame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Pack {
	private List<President> presidents;
	private int cost;
	private int count;
	
	public Pack(int count, int cost, President p1) {
		this.cost = cost;
		this.count = count;
		this.presidents = new ArrayList<President>();
		this.presidents.add(p1);
	}
	public Pack(int count, int cost, President p1, President p2) {
		this(count, cost, p1);
		this.presidents.add(p2);	
	}	
	public Pack(int count, int cost, President p1, President p2, President p3) {
		this(count, cost, p1, p2);
		this.presidents.add(p3);	
	}
	public Pack(int count, int cost, President p1, President p2, President p3, President p4) {
		this(count, cost, p1, p2, p3);
		this.presidents.add(p4);	
	}
	public Pack(int count, int cost, President p1, President p2, President p3, President p4, President p5) {
		this(count, cost, p1, p2, p3, p4);
		this.presidents.add(p5);	
	}
	
	public Pack(int count, int cost, List<President> pres) {
		this.cost = cost;
		this.count = count;
		this.presidents = new ArrayList<President>();
		this.presidents.addAll(pres);
	}
	
	public List<President> pull () {
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
