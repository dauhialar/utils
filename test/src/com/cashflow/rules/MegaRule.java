package com.cashflow.rules;

import com.cashflow.rules.derivative.DeltaRule;
import com.cashflow.rules.derivative.SumRule;
import pb.Chunk;
import pb.Utils;
import pb.file.FileReader;
import pb.statistics.ChunkStConfig;
import pb.statistics.ChunkStatistics;

import java.util.*;

/**
 * @author Ruslan Dauhiala
 */
public class MegaRule extends AbstractRule
{
	Set<List<Integer>> myMMChunk4;
	Set<List<Integer>> myMMChunk3NotO;
	Set<List<Integer>> myMMChunk3O;
	Set<List<Integer>> myMMChunk2NotO;
	Set<List<Integer>> myMMChunk2O;

	Set<List<Integer>> myD1Chunk4NotO;
	Set<List<Integer>> myD1Chunk3NotO;

	//Set<List<Integer>> myD1Chunk4O;
	Set<List<Integer>> myD1Chunk3O;
	Set<List<Integer>> myD1Chunk2O;


	Set<List<Integer>> myD2Chunk3;
	Set<List<Integer>> myD2Chunk2NotO;
	Set<List<Integer>> myD2Chunk2O;

	Set<List<Integer>> myD3Chunk2NotO;
	Set<List<Integer>> myD3Chunk2O;
	Set<Integer> myD4Chunk1;

	Set<List<Integer>> mySum2Chunk4Set;
	Set<List<Integer>> mySum2Chunk3SetNotO;
	Set<List<Integer>> mySum2Chunk2SetNotO;
	Set<List<Integer>> mySum2Chunk3SetO;
	Set<List<Integer>> mySum2Chunk2SetO;

	Set<List<Integer>> mySum3Chunk3Set;
	Set<List<Integer>> mySum3Chunk2Set;

	Set<List<Integer>> mySum4Chunk2Set;
	Set<Integer> mySum5Chunk1Set;

	//Set<List<Integer>> mySum2Der1Chunk3SetNotO;
	Set<List<Integer>> mySum2Der1Chunk2Set;
	Set<List<Integer>> mySum2Der1Chunk3O;

	//Set<List<Integer>> mySum2;


	public MegaRule(Map<Date, List<Integer>> theGamesMap, Map<Date, List<Integer>> theMMMap2)
	{
		super(theGamesMap, theMMMap2);
		//myMMGamesMap = Utils.replaceDates(myDates, theMMMap2);
	}



	@Override
	public boolean accept(List<Integer> thePotentialGame)
	{
		boolean aFound = false;


		// 750k ? 1 err
		List<Integer> aSum5List = SumRule.getSumList(thePotentialGame, 5);
		aFound = aFound || !mySum5Chunk1Set.contains(aSum5List.get(0));

//		//33k? 1 err ok  sucks!
//		List<Integer> aSum4List = SumRule.getSumList(thePotentialGame, 4);
//		aFound = aFound || gameChunkCheck(Utils.getAllChunks(aSum4List, 2), mySum4Chunk2Set);

		// 120k
		List<Integer> aSum3List = SumRule.getSumList(thePotentialGame, 3);
		aFound = aFound || gameChunkCheck(Utils.getAllChunks(aSum3List, 3), mySum3Chunk3Set);
		aFound = aFound || gameChunkCheck(Utils.getAllChunks(aSum3List, 2), mySum3Chunk2Set);
////

		List<Integer> aSum2List = SumRule.getSumList(thePotentialGame, 2);
		// + 240k
		List<Integer> aSum2Der1 = Utils.getGameHDelta(aSum2List);
		aFound = aFound || gameChunkCheck(Utils.getArrayChunks(aSum2Der1, 3), mySum2Der1Chunk3O);
		//aFound = aFound || gameChunkCheck(Utils.getAllChunks(aSum2Der1, 3), mySum2Der1Chunk3SetNotO);
		aFound = aFound || gameChunkCheck(Utils.getAllChunks(aSum2Der1, 2), mySum2Der1Chunk2Set);

		// === +800k 0 errors
		aFound = aFound || gameChunkCheck(Utils.getAllChunks(aSum2List, 4), mySum2Chunk4Set);
		aFound = aFound || gameChunkCheck(Utils.getAllChunks(aSum2List, 3), mySum2Chunk3SetNotO);
		aFound = aFound || gameChunkCheck(Utils.getAllChunks(aSum2List, 2), mySum2Chunk2SetNotO);
		aFound = aFound || gameChunkCheck(Utils.getArrayChunks(aSum2List, 3), mySum2Chunk3SetO);
		aFound = aFound || gameChunkCheck(Utils.getArrayChunks(aSum2List, 2), mySum2Chunk2SetO);
		// ===

		aFound = aFound || gameChunkCheck(Utils.getAllChunks(thePotentialGame, 4), myMMChunk4);
		aFound = aFound || gameChunkCheck(Utils.getAllChunks(thePotentialGame, 3), myMMChunk3NotO);
		aFound = aFound || gameChunkCheck(Utils.getAllChunks(thePotentialGame, 2), myMMChunk2NotO);

		aFound = aFound || gameChunkCheck(Utils.getArrayChunks(thePotentialGame, 3), myMMChunk3O);
		aFound = aFound || gameChunkCheck(Utils.getArrayChunks(thePotentialGame, 2), myMMChunk2O);

		if(!aFound)
		{	// + 700k
			List<Integer> aD1 = Utils.getGameHDelta(thePotentialGame);
			//aFound = aFound || gameChunkCheck(Utils.getAllChunks(aD1, 4), myD1Chunk4NotO);
			aFound = aFound || gameChunkCheck(Utils.getAllChunks(aD1, 3), myD1Chunk3NotO);
			//aFound = aFound || gameChunkCheck(Utils.getArrayChunks(aD1, 4), myD1Chunk4O);
			aFound = aFound || gameChunkCheck(Utils.getArrayChunks(aD1, 3), myD1Chunk3O);
			aFound = aFound || gameChunkCheck(Utils.getArrayChunks(aD1, 2), myD1Chunk2O);
			if (!aFound)
			{	// + 600k
				List<Integer> aD2 = Utils.getGameHDelta(aD1);
				aFound = aFound || gameChunkCheck(Utils.getAllChunks(aD2, 3), myD2Chunk3);
				aFound = aFound || gameChunkCheck(Utils.getAllChunks(aD2, 2), myD2Chunk2NotO);
				aFound = aFound || gameChunkCheck(Utils.getArrayChunks(aD2, 2), myD2Chunk2O);
				if(!aFound)
				{	// + 100k
					List<Integer> aD3 = Utils.getGameHDelta(aD2);
					aFound = aFound || gameChunkCheck(Utils.getAllChunks(aD3, 2), myD3Chunk2NotO);
					//aFound = aFound || gameChunkCheck(Utils.getArrayChunks(aD3, 2), myD3Chunk2O);
					if(!aFound)
					{	//+1.4M
						List<Integer> aD4 = Utils.getGameHDelta(aD3);
						aFound = !myD4Chunk1.contains(aD4.get(0));
					}
				}
			}
		}

		trackResult(!aFound);
		return !aFound;
	}

	public static void main(String args[])
	{
		Map<Date, List<Integer>> myAllTheFun = new FileReader().loadFromFile("/Users/jet/Documents/pb/pb.txt");

		Map<Date, List<Integer>> myMMTheFun = new FileReader().loadFromFile("/Users/jet/Documents/pb/mm.txt");
		myMMTheFun = Utils.replaceDates(new ArrayList<Date>(myAllTheFun.keySet()), myMMTheFun);
		new MegaRule(myAllTheFun, myMMTheFun).test();
	}

	@Override
	public void setup()
	{
		myMMChunk4 = getChunkSet(myMMGamesMap, new ChunkStConfig(4, false, myMMGamesMap.size())); // [435, 723, 723, 723,
		myMMChunk3NotO = getChunkSet(myMMGamesMap, new ChunkStConfig(3, false, 13)); // 10 [5, 14, 30, 30, 35, 54, 54, 55, 59, 66, 67
		myMMChunk2NotO = getChunkSet(myMMGamesMap, new ChunkStConfig(2, false, 2));  //  [1, 2, 3, 3, 4, 5, 5, 5, 7, 8, 8, 8, 9, 9, 9, 10, 10,
		// pilot ??   5 =  4err, #: [7, 14, 17, 19] AVERAGE ACCEPTED: 	 5366726
		//myMMChunk2NotO.addAll(Utils.getAgoChunks(myMMGamesMap, 5, new ChunkStConfig(2, false), myDates));


		myMMChunk3O = getChunkSet(myMMGamesMap, new ChunkStConfig(3, true, 50));     //   [55, 78, 105, 126, 291, 308, 320, 363, 407,
		myMMChunk2O = getChunkSet(myMMGamesMap, new ChunkStConfig(2, true, 2 ));     // [55, 78, 105, 126, 291, 308, 320, 363, 407,

		Map<Date, List<Integer>> aMMD1Map = DeltaRule.getDeltaMap(myMMGamesMap);

		// because checking whole aMMD2Map
		//myD1Chunk4NotO = getChunkSet(aMMD1Map, new ChunkStConfig(4, false, myMMGamesMap.size())); // [723, 723, 723, 723, 723,
		myD1Chunk3NotO = getChunkSet(aMMD1Map, new ChunkStConfig(3, false, 11));      				// [12, 19, 38, 70, 76, 88, 90, 100,

		//myD1Chunk4O = getChunkSet(aMMD1Map, new ChunkStConfig(4, true, aMMD1Map.size()));     // [70, 90, 101, 119, 147, 156, 263, 284,
		myD1Chunk3O = getChunkSet(aMMD1Map, new ChunkStConfig(3, true, 69));     // [70, 90, 101, 119, 147, 156, 263, 284,
		myD1Chunk2O = getChunkSet(aMMD1Map, new ChunkStConfig(2, true, 1));      // [2, 2, 2, 3, 3, 3, 3,


		Map<Date, List<Integer>> aMMD2Map = DeltaRule.getDeltaMap(aMMD1Map);
		myD2Chunk3 = getChunkSet(aMMD2Map, new ChunkStConfig(3, false, aMMD2Map.size()));   // [355, 723, 723, 723,
		myD2Chunk2NotO = getChunkSet(aMMD2Map, new ChunkStConfig(2, false, 3));    // 		[1, 5, 5, 11, 11, 11,
		myD2Chunk2O = getChunkSet(aMMD2Map, new ChunkStConfig(2, true, 5));    // [5, 11, 11, 17, 19, 21, 28, 30, 37,

		Map<Date, List<Integer>> aMMD3Map = DeltaRule.getDeltaMap(aMMD2Map);
		myD3Chunk2NotO = getChunkSet(aMMD3Map, new ChunkStConfig(2, false, 50)); // [14, 53, 78, 106, 221, 341, 355, 494, 553, 723,
		//myD3Chunk2O = getChunkSet(aMMD3Map, new ChunkStConfig(2, true, 50));     // [14, 53, 78, 106, 221, 341, 355, 494, 553, 723, 723, 723,

		Map<Date, List<Integer>> aMMD4Map = DeltaRule.getDeltaMap(aMMD3Map);
		myD4Chunk1 = Utils.getIntSet(aMMD4Map);
		for (int i = 0; i < 5; i++)   // [3, 6, 6, 11, 14, 14, 14, 15, 18,
		{
			myD4Chunk1.remove(aMMD4Map.get(myDates.get(i)).get(0));
		}
		// pilot
		myD4Chunk1.removeAll(Utils.getAgoInt(aMMD4Map, 10, myDates));

		// +1m?
		Map<Date, List<Integer>> aMMSum2Map = SumRule.getSumMap(myMMGamesMap, 2);
		mySum2Chunk4Set = getChunkSet(aMMSum2Map, new ChunkStConfig(4, false, aMMSum2Map.size()));  // [723, 723, 723, 723, 723, 723, 723, 723
		mySum2Chunk3SetNotO = getChunkSet(aMMSum2Map, new ChunkStConfig(3, false, 195));            // [145, 285, 723, 723, 723, 723, 723, 723, 723,
		mySum2Chunk2SetNotO = getChunkSet(aMMSum2Map, new ChunkStConfig(2, false, 7));              // [7, 8, 12, 14, 14, 16, 17, 25, 25,

		mySum2Chunk3SetO = getChunkSet(aMMSum2Map, new ChunkStConfig(3, true, aMMSum2Map.size()));  // [145, 723, 723, 723, 723, 723, 723,
		mySum2Chunk2SetO = getChunkSet(aMMSum2Map, new ChunkStConfig(2, true, 24));                 // [25, 52, 53, 55, 70, 71, 78, 84, 88, 90, 93, 98, 105, 118,

		Map<Date, List<Integer>> aMMSum3Map = SumRule.getSumMap(myMMGamesMap, 3);
		mySum3Chunk3Set = getChunkSet(aMMSum3Map, new ChunkStConfig(3, true, aMMSum2Map.size()));   // [278, 723, 723, 723, 723, 723,
		mySum3Chunk2Set = getChunkSet(aMMSum3Map, new ChunkStConfig(2, true, 22));                  // [23, 24, 49, 73, 92, 120,

		Map<Date, List<Integer>> aMMSum4Map = SumRule.getSumMap(myMMGamesMap, 4);
		mySum4Chunk2Set = getChunkSet(aMMSum4Map, new ChunkStConfig(2, true, 30));                  // [3, 25, 32, 64, 114, 286, 336, 433,

		Map<Date, List<Integer>> aMMSum5Map = SumRule.getSumMap(myMMGamesMap, 5);
		mySum5Chunk1Set = Utils.getIntSet(aMMSum5Map);                  // [2, 3, 3, 4, 5, 5, 5, 7, 10, 11, 18,

		// pilot
		mySum5Chunk1Set.removeAll(Utils.getAgoInt(aMMSum5Map, 10, myDates));
//		mySum5Chunk1Set.remove(aMMSum5Map.get(myDates.get(0)));
//		mySum5Chunk1Set.remove(aMMSum5Map.get(myDates.get(1)));

		Map<Date, List<Integer>> aSum2Der1Map = DeltaRule.getDeltaMap(aMMSum2Map);
		//	mySum2Der1Chunk3SetNotO =  getChunkSet(aSum2Der1Map, new ChunkStConfig(3, false, 129));  // [130, 356, 398, 910, 2002, 2002, 2002, 2002,
		mySum2Der1Chunk2Set =  getChunkSet(aSum2Der1Map, new ChunkStConfig(2, false, 1));        // [2, 2, 3, 3, 7, 8, 8,
		// pilot ??    10= TOTAL ERRORS:     	 5, #: [2, 11, 14, 17, 19] AVERAGE ACCEPTED: 	 6047158
		//              5= 		 ERRORS: 		 4, #: [2, 14, 17, 19]             ACCEPTED: 	 6187105
		//mySum2Der1Chunk2Set.addAll(Utils.getAgoChunks(aSum2Der1Map, 5, new ChunkStConfig(2, false), myDates));

		mySum2Der1Chunk3O =  getChunkSet(aSum2Der1Map, new ChunkStConfig(3, true, aSum2Der1Map.size()));  // [723, 723, 723, 723, 723, 723, 723,

	}

	private void test()
	{
		Map<Date, List<Integer>> aPP = Utils.get5SizeMap(myGamesMap);
		Map<Date, List<Integer>> aMM = Utils.get5SizeMap(myMMGamesMap);

//		ChunkStatistics.getAllChunkStats(aMM, aPP);
//
		Map<Date, List<Integer>> aPPDer1 = DeltaRule.getDeltaMap(aPP);
		Map<Date, List<Integer>> aMMDer1 = DeltaRule.getDeltaMap(aMM);
//		ChunkStatistics.getAllChunkStats(aMMDer1, aPPDer1);
//
//
		Map<Date, List<Integer>> aPPDer2 = DeltaRule.getDeltaMap(aPPDer1);
		Map<Date, List<Integer>> aMMDer2 = DeltaRule.getDeltaMap(aMMDer1);
//		ChunkStatistics.getAllChunkStats(aMMDer2, aPPDer2);
//
		Map<Date, List<Integer>> aPPDer3 = DeltaRule.getDeltaMap(aPPDer2);
		Map<Date, List<Integer>> aMMDer3 = DeltaRule.getDeltaMap(aMMDer2);
//		ChunkStatistics.getAllChunkStats(aMMDer3, aPPDer3);

		Map<Date, List<Integer>> aPPDer4 = DeltaRule.getDeltaMap(aPPDer3);
		Map<Date, List<Integer>> aMMDer4 = DeltaRule.getDeltaMap(aMMDer3);
//		ChunkStatistics.getAllChunkStats(aMMDer4, aPPDer4);

		int aSize = 5;
		Map<Date, List<Integer>> aPPSumMap = SumRule.getSumMap(myGamesMap, aSize);
		Map<Date, List<Integer>> aMMSumMap = SumRule.getSumMap(myMMGamesMap, aSize);
		System.out.println("pp: " + aPPSumMap.values());
		System.out.println("mm: " + aMMSumMap.values());
		ChunkStatistics.getAllChunkStats(aMMSumMap, aPPSumMap);
//
//		Map<Date, List<Integer>> aPPDer1 = DeltaRule.getDeltaMap(aPPSumMap);
//		Map<Date, List<Integer>> aMMDer1 = DeltaRule.getDeltaMap(aMMSumMap);
//		ChunkStatistics.getAllChunkStats(aMMDer1, aPPDer1);

//		Map<Date, List<Integer>> aMMSum5Map = SumRule.getSumMap(myMMGamesMap, 5);
//		Map<Date, List<Integer>> aPPSum5Map = SumRule.getSumMap(myGamesMap, 5);
//		ChunkStatistics.getAllChunkStats(aMMSum5Map, aPPSum5Map);
	}
}

/*



★ ★ ★  0 	 01/04/17.Wed = [16, 17, 29, 41, 42] =
= MegaRule =
    total: 11238513       excl:4906928   INCL: 6331585
 time: 132 sec.     [TOTAL] excl: 4906928, INCL: 6331585


★ ★ ★  1 	 12/31/16.Sat = [1, 3, 28, 57, 67] =
= MegaRule =
    total: 11238513       excl:4906799   INCL: 6331714
 time: 131 sec.     [TOTAL] excl: 4906799, INCL: 6331714


★ ★ ★  2 	 12/28/16.Wed = [16, 23, 30, 44, 58] =
= MegaRule =
    total: 11238513       excl:4749694   INCL: 6488819
 time: 123 sec.     [TOTAL] excl: 4749694, INCL: 6488819


★ ★ ★  3 	 12/24/16.Sat = [28, 38, 42, 51, 52] =
= MegaRule =
    total: 11238513       excl:5026917   INCL: 6211596
 time: 122 sec.     [TOTAL] excl: 5026917, INCL: 6211596


★ ★ ★  4 	 12/21/16.Wed = [25, 33, 40, 54, 68] =
= MegaRule =
    total: 11238513       excl:5051221   INCL: 6187292
 time: 122 sec.     [TOTAL] excl: 5051221, INCL: 6187292


★ ★ ★  5 	 12/17/16.Sat = [1, 8, 16, 40, 48] =
= MegaRule =
    total: 11238513       excl:4932433   INCL: 6306080
 time: 117 sec.     [TOTAL] excl: 4932433, INCL: 6306080


★ ★ ★  6 	 12/14/16.Wed = [18, 26, 37, 39, 66] =
= MegaRule =
    total: 11238513       excl:5081864   INCL: 6156649
 time: 131 sec.     [TOTAL] excl: 5081864, INCL: 6156649


★ ★ ★  7 	 12/10/16.Sat = [12, 21, 32, 44, 66] =
= MegaRule =
    total: 11238513       excl:4822459   INCL: 6416054
 time: 128 sec.     [TOTAL] excl: 4822459, INCL: 6416054


★ ★ ★  8 	 12/07/16.Wed = [41, 48, 49, 53, 64] =
= MegaRule =
    total: 11238513       excl:4834603   INCL: 6403910
 time: 114 sec.     [TOTAL] excl: 4834603, INCL: 6403910


★ ★ ★  9 	 12/03/16.Sat = [8, 10, 26, 27, 33] =
= MegaRule =
    total: 11238513       excl:4938918   INCL: 6299595
 time: 113 sec.     [TOTAL] excl: 4938918, INCL: 6299595


★ ★ ★  10 	 11/30/16.Wed = [3, 14, 18, 25, 45] =
= MegaRule =
    total: 11238513       excl:4689062   INCL: 6549451
 time: 115 sec.     [TOTAL] excl: 4689062, INCL: 6549451


★ ★ ★  11 	 11/26/16.Sat = [17, 19, 21, 37, 44] =
= MegaRule =
    total: 11238513       excl:4983899   INCL: 6254614
 time: 113 sec.     [TOTAL] excl: 4983899, INCL: 6254614


★ ★ ★  12 	 11/23/16.Wed = [7, 32, 41, 47, 61] =
= MegaRule =
    total: 11238513       excl:4859953   INCL: 6378560
 time: 112 sec.     [TOTAL] excl: 4859953, INCL: 6378560


★ ★ ★  13 	 11/19/16.Sat = [16, 24, 28, 43, 61] =
= MegaRule =
    total: 11238513       excl:4889365   INCL: 6349148
 time: 114 sec.     [TOTAL] excl: 4889365, INCL: 6349148


★ ★ ★  14 	 11/16/16.Wed = [28, 41, 61, 63, 65] =
 >>> BOOOOO <<< : MegaRule
= MegaRule =
    total: 11238513       excl:5001766   INCL: 6236747
 time: 113 sec.     [TOTAL] excl: 5001766, INCL: 6236747


★ ★ ★  15 	 11/12/16.Sat = [8, 17, 20, 27, 52] =
= MegaRule =
    total: 11238513       excl:4786677   INCL: 6451836
 time: 123 sec.     [TOTAL] excl: 4786677, INCL: 6451836


★ ★ ★  16 	 11/09/16.Wed = [1, 25, 28, 31, 54] =
= MegaRule =
    total: 11238513       excl:4991470   INCL: 6247043
 time: 122 sec.     [TOTAL] excl: 4991470, INCL: 6247043


★ ★ ★  17 	 11/05/16.Sat = [21, 31, 50, 51, 69] =
 >>> BOOOOO <<< : MegaRule
= MegaRule =
    total: 11238513       excl:4918416   INCL: 6320097
 time: 123 sec.     [TOTAL] excl: 4918416, INCL: 6320097


★ ★ ★  18 	 11/02/16.Wed = [13, 18, 37, 54, 61] =
= MegaRule =
    total: 11238513       excl:4892327   INCL: 6346186
 time: 123 sec.     [TOTAL] excl: 4892327, INCL: 6346186


★ ★ ★  19 	 10/29/16.Sat = [19, 20, 21, 42, 48] =
 >>> BOOOOO <<< : MegaRule
= MegaRule =
    total: 11238513       excl:4833705   INCL: 6404808
 time: 119 sec.     [TOTAL] excl: 4833705, INCL: 6404808



TOTAL ERRORS:     	 3, #: [14, 17, 19]

AVERAGE ACCEPTED: 	 6333589

AVERAGE TIME:     	 120; 	 TOTAL time: 2427 sec.


 */