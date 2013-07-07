import java.util.Scanner;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class InstructionsPage extends JFrame {
	private RButton exit;

	public InstructionsPage() {
		setSize(693, 520);
		setLocation(250, 75);
		setUndecorated(true);
		JPanel p = new JPanel();
		p.setLayout(null);
		MyActionListener al = new MyActionListener();

		exit = new RButton(0, 390);
		exit.setSize(65, 95);		
		exit.addActionListener(al);
		p.add(exit);

		JLabel l = new JLabel(new ImageIcon("InstructionsPage.png"));
		l.setSize(693, 520);
		p.add(l);
		add(p);
		setVisible(true);

	}

	public class MyActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			RButton temp = (RButton) e.getSource();
			if (temp == exit) {
				System.exit(0);
			}
		}
	}
}
