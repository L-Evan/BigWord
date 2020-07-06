package com.lifegame.graphics;
import javax.swing.*;

import com.lifegame.SQL.SeverSock;
import com.lifegame.User.revise;
import com.lifegame.User.users;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;
public class Main { 
	private static JFrame frame = null;
	private static JFrame LoginFrame = null;
	private static JFrame Revise = null;
	private static String username = null; 
	/**
	 * ������
	 * @param username
	 */ 
	public static void createAndShowGUI(String username) {
		if(frame==null)
			EventQueue.invokeLater(new Runnable() {
	            public void run() {
	            	 //���������ô���         
	            	Main.frame = new GamePage("GOL   User: "+username,username); 
	                Main.username = username;
	                Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize(); 
	        		int width = (int)screensize.getWidth();  
	        		int height = (int)screensize.getHeight();  
	        		frame.setLocation(width/5, height/5);  
	        		frame.setVisible(true);                         
	            }
	        });
		else{
			frame.setVisible(true);
		}
    }
	/**
	 * ��¼����
	 * @param username
	 */
	public static void createAndShowLogin() {
		if(LoginFrame==null)
			// ��¼ 
			EventQueue.invokeLater(new Runnable() {
				@Override
				public void run() {
					try {
						users window = new users();
						window.setVisible(true); 
						LoginFrame = window;
					} catch (Exception e) {
						e.printStackTrace();
					}  
				}
			});
		else{
			LoginFrame.setVisible(true);
		}
    }
	/**
	 * �޸�����
	 * @param args
	 */
	public static void createAndChangeLogin(String username) {
		if(Revise==null || !username.equals(Main.username))
		{
			EventQueue.invokeLater(new Runnable() {
				public void run() {
					try {
						Revise = new revise(username);
						Revise.setVisible(true);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		} 
		else {
			Revise.setVisible(true);
		}
	} 
	/**
	 * ��¼����
	 * @param username
	 */
	public static void createServer() { 
			// ��¼ 
			EventQueue.invokeLater(new Runnable() {
				@Override
				public void run() {
					try {
						new SeverSock().start();
					} catch (Exception e) {
						e.printStackTrace();
					}  
				}
			}); 
    }
	public static void main(String[] args) {
		//�򿪷�����
		createServer();
		// �򿪵�¼ 
		 createAndShowLogin();   
	}
}
