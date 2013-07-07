import java.awt.*;

import javax.swing.*;

public class DButton extends JButton {
	
	
	private int m_Value;
	
	public DButton(int id, int value) {
		m_Value = value;
		setSize(40, 40);
		setBorderPainted(true);
		setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		setLocation(383+(65*id), 246);
		setIcon(new ImageIcon("white" + value + ".gif"));
		setVisible(true);
		setActionCommand("dice");
	}
	
	/**
	 * Updates the dice graphics.
	 * @param value
	 * The new value of the dice.
	 */
	public void updateDice(int value)
	{
		m_Value = value;
		setIcon(new ImageIcon("white" + value + ".gif"));
		System.out.printf("setting icon to white%d.gif\n", value);
		updateUI();
	}
	
	/**
	 * Returns the value of the dice.
	 * @return
	 * integer as the value.
	 */
	public int getValue()
	{
		return m_Value;
	}

}
