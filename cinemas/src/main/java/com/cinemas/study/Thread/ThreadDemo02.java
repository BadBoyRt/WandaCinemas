package com.cinemas.study.Thread;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;


class MyThreadB implements Callable<Integer>{

	@Override
	public Integer call() throws Exception {
		System.out.println("come in call");
		return new Random().nextInt(100);
	}
	
}

/**
 *	第三种创建线程的方式：Callable接口
 * 
 * Callable函数式接口和Runnable接口的区别
 * 
 * Callable:返回结果并可能引发异常的任务。 实现者定义一个没有参数的单一方法，称为call。Callable接口类似于Runnable
 * 			然而，A Runnable不返回结果，也不能抛出被检查的异常。 
 * 
 * call()：计算一个结果，如果不能这样做，就会抛出一个异常。
 * 
 * 区别：1.接口名字不同，接口里边的方法也不同。一个是run，一个是call。
 * 		2.是否可以抛出异常（callable可以，runnable不行）
 * 		3.是否又返回值。（callable有返回值。runnable没有）
 */
public class ThreadDemo02 {
	
	public static void main(String[] args) {
		
		FutureTask  ft = new FutureTask(new MyThreadB());
		
		new Thread(ft).start();
		/**
		 * 在线程中，此构造方法几乎是【通配的】	Thread(Runnable target, String name) 
		 * 但是Thread的构造方法中没有接收Callable接口的构造方法,都是接收Runnable接口的。
		 */	
		
		/**	&思想&
		 * 1、退回原点。
		 * 2、退回原点，拿【多态】思想方式，用接口过桥。
		 * 
		 * 即，凡接口必有实现类，如果一个类同时实现了runnable和callable这两个接口。---------适配器模式。
		 * 找到Runnable接口的子接口RunnableFuture，进而找到RunnableFuture接口的实现类FutureTask，
		 * 		FutureTask的构造方法接收Callable接口（传接口，接口可以多实现）
		 */
		
		//获取返回值
		try {
			System.out.println(ft.get());
		} catch (InterruptedException | ExecutionException e) {//注意写法
			e.printStackTrace();
		}
	}

}
