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
		this.setTitle("ע��");
		initialize();
		this.setResizable(false);//�����С�����޸�
		frame.setTitle("�û�ע��");
		//��ʾ
		frame.setVisible(true);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		//����ͼ  
		panel = new JPanel(){
			   @Override
			   protected void paintComponent(Graphics g) {
			    super.paintComponent(g);
			    ImageIcon img = new ImageIcon("image\\zBack.jpg");
			    img.paintIcon(this, g, 0, 0);
			   }
		};
		//�˺ű�ǩ
		textField = new JLabel();
		textField.setForeground(Color.white);
		textField.setFont(new Font("����", Font.PLAIN, 20));
		textField.setText("�˺ţ�");
		textField.setSize(200, 100);
		textField.setLocation(170, 110);
		
		//�����ǩ
		textField_1 = new JLabel();
		textField_1.setForeground(Color.white);
		textField_1.setFont(new Font("����", Font.PLAIN, 20));
		textField_1.setText("���룺");
		textField_1.setSize(200, 100);
		textField_1.setLocation(170, 190);

		//�ٴ�ȷ�ϱ�ǩ
		textField_4 = new JLabel();
		textField_4.setForeground(Color.white);
		textField_4.setFont(new Font("����", Font.PLAIN, 20));
		textField_4.setText("�ٴ����룺");
		textField_4.setSize(200, 100);
		textField_4.setLocation(140, 270);

		//�˺ű༭�ı�
		username = new JTextField(16);
		username.setColumns(10);
		username.setSize(190, 30);
		username.setLocation(230, 145);
		
		//������ı�
		password = new JPasswordField();
		password.setColumns(10);
		password.setSize(190,30);
		password.setLocation(230, 225);
		//�ٴ�ȷ������
		pwd = new JPasswordField();
		pwd.setColumns(10);
		pwd.setSize(190, 30);
		pwd.setLocation(230, 305);
		
		//���尴ť
		JButton button1 = new JButton("ע��");
		button1.setSize(100,50);
		button1.setLocation(150,360);
		JButton button2 = new JButton("����");
		button2.setSize(100,50);
		button2.setLocation(340,360);
		
		//���沼��
		frame = new JFrame();
		frame.getContentPane().setBackground(Color.WHITE);
		frame.setBounds(100, 100, 583, 490);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setResizable(false);
		
		//���пؼ�����panel���
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
		//�������ݿ��
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO �Զ����ɵķ������
				ENTER.JDBCreateTable();//���ݿ����ӽ�����
			}
		});
		
		
		//ע�ᰴť������
		button1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String user=username.getText().toString();
				String pwd1=password.getText().toString();
				String pwd2=pwd.getText().toString();
				//�����Ϣ
				System.out.println(user+"     "+pwd1);
				System.out.println(user.length()+"     "+pwd1.length());
				//�ж��û��������Ƿ�Ϊ��
				if (user.equals("")) {
					JOptionPane.showMessageDialog(null, "�û�����Ϊ�գ�", "�û�������", JOptionPane.ERROR_MESSAGE);
					return;
				}
				if(pwd1.equals("")) {
					JOptionPane.showMessageDialog(null, "���벻Ϊ��", "�������", JOptionPane.ERROR_MESSAGE);
					return;
				}
				//�ж��˺ų����Ƿ����20
				if(user.length()>20){
					JOptionPane.showMessageDialog(null, "�˺ų��Ȳ�����20��", "�˺ų��ȴ���", JOptionPane.ERROR_MESSAGE);
					return;
				}
				//�ж����볤���Ƿ����6С��20
				if(pwd1.length()<6 || pwd1.length()>20){
					JOptionPane.showMessageDialog(null, "���볤�ȴ���6��������20��", "���볤�ȴ���", JOptionPane.ERROR_MESSAGE);
					return;
				}
				//�ж�����1������2�Ƿ���ͬ
				if(pwd1.equals(pwd2)){
					//��������м���
					String MD5=MD5Utitls.stringToMD5(pwd1);
					//���û��������� д�����ݿ� 
					ClientSock.insertData(user, MD5, new CallBack() {
						@Override
						public void solve(DataJson data) {
							// TODO �Զ����ɵķ������
							if(data==null) {
								JOptionPane.showMessageDialog(null, "����������1��");
								return ;
							}
							System.out.println(data);
							Map m = data.getMp(); 
							Object o = m.get("insertuser"); 
							if(o==null){ 
								JOptionPane.showMessageDialog(null, "ע��ʧ�ܣ�");
								return ;   
							} 
							int i =(int) o;
							if(i==1){
								JOptionPane.showMessageDialog(null, "ע��ɹ���");
								frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
								frame.dispose();
							}else if(i==-1){  
								JOptionPane.showMessageDialog(null, "�û��������ظ�"); 
							}else{
								JOptionPane.showMessageDialog(null, "ע��ʧ�ܣ�");
							}
						}
					});
				}else{
					JOptionPane.showMessageDialog(null, "��������벻һ�£�");
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
