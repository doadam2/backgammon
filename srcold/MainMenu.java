import java.util.Scanner;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class MainMenu extends JFrame {
	private RButton exit;
	private RButton instructionsButton;
	private RButton vsComputerButton;
	private RButton options;

	public MainMenu() {
		setSize(693, 520);
		setLocation(250, 75);
		setUndecorated(true);
		JPanel p = new JPanel();
		p.setLayout(null);
		MyActionListener al = new MyActionListener();

		instructionsButton = new RButton(260, 240);
		instructionsButton.setSize(165, 33);
		instructionsButton.addActionListener(al);
		p.add(instructionsButton);

		//90, 370
		vsComputerButton = new RButton(90, 370);
		instructionsButton.setSize(165, 33);
		vsComputerButton.addActionListener(al);
		p.add(vsComputerButton);
		
		exit = new RButton(0, 390);
		exit.setSize(65, 95);		
		exit.addActionListener(al);
		p.add(exit);

		JLabel l = new JLabel(new ImageIcon("MainMenu.png"));
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
			
			else if(temp == instructionsButton)
			{
				new InstructionsPage();
				dispose();
			}
			
			else if(temp == vsComputerButton)
			{
				new GameHandler(true);
				dispose();
			}
		}
	}
}
