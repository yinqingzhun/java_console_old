package com.yqz.console.tech.stock;

import java.util.*;
import java.io.*;

public class stock implements Runnable{
    //Perform I/O operation
    public static Queue qq ;
    private long startT;
    Exchange exchange;
    BufferedReader br;
    static boolean isQueued;
    
    stock(long start){
        qq= new Queue();
        startT = start;
        BufferedReader br = null;
    }
    
    public void run()
    {       
        try {
            String actionString;
            br = new BufferedReader(new FileReader("E:\\code\\workspace\\java_console\\src\\main\\java\\com\\yqz\\console\\stock\\input2.txt"));
            while ((actionString = br.readLine()) != null) {
                performAction(actionString);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null){br.close(); isQueued = true;}
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }        
    }
    
    public void performAction(String actionString){
        Order a= new Order();
        a.fill(actionString);
        if(a.inTime != -1 && !((a.name).equals(".")) && a.expTime != -1 && a.qty != -1 && a.price != -1)
        {
            while(((double)(System.currentTimeMillis()-startT))/1000 < a.inTime) {}
            qq.enque(a);
        }
        print(a,((double)(System.currentTimeMillis()-startT))/1000);
        }               
    
    public void print(Order a,double enqTime){
        try{
        FileOutputStream fos = new FileOutputStream("order.txt",true);
        PrintStream p = new PrintStream(fos);
        if(a.inTime == -1 || (a.name).equals(".") || a.expTime == -1 || a.qty == -1 || a.price == -1)
        {
            System.out.println("EXCEPTION");
            return;
        }
        if(a.type)
            System.out.println("准备卖出-->"+enqTime+" "+a.inTime+" "+a.name+" "+a.expTime+" "+"sell "+a.qty+" "+a.stock+" "+a.price+" "+a.partial);
        else System.out.println("准备买入-->"+enqTime+" "+a.inTime+" "+a.name+" "+a.expTime+" "+"buy "+a.qty+" "+a.stock+" "+a.price+" "+a.partial);       
      } catch(FileNotFoundException e)
        {System.out.println("EXCEPTION " + e);}
    }
    
}

class Order
 {
    public Order next;
    public long inTime;
    public String name;
    public int expTime;
    public boolean type;//sell=true buy=false
    public int qty;
    public String stock;
    public int price;
    public boolean partial;
    public boolean dead;
    Order(){dead=false;}
    public void fill(String str) 
    {
       String[] ss= str.split("\\s");
       int i=0;
        
        StringTokenizer stoken1 = new StringTokenizer(str);
        try{
            inTime = Long.parseLong(ss[i++]);
            if(inTime<0) throw new Exception();
        } catch(Exception e){        
        e.printStackTrace();
        inTime=-1;        
    }
    try{
        name = ss[i++];
        if(!name.matches("[a-zA-Z]+"))
            throw new Exception();
    }catch(Exception e){
        e.printStackTrace();
        name = ".";
    }
    try{
        expTime = Integer.parseInt(ss[i++]);
        if(expTime<0) throw new Exception();
    }catch(Exception e){        
        e.printStackTrace();
        expTime=-1;
    }
     try{
        String s = ss[i++];
        if((s.toLowerCase()).equals("buy"))
            type = false;
        else if((s.toLowerCase()).equals("sell"))
            type = true;
        else throw new Exception();            
    }catch(Exception e){
        e.printStackTrace();
    }
    try{
        qty = Integer.parseInt(ss[i++]);
        if(qty<0) 
            throw new Exception();
    }catch(Exception e){        
        qty = -1;
        e.printStackTrace();
    }
    try{
        stock = ss[i++];
    }catch(Exception e){
        e.printStackTrace();
        stock=".";
    }
    try{
        price = Integer.parseInt(ss[i++]);
        if(price<0) throw new Exception();
    }catch(Exception e){        
        price=-1;
        e.printStackTrace();
    }
    try{
        String s =(ss[i++]).toLowerCase();
        if(s.equals("t")) partial= true;
        else if (s.equals("f")) partial= false;
        else throw new Exception();
    }catch(Exception e){        
        System.out.println("EXCEPTION.. ");
        e.printStackTrace();
    }
  }
}

class Queue
{
    LinkedList ll;
    int size;
    Queue(){
        ll= new LinkedList();
        size = 0;
    }
    
    public void enque(Order a)
    {
        ll.add(a);
        size++;
    }
    
    public int getSize()
    {
        return size;
    }
    
    public Order deque()
    {
        try
        {
            return ll.removeLast();
        } catch(NoSuchElementException e)
        {
            System.out.println("EXCEPTION"+e);
        }
        return null;
    }
    public boolean isEmpty()
    {
        if(ll.size()==0)
            return true;
        else return false;
    }
}

class LinkedList
{
    Order parser=null;
    Order head = null;
    Order parser2=null;
    int siz=0;
    synchronized void add( Order a)
    {
      a.next=head;
      head=a;
      parser=head;
      siz++;
    }    
    synchronized Order removeLast()
    {        
        if(head==null)
            return null;
        if(head.next==null)
            {
                Order temp2 = head;
                head=null;
                siz--;
                return temp2;
            }
        siz--;
        Order a = head;
        while(a.next.next != null)
            a=a.next;
        Order temp = a.next;
        a.next=null;
        return temp;
    }
    synchronized int size()
    {
        return siz;
    }
    synchronized boolean isEmpty()
    {
        if(head==null)
            return true;
        else return false;
    }
    synchronized Order show()
    {
        Order temp=parser;
        parser= parser.next;
        return temp;
    }
    synchronized Order show2()
    {
        Order temp=parser2;
        parser2= parser2.next;
        return temp;
    }
    void remove(Order a){}
    synchronized boolean hasSome()
    {
        if(parser==null)
            return false;
        else return true;        
    }
    boolean hasSome2()
    {
        if(parser2==null)
            return false;
        else return true;        
    }
    void resetParser()
    {
       parser=head;
    }
    void resetParser2()
    {
       parser2=head;
    }
    void clear()
    {
        head=null;
        parser=null;
    }
    synchronized Order getHead(){
        Order temp;
        temp=head;
        return temp;
    }
}