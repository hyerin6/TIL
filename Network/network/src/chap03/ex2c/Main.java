package chap03.ex2c;

class SumTask implements Runnable {
	int from, to;
	long result;

	public SumTask(int from, int to) {
		this.from = from;
		this.to = to;
	}

	@Override
	public void run() {
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

	/*
	스레드가 sleep 상태인 동안 CPU core가 그 스레드를 실행하지 않는다.
	이런식으로 기다리는 것은 별로 좋은 방법은 아니다.

	계산 작업이 끝나는 정확한 시점을 알 수 없으니 100 밀리초씩 sleep 하고 있다.
	최악의 경우 계산이 끝난 후 99 밀리초 후에 결과를 출력하게 될 수도 있다.
	 */

	public static void main(String[] args) throws InterruptedException {
		int from = 1, to = 1000;
		SumTask sumTask = new SumTask(from, to);
		Thread thread = new Thread(sumTask);
		thread.start();
		// 계산 결과가 result에 대입될 때까지
		// 메인 쓰레드가 정해진 시간 동안 대기(sleep)
		while (sumTask.getResult() == 0) {
			Thread.sleep(100);
		}
		System.out.print(from + " 부터 " + to + " 까지 합계는 ");
		System.out.print(sumTask.getResult());
	}

}
