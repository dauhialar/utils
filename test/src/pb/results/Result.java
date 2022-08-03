package pb.results;

import pb.Intersection;

import java.util.Date;
import static pb.Meters.kFileDateFormat;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Ruslan Dauhiala
 */
public class Result
{
	private Map<Date, Intersection> myIntersectionMap;
	private String myName;

	public Result(String theName)
	{
		myName = theName;
	}

	public Map<Date, Intersection> getIntersectionMap()
	{
		if (myIntersectionMap == null)
		{
			myIntersectionMap = new LinkedHashMap<>();
		}
		return myIntersectionMap;
	}

	public String getName()
	{
		return myName;
	}

	public void print()
	{
		System.out.println("======= " + getName() + " =======\n");
		for (Map.Entry<Date, Intersection> aEntry : getIntersectionMap().entrySet())
		{
			String anOutPut = aEntry.getValue() + "\n";
			System.out.println(anOutPut);
		}
	}

	public Result combine(Result theAnother)
	{
		Result aResult = new Result("comb");
		for (Map.Entry<Date, Intersection> anEntry : myIntersectionMap.entrySet())
		{
			Intersection anAnotherIntersection = theAnother.getIntersectionMap().get(anEntry.getKey());
			Intersection aCombineIntersection = anEntry.getValue().combine(anAnotherIntersection);

			aResult.getIntersectionMap().put(anEntry.getKey(), aCombineIntersection);
		}
		return aResult;
	}
}
