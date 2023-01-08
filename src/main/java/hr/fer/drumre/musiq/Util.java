package hr.fer.drumre.musiq;

import java.util.List;
import java.util.PriorityQueue;

public class Util {

	public static void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
		}
	}
	
	// returns count largest items from lst
	public static <T extends Comparable<T>> List<T> heapselect(List<T> lst, int count) {
		PriorityQueue<T> q = new PriorityQueue<>();
		for (T item : lst) {
			if (q.size() < count) {
				q.add(item);
			} else if (item.compareTo(q.peek()) > 0) {
				q.add(item);
				q.poll();
			}
		}
		
		return q.stream().toList();
	}
	
}
