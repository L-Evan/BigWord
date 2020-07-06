package Test;

import java.awt.Button;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

public class musicplay {
	
	private boolean state = true;
	
	public static void main(String args[]) 
	{
		
		MusicThread mm = new MusicThread();
		Thread m = new Thread(mm);
		m.start();
		System.out.println("111");
	}
	
}



class MusicThread implements Runnable 
{ 

	
	public void run()
	{
	
		
		//�������
		Frame f = new Frame("����");
		//�����ǩ
		f.setTitle("���ֿ�����");
		//�����С
		f.setSize(200,100);
		//����λ��
		f.setLocation(400,200);
		//����ɼ�
		f.setVisible(true);
		//��ʽ����
		f.setLayout(new FlowLayout());
		//�ر�����
		f.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		//������ť
		Button play = new Button("����");
		play.setPreferredSize(new Dimension(80,50));
		
		//��ť���봰��
		f.add(play);
		//���������߳�
		Thread playThread = new Thread(new PlayThread());
		
		
		
		/*
		play.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent a) {
					
				System.out.println("111");
				playThread.start();
				play.setLabel("��ͣ");
				while(playThread != null) {
					if(a.=="��ͣ") {
						playThread.suspend();
						play.setLabel("����");
					}
					
				}
				System.out.println("222");
			}
			
		});	
	}
}	
*/
		
		
		
class PlayThread extends Thread{
	
	public void run() {
		
			File file = new File("C:\\Users\\Administrator\\Desktop\\�������� (�Ǥ� �褷����) - ���Λ� (��֮��).mp3");
			FileInputStream fis = null;
			try {
				fis = new FileInputStream(file);
			} catch (FileNotFoundException e) {
				// TODO �Զ����ɵ� catch ��
				e.printStackTrace();
			}
			BufferedInputStream stream = new BufferedInputStream(fis);
			Player player = null;
			try {
				player = new Player(stream);
			} catch (JavaLayerException e) {
				// TODO �Զ����ɵ� catch ��
				e.printStackTrace();
			}
			try {
				player.play();
			} catch (JavaLayerException e) {
				// TODO �Զ����ɵ� catch ��
				e.printStackTrace();
			}
		
	}
	
}