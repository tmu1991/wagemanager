package com.wz.wagemanager.tools;

public class ThreadLocalUtil {
    private static ThreadLocal<String> threadLocal=new ThreadLocal<>();
    public static void set(String msg){
        threadLocal.set(msg);
    }
    public static String get(){
        return threadLocal.get();
    }

    public static void clear(){
        threadLocal.remove();
    }
}
