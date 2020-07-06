package com.lifegame.graphics; 
import java.applet.Applet;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.*;
import java.net.ServerSocket;
import java.util.HashMap;
import java.util.Vector;

import com.lifegame.Sock.CallBack;
import com.lifegame.Sock.DataJson;
import com.lifegame.Sock.ClientSock;
import com.lifegame.graphics.MatrixTools; 
 
/**
 * The Game Of Life Applet.������Ϸ��ʾ��
 * boolean[][] table�������������ϸ��״�����Ƚ�char[][] table ����
 * int[][] neighbors���� ���յ���grid���Ƶ����ĸ������ӵ��ھ���Ŀ��
 * �÷���
 * ����������cell��ctrl+�����cell�ÿ�
 *                  ctrl+���ּ�  ȡ����Ӧ�Ļ���  ctrl+���� ����Ƶ��  ���ֵ���������С
 * ����space�л��߳�����/ֹͣ��Applet
 */
public class GoL extends Applet implements Runnable{
	//
	public GoL(String username) {  
	} 
	//�����õ�
	public GoL() {   
		canves = new Vector<Life>();  
		canve = "x = 157, y = 98, rule = B3/S23\r\n" + 
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
		canves.add(new Life(canve)); 
		canve=	"x = 52, y = 28, rule = B3/S23\r\n" + 
				"$22b3o3b3o$21bo2bo3bo2bo$2b4o18bo3bo18b4o$2bo3bo17bo3bo17bo3bo$2bo8bo\r\n" + 
				"12bo3bo12bo8bo$3bo2bo2b2o2bo25bo2b2o2bo2bo$8bo5bo7b3o3b3o7bo5bo$8bo5bo\r\n" + 
				"8bo5bo8bo5bo$8bo5bo8b7o8bo5bo$3bo2bo2b2o2bo2b2o4bo7bo4b2o2bo2b2o2bo2bo\r\n" + 
				"$2bo8bo3b2o4b11o4b2o3bo8bo$2bo3bo9b2o17b2o9bo3bo$2b4o11b19o11b4o$18bob\r\n" + 
				"o11bobo$21b11o$21bo9bo$22b9o$26bo$22b3o3b3o$24bo3bo2$23b3ob3o$23b3ob3o\r\n" + 
				"$22bob2ob2obo$22b3o3b3o$23bo5bo!\r\n" + 
				"" + 
				"";  
	}   
	//˫����
	private Image offScreenImage; 
	//���
	private final int SIZE = 1000;//��ά��Ϸ����Ĵ�С,��SIZE*SIZE������  ���
	//��Ⱦ��SIZE
    private int xsize; 
    private  int ysize;
    //���λ�� ���ľ���λ�� ���񶨵�Ŵ��С
    private int pagex = 0;
    private int pagey = 0;
    //��ǰ����λ��    ����Ŵ��С
    private int cellx = 0;
    private int celly = 0;
    //������Ӧ��x��y  ����Ŵ��С
    private int lastx = 0;
    private int lasty = 0;
    private int nowy = 0;
    private int nowx = 0;
    //��ǰx y����ѡ������
    private int ny = 0;
    private int nx = 0;
    private int ey = 0;
    private int ex = 0;
    
    private  int CELL_Size =10;//ÿ����ʽ�ı߳���Java����ϵ��λ�� 
    //���ⰴť״̬
    private final char STOP_BUTTON = ' '; //��ͣ
    private final char CTRL = 17;//��� ctrl  
    //��ť״̬
    private boolean click; 
    private boolean button[]  = new boolean[257];
    //��ɫ
	private Color cell =new Color(255,0,0); //���
    private Color space =new Color(48,48,48); //��
    private Color rangecolor = new Color(0x88008000,true); //������ɫ ͸����
    private Color backcolor = new Color(70,70,70); //����
    //������״�����������Ƿ�������
    boolean[][] table = new boolean[SIZE+2][SIZE+2];
    //����״̬
    boolean[][] lasttable = new boolean[SIZE+2][SIZE+2];
    //���ӵ��ھ���Ŀ
    int[][] neighbors = new int[SIZE+2][SIZE+2];  
    //���沢�Ͼ���
    private boolean[][] mergetable; 
    //canves����
    private String canve = "";//��ʱ���� 
    private Vector<Life> canves; 
    //�ϲ��߶ȺͿ��
    private int merh,merw;

    private Thread animator;//�߳�  
    int delay = 100;//�ӳ٣�����
    private final int timeStream = 1000;//��������ˢ��ʱ�� ��ûͶ��ʹ��
    private int nowTime = 0;//��������ˢ��ʱ��
    boolean running;//flag����ʶ�̵߳�����״��������������runningΪtrue�����û��жϣ�runningΪfalse�� 
    //������� �������ȡ�����ǵ��
    private int movestaic ;
     //�Ƿ�ȫ��ˢ�»���
    private boolean isupdata ;  
    
    
    @Override public void run() {
        long tm = System.currentTimeMillis();
        //currentThread����εĶ���
        while (Thread.currentThread() == animator) {
            if (running == true) {  
            	synchronized (table) {
            		getNeighbors();
                    nextWorld();
            	}
                    repaint();   
            }
            try { 
                tm += delay;
                Thread.sleep(Math.max(0, tm - System.currentTimeMillis()));
            } catch (InterruptedException e) {
                break;
            }
        }
    } // run 
    
    /**
     * ���úϲ��ľ���    
     */
    void setMerge(Life canves){ 
    	merw = canves.getx();
		merh = canves.gety();
		mergetable = canves.getTable();  
		if(movestaic==3) { 
			movestaic = 2;
			isupdata = true;
			repaint();
		}  
		movestaic = 2; 
    }
    /**
     * applet �������ڷ���
     */
    @Override public void init()  {
        animator = new Thread(this); 
        running = false;
        isupdata = true; 
        //���
        setBackground(backcolor);
        MouthLister ml = new MouthLister();
        ListenKey lsk = new ListenKey();
        addMouseListener(ml);
        //����
        addMouseWheelListener(ml);
        //����
        addMouseMotionListener(ml); 
        //����
        addKeyListener(lsk); 
        
    } 
    @Override public void start() {        
        animator.start();       
    }
 
    @Override public void stop()    {
        animator = null;    
    } 
    
    private Image iBuffer;
    private Graphics gBuffer;
    
    @Override public void paint(Graphics g) { 
    	super.paint(g); 
    	//���ڱ仯��paint
    	isupdata = true;
    	offScreenImage = this.createImage(this.getWidth(), this.getHeight()); 
    	update(g); 
    }
    /**
     * ����table
     */
    @Override public void update (Graphics gg) {  
    	//˫���弼��
    	Graphics g = gg; 
        Graphics gImage = offScreenImage.getGraphics();  //�����Ļ����ù���,��gImage������  
        g = gImage;    //��Ҫ���Ķ�������ͼ�񻺴�ռ�ȥ     
    	//��Ⱦ��С
    	xsize = getSize().width/CELL_Size; 
    	ysize = getSize().height/CELL_Size;   
    	int startx = 0;
    	int starty = 0;  
		//��ջ���   
    	if(isupdata){  
    		for (int x = 0; x < SIZE; x++)
	            for (int y = 0; y < SIZE; y++)  
	            	lasttable[x][y]=false;
    		g.setColor(space); 
    		g.fillRect(0, 0,getSize().width,getSize().height);
    	}  
    	g.setColor(cell); 
    	//�߳�ͬ�� �Ŵ��С�����ڳ�ͻ 
		for (int x = startx; x < startx+xsize; x++)
            for (int y = starty; y < starty+ysize; y++) {  
            	 //(isupdata ||  lasttable[x][y]!=table[x][y]) &&
				if(  (isupdata ||  lasttable[x][y]!=table[x][y]) && table[x][y] ) { 
					lasttable[x][y]=table[x][y];   
					g.fillRect( (x-startx) * CELL_Size, (y-starty) * CELL_Size, CELL_Size - 1, CELL_Size - 1);
					
				} 
            }     
		if(isupdata && CELL_Size>6 ) {
			g.setColor(backcolor); 
			for (int x = 1; x <= ysize; x++) {
				g.drawLine(0, CELL_Size*x-1, xsize*CELL_Size, CELL_Size*x-1); 
			}
			for (int y = 1; y <= xsize; y++) {
		 		g.drawLine(CELL_Size*y-1, 0, CELL_Size*y-1,ysize*CELL_Size ); 
			}   
		}else {
			g.setColor(space); 
			for (int x = startx; x < startx+xsize; x++)
	            for (int y = starty; y < starty+ysize; y++) { 
					if( lasttable[x][y]!=table[x][y] && !table[x][y] ) { 
						g.fillRect((x-startx) * CELL_Size, (y-starty) * CELL_Size, CELL_Size - 1, CELL_Size - 1);
						lasttable[x][y]=table[x][y]; 
					}
	            }
			//System.out.println("ˢ��"+ CELL_Size ); 
		}  
    	   
    	if( movestaic==3 &&  isupdata == true) {
    		drawRange(g);     
    	}
    	//���ȫ��ˢ��
    	isupdata = false;  
    	//˫���弼��  ����ȫ���ٻ���ȥ
    	gg.drawImage(offScreenImage, 0, 0, null);         //Ȼ��һ������ʾ���� 
	}
    /**
     * ������
     */
    void drawRange(Graphics g){
    	g.setColor(rangecolor); 
    	g.fillRect(Math.min(nx,ex), Math.min(ny,ey), Math.abs(nx-ex)  ,Math.abs(ny-ey)  );
    } 
    /**
     * ��table�������Ƶ���neighbors���顣
     */
    public void getNeighbors() { 
    	//��Ⱦ��С
    	xsize = getSize().width/CELL_Size;
    	ysize = getSize().height/CELL_Size; 
    	
        for (int c = 0; c < SIZE; c++){//row
            for (int r = 0; r < SIZE; r++){//col   
            		
            	  if(table[r][c]) {   
            		    //��  ����
            		  	neighbors[(r-1+SIZE)%SIZE][c]++;
      		    		neighbors[(r-1+SIZE)%SIZE][(c+1)%SIZE]++;
      		    		  
            		    // �� ����
            		    neighbors[(r+1)%SIZE][c]++;
        		    	neighbors[(r+1)%SIZE][(c+1)%SIZE]++;
        		    	//��
            		    neighbors[r][(c+1)%SIZE]++;
            		    //��
            		    neighbors[r][(c-1+SIZE)%SIZE]++;
            		    
            		    neighbors[(r-1+SIZE)%SIZE][(c-1+SIZE)%SIZE]++;
            		    neighbors[(r+1+SIZE)%SIZE][(c-1+SIZE)%SIZE]++;
            		    
            	  } 
            }
        }            
    } 
    /**
     * nextWorld()���������档
     * ������Ϸ�ĺ����Ǽ������һ����table��������һ���Ķ�ά���硣
     * ����ÿһ��neighborsԪ��
     */
    public void nextWorld() {  
        for (int r = 0; r < SIZE; r++){//row
            for (int c = 0; c < SIZE; c++){//col  
            	if( neighbors[r][c]!=2  )  
            		table[r][c] = false; 
                if (neighbors[r][c] == 3 )
                    table[r][c] = true;  
                neighbors[r][c] = 0;
            }           
        }
    }
    
    /**
     * �ϲ����� || �����ĸı�
     * @param x
     * @param y
     * @param status
     */
    public void mergerTable(int x,int y,int status) {  
    	switch(status) {
    		case 1: 
    			System.out.println(x+" "+y);
    			table[x][y] = true; 
    			break; 
    		case 0: 
    			table[x][y] = false; 
    			break;
    		case 2:   
    			//�߳�ͬ����ֹ�첽����
    			synchronized (table) {
    				y =  ((y-merh/2)+SIZE)%SIZE;
        			x =  ((x-merw/2)+SIZE)%SIZE;  
        			for(int i = 0,ty = y;i < merh;i++,ty++) {
        				for(int j = 0,tx =x;j < merw;j++, tx++) { 
        					//��ֵ  y��xҪ�ߵ�һ��
        					table[tx%SIZE][ty%SIZE] = mergetable[i][j]; 
        				} 
        			}
        			break;
				} 
    		case 3:
    			break;
    	}
    	repaint();
    	
    }
    //GOL����---------------------------------------------
    /**
     * event handler ����
     */ 
    public void mouseClicked(MouseEvent e){
    	switch(e.getClickCount()) {
	    	case 1:
	    		
	    		break;
    	} 
    } 
    /**
     * ��ɵ��״̬
     */
    void returnpoint(){
    	movestaic = 1;
    }
    /**
     * ����ѡ������
     */
    void changeCanves() {
    	movestaic = 3; 
    	running = false;  
    }  
    /**
     *  �ı�ɵ�
     */
    void changepoint(){ 
		movestaic=0;  
    } 
    /**
     *  ��ͣ
     */
    void stopCanves(boolean sta){ 
		running=sta;  
    } 
    /**
     * �������
     */
    void clear(){    
		synchronized (table) {
			for (int r = 0; r < SIZE; r++){//row
	            for (int c = 0; c < SIZE; c++){//col  
	            	table[r][c] = false;
	            }
	    	} 
		}
			isupdata=true;
	    	repaint();
		 
    } 
    /**
     * �������� 
     */
    public String copyrange(){
    	if(movestaic==3) {
    		int sX = Math.min(nx,ex)/CELL_Size%SIZE;
            int sY = Math.min(ny,ey)/CELL_Size%SIZE;
            int eX = Math.max(nx,ex)/CELL_Size%SIZE;
            int eY = Math.max(ny,ey)/CELL_Size%SIZE;    
            boolean ttable[][] = new boolean [eY+1][eX+1];
    		for( int i = sX;i <eX;i++) {
    			for( int j = sY;j <eY;j++) {
        			 ttable[j][i] = table[i][j];
        		}
    		}   
            String tempcan = MatrixTools.toString(ttable, sX, sY, eX, eY); 
            //���úϳ�ģʽ
            setMerge(new Life(tempcan)); 
            isupdata = true;
            repaint();
			return tempcan;
    	}
    	return "";
    }  
  
    /**
     *  �ڲ������
     */
	/*
     * �������¼����������ӿ�MouseWheelListener
     * MouseMotionListener�����ƶ����Ϸ�
     * MouseListener  �����ͷš�������������˳� 
     */
    public class MouthLister implements MouseListener,MouseMotionListener,MouseWheelListener{  
        public MouthLister() {  
    	}

        //GOL�������-----------------------------------------
        /**
    	* ���
    	*/
        public void mousePressed(MouseEvent e){  
        	click=true;//���״̬ 
        	//��������
        	if( click && button[CTRL] ) { 
        		clear();   
        	}  
        	System.out.println("���");
        	int cellX = e.getX()/CELL_Size%SIZE;
            int cellY = e.getY()/CELL_Size%SIZE; 
            
            switch(movestaic) {
    	        case 0 :
    	        	movestaic = !table[cellX][cellY] ? 1:0; 
    	        	break;
    	        case 1 :
    	        	movestaic = !table[cellX][cellY] ? 1:0; 
    	        	break;  
    	        case 3 : 
    	        	 nx = e.getX();
    	        	 ny = e.getY();
    	        	break;
            } 
        }
        /**
         * �����   ���л����仯 
         */
        public void mouseReleased(MouseEvent e){
        	click=false;//�ɿ�״̬ 
        	
        	if( !button[CTRL] ) {   
        		int cellX = e.getX()/CELL_Size%SIZE;
                int cellY = e.getY()/CELL_Size%SIZE;   
                mergerTable(cellX,cellY,movestaic);
        	} 
        }
        public void mouseEntered(MouseEvent e){}
        public void mouseExited(MouseEvent e){}
        /**
         * �϶�
         */
        public void mouseDragged(MouseEvent e){
        	System.out.println("�϶���");
        	int cellX = e.getX()/CELL_Size%SIZE;
            int cellY = e.getY()/CELL_Size%SIZE;  
    	   	 //�ϻ��Ƶ�������
    	       if( cellX>=0 && cellY>=0 ) {
    	    	   //ѡ�������ʱ��
    	    	   if(movestaic==3) {
    	    		   ex = e.getX();
    	    		   ey = e.getY();
    	    		   isupdata = true;
    	    	   }
    	       		mergerTable(cellX,cellY,movestaic); 
    	       }   
        }
        /**
         * ����ƶ�
         */
        public void mouseMoved(MouseEvent e){
        	nowx = e.getX();
        	nowy = e.getY();
        }     
        public void keyTyped(KeyEvent e){  
        }
        /**
         * ����
         * �������ƷŴ��С���ȱ仯
         */
    	@Override
    	public synchronized void  mouseWheelMoved(MouseWheelEvent e) { 
    		// TODO �Զ����ɵķ������
    		int num=e.getWheelRotation();    
    		//����Ƶ��
    		if( button[CTRL] == true ) { 
    			if(delay+num*5 >0 && delay+num*5<=500)
    				delay += num*5;
    			System.out.println(delay);
        		return ;  
        	}
    		//���ƻ�����С       
    		if(CELL_Size-num>1 && CELL_Size-num<500) {   
    			System.out.println(CELL_Size);
    			isupdata=true;
    			CELL_Size-=num;   
    			repaint(); 
        	}    
    	}

		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO �Զ����ɵķ������
			
		} 
    	 
    }
    
    /*
     * �������¼����������ӿ�MouseWheelListener
     * MouseMotionListener�����ƶ����Ϸ�
     * MouseListener  �����ͷš�������������˳�
     * KeyListener ����
     */
    public class ListenKey implements KeyListener{  
        public ListenKey() {  
    	}
    	 /**
         * ����
         */
        public void keyPressed(KeyEvent e){  
        	int key = e.getKeyCode();    
        	System.out.println("����"+Character.valueOf((char)key));
        	//getchar ��code��һ��
        	switch(key) {  
    	    	case STOP_BUTTON :
    	    		running = !running;
    	    		//�����ʱ��س�
    	    		if(movestaic==3) {
    	    			isupdata=true;
    	    			movestaic=0;
    	    		}
    	            repaint();
    	    		break;   
        	}    
        	if(key<255)
        		button[key] = true; 
        } 
        /**
         * �ɰ���  setMerg
         */
        public void keyReleased(KeyEvent e){  
        	int key = e.getKeyCode();
        	System.out.println("sonle��"+Character.valueOf((char)key)+button[CTRL]);
        	if( key-'0'>=0 && key-'0'<=9 && button[CTRL] == true && canves.size()-1 >= (key-'0') ) {
        		System.out.println(key-'0');
        		setMerge(canves.get(key-'0'));  
        		System.out.println("�л��ɹ�");
        	}  
        	//��������
        	if( button[CTRL] == true && button['R'] ) { 
        		changeCanves();
        		System.out.println("ѡ������");  
        	}   
        	//��������
        	if( button[CTRL] == true && button['V'] ) { 
        		String jq = SysClipboardText.getSysClipboardText();
        		setMerge(new Life(jq));
        		System.out.println("ѡ������");  
        	}   
        	//��������
        	if( button[CTRL] == true && button['C'] ) {  
        		//�����ı���������
        		SysClipboardText.setSysClipboardText(copyrange());
        		System.out.println("ѡ������");  
        	}   
        	if(key<255)
        		button[key]=false; 
        }
    	@Override //��bai����
    	public void keyTyped(KeyEvent e) {
    		// TODO �Զ����ɵķ������ 
    	}  
    }
   
}

