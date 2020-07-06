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
        System.out.println("���ݿ����ӳɹ�");
        return conn;
        }catch(ClassNotFoundException ex){
            System.out.println("���������Ҳ������쳣�����ݿ�����ʧ��");
            return null;
        }catch(SQLException ex){
            System.out.println("���ݿ�����ʧ��");
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
            System.out.println("�������ݳɹ���");  
            stmt.close();
            conn.close();
        } catch (SQLException e) {  
            e.printStackTrace();  
            System.out.println("��������ʧ�ܣ�");  
        }  

    }  

    public static void restore() throws SQLException {  
    	Connection conn=JDBCon(); 
        String sql = "restore database UserDB from disk = '"+path+"SqlData\\backup.bak' WITH REPLACE";         
        try {  
            java.sql.Statement stmt =(java.sql.Statement) conn.createStatement();
			((java.sql.Statement) stmt).executeUpdate(sql); 
            System.out.println("��ԭ���ݳɹ���");  
            stmt.close();
            conn.close();
        } catch (SQLException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
            System.out.println("��ԭ����ʧ�ܣ�");  
        }  

    }   
public static void main(String[] args) throws SQLException { 
	path = System.getProperty("user.dir") + System.getProperty("file.separator") ;
	//System.out.println(getClass().getResource("/").getFile().toString());
//	backup();
    restore();
}
}
