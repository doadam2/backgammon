import java.awt.*;
import java.awt.image.ImageObserver;

import javax.swing.*;

public class GameLine extends JButton {
	private String color; // black,white,empty
	private int id;
	private boolean isWorking;
	private int countPlayers; // number of players on line

	// top: 490
	// width: 80
	/**
	 * Fixes a location for each GameLine manually.
	 */
	private void setProperLocation()
	{
		setSize(40, 217);
		if(id==0)
			this.setLocation(632, 290);
		else if(id<7)
		{
		setLocation(626-(id*43),290);
		}
		else if(id<13)
		{
			setLocation(585-(id*43),290);
		}
		else if(id<19)
		{
			setLocation(69+((id-13)*43),20);
		}
		else if(id<25)
		{
			setLocation(368+((id-19)*43),20);
		}
		else if(id==25)
		{
			setLocation(632, 20);
		}
		else if(id==26)
		{
			setLocation(325,160);
			setSize(getSize().width, getSize().height/2);
		}
		else if(id==27)
		{
			setLocation(325,160+getSize().height/2);
			setSize(getSize().width, getSize().height/2);
		}
	}
	/**
	 * Creates a new GameLine.
	 * @param line_id
	 * the ID to associate to the line.
	 * @param col
	 * the color, "whitep" for white, "blackp" for black.
	 * @param count_Players
	 * the number of players on the line.
	 */
	public GameLine(int line_id, String col, int count_Players) {
		id = line_id;
		isWorking = true;
		countPlayers = count_Players;
		setOpaque(false);
		setContentAreaFilled(false);
		setBorderPainted(true);
		setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		color = col;
		if (col.equals("blackp")) {
			setIcon(new ImageIcon("blackp" + count_Players + (id > 12 ? "t" : "b") + ".png"));
		} else if (col.equals("whitep")) {
			setIcon(new ImageIcon("whitep" + count_Players + (id > 12 ? "t" : "b") + ".png"));
		} else if (col.equals("empty")) {
			setIcon(null);
		}
		
		setProperLocation();
	}

	/**
	 * Changes the line's color and change it's icon.
	 * @param col
	 * the new color.
	 */
	public void setcolor(String col) {
		color = col;
		if (!(col.equals("empty")))
			setIcon(new ImageIcon(col + ".png"));
		else {
			setIcon(null);
		}
	}
	
	/**
	 * Redraws the line.
	 */
	private void redraw()
	{
		setIcon(new ImageIcon(color + countPlayers + (id > 12 ? "t" : "b") + ".png"));
	}
	
	/**
	 * Changes the color.
	 * @param fixed
	 * the new color.
	 */
	public void fixColor(String fixed)
	{
		color = fixed;
	}
	
	/**
	 * Switches to the oppose color.
	 * the line will redraw itself.
	 */
	public void changeColor()
	{
		if(color == "blackp") color = "whitep";
		else if(color == "whitep") color = "blackp";
		redraw();
	}
	
	/**
	 * Checks whether it has the same color with another GameLine or if this line has no players.
	 * @param gl
	 * a pointer to the other GameLine.
	 * @return
	 * true if has the same color\empty, false if not.
	 */
	public boolean hasSameColorOrEmpty(GameLine gl) {
		if(gl.countPlayers == 0 || countPlayers==0)
			return true;
		else if(gl.getcolor()==getcolor())
			return true;
		return false;
	}
	
	/**
	 * Updates the line's count.
	 * @param count
	 * the new amount of players.
	 */
	public void setCount(int count)
	{
		countPlayers = count;
		redraw();
	}
	
	/**
	 * Checks how many players are on this line.
	 * @return
	 * the number of players on this line.
	 */
	public int getCount()
	{
		return countPlayers;
	}
	
	/**
	 * gets the ID of the line.
	 * @return
	 * the ID.
	 */
	public int getLineID()
	{
		return id;
	}
	
	/**
	 * Gets whether this line is enabled or disabled.
	 * @return
	 * true if enabled, false if not.
	 */
	public boolean isActive()
	{
		return isWorking;
	}
	
	/**
	 * Enables\Disables the line.
	 * @param active
	 * true to enable, false to disable.
	 */
	public void setActive(boolean active)
	{
		if(active)
		{
			setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		}
		else
		{
			setCursor(Cursor.getDefaultCursor());
		}
		isWorking = active;
	}
	
	/**
	 * Marks this line as selected\not selected and redraws appropariately.
	 */
	public void setSelected(boolean selected)
	{
		if(selected)
			setIcon(new ImageIcon(color + countPlayers + (id > 12 ? "t" : "b") + "k.png"));
		else
			setIcon(new ImageIcon(color + countPlayers + (id > 12 ? "t" : "b") + ".png"));
	}

	/**
	 * Gets the player's color ruling this line.
	 * @return
	 * the ruling color.
	 */
	public String getcolor() {
		return color;
	}
}