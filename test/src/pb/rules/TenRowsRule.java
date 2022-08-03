package pb.rules;

import pb.Intersection;
import pb.Utils;

import java.util.*;

import static pb.Meters.kFileDateFormat;

/**
 * @author Ruslan Dauhiala
 */
public class TenRowsRule
{

	public void checkTheRule(Map<Date, List<Integer>> myAllTheFun)
	{
		List<Date> aDateList = new ArrayList<>(myAllTheFun.keySet());
		Map<Date, Intersection> aResultMap = new LinkedHashMap<>();
		for (int i = 0; i < 30; i++)
		{
			Date aDate = aDateList.get(i);
			aResultMap.put(aDate, new Intersection(aDate, myAllTheFun.get(aDate)));

			List<Integer> aGame = myAllTheFun.get(aDate).subList(0,5);
			//int aGamesNum = 0;
			for (int j = i+1; j < aDateList.size(); j++)
			{
				List<Integer> aCheckGame = myAllTheFun.get(aDateList.get(j)).subList(0,5);
				if (!Utils.hasSameElements(aGame, aCheckGame)
						  && !Utils.hasSameElements(aCheckGame, aResultMap.get(aDate).getPlayedNumbers())
						  )
				{
					aResultMap.get(aDate).addPlayedNumbers(aDateList.get(j), aCheckGame);
					//aGamesNum++;
					aResultMap.get(aDate).addGameIndex(j-i);
				}
				if (60 < aResultMap.get(aDate).getPlayedNumbers().size() )
				{
					//break;
				}
			}
		}

		for (Map.Entry aEntry : aResultMap.entrySet())
		{
			String anOutPut =
					  //kFileDateFormat.format(aEntry.getKey()) + " " + myAllTheFun.get(aEntry.getKey()) + ": \n" +
								 aEntry.getValue() + "\n";
			System.out.println(anOutPut);
		}

	}
}
