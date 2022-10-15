
public class MyDQueTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		MyDQue dq = new MyDQue();

		dq.insertFront(1);

		dq.insertRear(2);
		dq.insertRear(3);
		dq.insertRear(4);
		dq.insertRear(5);

		dq.print();

		dq.insertFront(0);

		while(!dq.isEmpty()) // data가 없을때까지 삭제하고 data값 return 
			System.out.print(dq.deleteFront());
		System.out.println();

		dq.print();

	}
}
