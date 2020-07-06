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
	//模拟按键
	Robot r; 
	// 画布数量提示
	JLabel canves_number;
	// 列表框
	private JList<String> list = new JList<String>();

	GamePage(String title, String username) {
		//模拟事件
		try {
			r=new Robot();
		} catch (AWTException e1) {
			// TODO 自动生成的 catch 块
			e1.printStackTrace();
		}
		
		// 初始化
		this.username = username;
		getContentPane().setBackground(new Color(112, 128, 144));
		setTitle(title);
		setSize(979, 688);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// 画布
		GoL gol_1 = new GoL(); 
		//滚动
        addMouseWheelListener(gol_1.new MouthLister());
		//添加监听
		addKeyListener(gol_1.new ListenKey());  
		JPanel panel = new JPanel();
		panel.setBackground(Color.DARK_GRAY);
		panel.setPreferredSize(new Dimension(100, 100));
		getContentPane().setLayout(new BorderLayout(10, 10));

		// 从数据库调取用户的画布模板

		// 上面的
		JPanel panel_2 = new JPanel();
		panel_2.setBackground(new Color(112, 128, 144));
		getContentPane().add(panel_2, BorderLayout.CENTER);

		getCanves();
		SpringLayout sl_panel_2 = new SpringLayout();
		sl_panel_2.putConstraint(SpringLayout.NORTH, gol_1, 10, SpringLayout.NORTH, panel_2);
		sl_panel_2.putConstraint(SpringLayout.WEST, gol_1, 10, SpringLayout.WEST, panel_2);
		sl_panel_2.putConstraint(SpringLayout.SOUTH, gol_1, 0, SpringLayout.SOUTH, panel_2);
		panel_2.setLayout(sl_panel_2);
		// -----画布------------------------------
		this.gol = gol_1;
		gol_1.setLayout(null); 
		gol_1.init();
		gol_1.start();
		//打开音乐
		musicplay = new musicplayHandle();
		musicplay.start(); 
		list.setBackground(new Color(95, 158, 160));
		list.setFont(new Font("方正中倩简体", Font.PLAIN, 19));
		list.setBounds(759, 10, 188, 483);  
		// 添加选项选中状态被改变的监听器
		list.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				// 获取所有被选中的选项索引
				int[] indices = list.getSelectedIndices();
				// 获取被选中的选项索引
				int index_canve = list.getSelectedIndex();  
				// 获取选项数据的 ListModel
				ListModel<String> listModel = list.getModel();
				// 输出选中的选项
				for (int index : indices) {
					System.out.println("选中: " + index + " = " + listModel.getElementAt(index));

				}
				System.out.println(index_canve);
				if(index_canve==-1) {
					index_canve = indices.length>0?indices[0]:canves.size()-1;
				} 
				// index_canve = canves.size()-1;
				// 选择合并的 有问题不知道什么问题
				if (index_canve > 0) {
					Life l = canves.get(index_canve); 
					gol.setMerge(l);
					canveField.setText("x = "+l.getx()+",y = "+l.gety()+", rule = 1 ,"+l.getcanvestr());
					l.setSize(300, 300);
					l.setVisible(true);  
					//remove 后需要update
					panel.removeAll(); 
					panel.updateUI();
					panel.add(l);
					  
					System.out.println(l); 
				} else {
					System.out.println("没有选中");
				}
				
			}
		});
		;
		// 列表list的滚动条
		JScrollPane scrollPane = new JScrollPane(list);
		sl_panel_2.putConstraint(SpringLayout.NORTH, scrollPane, 10, SpringLayout.NORTH, panel_2);
		sl_panel_2.putConstraint(SpringLayout.SOUTH, scrollPane, 0, SpringLayout.SOUTH, panel_2);
		sl_panel_2.putConstraint(SpringLayout.EAST, gol_1, -10, SpringLayout.WEST, scrollPane);
		sl_panel_2.putConstraint(SpringLayout.WEST, scrollPane, -175, SpringLayout.EAST, panel_2);
		sl_panel_2.putConstraint(SpringLayout.EAST, scrollPane, -10, SpringLayout.EAST, panel_2);
		panel_2.add(scrollPane);
		panel_2.add(gol_1);

		// 设置默认选中项
		list.setSelectedIndex(0);
		// 画布数量提示
		canves_number = new JLabel();
		canves_number.setForeground(new Color(105, 105, 105));
		canves_number.setHorizontalAlignment(SwingConstants.CENTER);
		canves_number.setFont(new Font("方正中倩简体", Font.BOLD, 15));
		canves_number.setBackground(new Color(30, 144, 255));
		canves_number.setText("您有 " + canves.size() + " 个模板");
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

		// 文本框
		canveField = new JTextField();

		sl_panel_1.putConstraint(SpringLayout.NORTH, canveField, 51, SpringLayout.NORTH, panel_1);
		sl_panel_1.putConstraint(SpringLayout.WEST, canveField, 10, SpringLayout.WEST, panel_1);
		sl_panel_1.putConstraint(SpringLayout.SOUTH, canveField, -107, SpringLayout.SOUTH, panel_1);
		sl_panel_1.putConstraint(SpringLayout.EAST, canveField, -26, SpringLayout.WEST, panel);
		canveField.setBounds(177, 13, 136, 28);
		panel_1.add(canveField);
		canveField.setFont(new Font("方正中倩简体", Font.PLAIN, 15));
		canveField.setForeground(new Color(105, 105, 105));
		canveField.setText("\u5C06golly\u7801\u7C98\u8D34\u5728\u6B64\u5904");
		canveField.setColumns(10);
		this.cf = canveField;
		canveField.addFocusListener(new FocusListener() {

			@Override
			public void focusGained(FocusEvent e) {
				// TODO 自动生成的方法存根
				if (canveField.getText().equals("将golly码粘贴在此处")) {
					canveField.setText(" ");
				} 
			}

			@Override
			public void focusLost(FocusEvent e) {
				// TODO 自动生成的方法存根

			}

		});

		// 提交按钮
		JButton uploadButton = new JButton("\u4E0A\u4F20");
		sl_panel_1.putConstraint(SpringLayout.NORTH, uploadButton, 6, SpringLayout.SOUTH, canveField);
		sl_panel_1.putConstraint(SpringLayout.SOUTH, uploadButton, 48, SpringLayout.SOUTH, canveField);
		uploadButton.setBounds(187, 55, 71, 35);
		panel_1.add(uploadButton);
		uploadButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (canveField.getText().equals("") || canveField.getText().equals("将golly码粘贴在此处")) {
					JOptionPane.showMessageDialog(gol_1, "请确认您输入了正确的gol图形码", "无法上传", JOptionPane.INFORMATION_MESSAGE);
				} else {
					int result = JOptionPane.showConfirmDialog(gol_1, "确认你要提交？", "提示", JOptionPane.YES_NO_OPTION);
					System.out.println("选择结果: " + result);
					if (result == 0) {
						String inputContent = JOptionPane.showInputDialog(gol_1, "请为该图形命名:", "在这里输入名字");
						String canve = canveField.getText();
						Life l = new Life(canve);
						System.out.println(username + "?");
						ClientSock.insertCanvas(l, inputContent, username, new CallBack() {
							@Override
							public void solve(DataJson data) {
								// TODO 自动生成的方法存根
								if (data != null && data.getSta() > 0) {
									System.out.println("l:" + l.toString());
									System.out.println("文本框里输入了：" + canve);
									getCanves();
								} else {
									System.out.println("失败");
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

		uploadButton.setFont(new Font("方正中倩简体", Font.BOLD, 18));
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
		deleteButton.setFont(new Font("方正中倩简体", Font.BOLD, 18));
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
		clearButton.setFont(new Font("方正中倩简体", Font.BOLD, 18));
		clearButton.setBackground(new Color(186, 85, 211));
		clearButton.setForeground(Color.WHITE);
		
		JButton startStopButton = new JButton("\u5F00\u59CB");
		startStopButton.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode()==' ') {
					System.out.println("空额");
				}
			}
		});
		startStopButton.setForeground(new Color(255, 255, 255));
		startStopButton.setFont(new Font("方正中倩简体", Font.BOLD, 18));
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
						"生命游戏\n"+
								"游戏区域是网格棋盘，但只有红色的格子代表生命。\r\n" + 
								"在每个回合结束之后，每个格子都根据周围8个格子的状态获得或失去生命。\r\n" + 
								"全部规则只有4条：\r\n" + 
								"生命害怕孤独，如果一个生命周围的生命少于2个，它就在回合结束时死亡；\r\n" + 
								"生命也讨厌拥挤，如果一个生命周围的生命超过3个，它也在回合结束时死亡；\r\n" + 
								"生命会繁殖，如果一个死格子周围有3个生命，它就在回合结束时获得生命；\r\n" + 
								"除此之外，如果一个生命周围有2或者3个生命，它就在回合结束时保持原样。”\r\n" + 
								"游戏玩法：\r\n" + 
								"可以游戏提供的工具更加快捷方便地，观看研究“ 生命游戏 “，\r\n" + 
								"通过生命方格的创建演变，利用其规律进行观察，研究，用最简单的逻辑规则,能产生出复杂有趣的活动。甚至创造动画，“真正的生命”。\r\n" + 
								"\r\n" + 
								"游戏操作：\r\n" + 
								"\r\n" + 
								"快捷键\r\n" + 
								"空格： 暂停/开始 \r\n" + 
								"Ctrl + R 选择区域功能\r\n" + 
								"Ctrl + C 将选择区域复制字符串\r\n" + 
								"Ctrl+ V 将剪贴板转换成图案\r\n" + 
								"Ctrl + 点击 使画布清空\r\n" + 
								"\r\n" + 
								"游戏界面：\r\n" + 
								"\r\n" + 
								"中间是游戏区域\r\n" + 
								"可以点击进行创建生命，构建生命观看演示\r\n" + 
								"\r\n" + 
								"左边是生命/图案列表，保存所有列表个人/统一模板，可以点击获取\r\n" + 
								"点击棋盘后会生成对应的图案，在右下角可以观看对应的图案\r\n" + 
								"\r\n" + 
								"底部是文本框，可以输入加密密文（通过选择图案/区域后得到）\r\n" + 
								"上传按钮： 将加密密文图案解析上传到服务器\r\n" + 
								"清除：密文清空\r\n" + 
								"画布清空： 把画布清成无生命状态\r\n" + 
								"\r\n" + 
								"右键底部可以获得更多功能\r\n" + 
								"分别是\r\n" + 
								"选择区域： 开启选择区域功能\r\n" + 
								"画布保存： 保存选择的区域键入文本框\r\n" + 
								"还原画笔：将鼠标点击还原成点击生命\r\n" + 
								"音乐播放：对音乐播放和暂停\r\n" + 
								"音乐切换：切换音乐\r\n" + 
								"暂停：暂停/开始游戏\r\n" + 
								"修改密码：密码修改\r\n" + 
								"帮助：对游戏操作介绍\r\n" + 
								""
								,
						"使用帮助",
						JOptionPane.INFORMATION_MESSAGE);
			}
		});
		
		startStopButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(startStopButton.getText().equals("暂停")) {
					startStopButton.setText("开始"); 
					startStopButton.setBackground(new Color(50, 205, 50));
					gol_1.stopCanves(false);
				}else {
					startStopButton.setText("暂停");
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
				// 鼠标点击（按下并抬起）
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// 鼠标按下
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// 鼠标释放

				// 如果是鼠标右键，则显示弹出菜单
				if (e.isMetaDown()) {
					showPopupMenu(e.getComponent(), e.getX(), e.getY());
				}
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// 鼠标进入组件区域
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// 鼠标离开组件区域
			}
		});

	}

	/**
	 * 更新列表
	 */
	private void getCanves() {
		// 从数据库调取用户的画布模板
		canves = new Vector<Life>();
		// 发起请求
		ClientSock.queryAllCancas(username, new CallBack() {
			@Override
			public void solve(DataJson data) {
				System.out.println("????");
				// TODO 自动生成的方法存根
				Object o = data.getMp().get("allCanves");
				if (o != null && o instanceof Vector) {
					Vector<Life> vl = new Vector<>();
					Vector<Cancas> cl = (Vector<Cancas>) o;
					for (Cancas c : cl) {
						// 双开不能new不知道为什么。。。。。。。。。。。。。。。。。。。。。。。。。。
						Life l = new Life(c.getData(), c.getX(), c.getY(), c.getCname());
						vl.add(l); 
					}
					canves = vl;
					// 将canves的名称添加到list表中
					String[] ini = new String[canves.size()];
					for (int i = 0; i < canves.size(); i++) {
						ini[i] = canves.get(i).cname + "";
					}  
					System.out.println(canves);
					
					try {
						System.out.println(canves.size());
						canves_number.setText("您有"+String.valueOf(canves.size())+"个模板");
					}catch (Exception e) {
						// TODO: handle exception 
						System.err.println("为主动选中");
					} 
					list.setListData(ini);
				} else {
					System.out.println("请求失败");
				}
			}
		});
		
		
	
	}

	public static void showPopupMenu(Component invoker, int x, int y) {
		musicplayHandle musicplay = GamePage.musicplay;
		Main main=new Main();
		
		// 创建 弹出菜单 对象
		JPopupMenu popupMenu = new JPopupMenu();

		// 创建 一级菜单
		JMenuItem canvas_save = new JMenuItem("画布保存");
		JMenuItem changeLogin = new JMenuItem("修改密码");
		JMenuItem Restore_brush = new JMenuItem("还原画笔");
		JMenu musicPlayer = new JMenu("音乐播放");
		JMenuItem Select_area = new JMenuItem("选择区域");

		// 创建 二级菜单
		JMenuItem startBgm = new JMenuItem("播放");
		JMenuItem stopBgm = new JMenuItem("暂停");
		JMenuItem changeBgm = new JMenuItem("切换");
		// 添加 二级菜单 到 "音乐播放"一级菜单
		musicPlayer.add(startBgm);
		musicPlayer.add(stopBgm);
		musicPlayer.add(changeBgm);
		// 添加 一级菜单 到 弹出菜单
		popupMenu.add(canvas_save);
		popupMenu.add(Restore_brush);
		popupMenu.add(Select_area);
		popupMenu.addSeparator(); // 添加一条分隔符
		popupMenu.add(musicPlayer);
		popupMenu.add(changeLogin);

		// 添加菜单项的点击监听器
		canvas_save.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				cf.setText(gol.copyrange());
				JOptionPane.showMessageDialog(gol, "已经将其转化为字符串输入至文本框", "保存成功", JOptionPane.INFORMATION_MESSAGE);
				System.out.println("画布保存 被点击");
			}
		});
		Restore_brush.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				gol.changepoint();
				JOptionPane.showMessageDialog(gol, "画笔已还原", "切换成功", JOptionPane.INFORMATION_MESSAGE);
				System.out.println("还原画笔 被点击");

			}
		});
		Select_area.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("选择区域 被点击");
				JOptionPane.showMessageDialog(gol, "请滑动鼠标选择目标", "切换成功", JOptionPane.INFORMATION_MESSAGE);
				gol.changeCanves();
			}
		});
		startBgm.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("播放 被点击");
				musicplay.start();
			}
		});
		stopBgm.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("暂停 被点击");
				musicplay.stop();
				
			}
		});
		
		changeBgm.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("切换 被点击");
				 
				musicplay.change(musicplay.musicenames[(++musicnum)%musicplay.musicenames.length]);
				
				
			}
		});
		
		changeLogin.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("修改密码 被点击");
				main.createAndChangeLogin(username);
				
				
			}
		});

		// 在指定位置显示弹出菜单
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
