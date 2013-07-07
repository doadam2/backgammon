public class AI {

	private int current[];
	private int victim[];
	private Dice diceInfo;
	private boolean separated;

	/**
	 * Creates a new AI calculation.
	 * 
	 * @param computer
	 *            an array with the computer's pieces.
	 * @param player
	 *            an array with the player's pieces.
	 * @param d
	 *            the dice to calculate the movement with.
	 * @param sep
	 *            true if the players are separated, false if not.
	 */
	public AI(int computer[], int player[], Dice d, boolean sep) {
		System.out.println("Initializing AI class:");
		for (int i = 0; i < d.getCount(); i++)
			System.out.printf("dice %d=%d\n", i, d.getDice(i));
		for (int i = 27; i >= 0; i--)
			System.out.print(computer[i]);
		System.out.println();
		current = new int[28];
		victim = new int[28];
		for (int i = 0; i < 28; i++) {
			current[i] = computer[i];
			victim[i] = player[i];
		}
		diceInfo = d;
		separated = sep;
	}

	private void errorMove(Move m, String func) {
		if (!m.isOK())
			System.out.printf("AI::%s(): returned invalid movement info\n",
					func);
	}

	private float chanceToGetEaten(int new_line) {
		float calc = 0.0f;
		for (int i = 0; i <= 24; i++)
			if (victim[i] > 0)
				if (Math.abs(new_line - i) > 0 && Math.abs(new_line - i) < 7)
					calc += 1.0f;
		return calc / 36;
	}

	/**
	 * Checks if the computer can start taking out pieces.
	 * 
	 * @return true if can, false if can't.
	 */
	private boolean canTakeOut() {
		for (int i = 18; i > 0; i--)
			if (current[i] > 0)
				return false;
		return true;
	}

	/**
	 * Checks if the computer can take out a specific piece. The function
	 * considers the dice.
	 * 
	 * @param selection
	 *            the line ID with the piece
	 * @return true if can, false if can't.
	 */
	private boolean canTakeOut(int selection) {
		for (int i = 18; i > 0; i--)
			if (current[i] > 0)
				return false;

		selection = 25 - selection;
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

		selection = 25 - selection;
		for (int i = 0; i < diceInfo.getCount(); i++)
			if (diceInfo.getDice(i) - selection > 0)
				for (int j = selection - 1; selection > 18; selection--)
					if (current[j] > 0) {
						System.out.printf("current[%d]=%d\n", j, current[j]);
						return false;
					}

		return true;
	}

	/**
	 * Checks if the computer has eaten players to take care of first.
	 * 
	 * @return true if there are, false if not.
	 */
	private boolean hasEatenPlayers() {
		return current[26] > 0;
	}

	/**
	 * Get a house according to the house ID.
	 * 
	 * @param number
	 *            the house ID.
	 * @return a Move class with house movement info.
	 */
	private Move getHouse() {
		MovementVector houseVector = new MovementVector();
		for (int i = 0; i <= 24; i++)
			for (int j = 0; j < diceInfo.getCount(); j++)
				if (i + diceInfo.getDice(j) < 25)
					if ((current[i] == 1 || current[i] > 2)
							&& current[i + diceInfo.getDice(j)] == 1) {
						if (i != i + diceInfo.getDice(j)) {
							System.out.printf("getHouse:from=%d,to=%d\n", i, i
									+ diceInfo.getDice(j));
							Move m = new Move(1);
							m.addFrom(i);
							m.addTo(i + diceInfo.getDice(j));
							houseVector.addMovement(m, j);
						}
					}

		// Opened players, close them.
		if (houseVector.getCount() > 0) {
			// find the piece which is closest to the end
			int maxto = houseVector.getMovement(0).getTo(0);
			int index = 0;
			for (int i = 0; i < houseVector.getCount(); i++)
				if (houseVector.getMovement(i).getTo(0) > maxto)
					maxto = houseVector.getMovement(i).getTo(0);

			for (int i = 0; i < houseVector.getCount(); i++)
				if (houseVector.getMovement(i).getTo(0) == maxto) {
					Move result = houseVector.getMovement(i);
					diceInfo.usedDice(houseVector.getDiceByMovement(result));
					System.out.printf(
							"houseClose: closing hole in %d with %d\n", maxto,
							result.getFrom(0));
					return result;
				}
		}

		houseVector = new MovementVector();
		int[] cloneDice = new int[diceInfo.getCount()];
		for (int i = 0; i < diceInfo.getCount(); i++)
			cloneDice[i] = diceInfo.getDice(i);
		// Create houses from scratch.
		for (int i = 0; i <= 24; i++) {
			for (int j = 0; j < diceInfo.getCount(); j++) {
				if (cloneDice[j] > 0) {
					if (i + cloneDice[j] < 25) {
						if (current[i] == 1 || current[i] > 2) // do not destroy
																// 2-houses
						{
							if (victim[i + cloneDice[j]] < 2) {
								// we 'theoretically' move our player to this
								// position
								current[i]--;
								current[i + cloneDice[j]]++;
								int newHouse = i + cloneDice[j];
								System.out.printf("i=%d,dice=%d,newHouse=%d\n",
										i, cloneDice[j], newHouse);
								cloneDice[j] = 0;
								for (int k = 0; k <= 24; k++) {
									for (int l = 0; l < diceInfo.getCount(); l++) {
										if (cloneDice[l] > 0) {
											if (k + cloneDice[l] < 25) {
												if (current[k] == 1
														|| current[k] > 2) // do
																			// not
																			// destroy
													// 2-houses
													if (k + cloneDice[l] == newHouse) {
														Move newMove = new Move(
																2);
														newMove.addFrom(i);
														newMove.addTo(newHouse);
														newMove.addFrom(k);
														newMove.addTo(k
																+ cloneDice[l]);
														houseVector
																.addMovement(
																		newMove,
																		l);
														System.out
																.printf("TwoHouse:1:%dto%d\n2:%dto%d\n",
																		i,
																		newHouse,
																		k,
																		k
																				+ cloneDice[l]);
													}
											}
										}
									}
								}
								current[i]++;
								current[newHouse]--;
							}
						}
					}
				}
			}
			cloneDice = new int[diceInfo.getCount()];
			for (int itr = 0; itr < diceInfo.getCount(); itr++)
				cloneDice[itr] = diceInfo.getDice(itr);
		}

		for (int i = 0; i < 28; i++)
			System.out.print(current[i]);
		System.out.println();

		Move m = houseVector.getRandomMovement();
		//for (int i = 0; i < diceInfo.getCount(); i++)
			for (int j = 0; j < m.getCount(); j++)
				diceInfo.removeNumber(m.getTo(j) - m.getFrom(j));
		return m;
	}

	/**
	 * Checks how many houses can be built
	 * 
	 * @return number of houses which can be built.
	 */
	private int countPossibleHouses() {
		int res = 0;
		for (int i = 1; i <= 24; i++)
			for (int j = 0; j < diceInfo.getCount(); j++)
				if (diceInfo.getDice(j) > 0)
					if (i + diceInfo.getDice(j) < 25)
						if ((current[i] == 1 || current[i] > 2)
								&& current[i + diceInfo.getDice(j)] == 1) {
							res++;
							System.out.printf(
									"can build house from %d to %d\n", i, i
											+ diceInfo.getDice(j));
						}
		return res;
	}

	/**
	 * Gets the best escaping move.
	 * 
	 * @return a Move class with escaping movement info.
	 */
	private Move getBestEscapeMove() {
		Move m = new Move(1);
		for (int j = 0; j < diceInfo.getCount(); j++) {
			if (diceInfo.getDice(j) != 0)
				if (victim[diceInfo.getDice(j)] < 2) {
					if (victim[diceInfo.getDice(j)] == 1) {
						m.addFrom(26);
						m.addTo(diceInfo.getDice(j));
						diceInfo.usedDice(j);
						errorMove(m, "getBestEscapeMove+");
						return m;
					}
					if (!m.isOK()) {
						if (current[diceInfo.getDice(j)] == 1) {
							m.addFrom(26);
							m.addTo(diceInfo.getDice(j));
							diceInfo.usedDice(j);
							errorMove(m, "getBestEscapeMove++");
							return m;
						}
					}
					if (!m.isOK()) {
						m.addFrom(26);
						m.addTo(diceInfo.getDice(j));
						diceInfo.usedDice(j);
						errorMove(m, "getBestEscapeMove+++");
						return m;
					}
				}
		}
		return m;
	}

	/**
	 * Gets the best single move.
	 * 
	 * @return a Move class with single movement info.
	 */
	private Move getBestSingleMove() {
		Move m = new Move(1);
		for (int i = 0; i <= 24; i++)
			for (int j = 0; j < diceInfo.getCount(); j++)
				if (diceInfo.getDice(j) != 0)
					if (i + diceInfo.getDice(j) < 25)
						if (current[i] > 0
								&& victim[i + diceInfo.getDice(j)] == 1) {
							if (i != i + diceInfo.getDice(j)) {
								m.addFrom(i);
								m.addTo(i + diceInfo.getDice(j));
								diceInfo.usedDice(j);
								errorMove(m, "getBestSingleMove+");
								return m;
							}
						}
		if (!m.isOK()) {
			for (int i = 0; i <= 24; i++)
				for (int j = 0; j < diceInfo.getCount(); j++)
					if (diceInfo.getDice(j) != 0)
						if (i + diceInfo.getDice(j) < 25)
							if (current[i] > 0
									&& victim[i + diceInfo.getDice(j)] < 2) {
								if (i != i + diceInfo.getDice(j)
										&& i + diceInfo.getDice(j) != 0) {
									m.addFrom(i);
									m.addTo(i + diceInfo.getDice(j));
									diceInfo.usedDice(j);
									errorMove(m, "getBestSingleMove++");
									return m;
								}
							}
		}
		errorMove(m, "getBestSingleMove+++");
		return m;
	}

	/**
	 * Checks whether this line is the closest one to the entrance of the house with players.
	 * @param line
	 * line ID
	 * @return
	 * true if last in house, false if not.
	 */
	private boolean isLastInHouse(int line)
	{
		if(line==19)
			return true;
		for(int i = line-1; i >= 19; i--)
			if(current[i] > 0)
				return false;
		return true;
	}

	/**
	 * Gets the best move for taking pieces outside
	 * 
	 * @return a Move class with the appropriate movement info.
	 */
	private Move getBestTakeOutMove() {
		Move m = new Move(1);
		for (int i = 19; i <= 24; i++)
			for (int j = 0; j < diceInfo.getCount(); j++)
				if (diceInfo.getDice(j) != 0)
					if (current[i] > 0)
						if (i + diceInfo.getDice(j) < 31) {
							if (i + diceInfo.getDice(j) == 25
									|| (isLastInHouse(i) && i
											+ diceInfo.getDice(j) >= 25)) {
								m.addFrom(i);
								m.addTo(25);
								diceInfo.usedDice(j);
								System.out.printf(
										"taking out, from %d to %d\n", i, 25);
								errorMove(m, "getBestTakeOutMove");
								return m;
							}
						}

		for (int i = 19; i <= 24; i++)
			for (int j = 0; j < diceInfo.getCount(); j++)
				if (diceInfo.getDice(j) != 0)
					if (current[i] > 0)
							if (i + diceInfo.getDice(j) < 25) {
								m.addFrom(i);
								m.addTo(i + diceInfo.getDice(j));
								diceInfo.usedDice(j);
								System.out.printf("mekapel, from %d to %d\n",
										i, 25);
								errorMove(m, "getBestTakeOutMove+");
								return m;
							}

		return m;
	}

	/**
	 * Gets the best move for the current dice.
	 * 
	 * @return a Move class with multiple movement infos.
	 */
	public Move getBestMove() {
		Move m = new Move(diceInfo.getCount());
		int i = 0;
		while (i < diceInfo.getCount() && !diceInfo.finished()) {
			if (hasEatenPlayers()) {
				Move h = getBestEscapeMove();
				if (h.isOK()) {
					System.out.printf("escaping... to %d\n", h.getTo(0));
					m.addFrom(h.getFrom(0));
					m.addTo(h.getTo(0));
					current[h.getFrom(0)]--;
					current[h.getTo(0)]++;
				} else {
					System.out
							.println("nowhere to run, nowhere to hide, not playing..");
					return m;
				}
			} else {
				if (canTakeOut()) {
					System.out.printf("PC is ready to take out pieces!");
					Move h = getBestTakeOutMove();
					if (h.isOK()) {
						System.out.printf("takeout... to %d\n", h.getTo(0));
						m.addFrom(h.getFrom(0));
						m.addTo(h.getTo(0));
						current[h.getFrom(0)]--;
						current[h.getTo(0)]++;
					} else {
						System.out.println("what the fuck\n");
						return m;
					}
				} else {
					System.out.println("countPossibleHouses()="
							+ countPossibleHouses());
					Move h = getHouse();
					if (h.isOK()) {
						for (int j = 0; j < h.getCount(); j++) {
							System.out.printf(
									"building a house... from %d to %d\n",
									h.getFrom(j), h.getTo(j));
							m.addFrom(h.getFrom(j));
							m.addTo(h.getTo(j));
							current[h.getFrom(j)]--;
							current[h.getTo(j)]++;
						}
					} else {
						System.out
								.println("h.isOK() for house building is false!");
						h = getBestSingleMove();
						System.out.printf("single move... from %d to %d\n",
								h.getFrom(0), h.getTo(0));						
						m.addFrom(h.getFrom(0));
						m.addTo(h.getTo(0));
						current[h.getFrom(0)]--;
						current[h.getTo(0)]++;
					}
				}
			}
			i++;
		}
		for (int j = 0; j < m.getCount(); j++)
			System.out.printf("from %d to %d, id=%d\n", m.getFrom(j),
					m.getTo(j), j);

		if (hasEatenPlayers())
			System.out.println("should not move");
		return m;
	}
}
