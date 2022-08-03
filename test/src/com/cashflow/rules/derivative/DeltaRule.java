package com.cashflow.rules.derivative;

import com.cashflow.rules.AbstractRule;
import pb.Chunk;
import pb.Meters;
import pb.Utils;
import pb.file.FileReader;
import pb.statistics.ChunkStConfig;
import pb.statistics.ChunkStatistics;

import java.io.*;
import java.util.*;

/**
 * DeltaRule : excl:2674986 (23% off), incl:8563527, total:11238513
 *
 * + allTheSame trick:
 * DeltaRule : excl:2754653 (24% off), incl:8483860
 *
 * + doubleSame
 * DeltaRule : excl:2816798, incl:8421715
 *

 * @author Ruslan Dauhiala
 */
public class DeltaRule extends AbstractRule
{
	private static int k4DeltaGap = 40;

	public static List<Integer> getDeltaArray(List<Integer> theGame1, List<Integer> theGame2)
	{
		List<Integer> aList = new ArrayList<>();
		for (int i = 0; i < theGame1.size(); i++)
		{
			aList.add(theGame1.get(i) - theGame2.get(i));
		}
		return aList;
	}

	public enum kDirection {VERTICAL, HORIZONTAL};

	Map<Date, List<Integer>> myNotOrdered1;
	Map<Date, List<Integer>> myNotOrdered2;
	Map<Date, List<Integer>> myNotOrdered3;
	Map<Date, List<Integer>> myNotOrdered4;

	Set<List<Integer>> myNotOrdered1Set ;
	Set<List<Integer>> myNotOrdered2Set ;
	Set<List<Integer>> myNotOrdered3Set ;
	Set<Integer> myNotOrdered4Set;

	Map<Date, List<Integer>> myOrdered1;
	Map<Date, List<Integer>> myOrdered2;
	Map<Date, List<Integer>> myOrdered3;
//	Map<Date, List<Integer>> myOrdered4;

	Set<List<Integer>> myOrdered1Set ;
	Set<List<Integer>> myOrdered2Set ;
	Set<List<Integer>> myOrdered3Set ;
//	Set<List<Integer>> myOrdered4Set ;

	Set<List<Integer>> my1stDer3ChunkInOrderSet;
	Set<List<Integer>> my1stDer3ChunkNotInOrderSet;

	Set<List<Integer>> my1stDer2ChunkInOrderSet;

	Set<List<Integer>> my2ndDer3ChunkNotInOrderSet;
	Set<List<Integer>> my2ndDer2ChunkNotInOrderSet;

	Set<List<Integer>> my2ndDer2ChunkInOrderSet;

	Set<List<Integer>> my3ndDer2ChunkInOrderSet;

	public DeltaRule(Map<Date, List<Integer>> theGamesMap)
	{
		super(theGamesMap);
	}

	@Override
	public void setup()
	{
		int aGameInd = 0;
		myNotOrdered1 = getDeltaMap(aGameInd, aGameInd  + 252, myGamesMap);
		Map<Date, List<Integer>> aDelta500 = getDeltaMap(aGameInd, aGameInd  + 523, myGamesMap);
		myNotOrdered2 = getDeltaMap(0, 522, aDelta500);
		myNotOrdered3 = getDeltaMap(0, 201, myNotOrdered2);

		myNotOrdered4 = getDeltaMap(myGamesMap);
		myNotOrdered4 = getDeltaMap(myNotOrdered4);
		myNotOrdered4 = getDeltaMap(myNotOrdered4);
		myNotOrdered4 = getDeltaMap(myNotOrdered4);

//		myNotOrdered4 = getDeltaMap(0, 16, myNotOrdered3);

		myNotOrdered1Set = new HashSet<>(myNotOrdered1.values());
		myNotOrdered2Set = new HashSet<>(myNotOrdered2.values());
		myNotOrdered3Set = new HashSet<>(myNotOrdered3.values());

		myNotOrdered4Set = Utils.getIntSet(myNotOrdered4);
		for (int i = 0; i < k4DeltaGap; i++)
		{
			myNotOrdered4Set.remove(myNotOrdered4.get(myDates.get(i)).get(0));
		}

		myOrdered1 = getDeltaMap(aGameInd , myGamesMap.size(), myGamesMap);
		myOrdered2 = getDeltaMap(myOrdered1);
		myOrdered3 = getDeltaMap(0, 405, myOrdered2);
//		myOrdered4 = myNotOrdered4;

		myOrdered1Set = new HashSet<>(myOrdered1.values());
		myOrdered2Set = new HashSet<>(myOrdered2.values());
		myOrdered3Set = new HashSet<>(myOrdered3.values());

		List<List<Integer>> my1stDer3ChunkInOrder = Utils.getAllTheChunksTotal(-1,myOrdered1, Collections.singletonList(new ChunkStConfig(3, true, 300)));
		List<List<Integer>> my1stDer3ChunkNotInOrder = Utils.getAllTheChunksTotal(-1,myOrdered1, Collections.singletonList(new ChunkStConfig(3, false, 7)));

		List<List<Integer>> my1stDer2ChunkInOrder = Utils.getAllTheChunksTotal(-1,myOrdered1, Collections.singletonList(new ChunkStConfig(2, true, 7)));

		my1stDer3ChunkInOrderSet = new HashSet<>(my1stDer3ChunkInOrder);
		my1stDer3ChunkNotInOrderSet = new HashSet<>(my1stDer3ChunkNotInOrder);

		my1stDer2ChunkInOrderSet = new HashSet<>(my1stDer2ChunkInOrder);
//		myOrdered4Set = new HashSet<>(myOrdered4.values());

		List<List<Integer>> my2ndDer3ChunkNotInOrder = Utils.getAllTheChunksTotal(-1,myOrdered2, Collections.singletonList(new ChunkStConfig(3, false, 500)));
		List<List<Integer>> my2ndDer2ChunkNotInOrder = Utils.getAllTheChunksTotal(-1,myOrdered2, Collections.singletonList(new ChunkStConfig(2, false, 9)));
		my2ndDer3ChunkNotInOrderSet = new HashSet<>(my2ndDer3ChunkNotInOrder);
		my2ndDer2ChunkNotInOrderSet = new HashSet<>(my2ndDer2ChunkNotInOrder);

		List<List<Integer>> my2ndDer2ChunkInOrder = Utils.getAllTheChunksTotal(-1,myOrdered2, Collections.singletonList(new ChunkStConfig(2, true, 30)));
		my2ndDer2ChunkInOrderSet = new HashSet<>(my2ndDer2ChunkInOrder);

		List<List<Integer>> my3ndDer2ChunkInOrder = Utils.getAllTheChunksTotal(-1,myOrdered3, Collections.singletonList(new ChunkStConfig(2, true, 200)));
		my3ndDer2ChunkInOrderSet = new HashSet<>(my3ndDer2ChunkInOrder);

	}


	int myCount = 0;
	@Override
	public boolean accept(List<Integer> thePotentialGame)
	{
		List<Integer> aGameDelta1 = Utils.getGameHDelta(thePotentialGame);
		List<Integer> aGameDelta2 = Utils.getGameHDelta(aGameDelta1);
		List<Integer> aGameDelta3 = Utils.getGameHDelta(aGameDelta2);
		List<Integer> aGameDelta4 = Utils.getGameHDelta(aGameDelta3);

		//boolean aFound = findDerivative(aGameDelta4, myNotOrdered4Set, false);
		boolean aFound = !myNotOrdered4Set.contains(aGameDelta4.get(0));
		aFound = aFound || allTheSame(aGameDelta3) || findDerivative(aGameDelta3, myNotOrdered3Set, false);
		aFound = aFound || allTheSame(aGameDelta2) || doubleTheSame(aGameDelta2) || findDerivative(aGameDelta2, myNotOrdered2Set, false);
		aFound = aFound || allTheSame(aGameDelta1) || doubleTheSame(aGameDelta1) || findDerivative(aGameDelta1, myNotOrdered1Set, false);

		//aFound = aFound || findDerivative(aGameDelta4, myOrdered4, true);
		aFound = aFound || findDerivative(aGameDelta3, myOrdered3Set, true);
		aFound = aFound || findDerivative(aGameDelta2, myOrdered2Set, true);
		aFound = aFound || findDerivative(aGameDelta1, myOrdered1Set, true);

		if (!aFound)
		{
			List<Integer> aGame1stDer = Utils.getGameHDelta(thePotentialGame);
			List<List<Integer>> anOrderChunks = Utils.getArrayChunks(aGame1stDer, 3);

			for (int i = 0; i < anOrderChunks.size() && !aFound; i++ )
			{
				List<Integer> aChunk = anOrderChunks.get(i);
				aFound = my1stDer3ChunkInOrderSet.contains(aChunk);
				if (aFound)
				{
					myCount++;
				}
			}
			if (!aFound)
			{
				List<List<Integer>> anNotOrderChunks = Utils.getAllChunks(aGame1stDer, 3);
				for (int i = 0; i < anNotOrderChunks.size() && !aFound; i++)
				{
					List<Integer> aChunk = anNotOrderChunks.get(i);
					aFound = my1stDer3ChunkNotInOrderSet.contains(aChunk);
					if (aFound)
					{
						myCount++;
					}
				}
			}
			if (!aFound)
			{
				List<List<Integer>> anNotOrderChunks = Utils.getArrayChunks(aGame1stDer, 2);
				for (int i = 0; i < anNotOrderChunks.size() && !aFound; i++)
				{
					List<Integer> aChunk = anNotOrderChunks.get(i);
					aFound = my1stDer2ChunkInOrderSet.contains(aChunk);
					if (aFound)
					{
						myCount++;
					}
				}
			}

			// 2nd der 2 and 3 numbers not in order check
			if (!aFound)
			{
				List<Integer> aGame2ndDer = Utils.getGameHDelta(thePotentialGame);
				List<List<Integer>> anNotOrderChunks = Utils.getAllChunks(aGame2ndDer, 2);
				for (int i = 0; i < anNotOrderChunks.size() && !aFound; i++)
				{
					List<Integer> aChunk = anNotOrderChunks.get(i);
					aFound = my2ndDer2ChunkNotInOrderSet.contains(aChunk);
					if (aFound)
					{
						myCount++;
					}
				}
				if (!aFound)
				{
					anNotOrderChunks = Utils.getAllChunks(aGame2ndDer, 3);
					for (int i = 0; i < anNotOrderChunks.size() && !aFound; i++)
					{
						List<Integer> aChunk = anNotOrderChunks.get(i);
						aFound = my2ndDer3ChunkNotInOrderSet.contains(aChunk);
						if (aFound)
						{
							myCount++;
						}
					}
					if (!aFound)
					{
						anNotOrderChunks = Utils.getArrayChunks(aGame2ndDer, 2);
						for (int i = 0; i < anNotOrderChunks.size() && !aFound; i++)
						{
							List<Integer> aChunk = anNotOrderChunks.get(i);
							aFound = my2ndDer2ChunkInOrderSet.contains(aChunk);
							if (aFound)
							{
								myCount++;
							}
						}
						if (!aFound)
						{
							List<Integer> aGame3rdDer = Utils.getGameHDelta(aGame2ndDer);
							anNotOrderChunks = Utils.getAllChunks(aGame3rdDer, 2);
							for (int i = 0; i < anNotOrderChunks.size() && !aFound; i++)
							{
								List<Integer> aChunk = anNotOrderChunks.get(i);
								aFound = my3ndDer2ChunkInOrderSet.contains(aChunk);
								if (aFound)
								{
									myCount++;
								}
							}
						}

					}
				}
			}

		}

		trackResult(!aFound);
		return !aFound;
	}

//	@Override
//	public void printReport()
//	{
//		super.printReport();
//		System.out.println(" == new chunk method count : " + myCount);
//	}

	private boolean allTheSame(List<Integer> theGameDelta)
	{
		int aNum = theGameDelta.get(0);
		int aFind = 0;
		for (int i = 1; i < theGameDelta.size(); i ++)
		{
			if (aNum == theGameDelta.get(i))
			{
				aFind++;
			}
			if (aFind == theGameDelta.size() - 1)
			{
				return true;
			}
		}
		return false;
	}

	private boolean doubleTheSame(List<Integer> theGameDelta)
	{
		return (new HashSet(theGameDelta)).size() <= theGameDelta.size() - 2 ;
	}

	public static Map<Date, List<Integer>> getVerticalDelta(Map<Date, List<Integer>> theMap)
	{
		return getVerticalDelta(theMap, 1);
	}

	public static Map<Date, List<Integer>> getVerticalDelta(Map<Date, List<Integer>> theMap, int theGap)
	{
		Map<Date, List<Integer>> aRes = new LinkedHashMap<>();
		List<Date> aDates = new ArrayList<>(theMap.keySet());
		for (int i = 0; i < theMap.size()-theGap; i++)
		{
			Date aDate = aDates.get(i);
			Date aPrevDate = aDates.get(i+theGap);
			List<Integer> aGame = theMap.get(aDate);
			List<Integer> aPrevGame = theMap.get(aPrevDate);
			if (5 < aGame.size())
			{
				aGame = aGame.subList(0,5);
				aPrevGame = aPrevGame.subList(0,5);
			}
			List<Integer> aDeltaGame = new ArrayList<>();
			for (int j = 0; j < aGame.size(); j++)
			{
				aDeltaGame.add(aGame.get(j)-aPrevGame.get(j));
			}
			aRes.put(aDate, aDeltaGame);
		}
		return aRes;
	}

	public static void getFullDerivativeStat(Map<Date, List<Integer>> theFun)
	{
		getFullDerivativeStat(theFun, kDirection.HORIZONTAL);
	}

	public static Map<Date, List<Integer>> getDeltaWithDirection(Map<Date, List<Integer>> theGames, kDirection theDirection)
	{
		return theDirection.equals(kDirection.HORIZONTAL) ? getDeltaMap(theGames) : getVerticalDelta(theGames);
	}
	public static void getFullDerivativeStat(Map<Date, List<Integer>> theFun, kDirection theDirection)
	{
		//Map<Date, List<Integer>> myAllTheFun = new FileReader().loadFromFile("/Users/jet/Documents/pb/pb.txt");
		Map<Date, List<Integer>> aDelta = theFun;//getDeltaWithDirection(theFun, theDirection);
//		System.out.println("Derivative 'x1 statistics: ");
//		getStatistics(aDelta);
//		System.out.println("= = = = = = = = = = = = = = \n");
//
//		aDelta = getDeltaWithDirection(aDelta, theDirection);
//		System.out.println("Derivative 'x2 statistics: ");
//		getStatistics(aDelta);
//		System.out.println("= = = = = = = = = = = = = = \n");
//
//		aDelta = getDeltaWithDirection(aDelta, theDirection);
//		System.out.println("Derivative 'x3 statistics: ");
//		getStatistics(aDelta);
//		System.out.println("= = = = = = = = = = = = = = \n");
//
//		aDelta = getDeltaWithDirection(aDelta, theDirection);
//		System.out.println("Derivative 'x4 statistics: ");
//		getStatistics(aDelta);
//		System.out.println("= = = = = = = = = = = = = = \n");

		for (int i= 1; i < 5 && aDelta.values().iterator().next().size() > 1; i++)
		{
			aDelta = getDeltaWithDirection(aDelta, theDirection);
			System.out.println("Derivative 'x" + i + " statistics: ");
			getStatistics(aDelta);
			System.out.println("= = = = = = = = = = = = = = \n");
		}


	}

	private static void runDerivative() throws IOException
	{
		int aGameInd = 3;
		Map<Date, List<Integer>> myAllTheFun = new FileReader().loadFromFile("/Users/jet/Documents/pb/pb.txt");
//		boolean kInOrder = true;
//		Map<Date, List<Integer>> aDelta = getDeltaMap(aGameInd+1, myAllTheFun.size(), myAllTheFun);
//		Map<Date, List<Integer>> aDelta2 = getDeltaMap(aDelta);
//		Map<Date, List<Integer>> aDelta3 = getDeltaMap(0, 405, aDelta2);
//		Map<Date, List<Integer>> aDelta4 = getDeltaMap(0, 16, aDelta3);
		boolean inOrder = false;
		Map<Date, List<Integer>> aDelta = getDeltaMap(aGameInd+1, aGameInd + 1 + 252, myAllTheFun);
		Map<Date, List<Integer>> aDelta22 = getDeltaMap(aGameInd+1, aGameInd + 1 + 523, myAllTheFun);
		Map<Date, List<Integer>> aDelta2 = getDeltaMap(0, 522, aDelta22);
		Map<Date, List<Integer>> aDelta3 = getDeltaMap(0, 201, aDelta2);
		Map<Date, List<Integer>> aDelta4 = getDeltaMap(0, 16, aDelta3);

		try (BufferedWriter aBufferedWriter = new BufferedWriter(new FileWriter(new File("/Users/jet/Documents/pb/07-30-16-derivatives-applied-not-in-order.txt"))))
		{
			File aSource = new File("/Users/jet/Documents/pb/07-30-16-derivatives-applied.txt");
			int aTotal = 0;
			try (BufferedReader br = new BufferedReader(new java.io.FileReader(aSource)))
			{
				String aLine;
				while ((aLine = br.readLine()) != null)
				{
					if ("".equals(aLine))
					{
						continue;
					}
					String[] aNums = aLine.split("  ");
					List<Integer> aGame = new ArrayList<>();
					for (String aNum : aNums)
					{
						aGame.add(Integer.parseInt(aNum.trim()));
					}
					List<Integer> aGameDelta = Utils.getGameHDelta(aGame);
					List<Integer> aGameDelta2 = Utils.getGameHDelta(aGameDelta);
					List<Integer> aGameDelta3 = Utils.getGameHDelta(aGameDelta2);
					List<Integer> aGameDelta4 = Utils.getGameHDelta(aGameDelta3);

					Set<List<Integer>> aDelta1Set = new HashSet<>(aDelta.values());
					Set<List<Integer>> aDelta2Set = new HashSet<>(aDelta.values());
					Set<List<Integer>> aDelta3Set = new HashSet<>(aDelta.values());
					Set<List<Integer>> aDelta4Set = new HashSet<>(aDelta.values());

					boolean aFound = findDerivative(aGameDelta4, aDelta4Set, inOrder);
					aFound = aFound || findDerivative(aGameDelta3, aDelta3Set, inOrder);
					aFound = aFound || findDerivative(aGameDelta2, aDelta2Set, inOrder);
					aFound = aFound || findDerivative(aGameDelta, aDelta1Set, inOrder);

					if (!aFound)
					{
						aTotal ++;
						String aStr = aGame.toString();
						aBufferedWriter.write(aStr.substring(1, aStr.length() - 1).replace(",", " "));
						aBufferedWriter.newLine();
					}
				}
			}

			aBufferedWriter.flush();

			System.out.println("a total number of all variants: " + aTotal + "; skipped : " + mySkip);
		}
	}

	static int mySkip = 0;

	private static boolean findDerivative(List<Integer> theGameDelta, Collection<List<Integer>> theDeltaMap, boolean theInOrder)
	{
		Chunk aGameChunk = new Chunk();
		aGameChunk.addAll(theGameDelta);
		if (theInOrder )
		{
			boolean aB =  theDeltaMap.contains(theGameDelta);
		   if (aB)
			{
				mySkip++;
			}
			return aB;
		}

		for (List<Integer> aNoWayDelta : theDeltaMap)
		{
			if (
					  //theInOrder ?
					  //Utils.containsAllInOrder(aGameChunk, aNoWayDelta, false) :
					  theGameDelta.containsAll(aNoWayDelta) && aNoWayDelta.containsAll(theGameDelta)
					  )
			{
				mySkip++;
				return true;
			}
		}

		return false;
	}

	private static void runTheFun() throws IOException
	{
		Map<Date, List<Integer>> myAllTheFun = new FileReader().loadFromFile("/Users/jet/Documents/pb/pb.txt");

		Map<Date, List<Integer>> aDelta = getDeltaMap(myAllTheFun);

		try (BufferedWriter aBufferedWriter = new BufferedWriter(new FileWriter(new File("/Users/jet/Documents/pb/08-06-2016-delta.txt"))))
		{
			File aSource = new File("/Users/jet/Documents/pb/08-06-2016.txt");
			int aTotal = 0;
			int aSkip = 0;
			try (BufferedReader br = new BufferedReader(new java.io.FileReader(aSource)))
			{
				String aLine;
				while ((aLine = br.readLine()) != null)
				{
					if ("".equals(aLine))
					{
						continue;
					}
					String[] aNums = aLine.split("  ");
					List<Integer> aGame = new ArrayList<>();
					for (String aNum : aNums)
					{
						aGame.add(Integer.parseInt(aNum.trim()));
					}
					List<Integer> aGameDelta = Utils.getGameHDelta(aGame);
					Chunk aGameChunk = new Chunk();
					aGameChunk.addAll(aGameDelta);
					boolean aFound = false;
					for (List<Integer> aNoWayDelta : aDelta.values())
					{
						if (
								  Utils.containsAllInOrder(aGameChunk, aNoWayDelta, true)
								  //aGameDelta.containsAll(aNoWayDelta) && aNoWayDelta.containsAll(aGameDelta)
								  )
						{
							aFound = true;
							aSkip++;
							break;
						}
					}
					if (!aFound)
					{
						aTotal ++;
						String aStr = aGame.toString();
						aBufferedWriter.write(aStr.substring(1, aStr.length() - 1).replace(",", " "));
						aBufferedWriter.newLine();
					}
				}
			}

			aBufferedWriter.flush();

			System.out.println("a total number of all variants: " + aTotal + "; skipped : " + aSkip);
		}
	}

	public static void getStatistics(Map<Date, List<Integer>> aDelta)
	{
//		Map<Date, List<Integer>> myAllTheFun = new FileReader().loadFromFile("/Users/jet/Documents/pb/pb.txt");
//
//		Map<Date, List<Integer>> aDelta = getDeltaMap(myAllTheFun);
		List<Date> aDates = new ArrayList<>(aDelta.keySet());
		int aTotalOrNum = 0;
		int aTotalNotOrNum = 0;
		StringBuilder anOrderedBuf = new StringBuilder();
		StringBuilder aNOtOrderedBuf = new StringBuilder();
		for (int i = 0; i < aDates.size(); i++)
		{
			List<Integer> aGame = aDelta.get(aDates.get(i));
			for (int j = i+1; j < aDates.size()-2; j++)
			{
				Chunk aChunk = new Chunk();
				aChunk.addAll(aGame);
				if (Utils.containsAllInOrder(aChunk, aDelta.get(aDates.get(j)), false))
				{
					aTotalOrNum++;
					anOrderedBuf.append(Meters.kOutDateFormat.format(aDates.get(i)) + " / " +
							  Meters.kOutDateFormat.format(aDates.get(j)) + " matched deltas in ORDER!!!" +  aChunk +"\n");
				} else if (aGame.containsAll(aDelta.get(aDates.get(j))) &&
						  aDelta.get(aDates.get(j)).containsAll(aGame))
				{

					aTotalNotOrNum++;
					aNOtOrderedBuf.append(Meters.kOutDateFormat.format(aDates.get(i)) + " / " +
							  Meters.kOutDateFormat.format(aDates.get(j)) + " matched deltas NOT in order" + aChunk + "\n");
				}
			}
		}
		System.out.println("total: " + (aTotalOrNum + aTotalNotOrNum));
		System.out.println("Ordered:    \t total: " + aTotalOrNum
//				   + "\n" + anOrderedBuf.toString()
				   );
		System.out.println("Not ordered:\t total: " + aTotalNotOrNum
//				   + "\n" + aNOtOrderedBuf.toString()
				   );
		//String aStr = Utils.getChunkStatistic(1, getDeltaMap(myAllTheFun), 4);
//		System.out.print("Delta games statistics: \n" + aStr);
	}

	public static Map<Date, List<Integer>> getDeltaMap(Map<Date, List<Integer>> theGames)
	{
		return getDeltaMap(0, theGames.size(), theGames);
	}

	public static boolean kMode =
			  true;
			  //false;

	public static int getMaxMode(Map<Date, List<Integer>> theGames)
	{
		int aMax = 0;
		Set<Integer> aSet = new HashSet<>();
		for (List<Integer> aList: theGames.values())
		{
			aSet.addAll(aList);
		}
		aMax = Collections.max(aSet);
		if (Collections.min(aSet) <= 0)
		{
			aMax++;
		}
		return aMax;

	}
	public static Map<Date, List<Integer>> getDeltaMap(int theStart, int theEnd, Map<Date, List<Integer>> theGames)
	{
		Map<Date, List<Integer>> aRes = new LinkedHashMap<>();
		List<Date> aGames = new ArrayList<>(theGames.keySet());
		int aTrunk = 5 < theGames.get(aGames.get(0)).size() ? 5 : theGames.get(aGames.get(0)).size();

		int aMax = -1;
		if (kMode)
		{
			aMax = getMaxMode(theGames);
		}
		for (int i = theStart; i < theEnd; i++)
		{
			List<Integer> aGameNums = theGames.get(aGames.get(i)).subList(0, aTrunk);
			if (kMode)
			{
				aRes.put(aGames.get(i), Utils.getGameModDelta(aGameNums, aMax));
			} else
			{
				aRes.put(aGames.get(i), Utils.getGameHDelta(aGameNums));
			}
		}
		return aRes;
	}

	private void testSameNumbersInDerivative()
	{
		findSameNum(myOrdered1);
		findSameNum(myOrdered2);
		findSameNum(myOrdered3);
	}

	/**
	 *  Wed Aug 05 2009=[-2, -2, -2]
	 *  Sat Dec 10 2005=[-5, -5, -5]
	 *
	 *   Wed Apr 29 2015=[13, 13]
	 *   Sat Mar 07 2015=[2, 2]
	 *   Sat Dec 20 2014=[5, 5]
	 *   Sat Jun 22 2013=[8, 8]
	 */
	public static void findSameNum(Map<Date, List<Integer>> theOrdered1)
	{
		int aTot = 0;
		for (Map.Entry<Date, List<Integer>> aEntry : theOrdered1.entrySet())
		{
			//if (allTheSame(aEntry.getValue()))
			//new HashSet(aEntry.getValue()) ;
			if ((new HashSet(aEntry.getValue())).size() <= aEntry.getValue().size() - 2 )
			{
				aTot++;
				System.out.println("! same num found: " + aEntry);
			}
		}
		System.out.println("==== total: " + aTot + "\n");
	}


	private void trueTest()
	{
		Map<Date, List<Integer>> aDelta1Map = getDeltaMap(myGamesMap);
		Map<Date, List<Integer>> aDelta2Map = getDeltaMap(aDelta1Map);
		Map<Date, List<Integer>> aDelta3Map = getDeltaMap(aDelta2Map);
		Map<Date, List<Integer>> aDelta4Map = getDeltaMap(aDelta3Map);

		System.out.println("===  H delta`1 stat ===");
		ChunkStatistics.getAllChunkStats(aDelta1Map);
		System.out.println("========================\n\n");

		System.out.println("===  H delta`2 stat ===");
		ChunkStatistics.getAllChunkStats(aDelta2Map);
		System.out.println("========================\n\n");

		System.out.println("===  H delta`3 stat ===");
		ChunkStatistics.getAllChunkStats(aDelta3Map);
		System.out.println("========================\n\n");

		System.out.println("===  H delta`4 stat ===");
		ChunkStatistics.getAllChunkStats(aDelta4Map);
		System.out.println("========================\n\n");

	}

	public static void main(String[] args) throws IOException
	{

		Map<Date, List<Integer>> myAllTheFun = new FileReader().loadFromFile("/Users/jet/Documents/pb/pb.txt");
		//getFullDerivativeStat(myAllTheFun);

		//new DeltaRule(myAllTheFun).testSameNumbersInDerivative();
		new DeltaRule(myAllTheFun).trueTest();

		//getStatistics(aDelta);
		//runTheFun();
		//runDerivative();
	}

}
