package net.skhu.inner;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener; 
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

// innerClas 사용하지 않음
public class OutterClass3 extends JFrame { 

	String message = "클릭";

	public OutterClass3() {
		setBounds(100, 100, 450, 300);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null); 

		JButton button = new JButton("버튼");
		button.setBounds(100, 100, 100, 30);
		getContentPane().add(button); 

		ActionListener listener = new MyActionListener(this);
		button.addActionListener(listener); 
	}

	public static void main(String[] args) {
		OutterClass3 window = new OutterClass3();
		window.setVisible(true); 
	}
}

class MyActionListener implements ActionListener {
	OutterClass3 window;

	public MyActionListener(OutterClass3 window) {
		this.window = window; 
	}

	@Override
	public void actionPerformed(ActionEvent e) { 
		JOptionPane.showMessageDialog(window, window.message); 
	}
}
