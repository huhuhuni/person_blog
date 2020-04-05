package com.lock;

public class T {
    private static  Object o =new Object();
    public static  void main(String args[]){
        synchronized(o){
            test();
            System.out.print("hello");
        }
    }
    public static void test(){
        System.out.print(1);
        System.out.print(2);
    }
}