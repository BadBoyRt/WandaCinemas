package com.cinemas.study.java8;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.cinemas.domain.Dish;
import com.cinemas.domain.Trader;
import com.cinemas.domain.Transaction;

public class Java8_Stream {
	
	public static void main(String[] args) {
		/**
		 * 流-stream\
		 * 流是针对集合的一个升级，
		 * 1.类似数据库的操作
		 * 比如你利用sql group by type  但是使用集合却无法实现，流可以。
		 * 流的数据处理功能支持类似于数据库的操作，以及函数式编程语言中的常用操作，如filter、map、reduce、find、match、sort等。流操作可以顺序执行，也可并行执行
		 * 2。针对并行处理大量数据的简化。
		 * 
		 */
		List<Dish> menu = Arrays.asList(
				new Dish("pork", false, 800, Dish.Type.MEAT),
				new Dish("beef", false, 700, Dish.Type.MEAT),
				new Dish("chicken", false, 400, Dish.Type.MEAT),
				new Dish("french fries", true, 530, Dish.Type.OTHER),
				new Dish("rice", true, 350, Dish.Type.OTHER),
				new Dish("season fruit", true, 120, Dish.Type.OTHER),
				new Dish("pizza", true, 550, Dish.Type.OTHER),
				new Dish("prawns", false, 300, Dish.Type.FISH),
				new Dish("salmon", false, 450, Dish.Type.FISH) );
		
		Stream<String> names = 
				menu.stream()
					.filter(d -> d.getCalories() > 300)//热量大于300
					.map(Dish::getName)//获取名字。
					.limit(3);//前三个
					//.collect(Collectors.toList());//stream->list
		
		names.forEach(System.out::println);
		//  请注意，和迭代器类似，流只能遍历一次。遍历完之后，我们就说这个流已经被消费掉了
		//names.forEach(System.out::println);
		//java.lang.IllegalStateException: stream has already been operated upon or closed
		/**
		 * 集合和流的区别
		 * 
		 * 集合与流之间的差异就在于什么时候进行计算。
		 * 
		 * 流：其中元素按需计算。
		 * 
		 * 集合：从一个装满的仓库，if条件筛选，然后add进集合。
		 * 
		 */
		
		//外部迭代和内部迭代
		//1。for-each外部迭代
		List<String> names1 = new ArrayList<>();
			for(Dish d: menu){
				names1.add(d.getName());
		}
		//2.用for-each背后的迭代器做外部迭代
		List<String> names2 = new ArrayList<>();
		Iterator<Dish> iterator = menu.iterator();
		while(iterator.hasNext()) {
			Dish d = iterator.next();
			names2.add(d.getName());
		}
		//3.流：内部迭代
		List<String> names3 = menu.stream()
				.map(Dish::getName)
				.collect(Collectors.toList());
		
		/**
		 * 流使用三部曲：1.一个数据源（如集合）来执行一个查询
		 * 				2.一个中间操作链，形成一条流的流水线
		 * 					3.一个终端操作，执行流水线，并能生成结果
		 */
				
		//[map]流支持map方法，它会接受一个函数作为参数。这个函数会被应用到每个元素上，并将其映射成一个新的元素
		//给定一个单词列表，你想要返回另一个列表，显示每个单词中有几个字母。怎么做呢？你需要对列表中的每个元素应用一个函数。这听起来正好该用map方法去做.
	
		List<String> words = Arrays.asList("Java 8", "Lambdas", "In", "Action");
		List<Integer> wordLengths = 
				words.stream()
					.map(String::length)
					.collect(Collectors.toList());
		//现在让我们回到提取菜名的例子。如果你要找出每道菜的名称有多长，怎么做？你可以像下面这样，再链接上一个map：
		List<Integer> dishNameLengths = menu.stream()
				.map(Dish::getName)
				.map(String::length)
				.collect(Collectors.toList());
		
		//流的扁平化
		//flatmap方法让你把一个流中的每个值都换成另一个流，然后把所有的流连接起来成为一个流。
		List<String> uniqueCharacters =
				words.stream()
				.map(w -> w.split(""))
				.flatMap(Arrays::stream)//省去则返回 List<String[]>, 将各个生成流扁平化为单个流
				.distinct()
				.collect(Collectors.toList());
		for(String s : uniqueCharacters) {
			System.out.println(s);
		}
		
		/**
		 * 匹配：
		 * 方法返回一个boolean，因此是一个终端操作。
		 */
		//1.anyMatch(流中是否有一个元素能匹配给定的谓词)
		if(menu.stream().anyMatch(Dish::isVegetarian)){
			System.out.println("The menu is (somewhat) vegetarian friendly!!");
		}
		//2.allMatch(流中的所有元素都能匹配给定的谓词)
		boolean isHealthy = menu.stream()
				.allMatch(d -> d.getCalories() < 1000);
		//3.noneMatch(流中没有任何元素与给定的谓词匹配)
		boolean noHealthy = menu.stream()
				.noneMatch(d -> d.getCalories() >= 1000);
		//anyMatch、allMatch和noneMatch这三个操作都用到了我们所谓的短路.即，找到一个元素就返回结果了.
		
		/**
		 * 查找元素:
		 * findFirst和findAny
		 * 如果你不关心返回的元素是哪个，请使用findAny，因为它在使用并行流时限制较少.
		 */
		menu.stream()
			.filter(Dish::isVegetarian)
			.findAny()//findAny()
			.ifPresent(d -> System.out.println(d.getName()));//如果结果集中存在，则输出名字，否则什么都不做，避免了null异常
		//查找第一个元素
		List<Integer> someNumbers = Arrays.asList(1, 2, 3, 4, 5);
		Optional<Integer> firstSquareDivisibleByThree =
				someNumbers.stream()
					.map(x -> x * x)
						.filter(x -> x % 2 == 0)
						.findFirst(); //4
		
		firstSquareDivisibleByThree.ifPresent(i -> System.out.println(i));//4
		
		/**
		 * 归约：将流中所有元素，以某种方式结合起来（比如：累加，累乘等），得到一个值。
		 * 
		 * 1.reduce(initValue, BinaryOperator<T>)
		 * (一个初始值, 一个BinaryOperator<T>来将两个元素结合起来产生一个新值)
		 * 
		 * 2.reduce(BinaryOperator<T>) 返回Optional对象
		 * (无初始值，一个BinaryOperator<T>来将两个元素结合起来产生一个新值)
		 */
		
		//元素求和
		//for-each求和写法
		List<Integer> numbers = Arrays.asList(1, 2, 3, 4);
		int sum = 0;
		for (int x : numbers) {
			sum += x;
		}
		
		//新写法,初始值0，BinaryOperator的lambda表达式，同类型计算加法。
		int sum_reduce = numbers.stream().reduce(0, (a, b) -> a + b);
		
		//使用方法引用:
		int sum_reduce1 = numbers.stream().reduce(0, Integer::sum);//在Java 8中，Integer类现在有了一个静态的sum方法来对两个数求和
		
		//无初始值：
		//是有初始值reduce的一个【重载】方法。但是返回一个Optional对象。
		//为什么它返回一个Optional<Integer>呢？考虑流中没有任何元素的情况。reduce操作无法返回其和，因为它没有初始值。这就是为什么结果被包裹在一个Optional对象里，以表明和可能不存在
		
		//最大最小值
		Optional<Integer> max = numbers.stream().reduce(Integer::max);
		Optional<Integer> min = numbers.stream().reduce(Integer::min);
		//(x, y) -> x < y ? x : y而不是Integer::min后者易读
		
		//问题:怎样用map和reduce方法数一数流中有多少个菜呢？
		//答:你可以把流中每个元素都映射成数字1，然后用reduce求和。
		int count = menu.stream()
				.map(d -> 1)
				.reduce(0, Integer::sum);
		//或者使用流内置count方法
		long count1 = menu.stream().count();
		
		/**
		 * 流实践
		 */
		Trader raoul = new Trader("Raoul", "Cambridge");
		Trader mario = new Trader("Mario","Milan");
		Trader alan = new Trader("Alan","Cambridge");
		Trader brian = new Trader("Brian","Cambridge");
		
		List<Transaction> transactions = Arrays.asList(
				new Transaction(brian, 2011, 300),
				new Transaction(raoul, 2012, 1000),
				new Transaction(raoul, 2011, 400),
				new Transaction(mario, 2012, 710),
				new Transaction(mario, 2012, 700),
				new Transaction(alan, 2012, 950) );
		
		//1.找出2011年的所有交易并按交易额排序（从低到高）
		List<Transaction> tr2011 = transactions.stream()
				.filter(d -> d.getYear() == 2011)
				.sorted(Comparator.comparing(Transaction::getValue))
				.collect(Collectors.toList());
		//2交易员都在哪些不同的城市工作过
		List<String> cities = transactions.stream()
			.map(d -> d.getTrader().getCity())
			.distinct()
			.collect(Collectors.toList());
		//新招：你可以去掉distinct()，改用toSet()，这样就会把流转换为集合
		Set<String> cities1 =
				transactions.stream()
				.map(transaction -> transaction.getTrader().getCity())
				.collect(Collectors.toSet());
		//3查找所有来自于剑桥的交易员，并按姓名排序
		List<Trader> traders = transactions.stream()
				.map(Transaction::getTrader)
				.filter(trader -> trader.getCity().equals("Cambridge"))
				.distinct()
				.sorted(Comparator.comparing(Trader::getName))
				.collect(Collectors.toList());
		
		
			
		
		
		
}
	
}
