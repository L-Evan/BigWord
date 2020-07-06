package com.lifegame.SQL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class BackupData {
	static String path;
    public static Connection JDBCon(){
        try{
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        String ur1="jdbc:sqlserver://localhost:1433;databaseName=master";
        String sqlpassword="4663541757";  
        Connection conn=DriverManager.getConnection(ur1,"sa",sqlpassword);
        System.out.println("数据库连接成功");
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

    public static void backup() throws SQLException {
    	Connection conn=JDBCon();
    	data data=new data("backup",path+"SqlData\\");
        String d=data.path+data.name+".bak";  
        System.out.println(d);  
        String sql = "backup database UserDB to disk = '"+d+"' WITH FORMAT";  
		try {  
            java.sql.Statement stmt =(java.sql.Statement) conn.createStatement();
			((java.sql.Statement) stmt).executeUpdate(sql);  
            System.out.println("备份数据成功！");  
            stmt.close();
            conn.close();
        } catch (SQLException e) {  
            e.printStackTrace();  
            System.out.println("备份数据失败！");  
        }  

    }  

    public static void restore() throws SQLException {  
    	Connection conn=JDBCon(); 
        String sql = "restore database UserDB from disk = '"+path+"SqlData\\backup.bak' WITH REPLACE";         
        try {  
            java.sql.Statement stmt =(java.sql.Statement) conn.createStatement();
			((java.sql.Statement) stmt).executeUpdate(sql); 
            System.out.println("还原数据成功！");  
            stmt.close();
            conn.close();
        } catch (SQLException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
            System.out.println("还原数据失败！");  
        }  

    }   
public static void main(String[] args) throws SQLException { 
	path = System.getProperty("user.dir") + System.getProperty("file.separator") ;
	//System.out.println(getClass().getResource("/").getFile().toString());
//	backup();
    restore();
}
}
