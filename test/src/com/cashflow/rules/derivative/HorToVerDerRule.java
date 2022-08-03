package com.cashflow.rules.derivative;

import com.cashflow.rules.AbstractRule;
import com.cashflow.rules.vertical.GamesAgoInColumnRule;
import pb.Chunk;
import pb.Utils;
import pb.file.FileReader;
import pb.statistics.ChunkStConfig;
import pb.statistics.ChunkStatistics;

import java.util.*;

/**

 * @author Ruslan Dauhiala
 */
public class HorToVerDerRule extends AbstractRule
{
	public final static int kGap = 1;
	public final static boolean kCheckFirstDerivative = true;

	HashMap<Integer, _Deltas> myVertDeltaMap;

	Map<Date, List<Integer>> myTestGamesMap;
	Map<Date, List<Integer>> myDeltaMap;
	//	Map<Date, List<Integer>> myVertDeltaMap;
	final static Date myDate = new Date(System.currentTimeMillis());


	Set<List<Integer>> my1stVDer2ChunkNotInOrderSet;
	Set<List<Integer>> my1stVDer2ChunkInOrderSet;
	Set<List<Integer>> my1stVDer3ChunkNotInOrderSet;
	Set<List<Integer>> my1stVDer3ChunkInOrderSet;
	Set<List<Integer>> my1stVDer4ChunkNotInOrderSet;

	Set<List<Integer>> my2ndHDer2ChunkNotInOrderSet;
	Set<List<Integer>> my2ndHDer2ChunkInOrderSet;

	Set<List<Integer>> my2ndVDer2ChunkNotInOrderSet;
	Set<List<Integer>> my2ndVDer2ChunkInOrderSet;


	Set<List<Integer>> my3rdVDer2ChunkNotInOrderSet;
	Set<List<Integer>> my3rdVDer1ChunkNotInOrderSet;

	Set<List<Integer>> my3rdHDer2ChunkInOrderSet;
	Set<List<Integer>> my3rdHDer2ChunkNotInOrderSet;
	Set<List<Integer>> my3rdHDer1ChunkNotInOrderSet;

	Set<List<Integer>> myVDeltaMap1Set;

	Set<List<Integer>> myVDeltaMap2Set;
	Set<List<Integer>> myHDeltaMap2Set;

	Set<List<Integer>> myVDeltaMap3Set;
	//Set<List<Integer>> myHDeltaMap3Set;

	static boolean myTestMode = false;
	boolean myLastV4Positive;
	boolean myLastH4Positive;
	class _Deltas
	{
		Map<Date, List<Integer>> myHDeltaMap1;
		Map<Date, List<Integer>> myHVDeltaMap1;

		Map<Date, List<Integer>> myHVHDeltaMap2;
		Map<Date, List<Integer>> myHVHVDeltaMap2;

		Map<Date, List<Integer>> myHDeltaMap3;
		Map<Date, List<Integer>> myVDeltaMap3;

		Map<Date, List<Integer>> myHDeltaMap4;
		Map<Date, List<Integer>> myVDeltaMap4;

		Map<Date, List<Integer>> myVDeltaMap;
		Set<List<Integer>> myDelta1Set ;
		Set<List<Integer>> myDelta2Set ;
		Set<List<Integer>> myDelta3Set ;

		Set<Integer> myHDelta4Set ;
		Set<Integer> myVDelta4Set ;

		Set<List<Integer>> myHDelta1Set ;
		Set<List<Integer>> myVDelta1Set ;

		Set<List<Integer>> myHDelta2Set ;
		Set<List<Integer>> myVDelta2Set ;


		_Deltas(Map<Date, List<Integer>> theMap, int theGap)
		{
			myHDeltaMap1 = DeltaRule.getDeltaMap(theMap);
			myHVDeltaMap1 = DeltaRule.getVerticalDelta(myHDeltaMap1);
			//myHVDeltaMap1 = getVerticalDeltaNtimes(myHDeltaMap1, theGap);

			myHVHDeltaMap2 = DeltaRule.getDeltaMap(myHVDeltaMap1);
			myHVHVDeltaMap2 = DeltaRule.getVerticalDelta(myHVHDeltaMap2);
			//myHVHVDeltaMap2 = getVerticalDeltaNtimes(myHVHDeltaMap2, theGap);


			myHDeltaMap3 = DeltaRule.getDeltaMap(myHVHVDeltaMap2);
			myVDeltaMap3 = DeltaRule.getVerticalDelta(myHDeltaMap3);
			//myVDeltaMap3 = getVerticalDeltaNtimes(myHDeltaMap3, theGap);

			myHDeltaMap4 = DeltaRule.getDeltaMap(myVDeltaMap3);
			myVDeltaMap4 = DeltaRule.getVerticalDelta(myHDeltaMap4);
			//myVDeltaMap4 = getVerticalDeltaNtimes(myHDeltaMap4, theGap);

//			myHDelta1Set = new HashSet<>(myHDeltaMap1.values());
//			myVDelta1Set = new HashSet<>(myHVDeltaMap1.values());

//			myHDelta2Set = new HashSet<>(myHVHDeltaMap2.values());
//			myVDelta2Set = new HashSet<>(myHVHVDeltaMap2.values());


			myHDelta4Set = getLimitedSet(myHDeltaMap4, 27);      // [28, 33, 35, 40, 44, 51,
			List<Integer> aGapList = Utils.getGapBetweenSameNumbers(myHDeltaMap4.values());
			int aMax = myHDelta4Set.size() + 20;     // 20 = +1 err game #17    // 40 = +2 err #5 #11
			for (int i = 0; i < aGapList.size() && myHDelta4Set.size() < aMax; i++)  // 20 =
													// 40 =
			{                            // 70 =
				if (-1 < aGapList.get(i) && aGapList.get(i) < myDates.size())
				{
					myHDelta4Set.add(myHDeltaMap4.get(myDates.get(aGapList.get(i))).get(0));
				} else
				{
					aMax++;
				}
			}


			myVDelta4Set = getLimitedSet(myVDeltaMap4, 17);      // [1, 18, 19, 54, 71, 81, 98, 100,
			int aMaxV = myVDelta4Set.size() + 20;         // 40: 2, 11, 17 +200k
			for (int i = 0; i < aGapList.size() && myVDelta4Set.size() < aMaxV; i++)  // 20 =
			// 40 =
			{                            // 70 =
				if (-1 < aGapList.get(i) && aGapList.get(i) < myDates.size())
				{
					myVDelta4Set.add(myVDeltaMap4.get(myDates.get(aGapList.get(i))).get(0));
				} else
				{
					aMax++;
				}
			}

//			myHDelta4Set = Utils.getIntSet(myHDeltaMap4);
//			myVDelta4Set = Utils.getIntSet(myVDeltaMap4);

//			Map<Date, List<Integer>> myHdelta1Map = DeltaRule.getDeltaMap(myHVDeltaMap);
//			Map<Date, List<Integer>> myHdelta2Map = DeltaRule.getDeltaMap(myHdelta1Map);
//			Map<Date, List<Integer>> myHdelta3Map = DeltaRule.getDeltaMap(myHdelta2Map);
//
//			myDeltaHVH1Set = new HashSet<>(myHdelta1Map.values());
//			myDeltaHVH2Set = new HashSet<>(myHdelta2Map.values());
//			myDeltaHVH3Set = new HashSet<>(myHdelta3Map.values());
		}
	}

	private Set<Integer> getLimitedSet(Map<Date, List<Integer>> theMap, int theGap)
	{
		HashSet aSet = new LinkedHashSet<>();
		int k = 0;
		for (List<Integer> aList : theMap.values())
		{
			aSet.add(aList.get(0));
			if (theGap < ++k)
			{
				break;
			}
		}
		return aSet;
	}


	List<Integer> getIntList(Map<Date, List<Integer>>  theMap)
	{
		List<Integer> aSet = new ArrayList<>();
		for (List<Integer> a : theMap.values())
		{
			aSet.add(a.get(0));
		}
		return aSet;
	}

	Map<Date, List<Integer>> getVerticalDeltaNtimes(Map<Date, List<Integer>> theMap, int theTimes)
	{
		//Map<Date, List<Integer>> aMap = DeltaRule.getVerticalDelta(theMap);
		Map<Date, List<Integer>> aMap = DeltaRule.getVerticalDelta(theMap,theTimes);
		for (int i = 0; i < theTimes - 1;i++)
		{
			aMap = DeltaRule.getVerticalDelta(aMap,theTimes);
		}
		return aMap;
	}

	public HorToVerDerRule(Map<Date, List<Integer>> theGamesMap)
	{
		super(theGamesMap);
	}



	/**

	 myHDeltaMap4 gap stat
	 gap size : 1315 out of 1956
	 10% gap [40 - 830]

	 myHDeltaMap4 gap stat
	 gap size : 1013 out of 1955
	 10% gap [57 - 1010]
	 */
	@Override
	public void setup()
	{
		//myDeltaMap = DeltaRule.getDeltaMap(myGamesMap);
		myVertDeltaMap = new HashMap<>();

		for (int i = 1; i <= kGap; i++)
		{
			myVertDeltaMap.put(i, new _Deltas(myGamesMap, i));
		}

//		List<List<Integer>> my2ndDer2ChunkNotInOrder = Utils.getAllTheChunksTotal(-1, myVertDeltaMap.get(1).myHVHDeltaMap2, Collections.singletonList(new ChunkStConfig(2, false, 10)));
//		List<List<Integer>> my2ndDer2ChunkInOrder = Utils.getAllTheChunksTotal(-1, myVertDeltaMap.get(1).myHVHDeltaMap2, Collections.singletonList(new ChunkStConfig(2, true, 50)));

		my1stVDer4ChunkNotInOrderSet = getChunkSet(myVertDeltaMap.get(1).myHVDeltaMap1, new ChunkStConfig(4, false, myVertDeltaMap.get(1).myHVDeltaMap1.size())); 		// [2001, 2001,
		my1stVDer3ChunkNotInOrderSet = getChunkSet(myVertDeltaMap.get(1).myHVDeltaMap1, new ChunkStConfig(3, false, 26)); 		// [26, 136, 181,
		my1stVDer2ChunkNotInOrderSet = getChunkSet(myVertDeltaMap.get(1).myHVDeltaMap1, new ChunkStConfig(2, false, 1)); 		// 1

		// pilot
		List<Integer> a2ChunkList = ChunkStatistics.getChunkAgoList(new ChunkStConfig(2, false), myVertDeltaMap.get(1).myHVDeltaMap1, null);
		for (int i = 0 ; i < 10; i++)   // 5 +1err  #16 +1M        10 +1err #5 + 700k
		{
			List<Integer> aGame = myVertDeltaMap.get(1).myHVDeltaMap1.get(myDates.get(a2ChunkList.get(i)));
			my1stVDer2ChunkNotInOrderSet.addAll(Utils.getAllChunks(aGame, 2));
		}

		my1stVDer3ChunkInOrderSet = getChunkSet(myVertDeltaMap.get(1).myHVDeltaMap1, new ChunkStConfig(3, true, 220)); 			// [300, 399, 558,
		my1stVDer2ChunkInOrderSet = getChunkSet(myVertDeltaMap.get(1).myHVDeltaMap1, new ChunkStConfig(2, true, 3)); 				// [4, 6, 6, 13, 15, 18,

		my2ndVDer2ChunkNotInOrderSet = getChunkSet(myVertDeltaMap.get(1).myHVHVDeltaMap2, new ChunkStConfig(2, false, 30)); 		// [39, 51, 54, 56, 72
		my2ndVDer2ChunkInOrderSet = getChunkSet(myVertDeltaMap.get(1).myHVHVDeltaMap2, new ChunkStConfig(2, true, 38));  			// [50, 100, 132, 166, 183,

		my3rdVDer2ChunkNotInOrderSet = getChunkSet(myVertDeltaMap.get(1).myVDeltaMap3,new ChunkStConfig(2, false, 1000)); 		// [77, 1464, 1999, 1999, 1999,
		my3rdVDer1ChunkNotInOrderSet = getChunkSet(myVertDeltaMap.get(1).myVDeltaMap3,new ChunkStConfig(1, false, 1)); 		// [1, 2, 2, 2, 3, 4, 5, 7, 8, 8, 9, 9, 14,


		my2ndHDer2ChunkNotInOrderSet = getChunkSet(myVertDeltaMap.get(1).myHVHDeltaMap2,new ChunkStConfig(2, false, 10)); 		// [14, 20, 24, 25, 30, 33
		//my2ndHDer2ChunkInOrderSet = getChunkSet(myVertDeltaMap.get(1).myHVHDeltaMap2, new ChunkStConfig(2, true, 7)); 			// 7

		my3rdHDer2ChunkNotInOrderSet = getChunkSet(myVertDeltaMap.get(1).myHDeltaMap3,new ChunkStConfig(2, false, 600)); 		// [628, 644, 839, 994, 1241,
		my3rdHDer1ChunkNotInOrderSet = getChunkSet(myVertDeltaMap.get(1).myHDeltaMap3,new ChunkStConfig(1, true, 1)); 		// 1
		//my3rdHDer2ChunkInOrderSet = getChunkSet(myVertDeltaMap.get(1).myHDeltaMap3,new ChunkStConfig(2, true, 400)); 			// 344

		//List<List<Integer>> my1stDer2ChunkNotInOrder = Utils.getAllTheChunksTotal(-1, myVertDeltaMap.get(1).myHVDeltaMap1, Collections.singletonList(new ChunkStConfig(2, false, 10)));
		myVDeltaMap1Set = new HashSet<>(myVertDeltaMap.get(1).myHVDeltaMap1.values());

		myVDeltaMap2Set = new HashSet<>(myVertDeltaMap.get(1).myHVHVDeltaMap2.values());
		myHDeltaMap2Set = new HashSet<>(myVertDeltaMap.get(1).myHVHDeltaMap2.values());

		myVDeltaMap3Set = new HashSet<>(myVertDeltaMap.get(1).myVDeltaMap3.values());

//		myHDeltaMap3Set = new HashSet<>();//(myVertDeltaMap.get(1).myHDeltaMap3.values());
//
//		int k = 0;
//		for (List<Integer> aList : myVertDeltaMap.get(1).myHDeltaMap3.values())
//		{
//			myHDeltaMap3Set.add(aList);
//			if (400 < ++k)
//			{
//				break;
//			}
//		}

//		for (int i = 0; i < 40; i++)
//		{
//			myVertDeltaMap.get(1).myHDelta4Set.remove(myVertDeltaMap.get(1).myHDeltaMap4.get(myDates.get(i)).get(0));
//			myVertDeltaMap.get(1).myVDelta4Set.remove(myVertDeltaMap.get(1).myVDeltaMap4.get(myDates.get(i)).get(0));
//		}

//		int aLastV = myVertDeltaMap.get(1).myVDeltaMap4.get(myDates.get(0)).get(0);
//		int aLastH = myVertDeltaMap.get(1).myHDeltaMap4.get(myDates.get(0)).get(0);

//		myLastV4Positive = 0 < aLastV;
//		myLastH4Positive = 0 < aLastH;

		myTestGamesMap = new LinkedHashMap<>();
		myTestGamesMap.put(myDate, null);

		for(int i= 0; i < 4*kGap; i++)
		{
			myTestGamesMap.put(myDates.get(i), myGamesMap.get(myDates.get(i)));
		}
	}


//	@Override
//	public void printReport()
//	{
//		super.printReport();
//		System.out.println(" == new chunk method count : " + myCount);
//	}

	//int myCount = 0;

	@Override
	public boolean accept(List<Integer> thePotentialGame)
	{
		myTestGamesMap.put(myDate, thePotentialGame);
		int i=1;
		boolean aFound = false;

//		for (int i : myVertDeltaMap.keySet())
		{
			_Deltas aTestDeltas = new _Deltas(myTestGamesMap, i);
			_Deltas aExDelta = myVertDeltaMap.get(i);
			List<Integer> aV4Der = aTestDeltas.myVDeltaMap4.get(myDate);
			List<Integer> aH4Der = aTestDeltas.myHDeltaMap4.get(myDate);
//		boolean aV4Pos = 0 < aV4Der.get(0);
//		boolean aH4Pos = 0 < aH4Der.get(0);
//			if (i == 1)
			{
				if (aExDelta.myVDelta4Set.contains(aV4Der.get(0)) ||
						  aExDelta.myHDelta4Set.contains(aH4Der.get(0))
						  )
				{
					trackResult(false);
					return false;
				}
			}
//			else if (aExDelta.myVDelta2Set.contains(aTestDeltas.myHVHVDeltaMap2.get(myDate)) &&
//								 aExDelta.myHDelta2Set.contains(aTestDeltas.myHVHDeltaMap2.get(myDate)))
//			{
//				trackResult(false);
//				return false;
//			}
			aFound = myVDeltaMap1Set.contains(aTestDeltas.myHVDeltaMap1.get(myDate));
			//== new chunk method count : 6810
			aFound = aFound || myVDeltaMap2Set.contains(aTestDeltas.myHVHVDeltaMap2.get(myDate));
			aFound = aFound || myHDeltaMap2Set.contains(aTestDeltas.myHVHDeltaMap2.get(myDate));
			aFound = aFound || myVDeltaMap3Set.contains(aTestDeltas.myVDeltaMap3.get(myDate));
//			aFound = aFound || myHDeltaMap3Set.contains(aTestDeltas.myHDeltaMap3.get(myDate));
			//== new chunk method count : 517413

			if (aFound)
			{
				myCount++;
			}
			aFound = aFound || gameChunkCheck(Utils.getAllChunks(aTestDeltas.myHVHDeltaMap2.get(myDate), 2), my2ndHDer2ChunkNotInOrderSet);
			//aFound = aFound || gameChunkCheck(Utils.getArrayChunks(aTestDeltas.myHVHDeltaMap2.get(myDate), 2), my2ndHDer2ChunkInOrderSet);
			//== new chunk method count : 890614

			aFound = aFound || gameChunkCheck(Utils.getAllChunks(aTestDeltas.myHVHVDeltaMap2.get(myDate), 2), my2ndVDer2ChunkNotInOrderSet);
			aFound = aFound || gameChunkCheck(Utils.getArrayChunks(aTestDeltas.myHVHVDeltaMap2.get(myDate), 2), my2ndVDer2ChunkInOrderSet);
			//== new chunk method count : 1362893

			aFound = aFound || gameChunkCheck(Utils.getAllChunks(aTestDeltas.myHVDeltaMap1.get(myDate), 2), my1stVDer2ChunkNotInOrderSet);
			aFound = aFound || gameChunkCheck(Utils.getArrayChunks(aTestDeltas.myHVDeltaMap1.get(myDate), 2), my1stVDer2ChunkInOrderSet);
			aFound = aFound || gameChunkCheck(Utils.getAllChunks(aTestDeltas.myHVDeltaMap1.get(myDate), 3), my1stVDer3ChunkNotInOrderSet);
			aFound = aFound || gameChunkCheck(Utils.getArrayChunks(aTestDeltas.myHVDeltaMap1.get(myDate), 3), my1stVDer3ChunkInOrderSet);
			// == new chunk method count : 1949515

			aFound = aFound || gameChunkCheck(Utils.getAllChunks(aTestDeltas.myVDeltaMap3.get(myDate), 2), my3rdVDer2ChunkNotInOrderSet);
			aFound = aFound || gameChunkCheck(Utils.getArrayChunks(aTestDeltas.myVDeltaMap3.get(myDate), 1), my3rdVDer1ChunkNotInOrderSet);

			aFound = aFound || gameChunkCheck(Utils.getAllChunks(aTestDeltas.myHDeltaMap3.get(myDate), 2), my3rdHDer2ChunkNotInOrderSet);
			//aFound = aFound || gameChunkCheck(Utils.getArrayChunks(aTestDeltas.myHDeltaMap3.get(myDate), 2), my3rdHDer2ChunkInOrderSet);
			aFound = aFound || gameChunkCheck(Utils.getArrayChunks(aTestDeltas.myHDeltaMap3.get(myDate), 1), my3rdHDer1ChunkNotInOrderSet);


		}

		trackResult(!aFound);
		return !aFound;


//		LinkedHashMap<Date, List<Integer>> al = new LinkedHashMap<>();
//		//al.
//		int aNum = 0;
//		List<Integer> aList = DeltaRule.getGameDelta(thePotentialGame);
//		for (int i : myVertDeltaMap.keySet())
//		{
//			Date aDate = myDates.get(i-1);
//			List<Integer> aDelta = VerticalGameDeltaRule.getVDelta(aList, myDeltaMap.get(aDate));
//
//			aDelta = Utils.getGameHDelta(aDelta);
//			_Deltas aDeltaO = myVertDeltaMap.get(i);
//			if (!kCheckFirstDerivative ||
//					  !aDeltaO.myDeltaHVH1Set.contains(aDelta)
//				//&& !contNotInOrder(aDeltaO.myDeltaHVH1Set, aDelta)
//					  )
//			{
//				aDelta = Utils.getGameHDelta(aDelta);
//				//if (!aDeltaO.myDeltaHVH2Set.contains(aDelta) && !contNotInOrder(aDeltaO.myDeltaHVH2Set, aDelta))
//				{
//					aDelta = Utils.getGameHDelta(aDelta);
//					if (aDeltaO.myDeltaHVH3Set.contains(aDelta))
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
//		}
//		if (aNum == myVertDeltaMap.size())
//		{
//			trackResult(true);
//			return true;
//		}
//
//		//System.out.println(thePotentialGame);
//		trackResult(false);
//		return false;
	}

//	private boolean gameChunkCheck(List<List<Integer>> theGameChunk, Set<List<Integer>> theExistingChunk)
//	{
//		for (Chunk aChunk : theGameChunk)
//		{
//			if (theExistingChunk.contains(aChunk))
//			{
//				myCount++;
//				return true;
//			}
//		}
//		return false;
//	}

	public static void main(String[] args)
	{
		Map<Date, List<Integer>> myAllTheFun = new FileReader().loadFromFile("/Users/jet/Documents/pb/pb.txt");
		myTestMode = true;
		HorToVerDerRule aRule = new HorToVerDerRule(myAllTheFun);
		//aRule.myTestMode = true;
		//new HorToVerDerRule(myAllTheFun).test();
		//new HorToVerDerRule(myAllTheFun).testChunks();
		aRule.trueTest();

	}

	private void trueTest()
	{
		System.out.println("===  V delta`1 stat ===");
		ChunkStatistics.getAllChunkStats(myVertDeltaMap.get(1).myHVDeltaMap1);
		System.out.println("========================\n\n");

		System.out.println("===  V delta`2 stat ===");
		ChunkStatistics.getAllChunkStats(myVertDeltaMap.get(1).myHVHVDeltaMap2);
		System.out.println("========================\n\n");

		System.out.println("===  V delta`3 stat ===");
		ChunkStatistics.getAllChunkStats(myVertDeltaMap.get(1).myVDeltaMap3);
		System.out.println("========================\n\n");

		System.out.println("===  V delta`4 stat ===");
		ChunkStatistics.getAllChunkStats(myVertDeltaMap.get(1).myVDeltaMap4);
		System.out.println("========================\n\n");

		List<Integer> aGap = Utils.getGapBetweenSameNumbers(myVertDeltaMap.get(1).myVDeltaMap4.values());
		System.out.println("\naGap: " + aGap.size() +
				  "\n");
		System.out.println(aGap);

		int anRep = 100000;
		for (int i = aGap.size()-1; -1 < i; i--)
		{
			if (aGap.get(i) == -1)
			{
				aGap.remove(i);
				aGap.add(i, anRep++);
			}
		}

		System.out.println("\naGap2: \n");
		List<Integer> aGap2 = Utils.getGapBetweenSameNumbers(aGap);
		System.out.println(aGap2.size() + ": " + aGap2);
		for (int i = aGap2.size()-1; -1 < i; i--)
		{
			if (aGap2.get(i) == -1)
			{
				aGap2.remove(i);
			}
		}
		Collections.sort(aGap2);

		System.out.println(aGap2.size() + ": " + aGap2);

		System.out.println("\n||||||||||||||||||||||||||||\n");

		System.out.println("===  H delta`2 stat ===");
		ChunkStatistics.getAllChunkStats(myVertDeltaMap.get(1).myHVHDeltaMap2);
		System.out.println("========================\n\n");

		System.out.println("===  H delta`3 stat ===");
		ChunkStatistics.getAllChunkStats(myVertDeltaMap.get(1).myHDeltaMap3);
		System.out.println("========================\n\n");

		System.out.println("===  H delta`4 stat ===");
		ChunkStatistics.getAllChunkStats(myVertDeltaMap.get(1).myHDeltaMap4);
		System.out.println("========================\n\n");

		aGap = Utils.getGapBetweenSameNumbers(myVertDeltaMap.get(1).myHDeltaMap4.values());
		System.out.println("\naGap: " + aGap.size() +
				  "\n");
		System.out.println(aGap);

		anRep = 100000;
		for (int i = aGap.size()-1; -1 < i; i--)
		{
			if (aGap.get(i) == -1)
			{
				aGap.remove(i);
				aGap.add(i, anRep++);
			}
		}

		System.out.println("\naGap2: \n");
		aGap2 = Utils.getGapBetweenSameNumbers(aGap);
		System.out.println(aGap2.size() + ": " + aGap2);
		for (int i = aGap2.size()-1; -1 < i; i--)
		{
			if (aGap2.get(i) == -1)
			{
				aGap2.remove(i);
			}
		}
		Collections.sort(aGap2);
		System.out.println(aGap2.size() + ": " + aGap2);

	}


	void printGapStatistic(Map<Date, List<Integer>> theMap)
	{
		List<Integer> aH4Gap = Utils.getGapBetweenSameNumbers(getIntList(theMap));
		//Set<Integer>  aH4GapSet = new TreeSet<>(aH4Gap);
		System.out.println(" gap size : " + aH4Gap.size() + " out of " + theMap.size());
		System.out.println(" gap: " + aH4Gap);
		Collections.sort(aH4Gap);
		System.out.println(" sorted: " + aH4Gap);
		double aPers = 0.10;
		int aPersInd = (int)(aH4Gap.size() * aPers);
		System.out.println( (int)(aPers*100) + "% gap [" + aH4Gap.get(aPersInd) + " - " + aH4Gap.get(aH4Gap.size()-aPersInd) + "]");
	}

	private void checkIfBothPosOrNegative(Collection<List<Integer>> theValues1, Collection<List<Integer>> theValues2)
	{
		List<Integer> anIList = new ArrayList<>();
		List<Integer> aList1 = new ArrayList<>();
		for (List<Integer> aInteger : theValues1)
		{
			aList1.add(aInteger.get(0));
		}

		List<Integer> aList2 = new ArrayList<>();
		for (List<Integer> aInteger : theValues2)
		{
			aList2.add(aInteger.get(0));
		}
		int aT = 0;
		for (int i = 0; i < aList1.size() && i < aList2.size(); i++)
		{
			if ((aList1.get(i) < 0 && 0 < aList2.get(i)) ||
					  (aList2.get(i) < 0 && 0 < aList1.get(i)))
			{
				aT++;
				anIList.add(i);
			}
		}
		System.out.println("total positive/negative mismatch: " + aT);
		System.out.println("positive/negative  delta i: " + Utils.getGameHDelta(anIList));

	}

	private void checkIfOnly2PosOrNegInaRow(Collection<List<Integer>> theValues)
	{
		List<Integer> aList = new ArrayList<>();
		List<Integer> anIList = new ArrayList<>();
		for (List<Integer> aInteger : theValues)
		{
			aList.add(aInteger.get(0));
		}
		int aTot = 0;
		for (int i = 0; i < aList.size()-4; i++)
		{
			boolean aPos1 = 0 < aList.get(i);
			boolean aPos2 = 0 < aList.get(i+1);
			boolean aPos3 = 0 < aList.get(i+2);
		//	boolean aPos4 = 0 < aList.get(i+3);
			if ((aPos1 && aPos2 && aPos3
					 // && aPos4
							) || (!aPos1 && !aPos2 && !aPos3
					 // && !aPos4
						))
			{
				anIList.add(i);
				aTot++;
				//System.out.println(" 4 in a row (i=" + i + ")" + " : " + aList.get(i) + "~ " + aList.get(i+1) + "~ " + aList.get(i+2) + "~ ");
			}
		}
		System.out.println("delta i: " + Utils.getGameHDelta(anIList));
		System.out.println("total 3 same sing in a row: " + aTot);
	}

}


/*
★ ★ ★  0 	 01/04/17.Wed = [16, 17, 29, 41, 42] =
= HorToVerDerRule =
    total: 11238513       excl:2882315   INCL: 8356198
 == new chunk method count : 1037957
 time: 94 sec.     [TOTAL] excl: 2882315, INCL: 8356198

===============



★ ★ ★  1 	 12/31/16.Sat = [1, 3, 28, 57, 67] =
= HorToVerDerRule =
    total: 11238513       excl:4612699   INCL: 6625814
 == new chunk method count : 2703528
 time: 83 sec.     [TOTAL] excl: 4612699, INCL: 6625814

===============



★ ★ ★  2 	 12/28/16.Wed = [16, 23, 30, 44, 58] =
 >>> BOOOOO <<< : HorToVerDerRule
= HorToVerDerRule =
    total: 11238513       excl:3701665   INCL: 7536848
 == new chunk method count : 2365168
 time: 86 sec.     [TOTAL] excl: 3701665, INCL: 7536848

===============



★ ★ ★  3 	 12/24/16.Sat = [28, 38, 42, 51, 52] =
= HorToVerDerRule =
    total: 11238513       excl:3876580   INCL: 7361933
 == new chunk method count : 2444165
 time: 85 sec.     [TOTAL] excl: 3876580, INCL: 7361933

===============



★ ★ ★  4 	 12/21/16.Wed = [25, 33, 40, 54, 68] =
= HorToVerDerRule =
    total: 11238513       excl:2105406   INCL: 9133107
 == new chunk method count : 1663346
 time: 93 sec.     [TOTAL] excl: 2105406, INCL: 9133107

===============



★ ★ ★  5 	 12/17/16.Sat = [1, 8, 16, 40, 48] =
= HorToVerDerRule =
    total: 11238513       excl:3560748   INCL: 7677765
 == new chunk method count : 1510600
 time: 82 sec.     [TOTAL] excl: 3560748, INCL: 7677765

===============



★ ★ ★  6 	 12/14/16.Wed = [18, 26, 37, 39, 66] =
= HorToVerDerRule =
    total: 11238513       excl:3710908   INCL: 7527605
 == new chunk method count : 2134468
 time: 81 sec.     [TOTAL] excl: 3710908, INCL: 7527605

===============



★ ★ ★  7 	 12/10/16.Sat = [12, 21, 32, 44, 66] =
= HorToVerDerRule =
    total: 11238513       excl:3870252   INCL: 7368261
 == new chunk method count : 2605685
 time: 84 sec.     [TOTAL] excl: 3870252, INCL: 7368261

===============



★ ★ ★  8 	 12/07/16.Wed = [41, 48, 49, 53, 64] =
= HorToVerDerRule =
    total: 11238513       excl:3548519   INCL: 7689994
 == new chunk method count : 1810761
 time: 83 sec.     [TOTAL] excl: 3548519, INCL: 7689994

===============



★ ★ ★  9 	 12/03/16.Sat = [8, 10, 26, 27, 33] =
= HorToVerDerRule =
    total: 11238513       excl:3139858   INCL: 8098655
 == new chunk method count : 1849315
 time: 84 sec.     [TOTAL] excl: 3139858, INCL: 8098655

===============



★ ★ ★  10 	 11/30/16.Wed = [3, 14, 18, 25, 45] =
= HorToVerDerRule =
    total: 11238513       excl:3372254   INCL: 7866259
 == new chunk method count : 1995058
 time: 82 sec.     [TOTAL] excl: 3372254, INCL: 7866259

===============



★ ★ ★  11 	 11/26/16.Sat = [17, 19, 21, 37, 44] =
= HorToVerDerRule =
    total: 11238513       excl:3682778   INCL: 7555735
 == new chunk method count : 2098113
 time: 83 sec.     [TOTAL] excl: 3682778, INCL: 7555735

===============



★ ★ ★  12 	 11/23/16.Wed = [7, 32, 41, 47, 61] =
= HorToVerDerRule =
    total: 11238513       excl:3025268   INCL: 8213245
 == new chunk method count : 1727207
 time: 86 sec.     [TOTAL] excl: 3025268, INCL: 8213245

===============



★ ★ ★  13 	 11/19/16.Sat = [16, 24, 28, 43, 61] =
= HorToVerDerRule =
    total: 11238513       excl:3387267   INCL: 7851246
 == new chunk method count : 1807886
 time: 81 sec.     [TOTAL] excl: 3387267, INCL: 7851246

===============



★ ★ ★  14 	 11/16/16.Wed = [28, 41, 61, 63, 65] =
= HorToVerDerRule =
    total: 11238513       excl:3575744   INCL: 7662769
 == new chunk method count : 2196779
 time: 81 sec.     [TOTAL] excl: 3575744, INCL: 7662769

===============



★ ★ ★  15 	 11/12/16.Sat = [8, 17, 20, 27, 52] =
= HorToVerDerRule =
    total: 11238513       excl:2877982   INCL: 8360531
 == new chunk method count : 1647937
 time: 83 sec.     [TOTAL] excl: 2877982, INCL: 8360531

===============



★ ★ ★  16 	 11/09/16.Wed = [1, 25, 28, 31, 54] =
 >>> BOOOOO <<< : HorToVerDerRule
= HorToVerDerRule =
    total: 11238513       excl:3021864   INCL: 8216649
 == new chunk method count : 1616443
 time: 83 sec.     [TOTAL] excl: 3021864, INCL: 8216649

===============



★ ★ ★  17 	 11/05/16.Sat = [21, 31, 50, 51, 69] =
 >>> BOOOOO <<< : HorToVerDerRule
= HorToVerDerRule =
    total: 11238513       excl:3466369   INCL: 7772144
 == new chunk method count : 2076651
 time: 82 sec.     [TOTAL] excl: 3466369, INCL: 7772144

===============



★ ★ ★  18 	 11/02/16.Wed = [13, 18, 37, 54, 61] =
= HorToVerDerRule =
    total: 11238513       excl:3705682   INCL: 7532831
 == new chunk method count : 2443804
 time: 79 sec.     [TOTAL] excl: 3705682, INCL: 7532831

===============



★ ★ ★  19 	 10/29/16.Sat = [19, 20, 21, 42, 48] =
= HorToVerDerRule =
    total: 11238513       excl:2530606   INCL: 8707907
 == new chunk method count : 1498416
 time: 83 sec.     [TOTAL] excl: 2530606, INCL: 8707907

===============




TOTAL time: 1696 sec.
 */