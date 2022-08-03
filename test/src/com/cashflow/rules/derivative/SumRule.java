package com.cashflow.rules.derivative;

import com.cashflow.rules.AbstractRule;
import pb.Chunk;
import pb.Meters;
import pb.Utils;
import pb.file.FileReader;
import pb.statistics.ChunkStConfig;
import pb.statistics.ChunkStatistics;

import java.util.*;

/**
 *

  = SumRule =
 total: 58896       excl:530   INCL: 58366

 = SumRule =
 total: 58896       excl:10437   INCL: 48459 == new chunk method count : 9907

 total: 11238513       excl:1312079   INCL: 9926434
 == new chunk method count : 972200

 total: 11238513       excl:2137088   INCL: 9101425
 == new chunk method count : 1797209
 										1797209

 										2096645
 * @author Ruslan Dauhiala
 */
public class SumRule extends AbstractRule
{

	Map<Date, List<Integer>> mySum2Map;
	Map<Date, List<Integer>> mySum3Map;
	Map<Date, List<Integer>> mySum4Map;
	Map<Date, List<Integer>> mySum5Map;

	Set<List<Integer>> mySum3Chunk3NotO;
	Set<List<Integer>> mySum4Chunk2NotO;

	Set<List<Integer>> mySum4Chunk1NotO;

	//Set<List<Integer>> mySum3Chunk2O;
	Set<List<Integer>> mySum3Chunk2NotO;
	Set<Integer> mySum5Chunk1NotO;

	Map<Date, List<Integer>> myVDeltaSum2Map;
	Map<Date, List<Integer>> myDelta1Sum2Map;
	Map<Date, List<Integer>> myDelta2Sum2Map;
	Map<Date, List<Integer>> myDelta3Sum2Map;

	//Set<List<Integer>> mySum2Set;
	//Set<List<Integer>> myVDeltaSum2Set;
	Set<List<Integer>> mySum2Chunk4SetNotO;
	Set<List<Integer>> mySum2Chunk3SetNotO;
	Set<List<Integer>> mySum2Chunk3SetO;
	Set<List<Integer>> mySum2Chunk2SetNotO;

	Set<List<Integer>> myDelta1Sum2Chunk3SetNotO;
	Set<List<Integer>> myDelta1Sum2Chunk2SetNotO;
	Set<List<Integer>> myDelta1Sum2Chunk3SetO;

	Set<List<Integer>> myDelta2Sum2Chunk2SetNotO;
	Set<List<Integer>> myDelta2Sum2Chunk2SetO;

	Set<List<Integer>> mySum2Chunk2SetO;

	Set<Integer> myDelta3Sum2ChunkSet;


	Set<List<Integer>> myVDeltaSum2Chunk3SetO;
	Set<List<Integer>> myVDeltaSum2Chunk2SetO;
	Set<List<Integer>> myVDeltaSum2Chunk3SetNotO;
	Set<List<Integer>> myVDeltaSum2Chunk4SetNotO;
	Set<List<Integer>> myVDeltaSum2Chunk2SetNotO;

	Set<List<Integer>> myVHDelta1Chunk3Sum2SetNotO;
	Set<List<Integer>> myVHDelta1Chunk3Sum2SetO;
	Set<List<Integer>> myVHDelta1Chunk2Sum2SetNotO;
	Set<List<Integer>> myVHDelta1Chunk2Sum2SetO;

	Set<List<Integer>> myVHDelta2Chunk2SetSum2NotO;
	Set<List<Integer>> myVHDelta2Chunk2SetSum2O;

	Set<Integer> myVDelta3ChunkSet;

	Set<Double> my2Sum2130Rate;
	Set<Double> my2Sum3120Rate;
//	Set<Double> my2Sum3210Rate;

	public SumRule(Map<Date, List<Integer>> theGamesMap)
	{
		super(theGamesMap);
	}

	@Override
	public void setup()
	{
		mySum2Map = getSumMap(myGamesMap,2);
		mySum3Map = getSumMap(myGamesMap,3);
		mySum4Map = getSumMap(myGamesMap,4);
		mySum5Map = getSumMap(myGamesMap,5);

		mySum3Chunk3NotO = Utils.getChunkSet(mySum3Map, new ChunkStConfig(3, false, mySum3Map.size())); // new HashSet<>(mySum3Map.values());

		mySum3Chunk2NotO = Utils.getChunkSet(mySum3Map, new ChunkStConfig(2, false, 39));  // [31, 40, 42, 51, 66, 73, 94,
		mySum4Chunk2NotO = Utils.getChunkSet(mySum4Map, new ChunkStConfig(2, false, 20));   // [13, 54, 92, 237, 291, 297, 433, 467, 479, 532, 580,
		mySum4Chunk1NotO = Utils.getChunkSet(mySum4Map, new ChunkStConfig(1, false, 2));   // [1, 3, 3, 4, 4, 4, 5,
		// pilot --
		//mySum4Chunk1NotO.addAll(Utils.getAgoChunks(mySum4Map, 5, new ChunkStConfig(1, false), myDates));

		List<Double> aDelta2130Rate = Utils.getDeltaPercentList(2,1,3,0, mySum2Map.values());
		my2Sum2130Rate = Utils.getAgoFromGapNumbers(70, aDelta2130Rate);
		// pilot     10 =  3, #: [1, 7, 18]   denied: 	 4501738;
		//  		    20 =  3, #: [1, 7, 18]   denied: 	 4631895;
		//			    30 =  3, #: [1, 7, 18]   denied: 	 4774257;
		//           40 =  3, #: [1, 7, 18]   denied: 	 4879365;
		//           50 =  3, #: [1, 7, 18]   denied: 	 4990550;
		// 		    60 =  3, #: [1, 7, 18]   denied: 	 5091538;
		// 		    70 =  3, #: [1, 7, 18]   denied: 	 5207909;
		// 		    80 =  3, #: [1, 7, 18]   denied: 	 ~5291538;
		// 		    90 =  4, #: [1, 4, 7, 18]		denied: 	 5320123;
		// 		    100 = 4, #: [1, 4, 7, 18]		denied: 	 5520123;

		List<Double> aDelta3120Rate = Utils.getDeltaPercentList(3,1,2,0, mySum2Map.values());
		my2Sum3120Rate = Utils.getAgoFromGapNumbers(10, aDelta3120Rate);
		// pilot     10 =   3, #: [1, 7, 18]						denied: 	 5270423;
		// pilot     20 =   4, #: [1, 7, 12, 18]				denied: 	 5337046;
		// pilot     30 =
		// pilot     50 =  6, #: [0, 1, 7, 12, 15, 18]        denied: 	 5505550;
		// pilot     100 =  6, #: [0, 1, 7, 12, 15, 18]        denied: 	 5765848;

//		List<Double> aDelta3210Rate = Utils.getDeltaPercentList(3,2,1,0, mySum2Map.values());
//		my2Sum3210Rate = Utils.getAgoFromGapNumbers(10, aDelta3210Rate);
		// pilot   10 = ERRORS:     	 4, #: [1, 3, 7, 18]

		mySum5Chunk1NotO = new HashSet<>();
		for (int i = 0; i < 2; i++)    // 3=+200k  // 10 = 500k
		{
			mySum5Chunk1NotO.add(mySum5Map.get(myDates.get(i)).get(0));
		}
//		mySum3Chunk2O = Utils.getChunkSet(mySum3Map, new ChunkStConfig(2, true, 39));  	// 62639

		myDelta1Sum2Map = DeltaRule.getDeltaMap(mySum2Map);
		myDelta2Sum2Map = DeltaRule.getDeltaMap(myDelta1Sum2Map);
		myDelta3Sum2Map = DeltaRule.getDeltaMap(myDelta2Sum2Map);

		//mySum2Set = new HashSet<>(mySum2Map.values());
		mySum2Chunk4SetNotO = Utils.getChunkSet(mySum2Map, new ChunkStConfig(4, false, mySum2Map.size()));
		mySum2Chunk3SetNotO = Utils.getChunkSet(mySum2Map, new ChunkStConfig(3, false, 170));  	// [174, 192, 199, 312, 442, 5
		mySum2Chunk2SetNotO = Utils.getChunkSet(mySum2Map, new ChunkStConfig(2, false, 4));		// [3, 4, 6, 7, 7, 12,
		// pilot ??
		//mySum2Chunk2SetNotO.addAll(Utils.getAgoChunks(mySum2Map, 10, new ChunkStConfig(2, false), myDates));


		mySum2Chunk3SetO = Utils.getChunkSet(mySum2Map, new ChunkStConfig(3, true, 150));
		mySum2Chunk2SetO = Utils.getChunkSet(mySum2Map, new ChunkStConfig(2, true, 40));
		// pilot ??  TOTAL ERRORS: 4, #: [1, 6, 7, 18] +200k
		//mySum2Chunk2SetO.addAll(Utils.getAgoChunks(mySum2Map, 10, new ChunkStConfig(2, true), myDates));

		myDelta1Sum2Chunk3SetNotO = Utils.getChunkSet(myDelta1Sum2Map, new ChunkStConfig(3, false, 200));		//  [60, 287, 465, 718, 727, 1010, 1190,
		myDelta1Sum2Chunk3SetO = Utils.getChunkSet(myDelta1Sum2Map, new ChunkStConfig(3, true,
				  //500
				  50
		));         // 50=981726  // 500=1076076
		myDelta1Sum2Chunk2SetNotO = Utils.getChunkSet(myDelta1Sum2Map, new ChunkStConfig(2, true, 1));       //  [1, 1, 2, 2, 2, 3, 3, 4, 5, 5, 6
		myDelta2Sum2Chunk2SetNotO = Utils.getChunkSet(myDelta2Sum2Map, new ChunkStConfig(2, false, 20));		// [16, 33, 65, 98, 120, 122, 123, 185,
		// pilot ??  TOTAL ERRORS: 3, #: [1, 7, 18] +40k
		//myDelta2Sum2Chunk2SetNotO.addAll(Utils.getAgoChunks(myDelta2Sum2Map, 10, new ChunkStConfig(2, false), myDates));

		myDelta2Sum2Chunk2SetO = Utils.getChunkSet(myDelta2Sum2Map, new ChunkStConfig(2, true, 30));

		myDelta3Sum2ChunkSet = Utils.getIntSet(myDelta3Sum2Map);
		for (int i = 0; i < 1; i++)
		{
			myDelta3Sum2ChunkSet.remove(myDelta3Sum2Map.get(myDates.get(i)).get(0));
		}

		myVDeltaSum2Map = DeltaRule.getVerticalDelta(mySum2Map);
		Map<Date, List<Integer>> aVHDelta1Sum2 = DeltaRule.getDeltaMap(myVDeltaSum2Map);
		Map<Date, List<Integer>> aVHDelta2Sum2 = DeltaRule.getDeltaMap(aVHDelta1Sum2);
		Map<Date, List<Integer>> aVHDelta3Sum2 = DeltaRule.getDeltaMap(aVHDelta2Sum2);

		myVDeltaSum2Chunk4SetNotO = Utils.getChunkSet(myVDeltaSum2Map, new ChunkStConfig(4, false, myVDeltaSum2Map.size()));
		myVDeltaSum2Chunk3SetNotO = Utils.getChunkSet(myVDeltaSum2Map, new ChunkStConfig(3, false, 100));		// [38, 142, 294, 307, 430, 745,
		myVDeltaSum2Chunk2SetNotO = Utils.getChunkSet(myVDeltaSum2Map, new ChunkStConfig(2, false, 4));			// [4, 8, 13, 18, 19, 21, 22, 31,

		// pilot
		myVDeltaSum2Chunk2SetNotO.addAll(Utils.getAgoChunks(myVDeltaSum2Map, 10, new ChunkStConfig(2, false), myDates));

		// pilot
//		myDelta4Set.addAll(Utils.getAgoInt(aDelta4Map, 30, myDates)); // [1, 2, 31, 32, 36, 48, 49, 53, 53, 82, 84,


		myVDeltaSum2Chunk3SetO = Utils.getChunkSet(myVDeltaSum2Map, new ChunkStConfig(3, true, 500));		//
		myVDeltaSum2Chunk2SetO = Utils.getChunkSet(myVDeltaSum2Map, new ChunkStConfig(2, true, 10));		// 1674322


		myVHDelta1Chunk3Sum2SetNotO = Utils.getChunkSet(aVHDelta1Sum2, new ChunkStConfig(3, false, 1000));		// [13, 2001, 2001, 2001, 2001, 2001, 2001, 2001, 2001,
		myVHDelta1Chunk2Sum2SetNotO = Utils.getChunkSet(aVHDelta1Sum2, new ChunkStConfig(2, false, 2));		//    [2, 4, 9, 11, 13, 17, 26,
		// pilot
		myVHDelta1Chunk2Sum2SetNotO.addAll(Utils.getAgoChunks(aVHDelta1Sum2, 10, new ChunkStConfig(2, false), myDates));

		myVHDelta1Chunk3Sum2SetO = Utils.getChunkSet(aVHDelta1Sum2, new ChunkStConfig(3, true, 500));		//     1900=1912336
		myVHDelta1Chunk2Sum2SetO = Utils.getChunkSet(aVHDelta1Sum2, new ChunkStConfig(2, true, 5));		//  1761831

		myVHDelta2Chunk2SetSum2NotO = Utils.getChunkSet(aVHDelta2Sum2, new ChunkStConfig(2, false, 15));		// [13, 47, 122, 154, 177, 268, 286
		myVHDelta2Chunk2SetSum2O = Utils.getChunkSet(aVHDelta2Sum2, new ChunkStConfig(2, true, 40));		//  1877138
		// pilot
		myVHDelta2Chunk2SetSum2O.addAll(Utils.getAgoChunks(aVHDelta2Sum2, 10, new ChunkStConfig(2, false), myDates));

		myVDelta3ChunkSet = Utils.getIntSet(aVHDelta3Sum2);                                         // 1881045
		for (int i = 0; i < 1; i++)                            // [1, 1, 3, 4, 6, 6, 7, 7, 8,
		{
			myVDelta3ChunkSet.remove(aVHDelta3Sum2.get(myDates.get(i)).get(0));
		}
		// pilot
		// 0  =
		// 5  =		1err  +400k
		// 10 = 		+1err +800k
		myVDelta3ChunkSet.removeAll(Utils.getAgoInt(aVHDelta3Sum2, 10, myDates));
	}

	@Override
	public boolean accept(List<Integer> thePotentialGame)
	{
		List<Integer> aSum2List = getSumList(thePotentialGame,2);
		List<Integer> aDelta1 = Utils.getGameHDelta(aSum2List);
		List<Integer> aDelta2 = Utils.getGameHDelta(aDelta1);
		List<Integer> aDelta3 = Utils.getGameHDelta(aDelta2);

		List<Integer> aVDelta =  DeltaRule.getDeltaArray(aSum2List, mySum2Map.get(myDates.get(0)));
		List<Integer> aVDelta1 = Utils.getGameHDelta(aVDelta);
		List<Integer> aVDelta2 = Utils.getGameHDelta(aVDelta1);


		boolean aFound = false;
		List<Integer> aSum3List = getSumList(thePotentialGame, 3);
		List<Integer> aSum4List = getSumList(thePotentialGame, 4);
		List<Integer> aSum5List = getSumList(thePotentialGame, 5);

		aFound = aFound || my2Sum2130Rate.contains(Utils.getRate(aSum2List.get(2), aSum2List.get(1), aSum2List.get(3), aSum2List.get(0)));
		aFound = aFound || my2Sum3120Rate.contains(Utils.getRate(aSum2List.get(3), aSum2List.get(1), aSum2List.get(2), aSum2List.get(0)));
//		aFound = aFound || my2Sum3210Rate.contains(Utils.getRate(aSum2List.get(3), aSum2List.get(2), aSum2List.get(1), aSum2List.get(0)));

		aFound = aFound || gameChunkCheck(Utils.getAllChunks(aSum2List, 4), mySum2Chunk4SetNotO); //.contains(aSum2List);
		aFound = aFound || gameChunkCheck(Utils.getAllChunks(aSum3List, 3), mySum3Chunk3NotO); // mySum3Chunk3NotO.contains(aSum3List);
		aFound = aFound || gameChunkCheck(Utils.getAllChunks(aSum3List, 2), mySum3Chunk2NotO);  //+300k
		aFound = aFound || gameChunkCheck(Utils.getAllChunks(aSum4List, 2), mySum4Chunk2NotO);   //mySum4Chunk2NotO.contains(aSum4List);
		aFound = aFound || gameChunkCheck(Utils.getAllChunks(aSum4List, 1), mySum4Chunk1NotO);   //+300k
		aFound = aFound || mySum5Chunk1NotO.contains(aSum5List.get(0));                         // +200

		aFound = aFound || !myDelta3Sum2ChunkSet.contains(aDelta3.get(0));

		aFound = aFound || gameChunkCheck(Utils.getAllChunks(aSum2List, 3), mySum2Chunk3SetNotO);
		aFound = aFound || gameChunkCheck(Utils.getArrayChunks(aSum2List, 3), mySum2Chunk3SetO);

		aFound = aFound || gameChunkCheck(Utils.getAllChunks(aSum2List, 2), mySum2Chunk2SetNotO);
		aFound = aFound || gameChunkCheck(Utils.getArrayChunks(aSum2List, 2), mySum2Chunk2SetO);

		aFound = aFound || gameChunkCheck(Utils.getAllChunks(aDelta1, 3), myDelta1Sum2Chunk3SetNotO);
		aFound = aFound || gameChunkCheck(Utils.getArrayChunks(aDelta1, 3), myDelta1Sum2Chunk3SetO);

		aFound = aFound || gameChunkCheck(Utils.getAllChunks(aDelta2, 2), myDelta2Sum2Chunk2SetNotO);
		aFound = aFound || gameChunkCheck(Utils.getArrayChunks(aDelta2, 2), myDelta2Sum2Chunk2SetO);

		aFound = aFound || gameChunkCheck(Utils.getAllChunks(aVDelta, 4), myVDeltaSum2Chunk4SetNotO);
		aFound = aFound || gameChunkCheck(Utils.getArrayChunks(aVDelta, 3), myVDeltaSum2Chunk3SetO);
		aFound = aFound || gameChunkCheck(Utils.getArrayChunks(aVDelta, 2), myVDeltaSum2Chunk2SetO);

		aFound = aFound || gameChunkCheck(Utils.getAllChunks(aVDelta, 3), myVDeltaSum2Chunk3SetNotO);
		aFound = aFound || gameChunkCheck(Utils.getAllChunks(aVDelta, 2), myVDeltaSum2Chunk2SetNotO);

		aFound = aFound || gameChunkCheck(Utils.getAllChunks(aVDelta1, 3), myVHDelta1Chunk3Sum2SetNotO);
		aFound = aFound || gameChunkCheck(Utils.getAllChunks(aVDelta1, 2), myVHDelta1Chunk2Sum2SetNotO);

		aFound = aFound || gameChunkCheck(Utils.getArrayChunks(aVDelta1, 3), myVHDelta1Chunk3Sum2SetO);
		aFound = aFound || gameChunkCheck(Utils.getArrayChunks(aVDelta1, 2), myVHDelta1Chunk2Sum2SetO);

		aFound = aFound || gameChunkCheck(Utils.getAllChunks(aVDelta2, 2), myVHDelta2Chunk2SetSum2NotO);
		aFound = aFound || gameChunkCheck(Utils.getArrayChunks(aVDelta2, 2), myVHDelta2Chunk2SetSum2O);

		List<Integer> aVDelta3 = Utils.getGameHDelta(aVDelta2);
		aFound = aFound || !myVDelta3ChunkSet.contains(aVDelta3.get(0));

		trackResult(!aFound);
		return !aFound;
	}


	private void test()
	{
		Map<Date, List<Integer>> aSumMap = mySum2Map;
//		Map<Date, List<Integer>> aDelta1SumMap = DeltaRule.getDeltaMap(mySum2Map);
		Map<Date, List<Integer>> aDelta1SumMap = DeltaRule.getVerticalDelta(mySum2Map);

//		Map<Date, List<Integer>> aDelta1SumMap = DeltaRule.getDeltaMap(aSumMap);
		Map<Date, List<Integer>> aDelta2SumMap = DeltaRule.getDeltaMap(aDelta1SumMap);
		Map<Date, List<Integer>> aDelta3Sum2Map = DeltaRule.getDeltaMap(aDelta2SumMap);

		int i = 0;
		for (Map.Entry<Date, List<Integer>> anEntry : aSumMap.entrySet())
		{
			System.out.printf("%1$4d", i++);
			System.out.print(" \t\t" + Meters.kOutDateFormat.format(anEntry.getKey()));
			System.out.print("\t\tg=");
			Utils.printf(myGamesMap.get(anEntry.getKey()).subList(0, 5), 2, ", ", "");
//			System.out.print("\t\tsum=");
//			Utils.printf(mySum2Map.get(anEntry.getKey()), 3, ".", "\t\t| ");
			System.out.print("\t\t| ");
			Utils.printf(aSumMap.get(anEntry.getKey()), 4, "'", " | \t\t | ");
			System.out.print("\t\t| ");
//			Utils.printf(aDelta1SumMap.get(anEntry.getKey()), 4, "''", " | \t\t | ");

			System.out.print("\t\t| ");
//			Utils.printf(aDelta2SumMap.get(anEntry.getKey()) , 4, "'''", " | \t\t");
//			Utils.printf(aDelta4SumMap.get(anEntry.getKey()) , 4, "''''", " | ");
			System.out.println();
		}

//		ChunkStatistics.getAllChunkStats(aSumMap);
//		System.out.println("\n||||| delta 1:  |||||");
//		ChunkStatistics.getAllChunkStats(aDelta1SumMap);

//		System.out.println("\n||||| delta 2:  |||||");
//		ChunkStatistics.getAllChunkStats(aDelta3Sum2Map);

//		System.out.println("\n||||| delta 3:  |||||");
//		Map<Date, List<Integer>> aDelta3SumMap = DeltaRule.getDeltaMap(aDelta2SumMap);
//		ChunkStatistics.getAllChunkStats(aDelta3SumMap);
//		System.out.println("\n delta 3:  |||||\n" + aDelta3SumMap.values());


//		ChunkStatistics.getAllChunkStats(mySum4Map);
//		System.out.println("\n||||| sum5:  |||||");
//		ChunkStatistics.getAllChunkStats(mySum5Map);

		List<Double> aDelta3120Rate = Utils.getDeltaPercentList(3,1,2,0, mySum2Map.values());
		List<Integer> a2130List = Utils.getGapBetweenSameNumbers(aDelta3120Rate);
		System.out.println("2130 rule:\n" + a2130List);
		Collections.sort(a2130List);
		System.out.println("2130 rule sorted:\n" + a2130List);
	}

	public static void main(String args[])
	{
		Map<Date, List<Integer>> myAllTheFun = new FileReader().loadFromFile("/Users/jet/Documents/pb/pb.txt");

		new SumRule(myAllTheFun).test();

	}

	public static Map<Date, List<Integer>> getSumMap(Map<Date, List<Integer>> theGamesMap, int theCount)
	{
		Map<Date, List<Integer>> aRes = new LinkedHashMap<>();
		for (Map.Entry<Date, List<Integer>> anEntry : theGamesMap.entrySet())
		{
			aRes.put(anEntry.getKey(), getSumList(anEntry.getValue().subList(0, 5), theCount));
		}
		return aRes;
	}

	public static  List<Integer> getSumList(List<Integer> theIntegers, int theCount)
	{
		List<Integer> aSumList = new ArrayList<>();
		for (int i = 0; i <= theIntegers.size() - theCount; i++)
		{
			int aSum = 0;
			for (int j = i; j < i + theCount; j++)
			{
				aSum += theIntegers.get(j);
			}
			aSumList.add(aSum);
			//aSumList.add(theIntegers.get(i) + theIntegers.get(i+1));
		}
		return aSumList;
	}

//	@Override
//	public void printReport()
//	{
//		super.printReport();
//		System.out.println(" == new chunk method count : " + myCount);
//	}
}


/*



★ ★ ★  0 	 01/04/17.Wed = [16, 17, 29, 41, 42] =
= SumRule =
    total: 11238513       excl:5238651   INCL: 5999862
 time: 82 sec.     [TOTAL] excl: 5238651, INCL: 5999862


★ ★ ★  1 	 12/31/16.Sat = [1, 3, 28, 57, 67] =
 >>> BOOOOO <<< : SumRule
= SumRule =
    total: 11238513       excl:5606603   INCL: 5631910
 time: 77 sec.     [TOTAL] excl: 5606603, INCL: 5631910


★ ★ ★  2 	 12/28/16.Wed = [16, 23, 30, 44, 58] =
= SumRule =
    total: 11238513       excl:5236118   INCL: 6002395
 time: 76 sec.     [TOTAL] excl: 5236118, INCL: 6002395


★ ★ ★  3 	 12/24/16.Sat = [28, 38, 42, 51, 52] =
= SumRule =
    total: 11238513       excl:5557358   INCL: 5681155
 time: 75 sec.     [TOTAL] excl: 5557358, INCL: 5681155


★ ★ ★  4 	 12/21/16.Wed = [25, 33, 40, 54, 68] =
= SumRule =
    total: 11238513       excl:4760802   INCL: 6477711
 time: 72 sec.     [TOTAL] excl: 4760802, INCL: 6477711


★ ★ ★  5 	 12/17/16.Sat = [1, 8, 16, 40, 48] =
= SumRule =
    total: 11238513       excl:5601221   INCL: 5637292
 time: 73 sec.     [TOTAL] excl: 5601221, INCL: 5637292


★ ★ ★  6 	 12/14/16.Wed = [18, 26, 37, 39, 66] =
= SumRule =
    total: 11238513       excl:5534810   INCL: 5703703
 time: 72 sec.     [TOTAL] excl: 5534810, INCL: 5703703


★ ★ ★  7 	 12/10/16.Sat = [12, 21, 32, 44, 66] =
 >>> BOOOOO <<< : SumRule
= SumRule =
    total: 11238513       excl:4611043   INCL: 6627470
 time: 73 sec.     [TOTAL] excl: 4611043, INCL: 6627470


★ ★ ★  8 	 12/07/16.Wed = [41, 48, 49, 53, 64] =
= SumRule =
    total: 11238513       excl:4985303   INCL: 6253210
 time: 72 sec.     [TOTAL] excl: 4985303, INCL: 6253210


★ ★ ★  9 	 12/03/16.Sat = [8, 10, 26, 27, 33] =
= SumRule =
    total: 11238513       excl:4950477   INCL: 6288036
 time: 74 sec.     [TOTAL] excl: 4950477, INCL: 6288036


★ ★ ★  10 	 11/30/16.Wed = [3, 14, 18, 25, 45] =
= SumRule =
    total: 11238513       excl:5170261   INCL: 6068252
 time: 73 sec.     [TOTAL] excl: 5170261, INCL: 6068252


★ ★ ★  11 	 11/26/16.Sat = [17, 19, 21, 37, 44] =
= SumRule =
    total: 11238513       excl:5425361   INCL: 5813152
 time: 68 sec.     [TOTAL] excl: 5425361, INCL: 5813152


★ ★ ★  12 	 11/23/16.Wed = [7, 32, 41, 47, 61] =
= SumRule =
    total: 11238513       excl:5573210   INCL: 5665303
 time: 70 sec.     [TOTAL] excl: 5573210, INCL: 5665303


★ ★ ★  13 	 11/19/16.Sat = [16, 24, 28, 43, 61] =
= SumRule =
    total: 11238513       excl:4984666   INCL: 6253847
 time: 71 sec.     [TOTAL] excl: 4984666, INCL: 6253847


★ ★ ★  14 	 11/16/16.Wed = [28, 41, 61, 63, 65] =
= SumRule =
    total: 11238513       excl:5136946   INCL: 6101567
 time: 72 sec.     [TOTAL] excl: 5136946, INCL: 6101567


★ ★ ★  15 	 11/12/16.Sat = [8, 17, 20, 27, 52] =
= SumRule =
    total: 11238513       excl:5831247   INCL: 5407266
 time: 72 sec.     [TOTAL] excl: 5831247, INCL: 5407266


★ ★ ★  16 	 11/09/16.Wed = [1, 25, 28, 31, 54] =
= SumRule =
    total: 11238513       excl:5863152   INCL: 5375361
 time: 71 sec.     [TOTAL] excl: 5863152, INCL: 5375361


★ ★ ★  17 	 11/05/16.Sat = [21, 31, 50, 51, 69] =
= SumRule =
    total: 11238513       excl:5136408   INCL: 6102105
 time: 74 sec.     [TOTAL] excl: 5136408, INCL: 6102105


★ ★ ★  18 	 11/02/16.Wed = [13, 18, 37, 54, 61] =
 >>> BOOOOO <<< : SumRule
= SumRule =
    total: 11238513       excl:5283236   INCL: 5955277
 time: 68 sec.     [TOTAL] excl: 5283236, INCL: 5955277


★ ★ ★  19 	 10/29/16.Sat = [19, 20, 21, 42, 48] =
= SumRule =
    total: 11238513       excl:4921574   INCL: 6316939
 time: 72 sec.     [TOTAL] excl: 4921574, INCL: 6316939



 ERRORS:     	 3, #: [1, 7, 18]

 denied: 	 5270423;  accepted: 5968090

 time:     	 72; 	 total : 1473 sec.




 */