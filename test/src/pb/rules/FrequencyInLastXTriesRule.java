package pb.rules;

import pb.Intersection;

import java.util.*;

/**
 * @author Ruslan Dauhiala
 */
public class FrequencyInLastXTriesRule
{
	int[] kGap = new int[]{2, 140};

	public void checkTheRule1(Map<Date, List<Integer>> myAllTheFun)
	{
		List<Date> aDateList = new ArrayList<>(myAllTheFun.keySet());
		Map<Date, Intersection> aResultMap = new LinkedHashMap<>();

		for (int i = 1; i < 70; i++)
		{

		}
		for (int i = 0; i < 20; i++)
		{
			Date aDate = aDateList.get(i);
			aResultMap.put(aDate, new Intersection(aDate, myAllTheFun.get(aDate)));

			List<Integer> aGame = myAllTheFun.get(aDate).subList(0, 5);
			//int aGamesNum = 0;
			for (int j = i + 1; j < aDateList.size(); j++)
			{

			}
		}
	}
}
