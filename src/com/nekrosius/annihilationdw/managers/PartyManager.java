package com.nekrosius.annihilationdw.managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.entity.Player;

import com.nekrosius.annihilationdw.Main;
import com.nekrosius.annihilationdw.handlers.Party;

public class PartyManager {

	private static List<Party> parties = new ArrayList<Party>();
	private static List<Party> addedParties = new ArrayList<Party>();
	
	private static Map<Player, Player> invites = new HashMap<Player, Player>();
	
	private Main pl;
	public PartyManager(Main plugin) {
		pl = plugin;
	}
	
	public static void createParty(Player player) {
		parties.add(new Party(player, new ArrayList<Player>()));
	}
	
	public static List<Party> getParties() {
		return parties;
	}
	
	public static void deleteParty(Player player) {
		for(Party party : getParties()){
			if(party.isLeader(player)){
				parties.remove(party);
				return;
			}
		}
	}
	
	public static boolean hasParty(Player player) {
		for(Party party : getParties()) {
			if(party.getLeader().equals(player)) return true;
			if(party.getPlayers().contains(player)) return true;
		}
		return false;
	}
	
	public static boolean isLeader(Player player) {
		return getParty(player).isLeader(player);
	}
	
	public static Party getParty(Player player) {
		for(Party party : getParties()) {
			if(party.getLeader().equals(player)) return party;
			if(party.getPlayers().contains(player)) return party;
		}
		return null;
	}
	
	public static void sendInvite(Player invited, Player inviter) {
		invites.put(invited, inviter);
	}
	
	public static void removeInvite(Player invited) {
		invites.remove(invited);
	}
	
	public static Player getInviter(Player invited) {
		return invites.get(invited);
	}
	
	public static void setAddedParties(List<Party> parties) {
		addedParties = parties;
	}
	
	public static void addAddedParty(Party party) {
		addedParties.add(party);
	}
	
	public static void removeAddedParty(Party party){ 
		addedParties.remove(party);
	}
	
	public static boolean isPartyAdded(Party party) {
		return addedParties.contains(party);
	}
	
	public static List<Party> getAddedParties() {
		return addedParties;
	}
	
	public Main getMain(){
		return pl;
	}
}