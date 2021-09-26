package net.skhu.scanner.up;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Collections; 
import java.util.List;
import java.util.Map; 
import java.util.Scanner; 
import java.util.TreeMap;
import java.util.stream.Collectors;



class DataItem {
	String s; 
	int count; 

	public DataItem(String s, int count) {
		this.s = s; 
		this.count = count;
	} 
}
public class Example_streamAPI{
	public static void main(String[] args)throws IOException {
		Map<String,Integer> map = new TreeMap<>();
		String filePath = "/Users/hyerin/Desktop/test.java"; 
		Scanner scanner = new Scanner(Paths.get(filePath)); 
		scanner.useDelimiter("[^a-zA-Z]+"); 
		while (scanner.hasNext()) {
			String s = scanner.next();
			Integer count = map.get(s);
			if (count == null) count = 0; 
			count = count + 1;
			map.put(s, count); 
		} scanner.close();

		List<DataItem> list = map.keySet().stream()
				.map(s -> new DataItem(s, map.get(s)))
				.collect(Collectors.toList());

		// ORDER BY count DESC, s
		Collections.sort(list, (o1, o2) -> {
			if (o1 == null && o2 == null) return 0;
			else if (o1 == null) return -1;
			else if (o2 == null) return 1;
			else {
				int r = o2.count - o1.count; 
				if (r != 0) return r;
				return o1.s.compareTo(o2.s);
			}
		});

		list.stream().forEach(d -> System.out.printf("%s %d\n", d.s, d.count));
	}

}