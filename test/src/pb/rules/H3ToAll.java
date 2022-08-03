package pb.rules;

import pb.Chunk;
import pb.Intersection;
import pb.Utils;
import pb.results.Result;

import java.util.*;


/**
 * @author Ruslan Dauhiala
 */
public class H3ToAll
{
	/**
	 * exclude a game if a key game no more then a test num chunk
	 * @param myAllTheFun
	 */
	public Result checkTheRule(Map<Date, List<Integer>> myAllTheFun)
	{
		//final int kChunkSize = 2;
		final int kChunkSize = 3;
		final boolean kChunkInOrder = false;
		final int[] kKeyGameIndex = new int[]{1,2};

		Result aResult = new Result("H" + kChunkSize + "toAll");
		Map<Date, Intersection> aResultMap = aResult.getIntersectionMap();
		List<Date> aKeyList = new ArrayList<>(myAllTheFun.keySet())
			// .subList(0,971);
			//.subList(0, 1320)
			;

		for (int i = 0; i < 20; i++)
		{
			Date aDate = aKeyList.get(i);
			Calendar aCalendar = Calendar.getInstance();
			aCalendar.setTime(aDate);
			int aDayOfWeek = aCalendar.get(Calendar.DAY_OF_WEEK);

			Intersection aIntersection = aResultMap.get(aDate);
			if (aIntersection == null)
			{
				aIntersection = new Intersection(aKeyList.get(i), myAllTheFun.get(aKeyList.get(i)));
				aResultMap.put(aDate, aIntersection);
			}

			List<Integer> aCurrentGame = myAllTheFun.get(aDate).subList(0, 5);

			for (int aKeyGameInd = i + kKeyGameIndex[0]; aKeyGameInd < i+kKeyGameIndex[1] ; aKeyGameInd++)
			{
				List<Integer> aKeyGame = myAllTheFun.get(aKeyList.get(aKeyGameInd)).subList(0, 5);

				//List<List<Integer>> aChunks = Utils.getArrayChunks(aKeyGame, kChunkSize);
				List<List<Integer>> aChunks = Utils.getAllChunks(aKeyGame, kChunkSize);
				for (List<Integer> aCheckArray : aChunks)
				{
					for (int y = aKeyGameInd + 1; y < aKeyList.size()-1; y++)
					{
						List<Integer> aMaybeKeyGame = myAllTheFun.get(aKeyList.get(y)).subList(0, 5);
						Calendar aNextGameDayC = Calendar.getInstance();
						aNextGameDayC.setTime(aKeyList.get(y-kKeyGameIndex[0]));

						if (
								  //Utils.containsAllInOrder(aCheckArray, aMaybeKeyGame, kChunkInOrder) &&
								  aMaybeKeyGame.containsAll(aCheckArray) &&
								  kChunkSize <= Utils.getIntersection(aMaybeKeyGame, aKeyGame).size()
								  )
								// d&& aDayOfWeek != aNextGameDayC.get(Calendar.DAY_OF_WEEK))
						{
							//int aStartInd = aMaybeKeyGame.subList(0, 5).indexOf(aCheckArray.get(0));
							//int aStartInd = aKeyGame.indexOf(aCheckArray.get(0));
							//List<Integer> aNextGame = new ArrayList<>(myAllTheFun.get(aKeyList.get(y-kKeyGameIndex[0]))).subList(aStartInd, aStartInd+kChunkSize);
							List<Integer> aNextGame = new ArrayList<>(myAllTheFun.get(aKeyList.get(y-kKeyGameIndex[0]))).subList(0,5);//.subList(aStartInd,aStartInd+kChunkSize));
							List<Integer> anIntersection = Utils.getIntersection(aNextGame, aCurrentGame);

							if (!anIntersection.isEmpty())
							{
								aIntersection.addIntersection(aKeyList.get(y-kKeyGameIndex[0]), anIntersection);
							}
							aIntersection.addPlayedNumbers(aKeyList.get(y-kKeyGameIndex[0]), aNextGame);
						}
					}
				}

			}

		}

//		for (Map.Entry aEntry : aResultMap.entrySet())
//		{
//			String anOutPut = kOutDateFormat.format(aEntry.getKey()) + " " + myAllTheFun.get(aEntry.getKey()) + ": \n" + aEntry.getValue() + "\n";
//			System.out.println(anOutPut);
//		}
		return aResult;
	}


	/**
	 * ? ? ? ? ?
	 * x . . x .	// x - key numbers
	 *
	 * ..
	 * - ? ? - ?
	 * x . . x .
	 */
	public Result checkTheRule2(Map<Date, List<Integer>> myAllTheFun)
	{
		final int kChunkSize = 2;
		//final int kChunkSize = 3;
		final boolean kChunkInOrder = false;
		final int[] kKeyGameIndex = new int[]{1,2};

		Result aResult = new Result("H" + kChunkSize + "toAll");
		Map<Date, Intersection> aResultMap = aResult.getIntersectionMap();
		//List<Date> aKeyList = new ArrayList<>(myAllTheFun.keySet()).subList(0,971);
		List<Date> aKeyList = new ArrayList<>(myAllTheFun.keySet()).subList(0, 1320);

		for (int i = 0; i < 20; i++)
		{
			Date aDate = aKeyList.get(i);
			Calendar aCalendar = Calendar.getInstance();
			aCalendar.setTime(aDate);
			int aDayOfWeek = aCalendar.get(Calendar.DAY_OF_WEEK);

			Intersection aIntersection = aResultMap.get(aDate);
			if (aIntersection == null)
			{
				aIntersection = new Intersection(aKeyList.get(i), myAllTheFun.get(aKeyList.get(i)));
				aResultMap.put(aDate, aIntersection);
			}

			List<Integer> aCurrentGame = myAllTheFun.get(aDate).subList(0, 5);

			for (int aKeyGameInd = i + kKeyGameIndex[0]; aKeyGameInd < i+kKeyGameIndex[1] ; aKeyGameInd++)
			{
				List<Integer> aKeyGame = myAllTheFun.get(aKeyList.get(aKeyGameInd)).subList(0, 5);

				//List<List<Integer>> aChunks = Utils.getArrayChunks(aKeyGame, kChunkSize);
				List<List<Integer>> aChunks = Utils.getAllChunks(aKeyGame, kChunkSize);
				for (List<Integer> aCheckArray : aChunks)
				{
					List<Integer> aKeyListInd  = Utils.getIntersectionIndexes(aKeyGame, aCheckArray);
					for (int y = aKeyGameInd + 1; y < aKeyList.size()-1; y++)
					{
						List<Integer> aMaybeKeyGame = myAllTheFun.get(aKeyList.get(y)).subList(0, 5);
						Calendar aNextGameDayC = Calendar.getInstance();
						aNextGameDayC.setTime(aKeyList.get(y-kKeyGameIndex[0]));

						List<Integer> aIndexList = Utils.getIntersectionIndexes(aMaybeKeyGame, aKeyGame);
						if (
							//Utils.containsAllInOrder(aCheckArray, aMaybeKeyGame, kChunkInOrder) &&
								  aMaybeKeyGame.containsAll(aCheckArray) &&
											 aKeyListInd.containsAll(aIndexList) &&
											 kChunkSize <= aIndexList.size()
								  )
						// d&& aDayOfWeek != aNextGameDayC.get(Calendar.DAY_OF_WEEK))
						{
							//int aStartInd = aMaybeKeyGame.subList(0, 5).indexOf(aCheckArray.get(0));
							//int aStartInd = aKeyGame.indexOf(aCheckArray.get(0));
							//List<Integer> aNextGame = new ArrayList<>(myAllTheFun.get(aKeyList.get(y-kKeyGameIndex[0]))).subList(aStartInd, aStartInd+kChunkSize);

//							List<Integer> aNextGameRaw = new ArrayList<>(myAllTheFun.get(aKeyList.get(y-kKeyGameIndex[0]))).subList(0,5);//.subList(aStartInd,aStartInd+kChunkSize));
							List<Integer> aNextGame = new ArrayList<>(myAllTheFun.get(aKeyList.get(y-kKeyGameIndex[0]))).subList(0, 5);
							List<Integer> anExcludeList = new ArrayList<>();
							for (int anInd: aKeyListInd)
							{
								anExcludeList.add(aNextGame.get(anInd));
							}
							aNextGame.removeAll(anExcludeList);
							//List<Integer> aNextGame = new ArrayList<>(myAllTheFun.get(aKeyList.get(y-kKeyGameIndex[0]))).subList(0, 5);
							List<Integer> anIntersection = Utils.getIntersection(aNextGame, aCurrentGame);

							if (!anIntersection.isEmpty())
							{
								aIntersection.addIntersection(aKeyList.get(y-kKeyGameIndex[0]), anIntersection);
							}
							aIntersection.addPlayedNumbers(aKeyList.get(y-kKeyGameIndex[0]), aNextGame);
						}
					}
				}

			}

		}

//		for (Map.Entry aEntry : aResultMap.entrySet())
//		{
//			String anOutPut = kOutDateFormat.format(aEntry.getKey()) + " " + myAllTheFun.get(aEntry.getKey()) + ": \n" + aEntry.getValue() + "\n";
//			System.out.println(anOutPut);
//		}
		return aResult;
	}
}
