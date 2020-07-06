package com.lifegame.SQL;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import com.lifegame.Sock.DataJson;

public class SeverSock extends Thread{
	public static void main(String args[]) {
		new SeverSock().start();
	}
	public void run (){
        try {
        	ServerSocket serverSocket = new ServerSocket(8888); 
            int count = 0;// 记录客户端的数量
            System.out.println("服务器启动，等待客户端的连接。。。");
            Socket socket = null;
            while(true){ 
            	socket = serverSocket.accept(); 
                ++count;
                Thread serverHandleThread = new ServerHandleThread(socket); 
                //优先运行
                serverHandleThread.setPriority(4); 
                System.out.println("打开了线程"); 
                //开启
                serverHandleThread.start(); 
                //测试
                //test(socket);  
                System.out.println("上线的客户端有" + count + "个！");  
                //得到客户端信息 														
                InetAddress inetAddress = socket.getInetAddress(); 
                System.out.println("当前客户端的IP地址是："+inetAddress.getHostAddress());
            }
        } catch (IOException e) {
        	System.out.println("可能应该是端口被占用了把！");
            // TODO Auto-generated catch block
            e.printStackTrace(); 
        }
    }
	public static void test(Socket socket) throws IOException, ClassNotFoundException {
		/******************测试数据************************/
        ObjectInputStream i = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
        //获得了接收句柄         
        System.out.println("接收了");  
        Object ooo= i.readObject(); 
        socket.shutdownInput(); 
        DataJson o = new DataJson("response",new HashMap<Object,Object>());
        if( ooo instanceof DataJson) {
        	o = (DataJson) ooo;
        	if(o.path.equals("queryAllCanve")) {
        		Map<Object,Object> mp = new HashMap(); 
        		mp.put("Vector<Cancas>",ENTER.queryCancas("select * from cancas;")); 
        		o = new DataJson(mp);   
        	}else System.out.println(o.path);
        	
        } 
        ObjectOutputStream oo = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));					
        oo.writeObject(o);  
        //强制输出去  防止等待
        oo.flush();		
        socket.shutdownOutput(); 
        oo.close();
        socket.close();
        /******************************************/
	}
}
