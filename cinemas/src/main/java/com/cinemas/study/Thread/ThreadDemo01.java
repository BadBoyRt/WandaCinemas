package com.cinemas.study.Thread;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

//资源类（线程- 操作 - 资源类）
class Ticket
{
	/**
	 * 何为高内聚，低耦合？
	 * 
	 * 高内聚：即线程要操作Ticket这个类，这个类自己要带着方法。高内聚，往类自己身上聚拢。
	 * 低耦合：现在是四个线程+一个资源类。资源类带着同步方法，对外暴露出服务，让线程来调用
	 * 
	 * 比如说：现在有一个学生类，有name、age等属性，通常我们都要生成get、set方法。即对外暴露了get、set方法，让其他类调用。
	 */
	
	private int number = 40;
	private Lock lock = new ReentrantLock();
	
	//以前的方法，要加同步方法和同步代码块。jdk1.5以后出现可重复锁ReentrantLock（也能锁、也能控制）
	public /*synchronized*/ void sale()
	{
		
		lock.lock();
		try 
		{
			if(number > 0)
			{
				System.out.println(Thread.currentThread().getName()+"\t卖出第："+(number--)+"还剩下"+number+"张");
				//现在有一个问题，不加同步或者锁进行限定的货，会出现很多重复票。
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			lock.unlock();
		}
	}
}

/**
 * 复习线程基础知识：4个售票员卖40张票
 * 
 * synchrnoized：所谓加锁，就是避免多个线程争抢同一份资源。即锁提供了对共享资源的独占访问，一次只能有一个线程获得锁。对共享资源的所有访问都需要先获得锁。
 * 
 * 那如何进行多线程编程？(记住下面两点干货)
 *
 * 1.	线程	操作	资源类
 * 2.	高内聚+低耦合
 * 
 */
public class ThreadDemo01 
{
	public static void main(String[] args)
	{
		final Ticket ticket = new Ticket();
		
		//使用匿名内部类或者jdk1.8的lambda表达式
		//Thread的构造方法：Thread(Runnable target, String name) 
		
		/***********************多线程编程模板Start**************************/
		/*new Thread(new Runnable() {//匿名内部类
			
			@Override
			public void run() {
				
			}
		}, "name").start();*/
		/***********************多线程编程模板End**************************/
		
		//多线程操作资源类
		//1.使用匿名内部类。
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				for (int i = 0; i < 50; i++) {
					ticket.sale();
				}
			}
		}, "A").start();
		
		//2.使用jdk1.8的lambda表达式
		new Thread(() -> {
			for (int i = 0; i < 50; i++) {
				ticket.sale();
			}
		}, "lambda").start();
		
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				for (int i = 0; i < 50; i++) {
					ticket.sale();
				}
			}
		}, "B").start();
		new Thread(new Runnable() {
			@Override
			public void run() {
				for (int i = 0; i < 50; i++) {
					ticket.sale();
				}
			}
		}, "C").start();
		
		//到底调用哪个线程，需要线程调度
		
		/**
		 * 
		 * synchronized是java中的关键字，而lock，ReentrantLock是具体的类
		 * 
		 * 但是synchronized不能做到有序分配，ReentrantLock可重复锁是可以控制线程调度顺序的.
		 * 
		 */
	}

}






/**
 * 几个问题：
 * 1.什么是线程、什么是进程？
 * 2.举出工作生活中的例子？
 * 3.
 * */
