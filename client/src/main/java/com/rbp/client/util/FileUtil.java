package com.rbp.client.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuwenbin on 2017/8/31.
 */
public class FileUtil {
    public static List<String> read(String filePath) throws Exception{
        List<String> list = new ArrayList<String>();
        BufferedReader bufferedReader = new BufferedReader(new FileReader(new File(filePath)));
        String line = bufferedReader.readLine();
        while (null != line){
            list.add(line);
            line = bufferedReader.readLine();
        }
        bufferedReader.close();
        return list;
    }
}
