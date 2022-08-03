package pb.rules;

import pb.Intersection;
import pb.Utils;

import java.util.*;

import static pb.Meters.kFileDateFormat;

/**
 * @author Ruslan Dauhiala
 */
public class Rule4
{
	public void checkTheRule1(Map<Date, List<Integer>> myAllTheFun)
	{
		//final int kChunkSize = 2;
		//final boolean kChunkInOrder = true;

		for (int i = 0; i < 5; i++)
		{
			System.out.print("\n\ncol " + i + ":\n" + Utils.getColumn(myAllTheFun, i));
		}

		final int[] kKeyGameIndex = new int[]{1,2};

		Map<Date, Intersection> aResultMap = new LinkedHashMap<>();
		List<Date> aKeyList = new ArrayList<>(myAllTheFun.keySet());

		for (int i = 0; i < 10; i++)
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

				for (Integer aCheckInt : aKeyGame.subList(0, 5))
				{
					for (int y = i + aKeyGameInd + 1; y < aKeyList.size()-1; y++)
					{
						List<Integer> aMaybeKeyGame = myAllTheFun.get(aKeyList.get(y)).subList(0, 5);
						if (aMaybeKeyGame.contains(aCheckInt))
						{
							List<Integer> aNextGame = new ArrayList<>(myAllTheFun.get(aKeyList.get(y-1))).subList(0, 5);

//							Set<Integer> aKeyPlusNext = new HashSet<>(aMaybeKeyGame);
//							aKeyPlusNext.remove(aCheckInt);
//							aKeyPlusNext.addAll(aNextGame);
							//aNextGame = new ArrayList<>(aKeyPlusNext);

							List<Integer> anIntersection = Utils.getIntersection(aNextGame, aCurrentGame);

							if (!anIntersection.isEmpty())
							{
								aIntersection.addIntersection(aKeyList.get(y-1), anIntersection);
							}
							aIntersection.addPlayedNumbers(aKeyList.get(y-1), aNextGame);
							break;
						}
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
