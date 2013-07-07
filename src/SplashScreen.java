import java.awt.event.*;
import java.awt.*;
import javax.swing.*;

public class SplashScreen extends JFrame {
	private Timer timer;

	public SplashScreen() {
		setSize(450, 465);
		setLocation(250, 75);
		setUndecorated(true);
		JPanel p = new JPanel();
		p.setLayout(null);
		timer = new Timer(2500, new MyTimer());
		JLabel l = new JLabel(new ImageIcon("SplashScreen.png"));
		l.setSize(450, 465);
		l.setLocation(0, 0);
		p.add(l);

		add(p);
		setVisible(true);
		timer.start();
	}

	public class MyTimer implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			timer.stop();
			new MainMenu();
			dispose();
		}
	}
}
