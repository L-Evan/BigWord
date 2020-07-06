package com.lifegame.SQL;

public class data {
	public String name;
	public String path;
	data(){
		
	}
	data(String name,String path){
		this.name=name;
		this.path=path;
	}
    public String getName() {  
        return name;  
    }  
  
    /** 
     * @param name 
     *            the name to set 
     */  
    public void setName(String name) {  
        this.name = name;  
    }  
  
    /** 
     * @return the path 
     */  
    public String getPath() {  
        return path;  
    }  
  
    /** 
     * @param path 
     *            the path to set 
     */  
    public void setPath(String path) {  
        this.path = path;  
    } 

}

