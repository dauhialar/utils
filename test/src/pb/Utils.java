package pb;

import pb.results.Result;
import pb.statistics.ChunkStConfig;
import pb.statistics.ChunkStatistics;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Ruslan Dauhiala
 */
public class Utils
{

	public static final List<ChunkStConfig> kInOrderListConfig = new ArrayList<ChunkStConfig>()
	{{
			add(new ChunkStConfig(2, true, 9));
			add(new ChunkStConfig(3, true, 250));
			add(new ChunkStConfig(4, true, 1000));
	}};

	public static final List<ChunkStConfig> kNotInOrderListConfig = new ArrayList<ChunkStConfig>()
	{{
			add(new ChunkStConfig(2, false, 2));
			add(new ChunkStConfig(3, false, 10));
			add(new ChunkStConfig(4, false, 200));
	}};

	public static final List<ChunkStConfig> kListConfig = new ArrayList<ChunkStConfig>()
	{{
			addAll(kInOrderListConfig);
			addAll(kNotInOrderListConfig);

//			add(new ChunkStConfig(2, true, 9));
//			add(new ChunkStConfig(3, true, 250));
//			add(new ChunkStConfig(4, true, 1000));
//
//			add(new ChunkStConfig(2, false, 2));
//			add(new ChunkStConfig(3, false, 10));
//			add(new ChunkStConfig(4, false, 200));
	}};


	public static <T> List<List<T>> getArrayChunksO(List<T> theList, int theChunkSize)
	{
		List aRes = new ArrayList<>();
		for (int i = 0; i <= theList.size() - theChunkSize; i++)
		{
			List aIntegers = new ArrayList<>();
			//aIntegers.setStartInd(i);
			for (int j = 0; j < theChunkSize; j++)
			{
				aIntegers.add(theList.get(i+j));
			}
			aRes.add(aIntegers);
		}
		return aRes;
	}

	public static <T> List<List<T>> getArrayChunks(List<T> theList, int theChunkSize)
	{
		List<List<T>> aRes = new ArrayList<>();
		for (int i = 0; i <= theList.size() - theChunkSize; i++)
		{
			List<T> aIntegers = new ArrayList<>();
			//aIntegers.setStartInd(i);
			for (int j = 0; j < theChunkSize; j++)
			{
				aIntegers.add(theList.get(i+j));
			}
			aRes.add(aIntegers);
		}
		return aRes;
	}

	public static <T> Set<List<T>> getAllChunksO(List<T> theSource, int theChunkSize)
	{
		int mask = 0;
		int limit = 1 << theSource.size();
		Set<List<T>> aResList = new HashSet<>();
		while (mask < limit)
		{
			List<T> aIntegerList = new ArrayList<>();
			for (int i = 0; i < theSource.size(); i++) {
				//исли бит номер i еденичный, печатаем i-й элемент массива
				if ((mask & (1 << i)) != 0)
				{
					aIntegerList.add(theSource.get(i));
					//System.out.print(array[i] + ",");
				}
			}
			if (aIntegerList.size() == theChunkSize)
			{
				//Collections.sort(aIntegerList);
				aResList.add(aIntegerList);
			}
			mask++;
		}
		return aResList;
	}

	public static <T> List<List<T>> getAllChunks(List<T> theSource, int theChunkSize)
	{
		int mask = 0;
		int limit = 1 << theSource.size();
		List<List<T>> aResList = new java.util.ArrayList<>();
		while (mask < limit)
		{
			List<T> aIntegerList = new ArrayList<>();
			for (int i = 0; i < theSource.size(); i++) {
				//исли бит номер i еденичный, печатаем i-й элемент массива
				if ((mask & (1 << i)) != 0)
				{
					aIntegerList.add(theSource.get(i));
					//System.out.print(array[i] + ",");
				}
			}
			if (aIntegerList.size() == theChunkSize)
			{
				//Collections.sort(aIntegerList);
				aResList.add(aIntegerList);
			}
			mask++;
		}
		return aResList;
	}

	public static List<Integer> getIntersection(Collection<Integer> theBeforeCompareList, Collection<Integer> theBeforeMainRow)
	{
		List<Integer> aRes = new ArrayList<>();
		for (Integer anInt: theBeforeCompareList)
		{
			if(theBeforeMainRow.contains(anInt))
			{
				aRes.add(anInt);
			}
		}
		return aRes;
	}

	public static List getIntersectionIndexes(List<Integer> theMaybeKeyGame, List<Integer> theKeyGame)
	{
		List<Integer> aRes = new ArrayList<>();
		for (Integer anInt: theMaybeKeyGame)
		{
			if(theKeyGame.contains(anInt))
			{
				aRes.add(theMaybeKeyGame.indexOf(anInt));
			}
		}
		return aRes;
	}

	public static List<Integer> getColumn(Map<Date, List<Integer>> theMyAllTheFun, int theI)
	{
		List<Integer> aRes = new ArrayList<>();
		for (List<Integer> aList : theMyAllTheFun.values())
		{
			aRes.add(aList.get(theI));
		}
		return aRes;
	}

	public static boolean hasSameElements(Collection<Integer> theGame, Collection<Integer> theCheckGame)
	{
		for (Integer i : theGame)
		{
			if (theCheckGame.contains(i))
			{
				return true;
			}
		}
		return false;
	}

	public static  <T> boolean containsAllInOrder(List<T> theChunkArray, List<T> theIntegerList, boolean theExactPosition)
	{
		if (theExactPosition)
		{
//			if (theIntegerList.size() < theChunkArray.getStartInd() + theChunkArray.size() )
//			{
//				return false;
//			}
//			for (int i = 0; i < theChunkArray.size(); i++)
//			{
//				if (theIntegerList.get(theChunkArray.getStartInd() + i) != theChunkArray.get(i))
//				{
//					return false;
//				}
//			}
//			return true;
		}
		List<T> aBigArray = theChunkArray.size() < theIntegerList.size() ? theIntegerList: theChunkArray;
		List<T> aSmallArray = theChunkArray.size() < theIntegerList.size() ? theChunkArray : theIntegerList;
		for (int i = 0; i < aBigArray.size() - aSmallArray.size()+1; i++)
		{
			if (aBigArray.get(i).equals(aSmallArray.get(0)))
			{
				boolean aFound = true;
				for (int j = 1; j < aSmallArray.size(); j++)
				{
					if (!aBigArray.get(i+j).equals(aSmallArray.get(j)))
					{
						aFound = false;
						break;
					}
				}
				if (aFound)
				{
					return true;
				}
			}
		}

		return false;
	}


	public static void printInRow(Map<Date, List<Integer>> myAllTheFun)
	{
		int kGamesLength = myAllTheFun.size();                                                                        //since 2016
		List<Date> aKeyList = new ArrayList<>(myAllTheFun.keySet()).subList(0, kGamesLength);
		Map<Date, Intersection> aResMap = new LinkedHashMap<>();
		for (int i = 0; i < 5; i++)
		{
			System.out.print("\n\ncol " + i + ":\n" + Utils.getColumn(myAllTheFun, i).subList(0, kGamesLength));
		}
		System.out.print("\n\n");
	}


	public static void print(List<Result> theResults)
	{
		if (theResults == null || theResults.isEmpty())
		{
			System.out.print("= no results =");
			return;
		}
		Result aResult = theResults.get(0);
		for (Map.Entry<Date, Intersection> aEntry : aResult.getIntersectionMap().entrySet())
		{
			Intersection anInter = aEntry.getValue();
			Date aDate = aEntry.getKey();

			String aLog = "";
			Intersection anInterComb = null;
			for (int i = 1; i < theResults.size(); i++)
			{
				Result aResult12 = theResults.get(i);
				Intersection anInter2 = aResult12.getIntersectionMap().get(aDate);
				if (anInter2 != null)
				{
					anInterComb = anInter.combine(anInter2);
				}
				aLog += getLog(i == 1 ? aResult.getName() : "comb", anInter, aResult12.getName(), anInter2);
				anInter = anInterComb;
			}
			System.out.println(Meters.kFileDateFormat.format(aEntry.getKey()) + " " + aEntry.getValue().getGame());
			System.out.println("\twrong     : " + anInter.getIntersections().size() + "\t " + anInter.getStringIntersections());
			System.out.println("\tplayed    : " + anInter.getPlayedNumbers().size() + "\t " + anInter.getPlayedNumbers());
			System.out.println("\tsuggested : " + anInter.getSuggestedNumbers().size() + "\t " + anInter.getSuggestedNumbers());
			System.out.println(!"".equals(aLog) ? "\t\t log: \n" + aLog : "\n");
		}
	}

	public static String getLog(String theName1, Intersection theI1, String theName2, Intersection theI2)
	{
		StringBuilder aStringBuilder = new StringBuilder();


		aStringBuilder.append(theName1 + "\t\t  w="  + theI1.getIntersections().size() + ";  p=" + theI1.getPlayedNumbers().size() + "\n");
		aStringBuilder.append(theName2 + "\t\t  w="  + theI2.getIntersections().size() + ";  p=" + theI2.getPlayedNumbers().size() + "\n");


		aStringBuilder.append(theName1 + "/" + theName2 + "\t\t Dw="  + getDelta(theI1.getIntersections().keySet(), theI2.getIntersections().keySet()) +
				  "; Dp=" + getDelta(theI1.getPlayedNumbers(), theI2.getPlayedNumbers() ) + "\n");
		return aStringBuilder.toString();
	}

	public static int getDelta(Collection<Integer> theSet1, Collection<Integer> theSet2)
	{
		Set<Integer> aDupSet = new TreeSet<>(theSet1);
		aDupSet.removeAll(theSet2);
		return theSet1.size() - aDupSet.size();
	}

	public static List<Integer> getAreaChunkForInd(List<Integer> theList, int theL, int theKChunkSize)
	{
		List<Integer> aList = new ArrayList<>();
		for (int i = theL; i < theL + theKChunkSize; i++)
		{
			aList.add(theList.get(i < theList.size() ? i : (i % theList.size())));
		}
		return aList;
	}

	/**
	 *
	 * @param theAnIndexes
	 * @return
	 */
	public static List<Integer> getGameHDelta(List<Integer> theAnIndexes)
	{
		List<Integer> aRes = new ArrayList<>();
		for (int i = 0; i < theAnIndexes.size()-1; i++)
		{
			aRes.add(theAnIndexes.get(i+1) - theAnIndexes.get(i));
		}
		return aRes;
	}

	public static List<Integer> getGameModDelta(List<Integer> theIndexes)
	{
		return getGameModDelta(theIndexes, 10);
	}
	public static List<Integer> getGameModDelta(List<Integer> theIndexes, int theMax)
	{
		List<Integer> aRes = new ArrayList<>();
		for (int i = 0; i < theIndexes.size()-1; i++)
		{
			int aNext = theIndexes.get(i + 1);
			int aCur = theIndexes.get(i);
			aNext = aNext < aCur ? aNext + theMax : aNext;
			aRes.add(aNext - aCur);
		}
		return aRes;
	}

	/**
	 *
	 * @param theAnIndexes
	 * @return
	 */
	public static List<Double> getGameDelta(List<Double> theAnIndexes)
	{
		List<Double> aRes = new ArrayList<>();
		for (int i = 0; i < theAnIndexes.size()-1; i++)
		{
			aRes.add(theAnIndexes.get(i+1) - theAnIndexes.get(i));
		}
		return aRes;
	}

	/**
	 *
	 * @param theAnIndexes
	 * @return
	 */
	public static List<Long> getGameHDeltaLong(List<Long> theAnIndexes)
	{
		List<Long> aRes = new ArrayList<>();
		for (int i = 0; i < theAnIndexes.size()-1; i++)
		{
			aRes.add(Math.abs(theAnIndexes.get(i+1) - theAnIndexes.get(i)));
		}
		return aRes;
	}


	public static int getAverage(List<Integer> theDiff)
	{
		int aRes = 0;
		for (int i : theDiff)
		{
			aRes+=i;
		}
		return aRes/theDiff.size();
	}


	public static <T> Set<List<T>> getAllTheChunksTotalO(Map<Date, List<T>> theGameMap, List<ChunkStConfig> theConfigList)
	{
		Set<List<T>> aRes = new HashSet<>();

		List<Date> aDates = new ArrayList<>(theGameMap.keySet());

		for (ChunkStConfig aConfig : theConfigList)
		{
			int aStart = -1 < aConfig.getStartInd() ? aConfig.getStartInd() : 0;
			for (int i = aStart; i < aStart + aConfig.getLenght() && i < aDates.size() ; i++)
			{
				List<T> aList = theGameMap.get(aDates.get(i));
				if (5 < aList.size())
				{
					aList = aList.subList(0,5);
				}
				aRes.addAll(
						  aConfig.isInOrder() ?
									 Utils.getArrayChunksO(aList, aConfig.getChunkSize()) :
									 Utils.getAllChunksO(aList, aConfig.getChunkSize())
				);
			}
		}
		return aRes;
	}

	public static Map<String, Double> getSortedMap(Map<String, Double> theAnIndexGapMap, List<String> theBestConf)
	{
		return theAnIndexGapMap
				  .entrySet()
				  .stream()
				  .sorted((e1, e2) -> {
								 int cmp1 = e2.getValue().compareTo(e1.getValue());
								 if (cmp1 != 0)
								 {
									 return cmp1;
								 } else
								 {
									 Integer anInd1 = theBestConf.indexOf(e1.getKey());
									 Integer anInd2 = theBestConf.indexOf(e2.getKey());
									 if (anInd1 != -1 || anInd2 != -1)
									 {
										 if (anInd1 > -1 && anInd2 > -1)
										 {
											 return anInd1.compareTo(anInd2);
										 }
										 return -anInd1.compareTo(anInd2);
									 }
									 return e1.getKey().compareTo(e2.getKey());
									 //return e2.getKey().compareTo(e1.getKey());
								 }
							 }
							 //Map.Entry.comparingByValue()
				  )
				  .collect(Collectors.toMap(
										Map.Entry::getKey,
										Map.Entry::getValue,
										(e1, e2) -> e1,
										LinkedHashMap::new)
				  );
	}

	public static <T> List<List<T>> getAllTheChunksTotal(int theStartInd, Map<Date, List<T>> theGameMap, List<ChunkStConfig> theConfigList)
	{
		List<List<T>> aRes = new ArrayList<>();
		List<Date> aDates = new ArrayList<>(theGameMap.keySet());
		for (ChunkStConfig aConfig : theConfigList)
		{
			int aStart = -1 < aConfig.getStartInd() ? aConfig.getStartInd() : 0;
			for (int i = aStart; i < aStart + aConfig.getLenght() && i < aDates.size(); i++)
			{
				List<T> aList = theGameMap.get(aDates.get(i));
				if (5 < aList.size())
				{
					aList = aList.subList(0,5);
				}
				aRes.addAll(
						  aConfig.isInOrder() ?
									 Utils.getArrayChunks(aList, aConfig.getChunkSize()) :
									 Utils.getAllChunks(aList, aConfig.getChunkSize())
				);
			}
		}
		return aRes;
	}

	public static <T> String getChunkStatistic(int theStartInd, Map<Date, List<T>> theGameMap, int theGameSize)
	{
		String aRes = new String();

		if (false && 100 < theStartInd)
		{
			return aRes;
		}
		List<Date> aDates = new ArrayList<>(theGameMap.keySet());
		List<T> aGame = theGameMap.get(aDates.get(theStartInd)).subList(0,theGameSize);
		for (ChunkStConfig aConfig : kListConfig)
		{
			String aConfRes = null;
			int i = theStartInd + 1;
			for (; i < aDates.size() - 1; i++)
			{
				List<List<T>> aChunks = aConfig.isInOrder() ?
						  Utils.getArrayChunks(theGameMap.get(aDates.get(i)).subList(0,theGameSize), aConfig.getChunkSize()) :
						  Utils.getAllChunks(theGameMap.get(aDates.get(i)).subList(0, theGameSize), aConfig.getChunkSize());
				for (List<T> aChunk : aChunks)
				{
					if (aConfig.isInOrder() ? Utils.containsAllInOrder(aChunk, aGame, false) : aGame.containsAll(aChunk))
					{
						aConfRes = aConfig.getChunkSize() + "_" + (aConfig.isInOrder() ? "order" : "all") + "=" + (i - theStartInd);
						aRes += "\t" + aConfRes + "\t";
						break;
					}
				}
				if (aConfRes != null)
				{
					break;
				} else if (i == aDates.size() - 2)
				{
//					aConfRes = aConfig.getChunkSize() + "_" + (aConfig.isInOrder() ? "ord" : "all") + "=MAX";
//					aRes += "\t" + aConfRes + "\t";
				}
			}
		}

		return aRes;
	}

	public static Map<Date, String> getChunkStatistic(Map<Date, List<Integer>> theDateListMap)
	{
		List<Date> aKeyList = new ArrayList<>(theDateListMap.keySet());
		Map<Date, String> aChunkStatMap = new HashMap<>();
		for (int i = 0; i < aKeyList.size(); i++)
		{
			Date aDate = aKeyList.get(i);
			aChunkStatMap.put(aDate, Utils.getChunkStatistic(i, theDateListMap, 5));
		}
		return aChunkStatMap;
	}

	/**
	 * Games ago in each column
	 * @param theDateListMap
	 * @return
	 */
	public static Map<Date, List<Integer>> getGamesAgoStat(Map<Date, List<Integer>> theDateListMap)
	{
		LinkedHashMap<Date, List<Integer>> aGamesAgoMap = new LinkedHashMap<>();
		List<Date> aKeyList = new ArrayList<>(theDateListMap.keySet());
		//Map<Date, String> aChunkStatMap = new HashMap<>();
		for (int i = 0; i < aKeyList.size(); i++)
		{
			Date aDate = aKeyList.get(i);
			//aChunkStatMap.put(aDate, Utils.getChunkStatistic(i, theDateListMap, 5));

			List<Integer> aGameAgoRow = new LinkedList<>();

			List<Integer> aMainRow = theDateListMap.get(aDate);

			for (int t = 0 ; t < aMainRow.size()-1; t++)
			{
				Integer aInteger = aMainRow.get(t);
				boolean aFound = false;
				for (int j = i+1; j < aKeyList.size(); j++ )
				{
					List<Integer> aSearchIn = theDateListMap.get(aKeyList.get(j)).subList(0,5);
					if (aSearchIn.contains(aInteger)) 	// in the game
					//if (aSearchIn.get(t) == aInteger) 		// at particular position
					{
						aGameAgoRow.add(j-i);
						aFound = true;
						break;
					}
//					for (int p = 0; p < aSearchIn.size() - 1; p++)
//					{
//						Integer anAnother = aSearchIn.get(p);
//						if (anAnother == aInteger)
//						{
//							aGameAgoRow.add(j-i);
//							aFound = true;
//							break;
//						}
//					}
//					if (aFound)
//					{
//						break;
//					}
				}
				if (!aFound)
				{
					aGameAgoRow.add(0);
				}
			}
			aGamesAgoMap.put(aDate, aGameAgoRow);

		}
		return aGamesAgoMap;
	}

	public static void printf(Collection<Integer> theList, int theMaxSize, String theDelim, String theEnd)
	{
		String aFormat = "";
		for (int i = 1; i <=  theList.size(); i++)
		{
			aFormat += "%" + i + "$" + theMaxSize + "d" + theDelim;
		}
		System.out.printf(aFormat, theList.toArray());
		System.out.print(theEnd);
	}

	public static <T> List<Integer> getGapBetweenSameNumbers(List<T> theCollection, List<T> theMMCollection)
	{
		List<Integer> aList = new ArrayList<>();
		for (int i = 0; i < theCollection.size() && i < theMMCollection.size()-10; i++)
		{
			T anEl = theCollection.get(i);
			aList.add(theMMCollection.subList(i,theMMCollection.size()).indexOf(anEl));
		}
		return aList;
	}

	public static <T> Map<T, List<Integer>> getGapBetweenSameNumbersAll(Collection<T> theCollection)
	{
		Map<T, List<Integer>> aTreeMap = new LinkedHashMap<>();
		List<T> aList = new ArrayList<>(theCollection);
		for (int i = 0; i < theCollection.size()-10; i++)
		{
			T aNum = aList.remove(0);
			int anInd = aList.indexOf(aNum);
			if (aTreeMap.get(aNum) == null)
			{
				aTreeMap.put(aNum, new ArrayList<>());
			}
			aTreeMap.get(aNum).add(anInd);
		}
		return aTreeMap;
	}

	public static <T> List<Integer> getGapBetweenSameNumbers(T theItem, Collection<T> theCollection)
	{
		List<T> aList = new ArrayList<>(theCollection);
		List<Integer> aRes = new ArrayList<>();
		//int
		int aNextInd = 0;
		int anOldInd = 1;
		do
		{
			aNextInd = aList.subList(anOldInd, aList.size()-1).indexOf(theItem);
			if (aNextInd > -1)
			{
				aRes.add(aNextInd);
				anOldInd += aNextInd + 1;
			}
		} while (aNextInd > -1);
		return aRes;
	}

	public static <T> List<Integer> getGapBetweenSameNumbers(Collection<T> theCollection)
	{
		List<T> aList = new ArrayList<>(theCollection);
		List<Integer> aRes = new ArrayList<>();
		for (int i = 0; i <
				 //100
				  theCollection.size()-10
				 ;
			  i++)
		{
			T aNum = aList.remove(0);
			int anInd = aList.indexOf(aNum);
			//if (-1 < anInd)
			{
				aRes.add(anInd);
			}
		}
		return aRes;
	}

	public static <T> Set<T> getIntSet(Map<Date, List<T>>  theMap)
	{
		Set<T> aSet = new LinkedHashSet();
		for (List<T> a : theMap.values())
		{
			aSet.add(a.get(0));
		}
		return aSet;
	}

	public static <T> List<T> getIntList(Map<Date, List<T>>  theMap)
	{
		List<T> aList = new ArrayList<>();
		for (List<T> a : theMap.values())
		{
			aList.add(a.get(0));
		}
		return aList;
	}

	public static <T> Set<List<T>> getChunkSet(Map<Date, List<T>> theHDeltaMap2, ChunkStConfig theChunkStConfigs)
	{
		List<List<T>> aList = Utils.getAllTheChunksTotal(-1, theHDeltaMap2, Collections.singletonList(theChunkStConfigs));
		return new HashSet<>(aList);
	}

	public static Map<Date, List<Integer>> replaceDates(List<Date> thePbDates, Map<Date, List<Integer>> theMMTheFun)
	{
		List<Date> aMMDates = new java.util.ArrayList<>(theMMTheFun.keySet());
		Map<Date, List<Integer>> aMMTheFunDates = new LinkedHashMap<>();
		for (int i = 0 ; i < theMMTheFun.size(); i++)
		{
			aMMTheFunDates.put(thePbDates.get(i), theMMTheFun.get(aMMDates.get(i)));
		}
		return aMMTheFunDates;
	}

	public static Map<Date, List<Integer>> get5SizeMap( Map<Date, List<Integer>> theMap)
	{
		Map<Date, List<Integer>> aRes = new LinkedHashMap<>();
		for (Map.Entry<Date, List<Integer>> anEntry : theMap.entrySet())
		{
			aRes.put(anEntry.getKey(), anEntry.getValue().subList(0,5));
		}
		return aRes;
	}

	public static <T> Set<List<T>> getAgoChunks(Map<Date, List<T>> theDelta1Map, int theInd, ChunkStConfig theChunkStConfig, List<Date> theDates)
	{
		Set<List<T>> aRes = new HashSet<>();
		List<Integer> a2ChunkList = ChunkStatistics.getChunkAgoList(theChunkStConfig, theDelta1Map, null);
		int anInd = 0;
		int i = 0;
		while(i < a2ChunkList.size() && anInd < theInd)
		{
			if (-1 < a2ChunkList.get(i) && a2ChunkList.get(i) < theDates.size())
			{
				List<T> aGame = theDelta1Map.get(theDates.get(a2ChunkList.get(i)));
				if (aGame != null)
				{
					aRes.addAll(Utils.getAllChunks(aGame, theChunkStConfig.getChunkSize()));
					anInd++;
				}
			}
			i++;
		}
		return aRes;
	}

	public static Collection<Integer> getAgoInt(Map<Date, List<Integer>> theHVH3delta3Map, int theGap, List<Date> theDates)
	{
		Set<Integer> aSet = new HashSet<>();
		List<Integer> aGapList = Utils.getGapBetweenSameNumbers(theHVH3delta3Map.values());
		for (int i = 0; i < theGap; i++)
		{
			if (-1 < aGapList.get(i) && aGapList.get(i) < theDates.size())
			{
				aSet.add(theHVH3delta3Map.get(theDates.get(aGapList.get(i))).get(0));
			}
		}
		return aSet;
	}


	public static List<Double> getDeltaPercentList(int i1, int i2, int j1, int j2, Collection<List<Integer>> theGamesMap)
	{
		List<Double> aMaxDeltaPersent = new ArrayList<>();
		for (List<Integer> aGame : theGamesMap)
		{
			int d1 = aGame.get(i1) - aGame.get(i2);
			int d2 = aGame.get(j1) - aGame.get(j2);
			aMaxDeltaPersent.add((double) d1 / d2);
		}
		return aMaxDeltaPersent;
	}

	public static <T> Set<T> getAgoFromGapNumbers(int theSize, List<T> theList)
	{
		List<Integer> aAgoList = Utils.getGapBetweenSameNumbers(theList);
		Set<T> aRes = new HashSet<>();
		int aAdded = 0;
		for (int i = 0; i < aAgoList.size()
				  && aAdded < theSize
				  ; i++)
		{
			int anAgoNum = aAgoList.get(i);
//			if (anAgoNum == -1)     // pilot ??
//			{
//				aRes.add(theList.get(i));
//			} else
			if (-1 < anAgoNum && anAgoNum < theList.size())
			{
				aAdded++;
//				aRes.add(theList.get(anAgoNum));

				aRes.add(theList.get(anAgoNum+i)); // ?? pilot

//				int anInd = anAgoNum;
//				aRes.add(theList.get(0 < anInd-1 ? anInd-1 : anAgoNum+i)); // ?? pilot
//				aRes.add(theList.get(0 < anInd-2 ? anInd-2 : anAgoNum+i)); // ?? pilot
//				aRes.add(theList.get(0 < anInd-3 ? anInd-3 : anAgoNum+i)); // ?? pilot
//				aRes.add(theList.get(anInd)); // ?? pilot
//				aRes.add(theList.get(anInd+2)); // ?? pilot
//				aRes.add(theList.get(anInd+3)); // ?? pilot
//				aRes.add(theList.get(anInd+4)); // ?? pilot
//
			}
		}

		return aRes;
	}

	public static double getRate(int i1, int i2, int j1, int j2)
	{
		return (double)(i2-i1)/(j2-j1);
	}

}
