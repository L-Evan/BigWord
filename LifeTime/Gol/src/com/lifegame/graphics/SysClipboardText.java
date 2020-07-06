package com.lifegame.graphics;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
/**
 * ���������
 * @author 11291
 *
 */
public class SysClipboardText {
	 /** 
     * ���ַ������Ƶ����а塣 
     */  
    public static void setSysClipboardText(String writeMe) {  
        Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();  
        Transferable tText = new StringSelection(writeMe);  
        clip.setContents(tText, null);  
    } 
    /**
     * �Ӽ��а������֡�
     */
    public static String getSysClipboardText() {
        String ret = "";
        Clipboard sysClip = Toolkit.getDefaultToolkit().getSystemClipboard();
        // ��ȡ���а��е�����
        Transferable clipTf = sysClip.getContents(null);

        if (clipTf != null) {
            // ��������Ƿ����ı�����
            if (clipTf.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                try {
                    ret = (String) clipTf
                            .getTransferData(DataFlavor.stringFlavor);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } 
        return ret;
    }
}