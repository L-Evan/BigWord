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
	
		
		//窗体对象
		Frame f = new Frame("音乐");
		//窗体标签
		f.setTitle("音乐控制器");
		//窗体大小
		f.setSize(200,100);
		//窗体位置
		f.setLocation(400,200);
		//窗体可见
		f.setVisible(true);
		//流式布局
		f.setLayout(new FlowLayout());
		//关闭团体
		f.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		//创建按钮
		Button play = new Button("播放");
		play.setPreferredSize(new Dimension(80,50));
		
		//按钮加入窗体
		f.add(play);
		//创建播放线程
		Thread playThread = new Thread(new PlayThread());
		
		
		
		/*
		play.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent a) {
					
				System.out.println("111");
				playThread.start();
				play.setLabel("暂停");
				while(playThread != null) {
					if(a.=="暂停") {
						playThread.suspend();
						play.setLabel("播放");
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
		
			File file = new File("C:\\Users\\Administrator\\Desktop\\出羽良彰 (でわ よしあき) - 海の涙 (海之泪).mp3");
			FileInputStream fis = null;
			try {
				fis = new FileInputStream(file);
			} catch (FileNotFoundException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
			BufferedInputStream stream = new BufferedInputStream(fis);
			Player player = null;
			try {
				player = new Player(stream);
			} catch (JavaLayerException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
			try {
				player.play();
			} catch (JavaLayerException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
		
	}
	
}