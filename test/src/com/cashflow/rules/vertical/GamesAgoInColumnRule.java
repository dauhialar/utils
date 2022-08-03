package com.cashflow.rules.vertical;

import com.cashflow.rules.AbstractRule;
import com.cashflow.rules.derivative.DeltaRule;
import pb.Chunk;
import pb.Utils;
import pb.file.FileReader;
import pb.statistics.ChunkStConfig;
import pb.statistics.ChunkStatistics;

import java.util.*;

/**
 *

 * @author Ruslan Dauhiala
 */
public class GamesAgoInColumnRule extends AbstractRule
{
	Map<Date, List<Integer>> myGamesAgoMap;
	Set<List<Integer>> myGamesAgoSet;
	List<Integer> myCol0;
	List<Integer> myCol1;
	List<Integer> myCol2;
	List<Integer> myCol3;
	List<Integer> myCol4;

	HashSet<List<Integer>> myDelta1Set;
	HashSet<List<Integer>> myDelta2Set;
	HashSet<List<Integer>> myDelta3Set;
	Set<Integer> myDelta4Set;

	Set<Integer> myVHDelta4Set;

	Set<List<Integer>> myGame4ChunkNotInOrderSet;
	Set<List<Integer>> myGame3ChunkNotInOrderSet;
	Set<List<Integer>> myGame2ChunkNotInOrderSet;

	Set<List<Integer>> myGame3ChunkInOrderSet;
	Set<List<Integer>> myGame2ChunkInOrderSet;

	Set<List<Integer>> my1stDer2ChunkNotInOrderSet;
	Set<List<Integer>> my1stDer3ChunkNotInOrderSet;
	Set<List<Integer>> my1stDer4ChunkNotInOrderSet;

	Set<List<Integer>> my1stDer2ChunkInOrderSet;
	Set<List<Integer>> my1stDer3ChunkInOrderSet;

	Set<List<Integer>> myVHDer2ChunkNotInOrderSet;
	Set<List<Integer>> myVHDer3ChunkNotInOrderSet;
	Set<List<Integer>> myVHDer4ChunkNotInOrderSet;

	Set<List<Integer>> myVHDer2ChunkInOrderSet;
	Set<List<Integer>> myVHDer3ChunkInOrderSet;

	Map<Date, List<Integer>> myDelta4Map ;

	public GamesAgoInColumnRule(Map<Date, List<Integer>> theGamesMap)
	{
		super(theGamesMap);
	}

	Map<Date, List<Integer>> myDelta1Map;
	Map<Date, List<Integer>> myVHDelta;

	@Override
	public void setup()
	{
		myGamesAgoMap = Utils.getGamesAgoStat(myGamesMap);
		myGamesAgoSet = new HashSet<>(myGamesAgoMap.values());
		// ★ games ago derivatives ★
		myDelta1Map = DeltaRule.getDeltaMap(0, myGamesAgoMap.size() - 4, myGamesAgoMap);
		Map<Date, List<Integer>> myDelta2Map = DeltaRule.getDeltaMap(myDelta1Map);
		Map<Date, List<Integer>> myDelta3Map = DeltaRule.getDeltaMap(myDelta2Map);

		Map<Date, List<Integer>> aDelta4Map = DeltaRule.getDeltaMap(myDelta3Map);
		myDelta4Map = DeltaRule.getDeltaMap(0, 30, myDelta3Map);                    // [1, 2, 31, 32, 36, 48, 49, 53, 53, 82, 84,

		//myDelta4Map = DeltaRule.getDeltaMap(0, myDelta3Map.size(), myDelta3Map);

//		printConf(myGamesMap, myDelta1Map, myDelta2Map, myDelta3Map, myDelta4Map);

		myDelta1Set = new HashSet<List<Integer>>(myDelta1Map.values());
		myDelta2Set = new HashSet<List<Integer>>(myDelta2Map.values());
		myDelta3Set = new HashSet<List<Integer>>(myDelta3Map.values());

		myDelta4Set = Utils.getIntSet(myDelta4Map);

		// pilot
		myDelta4Set.addAll(Utils.getAgoInt(aDelta4Map, 30, myDates)); // [1, 2, 31, 32, 36, 48, 49, 53, 53, 82, 84,

		// ★	★	★	★	★	★	★	★	★

		myCol0 = Utils.getColumn(myGamesAgoMap, 0);
		myCol1 = Utils.getColumn(myGamesAgoMap, 1);
		myCol2 = Utils.getColumn(myGamesAgoMap, 2);
		myCol3 = Utils.getColumn(myGamesAgoMap, 3);
		myCol4 = Utils.getColumn(myGamesAgoMap, 4);

		myGame4ChunkNotInOrderSet = Utils.getChunkSet(myGamesAgoMap, new ChunkStConfig(4, false, 1530)); // [1532, 2002, 2002, 2002,
		myGame3ChunkNotInOrderSet = Utils.getChunkSet(myGamesAgoMap, new ChunkStConfig(3, false, 5));  //[25, 59, 205, 236, 269, 421, 432, 469,
		myGame2ChunkNotInOrderSet = Utils.getChunkSet(myGamesAgoMap, new ChunkStConfig(2, false, 1));  //[2, 2, 3, 4, 5, 5,
		// pilot
		myGame2ChunkNotInOrderSet.addAll(Utils.getAgoChunks(myGamesAgoMap, 20, new ChunkStConfig(2, false), myDates));

		myGame3ChunkInOrderSet = Utils.getChunkSet(myGamesAgoMap, new ChunkStConfig(3, false, myGamesAgoMap.size()));
		myGame2ChunkInOrderSet = Utils.getChunkSet(myGamesAgoMap, new ChunkStConfig(2, false, 3));     // [5, 7, 10, 10, 10, 12, 13, 16,

		my1stDer4ChunkNotInOrderSet = Utils.getChunkSet(myDelta1Map, new ChunkStConfig(4, false, myDelta1Map.size() - 100));
		my1stDer3ChunkNotInOrderSet = Utils.getChunkSet(myDelta1Map, new ChunkStConfig(3, false, 500)); 	// [514, 1318, 1394, 1421, 1998,
		my1stDer2ChunkNotInOrderSet = Utils.getChunkSet(myDelta1Map, new ChunkStConfig(2, false, 4));   	// [4, 5, 6, 9, 13, 19, 22, 23, 27,

		// pilot
		my1stDer2ChunkNotInOrderSet.addAll(Utils.getAgoChunks(myDelta1Map, 10, new ChunkStConfig(2, false), myDates));  // 10= 0err, 20= 1err #2 100k

		my1stDer3ChunkInOrderSet = Utils.getChunkSet(myDelta1Map, new ChunkStConfig(3, true, 1000));    // [514, 1318, 1394, 1421, 1998,
		my1stDer2ChunkInOrderSet = Utils.getChunkSet(myDelta1Map, new ChunkStConfig(2, true, 10));      // [4, 5, 6, 9, 13, 19, 22, 23, 27, 31,

		// pilot so-so
		//my1stDer2ChunkInOrderSet.addAll(Utils.getAgoChunks(myDelta1Map, 10, new ChunkStConfig(2, true), myDates));  // 10= +10k ;20= +1err #2

		Map<Date, List<Integer>> myVDelta = DeltaRule.getVerticalDelta(myGamesAgoMap);
		myVHDelta = DeltaRule.getDeltaMap(myVDelta);
		Map<Date, List<Integer>> aVHDelta2 = DeltaRule.getDeltaMap(myVHDelta);
		Map<Date, List<Integer>> aVHDelta3 = DeltaRule.getDeltaMap(aVHDelta2);
		Map<Date, List<Integer>> aVHDelta4 = DeltaRule.getDeltaMap(aVHDelta3);

		myVHDelta4Set = Utils.getIntSet(DeltaRule.getDeltaMap(0, 50, aVHDelta3)); //		[49, 62, 84, 100, 109, 126, 144, 217, 218, 221, 230, 232
		// pilot
		myVHDelta4Set.addAll(Utils.getAgoInt(aVHDelta4, 30, myDates));

		myVHDer4ChunkNotInOrderSet = Utils.getChunkSet(myVHDelta, new ChunkStConfig(4, false, myVHDelta.size()));
		myVHDer3ChunkNotInOrderSet = Utils.getChunkSet(myVHDelta, new ChunkStConfig(3, false, myVHDelta.size()));
		myVHDer2ChunkNotInOrderSet = Utils.getChunkSet(myVHDelta, new ChunkStConfig(2, false, 30));   // [32, 40, 45, 49, 52, 158, 167,
		// pilot
		myVHDer2ChunkNotInOrderSet.addAll(Utils.getAgoChunks(myVHDelta, 20, new ChunkStConfig(2, false), myDates));

		myVHDer3ChunkInOrderSet = Utils.getChunkSet(myVHDelta, new ChunkStConfig(3, true, myVHDelta.size() - 100));
		myVHDer2ChunkInOrderSet = Utils.getChunkSet(myVHDelta, new ChunkStConfig(2, true, 40)); // [40, 49, 52, 205, 321, 481, 56
		// pilot
		myVHDer2ChunkInOrderSet.addAll(Utils.getAgoChunks(myVHDelta, 20, new ChunkStConfig(2, true), myDates));   // 10=0err
	}

	private void printConf(Map<Date, List<Integer>> theGameMap,
								  Map<Date, List<Integer>> theDelta1Map,
								  Map<Date, List<Integer>> theDelta2Map,
								  Map<Date, List<Integer>> theDelta3Map,
								  Map<Date, List<Integer>> theDelta4Map)
	{
		int v = 1;
		for (Date aDate: theDelta1Map.keySet())
		{
			List<Integer> aGame = theGameMap.get(aDate);
			List<Integer> aD1Game = theDelta1Map.get(aDate);
			List<Integer> aD2Game = theDelta2Map.get(aDate);
			List<Integer> aD3Game = theDelta3Map.get(aDate);
			List<Integer> aD4Game = theDelta4Map.get(aDate);

			//System.out.printf("%1$4d    " + kOutDateFormat.format(aDate) + " = ", v++);

			Utils.printf(aGame, 2, ".", " || ");
			Utils.printf(myGamesAgoMap.get(aDate), 3, "_", " | ");
			Utils.printf(aD1Game, 4, "`", " | ");
			Utils.printf(aD2Game, 4, "`☆", " | ");
			Utils.printf(aD3Game, 4, "``☆", " | ");
			Utils.printf(aD4Game, 4, "```☆", " | ");
			System.out.println();
		}
	}

	public static void findTheSame(Collection<List<Integer>> theValues)
	{
		int aGameNum = 100;
		int aContain = 0;
		int aNotContain = 0;
		List<Integer> aList = new ArrayList<>();
		for (List<Integer> aList1 : theValues )
		{
			aList.add(aList1.get(0));
		}

		System.out.println("4th derivative in a row: " + aList.toString().replace(",", "~"));

		for (int i = 0; i < aGameNum; i ++)
		{
			int aNum = aList.remove(0);
			if (aList.contains(aNum))
			{
				aContain++;
			} else
			{
				aNotContain++;
			}
		}

		System.out.println("4th derivative include test out of [" + aGameNum + "] total : cont: " + aContain + "; not cont:" + aNotContain);
	}


	@Override
	public boolean accept(List<Integer> thePotentialGame)
	{
		List<Integer> aGameAgo = getGameAgoNums(thePotentialGame);

		List<Integer> aDiff1 = Utils.getGameHDelta(aGameAgo);
		List<Integer> aDiff2 = Utils.getGameHDelta(aDiff1);
		List<Integer> aDiff3 = Utils.getGameHDelta(aDiff2);
		List<Integer> aDiff4 = Utils.getGameHDelta(aDiff3);

		boolean aFound = myDelta4Set.contains(aDiff4.get(0));
		aFound = aFound || myDelta3Set.contains(aDiff3);
		aFound = aFound || myDelta2Set.contains(aDiff2);
		aFound = aFound || myDelta1Set.contains(aDiff1);
		aFound = aFound || myGamesAgoSet.contains(aGameAgo);

		// + ~1000 in the result
		aFound = aFound ||gameChunkCheck(Utils.getAllChunks(aDiff1, 2), my1stDer2ChunkNotInOrderSet);
		aFound = aFound || gameChunkCheck(Utils.getArrayChunks(aDiff1, 2), my1stDer2ChunkInOrderSet);

		aFound = aFound || gameChunkCheck(Utils.getAllChunks(aDiff1, 3), my1stDer3ChunkNotInOrderSet);
		aFound = aFound || gameChunkCheck(Utils.getArrayChunks(aDiff1, 3), my1stDer3ChunkInOrderSet);

		aFound = aFound || gameChunkCheck(Utils.getAllChunks(aDiff1, 4), my1stDer4ChunkNotInOrderSet);

		if (!aFound)
		{
			List<Integer> aVDelta = DeltaRule.getDeltaArray(aGameAgo, myGamesAgoMap.get(myDates.get(0)));
			List<Integer> aVHDelta = Utils.getGameHDelta(aVDelta);

			List<Integer> aVHDelta2 = Utils.getGameHDelta(aVHDelta);
			List<Integer> aVHDelta3 = Utils.getGameHDelta(aVHDelta2);
			List<Integer> aVHDelta4 = Utils.getGameHDelta(aVHDelta3);

			aFound = myVHDelta4Set.contains(aVHDelta4.get(0));
			aFound = aFound || gameChunkCheck(Utils.getArrayChunks(aVHDelta, 2), myVHDer2ChunkInOrderSet);
			aFound = aFound || gameChunkCheck(Utils.getArrayChunks(aVHDelta, 3), myVHDer3ChunkInOrderSet);
			aFound = aFound || gameChunkCheck(Utils.getAllChunks(aVHDelta, 2), myVHDer2ChunkNotInOrderSet);
			aFound = aFound || gameChunkCheck(Utils.getAllChunks(aVHDelta, 3), myVHDer3ChunkNotInOrderSet);
			aFound = aFound || gameChunkCheck(Utils.getAllChunks(aVHDelta, 4), myVHDer4ChunkNotInOrderSet);

			aFound = aFound || gameChunkCheck(Utils.getAllChunks(aGameAgo, 4), myGame4ChunkNotInOrderSet);
			aFound = aFound || gameChunkCheck(Utils.getAllChunks(aGameAgo, 3), myGame3ChunkNotInOrderSet);
			aFound = aFound || gameChunkCheck(Utils.getAllChunks(aGameAgo, 2), myGame2ChunkNotInOrderSet);

			aFound = aFound || gameChunkCheck(Utils.getArrayChunks(aGameAgo, 3), myGame3ChunkInOrderSet);
			aFound = aFound || gameChunkCheck(Utils.getArrayChunks(aGameAgo, 2), myGame2ChunkInOrderSet);
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



	private List<Integer> getGameAgoNums(List<Integer> thePotentialGame)
	{
		List<Integer> aRes = new ArrayList<>();
		aRes.add(myCol0.indexOf(thePotentialGame.get(0)));
		aRes.add(myCol1.indexOf(thePotentialGame.get(1)));
		aRes.add(myCol2.indexOf(thePotentialGame.get(2)));
		aRes.add(myCol3.indexOf(thePotentialGame.get(3)));
		aRes.add(myCol4.indexOf(thePotentialGame.get(4)));
		return aRes;
	}

	public static void main(String[] args)
	{
		Map<Date, List<Integer>> myAllTheFun = new FileReader().loadFromFile("/Users/jet/Documents/pb/pb.txt");

		GamesAgoInColumnRule aRule = new GamesAgoInColumnRule(myAllTheFun);
		aRule.test();
		//aRule.accept(Arrays.asList(new Integer[]{1,2,3,4,5}));
	}

	/**
	 *
	 *
	 */

	private void test()
	{

//		myDelta1Map = DeltaRule.getDeltaMap(0, myGamesAgoMap.size()-4, myGamesAgoMap);
		Map<Date, List<Integer>> myDelta2Map = DeltaRule.getDeltaMap(myDelta1Map);
		Map<Date, List<Integer>> myDelta3Map = DeltaRule.getDeltaMap(myDelta2Map);
		myDelta4Map = DeltaRule.getDeltaMap(myDelta3Map);

//		System.out.println("=== my games ago stat ===");
//		ChunkStatistics.getAllChunkStats(myGamesAgoMap);
//		System.out.println("=========================\n\n");


		System.out.println("===  H delta`1 stat ===");
		ChunkStatistics.getAllChunkStats(myDelta1Map);
		System.out.println("=======================\n\n");

//
//		System.out.println("===  H delta`4 stat ===");
//		ChunkStatistics.getAllChunkStats(myDelta4Map);
//		System.out.println("=======================\n\n");
//
//
//		System.out.println("===  VH delta`1 stat ===");
//		ChunkStatistics.getAllChunkStats(myVHDelta);
//		System.out.println("========================\n\n");
//
//		myVHDelta = DeltaRule.getDeltaMap(myVHDelta);
//		myVHDelta = DeltaRule.getDeltaMap(myVHDelta);
//		myVHDelta = DeltaRule.getDeltaMap(myVHDelta);
//
//		System.out.println("===  VH delta`4 stat ===");
//		ChunkStatistics.getAllChunkStats(myVHDelta);
//		System.out.println("========================\n\n");

//		Map<Date, List<Integer>> aMap = Utils.getGamesAgoStat(myGamesMap);
//		//Map<Date, List<Integer>> aDelta1Map = DeltaRule.getDeltaMap(aMap);
//		Map<Date, List<Integer>> aDelta1Map = DeltaRule.getVerticalDelta(aMap);
//		aDelta1Map = DeltaRule.getDeltaMap(aDelta1Map);
//
//		//		Map<Date, String> aChunkStatMap = Utils.getChunkStatistic(aMap);
//		int i = 0;
//		//for (Date aDate : myDates)
//		for (Date aDate : aDelta1Map.keySet())
//		{
//			System.out.printf("%1$4d", i++);
//			System.out.print(
//					  " \t\t" + Meters.kOutDateFormat.format(aDate)
////								 " \t\t g" + myGamesMap.get(aDate).subList(0, 5) +
////								 " |\t\td" + aMap.get(aDate).toString().replace(",", "'") +
////								 " |\t\td{x}" + aDelta1Map.get(aDate).toString().replace(",", "`")
////					  + " |\t\t" + aChunkStatMap.get(aDate)
//			);
//			System.out.print("\t\tg=");
//			Utils.printf(myGamesMap.get(aDate).subList(0, 5), 2, ", ", "");
//			System.out.print("\t\td=");
//			Utils.printf(aMap.get(aDate) , 3, "'", "");
//			System.out.print("\t\td{x}=");
//			Utils.printf(aDelta1Map.get(aDate) , 4, "`", "\n");
//		}
//		System.out.println();
//		findTheSame(myDelta4Map.values());
//
//		DeltaRule.getFullDerivativeStat(aMap);

	}


}


/*


★ ★ ★  0 	 01/04/17.Wed = [16, 17, 29, 41, 42] =
= GamesAgoInColumnRule =
    total: 11238513       excl:2151964   INCL: 9086549
 time: 245 sec.     [TOTAL] excl: 2151964, INCL: 9086549

===============



★ ★ ★  1 	 12/31/16.Sat = [1, 3, 28, 57, 67] =
= GamesAgoInColumnRule =
    total: 11238513       excl:1712583   INCL: 9525930
 time: 243 sec.     [TOTAL] excl: 1712583, INCL: 9525930

===============



★ ★ ★  2 	 12/28/16.Wed = [16, 23, 30, 44, 58] =
= GamesAgoInColumnRule =
    total: 11238513       excl:1723659   INCL: 9514854
 time: 242 sec.     [TOTAL] excl: 1723659, INCL: 9514854

===============



★ ★ ★  3 	 12/24/16.Sat = [28, 38, 42, 51, 52] =
= GamesAgoInColumnRule =
    total: 11238513       excl:1853571   INCL: 9384942
 time: 240 sec.     [TOTAL] excl: 1853571, INCL: 9384942

===============



★ ★ ★  4 	 12/21/16.Wed = [25, 33, 40, 54, 68] =
= GamesAgoInColumnRule =
    total: 11238513       excl:1986153   INCL: 9252360
 time: 274 sec.     [TOTAL] excl: 1986153, INCL: 9252360

===============



★ ★ ★  5 	 12/17/16.Sat = [1, 8, 16, 40, 48] =
= GamesAgoInColumnRule =
    total: 11238513       excl:1924308   INCL: 9314205
 time: 503 sec.     [TOTAL] excl: 1924308, INCL: 9314205

===============



★ ★ ★  6 	 12/14/16.Wed = [18, 26, 37, 39, 66] =
= GamesAgoInColumnRule =
    total: 11238513       excl:2463695   INCL: 8774818
 time: 552 sec.     [TOTAL] excl: 2463695, INCL: 8774818

===============



★ ★ ★  7 	 12/10/16.Sat = [12, 21, 32, 44, 66] =
= GamesAgoInColumnRule =
    total: 11238513       excl:1843847   INCL: 9394666
 time: 250 sec.     [TOTAL] excl: 1843847, INCL: 9394666

===============



★ ★ ★  8 	 12/07/16.Wed = [41, 48, 49, 53, 64] =
= GamesAgoInColumnRule =
    total: 11238513       excl:2189237   INCL: 9049276
 time: 253 sec.     [TOTAL] excl: 2189237, INCL: 9049276

===============



★ ★ ★  9 	 12/03/16.Sat = [8, 10, 26, 27, 33] =
= GamesAgoInColumnRule =
    total: 11238513       excl:1830782   INCL: 9407731
 time: 251 sec.     [TOTAL] excl: 1830782, INCL: 9407731

===============



★ ★ ★  10 	 11/30/16.Wed = [3, 14, 18, 25, 45] =
= GamesAgoInColumnRule =
    total: 11238513       excl:2121302   INCL: 9117211
 time: 252 sec.     [TOTAL] excl: 2121302, INCL: 9117211

===============



★ ★ ★  11 	 11/26/16.Sat = [17, 19, 21, 37, 44] =
= GamesAgoInColumnRule =
    total: 11238513       excl:1956870   INCL: 9281643
 time: 234 sec.     [TOTAL] excl: 1956870, INCL: 9281643

===============



★ ★ ★  12 	 11/23/16.Wed = [7, 32, 41, 47, 61] =
 >>> BOOOOO <<< : GamesAgoInColumnRule
= GamesAgoInColumnRule =
    total: 11238513       excl:2013182   INCL: 9225331
 time: 228 sec.     [TOTAL] excl: 2013182, INCL: 9225331

===============



★ ★ ★  13 	 11/19/16.Sat = [16, 24, 28, 43, 61] =
= GamesAgoInColumnRule =
    total: 11238513       excl:1947307   INCL: 9291206
 time: 229 sec.     [TOTAL] excl: 1947307, INCL: 9291206

===============



★ ★ ★  14 	 11/16/16.Wed = [28, 41, 61, 63, 65] =
= GamesAgoInColumnRule =
    total: 11238513       excl:2147089   INCL: 9091424
 time: 235 sec.     [TOTAL] excl: 2147089, INCL: 9091424

===============



★ ★ ★  15 	 11/12/16.Sat = [8, 17, 20, 27, 52] =
= GamesAgoInColumnRule =
    total: 11238513       excl:1924362   INCL: 9314151
 time: 237 sec.     [TOTAL] excl: 1924362, INCL: 9314151

===============



★ ★ ★  16 	 11/09/16.Wed = [1, 25, 28, 31, 54] =
= GamesAgoInColumnRule =
    total: 11238513       excl:1730184   INCL: 9508329
 time: 230 sec.     [TOTAL] excl: 1730184, INCL: 9508329

===============



★ ★ ★  17 	 11/05/16.Sat = [21, 31, 50, 51, 69] =
= GamesAgoInColumnRule =
    total: 11238513       excl:2063896   INCL: 9174617
 time: 228 sec.     [TOTAL] excl: 2063896, INCL: 9174617

===============



★ ★ ★  18 	 11/02/16.Wed = [13, 18, 37, 54, 61] =
= GamesAgoInColumnRule =
    total: 11238513       excl:1856278   INCL: 9382235
 time: 227 sec.     [TOTAL] excl: 1856278, INCL: 9382235

===============



★ ★ ★  19 	 10/29/16.Sat = [19, 20, 21, 42, 48] =
= GamesAgoInColumnRule =
    total: 11238513       excl:1963481   INCL: 9275032
 time: 236 sec.     [TOTAL] excl: 1963481, INCL: 9275032

===============




TOTAL time: 5404 sec.


 */