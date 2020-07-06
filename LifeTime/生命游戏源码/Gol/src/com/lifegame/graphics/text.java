package com.lifegame.graphics;

import java.awt.Panel;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.FlowLayout;
import java.awt.CardLayout;

public class text extends JFrame {
	public static void main(String args[]) {
		
		text t =new text();
		t.setSize(2000, 2000);;
		t.setVisible(true);
		
	}
	public text() {
		
		String canve=	"x = 52, y = 28, rule = B3/S23\r\n" + 
				"$22b3o3b3o$21bo2bo3bo2bo$2b4o18bo3bo18b4o$2bo3bo17bo3bo17bo3bo$2bo8bo\r\n" + 
				"12bo3bo12bo8bo$3bo2bo2b2o2bo25bo2b2o2bo2bo$8bo5bo7b3o3b3o7bo5bo$8bo5bo\r\n" + 
				"8bo5bo8bo5bo$8bo5bo8b7o8bo5bo$3bo2bo2b2o2bo2b2o4bo7bo4b2o2bo2b2o2bo2bo\r\n" + 
				"$2bo8bo3b2o4b11o4b2o3bo8bo$2bo3bo9b2o17b2o9bo3bo$2b4o11b19o11b4o$18bob\r\n" + 
				"o11bobo$21b11o$21bo9bo$22b9o$26bo$22b3o3b3o$24bo3bo2$23b3ob3o$23b3ob3o\r\n" + 
				"$22bob2ob2obo$22b3o3b3o$23bo5bo!\r\n" + 
				"" + 
				"";
		getContentPane().setLayout(null);
		
		Life l = new Life(canve);
		JPanel j = new JPanel();
		j.setBounds(160, 120, 218, 149);
		j.setLayout(new CardLayout(0, 0));
		j.add(l, "name_828035811091700");
		getContentPane().add(j); 
		j.remove(l); 
		j.add(l);
		
		GoL g = new GoL("abc");
		g.setBounds(0, 141, 107, -141);
		getContentPane().add(g);
		
		
	}
	
}
