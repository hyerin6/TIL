package chap03.ex3a;

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
		for (int i = from; i <= to; ++i)
			sum += i;
		result = sum;
		if (listener != null)
			listener.onSumFinish(this);
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
