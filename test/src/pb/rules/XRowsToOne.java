package pb.rules;

import pb.Intersection;
import pb.Utils;
import pb.results.Result;

import java.util.*;

/**
 * @author Ruslan Dauhiala
 */
public class XRowsToOne
{
	public Result checkTheRule(Map<Date, List<Integer>> myAllTheFun)
	{
		final int kChunkSize =
		//  2;
			4;

		Result aResult = new Result(kChunkSize + "RowToOneRule");
		Map<Date, Intersection> aResultMap = aResult.getIntersectionMap();
		//List<Date> aKeyList = new ArrayList<>(myAllTheFun.keySet()).subList(0,971);
		List<Date> aKeyList = new ArrayList<>(myAllTheFun.keySet())
				  //.subList(0, 572)
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
			Set<Integer> aPriorNumbers = new HashSet<>();
			for (int j = i + 1; j <= i + kChunkSize; j++)
			{
				aPriorNumbers.addAll(myAllTheFun.get(aKeyList.get(j)).subList(0, 5));
			}

			for (int k = i + kChunkSize + 1; k < aKeyList.size() - kChunkSize - 1; k++)
			{
				List<Integer> aCandidate = myAllTheFun.get(aKeyList.get(k)).subList(0, 5);

//				Set<Integer> aCandidatePriorNumbers = new HashSet<>();
//				for (int j = k + 1; j <= k + kChunkSize; j++)
//				{
//					aCandidatePriorNumbers.addAll(myAllTheFun.get(aKeyList.get(j)).subList(0, 5));
//				}

				if (!Utils.hasSameElements(aCandidate, aPriorNumbers)
						  //&& !Utils.hasSameElements(aCandidate, aIntersection.getPlayedNumbers())
						  //&& !Utils.hasSameElements(aCandidate, aCurrentGame)
						  )
				{
					List<Integer> anIntersection = Utils.getIntersection(aCandidate, aCurrentGame);

					if (!anIntersection.isEmpty() )
					{
						aIntersection.addIntersection(aKeyList.get(k), anIntersection);
					}
					aIntersection.addPlayedNumbers(aKeyList.get(k), aCandidate);
				}
				if (40 < aIntersection.getPlayedNumbers().size())
				{
					//break;
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