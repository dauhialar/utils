package com.cashflow.rules.vertical;

import com.cashflow.rules.AbstractRule;
import com.cashflow.rules.derivative.DeltaRule;
import pb.Chunk;
import pb.Meters;
import pb.Utils;
import pb.file.FileReader;
import pb.statistics.ChunkStConfig;
import pb.statistics.ChunkStatistics;

import java.io.IOException;
import java.util.*;

/**
 *
 * @author Ruslan Dauhiala

 
 
 */
public class GameAgoOneByOneRule extends AbstractRule
{
	Map<Date, List<Integer>> myGameAgoMap;
	Set<List<Integer>> myGameIndSet;

	Set<List<Integer>> myGameChunk4O;
	Set<List<Integer>> myGameChunk3O;

	Set<List<Integer>> myGameChunk4NotO;

	Set<List<Integer>> myDer1Chunk4NotO;
	Set<List<Integer>> myDer1Chunk3NotO;

	Set<List<Integer>> myDer1Chunk3O;
	Set<List<Integer>> myDer1Chunk2O;

	Set<List<Integer>> myDer2Chunk3NotO;
	Set<List<Integer>> myDer2Chunk2NotO;

	Set<List<Integer>> myDer2Chunk3O;
	Set<List<Integer>> myDer2Chunk2O;

	Set<List<Integer>> myDer3Chunk2NotO;
	//Set<List<Integer>> myDer3Chunk2O;


	Set<Integer> myDer4Set;

	public GameAgoOneByOneRule(Map<Date, List<Integer>> theGamesMap)
	{
		super(theGamesMap);
	}

	@Override
	public void setup()
	{
		myGameAgoMap = getGameAgoMap();
		myGameIndSet = new HashSet<>(myGameAgoMap.values());
		Map<Date, List<Integer>> aDelta1Map = DeltaRule.getDeltaMap(myGameAgoMap);
		Map<Date, List<Integer>> aDelta2Map = DeltaRule.getDeltaMap(aDelta1Map);
		Map<Date, List<Integer>> aDelta3Map = DeltaRule.getDeltaMap(aDelta2Map);
		Map<Date, List<Integer>> aDelta4Map = DeltaRule.getDeltaMap(aDelta3Map);


		myGameChunk4O		= getChunkSet(myGameAgoMap, new ChunkStConfig(4, true, myGameAgoMap.size())); 	// [131, 1129, 1992,
		myGameChunk3O		= getChunkSet(myGameAgoMap, new ChunkStConfig(3, true, 5));								// [1, 7, 36, 46, 54,

		myDer1Chunk4NotO	= getChunkSet(aDelta1Map, new ChunkStConfig(4, false, myGameAgoMap.size()));     // [1992, 1992, 1992
		myDer1Chunk3NotO	= getChunkSet(aDelta1Map, new ChunkStConfig(3, false, 15));                      // [17, 59, 80, 131, 171, 396, 442, 477, 511,

		myDer1Chunk3O		= getChunkSet(aDelta1Map, new ChunkStConfig(3, true, 60));                       // [59, 80, 131, 511, 864, 1
		myDer1Chunk2O		= getChunkSet(aDelta1Map, new ChunkStConfig(2, true, 5));                        // [1, 7, 7, 12, 14, 19,

		//pilot
//		List<Integer> a2ChunkList = ChunkStatistics.getChunkAgoList(new ChunkStConfig(2, true), aDelta1Map, null);
//		for (int i = 0 ; i < 3; i++)   // 3 or 5 +1err 17ind .2M;
//		{
//			if (-1 < a2ChunkList.get(i) && a2ChunkList.get(i) < myDates.size() )
//			{
//				List<Integer> aGame = aDelta1Map.get(myDates.get(a2ChunkList.get(i)));
//				if (aGame != null)
//				{
//					myDer1Chunk2O.addAll(Utils.getArrayChunks(aGame, 2));
//				}
//			}
//		}

		myDer2Chunk3NotO	= getChunkSet(aDelta2Map, new ChunkStConfig(3, false, aDelta2Map.size()));        // [1992, 1992, 1992, 1992,
		myDer2Chunk2NotO	= getChunkSet(aDelta2Map, new ChunkStConfig(2, false, 10));                       // [7, 9, 19, 36, 44, 45,

		myDer2Chunk3O		= getChunkSet(aDelta2Map, new ChunkStConfig(3, true, myGameAgoMap.size()));
		myDer2Chunk2O		= getChunkSet(aDelta2Map, new ChunkStConfig(2, true, 5));                        // [7, 9, 45, 59, 63, 74,

		myDer3Chunk2NotO	= getChunkSet(aDelta3Map, new ChunkStConfig(2, false, 100));                     // [107, 240, 348, 557, 594
		//myDer3Chunk2O		= getChunkSet(aDelta3Map, new ChunkStConfig(2, true, myGameAgoMap.size()));

		myDer4Set = new HashSet<>();
		int aGap = 5;
		for (int i = aGap; i < aDelta4Map.size(); i++)
		{
			myDer4Set.add(aDelta4Map.get(myDates.get(i)).get(0));
		}
		for (int i = 0; i < aGap; i++)
		{
			myDer4Set.remove(aDelta4Map.get(myDates.get(i)).get(0));
		}

		//pilot
//		List<Integer> aGapList = Utils.getGapBetweenSameNumbers(aDelta4Map.values());
//		for (int i = 0; i < 5; i++)  // 5= 1err ind 10 +0.1M;  10= + .2M 1err ind 10   ;
//		{
//			if (-1 < aGapList.get(i) && aGapList.get(i) < myDates.size())
//			{
//				myDer4Set.remove(aDelta4Map.get(myDates.get(aGapList.get(i))).get(0));
//			}
//		}

		//myDer4Set.add(myDer4Set.size());
	}

	@Override
	public boolean accept(List<Integer> thePotentialGame)
	{
		List<Integer> aGameAgoList = getGameIndexes(thePotentialGame, 0);
		List<Integer> aDer1 = Utils.getGameHDelta(aGameAgoList);
		List<Integer> aDer2 = Utils.getGameHDelta(aDer1);
		List<Integer> aDer3 = Utils.getGameHDelta(aDer2);
		List<Integer> aDer4 = Utils.getGameHDelta(aDer3);

		boolean aFound = false;//myGameIndSet.contains(aGameAgoList);
		aFound = aFound || !myDer4Set.contains(aDer4.get(0));
		aFound = aFound || gameChunkCheck(Utils.getAllChunks(aDer1, 4), myDer1Chunk4NotO);

		aFound = aFound || gameChunkCheck(Utils.getArrayChunks(aGameAgoList, 4), myGameChunk4O);
		aFound = aFound || gameChunkCheck(Utils.getArrayChunks(aGameAgoList, 3), myGameChunk3O);

		aFound = aFound || gameChunkCheck(Utils.getAllChunks(aDer1, 4), myDer1Chunk4NotO);
		aFound = aFound || gameChunkCheck(Utils.getAllChunks(aDer1, 3), myDer1Chunk3NotO);
		aFound = aFound || gameChunkCheck(Utils.getArrayChunks(aDer1, 3), myDer1Chunk3O);
		aFound = aFound || gameChunkCheck(Utils.getArrayChunks(aDer1, 2), myDer1Chunk2O);


		aFound = aFound || gameChunkCheck(Utils.getAllChunks(aDer2, 3), myDer2Chunk3NotO);
		aFound = aFound || gameChunkCheck(Utils.getAllChunks(aDer2, 2), myDer2Chunk3NotO);

		aFound = aFound || gameChunkCheck(Utils.getArrayChunks(aDer2, 3), myDer2Chunk3O);
		aFound = aFound || gameChunkCheck(Utils.getArrayChunks(aDer2, 2), myDer2Chunk2O);

		aFound = aFound || gameChunkCheck(Utils.getAllChunks(aDer3, 2), myDer3Chunk2NotO);

		trackResult(!aFound);
		return !aFound;
	}


	private Map<Date, List<Integer>> getGameAgoMap()
	{
		Map<Date, List<Integer>> aMap = new LinkedHashMap<>();
		for (int i = 0; i < myGamesMap.size() - 10; i++)
		{
			Date aDate = myDates.get(i);
			List<Integer> aGame = myGamesMap.get(aDate).subList(0, 5); 		//1.5M   56sec
			List<Integer> anIndexList = getGameIndexes(aGame, i+1); 			//150k   70sec
		//	Date aDate = myDates.get(i);

			//Collections.sort(anIndexList);
			aMap.put(aDate, anIndexList);
		}
		return aMap;
	}

	private List<Integer> getGameIndexes(List<Integer> aGame, int theFirstInd)
	{
		List<Integer> anIndexList = new ArrayList<>();
		for (int ind = 0; ind < aGame.size(); ind++ )
		{
			Integer aNumber = aGame.get(ind);
			for (int j = theFirstInd; j < myGamesMap.size() ; j++)
			{
				if (myGamesMap.get(myDates.get(j)).subList(0,5).contains(aNumber))
				{
					//aLastInd = j;
					anIndexList.add(j - theFirstInd);
					break;
				}
			}
		}
		while (anIndexList.size() <5)
		{
			anIndexList.add(myGamesMap.size());
		}
		return anIndexList;
	}

	public static void main(String[] args) throws IOException
	{
		Map<Date, List<Integer>> myAllTheFun = new FileReader().loadFromFile("/Users/jet/Documents/pb/pb.txt");
		new GameAgoOneByOneRule(myAllTheFun).trueTest();
	}

	private void trueTest()
	{
		int anInd = 0;
		Map<Date, List<Integer>> aDelta1Map = DeltaRule.getDeltaMap(myGameAgoMap);
		Map<Date, List<Integer>> aDelta2Map = DeltaRule.getDeltaMap(aDelta1Map);
		Map<Date, List<Integer>> aDelta3Map = DeltaRule.getDeltaMap(aDelta2Map);
		Map<Date, List<Integer>> aDelta4Map = DeltaRule.getDeltaMap(aDelta3Map);

		for (Map.Entry<Date, List<Integer>> aEntry : myGameAgoMap.entrySet())
		{
			System.out.printf("%1$4d" + "\t\t" + Meters.kOutDateFormat.format(aEntry.getKey()) + "\t", ++anInd);
			Utils.printf(myGamesMap.get(aEntry.getKey()).subList(0, 5), 2, "_", "\t | \t");
			Utils.printf(aEntry.getValue(), 4, ".", "\t | \t");
			Utils.printf(aDelta1Map.get(aEntry.getKey()), 4, "'", "\t | \t");
			Utils.printf(aDelta2Map.get(aEntry.getKey()), 4, "''", "\t | \t");
			Utils.printf(aDelta3Map.get(aEntry.getKey()), 4, "'''", "\t | \t");
			Utils.printf(aDelta4Map.get(aEntry.getKey()), 4, "`", "\n");
		}

		System.out.println("\n\n ||||||||||  MAP  ||||||||||");
		ChunkStatistics.getAllChunkStats(myGameAgoMap);

		System.out.println("\n\n ||||||||||  Der 1  ||||||||||");
		ChunkStatistics.getAllChunkStats(aDelta1Map);
		System.out.println("\n\n ||||||||||  Der 2  ||||||||||");
		ChunkStatistics.getAllChunkStats(aDelta2Map);
		System.out.println("\n\n ||||||||||  Der 3  ||||||||||");
		ChunkStatistics.getAllChunkStats(aDelta3Map);
		System.out.println("\n\n ||||||||||  Der 4  ||||||||||");
		ChunkStatistics.getAllChunkStats(aDelta4Map);

	}
}


/*



★ ★ ★  0 	 01/04/17.Wed = [16, 17, 29, 41, 42] =
 >>> BOOOOO <<< : GameAgoOneByOneRule
= GameAgoOneByOneRule =
    total: 11238513       excl:2139080   INCL: 9099433
 time: 90 sec.     [TOTAL] excl: 2139080, INCL: 9099433

===============



★ ★ ★  1 	 12/31/16.Sat = [1, 3, 28, 57, 67] =
= GameAgoOneByOneRule =
    total: 11238513       excl:2134764   INCL: 9103749
 time: 86 sec.     [TOTAL] excl: 2134764, INCL: 9103749

===============



★ ★ ★  2 	 12/28/16.Wed = [16, 23, 30, 44, 58] =
= GameAgoOneByOneRule =
    total: 11238513       excl:2154013   INCL: 9084500
 time: 86 sec.     [TOTAL] excl: 2154013, INCL: 9084500

===============



★ ★ ★  3 	 12/24/16.Sat = [28, 38, 42, 51, 52] =
= GameAgoOneByOneRule =
    total: 11238513       excl:1957020   INCL: 9281493
 time: 84 sec.     [TOTAL] excl: 1957020, INCL: 9281493

===============



★ ★ ★  4 	 12/21/16.Wed = [25, 33, 40, 54, 68] =
= GameAgoOneByOneRule =
    total: 11238513       excl:1901967   INCL: 9336546
 time: 85 sec.     [TOTAL] excl: 1901967, INCL: 9336546

===============



★ ★ ★  5 	 12/17/16.Sat = [1, 8, 16, 40, 48] =
= GameAgoOneByOneRule =
    total: 11238513       excl:2032592   INCL: 9205921
 time: 80 sec.     [TOTAL] excl: 2032592, INCL: 9205921

===============



★ ★ ★  6 	 12/14/16.Wed = [18, 26, 37, 39, 66] =
= GameAgoOneByOneRule =
    total: 11238513       excl:1992735   INCL: 9245778
 time: 80 sec.     [TOTAL] excl: 1992735, INCL: 9245778

===============



★ ★ ★  7 	 12/10/16.Sat = [12, 21, 32, 44, 66] =
= GameAgoOneByOneRule =
    total: 11238513       excl:1978565   INCL: 9259948
 time: 83 sec.     [TOTAL] excl: 1978565, INCL: 9259948

===============



★ ★ ★  8 	 12/07/16.Wed = [41, 48, 49, 53, 64] =
= GameAgoOneByOneRule =
    total: 11238513       excl:1807940   INCL: 9430573
 time: 83 sec.     [TOTAL] excl: 1807940, INCL: 9430573

===============



★ ★ ★  9 	 12/03/16.Sat = [8, 10, 26, 27, 33] =
= GameAgoOneByOneRule =
    total: 11238513       excl:2166195   INCL: 9072318
 time: 84 sec.     [TOTAL] excl: 2166195, INCL: 9072318

===============



★ ★ ★  10 	 11/30/16.Wed = [3, 14, 18, 25, 45] =
= GameAgoOneByOneRule =
    total: 11238513       excl:2747770   INCL: 8490743
 time: 84 sec.     [TOTAL] excl: 2747770, INCL: 8490743

===============



★ ★ ★  11 	 11/26/16.Sat = [17, 19, 21, 37, 44] =
= GameAgoOneByOneRule =
    total: 11238513       excl:2616704   INCL: 8621809
 time: 80 sec.     [TOTAL] excl: 2616704, INCL: 8621809

===============



★ ★ ★  12 	 11/23/16.Wed = [7, 32, 41, 47, 61] =
 >>> BOOOOO <<< : GameAgoOneByOneRule
= GameAgoOneByOneRule =
    total: 11238513       excl:2652821   INCL: 8585692
 time: 80 sec.     [TOTAL] excl: 2652821, INCL: 8585692

===============



★ ★ ★  13 	 11/19/16.Sat = [16, 24, 28, 43, 61] =
= GameAgoOneByOneRule =
    total: 11238513       excl:2674633   INCL: 8563880
 time: 79 sec.     [TOTAL] excl: 2674633, INCL: 8563880

===============



★ ★ ★  14 	 11/16/16.Wed = [28, 41, 61, 63, 65] =
= GameAgoOneByOneRule =
    total: 11238513       excl:2680895   INCL: 8557618
 time: 80 sec.     [TOTAL] excl: 2680895, INCL: 8557618

===============



★ ★ ★  15 	 11/12/16.Sat = [8, 17, 20, 27, 52] =
 >>> BOOOOO <<< : GameAgoOneByOneRule
= GameAgoOneByOneRule =
    total: 11238513       excl:2526432   INCL: 8712081
 time: 81 sec.     [TOTAL] excl: 2526432, INCL: 8712081

===============



★ ★ ★  16 	 11/09/16.Wed = [1, 25, 28, 31, 54] =
 >>> BOOOOO <<< : GameAgoOneByOneRule
= GameAgoOneByOneRule =
    total: 11238513       excl:2572924   INCL: 8665589
 time: 83 sec.     [TOTAL] excl: 2572924, INCL: 8665589

===============



 */