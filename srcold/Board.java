import java.awt.*;
import java.util.Vector;
import java.util.Scanner;
import javax.swing.*;
import javax.swing.border.Border;

import java.awt.event.*;

public class Board extends JPanel {

	private DButton dice[];
	private Dice diceInfo, enemyDice;
	private JLabelStatus jls;
	private GameLine lines[];
	private int checkers1[]; // white
	private int checkers2[]; // black
	private int playerAtMove;
	private boolean playerClicked;
	private int currentSelection;
	private MyActionListener al;
	private Timer animationTimer;
	private String timerMode;
	private RButton exit;
	public Board() {
		exit = new RButton(0, 390);
		exit.setSize(65, 95);		
		exit.setActionCommand("exit");
		animationTimer = new Timer(2000, new MyActionListenerTimer());		
		setOpaque(false);
		setLayout(null);
		lines = new GameLine[28]; // 26 = black eaten, 27 = white eaten
		checkers1 = new int[28];
		checkers2 = new int[28];
		dice = new DButton[4];
		playerAtMove = 0;
		setupBoard();
	}

	// returns 0 if empty, 1 for white and 2 for black
	public int getColorOnLine(int line) {
		if (getPoint(1, line) > 0)
			return 1;
		if (getPoint(2, line) > 0)
			return 2;
		return 0;
	}

	public int getPoint(int player, int pointnumber) {
		if (player == 1) {
			return checkers1[pointnumber];
		} else {
			return checkers2[pointnumber];
		}
	}

	/**
	 * Gets the current 'thinking' player.
	 * @return 1 for white, 2 for black.
	 */
	public int getActivePlayer() {
		return playerAtMove;
	}

	/**
	 * Setting the current 'thinking' player (changing the turn)
	 * @param player
	 * The player to play right now.
	 */
	public void setActivePlayer(int player) {
		if(player==1)
		{
			System.out.println("player is now playing..");
			roll();
			setPlayerClicked(false);			
			PossibleMoves pm = new PossibleMoves(checkers1,checkers2,diceInfo);
			updateGameLines(pm.getPossibleSelections());
			if(pm.cantMove())
			{
				JOptionPane.showMessageDialog(null, "You're unable to move! The black player is now playing!");
				setActivePlayer(2);
			}
		}
		playerAtMove = player;
		if (player == 2) // computer
		{
			timerMode = "RollDice";
			System.out.printf("PC is playing..\n");
			animationTimer.start();
		}
		updateStatusBar();
	}

	/**
	 * Moves a player on the board, updating the GUI and the internal scores.
	 * @param from
	 * line to move from.
	 * @param to
	 * line to move to.
	 */
	public void movePlayer(int from, int to) {
		System.out.printf("moving from %d to %d\n", from, to);
		String moving = lines[from].getcolor();
		if (lines[from].hasSameColorOrEmpty(lines[to])) {
			lines[to].fixColor(moving);
			lines[from].setCount(lines[from].getCount() - 1);
			lines[to].setCount(lines[to].getCount() + 1);
			if (getColorOnLine(from) == 1) 
			{
				checkers1[from]--;
				checkers1[to]++;
			} else {
				checkers2[from]--;
				checkers2[to]++;
			}
		} else { // eating a player
			System.out.printf("eating in line %d\n", to);
			lines[from].setCount(lines[from].getCount() - 1);
			lines[to].setCount(1);
			if (getColorOnLine(from) == 1) {
				checkers1[from]--;
				checkers1[to]++;
				checkers2[to]--;
				checkers2[26]++;
				lines[26].setCount(lines[26].getCount()+1);
			} else {
				checkers2[from]--;
				checkers2[to]++;
				checkers1[to]--;
				checkers1[27]++;
				lines[27].setCount(lines[27].getCount()+1);
			}
			lines[to].changeColor();
		}
		updateStatusBar();
	}
	
	/**
	 * Rolls a dice for the player.
	 */
	public void roll() {
		setDice(new Dice());
	}

	/**
	 * Updates the status bar on the top left screen.
	 */
	public void updateStatusBar() {
		jls.updateInfo(getActivePlayer(), calcPip(1), calcPip(2));
	}

	/**
	 * Updates the GameLines, making them enabled\disabled.
	 * @param active
	 * an array of booleans to indicate which GameLine should be enabled or disabled.
	 */
	public void updateGameLines(boolean active[]) {
		for (int i = 0; i < 28; i++)
			lines[i].setActive(active[i]);
	}

	/**
	 * Randomly chooses a player to be the first.
	 */
	public void chooseFirstPlayer() {
		if (Math.random() > 0.5f)
			setActivePlayer(1); // white
		else
		{
			setActivePlayer(2); // black
			enemyDice = new Dice();
			setDice(enemyDice);
			timerMode="InitiateAI";
			animationTimer.start();
		}
	}

	/**
	 * Updates the dice GUI and the dice values.
	 */
	public void setDice(Dice d) {
		for (int i = 0; i < d.getCount(); i++) {
			dice[i].updateDice(d.getDice(i));
		}
		boolean set_double_visibility = (d.getCount() == 4 ? true : false);
		for (int i = 2; i < 4; i++)
			dice[i].setVisible(set_double_visibility);
		diceInfo = d;
		PossibleMoves pm = new PossibleMoves(
				getActivePlayer() == 1 ? checkers1
						: checkers2,
				getActivePlayer() == 1 ? checkers2
						: checkers1, diceInfo);
		updateGameLines(pm.getPossibleSelections());
	}

	/**
	 * Initializing the board: GUI, values etc.
	 */
	private void setupBoard() {
		currentSelection = 1;
		playerClicked = false;
		al = new MyActionListener();
		exit.addActionListener(al);
		add(exit);
		String setupOrder = "0000005030000500000000002000";
		//String setupOrder = "0333330000000000000000000000";
		for (int i = 0; i < 28; i++)
			checkers1[i] = Character.getNumericValue(setupOrder.charAt(i));
		for (int i = 25, j = 0; i >= 0; i--, j++)
			checkers2[i] = Character.getNumericValue(setupOrder.charAt(j));
		for (int i = 0; i < 28; i++)
			System.out.print(checkers2[i]);
		for (int i = 0; i < 28; i++) {
			String color = "";
			if (getColorOnLine(i) == 1)
				color = "whitep";
			else if (getColorOnLine(i) == 2)
				color = "blackp";
			else
				color = "empty";
			if(i==0||i==27)
				color = "whitep";
			else if(i==25||i==26)
				color = "blackp";
			if (color.equals("whitep"))
				lines[i] = new GameLine(i, color, checkers1[i]);
			else if (color.equals("blackp"))
				lines[i] = new GameLine(i, color, checkers2[i]);
			else
				lines[i] = new GameLine(i, "empty", 0);
			lines[i].addActionListener(al);

			if (i < 13)
				lines[i].setVerticalAlignment(JButton.BOTTOM);
			else
				lines[i].setVerticalAlignment(JButton.TOP);

			add(lines[i]);

		}
		for (int i = 0; i < 4; i++) {
			dice[i] = new DButton(i, 0);
			dice[i].addActionListener(al);
			add(dice[i]);
			if (i > 1)
				dice[i].setVisible(false);
		}
		jls = new JLabelStatus();
		add(jls);

		chooseFirstPlayer();
		updateStatusBar();
	}

	/**
	 * get the number of checkers already played off. shortcut for
	 * getPoint(playerno, 0);
	 * 
	 * @param playerno
	 *            Player to look at.
	 * @return the number of checkers in the off (0 - 15)
	 */
	public int getOff(int playerno) {
		return getPoint(playerno, 0);
	}

	/**
	 * get the number of checkers on the bar. shortcut for getPoint(playerno,
	 * 25);
	 * 
	 * @param playerno
	 *            Player to look at.
	 * @return the number of checkers on the bar (0 - 15)
	 */
	public int getBar(int playerno) {
		if (playerno == 1)
			return getPoint(playerno, 25);
		else
			return getPoint(playerno, 0);
	}

	public int[] getBoardAsArray(int playerno) {
		int[] ret = new int[26];
		for (int i = 0; i < ret.length; i++) {
			ret[i] = getPoint(playerno, i);
		}
		return ret;
	}

	/**
	 * get the point with the highest number that has at least one checker on
	 * it.
	 * 
	 * @param player
	 *            player to look up for (1 or 2)
	 * @return integer between 0 and 25 (incl)
	 */
	public int getMaxPoint(int player) {
		int maxpoint = -1;
		for (int i = 1; i <= 25; i++) {
			if (getPoint(player, i) > 0) {
				maxpoint = i;
			}
		}
		if(player==2)
			return 25-maxpoint;
		return maxpoint;
	}

	public boolean isSeparated() {
		return getMaxPoint(1) + getMaxPoint(2) < 25 && (checkers2[26]==0 && checkers1[27]==0);
	}

	/**
	 * Calculates the steps left before a user wins.
	 * @param player
	 * 1 for white, 2 for black
	 * @return
	 * Returns the number of steps left.
	 */
	public int calcPip(int player) {
		int pip = 0;
		for (int i = 1; i <= 25; i++) {
			if (player == 1)
				pip += i * getPoint(player, i);
			else
				pip += (25 - (i-1)) * getPoint(player, i-1);
		}
		return pip;
	}

	/**
	 * Indicates whether a player has clicked a GameLine.
	 * @return
	 * true if clicked, false if not.
	 */
	public boolean hasPlayerClicked() {
		return playerClicked;
	}

	/**
	 * Changes the flag whether a player clicked a flag or not.
	 * @param clicked
	 * the new flag value.
	 */
	public void setPlayerClicked(boolean clicked) {
		playerClicked = clicked;
	}

	/**
	 * Gets the current GameLine selected by the player.
	 * @return
	 * The GameLine's ID.
	 */
	public int getCurrentSelection() {
		return currentSelection;
	}

	/**
	 * Sets the current GameLine selected by the player.
	 * @param i
	 * The GameLine's ID.
	 */
	public void setCurrentSelection(int i) {
		currentSelection = i;
	}

	/**
	 * Checks if there's a winner.
	 * @return
	 * 1 if white win, 2 if black win, zero if nobody won.
	 */
	public int getWinner() {
		if (checkers1[0] == 15)
			return 1;
		if (checkers2[25] == 15)
			return 2;
		return 0;
	}
	
	public class MyActionListenerTimer implements ActionListener {
		public void actionPerformed(ActionEvent e)
		{
			System.out.println("Timer ticked: " + timerMode);
			if(timerMode=="RollDice")
			{
				enemyDice = new Dice();
				setDice(enemyDice);
				timerMode="InitiateAI";
				animationTimer.restart();
			}
			else if(timerMode=="InitiateAI")
			{
				AI aiComputer = new AI(checkers2, checkers1, enemyDice, isSeparated());
				Move m = aiComputer.getBestMove();
				for (int i = 0; i < m.getCount(); i++)
					movePlayer(m.getFrom(i), m.getTo(i));
				animationTimer.stop();
				setActivePlayer(1);
			}
		}
	}
	

	public class MyActionListener implements ActionListener {
		
		public void actionPerformed(ActionEvent e) {
			if(e.getActionCommand().equals("exit"))
			{
				System.out.printf("exiting!");
				System.exit(0);
			}
		else if (e.getActionCommand().equals("dice")) {
				if(diceInfo.finished())
				{
				DButton d = (DButton) e.getSource();
				roll();
				}
			} else {
				GameLine temp = (GameLine) e.getSource();
				if (temp.isActive()) {
					if (!hasPlayerClicked()) {
						for (int i = 0; i < 26; i++)
							lines[i].setSelected(false);
						currentSelection = temp.getLineID();
						temp.setSelected(true);
						setPlayerClicked(true);
						PossibleMoves pm = new PossibleMoves(checkers1,checkers2,diceInfo);
						updateGameLines(pm.getPossibleMoves(currentSelection,
								isSeparated()));
					} else {
						if (temp.getLineID() == currentSelection) {
							setPlayerClicked(false);
							temp.setSelected(false);
							PossibleMoves pm = new PossibleMoves(checkers1,checkers2,diceInfo);
							updateGameLines(pm.getPossibleSelections());
						} else {
							movePlayer(currentSelection, temp.getLineID());
							temp.setSelected(false);
							setPlayerClicked(false);
							PossibleMoves pm = new PossibleMoves(checkers1,checkers2,diceInfo);
							updateGameLines(pm.getPossibleSelections());
							
							if(currentSelection==27)
								diceInfo.removeNumber(Math.abs(currentSelection-2-temp.getLineID()));
							else if(temp.getLineID()==0)
							{
								int j = 0;
								boolean removed = false;
								while(j < diceInfo.getCount() && !removed)
								{
									if(diceInfo.getDice(j) > 0)
									{
										if(currentSelection == diceInfo.getDice(j))
										{
											diceInfo.usedDice(j);
											removed = true;
										}
									}
									j++;
								}
								if(!removed)
								{
									j = 0;
									while(j < diceInfo.getCount() && !removed)
									{
										if(diceInfo.getDice(j) > 0)
										{
											if(currentSelection < diceInfo.getDice(j))
											{
												diceInfo.usedDice(j);
												removed = true;
											}
										}
										j++;
									}
								}
							}
							else
							diceInfo.removeNumber(Math.abs(temp.getLineID()
									- currentSelection));
							if (diceInfo.finished() || pm.cantMove())
								setActivePlayer(2);
						}
					}
				}
				if (getWinner() != 0)
				{
					JOptionPane.showMessageDialog(null, "Werker Werker!");
					int win_value = getWinner();
					if(win_value == 1) // black wins
					{
						if(checkers2[25]==0)
						JOptionPane.showMessageDialog(null, "The white player wins a march!");
						else
							JOptionPane.showMessageDialog(null, "The white player wins!");
					}
					if(win_value == 2) // black wins
					{
						if(checkers1[0]==0)
						JOptionPane.showMessageDialog(null, "The black player wins a march!");
						else
							JOptionPane.showMessageDialog(null, "The black player wins!");
					}
				}
			}
		}
	}

}
