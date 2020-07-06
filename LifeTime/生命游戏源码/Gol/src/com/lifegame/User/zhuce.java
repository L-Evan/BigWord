package com.lifegame.User;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JTextField;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Color;
import javax.swing.LayoutStyle.ComponentPlacement;

import com.lifegame.SQL.ENTER;
import com.lifegame.Sock.CallBack;
import com.lifegame.Sock.ClientSock;
import com.lifegame.Sock.DataJson;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.Map;
import java.awt.event.ActionEvent;

public class zhuce extends JFrame{

	private JFrame frame;
	private JLabel textField;
	private JLabel textField_1;
	private JLabel textField_4;
	private JTextField username;
	private JTextField password;
	private JPanel panel;
	private JTextField pwd;
	
	/**
	 * Create the application.
	 */
	public zhuce() {
		this.setLocationRelativeTo(null);
		this.setTitle("注册");
		initialize();
		this.setResizable(false);//界面大小不可修改
		frame.setTitle("用户注册");
		//显示
		frame.setVisible(true);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		//背景图  
		panel = new JPanel(){
			   @Override
			   protected void paintComponent(Graphics g) {
			    super.paintComponent(g);
			    ImageIcon img = new ImageIcon("image\\zBack.jpg");
			    img.paintIcon(this, g, 0, 0);
			   }
		};
		//账号标签
		textField = new JLabel();
		textField.setForeground(Color.white);
		textField.setFont(new Font("宋体", Font.PLAIN, 20));
		textField.setText("账号：");
		textField.setSize(200, 100);
		textField.setLocation(170, 110);
		
		//密码标签
		textField_1 = new JLabel();
		textField_1.setForeground(Color.white);
		textField_1.setFont(new Font("宋体", Font.PLAIN, 20));
		textField_1.setText("密码：");
		textField_1.setSize(200, 100);
		textField_1.setLocation(170, 190);

		//再次确认标签
		textField_4 = new JLabel();
		textField_4.setForeground(Color.white);
		textField_4.setFont(new Font("宋体", Font.PLAIN, 20));
		textField_4.setText("再次输入：");
		textField_4.setSize(200, 100);
		textField_4.setLocation(140, 270);

		//账号编辑文本
		username = new JTextField(16);
		username.setColumns(10);
		username.setSize(190, 30);
		username.setLocation(230, 145);
		
		//密码的文本
		password = new JPasswordField();
		password.setColumns(10);
		password.setSize(190,30);
		password.setLocation(230, 225);
		//再次确认密码
		pwd = new JPasswordField();
		pwd.setColumns(10);
		pwd.setSize(190, 30);
		pwd.setLocation(230, 305);
		
		//定义按钮
		JButton button1 = new JButton("注册");
		button1.setSize(100,50);
		button1.setLocation(150,360);
		JButton button2 = new JButton("返回");
		button2.setSize(100,50);
		button2.setLocation(340,360);
		
		//界面布局
		frame = new JFrame();
		frame.getContentPane().setBackground(Color.WHITE);
		frame.setBounds(100, 100, 583, 490);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setResizable(false);
		
		//所有控件加入panel面板
		panel.add(textField);
		panel.add(textField_1);
		panel.add(textField_4);
		panel.add(username);
		panel.add(password);
		panel.add(pwd);
		panel.add(button1);
		panel.add(button2);
		panel.setLayout(null);
		panel.setOpaque(true);
		frame.getContentPane().add(panel);
		//建立数据库表
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO 自动生成的方法存根
				ENTER.JDBCreateTable();//数据库连接建立表
			}
		});
		
		
		//注册按钮监听器
		button1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String user=username.getText().toString();
				String pwd1=password.getText().toString();
				String pwd2=pwd.getText().toString();
				//输出信息
				System.out.println(user+"     "+pwd1);
				System.out.println(user.length()+"     "+pwd1.length());
				//判断用户和密码是否为空
				if (user.equals("")) {
					JOptionPane.showMessageDialog(null, "用户名不为空！", "用户名错误", JOptionPane.ERROR_MESSAGE);
					return;
				}
				if(pwd1.equals("")) {
					JOptionPane.showMessageDialog(null, "密码不为空", "密码错误", JOptionPane.ERROR_MESSAGE);
					return;
				}
				//判断账号长度是否大于20
				if(user.length()>20){
					JOptionPane.showMessageDialog(null, "账号长度不超过20！", "账号长度错误", JOptionPane.ERROR_MESSAGE);
					return;
				}
				//判断密码长度是否大于6小于20
				if(pwd1.length()<6 || pwd1.length()>20){
					JOptionPane.showMessageDialog(null, "密码长度大于6但不超过20！", "密码长度错误", JOptionPane.ERROR_MESSAGE);
					return;
				}
				//判断密码1和密码2是否相同
				if(pwd1.equals(pwd2)){
					//对密码进行加密
					String MD5=MD5Utitls.stringToMD5(pwd1);
					//把用户名和密码 写入数据库 
					ClientSock.insertData(user, MD5, new CallBack() {
						@Override
						public void solve(DataJson data) {
							// TODO 自动生成的方法存根
							if(data==null) {
								JOptionPane.showMessageDialog(null, "服务器错误1！");
								return ;
							}
							System.out.println(data);
							Map m = data.getMp(); 
							Object o = m.get("insertuser"); 
							if(o==null){ 
								JOptionPane.showMessageDialog(null, "注册失败！");
								return ;   
							} 
							int i =(int) o;
							if(i==1){
								JOptionPane.showMessageDialog(null, "注册成功！");
								frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
								frame.dispose();
							}else if(i==-1){  
								JOptionPane.showMessageDialog(null, "用户名不能重复"); 
							}else{
								JOptionPane.showMessageDialog(null, "注册失败！");
							}
						}
					});
				}else{
					JOptionPane.showMessageDialog(null, "输入的密码不一致！");
					return;
				}
			}
		});
		
		
		
		button2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				frame.dispose();
			}
		});
	}
}
