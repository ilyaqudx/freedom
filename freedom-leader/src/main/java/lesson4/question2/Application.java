package lesson4.question2;

import java.util.stream.Stream;

public class Application {

	public static void main(String[] args) {
		
		Stream.of("d2","a1","b1","b3","c")
		.filter(s -> {
			System.out.println("filter " + s);
			return true;
		}).forEach(System.out::println);
		
	}
}
