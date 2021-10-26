package chap03.ex3b;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/*
몇 십개 이상의 대규모 병렬 작업을 구현해야 한다면, ExecutorService 클래스를 이용해야 한다.
그 스레드들이 많은 병렬 작업들을 순서대로 처리하도록 구현할 수 있다.
 */
public class Main {

	public static void main(String[] args) {
		/*
		onSumFinish 메소드의 System.out.printf 출력은 thread safe 하다.
		System.out.printf 출력 중간에 다른 스레드의 출력이 끼어들 수 없다.
		System.out 클래스의 모든 메소드는 메소드 시작 부분에서 lock 되고,
		메소드 끝 부분에서 unlock 되기 때문이다.
		 */
		OnSumFinishListener listener = new OnSumFinishListener() {
			@Override
			public void onSumFinish(SumTask task) {
				System.out.printf("%d 부터 %d 까지 합계는 %d\n",
					task.getFrom(), task.getTo(), task.getResult());
			}
		};


		/*
		그런데 위 구현을 다음과 같이 수정하면
		이제 onSumFinish 메소드의 출력은 thread safe 하지 않다.
		System.out.print와 println 사이에 다른 스레드가 끼어들 수 있다. 
		 */
		// OnSumFinishListener listener = new OnSumFinishListener() {
		// 	@Override
		// 	public void onSumFinish(SumTask task) {
		// 		System.out.print(task.getFrom() + " 부터 " + task.getTo() + " 까지 합계는 ");
		// 		System.out.println(task.getResult());
		// 	}
		// };


		/*
		다음과 같이 변경하면 thread safe 하다.
		어느 한 쓰레드가 이 메소드를 실행하는 동안, this 객체 (리스너 객체)가 lock 되므로,
		다른 쓰레드들은 이 메소드를 실행할 수 없기 때문이다.
		 */
		// @Override
		// public void onSumFinish(SumTask task) {
		// 	synchronized (this) {
		// 		System.out.print(task.getFrom() + " 부터 " + task.getTo() + " 까지 합계는 ");
		// 		System.out.println(task.getResult());
		// 	}
		// }

		ExecutorService service = Executors.newFixedThreadPool(8); // 실행 흐름을 8개만 만든다.
		int from = 1;
		for (int to = 10; to <= 1000000; to += 10) {
			SumTask sumTask = new SumTask(from, to);
			sumTask.setOnSumFinishListener(listener);
			service.submit(sumTask); // 병렬 작업을 해야할 객체를 ExecutorService 객체에 등록
		}
		service.shutdown(); // 미리 만들어둔 실행 흐름들을 종료
	}

}
