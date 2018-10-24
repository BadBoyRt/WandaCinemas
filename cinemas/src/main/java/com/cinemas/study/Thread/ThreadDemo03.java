package com.cinemas.study.Thread;

/**
 * java中一切皆对象。线程操作资源类。现在线程有了，资源类是什么呢？就是对初始值为0的变量+1或者-1，那这个变量放在哪里呢？那肯定是放在一个对象里、也就是放在一个类里边
 * 		并且对外提供了同步方法供线程调用。
 */
class ShareData{
	private int number = 0;
	
	public synchronized void increment() throws InterruptedException {
		
		while/*if*/(number != 0) {//0的时候加1
			this.wait();
		}
		number++;
		System.out.println(Thread.currentThread().getName()+"\t"+number);
		//生产完了，通知消费者用吧
		this.notifyAll();
	}
	
	public synchronized void decrement() throws InterruptedException {
		while/*if*/(number == 0) {//0的时候不能再减了，为负了
			this.wait();
		}
		number--;
		System.out.println(Thread.currentThread().getName()+"\t"+number);
		//生产完了，通知消费者用吧
		this.notifyAll();
	}
}



/**
 * 题目：两个线程，实现对一个初始值为0 的变量操作。一个加1 ，一个减1， 来10次。
 * 
 * 生产者消费者+等待+通知唤醒
 * 
 * @author rongtao
 * create date:2018年10月22日 下午1:46:31
 */
public class ThreadDemo03 {
	
	public static void main(String[] args) {
		/**
		 * wait和notify是Object类中的方法。(5个常用的Object类的方法：toString，equals，hashCode，wait,notify)
		 * wait和sleep的区别：就在于是否交出控制权。wait停了交出控制权。sleep控制权不变，醒了以后继续走。
		 * wait和notify必须在同步方法里面，或者同步块里面。
		 */
		
		final ShareData sd = new ShareData();
		
		new Thread(
				() -> {
					for (int i = 0; i < 10; i++) {
						try {
							//Thread.sleep(200);
							sd.increment();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}, "A线程+1:").start();
		
		new Thread(
				() -> {
					for (int i = 0; i < 10; i++) {
						try {
							//Thread.sleep(300);
							sd.decrement();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}, "B线程-1:").start();
		
		//注意：两个线程此时结果是正常的1，0,1,0相互交替。那如果四个线程会发生什么情况呢？
		
		new Thread(
				() -> {
					for (int i = 0; i < 10; i++) {
						try {
							//Thread.sleep(400);
							sd.increment();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}, "C线程 +1:").start();
		
		new Thread(
				() -> {
					for (int i = 0; i < 10; i++) {
						try {
							//Thread.sleep(500);
							sd.decrement();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}, "D线程-1:").start();
		//这时候数据就是乱的了，那下一步模拟网络延时，为各个线程加上sleep
		/**
		 * 这个时候就是wait和notify的事了，这个叫做线程的虚假唤醒。
		 * 结果是:
		 * A线程+1:	1
			B线程-1:	0
			A线程+1:	1
			D线程-1:	0
			C线程 +1:	1
			B线程-1:	0
			A线程+1:	1
			B线程-1:	0
			C线程 +1:	1
			A线程+1:	2
			1后边应该是0，为什么是2。C线程+1完事后，应该做减法，为什么执行了A加法线程呢？
			if(number != 0) {
				this.wait();//以前两个线程，不是增就是减，所以没问题。
				具体的流程就是：C线程执行完后number=1，然而此时又被A线程抢到了执行，此时number=1，wait了。此时外边有3个线程，即2减1加的线程。
				希望减法的线程抢到，但是却又被加法的线程C抢到了。number=1，所以C又wait了。此时外边剩下两个减法的线程，所以走减法的线程，number--之后变成0
				此时再调用加法的线程，由于a、c都wait了，所以a、c被唤醒连续执行了两次加法。所以变成2.
				
				问题是：线程唤醒以后，没有拉回来重新进行判断。
			}		
		 */
		
		//*******注意：多线程编程当中，永远不允许用if判断，只允许用while。 wait()方法只允许在循环中使用，即while,那把资源类的if改成while
		
	}

}
