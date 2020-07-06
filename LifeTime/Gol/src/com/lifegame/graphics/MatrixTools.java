package com.lifegame.graphics;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.util.Date;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
 
/**
 * 
 * @author 11291
 * 矩阵变化
 *
 */
public class MatrixTools {
	//测试
	public static void main(String args[]) { 
		new MatrixTools(); 
	}
	MatrixTools(){
		 for(int i = 0;i<1000;i++) {
			 text();
		 }
	}
	/**
	 * 测试正确性
	 */
	public static void text() {
		boolean[][] table = new boolean [1000][1000];
		Random r = new Random(new Date().getTime());
		for(int i = 0;i <= 100;i++) {
			for(int j = 0;j <= 100;j++) { 
				table[i][j] = r.nextBoolean();
			}
		}
		for(int i = 0;i <= 6;i++) {
			for(int j = 0;j <= 6;j++) { 
				System.out.print(table[i][j]?1:0);
				System.out.print(" ");
			}
			System.out.println();
		} 
		//变字符串
		String ans =toString(table,0,0,6,6);
		System.out.println(ans);
		String s = ans.split("!")[0];
		//分割正则
		String wh[] = ans.split("[.*!\\s*,\\s*.*:]"); 
		//高度和宽度
		int w = Integer.valueOf(wh[3]);
		int h = Integer.valueOf(wh[5]); 
		//变矩阵
		table = toMatrix(s ,w,h); 
		//System.out.println(w+"-"+h);
		
		for(int i = 0;i < h;i++) {
			for(int j = 0;j < w;j++) {
				System.out.print(table[i][j]?1:0);
				System.out.print(" ");
			}
			System.out.println();
		}
	}
	/**
	 * 矩阵转换String
	 * @param table 矩阵
	 * @param sh 开始宽高
	 * @param sw
	 * @param oh 结束宽高
	 * @param ow
	 * @return
	 */
	public static String toString( boolean[][] table,int sw,int sh,int ow,int oh)  {
		
		String matrixAns = "";
		boolean start = false; 
		int nsh=sh,nsw=sw;
		//找高度
		for(int i = sh;i <= oh;i++) {
			for(int j = sw;j <= ow;j++) {
				if(table[i][j]) {
					nsh = i; 
					nsw = j;
					start = true;
					break;
				}
			}
			if(start) {
				start = false;
				break;
			}
		} 
		//找宽度
		for(int i = sw;i <= nsw;i++) {
			for(int j = nsh;j <= oh;j++) {
				if(table[j][i]) {
					nsw = i;
					start = true;
					break;
				}
			}
			if(start) { 
				break;
			}
		}
		
		//开始存储
		int maxh = nsh;
		int maxw = nsw;
		//连续长度
		int lenh = 0,lenw = 0;
		//状态
		boolean statich = false,staticw = false;
		//临时
		String tmatrixAns="";
		if(start) {  
			for(int i = nsh;i <= oh;i++) {
				//初始化
				statich = false;
				lenh++;
				staticw = table[i][nsw]; 
				tmatrixAns="";
				lenw = 0; 
				for(int j = nsw;j <= ow;j++) { 
					lenw++; 
					if( ( j+1 <= ow && table[i][j+1] != staticw) || (j==ow && staticw) ){
						//更新最大宽
						if(j>maxw) maxw = j; 
						//更新最大高
						if(i>maxh) maxh = i; 
						//此行有无
						if( !statich )
							statich = true;
						 
						if(lenw>1) tmatrixAns+=String.valueOf(lenw);
						//常规
						tmatrixAns+=staticw?'o':'b';
						 
						staticw = !staticw;
						
						lenw = 0;   
					} 
					
				}  
				//第一行不用换行 
				if( i!=nsh && statich ){ 
					
					if(lenh>1) matrixAns+=String.valueOf(lenh);
					matrixAns+='$'; 
					lenh = 0;   
				} 
				
				if(i==nsh) {
					lenh = 0; 
				}
				
				//横向的  总是先横再换行
				matrixAns+=tmatrixAns;
				  
			} 
		} 
		//System.out.println("H"+nsh+"w"+nsw);
		matrixAns+='!';	
		//x = 27, y = 20, rule = B3/S23
		matrixAns="x = "+(maxw-nsw+1)+", y = "+(maxh-nsh+1)+", rule = 1\r\n,"+matrixAns;
		return matrixAns;
	} 
	/**解压成矩阵
	 * 
	 * @param list 字符串
	 * @param ow 需要的宽度
	 * @param oh 需要的高度
	 * @return 形成的矩阵
	 */
	public static boolean [][] toMatrix(String list,int ow,int oh){ 
		int h = 0;
		int w = 0;
		//去空格字符串
		list = replaceBlank(list);
		boolean table[][] = new boolean [oh][ow]; 
		//分割
		String s[] = list.split("[$]"); 
		for( int i = 0;i<s.length;i++) { 
			//System.out.println(s[i]);
			/*  硬取  健壮性差
			String ts = s[i].replace('b', 'o');  
			int index = ts.indexOf('o'); 
			boolean b = s[i].charAt(index)=='o';  */  
			//开始
			w=0; 
			for(int j = 0;j<s[i].length();j++) { 
				//System.out.println("in:"+j);
				int  num = 1;
				//不用初始化
				boolean b = false;
				int oi = s[i].indexOf('o',j);
				int bi = s[i].indexOf('b',j);
				//3种情况
				if(oi==-1 && bi==-1) {
					String snum = s[i].substring(j);  
					if(!snum.equals("!") ) { 
						num = Integer.valueOf(snum);
					}   
					//缓和for外面的
					while(--num>0) {
						h++;
					} 
					//递进
					j+=snum.length()-1; 
				}else if(bi==-1 || bi>oi&&oi!=-1 ){  
					if(j!=oi){
						String snum = s[i].substring(j,oi);
						num = Integer.valueOf(snum); 
						//递进
						j+=snum.length();
					}   
					b = true;  
				}else{ 
					if(j!=bi){
						String snum = s[i].substring(j,bi);
						num = Integer.valueOf(snum); 
						j+=snum.length();
					}   
					b = false;
				}  
				//取到高度
				while(num-->0) { 
					table[h][w++] = b;
				}  
			} 
			//下一行
			h++;
			//"bob2o3 $ b2o $ 2ob2o!"  
		} 
		return table;
	}
	/**
	 * 去掉回车空格
	 * @param str
	 * @return
	 */
	public static String replaceBlank(String str) {
		String dest = "";
		if (str!=null) {
			Pattern p = Pattern.compile("\\s*|\t|\r|\n"); 
			Matcher m = p.matcher(str); 
			dest = m.replaceAll("");
		}
		return dest;
	}
	/**
	 * list to String 
	 */
	public static String [] Transformat(String Matrix){ 
		String ans[ ] = new String[4] ;
		 try { 
			 
		 if(Matrix.split(",").length==3) {
			 Matrix = Matrix.replaceAll("rule(.*)", "rule=1,") ; 
		 } 
		 String canves = MatrixTools.replaceBlank(Matrix); 
		 String a[] = canves.split(","); 
			 ans[0] = a[0].split("=")[1];
			 ans[1] = a[1].split("=")[1];   
			 ans[2] = a[2].split("=")[1]; 
			 ans[3] = a[3]; 
		 }catch (Exception e) {
			// TODO: handle exception
			 System.out.println(e);  
			 System.out.println("错误");
			 ans[0] = "0";
			 ans[1] = "0";
			 ans[2] = "0";
			 ans[3] = "0";
		} 
		return ans;  
	 }
	
	
	
}



