package net.skhu.objects;

import java.util.Date; 
import java.util.Objects;

public class Data {

	int year;
	String address;
	Date startDate;

	@Override
	public int hashCode() { 
		// Objects 클래스의 hash 메소드가 해시 값을 계산해 준다.
		return Objects.hash(year, address, startDate);
	}

	@Override 
	public boolean equals(Object obj) {
		if (this == obj) return true; 
		if (obj instanceof Data == false) return false;

		Data d = (Data)obj;

		// return 문에서, 모든 멤버 변수를 비교해야 한다.
		return this.year == d.year && 
				Objects.equals(this.address, d.address) &&
				Objects.equals(this.startDate, d.startDate); 
	}

}
