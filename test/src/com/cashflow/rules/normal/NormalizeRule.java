
package com.cashflow.rules.normal;

import com.cashflow.rules.AbstractRule;
import main.Combination;
import pb.Chunk;
import pb.Meters;
import pb.Utils;
import pb.file.FileReader;
import pb.statistics.ChunkStConfig;

import java.text.NumberFormat;
import java.util.*;
import java.util.concurrent.Delayed;

/**



 * @author Ruslan Dauhiala
 */
public class NormalizeRule extends AbstractRule
{
	Map<Date, List<Double>> myRateMap;
	Set<Double> mySumList;

	Set<Chunk> myRateChunk5Set;
	Collection myRateChunk4Set;
	Set<Chunk> myRateChunk3Set;
	Set<List<Double>> myRateChunk2Set;
	Set<Double> myRate1Set;


	private Config myConfig;

	class Config
	{
		public Config(int theMinBoundaryGap, int theMaxBoundaryGap, double theDeltaToBoundaryGapMin, double theDeltaToBoundaryGapMax)
		{
			myMinBoundaryGap = theMinBoundaryGap;
			myMaxBoundaryGap = theMaxBoundaryGap;
			myDeltaToBoundaryGapMin = theDeltaToBoundaryGapMin;
			myDeltaToBoundaryGapMax = theDeltaToBoundaryGapMax;
		}

		int myMinBoundaryGap;
		int myMaxBoundaryGap;

		double myDeltaToBoundaryGapMin;
		double myDeltaToBoundaryGapMax;
	}

	public NormalizeRule(Map<Date, List<Integer>> theGamesMap, Map<Date, List<Integer>> theGamesMMMap)
	{
		super(theGamesMap, theGamesMMMap);
	}

	private Set<Double> myMaxPersent;
	private Set<Double> my3140Persent;
	private Set<Double> my3241Persent;
	private Set<Double> my2140Persent;
	private Set<Double> my2130Persent;

	long myPrevGameBigNumMin;
	long myPrevGameBigNumMax;
	long myPrevGameBigNum;
	static
	long kBigNumGapMin =
			  // 69_705_764; 		// 0, #: [] 		 Denied: 	 614985;  Accepted: 10623528
			  // 100_210_703;   	// 0, #: [] 		 Denied: 	 894011;  Accepted: 10344502 		 time: 	 8; 	 total : 194 sec.
			     206_028_683;      // 0, #: [] 		 Denied: 	 1811251;  Accepted: 9427262 		 time: 	 8; 	 total : 193 sec.
			  // 300_210_703;      // 3, #: [3, 7, 13] 		 Denied: 	 2472366;  Accepted: 8766147 		 time: 	 8; 	 total : 183 sec
			  // 500_210_703;   		// 7, #: [0, 3, 6, 7, 9, 13, 18] 		 Denied: 	 3963537;  Accepted: 7274976 		 time: 	 7; 	 total : 172 sec.

	static long kBigNumGapMax = 1_496_798_078 ;
	// 206_028_683  			  4, #: [12, 15, 16, 19] 		 Denied: 	 4692965;  Accepted: 6545548 		 time: 	 7; 	 total : 166 sec.

	public boolean accept3(List<Integer> thePotentialGame)
	{
		boolean anAccept = true;
		double aMinSum = getMinSumD(thePotentialGame);
		anAccept = !myMinSumList.contains(aMinSum);  // ++ ERRORS: 	 5, #: [3, 5, 6, 12, 14] 		 Denied: 	 3361000;  Accepted: 7877513 		 time: 	 4; 	 total : 107 sec.
		if (anAccept)
		{
			double aMinSumPrev = getMinSumD(myGamesMap.get(myDates.get(0)).subList(0,5));
			double aDelta = Math.abs(aMinSumPrev - aMinSum);
			anAccept = 0.02368207623682078 < aDelta && aDelta < 0.28214731585518105;  // ERRORS: 	 5, #: [3, 5, 6, 12, 14] 		 Denied: 	 3007569;  Accepted: 8230944 		 time: 	 4; 	 total : 101 sec.
		}

		trackResult(anAccept);
		return anAccept;
	}

	// ==========

	public boolean minMaxSumAccept(List<Integer> thePotentialGame)
	{
		double aD = getSumMinMax(thePotentialGame);
		boolean anAccept = true;
		double aDelta = myFirstSumMinMax - aD;
		double aDeltaMM = myFirstSumMMMinMax - aD;
		anAccept = anAccept && kMinGap < aDelta && aDelta < kMaxGap;
		anAccept = anAccept && -5 < aDeltaMM && aDeltaMM < 5;

		// 11 = 80k alone
		// 5  = 857477 alone !!!
		// in pare      1, #: [1] 		 Denied: 	 2009250;  Accepted: 9229263 		 time: 	 6; 	 total : 141 sec.

		anAccept = anAccept && !mySumMinMax.contains(aD);
		anAccept = anAccept && !mySumMinMaxDelta.contains(aDelta);
//		//  alone : 0, #: [] 		 Denied: 	 133618;  Accepted: 11104895 		 time: 	 5; 	 total : 135 sec.
//		// in pare   +70k   Denied: 	 1117776;

		anAccept = anAccept && !mySumMinMaxMMDelta.contains(aDeltaMM);
		return anAccept;
	}

	Set<Double> mySumMinMax;
	Set<Double> mySumMinMaxDelta;
	Set<Double> mySumMinMaxMMDelta;
	double myFirstSumMinMax;
	double myFirstSumMMMinMax;
	double kMinGap;
	double kMaxGap;

	public void minMaxSumLoad()
	{
		mySumMinMax = new HashSet<>();

		List<Double> aDoubles = new ArrayList<>();
		for (int i = 0; i < myDates.size(); i++)
		{
			aDoubles.add(getSumMinMax(myGamesMap.get(myDates.get(i)).subList(0, 5)));
		}

		List<Double> aDoublesMM = new ArrayList<>();
		for (int i = 0; i < myMMGamesMap.size(); i++)
		{
			aDoublesMM.add(getSumMinMax(myMMGamesMap.get(myDates.get(i)).subList(0, 5)));
		}

		// 100 = 0, #: [] 		 Denied: 	 405223;  Accepted: 10833290 		 time: 	 5; 	 total : 119 sec.
		mySumMinMax.addAll(aDoubles.subList(0, 150));
		mySumMinMax.addAll(Utils.getAgoFromGapNumbers(150, aDoubles));// w/o = 0, #: [] 		 Denied: 	 405223;
		// 	100/100 =    1, #: [1] 		 Denied: 	 848964;  Accepted: 10389549 		 time: 	 11; 	 total : 244 sec.
		// 	150/150 = 	 1, #: [1] 		 Denied: 	 1117776;  Accepted: 10120737 		 time: 	 5; 	 total : 120 sec.
		// 	150/250 =    2, #: [1, 4] 		 Denied: 	 1439975;  Accepted: 9798538 		 time: 	 5; 	 total : 122 sec.


		mySumMinMax.addAll(aDoublesMM.subList(0, 50));
		mySumMinMax.addAll(Utils.getAgoFromGapNumbers(50, aDoublesMM));
		//  0		    1, #: [1] 		 Denied: 	 2448115;  Accepted: 8790398 		 time: 	 5; 	 total : 130 sec.
		// 50 	 	 1, #: [1] 		 Denied: 	 2586782;  Accepted: 8651731 		 time: 	 6; 	 total : 139 sec.
		// 50+50     1, #: [1] 		 Denied: 	 2746912;  Accepted: 8491601 		 time: 	 5; 	 total : 128 sec.
		//
		mySumMinMaxDelta = new HashSet<>(Utils.getGameDelta(aDoubles));
		mySumMinMaxMMDelta = new HashSet<>(Utils.getGameDelta(aDoublesMM));

		myFirstSumMinMax = aDoubles.get(0);
		myFirstSumMMMinMax = aDoublesMM.get(0);
		double aPrevGap = aDoubles.get(1) - aDoubles.get(0);
		double aPrevPrevGap = aDoubles.get(2) - aDoubles.get(1);
		kMinGap = -5;
		kMaxGap = 5;
		if (aPrevGap < 0 && aPrevPrevGap < 0)
		{
			kMinGap = 0;
		} else if (0 < aPrevGap && 0 < aPrevPrevGap)
		{
			kMaxGap = 0;
		}      // + 300k

		// -5<x<5 	 #: [1] 		 Denied: 	 2009250;  Accepted: 9229263 		 time: 	 6; 	 total : 141 sec.
	}

	// ============



	Set<Integer> myFirstLastSum;
	Set<Integer> myFirstLastDelta;
	Set<Double> myFirstLastDiv;


	void firstLastLoad()
	{
		List<Integer> aSum = new ArrayList<>();
		List<Integer> aDelta = new ArrayList<>();
		List<Double> aDiv = new ArrayList<>();

		for (List<Integer> aGame : myGamesMap.values())
		{
			aDelta.add(aGame.get(4) - aGame.get(0));
			aSum.add(aGame.get(4) + aGame.get(0));
			aDiv.add((double)aGame.get(4) / aGame.get(0));
		}
		myFirstLastSum = new HashSet<>(Utils.getAgoFromGapNumbers(3, aSum));
		// pilot??
		// 3 =>  0, #: [] 		 Denied: 	 750301;  Accepted: 10488212 		 time: 	 4; 	 total : 108 sec.
		// 5     1, #: [11] 		 Denied: 	 1236227;  Accepted: 10002286 		 time: 	 3; 	 total : 92 sec.
		// 10 =  3, #: [8, 10, 11] 		 Denied: 	 2047194;  Accepted: 9191319 		 time: 	 4; 	 total : 93 sec.

		myFirstLastDelta = new HashSet<>(Utils.getAgoFromGapNumbers(3, aDelta));
 		// one rule only
 		// 3 => 0, #: [] 		 Denied: 	 795621;  Accepted: 10442892 		 time: 	 4; 	 total : 109 sec.

		myFirstLastDiv = new HashSet<>(Utils.getAgoFromGapNumbers(10, aDiv));
		// 3 => 0, #: [] 		 Denied: 	 70676;  Accepted: 11167837 		 time: 	 4; 	 total : 102 sec.
		// 10 => 0, #: [] 		 Denied: 	 254047;  Accepted: 10984466 		 time: 	 4; 	 total : 99 sec.

		// TOTAL
		// 3sum + 3delta = 			0, #: [] 		 Denied: 	 1489765;  Accepted: 9748748 		 time: 	 4; 	 total : 98 sec.
		// 3sum + 3delta + 3div  	0, #: [] 		 Denied: 	 1540369;  Accepted: 9698144 		 time: 	 4; 	 total : 106 sec.
		// 3sum + 3delta + 10div  	0, #: [] 		 Denied: 	 1708415;  Accepted: 9530098 		 time: 	 4; 	 total : 101 sec.
		// 3sum + 3delta + 10div  	ERRORS: 	 20, #: [21, 35, 37, 38, 50, 64, 65, 81, 82, 95, 106, 110, 118, 135, 144, 167, 190, 191, 192, 193] 		 Denied: 	 1479208;  Accepted: 9759305 		 time: 	 4; 	 total : 931 sec.
	}

	boolean firstLastAccept(List<Integer> thePotentialGame)
	{
		boolean anAccept = true;
		anAccept = anAccept && !myFirstLastSum.contains(thePotentialGame.get(4) + thePotentialGame.get(0));
		anAccept = anAccept && !myFirstLastDelta.contains(thePotentialGame.get(4) - thePotentialGame.get(0));
		anAccept = anAccept && !myFirstLastDiv.contains((double)thePotentialGame.get(4) / thePotentialGame.get(0));
		return anAccept;
	}

	// ==============

	public boolean accept(List<Integer> thePotentialGame)
	{
		boolean anAccept = true;
		// anAccept = anAccept && accept4(thePotentialGame);

		anAccept = anAccept && minMaxSumAccept(thePotentialGame); // !current!
		// 1, #: [1] 		 Denied: 	 2746912;  Accepted: 8491601 		 time: 	 5; 	 total : 134 sec.
		// out of 200 :
		// ERRORS: 	 64, #: [1, 20, 22, 23, 31, 35, 36, 37, 39, 46, 48, 53, 54, 55, 56, 59, 62, 63, 66, 71, 79, 81, 90, 92, 93, 95, 104, 112, 124, 131, 132, 134, 137, 138, 139, 141, 142, 146, 150, 152, 153, 155, 156, 157, 163, 164, 165, 166, 169, 172, 175, 176, 178, 179, 180, 181, 185, 186, 188, 189, 194, 197, 198, 199]
		// Denied: 	 3693088;  Accepted: 7545425 		 time: 	 4; 	 total : 1074 sec.

		anAccept = anAccept && firstLastAccept(thePotentialGame);
		//
		// minMaxSumAccept + firstLastAccept
		// 1, #: [1] 		 Denied: 	 4103184;  Accepted: 7135329 		 time: 	 4; 	 total : 107 sec.

		trackResult(anAccept);
		return anAccept;
		//
	}

	public void setup()
	{
		//load4();
		//minMaxSumLoad();
		firstLastLoad();
	}

	Set<Double> myMinSumList;
	public void loadConfigFromStatistic3()
	{
		myMinSumList = new HashSet<>(getMinSumlist(50));
		//50   ERRORS: 	 0, #: [] 		 Denied: 	 452342;  Accepted: 10786171 		 time: 	 5; 	 total : 120 sec.
		// 500 ERRORS: 	 4, #: [2, 6, 10, 19] 		 Denied: 	 2296899;  Accepted: 8941614 		 time: 	 4; 	 total : 111 sec.

	}

	public void load4()
	{
		myPrevGameBigNum = getBigNumber(myGamesMap.get(myDates.get(0)).subList(0, 5));

		initRateList();

		//myRateChunk5Set = getChunkSet(myRateIntList, new ChunkStConfig(5, false, myRateIntList.size()));                  // [1, 1, 2, 2, 2, 2,
		//myRateChunk4Set = getChunkSetO(myRateIntList, new ChunkStConfig(4, false, myRateIntList.size()));                  // [1, 1, 2, 2, 2, 2,
		//myRateChunk3Set = getChunkSet(myRateIntList, new ChunkStConfig(3, false, 500));                  //[695, 1050, 1676, 1760, 2017, 2017


		myRate1Set = new HashSet<>();

		Set<List<Double>> anAgoSet = Utils.getAgoChunks(myRateMap, 30, new ChunkStConfig(5, true), myDates );
		for (List<Double> aList : anAgoSet)
		{
			myRate1Set.addAll(aList);      //[1, 2, 3, 5, 6, 9, 9, 11, 15, 18, 18, 23, 24, 25,
		}

		for (int i = 0; i < 30; i++)
		//  15 	ERRORS:     	 0, #: []  		denied: 	 1201672;
		//= 30 	ERRORS:     	 0, #: [] 		 Denied: 	 2121922;
		//  40 	ERRORS: 	 3, #: [8, 12, 19] 		 Denied: 	 2529888;
		//  50 	ERRORS: 	    4, #: [6, 8, 12, 19] 		 Denied: 	 2936032;
		//  200	ERRORS: 	 16, #: [0, 1, 2, 3, 4, 5, 6, 8, 9, 10, 12, 13, 15, 16, 17, 19] 		 Denied: 	 8675887;  Accepted: 2562626 		 time: 	 46;
		{
			//myRate1Set.addAll(myRateMap.get(myDates.get(i)));      //[1, 2, 3, 5, 6, 9, 9, 11, 15, 18, 18, 23, 24, 25,
		}

//		myPrevGameBigNumMin = myPrevGameBigNum - kBigNumGapMin;
//		myPrevGameBigNumMax = myPrevGameBigNum + kBigNumGapMin;
//		if (myPrevGameBigNumMin < 0)
//		{
//			myPrevGameBigNumMax += -myPrevGameBigNumMin;
//			myPrevGameBigNumMin = 0;
//		}

	}


	public boolean accept4(List<Integer> thePotentialGame)
	{

		boolean anAccept = true;

		//long aBigN = getBigNumber(thePotentialGame);
		//long aGap = Math.abs(aBigN - myPrevGameBigNum);
		//anAccept = kBigNumGapMin < aGap && aGap < kBigNumGapMax ;

		//anAccept = !(myPrevGameBigNumMin < aBigN && aBigN < myPrevGameBigNumMax);

		List<Double> aDoubles = geDoubleGameRate(thePotentialGame);
		for (double aD : aDoubles)
		{
			anAccept = anAccept && !myRate1Set.contains(aD);
			//anAccept = anAccept && !gameChunkCheckO(Utils.getAllChunksO(aDoubles, 2), myRateChunk2Set);
		}

		trackResult(anAccept);
		return anAccept;
	}


	public void loadConfigFromStatistic2()
	{
		long aPrevGameBigNum = getBigNumber(myGamesMap.get(myDates.get(0)).subList(0,5));
		myPrevGameBigNumMin = aPrevGameBigNum - kBigNumGapMin;
		myPrevGameBigNumMax = aPrevGameBigNum + kBigNumGapMin;

		if (myPrevGameBigNumMin < 0)
		{
			myPrevGameBigNumMax += -myPrevGameBigNumMin;
			myPrevGameBigNumMin = 0;
		}

		//myConfig = new Config(20, 52, .30, .80);
		myConfig = new Config(17, 56, .32, .79);    // .02 + .02
		//myConfig = new Config(14, 60, .30, .82);
		List<Double> aMaxDeltaPercent = getMaxDeltaPercentList();
		myMaxPersent = Utils.getAgoFromGapNumbers(20, aMaxDeltaPercent);// new HashSet<>();
		// pilot 	 10 = err 1, #: [1] DENIED: 	 4257212
		// 			 20 = err 1, #: [1] DENIED: 	 4515395
		// 			 30 = err 2, #: [1, 11]            		DENIED: 	 4705421
		// 			 40 = err 4, #: [1, 2, 11, 16] [1] 		DENIED: 	 4909947


		List<Double> a3140Percent = Utils.getDeltaPercentList(3, 1, 4, 0, myGamesMap.values());
		my3140Persent = Utils.getAgoFromGapNumbers(40, a3140Percent);// new HashSet<>();
		// pilot     20 = ERRORS:     	 1, #: [1]  DENIED: 	 4698761
		// 			 30 = ERRORS:     	 1, #: [1]  DENIED: 	 4773676
		//           40 = ERRORS:     	 1, #: [1]  DENIED: 	 4827983
		// 			 50 = ERRORS:     	 2, #: [1, 16]		DENIED: 	 4884078

//		List<Double> a3241Percent = getDeltaPercentList(3, 2, 4, 1);
//		my3241Persent = getAgoFromGapNumbers(20, a3241Percent);// new HashSet<>();
		// pilot  ?? 10 =              	 2, #: [1, 5]       DENIED: 	 5031290
		// pilot  ?? 20 =  ERRORS:     	 3, #: [1, 5, 11]   DENIED: 	 5247934

//		List<Double> a2140Percent = getDeltaPercentList(2, 1, 4, 0);
//		my2140Persent = getAgoFromGapNumbers(10, a2140Percent);
		// pilot  ?? 10 =    2, #: [1, 3]   DENIED: 	 4994142

		List<Double> a2130Percent = Utils.getDeltaPercentList(2, 1, 3, 0, myGamesMap.values());
		my2130Persent = Utils.getAgoFromGapNumbers(40, a2130Percent);
		// pilot   10 =  		  1, #: [1]     DENIED: 	 5032544
		//			  20 = 		  1, #: [1]     DENIED: 	 5235392
		//			  30 = 		  1, #: [1]     DENIED: 	 5421057
		//			  40 = 		  1, #: [1]     DENIED: 	 5617764
		//			  50 = 		  ERRORS:     	 2, #: [1, 8]      DENIED: 	 5781283



//		Set<List<Double>> a1Ago = Utils.getAgoChunks(myRateMap, 30, new ChunkStConfig(1, false), myDates);
//		for (List<Double> aList : a1Ago )
//		{
//			myRate1Set.addAll(aList);
//		}
		// 1 + 30   ERRORS: 	 4, #: [6, 8, 12, 17] 		 Denied: 	 3229472;  Accepted: 8009041
		// 2 + 30   ERRORS: 	 3, #: [4, 6, 19] 		    Denied: 	 3438686;  Accepted: 7799827 		 time: 	 9; 	 total : 198 sec.
		// 2 + 50   ERRORS: 	 5, #: [2, 4, 6, 15, 19] 		 Denied: 	 3976543;  Accepted: 7261970 		 time: 	 10; 	 total : 228 sec.
		// 2 + 100  ERRORS:    ERRORS: 	 10, #: [1, 2, 3, 4, 6, 12, 13, 15, 17, 19] 		       Denied: 	 5528773;  Accepted: 5709740 		 time: 	 19; 	 total : 406 sec.
		// 2 + 500  ERRORS: 	 15, #: [1, 2, 3, 4, 5, 6, 8, 9, 10, 12, 13, 15, 16, 17, 19] 		 Denied: 	 8341976;  Accepted: 2896537 		 time: 	 17; 	 total : 364 sec.

		myRateChunk2Set = new HashSet<>();
		//myRateChunk2Set.addAll(Utils.getAgoChunks(myRateMap, 500, new ChunkStConfig(2, false), myDates));
		// 100  =  ERRORS: 	 1, #: [6] 		 Denied: 	 2287296;  Accepted: 8951217 		 time: 	 110;

		//myRateChunk2Set = getChunkSet(myRateMap, new ChunkStConfig(2, false, 30, 100));                  // [15, 28, 66, 70, 174, 175, 236, 246, 291,
		// 100  = ERRORS: 	 1, #: [5] 									 Denied: 	 2463399;  time: 	 104;
		// 500  = ERRORS: 	 3, #: [5, 6, 9] 		 					 Denied: 	 3680967;  time: 	 95;
		// 1000 = ERRORS: 	 6, #: [3, 5, 6, 9, 17, 19] 			 Denied: 	 5007599;  time: 	 93;
		// 2k   = ERRORS: 	 8, #: [1, 3, 4, 5, 6, 9, 17, 19] 	 Denied: 	 6722196;
	}




	public boolean accept2(List<Integer> thePotentialGame)
	{
		int aGap = thePotentialGame.get(4) - thePotentialGame.get(0);

		boolean anAccept = true;
		anAccept =
				  //myConfig.myMinBoundaryGap < aGap && aGap < myConfig.myMaxBoundaryGap &&
				  !allOddOrEven(thePotentialGame, false) &&
				  !allOddOrEven(thePotentialGame, true);

		//if (anAccept)
		if (false)
		{
			// - ~200k
			int aMaxDeltaGap= Collections.max(Utils.getGameHDelta(thePotentialGame.subList(0, 5)));
			double aRate = (double)aMaxDeltaGap/aGap;
			//anAccept = myConfig.myDeltaToBoundaryGapMin < aRate && aRate < myConfig.myDeltaToBoundaryGapMax;
			if (anAccept)
			{
				//anAccept = !myMaxPersent.contains(aRate);
				if (anAccept)
				{
// ???
					int aGap2 = thePotentialGame.get(3) - thePotentialGame.get(1);
					double aDouble = (double)aGap2/aMaxDeltaGap;
					anAccept = !my3140Persent.contains(aDouble);
					if (anAccept)
					{
//						int aGap11 = thePotentialGame.get(3) - thePotentialGame.get(2);
//						int aGap22 = thePotentialGame.get(4) - thePotentialGame.get(1);
//						double aDouble2 = (double)aGap11/aGap22;
//						anAccept = !my3241Persent.contains(aDouble2);
//						int aGap11 = thePotentialGame.get(2) - thePotentialGame.get(1);
//						int aGap22 = thePotentialGame.get(4) - thePotentialGame.get(0);
//						double aDouble2 = (double)aGap11/aGap22;
//						anAccept = !my2140Persent.contains(aDouble2);

						int aGap11 = thePotentialGame.get(2) - thePotentialGame.get(1);
						int aGap22 = thePotentialGame.get(3) - thePotentialGame.get(0);
						double aDouble2 = (double) aGap11 / aGap22;
//						anAccept = !my2130Persent.contains(aDouble2);

					}
				}
			}
		}

		List<Double> aDoubles = geDoubleGameRate(thePotentialGame);

		for (double aD : aDoubles)
		{
			anAccept = anAccept && !myRate1Set.contains(aD);
			//anAccept = anAccept && !gameChunkCheckO(Utils.getAllChunksO(aDoubles, 2), myRateChunk2Set);
		}

		// ??
		anAccept = anAccept && !mySumList.contains(sumD(aDoubles));

		trackResult(anAccept);
		return anAccept;
	}

	boolean gameCheck(List theList, int theSize, List<List> theBigBroList, List theBigSize2)
	{
	//	for ()
		return false;
	}

	private List<Double> getMaxDeltaPercentList()
	{
		List<Double> aMaxDeltaPersent = new ArrayList<>();
		for (List<Integer> aGame : myGamesMap.values())
		{
			int i = aGame.get(4) - aGame.get(0);
			int aMaxGameDelta = Collections.max((Utils.getGameHDelta(aGame.subList(0, 5))));
			aMaxDeltaPersent.add((double) aMaxGameDelta / i);
		}
		return aMaxDeltaPersent;
	}



	private void testNomal()
	{
		List<Short> aMaxGap = new ArrayList<>();
		List<Integer> aMaxDelta = new ArrayList<>();
		List<Double> aMaxDeltaPersent = new ArrayList<>();

		int allEven = 0;
		int allOdd = 0;
		for (List<Integer> aGame : myGamesMap.values())
		{
			int i = aGame.get(4) - aGame.get(0);
			int aMaxGameDelta = Collections.max((Utils.getGameHDelta(aGame.subList(0, 5))));

			aMaxDelta.add(aMaxGameDelta);
			aMaxDeltaPersent.add( (double) aMaxGameDelta / i);

			aMaxGap.add((short)i);
			if (allOddOrEven(aGame.subList(0,5), false))
			{
				allOdd++;
				//System.out.println("odd:  " + aGame);
			} else if (allOddOrEven(aGame.subList(0,5), true))
			{
				allEven++;
				//System.out.println("\teven:  " + aGame);
			}
		}

		Collections.sort(aMaxGap);

		double aPersent = .02;
		int a5th =(int) (aMaxGap.size() * aPersent);
		System.out.println(" ☆☆☆☆☆ x[4]-x[0] ☆☆☆☆\u2606 : \n   " + aMaxGap);

		System.out.println("  = min " + (aPersent*100) + "% : " + aMaxGap.get(a5th));
		System.out.println("  = max " + (aPersent*100) + "% : " + aMaxGap.get(myGamesMap.size() - a5th));

		System.out.println("total all even : " + allEven);
		System.out.println("total all odd  : " + allOdd);

		Collections.sort(aMaxDelta);
		System.out.println("\n \u2605★★★★ MAX(x[i]-x[j]) ★★★★★ \n   " + aMaxDelta);
		System.out.println("  = min  " + (aPersent*100) + "% : " + aMaxDelta.get(a5th));
		System.out.println("  = max  " + (aPersent*100) + "% : " + aMaxDelta.get(myGamesMap.size() - a5th));

		System.out.print("\n ★★★★★ MAX(x[i]-x[j]) / (x[4]-x[0]) ★★★★★ \n   ");
		NumberFormat aNf = NumberFormat.getNumberInstance();
		aNf.setMaximumFractionDigits(2);
		aNf.setMinimumFractionDigits(2);
		for (double aD : aMaxDeltaPersent)
		{
			System.out.print(aNf.format(aD) + " ");
		}
		List<Integer> aGapList = Utils.getGapBetweenSameNumbers(aMaxDeltaPersent);

		Collections.sort(aMaxDeltaPersent);

		System.out.print("\nsorted:   \n   ");
		for (double aD : aMaxDeltaPersent)
		{
			System.out.print(aNf.format(aD) + " ");
		}

		System.out.print("\n\ngap list: \n   " + aGapList);
		Collections.sort(aGapList);
		System.out.println("\nsorted gap list: \n   " + aGapList);

		//System.out.println("]");
		System.out.println("  = min " + (aPersent*100) + "% : " + aNf.format(aMaxDeltaPersent.get(a5th)));
		System.out.println("  = max " + (aPersent*100) + "% : " + aNf.format(aMaxDeltaPersent.get(myGamesMap.size() - a5th)));
		System.out.println();

		///
		List<Double> a2List = Utils.getDeltaPercentList(3, 1, 4, 0, myGamesMap.values());
		System.out.print("\n\n2 list: \n   " );
		for (double aD : a2List)
		{
			System.out.print(aNf.format(aD) + " ");
		}
		List<Integer> aGapList2 = Utils.getGapBetweenSameNumbers(a2List);

		Collections.sort(a2List);
		System.out.print("\nsorted:   \n   ");
		for (double aD : a2List)
		{
			System.out.print(aNf.format(aD) + " ");
		}
		System.out.print("\n\ngap list: \n   " + aGapList2);
		Collections.sort(aGapList2);
		System.out.println("\nsorted gap list: \n   " + aGapList2);



	}

	private boolean allOddOrEven(List<Integer> theGame, boolean theEven)
	{
		for (int i : theGame)
		{
			if (i % 2 == (theEven ? 1 : 0))
			{
				return false;
			}
		}
		return true;
	}

	private void testPrevGames()
	{
	//	setup();
		for (List<Integer> aList : myGamesMap.values())
		{
			accept(aList.subList(0,5));
		}
		printReport();
	}

	private void testFraction()
	{
		List<Integer> aPercent = new ArrayList<>();
		for (List<Integer> aGame : myGamesMap.values())
		{
			int i = aGame.get(4) - aGame.get(0);
			int aMaxGameDelta = Collections.max((Utils.getGameHDelta(aGame.subList(0, 5))));

		}
	}



	private double getGameRate(int theInd, List<Integer> theGame)
	{
		return ((double) theGame.get(theInd)/ (sum(theGame) -  theGame.get(theInd)));
	}

	private int sum(List<Integer> theGame)
	{
		int aSum = 0;
		for (int i : theGame)
		{
			aSum+=i;
		}
		return aSum;
	}
	private int min(List<Integer> theGame)
	{
		int aMin = theGame.get(theGame.size()-1);
		for (int i = theGame.size()-2; 0 <= i; i--)
		{
			aMin = aMin- theGame.get(i);
		}
		return Math.abs(aMin);
	}

	private double sumD(List<Double> theGame)
	{
		double aSum = 0;
		for (double i : theGame)
		{
			aSum+=i;
		}
		return aSum;
	}

	private List<Double> geDoubleGameRate(List<Integer> theGame)
	{
		List<Double> aDoubles = new ArrayList<>();
		for (int i = 0; i < theGame.size(); i++)
		{
			aDoubles.add(getGameRate(i, theGame));
		}
		return aDoubles;
	}


	private void initRateList()
	{
		myRateMap = new LinkedHashMap<>();
		mySumList = new HashSet<>();
		for (Date aDate : myDates)
		{
			List<Integer> aGame = myGamesMap.get(aDate).subList(0,5);
			List<Double> aRate = new ArrayList<>();
			for (int i = 0; i < aGame.size(); i++)
			{
				aRate.add(getGameRate(i, aGame));
			}
			myRateMap.put(aDate, aRate);
			mySumList.add(sumD(aRate));
		}
	}

	private void test4()
	{
		int aC = 0;

		initRateList();

//		Set<List<Double>> a1Ago = Utils.getAgoChunks(myRateMap, 30, new ChunkStConfig(1, false), myDates);
//		for (List<Double> aList : a1Ago )
//		{
//			myRate1Set.addAll(aList);
//		}

//		myRateChunk2Set.addAll(getChunkSet(myRateMap, new ChunkStConfig(2, false, 30, myRateMap.size() - 1)));
		//myRateChunk2Set.addAll(Utils.getAgoChunks(myRateMap, 30, new ChunkStConfig(2, false), myDates));

		List<Long> aBigNumDiff = new ArrayList<>();

		for (int i = 0; i < myDates.size(); i++)
		{
			List<Integer> aGame = myGamesMap.get(myDates.get(i)).subList(0,5);
			aBigNumDiff.add(getBigNumber(aGame));
		}
		aBigNumDiff = Utils.getGameHDeltaLong(aBigNumDiff);

		for (Map.Entry<Date, List<Double>> anEntry : myRateMap.entrySet())
		{
			System.out.printf("%1$4d", aC);
			System.out.print(" \t\t" + Meters.kOutDateFormat.format(anEntry.getKey())  + " \t\t ");
			List<Integer> aGame = myGamesMap.get(anEntry.getKey()).subList(0,5) ;
			Utils.printf(aGame, 2, " ", " \t\t ");
			if (aC < aBigNumDiff.size())
			{
				System.out.printf("%1$11d", aBigNumDiff.get((aC)));
			}
			System.out.print(" \t\t");

			double aSum = 0;
			for (int i = 0; i < 5; i++)
			{
				double aD = ((double) aGame.get(i)/ (sum(aGame) - aGame.get(i)));
				System.out.print(aD + "  ");
				aSum+=aD;
			}

			System.out.println("   \t   " + aSum);
			aC++;
		}

		System.out.println("\n\n   big num diff:   " + aBigNumDiff);
		Collections.sort(aBigNumDiff);
		System.out.println("   sorted big num diff:   " + aBigNumDiff);
		for (double aP = .01; aP < .2; aP+=.01)
		{
			int anInd = (int)(aBigNumDiff.size() * aP) ;
			System.out.println(" - " + aP+ "%:   " + aBigNumDiff.get(anInd) + " -- " + aBigNumDiff.get(aBigNumDiff.size() -anInd));
		}
//		double aPercent = .05;
//		System.out.println("   sorted big nim diff " + aPercent+ "%:   " + aBigNumDiff.get((int)(aBigNumDiff.size() * aPercent)));

		//ChunkStatistics.getAllChunkStats(myRateMap);



		List<Integer > aSumGap = Utils.getGapBetweenSameNumbers(getMaxDeltaPercentList());
//		List<Integer > aSumGap = Utils.getGapBetweenSameNumbers(mySumList);
		System.out.println("sum gap :" + aSumGap);
		for(;aSumGap.remove(new Integer(-1)););
		Collections.sort(aSumGap);
		System.out.println("sorted sum gap (" + aSumGap.size() + "): " + aSumGap);
		Map<Date, List<Double>> aD = getDelta(myRateMap);
		aD = getDelta(aD);
		aD = getDelta(aD);
		aD = getDelta(aD);

		List<Double > a4Delta = Utils.getIntList(aD);
		List<Integer > aGap = Utils.getGapBetweenSameNumbers(a4Delta);
		System.out.println("\n\n4 delta rate gap :" + aGap);
		Collections.sort(aGap);
		System.out.println("sorted 4 delta rate gap  :" + aGap);

		System.out.println("\n\n ==== \n\n");

		List<Double> aNormList= getMinSumlist(-1);

		System.out.println(" norm list:\n " + aNormList);
		List<Integer> anAgo = Utils.getGapBetweenSameNumbers(aNormList);
		List<Double> aDelta = Utils.getGameDelta(aNormList);
		Collections.sort(aNormList);
		System.out.println(" norm list sorted:\n " + aNormList);
		System.out.println(" norm list same nums gap (" + anAgo.size() + "):\n" + anAgo);
		Collections.sort(anAgo);
		for(;anAgo.remove(new Integer(-1)););

		System.out.println(" norm list same nums gap sorted (" + anAgo.size() + ")w/o -1:\n" + anAgo);
		double aPercent = .1;
		System.out.println(" gap bracket :\n " + anAgo.get((int) (anAgo.size() * aPercent)) + " - " +  anAgo.get((int)(anAgo.size()*(1-aPercent))));


		List<Double> aDeltaPos = new ArrayList<>();
		for (double aDt : aDelta)
		{
			aDeltaPos.add(Math.abs(aDt));
		}
		System.out.println(" delta :\n " + aDeltaPos);
		Collections.sort(aDeltaPos);
		System.out.println(" delta sorted :\n " + aDeltaPos);
		double aPerc = .1;
		System.out.println(" delta bracket :\n " + aDeltaPos.get((int)(aDeltaPos.size()*aPerc)) + " - " +  aDeltaPos.get((int)(aDeltaPos.size()*(1-aPerc))));


	}

	private List<Double> getMinSumlist(int theSize)
	{
		List<Double> aNormList = new ArrayList<>();
		ArrayList<List<Integer>> aGames = new ArrayList<>( myGamesMap.values());
		int aMax = 0 < theSize ? theSize : aGames.size();
		for (int i = 0; i < aMax; i++ )
		{
			aNormList.add(getMinSumD(aGames.get(i).subList(0, 5)));
		}
		return aNormList;
	}

	private double getMinSumD(List<Integer> theGame)
	{
		return min(theGame) / (double)sum(theGame);
	}


	private Map<Date, List<Double>> getDelta(Map<Date, List<Double>> theMap)
	{
		Map<Date, List<Double>> aRes = new LinkedHashMap<Date, List<Double>>();
		for (Map.Entry<Date, List<Double>> aEntry : theMap.entrySet() )
		{
			aRes.put(aEntry.getKey(), Utils.getGameDelta(aEntry.getValue()));
		}
		return aRes;
	}

	private long getBigNumber(List<Integer> theGame)
	{
		String aS = "";
		for (Integer aNum : theGame)
		{
			aS += aNum ;
		}
		return Long.parseLong(aS);
	}

	private void test5()
	{
		//List<Integer> list = new ArrayList<Integer>(Collections.nCopies(60, 0));
		List<Integer> aGamesNumbers = new ArrayList<>(Collections.nCopies(myGamesMap.size(), 0));
		int[] aGameInds = {0};
		int[] aCombinatInds = {0};
		Map<Date, List<Integer>> a5Map = Utils.get5SizeMap(myGamesMap);
		List<List<Integer>> aGameList = new ArrayList<>(a5Map.values());
		Set<List<Integer>> aHashSet = new HashSet<>(a5Map.values());

		Combination.combination(Combination.getAll69(), 5, new Combination.GamePrint()
		{
			@Override
			public void print(List<Integer> theGame)
			{
				if (aHashSet.contains(theGame))
				{
					aGamesNumbers.set(aGameList.indexOf(theGame), aCombinatInds[0]);
				}

				aCombinatInds[0] = aCombinatInds[0] + 1;
				aGameInds[0] = aGameInds[0] + 1;
			}
		});


		List<Integer> aDelta = Utils.getGameHDelta(aGamesNumbers);

		for (int i = 0; i < aDelta.size(); i++)
		{
			if (aDelta.get(i) < 0)
			{
				aDelta.set(i, -aDelta.get(i));
			}
		}

		List<Integer> aGamesNumbersCopy = new ArrayList<>(aGamesNumbers);
		List<Integer> aDeltaCopy = new ArrayList<>(aDelta);

		List<Double> aPercent = new ArrayList<>();
		for (int i = 0; i < myDates.size()-1; i++)
		{
			System.out.printf("%1$4d", i);
			System.out.print(" \t " + Meters.kOutDateFormat.format(myDates.get(i)) + " \t ");
			Utils.printf(aGameList.get(i), 3, ".", " \t|\t ");
			System.out.printf("%1$8d", aGamesNumbers.get(i));
			System.out.printf(" \t\t d: %1$8d",  aDelta.get(i));
			double aP = (double)aGamesNumbers.get(new Integer(i)) / 11_238_513;
			aPercent.add(aP);
			System.out.printf(" \t\t peace:  %1d", (int)(aP*10));
			System.out.printf("   %.2f",  aP);
			System.out.println();
		}

		System.out.println("\n section:");
		for (double d : aPercent)
		{
			System.out.printf("%.2f, ",  d);
		}

		System.out.println("\n section integer *10:");
		List<Integer> aNumList = new ArrayList<>();
		List<Byte> anOddEven = new ArrayList<>();
		for (double d : aPercent)
		{
			int a10n = (int) (d * 10);
			aNumList.add(a10n);
			System.out.print(a10n);
			anOddEven.add((byte)(a10n % 2));
		}

		List<Integer> aMod = Utils.getGameModDelta(aNumList);
		System.out.println("\n mod delta: \n" + aMod.toString().replace(", ", "").replace("[", ""));
		System.out.println("\n odd/even (in part new=>old): \n" + anOddEven.toString().replace(", ", "").replace("[", ""));
		printNextStat(aNumList);

		System.out.println("\n section integer *10 reverse (old => new):");
		List<Integer> aRevNumList = new ArrayList<>();
		anOddEven.clear();
		for (int i = aPercent.size()-1; i>=0;i--)
		{
			int aNum = (int) (aPercent.get(i) * 10);
			aRevNumList.add(aNum);
			System.out.print(aNum);
			anOddEven.add((byte)(aNum % 2));
		}
		List<Integer> aRMod = Utils.getGameModDelta(aRevNumList);
		System.out.println("\n mod delta: \n" + aRMod.toString().replace(", ", "").replace("[", ""));
		System.out.println("\n odd/even (in part old=>new): \n" + anOddEven.toString().replace(", ", "").replace("[", ""));
		printNextStat(aRevNumList);

		System.out.println("\n section integer *100:");
		for (double d : aPercent)
		{
			System.out.print((int) (d * 100));
		}

		System.out.println("\n section:\n");

		System.out.println("\n");

		System.out.println("!!! games nums: new -> old\n\t" + aGamesNumbers);

		System.out.print("!!! games nums OLD -> NEW: \n\t[");

		for (int i = aGamesNumbers.size()-1; i >-1; i--)
		{
			System.out.print(aGamesNumbers.get(i) + (i == 0 ? "": ", "));
		}
		System.out.println("]");


		List<Byte> aBytes = new ArrayList<>();
		List<Byte> aReversBytes = new ArrayList<>();
		for (Integer aInteger : aGamesNumbers)
		{
			byte aB = (byte) (aInteger % 2);
			aBytes.add(aB);
			aReversBytes.add(0, aB);
		}
		System.out.println("odd/even: \n\t" + aBytes.toString().replace(", ","").replace("[",""));
		System.out.println("odd/even (revers): \n\t" + aReversBytes.toString().replace(", ","").replace("[",""));

		System.out.println("delta:\n\t" + aDelta);
		Collections.sort(aDelta);
		Set<Integer> aTs = new TreeSet<>(aDelta) ;
		System.out.println("sorted delta (" + aTs.size() + "):\n\t" + new TreeSet<>(aDelta));
		double aGap = .02;
//		.05:    247090 - 8078902
//		.1 :    524662 - 7149650
//		.2 :   1097197 - 5803918
		System.out.println("Gap:\n\t" + aDelta.get((int)(aDelta.size()*aGap)) + " - " + aDelta.get((int)(aDelta.size()*(1-aGap))));

		Collections.sort(aGamesNumbers);
		System.out.print("\n = = = = = \nsorted games nums: " + "\n\t"
				  //		  + myGamesNumbers
		);
		Utils.printf(aGamesNumbers, 8, ",","\n");
		List<Integer> aDeltaSorted = Utils.getGameHDelta(aGamesNumbers);
		System.out.print("delta sorted games nums : \n\t"
		//		  + aDeltaSorted
		);
		Utils.printf(aDeltaSorted, 8, ",","\n");
		Collections.sort(aDeltaSorted);
		System.out.println("sorted delta sorted games nums (minimum and maximum gap between numbers) :\n\t" +
				  aDeltaSorted
		);

//		int aShort = 100;
//		List<Integer> aLimitedDeltas = aDeltaCopy.subList(0, aShort);
//		System.out.println("\nlast " + aShort + " delta :\n\t" + aLimitedDeltas);
//		Collections.sort(aLimitedDeltas);
//		System.out.println("last " + aShort + " sorted delta :\n\t" + aLimitedDeltas);
//		System.out.println("-");
//		List<Integer> aLimitedGames = aGamesNumbersCopy.subList(0, 100);
//		Collections.sort(aLimitedGames);
//		System.out.println("last " + aShort + " games nums sorted:\n\t" + aLimitedGames);
//		List aShortSortNumDelta = Utils.getGameHDelta(aLimitedDeltas);
//		System.out.println("last " + aShort + " games nums sorted DELTA:\n\t" + aShortSortNumDelta);
//		Collections.sort(aShortSortNumDelta);
//		System.out.println("last " + aShort + " games nums sorted DELTA sorted:\n\t" + aShortSortNumDelta);
//		aShortSortNumDelta = Utils.getGameHDelta(aShortSortNumDelta);
//		System.out.println("last " + aShort + " games nums sorted DELTAx2 :\n\t" + aShortSortNumDelta);
//		Collections.sort(aShortSortNumDelta);
//		System.out.println("last " + aShort + " games nums sorted DELTA sorted:\n\t" + aShortSortNumDelta);

//
// List<Integer> aLimDelta = Utils.getGameDelta(aLimitedInds)

		int aMatchPoint = 0;
		int aTotalChecked = 0;
		Set<Integer> aBigNumSet = new TreeSet<>(aGamesNumbers);
		Set<Integer> aSumInds = new TreeSet<>();
		Set<Integer> aDivInds = new TreeSet<>();
		for (int i = 0; i <
				  aGamesNumbersCopy.size()
				  //1000
				  ; i++)
		{
			int aBigInd = aGamesNumbersCopy.get(i);
			if(aBigInd == 0)
			{
				continue;
			}

			aBigNumSet = new TreeSet<>(aGamesNumbersCopy.subList(i+1,aGamesNumbersCopy.size()));

			for (int j = i+1; j < aDeltaCopy.size(); j++)
			{
				if (j==i || i-1==j)
				{
					continue;
				}
				int aDeltaNum = aDeltaCopy.get(j);
				int aSum = aBigInd + aDeltaNum;
				int aDiv = Math.abs(aBigInd - aDeltaNum);
				//int aDiv = aBigInd - aDeltaNum;
				if (11_238_513 < aSum )
				{
					aSum -= 11_238_513;
				}
				aTotalChecked++;
				if (aBigNumSet.contains(aSum))
				{
					aSumInds.add(i);
					aMatchPoint++;
					//break;

				}
//				if (aBigNumSet.contains(aDiv))
//				{
//					aDivInds.add(i);
//					aMatchPoint++;
//					//break;
//				}
			}
		}
		System.out.println("\n==========\n aTotalChecked:" + aTotalChecked + "; aMatchPoint : " + aMatchPoint + "\n" +
				  "\taSumInd s=" + aSumInds.size() + ": " + aSumInds + "\n" +
				  "\taDivInd s=" + aDivInds.size() + ": " + aDivInds + "\n" +
				  "\ttotal: " +  (aSumInds.size() + aDivInds.size()));

		List<Integer> aSortedGamesInd = new ArrayList<>(aGamesNumbersCopy);
		Collections.sort(aSortedGamesInd);
		List<Integer> aGapList = new ArrayList<>();
		int aSize =
				  //500;
				  aGamesNumbersCopy.size()-1;
		for (int i = 0; i < aSize; i++)
		{
			int aGameInd = aGamesNumbersCopy.get(i);
			int aSortedInd = aSortedGamesInd.indexOf(new Integer(aGameInd));
			if (-1 < aSortedInd)
			{
				if (-1 < aSortedInd-1 && aSortedInd+1 < aSortedGamesInd.size())
				{
					aGapList.add(aSortedGamesInd.get(aSortedInd+1) - aSortedGamesInd.get(aSortedInd-1));

//					int aGap1 = aSortedGamesInd.get(aSortedInd+1) - aSortedGamesInd.get(aSortedInd);
//					aSortedGamesInd.remove(new Integer(aGap1));
//					aGap1 = aSortedGamesInd.get(aSortedInd) - aSortedGamesInd.get(aSortedInd-1);
//					aSortedGamesInd.remove(new Integer(aGap1));

				}
				aSortedGamesInd.remove(aSortedInd);
			}

		}

		// == gap2
		List<Integer> aSortedGapList2 = new ArrayList<>(aGapList);
		Collections.sort(aSortedGapList2);
		List<Integer> aGapList2 = new ArrayList<>();
		for (int i = 0; i < aGapList
				  .size(); i++)
		{
			int aGameInd = aGapList.get(i);
			int aSortedInd = aSortedGapList2.indexOf(new Integer(aGameInd));
			if (-1 < aSortedInd)
			{
				if (-1 < aSortedInd-1 && aSortedInd+1 < aSortedGapList2.size())
				{
					aGapList2.add(aSortedGapList2.get(aSortedInd+1) - aSortedGapList2.get(aSortedInd-1));
				}
				aSortedGapList2.remove(aSortedInd);
			}

		}

		// first 670 gap sum: 596335 (up to 2000 gap)
		int aSum = 0;
		// from 20k -  2342059
		int[] aR = new int[]{ 20116, 20208, 20236, 20530, 20553, 20945, 20963, 21020, 21024, 21098, 21206, 21478, 22313, 22384, 22493, 22510, 22746, 22847, 23113, 23208, 23287, 23329, 23553, 23561, 23978, 24195, 24351, 24408, 24500, 24714, 24730, 24945, 25152, 25314, 25352, 25655, 25993, 26082, 26188, 26859, 27363, 28377, 28532, 28670, 29500, 29898, 30433, 30486, 30986, 31048, 31316, 31868, 31891, 32356, 32361, 32782, 32819, 33491, 33979, 34186, 35057, 35259, 36689, 37002, 37108, 37830, 39070, 39976, 40045, 40050, 40725, 41734, 42160, 43067, 43213, 44740, 45416, 47411, 66028};
		for(int aN : aR)
		{
			aSum += aN;
		}
		System.out.println("\n first " + aR.length + " gap sum: " + aSum);


		System.out.print("\n\ngames ind in gap: \n\t");
		Utils.printf(aGapList, 5, ", ", " ");

		Collections.sort(aGapList);
		System.out.print("\ngames ind in gap sorted: \n\t");
		Utils.printf(aGapList, 5, ", ", " ");
		System.out.print("\ndelta for games ind in gap: \n\t");
		Utils.printf(aGapList2, 5, ", ", " ");

		Collections.sort(aGapList2);
		System.out.println("\ndelta for games ind in gap sorted: \n\t" + aGapList2);


		List<Integer> aD = Utils.getGameHDelta(aGapList);
		System.out.println("games ind in gap sorted delta: \n\t" + aD);
		Collections.sort(aD);
		System.out.println("games ind in gap sorted delta sorted: \n\t" + aD);


	}

	private void printNextStat(List<Integer> theNumList)
	{
		System.out.println();
		for (int i =0 ;i < 10; i++)
		{
			List<Integer> anIndList = new ArrayList<>();
			int aLastInd = 0;
			for (int j = 0; j < theNumList.size(); j++)
			{
				if (theNumList.get(j) == i)
				{
					if (aLastInd !=0 )
					{
						anIndList.add(j - aLastInd);
					}
					aLastInd = j;
				}
			}
			System.out.println("\t\t" + i + ":\t" + anIndList);
			System.out.println("\t\t"  + "\t" + anIndList.toString().replace(", ", ""));
		}
	}

	double getSumMinMax(List<Integer> theGame)
	{
		List<Integer> aGame = theGame;
		List<Integer> aDeltaMap = Utils.getGameHDelta(aGame);
		int aMinGap = Collections.min(aDeltaMap);
		double aMaxGap = aGame.get(4) - aGame.get(0);
		return  sum(aGame) / (aMaxGap-aMinGap);
	}

	private void testMinMaxSum()
	{

		List<Double> aSumMinMax = new ArrayList<>();
		for (Map.Entry<Date, List<Integer>> aEntry : myGamesMap.entrySet())
		{
			aSumMinMax.add(getSumMinMax(aEntry.getValue().subList(0, 5)));
		}

		List<Double> aMMSumMinMax = new ArrayList<>();
		for (Map.Entry<Date, List<Integer>> aEntry : myMMGamesMap.entrySet())
		{
			aMMSumMinMax.add(getSumMinMax(aEntry.getValue().subList(0, 5)));
		}

		List<Integer> aGap = Utils.getGapBetweenSameNumbers(aSumMinMax);
		printSumMinMax(aSumMinMax, aGap);

		System.out.println("= ==== = MM  = = = \n");

		aGap = Utils.getGapBetweenSameNumbers(aSumMinMax, aMMSumMinMax);
		printSumMinMax(aMMSumMinMax, aGap);


	}

	public static void printSumMinMax(List<Double> theSumMinMax, List<Integer> theGap)
	{
		System.out.println("a min/max gap : \n\t" + theGap);
		for(;theGap.remove(new Integer(-1)););
		Collections.sort(theGap);
		System.out.println("a min/max gap sorted: size=" + theGap.size() + "\n\t" + theGap);

		List<Double> aDeltaList = Utils.getGameDelta(theSumMinMax);
		System.out.println("min/max Delta : \n\t " + aDeltaList);
		System.out.println("MIN-MAX Delta : \n\t" + Collections.min(aDeltaList) + " - " + Collections.max(aDeltaList));
		List<Double> aSortedDeltaList = new ArrayList<>(aDeltaList);
		Collections.sort(aSortedDeltaList);
		System.out.println("min/max Delta sorted: \n\t" + aSortedDeltaList);
		theGap = Utils.getGapBetweenSameNumbers(aDeltaList);
		System.out.println("min/max Delta gap : \n\t" + theGap);
		Collections.sort(theGap);
		System.out.println("a min/max Delta gap sorted:\n\t" + theGap);
	}

	public static <T> void printGapStat(Collection<T> theCollection)
	{
		System.out.println("collection: \n\t" + theCollection);
		List<Integer> aGap = Utils.getGapBetweenSameNumbers(theCollection);
		System.out.println("gap: \n\t" + aGap);
		List<Integer> aGapDelta = Utils.getGameHDelta(aGap);
		for(;aGap.remove(new Integer(-1));)
		Collections.sort(aGap);
		System.out.println("sorted gap (" + aGap.size() + "): \n\t" + aGap);

		//List<Integer> aGapDelta = Utils.getGameHDelta(aGap);
		System.out.println("gap delta: \n\t" + aGapDelta);
		Collections.sort(aGapDelta);
		System.out.println("sorted gap delta: \n\t" + aGapDelta);

	}

	public void firstLastTest()
	{
		List<Integer> aDelta = new ArrayList<>();
		List<Integer> aSum = new ArrayList<>();
		List<Double> aDiv = new ArrayList<>();

		for (List<Integer> aGame : myGamesMap.values())
		{
			aDelta.add(aGame.get(4) - aGame.get(0));
			aSum.add(aGame.get(4) + aGame.get(0));
			aDiv.add((double)aGame.get(4) / aGame.get(0));
		}

		System.out.println("\n = sum = ");
		printGapStat(aSum);
		System.out.println("\n = delta = ");
		printGapStat(aDelta);
		System.out.println("\n = div = ");
		printGapStat(aDiv);

		List<Integer> aMidCol = Utils.getColumn(myGamesMap, 2);
		System.out.println("\n = mid col = ");
		printGapStat(aMidCol);

	}

	public void test()
	{
		//test4();
		//testMinMaxSum();
		firstLastTest();
	}

	public static void main(String args[])
	{
		//Map<Date, List<Integer>> myAllTheFun = new FileReader().loadFromFile("/Users/jet/Documents/pb/pb.txt");
		Map<Date, List<Integer>> myAllTheFun = new FileReader().loadFromFile("/Users/jet/work/numbers/pb-new.txt");
//		Map<Date, List<Integer>> myMMTheFun = new FileReader().loadFromFile("/Users/jet/Documents/pb/mm.txt");
//		myMMTheFun = Utils.replaceDates(new ArrayList<Date>(myAllTheFun.keySet()), myMMTheFun);

		NormalizeRule aNormalizeRule = new NormalizeRule(myAllTheFun, null);
		//aNormalizeRule.testPrevGames();
		//aNormalizeRule.testNomal();
		//aNormalizeRule.test();
		aNormalizeRule.test5();
		//aNormalizeRule.testFraction();
	}

}