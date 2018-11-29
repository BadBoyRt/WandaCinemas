package com.cinemas.study.java8;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.IntSummaryStatistics;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import com.cinemas.domain.Apple;
import com.cinemas.domain.Dish;
import com.cinemas.domain.Dish.Type;
import com.cinemas.domain.Trader;
import com.cinemas.domain.Transaction;

public class Java8_Stream {
	public enum CaloricLevel { DIET, NORMAL, FAT };
	
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
		
		//注意：流的中间操作接收lambda表达式
		
		Stream<String> names = 
				menu.stream()
					.filter(d -> d.getCalories() > 300)//热量大于300
					.map(Dish::getName)//获取名字。
					.limit(3);//前三个
					//.collect(Collectors.toList());//stream->list
		//为了利用多核架构并行执行这段代码，你只需要把stream()换成parallelStream()
		
		Stream<String> parallelNames = 
				menu.parallelStream()
				.filter(d -> d.getCalories() > 300)//热量大于300
				.map(Dish::getName)//获取名字。
				.limit(3);//前三个
				
		
		names.forEach(System.out::println);
		//  请注意，和迭代器类似，流只能遍历一次。遍历完之后，我们就说这个流已经被消费掉了(中间操作不会消耗流，终端操作会消耗流，以产生一个最终结果)
		//names.forEach(System.out::println);
		//java.lang.IllegalStateException: stream has already been operated upon or closed
		/**
		 * 集合和流的区别
		 * 1.外部迭代与内部迭代
		 * 2.集合与流之间的差异就在于什么时候进行计算。
		 * 
		 * 在内部迭代中，项目可以透明的并行处理，或者使用更优化的顺序进行处理。
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
		 * 中间操作与终端操作。
		 * 
		 * 中间操作：filter、map、sorted等中间操作会返回一个流，
		 * 	注意：流的中间操作接收lambda表达式
		 * 
		 * 终端操作：终端操作会从流的流水线生成结果。其结果是任何不是流的值，比如List、Integer，甚至void。例如menu.stream().forEach(System.out::println);
		 * menu.stream().filter(d -> d.getCalories() > 300).distinct().limit(3).count();返回一个long
		 * 
		 * (中间操作不会消耗流，终端操作会消耗流，以产生一个最终结果)
		 */
		System.out.println("----------------------------------------------------");
		List<String> name4 = menu.stream()
				.filter(d -> 
					{
						System.out.println("filter:"+d.getName());
						return d.getCalories() < 500; //写在块里的lambda表达式必须写return，否则报错
					})
				.map(d -> 
					{
						System.out.println("map:"+d.getName());
						return d.getName();
					})
				.limit(3)
				.collect(Collectors.toList());
		name4.forEach(System.out::println);
		System.out.println("----------------------------------------------------");
		
		/**
		 * 流使用三部曲：1.一个数据源（如集合）来执行一个查询
		 * 				2.一个中间操作链，形成一条流的流水线
		 * 					3.一个终端操作，执行流水线，并能生成结果
		 */
		
		//筛选：filter，distinct
		//截短：limit
		//跳过：skip	filter(d -> d.getCalories() > 300).skip(2)跳过超过300卡路里的头两道菜，返回剩下的菜。
				
		//[map]就是对流中每一个元素应用一个函数
		//流支持map方法，它会接受一个函数作为参数。这个函数会被应用到每个元素上，并将其映射成一个新的元素
		//给定一个单词列表，你想要返回另一个列表，显示每个单词中有几个字母。怎么做呢？你需要对列表中的每个元素应用一个函数。这听起来正好该用map方法去做.
	
		List<String> words = Arrays.asList("Java 8", "Lambdas", "In", "Action");
		List<Integer> wordLengths = 
				words.stream()
					//.map(String::length)
					.map((a) -> a.length())
					.collect(Collectors.toList());
		//现在让我们回到提取菜名的例子。如果你要找出每道菜的名称有多长，怎么做？你可以像下面这样，再链接上一个map：
		List<Integer> dishNameLengths = menu.stream()
				.map(Dish::getName)
				.map(String::length)
				.collect(Collectors.toList());
		
		
		/**
		 * 给定单词列表["Hello","World"]，你想要返回列表["H","e","l", "o","W","r","d"],列出里面各不相同的字符呢?
		 * 第一个版本：words.stream().map(word -> word.split("")).distinct().collect(toList());
		 * 
		 * 这个方法的问题在于，传递给map方法的Lambda为每个单词返回了一个String[]。map 返回的流实际上是Stream<String[]> 类型的
		 * 幸好可以用flatMap来解决这个问题
		 */
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
		
		/**
		 * Optional里面几种可以迫使你显式地检查值是否存在或处理值不存在的情形的方法
		 * ()将在Optional包含值的时候返回true, 否则返回false。、
		 * ifPresent(Consumer<T> block)会在值存在的时候执行给定的代码块。Consumer函数式接口；它让你传递一个接收T类型参数，并返回void的Lambda、
		 * T get()会在值存在时返回值，否则抛出一个NoSuchElement异常。
		 * T orElse(T other)会在值存在时返回值，否则返回一个默认值。
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
		//(x, y) -> x < y ? x : y而不是Integer::min，后者易读
		
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
		//有没有交易员是在米兰工作的
		boolean anyMatch = transactions.stream()
			.anyMatch(d -> d.getTrader().getCity().equals("米兰"));
		
		//打印生活在剑桥的交易员的所有交易额
		transactions.stream()
			.filter(d -> d.getTrader().getCity().equals("剑桥"))
			.map(d -> d.getValue())
			.forEach(System.out::println);
		
		//所有交易中，最高的交易额是多少
		Optional<Integer> reduce = transactions.stream()
			.map(d -> d.getValue())
			.reduce((a, b) -> a > b ? a : b);
		System.out.println("reduce:"+reduce);
		
		//返回交易额最大的交易，而非交易额
		transactions.stream()
			.reduce((a1, a2) -> a1.getValue()>a2.getValue() ? a1 : a2);
		
		
		
		/**
		 * 原始类型流特化IntStream、DoubleStream、LongStream
		 * 将流转换为特化版本的常用方法是mapToInt、mapToDouble和mapToLong，和map方法的工作方式一样，只是它们返回的是一个特化流，而不是Stream<T>
		 * */
		IntStream mapToInt = menu.stream().mapToInt(Dish::getCalories);//注意此处返回的是IntSrream，而不是Stream<Integer>
		int sum2 = mapToInt.sum();//然后你就可以调用IntStream接口中定义的sum方发求和.查看源码发现，sum使用 return reduce(0, Integer::sum);
		//IntStream还支持其他的方便方法max、min、average等
		
		/**
		 * 此处注意：流只能遍历一次。遍历完之后，我们就说这个流已经被消费掉了(中间操作不会消耗流，终端操作会消耗流，以产生一个最终结果)
		 * 如果使用mapToInt流，因为上面使用了.sum()遍历一次了，所以报错 java.lang.IllegalStateException: stream has already been operated upon or closed
		 * 此处重新定义一个流	mapToInt2
		 */
		IntStream mapToInt2 = menu.stream().mapToInt(Dish::getCalories);//d -> d.getCalories()
		OptionalInt max2 = mapToInt2.max();//可能没有max，所以返回Optional的特化类OptionalInt
		//如果没有最大值的话，你就可以显式处理OptionalInt去定义一个默认值了
		int orElse = max2.orElse(1);
		
		/**
		 * 转换回对象流
		 * 如果使用mapToInt流，因为上面使用了.sum()遍历一次了，所以报错 java.lang.IllegalStateException: stream has already been operated upon or closed
		 */
		//Stream<Integer> boxed = mapToInt.boxed();//转换回非特化流
		
		/**
		 * 数值范围
		 * range		不包含结束值
		 * rangeClosed	包含结束值
		 * 
		 * 例：求1-100中所有偶数的个数
		 */
		IntStream filter = IntStream.range(1, 100).filter(n -> n%2 == 0);
		System.out.println(filter.count());//49,查看源码可以看到类似for(i=1,i<100,i++)
		IntStream filter2 = IntStream.rangeClosed(1, 100).filter(n -> n%2 == 0);
		System.out.println(filter2.count());//50,查看源码可以看到类似for(i=1,i<=100,i++)
		
		/**
		 * 构建流
		 * 
		 * 1.由值创建流
		 */
		Stream<Integer> of = Stream.of(1,2,3,4,5,4,3,2,4,3,6);
		of.distinct().forEach(System.out::println);//求不重复数并打印
		//创建了一个字符串流
		Stream<String> of2 = Stream.of("Hello World","Java");
		of2.map(s -> s.toUpperCase()).forEach(System.out::println);//转换成大写，那s -> s.toUpperCase()表达式可以使用方法引用表示：String::toUpperCase
		
		/**
		 * 由数组创建流
		 */
		int[] intArray = {1,2,3,4,5,6};
		String[] stringArray = {"l","y","w","a"};
		Apple[] appleArray = {new Apple("red", 10), new Apple("yellow", 100)};
		int sum3 = Arrays.stream(intArray).sum();System.out.println(sum3);//整数数组求和
		Arrays.stream(stringArray).sorted().forEach(System.out::print);//字符数字排序
		Optional<Integer> reduce2 = Arrays.stream(appleArray).map(Apple::getHeight).reduce(Integer::max);//对象数组
		System.out.println(reduce2);
		
		/**
		 * 由函数生成流：创建无限流
		 * 
		 * Stream API提供了两个静态方法来从函数生成流：Stream.iterate和Stream.generate
		 * 应该使用limit(n)来对这种流加以限制，以避免打印无穷多个值。
		 */
		//一般来说，在需要依次生成一系列值的时候应该使用iterate
		Stream.iterate(0, n -> n+2).limit(10).forEach(System.out::println);
		Stream.generate(Math::random).limit(5).forEach(System.out::println);
		
		
		/**
		 * Collectors:使用流收集数据
		 */
		
		/**
		 * 对交易员按国家分组
		 * key：Trader
		 * value：List<Transaction>
		 */
		Map<Trader, List<Transaction>> collect2 = 
					transactions.stream().collect(Collectors.groupingBy(Transaction::getTrader));
		System.out.println(collect2);
		
		/**
		 * 数一数菜单中有多少种菜
		 */
		Long collect3 = menu.stream().collect(Collectors.counting());
		System.out.println("数量统计Collectors.counting()="+collect3);
		System.out.println("更直接的写法 stream().count()="+menu.stream().count());
		
		
		/**
		 * 汇总:
		 * 求和summingInt,summingLong,summingDouble
		 * 平均averagingInt,averagingLong,averagingDouble
		 */
		Integer collect = menu.stream().collect(Collectors.summingInt(Dish::getCalories));
		System.out.println("求和"+collect);
		
		Double collect4 = menu.stream().collect(Collectors.averagingInt(Dish::getCalories));
		System.out.println("avg:"+collect4);
		
		/**
		 * summarizingInt,long,double
		 * 一次操作，返回总和、平均值、最大值和最小值
		 * 通过get方法，访问IntSummaryStatistics中的属性
		 */
		IntSummaryStatistics collect5 = menu.stream().collect(Collectors.summarizingInt(Dish::getCalories));
		System.out.println("汇总："+collect5+",数量："+collect5.getCount()+",总和："+collect5.getSum()+",平均："+collect5.getAverage()+",最大值："+collect5.getMax()+",最小值："+collect5.getMin());
		
		/**
		 * 连接字符串：joining返回String
		 */
		String collect6 = menu.stream().map(d -> d.getName()).collect(Collectors.joining());
		System.out.println(collect6);
		//joining工厂方法有一个重载版本可以接受元素之间的分界符
		String collect7 = menu.stream().map(Dish::getName).collect(Collectors.joining(","));
		System.out.println(collect7);
		
		/**
		 * 上述的规约与汇总方法，其实都是reducing工厂方法的特殊情况
		 * reduceing(a, b, c) 
		 * 		a：是归约操作的起始值，也是流中没有元素时的返回值
		 * 		b:计算什么东西，热量还是数量
		 * 		c:怎么求，求和还是，求平均数
		 */
		//计算菜单总热量
		Integer collect8 = menu.stream().collect(Collectors.reducing(
				0, Dish::getCalories, Integer::sum));//a:0,b:计算热量，c:求和
		//lambda写法
		Integer collect9 = menu.stream().collect(Collectors.reducing(
				0, Dish::getCalories, (a, b) -> a+b));
		System.out.println(collect8+","+collect9);
		
		// 更简洁的方法是把流映射到一个IntStream，然后调用sum方法，你也可以得到相同的结果
		//IntStream可以让我们避免自动拆箱操作
		int sum4 = menu.stream().mapToInt(Dish::getCalories).sum();
		System.out.println(sum4);
		
		/**
		 * 总结:根据情况选择最佳解决方案，但在通用的方案里面，始终选择最专门化的一个.无论是从可读性还是性能上看，这一般都是最好的决定
		 */
		
		
		/**
		 * 分组
		 */
		//把菜单中的菜按照类型进行分类
		Map<Type, List<Dish>> collect10 = menu.stream().collect(Collectors.groupingBy(Dish::getType));
		System.out.println(collect10);
		
		//你可能想把热量不到400卡路里的菜划分为“低热量”（diet），热量400到700卡路里的菜划为“普通”（normal），高于700卡路里的划为“高热量”（fat）
		Map<CaloricLevel, List<Dish>> collect11 = menu.stream().collect(Collectors.groupingBy(
				dish -> {
					if (dish.getCalories() <= 400) return CaloricLevel.DIET;
					else if (dish.getCalories() <= 700) return CaloricLevel.NORMAL;
					else return CaloricLevel.FAT;
				}));
		System.out.println(collect11);
		
		//多级分组
		Map<Type, Map<CaloricLevel, List<Dish>>> collect12 = menu.stream().collect(
				Collectors.groupingBy(Dish::getType, 
						Collectors.groupingBy(
								dish -> {
									if (dish.getCalories() <= 400) return CaloricLevel.DIET;
									else if (dish.getCalories() <= 700) return CaloricLevel.NORMAL;
									else return CaloricLevel.FAT;
								})
						)
				);
		System.out.println(collect12);
		//传递给第一个groupingBy的第二个收集器可以是任何类型，而不一定是另一个groupingBy
		//要数一数菜单中每类菜有多少个。可以传递counting收集器作为groupingBy收集器的第二个参数：
		Map<Type, Long> collect13 = menu.stream().collect(Collectors.groupingBy(Dish::getType, Collectors.counting()));
		System.out.println(collect13);
		
		/**
		 * 分区true是一组，false是一组
		 */
		//把菜单按照素食和非素食分开
		Map<Boolean, List<Dish>> collect14 = menu.stream().collect(Collectors.partitioningBy(Dish::isVegetarian));
		System.out.println(collect14.get(true));
		System.out.println(collect14.get(false));
		//使用filter
		List<Dish> collect15 = menu.stream().filter(dish -> dish.isVegetarian()).collect(Collectors.toList());//true
		List<Dish> collect16 = menu.stream().filter(dish -> !dish.isVegetarian()).collect(Collectors.toList());//false
		
		//可以把分区看作分组一种特殊情况
		//多级分区
		menu.stream().collect(Collectors.partitioningBy(Dish::isVegetarian, 
				Collectors.partitioningBy(d -> d.getCalories() > 500)));
		
		/**
		 * 并行流
		 * 并行流就是一个把内容分成多个数据块，并用不同的线程分别处理每个数据块的流。
		 * 
		 * 顺序流-> 并行流parallel()
		 * 并行流->顺序流sequential()
		 */
		
		/**
		 * 并行流用的线程是从哪儿来的？有多少个？怎么自定义这个过程呢？并行流内部使用了默认的ForkJoinPool(分支/合并框架),它默认的线程数量就是你的处理器数量， 
		 * 这个值是由Runtime.getRuntime().available-Processors()得到的
		 */
		
		
		
		
		
		
}
	
}
