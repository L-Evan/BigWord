package com.lifegame.User;

import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JPasswordField;

public class JPasswordFieldHintListener implements FocusListener {
	// 初始化文本框
	private String mText; // 提示语
	private  JPasswordField mPasswordField; // 文本位置
	// 定义类的方法

	public JPasswordFieldHintListener(String mText, JPasswordField passwordField) {
		this.mText = mText; 
		this.mPasswordField = passwordField;
		passwordField.setText(mText);
		passwordField.setEchoChar((char) 0);
		passwordField.setForeground(Color.GRAY); // 设置文本颜色为灰色
	}

	@Override
	public void focusGained(FocusEvent e) {
		String temp = mPasswordField.getText();// 获取当前文本信息
		if (temp.equals(mText)) {
			mPasswordField.setForeground(Color.BLACK);
			mPasswordField.setEchoChar('*');
			mPasswordField.setText("");
		}
	}

	@Override
	public void focusLost(FocusEvent e) { 
		String temp = mPasswordField.getText();// 获取当前文本信息
		if (temp.equals("")) {
			mPasswordField.setEchoChar((char) 0);
			mPasswordField.setForeground(Color.GRAY);
			mPasswordField.setText(mText);
		}
	}
}