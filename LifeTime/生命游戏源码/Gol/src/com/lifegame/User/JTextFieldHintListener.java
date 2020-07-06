package com.lifegame.User;

import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JTextField;

public class JTextFieldHintListener implements FocusListener {
	// ��ʼ���ı���
	private String mText; // ��ʾ��
	private JTextField mTextField; // �ı�λ��
	// ������ķ���

	public JTextFieldHintListener(String mText, JTextField textField) {
		this.mText = mText; 
		this.mTextField = textField;
		textField.setText(mText);
		textField.setForeground(Color.GRAY); // �����ı���ɫΪ��ɫ
	}

	@Override
	public void focusGained(FocusEvent e) {
		String temp = mTextField.getText();// ��ȡ��ǰ�ı���Ϣ
		if (temp.equals(mText)) {
			mTextField.setForeground(Color.BLACK);
			mTextField.setText("");
		}
	}

	@Override
	public void focusLost(FocusEvent e) { 
		String temp = mTextField.getText();// ��ȡ��ǰ�ı���Ϣ
		if (temp.equals("")) {
			mTextField.setForeground(Color.GRAY);
			mTextField.setText(mText);
		}
	}
}
