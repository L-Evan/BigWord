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
            int count = 0;// ��¼�ͻ��˵�����
            System.out.println("�������������ȴ��ͻ��˵����ӡ�����");
            Socket socket = null;
            while(true){ 
            	socket = serverSocket.accept(); 
                ++count;
                Thread serverHandleThread = new ServerHandleThread(socket); 
                //��������
                serverHandleThread.setPriority(4); 
                System.out.println("�����߳�"); 
                //����
                serverHandleThread.start(); 
                //����
                //test(socket);  
                System.out.println("���ߵĿͻ�����" + count + "����");  
                //�õ��ͻ�����Ϣ 														
                InetAddress inetAddress = socket.getInetAddress(); 
                System.out.println("��ǰ�ͻ��˵�IP��ַ�ǣ�"+inetAddress.getHostAddress());
            }
        } catch (IOException e) {
        	System.out.println("����Ӧ���Ƕ˿ڱ�ռ���˰ѣ�");
            // TODO Auto-generated catch block
            e.printStackTrace(); 
        }
    }
	public static void test(Socket socket) throws IOException, ClassNotFoundException {
		/******************��������************************/
        ObjectInputStream i = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
        //����˽��վ��         
        System.out.println("������");  
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
        //ǿ�����ȥ  ��ֹ�ȴ�
        oo.flush();		
        socket.shutdownOutput(); 
        oo.close();
        socket.close();
        /******************************************/
	}
}
