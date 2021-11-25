package chap03.ex2d;

public class Main {

	public static void printResult(SumTask sumTask) {
		System.out.print(sumTask.getFrom() + " 부터 " + sumTask.getTo() + " 까지 합계는 ");
		System.out.print(sumTask.getResult());
	}
	
	public static void main(String[] args) {
		int from = 1, to = 1000;
		SumTask sumTask = new SumTask(from, to);
		Thread thread = new Thread(sumTask);
		thread.start();
	}

}


