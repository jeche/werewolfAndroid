package edu.wm.werewolf.web;

public class Constants {
	static private String url = "http://secure-lake-6285.herokuapp.com/";
	static private String success = "success";
	
	public Constants(){
		
	}
	
	public String getBaseUrl(){
		return url;
	}
	
	public String success(){
		return success;
	}
}