import java.util.Random;
import java.util.Vector;
public class MovementVector {
	
	private Vector<Move> movements;
	private Vector<Integer> diceIdUsed;
	public MovementVector()
	{
		movements = new Vector<Move>();
		diceIdUsed = new Vector<Integer>();
	}
	
	public Move getRandomMovement()
	{
		
		Move m = new Move(1);
		System.out.printf("movements.size()=%d\n", movements.size());
		if(movements.size()>0)
		{
			Random r = new Random();
			int idx = (movements.size() - (r.nextInt(movements.size()+1)));
			if(idx==movements.size())
				idx--;
		m = movements.get(idx);
		}
		return m;
	}
	
	public void addMovement(Move m, int diceId)
	{
		movements.add(m);
		diceIdUsed.add(diceId);
	}
	
	public Move getMovement(int id)
	{
		return movements.get(id);
	}
	
	public int getCount()
	{
		return movements.size();
	}
	
	public int getDiceByMovement(Move m)
	{
		for(int i = 0; i < movements.size(); i++)
			if(movements.get(i) == m)
				return i;
		System.out.printf("MovementVector::getDicebyMovement() returned -1\n");
		return -1;
	}

}
