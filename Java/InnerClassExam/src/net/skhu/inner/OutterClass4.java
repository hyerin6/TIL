package net.skhu.inner;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener; 
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

// innerClas 사용하지 않음
public class OutterClass4 extends JFrame { 

	String message = "클릭";

	public OutterClass4() {
		setBounds(100, 100, 450, 300);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null); 

		JButton button = new JButton("버튼");
		button.setBounds(100, 100, 100, 30);
		getContentPane().add(button); 

		ActionListener listener = new MyActionListener();
		button.addActionListener(listener); 
	}

	public static void main(String[] args) {
		OutterClass3 window = new OutterClass3();
		window.setVisible(true); 
	}

	// MyActionListener - InnerClass 
	// outterClass 의 this가 innerClass 객체 생성 시 자동 생성되기 때문에 코드가 간결해졌다.
	class MyActionListener implements ActionListener {
		@Override 
		public void actionPerformed(ActionEvent e) {
			JOptionPane.showMessageDialog(OutterClass4.this, message);
		}
	}
}
