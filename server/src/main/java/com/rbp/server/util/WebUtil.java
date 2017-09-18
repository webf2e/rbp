package com.rbp.server.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by liuwenbin on 2017/8/31.
 */
public class WebUtil {

    public static String get(String urlPath){
        try{
            URL url = new URL(urlPath);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(10000);
            urlConnection.setReadTimeout(10000);
            int code = urlConnection.getResponseCode();
            if(200 == code){
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                return bufferedReader.readLine();
            }
            return "Error Code: " + code;
        }catch(Exception e){
            e.printStackTrace();
            return "Error Msg: " + e.getMessage();
        }
    }
}
