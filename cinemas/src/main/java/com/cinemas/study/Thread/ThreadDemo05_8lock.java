package com.cinemas.study.Thread;

/**
 * 线程8锁
 * 
 * 1.两个线程标准访问普通的同步方法，先打印 ios 还是 android	----------------------ios
 * 2.新增Thread.sleep(4000)，先打印 ios 还是 android			--------------ios
 * 3.新增getHello()普通方法，先打印 ios 还是 hello				--------------hello
 * 4.有两部手机，先打印 ios 还是 android							--------------Android
 * 5.两个静态同步方法，同一部手机，先打印 ios 还是 android				--------------ios
 * 6.两个静态同步方法，俩部手机，先打印 ios 还是 android				--------------ios
 * 7.1个静态同步方法，1个普通同步方法，1部手机，先打印 ios 还是 android	--------------Android
 * 8.1个静态同步方法，1个普通同步方法，2部手机，先打印 ios 还是 android	--------------Android
 * 
 * 1.一个对象里面如果有多个synchronized方法，某一个时刻内，只要一个线程去调用其中的一个synchronized方法了，
 * 其它的线程都只能等待，换句话说，某一个时刻内，只能有唯一一个线程去访问这些synchronized方法

 * 2.锁的是当前对象this（锁住的是new出来的实例对象），被锁定后，其它的线程都不能进入到当前对象的其它的synchronized方法

 * 3.加个普通方法后发现和同步锁无关

   4.换成两个对象后，不是同一把锁了，情况立刻变化。

	5.6.如果是两个普通的同步方法，两部手机，你锁的是phone和phone2（锁住的是new出来的实例对象），
	但是两个同步方法加了static，静态是全局类，锁住的是phone.class类
	
	7.8. 一个锁住的是phone。class，一个锁住的是实例对象，没有竞争。
 * @author rongtao
 * create date:2018年10月23日 下午12:14:19
 */
public class ThreadDemo05_8lock {

	public static void main(String[] args) {
		
		final Phone phone = new Phone();
		final Phone phone2 = new Phone();//4.
		new Thread(
				() ->{
					try {
						phone.getIos();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}, "A").start();
		new Thread(
				() ->{
					try {
						//phone.getAndroid();
						//phone.getHello();
						phone2.getAndroid();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}, "B").start();
	}
}

class Phone{
	public static synchronized void getIos() throws Exception 
	{
		Thread.sleep(4000);
		System.out.println("get ios");
	}
	
	public /*static*/ synchronized void getAndroid() throws Exception 
	{
		System.out.println("get Android");
	}
	
	public void getHello() throws Exception
	{
		System.out.println("get hello");
	}
}
