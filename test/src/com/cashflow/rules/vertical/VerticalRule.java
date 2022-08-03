package com.cashflow.rules.vertical;

import com.cashflow.rules.AbstractRule;
import pb.Meters;
import pb.Utils;
import pb.file.FileReader;

import java.text.NumberFormat;
import java.util.*;

/**
 * unstable, use in if there is nothing else
 *
 total common mistakes: 481 (48.1%)
 total in columns mistakes: 212

 per columns mistakes: {0=44, 1=58, 2=57, 3=30, 4=39}
...
 07/30/16.Sat [11, 17, 21, 23, 32]:	0=[8]		1=[6, 25]		2=[43, 28]		3=[22]		4=[63]		total : []
 07/27/16.Wed [10, 47, 50, 65, 68]:	0=[23]		1=[9, 24]		2=[50, 25]		3=[31]		4=[48]		total : [50] [2col=50]
 07/23/16.Sat [5, 7, 23, 35, 39]:	0=[12]		1=[23, 44]		2=[16, 43]		3=[40]		4=[64]		total : [23]
 07/20/16.Wed [6, 25, 35, 58, 66]:	0=[3]		1=[10, 12]		2=[33, 29]		3=[45]		4=[64]		total : []
...


 * @author Ruslan Dauhiala
 * @deprecated
 */
public class VerticalRule extends AbstractRule
{
	Map<Integer, List<Integer>> myExclNums = new HashMap<>();

	public VerticalRule(Map<Date, List<Integer>> theGamesMap)
	{
		super(theGamesMap);
	}

	@Override
	public void setup()
	{

	}

	@Override
	public boolean accept(List<Integer> thePotentialGame)
	{
		return false;
	}

	public static void main(String[] args)
	{
		Map<Date, List<Integer>> myAllTheFun = new FileReader().loadFromFile("/Users/jet/Documents/pb/pb.txt");
		//new VerticalRule(myAllTheFun).testDynamicVerticalNums();
		new VerticalRule(myAllTheFun).testOneToTwoToOne();
	}

	private Set<Integer> getExcluded(List<Integer> theColumn, int theStartInd, int theChunkSize, int theMaxRes, Set<Integer> aRes)
	{
		List<Integer> aChunk = new ArrayList<>();
		int c = theStartInd+1;
		for (; c < theStartInd+1+theChunkSize; c++)
		{
			aChunk.add(theColumn.get(c));
		}

		for (; c < theColumn.size() - theChunkSize  && aRes.size() < theMaxRes; c++)
		{
			byte aSize = 0;
//			for (int anInd = c+1; anInd < c + 1 + theChunkSize && aChunk.get(aSize) == theColumn.get(anInd); anInd++)
//			{
//				aSize++;
//			}
			List<Integer> aTestChunk = new ArrayList<>();
			for (int anInd = c+1; anInd < c+1+theChunkSize; anInd ++)
			{
				aTestChunk.add(theColumn.get(anInd));
			}
			if (aTestChunk.containsAll(aChunk) )
			//if (theChunkSize <= aSize)
			{
				aRes.add(theColumn.get(c));
			}
		}

//		for (int i = theColumn.size()-1; c < i && aRes.size() < theMaxRes; i--)
//		{
//			int aSize = theChunkSize-1;
//			for (int anInd = i;  0 <= aSize && c + 1 < anInd + theChunkSize && aChunk.get(aSize) == theColumn.get(anInd); anInd--)
//			{
//				aSize--;
//			}
//			if ( aSize < 0)
//			{
//				aRes.add(theColumn.get(i-theChunkSize));
//			}
//		}

		if (aRes.size() < theMaxRes
				 // && 1 < theChunkSize - 1
				 // &&  2 <= theChunkSize - 1
				   )
		{
			aRes.addAll(getExcluded(theColumn, theStartInd, theChunkSize - 1, theMaxRes, aRes));
		}

		return aRes;
	}

	/**
	 *
	 total common mistakes: 662 (66.2%)
	 total in columns mistakes: 201

	 per columns mistakes: {0=50, 1=64, 2=49, 3=38, 4=20}
	 */
	private void testDynamicVerticalNums2()
	{
		List<Date> aDateList = new ArrayList<>(myGamesMap.keySet());

		Map<Date, List<Set<Integer>>> aResMap = new LinkedHashMap<>();

		for (int i = 0; i < kGameNum-1; i++)
		{
			Date aDate = aDateList.get(i);
			aResMap.put(aDate, new ArrayList<Set<Integer>>());
			List<Integer> aGame = myGamesMap.get(aDate).subList(0, 5);
			List<Integer> aMarker = myGamesMap.get(aDateList.get(i+1)).subList(0,5);

			for (int j = 0; j < 5; j++)
			{
				int aNum = aMarker.get(j);
				Map<Integer , Integer> aFound = new HashMap<>();
				for (int k = i + 2; k < kGameNum && aFound.size() < 3 ; k++)
				{
					List<Integer> aTest = myGamesMap.get(aDateList.get(k));
					int anInd  = aTest.indexOf(aNum);
					if (-1 < anInd
							  && anInd != j
							  && !aFound.containsKey(anInd) && !aFound.containsValue(myGamesMap.get(aDateList.get(k-1)).get(anInd)))
					{
						aFound.put(anInd, myGamesMap.get(aDateList.get(k-1)).get(anInd));
					}
				}
				aResMap.get(aDate).add(new HashSet<Integer>(aFound.values()));
			}
		}

		printRes(aResMap);

	}


	int kGameNum = 1000;


	private void testDynamicVerticalNums()
	{
		List<Date> aDateList = new ArrayList<>(myGamesMap.keySet());
		//int kGameNum = 300;
		Map<Date, List<Set<Integer>>> aResMap = new LinkedHashMap<>();
		for (int i = 0; i < 5; i++)
		{
			List<Integer> aColumn = Utils.getColumn(myGamesMap, i).subList(0, kGameNum + 50);
			for (int g = 0; g < kGameNum; g++)
			{
				int aMax;
				switch (i)
				{
					//case 0: aMax =1; break;
					case 1: aMax =2; break;
//					case 2: aMax = 1; break;
//					case 3: aMax = 0; break;
					//case 4: aMax = 0; break;
					default: aMax = 1;
				}
				Set<Integer> aExcl = getExcluded(aColumn, g, 4,
						  //1,
						  //i == 0 || i == 4 ? 1 : 2,
						  //i == 1 || i == 2 ? 2 : 1,
						  aMax,
						  new HashSet<Integer>());
				if (!aResMap.keySet().contains(aDateList.get(g)))
				{
					List<Set<Integer>> aList = new ArrayList<>();
					aList.add(aExcl);
					aResMap.put(aDateList.get(g), aList);
				} else
				{
					aResMap.get(aDateList.get(g)).add(aExcl);
				}
			}
		}

		printRes(aResMap);


	}

	private void testOneToTwoToOne()
	{
		Map<Date, Set<Integer>> aResMap = new LinkedHashMap<>();
		for (int i= 0; i < kGameNum; i++)
		{
			//List<Integer> aGame = myGamesMap.get(myDates.get(i));
			aResMap.put(myDates.get(i), new TreeSet<Integer>());
			List<Integer> a2CheckGame = myGamesMap.get(myDates.get(i+1)).subList(0,5);
			List<Integer> a1CheckGame= myGamesMap.get(myDates.get(i+2)).subList(0,5);

			for (int j = i+4; j < kGameNum+3; j++ )
			{
				List<Integer> a2MaybeTestGame = myGamesMap.get(myDates.get(j)).subList(0,5);
				List<Integer> a1MaybeTestGame = myGamesMap.get(myDates.get(j+1)).subList(0,5);

				if (Utils.getDelta(a2CheckGame, a2MaybeTestGame) <=1
						  && Utils.getDelta(a1CheckGame, a1MaybeTestGame) <= 1)
				{
					aResMap.get(myDates.get(i)).addAll(myGamesMap.get(myDates.get(j - 1)).subList(0, 5));
					break;
				}

			}


		}
		int aWtotal = 0;
		for (Map.Entry<Date, Set<Integer>> aSetEntry : aResMap.entrySet())
		{
			System.out.print(Meters.kOutDateFormat.format(aSetEntry.getKey()) + myGamesMap.get(aSetEntry.getKey()));

			ArrayList aPlayedNums = new ArrayList<Integer>(aSetEntry.getValue());
			aPlayedNums.removeAll(myGamesMap.get(aSetEntry.getKey()));
			int aW = aSetEntry.getValue().size()-aPlayedNums.size();
			int aR = aSetEntry.getValue().size() - aW;
			System.out.print("\t\t r: " + aR + "; w: " + aW);
			System.out.println("\t | played: " + aSetEntry.getValue());
			if (0 < aW )
			{
				aWtotal++;
			}
		}
		System.out.println("\n total wrong: " + aWtotal + " " + (NumberFormat.getPercentInstance().format((double)aWtotal/kGameNum)));

	}

	private void printRes(Map<Date, List<Set<Integer>>> theResMap)
	{
		int aTotalCommonMistakes = 0;
		int aTotalinColumnMistakes = 0;
		HashMap<Integer, Integer> aPerColMist = new HashMap<>();
		for (int m = 0; m < 5; m++)
		{
			aPerColMist.put(m,0);
		}
		for (Map.Entry<Date, List<Set<Integer>>> anEntry : theResMap.entrySet())
		{
			List<Integer> aRealGame = myGamesMap.get(anEntry.getKey()).subList(0, 5);
			Set<Integer> aCommon = new HashSet<>();
			System.out.print(Meters.kOutDateFormat.format(anEntry.getKey()) + " " + aRealGame + ":\t");
			String aWrongInd = "";
			boolean isColmistake = false;
			for (int i = 0; i < anEntry.getValue().size() && i < aRealGame.size(); i++)
			{
				aCommon.addAll(anEntry.getValue().get(i));
				System.out.print(i + "=" + anEntry.getValue().get(i) + "\t\t");
				//if (anEntry.getValue().get(i).contains(aRealGame.get(i)))
				if (anEntry.getValue().get(i).contains(aRealGame.get(i)))
				{
					isColmistake = true;
					aPerColMist.put(i, aPerColMist.get(i)+1);
					//aTotalinColumnMistakes++;
					aWrongInd += " [" + i +"col="+aRealGame.get(i)+"] " ;
				}
			}
			if (isColmistake)
			{
				aTotalinColumnMistakes++;
			}

			List<Integer> aRes = Utils.getIntersection(aCommon, aRealGame);
			System.out.println("total : " + aRes + (!"".equals(aWrongInd) ? aWrongInd : ""));
			if (0 < aRes.size())
			{
				aTotalCommonMistakes++;
			}
		}

		System.out.println("\n\ntotal common mistakes: " + aTotalCommonMistakes + " (" + ((double)aTotalCommonMistakes/kGameNum)*100 + "%)");
		System.out.println("total in columns mistakes: " + aTotalinColumnMistakes + "\n");
		System.out.println("per columns mistakes: " + aPerColMist + "\n");
	}


}
