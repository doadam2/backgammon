public class PossibleMoves {

	private int current_player[], victim[];
	private Dice diceInfo;

	public PossibleMoves(int player[], int enemy[], Dice dice) {
		current_player = player;
		victim = enemy;
		diceInfo = dice;
	}

	public boolean[] getPossibleSelections() {
		boolean result[] = new boolean[28];
		for (int i = 0; i < 28; i++)
			result[i] = false;
		if (current_player[27] > 0) {
			result[27] = true;
			return result;
		}
		for (int i = 1; i < 28; i++)
			if (getColorOnLine(i) == 1)
				result[i] = true;
		return result;
	}

	private boolean isDiceAppropriate(int selection, int line) {
		if (line == 26)
			return false;
		for (int i = 0; i < diceInfo.getCount(); i++) {
			if (selection == 27) {
				if (selection - line - 2 == diceInfo.getDice(i))
					return true;
			} else if (selection - line == diceInfo.getDice(i))
				return true;
		}
		return false;
	}

	public boolean cantMove() {

		boolean all_dices = true;
		int j = 0;
		if (current_player[27] > 0) {
			while (j < diceInfo.getCount() && all_dices) {
				if (diceInfo.getDice(j) > 0) {
					if (victim[25 - diceInfo.getDice(j)] >= 2)
						all_dices = false;
					else
						all_dices = true;
				}
				j++;
				System.out.printf("well.. all_dices is %s[%d]\n", all_dices, j);
			}
		}

		if (!all_dices)
			return true;

		for (int i = 27; i > 0; i--)
			for (j = 0; j < diceInfo.getCount(); j++)
				if (diceInfo.getDice(j) > 0)
					if (i - diceInfo.getDice(j) > 0)
						if (current_player[i] > 0
								&& victim[i - diceInfo.getDice(j)] < 2)
							return false;

		for (int i = 6; i > 0; i--)
			if (canTakeOut(i))
				return false;
		return true;
	}

	private boolean canTakeOut(int selection) {
		for (int i = 7; i < 28; i++)
			if (current_player[i] > 0)
				return false;

		int helper = 0;
		for (int i = 0; i < diceInfo.getCount(); i++)
			if (diceInfo.getDice(i) > 0) {
				if (diceInfo.getDice(i) == selection)
					return true;
			}

		for (int i = 0; i < diceInfo.getCount(); i++)
			if (selection > diceInfo.getDice(i))
				helper++;

		if (helper == diceInfo.getCount())
			return false;

		for (int i = 0; i < diceInfo.getCount(); i++)
			if (diceInfo.getDice(i) - selection > 0)
				for (int j = selection + 1; selection <= 6; selection++)
					if (current_player[j] > 0)
						return false;

		return true;
	}

	public boolean[] getPossibleMoves(int selection, boolean can_off) {
		boolean result[] = new boolean[28];
		for (int i = 0; i < 28; i++)
			result[i] = true;

		if (!canTakeOut(selection))
			result[0] = false;

		for (int i = 1; i < 28; i++) // i=0 is the off-players
		{
			if (selection < i)
				result[i] = false;

			if (!isDiceAppropriate(selection, i))
				result[i] = false;

			if (getColorOnLine(i) == 2 && getPoint(false, i) > 1) // if the
																	// ruling
																	// color
																	// is
																	// black
																	// and
																	// it
																	// has
																	// an
																	// house,
																	// disable
																	// it;
				result[i] = false;
		}

		result[26] = false;
		result[27] = false;
		result[selection] = true;
		for (int i = 0; i < 28; i++)
			if (result[i])
				System.out.printf("Line %d is active\n", i);
		return result;
	}

	private int getColorOnLine(int line) {
		if (getPoint(true, line) > 0)
			return 1; // current player is on this line
		if (getPoint(false, line) > 0)
			return 2; // enemy is on this line
		return 0;
	}

	private int getPoint(boolean _current_player, int pointnumber) {
		if (_current_player) {
			return current_player[pointnumber];
		} else {
			return victim[pointnumber];
		}
	}

}
