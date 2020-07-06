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
		this.setTitle("��¼");
		initialize();
		//��ʾ
		frame.setVisible(true);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		//�����ǩ�ؼ�
		a1 = new JLabel("�˺ţ�");	//---�û���
		a1.setFont(new Font("����", Font.PLAIN, 20));
		a1.setSize(200, 100);
		a1. setLocation(100,150);

		a2 = new JLabel("���룺");	//-----����
		a2.setFont(new Font("����", Font.PLAIN, 20));
		a2.setSize(200, 100);
		a2.setLocation(100,230);
		
		// �û���������
		username = new JTextField(16);
		username.setSize(190,30);
		username.setLocation(160, 185);
		username.setColumns(10);
		username.addFocusListener(new JTextFieldHintListener("�������˺�", username));
		// ���������
		password = new JPasswordField(16);
		password.setSize(190,30);
		password.setLocation(160, 265);
		password.setColumns(10);
		password.addFocusListener(new JPasswordFieldHintListener("������6-20λ������", password));
		//��ť�ؼ�
		JButton in = new JButton("��¼"); //��¼
		in.setSize(120, 40);
		in.setLocation(60, 360);
		JButton newone = new JButton("ע��"); //ע��
		newone.setSize(120, 40);
		newone.setLocation(340, 360);
		JButton innew = new JButton("����"); //����
		innew.setSize(120, 40);
		innew.setLocation(200, 360);
		//���沼��
		frame = this;
		frame.getContentPane().setBackground(Color.WHITE);
		frame.setBounds(100, 100,500, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//����MyPanel��
		panel=new MyPanel();
		//���пؼ�����panel���
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
		// ��¼��ť������
		in.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String s1 = username.getText();
				String s2 = password.getText();
				System.out.println("�����"+s1.toString()+"  "+s2.toString());
				System.out.println("�˺ų��ȣ�"+s1.length()+"���볤��"+s2.length());
				// 1����������� 
				String s3 = MD5Utitls.stringToMD5(s2);
				// 2���ж��û��������Ƿ�Ϊ��
				if (s1.equals("�������˺�")) {
					JOptionPane.showMessageDialog(null, "�û�����Ϊ�գ�", "�û�������", JOptionPane.ERROR_MESSAGE);
					return;
				}
				if(s2.equals("������6-20λ������")) {
					JOptionPane.showMessageDialog(null, "���벻Ϊ��", "�������", JOptionPane.ERROR_MESSAGE);
					return;
				}
				// 3���ж��˺ų��Ȼ������볤���Ƿ񳬹�20
				if (s1.length()>20) {
					System.out.println("�˺ų��ȣ�"+s1.length()+"���볤��"+s2.length());
					JOptionPane.showMessageDialog(null, "�˺ų��Ȳ�����20��", "�˺ų��ȴ���", JOptionPane.ERROR_MESSAGE);
					return;
				} 
				if (s2.length()<6 || s2.length()>20) {
					JOptionPane.showMessageDialog(null, "���볤�ȴ���6��������20��", "���볤�ȴ���", JOptionPane.ERROR_MESSAGE);
					return;
				}
				// 4�������ݿ��п���������û����Ƿ����
				ClientSock.userInfo(s1, s3, new CallBack(){

					@Override
					public void solve(DataJson data) {
						// TODO �Զ����ɵķ������
						if(data==null ){
							JOptionPane.showMessageDialog(null, "����������1��");
							return;
						}
						Map m = data.getMp(); 
						Object o=m.get("queryuser");
						
						int i ;  
						if( o instanceof Integer) {
							i=(int) o;
							switch(i){
							case 0:
								JOptionPane.showMessageDialog(null, "û������û���");
								return;
							case -1:
								JOptionPane.showMessageDialog(null, "�������");
								return;
							case 1:
								JOptionPane.showMessageDialog(null, "��¼�ɹ�");
								frame.dispose();
								//��
								Main.createAndShowGUI(s1);
							}
						}else{
							JOptionPane.showMessageDialog(null, "����������2��");
							return;
						}
					}
				});
			}
		});

		
		// ע�ᰴť������
		newone.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				new zhuce();// ����ע�ᴰ��
			}
		});

		
		// ���ð�ť������
		innew.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				username.setText("");
				password.setText("");
				username.addFocusListener(new JTextFieldHintListener("�������˺�", username));
				password.addFocusListener(new JPasswordFieldHintListener("������6-20λ������", password));
			}
		});
	}
}
