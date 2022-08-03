package com.cashflow.rules.vertical;

import com.cashflow.rules.AbstractRule;
import com.cashflow.rules.derivative.DeltaRule;
import pb.Utils;
import pb.file.FileReader;

import java.util.*;

/**
 *

 total:11238513
 time: 47 sec.
 TOTAL. excl: 512714, incl: 10725799
 = VerticalGameDeltaRule =
 excl:512714, incl:10725799; total:11238513


 total:11238513
 time: 1217 sec.
 TOTAL. excl: 2586972, incl: 8651541
 = VerticalGameDeltaRule =
 excl:2586972, incl:8651541; total:11238513


 ===== just 4th derivative
				total:11238513
				time: 9 sec.
				TOTAL. excl: 338771, incl: 10899742
				= VerticalGameDeltaRule =
				excl:338771, incl:10899742; total:11238513
 ===== end


 total:11238513
 time: 46 sec.
 TOTAL. excl: 3630356, incl: 7608157
 excl:3630356, incl:7608157; total:11238513

 total:11238513
 time: 41 sec.
 TOTAL. excl: 3630356, incl: 7608157
 excl:3630356, incl:7608157; total:11238513


<<< kGap = 12; kCheckFirstDerivative = false; >>>
 time: 42 sec.
 TOTAL. excl: 3609022, incl: 7629491

 <<< kGap = 12; kCheckFirstDerivative = true; >>>
 time: 51 sec.
 TOTAL. excl: 3691160, incl: 7547353

 * @author Ruslan Dauhiala
 */
//@Deprecated
public class VerticalGameDeltaRule extends AbstractRule
{
	public final static int kGap = 12;
	public final static boolean kCheckFirstDerivative = true;

	class _Deltas
	{
		Map<Date, List<Integer>> myVertDeltaMap;
		Set<List<Integer>> myDelta1Set ;
		Set<List<Integer>> myDelta2Set ;
		Set<List<Integer>> myDelta3Set ;
		Set<List<Integer>> myDelta4Set ;

		_Deltas(Map<Date, List<Integer>> theMap, int theGap)
		{
			myVertDeltaMap = DeltaRule.getVerticalDelta(theMap, theGap);

			Map<Date, List<Integer>> myHdelta1Map = DeltaRule.getDeltaMap(myVertDeltaMap);
			Map<Date, List<Integer>> myHdelta2Map = DeltaRule.getDeltaMap(myHdelta1Map);
			Map<Date, List<Integer>> myHdelta3Map = DeltaRule.getDeltaMap(myHdelta2Map);
			Map<Date, List<Integer>> myHdelta4Map = DeltaRule.getDeltaMap(myHdelta3Map);

			myDelta1Set = new HashSet<>(myHdelta1Map.values());
			myDelta2Set = new HashSet<>(myHdelta2Map.values());
			myDelta3Set = new HashSet<>(myHdelta3Map.values());
			myDelta4Set = new HashSet<>(myHdelta4Map.values());
		}
	}

//	List<Integer> myCol0;
//	List<Integer> myCol1;
//	List<Integer> myCol2;
//	List<Integer> myCol3;
//	List<Integer> myCol4;


	HashMap<Integer, _Deltas> myVertDeltaMap;

	public VerticalGameDeltaRule(Map<Date, List<Integer>> theGamesMap)
	{
		super(theGamesMap);
	}

	/**

	 total:11238513

	 i=5:
		 time: 38 sec.
		 TOTAL. excl: 2045534, incl: 9192979
		 excl:2045534, incl:9192979; total:11238513

	 i=10:
		 time: 40 sec.
		 TOTAL. excl: 3565442, incl: 7673071
		 excl:3565442, incl:7673071; total:11238513

	 i=15:
		 time: 61 sec.
		 TOTAL. excl: 3609022, incl: 7629491
		 excl:3609022, incl:7629491; total:11238513

	 i=20:
		 time: 81 sec.
		 TOTAL. excl: 3715420, incl: 7523093
		 excl:3715420, incl:7523093; total:11238513
	 */
	@Override
	public void setup()
	{
		myVertDeltaMap = new HashMap<>();

		for (int i = 1; i <= kGap; i++)
		{
			myVertDeltaMap.put(i, new _Deltas(myGamesMap, i));
		}
	}


	public boolean contNotInOrder(List<Integer> theList1, List<Integer> theList2)
	{
		return theList1.containsAll(theList2) && theList2.containsAll(theList1);
	}

	public boolean contNotInOrder(Set<List<Integer>> theListSet, List<Integer> theIntegerList)
	{
		for (List<Integer> aList : theListSet)
		{
			if (contNotInOrder(aList, theIntegerList))
			{
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean accept(List<Integer> thePotentialGame)
	{
		int aNum = 0;
		for (int i : myVertDeltaMap.keySet())
		{
			Date aDate = myDates.get(i-1);
			List<Integer> aDelta = getVDelta(thePotentialGame, myGamesMap.get(aDate).subList(0,5));
			aDelta = Utils.getGameHDelta(aDelta);
			_Deltas aDeltaO = myVertDeltaMap.get(i);
			if ( !kCheckFirstDerivative ||
					  !aDeltaO.myDelta1Set.contains(aDelta)
					  //&& !contNotInOrder(aDeltaO.myDelta1Set, aDelta)
					  )
			{
				aDelta = Utils.getGameHDelta(aDelta);
				//if (!aDeltaO.myDelta2Set.contains(aDelta) && !contNotInOrder(aDeltaO.myDelta2Set, aDelta))
				{
					aDelta = Utils.getGameHDelta(aDelta);
					aDelta = Utils.getGameHDelta(aDelta);
					if (aDeltaO.myDelta4Set.contains(aDelta))
					{
						aNum++;
//						trackResult(true);
//						return true;
					} else
					{
						trackResult(false);
						return false;
					}
				}
			} else
			{
				trackResult(false);
				return false;
			}
		}
		if (aNum == myVertDeltaMap.size())
		{
			trackResult(true);
			return true;
		}

		//System.out.println(thePotentialGame);
		trackResult(false);
		return false;
	}

	public static List<Integer> getVDelta(List<Integer> thePotentialGame, List<Integer> thePrevGame)
	{
		List<Integer> aRes = new ArrayList<>();
		for (int i = 0; i < thePotentialGame.size(); i++)
		{
			aRes.add(thePotentialGame.get(i) - thePrevGame.get(i));
		}
		return aRes;
	}

	public static void main(String[] args)
	{
		Map<Date, List<Integer>> myAllTheFun = new FileReader().loadFromFile("/Users/jet/Documents/pb/pb.txt");

		new VerticalGameDeltaRule(myAllTheFun).test();

	}
//	private List<Integer> getGameAgoNums(List<Integer> thePotentialGame)
//	{
//		List<Integer> aRes = new ArrayList<>();
//		aRes.add(myCol0.indexOf(thePotentialGame.get(0)));
//		aRes.add(myCol1.indexOf(thePotentialGame.get(1)));
//		aRes.add(myCol2.indexOf(thePotentialGame.get(2)));
//		aRes.add(myCol3.indexOf(thePotentialGame.get(3)));
//		aRes.add(myCol4.indexOf(thePotentialGame.get(4)));
//		return aRes;
//	}

	private void test()
	{
		System.out.println("VertivalGameDeltaRule derivativeX1 statistic: ");

		for (int i : myVertDeltaMap.keySet())
		{
			System.out.println(" === " + i + " === ");
			DeltaRule.getFullDerivativeStat(myVertDeltaMap.get(i).myVertDeltaMap);
			System.out.println(" ================= \n");
		}
	}
}
