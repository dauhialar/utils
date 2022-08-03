package com.cashflow.rules.vertical;

import com.cashflow.rules.AbstractRule;
import com.cashflow.rules.derivative.DeltaRule;
import pb.Chunk;
import pb.Meters;
import pb.Utils;
import pb.file.FileReader;
import pb.statistics.ChunkStConfig;
import pb.statistics.ChunkStatistics;

import java.util.*;

/**

 kGap = 20
 time: 371 sec.     [TOTAL] excl: 1593223, INCL: 9645290

 kGap = 40
 time: 281 sec.     [TOTAL] excl: 2341168, INCL: 8897345

 kGap = 60
 time: 276 sec.     [TOTAL] excl: 3105496, INCL: 8133017


 total: 11238513       excl:1463738   INCL: 9774775
 		== new chunk method count : 16282

 +++ 1st derivative 3 and 4 chunk check:
		 == new chunk method count : 25810

 +++ 1st derivative 3 and 4 chunk check + 3rd not in order check:
 time: 308 sec.
		 total: 11238513       excl:1628303   INCL: 9610210
 		== new chunk method count : 387486

 +++ 1st derivative 2, 3 and 4 chunk check
 time: 312 sec.
		 total: 11238513       excl:2350698   INCL: 8887815
		 == new chunk method count : 886960

on flight:
 ...
	 total: 81436       excl:17861   INCL: 63575
	 == new chunk method count : 7437
 ...


 * @author Ruslan Dauhiala
 */
public class OneNumberPerGameAgoRule extends AbstractRule
{

	private Date myToday ;
	private Map<Date, List<Integer>> myNumInGamesMap;
	private Set<Integer> myDelta4Set;

	private Set<List<Integer>> myDelta1Set;

	private Set<List<Integer>> my1stDelta2NumInOrderSet;
	private Set<List<Integer>> myDelta1Chunk3InOrderSet;
	private Set<List<Integer>> my1stDelta2NumNotInOrderSet;
	private Set<List<Integer>> myDelta1Chunk3NotInOrderSet;
	private Set<List<Integer>> myDelta1Chunk4NotInOrderSet;

	private Set<List<Integer>> myDelta2Chunk3NotInOrderSet;
	private Set<List<Integer>> myDelta2Chunk2NumNotInOrderSet;

	private Set<List<Integer>> myDelta3Chunk2Set;

	private static int kGap = 20; // [15, 17, 21, 23, 26, 26, 31, 37,

	public OneNumberPerGameAgoRule(Map<Date, List<Integer>> theGamesMap)
	{
		super(theGamesMap);
	}

	@Override
	public void setup()
	{
		myNumInGamesMap = getNumInGamesMap(myDates.size()-5);

		Map<Date, List<Integer>> aDeltaMap = DeltaRule.getDeltaMap(myNumInGamesMap);
		Map<Date, List<Integer>> aDelta2Map = DeltaRule.getDeltaMap(aDeltaMap);
		Map<Date, List<Integer>> aDelta3Map = DeltaRule.getDeltaMap(aDelta2Map);
		Map<Date, List<Integer>> aDelta4Map = DeltaRule.getDeltaMap(aDelta3Map);

		myDelta1Set = new HashSet<>(aDeltaMap.values());

		myDelta1Chunk4NotInOrderSet = Utils.getChunkSet(aDeltaMap, new ChunkStConfig(4, false, 1000));    // 82, 1997, 1997, 1997, 1997, 1997
		myDelta1Chunk3NotInOrderSet = Utils.getChunkSet(aDeltaMap, new ChunkStConfig(3, false, 5));     // [1, 7, 11, 37, 38, 79, 82,
		//my1stDelta2NumNotInOrderSet = Utils.getChunkSet(aDeltaMap, new ChunkStConfig(2, false, 4)); //[2]=886960  [3]=1155919 [4]=1193606

		myDelta1Chunk3InOrderSet = Utils.getChunkSet(aDeltaMap, new ChunkStConfig(3, true, 35)); 	//  [1, 38, 79, 82, 162,
		//my1stDelta2NumInOrderSet = Utils.getChunkSet(aDeltaMap, new ChunkStConfig(2, true, 2));

		myDelta2Chunk3NotInOrderSet = Utils.getChunkSet(aDelta2Map, new ChunkStConfig(3, false, 500)); 	//  [82, 774, 996, 1997, 1997, 1997, 1997
		myDelta2Chunk2NumNotInOrderSet = Utils.getChunkSet(aDelta2Map, new ChunkStConfig(2, false, 3)); 		//  [1, 4, 6, 8, 10, 12, 13, 13,

		//pilot
		myDelta2Chunk2NumNotInOrderSet.addAll(Utils.getAgoChunks(aDeltaMap, 10, new ChunkStConfig(2, false),myDates));
		myDelta2Chunk2NumNotInOrderSet.addAll(Utils.getAgoChunks(aDelta2Map, 10, new ChunkStConfig(2, false),myDates));

		myDelta3Chunk2Set = Utils.getChunkSet(aDelta3Map, new ChunkStConfig(2, false, 100)); 		//  [82, 112, 114, 276, 310, 774, 804, 996, 1116, 1376, 1676, 1997

		myToday = new Date(System.currentTimeMillis());
		myDates.add(0, myToday);
		//Map<Date, List<Integer>> a4theDeltaMap = get4htDelta(myNumInGamesMap);
		myDelta4Set = Utils.getIntSet(aDelta4Map);
		myDelta4Set.remove(new Integer(-1));
		List<Integer> aList = new ArrayList(myDelta4Set);
		for (int i = 0; i <
				  //kGap        //20   1.5M 0err
				    20
				  //40            // 40  2.2M 1err 1st ind
				  ; i ++)
		{
			myDelta4Set.remove(aList.get(i));
		}

		myDelta4Set.removeAll(Utils.getAgoInt(aDelta4Map, 20, myDates));
	}

	private Map<Date, List<Integer>> getNumInGamesMap(int theSize)
	{
		Map<Date, List<Integer>> aNumInGamesMap = new LinkedHashMap<>();
		int aNeg = -1;
		for (int i = 0; i < theSize; i++)
		{
			List<Integer> aGameIndexes = new ArrayList<>();
			Date aGameDate = myDates.get(i);
			List<Integer> aGame = new ArrayList<>(myGamesMap.get(aGameDate)).subList(0,5);
			Set<Integer> aPlayedNumSet = new HashSet<>();
			for (int j = i+1; j < myDates.size() && aGame.size() != 0 && aGameIndexes.size() < 5; j++)
			{
				List<Integer> aNumInGame = myGamesMap.get(myDates.get(j)).subList(0,5);
				List<Integer> anIntersection = Utils.getIntersection(aGame, aNumInGame);
				if (anIntersection.size() == 1 && Utils.getIntersection(aPlayedNumSet, aNumInGame).size() == 0)
				{
					aPlayedNumSet.addAll(aNumInGame);
					aGame.removeAll(anIntersection);
					aGameIndexes.add(j-i);
				}
			}

			while (aGameIndexes.size() < 5)
			{
				aGameIndexes.add(0,
						  //aNeg
						  aNeg--
				);
			}
//			Collections.sort(aGameIndexes);
			aNumInGamesMap.put(aGameDate, aGameIndexes);
		}
		return aNumInGamesMap;
	}

	int myCount = 0;

	@Override
	public boolean accept(List<Integer> thePotentialGame)
	{
		myGamesMap.put(myToday, thePotentialGame);
		Map<Date, List<Integer>> aPotentialMap = getNumInGamesMap(1);

		List<Integer> aPotDelta = Utils.getGameHDelta(aPotentialMap.get(myToday));
		List<Integer> aPotDelta2 = Utils.getGameHDelta(aPotDelta);
		List<Integer> aPotDelta3 = Utils.getGameHDelta(aPotDelta2);
		int a4thDeltaNum = Utils.getGameHDelta(aPotDelta3).get(0);

		boolean aFound = !myDelta4Set.contains(a4thDeltaNum);
		aFound = aFound || myDelta1Set.contains(aPotDelta);
		aFound = aFound || gameChunkCheck(Utils.getAllChunks(aPotDelta, 3), myDelta1Chunk3NotInOrderSet);
		aFound = aFound || gameChunkCheck(Utils.getArrayChunks(aPotDelta, 3), myDelta1Chunk3InOrderSet);

		aFound = aFound || gameChunkCheck(Utils.getAllChunks(aPotDelta, 4), myDelta1Chunk4NotInOrderSet);

		aFound = aFound || gameChunkCheck(Utils.getAllChunks(aPotDelta2, 3), myDelta2Chunk3NotInOrderSet);
		aFound = aFound || gameChunkCheck(Utils.getAllChunks(aPotDelta2, 2), myDelta2Chunk2NumNotInOrderSet);

		aFound = aFound || gameChunkCheck(Utils.getAllChunks(aPotDelta3, 2), myDelta3Chunk2Set);


		trackResult(!aFound);
		return !aFound;
	}

//	@Override
//	public void printReport()
//	{
//		super.printReport();
//		System.out.println(" == new chunk method count : " + myCount);
//	}

	public static void main(String args[])
	{
		Map<Date, List<Integer>> myAllTheFun = new FileReader().loadFromFile("/Users/jet/Documents/pb/pb.txt");

		new OneNumberPerGameAgoRule(myAllTheFun).test();

	}


	private void test()
	{
		myDates.remove(myToday);

		//DeltaRule.getFullDerivativeStat(myNumInGamesMap);

		//System.out.println("gap between game indexes numbers: ");
		//System.out.println(Utils.getGapBetweenSameNumbers(myNumInGamesMap.values()));


		System.out.println("indexes of games that contains only one current game number: ");
		Map<Date, List<Integer>> aDelta1Map = DeltaRule.getDeltaMap(myNumInGamesMap);
		int anInd = 0;
		for (Map.Entry<Date, List<Integer>> aEntry : myNumInGamesMap.entrySet())
		{
			String aStr = aEntry.getValue().toString();
			System.out.printf("%1$4d" + "\t\t" + Meters.kOutDateFormat.format(aEntry.getKey()) + "\t", ++anInd);
			Utils.printf(myGamesMap.get(aEntry.getKey()).subList(0,5), 2, "_", "\t | \t");
			Utils.printf(aEntry.getValue(), 4, ".", "\t | \t");
			Utils.printf(aDelta1Map.get(aEntry.getKey()), 4, ".", "\n");

//			System.out.println(++anInd +
//					  "\t\t" + Meters.kOutDateFormat.format(aEntry.getKey()) +
//					  " \t " + aStr.substring(1, aStr.length() - 1) +
//					  "\t\t\t " + aDeltaMap.get(aEntry.getKey()).toString().replace(",", "'"));
		}

		Map<Date, List<Integer>> aMapToCheck =
				  aDelta1Map;
				  //myNumInGamesMap;

		System.out.println(" === 1st' DELTA === ");
		ChunkStatistics.getAllChunkStats(aDelta1Map);

		Map<Date, List<Integer>> aDelta2Map = DeltaRule.getDeltaMap(aDelta1Map);
		System.out.println(" === 2st' DELTA === ");
		ChunkStatistics.getAllChunkStats(aDelta2Map);

		Map<Date, List<Integer>> aDelta3Map = DeltaRule.getDeltaMap(aDelta2Map);
		System.out.println(" === 3st' DELTA === ");
		ChunkStatistics.getAllChunkStats(aDelta3Map);

		Map<Date, List<Integer>> aDelta4Map = DeltaRule.getDeltaMap(aDelta3Map);
		System.out.println(" === 4th' DELTA === ");
		ChunkStatistics.getAllChunkStats(aDelta4Map);


		System.out.println("\n\ngap between 1st derivative of game indexes numbers: ");
		System.out.println(Utils.getGapBetweenSameNumbers(aDelta1Map.values()));


		List<Integer> aGapList = Utils.getIntList(aDelta4Map);
//		for (int i = 0; i < aDelta.size(); i++)
//		{
//			aGapList.add(aDelta.get(myDates.get(i)).get(0));
//		}
		System.out.println("4th delta : ");
		System.out.println(aGapList);

		System.out.println("4th delta gap: ");
		List<Integer > aGap = Utils.getGapBetweenSameNumbers(aGapList);
		System.out.println("size: " + aGap.size() + "\n" + aGap);

		for (int i = aGap.size()-1; i >= 0; i--)
		{
			if (aGap.get(i) == -1)
			{
				aGap.remove(i);
			}
		}
		System.out.println("4th delta gap w/o -1 value (size :" + aGap.size() + "): ");
		//aGap.remove(new Integer(-1));
		System.out.println(aGap);

		Collections.sort(aGap);
		System.out.println("4th delta gap w/o -1 value SORTED: ");
		//aGap.remove(new Integer(-1));
		System.out.println(aGap);
		int aPart = 10;
		System.out.println(aPart + "% element:" + aGap.get(aGap.size()/aPart));

		Set<Integer> aSet = new LinkedHashSet<>(aGap);
		aGap = new ArrayList<>(aSet);
		System.out.println( "\nSET (size :" + aGap.size() + "): ");
		System.out.println( aGap);
		System.out.println(aPart + "% element:" + aGap.get(aGap.size()/aPart));
	}


	private Map<Date, List<Integer>> get4htDelta(Map<Date, List<Integer>> theNumInGamesMap)
	{
		Map<Date, List<Integer>> aDelta = DeltaRule.getDeltaMap(theNumInGamesMap);
		aDelta = DeltaRule.getDeltaMap(aDelta);
		aDelta = DeltaRule.getDeltaMap(aDelta);
		aDelta = DeltaRule.getDeltaMap(aDelta);
		return aDelta;
	}
}

/*



======
★ ★ ★  0 	 01/04/17.Wed = [16, 17, 29, 41, 42] =
= OneNumberPerGameAgoRule =
    total: 11238513       excl:2740477   INCL: 8498036
 == new chunk method count : 0
 time: 235 sec.     [TOTAL] excl: 2740477, INCL: 8498036

===============



★ ★ ★  1 	 12/31/16.Sat = [1, 3, 28, 57, 67] =
= OneNumberPerGameAgoRule =
    total: 11238513       excl:2579182   INCL: 8659331
 == new chunk method count : 0
 time: 221 sec.     [TOTAL] excl: 2579182, INCL: 8659331

===============



★ ★ ★  2 	 12/28/16.Wed = [16, 23, 30, 44, 58] =
= OneNumberPerGameAgoRule =
    total: 11238513       excl:2737238   INCL: 8501275
 == new chunk method count : 0
 time: 217 sec.     [TOTAL] excl: 2737238, INCL: 8501275

===============



★ ★ ★  3 	 12/24/16.Sat = [28, 38, 42, 51, 52] =
= OneNumberPerGameAgoRule =
    total: 11238513       excl:2829886   INCL: 8408627
 == new chunk method count : 0
 time: 214 sec.     [TOTAL] excl: 2829886, INCL: 8408627

===============



★ ★ ★  4 	 12/21/16.Wed = [25, 33, 40, 54, 68] =
= OneNumberPerGameAgoRule =
    total: 11238513       excl:2718077   INCL: 8520436
 == new chunk method count : 0
 time: 221 sec.     [TOTAL] excl: 2718077, INCL: 8520436

===============



★ ★ ★  5 	 12/17/16.Sat = [1, 8, 16, 40, 48] =
= OneNumberPerGameAgoRule =
    total: 11238513       excl:2790143   INCL: 8448370
 == new chunk method count : 0
 time: 232 sec.     [TOTAL] excl: 2790143, INCL: 8448370

===============



★ ★ ★  6 	 12/14/16.Wed = [18, 26, 37, 39, 66] =
= OneNumberPerGameAgoRule =
    total: 11238513       excl:3015884   INCL: 8222629
 == new chunk method count : 0
 time: 237 sec.     [TOTAL] excl: 3015884, INCL: 8222629

===============



★ ★ ★  7 	 12/10/16.Sat = [12, 21, 32, 44, 66] =
= OneNumberPerGameAgoRule =
    total: 11238513       excl:2908757   INCL: 8329756
 == new chunk method count : 0
 time: 232 sec.     [TOTAL] excl: 2908757, INCL: 8329756

===============



★ ★ ★  8 	 12/07/16.Wed = [41, 48, 49, 53, 64] =
= OneNumberPerGameAgoRule =
    total: 11238513       excl:2672131   INCL: 8566382
 == new chunk method count : 0
 time: 227 sec.     [TOTAL] excl: 2672131, INCL: 8566382

===============



★ ★ ★  9 	 12/03/16.Sat = [8, 10, 26, 27, 33] =
= OneNumberPerGameAgoRule =
    total: 11238513       excl:2481093   INCL: 8757420
 == new chunk method count : 0
 time: 223 sec.     [TOTAL] excl: 2481093, INCL: 8757420

===============



★ ★ ★  10 	 11/30/16.Wed = [3, 14, 18, 25, 45] =
= OneNumberPerGameAgoRule =
    total: 11238513       excl:2634201   INCL: 8604312
 == new chunk method count : 0
 time: 218 sec.     [TOTAL] excl: 2634201, INCL: 8604312

===============



★ ★ ★  11 	 11/26/16.Sat = [17, 19, 21, 37, 44] =
= OneNumberPerGameAgoRule =
    total: 11238513       excl:2713255   INCL: 8525258
 == new chunk method count : 0
 time: 220 sec.     [TOTAL] excl: 2713255, INCL: 8525258

===============



★ ★ ★  12 	 11/23/16.Wed = [7, 32, 41, 47, 61] =
= OneNumberPerGameAgoRule =
    total: 11238513       excl:2678701   INCL: 8559812
 == new chunk method count : 0
 time: 217 sec.     [TOTAL] excl: 2678701, INCL: 8559812

===============



★ ★ ★  13 	 11/19/16.Sat = [16, 24, 28, 43, 61] =
= OneNumberPerGameAgoRule =
    total: 11238513       excl:2609539   INCL: 8628974
 == new chunk method count : 0
 time: 205 sec.     [TOTAL] excl: 2609539, INCL: 8628974

===============



★ ★ ★  14 	 11/16/16.Wed = [28, 41, 61, 63, 65] =
= OneNumberPerGameAgoRule =
    total: 11238513       excl:2396269   INCL: 8842244
 == new chunk method count : 0
 time: 217 sec.     [TOTAL] excl: 2396269, INCL: 8842244

===============



★ ★ ★  15 	 11/12/16.Sat = [8, 17, 20, 27, 52] =
= OneNumberPerGameAgoRule =
    total: 11238513       excl:2709490   INCL: 8529023
 == new chunk method count : 0
 time: 205 sec.     [TOTAL] excl: 2709490, INCL: 8529023

===============



★ ★ ★  16 	 11/09/16.Wed = [1, 25, 28, 31, 54] =
= OneNumberPerGameAgoRule =
    total: 11238513       excl:2604179   INCL: 8634334
 == new chunk method count : 0
 time: 200 sec.     [TOTAL] excl: 2604179, INCL: 8634334

===============



★ ★ ★  17 	 11/05/16.Sat = [21, 31, 50, 51, 69] =
= OneNumberPerGameAgoRule =
    total: 11238513       excl:2570421   INCL: 8668092
 == new chunk method count : 0
 time: 202 sec.     [TOTAL] excl: 2570421, INCL: 8668092

===============



★ ★ ★  18 	 11/02/16.Wed = [13, 18, 37, 54, 61] =
= OneNumberPerGameAgoRule =
    total: 11238513       excl:2931228   INCL: 8307285
 == new chunk method count : 0
 time: 210 sec.     [TOTAL] excl: 2931228, INCL: 8307285

===============



★ ★ ★  19 	 10/29/16.Sat = [19, 20, 21, 42, 48] =
= OneNumberPerGameAgoRule =
    total: 11238513       excl:2732667   INCL: 8505846
 == new chunk method count : 0
 time: 212 sec.     [TOTAL] excl: 2732667, INCL: 8505846

===============




TOTAL time: 4382 sec.




*
* */