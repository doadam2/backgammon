
public class Move {
	
	private int from[], to[], counter_from, counter_to, count;	
	public Move(int c)
	{
		from = new int[c];
		to = new int[c];
		for(int i = 0; i < count; i++)
		{
			from[i] = -1;
			to[i] = -1;
		}
		counter_from = 0;
		counter_to = 0;
		count = c;
	}
	
	public int getCount()
	{
		return count;
	}
	
	public boolean isOK()
	{
		for(int i = 0; i < count; i++)
			if(from[i] == to[i] || from[i] == -1)
				return false;
		return true;
	}
	
	public void addFrom(int value)
	{
		if(counter_from!=count)
		from[counter_from++] = value;
	}
	
	public void addTo(int value)
	{
		if(counter_to!=count)
		to[counter_to++] = value;
	}
	
	public int getFrom(int i)
	{
		return from[i];
	}
	
	public int getTo(int i)
	{
		return to[i];
	}

}
