package com.cashflow.rules.chunkcomb;

import com.cashflow.rules.AbstractRule;
import pb.Chunk;
import pb.Utils;
import pb.file.FileReader;
import pb.statistics.ChunkStConfig;
import pb.statistics.ChunkStatistics;

import java.util.*;

/**

 * @author Ruslan Dauhiala
 */
public class ChunkCombinationRule extends AbstractRule
{
	Map<Date,List<Integer>> myGame5NumMap;
	Set<List<Integer>> myGamesSet;
	Set<List<Integer>> myChunk4NotOrderedSet ;
	Set<List<Integer>> myChunk4OrderedSet ;
	Set<List<Integer>> myChunk3NotOrderedSet ;
	Set<List<Integer>> myChunk3OrderedSet ;
	Set<List<Integer>> myChunk2NotOrderedSet ;
	Set<List<Integer>> myChunk2OrderedSet ;

	List<List<Integer>> myNotInOrderChunks;
	List<List<Integer>> myInOrderChunks;

	public 	ChunkCombinationRule(Map<Date, List<Integer>> theGamesMap)
	{
		super(theGamesMap);
	}

	@Override
	public void setup()
	{

//		myNotInOrderChunks = Utils.getAllTheChunksTotal(-1, myGamesMap, Utils.kNotInOrderListConfig);
//		myInOrderChunks = Utils.getAllTheChunksTotal(-1, myGamesMap, Utils.kInOrderListConfig);
		myGame5NumMap = Utils.get5SizeMap(myGamesMap);
		myGamesSet = new HashSet<>(myGame5NumMap.values());
		                                                                                       //   s.600		s.100
		myChunk4NotOrderedSet = getChunkSet(myGame5NumMap, new ChunkStConfig(4, false, 500));  // 	337, 442, 442, 558, 1547, 1723, 1789, 2002, 2002, 2002, 2002,
		myChunk3NotOrderedSet = getChunkSet(myGame5NumMap, new ChunkStConfig(3, false, 14));	// 	14, 18, 19,
		// pilot
		List<Integer> a3ChunkList = ChunkStatistics.getChunkAgoList(new ChunkStConfig(3, false), myGame5NumMap, null);
		for (int i = 0 ; i < 20 &&         // 5 +.1M 0err;  10 + 70k 0err;
				  -1 < a3ChunkList.get(i) &&
				  a3ChunkList.get(i) < myDates.size();
			  i++) //
		{
			List<Integer> aGame = myGame5NumMap.get(myDates.get(a3ChunkList.get(i)));
			myChunk3NotOrderedSet.addAll(Utils.getAllChunks(aGame, 3));
		}

		//myChunk2NotOrderedSet = getChunkSet(myGame5NumMap, new ChunkStConfig(2, false, 1));    // 	1 ?			2

		//myChunk4OrderedSet = getChunkSet(myGame5NumMap, new ChunkStConfig(4, true, 500)); 		// 442, 558, 1723, 1789, 2002, 2002, 2002,
		myChunk3OrderedSet = getChunkSet(myGame5NumMap, new ChunkStConfig(3, true, 100));			// [45, 99, 101, 113, 127, 132, 145
		myChunk2OrderedSet = getChunkSet(myGame5NumMap, new ChunkStConfig(2, true, 2));			// 1           2

		// pilot
		//myChunk2NotOrderedSet = new HashSet<>();
		List<Integer> a2ChunkListO = ChunkStatistics.getChunkAgoList(new ChunkStConfig(2, true), myGame5NumMap, null);
		for (int i = 0 ; i < 10; i++) // 5 +.2M 0err; 		10 +.6M 0err; 		15 +.4M 3err [1,2,16]
		{
			List<Integer> aGame = myGame5NumMap.get(myDates.get(a2ChunkListO.get(i)));
			myChunk2OrderedSet.addAll(Utils.getArrayChunks(aGame, 2));
		}

		// pilot
		myChunk2NotOrderedSet = new HashSet<>();
		List<Integer> a2ChunkList = ChunkStatistics.getChunkAgoList(new ChunkStConfig(2, false), myGame5NumMap, null);
		for (int i = 0 ; i < 3; i++)   // 3= 1.8M 1err ind12;  5 =2.6M 2err;  10= 4M 4(3)err
		{
			List<Integer> aGame = myGame5NumMap.get(myDates.get(a2ChunkList.get(i)));
			myChunk2NotOrderedSet.addAll(Utils.getAllChunks(aGame,2));
		}
	}

	@Override
	public boolean accept(List<Integer> thePotentialGame)
	{
		boolean aFound = false;

		aFound = aFound || gameChunkCheck(Utils.getArrayChunks(thePotentialGame, 2), myChunk2OrderedSet);
		aFound = aFound || gameChunkCheck(Utils.getAllChunks(thePotentialGame, 2), myChunk2NotOrderedSet);

		aFound = aFound || gameChunkCheck(Utils.getArrayChunks(thePotentialGame, 3), myChunk3OrderedSet);
		aFound = aFound || gameChunkCheck(Utils.getAllChunks(thePotentialGame, 3), myChunk3NotOrderedSet);

		//aFound = aFound || gameChunkCheck(Utils.getArrayChunks(thePotentialGame, 4), myChunk4OrderedSet);
		aFound = aFound || gameChunkCheck(Utils.getAllChunks(thePotentialGame, 4), myChunk4NotOrderedSet);

		aFound = aFound || myGamesSet.contains(thePotentialGame);

		trackResult(!aFound);
		return !aFound;
	}

	public static void main(String[] args)
	{
		Map<Date, List<Integer>> myAllTheFun = new FileReader().loadFromFile("/Users/jet/Documents/pb/pb.txt");

		//new HorizonToVerticalDerivativeRule(myAllTheFun).test();
		new ChunkCombinationRule(myAllTheFun).trueTest();

	}

	private void trueTest()
	{
		System.out.println("===  Game stat ===");
		ChunkStatistics.getAllChunkStats(myGame5NumMap);
		System.out.println("========================\n\n");


		List<Integer> a2ChunkList = ChunkStatistics.getStatistics(new ChunkStConfig(2, false), myGame5NumMap, null);
		System.out.println("2 chunk stat:\n" + a2ChunkList);
		List<Integer> aGap = Utils.getGapBetweenSameNumbers(a2ChunkList);
		System.out.println("2 chunk gap: [" + aGap.size() + "]\n" + aGap);
		Collections.sort(aGap);
		System.out.println("2 chunk gap ordered:\n" + aGap);

	}
}

/*


★ ★ ★  0 	 01/04/17.Wed = [16, 17, 29, 41, 42] =
= ChunkCombinationRule =
    total: 11238513       excl:3024530   INCL: 8213983
 time: 75 sec.     [TOTAL] excl: 3024530, INCL: 8213983

===============



★ ★ ★  1 	 12/31/16.Sat = [1, 3, 28, 57, 67] =
= ChunkCombinationRule =
    total: 11238513       excl:2882837   INCL: 8355676
 time: 76 sec.     [TOTAL] excl: 2882837, INCL: 8355676

===============



★ ★ ★  2 	 12/28/16.Wed = [16, 23, 30, 44, 58] =
= ChunkCombinationRule =
    total: 11238513       excl:2967581   INCL: 8270932
 time: 76 sec.     [TOTAL] excl: 2967581, INCL: 8270932

===============



★ ★ ★  3 	 12/24/16.Sat = [28, 38, 42, 51, 52] =
= ChunkCombinationRule =
    total: 11238513       excl:2581288   INCL: 8657225
 time: 75 sec.     [TOTAL] excl: 2581288, INCL: 8657225

===============



★ ★ ★  4 	 12/21/16.Wed = [25, 33, 40, 54, 68] =
= ChunkCombinationRule =
    total: 11238513       excl:2971724   INCL: 8266789
 time: 73 sec.     [TOTAL] excl: 2971724, INCL: 8266789

===============



★ ★ ★  5 	 12/17/16.Sat = [1, 8, 16, 40, 48] =
= ChunkCombinationRule =
    total: 11238513       excl:2918896   INCL: 8319617
 time: 72 sec.     [TOTAL] excl: 2918896, INCL: 8319617

===============



★ ★ ★  6 	 12/14/16.Wed = [18, 26, 37, 39, 66] =
= ChunkCombinationRule =
    total: 11238513       excl:2718508   INCL: 8520005
 time: 70 sec.     [TOTAL] excl: 2718508, INCL: 8520005

===============



★ ★ ★  7 	 12/10/16.Sat = [12, 21, 32, 44, 66] =
= ChunkCombinationRule =
    total: 11238513       excl:2810892   INCL: 8427621
 time: 71 sec.     [TOTAL] excl: 2810892, INCL: 8427621

===============



★ ★ ★  8 	 12/07/16.Wed = [41, 48, 49, 53, 64] =
= ChunkCombinationRule =
    total: 11238513       excl:2720234   INCL: 8518279
 time: 70 sec.     [TOTAL] excl: 2720234, INCL: 8518279

===============



★ ★ ★  9 	 12/03/16.Sat = [8, 10, 26, 27, 33] =
= ChunkCombinationRule =
    total: 11238513       excl:2709433   INCL: 8529080
 time: 66 sec.     [TOTAL] excl: 2709433, INCL: 8529080

===============



★ ★ ★  10 	 11/30/16.Wed = [3, 14, 18, 25, 45] =
= ChunkCombinationRule =
    total: 11238513       excl:2809213   INCL: 8429300
 time: 66 sec.     [TOTAL] excl: 2809213, INCL: 8429300

===============



★ ★ ★  11 	 11/26/16.Sat = [17, 19, 21, 37, 44] =
= ChunkCombinationRule =
    total: 11238513       excl:2691965   INCL: 8546548
 time: 67 sec.     [TOTAL] excl: 2691965, INCL: 8546548

===============



★ ★ ★  12 	 11/23/16.Wed = [7, 32, 41, 47, 61] =
 >>> BOOOOO <<< : ChunkCombinationRule
= ChunkCombinationRule =
    total: 11238513       excl:2632395   INCL: 8606118
 time: 67 sec.     [TOTAL] excl: 2632395, INCL: 8606118

===============



★ ★ ★  13 	 11/19/16.Sat = [16, 24, 28, 43, 61] =
= ChunkCombinationRule =
    total: 11238513       excl:2730394   INCL: 8508119
 time: 66 sec.     [TOTAL] excl: 2730394, INCL: 8508119

===============



★ ★ ★  14 	 11/16/16.Wed = [28, 41, 61, 63, 65] =
= ChunkCombinationRule =
    total: 11238513       excl:2987424   INCL: 8251089
 time: 68 sec.     [TOTAL] excl: 2987424, INCL: 8251089

===============



★ ★ ★  15 	 11/12/16.Sat = [8, 17, 20, 27, 52] =
= ChunkCombinationRule =
    total: 11238513       excl:2780575   INCL: 8457938
 time: 73 sec.     [TOTAL] excl: 2780575, INCL: 8457938

===============



★ ★ ★  16 	 11/09/16.Wed = [1, 25, 28, 31, 54] =
= ChunkCombinationRule =
    total: 11238513       excl:2707304   INCL: 8531209
 time: 73 sec.     [TOTAL] excl: 2707304, INCL: 8531209

===============



★ ★ ★  17 	 11/05/16.Sat = [21, 31, 50, 51, 69] =
= ChunkCombinationRule =
    total: 11238513       excl:2696100   INCL: 8542413
 time: 73 sec.     [TOTAL] excl: 2696100, INCL: 8542413

===============



★ ★ ★  18 	 11/02/16.Wed = [13, 18, 37, 54, 61] =
= ChunkCombinationRule =
    total: 11238513       excl:2805877   INCL: 8432636
 time: 74 sec.     [TOTAL] excl: 2805877, INCL: 8432636

===============



★ ★ ★  19 	 10/29/16.Sat = [19, 20, 21, 42, 48] =
= ChunkCombinationRule =
    total: 11238513       excl:2890053   INCL: 8348460
 time: 74 sec.     [TOTAL] excl: 2890053, INCL: 8348460

===============




TOTAL time: 1442 sec.


 */