package com.chenva;

import java.io.*;

public class Main {

    static BufferedReader bufferedReader;

    static int c;
    static int c2;
    static int c3;

    static String sick_start = "<SCRIPT Language=VBScript><!--";
    static String sick_end = "//--></SCRIPT>";

    public static void main(String[] args) {
//        File f = new File("C:\\Users\\Administrator\\Desktop\\New Folder");
        File f = new File("g:\\");
        getCount(f);
        System.out.println("\n扫描到" + c2 + "个html或者htm类型的文件");
        System.out.println(c + "个文件被感染");
        System.out.println(c3 + "个文件被修复");
    }

    public static void getCount(File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File f : files) {
                    if (f.isDirectory()) {
                        getCount(f);
                    } else {
                        String fileName = f.getName();
                        if (fileName.endsWith(".html") || fileName.endsWith(".htm")) {
                            c2++;
                            if (validSick(f)) {
                                fix(f);
                                c++;
                            }
                        }
                    }
                }
            }
        }
    }

    public static boolean validSick(File f) {
        try {
            bufferedReader = new BufferedReader(new FileReader(f));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if (line.contains(sick_start)) {
                    bufferedReader.close();
                    System.err.println(f + "已经感染病毒...");
                    return true;
                }
            }
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(f + "...文件安全 !");
        return false;
    }

    public static void fix(File f) {
        System.out.print("正在修复" + f.getName() + ".........");
        boolean flag = false;
        try {
            BufferedReader br = new BufferedReader(new FileReader(f));
            // 声明一个内存流
            CharArrayWriter tempStream = new CharArrayWriter();
            String line;
            while ((line = br.readLine()) != null) {
                if (line.contains(sick_end)) {
                    flag = false;
                }
                if (flag) {
                    continue;
                }
                if (line.contains(sick_start)) {
                    tempStream.write(line.replace(sick_start, ""));
                    flag = true;
                } else {
                    if (line.contains(sick_end)) {
                        tempStream.write(line.replace(sick_end, ""));
                    } else {
                        tempStream.write(line);
                    }
                }
                // 追加换行符
                tempStream.append(System.getProperty("line.separator"));
            }

            // 将内存记录的内容写入源文件
            FileWriter writer = new FileWriter(f);
            tempStream.writeTo(writer);

            // 关闭资源
            writer.close();
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        c3++;
        System.out.println("修复完毕 !");
    }
}

