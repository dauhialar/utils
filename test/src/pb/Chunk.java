package pb;


import java.util.ArrayList;

/**
 * @author Ruslan Dauhiala
 */
public class Chunk<T> extends ArrayList<T>
{
	private int myStartInd;


	public int getStartInd()
	{
		return myStartInd;
	}

	public void setStartInd(int theStartInd)
	{
		myStartInd = theStartInd;
	}
}
