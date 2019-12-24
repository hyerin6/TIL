package designPattern.adapter;

public class ClientWithAdapter {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		AdapterServiceA a = new AdapterServiceA();
		AdapterServiceB b = new AdapterServiceB();


		// 클라이언트가 변환기를 통해 runService() 라는 동일한 메서드명으로 두 객체의 메서드를 호출한다.  
		// 어댑터 패턴은 합성, 즉 객체를 속성으로 만들어서 참조하는 디자인 패턴으로 
		// "호출당하는 쪽의 메서드를 호출하는 쪽의 코드에 대응하도록 중간에 변환기를 통해 호출하는 패턴"
		a.runService();
		b.runService();

	}

}
