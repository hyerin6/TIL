package chap03.ex3c;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Main {

	public static void main(String[] args) throws InterruptedException, ExecutionException {
		ExecutorService service = Executors.newFixedThreadPool(4);

		/*
		 이 Future 객체의 get 메소드를 호출하면, 작업 결과가 리턴된다.
		 그런데 병렬로 처리해야할 작업을 ExecutorService에 다 등록도 하기 전에
		 Future 객체의 get 메소드를 호출하면 안된다.

		 메인 스레드에서 Future의 get 메소드를 호출하면
		 그 Future를 리턴한 작업이 ExecutureService 내부의 실행 흐름들에 의해서 실행이 완료될 때까지,
		 메인 스레드가 대기(block) 상태에 빠지기 때문이다.

		 그래서 병렬로 처리해야 할 작업을 ExecutorService에 다 등록한 후
		 Future 객체의 get 메소드를 호출해야 하므로 Future 객체를 List에 보관한다.
		 */
		List<Future<Long>> futures = new ArrayList<>();

		int from = 1;
		for (int to = 10; to <= 1000000; to += 10) {
			SumTask sumTask = new SumTask(from, to);
			Future<Long> future = service.submit(sumTask);
			futures.add(future);
		}
		service.shutdown();

		// 작업 등록을 마치고 Future의 get 메소드를 호출하여 작업 결과를 리턴 받아 출력한다.
		for (Future<Long> future : futures)
			System.out.println(future.get());
	}

}
