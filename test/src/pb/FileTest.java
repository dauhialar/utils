package pb;

import pb.file.FileReader;
import pb.results.Result;
import pb.rules.*;
import pb.statistics.ChunkStConfig;
import pb.statistics.ChunkStatistics;

import java.util.*;
import static pb.Meters.*;

/**
 * @author Ruslan Dauhiala
 */
public class FileTest
{
	static Map<Date, List<Integer>> myAllTheFun;


	public static void main(String args[])
	{
		new FileTest().shoot();
	}

	/**
	 * The magic.
	 */
	private void shoot()
	{
		List<Result> aResults = new ArrayList<>();
		myAllTheFun = new FileReader().loadFromFile("/Users/jet/Documents/pb/pb.txt");

		//everyNumberAnalyzer();

		gamesAgoStatistics(myAllTheFun);
		Utils.printInRow(myAllTheFun);

		//
		//chunkStatistics();

		//checkTheRule1();

		//new VerticalSearch_2x2().checkTheRule2(myAllTheFun);


		//new Rule4().checkTheRule1(myAllTheFun);

		// ten games w/o next game numbers
		//new TenRowsRule().checkTheRule(myAllTheFun); //

		//new FibonachiRule().checkRule(myAllTheFun);

//		Result aResult3 = new X1x2Rule().checkTheRule(myAllTheFun); //bad
//		aResults.add(aResult3);



		//Result aResult1 = new VerticalCheck_2x1().checkTheRule3(myAllTheFun); //checkTheRule4 seems better

		Result aResult1 = new VerticalCheck_2x1().checkTheRule4(myAllTheFun); //best so far
		aResults.add(aResult1);

//		Result aResult2 = new H3ToAll().checkTheRule(myAllTheFun);  //test
		Result aResult2 = new H3ToAll().checkTheRule2(myAllTheFun); //best so far
		aResults.add(aResult2);

//		Result aResult3 = new GamesAgoIsPlayedNum().checkTheRule2(myAllTheFun); //test, inaccurate
//		aResults.add(aResult3);


//		Result aTest = new V3xHAll().checkTheRule3(myAllTheFun); //test
//		aResults.add(aTest);

//		Result aTest = new XRowsToOne().checkTheRule(myAllTheFun); //on a fly
//		aResults.add(aTest);


		Utils.print(aResults);

	}


	/**
	 * Vertical search
	 */
	private void checkTheRule1()
	{
		final int kChunkSize = 2;
		final boolean kChunkInOrder = false;
		final int[] kKeyGameIndex = new int[]{1,2};

		Map<Date, Intersection> aResultMap = new LinkedHashMap<>();
		List<Date> aKeyList = new ArrayList<>(myAllTheFun.keySet()).subList(0,971);

		for (int i = 0; i < 15; i++)
		{
			Date aDate = aKeyList.get(i);

			Intersection aIntersection = aResultMap.get(aDate);
			if (aIntersection == null)
			{
				aIntersection = new Intersection(aDate, myAllTheFun.get(aDate));
				aResultMap.put(aDate, aIntersection);
			}

			List<Integer> aCurrentGame = myAllTheFun.get(aDate).subList(0, 5);

			for (int aKeyGameInd = i + kKeyGameIndex[0]; aKeyGameInd < i+kKeyGameIndex[1] ; aKeyGameInd++)
			{
				List<Integer> aKeyGame = myAllTheFun.get(aKeyList.get(aKeyGameInd));

				List<List<Integer>> aChunks = Utils.getArrayChunks(aKeyGame.subList(0, 5), kChunkSize);
				for (List<Integer> aCheckArray : aChunks)
				{

					for (int y = aKeyGameInd + 1; y < aKeyList.size()-1; y++)
					{
						List<Integer> aMaybeKeyGame = myAllTheFun.get(aKeyList.get(y)).subList(0,5);
						if (Utils.containsAllInOrder(aCheckArray, aMaybeKeyGame, kChunkInOrder))
						{
							int aStartInd = aMaybeKeyGame.subList(0, 5).indexOf(aCheckArray.get(0));
							List<Integer> aNextGame = new ArrayList<>(myAllTheFun.get(aKeyList.get(y-kKeyGameIndex[0])).subList(aStartInd,aStartInd+kChunkSize));
							List<Integer> anIntersection = Utils.getIntersection(aNextGame, aCurrentGame);

							if (!anIntersection.isEmpty())
							{
								aIntersection.addIntersection(aKeyList.get(y), anIntersection);
							}
							aIntersection.addPlayedNumbers(aKeyList.get(y-kKeyGameIndex[0]), aNextGame);
						}
					}
				}

			}

		}

		for (Map.Entry aEntry : aResultMap.entrySet())
		{
			String anOutPut = kOutDateFormat.format(aEntry.getKey()) + " " + myAllTheFun.get(aEntry.getKey()) + ": \n" + aEntry.getValue() + "\n";
			System.out.println(anOutPut);
		}
	}



	public void gamesAgoStatistics(Map<Date, List<Integer>> myAllTheFun)
	{
		// games ago:
		Map<Date, List<Integer>> aGamesAgoMap = Utils.getGamesAgoStat(myAllTheFun);
		Map<Date, String> aChunkStatMap = Utils.getChunkStatistic(myAllTheFun);

		//checkGamesAgoAndGames(aGamesAgoMap, myAllTheFun);

		int v = 0;
		Map<Date, List<Frequency>> aEqualFrequencyMap = getEqualFrequencyMap(myAllTheFun);
		Map<Date, List<Frequency>> aStepFrequencyMap = getEqualFrequencyMap(aGamesAgoMap);

		for (Map.Entry<Date, List<Integer>> aSetEntry : aGamesAgoMap.entrySet())
		{

			List<Integer> aRes = new LinkedList<Integer>(aSetEntry.getValue());
			List<Integer> aPlay = myAllTheFun.get(aSetEntry.getKey());

//			String aLetters = "";
//			for (Integer aInteger : aPlay)
//			{
//				aLetters += " " + myAlf.charAt(aInteger);
//			}
			System.out.printf("%12$4d    " +  kOutDateFormat.format(aSetEntry.getKey()) + "   >   " +
								 "%1$2d.%2$2d.%3$2d.%4$2d.%5$2d. \t| %6$2d_%7$2d_%8$2d_%9$2d_%10$2d_ %11$2d | " ,
					  aRes.get(0), aRes.get(1), aRes.get(2), aRes.get(3), aRes.get(4),
					  aPlay.get(0), aPlay.get(1), aPlay.get(2), aPlay.get(3), aPlay.get(4), aPlay.get(5), v++);
//			System.out.print(aLetters);

//			System.out.printf(" `" + (aPlay.get(1) - aPlay.get(0)) +
//								 " `" + (aPlay.get(2) - aPlay.get(1)) +
//								 " `" + (aPlay.get(3) - aPlay.get(2)) +
//								 " `" + (aPlay.get(4) - aPlay.get(3)));
			int d1 = modDif(aPlay.get(1), aPlay.get(0), 10);
			int d2 = modDif(aPlay.get(2), aPlay.get(1), 10);
			int d3 = modDif(aPlay.get(3), aPlay.get(2), 10);
			int d4 = modDif(aPlay.get(4), aPlay.get(3), 10);

			System.out.printf("'%1$2d'%2$2d'%3$2d'%4$2d", d1, d2, d3, d4);
			System.out.print(" | ");

			int dd1 = modDif(d2, d1, 51);
			int dd2 = modDif(d3, d2, 51);
			int dd3 = modDif(d4, d3, 51);
			System.out.printf("''%1$3d''%2$3d''%3$3d", dd1, dd2, dd3);
			System.out.print(" | ");

			int ddd1 = modDif(dd2, dd1, 51);
			int ddd2 = modDif(dd3, dd2, 51);
			System.out.printf("'''%1$3d'''%2$3d", ddd1, ddd2);
			System.out.print(" | ");

			System.out.printf("''''%1$3d", modDif(ddd2, ddd1, 51));
			System.out.print(" | ");


			//System.out.print("\t\tst. fr. " + aStepFrequencyMap.get(aSetEntry.getKey()) + "\t^ \t\t");
			System.out.print(aChunkStatMap.get(aSetEntry.getKey()));
			System.out.print(aEqualFrequencyMap.get(aSetEntry.getKey()) != null && aEqualFrequencyMap.get(aSetEntry.getKey()).size() != 0 ? "\teq. fr. " + aEqualFrequencyMap.get(aSetEntry.getKey()) : "");
			System.out.println();
		}
	}

	private int modDif(int theDd2, int theDd1, int theMod)
	{
		int aRes = theDd2 - theDd1;
		if (aRes<0)
		{
			aRes+=theMod;
		}
		return aRes;
	}

	private void checkGamesAgoAndGames(LinkedHashMap<Date, List<Integer>> theGamesAgoMap, Map<Date, List<Integer>> theMyAllTheFun)
	{
		Map<Date, List<Integer>> aRes = new LinkedHashMap<>();
		for (Date aDate : theGamesAgoMap.keySet())
		{
			List<Integer> aGago = theGamesAgoMap.get(aDate);
			List<Integer> aGame = theMyAllTheFun.get(aDate);
			List<Integer> aMatch = new ArrayList<>();
			for (int i = 0; i < aGago.size(); i++)
			{
				if (aGago.get(i) == aGame.get(i))
				{
					aMatch.add(aGago.get(i));
				}

			}
			if (!aMatch.isEmpty())
			{
				aRes.put(aDate, aMatch);
			}
		}
		System.out.println("games ago = fact num " + aRes.size() + "\n" + aRes.toString());
		for (Map.Entry<Date, List<Integer>> anE : aRes.entrySet())
		{
			System.out.println( kOutDateFormat.format(anE.getKey() )+ " : " + anE.getValue().toString());
		}
	}

	private void everyNumberAnalyzer()
	{
		Map<Integer, Map<Integer, List<Gap>>> aRateMap = new LinkedHashMap<>();
		for (int k = 1; k < 71; k++)
		{
			int aStep = 0;
			int aLastFound = 0;
			Map<Integer, List<Gap>> aMap = new LinkedHashMap<>();
			for (List<Integer> aListV : myAllTheFun.values())
			{
				aStep++;
				for (int x = 0; x < aListV.size() - 1 ; x++)
				{
					if (aListV.get(x) == k)
					{

						if (!aMap.containsKey(x+1))
						{
							aMap.put(x+1, new ArrayList<Gap>());
						}

						int aPosGap = 0;
						List<Gap> aGapList = aMap.get(x+1);
						if (!aGapList.isEmpty())
						{
							aPosGap = aStep - aGapList.get(aGapList.size()-1).myGameNum;
						}
						Gap aGap = new Gap(aStep, aPosGap, aStep - aLastFound);
						aMap.get(x+1).add(aGap);
						aLastFound = aStep;
						break;
					}
				}
			}
			aRateMap.put(k, aMap);
		}

		for (Map.Entry<Integer,Map<Integer, List<Gap>>> anEntry : aRateMap.entrySet())
		{
			System.out.println(" = " + anEntry.getKey() + " = ");
			{
				for (Map.Entry<Integer, List<Gap>> aVa : anEntry.getValue().entrySet())
				{
					System.out.println( "\t\t" + aVa.getKey() + " : " + aVa.getValue());
				}
			}
			System.out.println("");
		}
//		for (Map.Entry<Integer,List<Integer>> anEntry : aRateMap.entrySet())
//		{
//			if (anEntry.getValue().size() == 0 )
//			{
//				continue;
//			}
//			String aGap ="";
//			int aOvGap = 0;
//			for (int aStep = 0; aStep <  anEntry.getValue().size() - 1; aStep++)
//			{
//				aOvGap += anEntry.getValue().get(aStep+1) - anEntry.getValue().get(aStep);
//				aGap += anEntry.getValue().get(aStep+1) - anEntry.getValue().get(aStep) +"-";
//			}
//
//			System.out.println(anEntry.getKey() + " \ttotal: " + anEntry.getValue().size() + ";\teach: " + aOvGap/anEntry.getValue().size() + "nd/rd game; \trate: " + myAllTheFun.size()/anEntry.getValue().size()  + "" +
//					  "\n\t\tvalues: " + anEntry.getValue()  + "" +
//					  "\n\t\tsteps:  " + aGap + "\n");
//		}

		System.out.print("\n===========\n\n");
	}


	private Map<Date, List<Frequency>> getEqualFrequencyMap(Map<Date, List<Integer>> myDateNumMap)
	{
		Map<Date, List<Frequency>> aResMap = new HashMap<>();
		//int amInt = 0;
		List<Date> aKeySet = new ArrayList<>(myDateNumMap.keySet());
		for (int i = 0; i < aKeySet.size() - 1; i++)
		{
			Date aDate = aKeySet.get(i);
			//amInt++;
			List<Integer> aTest = myDateNumMap.get(aDate).subList(0,5);
			List<Frequency> aFrList = new LinkedList<>();
			//int aInd = 0;
			for (int j = i+1; j < aKeySet.size(); j++)
			{
				Date aDate2 = aKeySet.get(j);
				//aInd++;
				if (!aDate.equals(aDate2))
				{
					int aMatch = compareArrays(aTest, myDateNumMap.get(aDate2).subList(0,5));
					if (4 <= aMatch)
					{
						aFrList.add(new Frequency(aMatch, j, i));
						break;
					}
				}
			}
			aResMap.put(aDate, aFrList);
		}

		return aResMap;
	}

	private int compareArrays(List<Integer> theList, List<Integer> theList2)
	{
		int aRes = 0;
		List<Integer> aCompareTo = new ArrayList<>(theList2);
		for (int i = 0; i < theList.size(); i++)
		{
			int aNum = theList.get(i);
			int anInd = aCompareTo.indexOf(aNum);
			if (-1 < anInd)
			{
				aCompareTo.remove(anInd);
				aRes++;
			}
		}

		return aRes;
	}
}
