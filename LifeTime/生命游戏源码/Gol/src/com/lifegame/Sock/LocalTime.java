package com.lifegame.Sock;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LocalTime {
	private LocalTime(){ 
	}
	static String getTime() {
		SimpleDateFormat sf = new SimpleDateFormat("HH:mm:ss");
		return sf.format(new Date());
	}
}
