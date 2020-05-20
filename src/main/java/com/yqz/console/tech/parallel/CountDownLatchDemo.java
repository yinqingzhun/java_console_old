package com.yqz.console.tech.parallel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class CountDownLatchDemo {

	 public static void main(String[] args) throws InterruptedException {
	        List<TaskItem> taskItems=new ArrayList<TaskItem>();
	        for(int i=0;i<20;i++){
	            taskItems.add(new TaskItem(i, "task "+i));
	        }
	        
	        CountDownLatch threadsSignal = new CountDownLatch(taskItems.size());
	        ExecutorService executor = Executors.newFixedThreadPool(taskItems.size());
	        List<Future<MyTaskResult>> resultLazyItems=new ArrayList<Future<MyTaskResult>>();
	        System.out.println("主線程開始進入並行任務提交");
	        for (TaskItem taskItem : taskItems) {
	            // 使用future存储子线程执行后返回结果，必须在所有子线程都完成后才可以使用get();
	            // 如果在这里使用get(),会造成等待同步。
	            Future<MyTaskResult> future = executor.submit(new MyTask(threadsSignal,taskItem));
	            resultLazyItems.add(future);
	        }
	        System.out.println("主線程開始走出並行任務提交");
	        System.out.println("主線程進入等待階段（等待所有并行子线程任务完成）。。。。。");
	        // 等待所有并行子线程任务完成。
	        threadsSignal.await();
	        // 并不是终止线程的运行，而是禁止在这个Executor中添加新的任务
	        executor.shutdown(); 
	        System.out.println("主線程走出等待階段（等待所有并行子线程任务完成）。。。。。");
	        
	        for(Future<MyTaskResult> future :resultLazyItems){
	            try {
	                MyTaskResult result = future.get();
	                System.out.println(result.getName());
	            } catch (ExecutionException e) {
	                // TODO Auto-generated catch block
	                e.printStackTrace();
	            }
	        }
	    }
	 

	static class MyTaskResult {
	    private String name;
	    
	    public MyTaskResult(String name){
	        this.name=name;
	    }

	    public String getName() {
	        return name;
	    }

	    public void setName(String name) {
	        this.name = name;
	    }
	}
	

	static class TaskItem {
	    private int id;
	    private String name;
	    
	    public TaskItem(int id,String name){
	        this.id=id;
	        this.name=name;
	    }
	    
	    public int getId() {
	        return id;
	    }
	    public void setId(int id) {
	        this.id = id;
	    }
	    
	    public String getName() {
	        return name;
	    }
	    public void setName(String name) {
	        this.name = name;
	    }
	}
	
	 
	static class MyTask implements Callable<MyTaskResult> {
	    private final TaskItem taskItem;
	    private final CountDownLatch threadsSignal;
	    
	    public MyTask(CountDownLatch threadsSignal,TaskItem taskItem) {
	        this.threadsSignal= threadsSignal;
	        this.taskItem=taskItem;
	    }
	    
	    @Override
	    public MyTaskResult call() throws Exception {
	        MyTaskResult result=new MyTaskResult(this.taskItem.getName());
	        
	        // 核心处理逻辑处理
	        Thread.sleep(2000);
	        
	        System.out.println("task id:" + taskItem.getId() +" >>>>等待結束");
	        System.out.println("task id:" + taskItem.getId() + " >>>>线程名称:" + Thread.currentThread().getName() + "结束. 还有" + threadsSignal.getCount() + " 个线程");

	        // 必须等核心处理逻辑处理完成后才可以减1
	        this.threadsSignal.countDown();
	        
	        return result;
	    }

	}
	

	   

	 
}
