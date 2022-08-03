package pb.rules;

import pb.Intersection;
import pb.Utils;

import java.util.*;

import static pb.Meters.kFileDateFormat;

/**
 * @author Ruslan Dauhiala
 */
public class FibonachiRule
{
	private static final int[] kFibonachi = new int[]{2, 5, 8};

	public void checkRule(Map<Date, List<Integer>> myAllTheFun)
	{
		List<Date> aDateList = new ArrayList<>(myAllTheFun.keySet());
		Map<Date, Intersection> aResultMap = new LinkedHashMap<>();
		for (int i = 0; i < 90; i++)
		{
			Date aDate = aDateList.get(i);
			aResultMap.put(aDate, new Intersection(aDate, myAllTheFun.get(aDate)));

			List<Integer> aGame = myAllTheFun.get(aDate).subList(0, 5);

			for (int j = 0; j < 5; j++)
			{
				Date aPointerDate = aDateList.get(i + 1);
				List<Integer> aPointerGame = myAllTheFun.get(aPointerDate).subList(0, 5);
				//List<Integer> aPointerGame2 = myAllTheFun.get(aDateList.get(i + 2)).subList(0, 5);
				for (int k = i + 2; k < aDateList.size()-20; k ++)
				{
					Date aCheckDate = aDateList.get(k);
					List<Integer> aCheckGame = myAllTheFun.get(aCheckDate).subList(0, 5);
					if (aPointerGame.get(j) == aCheckGame.get(j))
							  //&& aPointerGame2.get(j) == myAllTheFun.get(aDateList.get(k+1)).get(j))
					{
						for (int f : kFibonachi)
						{
							Date aFDate = aDateList.get(k + f);
							aResultMap.get(aDate).addPlayedNumbers(aFDate, myAllTheFun.get(aFDate).subList(0,5));
							List<Integer> anIntersection = Utils.getIntersection(aGame, myAllTheFun.get(aFDate).subList(0, 5));

							if (!anIntersection.isEmpty())
							{
								aResultMap.get(aDate).addIntersection(aFDate, anIntersection);
							}
						}
						break;
					}
				}

			}
		}

		for (Map.Entry aEntry : aResultMap.entrySet())
		{
			String anOutPut = kFileDateFormat.format(aEntry.getKey()) + " " + myAllTheFun.get(aEntry.getKey()) + ": \n" + aEntry.getValue() + "\n";
			System.out.println(anOutPut);
		}

	}

}
