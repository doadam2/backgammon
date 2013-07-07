import java.util.Scanner;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.swing.*;

public class GameHandler extends JFrame {
	
	private ImageIcon playersImage[] = new ImageIcon[26];
	private Board gameBoard;
	
	public GameHandler(boolean loadGame) throws IOException {
		setSize(693, 520);
		setLocation(250, 75);
		setUndecorated(true);
		File f = new File("gammon.save");
		if(f.exists() && loadGame)
		{
			String whiteData, blackData, data = readFile("gammon.save", Charset.defaultCharset());
			whiteData = data.substring(0, 28);
			blackData = data.substring(28);
			gameBoard = new Board(whiteData, blackData);
		}
		else
			gameBoard = new Board();
		gameBoard.setLayout(null);
		MyActionListener al = new MyActionListener();
		
		JLabel l = new JLabel(new ImageIcon("Background.png"));
		l.setSize(693, 520);
		gameBoard.add(l);
		add(gameBoard);
		setVisible(true);
	}
	
	private String readFile(String path, Charset encoding) 
			  throws IOException 
			{
			  byte[] encoded = Files.readAllBytes(Paths.get(path));
			  return encoding.decode(ByteBuffer.wrap(encoded)).toString();
			}
	
	public class MyActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
		}
		}

}
