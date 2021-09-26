package net.skhu.list.oddnumber;

import java.util.LinkedList;

public class Exam01 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		LinkedList<Integer> list = new LinkedList<Integer>();

		for(int i=0; i<100; i++) {
			list.add(i);
		}

		System.out.println(list.toString());
		System.out.println(list.size());

		// remove 사용하여 홀수만 남기기
		for(int i=0; i<list.size(); i++) {
			if(list.get(i) % 2 == 0)list.remove(i);
		}

		System.out.println(list.toString());
		System.out.println(list.size());

	}

}