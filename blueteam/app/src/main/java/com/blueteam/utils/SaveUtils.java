package com.blueteam.utils;

import android.content.Context;

import java.io.*;

public class SaveUtils {

    static File file;

    public static boolean saveInfo(String username, String password) {
        String info = username + "##" + password;
        file = new File("/data/data/com.blueteam/info.txt");
        System.out.println(file.exists());
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            fos.write(info.getBytes());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


public static String[] readInfo(Context context) {
file = new File(context.getFilesDir().getAbsolutePath() + "/info.txt ");
FileInputStream fis = null;
BufferedReader br = null;
try {
    fis = new FileInputStream(file);
    br = new BufferedReader(new InputStreamReader(fis));
    String tmp = br.readLine();
    String[] info = tmp.split("##");
    return info;
} catch (IOException e) {
    e.printStackTrace();
    return null;
}
}


    public static boolean saveInfoByContext(Context context, String username, String password) {
        String info = username + "##" + password;
//        file = new File("/data/data/com.blueteam/info.txt");
        file = new File(context.getFilesDir().getAbsolutePath() + "/info.txt ");
        FileOutputStream fos = null;
        try {
//            fos = new FileOutputStream(SaveUtils.file);
            fos = context.openFileOutput("info.txt",Context.MODE_PRIVATE);
            fos.write(info.getBytes());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public static String[] readInfoByContext(Context context) {
        file = new File(context.getFilesDir().getAbsolutePath() + "/info.txt ");
        FileInputStream fis = null;
        BufferedReader br = null;
        try {
//            fis = new FileInputStream(file);
            fis = context.openFileInput("info.txt");
            br = new BufferedReader(new InputStreamReader(fis));
            String tmp = br.readLine();
            String[] info = tmp.split("##");
            return info;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


}
