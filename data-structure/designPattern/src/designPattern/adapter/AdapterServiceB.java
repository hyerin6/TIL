package designPattern.adapter;

public class AdapterServiceB {
	
	ServiceB b = new ServiceB();
	
	void runService() {
		b.runServiceB();
	}

}
