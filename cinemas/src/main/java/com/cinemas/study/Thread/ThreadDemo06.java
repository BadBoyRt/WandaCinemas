package com.cinemas.study.Thread;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 按序接力（A->B->C-A）
 * 
 * A线程打印2次，B线程打印3次，C线程打印4次
 * 接着
 * A线程打印2次，B线程打印3次，C线程打印4次
 * 共计来3轮
 * 
 * @author rongtao
 * create date:2018年10月23日 下午3:48:50
 */
public class ThreadDemo06 {

	public static void main(String[] args) {
		
		final shareResource se = new shareResource();
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				for (int i = 1; i <= 3; i++) {
					se.loopA(i);
				}
			}
		}, "A").start();
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				for (int i = 1; i <= 3; i++) {
					se.loopB(i);
				}
			}
		}, "B").start();
		
		new Thread(() -> {
					for (int i = 1; i <= 3; i++) {
						se.loopC(i);
					}
				}, "C").start();
	}
}

class shareResource{
	
	private int flag = 1;//标志位：1-->a,2-->b,3-->c
	private Lock lock = new ReentrantLock();
	private Condition c1 = lock.newCondition();
	private Condition c2 = lock.newCondition();
	private Condition c3 = lock.newCondition();
	
	/**
	 * 注意模板
	 */
	/*lock.lock();
	try {
		
	} catch (Exception e) {
		e.printStackTrace();
	}finally {
		lock.unlock();
	}*/
	public void loopA(int loopNum) {
		lock.lock();
		try {
			//1.判断
			if(flag != 1) {
				c1.await();
			}
			//2.做事
			for (int i = 1; i <= 2; i++) {
				System.out.println(Thread.currentThread().getName() + "\t" + i + "\t 第几次循环：" + loopNum);
			}
			//3.通知+唤醒下一个接力的线程
			flag = 2;
			c2.signal();
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			lock.unlock();
		}
	}
	
	public void loopB(int loopNum) {
		lock.lock();
		try {
			//1.判断
			if(flag != 2) {
				c2.await();
			}
			//2.做事
			for (int i = 1; i <= 3; i++) {
				System.out.println(Thread.currentThread().getName() + "\t" + i + "\t 第几次循环：" + loopNum);
			}
			//3.通知+唤醒下一个接力的线程
			flag = 3;
			c3.signal();
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			lock.unlock();
		}
	}
	
	public void loopC(int loopNum) {
		lock.lock();
		try {
			//1.判断
			if(flag != 3) {
				c3.await();
			}
			//2.做事
			for (int i = 1; i <= 4; i++) {
				System.out.println(Thread.currentThread().getName() + "\t" + i + "\t 第几次循环：" + loopNum);
			}
			//3.通知+唤醒下一个接力的线程
			flag = 1;
			c1.signal();
			
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			lock.unlock();
		}
	}
}
