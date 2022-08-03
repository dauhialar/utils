package pb;

import java.util.*;

/**
 * @author Ruslan Dauhiala
 */
public class Intersection
{
	private static final Set<Integer> kNumSet = new HashSet<>();
	static{
		for (int i=1; i < 70; i++)
		{
			kNumSet.add(i);
		}
	}
	private Map<Date, Set<Integer>> myPlayedNumbers = new HashMap<>();
	private Map<Integer, Set<Date>> myIntersections = new TreeMap<>();

	private Set<Integer> myIndex = new TreeSet<>();

	public List<Integer> getGame()
	{
		return myGame;
	}

	private List<Integer> myGame;
	private Date myGdate;

	public Intersection(Date theGdate, List<Integer> theGame)
	{
		myGame = theGame;
		myGdate = theGdate;
	}

	public Map<Integer, Set<Date>> getIntersections()
	{
		return myIntersections;
	}


	public void addGameIndex(int theInd)
	{
		myIndex.add(theInd);
	}

	public Set<Integer> getPlayedNumbers()
	{
		return getValues(myPlayedNumbers);
	}



	public void addPlayedNumbers(Date theDate, List<Integer> theList)
	{
		DateOk aDate = new DateOk(theDate.getTime());
		if (myPlayedNumbers.get(aDate) == null)
		{
			myPlayedNumbers.put(aDate, new HashSet<Integer>(theList));
		} else
		{
			myPlayedNumbers.get(aDate).addAll(theList);
		}
	}

	public void addIntersection(Date theDate, List<Integer> theList)
	{
		for (Integer aInt : theList)
		{
			if (!myIntersections.containsKey(aInt))
			{
				myIntersections.put(aInt, new TreeSet<Date>());
			}
			myIntersections.get(aInt).add(theDate);
		}


	}

	private static Set<Integer> getValues(Map<Date, Set<Integer>> theMap)
	{
		Set<Integer> aRes = new TreeSet<>();
		for (Set<Integer> aList : theMap.values())
		{
			aRes.addAll(aList);
//			for (Integer aNum : aList)
//			{
//				aRes.add(aNum);
//			}
		}
		return aRes;
	}

	public Set<Integer> getSuggestedNumbers()
	{
		Set<Integer> aRes = new TreeSet<>(kNumSet);
		aRes.removeAll(getPlayedNumbers());
		return aRes;
	}


	public Map<Integer, List<Date>> getDupAmountPlayed()
	{
		Map<Integer, List<Date>> aMap = new TreeMap<>();
		List<Date> aDates = new ArrayList<>(myPlayedNumbers.keySet());

		for (int i = 0; i < aDates.size(); i++)
		{
			Date aDate = aDates.get(i);
			List<Integer> aList = new ArrayList<>(myPlayedNumbers.get(aDate));
			aList = 5 < aList.size() ? aList.subList(0,5) : aList;

			for (int j = i+1; j < aDates.size(); j++)
			{
				Date aNotherDate = aDates.get(j);

				List<Integer> aList2 = new ArrayList<>(myPlayedNumbers.get(aNotherDate));
				aList2 = 5<aList2.size() ? aList2.subList(0,5) : aList2;
				List<Integer> aIntegers = Utils.getIntersection(aList, aList2);
				for (Integer aNum : aIntegers)
				{
					if (!aMap.containsKey(aNum))
					{
						aMap.put(aNum, new ArrayList<Date>());
					}
					aMap.get(aNum).add(aNotherDate);
					aMap.get(aNum).add(aDate);
				}
			}
		}

		return aMap;
	}

	public String toString()
	{
		StringBuffer aRes = new StringBuffer();
		aRes.append(Meters.kFileDateFormat.format(myGdate) + myGame + " :\n");
		aRes.append("\twrong excluded numbers: ");
		for (Map.Entry<Integer, Set<Date>> aEntry :  myIntersections.entrySet())
		{
			aRes.append("\n\t\t\t\t" + aEntry.getKey() + " (");
			List<Date> aDateL = new ArrayList<Date>(aEntry.getValue());
			Collections.sort(aDateL);
			for (Date aDate: aDateL)
			{
				aRes.append(Meters.kOutDateFormat.format(aDate) + " ");
			}
			aRes.append(")");
		}
		aRes.append("\n\tplayed numbers:     \t\t" + getPlayedNumbers().size() + "\t\t" + getPlayedNumbers() );
		aRes.append("\n\tsuggested numbers:  \t\t" + getSuggestedNumbers().size() + "\t\t" + getSuggestedNumbers() );
		//aRes.append("\n\tdup numbers:        \t\t" + getDupAmountPlayed().size() + "\t\t" + getDupAmountPlayed() );
		if (!myIndex.isEmpty())
		{
			aRes.append("\n\tindex :             \t\t" + myIndex);
		}

		return aRes.toString();
	}

	public String getStringIntersections()
	{
		StringBuffer aRes = new StringBuffer();
		for (Map.Entry<Integer, Set<Date>> aEntry :  myIntersections.entrySet())
		{
			aRes.append(aEntry.getKey() + " (");
			List<Date> aDateL = new ArrayList<Date>(aEntry.getValue());
			Collections.sort(aDateL);
			for (Date aDate: aDateL)
			{
				aRes.append(Meters.kOutDateFormat.format(aDate) + " ");
			}
			aRes.append("), ");
		}
		return aRes.toString();
	}

	public Intersection combine(Intersection theAnother)
	{
		Intersection aRes = new Intersection(myGdate, myGame);
		for (Map.Entry<Date, Set<Integer>> anEntry : myPlayedNumbers.entrySet())
		{
			aRes.myIntersections.putAll(myIntersections);
			for (Map.Entry<Integer, Set<Date>> anIntersection : theAnother.myIntersections.entrySet())
			{
				for (Date aDate : anIntersection.getValue())
				{
					aRes.addIntersection(aDate, Collections.singletonList(anIntersection.getKey())) ;
				}
			}

		}
		aRes.myPlayedNumbers.putAll(myPlayedNumbers);
		aRes.myPlayedNumbers.putAll(theAnother.myPlayedNumbers);
		return aRes;
	}

	private void addWrongExcludedComment(String s)
	{

	}

	class DateOk extends Date
	{
		public DateOk(long date)
		{
			super(date);
		}

		@Override
		public String toString()
		{
			return Meters.kOutDateFormat.format(this);
		}
	}
}
