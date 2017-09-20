package com.rbp.client.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * 执行Shell
 */
public class ShellUtil {

    /**
     * 执行shell
     * @param shell shell语句
     * @return
     * @throws Exception
     */
    public static List<String> run(String shell) throws Exception {
        List<String> result = new ArrayList<>();
        Process process = Runtime.getRuntime().exec(shell);
        InputStream is = process.getInputStream();
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
