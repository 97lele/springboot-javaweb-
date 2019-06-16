package com.gdut.xg.shop.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;


public class CommonUtil {
    private volatile static SimpleDateFormat sdf;
    public final static Integer MAXAGE = 24 * 60 * 60;
    public final static String USERCOOKIE = "token";
    public final static String CARTCOOKIE = "cart";
    public final static String OFF = "off";
    private final static String filepath = "/upload/product/img/";
    private static byte[] b = new byte[1024];



    private CommonUtil() {

    }

    public static synchronized void deleteFile(String name) {
        String os = System.getProperties().getProperty("os.name");
        String path = "";
        if (os.toLowerCase().startsWith("win")) {
            path = "D:/schoolSpace" + name;
        } else if (os.toLowerCase().startsWith("win")) {
            path = "/usr/local" + name;
        }

        File file = new File(path);
        if (file.exists()) {
            file.delete();

        }
    }

    public static synchronized String generateFile(InputStream input, String name) {

        String os = System.getProperties().getProperty("os.name");
        String path = "";
        if (os.toLowerCase().startsWith("win")) {
            path = "D:/schoolSpace" + filepath;
        } else if (os.toLowerCase().startsWith("win")) {
            path = "/usr/local" + filepath;
        }

        path = path.replace("/", File.separator);
        File p = new File(path);
        if (!p.exists()) {
            p.mkdirs();
        }
        path = path + name;
        BufferedInputStream in = null;
        //path为文件的全路径
        File f = new File(path);

        BufferedOutputStream out = null;
        if (!f.exists()) {
            try {
                f.createNewFile();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }


        try {
            out = new BufferedOutputStream(new FileOutputStream(f));

            in = new BufferedInputStream(input);
            while (in.read(b) != -1) {
                out.write(b);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                in.close();
                input.close();
                out.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return filepath + name;
    }


    public static String GenerateKey() {
        Random random = new Random();
        int nextInt = random.nextInt(9000000);
        nextInt = nextInt + 1000000;
        String str = nextInt + "";
        return System.currentTimeMillis() + str;

    }

    public static Cookie getCookie(HttpServletRequest request, String name) {
        Map<String, Cookie> cookieMap = readCookieMap(request);
        if (cookieMap.containsKey(name)) {
            return cookieMap.get(name);
        } else {
            return null;
        }
    }

    private static Map<String, Cookie> readCookieMap(HttpServletRequest request) {
        Map<String, Cookie> cookieMap = new HashMap<>();
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                cookieMap.put(cookie.getName(), cookie);
            }
        }
        return cookieMap;
    }

}
