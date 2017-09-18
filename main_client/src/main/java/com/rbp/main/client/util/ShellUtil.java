package com.rbp.main.client.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuwenbin on 2017/8/29.
 */
public class ShellUtil {

    public static List<String> run(String shell) throws Exception {
        List<String> result = new ArrayList<>();
        Process process = Runtime.getRuntime().exec(shell);
        InputStream is = process.getInputStream();
        //关闭错误的输出流
        new Thread(() -> {
            BufferedReader br = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            try {
                String line = br.readLine();
                while (line != null){
                    System.out.println("error shell : "+line);
                    line = br.readLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try{
                    br.close();
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        String line;
        while ((line = reader.readLine()) != null) {
            line = line.trim();
            result.add(line);
        }
        process.waitFor();
        is.close();
        reader.close();
        process.destroy();
        return result;
    }
}
