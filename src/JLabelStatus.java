import java.awt.*;
import javax.swing.*;

public class JLabelStatus extends JLabel {
	
	public JLabelStatus()
	{
		setSize(500, 18);
		setLocation(17, 0);
		setText("Hello!");
		setVisible(true);
	}
	
	public void updateInfo(int turn, int white_left, int black_left)
	{
		String updated_info = "Currently Playing: ";
		if(turn == 2)
			updated_info += " Black";
		else
			updated_info += " White";
		updated_info += " White left: " + white_left;
		updated_info += " Black left: " + black_left;
		setText(updated_info);
	}

}
