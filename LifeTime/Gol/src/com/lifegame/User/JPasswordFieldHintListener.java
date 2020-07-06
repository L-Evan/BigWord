package com.lifegame.User;

import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JPasswordField;

public class JPasswordFieldHintListener implements FocusListener {
	// ��ʼ���ı���
	private String mText; // ��ʾ��
	private  JPasswordField mPasswordField; // �ı�λ��
	// ������ķ���

	public JPasswordFieldHintListener(String mText, JPasswordField passwordField) {
		this.mText = mText; 
		this.mPasswordField = passwordField;
		passwordField.setText(mText);
		passwordField.setEchoChar((char) 0);
		passwordField.setForeground(Color.GRAY); // �����ı���ɫΪ��ɫ
	}

	@Override
	public void focusGained(FocusEvent e) {
		String temp = mPasswordField.getText();// ��ȡ��ǰ�ı���Ϣ
		if (temp.equals(mText)) {
			mPasswordField.setForeground(Color.BLACK);
			mPasswordField.setEchoChar('*');
			mPasswordField.setText("");
		}
	}

	@Override
	public void focusLost(FocusEvent e) { 
		String temp = mPasswordField.getText();// ��ȡ��ǰ�ı���Ϣ
		if (temp.equals("")) {
			mPasswordField.setEchoChar((char) 0);
			mPasswordField.setForeground(Color.GRAY);
			mPasswordField.setText(mText);
		}
	}
}