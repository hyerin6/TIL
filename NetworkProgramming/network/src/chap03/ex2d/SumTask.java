package chap03.ex2d;

public class SumTask implements Runnable {
	int from, to;
	long result;

	public SumTask(int from, int to) {
		this.from = from;
		this.to = to;
	}

	/*
	계산 작업이 끝나는 시점은 run() 메소드의 끝이다.
 	그래서 메소드 끝에 Main 클래스의 출력 메소드를 호출하는 코드를 추가했다.
 	=> call back
 	계산 작업이 끝나면 즉시 결과가 출력된다.

 	그러나 아직 유지보수성이 충분히 개선된 것은 아니다.
 	다른 곳에서 이 클래스를 재사용할 때 수정 없이 재사용할 수 없다.
 	Main 클래스 이름을 수정해야 한다.

 	부모 클래스의 이름을 언급하지 않고, 부모 클래스의 메소드를 호출하는 방법이 있는가?
 	=> Interface
 	*/
	@Override
	public void run() {
		long sum = 0;
		for (int i = from; i <= to; ++i) {
			sum += i;
		}
		result = sum;
		Main.printResult(this);
	}

	public long getResult() {
		return result;
	}

	public int getFrom() {
		return from;
	}

	public int getTo() {
		return to;
	}
}

