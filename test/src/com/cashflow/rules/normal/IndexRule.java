package com.cashflow.rules.normal;

import com.cashflow.rules.AbstractRule;
import main.Combination;
import pb.Meters;
import pb.Utils;
import pb.file.FileReader;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Ruslan Dauhiala
 */
public class IndexRule extends AbstractRule
{

	List<Integer> myGameIndex;
	Map<Integer, Date> myIndexDateMap;
	Map<Integer, Integer> mySnippetMap;
	List<Integer> mySnippetKeys;
	Map<Integer, Integer> myMaxSnippetMap;
	List<Integer> myMaxKeyList;
	List<Integer> myGamesNumbers;

	private int myCurrentGame;
	private int myMaxKey;


	private static int kGapSize = 10;


	// 2 = ERRORS: 	 4, #: [6, 10, 15, 16] 																Denied: 	 1317937;  Accepted: 9920576 		 time: 	 5; 	 total : 130 sec.
	// 3 = ERRORS: 	 6, #: [2, 6, 10, 15, 16, 19] 													Denied: 	 2352159;  Accepted: 8886354 		 time: 	 6; 	 total : 160 sec.
	// 5 = ERRORS: 	 9, #: [2, 6, 8, 10, 12, 13, 15, 16, 19] 		 								Denied: 	 3853708;  Accepted: 7384805
	// 7 = ERRORS: 	 9, #: [2, 6, 8, 10, 12, 13, 15, 16, 19] 		 								Denied: 	 4823217;  Accepted: 6415296 		 time: 	 5; 	 total : 127 sec.
	// 10 = ERRORS: 	 10, #: [2, 6, 8, 10, 12, 13, 15, 16, 18, 19] 		 						Denied: 	 5826424;  Accepted: 5412089
	// 15 = ERRORS: 	 11, #: [2, 6, 8, 10, 12, 13, 15, 16, 17, 18, 19] 							Denied: 	 6887168;  Accepted: 4351345
	// 20 = ERRORS: 	 12, #: [2, 6, 8, 10, 12, 13, 14, 15, 16, 17, 18, 19] 		 			Denied: 	 7555679;  Accepted: 3682834
	// 30 = ERRORS: 	 16, #: [0, 2, 3, 4, 6, 7, 8, 10, 12, 13, 14, 15, 16, 17, 18, 19] 	Denied: 	 8398305;  Accepted: 2840208
	// 50 = excl: 2067129
	// 100 = excl: 1198741

	// min:
	// 12 = 0, #: [] 							 Denied: 	 185984;
	// 5  = 3, #: [6, 10, 16] 				 Denied: 	 970827;  Accepted: 10267686
	// 3  = 4, #: [6, 10, 15, 16] 		 Denied: 	 2294187;  Accepted: 8944326
	// 2  = 8, #: [2, 6, 7, 9, 10, 15, 16, 19] 		 Denied: 	 4214784;  Accepted: 7023729


	// 10 = ERRORS: 	 1, #: [2] 		 Denied: 	 3848690;  Accepted: 7389823 		 time: 	 3; 	 total : 94 sec.

	// r3
	// 15 = ERRORS: 	 1, #: [2] 						 Denied: 	 2702840;  Accepted: 8535673 		 time: 	 4; 	 total : 106 sec.
	// 12 = ERRORS: 	 1, #: [2] 		 Denied: 	 3949602;  Accepted: 7288911 		 time: 	 3; 	 total : 95 sec.

	// r4
	// 3                                excl: 1749687
	// 5                                excl:2411803
	// 8  = ERRORS: 	 2, #: [2, 16] 		 Denied: 	 2610648;  Accepted: 8627865 		 time: 	 5; 	 total : 119 sec.
	// 10 = ERRORS: 	 1, #: [2] 		 Denied: 	 2625200;  Accepted: 8613313 		 time: 	 5; 	 total : 122 sec.
	// 12 = ERRORS: 	 1, #: [2] 		 Denied: 	 2558941;  Accepted: 8679572 		 time: 	 4; 	 total : 114 sec.
	// 15 										excl: 2394952

	//  ERRORS: 	 14, #: [0, 1, 2, 3, 4, 5, 7, 8, 11, 12, 14, 15, 17, 18] 		 Denied: 	 8691450;  Accepted: 2547063 		 time: 	 2; 	 total : 68 sec.

	public IndexRule(Map<Date, List<Integer>> theGamesMap, Map<Date, List<Integer>> theMMMap2)
	{
		super(theGamesMap, null);
	}

	@Override
	public void setup()
	{
		myCurrentGame = 0;
		myMaxKey = 0;
		myMaxSnippetMap = new TreeMap<>();
		mySnippetMap = new TreeMap<>();

		myGamesNumbers = new ArrayList<>(Collections.nCopies(myGamesMap.size(), 0));
		int[] aCombinatInds = {0};
		Map<Date, List<Integer>> a5Map = Utils.get5SizeMap(myGamesMap);
		List<List<Integer>> aGameList = new ArrayList<>(a5Map.values());
		Set<List<Integer>> aHashSet = new HashSet<>(a5Map.values());

		int[] aCurrent = {0};
		Combination.combination(Combination.getAll69(), 5, new Combination.GamePrint()
		{
			@Override
			public void print(List<Integer> theGame)
			{
				if (aHashSet.contains(theGame))
				{
					myGamesNumbers.set(aGameList.indexOf(theGame), aCombinatInds[0]);
					mySnippetMap.put(aCurrent[0], aCombinatInds[0]);
					aCurrent[0] = aCombinatInds[0];
				} else if (aCombinatInds[0] == 11_238_512)
				{
					mySnippetMap.put(aCurrent[0], aCombinatInds[0]);
				}
				aCombinatInds[0] = aCombinatInds[0] + 1;
			}
		});

		List<Integer> aKeysList = new ArrayList<>(mySnippetMap.keySet());
		List<Integer> aSnippetLengthList = Utils.getGameHDelta(aKeysList);

		//myMaxSnippetMap.putAll(mySnippetMap);
		for (int i = 0; i < aSnippetLengthList.size() - kGapSize; i++)
		{
			/*
			gap 10 =  ERRORS: 	 14, #: [0, 1, 2, 3, 4, 5, 7, 8, 11, 12, 14, 15, 17, 18] 		 Denied: 	 8691450;  Accepted: 2547063 		 time: 	 2; 	 total : 68 sec.
			gap 5  =  ERRORS: 	 10, #: [0, 2, 3, 8, 14, 15, 16, 17, 18, 19] 		 				 Denied: 	 7059716;  Accepted: 4178797 		 time: 	 2; 	 total : 75 sec.
			gap 4  =  ERRORS: 	 11, #: [0, 2, 8, 12, 13, 14, 15, 16, 17, 18, 19] 		 Denied: 	 6398518;  Accepted: 4839995 		 time: 	 3; 	 total : 84 sec.
			+20k check:
			3 =  ERRORS: 	 3, #: [2, 4, 16] 		 Denied: 	 3430163;  Accepted: 7808350 		 time: 	 4; 	 total : 103 sec.
			4 =  ERRORS: 	 2, #: [2, 14] 		 Denied: 	 3178970;  Accepted: 8059543 		 time: 	 4; 	 total : 102 sec.
			6 =  ERRORS: 	 2, #: [2, 14] 		 Denied: 	 3019755;  Accepted: 8218758 		 time: 	 4; 	 total : 109 sec.
			10 =   ERRORS: 	 3, #: [1, 4, 14] 		 Denied: 	 1949665;  Accepted: 9288848 		 time: 	 6; 	 total : 144 sec.
			*/
			//prevNextChunkSum(aSnippetLengthList, i, aKeysList);
			// ================

			/*
         6 = ERRORS: 	 1, #: [0] 		 Denied: 	 1094699;  Accepted: 10143814 		 time: 	 5; 	 total : 135 sec.
			 */
			//maxIsMoreThenTwiceAverage(aSnippetLengthList, i, aKeysList);
			// ================


			/*
			5  =   ERRORS: 	 4, #: [2, 8, 15, 16] 		 Denied: 	 2398406;  Accepted: 8840107 		 time: 	 6; 	 total : 149 sec.
			10 = 	 ERRORS: 	 1, #: [2] 		 				 Denied: 	 2625200;  Accepted: 8613313 		 time: 	 5; 	 total : 121 sec.
			15 =   ERRORS: 	 1, #: [2] 		 				 Denied: 	 2359190;  Accepted: 8879323 		 time: 	 5; 	 total : 120 sec.

			- min half
			 10  ERRORS: 	 4, #: [2, 12, 16, 19] 		 Denied: 	 3307917;  Accepted: 7930596 		 time: 	 7; 	 total : 176 sec.
          10 <20k only 	 9, #: [1, 2, 4, 5, 7, 12, 14, 16, 19] 		 Denied: 	 5394026;  Accepted: 5844487 		 time: 	 3; 	 total : 84 sec.

			10  -3min 	ERRORS: 	 2, #: [2, 16] 		 Denied: 	 2829364;  Accepted: 8409149 		 time: 	 5; 	 total : 129 sec.
			10 - 4min 	ERRORS: 	 2, #: [2, 16] 		 Denied: 	 3014336;  Accepted: 8224177 		 time: 	 4; 	 total : 115 sec.
                     ERRORS: 	 41, #: [2, 16, 20, ..]		 Denied: 	 2877723;  Accepted: 8360790 		 time: 	 3; 	 total : 898 sec.
                 -10 sub.	 ERRORS: 	 2, #: [2, 16] 		 Denied: 	 3339276;  Accepted: 7899237 		 time: 	 4; 	 total : 113 sec.
                 -8 	sub	 ERRORS: 	 2, #: [2, 16] 		 Denied: 	 3466445;  Accepted: 7772068 		 time: 	 6; 	 total : 156 sec.
                 -5	sub	 ERRORS: 	 2, #: [2, 16] 		 Denied: 	 3623340;  Accepted: 7615173 		 time: 	 4; 	 total : 117 sec.
                 -4	sub	 ERRORS: 	 2, #: [2, 16] 		 Denied: 	 3678020;  Accepted: 7560493 		 time: 	 4; 	 total : 108 sec.
                 -3  sub	 ERRORS: 	 3, #: [2, 4, 16] 	 Denied: 	 3817326;  Accepted: 7421187 		 time: 	 3; 	 total : 92 sec.
                 -2  sub	 ERRORS: 	 3, #: [2, 4, 16] 		 Denied: 	 4073294;  Accepted: 7165219 		 time: 	 3; 	 total : 92 sec.
                 				 ERRORS: 	 61, #: [2, 4, 16, 20, 24, 25
			10 - 5min   ERRORS: 	 4, #: [2, 12, 16, 19] 		 Denied: 	 3307917;  Accepted: 7930596 		 time: 	 5; 	 total : 125 sec.


!!!!
			10-2-4
				 ERRORS: 	 3; 		 Denied : 	 4073294	;  Accepted : 7165219 		 time: 	 4; 	 total : 97 sec. #: [2, 4, 16]
 							 denied%: 0.36    	;  err %: 0.15

							aSt, aSt+1/4  +  End-1/4, aEnd
							 ERRORS: 	 8; 		 Denied : 	 7657486	;  Accepted : 3581027 		 time: 	 3; 	 total : 78 sec. #: [2, 4, 9, 10, 11, 14, 16, 19]
														 denied%: 0.68    	;  errors % : 0.40

 							 aSt, aSt+1/5  +  End-1/5, aEnd
 							  ERRORS: 	 11; 		 Denied : 	 8374107	;  Accepted : 2864406 		 time: 	 3; 	 total : 81 sec. #: [2, 4, 5, 8, 9, 10, 11, 14, 16, 18, 19]
 							 							 denied%: 0.75    	;  errors % : 0.55

                     ERRORS: 	 144; 		 Denied : 	 8273074	;  Accepted : 2965439 		 time: 	 2; 	 total : 625 sec. #: [2, 4, 5, 8, 9, 10, 11, 14, 16, 18, 19, 20, 21, 23, 24, 25, 28, 29, 30, 31, 33, 35, 36, 37, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 52, 53, 54, 55, 60, 61, 62, 63, 65, 66, 67, 72, 73, 74, 75, 76, 78, 79, 80, 81, 82, 84, 85, 86, 87, 88, 89, 90, 92, 95, 96, 97, 98, 99, 100, 101, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 117, 118, 119, 120, 121, 123, 125, 126, 127, 128, 130, 131, 134, 136, 137, 138, 140, 141, 142, 143, 145, 147, 148, 149, 151, 152, 153, 155, 156, 158, 159, 160, 161, 162, 163, 164, 165, 167, 168, 169, 170, 171, 172, 173, 176, 178, 180, 181, 182, 184, 185, 186, 188, 189, 190, 191, 193, 195, 196, 197, 199]
 							 denied%: 0.74    	;  errors % : 0.72

!!!!

         10-2-5
				 ERRORS: 	 5; 		 Denied : 	 4374487	;  Accepted : 6864026 		 time: 	 4; 	 total : 96 sec. #: [2, 4, 12, 16, 19]
 											 denied%: 0.39    	;  errors % : 0.25

			10-2-5 25
			 ERRORS: 	 9; 		 Denied : 	 5748183	;  Accepted : 5490330 		 time: 	 2; 	 total : 76 sec. #: [2, 3, 4, 6, 12, 13, 14, 16, 17]
 										denied% : 	 0.51   	;  errors % : 0.45
			10-2-5 30
			 ERRORS: 	 12; 		 Denied : 	 6813086	;  Accepted : 4425427 		 time: 	 3; 	 total : 78 sec. #: [1, 2, 3, 4, 5, 6, 8, 12, 13, 14, 16, 17]
 										 denied%:	 0.61   	;  errors % : 0.60


			                    */
			maxAnd20k(aSnippetLengthList, i, aKeysList);
			// ================


			/*
			big area
			20k, 3+3 =  ERRORS: 	 4, #: [2, 8, 15, 16] 		 Denied: 	 1867662;  Accepted: 9370851 		 time: 	 6; 	 total : 143 sec.
			20k, 3+2 =   ERRORS: 	 5, #: [2, 8, 15, 16, 17] 		 Denied: 	 2345615;  Accepted: 8892898 		 time: 	 7; 	 total : 167 sec.

			30k, 3+3 =  ERRORS: 	 10, #: [0, 1, 2, 4, 5, 8, 14, 15, 16, 17] 		 Denied: 	 4841482;  Accepted: 6397031 		 time: 	 3; 	 total : 92 sec.

			3    = ERRORS: 	 4, #: [2, 8, 16, 17] 		 Denied: 	 2048587;  Accepted: 9189926 		 time: 	 18; 	 total : 390 sec.

			      */
			//bigArea(aSnippetLengthList, i, aKeysList);
			// ================

			//gangSum(aSnippetLengthList, i, aKeysList);
		}

		myMaxKeyList = new ArrayList<>(myMaxSnippetMap.keySet());
		mySnippetKeys = new ArrayList<>(mySnippetMap.keySet());
	}

	private void gangSum(List<Integer> theSnippetLengthList, int theI, List<Integer> theKeysList)
	{

	}

	private void bigArea(List<Integer> theSnippetLengthList, int theI, List<Integer> theKeysList)
	{
		kGapSize = 3;
		int anAreaSize = 2;
		List<Integer> aChunk = theSnippetLengthList.subList(theI, theI + kGapSize);

		int aSum = aChunk.stream().mapToInt(Integer::intValue).sum();

		if (aSum > 40_000)
		{
			anAreaSize = 8;
		} else if (aSum > 30_000)
		{
			anAreaSize = 5;
		}

		if (aSum > 20_000)
		{
			int aStart = theI - anAreaSize;
			aStart = aStart > 0 ? aStart : 0;
			int anEnd = theI + kGapSize + anAreaSize;
			anEnd = anEnd >= theKeysList.size() ? theKeysList.size() - 1 : anEnd;
			for (int m = aStart; m < anEnd; m++)
			{
				int aKey = theKeysList.get(m);
				myMaxSnippetMap.put(aKey, mySnippetMap.get(aKey));
			}
		}

	}

	private void maxAnd20k(List<Integer> aSnippetLengthList, int i, List<Integer> aKeysList)
	{
		int k20k = 20_000;
		List<Integer> aChunk = aSnippetLengthList.subList(i, i + kGapSize);
		int aMax = Collections.max(aChunk);
		int aChunkInd = aChunk.indexOf(aMax);
		int aMaxKey = aKeysList.get(aChunkInd + i);

		boolean isOk = false;
		if (aMax < k20k)
		{
			int aShift = 2;
			int aS = aChunkInd + i - aShift;
			aS = aS < 0 ? 0 : aS;
			int aEnd = aChunkInd + i + aShift;
			aEnd = aSnippetLengthList.size() < aEnd ? aSnippetLengthList.size() - 1 : aEnd;
			int aM = Collections.max(aSnippetLengthList.subList(aS, aEnd));
			isOk = aM > k20k;
			if (!isOk)
			{
				for (int z = aS; z < aEnd - 3; z++)
				{
					int aSum = aSnippetLengthList.get(z) + aSnippetLengthList.get(z + 1) + aSnippetLengthList.get(z + 2);
					if (aSum > k20k)
					{
						isOk = true;
						break;
					}
				}
			}
		}

		int aUsed = -1;
		if (isOk)
		{
			addNum(myMaxSnippetMap, aMaxKey, mySnippetMap.get(aMaxKey), aSnippetLengthList, aChunkInd + i);

			/**

			ERRORS: 	 9; 		 Denied : 	 8164167	;  Accepted : 3074346 		 time: 	 3; 	 total : 79 sec. #: [2, 4, 9, 10, 11, 15, 16, 18, 19]
			 									denied%: 0.73    	;  errors % : 0.45
			 myMaxSnippetMap.put(aMaxKey , mySnippetMap.get(aMaxKey));

			 aMaxKey + 1/2 aPart
			 ERRORS: 	 10; 		 Denied : 	 8916728	;  Accepted : 2321785 		 time: 	 2; 	 total : 70 sec. #: [1, 2, 4, 9, 10, 11, 15, 16, 18, 19]
			 denied%: 0.79    	;  errors % : 0.50
			 = = =  		29% 		!!!!

			 aMaxKey , aMaxKey + 1/4L  and  mySnippetMap.get(aMaxKey) - 1/4L, mySnippetMap.get(aMaxKey)
			 ERRORS: 	 10; 		 Denied : 	 9020231	;  Accepted : 2218282 		 time: 	 2; 	 total : 66 sec. #: [2, 4, 9, 10, 11, 14, 15, 16, 18, 19]
			 							 denied%: 	 0.80    	;  errors % : 0.50 !!! 30%%

			 */
//			int aL = mySnippetMap.get(aMaxKey) - aMaxKey  ;
//			int aPart = aL / 4;
//			aUsed = aMaxKey;
//			myMaxSnippetMap.put(aMaxKey , aMaxKey + aPart);
//			myMaxSnippetMap.put(mySnippetMap.get(aMaxKey) - aPart, mySnippetMap.get(aMaxKey));
		}

		List<Integer> aSortedList = new ArrayList<>(aChunk);
		Collections.sort(aSortedList);
		List<Integer> aMinIndList = new ArrayList<>();

		if (k20k < aMax)
		{
			for (int m = 0; m < 4; m++)
			{
				int aMinInd = aChunk.indexOf(aSortedList.get(m));
				aMinIndList.add(aMinInd + i);
			}

			for (int k = i; k < i + kGapSize; k++)
			{
				if (aMinIndList.contains(k))
				{
					continue;
				}
				int aKey = aKeysList.get(k);

				if (aUsed != aKey)
				{
					addNum(myMaxSnippetMap, aKey, mySnippetMap.get(aKey), aSnippetLengthList, k);
				}
				//myMaxSnippetMap.put(aKey , mySnippetMap.get(aKey) );
			}
		}

	}

	private void addNum(Map<Integer, Integer> theMaxSnippetMap, int theStart, int theEnd, List<Integer> theSnippetLengthList, int i)
	{
		int aL = theEnd - theStart  ;
//		int aPart = aL / 4;
		int aPart = 0;
		theMaxSnippetMap.put(theStart + aPart, theEnd - aPart);

//		int aGap = 10;
//		int aS = i - aGap;
//		aS = aS <  0 ? 0 : aS;
//
//		int aE = i + aGap;
//		aE = theSnippetLengthList.size() < aE ? theSnippetLengthList.size()-1 : aE;
//
//		int aBef = theSnippetLengthList.subList(aS, i-1 > 0 ? i-1 : i).stream().mapToInt(Integer::intValue).sum();
//		int aAfter = theSnippetLengthList.subList(i < theSnippetLengthList.size() ? i : theSnippetLengthList.size()-1, aE).stream().mapToInt(Integer::intValue).sum();
//
//		if(aBef < aAfter)
//		{
//			theMaxSnippetMap.put(theStart, theStart + aPart);
//		} else
//		{
//			theMaxSnippetMap.put(theEnd - aPart, theEnd);
//		}
	}

	private void maxIsMoreThenTwiceAverage(List<Integer> aSnippetLengthList, int i, List<Integer> aKeysList)
	{
		List<Integer> aChunk = aSnippetLengthList.subList(i, i + kGapSize);
		int aMax = Collections.max(aChunk);

		int anAverage = (aChunk.stream().mapToInt(Integer::intValue).sum() - aMax) / (kGapSize-1);
		boolean anIncludeAllChunk = anAverage * 2 <= aMax ;

		if (anIncludeAllChunk || 20000 < aMax )
		{
			int aChunkInd = aChunk.indexOf(aMax);
			int aMaxKey = aKeysList.get(aChunkInd + i);
			myMaxSnippetMap.put(aMaxKey, mySnippetMap.get(aMaxKey));

//			if (20000 < aMax)
//			{
//				for (int k = i; k < i + kGapSize; k++)
//				{
//					int aKey = aKeysList.get(k);
//					myMaxSnippetMap.put(aKey, mySnippetMap.get(aKey));
//				}
//			}
		}
	}

	private void prevNextChunkSum(List<Integer> aSnippetLengthList, int i, List<Integer> aKeysList)
	{
		List<Integer> aChunk = aSnippetLengthList.subList(i, i + kGapSize);
//			int aMax = Collections.max(aChunk);
//			int aChunkInd = aChunk.indexOf(aMax);
//			int aMaxKey = aKeysList.get(aChunkInd + i);
//			myMaxSnippetMap.put(aMaxKey, mySnippetMap.get(aMaxKey));
//

		int aChunkSum = aChunk.stream().mapToInt(Integer::intValue).sum();

		int aPreStart = i - 2*kGapSize < 0 ? 0 : i - 2*kGapSize;
		int aPreEnd = i;

		int aNextStart = i + kGapSize;
		int aNextEnd = i + 2*kGapSize < aSnippetLengthList.size() ? i + 2*kGapSize : aSnippetLengthList.size();

		List<Integer> aPreChunk = aSnippetLengthList.subList(aPreStart, aPreEnd);
		int aPreSum = aPreChunk.stream().mapToInt(Integer::intValue).sum();

		List<Integer> aNextChunk= aSnippetLengthList.subList(aNextStart, aNextEnd);
		int aNextSum = aNextChunk.stream().mapToInt(Integer::intValue).sum();

		boolean anIncludeAllChunk = false;
		//anIncludeAllChunk = 20_000 < mySnippetMap.get(aMaxKey) - aMaxKey;
		//anIncludeAllChunk = aChunk.size()/aSmallSum > aBigSum/3;    // ERRORS: 	 5, #: [1, 2, 3, 17, 18] 		 Denied: 	 4443984;  Accepted: 6794529 		 time: 	 3; 	 total : 91 sec.
		//anIncludeAllChunk = aBigChunk.size()/aChunk.size() < aBigSum/aSmallSum;

		int aCoef = 1;
		anIncludeAllChunk = aCoef * aPreSum < aChunkSum && aCoef * aNextSum < aChunkSum
				  || (aPreChunk.size() > 0 && 20000 < Collections.max(aPreChunk))
				  || (aNextChunk.size() > 0 && 20000 < Collections.max(aNextChunk));

		if (anIncludeAllChunk)
		{
			for (int k = i; k < i + kGapSize; k++)
			{
				int aKey = aKeysList.get(k);
				myMaxSnippetMap.put(aKey, mySnippetMap.get(aKey));
			}
		}



//			int aMin = Collections.min(aChunk);
//			int aChunkMinInd = aChunk.indexOf(aMin);
//			int aMinKey = aKeysList.get(aChunkMinInd + i);
//			myMaxSnippetMap.put(aMinKey, mySnippetMap.get(aMinKey));

//			List<Integer> aChunkCopy = new ArrayList<>(aChunk);
//			aChunkCopy.set(aChunkInd, 0);
//			aMax = Collections.max(aChunkCopy);
//			aChunkInd = aChunk.indexOf(aMax);
//			aMaxKey = aKeysList.get(aChunkInd + i);
//			myMaxSnippetMap.put(aMaxKey, mySnippetMap.get(aMaxKey));

		//
//			int aMin = Collections.min(aChunk);
//			int aMinInd = aChunk.indexOf(aMin) + i;
//			int aMinxKey = aKeysList.get(aMinInd);
//			myMaxSnippetMap.remove(aMinxKey);
	}


	@Override
	public boolean accept(List<Integer> thePotentialGame)
	{
//		if (myCurrentGame == 4131080 || myCurrentGame == 4142055)
//		{
//			String a= "l"; a+="2";
//		}
		boolean aRes = false;

		if (myMaxKeyList.get(myMaxKey) < myCurrentGame && myCurrentGame < myMaxSnippetMap.get(myMaxKeyList.get(myMaxKey)))
		{
			aRes = true;
		} else if (myMaxSnippetMap.get(myMaxKeyList.get(myMaxKey)) < myCurrentGame && myMaxKey < myMaxKeyList.size() - 1)
		{
			myMaxKey++;
			return accept(thePotentialGame);
		}
		myCurrentGame++;
		trackResult(aRes);
		return aRes;
	}

	private void test1()
	{
		List<Integer> aKeysList = new ArrayList<>(mySnippetMap.keySet());
		for (int i = 0; i < mySnippetMap.size(); i++)
		{
			int anInd = myGamesNumbers.get(i);
			System.out.format("%1$4d", i);
			System.out.print("\t" + Meters.kOutDateFormat.format(myDates.get(i)) + "\t" );
			Utils.printf(myGamesMap.get(myDates.get(i)), 2, ". ", " \t");
			int aKey = anInd;//aKeysList.get(i);
			int aValue = mySnippetMap.get(aKey);
			System.out.format("%1$11d \t - %3$11d ->\t%2$11d", aKey, aValue, aValue-aKey);
			System.out.println();
		}

		List<Integer> aSnippetLengthList = Utils.getGameHDelta(aKeysList);
		System.out.println("\ngaps: \n\t" + aSnippetLengthList);

		System.out.println("\nmax snippet (size: " + myMaxSnippetMap.size() + ")\n\t" + myMaxSnippetMap);

		int aSum = 0;
		List<Integer> aMaxDelta = new ArrayList<>();
		for (Map.Entry<Integer, Integer> aEntry : myMaxSnippetMap.entrySet())
		{
			aMaxDelta.add(aEntry.getValue() - aEntry.getKey());
			aSum += aEntry.getValue() - aEntry.getKey() ;
		}

		System.out.println("\nmax delta: \n\t" + aMaxDelta);
		Collections.sort(aMaxDelta);
		System.out.println("\nmax delta sorted: \n\t" + aMaxDelta);

		System.out.println("\nmax snippet sum: " + aSum);
	}


	public static void main(String args[])
	{
		Map<Date, List<Integer>> myAllTheFun = new FileReader().loadFromFile("/Users/jet/Documents/pb/pb.txt");
		//Map<Date, List<Integer>> myMMTheFun = new FileReader().loadFromFile("/Users/jet/Documents/pb/mm.txt");
		//myMMTheFun = Utils.replaceDates(new ArrayList<Date>(myAllTheFun.keySet()), myMMTheFun);

		IndexRule aIndexRule = new IndexRule(myAllTheFun, null);
		aIndexRule.test1();

//		testIndex();

	}

	private static void testIndex()
	{
		int[] aNums = {1, 3, 6, 4, 1, 1, 7, 1, 4, 4, 2, 3, 1, 5, 1, 4, 2, 2, 1, 3, 3, 5, 0, 3, 7, 3, 4, 2, 8, 2, 0, 4, 1, 2, 1, 8, 0, 7, 0, 2, 3, 9, 5, 1, 1, 2, 4, 7, 2, 2, 4, 1, 5, 4, 1, 0, 2, 0, 1, 1, 8, 7, 2, 1, 3, 5, 6, 3, 1, 7, 2, 0, 5, 4, 0, 1, 4, 0, 1, 2, 0, 6, 4, 3, 3, 6, 8, 4, 2, 7, 3, 0, 0, 0, 0, 2, 3, 0, 6, 9, 8, 5, 4, 7, 0, 2, 5, 1, 8, 0, 3, 0, 2, 4, 6, 6, 1, 1, 9, 3, 5, 2, 0, 2, 7, 1, 1, 1, 0, 7, 8, 7, 5, 2, 5, 3, 4, 6, 0, 6, 0, 4, 4, 2, 4, 2, 1, 0, 3, 1, 5, 6, 1, 0, 2, 3, 3, 8, 2, 2, 2, 3, 9, 3, 2, 0, 4, 7, 0, 1, 5, 4, 2, 0, 1, 3, 0, 9, 2, 2, 8, 7, 2, 0, 2, 0, 2, 1, 5, 6, 5, 6, 4, 2, 0, 0, 4, 7, 5, 5, 3, 4, 7, 2, 1, 5, 7, 5, 0, 2, 6, 6, 5, 2, 0, 1, 1, 3, 6, 3, 8, 0, 5, 3, 1, 4, 2, 4, 5, 0, 6, 2, 7, 3, 3, 3, 3, 8, 4, 6, 5, 3, 8, 1, 5, 1, 6, 6, 1, 3, 6, 1, 3, 2, 2, 7, 1, 1, 2, 5, 2, 2, 4, 0, 7, 2, 4, 1, 6, 6, 8, 1, 8, 3, 0, 1, 6, 6, 5, 0, 3, 2, 2, 1, 1, 5, 6, 6, 5, 3, 6, 5, 8, 7, 2, 2, 2, 6, 1, 0, 7, 0, 8, 7, 1, 2, 9, 6, 7, 6, 1, 3, 2, 2, 6, 2, 6, 1, 2, 1, 0, 5, 3, 4, 3, 1, 2, 3, 7, 8, 4, 0, 3, 6, 4, 0, 9, 4, 7, 8, 2, 7, 2, 2, 2, 9, 0, 4, 4, 6, 6, 6, 6, 1, 2, 5, 2, 7, 2, 1, 3, 3, 6, 0, 1, 3, 4, 5, 5, 3, 7, 4, 5, 5, 5, 8, 5, 1, 2, 3, 1, 1, 1, 1, 5, 0, 2, 1, 7, 2, 8, 7, 0, 5, 9, 1, 1, 4, 2, 4, 6, 3, 6, 8, 8, 8, 7, 1, 1, 0, 0, 9, 2, 3, 3, 0, 1, 3, 0, 3, 2, 0, 9, 5, 2, 3, 4, 7, 2, 4, 8, 6, 4, 5, 8, 3, 4, 6, 8, 8, 6, 2, 3, 6, 5, 7, 0, 8, 4, 4, 0, 3, 2, 1, 2, 5, 2, 0, 7, 4, 4, 6, 3, 7, 0, 9, 0, 0, 1, 3, 3, 1, 9, 1, 3, 5, 7, 0, 0, 9, 6, 3, 0, 0, 3, 2, 4, 0, 9, 1, 5, 0, 7, 0, 0, 6, 6, 5, 2, 1, 0, 2, 8, 5, 6, 3, 1, 2, 3, 1, 0, 6, 0, 6, 4, 8, 4, 2, 1, 5, 4, 4, 4, 9, 7, 9, 6, 6, 0, 0, 6, 1, 3, 4, 1, 3, 2, 1, 7, 0, 7, 5, 2, 2, 8, 5, 5, 1, 0, 0, 3, 0, 3, 0, 5, 3, 1, 0, 3, 6, 8, 3, 5, 0, 6, 3, 3, 7, 2, 5, 6, 2, 3, 6, 6, 0, 8, 2, 2, 5, 3, 5, 1, 5, 6, 6, 7, 6, 3, 3, 7, 2, 7, 3, 7, 6, 2, 4, 7, 7, 5, 7, 9, 6, 4, 0, 8, 5, 3, 3, 1, 3, 1, 5, 7, 4, 2, 4, 0, 5, 3, 8, 1, 3, 4, 7, 3, 2, 3, 4, 7, 4, 0, 6, 0, 8, 2, 7, 6, 6, 0, 7, 2, 2, 2, 1, 6, 4, 6, 0, 4, 1, 3, 9, 2, 5, 2, 7, 2, 3, 2, 4, 5, 5, 3, 1, 7, 1, 7, 3, 0, 0, 0, 2, 4, 1, 9, 0, 4, 1, 5, 0, 6, 0, 1, 0, 2, 3, 0, 3, 7, 0, 4, 4, 1, 1, 7, 7, 5, 8, 1, 0, 2, 6, 5, 4, 2, 2, 9, 1, 0, 3, 9, 6, 0, 8, 0, 1, 3, 5, 7, 1, 2, 0, 2, 3, 0, 7, 5, 8, 5, 7, 6, 2, 7, 2, 1, 7, 1, 3, 6, 3, 0, 0, 0, 9, 8, 8, 2, 4, 8, 8, 4, 5, 3, 5, 5, 5, 4, 7, 8, 5, 2, 4, 1, 4, 5, 4, 5, 0, 8, 4, 8, 2, 2, 0, 3, 5, 3, 4, 5, 4, 5, 8, 3, 8, 3, 0, 9, 2, 7, 3, 5, 1, 6, 2, 4, 4, 1, 4, 1, 4, 2, 4, 3, 6, 0, 4, 2, 0, 6, 0, 1, 6, 4, 4, 6, 0, 6, 4, 8, 2, 1, 0, 4, 4, 1, 9, 2, 0, 4, 3, 0, 6, 3, 4, 2, 7, 2, 4, 4, 9, 7, 1, 8, 1, 4, 8, 4, 2, 5, 2, 5, 1, 1, 3, 7, 1, 5, 6, 5, 2, 6, 4, 1, 6, 4, 2, 5, 6, 0, 7, 3, 0, 7, 8, 1, 0, 1, 2, 7, 6, 3, 2, 6, 8, 2, 6, 1, 8, 6, 0, 0, 3, 5, 2, 2, 7, 2, 5, 5, 4, 0, 1, 9, 9, 7, 8, 1, 6, 2, 6, 5, 0, 7, 6, 1, 6, 4, 8, 1, 1, 1, 1, 6, 3, 3, 6, 8, 3, 1, 1, 2, 0, 1, 5, 7, 6, 3, 5, 4, 9, 5, 0, 8, 2, 0, 7, 4, 7, 8, 2, 0, 4, 6, 4, 5, 0, 9, 0, 7, 0, 4, 5, 1, 5, 2, 1, 1, 0, 0, 9, 6, 0, 6, 6, 2, 3, 7, 5, 3, 0, 8, 1, 3, 6, 3, 8, 5, 7, 0, 6, 1, 9, 3, 1, 1, 0, 4, 1, 2, 3, 5, 2, 8, 6, 4, 8, 0, 7, 4, 0, 1, 6, 9, 6, 4, 1, 4, 0, 7, 5, 6, 0, 6, 6, 5, 3, 3, 6, 8, 2, 8, 9, 0, 4, 6, 8, 6, 5, 5, 6, 3, 0, 8, 3, 7, 7, 1, 0, 4, 4, 0, 2, 3, 8, 6, 2, 4, 4, 1, 2, 1, 8, 4, 8, 2, 5, 1, 6, 1, 2, 6, 1, 4, 4, 8, 1, 5, 7, 6, 3, 3, 4, 4, 7, 0, 6, 1, 5, 5, 6, 5, 3, 0, 1, 5, 4, 7, 5, 3, 9, 5, 1, 2, 6, 4, 4, 4, 3, 6, 6, 2, 0, 2, 1, 1, 3, 5, 5, 1, 0, 3, 9, 2, 3, 6, 7, 6, 1, 9, 7, 6, 3, 6, 3, 2, 6, 4, 0, 1, 1, 1, 5, 5, 0, 8, 2, 2, 0, 0, 1, 4, 7, 5, 3, 7, 5, 3, 6, 3, 1, 1, 5, 3, 7, 8, 4, 4, 0, 8, 3, 2, 0, 4, 2, 2, 0, 1, 6, 7, 1, 0, 8, 1, 4, 7, 7, 5, 0, 0, 6, 0, 4, 7, 3, 7, 4, 3, 1, 3, 2, 8, 3, 8, 7, 2, 0, 7, 5, 7, 6, 2, 1, 7, 7, 5, 2, 8, 0, 9, 2, 2, 2, 0, 3, 8, 2, 5, 6, 6, 1, 0, 0, 7, 4, 2, 6, 7, 3, 3, 7, 9, 6, 1, 6, 2, 2, 3, 6, 1, 1, 5, 4, 4, 8, 9, 4, 2, 9, 6, 9, 0, 5, 6, 7, 4, 9, 2, 1, 8, 8, 4, 3, 7, 6, 3, 3, 7, 6, 2, 5, 3, 6, 2, 7, 3, 5, 7, 3, 8, 5, 6, 4, 2, 5, 2, 8, 3, 3, 5, 0, 6, 7, 6, 3, 9, 6, 0, 7, 0, 0, 2, 7, 6, 4, 4, 5, 5, 6, 3, 1, 5, 8, 8, 8, 7, 8, 0, 0, 8, 2, 3, 5, 2, 3, 7, 2, 7, 5, 5, 3, 4, 0, 5, 4, 6, 6, 6, 0, 6, 5, 3, 0, 8, 0, 9, 3, 2, 7, 6, 5, 4, 5, 2, 6, 4, 0, 5, 2, 5, 0, 1, 7, 8, 3, 7, 4, 8, 9, 2, 8, 1, 7, 3, 5, 4, 1, 9, 2, 4, 1, 6, 0, 9, 1, 2, 2, 7, 3, 5, 2, 2, 8, 5, 1, 2, 3, 1, 1, 4, 4, 3, 0, 2, 6, 4, 7, 6, 7, 7, 6, 5, 7, 8, 0, 5, 0, 4, 8, 0, 0, 9, 8, 6, 8, 5, 4, 7, 1, 4, 1, 6, 7, 1, 2, 7, 3, 6, 1, 9, 0, 4, 1, 5, 2, 7, 1, 0, 5, 5, 1, 3, 2, 6, 4, 2, 8, 0, 2, 1, 2, 1, 6, 5, 6, 7, 3, 8, 1, 3, 5, 3, 6, 2, 3, 4, 7, 7, 0, 5, 8, 3, 3, 0, 9, 6, 2, 0, 5, 9, 0, 5, 2, 0, 2, 7, 6, 8, 3, 2, 9, 3, 4, 0, 5, 1, 4, 2, 6, 4, 4, 8, 7, 3, 4, 5, 0, 3, 3, 6, 1, 3, 2, 0, 5, 1, 2, 1, 8, 1, 2, 3, 6, 8, 0, 8, 4, 2, 3, 8, 1, 0, 1, 6, 6, 7, 7, 7, 0, 0, 2, 1, 8, 0, 2, 9, 9, 4, 1, 4, 8, 2, 1, 6, 4, 4, 7, 2, 0, 5, 9, 7, 9, 5, 5, 4, 4, 5, 1, 6, 5, 3, 2, 6, 6, 1, 0, 1, 1, 3, 5, 2, 1, 6, 7, 4, 5, 0, 2, 0, 5, 6, 3, 4, 1, 8, 3, 8, 3, 1, 5, 5, 0, 4, 8, 2, 1, 7, 9, 4, 6, 0, 4, 1, 1, 9, 0, 0, 6, 5, 4, 4, 8, 3, 2, 2, 7, 9, 6, 3, 0, 0, 1, 5, 0, 3, 6, 0, 6, 2, 5, 1, 4, 1, 5, 1, 2, 1, 6, 0, 1, 3, 5, 2, 2, 7, 3, 3, 6, 0, 6, 4, 8, 8, 4, 7, 7, 5, 5, 3, 6, 0, 4, 5, 2, 4, 8, 9, 0, 0, 0, 5, 1, 1, 5, 6, 0, 1, 6, 9, 0, 4, 5, 4, 6, 9, 2, 7, 1, 0, 2, 7, 2, 4, 8, 2, 6, 1, 7, 0, 9, 6, 5, 3, 2, 5, 4, 4, 8, 5, 0, 2, 5, 2, 8, 6, 6, 0, 1, 4, 3, 2, 9, 7, 3, 1, 5, 1, 0, 7, 8, 3, 1, 0, 6, 2, 5, 2, 8, 9, 3, 8, 0, 1, 4, 9, 6, 3, 8, 7, 6, 8, 5, 9, 2, 8, 6, 5, 3, 7, 2, 6, 0, 0, 6, 5, 7, 6, 2, 8, 2, 5, 0, 0, 5, 7, 5, 4, 9, 5, 4, 6, 5, 4, 0, 1, 9, 0, 0, 0, 6, 5, 8, 0, 0, 8, 2, 0, 8, 0, 4, 4, 4, 3, 4, 9, 9, 8, 4, 1, 7, 4, 1, 2, 5, 6, 3, 6, 9, 2, 3, 4, 5, 4, 1, 3, 2, 1, 7, 7, 5, 9, 0, 2, 6, 4, 8, 8, 3, 7, 6, 6, 9, 9, 8, 2, 4, 1, 3, 2, 6, 7, 9, 7, 0, 6, 6, 3, 1, 4, 9, 7, 9, 6, 2, 1, 7, 2, 2, 3, 8, 1, 2, 9, 2, 0, 3, 3, 5, 8, 5, 5, 1, 6, 5, 5, 5, 2, 5, 8, 5, 2, 6, 9, 1, 6, 8, 1, 1, 9, 3, 8, 6, 8, 2, 5, 3, 8, 7, 6, 8, 2, 1, 6, 1, 8, 5, 1, 5, 1, 5, 3, 2, 5, 5, 4, 8, 8, 9, 9, 1, 4, 2, 2, 4, 8, 1, 5, 4, 0, 3, 9, 1, 4, 2, 7, 8, 5, 0, 0, 7, 6, 8, 0, 4, 9, 7, 4, 7, 1, 4, 9, 6, 7, 0, 8, 9, 7, 0, 7, 1, 0, 8, 5, 8, 7, 6, 5, 3, 6, 2, 3, 1, 5, 3};

		List<Integer> aList = new ArrayList<Integer>();
		for (int aN : aNums)
		{
			aList.add(aN);
		}

		int aFailedGames = 0;
		int aGamesTotal = 200;
		boolean aLastRuleWin = false;
		int aDif = 1;
		for (int i = aList.size()-aGamesTotal; i < aList.size()-1; i++)
		{
			Map<Integer,Integer> aFrequencyMap = new HashMap<Integer,Integer>(){{
				for (int i = 0; i< 10;i++)
				{
					put(i, 0);
				}
			}};

			int aGame = aList.get(i);
			if (aLastRuleWin)
			{
				aDif = -1 * aDif;
			}

			for (int j = 2; j < i-2; j++)
			{
				if (aList.get(j) == aGame
						  && aList.get(j+aDif) == aList.get(i-1)
						  && aList.get(j+ (2 * aDif)) == aList.get(i-2)
						  )
				{

					//aDif = aLastRuleWin ? -1 : 1;
					aFrequencyMap.put(aList.get(j + aDif), aFrequencyMap.get(aList.get(j + aDif)) + 1);
				}
			}
			Map<Integer, Integer> aSortedMap = aFrequencyMap.entrySet()
					  .stream()
					  .sorted(Map.Entry.comparingByValue(Collections.reverseOrder())).collect(Collectors.toMap(
					  Map.Entry::getKey,
					  Map.Entry::getValue,
					  (e1, e2) -> e1,
					  LinkedHashMap::new
			));

			List<Integer> aHighFrequency = new ArrayList();
			int g = 0;
			for (Map.Entry<Integer, Integer> aEntry : aSortedMap.entrySet())
			{
				aHighFrequency.add(aEntry.getKey());
				if (++g >= 5)
				{
					break;
				}
			}

			Collections.sort(aHighFrequency);
			int aNext = i+1 < aList.size() ? aList.get(i+1) : -1;
			aLastRuleWin = !aHighFrequency.contains(aNext);
			System.out.println("game #" + i + " : num " + aGame + "\t\t" +
					  (aNext > -1 ? (!aLastRuleWin ? " FAILED " : "  WIN!  ") : "\t\t") +
								 "\t\t" + aHighFrequency);
			if (!aLastRuleWin)
			{
				aFailedGames++;
			}
		}
		System.out.println("\ntotal:\n" +
				  "\tWIN : " + (aGamesTotal-aFailedGames) + "\n" +
				  "\tFAIL: " + aFailedGames);

	}


	public void showStatus()
	{

		int anInd = 0;
		for (int i = 0; i < mySnippetKeys.size()-1; i++)
		{
			if (mySnippetKeys.get(i) < myCurrentGame-1 && myCurrentGame - 1 < mySnippetKeys.get(i+1))
			{
				anInd = i;
				break;
			}
		}
		int aDoubleGap = kGapSize * 2;

		List<Integer> aDivList = new ArrayList<>();
		int aStart = anInd - aDoubleGap;
		aStart = aStart < 0 ? 0 : aStart;
		for (int i = aStart; 0 <= i && i < anInd; i++)
		{
			int aKey = mySnippetKeys.get(i);
			int aValue = mySnippetMap.get(aKey);
			aDivList.add(aValue - aKey);
		}
		Utils.printf(aDivList, 6, ", ", "\t | ");
		int aKey = mySnippetKeys.get(anInd);
		int aValue = mySnippetMap.get(mySnippetKeys.get(anInd));
		System.out.print(aValue - aKey + " | \t ");
		aDivList = new ArrayList<>();
		for (int i = anInd +1; i < anInd + aDoubleGap+1 && i < mySnippetKeys.size() ; i++)
		{
			int aKey2 = mySnippetKeys.get(i);
			int aValue2 = mySnippetMap.get(aKey2);
			aDivList.add(aValue2 - aKey2);
		}
		Utils.printf(aDivList, 6, ", ", "");

		//double aRate = (double)Utils.getGameHDelta(mySnippetKeys.subList(anInd-kGapSize, anInd+kGapSize+1)).stream().mapToInt(Integer::intValue).sum() / Utils.getGameHDelta(mySnippetKeys.subList(anInd - 2*kGapSize, anInd +2*kGapSize)).stream().mapToInt(Integer::intValue).sum();
		//System.out.println(aRate + "\n");
		double aD =   ((double)myCurrentGame-aKey)/(aValue - aKey);
		System.out.printf("\n == %.2f == ", aD);
		myRate.add( (int)(aD*10));
		System.out.println();
	}
	public static List<Integer> myRate = new ArrayList<>();



}
