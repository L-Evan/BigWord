package com.lifegame.graphics;

import javax.swing.Box;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;

import java.awt.ScrollPane;
import java.awt.Scrollbar;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.ListModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.lifegame.Music.musicplayHandle;
import com.lifegame.SQL.Cancas;
import com.lifegame.Sock.CallBack;
import com.lifegame.Sock.DataJson;
import com.lifegame.graphics.GoL.ListenKey;
import com.lifegame.Sock.ClientSock;
import com.sun.corba.se.spi.activation.Server;
import com.sun.javafx.property.adapter.PropertyDescriptor.Listener;

import java.awt.Window.Type;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.Dialog.ModalExclusionType;
import java.awt.Font;
import java.awt.Robot;

import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.SpringLayout;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ActionEvent;
import javax.swing.JPopupMenu;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import javax.swing.BoxLayout;
import java.awt.FlowLayout;
import java.awt.Button;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class GamePage extends JFrame {
	Vector<Life> canves;
	private static GoL gol;
	private static JTextField cf;
	private JTextField canveField;
	private static String username = "abc";
	private static int musicnum =0;
	private static musicplayHandle musicplay = null;
	//ģ�ⰴ��
	Robot r; 
	// ����������ʾ
	JLabel canves_number;
	// �б��
	private JList<String> list = new JList<String>();

	GamePage(String title, String username) {
		//ģ���¼�
		try {
			r=new Robot();
		} catch (AWTException e1) {
			// TODO �Զ����ɵ� catch ��
			e1.printStackTrace();
		}
		
		// ��ʼ��
		this.username = username;
		getContentPane().setBackground(new Color(112, 128, 144));
		setTitle(title);
		setSize(979, 688);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// ����
		GoL gol_1 = new GoL(); 
		//����
        addMouseWheelListener(gol_1.new MouthLister());
		//��Ӽ���
		addKeyListener(gol_1.new ListenKey());  
		JPanel panel = new JPanel();
		panel.setBackground(Color.DARK_GRAY);
		panel.setPreferredSize(new Dimension(100, 100));
		getContentPane().setLayout(new BorderLayout(10, 10));

		// �����ݿ��ȡ�û��Ļ���ģ��

		// �����
		JPanel panel_2 = new JPanel();
		panel_2.setBackground(new Color(112, 128, 144));
		getContentPane().add(panel_2, BorderLayout.CENTER);

		getCanves();
		SpringLayout sl_panel_2 = new SpringLayout();
		sl_panel_2.putConstraint(SpringLayout.NORTH, gol_1, 10, SpringLayout.NORTH, panel_2);
		sl_panel_2.putConstraint(SpringLayout.WEST, gol_1, 10, SpringLayout.WEST, panel_2);
		sl_panel_2.putConstraint(SpringLayout.SOUTH, gol_1, 0, SpringLayout.SOUTH, panel_2);
		panel_2.setLayout(sl_panel_2);
		// -----����------------------------------
		this.gol = gol_1;
		gol_1.setLayout(null); 
		gol_1.init();
		gol_1.start();
		//������
		musicplay = new musicplayHandle();
		musicplay.start(); 
		list.setBackground(new Color(95, 158, 160));
		list.setFont(new Font("������ٻ����", Font.PLAIN, 19));
		list.setBounds(759, 10, 188, 483);  
		// ���ѡ��ѡ��״̬���ı�ļ�����
		list.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				// ��ȡ���б�ѡ�е�ѡ������
				int[] indices = list.getSelectedIndices();
				// ��ȡ��ѡ�е�ѡ������
				int index_canve = list.getSelectedIndex();  
				// ��ȡѡ�����ݵ� ListModel
				ListModel<String> listModel = list.getModel();
				// ���ѡ�е�ѡ��
				for (int index : indices) {
					System.out.println("ѡ��: " + index + " = " + listModel.getElementAt(index));

				}
				System.out.println(index_canve);
				if(index_canve==-1) {
					index_canve = indices.length>0?indices[0]:canves.size()-1;
				} 
				// index_canve = canves.size()-1;
				// ѡ��ϲ��� �����ⲻ֪��ʲô����
				if (index_canve > 0) {
					Life l = canves.get(index_canve); 
					gol.setMerge(l);
					canveField.setText("x = "+l.getx()+",y = "+l.gety()+", rule = 1 ,"+l.getcanvestr());
					l.setSize(300, 300);
					l.setVisible(true);  
					//remove ����Ҫupdate
					panel.removeAll(); 
					panel.updateUI();
					panel.add(l);
					  
					System.out.println(l); 
				} else {
					System.out.println("û��ѡ��");
				}
				
			}
		});
		;
		// �б�list�Ĺ�����
		JScrollPane scrollPane = new JScrollPane(list);
		sl_panel_2.putConstraint(SpringLayout.NORTH, scrollPane, 10, SpringLayout.NORTH, panel_2);
		sl_panel_2.putConstraint(SpringLayout.SOUTH, scrollPane, 0, SpringLayout.SOUTH, panel_2);
		sl_panel_2.putConstraint(SpringLayout.EAST, gol_1, -10, SpringLayout.WEST, scrollPane);
		sl_panel_2.putConstraint(SpringLayout.WEST, scrollPane, -175, SpringLayout.EAST, panel_2);
		sl_panel_2.putConstraint(SpringLayout.EAST, scrollPane, -10, SpringLayout.EAST, panel_2);
		panel_2.add(scrollPane);
		panel_2.add(gol_1);

		// ����Ĭ��ѡ����
		list.setSelectedIndex(0);
		// ����������ʾ
		canves_number = new JLabel();
		canves_number.setForeground(new Color(105, 105, 105));
		canves_number.setHorizontalAlignment(SwingConstants.CENTER);
		canves_number.setFont(new Font("������ٻ����", Font.BOLD, 15));
		canves_number.setBackground(new Color(30, 144, 255));
		canves_number.setText("���� " + canves.size() + " ��ģ��");
		scrollPane.setColumnHeaderView(canves_number);

		JPanel panel_1 = new JPanel();
		panel_1.setBackground(new Color(112, 128, 144));
		getContentPane().add(panel_1, BorderLayout.SOUTH);
		panel_1.setPreferredSize(new Dimension(100, 200));
		SpringLayout sl_panel_1 = new SpringLayout();
		sl_panel_1.putConstraint(SpringLayout.NORTH, panel, 10, SpringLayout.NORTH, panel_1);
		sl_panel_1.putConstraint(SpringLayout.WEST, panel, -176, SpringLayout.EAST, panel_1);
		sl_panel_1.putConstraint(SpringLayout.SOUTH, panel, -22, SpringLayout.SOUTH, panel_1);
		sl_panel_1.putConstraint(SpringLayout.EAST, panel, -10, SpringLayout.EAST, panel_1);
		panel_1.setLayout(sl_panel_1);

		// �ı���
		canveField = new JTextField();

		sl_panel_1.putConstraint(SpringLayout.NORTH, canveField, 51, SpringLayout.NORTH, panel_1);
		sl_panel_1.putConstraint(SpringLayout.WEST, canveField, 10, SpringLayout.WEST, panel_1);
		sl_panel_1.putConstraint(SpringLayout.SOUTH, canveField, -107, SpringLayout.SOUTH, panel_1);
		sl_panel_1.putConstraint(SpringLayout.EAST, canveField, -26, SpringLayout.WEST, panel);
		canveField.setBounds(177, 13, 136, 28);
		panel_1.add(canveField);
		canveField.setFont(new Font("������ٻ����", Font.PLAIN, 15));
		canveField.setForeground(new Color(105, 105, 105));
		canveField.setText("\u5C06golly\u7801\u7C98\u8D34\u5728\u6B64\u5904");
		canveField.setColumns(10);
		this.cf = canveField;
		canveField.addFocusListener(new FocusListener() {

			@Override
			public void focusGained(FocusEvent e) {
				// TODO �Զ����ɵķ������
				if (canveField.getText().equals("��golly��ճ���ڴ˴�")) {
					canveField.setText(" ");
				} 
			}

			@Override
			public void focusLost(FocusEvent e) {
				// TODO �Զ����ɵķ������

			}

		});

		// �ύ��ť
		JButton uploadButton = new JButton("\u4E0A\u4F20");
		sl_panel_1.putConstraint(SpringLayout.NORTH, uploadButton, 6, SpringLayout.SOUTH, canveField);
		sl_panel_1.putConstraint(SpringLayout.SOUTH, uploadButton, 48, SpringLayout.SOUTH, canveField);
		uploadButton.setBounds(187, 55, 71, 35);
		panel_1.add(uploadButton);
		uploadButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (canveField.getText().equals("") || canveField.getText().equals("��golly��ճ���ڴ˴�")) {
					JOptionPane.showMessageDialog(gol_1, "��ȷ������������ȷ��golͼ����", "�޷��ϴ�", JOptionPane.INFORMATION_MESSAGE);
				} else {
					int result = JOptionPane.showConfirmDialog(gol_1, "ȷ����Ҫ�ύ��", "��ʾ", JOptionPane.YES_NO_OPTION);
					System.out.println("ѡ����: " + result);
					if (result == 0) {
						String inputContent = JOptionPane.showInputDialog(gol_1, "��Ϊ��ͼ������:", "��������������");
						String canve = canveField.getText();
						Life l = new Life(canve);
						System.out.println(username + "?");
						ClientSock.insertCanvas(l, inputContent, username, new CallBack() {
							@Override
							public void solve(DataJson data) {
								// TODO �Զ����ɵķ������
								if (data != null && data.getSta() > 0) {
									System.out.println("l:" + l.toString());
									System.out.println("�ı����������ˣ�" + canve);
									getCanves();
								} else {
									System.out.println("ʧ��");
								}

							}
						});
						/*
						 * String[] ini =new String[canves.size()] ; for(int i=0;i<canves.size();i++) {
						 * ini[i]=canves.get(i).getx()+""; } list.setListData(ini);
						 */
					}
				}

			}
		});

		uploadButton.setFont(new Font("������ٻ����", Font.BOLD, 18));
		uploadButton.setForeground(new Color(255, 255, 255));
		uploadButton.setBackground(new Color(0, 139, 139));

		JButton deleteButton = new JButton("\u6E05\u9664");
		sl_panel_1.putConstraint(SpringLayout.WEST, uploadButton, -130, SpringLayout.WEST, deleteButton);
		sl_panel_1.putConstraint(SpringLayout.EAST, uploadButton, -30, SpringLayout.WEST, deleteButton);
		sl_panel_1.putConstraint(SpringLayout.NORTH, deleteButton, 6, SpringLayout.SOUTH, canveField);
		sl_panel_1.putConstraint(SpringLayout.WEST, deleteButton, -130, SpringLayout.WEST, panel);
		sl_panel_1.putConstraint(SpringLayout.SOUTH, deleteButton, 48, SpringLayout.SOUTH, canveField);
		sl_panel_1.putConstraint(SpringLayout.EAST, deleteButton, -30, SpringLayout.WEST, panel);
		deleteButton.setBounds(463, 55, 71, 35);
		panel_1.add(deleteButton);
		deleteButton.setFont(new Font("������ٻ����", Font.BOLD, 18));
		deleteButton.setForeground(new Color(255, 255, 255));
		deleteButton.setBackground(new Color(255, 99, 71));
		panel_1.add(panel);
		panel.setLayout(new CardLayout(0, 0));

		JButton clearButton = new JButton("\u753B\u5E03\u6E05\u7A7A");
		sl_panel_1.putConstraint(SpringLayout.NORTH, clearButton, 99, SpringLayout.NORTH, panel_1);
		sl_panel_1.putConstraint(SpringLayout.SOUTH, clearButton, -59, SpringLayout.SOUTH, panel_1);
		sl_panel_1.putConstraint(SpringLayout.WEST, clearButton, 10, SpringLayout.WEST, panel_1);
		sl_panel_1.putConstraint(SpringLayout.EAST, clearButton, 127, SpringLayout.WEST, panel_1);
		clearButton.setBounds(323, 56, 109, 35);
		panel_1.add(clearButton);
		clearButton.setFont(new Font("������ٻ����", Font.BOLD, 18));
		clearButton.setBackground(new Color(186, 85, 211));
		clearButton.setForeground(Color.WHITE);
		
		JButton startStopButton = new JButton("\u5F00\u59CB");
		startStopButton.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode()==' ') {
					System.out.println("�ն�");
				}
			}
		});
		startStopButton.setForeground(new Color(255, 255, 255));
		startStopButton.setFont(new Font("������ٻ����", Font.BOLD, 18));
		startStopButton.setBackground(new Color(50, 205, 50));
		sl_panel_1.putConstraint(SpringLayout.NORTH, startStopButton, 6, SpringLayout.SOUTH, clearButton);
		sl_panel_1.putConstraint(SpringLayout.WEST, startStopButton, 10, SpringLayout.WEST, panel_1);
		sl_panel_1.putConstraint(SpringLayout.SOUTH, startStopButton, 49, SpringLayout.SOUTH, clearButton);
		sl_panel_1.putConstraint(SpringLayout.EAST, startStopButton, 127, SpringLayout.WEST, panel_1);
		panel_1.add(startStopButton);
		
		Button helpButton = new Button("?");
		helpButton.setFont(new Font("Dialog", Font.BOLD, 35));
		helpButton.setForeground(new Color(255, 255, 255));
		helpButton.setBackground(new Color(0, 128, 0));
		sl_panel_1.putConstraint(SpringLayout.NORTH, helpButton, 0, SpringLayout.NORTH, panel_1);
		sl_panel_1.putConstraint(SpringLayout.WEST, helpButton, -85, SpringLayout.WEST, panel);
		sl_panel_1.putConstraint(SpringLayout.SOUTH, helpButton, -6, SpringLayout.NORTH, canveField);
		sl_panel_1.putConstraint(SpringLayout.EAST, helpButton, -29, SpringLayout.WEST, panel);
		panel_1.add(helpButton);
		helpButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(gol,
						"������Ϸ\n"+
								"��Ϸ�������������̣���ֻ�к�ɫ�ĸ��Ӵ���������\r\n" + 
								"��ÿ���غϽ���֮��ÿ�����Ӷ�������Χ8�����ӵ�״̬��û�ʧȥ������\r\n" + 
								"ȫ������ֻ��4����\r\n" + 
								"�������¹¶������һ��������Χ����������2���������ڻغϽ���ʱ������\r\n" + 
								"����Ҳ����ӵ�������һ��������Χ����������3������Ҳ�ڻغϽ���ʱ������\r\n" + 
								"�����ֳᷱ�����һ����������Χ��3�������������ڻغϽ���ʱ���������\r\n" + 
								"����֮�⣬���һ��������Χ��2����3�������������ڻغϽ���ʱ����ԭ������\r\n" + 
								"��Ϸ�淨��\r\n" + 
								"������Ϸ�ṩ�Ĺ��߸��ӿ�ݷ���أ��ۿ��о��� ������Ϸ ����\r\n" + 
								"ͨ����������Ĵ����ݱ䣬��������ɽ��й۲죬�о�������򵥵��߼�����,�ܲ�����������Ȥ�Ļ���������춯��������������������\r\n" + 
								"\r\n" + 
								"��Ϸ������\r\n" + 
								"\r\n" + 
								"��ݼ�\r\n" + 
								"�ո� ��ͣ/��ʼ \r\n" + 
								"Ctrl + R ѡ��������\r\n" + 
								"Ctrl + C ��ѡ���������ַ���\r\n" + 
								"Ctrl+ V ��������ת����ͼ��\r\n" + 
								"Ctrl + ��� ʹ�������\r\n" + 
								"\r\n" + 
								"��Ϸ���棺\r\n" + 
								"\r\n" + 
								"�м�����Ϸ����\r\n" + 
								"���Ե�����д������������������ۿ���ʾ\r\n" + 
								"\r\n" + 
								"���������/ͼ���б����������б����/ͳһģ�壬���Ե����ȡ\r\n" + 
								"������̺�����ɶ�Ӧ��ͼ���������½ǿ��Թۿ���Ӧ��ͼ��\r\n" + 
								"\r\n" + 
								"�ײ����ı��򣬿�������������ģ�ͨ��ѡ��ͼ��/�����õ���\r\n" + 
								"�ϴ���ť�� ����������ͼ�������ϴ���������\r\n" + 
								"������������\r\n" + 
								"������գ� �ѻ������������״̬\r\n" + 
								"\r\n" + 
								"�Ҽ��ײ����Ի�ø��๦��\r\n" + 
								"�ֱ���\r\n" + 
								"ѡ������ ����ѡ��������\r\n" + 
								"�������棺 ����ѡ�����������ı���\r\n" + 
								"��ԭ���ʣ����������ԭ�ɵ������\r\n" + 
								"���ֲ��ţ������ֲ��ź���ͣ\r\n" + 
								"�����л����л�����\r\n" + 
								"��ͣ����ͣ/��ʼ��Ϸ\r\n" + 
								"�޸����룺�����޸�\r\n" + 
								"����������Ϸ��������\r\n" + 
								""
								,
						"ʹ�ð���",
						JOptionPane.INFORMATION_MESSAGE);
			}
		});
		
		startStopButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(startStopButton.getText().equals("��ͣ")) {
					startStopButton.setText("��ʼ"); 
					startStopButton.setBackground(new Color(50, 205, 50));
					gol_1.stopCanves(false);
				}else {
					startStopButton.setText("��ͣ");
					startStopButton.setBackground(new Color(255, 69, 0));
					gol_1.stopCanves(true);
				} 
				
			}
		});
		
		clearButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				gol_1.changepoint();
				gol_1.clear();
			}
		});

		deleteButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				canveField.setText("\u5C06golly\u7801\u7C98\u8D34\u5728\u6B64\u5904");
			}
		});

		JPopupMenu popupMenu = new JPopupMenu();
		addPopup(this, popupMenu);

		getContentPane().addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// ����������²�̧��
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// ��갴��
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// ����ͷ�

				// ���������Ҽ�������ʾ�����˵�
				if (e.isMetaDown()) {
					showPopupMenu(e.getComponent(), e.getX(), e.getY());
				}
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// �������������
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// ����뿪�������
			}
		});

	}

	/**
	 * �����б�
	 */
	private void getCanves() {
		// �����ݿ��ȡ�û��Ļ���ģ��
		canves = new Vector<Life>();
		// ��������
		ClientSock.queryAllCancas(username, new CallBack() {
			@Override
			public void solve(DataJson data) {
				System.out.println("????");
				// TODO �Զ����ɵķ������
				Object o = data.getMp().get("allCanves");
				if (o != null && o instanceof Vector) {
					Vector<Life> vl = new Vector<>();
					Vector<Cancas> cl = (Vector<Cancas>) o;
					for (Cancas c : cl) {
						// ˫������new��֪��Ϊʲô����������������������������������������������������
						Life l = new Life(c.getData(), c.getX(), c.getY(), c.getCname());
						vl.add(l); 
					}
					canves = vl;
					// ��canves��������ӵ�list����
					String[] ini = new String[canves.size()];
					for (int i = 0; i < canves.size(); i++) {
						ini[i] = canves.get(i).cname + "";
					}  
					System.out.println(canves);
					
					try {
						System.out.println(canves.size());
						canves_number.setText("����"+String.valueOf(canves.size())+"��ģ��");
					}catch (Exception e) {
						// TODO: handle exception 
						System.err.println("Ϊ����ѡ��");
					} 
					list.setListData(ini);
				} else {
					System.out.println("����ʧ��");
				}
			}
		});
		
		
	
	}

	public static void showPopupMenu(Component invoker, int x, int y) {
		musicplayHandle musicplay = GamePage.musicplay;
		Main main=new Main();
		
		// ���� �����˵� ����
		JPopupMenu popupMenu = new JPopupMenu();

		// ���� һ���˵�
		JMenuItem canvas_save = new JMenuItem("��������");
		JMenuItem changeLogin = new JMenuItem("�޸�����");
		JMenuItem Restore_brush = new JMenuItem("��ԭ����");
		JMenu musicPlayer = new JMenu("���ֲ���");
		JMenuItem Select_area = new JMenuItem("ѡ������");

		// ���� �����˵�
		JMenuItem startBgm = new JMenuItem("����");
		JMenuItem stopBgm = new JMenuItem("��ͣ");
		JMenuItem changeBgm = new JMenuItem("�л�");
		// ��� �����˵� �� "���ֲ���"һ���˵�
		musicPlayer.add(startBgm);
		musicPlayer.add(stopBgm);
		musicPlayer.add(changeBgm);
		// ��� һ���˵� �� �����˵�
		popupMenu.add(canvas_save);
		popupMenu.add(Restore_brush);
		popupMenu.add(Select_area);
		popupMenu.addSeparator(); // ���һ���ָ���
		popupMenu.add(musicPlayer);
		popupMenu.add(changeLogin);

		// ��Ӳ˵���ĵ��������
		canvas_save.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				cf.setText(gol.copyrange());
				JOptionPane.showMessageDialog(gol, "�Ѿ�����ת��Ϊ�ַ����������ı���", "����ɹ�", JOptionPane.INFORMATION_MESSAGE);
				System.out.println("�������� �����");
			}
		});
		Restore_brush.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gol.changepoint();
				JOptionPane.showMessageDialog(gol, "�����ѻ�ԭ", "�л��ɹ�", JOptionPane.INFORMATION_MESSAGE);
				System.out.println("��ԭ���� �����");

			}
		});
		Select_area.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("ѡ������ �����");
				JOptionPane.showMessageDialog(gol, "�뻬�����ѡ��Ŀ��", "�л��ɹ�", JOptionPane.INFORMATION_MESSAGE);
				gol.changeCanves();
			}
		});
		startBgm.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("���� �����");
				musicplay.start();
			}
		});
		stopBgm.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("��ͣ �����");
				musicplay.stop();
				
			}
		});
		
		changeBgm.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("�л� �����");
				 
				musicplay.change(musicplay.musicenames[(++musicnum)%musicplay.musicenames.length]);
				
				
			}
		});
		
		changeLogin.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("�޸����� �����");
				main.createAndChangeLogin(username);
				
				
			}
		});

		// ��ָ��λ����ʾ�����˵�
		popupMenu.show(invoker, x, y);
	}

	private static void addPopup(Component component, final JPopupMenu popup) {
		component.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			} 
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			} 
			private void showMenu(MouseEvent e) {
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		});
	}
}
