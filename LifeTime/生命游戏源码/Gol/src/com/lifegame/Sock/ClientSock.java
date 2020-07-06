package com.lifegame.Sock;
 
import java.awt.Canvas;
import java.awt.TextArea;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.swing.JOptionPane;

import com.lifegame.SQL.Cancas;
import com.lifegame.SQL.ENTER;
import com.lifegame.SQL.User;
import com.lifegame.graphics.Life;
/**
 * 中转
 * @author 11291
 *
 */
public class ClientSock {  
	/**
	 * sock版本s
	 * @param life
	 * @param username
	 * @param c
	 * @return 
	 */
	 public static void insertCanvas(Life life,String canveName,String username ,CallBack c  ) { 
		 HashMap<Object, Object> mp = new HashMap<>();
//		 mp.put("username",username);
//		 mp.put("cname",canveName);
//		 mp.put("time",1); 
//		 mp.put("canve",life.getcanvestr());  
//		 mp.put("x",life.getx()); 
//		 mp.put("y",life.gety());  
//		 mp.put("life",life);
		 System.out.println(life.gety());
		mp.put("life",new Cancas(canveName,life.getcanvestr(), 1, life.getx(), life.gety(),username));
		DataJson data = new DataJson("insertCanvas", mp);     
		new HttpRequestServer("127.0.0.1",data, c).start();   
		 // return  ENTER.insertCanvas(life.getcanvestr(), 1, Integer.valueOf(life.getx()), Integer.valueOf(life.gety()),canveName, username);
	 } 
	 public static Life queryCancas(CallBack cb) { 
		 Vector<Cancas> v = ENTER.queryCancas("select * from cancas;"); 
		 if(v==null || v.size()==0) System.out.println("没有欸");
		 Cancas c = v.get(0); 
		 System.out.println(v.toString());
		 return new Life(c.getData(),c.getX(),c.getY());
	 }
	 /**
	  *    
	  * @param cb
	  * @return
	  */
	 public static void queryAllCancas(String username,CallBack c) {
		 
		 HashMap<Object, Object> mp = new HashMap<>();
		 mp.put("username",username);  
		 DataJson d = new DataJson("queryAllCanve", mp);     
		 new HttpRequestServer("127.0.0.1",d, c).start();   
//		 Vector<Cancas> v = ENTER.queryCancas("select * from cancas;");
//		 if(v==null || v.size()==0) {
//			 System.out.println("没有欸"); 
//			 return null;
//		 } 
//		 System.out.println(v.toString()); 
//		 Vector<Life> vl = new Vector<>() ;
//		 for( Cancas c : v ) {
//			vl.add(new Life(c.getData(),c.getX(),c.getY())); 
//		 }
//		 return  vl;
	 }
	 
	 //查询用户信息
	 public static  void userInfo(String username,String password,CallBack c){
		 Map<Object, Object> mp = new HashMap<>();
 		 User user=new User(username,password);
		 mp.put("queryuser",user);
		 DataJson d = new DataJson("queryuser", mp);     
		 new HttpRequestServer("127.0.0.1",d, c).start(); 
	}
	 //插入数据库
	 public static void insertData(String username,String password, CallBack c) {
		 Map<Object, Object> mp = new HashMap<>();
		 User user=new User(username,password);
		 mp.put("insertuser",user);
		 DataJson d = new DataJson("user", mp);     
		 new HttpRequestServer("127.0.0.1",d, c).start();
	 } 
	 //更新密码
	 public static void upData(String username,String password,CallBack c){
		 Map<Object, Object> mp = new HashMap<>();
		 User user=new User(username,password);
		 mp.put("updatauser",user);
		 DataJson d = new DataJson("updatauser", mp);     
		 new HttpRequestServer("127.0.0.1",d, c).start(); 
	 }
}
