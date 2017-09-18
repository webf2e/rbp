package com.rbp.main.client.util;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by liuwenbin on 2017/9/12.
 */
public class DownLoadImg {

    public static void down(String fileUrl,String savePath) throws Exception{
        File parent = new File(savePath).getParentFile();
        if(!parent.exists()){
            parent.mkdirs();
        }
        URL url = new URL(fileUrl);
        URLConnection urlConnection = url.openConnection();
        urlConnection.setReadTimeout(60000);
        urlConnection.setConnectTimeout(30000);
        InputStream inputStream = urlConnection.getInputStream();
        BufferedInputStream in = new BufferedInputStream(inputStream);
        BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(savePath));
        byte[] buf = new byte[2048];
        int length = in.read(buf);
        while (length != -1) {
            out.write(buf, 0, length);
            length = in.read(buf);
        }
        in.close();
        out.close();
    }
}
