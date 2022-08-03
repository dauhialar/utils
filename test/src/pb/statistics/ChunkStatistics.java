package pb.statistics;

import pb.Chunk;
import pb.Utils;

import java.util.*;

/**
 * @author Ruslan Dauhiala
 */
public class ChunkStatistics
{

	public static <T> List<Integer> getChunkAgoList(ChunkStConfig theConfig, Map<Date, List<T>> myAllTheFun, Map<Date, List<T>> myAllTheFun2)
	{
		return getStatistics(theConfig, myAllTheFun, myAllTheFun2, false);
	}

	public static <T> List<Integer>  getStatistics(ChunkStConfig theConfig, Map<Date, List<T>> myAllTheFun, Map<Date, List<T>> myAllTheFun2)
	{
		return getStatistics(theConfig, myAllTheFun, myAllTheFun2, true);
	}

	public static <T> List<Integer> getStatistics(ChunkStConfig theConfig, Map<Date, List<T>> myAllTheFun, Map<Date, List<T>> myAllTheFun2, boolean thePrint)
	{
		//int aSize = 600;
		List<Date> aKeyList = new ArrayList<>(myAllTheFun.keySet())
				//  .subList(0, 500)
				  ;
		List<Integer> aTotalAverage = new ArrayList<>();
		for (int aGameDate = 0;
			  //aGameDate < 100;
			  aGameDate < 600;
			  aGameDate++)
		{

			List<T> aGame = myAllTheFun2 == null ? myAllTheFun.get(aKeyList.get(aGameDate)) : myAllTheFun2.get(aKeyList.get(aGameDate));
			List<List<T>> aGameChunks = theConfig.isInOrder() ? Utils.getArrayChunks(aGame, theConfig.getChunkSize()) : Utils.getAllChunks(aGame, theConfig.getChunkSize());
			//List<Integer> aGameAverage = new ArrayList<>();
			int aGameMin = aKeyList.size();
			for (List<T> aChunk : aGameChunks)
			{
				List<Integer> anIndexes = new ArrayList<>();
				anIndexes.add(aGameDate);
				for (int aCompareGameInd = aGameDate + 1; aCompareGameInd <
						  aKeyList.size()
									 //- 1;
									  -100;
					  aCompareGameInd++)
				{
					List<T> aCompareGame = myAllTheFun.get(aKeyList.get(aCompareGameInd));

					boolean aContain = theConfig.isInOrder() ? Utils.containsAllInOrder(aChunk, aCompareGame, false) : Utils.getAllChunks(aCompareGame, theConfig.getChunkSize()).contains(aChunk)
									//	aCompareGame.containsAll(aChunk)
									;
					if (aContain)
					{
						anIndexes.add(aCompareGameInd);
						if (aCompareGameInd - aGameDate < aGameMin)
						{
							aGameMin = aCompareGameInd - aGameDate;
						}
						break;
					}
				}
//				if (anIndexes.size() == 1)
//				{
//					//aTotalAverage.add(myAllTheFun.size());
//				} else
//				{
//					List<Integer> aDiff = Utils.getGameHDelta(anIndexes);
//					int anAverage = Utils.getAverage(aDiff);
//					aTotalAverage.add(anAverage);
//				}
			}
		//	if (aGameAverage != aKeyList.size())
			{
				aTotalAverage.add(aGameMin);
			}

		}
		//return Utils.getAverage(aTotalAverage);

		if (thePrint)
		{
			System.out.println(theConfig.getChunkSize() + ": size=" + aTotalAverage.size() + " : " + aTotalAverage);
			List<Integer> aSorted = new ArrayList<>(aTotalAverage);
			Collections.sort(aSorted);
			System.out.println(theConfig.getChunkSize() + ": size=" + aSorted.size() + " : " + aSorted);
		}


		//int aRes = aTotalAverage.get((int)(aTotalAverage.size() * .01));

		return aTotalAverage;
	}


	public static <T> void getAllChunkStats(Map<Date, List<T>> theDelta1Map)
	{
		getAllChunkStats(theDelta1Map, null);
	}

	public static <T> void getAllChunkStats(Map<Date, List<T>> theDelta1Map, Map<Date, List<T>> theDelta1Map2)
	{
		int aSize = theDelta1Map.get(theDelta1Map.keySet().iterator().next()).size();
		List<Boolean> aBooleans = new ArrayList<>();
		aBooleans.add(false);
		if (1 < aSize)
		{
			aBooleans.add(true);
		}

		int aMinSize = //2 < aSize ? 2 :
				  1;
		for (boolean anOrder : aBooleans)
		{
			String aStr = " nums in a row stat (" + (anOrder ? "in order" : "not in order") + "): ";
			System.out.println("\n >>> - - " + ( anOrder ? "" : " NOT") + " ORDERED - - <<< ");
			for (int i = aSize; aMinSize <= i; i--)
			{
				System.out.println("\n " + i + aStr);
				List<Integer> aList = ChunkStatistics.getStatistics(new ChunkStConfig(i, anOrder), theDelta1Map, theDelta1Map2);

//				int aRes = aList.get((int)(aList.size() * .01));
//				System.out.println("average gap: " + aRes);
			}
			System.out.println(" <<< - - - - - - - - - >>> ");
		}
	}
}
