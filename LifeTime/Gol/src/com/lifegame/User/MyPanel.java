package com.lifegame.User;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class MyPanel extends JPanel{
	public void paintComponent(Graphics g){
        super.paintComponent(g);
        //ªÊ÷∆“ª’≈±≥æ∞Õº∆¨
        Image image = new ImageIcon("image\\BackGround.jpg").getImage();
        g.drawImage(image, 0, 0, this);
    }
}
