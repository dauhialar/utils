package com.cashflow.rules.derivative;

import com.cashflow.rules.AbstractRule;
import com.cashflow.rules.Accept;
import com.cashflow.rules.cofig.De4Config;
import com.cashflow.rules.cofig.IConfig;
import pb.Utils;
import pb.file.FileReader;
import pb.statistics.ChunkStConfig;
import pb.statistics.ChunkStatistics;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Updated DeltaRule
 * @author Ruslan Dauhiala
 */
public class DeltaRule2 extends AbstractRule
{
//	Set<Integer> my1stDerChunk1Set;
//
//
//
//	Set<List<Integer>> my2ndDerChunk3NotInOrderSet;
//	Set<List<Integer>> my2ndDerChunk2NotInOrderSet;
//
//	Set<List<Integer>> my2ndDerChunk3InOrderSet;
//	Set<List<Integer>> my2ndDerChunk2InOrderSet;
//
//	Set<List<Integer>> my3rdDerChunk2InOrderSet;
//
//	Set<Integer> my4ndDerChunk1ExcSet;
//	Set<Integer> my4ndDerChunk1IncSet;
//	Set<Double> myDelta2130Rate;
//	Set<Double> myDelta1032Rate;

	List<Accept> myRules;
	int myGameModMax;
	int myDelta1ModMax;
	int myDelta2ModMax;
	int myDelta3ModMax;


	public DeltaRule2(Map<Date, List<Integer>> theGamesMap)
	{
		super(theGamesMap);
	}

	@Override
	public IConfig getRuleConfig()
	{
		return new De4Config();
	}

	@Override
	public void setup()
	{
		Map<Date, List<Integer>> aDelta1Map = DeltaRule.getDeltaMap(myGamesMap);
		Map<Date, List<Integer>> aDelta2Map = DeltaRule.getDeltaMap(aDelta1Map);
		Map<Date, List<Integer>> aDelta3Map = DeltaRule.getDeltaMap(aDelta2Map);
		Map<Date, List<Integer>> aDelta4Map = DeltaRule.getDeltaMap(aDelta3Map);

		myRules = new ArrayList<>();
		myGameModMax = DeltaRule.getMaxMode(myGamesMap);
		myDelta1ModMax = DeltaRule.getMaxMode(aDelta1Map);
		myDelta2ModMax = DeltaRule.getMaxMode(aDelta2Map);
		myDelta3ModMax = DeltaRule.getMaxMode(aDelta3Map);

		myRules.add(new De4SubRule(aDelta4Map, myDates,
				  myGameModMax,
				  myDelta1ModMax,
				  myDelta2ModMax,
				  myDelta3ModMax));
//		myRules.add(new De4PerGame(aDelta4Map, myDates,
//				  myGameModMax,
//				  myDelta1ModMax,
//				  myDelta2ModMax,
//				  myDelta3ModMax));
		//myRules.add(new De1Rule(aDelta1Map, aDelta3Map));


//		List<Double> aDelta2130Rate = Utils.getDeltaPercentList(2,1,3,0, aDelta1Map.values());
//		myDelta2130Rate = Utils.getAgoFromGapNumbers(10, aDelta2130Rate);
//		// 10				ERRORS: 	 1; 		 Denied : 	 525227	;  Accepted : 10713286 		 time: 	 9; 	 total : 197 sec. #: [10]
//		//							denied%: 	 0.05    	;  errors % : 0.05
//		// 20:			ERRORS: 	 2; 		 Denied : 	 928546	;  Accepted : 10309967 		 time: 	 8; 	 total : 186 sec. #: [1, 10]
//		// 										denied%: 	 0.08    	;  errors % : 0.10
//
//		List<Double> aDelta3120Rate = Utils.getDeltaPercentList(1,0,3,2, aDelta1Map.values());
//		myDelta1032Rate = Utils.getAgoFromGapNumbers(20, aDelta3120Rate);

		// 20  ERRORS: 	 1; 		 Denied : 	 845577	;  Accepted : 10392936 		 time: 	 8; 	 total : 183 sec. #: [13]
		//									denied%: 	 0.08    	;  errors % : 0.05

//		my2ndDerChunk3NotInOrderSet = getChunkSet(aDelta2Map, new ChunkStConfig(3, false, 591));            // [26, 356, 371, 469, 503, 522, 579, 589, 590, 591, 690, 718, 726,
//		my2ndDerChunk2NotInOrderSet = getChunkSet(aDelta2Map, new ChunkStConfig(2, false, 3));              // [1, 1, 1, 2, 3, 3, 3, 3, 3, 3, 4, 5, 6, 6, 6, 6, 7,
//
//		//my2ndDerChunk3InOrderSet = getChunkSet(aDelta2Map, new ChunkStConfig(3, true, 350)); 		// [356, 503, 690, 718, 1442, 1649, 2002,
//		my2ndDerChunk2InOrderSet = getChunkSet(aDelta2Map, new ChunkStConfig(2, true, 3));        // [1, 2, 3, 3, 3, 3, 4, 6, 6, 6, 7, 7, 8, 9, 9, 9, 10
//		// =
//
		//my3rdDerChunk2InOrderSet = getChunkSet(aDelta3Map, new ChunkStConfig(2, false, 100)); 		//  [86, 105, 108, 159, 186,
	}

//	List<Integer> aTest = Arrays.asList(new Integer[]{6, 32, 47, 62 ,65});

	@Override
	public boolean accept(List<Integer> thePotentialGame)
	{
//		if (aTest.equals(thePotentialGame))
//		{
//			String here = "";
//			here +="2";
//		}

		//boolean aNotAccepted = false;//my4ndDerChunk1ExcSet.contains(aDer4);
		boolean aOk = true;//my4ndDerChunk1ExcSet.contains(aDer4);

		for (int i = 0; i < myRules.size() && aOk; i++)
		{
			aOk = myRules.get(i).accept(thePotentialGame);
		}
		trackResult(aOk);
		return aOk;

//		int aGap11 = aDer1.get(2) - aDer1.get(1);
//		int aGap22 = aDer1.get(3) - aDer1.get(0);
//		double aDouble2 = (double) aGap11 / aGap22;
//		//aNotAccepted = myDelta2130Rate.contains(aDouble2); //

//		aGap11 = aDer1.get(1) - aDer1.get(0);
//		aGap22 = aDer1.get(3) - aDer1.get(2);
//		aDouble2 = (double) aGap11 / aGap22;
//		aNotAccepted = aNotAccepted || myDelta1032Rate.contains(aDouble2); //



//
//		aNotAccepted = aNotAccepted || gameChunkCheck(Utils.getArrayChunks(aDer1, 3), my1stDerChunk3InOrderSet);
//		aNotAccepted = aNotAccepted || gameChunkCheck(Utils.getArrayChunks(aDer1, 2), my1stDerChunk2InOrderSet);
//
//		aNotAccepted = aNotAccepted || gameChunkCheck(Utils.getAllChunks(aDer2, 3), my2ndDerChunk3NotInOrderSet);
//		//aNotAccepted = aNotAccepted || gameChunkCheck(Utils.getArrayChunks(aDer2, 3), my2ndDerChunk3InOrderSet);
//		aNotAccepted = aNotAccepted || gameChunkCheck(Utils.getAllChunks(aDer2, 2), my2ndDerChunk2NotInOrderSet);
//		aNotAccepted = aNotAccepted || gameChunkCheck(Utils.getArrayChunks(aDer2, 2), my2ndDerChunk2InOrderSet);
//
////		//aFound = aFound || gameChunkCheck(Utils.getArrayChunks(aDer3, 2), my3rdDerChunk2InOrderSet);


//		aNotAccepted = Collections.disjoint(my1stDerChunk1Set, aDer1);

//		trackResult(!aNotAccepted);
//		return !aNotAccepted;
	}

	private void trueTest()
	{
		Map<Date, List<Integer>> aDelta1Map = DeltaRule.getDeltaMap(myGamesMap);
		Map<Date, List<Integer>> aDelta2Map = DeltaRule.getDeltaMap(aDelta1Map);
		Map<Date, List<Integer>> aDelta3Map = DeltaRule.getDeltaMap(aDelta2Map);
		Map<Date, List<Integer>> aDelta4Map = DeltaRule.getDeltaMap(aDelta3Map);

		System.out.println("===  H delta`1 stat ===");
		ChunkStatistics.getAllChunkStats(aDelta1Map);
		System.out.println("========================\n\n");

		System.out.println("===  H delta`2 stat ===");
		ChunkStatistics.getAllChunkStats(aDelta2Map);
		System.out.println("========================\n\n");

		System.out.println("===  H delta`3 stat ===");
		ChunkStatistics.getAllChunkStats(aDelta3Map);
		System.out.println("========================\n\n");

		System.out.println("===  H delta`4 stat ===");
		ChunkStatistics.getAllChunkStats(aDelta4Map);
		System.out.println("========================\n\n");

		System.out.print("\n4th delta:\n [");
		List<Integer> a4DeltainOrder = new ArrayList<>();
		for (List<Integer> aList : aDelta4Map.values())
		{
			a4DeltainOrder.add(aList.get(0));
			System.out.print(aList.get(0) + ", ");
		}

		printFrequencyRank(a4DeltainOrder);

		System.out.println("\nall numbers gap: ");
		Map<Integer, List<Integer>> aSameNumbersGapAll = Utils.getGapBetweenSameNumbersAll(a4DeltainOrder);
		for (Map.Entry aEntry : aSameNumbersGapAll.entrySet())
		{
			int aMayBe = a4DeltainOrder.indexOf(aEntry.getKey());
			System.out.printf(" %1$2d \t-%2$3d- \t\t" + aEntry.getValue().toString().replaceFirst("\\[", "") + "\n", aEntry.getKey(), aMayBe);
		}

		Collections.sort(a4DeltainOrder);
		System.out.print("\n4th delta: in order\n " + a4DeltainOrder);

		List<Integer> aGap = Utils.getGapBetweenSameNumbers(aDelta4Map.values());
		System.out.println("\n\naGap:\n " + aGap);
		List<Integer> aGap2 = Utils.getGapBetweenSameNumbers(aGap);

		Collections.sort(aGap);
		System.out.println("\naGap in order:\n " + aGap);


		System.out.println("\naGap2:\n " + aGap2);
		Collections.sort(aGap2);
		System.out.println("\naGap2: in order\n " + aGap2);
	}

	private void printFrequencyRank(List<Integer> theList)
	{
		Map<Integer,Integer> aFreqMap = new HashMap<>();
		theList.stream().forEach(x -> aFreqMap.put(x, aFreqMap.computeIfAbsent(x, s -> 0) + 1));
		System.out.print("\n\nFrequency rank: (0=the most frequent, 50=less frequent), " + aFreqMap.size() + " total \n ");

		Map<Integer,Integer> aSortedMap = sortMap(aFreqMap);
		List<Integer> aFrList = new ArrayList<>();
		for (int aNum : theList)
		{
			aSortedMap.put(aNum, aSortedMap.get(aNum) - 1);
			aSortedMap = sortMap(aSortedMap);
			int anInd = new ArrayList<>(aSortedMap.keySet()).indexOf(aNum);
			aFrList.add(anInd);
			System.out.print(anInd + ", ");
		}
		System.out.println("");

		int aSubListN = 10;
		List<Integer> aSubList = aFrList.subList(0, aSubListN);
		System.out.println("most frequent top: (out of " + aSubListN+ ")");
		for (int i = 5; i <= 50; i+=5)
		{
			final double k = i;
			long aCount = aSubList.stream().filter(x -> x > aFreqMap.size() * (1 - k/100)).count();
			System.out.println( i + "%: " + aCount);
		}

		System.out.println("less frequent:");
		for (int i = 5; i <= 50; i+=5)
		{
			final double k = i;
			long aCount = aSubList.stream().filter(x -> x < aFreqMap.size() * (k/100)).count();
			System.out.println(i + "%: " + aCount);
		}

		System.out.println("\n");
	}

	private Map<Integer, Integer> sortMap(Map<Integer, Integer> theFreqMap)
	{
		return theFreqMap
				  .entrySet()
				  .stream()
				  .sorted((e1, e2) -> {
								 int cmp1 = e2.getValue().compareTo(e1.getValue());
								 if (cmp1 != 0)
								 {
									 return cmp1;
								 } else
								 {
									 return e2.getKey().compareTo(e1.getKey());
								 }
							 }
							 //Map.Entry.comparingByValue()
				  )
				  .collect(Collectors.toMap(
										Map.Entry::getKey,
										Map.Entry::getValue,
										(e1, e2) -> e1,
										LinkedHashMap::new)
				  );
	}


	public static void main(String[] args) throws IOException
	{
		Map<Date, List<Integer>> myAllTheFun = new FileReader().loadFromFile("/Users/jet/Documents/pb/pb.txt");
		new DeltaRule2(myAllTheFun).trueTest();
	}

	class De1Rule implements Accept
	{
		Set<List<Integer>> my1stDerChunk4NotInOrderSet;
		Set<List<Integer>> my1stDerChunk3NotInOrderSet;
		Set<Integer> my1stDerChunk1NotInOrderSet;
		Set<List<Integer>> my3rdDerChunk2NotInOrderSet;

		//Set<List<Integer>> my1stDerChunk2NotInOrderSet;
		//	Set<List<Integer>> my1stDerChunk4InOrderSet;
		//	Set<List<Integer>> my1stDerChunk3InOrderSet;
		//	Set<List<Integer>> my1stDerChunk2InOrderSet;

		public De1Rule(Map<Date, List<Integer>> theDelta1Map, Map<Date, List<Integer>> aDelta3Map)
		{

			// ~62693   14 sec.
//			my1stDerChunk4NotInOrderSet = getChunkSet(theDelta1Map, new ChunkStConfig(4, false, theDelta1Map.size()));    // [718, 2002, 2002, 2002,
			// ~47123	26 sec.
//			my1stDerChunk3NotInOrderSet = getChunkSet(theDelta1Map, new ChunkStConfig(3, false, 5));      // 2, 2, 2, 3, 3, 4, 6, 6, 7, 8, 9, 11,
//       // == 5   ~100k 35 sec.

//			my1stDerChunk1NotInOrderSet = new HashSet<>();
//			// 9 =  118164, 11sec
//			// 6 =  313014, 6 sec
//			// 2 = 3358981 denied%: 	 0.30    	;  errors % : 0.35
//			// 1 = 6023777 denied%: 	 0.54    	;  errors % : 0.50
//			for (int i = 0; i < 1; i++ )
//			{
//				my1stDerChunk1NotInOrderSet.addAll(theDelta1Map.get(myDates.get(i)));
//			}
			//= getChunkSet(theDelta1Map, new ChunkStConfig(1, false, 9));

			// 25 ~545394 90sec
			my3rdDerChunk2NotInOrderSet = getChunkSet(aDelta3Map, new ChunkStConfig(2, false, 25));    // [10, 11, 17, 19, 25, 26, 32, 35, 40, 42, 42, 43, 43, 49, 51

//		//my1stDerChunk4InOrderSet = getChunkSet(aDelta1Map, new ChunkStConfig(4, true, aDelta1Map.size()));  // [718, 2002, 2002, 2002, 2002, 2002,
//		my1stDerChunk3InOrderSet = getChunkSet(aDelta1Map, new ChunkStConfig(3, true, 31));                 // 2, 3, 19, 24, 31, 36, 38, 51, 53, 58, 65, 68, 77,
//		my1stDerChunk2InOrderSet = getChunkSet(aDelta1Map, new ChunkStConfig(2, true, 1));                  // 1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2

			// 3 =
			// ERRORS: 	 3; 		 Denied : 	 1776332	;  Accepted : 9462181 \\		 time: 	 9; 	 total : 210 sec. #: [1, 12, 18]
			//							denied%: 	 0.16    	;  errors % : 0.15
			// 6 =
			//  ERRORS: 	 0; 		 Denied : 	 313014	;  Accepted : 10925499 		 time: 	 8; 	 total : 195 sec. #: []
			//  							denied%: 	 0.03    	;  errors % : 0.00
//		my1stDerChunk1Set = new HashSet<>();
//		for (int i = 0; i < 1; i++)
//		{
//			my1stDerChunk1Set.addAll(aDelta1Map.get(myDates.get(i)));
//		}
			// pilot
//		my1stDerChunk2NotInOrderSet = new HashSet<>();
//		my1stDerChunk2NotInOrderSet.addAll(Utils.getAgoChunks(aDelta1Map, 3, new ChunkStConfig(2, false), myDates));

		}

		@Override
		public boolean accept(List<Integer> thePotentialGame)
		{
			boolean aNotAccepted = false;
			List<Integer> aDer1 = DeltaRule.kMode ? Utils.getGameModDelta(thePotentialGame, myGameModMax) : Utils.getGameHDelta(thePotentialGame);
			List<Integer> aDer2 = DeltaRule.kMode ? Utils.getGameModDelta(thePotentialGame, myDelta1ModMax) : Utils.getGameHDelta(aDer1);
			List<Integer> aDer3 = DeltaRule.kMode ? Utils.getGameModDelta(thePotentialGame, myDelta2ModMax) : Utils.getGameHDelta(aDer2);

			//aNotAccepted = aNotAccepted || gameChunkCheck(Utils.getAllChunks(aDer1, 4), my1stDerChunk4NotInOrderSet);
	//		aNotAccepted = aNotAccepted || gameChunkCheck(Utils.getArrayChunks(aDer1, 4), my1stDerChunk4InOrderSet);

			//aNotAccepted = aNotAccepted || gameChunkCheck(Utils.getAllChunks(aDer1, 3), my1stDerChunk3NotInOrderSet);

			//aNotAccepted = aNotAccepted || Collections.disjoint(my1stDerChunk1NotInOrderSet, aDer1);

			aNotAccepted = aNotAccepted || gameChunkCheck(Utils.getAllChunks(aDer3, 2), my3rdDerChunk2NotInOrderSet);


			//		aNotAccepted = aNotAccepted || gameChunkCheck(Utils.getAllChunks(aDer1, 2), my1stDerChunk2NotInOrderSet);

			return !aNotAccepted;
		}
	}
}