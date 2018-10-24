package com.cinemas.study.Thread;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 将demo3中的synchrnoized换成lock，unlock。
 * 			wait，notify换成condition的await，signalAll
 * 
 * @author rongtao
 * create date:2018年10月22日 下午5:10:14
 */
public class ThreadDemo04 {

	public static void main(String[] args) {
		final ShareData1 sd1 = new ShareData1();
		new Thread(() -> {
					for(int i = 0; i < 10; i++) {
						try {
							sd1.increment();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}, "A").start();
		
		new Thread(() -> {
					for(int i = 0; i < 10; i++) {
						try {
							sd1.decrement();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}, "B").start();
	}
}


class ShareData1{
	private int number = 0;
	private Lock lock = new ReentrantLock();
	private Condition condition = lock.newCondition();
	
	
	public void increment() throws InterruptedException {
		lock.lock();
		try {
			while(number != 0) {
				condition.await();//this.wait();
			}
			number++;
			System.out.println(Thread.currentThread().getName()+"\t"+number);
			condition.signalAll();//this.notifyAll();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			lock.unlock();
		}
	}
	
	public void decrement() throws InterruptedException {
		lock.lock();
		try {
			while(number == 0) {
				condition.await();//this.wait();
			}
			number--;
			System.out.println(Thread.currentThread().getName()+"\t"+number);
			condition.signalAll();//this.notifyAll();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
	}
}