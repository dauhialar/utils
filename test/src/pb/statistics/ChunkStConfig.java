package pb.statistics;

/**
 * @author Ruslan Dauhiala
 */
public class ChunkStConfig
{
	private int myStartInd = -1;

	private int myChunkSize;

	private boolean myInOrder;

	private int myLenght;

	public ChunkStConfig(int theChunkSize, boolean theInOrder)
	{
		myChunkSize = theChunkSize;
		myInOrder = theInOrder;
	}

	public ChunkStConfig(int theChunkSize, boolean theInOrder, int theLenght)
	{
		myChunkSize = theChunkSize;
		myInOrder = theInOrder;
		myLenght = theLenght;
	}

	public ChunkStConfig(int theChunkSize, boolean theInOrder, int theStartInd, int theLenght)
	{
		myStartInd = theStartInd;
		myChunkSize = theChunkSize;
		myInOrder = theInOrder;
		myLenght = theLenght;
	}

	public int getStartInd()
	{
		return myStartInd;
	}

	public int getChunkSize()
	{
		return myChunkSize;
	}

	public void setChunkSize(int theChunkSize)
	{
		myChunkSize = theChunkSize;
	}

	public boolean isInOrder()
	{
		return myInOrder;
	}

	public void setInOrder(boolean theInOrder)
	{
		myInOrder = theInOrder;
	}

	public int getLenght()
	{
		return myLenght;
	}
}
