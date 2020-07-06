package com.lifegame.Music; 

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

public class music {
	
	static Player player = null;

	public static void main(String[] args) throws FileNotFoundException, JavaLayerException {
			File file = new File("raw\\a.mp3");
			FileInputStream fis = new FileInputStream(file);
			BufferedInputStream stream = new BufferedInputStream(fis);
			Player player = new Player(stream);
			player.play(); 
	}
} 