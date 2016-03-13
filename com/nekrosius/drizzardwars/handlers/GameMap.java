package com.nekrosius.drizzardwars.handlers;

public class GameMap {
	
	private int votes;
	private String name;
	private int id;
	
	public GameMap(int id, String name){
		setId(id);
		setName(name);
		setVotes(0);
	}

	public int getVotes() {
		return votes;
	}

	public void setVotes(int votes) {
		this.votes = votes;
	}
	
	public void addVote() {
		this.votes++;
	}
	
	public void removeVote() {
		this.votes--;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}