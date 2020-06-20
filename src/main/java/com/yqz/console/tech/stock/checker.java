package com.yqz.console.tech.stock;

public class checker
{
    public static void main ( String args [])
    {   
        long startT=System.currentTimeMillis();
       
        Thread OrderThread = new Thread(new stock(startT));
        OrderThread.start();
        
     
        Thread ExchangeThread = new Thread(new Exchange(startT));
        ExchangeThread.start();
        
        
        Thread CleanUpThread = new Thread(new test(startT));
        CleanUpThread.start();        
    }
}
