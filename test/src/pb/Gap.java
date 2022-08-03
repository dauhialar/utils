package pb;

/**
 * @author Ruslan Dauhiala
 */
public class Gap
{
	int myGameNum;
	int myPositionGap;
	int myTotalGap;

	public Gap(int theGameNum, int thePositionGap, int theTotalGap)
	{
		myGameNum = theGameNum;
		myPositionGap = thePositionGap;
		myTotalGap = theTotalGap;
	}

	@Override
	public String toString()
	{
		return myPositionGap + (myPositionGap != myTotalGap ? "/" + myTotalGap + "t" : "");
	}
}
