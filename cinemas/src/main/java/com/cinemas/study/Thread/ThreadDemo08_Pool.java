package com.cinemas.study.Thread;

import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * 第四种创建线程的方法：-------线程池
 * 
 * 模拟去银行办业务。
 */

public class ThreadDemo08_Pool {

	public static void main(String[] args) {
		//第四种线程池：调度
		ScheduledExecutorService service = Executors.newScheduledThreadPool(5);//5个操作员
		ScheduledFuture<Integer> result = null;
		try {
			for (int i = 1; i <= 20; i++) {//20个客人上门
				result = service.schedule(new Callable<Integer>() {//schedule方法返回值ScheduledFuture<V>
					@Override
					public Integer call() throws Exception {
						System.out.print(Thread.currentThread().getName());
						return new Random().nextInt(10);
					}
				}, 3, TimeUnit.SECONDS);//delay：延时,unit:单位,通过查看schedule方法参数，知道unit是枚举类型。
				System.out.println("#############"+result.get());
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			service.shutdown();//关闭
		}
	}

	/**
	 * 三种线程池
	 * 
	 */
	private static void test_Thread_Pool() {
		//ExecutorService service = Executors.newFixedThreadPool(5);//第一种：Fixed适配,银行五个收银员
		//ExecutorService service = Executors.newSingleThreadExecutor();//第二种：只创建1个线程：20个顾客，只有一个操作员服务。
		ExecutorService service = Executors.newCachedThreadPool();//第三种：根据业务逻辑【自适应】创建线程个数。
		
		Future<Integer> result = null;
		
		try {
			for (int i = 1; i <= 20; i++) {//20个客人上门
				//提交
				result = service.submit(new Callable<Integer>() {//没有new线程，线程由池子创建好了
					@Override
					public Integer call() throws Exception {
						System.out.print(Thread.currentThread().getName());
						return new Random().nextInt(10);
					}
				});
				System.out.println("#############"+result.get());
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			service.shutdown();//关闭
		}
	}
}
