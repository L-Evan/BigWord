package com.lifegame.Sock; 
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
public class HttpRequestServer{
	int port = 8888;
	String hosts = "127.0.0.1";
	CallBack c;
	DataJson data;
	/**
	 * 测试
	 * @param args
	 */
	public static void main(String args[]) {  
		
	}
	HttpRequestServer(String ip,DataJson data,CallBack c){
		this.c = c;   
		this.hosts = ip;
		this.data = data;
	}    
	void start() {   
		reQuestServer th = new reQuestServer(hosts, port, data);
		th.start();  
	}
	/**
	 * 分发运行hui'x
	 * @param data
	 * @param c
	 */
	public void solve(DataJson data) { 
		if(c!=null)
			c.solve(data);
	}   
	//请求线程
	private class reQuestServer extends Thread { 
		boolean over;
		Socket sock;
		// 端口号
		int port;
		String hosts;
		// 接收 
		DataJson data;
		InputStream ips;
		ObjectInputStream dips;
		// 发送   
		BufferedWriter sockSend;
		OutputStream ops;
		ObjectOutputStream dops;
		//回复
		DataJson info; 
		reQuestServer(String hosts, int port, DataJson data) {
			System.out.println("创建对象");
			this.port = port;
			this.hosts = hosts;
			// send
			this.dops = null;
			this.ops = null;
			this.sock = null;
			// recive   
			this.ips = null;
			this.dips = null;
			this.data = data; 
			this.over = false;
			this.info = null;
		}
		//接收線程
		public void run() { 
			System.out.println("正在请求。。。");
			if (!getlink()) {
				System.out.println("Client线程初始化失败：" + hosts + " " + port + "over"); 
				solve(info);
				return;
			} 
			if (!initSend()) {
				System.out.println("客户端失败1");
				solve(info); 
				return;
			} 
			try {
				dops.writeObject(data);
				System.out.println("服务器端发送完毕");
				//客户端socket的输出流关闭
				sock.shutdownOutput();  
				 System.out.println("客户端接收回复"); 
	            // 3.获取输入流，取得服务器的信息   
				if (!initRecive()) {  
					System.out.println("客户端失败2");
					System.out.println(info);
					solve(info); 
					return;
				}    
				System.out.println("客户端接收chengg1");
	            Object o = dips.readObject();
	        	if( o!=null && o instanceof DataJson) {
	        		info = (DataJson) o;
	        		System.out.println("接收成功");
	        	}  
	            //客户端socket的输入流关闭
	            sock.shutdownInput();  
			} catch (IOException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			} 
			 //释放
            freeServer();
            
			solve(info);  
			System.out.println("客户端断开连接！");
			// -----------------------------
		} 
		// 结束
		boolean solve(DataJson data) {
			HttpRequestServer.this.c.solve(data);
			return true;
		}   
		private boolean getlink() {  
			// 开启端口
			try {
				System.out.println("建立连接");
				sock = new Socket(hosts, port);
				System.out.println("成功连接");
			} catch (IOException e1) {
				e1.printStackTrace();
				return false;
			}
			return true;
		}

		// 释放
		void freeServer() {
			try {
				synchronized (this) { 
					dops.close(); 
					ops.close(); 
					dips.close(); 
					ips.close(); 
					sock.close(); 
					this.over = true; 
					System.out.println("khdnow:" + over);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		// 初始化
		boolean initSend() {
			// 设置send								
			try {									
				ops = sock.getOutputStream();		
				dops = new ObjectOutputStream(ops);	
			} catch (IOException e) {				
				// TODO 自动生成的 catch 块			
				e.printStackTrace();
				return false;
			}
			return true;
		} 
		// 初始化		
		boolean initRecive() {
			try {				 
				ips = sock.getInputStream(); 
				dips = new ObjectInputStream(ips);   
			} catch (IOException e1) {
				e1.printStackTrace();
				return false;
			}
			return true;
		}
	}
}
