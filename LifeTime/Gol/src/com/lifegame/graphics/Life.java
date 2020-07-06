package com.lifegame.graphics;
 
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.io.Serializable;
import java.lang.Math; 
import javax.swing.JFrame;
import javax.swing.JPanel;
/**
 * 生命类  代表每一个生命   自己也是一个jframe
 * @author  
 *
 */
public class Life extends JPanel implements Serializable {
	//颜色
	private Color cell =new Color(255,0,0); 
    private Color space =new Color(48,48,48); 
    private Color backcolor = new Color(70,70,70); 
    //life name
    public String cname;
	//画布
	private String  canvestr;
	// x y 轴
	private int X,Y; 
	//周期和
	String cycle;
	//创建日期
	String createdate; 
	private boolean table[][];
	//细胞大小   
	private int CELL_Size ; 
	//整合字符串
	private String string;  
	private static final Life biglife = new Life("x = 16,y = 39, rule = 1 ,7bo$7bo$7bo$2b12o$6b2o$6bobo$4b3ob2o$3b2o4b2o$2b2o"
			+ "6b2o$b2o8b2o$12b2o$3bo$3bo$3b2o3bo$4bo3bo$2b11o$3o5bo$o7bo$5b7o$8bo$8bo"
			+ "$8bo$16o2$7b3o$6b2ob4o$2b5o5b3o$b2o11b2o$5b7o$10b3o$b3o$bobo4b6o$b3o4bo4bo$8bo2b3o$7b2o2b2o$7bo$7bo$7bo$7bo!"); 
	@Override public void paint(Graphics g) { 
    	super.paint(g); 
        update(g);
    }
	public String toString() {
			return canvestr;
	} 
	 public void update(Graphics g) {   
		//找到最小格
		 int ycell = getSize().height/Y;
		 int xcell = getSize().width/X;  
		CELL_Size = Math.min(xcell,ycell)  ;
		//大生命绘制
		if(CELL_Size==0) {
			System.out.println("没了"); 
			if(getSize().height>40 && getSize().width>40) {
				biglife.setSize(getSize());  
				biglife.update(g);
			}
			return; 
		}
 		g.setColor(space); 
 		g.fillRect(0, 0, getSize().width,getSize().height);
 		g.setColor(cell); 
 		int chax = getSize().width-X*CELL_Size;
 		int chay = getSize().height-Y*CELL_Size;
		 for (int y = 0; y < Y; y++)
	            for (int x = 0; x <X;x++) {  
	            	//(isupdata ||  lasttable[x][y]!=table[x][y]) &&
					if(  table[y][x] ) {  
						if(CELL_Size==1)
							g.fillRect(x * CELL_Size+chax/2, y * CELL_Size+chay/2, CELL_Size , CELL_Size );
						else 
							g.fillRect(x * CELL_Size+chax/2, y * CELL_Size+chay/2, CELL_Size - 1, CELL_Size - 1); 
					}
	            }      
 
	};
	/**
	 * 接收画布对应的字符串  
	*/
	public Life(String string) { 
		setMerge(string); 
	}
	/**
	 * 接收画布对应的字符串
	*/
	public Life(String canve,int x,int y) {  
		table = MatrixTools.toMatrix(canve,x,y);
		X = x;
		Y = y; 
		canvestr = canve;
	}
	/**
	 * 接收画布对应的字符串
	*/
	public Life(String canve,int x,int y,String name) {  
		table = MatrixTools.toMatrix(canve,x,y);
		X = x;
		Y = y;
		this.cname = name;
		canvestr = canve;
	}
	/**
     * 设置合并的矩阵
     */
    void setMerge(String string){
    	String list[] = MatrixTools.Transformat(string); 
    	X = Integer.valueOf(list[0]);  
		Y = Integer.valueOf(list[1]); 
		System.out.println(X+" "+Y);
		table = MatrixTools.toMatrix(list[3],X,Y);   
		canvestr = list[3];
    }
    public String getcanvestr(){
    	return this.canvestr;
    }
    public boolean[][] getTable(){
    	return this.table;
    }
    public int getx(){
    	return this.X;
    }
    public int gety(){
    	return this.Y;
    }
    /**
     * 测试用的
     * @param args
     */
    public static void main(String args[]) {
    	String tp = "x = 157, y = 98, rule = B3/S23\r\n" + 
				"$80b2o$80bobo$80bo$83bo$83b2o$83b2o$82b2o$78b2ob2o$78bo4bo$78bob4o$82b\r\n" + 
				"2o$82b2o$82b2o$84bob3o$80bo2bo$79b5o6bo$79bo2b2o5bobo$77bobo10bo$76b3o\r\n" + 
				"4bo$76bo2b2o$80b3o$80b2o10bo$82bo8bobo3bo$80bobo8bobo2bobo$73b2o4b2o\r\n" + 
				"11bo4bo$72b2o5b2obo$74bo4b2ob2obo2b2o$79b3obobo2bobo$70b2o3b2o2b2o7bo\r\n" + 
				"10bo$69b2o3b2o15bo6bobo3bo$71bo2b3o14b2o5bobo2bobo$73b4o14b2o6bo4bo$\r\n" + 
				"72bobo15b2o$71b2o13b2ob2o$71bobo12bo4bo$70bo15bob4o14bo$71b2o17b2o13bo\r\n" + 
				"bo3bo$90b2o13bobo2bobo$90b2o14bo4bo$92bo$88bo2bo4bo$87b5o4bo$87bo2b2o\r\n" + 
				"4bo16bo$85bobo12bo11bobo3bo$84b3o4bo7bobo10bobo2bobo$84bo2b2o11bo12bo\r\n" + 
				"4bo$88b3o$58bobo27b2o$57bo2bo29bo$56b2o30bobo11bo17bo$55bo25b2o4b2o12b\r\n" + 
				"obo3bo11bobo3bo$54b4o22b2o5b2obo10bobo2bobo10bobo2bobo$53bo4bo6bo16bo\r\n" + 
				"4b2ob2obo8bo4bo12bo4bo$53bo2bo5b2obo21b3obobo$53bo2bo5bo15b2o3b2o2b2o$\r\n" + 
				"54bo7bo14b2o3b2o$55b4obo6b2o10bo2b3o24bo17bo$56bo3bo4bo4bo10b4o23bobo\r\n" + 
				"3bo11bobo3bo$57bo6bo15bobo25bobo2bobo10bobo2bobo$57b5o2bo5bo8b2o28bo4b\r\n" + 
				"o12bo4bo$64b6o9bobo$57b5o16bo$57bo21b2o$56bo3bo2bo24b2o26bo17bo$14bobo\r\n" + 
				"12b2o24b4obo25bo4bo23bobo3bo11bobo3bo$13bo2bo10bo4bo21bo7bob2o19bo29bo\r\n" + 
				"bo2bobo10bobo2bobo$12b2o7bo4bo26bo2bo5bo2bo6bo12bo5bo24bo4bo12bo4bo$\r\n" + 
				"11bo6b2obo4bo5bo20bo2bo5bo2bo4bobo12b6o9b2o$10b6o2bo7b6o21bo4bo4b2o6b\r\n" + 
				"2o5bo20b4o$7b2o7bo3b4o30b4o18bobo19b2ob2o$6bo3b3obo4bo35bo21b2o5bo14b\r\n" + 
				"2o22bo17bo$5bo3bo3b2obo2b2obo2bo30b2o24bobo5bo31bobo3bo11bobo3bo$5bo5b\r\n" + 
				"2o3bo5bo6b2o26bo2bo22b2o5bo31bobo2bobo10bobo2bobo$5b3o3b4obo7bobo2b3o\r\n" + 
				"26bobo27bobo32bo4bo12bo4bo$14bo9bo6bo57b2o$5b3o6bobo7bo6bo5bo73b2o$4bo\r\n" + 
				"5bo5bo8bo4bo4bobo3b2o2b2o2b2o2b2o2b2o2b2o2b2o2b2o2b2o2b3ob2o27bo2bo3b\r\n" + 
				"3o$3bo3b2obo3b2o11b2o7b2o3b2o2b2o2b2o2b2o2b2o2b2o2b2o2b2o2b2o2b4o2bo\r\n" + 
				"27b2o6bo10bo17bo$3bo3bo6b3o62bob2o35bo10bobo3bo11bobo3bo$3bo3b2obo3b2o\r\n" + 
				"11b2o7b2o3b2o2b2o2b2o2b2o2b2o2b2o2b2o2b2o2b2o2b2o50bobo2bobo10bobo2bob\r\n" + 
				"o$4bo5bo5bo8bo4bo4bobo3b2o2b2o2b2o2b2o2b2o2b2o2b2o2b2o2b2o2b2o34b2o15b\r\n" + 
				"o4bo12bo4bo$5b3o6bobo7bo6bo5bo75bo2bo$14bo9bo6bo80b2o$5b3o3b4obo7bobo\r\n" + 
				"2b3o78bo$5bo5b2o3bo5bo6b2o50b2o27bo10b2o14bo$5bo3bo3b2obo2b2obo2bo54b\r\n" + 
				"2ob2o25bo4bo5b2o13bobo3bo$6bo3b3obo4bo61b4o6b2o19b3o21bobo2bobo$7b2o7b\r\n" + 
				"o3b4o58b2o6b2ob3o41bo4bo$10b6o2bo7b6o59b5o$11bo6b2obo4bo5bo59b3o$12b2o\r\n" + 
				"7bo4bo95b2o$13bo2bo10bo4bo88b2ob4o$14bobo12b2o91b6o$123b4o!\r\n" + 
				""; 
    	 //创建及设置窗口          
    	Life frame = new Life(tp);  
        Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize(); 
		int width = (int)screensize.getWidth();  
		int height = (int)screensize.getHeight();  
		frame.setLocation(width/5, height/5);  
		frame.setSize(frame.X,frame.Y);
		frame.setVisible(true);  
    }
}
