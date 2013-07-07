import java.util.Random;

public class Dice {

	private int m_Count;
	private int m_Dice[] = new int[4];
	private static Random r = new Random();

	/**
	 * Creates a new dice instance.
	 */
	public Dice() {
		
			int first  = (int)(r.nextInt(6) + 1);
			int second = (int)(r.nextInt(6) + 1);
			if (first == second) {
				for (int i = 0; i < 4; i++)
					m_Dice[i] = first;
				m_Count = 4;
			} else {
				m_Count = 2;
				m_Dice[0] = first;
				m_Dice[1] = second;
				for (int i = 2; i < 4; i++)
					m_Dice[i] = 0;
			System.out.printf("first=%d, second=%d,count=%d\n", first, second, m_Count);
		}
	}
	
	/**
	 * Checks whether all the movements by this dice are done.
	 * @return
	 * true if done, false if not.
	 */
	public boolean finished()
	{
		for(int i = 0; i < m_Count; i++)
			if(m_Dice[i]!=0)
				return false;
		return true;
	}
	
	/**
	 * Removes a specific number of the dice's results.
	 * @param num
	 * the number to remove.
	 */
	public void removeNumber(int num)
	{
		int i = 0;
		boolean changed = false;
		System.out.printf("requesting to remove number %d\n", num);
		while(i < m_Count && !changed)
		{
			if(m_Dice[i]==num)
			{
				System.out.printf("changed %d\n", num);
				m_Dice[i]=0;
				changed = true;
			}
			i++;
		}
	}
	
	/**
	 * Removes a specific dice by it's id.
	 * @param i
	 * the id to remove.
	 */
	public void usedDice(int i)
	{
		m_Dice[i] = 0;
	}

	/**
	 * Gets the number of dices.
	 * @return
	 * 4 if double, otherwise 2.
	 */
	public int getCount() {
		return m_Count;
	}

	/**
	 * Gets the value of the dice by it's ID.
	 * @param i
	 * the ID.
	 * @return
	 * the value of the dice.
	 */
	public int getDice(int i) {
		return m_Dice[i];
	}

}
