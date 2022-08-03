package pb.rules;

import pb.Intersection;
import pb.Utils;
import pb.results.Result;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Ruslan Dauhiala
 */
public class V3xHAll
{

	/**
	 * ? ? ? x x
	 * y x x x x
	 * y x x x x
	 * y x x x x
	 *
	 */
	public Result checkTheRule3(Map<Date, List<Integer>> myAllTheFun)
	{
		int kChunkSize = 2;
		int kGamesLength = myAllTheFun.size();                                                                        //since 2016
		kGamesLength = 719;                                                                        //since 2016
		List<Date> aKeyList = new ArrayList<>(myAllTheFun.keySet()).subList(0, kGamesLength);
		Result aResult = new Result("V" + kChunkSize+ "xH" + kChunkSize);
		Map<Date, Intersection> aResMap = aResult.getIntersectionMap();
		for (int z = 0; z < 20; z++)
		{
			aResMap.put(aKeyList.get(z), new Intersection(aKeyList.get(z), myAllTheFun.get(aKeyList.get(z))));
			for (int i = 0; i < 5; i++)
			{
				for (int aShift = 0 ; aShift < 1; aShift++ )
				{
					List<Integer> aTestChunk = new ArrayList();
					for (int j = z + 1 + aShift; j < z + 1 + kChunkSize + aShift; j++)
					{
						aTestChunk.add(myAllTheFun.get(aKeyList.get(j)).get(i));
					}

					for (int l = 0; l < 5; l++)
					//int l = i;
					{
						final List<Integer> aColumn = Utils.getColumn(myAllTheFun, l).subList(0, kGamesLength);

						for (int y = z + 1 + kChunkSize + aShift; y < aColumn.size() - kChunkSize - aShift; y++)
						{
							int h ;
							for (h = 0; h < aTestChunk.size() && aColumn.get(y+h) == aTestChunk.get(h); h++)
							{

							}
							if (h == kChunkSize)
							{
								List aList = Utils.getAreaChunkForInd(myAllTheFun.get(aKeyList.get(y - kChunkSize - aShift)).subList(0, 5), l, 1);

								List<Integer> aInters = Utils.getIntersection(myAllTheFun.get(aKeyList.get(z)).subList(0, 5), aList);
								if (aInters.size() != 0)
								{
									aResMap.get(aKeyList.get(z)).addIntersection(aKeyList.get(y - kChunkSize - aShift), aInters);
								}
								aResMap.get(aKeyList.get(z)).addPlayedNumbers(aKeyList.get(y - kChunkSize - aShift), aList);
							}
						}
					}
				}

			}
		}
		return aResult;
	}
}
