package com.cashflow.rules.derivative;

import com.cashflow.rules.Accept;
import com.cashflow.rules.cofig.De4Config;
import pb.Utils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Ruslan Dauhiala
 */
public class De4PerGame implements Accept
{
	Set<Integer> my4ndDerChunk1IncSet;
	int myGameModMax;
	int myDelta1ModMax;
	int myDelta2ModMax;
	int myDelta3ModMax;


	public De4PerGame(Map<Date, List<Integer>> aDelta4Map, List<Date> myDates, int theGameModMax, int theDelta1ModMax, int theDelta2ModMax, int theDelta3ModMax)
	{
		myGameModMax=theGameModMax;
		myDelta1ModMax= theDelta1ModMax;
		myDelta2ModMax= theDelta2ModMax;
		myDelta3ModMax= theDelta3ModMax;

		my4ndDerChunk1IncSet = new HashSet<>();
		List<Integer> aList = new ArrayList<>();
		for (int i = 0; i < aDelta4Map.size(); i++)
		{
			int aNum = aDelta4Map.get(myDates.get(i)).get(0);
			my4ndDerChunk1IncSet.add(aNum);
			aList.add(aNum);
		}

		Set<Integer> aRemoveNums = new HashSet<>();

		Map<Integer, List<Integer>> aSameNumbersGapAll = Utils.getGapBetweenSameNumbersAll(aList);
		List<Integer> anAllGaps = aSameNumbersGapAll.values().stream().flatMap(List::stream).collect(Collectors.toList());
		Map<Integer, Integer> aMayBeMap = new HashMap<>();
		int aMaxSize = 0;
		for (Integer aKey : aSameNumbersGapAll.keySet())
		{
			aMayBeMap.put(aKey, aList.indexOf(aKey));
			aMaxSize = Math.max(aMaxSize, aSameNumbersGapAll.get(aKey).size());
		}
		int aLine = 0;
		do
		{
			for (Map.Entry<Integer, List<Integer>> aNumEntry : aSameNumbersGapAll.entrySet())
			{
				if (aLine < aNumEntry.getValue().size())
				{
					List<Integer> aIntegers = new ArrayList<>();
					aIntegers.add(aMayBeMap.get(aNumEntry.getKey()));
					aIntegers.add(aNumEntry.getValue().get(aLine));
					int anInd = Collections.indexOfSubList(anAllGaps, aIntegers);
					if (anInd > -1)
					{
						aRemoveNums.add(aNumEntry.getKey());
						if (aRemoveNums.size() >= De4Config.kToRemove)
						{
							break;
						}
					}
				}
			}
			aLine++;
		}
		while (aRemoveNums.size() < De4Config.kToRemove);

		De4Config.kDeniedRate = aRemoveNums.size() / (double) my4ndDerChunk1IncSet.size();
		my4ndDerChunk1IncSet.removeAll(aRemoveNums);

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
