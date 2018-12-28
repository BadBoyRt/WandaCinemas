package com.cinemas.study.java8;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntPredicate;
import java.util.function.IntSupplier;
import java.util.function.Predicate;
import java.util.function.Supplier;

import com.cinemas.domain.Apple;


public class Java8_Lambda {
	
	/**
	 * 
	 * 有时间吧这篇整理成文章，即从行为参数化~lambda,把这个思想用自己的话表达出来.
	 * 
	 * java8中常用函数式接口：
	 * java.util.function.~
	 * 
	 * 	函数式接口				描述符			原始类型特化
	 * Predicate<T> 	T->boolean 		IntPredicate,LongPredicate, DoublePredicate		设计到类型T的布尔表达式时使用此函数式接口
	 * 
	 * Consumer<T> 		T->void 		IntConsumer,LongConsumer, DoubleConsumer		定义了一个accept的抽象方法，用于接收T对象，并且没有返回值。
	 * 																						如果你需要访问T对象，并且对其【执行某些操作】，即可使用此函数式接口。
	 * 
	 * Function<T,R>	T->R 			IntFunction<R>,	IntToDoubleFunction, IntToLongFunction
	 * 									LongFunction<R>, LongToDoubleFunction, LongToIntFunction,	
	 * 									DoubleFunction<R>,												接收T对象，返回R对象
	 * 									ToIntFunction<T>, ToDoubleFunction<T>, ToLongFunction<T>
	 * 
	 * Supplier<T> 		()->T 			BooleanSupplier, IntSupplier, LongSupplier, DoubleSupplier	返回T对象（常用于创建对象）
	 * 
	 * UnaryOperator<T>  T->T 			IntUnaryOperator, LongUnaryOperator, DoubleUnaryOperator	
	 * 
	 * BinaryOperator<T> (T,T)->T 		IntBinaryOperator, LongBinaryOperator, DoubleBinaryOperator		同类型运算
	 * 
	 * BiPredicate<L,R>  (L,R)->boolean
	 * 
	 * BiConsumer<T,U>   (T,U)->void	ObjIntConsumer<T>, 	ObjLongConsumer<T>, ObjDoubleConsumer<T>
	 * 
	 * BiFunction<T,U,R> (T,U)->R 		ToIntBiFunction<T,U>, ToLongBiFunction<T,U>, 
	 * 									ToDoubleBiFunction<T,U>
	 * **/
	
	
	public static void main(String[] args) {
		
		List<Apple> redList = new ArrayList<Apple>();
		Apple apple1 = new Apple();
		apple1.setColor("red");
		apple1.setHeight(500);
		Apple apple2 = new Apple();
		apple2.setColor("blue");
		apple2.setHeight(20);
		Apple apple3 = new Apple();
		apple3.setHeight(120);
		apple3.setColor("a");
		redList.add(apple1);
		redList.add(apple2);
		redList.add(apple3);
		
		Predicate<Apple> testPredicate = (Apple apple) -> apple.getHeight()<30;
		boolean test = testPredicate.test(apple2);
		
		
		
		//筛选苹果
		List<Apple> redApples = filter(redList, (Apple apple) -> "red".equals(apple.getColor()) && 50 < apple.getHeight());
		//获取苹果的重量
		for(Apple apple : redList) {
			IntSupplier height = () -> apple.getHeight();
			//System.out.println(height.getAsInt());
		}
		
		//改变了苹果的颜色
		testConsumer(redList, 
				(Apple apple) -> {apple.setColor("Yellow");System.out.println(apple);});
		//接收一个apple类型参数，返回Integer类型
		List<Integer> testFunction = map(redList, (Apple apple) -> apple.getHeight());//int->Integer 装箱
		
		//但这在性能方面是要付出代价的。装箱后的值本质上就是把原始类型包裹起来，并保存在堆里。因此，装箱后的值需要更多的内存，并需要额外的内存搜索来获取被包裹的原始值
		
		//避免装箱
		IntPredicate evenNumbers = (int i) -> i % 2 == 0;
		//装箱
		Predicate<Integer> oddNumbers = (Integer i) -> i % 2 == 1;
		
		
		/*任何函数式接口都不允许抛出受检异常（checked exception）。
		 * 如果你需要Lambda表达式来抛出异常，有两种办法：定义一个自己的函数式接口，并声明受检异常，
		 * 或者把Lambda包在一个try/catch块中*/
		Function<BufferedReader, String> f = (BufferedReader b) -> {//接收BufferedReader->返回String
			try {
				return b.readLine();
			}
			catch(IOException e) {
				throw new RuntimeException(e);
			}
		};
		
		
		
		/**
		 * 方法引用主要有三类:
		 * 1.指向静态方法的方法引用Integer.parseInt
		 * 2.指向任意类型实例方法的方法引用String.length
		 * 3.指向现有对象的实例方法的方法引用Apple.getColor
		 * 
		 */
		
		//方法引用
		List<String> list = Arrays.asList("a","b","A","B");
		list.sort((String s1, String s2) -> s1.compareToIgnoreCase(s2));
		//Java编译器可以根据Lambda出现的上下文来推断Lambda表达式参数的类型
		//下面省去String声明
		list.sort((s1, s2) -> s1.compareToIgnoreCase(s2));
		//方法引用写法
		list.sort(String::compareToIgnoreCase);
		
		for(String string : list) {
			System.out.println(string);
		}
		
		//构造函数引用
		Supplier<Apple> appleNew = () ->  new Apple();//创建一个apple对象
		Supplier<Apple> appleNew_1 = Apple::new;
		BiFunction<String, Integer, Apple> c3 = Apple::new;
		BiFunction<String, Integer, Apple> c3_lambda = (color, hight) -> new Apple(color, hight);
		Apple a3 = c3.apply("green", 110);
		Apple a4 = c3_lambda.apply("green", 110);
		System.out.println(a4);
		
		Integer.parseInt("1");
		
		//根据重量对苹果进行排序
		redList.sort(Comparator.comparing((a) -> a.getHeight()));//lambda
		redList.sort(Comparator.comparing(Apple::getHeight));//方法引用
		
		
		
		/**
		 * 复合lambda表达式
		 * 
		 * **/
		//1.比较器复合
		redList.sort(Comparator.comparing(Apple::getHeight).reversed());//方法引用,重量逆序
		//当两个苹果重量一样时，按照颜色进行排序
		redList.sort(Comparator.comparing(Apple::getHeight).thenComparing(Apple::getColor));
		/**
		 * 2.谓词复合
		 * 			negate	非
		 * 			and		和
		 *			 or		或
		 * **/
		Predicate<Apple> preApple = (a) -> a.getHeight()>120;//判断重量大于120，
		preApple.negate().test(a3);//a3重量110小于120，显然test返回false，negate代表非，所以返回true
		preApple.and((a)->"green".equals(a.getColor()));//在重量小于120的同时，再加上绿色的筛选条件
		preApple.test(a3);//true
		preApple.or((a)->"green".equals(a.getColor()));
		
		/**
		 * 函数复合
		 * Function接口的表达式，andThen和compose两个默认方法
		 * 
		 * 先加1，再乘以2
		 */
		Function<Integer, Integer> z = x -> x + 1;
		Function<Integer, Integer> g = x -> x * 2;
		Function<Integer, Integer> h = z.andThen(g);
		int result = h.apply(1);
		
	}
	
	public static void out() {
		System.out.println("----------------------------------------------");
	}

	//Predicate<T>
	public static <T> List<T> filter(List<T> list, Predicate<T> p){
		List<T> result = new ArrayList<>();
		for(T e: list){
			if(p.test(e)){
				result.add(e);
			}
		}
		return result;
	}
	
	//Consumer<T>
	public static <T> void testConsumer(List<T> list, Consumer<T> c) {
		for(T i : list) {
			c.accept(i);
		}
	}

	//Function<T, R>
	public static <T, R> List<R> map(List<T> list,
			Function<T, R> f) {
			List<R> result = new ArrayList<>();
			for(T s: list){
				result.add(f.apply(s));
			}
			return result;
	}
}
