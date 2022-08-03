package pb.rules;

import pb.Intersection;
import pb.Utils;
import pb.results.Result;

import java.util.*;

/**
 * @author Ruslan Dauhiala
 */
public class GamesAgoIsPlayedNum
{
	public Result checkTheRule2(Map<Date, List<Integer>> myAllTheFun)
	{
		final int kShift = 1;
		final int kIterate = 10;

		Result aResult = new Result("GamesAgoIsPlayedNums");
		Map<Date, Intersection> aResultMap = aResult.getIntersectionMap();
		List<Date> aKeyList = new ArrayList<>(myAllTheFun.keySet())
				  .subList(0, 1320)
				  ;
		Map<Date, Intersection> aResMap = aResult.getIntersectionMap();

		for (int i = 0; i < 25; i++)
		{
			List<Integer> aCurrentGame = myAllTheFun.get(aKeyList.get(i)).subList(0,5);
			aResMap.put(aKeyList.get(i), new Intersection(aKeyList.get(i), aCurrentGame));

			int aFKeyInd = i + kShift;
			List<Integer> aKeyGame = myAllTheFun.get(aKeyList.get(aFKeyInd)).subList(0,5);

			for (int g = 0; g < aKeyGame.size(); g++)
			{
				int aInitInd = (int) Math.sqrt(aFKeyInd*aFKeyInd + aKeyGame.get(g)*aKeyGame.get(g));
				//int aInitInd = aFKeyInd + aKeyGame.get(g);
				for (int j = 0; j < kIterate; j ++)
				{
					//List<Integer> anExcludeGame = myAllTheFun.get(aKeyList.get(aInitInd)).subList(0,5);
					List<Integer> anExcludeGame = Collections.singletonList(myAllTheFun.get(aKeyList.get(aInitInd)).get(g));
					List<Integer> anIntersection = Utils.getIntersection(aCurrentGame, anExcludeGame);
					if (!anIntersection.isEmpty())
					{
						aResMap.get(aKeyList.get(i)).addIntersection(aKeyList.get(aInitInd), anIntersection);
					}
					aResMap.get(aKeyList.get(i)).addPlayedNumbers(aKeyList.get(aInitInd), anExcludeGame);
					//aInitInd += anExcludeGame.get(g);
					//aInitInd += anExcludeGame.get(0);
					aInitInd = (int) Math.sqrt(aInitInd*aInitInd + anExcludeGame.get(0)*anExcludeGame.get(0));
				}
			}
		}

		return aResult;
	}
}
