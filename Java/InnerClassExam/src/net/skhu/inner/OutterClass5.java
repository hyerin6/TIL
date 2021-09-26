package net.skhu.inner;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener; 
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class OutterClass5 extends JFrame{ // + OutterClass6, OutterClass8

	String message = "클릭";

	public OutterClass5() {
		setBounds(100, 100, 450, 300);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null); 

		JButton button = new JButton("버튼");
		button.setBounds(100, 100, 100, 30);
		getContentPane().add(button); 

		/*
		anonymous inner class 문법으로 구현 - 자식클래스 이름을 생략한다.

		(1) ActionListener 인터페이스를 구현(implements)한 자식 클래스를 만들었다.
		(2) 이 자식 클래스에서 actionPerformed 메소드를 재정의하였다.
		(3) 이 자식 클래스의 객체가 하나 생성되고,
		(4) 그 객체에 대한 참조가 listener 지역 변수에 대입된다.

		ActionListener listener = new ActionListener() {
			@Override 
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(OutterClass5.this, message); 
			}	
		};

		위 코드에서 객체를 생성했다. 메소드를 호출한 것은 아니다.
		 */

		// 위 코드와 동일
		button.addActionListener(new ActionListener() {
			@Override 
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(OutterClass5.this, message); 
			}
		}); 
	}
	// 위 코드와 동일 - lambda expression 활용
	// button.addActionListener(e -> JOptionPane.showMessageDialog(OutterClass8.this, message));

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		OutterClass5 window = new OutterClass5(); 
		window.setVisible(true);
	}
}