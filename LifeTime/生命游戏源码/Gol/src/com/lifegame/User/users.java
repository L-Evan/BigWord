package com.lifegame.User;

import java.awt.Color;
import java.awt.Container;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;

import com.lifegame.SQL.ENTER;
import com.lifegame.Sock.CallBack;
import com.lifegame.Sock.ClientSock;
import com.lifegame.Sock.DataJson;
import com.lifegame.graphics.GoL;
import com.lifegame.graphics.Main;

public class users extends JFrame {

	private JFrame frame;
	private JLabel a1;
	private JLabel a2;
	private JTextField username;
	private JPasswordField password;
	private MyPanel panel;
	private Container contentPane=this.getContentPane();
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					users window = new users();
					window.frame.setVisible(true);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	} 
	/**
	 * Create the application.
	 */
	public users() {
		this.setLocationRelativeTo(null);
		this.setResizable(false);
		this.setTitle("登录");
		initialize();
		//显示
		frame.setVisible(true);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		//定义标签控件
		a1 = new JLabel("账号：");	//---用户名
		a1.setFont(new Font("宋体", Font.PLAIN, 20));
		a1.setSize(200, 100);
		a1. setLocation(100,150);

		a2 = new JLabel("密码：");	//-----密码
		a2.setFont(new Font("宋体", Font.PLAIN, 20));
		a2.setSize(200, 100);
		a2.setLocation(100,230);
		
		// 用户名的输入
		username = new JTextField(16);
		username.setSize(190,30);
		username.setLocation(160, 185);
		username.setColumns(10);
		username.addFocusListener(new JTextFieldHintListener("请输入账号", username));
		// 密码的输入
		password = new JPasswordField(16);
		password.setSize(190,30);
		password.setLocation(160, 265);
		password.setColumns(10);
		password.addFocusListener(new JPasswordFieldHintListener("请输入6-20位的密码", password));
		//按钮控件
		JButton in = new JButton("登录"); //登录
		in.setSize(120, 40);
		in.setLocation(60, 360);
		JButton newone = new JButton("注册"); //注册
		newone.setSize(120, 40);
		newone.setLocation(340, 360);
		JButton innew = new JButton("重置"); //重置
		innew.setSize(120, 40);
		innew.setLocation(200, 360);
		//界面布局
		frame = this;
		frame.getContentPane().setBackground(Color.WHITE);
		frame.setBounds(100, 100,500, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//定义MyPanel类
		panel=new MyPanel();
		//所有控件加入panel面板
		panel.add(a1);
		panel.add(a2);
		panel.add(username);
		panel.add(password);
		panel.add(innew);
		panel.add(in);
		panel.add(newone);
		panel.setLayout(null);
		panel.setOpaque(true);
		frame.getContentPane().add(panel);
		// 登录按钮监听器
		in.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String s1 = username.getText();
				String s2 = password.getText();
				System.out.println("输出："+s1.toString()+"  "+s2.toString());
				System.out.println("账号长度："+s1.length()+"密码长度"+s2.length());
				// 1、将密码加密 
				String s3 = MD5Utitls.stringToMD5(s2);
				// 2、判断用户和密码是否为空
				if (s1.equals("请输入账号")) {
					JOptionPane.showMessageDialog(null, "用户名不为空！", "用户名错误", JOptionPane.ERROR_MESSAGE);
					return;
				}
				if(s2.equals("请输入6-20位的密码")) {
					JOptionPane.showMessageDialog(null, "密码不为空", "密码错误", JOptionPane.ERROR_MESSAGE);
					return;
				}
				// 3、判断账号长度或者密码长度是否超过20
				if (s1.length()>20) {
					System.out.println("账号长度："+s1.length()+"密码长度"+s2.length());
					JOptionPane.showMessageDialog(null, "账号长度不超过20！", "账号长度错误", JOptionPane.ERROR_MESSAGE);
					return;
				} 
				if (s2.length()<6 || s2.length()>20) {
					JOptionPane.showMessageDialog(null, "密码长度大于6但不超过20！", "密码长度错误", JOptionPane.ERROR_MESSAGE);
					return;
				}
				// 4、在数据库中看看密码和用户名是否对上
				ClientSock.userInfo(s1, s3, new CallBack(){

					@Override
					public void solve(DataJson data) {
						// TODO 自动生成的方法存根
						if(data==null ){
							JOptionPane.showMessageDialog(null, "服务器错误1！");
							return;
						}
						Map m = data.getMp(); 
						Object o=m.get("queryuser");
						
						int i ;  
						if( o instanceof Integer) {
							i=(int) o;
							switch(i){
							case 0:
								JOptionPane.showMessageDialog(null, "没有这个用户！");
								return;
							case -1:
								JOptionPane.showMessageDialog(null, "密码错误！");
								return;
							case 1:
								JOptionPane.showMessageDialog(null, "登录成功");
								frame.dispose();
								//打开
								Main.createAndShowGUI(s1);
							}
						}else{
							JOptionPane.showMessageDialog(null, "服务器错误2！");
							return;
						}
					}
				});
			}
		});

		
		// 注册按钮监听器
		newone.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new zhuce();// 进入注册窗口
			}
		});

		
		// 重置按钮监听器
		innew.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				username.setText("");
				password.setText("");
				username.addFocusListener(new JTextFieldHintListener("请输入账号", username));
				password.addFocusListener(new JPasswordFieldHintListener("请输入6-20位的密码", password));
			}
		});
	}
}
