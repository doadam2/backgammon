import java.util.Scanner;
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class GameHandler extends JFrame {
	
	private ImageIcon playersImage[] = new ImageIcon[26];
	private Board gameBoard;
	
	public GameHandler(boolean vsComputer) {
		setSize(693, 520);
		setLocation(250, 75);
		setUndecorated(true);
		gameBoard = new Board();
		gameBoard.setLayout(null);
		MyActionListener al = new MyActionListener();
		
		JLabel l = new JLabel(new ImageIcon("Background.png"));
		l.setSize(693, 520);
		gameBoard.add(l);
		add(gameBoard);
		setVisible(true);
	}
	
	public class MyActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
		}
		}

}
