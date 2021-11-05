package chap03.ex2a;

class SumTask implements Runnable {
	int from, to;
	long result;

	public SumTask(int from, int to) {
		this.from = from;
		this.to = to;
	}

	@Override
	public void run() {
		System.out.println("123");
		long sum = 0;
		for (int i = from; i <= to; ++i) {
			sum += i;
		}

		result = sum;
	}

	public long getResult() {
		return result;
	}
}

public class Main {
	public static void main(String[] args) {
		int from = 1, to = 1000;
		SumTask sumTask = new SumTask(from, to);
		Thread thread = new Thread(sumTask);
		thread.start();
		System.out.print(from + " 부터 " + to + " 까지 합계는 ");
		System.out.print(sumTask.getResult());
	}
}
