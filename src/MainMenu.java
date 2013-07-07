import java.util.Scanner;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

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
		vsComputerButton = new RButton(260, 368);
		vsComputerButton.setSize(165, 33);
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
				File f = new File("gammon.save");
				if(f.exists()) {
			    int reply = JOptionPane.showConfirmDialog(null, "Would you like to load the game from a previously save file?", "You didn't finish your last game!", JOptionPane.YES_NO_OPTION);
				try {
					new GameHandler(reply == JOptionPane.YES_OPTION);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				} else
					try {
						new GameHandler(false);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				dispose();
			}
		}
	}
}
