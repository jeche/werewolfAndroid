package edu.wm.werewolf.model;

import java.io.Serializable;

public class GameStatus implements Serializable {
	private String username;
	private String password;
	public boolean isWerewolf;
	public boolean isNight;
	private long createDate;
	private long nightFreq;
	
	public GameStatus(String username, String password, boolean isWerewolf,
			boolean isNight, long createDate, long nightFreq) {
		super();
		this.username = username;
		this.password = password;
		this.isWerewolf = isWerewolf;
		this.isNight = isNight;
		this.createDate = createDate;
		this.nightFreq = nightFreq;
	}
}
