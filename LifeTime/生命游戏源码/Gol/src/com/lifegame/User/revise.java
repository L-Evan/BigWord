package com.lifegame.User;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;

import com.lifegame.Sock.CallBack;
import com.lifegame.Sock.ClientSock;
import com.lifegame.Sock.DataJson;
import com.lifegame.graphics.Main;

public class revise extends JFrame{

	private JFrame frame;
	private JLabel textField;
	private JLabel textField_1;
	private JLabel textField_4;
	private JTextField username;
	private JTextField password;
	
	private JTextField pwd;
	private String name;
	private JPanel panel;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					revise window = new revise("1129190684");
					window.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	} 
	
	/**
	 * Create the application.  
	 */
	public revise(String username) {  
		frame = this;
		name = username;
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);//界面大小不可修改 
		initialize();
		frame.setTitle("修改信息");  
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

				//旧密码标签
				textField_1 = new JLabel();
				textField_1.setForeground(Color.white);
				textField_1.setFont(new Font("宋体", Font.PLAIN, 20));
				textField_1.setText("旧密码：");
				textField_1.setSize(200, 100);
				textField_1.setLocation(150, 190);

				//新密码标签
				textField_4 = new JLabel();
				textField_4.setForeground(Color.white);
				textField_4.setFont(new Font("宋体", Font.PLAIN, 20));
				textField_4.setText("新密码：");
				textField_4.setSize(200, 100);
				textField_4.setLocation(150, 270);

				//账号编辑文本
				username = new JTextField(16);
				username.setEditable(false);
				username.setBackground(Color.LIGHT_GRAY);
				username.setColumns(10);
				username.setSize(190, 30);
				username.setLocation(230, 145);
				username.setText(name);
				
				//旧密码的文本
				password = new JPasswordField();
				password.setColumns(10);
				password.setSize(190,30);
				password.setLocation(230, 225);
				//新密码
				pwd = new JPasswordField();
				pwd.setColumns(10);
				pwd.setSize(190, 30);
				pwd.setLocation(230, 305);
				
				//定义按钮
				JButton button1 = new JButton("确认");
				button1.setSize(100,50);
				button1.setLocation(150,360);
				JButton button2 = new JButton("返回");
				button2.setSize(100,50);
				button2.setLocation(340,360);
				
				//界面布局 
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
		
		//确认按钮
		button1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String user=username.getText().toString();
				String pwd1=password.getText().toString();
				String pwd2=pwd.getText().toString();
				//输出信息
				System.out.println(user+"     "+pwd1);
				System.out.println(user.length()+"     "+pwd1.length());
				//对输入的旧密码加密
				String MD51=MD5Utitls.stringToMD5(pwd1);
				//判断旧密码是否相同
				ClientSock.userInfo(user, MD51, new CallBack(){
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
								JOptionPane.showMessageDialog(null, "旧密码错误！");
								return;
							case 1:
								//判断新密码长度是否大于6小于20
								if(pwd2.length()<6 || pwd2.length()>20){
									JOptionPane.showMessageDialog(null, "密码长度大于6但不超过20！", "密码长度错误", JOptionPane.ERROR_MESSAGE);
									return;
								}
								//对密码进行加密
								String MD5=MD5Utitls.stringToMD5(pwd2);
								//把用户名和密码 写入数据库 
								ClientSock.upData(user, MD5, new CallBack() {
									@Override
									public void solve(DataJson data) {
										// TODO 自动生成的方法存根
										Map m = data.getMp(); 
										Object o = m.get("updatauser"); 
										if(o==null){
											JOptionPane.showMessageDialog(null, "修改失败！");
											return ;   
										} 
										boolean i =(boolean) o;
										if(i){
											JOptionPane.showMessageDialog(null, "修改成功！");
											frame.dispose();
											//打开
											Main.createAndShowGUI(user);
										}else{
											JOptionPane.showMessageDialog(null, "修改失败！");
										} 
									}
								});
							}
						}else{
							JOptionPane.showMessageDialog(null, "服务器错误2！");
							return;
						}
					}
				});
			}
		});
		
		//返回按钮
		button2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				frame.dispose();
			}
		});
	}
}
