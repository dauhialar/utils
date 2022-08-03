package pb;

/**
 * @author Ruslan Dauhiala
 */
public class Frequency
{
	int myMatch;
	int myInd;
	int myCurrentInt;

	public Frequency(int theMatch, int theInd, int theCurrentInt)
	{
		myMatch = theMatch;
		myInd = theInd;
		myCurrentInt = theCurrentInt;
	}

	@Override
	public String toString()
	{
		return myMatch + "(" + myInd + "'" + (myInd - myCurrentInt) + "')";
	}
}
