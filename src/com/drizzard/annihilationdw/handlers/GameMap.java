package com.drizzard.annihilationdw.handlers;

public class GameMap {
	
	private int votes;
	private String name;

	/**
	 * @deprecated Use GameMap#name instead.
	 */
	@Deprecated
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

	/**
	 * @deprecated Use GameMap#getName() instead.
	 * @return
	 */
	@Deprecated
	public int getId() {
		return id;
	}

	@Deprecated
	public void setId(int id) {
		this.id = id;
	}
}