package chap03.ex3a;

public class Main {

	public static void main(String[] args) {
		OnSumFinishListener listener = new OnSumFinishListener() {
			@Override
			public void onSumFinish(SumTask task) {
				System.out.printf("%d 부터 %d 까지 합계는 %d\n",
					task.getFrom(), task.getTo(), task.getResult());
			}
		};

		int from = 1;
		for (int to = 10; to <= 1000000; to += 10) {
			SumTask sumTask = new SumTask(from, to);
			sumTask.setOnSumFinishListener(listener);

			/*
			너무 많은 스레드가 생성된다.
			지금은 자원을 사용하지 않기 때문에 자원 부족 에러가 발생하지는 않지만
			실무에서 이렇게 많은 스레드를 만들면, 자원 부족 에러가 발생한다.
			프로세스가 종료될 것이다.
			*/
			Thread thread = new Thread(sumTask);
			thread.start();
		}
	}

}
