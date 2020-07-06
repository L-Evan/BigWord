package com.lifegame.SQL;
 
import java.io.Serializable;
import java.util.Scanner;

public class User implements Serializable {
	int ID;
	String username;
	String password;
	User(){
		
	}
	public User(int ID,String username,String password){
		this.ID=ID;
		this.username=username;
		this.password=password;  
	}
	public User(String username,String password){
		this.ID = -1;
		this.username=username;
		this.password=password;
	}
	public String toString() {
		return ID+" "+username+" "+password+"\n";
	}
}
