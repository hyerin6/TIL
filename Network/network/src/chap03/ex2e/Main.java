package chap03.ex2e;

public class Main {

	public static void main(String[] args) {
		int from = 1, to = 1000;

		// annonymous inner class 객체는 listener 지역 변수에 대입된다.
		OnSumFinishListener listener = new OnSumFinishListener() {
			@Override
			public void onSumFinish(SumTask task) {
				System.out.print(task.getFrom() + " 부터 " + task.getTo() + " 까지 합계는 ");
				System.out.print(task.getResult());
			}
		};
		SumTask sumTask = new SumTask(from, to);
		sumTask.setOnSumFinishListener(listener);
		Thread thread = new Thread(sumTask);
		thread.start();
	}

}

