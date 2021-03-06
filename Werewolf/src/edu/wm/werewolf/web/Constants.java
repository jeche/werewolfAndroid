package edu.wm.werewolf.web;

public class Constants {
	static private String url = "http://secure-lake-6285.herokuapp.com/";
	static private String success = "success";
	static private String stats = "gameStats";
	static private String addUser = "addUser";
	static private String players = "players";
	static private String getInfo = "getinfo";
	static private String alive = "alive";
	static private String scent = "scent";
	static private String gameStatus = "gameStatus";
	static private String isWerewolf = "isWerewolf";
	static private String responseStatus = "status";
	static private String kills = "kills";
	static private String kill = "/kill";
	static private String createTime = "created";
	static private String isDead = "dead";
	static private String nightFreq = "nightFreq";
	static private String updateloc = "/location";
	
	
	public Constants(){
		
	}
	
	public String updateLocation(){
		return url + players + updateloc;
	}
	
	public String createTime(){
		return createTime;
	}
	
	public String kill(){
		return url + players + kill;
	}
	
	public String nightFreq(){
		return nightFreq;
	}
	
	public String isDead(){
		return isDead;
	}
	
	public String kills(){
		return kills;
	}
	
	public String responseStatus(){
		return responseStatus;
	}

	public String getGameStatus(){
		return gameStatus;
	}
	
	public String isWerewolf(){
		return isWerewolf;
	}
	
	public String getBaseUrl(){
		return url;
	}
	
	public String success(){
		return success;
	}
	
	public String statusURL(){
		return url+stats;
	}
	
	public String addUserURL(){
		return url+addUser;
	}
	
	public String getInfoURL(){
		return url+players+"/"+getInfo;
	}
	
	public String scentURL(){
		return url+players+"/"+scent;
	}
	
	public String aliveURL(){
		return url+players+"/"+alive;
	}
	
	public String allPlayers(){
		return url+players+"/all";
	}
}
