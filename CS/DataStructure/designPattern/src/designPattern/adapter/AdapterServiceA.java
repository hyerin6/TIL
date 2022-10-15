package designPattern.adapter;

public class AdapterServiceA {
	
	ServiceA a = new ServiceA();
	
	void runService() {
		a.runServiceA();
	}

}
