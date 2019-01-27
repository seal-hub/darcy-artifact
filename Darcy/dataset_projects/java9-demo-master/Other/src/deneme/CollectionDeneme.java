package deneme;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class CollectionDeneme {

	public static void main(String[] args) {
		Map<Integer, Integer> m = Map.of(1, 1, 2, 2);
		System.out.println(m);
		
		Set<Integer> s = Set.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
		System.out.println(s);
		Set<Integer> s2 = s.stream().filter(i -> i % 2 == 0).collect(Collectors.toSet());
		System.out.println(s2);
	}
}
