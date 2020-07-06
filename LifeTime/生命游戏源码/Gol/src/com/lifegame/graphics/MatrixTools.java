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
 * ����仯
 *
 */
public class MatrixTools {
	//����
	public static void main(String args[]) { 
		new MatrixTools(); 
	}
	MatrixTools(){
		 for(int i = 0;i<1000;i++) {
			 text();
		 }
	}
	/**
	 * ������ȷ��
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
		//���ַ���
		String ans =toString(table,0,0,6,6);
		System.out.println(ans);
		String s = ans.split("!")[0];
		//�ָ�����
		String wh[] = ans.split("[.*!\\s*,\\s*.*:]"); 
		//�߶ȺͿ��
		int w = Integer.valueOf(wh[3]);
		int h = Integer.valueOf(wh[5]); 
		//�����
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
	 * ����ת��String
	 * @param table ����
	 * @param sh ��ʼ���
	 * @param sw
	 * @param oh �������
	 * @param ow
	 * @return
	 */
	public static String toString( boolean[][] table,int sw,int sh,int ow,int oh)  {
		
		String matrixAns = "";
		boolean start = false; 
		int nsh=sh,nsw=sw;
		//�Ҹ߶�
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
		//�ҿ��
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
		
		//��ʼ�洢
		int maxh = nsh;
		int maxw = nsw;
		//��������
		int lenh = 0,lenw = 0;
		//״̬
		boolean statich = false,staticw = false;
		//��ʱ
		String tmatrixAns="";
		if(start) {  
			for(int i = nsh;i <= oh;i++) {
				//��ʼ��
				statich = false;
				lenh++;
				staticw = table[i][nsw]; 
				tmatrixAns="";
				lenw = 0; 
				for(int j = nsw;j <= ow;j++) { 
					lenw++; 
					if( ( j+1 <= ow && table[i][j+1] != staticw) || (j==ow && staticw) ){
						//��������
						if(j>maxw) maxw = j; 
						//��������
						if(i>maxh) maxh = i; 
						//��������
						if( !statich )
							statich = true;
						 
						if(lenw>1) tmatrixAns+=String.valueOf(lenw);
						//����
						tmatrixAns+=staticw?'o':'b';
						 
						staticw = !staticw;
						
						lenw = 0;   
					} 
					
				}  
				//��һ�в��û��� 
				if( i!=nsh && statich ){ 
					
					if(lenh>1) matrixAns+=String.valueOf(lenh);
					matrixAns+='$'; 
					lenh = 0;   
				} 
				
				if(i==nsh) {
					lenh = 0; 
				}
				
				//�����  �����Ⱥ��ٻ���
				matrixAns+=tmatrixAns;
				  
			} 
		} 
		//System.out.println("H"+nsh+"w"+nsw);
		matrixAns+='!';	
		//x = 27, y = 20, rule = B3/S23
		matrixAns="x = "+(maxw-nsw+1)+", y = "+(maxh-nsh+1)+", rule = 1\r\n,"+matrixAns;
		return matrixAns;
	} 
	/**��ѹ�ɾ���
	 * 
	 * @param list �ַ���
	 * @param ow ��Ҫ�Ŀ��
	 * @param oh ��Ҫ�ĸ߶�
	 * @return �γɵľ���
	 */
	public static boolean [][] toMatrix(String list,int ow,int oh){ 
		int h = 0;
		int w = 0;
		//ȥ�ո��ַ���
		list = replaceBlank(list);
		boolean table[][] = new boolean [oh][ow]; 
		//�ָ�
		String s[] = list.split("[$]"); 
		for( int i = 0;i<s.length;i++) { 
			//System.out.println(s[i]);
			/*  Ӳȡ  ��׳�Բ�
			String ts = s[i].replace('b', 'o');  
			int index = ts.indexOf('o'); 
			boolean b = s[i].charAt(index)=='o';  */  
			//��ʼ
			w=0; 
			for(int j = 0;j<s[i].length();j++) { 
				//System.out.println("in:"+j);
				int  num = 1;
				//���ó�ʼ��
				boolean b = false;
				int oi = s[i].indexOf('o',j);
				int bi = s[i].indexOf('b',j);
				//3�����
				if(oi==-1 && bi==-1) {
					String snum = s[i].substring(j);  
					if(!snum.equals("!") ) { 
						num = Integer.valueOf(snum);
					}   
					//����for�����
					while(--num>0) {
						h++;
					} 
					//�ݽ�
					j+=snum.length()-1; 
				}else if(bi==-1 || bi>oi&&oi!=-1 ){  
					if(j!=oi){
						String snum = s[i].substring(j,oi);
						num = Integer.valueOf(snum); 
						//�ݽ�
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
				//ȡ���߶�
				while(num-->0) { 
					table[h][w++] = b;
				}  
			} 
			//��һ��
			h++;
			//"bob2o3 $ b2o $ 2ob2o!"  
		} 
		return table;
	}
	/**
	 * ȥ���س��ո�
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
			 System.out.println("����");
			 ans[0] = "0";
			 ans[1] = "0";
			 ans[2] = "0";
			 ans[3] = "0";
		} 
		return ans;  
	 }
	
	
	
}



