package com.cashflow.rules;

import com.cashflow.rules.cofig.IConfig;
import com.cashflow.rules.vertical.DefaultConfig;
import pb.Utils;
import pb.statistics.ChunkStConfig;

import java.util.*;

/**
 * @author Ruslan Dauhiala
 */
public abstract class AbstractRule implements Rule
{
	protected Map<Date, List<Integer>> myGamesMap;
	protected List<Date> myDates;

	protected int myCount = 0;

	private int myAccepted;
	private int myDenied;

	protected Map<Date, List<Integer>> myMMGamesMap;

	public AbstractRule(Map<Date, List<Integer>> theGamesMap)
	{
		this(theGamesMap, null);
	}

	public AbstractRule(Map<Date, List<Integer>> theGamesMap, Map<Date, List<Integer>> theMMMap2)
	{
		//super(theGamesMap);
		myGamesMap = theGamesMap;
		myDates = new ArrayList<>(myGamesMap.keySet());
//		if (theMMMap2 != null)
//		{
//			myMMGamesMap = Utils.replaceDates(myDates, theMMMap2);
//		}
		myMMGamesMap = theMMMap2;
		setup();

	}

	protected void trackResult(boolean theAccepted)
	{
		if (theAccepted)
		{
			myAccepted++;
		} else
		{
			myDenied++;
		}
	}

	public void printReport()
	{
		{
			System.out.println("= " + getClass().getSimpleName() + " =\n" +
					  "    total: " + (myDenied + myAccepted) + "       excl:" + myDenied + "   INCL: " + myAccepted );
		}
	}



	protected boolean gameChunkCheckO(Set<List<Double>> theGameChunk, Set<List<Double>> theExistingChunk)
	{
		for (List aChunk : theGameChunk)
		{
			if (theExistingChunk.contains(aChunk))
			{
				myCount++;
				return true;
			}
		}
		return false;
	}

	protected <T> boolean gameChunkCheck(List<List<T>> theGameChunk, Set<List<T>> theExistingChunk)
	{
		for (List<T> aChunk : theGameChunk)
		{
			if (theExistingChunk.contains(aChunk))
			{
				myCount++;
				return true;
			}
		}
		return false;
	}

//	protected Set<List<Double>> getChunkSetO(Map theHDeltaMap2, ChunkStConfig theChunkStConfigs)
//	{
//		Set<List<Double>> aList = Utils.getAllTheChunksTotalO(theHDeltaMap2, Collections.singletonList(theChunkStConfigs));
//		return aList;
//	}

	protected <T> Set<List<T>> getChunkSetO(Map<Date, List<T>>  theHDeltaMap2, ChunkStConfig theChunkStConfigs)
	{
		Set<List<T>> aList = Utils.getAllTheChunksTotalO(theHDeltaMap2, Collections.singletonList(theChunkStConfigs));
		return aList;
	}

	protected <T> Set<List<T>> getChunkSet(Map<Date, List<T>> theHDeltaMap2, ChunkStConfig theChunkStConfigs)
	{
		List<List<T>> aList = Utils.getAllTheChunksTotal(-1, theHDeltaMap2, Collections.singletonList(theChunkStConfigs));
		return new HashSet<>(aList);
	}

	protected <T> Set<List<T>> getChunkSet(Map<Date, List<T>> theHDeltaMap2, int theStart, ChunkStConfig theChunkStConfigs)
	{
		List<List<T>> aList = Utils.getAllTheChunksTotal(theStart-1, theHDeltaMap2, Collections.singletonList(theChunkStConfigs));
		return new HashSet<>(aList);
	}

	public IConfig getRuleConfig()
	{
		return new DefaultConfig();
	}
}
