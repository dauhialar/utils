package com.cashflow.rules.cofig;

import com.cashflow.rules.AbstractRule;
import com.cashflow.rules.derivative.DeltaRule2;
import pb.Meters;
import pb.Utils;
import pb.file.FileReader;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Ruslan Dauhiala
 */
public class ConfigTracer
{
	List<AbstractRule> myRuleList;

	int kTestGameNumber = 200;

	private void trace()
	{
		Map<Date, List<Integer>> anAllTheFun = new FileReader().loadFromFile("/Users/jet/Documents/pb/pb.txt");
		List<Date> aDates = new ArrayList<>(anAllTheFun.keySet());

		System.out.println("games: " + IConfig.kGamesNum + "\n");
		System.out.println("-------------------------------------------");
		System.out.println("\tGain\t|\t\tden-err\t|\tErr\t|\tConf\t");
		System.out.println("-------------------------------------------");

		Map<Date, List<Integer>> aFutureGames = new LinkedHashMap<>();
		for (int i = kTestGameNumber; i >=0; i--)
		{
			Date aDate = aDates.remove(i);
			aFutureGames.put(aDate, anAllTheFun.remove(aDate));
		}

		List<String> aCurBestConf = new ArrayList<>();
		for (Map.Entry<Date, List<Integer>> aFutureEntry : aFutureGames.entrySet())
		{
			Map<String, Double> aConfGainMap = new HashMap<>();
			do
			{
				Map<Date, List<Integer>> aGamesMap = new LinkedHashMap<>(anAllTheFun);
				int anErr = 0;
				for (int aGameInd = 0; aGameInd < IConfig.kGamesNum; aGameInd++)
				{
					//long aStartGame = System.currentTimeMillis();
					Date aDate = aDates.get(aGameInd);

					final List<Integer> aCurGame = aGamesMap.remove(aDate).subList(0, 5);
					//Map<Date, List<Integer>> aMap = new LinkedHashMap<>(aGamesMap);
					setupRules(aGamesMap);

					for (AbstractRule aRule : myRuleList)
					{
						if (!aRule.accept(aCurGame))
						{
							anErr++;
						}
					}
				}
				double anErrRate = anErr / (double)IConfig.kGamesNum ;
				double aDenied = De4Config.kDeniedRate;
				double aGain = aDenied - anErrRate;
//				System.out.printf("\t%.2f\t", aGain);
//				System.out.printf("\t%1.2f - %2.2f\t", aDenied, anErrRate);
//				System.out.printf("\t%1$3d\t", anErr);
//				System.out.println("\t" + getConfigStr());
				aConfGainMap.put(getConfigStr(), aGain);
			} while (hasNextConfig());

			//System.out.println("-------------------------------------------");

			//System.out.println("games: " + IConfig.kGamesNum);


			Map<String, Double> aSortedMap = Utils.getSortedMap(aConfGainMap, aCurBestConf);
			int anInd = 10;
			aCurBestConf = aSortedMap.keySet().stream().limit(5).collect(Collectors.toList());

			System.out.print(Meters.aReverceDateFormat.format(aDates.get(0)) + ":\t\t");
			for (Map.Entry<String, Double> aEntry : aSortedMap.entrySet())
			{
				//System.out.println(aEntry.getKey() + " => " + aEntry.getValue());
				System.out.printf(aEntry.getKey() + " => %.2f | \t", aEntry.getValue());
				if (anInd-- < 0)
				{
					break;
				}
			}
			//aBestConf = aSortedMap.entrySet().iterator().next().getKey();
			System.out.println();

			anAllTheFun.put(aFutureEntry.getKey(), aFutureEntry.getValue());
			aDates.add(0, aFutureEntry.getKey());

			resetConfig();
		}


	}

	private String getConfigStr()
	{
		String aConf = "";
		for (AbstractRule aAbstractRule : myRuleList)
		{
			aConf += aAbstractRule.getRuleConfig().toStr() + " ";
			//System.out.println(aAbstractRule.getRuleConfig().toStr());
		}
		return aConf;
	}

	private boolean hasNextConfig()
	{
		for (AbstractRule aAbstractRule : myRuleList)
		{
			if (!aAbstractRule.getRuleConfig().hasNext())
			{
				return false;
			}
		}
		return true;
	}

	private void resetConfig()
	{
		for (AbstractRule aAbstractRule : myRuleList)
		{
			aAbstractRule.getRuleConfig().reset();
		}
	}

	private void setupRules(Map<Date, List<Integer>> theMap)
	{
		myRuleList = new ArrayList<AbstractRule>()
		{{
				add(new DeltaRule2(theMap));
		}};
	}


	public static void main(String args[])
	{
		new ConfigTracer().trace();
	}

}
