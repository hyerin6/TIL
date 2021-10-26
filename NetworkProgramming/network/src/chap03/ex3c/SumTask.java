package chap03.ex3c;

import java.util.concurrent.Callable;

/*
 리스너 구조를 이용하지 않고, Callable과 Future 인터페이스를 이용해서 작업 결과를 전달할 수 있다.

 Callable과 Future 인터페이스를 이용해서 작업 결과를 전달할 경우에
 작업 클래스는 Callable<리턴타입> 인터페이스를 implements 해야 한다.

 여기서 리턴 타입은 참조형이어야 하고, 기본 자료형은 안된다.
 (참조형: 클래스, 배열 등)

 작업 메소드 이름은 call이다.
 이 메소드에서 작업 결과를 리턴해야 한다.
 */
public class SumTask implements Callable<Long> {
	int from, to;
	long result;

	public SumTask(int from, int to) {
		this.from = from;
		this.to = to;
	}

	@Override
	public Long call() throws Exception {
		long sum = 0;
		for (int i = from; i <= to; ++i)
			sum += i;
		result = sum;
		return result;
	}

}
