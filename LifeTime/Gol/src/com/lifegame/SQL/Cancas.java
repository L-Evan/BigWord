package com.lifegame.SQL;

import java.io.Serializable;

public class Cancas implements Serializable {
	String data;
	int cycle,X,Y;
	String cname;
	String username;
	int userid;
	public Cancas(){
		
	}
	public Cancas(String cname,String data,int cycle,int X,int Y,int userid){
		this.data=data;
		this.cycle=cycle;
		this.X=X;
		this.Y=Y;
		this.cname=cname;
		this.userid=userid;
	}
	public Cancas(String cname,String data,int cycle,int X,int Y,String username){
		this.data=data;
		this.cycle=cycle;
		this.X=X;
		this.Y=Y;
		this.cname=cname;
		this.username=username;
	}
	public Cancas(String cname,String data,int cycle,int X,int Y){
		this.data=data;
		this.cycle=cycle;
		this.X=X;
		this.Y=Y;
		this.cname=cname;
	}
	public String toString() {
		return cname+" "+data+" "+cycle+" "+X+" "+Y+" "+userid+"\n";
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public int getCycle() {
		return cycle;
	}
	public void setCycle(int cycle) {
		this.cycle = cycle;
	}
	public int getX() {
		return X;
	}
	public void setX(int x) {
		X = x;
	}
	public int getY() {
		return Y;
	}
	public void setY(int y) {
		Y = y;
	}
	public String getCname() {
		return cname;
	}
	public void setCname(String cname) {
		this.cname = cname;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}

}
