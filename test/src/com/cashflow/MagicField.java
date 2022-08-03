package com.cashflow;

import com.cashflow.rules.AbstractRule;
import com.cashflow.rules.cofig.IConfig;
import com.cashflow.rules.Rule;
import com.cashflow.rules.derivative.DeltaRule2;
import com.cashflow.rules.normal.IndexRule;
import main.Combination;
import pb.Chunk;
import pb.FileTest;
import pb.Meters;
import pb.Utils;
import pb.file.FileReader;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 *

 * @author Ruslan Dauhiala
 */
public class MagicField
{
	List<AbstractRule> myRuleList;
	int myExcl;
	int myGames;

	private void growMoneyTree()
	{
		Map<Date, List<Integer>> anAllTheFun = new FileReader().loadFromFile("/Users/jet/Documents/pb/pb.txt");
		List<Date> aDates = new ArrayList<>(anAllTheFun.keySet());

		Map<Date, List<Integer>> anAllMMFun = null;
		if (IConfig.kUseMM)
		{
			anAllMMFun = new FileReader().loadFromFile("/Users/jet/Documents/pb/mm.txt");
			anAllMMFun = Utils.replaceDates(aDates, anAllMMFun);
		}

		if (IConfig.kDetailedMode)
		{
			new FileTest().gamesAgoStatistics(anAllTheFun);
			System.out.println();
		}
		BufferedOutputStream aBufferedOutputStream = null;
		try
		{
			if (IConfig.kFilePrint)
			{
				Files.createFile(Paths.get(IConfig.kOutFilePath));
				FileOutputStream aFileOutputStream = new FileOutputStream(new File(IConfig.kOutFilePath));
				StringBuffer aBuffer = new StringBuffer().
						  append("games: " + IConfig.kGamesNum + "\n\n\n").
						  append("------------------------------------------------------------------------------------------------------------------------------").
						  append("\tGain\t|\tden - err (%) \t|\tErr\t|\t\tDen / Acc\t\t|\tT(sec.)\t\t|\tConf.\t").
						  append("------------------------------------------------------------------------------------------------------------------------------").
						  append("\n\n");
				aBufferedOutputStream = new BufferedOutputStream(aFileOutputStream);
				aBufferedOutputStream.write(aBuffer.toString().getBytes());
			}
			Map<String, Double> aConfGainMap = new HashMap<>();
			do
			{
				long aStartTotal = System.currentTimeMillis();
				int aRem = 0;
				Map<Date, List<Integer>> aGamesMap = new LinkedHashMap<>(anAllTheFun);
				//		anAllTheFun.remove(aDates.get(aRem++)).subList(0,5);
				//		final List<Integer> aCurGame = aGamesMap.remove(aDates.get(1)).subList(0,5);

				//		for (int i = 0; i < 20; i++)
				//		{
				//			Date aDate = aDates.get(i);
				//			List<Integer> aCurGame = aGamesMap.remove(aDate).subList(0, 5);
				//			List<Integer> aMMCurGame = anAllMMFun.remove(aDate).subList(0,5);
				//			aRem=i+1;
				//		}
				final int[] anErr = {0};
				int aAverageTime = 0;
				long anAverageAccepted = 0;

				List<Integer> anIndList = new ArrayList<>();

				for (; aRem < IConfig.kGamesNum; aRem++)
				{
					long aStartGame = System.currentTimeMillis();
					Date aDate = aDates.get(aRem);

					final List<Integer> aCurGame = aGamesMap.remove(aDate).subList(0, 5);
					if (IConfig.kUseMM && anAllMMFun != null)
					{
						anAllMMFun.remove(aDate);
					}


					Map<Date, List<Integer>> aMap = new LinkedHashMap<>(aGamesMap);
					//			if (aRem < 29)
					//			{
					//				continue;
					//			}
					if (IConfig.kDetailedMode)
					{
						System.out.println("\n★ ★ ★  " + aRem + " \t " + Meters.kOutDateFormat.format(aDate) + " = " + aCurGame.toString() + " = ");
					} else if (aRem % 10 == 0)
					{
						System.out.print(" " + aRem);
					}

					aOkGames = new ArrayList<>();
					setupRules(aMap, anAllMMFun);
					myExcl = 0;
					myGames = 0;
					int aGameNumArr[] = {aRem};
					Combination.combination(Combination.getAll69(), 5, new Combination.GamePrint()
					{
						@Override
						public void print(List<Integer> theGame)
						{
							boolean anExcl = false;
							for (Rule aRule : myRuleList)
							{
								if (!aRule.accept(theGame))
								{
									if (aCurGame.equals(theGame))
									{
										if (IConfig.kDetailedMode)
										{
											if (aRule instanceof IndexRule)
											{
												((IndexRule) aRule).showStatus();
											}
											System.out.println(" >>> BOOOOO <<< : " + aRule.getClass().getSimpleName());
										} else
										{
											System.out.print("\u2019");
										}
										anErr[0]++;
										anIndList.add(aGameNumArr[0]);
									}
									anExcl = true;
									myExcl++;
									break;
								}
							}
							if (!anExcl)
							{
								addGame(theGame);
								myGames++;
							}
						}
					});

					int aGameTime = (int) ((System.currentTimeMillis() - aStartGame) / 1000);
					aAverageTime += aGameTime;
					if (IConfig.kDetailedMode)
					{
						for (AbstractRule aRule : myRuleList)
						{
							aRule.printReport();
						}
						System.out.println(" time: " + aGameTime + " sec.     [TOTAL] excl: " + myExcl + ", INCL: " + myGames);
					}
					anAverageAccepted += myGames;

					if (IConfig.kDetailedMode && aRem == IConfig.kGamesNum - 1)
					{
						for (AbstractRule aRule : myRuleList)
						{
							if (aRule instanceof IndexRule)
							{
								System.out.println("\n\n" + IndexRule.myRate + "\n");
								System.out.println("\n\n" + IndexRule.myRate.toString().replaceAll(", ", "") + "\n");
							}
						}
					}

					//
					// getUseRangeStatistic(aOkGames, aCurGame);

					//System.out.println("***********************");
					//excludeNum();
					//			List<Chunk> a5Chunk = Utils.getAllChunks(aCurGame, 5);
					//			List<Chunk> a4Chunk = Utils.getAllChunks(aCurGame, 4);
					//			List<Chunk> a3Chunk = Utils.getAllChunks(aCurGame, 3);
					//			List<Chunk> a2Chunk = Utils.getAllChunks(aCurGame, 2);
					//
					//			Map<Date, List<Integer>> aResMap = new HashMap<>();
					//			int d = 0;
					//			for(List<Integer> aList : aOkGames)
					//			{
					//				aResMap.put(new Date(d++), aList);
					//			}

					//			Set<Chunk> aChunks5 = new HashSet<Chunk>(Utils.getAllTheChunksTotal(-1, aResMap, Collections.singletonList(new ChunkStConfig(5, false, aOkGames.size()))));
					//			Set<Chunk> aChunks4 = new HashSet<Chunk>(Utils.getAllTheChunksTotal(-1, aResMap, Collections.singletonList(new ChunkStConfig(4, false, aOkGames.size()))));
					//			Set<Chunk> aChunks3 = new HashSet<Chunk>(Utils.getAllTheChunksTotal(-1, aResMap, Collections.singletonList(new ChunkStConfig(3, false, aOkGames.size()))));
					//			Set<Chunk> aChunks2 = new HashSet<Chunk>(Utils.getAllTheChunksTotal(-1, aResMap, Collections.singletonList(new ChunkStConfig(2, false, aOkGames.size()))));
					//			System.out.println("\n\ntest game: " + aCurGame);
					//			System.out.println("5 numbers match? " + gameChunkCheck(a5Chunk, aChunks5));
					//			System.out.println("4 numbers match? " + gameChunkCheck(a4Chunk, aChunks4));
					//			System.out.println("3 numbers match? " + gameChunkCheck(a3Chunk, aChunks3));
					//			System.out.println("2 numbers match? " + gameChunkCheck(a2Chunk, aChunks2));

					//			System.out.println("\n===============\n");

					//System.out.println();

					//			System.out.println("\n Maybe games: \n");
					//			for (int i = 0; i < aOkGames.size(); i++)
					//			{
					//				for (int aN : aOkGames.get(i))
					//				{
					//					if (aCurGame.contains(aN))
					//					{
					//						System.out.println(aOkGames.get(i));
					//					}
					//				}
					//			}

				}

				long anAcceptedNum = anAverageAccepted / IConfig.kGamesNum;
				long aDenied = 11_238_513 - anAcceptedNum;
				double aDenPercent = (double) aDenied / 11_238_513;
				double anErrPercent = anErr[0] == 0 ? 0.0 : ((double) anErr[0] / IConfig.kGamesNum);
				if (IConfig.kDetailedMode)
				{
					System.out.print("\n\n// ERRORS: \t " + anErr[0]);
				}
				double aGain = aDenPercent - ((double) anErr[0] / IConfig.kGamesNum);

				String aCommonConf = getConfigStr();

				if (aBufferedOutputStream != null)
				{
					//"\tGain\t|\tden - err (%) \t|\tErr\t|\t\tDen / Acc\t\t|\tT(sec.)\t\t|\tConf.\t").
					StringBuffer aMessage = new StringBuffer().
							  append("\t" + String.format("%.2f\t", aGain)).
							  append(String.format("\t%1.2f - %2.2f\t", aDenPercent, anErrPercent)).
							  append(String.format("\t%1$3d\t", anErr[0])).
							  append(String.format("\t%1$8d / %2$8d\t", 11_238_513 - anAcceptedNum, anAcceptedNum)).
							  append(String.format("\t%1$3d/%2$5d\t ",
										 (aAverageTime / IConfig.kGamesNum),
										 (System.currentTimeMillis() - aStartTotal) / 1000)).
							  append("\t" + aCommonConf + "\n");
					aBufferedOutputStream.write(aMessage.toString().getBytes());
				} else
				{
					System.out.printf("\n Gain: %.2f   | ", aGain);
					System.out.printf(" %1.2f%% - %2.2f%% (den-err) | ", aDenPercent, anErrPercent);
					System.out.printf(" %1$3d Err  |", anErr[0]);
					System.out.printf(" den/acc : %1$8d / %2$8d", 11_238_513 - anAcceptedNum, anAcceptedNum);
					System.out.printf("   t: %1$3d / %2$5d sec. \n", (aAverageTime / IConfig.kGamesNum), (System.currentTimeMillis() - aStartTotal) / 1000);
				}
				System.out.println(aCommonConf
				//		  + "\n"
				);

//				System.out.print("// \t\t Denied : \t " + (11_238_513 - anAcceptedNum) + "\t;  Accepted : " + anAcceptedNum);
//
//				System.out.print(" \t time: \t " + (aAverageTime / IConfig.kGamesNum) + "; \t total : " + (System.currentTimeMillis() - aStartTotal) / 1000 + " sec.");
//				System.out.print(" Err " + anIndList.size() + " " + anIndList);
//
//				System.out.print("\n// \t\t\t\t\t\t\t denied%: \t ");
//				System.out.printf("%.2f", aDenPercent);
//
//				System.out.print("    \t;  errors % : ");
//				System.out.printf("%.2f", anErr[0] == 0 ? 0.0 : ((double) anErr[0] / IConfig.kGamesNum));
				aConfGainMap.put(aCommonConf, aGain);

			} while (!IConfig.kDetailedMode && hasNextConfig());

//			Map<String, Double> aSortedMap = Utils.getSortedMap(aConfGainMap);
//			int anInd = 10;
//			for (Map.Entry<String, Double> aEntry : aSortedMap.entrySet())
//			{
//				System.out.println(aEntry.getKey() + " => " + aEntry.getValue());
//				if (anInd-- < 0)
//				{
//					break;
//				}
//			}

//			String a = "-1";
//			for (int i = 0; i < 10; i++)
//			{
//				a = " " + " ";
//			}
//			System.out.println(a + " ");
//			String a1 = new String();
//
//			System.out.println(a + " ");
//			try
//			{
//				Thread.sleep(500);
//			} catch (InterruptedException e)
//			{
//			}

//		System.out.println("\n\n Range statistic based on numbers frequency after all rules applied:");
//		for (int i = 0; i < myRangeStat.size(); i ++)
//		{
//			System.out.println(i + " \t\t " + myRangeStat.get(i).toString().replace(", ", "_").replace("[","").replace("]",""));
//		}
		} catch (IOException anExc)
		{
			anExc.printStackTrace();
		} finally
		{
			if (aBufferedOutputStream != null)
			{
				try
				{
					aBufferedOutputStream.flush();
					aBufferedOutputStream.close();
				} catch (IOException e)
				{
					e.printStackTrace();
				}
			}
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

	List<List<Integer>> myRangeStat = new ArrayList<>();

	protected boolean gameChunkCheck(List<Chunk> theGameChunk, Set<Chunk> theExistingChunk)
	{
		for (Chunk aChunk : theGameChunk)
		{
			if (theExistingChunk.contains(aChunk))
			{
				return true;
			}
		}
		return false;
	}

	private void getUseRangeStatistic(List<List<Integer>> theAOkGames, List<Integer> theRealGame)
	{
		Map<Integer, Integer> aUseCount = new TreeMap<>();

		for (int i = 1; i < 70; i++)
		{
			aUseCount.put(i, 0);
		}
		for (List<Integer> aIntegerList : theAOkGames)
		{
			for (int aNum  : aIntegerList)
			{
				int aCount = aUseCount.get(aNum);
				aUseCount.put(aNum, aCount + 1);
			}
		}

//		System.out.println("\nuse range stat: ");
//		for (Map.Entry<Integer,Integer> aEntry : aUseCount.entrySet())
//		{
//			System.out.printf("%1$2d: %2$6d;  ", aEntry.getKey(), aEntry.getValue());
//		}
		Map<Integer, List<Integer>> aSortedMap = new TreeMap<>();
		for (Map.Entry<Integer, Integer> anEntry : aUseCount.entrySet() )
		{
			if (!aSortedMap.containsKey(anEntry.getValue()))
			{
				aSortedMap.put(anEntry.getValue(), new ArrayList<Integer>());
			}
			aSortedMap.get(anEntry.getValue()).add(anEntry.getKey());
		}

		//System.out.println("\nsorted use range stat: ");
		int anInd=-1;
		List<Integer> anIndList = new ArrayList<>();
		for (Map.Entry<Integer,List<Integer>> aEntry : aSortedMap.entrySet())
		{
			anInd++;
			boolean aC = false;
			for (int aNum : theRealGame)
			{
				if (aEntry.getValue().contains(aNum))
				{
					aC = true;
					anIndList.add(anInd);
					break;
				}
			}
			//System.out.printf((aC ? "\t" : "") + anInd + "\t %1d:\t %2s;\n", aEntry.getKey(), aEntry.getValue().toString().replace("[","").replace("]",""));
		}
		myRangeStat.add(anIndList);
		System.out.println(" indexes: " + anIndList.toString());

	}

	private void excludeNum()
	{
		List<Integer> aExclNumList = new ArrayList<Integer>(){{
			add(1);
			add(5);
			add(9);
			add(13);
			add(28);
			add(34);
			add(43);
			add(59);
			add(64);
			add(67);
		}};
		for (int i = aOkGames.size()-1; 0 <= i; i--)
		{
			for (int aNum : aExclNumList)
			{
				if (aOkGames.get(i).contains(aNum))
				{
					aOkGames.remove(i);
					break;
				}
			}
		}
		System.out.println("after excluded " + aExclNumList.size() + " numbers: " + aOkGames.size());
	}

	List<List<Integer>> aOkGames = new ArrayList<>();

	private void addGame(List<Integer> thePotentialGame)
	{
		aOkGames.add(thePotentialGame);
	}

	private void setupRules(final Map<Date, List<Integer>> theAllFun, final Map<Date, List<Integer>> theMMFun)
	{
		myRuleList = new ArrayList(){{                           //	V.	   	Errors               	Excl.       Excl.         Failed
																					// 		for 20 games | time 	| only rule 	| in team	| indexes

//			add(new IndexRule(theAllFun, theMMFun));  			// 5			1           3           2.7M                     1

//			add(new NormalizeRule(theAllFun, theMMFun));  			// 5			1           3           2.7M                     1
//			add(new SortedNormalizeRule(theAllFun));  				// 3			3           30          4.6M                    0, 2,     10
			add(new DeltaRule2(theAllFun));           				// 4			2 			 	33s		   4.2M  	           	 		2,         13                19         (1.6before)
//			add(new ChunkCombinationRule(theAllFun));					// 3			1           70s     		2.8                                 12
//			add(new SumRule(theAllFun));        						// 4			3  		    72     	   5.2M                       1,     7,                  18
//			add(new HorToVerDerRule(theAllFun));      				// 3			4				85s			3-4M                       2, 5,           16, 17
//			add(new GameAgoOneByOneRule(theAllFun));          		// 3			4(1)         80         2.1                		0,          12,15, 16              (1.6) 64sec
//			add(new HorizonToVerticalDerivativeRule(theAllFun));  // 3			3           101         4.5m                     1,   5                     18         (2.3m)
//			add(new OneNumberPerGameAgoRule(theAllFun));          // 3			0 			 	190s    		2.6M                  		                                (2.8) 241sec
//			add(new MegaRule(theAllFun, theMMFun));            	// 3   		3           120         4.7                                       14,    17,    19
//			add(new GamesAgoInColumnRule(theAllFun)); 				// 3			1           240s        1.9M                                12
		}};
		//  add(new VerticalGameDeltaRule(myAllTheFun)); same as HorizonToVerticalDerivativeRule
	}

	public static void main(String args[])
	{
		new MagicField().growMoneyTree();
	}

}


/*






 */
