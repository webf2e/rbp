package com.rbp.main.client.util;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

/**
 * Created by liuwenbin on 2017/9/11.
 */
public class ClipboardUtil {

    public static void setSystemClipboard(String refContent){
        String vc = refContent.trim();
        StringSelection ss = new StringSelection(vc);
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ss, null);
    }

    public static String getSystemClipboard(){
        Transferable t = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
        try {
            if (null != t && t.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                String text = (String)t.getTransferData(DataFlavor.stringFlavor);
                return text;
            }
        } catch (UnsupportedFlavorException e) {
            //System.out.println("Error tip: "+e.getMessage());
        } catch (IOException e) {
        }   //System.out.println("Error tip: "+e.getMessage());
        return null;
    }
}
