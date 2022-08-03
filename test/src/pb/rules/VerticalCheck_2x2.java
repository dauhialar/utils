package pb.rules;

import pb.Chunk;
import pb.Intersection;
import pb.Utils;

import java.util.*;

import static pb.Meters.kFileDateFormat;

/**
 * @author Ruslan Dauhiala
 */
public class VerticalCheck_2x2
{
	// vertical search XxX(2x2)
	public void checkTheRule2( Map<Date, List<Integer>> myAllTheFun)
	{
		final int kChunkSize = 2;
		final boolean kChunkInOrder = false;
		final int[] kKeyGameIndex = new int[]{1,3};

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

			List<Integer> aCurrentGame = myAllTheFun.get(aDate);

			for (int aKeyGameInd = i + kKeyGameIndex[0]; aKeyGameInd < i + kKeyGameIndex[1] ; aKeyGameInd++)
			{
				List<Integer> aKeyGame = myAllTheFun.get(aKeyList.get(aKeyGameInd));

				List<List<Integer>> aChunks = Utils.getArrayChunks(aKeyGame.subList(0, 5), kChunkSize);
				for (List<Integer> aCheckArray : aChunks)
				{
					for (int y = aKeyGameInd + 1; y < aKeyList.size()-1; y++)
					{
						List<Integer> aMaybeKeyGame = myAllTheFun.get(aKeyList.get(y));
						if (Utils.containsAllInOrder(aCheckArray, aMaybeKeyGame.subList(0, 5), kChunkInOrder))
						{
							List<Integer> aNextGame = new ArrayList<>(myAllTheFun.get(aKeyList.get(y - (aKeyGameInd-i) )));
							int aChunkStart = aMaybeKeyGame.indexOf(aCheckArray.get(0));
							int aChunkEnd = aChunkStart + aCheckArray.size();
							List<Integer> anIntersection = Utils.getIntersection(aNextGame.subList(aChunkStart, aChunkEnd), aCurrentGame.subList(0, 5));

							if (!anIntersection.isEmpty())
							{
								aIntersection.addIntersection(aKeyList.get(y), anIntersection);
							}
							aIntersection.addPlayedNumbers(aKeyList.get(y-aKeyGameInd-1), aNextGame.subList(aChunkStart, aChunkEnd));
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
