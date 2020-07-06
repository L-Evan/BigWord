package com.lifegame.SQL;

  
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import java.util.Vector;

import javax.accessibility.AccessibleContext;

import com.lifegame.graphics.*;

public class ENTER {
	//数据库密码
	private final static String sqlpassword = "4663541757";
	private static Scanner in;
	
	/******************连接数据库*********************/
    public static Connection JDBCon(){
        try{
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        String ur1="jdbc:sqlserver://localhost:1433;databaseName=UserDB";
        Connection conn=DriverManager.getConnection(ur1,"sa",sqlpassword);
        //System.out.println("数据库连接成功");
        return conn;
        }catch(ClassNotFoundException ex){
            System.out.println("驱动程序找不到的异常，数据库连接失败");
            return null;
        }catch(SQLException ex){
            System.out.println("数据库连接失败");
            ex.printStackTrace();
            return null;
        }
    } 
    /**
     * 数据库初始化
     * @return
     */
    public static void JDBCreateTable(){
        try{
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        String ur1="jdbc:sqlserver://localhost:1433;databaseName=master";
        Connection conn=DriverManager.getConnection(ur1,"sa",sqlpassword);
        System.out.println("数据库连接成功");
        Statement b = conn.createStatement();
        b.addBatch("create database UserDB;");
        b.executeBatch();
        //关闭
        b.close();
        conn.close(); 
        //创建表
        b = JDBCon().createStatement(); 
        //用户表
        b.addBatch("create table users\n" + 
        		"(ID  int IDENTITY(1,1) not null primary key,\n" + 
        		"username varchar(20) not null, \n" + 
        		"password varchar(32) not null,\n" + 
        		"constraint unique_username unique(username)"+
        		");");
        //画布表
        b.addBatch("create table cancas(\n" + 
        		"cname nvarchar(40),\n"+
        		"  data varchar(max),\n" + 
        		"  cycle int,\n" + 
        		"  X int,\n" + 
        		"  Y int, \n" + 
        		" userid int not null,\n"+ 
        		" ID  int IDENTITY(1,1) not null primary key," +  
        		" constraint fk_name foreign key(userid) references users(ID) \n"+
        		"); ");
        //模板表
        b.addBatch("create table Template(\n" + 
        		"cname nvarchar(40),\n"+
        		"  data varchar(max),\n" + 
        		"  cycle int,\n" + 
        		"  X int,\n" + 
        		"  Y int, \n" + 
        		" ID  int IDENTITY(1,1) not null primary key," + 
        		"); ");
        b.executeBatch(); 
        //关闭
        b.close();
        conn.close(); 
        }catch(ClassNotFoundException ex){
            System.out.println("驱动程序找不到的异常，数据库连接失败"); 
        }catch(SQLException ex){
            System.out.println("数据库连接失败");
            ex.printStackTrace(); 
        } 
    } 
    
    /************************插入更新数据******************/
    //用户信息插入
    public static int insertData(String username,String password){   	
		  //数据插入
        Connection conn=JDBCon();
        String sql1="insert into users(username,password) values('"+username+"','"+password+"')";
        try{
        Statement stmt=conn.createStatement();
        int r=stmt.executeUpdate(sql1);
        System.out.println("数据库连接成功！");
        stmt.close();
        conn.close();
        if(r>0){
            return 1;	
        }else{
            return 0;
        } 
        }catch(SQLException ex){
            System.out.println("更新数据失败");
            ex.printStackTrace();
            return 0;
        }     
    } 

    //画布信息插入
    public static int insertCanvas(String cname,String data,int cycle,int X,int Y,String username){
    	int id = queryUserID(username); 
    	if(id==-1) {
    		System.out.println(username);
    		System.out.println("没ID");  
    		return -1;
    	} 
    	
		//数据插入
        Connection conn=JDBCon();
        String sql2="insert into cancas(cname,data,cycle,X,Y,userid) values('"+cname+"','"+data+"',"+cycle+","+X+","+Y+","+id+");";
        System.out.println(sql2);
        
        try{
	        Statement stmt=conn.createStatement();
	        int r=stmt.executeUpdate(sql2);
	        //System.out.println("数据库连接成功！");
	        stmt.close();
	        conn.close();
	        if(r>0){
	            return r;	
        }else{
            return 0;
        }
        
        }catch(SQLException ex){
            System.out.println("更新数据失败");
            ex.printStackTrace();
            return 0;
        }     
    } 
    //模板信息插入
    public static int insertTemplate(String cname,String data,int cycle,int X,int Y,String username){ 
    	int id = queryUserID(username);
    	if(id==-1) {
    		System.out.println(username);
    		System.out.println("没ID");  
    		return -1;
    	} 
    	//非管理人员不可进行画布插入
    	String admin="admin";
    	if(!username.equals(admin)) {
    		return -1;
    	}
		//数据插入
        Connection conn=JDBCon();
        String sql3="insert into Template(cname,data,cycle,X,Y) values('"+cname+"','"+data+"',"+cycle+","+X+","+Y+");";
        System.out.println(sql3);
        try{
	        Statement stmt=conn.createStatement();
	        int r=stmt.executeUpdate(sql3);
	        //System.out.println("数据库连接成功！");
	        stmt.close();
	        conn.close();
	        if(r>0){
	            return r;	
        }else{
            return 0;
        }
        
        }catch(SQLException ex){
            System.out.println("更新数据失败");
            ex.printStackTrace();
            return 0;
        }     
    } 
    
    /**************************用户密码修改**************************/
    public static boolean updatePassword(String username,String password){
    	Connection conn=JDBCon();
    	//String newPassword=enterPassword();
    	try {
        	Statement state=conn.createStatement();
        	String sql="update users set password='"+password+"' where username='"+username+"' "; 
            int r=state.executeUpdate(sql);
        	conn.close();
            if(r>0){
                return true;	
            }else{
                return false;
            }
    	}catch(SQLException ex){
            System.out.println("更新数据失败");
            ex.printStackTrace();
            return false;
        }  
    }
    
    /**************************查询**************************/
    //用户查询
    public static Vector<User> queryData(String sql){
        Connection conn=JDBCon();
        try{
        Statement stmt=conn.createStatement();
        ResultSet rs=stmt.executeQuery(sql);
        Vector<User> data=new Vector<User>();
        while(rs.next()){
            Integer ID=(Integer) rs.getObject(1);
            String username=rs.getObject(2).toString();
            String password=rs.getObject(3).toString();
            User user=new User(ID,username,password);
            data.add(user);    
            //System.out.println(user.password);
        } 
        rs.close();
        stmt.close();
        conn.close(); 
        return data;
        }catch(SQLException ex){
            System.out.println("查询数据失败");
            ex.printStackTrace();
            return null;
        }      
    }
    /*查询是否有重复用户名*/
    public static boolean queryUserInfo(String username){ 
    	Vector<User>  v = queryData("select * from users ;"); 
    	if(v==null || v.size()==0) return false;
    	for(int i = 0;i<v.size();i++) {
        	if( v.get(i).username.equals(username)) return true; 
    	}
		return false;
    }
    /*查询所有用户列表*/
    public static Vector<User> queryAllUser(){ 
    	Vector<User>  v = queryData("select * from users ;"); 
    	return v;
    }
    /*用户密码匹配*/
    public static int queryUser(String username,String password){ 
    	Vector<User>  v = queryData("select * from users where username = '"+username+"';");
    	if(v==null || v.size()==0) return 0;    	
    	if( v.get(0).password.equals(password)) return 1;
    	
		return -1;
    } 
    /*查询用户ID*/
    public static int queryUserID(String username){ 
    	Vector<User>  v = queryData("select * from users where username = '"+username+"';");
    	if(v==null || v.size()==0) return -1;  
    	return v.get(0).ID; 	 
    }  
    
    //画布查询
    public static Vector<Cancas> queryCancas(String sql){
        Connection conn=JDBCon();
        try{
	        Statement stmt=conn.createStatement();
	        ResultSet rs=stmt.executeQuery(sql);
	        Vector<Cancas> data2=new Vector<Cancas>();
	        while(rs.next()){
	        	String cname  = rs.getObject(1).toString();
	            String data= rs.getObject(2).toString();
	            Integer cycle=(Integer)rs.getObject(3);
	            Integer X=(Integer)rs.getObject(4);
	            Integer Y=(Integer)rs.getObject(5); 
            	Integer userid=(Integer)rs.getObject(6); 
	            Cancas cancas=new Cancas(cname,data,cycle,X,Y,userid);
	            data2.add(cancas); 
	        }
	        System.out.println("???!!1!!");
	        rs.close();
	        stmt.close();
	        conn.close(); 
	        System.out.println("???!!!!");
	        return data2;
        }catch(SQLException ex){
            System.out.println("查询数据失败");
            ex.printStackTrace(); 
            return null;
        }catch (Exception e) {
        	System.out.println("未知错误");
        	return null;
			// TODO: handle exception
		}  
    }
    /*查询所有用户创建的画布*/
    public static Vector<Cancas> queryCancasInfo(){ 
    	Vector<Cancas>  v = queryCancas("select * from cancas ;"); 
    	return v;
    }
    /*查询某用户的画布信息*/
    public static Vector<Cancas> queryUserCancas(String username){ 
    	int ID=queryUserID(username); 
    	//System.out.println(ID);
    	Vector<Cancas>  v = queryCancas("select * from cancas where userid = '"+ID+"';");
    	if(v==null) return null; 
    	return v;
    }
    
    //模板查询
    public static Vector queryModel(String sql){
        Connection conn=JDBCon();
        try{
        Statement stmt=conn.createStatement();
        ResultSet rs=stmt.executeQuery(sql);
        Vector data2=new Vector();
        while(rs.next()){
        	String cname  = (String) rs.getObject(1);
            String data=(String) rs.getObject(2);
            int cycle=rs.getInt(3);
            int X=rs.getInt(4);
            int Y=rs.getInt(5); 
            Cancas cancas=new Cancas(cname,data,cycle,X,Y);
            data2.add(cancas);          
        } 
        rs.close();
        stmt.close();
        conn.close();   
        return data2;
        }catch(SQLException ex){
            System.out.println("查询数据失败");
            ex.printStackTrace();
            return null;
        }      
    }
    
    public static Vector<Cancas> queryModelInfo(){ 
    	Vector<Cancas>  v = queryCancas("select * from Template ;"); 
    	return v;
    }

    /**************************用户输入***********************/
    /*键盘输入用户名*/
	public static String enterUsername() {
		  Scanner s = new Scanner(System.in); 
		  String name = null; 
		  System.out.println("请输入您的用户名："); 
		  name = s.next();   		  
		  System.out.println("您输入的是："+name); 
		  
		return name; 
	}
	
	/*键盘输入密码*/
	public static String enterPassword() {
		  Scanner s = new Scanner(System.in); 
		  String password = null; 
		  System.out.println("请输入您的密码："); 
		  password = s.next();  
		  System.out.println("您输入的是："+password);
		 
		return password; 
	}
	
	/****************main**************/
	public static void main(String[] args) {
		  String menu ="0";
		  String bye="bye";
		  System.out.println("请选择功能选项：\n0-创建数据库及表结构\n1-注册\n2-查询用户信息\n3-用户密码匹配\n4-修改密码\n5-插入画布\n6-查询画布信息\n7-查询某用户的画布信息\n8-插入模板\n9-查询模板信息");
		  while(!menu.equals(bye)) {
			  System.out.print("您的选择：");
			  in = new Scanner(System.in);  
			  menu = in.next();
			  switch(menu) {
			  //创建数据库及表结构
			  case "0":
				  JDBCreateTable();
				  break;
			  //注册
			  case "1":
					String username=enterUsername();
					//用户名是否重复
					boolean flag=queryUserInfo(username);
					if(flag) {
						System.out.println("用户名已存在!");
						break;
					}
					String password=enterPassword(); 
					int flag1= insertData(username,password); 
					if(flag1==1) {
						System.out.println("注册成功！");
					}
				  break;
			  //查询用户信息
			  case "2":
				  System.out.println(queryAllUser());
				  break;
			 //用户密码匹配
			  case "3":
					String username2=enterUsername();
					String password2=enterPassword();
					int flag2=queryUser(username2,password2);
					if(flag2==1) {
						System.out.println("密码正确。");
					}
					else {
						System.out.println("用户名不存在或密码错误！");
					}
				  break;
			  //修改密码
			  case "4":
				  updatePassword("蔡梓沁","456");
				  boolean flag3=true;
				  if(flag3==true) 
					  System.out.println("密码修改成功");
				  else
					  System.out.println("密码修改失败");
				  break;
			 //插入画布
			  case "5":
				  int flag4=insertCanvas("piture","abc",1,2,3,"蔡梓沁");
				  if (flag4==1)
					  System.out.println("画布信息插入成功！");
				  else
					  System.out.println("画布信息插入失败！");
				  break;
			 //查询画布信息
			  case "6":
				  System.out.println(queryCancasInfo()); 
				 break;
			//查询某用户的画布信息
			  case "7":
				  System.out.println(queryUserCancas("蔡梓沁"));
				break; 
			//插入模板
			  case "8":
				  int flag5=insertTemplate("模板1","happy",4,5,6,"admin");
				  if (flag5==1)
					  System.out.println("模板信息插入成功！");
				  else
					  System.out.println("模板信息插入失败！");
				  break;
				  
		    //查询模板信息
			  case "9":
				  System.out.println(queryModelInfo());
				  break;

						  			
			  }
		  }
		     System.out.println("退出程序...");
			//System.out.println(queryUserInfo().toString());
		
	}
		
}