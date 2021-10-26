package chap03.ex2e;

public class SumTask implements Runnable {
	int from, to;
	long result;
	OnSumFinishListener listener;

	public SumTask(int from, int to) {
		this.from = from;
		this.to = to;
	}

	public void setOnSumFinishListener(OnSumFinishListener listener) {
		this.listener = listener;
	}

	@Override
	public void run() {
		long sum = 0;
		for (int i = from; i <= to; ++i) {
			sum += i;
		}
		result = sum;

		// 작업을 마치면 annonymous inner clalss 객체의 onSumFinish 메소드가 호출된다.
		if (listener != null) {
			listener.onSumFinish(this);
		}
	}

	public int getFrom() {
		return from;
	}

	public int getTo() {
		return to;
	}

	public long getResult() {
		return result;
	}
}
