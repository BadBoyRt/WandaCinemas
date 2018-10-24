package com.cinemas.study.Thread;

import java.util.Random;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 读写锁
 * 
 * 1个线程写操作，100个线程过来读。
 * 
 * @author rongtao create date:2018年10月23日 下午5:08:59
 */
public class ThreadDemo07 {
	public static void main(String[] args) throws InterruptedException {
		final shareData_new sd = new shareData_new();
		new Thread(() -> {
			sd.write(new Random().nextInt(100));
		}, "write thread:").start();

		Thread.sleep(200);//针对obj=null的情况
		
		for (int i = 1; i <= 100; i++) {
			new Thread(() -> {
				sd.read();
			}, "read Thread:").start();
		}
	}
	
	/**
	 * 但是却出现了 这样的结果:(当然，这样的结果也不是总发生的)
	 * 
	 *  read Thread:	null
		read Thread:	null
		write thread:	70
		read Thread:	70
		read Thread:	70
		read Thread:	70
		
		本来应该是write的线程先写一个数，然后读的线程才获取，但是现在却出现了write的线程还没写，却有几个读的线程抢读了Obj，
			但是此时write的线程还没有进行写操作。所以read的线程抢读到的是Obj = null
			
		解决办法：就是在write线程后边加sleep，这样就能保证read线程执行时，obj对象不为null
	 */
	
}

class shareData_new {

	private Object obj;
	private ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();// 读写锁

	public void read() {
		// 由于读写锁，读和写没有分开，所以写法特殊。并不是lock.lock();
		rwLock.readLock().lock();
		try {
			System.out.println(Thread.currentThread().getName() + "\t" + obj);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			rwLock.readLock().unlock();
		}
	}

	public void write(Object obj) {
		rwLock.writeLock().lock();
		try {
			this.obj = obj;
			System.out.println(Thread.currentThread().getName() + "\t" + obj);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			rwLock.writeLock().unlock();
		}
	}

}