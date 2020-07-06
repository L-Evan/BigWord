package com.lifegame.Sock;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class DataJson  implements Serializable { 
	/**
	 * 
	 */ 
	private static final long serialVersionUID = 6961589317382622196L;
    public String path; 
    private int sta; 
    private  Map mp;  
    public DataJson(String path,Map<Object, Object> mp){
    	this.path=path;
    	this.mp=mp;
    } 
    public DataJson(){
    	this.path="";
    	this.mp= new HashMap<Object, Object>(); 
    }
    public DataJson(Map<Object, Object>  mp2) {
		// TODO 自动生成的构造函数存根
    	this.mp=mp2;
    	this.path = "";
	}
	public String toString(){
    	return path;
    }
	public  Map<Object, Object> getMp() { 
		return mp;
	}
	public void setMp(Map<Object, Object> mp) {
		this.mp = mp;
	}
	public int getSta() {
		return sta;
	}
	public void setSta(int sta) {
		this.sta = sta;
	}
}
