package pb.rules;

import pb.Intersection;
import pb.Utils;
import pb.results.Result;

import java.util.*;

/**
 * @author Ruslan Dauhiala
 */
public class X1x2Rule
{
	/**
	 * ? ? ? ? ?
	 * x x Z x x
	 * x Z x Z x
	 *
	 * @param myAllTheFun
	 */
	public Result checkTheRule(Map<Date, List<Integer>> myAllTheFun)
	{
		//final int kChunkSize = 2;
		final int kChunkSize = 3;
		final boolean kChunkInOrder = false;
		final int[] kKeyGameIndex = new int[]{1,2};

		Result aResult = new Result("X1x2Rule");
		Map<Date, Intersection> aResultMap = aResult.getIntersectionMap();
		//List<Date> aKeyList = new ArrayList<>(myAllTheFun.keySet()).subList(0,971);
		List<Date> aKeyList = new ArrayList<>(myAllTheFun.keySet()).subList(0, 572);

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
			List<Integer> aKingKeyGame = myAllTheFun.get(aKeyList.get(i + 1)).subList(0, 5);
			List<Integer> aKingKeyGame2ndRow = myAllTheFun.get(aKeyList.get(i + 2)).subList(0, 5);
			List<Integer> aKingKeyGame3ndRow = myAllTheFun.get(aKeyList.get(i + 3)).subList(0, 5);

			for (int aK = 0; aK < 5; aK++)
			{
				int aLeftInd = aK - 1 < 0 ? -1 : aK - 1;
				int aRightInd = aK + 1 < 5 ? aK + 1 : -1;

				int aTopKey = aKingKeyGame.get(aK);
				int aKeyLNum = aLeftInd < 0 ? -1 : aKingKeyGame2ndRow.get(aLeftInd);
				int aKeyRNum = aRightInd < 0 ? -1 : aKingKeyGame2ndRow.get(aRightInd);

//				int aKeyLNum3 = aLeftInd < 1 ? -1 : aKingKeyGame3ndRow.get(aLeftInd-1);
//				int aKeyRNum3 = aRightInd < 0 ? -1 : aKingKeyGame3ndRow.get(aRightInd+1);

				for (int aKeyGameInd = i + 3; aKeyGameInd < aKeyList.size() - 2; aKeyGameInd++)
				{
					List<Integer> aMKeyGame = myAllTheFun.get(aKeyList.get(aKeyGameInd)).subList(0, 5);
					int anFoundInd = aMKeyGame.indexOf(aTopKey);
					if (-1 < anFoundInd
							  && anFoundInd == aK
							  )
					{
						boolean a3rdContains = Utils.hasSameElements(aKingKeyGame3ndRow, myAllTheFun.get(aKeyList.get(aKeyGameInd + 2)).subList(0, 5));
						List<Integer> aMaybeButtomKeyGame = myAllTheFun.get(aKeyList.get(aKeyGameInd + 1)).subList(0, 5);
						List<Integer> aMaybeButtomKeyGame3 = myAllTheFun.get(aKeyList.get(aKeyGameInd + 2)).subList(0, 5);
						boolean aMatch = 0 < anFoundInd &&
								  // a3rdContains &&
								  (aMaybeButtomKeyGame.get(anFoundInd - 1) == aKeyLNum
								  || aMaybeButtomKeyGame.get(anFoundInd - 1) == aKeyRNum
								  )
//								  && ( -1 < anFoundInd - 2 ?
//								  (aMaybeButtomKeyGame3.get(anFoundInd - 2) == aKeyLNum3
//								  || aMaybeButtomKeyGame3.get(anFoundInd - 2) == aKeyRNum3) : false)
						 ||
								  (anFoundInd < 4 &&
											 //a3rdContains &&
											 (
														aMaybeButtomKeyGame.get(anFoundInd + 1) == aKeyLNum ||
														aMaybeButtomKeyGame.get(anFoundInd + 1) == aKeyRNum))
//								  && ( anFoundInd + 2 < 5 ?
//											 (aMaybeButtomKeyGame3.get(anFoundInd + 2) == aKeyLNum3 ||
//											 aMaybeButtomKeyGame3.get(anFoundInd + 2) == aKeyRNum3) : false)
						;

						if (aMatch)
						{
							List<Integer> aNextGame = new ArrayList<>(myAllTheFun.get(aKeyList.get(aKeyGameInd - 1))).subList(0, 5);//.subList(aStartInd,aStartInd+kChunkSize));
							List<Integer> anIntersection = Utils.getIntersection(aNextGame, aCurrentGame);

							if (!anIntersection.isEmpty())
							{
								aIntersection.addIntersection(aKeyList.get(aKeyGameInd - 1), anIntersection);
							}
							aIntersection.addPlayedNumbers(aKeyList.get(aKeyGameInd - 1), aNextGame);
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
