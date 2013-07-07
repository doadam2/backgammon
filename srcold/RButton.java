import java.awt.*;

import javax.swing.*;

public class RButton extends JButton {
	public RButton(int lx, int ly) {
		setSize(212, 47);
		setBackground(Color.black);
		setLocation(lx, ly);
		setOpaque(false);
		setContentAreaFilled(false);
		setBorderPainted(false);
		setBackground(Color.gray);
		setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
	}

}
