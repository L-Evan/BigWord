package com.lifegame.Music;

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
import java.io.IOException;

import org.w3c.dom.events.EventException;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;
import javazoom.jl.player.advanced.AdvancedPlayer;
import javazoom.jl.player.advanced.PlaybackEvent;
import javazoom.jl.player.advanced.PlaybackListener;

public class musicplayHandle {
	private boolean state = true;
	PlayThread musicrun;
	public String musicename;//"raw\\Super-Mario-Bros.mp3", "raw\\Zelda1.mp3"  "raw\\Super.mp3"
	public String[] musicenames = { "raw\\a.mp3" ,"raw\\Zelda1.mp3","raw\\Super.mp3" };

	public musicplayHandle() {
		musicename ="raw\\a.mp3";
		// 创建播放线程
		musicrun = new PlayThread(musicename);
		Thread playThread = new Thread(musicrun);
		musicrun.th = playThread;
		playThread.start();
	}

	public musicplayHandle(String name) {
		// 创建播放线程
		musicename = name;
		musicrun = new PlayThread(musicename);
		Thread playThread = new Thread(musicrun);
		musicrun.th = playThread;
		playThread.start();
	}

	public static void main(String args[]) {
		musicplayHandle m = new musicplayHandle();
		if (m.musicrun != null) {
			// m.musicrun.start();
		}
	}

	public void start() {
		musicrun.start();
		System.out.println("播放");
		// 调用了start
	}

	public void stop() {
		musicrun.stop();
	}

	public void change(String s) {
		musicrun.change(s);
	}
}

class PlayThread implements Runnable {
	public AdvancedPlayer player;
	public Thread th;
	private int pausedOnFrame = 0;
	int sta = 0;
	String musicname;
	BufferedInputStream stream;
	FileInputStream fis;

	PlayThread(String s) {
		musicname = s;
	}

	void init() {
		File file = new File(musicname);
		fis = null;
		try {
			fis = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		stream = new BufferedInputStream(fis);
		try {
			player = new AdvancedPlayer(fis);
			player.setPlayBackListener(new PlaybackListener() {
				@Override
				public void playbackFinished(PlaybackEvent event) {
					pausedOnFrame = event.getFrame();  
					System.out.println(pausedOnFrame);
				}
			});
		} catch (JavaLayerException e1) {
			// TODO 自动生成的 catch 块
			e1.printStackTrace();
			System.err.println("音乐有问题");
		}
		System.err.println(player);
	}

	public void run() {
		while (true) {
			// 播放
			if (sta == 1) {
				try { 
					System.out.println("play");
					player.play(pausedOnFrame,Integer.MAX_VALUE); 
					
				} catch (Exception e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
					player = null; 
					sta = 0;
					System.out.println("歌泊完了"); 
				}

			} else if (sta == 0) {
				try {
					th.sleep(1);
				} catch (InterruptedException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
			}

		}
	}

	/**
	 * 暂停
	 */
	void stop() {
		System.out.println(this);
		System.err.println(player);
		if (player != null) {
			sta = 0; 
			try {
				player.stop();
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("歌曲播放结束！");
				player = null; 
				return ;
			} 
			try {
				fis.close();
				stream.close();
			} catch (IOException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}    
			
			player = null;
			System.err.println("tinl--------------e");
		}
		System.err.println("!!--------------e");
	}

	/**
	 * 继续
	 */
	void start() { 
		if (player == null) {
			init();
			System.out.println(player);
			System.out.println(this);
		}
		sta = 1;
	} 
	/**
	 * 切换
	 */
	void change(String s) {
		System.out.println(s);
		this.musicname = s;
		stop();
		pausedOnFrame = 0;
		start();
	}
}
