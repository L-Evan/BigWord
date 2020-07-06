package com.lifegame.User;

import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JTextField;

public class JTextFieldHintListener implements FocusListener {
	// 初始化文本框
	private String mText; // 提示语
	private JTextField mTextField; // 文本位置
	// 定义类的方法

	public JTextFieldHintListener(String mText, JTextField textField) {
		this.mText = mText; 
		this.mTextField = textField;
		textField.setText(mText);
		textField.setForeground(Color.GRAY); // 设置文本颜色为灰色
	}

	@Override
	public void focusGained(FocusEvent e) {
		String temp = mTextField.getText();// 获取当前文本信息
		if (temp.equals(mText)) {
			mTextField.setForeground(Color.BLACK);
			mTextField.setText("");
		}
	}

	@Override
	public void focusLost(FocusEvent e) { 
		String temp = mTextField.getText();// 获取当前文本信息
		if (temp.equals("")) {
			mTextField.setForeground(Color.GRAY);
			mTextField.setText(mText);
		}
	}
}
