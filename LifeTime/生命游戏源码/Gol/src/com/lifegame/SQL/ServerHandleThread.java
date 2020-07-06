package com.lifegame.SQL;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.swing.JOptionPane;

import com.lifegame.SQL.*;
import com.lifegame.Sock.*;
public class ServerHandleThread extends Thread{
	Socket socket=null;
	public ServerHandleThread(Socket socket) { 
		this.socket=socket;
	}
	/*得到全部画布*/
	public DataJson solveAllCanves(Map mp) {  
		Map<Object,Object> nmp = new HashMap(); 
		String username = (String) mp.getOrDefault("username", null);
		DataJson data = new DataJson("response",nmp);
		//还没写名字  where username='"+username+"';
		if(username!=null) {  
			System.out.println("开始查询"); 
			int id = ENTER.queryUserID(username); 
			Vector  v = ENTER.queryModel( "select * from Template;");   
			if(v==null) {
				v = new Vector<>();
			}
			Vector  vu= ENTER.queryCancas("select * from cancas where userid= " + id+ " ;"); 
			System.out.println(vu);
			System.out.println("??!");
			if(  vu!=null) {
				v.addAll(vu);
			} 
			nmp.put("allCanves",v);  
		}else {
			System.out.println("没名字");
			data.setSta(-1);
		} 
		System.out.println("发送回去");
        return data;
	} 
	/*画布部分*/
	public DataJson solveCancas(Map mp) {
		//插入画布信息
		Object o=mp.get("life");
		DataJson returnData = null;
		Cancas c = null;
		if(o!=null && o instanceof Cancas) {
			returnData=new DataJson();
			c=(Cancas)o;
			System.out.println(c.username);
			int flag;
			if(c.username.equals("admin")) {
				flag = ENTER.insertTemplate(c.cname,c.data,c.cycle,c.X,c.Y,c.username);
	    	}else
	    		flag=ENTER.insertCanvas(c.cname,c.data,c.cycle,c.X,c.Y,c.username);
			
			if(flag>0) { 
				System.out.println("插入成功");
				returnData=new DataJson();
				returnData.setSta(1);
				returnData.getMp().put("cancas", "数据已存入数据库"); 
				return returnData;
			}else {
				System.err.println("插入错误");
			}
			System.out.println(flag);
		}else { 
			returnData.setSta(-1);
		}
		return returnData;
		
	}
	/*用户部分*/
	public DataJson solveUser(Map mp) {
		//插入用户信息
		Object o=mp.get("insertuser");
		if(o!=null && o instanceof User) {
			User user=new User();
			user=(User)o;
			boolean result=ENTER.queryUserInfo(user.username);
			if(!result){
				int flag=ENTER.insertData(user.username,user.password);
                //DataJson(new Map)
				if(flag>=0) {
					DataJson returnData=new DataJson();
					returnData.getMp().put("insertuser", flag);
					return returnData;
				}  
			}else{   
				DataJson returnData=new DataJson();
				returnData.getMp().put("insertuser", -1);
				return returnData;
			}
			
		} 
		return null;
	}
	/*用户查询密码*/  
	public DataJson userInfo(Map mp){
		//查询用户信息
		Object o =mp.get("queryuser");
		if(o!=null && o instanceof User) {
			User user=new User();
			user=(User)o;
			int i = ENTER.queryUser(user.username, user.password);
			DataJson returnData=new DataJson();
			returnData.getMp().put("queryuser", i);
			return returnData;
		}
		return null;
	}
	/*用户修改密码*/
	public DataJson updataInfo(Map mp){
		Object o =mp.get("updatauser");
		if(o!=null && o instanceof User) {
			User user=new User();
			user=(User)o;
			boolean i = ENTER.updatePassword(user.username,user.password);
			DataJson returnData=new DataJson();
			System.err.println(i);
			returnData.getMp().put("updatauser", i);
			return returnData;
		}
		return null;
	}
	
	/**
	 * 线程
	 */
	public void run() {
		OutputStream os=null;
		ObjectOutputStream oos=null;
		ObjectInputStream ois=null;
		InputStream is = null;
		BufferedOutputStream bops = null;
		BufferedInputStream bips = null;
		try {
			is=socket.getInputStream();
			bips = new BufferedInputStream(is);
			ois=new ObjectInputStream(bips);
            System.out.println("客户端发送对象");
            Object o=ois.readObject(); 
            DataJson data = null;
            //分发
            if( o instanceof DataJson) {
            	data=(DataJson)o;
            	switch(data.path) {
            	case "insertCanvas":
            		data =solveCancas(data.getMp());           
            		break;
            	case "user":
             		data = solveUser(data.getMp());
            		break;
            	case "queryAllCanve":
            		System.out.println("开始处理数据");
            		data = solveAllCanves(data.getMp());
            		break;
            	case"queryuser":
            		data=userInfo(data.getMp());
            		break;
            	case"updatauser":
            		data=updataInfo(data.getMp());
            		break;
            	} 
            } 
            //不能返回null
            if(data==null) {
            	data = new DataJson();
            	data.setSta(-1);
            } 
            System.out.println(data);
            socket.shutdownInput();// 禁用套接字的输入流 
            os=socket.getOutputStream();
            bops = new BufferedOutputStream(os);
			oos=new ObjectOutputStream(bops); 
			oos.writeObject(data);
            //清缓存
			oos.flush();
            socket.shutdownOutput();
            System.out.println("结束");
		}catch(IOException | ClassNotFoundException e) {
			 e.printStackTrace();
		}finally {
            try{
            	
                if(oos!=null){
                	oos.close();
                }
                if(bops!=null){
                	bops.close();
                }
                if(os!=null){
                    os.close();
                }
                if(ois!=null){
                	ois.close();
                }
                if(bips!=null){
                	bips.close();
                }
                if(is!=null){
                    is.close();
                }
                if(socket!=null){
                    socket.close();
                }
            }catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
		}
		
	}
	
  }
}
