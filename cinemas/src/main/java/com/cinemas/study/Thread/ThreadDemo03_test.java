package com.cinemas.study.Thread;

class Thread_Test{
	private int flag = 1;
	public synchronized void printNumber(int number) throws InterruptedException {
		while(flag % 3 == 0) {
			this.wait();
		}
		flag++;
		System.out.print(number);
		this.notifyAll();
	}
	
	public synchronized void printChar(char str) throws InterruptedException {
		while(flag % 3 != 0) {
			this.wait();
		}
		flag++;
		System.out.print(str);
		this.notifyAll();
	}
}

/**
 * 写两个线程，一个线程打印1~52，另一个线程打印字符A~Z，打印顺序为12A34B56C...5152Z,要求用线程间的通信。
 * 
 * @author rongtao
 * create date:2018年10月22日 下午3:44:36
 */
public class ThreadDemo03_test {

	public static void main(String[] args) {
		final Thread_Test tt = new Thread_Test();
		new Thread(
				() ->{
					for (int i = 1; i < 53; i++) {
						try {
							tt.printNumber(i);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}, "A线程：").start();
		new Thread(
				() ->{
					for (char i ='A'; i <= 'Z'; i++) {
						try {
							tt.printChar(i);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}, "B线程：").start();
	}
}
