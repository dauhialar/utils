package pb.rules;

import pb.Intersection;
import pb.Utils;
import pb.results.Result;

import java.util.*;

/**
 * @author Ruslan Dauhiala
 */
public class VerticalCheck_2x1
{

	/**
	 * ? x x x x
	 * y x x x x
	 * y x x x x
	 *
	 *
	 */
	public Result checkTheRule3(Map<Date, List<Integer>> myAllTheFun)
	{
		int kChunkSize = 2;
		int kGamesLength = myAllTheFun.size();
		//kGamesLength = 719;							//since 2006
		List<Date> aKeyList = new ArrayList<>(myAllTheFun.keySet()).subList(0, kGamesLength);
		Result aResult = new Result("V1to2  ");
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
								List<Integer> aPlayedNum = new ArrayList<>();
								aPlayedNum.add(aColumn.get(y - 1 - aShift));
								if (myAllTheFun.get(aKeyList.get(z)).subList(0,5).contains(aColumn.get(y - 1 - aShift)))
								{
									aResMap.get(aKeyList.get(z)).addIntersection(aKeyList.get(y - 1 - aShift), aPlayedNum);
								}
								aResMap.get(aKeyList.get(z)).addPlayedNumbers(aKeyList.get(y - 1 - aShift), aPlayedNum);
							}
						}
					}
				}

			}
		}
		return aResult;
	}

	public Result checkTheRule4(Map<Date, List<Integer>> myAllTheFun)
	{
		int kChunkSize = 2;
		int kGamesLength = myAllTheFun.size();
		//kGamesLength = 719;							//since 2006
		List<Date> aKeyList = new ArrayList<>(myAllTheFun.keySet()).subList(0, kGamesLength);
		Result aResult = new Result("V1to2  ");
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
							for (h = 0;
								  h < aTestChunk.size() && aColumn.get(y+h) == aTestChunk.get(h)
											 && Utils.getIntersection(myAllTheFun.get(aKeyList.get(z+1+h)), myAllTheFun.get(aKeyList.get(y+h))).size() < 2
											 ;
								  h++)
							{

							}
							if (h >= kChunkSize
									  //&& Utils.getIntersection(myAllTheFun.get(aKeyList.get(z+1+h)), myAllTheFun.get(aKeyList.get(y+h))).size() < 2
									  )
							{
								List<Integer> aPlayedNum = new ArrayList<>();
								int aNum = myAllTheFun.get(aKeyList.get(y - 1 - aShift)).get(i);
								aPlayedNum.add(aNum);
								if (myAllTheFun.get(aKeyList.get(z)).subList(0,5).contains(aNum))
								{
									aResMap.get(aKeyList.get(z)).addIntersection(aKeyList.get(y - 1 - aShift), aPlayedNum);
								}
								aResMap.get(aKeyList.get(z)).addPlayedNumbers(aKeyList.get(y - 1 - aShift), aPlayedNum);
							}
						}
					}
				}

			}
		}
		return aResult;
	}
}
