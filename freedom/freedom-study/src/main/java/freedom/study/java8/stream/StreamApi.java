package freedom.study.java8.stream;

public class StreamApi {

	
	
	public static void main(String[] args) 
	{
		/*Random r = new Random();
		List<Person> ps = new ArrayList<Person>();
		for (int i = 0; i < 7; i++)
		{
			Person p = new Person(i,"张三" + i,r.nextInt(60),r.nextInt(2));
			ps.add(p);
		}
		
		Stream<Long> s = map(filter(ps, p -> p.getAge() > 18), Person::getId);
		
		List<Long> list = toList(s);*/
		
		/*Person max = ps.stream().max((p1,p2) -> p1.getAge() > p2.getAge() ? 1 : -1).get();
		System.out.println(max);
		ps = ps.stream().sorted((p1,p2) -> p1.getAge() > p2.getAge() ? -1 : 1).limit(1).collect(Collectors.toList());
		
		List<Integer> ids = new ArrayList<Integer>(){
			{
				add(3);
				add(5);
				add(2);
			}
		};
		
		List<String> pps = ps.stream().filter(p -> ids.stream().anyMatch(n -> n.intValue() == p.getId())).map(Person::getName).collect(Collectors.toList());
		
		for (String person : pps) {
			System.out.println(person);
		}*/
	}
	
	
	
	
	
	/*public static final <T> Stream<T> filter(List<T> list,Predicate<T> predicate)
	{
		return list.stream().filter(predicate);
	}
	
	public static final <T,R> Stream<R> map(Stream<T> s,Function<T,R> function)
	{
		return s.map(function);
	}
	
	public static final <T> List<T> toList(Stream<T> s)
	{
		return s.collect(Collectors.toList());
	}
	
	
	
	*//**
	 * 指定单个值过滤
	 * *//*
	public static final List<Person> filterEquals(List<Person> list,int age)
	{
		return list.stream().filter(p -> p.getAge() == age && p.getName().contains("张三")).collect(Collectors.toList());
	}
	
	*//**
	 * 指定集合过滤(在集合内容的元素有效)
	 * *//*
	public static final List<Person> filterInCollection(List<Integer> ids,List<Person> list)
	{
		return list.stream().filter(p -> ids.stream().anyMatch(n -> p.getId() == n.intValue())).collect(Collectors.toList());
	}
	
	*//**
	 * 排序
	 * *//*
	public static final List<Person> sorted(List<Person> list)
	{
		return list.stream().sorted((p1,p2) -> p1.getAge() > p2.getAge() ? 1 : -1).collect(Collectors.toList());
	}
	*//**
	 * 取最小值
	 * *//*
	public static final Person min(List<Person> list)
	{
		return list.stream().min((p1,p2) -> p1.getAge() > p2.getAge() ? 1 : -1).get();
	}
	*//**
	 * 最大值
	 * *//*
	public static final Person max(List<Person> list)
	{
		return list.stream().max((p1,p2) -> p1.getAge() < p2.getAge() ? 1 : -1).get();
	}
	
	public static final long sum(List<Person> list)
	{
		return list.stream().mapToLong(Person::getId).sum();
	}
	
	*//**
	 * 取前N个元素
	 * *//*
	public static final List<Person> limit(List<Person> list, int n)
	{
		return list.stream().limit(n).collect(Collectors.toList());
	}
	*//**
	 * 跳过前N个元素
	 * *//*
	public static final List<Person> skip(List<Person> list,int n)
	{
		return list.stream().skip(n).collect(Collectors.toList());
	}
	
	public static final List<Long> map(List<Person> list)
	{
		//list.stream().map(Person::getName);
		return list.stream().map(p -> p.getId()).collect(Collectors.toList());
	}*/
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
