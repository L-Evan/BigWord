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
	 * ����
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
	 * �ַ�����hui'x
	 * @param data
	 * @param c
	 */
	public void solve(DataJson data) { 
		if(c!=null)
			c.solve(data);
	}   
	//�����߳�
	private class reQuestServer extends Thread { 
		boolean over;
		Socket sock;
		// �˿ں�
		int port;
		String hosts;
		// ���� 
		DataJson data;
		InputStream ips;
		ObjectInputStream dips;
		// ����   
		BufferedWriter sockSend;
		OutputStream ops;
		ObjectOutputStream dops;
		//�ظ�
		DataJson info; 
		reQuestServer(String hosts, int port, DataJson data) {
			System.out.println("��������");
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
		//���վ���
		public void run() { 
			System.out.println("�������󡣡���");
			if (!getlink()) {
				System.out.println("Client�̳߳�ʼ��ʧ�ܣ�" + hosts + " " + port + "over"); 
				solve(info);
				return;
			} 
			if (!initSend()) {
				System.out.println("�ͻ���ʧ��1");
				solve(info); 
				return;
			} 
			try {
				dops.writeObject(data);
				System.out.println("�������˷������");
				//�ͻ���socket��������ر�
				sock.shutdownOutput();  
				 System.out.println("�ͻ��˽��ջظ�"); 
	            // 3.��ȡ��������ȡ�÷���������Ϣ   
				if (!initRecive()) {  
					System.out.println("�ͻ���ʧ��2");
					System.out.println(info);
					solve(info); 
					return;
				}    
				System.out.println("�ͻ��˽���chengg1");
	            Object o = dips.readObject();
	        	if( o!=null && o instanceof DataJson) {
	        		info = (DataJson) o;
	        		System.out.println("���ճɹ�");
	        	}  
	            //�ͻ���socket���������ر�
	            sock.shutdownInput();  
			} catch (IOException e) {
				// TODO �Զ����ɵ� catch ��
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO �Զ����ɵ� catch ��
				e.printStackTrace();
			} 
			 //�ͷ�
            freeServer();
            
			solve(info);  
			System.out.println("�ͻ��˶Ͽ����ӣ�");
			// -----------------------------
		} 
		// ����
		boolean solve(DataJson data) {
			HttpRequestServer.this.c.solve(data);
			return true;
		}   
		private boolean getlink() {  
			// �����˿�
			try {
				System.out.println("��������");
				sock = new Socket(hosts, port);
				System.out.println("�ɹ�����");
			} catch (IOException e1) {
				e1.printStackTrace();
				return false;
			}
			return true;
		}

		// �ͷ�
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

		// ��ʼ��
		boolean initSend() {
			// ����send								
			try {									
				ops = sock.getOutputStream();		
				dops = new ObjectOutputStream(ops);	
			} catch (IOException e) {				
				// TODO �Զ����ɵ� catch ��			
				e.printStackTrace();
				return false;
			}
			return true;
		} 
		// ��ʼ��		
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
