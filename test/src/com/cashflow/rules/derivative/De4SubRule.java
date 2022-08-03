package com.cashflow.rules.derivative;

import com.cashflow.rules.Accept;
import com.cashflow.rules.cofig.De4Config;
import pb.Utils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Ruslan Dauhiala
 */
public class De4SubRule implements Accept
{
	Set<Integer> my4ndDerChunk1IncSet;
	int myGameModMax;
	int myDelta1ModMax;
	int myDelta2ModMax;
	int myDelta3ModMax;

	public De4SubRule(Map<Date, List<Integer>> aDelta4Map, List<Date> myDates, int theGameModMax, int theDelta1ModMax, int theDelta2ModMax, int theDelta3ModMax)
	{
		myGameModMax=theGameModMax;
		myDelta1ModMax= theDelta1ModMax;
		myDelta2ModMax= theDelta2ModMax;
		myDelta3ModMax= theDelta3ModMax;

		my4ndDerChunk1IncSet = new LinkedHashSet<>();
		List<Integer> aList = new ArrayList<>();
		for (int i = 0; i < aDelta4Map.size(); i++)
		{
			int aNum = aDelta4Map.get(myDates.get(i)).get(0);
			my4ndDerChunk1IncSet.add(aNum);
			aList.add(aNum);
		}

		// =========
//		int aTargetSize = my4ndDerChunk1IncSet.size() - De4Config.kToRemove;
//		int i = 0;
//		while (my4ndDerChunk1IncSet.size() > aTargerSize)
//		{
//			my4ndDerChunk1IncSet.remove(aDelta4Map.get(myDates.get(i++)).get(0));
//		}

//		//List<Integer> aGapList = Utils.getGapBetweenSameNumbers(aDelta4Map.values());
//		List<Integer> aGapList = Utils.getGapBetweenSameNumbers(aDelta4Map.get(myDates.get(0)).get(0), aList);
//		Set<Integer> aRemoveNums = new HashSet<>();
//		while (aRemoveNums.size() < aTargetSize && i < aGapList.size())
//												//x=15   3 = denied%: 	 0.29    	;  errors % : 0.20      [13, 14, 15]
//												//       5 = denied%: 	 0.35    	;  errors % : 0.25  [11, 13, 14, 15, 18]
//		                             // x=1   25= denied%: 	 0.38    	;  errors % : 0.25  [11, 13, 14, 15, 18]
//												//x =0   35= denied%: 	 0.47    	;  errors % : 0.45  [0, 7, 9, 10, 11, 13, 14, 15, 18]
//												// x=15  25= denied%: 	 0.46    	;  errors % : 0.25  [11, 13, 14, 15, 18]    Denied : 	 5193489	;  Accepted : 6045024
//		{
//			for (int k = 0; k < aGapList.size() && aRemoveNums.size() < aTargetSize; k++)
//			{
//				int anInd = aGapList.get(k);
//				aRemoveNums.add(aDelta4Map.get(myDates.get(i + anInd)).get(0));
//			}
////			int anInd = aGapList.get(i);
////			if (-1 < anInd && anInd < myDates.size())
////			{
////				//my4ndDerChunk1IncSet.remove(aDelta4Map.get(myDates.get(anInd + i)).get(0));
////				my4ndDerChunk1IncSet.remove(aDelta4Map.get(myDates.get(i + anInd)).get(0));
////			}
////			if (my4ndDerChunk1IncSet.size() > aTargetSize)
////			{
////				my4ndDerChunk1IncSet.remove(aDelta4Map.get(myDates.get(i)).get(0));
////			}
//			i++;
//		}
		// ============


// best so far !!!
// =====================

		Set<Integer> aRemoveNums = new HashSet<>();

		Map<Integer, Integer> aNextChunkPos =
				  De4Config.kInOrder ?
							 getNextChunkPos(aList, De4Config.kGap) :
							 getNextChunkPosAll(aList, De4Config.kGap);
		List<Integer> aKeys = new ArrayList<>(aNextChunkPos.keySet());

		int aLine = 0;

		while (aRemoveNums.size() < De4Config.kToRemove && aLine < aList.size())
		{
			for (int i = 0;
				  i < De4Config.kClusterCheckNum &&
							 aRemoveNums.size() < De4Config.kToRemove &&
							 i < aKeys.size();
				  i++)
			{
				int aNum = aKeys.get(i) - aLine;
				if (aNum > -1 && aNum < aList.size())
				{
					aRemoveNums.add(aList.get(aNum));
				}
			}
			// ??
//			if (aLine <= 0)
//			{
//				aLine = -aLine;
//				aLine++;
//			} else
//			{
//				aLine = -aLine;
//			}
			aLine++;
		}
		for (int i = aKeys.get(0); i > 0 && aRemoveNums.size() < De4Config.kToRemove; i--)
		{
			aRemoveNums.add(aList.get(i));
		}
// =====================

//		List<Integer> anArrayList = new ArrayList<>(my4ndDerChunk1IncSet);
//		Set<Integer> aRemoveNums = new HashSet<>(my4ndDerChunk1IncSet);
//
//		int aPart = my4ndDerChunk1IncSet.size() / De4Config.kPartToStay;
//
//		int aStart = De4Config.kPartStep * aPart;
//		int anEnd = aStart + aPart;
//		if (anEnd < anArrayList.size() && anEnd + aPart > anArrayList.size())
//		{
//			anEnd = anArrayList.size();
//		}
//		aRemoveNums.removeAll(anArrayList.subList(aStart, anEnd < anArrayList.size() ? anEnd : anArrayList.size()));

		De4Config.kDeniedRate = aRemoveNums.size() / (double) my4ndDerChunk1IncSet.size();
		my4ndDerChunk1IncSet.removeAll(aRemoveNums);
	}



	/**
	 * 2 40
	 // ERRORS: 	 15; 		 Denied : 	 8815358	;  Accepted : 2423155 t	 time: 	 4; 	 total : 99 sec. #: [0, 1, 4, 5, 6, 8, 9, 11, 12, 13, 14, 15, 17, 18, 19]
	 // 							 denied%: 	 0.78    	;  errors % : 0.75
	 * 4 10
	 // ERRORS: 	 2; 		 Denied : 	 2207678	;  Accepted : 9030835 t	 time: 	 11; 	 total : 246 sec. #: [14, 18]
	 // 							 denied%: 	 0.20    	;  errors % : 0.10
	 * 4 40
	 // ERRORS: 	 15; 		 Denied : 	 8816096	;  Accepted : 2422417 t	 time: 	 3; 	 total : 95 sec. #: [0, 1, 4, 5, 6, 7, 9, 11, 12, 13, 14, 15, 17, 18, 19]
	 // 							 denied%: 	 0.78    	;  errors % : 0.75
	 */
	private Map<Integer,Integer> getNextChunkPosAll(List<Integer> theList, int theFirst)
	{
		List<Integer> aPattern = new ArrayList<>();
		for (int i = 0; i < theFirst; i++)
		{
			aPattern.add(theList.get(i));
		}
		int aStart = theFirst;// + 50;
		int anEnd = theList.size() - theFirst;

		Map<Integer, Integer> anIndexGapMap = new HashMap<>();
		for (int i = aStart; i < anEnd; i++)
		{
			for (int j = i + theFirst; j < anEnd; j++)
			{
				if (theList.subList(i, j).containsAll(aPattern))
				{
					//anIndexGapMap.put(j, j-i);
					int aTrueStart = i;
					int aTrueEnd = j;
					for (int k = i; k < j;k++)
					{
						if (aPattern.contains(theList.get(k)))
						{
							aTrueStart = k;
							break;
						}
					}
					for (int k = j; k > i; k--)
					{
						if (aPattern.contains(theList.get(k)))
						{
							aTrueEnd = k;
							break;
						}
					}

					i = aTrueStart;
					anIndexGapMap.put(aTrueEnd, aTrueEnd-i);
					break;
				}
			}
		}
		Map<Integer, Integer> aConvertMap = new HashMap<>();
		for (Map.Entry<Integer, Integer> aEntry : anIndexGapMap.entrySet())
		{
			aConvertMap.put(aEntry.getKey() + aEntry.getValue(), aEntry.getValue());
		}
		anIndexGapMap = aConvertMap; // ???

		return getSortedMap(anIndexGapMap);
	}

	private Map<Integer,Integer> getNextChunkPos(List<Integer> theList, int theFirst)
	{
		List<Integer> aPattern = new ArrayList<>();
		for (int i = 0; i < theFirst; i++)
		{
			aPattern.add(theList.get(i));
		}
		int aStart = theFirst;// + 50;
		int anEnd = theList.size() - theFirst;
		Map<Integer, Integer> anIndexGapMap = new HashMap<>();
		for (int i = aStart; i < anEnd; i++)
		{
			int aFirst = theList.subList(i, anEnd).indexOf(aPattern.get(0));
			if (aFirst > -1)
			{
				aFirst += i;
				int aNext = aFirst;
				for (int j = 1; j < aPattern.size() && aNext > -1; j++)
				{
					int aShift = aNext;
					aNext = theList.subList(aShift, anEnd).indexOf(aPattern.get(j));
					if (aNext > -1)
					{
						aNext += aShift;
					} else
					{
						break;
					}
				}
				if (aNext > -1)
				{
					anIndexGapMap.put(aNext, aNext - aFirst);
					//anIndexGapMap.put(aFirst, aNext - aFirst);   // ???
				}
			}
		}
		return getSortedMap(anIndexGapMap);
	}

	public static Map<Integer, Integer> getSortedMap(Map<Integer, Integer> theAnIndexGapMap)
	{
		return theAnIndexGapMap
				  .entrySet()
				  .stream()
				  .sorted((e1, e2) -> {
								 int cmp1 = e1.getValue().compareTo(e2.getValue());
								 if (cmp1 != 0)
								 {
									 return cmp1;
								 } else
								 {
									 return e1.getKey().compareTo(e2.getKey());
								 }
							 }
							 //Map.Entry.comparingByValue()
				  )
				  .collect(Collectors.toMap(
										Map.Entry::getKey,
										Map.Entry::getValue,
										(e1, e2) -> e1,
										LinkedHashMap::new)
				  );
	}

	@Override
	public boolean accept(List<Integer> thePotentialGame)
	{
		List<Integer> aDer1 = DeltaRule.kMode ? Utils.getGameModDelta(thePotentialGame, myGameModMax) : Utils.getGameHDelta(thePotentialGame);
		List<Integer> aDer2 = DeltaRule.kMode ? Utils.getGameModDelta(aDer1, myDelta1ModMax) : Utils.getGameHDelta(aDer1);
		List<Integer> aDer3 = DeltaRule.kMode ? Utils.getGameModDelta(aDer2, myDelta2ModMax) : Utils.getGameHDelta(aDer2);
		Integer aDer4 = DeltaRule.kMode ? Utils.getGameModDelta(aDer3, myDelta3ModMax).get(0) : Utils.getGameHDelta(aDer3).get(0);
		return my4ndDerChunk1IncSet.contains(aDer4);
	}
}

