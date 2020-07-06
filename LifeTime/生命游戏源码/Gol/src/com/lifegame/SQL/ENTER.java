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
	//���ݿ�����
	private final static String sqlpassword = "4663541757";
	private static Scanner in;
	
	/******************�������ݿ�*********************/
    public static Connection JDBCon(){
        try{
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        String ur1="jdbc:sqlserver://localhost:1433;databaseName=UserDB";
        Connection conn=DriverManager.getConnection(ur1,"sa",sqlpassword);
        //System.out.println("���ݿ����ӳɹ�");
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
    /**
     * ���ݿ��ʼ��
     * @return
     */
    public static void JDBCreateTable(){
        try{
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        String ur1="jdbc:sqlserver://localhost:1433;databaseName=master";
        Connection conn=DriverManager.getConnection(ur1,"sa",sqlpassword);
        System.out.println("���ݿ����ӳɹ�");
        Statement b = conn.createStatement();
        b.addBatch("create database UserDB;");
        b.executeBatch();
        //�ر�
        b.close();
        conn.close(); 
        //������
        b = JDBCon().createStatement(); 
        //�û���
        b.addBatch("create table users\n" + 
        		"(ID  int IDENTITY(1,1) not null primary key,\n" + 
        		"username varchar(20) not null, \n" + 
        		"password varchar(32) not null,\n" + 
        		"constraint unique_username unique(username)"+
        		");");
        //������
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
        //ģ���
        b.addBatch("create table Template(\n" + 
        		"cname nvarchar(40),\n"+
        		"  data varchar(max),\n" + 
        		"  cycle int,\n" + 
        		"  X int,\n" + 
        		"  Y int, \n" + 
        		" ID  int IDENTITY(1,1) not null primary key," + 
        		"); ");
        b.executeBatch(); 
        //�ر�
        b.close();
        conn.close(); 
        }catch(ClassNotFoundException ex){
            System.out.println("���������Ҳ������쳣�����ݿ�����ʧ��"); 
        }catch(SQLException ex){
            System.out.println("���ݿ�����ʧ��");
            ex.printStackTrace(); 
        } 
    } 
    
    /************************�����������******************/
    //�û���Ϣ����
    public static int insertData(String username,String password){   	
		  //���ݲ���
        Connection conn=JDBCon();
        String sql1="insert into users(username,password) values('"+username+"','"+password+"')";
        try{
        Statement stmt=conn.createStatement();
        int r=stmt.executeUpdate(sql1);
        System.out.println("���ݿ����ӳɹ���");
        stmt.close();
        conn.close();
        if(r>0){
            return 1;	
        }else{
            return 0;
        } 
        }catch(SQLException ex){
            System.out.println("��������ʧ��");
            ex.printStackTrace();
            return 0;
        }     
    } 

    //������Ϣ����
    public static int insertCanvas(String cname,String data,int cycle,int X,int Y,String username){
    	int id = queryUserID(username); 
    	if(id==-1) {
    		System.out.println(username);
    		System.out.println("ûID");  
    		return -1;
    	} 
    	
		//���ݲ���
        Connection conn=JDBCon();
        String sql2="insert into cancas(cname,data,cycle,X,Y,userid) values('"+cname+"','"+data+"',"+cycle+","+X+","+Y+","+id+");";
        System.out.println(sql2);
        
        try{
	        Statement stmt=conn.createStatement();
	        int r=stmt.executeUpdate(sql2);
	        //System.out.println("���ݿ����ӳɹ���");
	        stmt.close();
	        conn.close();
	        if(r>0){
	            return r;	
        }else{
            return 0;
        }
        
        }catch(SQLException ex){
            System.out.println("��������ʧ��");
            ex.printStackTrace();
            return 0;
        }     
    } 
    //ģ����Ϣ����
    public static int insertTemplate(String cname,String data,int cycle,int X,int Y,String username){ 
    	int id = queryUserID(username);
    	if(id==-1) {
    		System.out.println(username);
    		System.out.println("ûID");  
    		return -1;
    	} 
    	//�ǹ�����Ա���ɽ��л�������
    	String admin="admin";
    	if(!username.equals(admin)) {
    		return -1;
    	}
		//���ݲ���
        Connection conn=JDBCon();
        String sql3="insert into Template(cname,data,cycle,X,Y) values('"+cname+"','"+data+"',"+cycle+","+X+","+Y+");";
        System.out.println(sql3);
        try{
	        Statement stmt=conn.createStatement();
	        int r=stmt.executeUpdate(sql3);
	        //System.out.println("���ݿ����ӳɹ���");
	        stmt.close();
	        conn.close();
	        if(r>0){
	            return r;	
        }else{
            return 0;
        }
        
        }catch(SQLException ex){
            System.out.println("��������ʧ��");
            ex.printStackTrace();
            return 0;
        }     
    } 
    
    /**************************�û������޸�**************************/
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
            System.out.println("��������ʧ��");
            ex.printStackTrace();
            return false;
        }  
    }
    
    /**************************��ѯ**************************/
    //�û���ѯ
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
            System.out.println("��ѯ����ʧ��");
            ex.printStackTrace();
            return null;
        }      
    }
    /*��ѯ�Ƿ����ظ��û���*/
    public static boolean queryUserInfo(String username){ 
    	Vector<User>  v = queryData("select * from users ;"); 
    	if(v==null || v.size()==0) return false;
    	for(int i = 0;i<v.size();i++) {
        	if( v.get(i).username.equals(username)) return true; 
    	}
		return false;
    }
    /*��ѯ�����û��б�*/
    public static Vector<User> queryAllUser(){ 
    	Vector<User>  v = queryData("select * from users ;"); 
    	return v;
    }
    /*�û�����ƥ��*/
    public static int queryUser(String username,String password){ 
    	Vector<User>  v = queryData("select * from users where username = '"+username+"';");
    	if(v==null || v.size()==0) return 0;    	
    	if( v.get(0).password.equals(password)) return 1;
    	
		return -1;
    } 
    /*��ѯ�û�ID*/
    public static int queryUserID(String username){ 
    	Vector<User>  v = queryData("select * from users where username = '"+username+"';");
    	if(v==null || v.size()==0) return -1;  
    	return v.get(0).ID; 	 
    }  
    
    //������ѯ
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
            System.out.println("��ѯ����ʧ��");
            ex.printStackTrace(); 
            return null;
        }catch (Exception e) {
        	System.out.println("δ֪����");
        	return null;
			// TODO: handle exception
		}  
    }
    /*��ѯ�����û������Ļ���*/
    public static Vector<Cancas> queryCancasInfo(){ 
    	Vector<Cancas>  v = queryCancas("select * from cancas ;"); 
    	return v;
    }
    /*��ѯĳ�û��Ļ�����Ϣ*/
    public static Vector<Cancas> queryUserCancas(String username){ 
    	int ID=queryUserID(username); 
    	//System.out.println(ID);
    	Vector<Cancas>  v = queryCancas("select * from cancas where userid = '"+ID+"';");
    	if(v==null) return null; 
    	return v;
    }
    
    //ģ���ѯ
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
            System.out.println("��ѯ����ʧ��");
            ex.printStackTrace();
            return null;
        }      
    }
    
    public static Vector<Cancas> queryModelInfo(){ 
    	Vector<Cancas>  v = queryCancas("select * from Template ;"); 
    	return v;
    }

    /**************************�û�����***********************/
    /*���������û���*/
	public static String enterUsername() {
		  Scanner s = new Scanner(System.in); 
		  String name = null; 
		  System.out.println("�����������û�����"); 
		  name = s.next();   		  
		  System.out.println("��������ǣ�"+name); 
		  
		return name; 
	}
	
	/*������������*/
	public static String enterPassword() {
		  Scanner s = new Scanner(System.in); 
		  String password = null; 
		  System.out.println("�������������룺"); 
		  password = s.next();  
		  System.out.println("��������ǣ�"+password);
		 
		return password; 
	}
	
	/****************main**************/
	public static void main(String[] args) {
		  String menu ="0";
		  String bye="bye";
		  System.out.println("��ѡ����ѡ�\n0-�������ݿ⼰��ṹ\n1-ע��\n2-��ѯ�û���Ϣ\n3-�û�����ƥ��\n4-�޸�����\n5-���뻭��\n6-��ѯ������Ϣ\n7-��ѯĳ�û��Ļ�����Ϣ\n8-����ģ��\n9-��ѯģ����Ϣ");
		  while(!menu.equals(bye)) {
			  System.out.print("����ѡ��");
			  in = new Scanner(System.in);  
			  menu = in.next();
			  switch(menu) {
			  //�������ݿ⼰��ṹ
			  case "0":
				  JDBCreateTable();
				  break;
			  //ע��
			  case "1":
					String username=enterUsername();
					//�û����Ƿ��ظ�
					boolean flag=queryUserInfo(username);
					if(flag) {
						System.out.println("�û����Ѵ���!");
						break;
					}
					String password=enterPassword(); 
					int flag1= insertData(username,password); 
					if(flag1==1) {
						System.out.println("ע��ɹ���");
					}
				  break;
			  //��ѯ�û���Ϣ
			  case "2":
				  System.out.println(queryAllUser());
				  break;
			 //�û�����ƥ��
			  case "3":
					String username2=enterUsername();
					String password2=enterPassword();
					int flag2=queryUser(username2,password2);
					if(flag2==1) {
						System.out.println("������ȷ��");
					}
					else {
						System.out.println("�û��������ڻ��������");
					}
				  break;
			  //�޸�����
			  case "4":
				  updatePassword("������","456");
				  boolean flag3=true;
				  if(flag3==true) 
					  System.out.println("�����޸ĳɹ�");
				  else
					  System.out.println("�����޸�ʧ��");
				  break;
			 //���뻭��
			  case "5":
				  int flag4=insertCanvas("piture","abc",1,2,3,"������");
				  if (flag4==1)
					  System.out.println("������Ϣ����ɹ���");
				  else
					  System.out.println("������Ϣ����ʧ�ܣ�");
				  break;
			 //��ѯ������Ϣ
			  case "6":
				  System.out.println(queryCancasInfo()); 
				 break;
			//��ѯĳ�û��Ļ�����Ϣ
			  case "7":
				  System.out.println(queryUserCancas("������"));
				break; 
			//����ģ��
			  case "8":
				  int flag5=insertTemplate("ģ��1","happy",4,5,6,"admin");
				  if (flag5==1)
					  System.out.println("ģ����Ϣ����ɹ���");
				  else
					  System.out.println("ģ����Ϣ����ʧ�ܣ�");
				  break;
				  
		    //��ѯģ����Ϣ
			  case "9":
				  System.out.println(queryModelInfo());
				  break;

						  			
			  }
		  }
		     System.out.println("�˳�����...");
			//System.out.println(queryUserInfo().toString());
		
	}
		
}