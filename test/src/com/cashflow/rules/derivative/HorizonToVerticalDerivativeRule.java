package com.cashflow.rules.derivative;

import com.cashflow.rules.AbstractRule;
import com.cashflow.rules.vertical.VerticalGameDeltaRule;
import pb.Chunk;
import pb.Utils;
import pb.file.FileReader;
import pb.statistics.ChunkStConfig;
import pb.statistics.ChunkStatistics;

import java.util.*;

/**


 * @author Ruslan Dauhiala
 */
public class HorizonToVerticalDerivativeRule extends AbstractRule
{

	// kGap = 6 kInclGap = 5  ~2.2m    2 BOO
	// kGap = 3 kInclGap = 10  ~2.1m    2 BOO
	// kGap = 3 kInclGap = 12  ~2.5m    2 BOO
	// kGap = 3 kInclGap = 15  ~3.2m    3 BOO
	//

	public final static int kGap = 5;       // 10=>4  // 6=>2
	private final int kIncExcGap = 5;		//  5=>2err

	//public final static boolean kCheckFirstDerivative = false;
	Map<Integer, _Deltas> myVertDeltaMap;

	class _Deltas
	{
		Map<Date, List<Integer>> myHVDeltaMap;
		Set<List<Integer>> myDeltaHVH1Set;
		Set<List<Integer>> myDeltaHVH2Set;
		Set<Integer> myDeltaHVH3IncSet;
		Set<Integer> myDeltaHVH3ExcSet;
		Set<List<Integer>> myVDeltaChunkNotInOrderSet;
		Set<List<Integer>> myHVDeltaSet;
		Set<List<Integer>> myHVDelta3ChunkSet;
		Set<List<Integer>> myHVDelta2ChunkSetO;
		Set<List<Integer>> myHVH1DeltaChunk3InOrderSet;
		Set<List<Integer>> myHVH1DeltaChunk3NotInOrderSet;
		Set<List<Integer>> myHVH1DeltaChunk2NotInOrderSet;

		Set<List<Integer>> myHVH2DeltaChunkNotInOrderSet;
		//Set<List<Integer>> myHVH2DeltaChunkInOrderSet;

		Map<Date, List<Integer>> myHVH1delta1Map ;
		Map<Date, List<Integer>> myHVH2delta2Map ;
		Map<Date, List<Integer>> myHVH3delta3Map ;

		_Deltas(Map<Date, List<Integer>> theMap, int theGap)
		{
			myHVDeltaMap = DeltaRule.getVerticalDelta(theMap, theGap);

//			Map<Date, List<Integer>> myHVH1delta1Map = DeltaRule.getDeltaMap(myHVDeltaMap);
//			Map<Date, List<Integer>> myHVH2delta2Map = DeltaRule.getDeltaMap(myHVH1delta1Map);
//			Map<Date, List<Integer>> myHVH3delta3Map = DeltaRule.getDeltaMap(myHVH2delta2Map);
			myDeltaHVH3ExcSet = new HashSet<>();
			myDeltaHVH3IncSet = new HashSet<>();
			myHVH1delta1Map = DeltaRule.getDeltaMap(myHVDeltaMap);
			myHVH2delta2Map = DeltaRule.getDeltaMap(myHVH1delta1Map);
			myHVH3delta3Map = DeltaRule.getDeltaMap(myHVH2delta2Map);

			myDeltaHVH1Set = new HashSet<>(myHVH1delta1Map.values());
			myDeltaHVH2Set = new HashSet<>(myHVH2delta2Map.values());


			for (int i = kIncExcGap; i < myHVH3delta3Map.size(); i++)
			{
				myDeltaHVH3IncSet.add(myHVH3delta3Map.get(myDates.get(i)).get(0));
			}
			for (int i = 0; i < kIncExcGap; i++)
			{
				myDeltaHVH3IncSet.remove(myHVH3delta3Map.get(myDates.get(i)).get(0));
			}
			myDeltaHVH3IncSet.removeAll(Utils.getAgoInt(myHVH3delta3Map, 5, myDates));

//			List<Integer> aGapList = Utils.getGapBetweenSameNumbers(myHVH3delta3Map.values());
//			for (int i = 0; i < 5; i++)
//			{
//				if (-1 < aGapList.get(i) && aGapList.get(i) < myDates.size())
//				{
//					myDeltaHVH3IncSet.remove(myHVH3delta3Map.get(myDates.get(aGapList.get(i))).get(0));
//				}
//			}

			myHVDeltaSet = new HashSet<>(myHVDeltaMap.values());
// ok, but hide for the speed
			myHVDelta3ChunkSet = getChunkSet(myHVDeltaMap, new ChunkStConfig(3, true, 200));

			// pilot 	1 =0.5M 0err; 			2= +.9M 2err 11,13;
			myHVDelta2ChunkSetO = Utils.getAgoChunks(myHVDeltaMap, 1, new ChunkStConfig(2, true), myDates);

			myHVH1DeltaChunk3InOrderSet = getChunkSet(myHVH1delta1Map,new ChunkStConfig(3, true, 300));
			myHVH1DeltaChunk3NotInOrderSet = getChunkSet(myHVH1delta1Map,new ChunkStConfig(3, false, 100));

			// pilot
			myHVH1DeltaChunk2NotInOrderSet = getChunkSet(myHVH1delta1Map,new ChunkStConfig(2, false, 3));
			myHVH2DeltaChunkNotInOrderSet = getChunkSet(myHVH2delta2Map,new ChunkStConfig(2, false, 50));

			//myHVH2DeltaChunkInOrderSet = getChunkSet(myHVH2delta2Map,new ChunkStConfig(2, true, 40));

		}
	}

	Map<Date, List<Integer>> myDeltaMap;
//	Map<Date, List<Integer>> myVertDeltaMap;

	public HorizonToVerticalDerivativeRule(Map<Date, List<Integer>> theGamesMap)
	{
		super(theGamesMap);
	}


	@Override
	public void setup()
	{
		myDeltaMap = DeltaRule.getDeltaMap(myGamesMap);
		myVertDeltaMap = new LinkedHashMap<>();

		for (int i = 1; i <= kGap; i++)
		{
			myVertDeltaMap.put(i, new _Deltas(myDeltaMap, i));
		}
	}

//	int myCount = 0;

//	private boolean gameChunkCheck(List<List<Integer>> theGameChunk, Set<List<Integer>> theExistingChunk)
//	{
//		for (Chunk aChunk : theGameChunk)
//		{
//			if (theExistingChunk.contains(aChunk))
//			{
//				//myCount++;
//				return true;
//			}
//		}
//		return false;
//	}

//	@Override
//	public void printReport()
//	{
//		super.printReport();
//		System.out.println(" == new chunk method count : " + myCount);
//	}

	@Override
	public boolean accept(List<Integer> thePotentialGame)
	{
		int aNum = 0;
		List<Integer> aHDelta = Utils.getGameHDelta(thePotentialGame);
		for (int i : myVertDeltaMap.keySet())
		{
			//DeltaRule.getVerticalDelta(theMap, theGap);
			Date aDate = myDates.get(i-1);
			List<Integer> aHVDelta = VerticalGameDeltaRule.getVDelta(aHDelta, myDeltaMap.get(aDate));

			List<Integer> aHVH1Delta = Utils.getGameHDelta(aHVDelta);
			List<Integer> aHVH2Delta = Utils.getGameHDelta(aHVH1Delta);
			Integer aHVH3Delta = Utils.getGameHDelta(aHVH2Delta).get(0);

			_Deltas aDeltaO = myVertDeltaMap.get(i);

			boolean aFound = aDeltaO.myHVDeltaSet.contains(aHVDelta);

			//aFound = aFound || aDeltaO.myDeltaHVH3ExcSet.contains(aHVH3Delta);
			aFound = aFound || !aDeltaO.myDeltaHVH3IncSet.contains(aHVH3Delta);

			aFound = aFound || gameChunkCheck(Utils.getArrayChunks(aHVDelta, 3), aDeltaO.myHVDelta3ChunkSet);
			// pilot
			aFound = aFound || gameChunkCheck(Utils.getArrayChunks(aHVDelta, 2), aDeltaO.myHVDelta2ChunkSetO);

			aFound = aFound || gameChunkCheck(Utils.getAllChunks(aHVH1Delta, 3), aDeltaO.myHVH1DeltaChunk3NotInOrderSet);
			aFound = aFound || gameChunkCheck(Utils.getArrayChunks(aHVH1Delta, 3), aDeltaO.myHVH1DeltaChunk3InOrderSet);

			aFound = aFound || gameChunkCheck(Utils.getAllChunks(aHVH1Delta, 2), aDeltaO.myHVH1DeltaChunk2NotInOrderSet);
//
			aFound = aFound || gameChunkCheck(Utils.getAllChunks(aHVH2Delta, 2), aDeltaO.myHVH2DeltaChunkNotInOrderSet);
			//aFound = aFound || gameChunkCheck(Utils.getArrayChunks(aHVH2Delta, 2), aDeltaO.myHVH2DeltaChunkInOrderSet);

			if (aFound)
			{
				trackResult(false);
				return false;
			}

			aNum++;
//			if (!kCheckFirstDerivative ||
//					  !aDeltaO.myDeltaHVH1Set.contains(aHVH1Delta)
//				//&& !contNotInOrder(aDeltaO.myDeltaHVH1Set, aDelta)
//					  )
//			{
//				List<Integer> aGameHVH2Delta = Utils.getGameHDelta(aHVH1Delta);
//				//if (!aDeltaO.myDeltaHVH2Set.contains(aDelta) && !contNotInOrder(aDeltaO.myDeltaHVH2Set, aDelta))
//				{
//					List<Integer> aGameHVH3Delta = Utils.getGameHDelta(aGameHVH2Delta);
//					if (aDeltaO.myDeltaHVH3Set.contains(aGameHVH3Delta))
//					{
//						aNum++;
////						trackResult(true);
////						return true;
//					} else
//					{
//						trackResult(false);
//						return false;
//					}
//				}
//			}
//			else
//			{
//				trackResult(false);
//				return false;
//			}
		}

		boolean aRes = aNum == myVertDeltaMap.size();
		//System.out.println(thePotentialGame);
		trackResult(aRes);
		return aRes;
	}

	public static void main(String[] args)
	{
		Map<Date, List<Integer>> myAllTheFun = new FileReader().loadFromFile("/Users/jet/Documents/pb/pb.txt");

		//new HorizonToVerticalDerivativeRule(myAllTheFun).test();
		new HorizonToVerticalDerivativeRule(myAllTheFun).trueTest();

	}

	private void trueTest()
	{
		for (int i : myVertDeltaMap.keySet())
		{
			System.out.println(" ============================= ");
			System.out.println(" ========= " + i + " ========= ");
			System.out.println(" ======                 ====== ");

//			System.out.println("===  HV delta stat ===");
//			ChunkStatistics.getAllChunkStats(myVertDeltaMap.get(i).myHVDeltaMap);
//			System.out.println("========================\n\n");

//			System.out.println("---  HVH delta`1 stat ---");
//			ChunkStatistics.getAllChunkStats(myVertDeltaMap.get(i).myHVH1delta1Map);
//			System.out.println("------------------------\n\n");
//
//			System.out.println("---  HVH delta`2 stat ---");
//			ChunkStatistics.getAllChunkStats(myVertDeltaMap.get(i).myHVH2delta2Map);
//			System.out.println("------------------------\n\n");
//
			System.out.println("---  HVH delta`3 stat ---");
			ChunkStatistics.getAllChunkStats(myVertDeltaMap.get(i).myHVH3delta3Map);
			System.out.println("------------------------\n\n");

		}

	}



}


/*



★ ★ ★  0 	 01/04/17.Wed = [16, 17, 29, 41, 42] =
= HorizonToVerticalDerivativeRule =
    total: 11238513       excl:4049892   INCL: 7188621
 time: 93 sec.     [TOTAL] excl: 4049892, INCL: 7188621

===============



★ ★ ★  1 	 12/31/16.Sat = [1, 3, 28, 57, 67] =
 >>> BOOOOO <<< : HorizonToVerticalDerivativeRule
= HorizonToVerticalDerivativeRule =
    total: 11238513       excl:4864565   INCL: 6373948
 time: 87 sec.     [TOTAL] excl: 4864565, INCL: 6373948

===============



★ ★ ★  2 	 12/28/16.Wed = [16, 23, 30, 44, 58] =
= HorizonToVerticalDerivativeRule =
    total: 11238513       excl:4831394   INCL: 6407119
 time: 86 sec.     [TOTAL] excl: 4831394, INCL: 6407119

===============



★ ★ ★  3 	 12/24/16.Sat = [28, 38, 42, 51, 52] =
= HorizonToVerticalDerivativeRule =
    total: 11238513       excl:4581168   INCL: 6657345
 time: 82 sec.     [TOTAL] excl: 4581168, INCL: 6657345

===============



★ ★ ★  4 	 12/21/16.Wed = [25, 33, 40, 54, 68] =
= HorizonToVerticalDerivativeRule =
    total: 11238513       excl:4599044   INCL: 6639469
 time: 80 sec.     [TOTAL] excl: 4599044, INCL: 6639469

===============



★ ★ ★  5 	 12/17/16.Sat = [1, 8, 16, 40, 48] =
 >>> BOOOOO <<< : HorizonToVerticalDerivativeRule
= HorizonToVerticalDerivativeRule =
    total: 11238513       excl:4793385   INCL: 6445128
 time: 81 sec.     [TOTAL] excl: 4793385, INCL: 6445128

===============



★ ★ ★  6 	 12/14/16.Wed = [18, 26, 37, 39, 66] =
= HorizonToVerticalDerivativeRule =
    total: 11238513       excl:4509513   INCL: 6729000
 time: 84 sec.     [TOTAL] excl: 4509513, INCL: 6729000

===============



★ ★ ★  7 	 12/10/16.Sat = [12, 21, 32, 44, 66] =
= HorizonToVerticalDerivativeRule =
    total: 11238513       excl:4384073   INCL: 6854440
 time: 86 sec.     [TOTAL] excl: 4384073, INCL: 6854440

===============



★ ★ ★  8 	 12/07/16.Wed = [41, 48, 49, 53, 64] =
= HorizonToVerticalDerivativeRule =
    total: 11238513       excl:4389467   INCL: 6849046
 time: 84 sec.     [TOTAL] excl: 4389467, INCL: 6849046

===============



★ ★ ★  9 	 12/03/16.Sat = [8, 10, 26, 27, 33] =
= HorizonToVerticalDerivativeRule =
    total: 11238513       excl:4429733   INCL: 6808780
 time: 90 sec.     [TOTAL] excl: 4429733, INCL: 6808780

===============



★ ★ ★  10 	 11/30/16.Wed = [3, 14, 18, 25, 45] =
= HorizonToVerticalDerivativeRule =
    total: 11238513       excl:4498309   INCL: 6740204
 time: 88 sec.     [TOTAL] excl: 4498309, INCL: 6740204

===============



★ ★ ★  11 	 11/26/16.Sat = [17, 19, 21, 37, 44] =
= HorizonToVerticalDerivativeRule =
    total: 11238513       excl:4364527   INCL: 6873986
 time: 89 sec.     [TOTAL] excl: 4364527, INCL: 6873986

===============



★ ★ ★  12 	 11/23/16.Wed = [7, 32, 41, 47, 61] =
= HorizonToVerticalDerivativeRule =
    total: 11238513       excl:4291206   INCL: 6947307
 time: 86 sec.     [TOTAL] excl: 4291206, INCL: 6947307

===============



★ ★ ★  13 	 11/19/16.Sat = [16, 24, 28, 43, 61] =
= HorizonToVerticalDerivativeRule =
    total: 11238513       excl:4389228   INCL: 6849285
 time: 88 sec.     [TOTAL] excl: 4389228, INCL: 6849285

===============



★ ★ ★  14 	 11/16/16.Wed = [28, 41, 61, 63, 65] =
= HorizonToVerticalDerivativeRule =
    total: 11238513       excl:4535015   INCL: 6703498
 time: 88 sec.     [TOTAL] excl: 4535015, INCL: 6703498

===============



★ ★ ★  15 	 11/12/16.Sat = [8, 17, 20, 27, 52] =
= HorizonToVerticalDerivativeRule =
    total: 11238513       excl:4756059   INCL: 6482454
 time: 91 sec.     [TOTAL] excl: 4756059, INCL: 6482454

===============



★ ★ ★  16 	 11/09/16.Wed = [1, 25, 28, 31, 54] =
= HorizonToVerticalDerivativeRule =
    total: 11238513       excl:4831385   INCL: 6407128
 time: 98 sec.     [TOTAL] excl: 4831385, INCL: 6407128

===============



★ ★ ★  17 	 11/05/16.Sat = [21, 31, 50, 51, 69] =
= HorizonToVerticalDerivativeRule =
    total: 11238513       excl:5256990   INCL: 5981523
 time: 97 sec.     [TOTAL] excl: 5256990, INCL: 5981523

===============



★ ★ ★  18 	 11/02/16.Wed = [13, 18, 37, 54, 61] =
 >>> BOOOOO <<< : HorizonToVerticalDerivativeRule
= HorizonToVerticalDerivativeRule =
    total: 11238513       excl:5325085   INCL: 5913428
 time: 101 sec.     [TOTAL] excl: 5325085, INCL: 5913428

===============



★ ★ ★  19 	 10/29/16.Sat = [19, 20, 21, 42, 48] =
= HorizonToVerticalDerivativeRule =
    total: 11238513       excl:5129071   INCL: 6109442
 time: 82 sec.     [TOTAL] excl: 5129071, INCL: 6109442

===============




TOTAL time: 1779 sec.



 */