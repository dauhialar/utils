package com.cashflow.rules.normal;

import com.cashflow.rules.AbstractRule;
import pb.Meters;
import pb.Utils;
import pb.file.FileReader;

import java.util.*;

/**
 * @author Ruslan Dauhiala
 */
public class SortedNormalizeRule extends AbstractRule
{

	Set<String> myShortCodeSet;
	Set<String> myFullCodeSet;
	Set<Character> myShortExclCode;
	Set<Character> myShortExclCode2;

	final static int kFullCodeGap = 1000;
	// 500 	0, #: [] 		 Denied: 	 232686
	// 1000  0, #: [] 		 Denied: 	 400242;

	final static int kShortCodeGap = 50;
	// 50    3, #: [7, 11, 12] 		 Denied: 	 1932191
	// 60 	5, #: [6, 7, 11, 12, 14] 		 Denied: 	 2116508

	public SortedNormalizeRule(Map<Date, List<Integer>> theGamesMap)
	{
		super(theGamesMap);
	}

	@Override
	public void setup()
	{
		myShortCodeSet = new HashSet();
		myFullCodeSet = new HashSet();

		List<String> aFullCodeList = new ArrayList<>();
		List<String> aShortCodeList = new ArrayList<>();

//		int i = 0;
		for (Map.Entry<Date, List<Integer>> anEntry : myGamesMap.entrySet())
		{
			List<Integer> aGame = anEntry.getValue().subList(0, 5);

			String aCode = getCode(aGame);
			//Long aFullCode = codeToLong(aCode);
			aFullCodeList.add(aCode);
			aShortCodeList.add(getShortCode(aCode));
		}
		//myShortExclCode = aShortCodeList.get(0);

		for(int i = 0; i < kFullCodeGap; i++)
		{
			myFullCodeSet.add(aFullCodeList.get(i));
		}
		myFullCodeSet.addAll(Utils.getAgoFromGapNumbers(500, aFullCodeList));


		String aAgoShortCode = aShortCodeList.get(0);
		String aAgoShortCode2 = aShortCodeList.get(1);
		int a1 = aShortCodeList.subList(1,aShortCodeList.size()).indexOf(aShortCodeList.get(0));
		if (a1 > -1)
		{
			aAgoShortCode = aShortCodeList.get(a1);
			a1 = aShortCodeList.subList(a1+1,aShortCodeList.size()).indexOf(aShortCodeList.get(0));
			if (a1 > -1)
			{
				aAgoShortCode2 = aShortCodeList.get(a1);
			}
		}

//		int a2 = aShortCodeList.subList(2,aShortCodeList.size()).indexOf(aShortCodeList.get(1));
//		if (a2 > -1)
//		{
//			aAgoShortCode2 = aShortCodeList.get(a2);
//		}
		myShortExclCode = stringToCharSet(aAgoShortCode); //  0, #: [] 		 Denied: 	 1155722;  Accepted: 10082791 		 time: 	 11; 	 total : 252 sec.
		myShortExclCode2 = stringToCharSet(aAgoShortCode2); // 2, #: [13, 17] 		 Denied: 	 2024090;  Accepted: 9214423 		 time: 	 11; 	 total : 243 sec.


//		List<Integer> aIndList = Utils.getGapBetweenSameNumbers(aFullCodeList);
//		while (myFullCodeSet.size() < aMaxS && anInd < aIndList.size()-1)
//		{
//			if (-1 < aIndList.get(++anInd))
//			{
//				myFullCodeSet.add(aFullCodeList.get(aIndList.get(anInd)));
//			} else
//			{
//				myFullCodeSet.add(aFullCodeList.get(anInd));
//			}
//		}
		//

		myShortCodeSet.addAll(aShortCodeList.subList(0,5));

		myShortCodeSet.addAll(Utils.getAgoFromGapNumbers(kShortCodeGap, aShortCodeList));

//		aIndList = Utils.getGapBetweenSameNumbers(aShortCodeList);
//		while (myShortCodeSet.size() < aMaxS && anInd < aIndList.size())
//		{
//			if (-1 < aIndList.get(++anInd))
//			{
//				//myShortCodeSet.add(aShortCodeList.get(aIndList.get(anInd)));        // +1 5err
//			} else
//			{
//				myShortCodeSet.add(aShortCodeList.get(anInd));                // only: 60=>0err, 700k  ; 120=>0err 1.4M
//				                                                              //       200=> 0,15 err 2.7M
//				                                                              //       220=> 0,5,15  3.1M
//				                                                              //       240=> 0,5,15  3.4M
//			}                                                                //       300=> 0,4,5,11,13,15 5M
//		}

//		myRecordMap = new LinkedHashMap<>();
//		myCodeMap = new LinkedHashMap<>();
//		for (int i = 0; i < myDates.size(); i++)
//		{
//			Date aDate = myDates.get(i);
//			List<Integer> aList = myGamesMap.get(aDate).subList(0, 5);
//			List<Integer> aCode = getCode(aList);
//			myCodeMap.put(aDate, new TreeSet<Integer>(aCode));
//			String aStrCode = codeToLong(aCode);
//			if (!myRecordMap.containsKey(aStrCode))
//			{
//				myRecordMap.put(aStrCode, new ArrayList<Date>());
//			}
//			myRecordMap.get(aStrCode).add(aDate);
//		}
//		for (int i = 0; i < kGap; i ++)
//		{
//			Date aDate = myDates.get(i);
//			myShortCodeSet.add(myCodeMap.get(aDate));
//		}
//		myFullCodeSet = myRecordMap.keySet();
	}

	private Long codeToLong(String theList)
	{
		return Long.parseLong(theList);
	}

	private String getCode(List<Integer> theList)
	{
		StringBuilder aStringBuilder = new StringBuilder();
		for (int anInt : theList)
		{
			aStringBuilder.append(anInt);
		}
		char[] aChar = aStringBuilder.toString().toCharArray();
		Arrays.sort(aChar);
		String aS = new String(aChar);
		return aS;
	}

	private List<Integer> getCodeInt(List<Integer> theList)
	{
		List<Integer> aList = new ArrayList<>();
		List<Integer> aZeros = new ArrayList<>();
		for (int aNum : theList)
		{
			if (aNum < 10)
			{
				aZeros.add(0);
				aList.add(aNum);
			} else
			{
				String aNumS = aNum+"";
				if (aNumS.charAt(1) == '0')
				{
					aZeros.add(0);
				} else
				{
					aList.add(Integer.parseInt(aNumS.charAt(1)+""));
				}
				aList.add(Integer.parseInt(aNumS.charAt(0)+""));

			}
//			if (9 < aNum)
//			{
//
//				int aDec = aNum / 10;
//				aList.add(aDec);
//			}
//
//			int anAdz = aNum % 10;
//			aList.add(anAdz);
		}
		Collections.sort(aList);
		aList.addAll(aZeros);
		return aList;
	}

	String getShortCode(String theS)
	{
		StringBuilder noDupes = new StringBuilder(theS);
		for (int i = noDupes.length()-1; 0 < i; i--  )
		{
			if (noDupes.charAt(i) == noDupes.charAt(i-1))
			{
				noDupes.deleteCharAt(i);
			}
		}
		return noDupes.toString();
	}

	Set<Character> stringToCharSet(String theS)
	{
		Set<Character> aShortExclCode = new LinkedHashSet<>();
		for (char a : theS.toCharArray())
		{
			aShortExclCode.add(a);
		}
		return aShortExclCode;
	}

	@Override
	public boolean accept(List<Integer> thePotentialGame)
	{
		String aCode = getCode(thePotentialGame);
		String aShortCode = getShortCode(aCode);

		boolean aRes = true
				   && !myFullCodeSet.contains(aCode)
							&& !myShortCodeSet.contains(aShortCode)
					//&& !aSet.containsAll(myFirstShortCode)    // ++ 1, #: [17] 		 Denied: 	 525057;  Accepted: 10713456 		 time: 	 15; 	 total : 329 sec.
				  ;

		Set<Character> aGameSet = stringToCharSet(aShortCode);
		if (aGameSet.size() < myShortExclCode.size())
		{
			aRes = aRes && !myShortExclCode.containsAll(aGameSet);
		} else
		{
			aRes = aRes && !aGameSet.containsAll(myShortExclCode);
		}

		if (aGameSet.size() < myShortExclCode2.size())
		{
			aRes = aRes && !myShortExclCode2.containsAll(aGameSet);
		} else
		{
			aRes = aRes && !aGameSet.containsAll(myShortExclCode2);
		}

		trackResult(aRes);
		return aRes;
	}

	public static void main(String args[])
	{
		Map<Date, List<Integer>> myAllTheFun = new FileReader().loadFromFile("/Users/jet/Documents/pb/pb.txt");
		SortedNormalizeRule aNormalizeRule = new SortedNormalizeRule(myAllTheFun);
//		aNormalizeRule.test();
		aNormalizeRule.test2();
	}

	private void test2()
	{
		List<String> aFullCodeList = new ArrayList<>();
		List<String> aShortCodeList = new ArrayList<>();

		int i = 0;
		for (Map.Entry<Date, List<Integer>> anEntry : myGamesMap.entrySet())
		{
			System.out.printf("%1$4d   " + Meters.kOutDateFormat.format(anEntry.getKey()) + "   ", i++);
			List<Integer> aGame = anEntry.getValue().subList(0, 5);
			Utils.printf(aGame, 2, " ", "");
			System.out.printf(" %1$2d   ", anEntry.getValue().get(5));

			//long aFullCode = codeToLong(getCode(aGame));
			aFullCodeList.add(getCode(aGame));
			System.out.printf(" | %1$12s | ", getCode(aGame));

			String aShortCode = getShortCode(getCode(aGame));
			//long aL = codeToLong(aShortCode);
			aShortCodeList.add(aShortCode);
			System.out.print(" " + aShortCode + " \n");
			//Utils.printf(aShortCode, 1, ".", "\n");
			//System.out.print(" | " + aShortCodeSet + " |  ");
		}

		NormalizeRule.printGapStat(aFullCodeList);
//		List<Long> aDelta = Utils.getGameHDeltaLong(aFullCodeList);
//		System.out.println("delta: \n\t" + aDelta);
//		Collections.sort(aDelta);
//		System.out.println("sorted delta: \n\t" + aDelta);
		System.out.println("\n = = = \n");

		NormalizeRule.printGapStat(aShortCodeList);
//		List<Long> aDeltaS = Utils.getGameHDeltaLong(aShortCodeList);
//		System.out.println("delta: \n\t" + aDeltaS);
//		Collections.sort(aDeltaS);
//		System.out.println("sorted delta: \n\t" + aDeltaS);


		System.out.println("\n = = = \n\n\n");

//		double aGap = .101;
//
//		List<Integer> aGapList = Utils.getGapBetweenSameNumbers(aFullCodeList);
//		System.out.println("Full code set gap:\n\t" + aGapList);
//		List<Integer> aDivGap = Utils.getGameHDelta(aGapList);
//		Collections.sort(aGapList);
//		System.out.println("sorted gap:\n\t" + aGapList );
//		System.out.println("delta gap:\n\t"+ aDivGap );
//		Collections.sort(aDivGap);
//		System.out.println("sorted delta gap:\n\t" + aDivGap );

//		TreeSet aSet = new TreeSet<>(aGapList);
//		aGapList = new ArrayList<>(aSet);
//		System.out.println("size: " + aGapList.size() + " " + (aGap*100) + "%=" + aGapList.get((int)(aGapList.size()*aGap)) + " " + aGapList + "\n");

//		for (List<Integer> aGame : myGamesMap.values())
//		{
//			aShortCodeList.add(codeToLong(new TreeSet<>(getCode(aGame.subList(0,5)))));
//		}
//		aGapList = Utils.getGapBetweenSameNumbers(aShortCodeList);
//		System.out.println("\n\nShort code set gap:\n\t" + aGapList);
//		List<Integer> aDivGapS = Utils.getGameHDelta(aGapList);
////		System.out.println("Short code set gap of gap:");
////		List<Integer> aGapOfList = Utils.getGapBetweenSameNumbers(aGapList);
////		System.out.println(aGapOfList + "\n sorted main gap: " );
//
//		Collections.sort(aGapList);
//		System.out.println("sorted gap: \n\t" + aGapList);
//		System.out.println("delta gap:\n\t" + aDivGapS );
//		Collections.sort(aDivGapS);
//		System.out.println("sorted delta gap:\n\t" + aDivGapS );
//		aSet = new TreeSet<>(aGapList);
//		aGapList = new ArrayList<>(aSet);
//		System.out.println("size: " + aGapList.size() + " " + (aGap*100) + "%=" + aGapList.get((int)(aGapList.size()*aGap)) + " " + aGapList + "\n");

	}

	//Map<String, List<Date>> myRecordMap;
	//Map<Date, Set<Integer>> myCodeMap;


}

/*
★ ★ ★  0 	 02/25/17.Sat = [6, 32, 47, 62, 65] =
= SortedNormalizeRule =
    total: 11238513       excl:4017169   INCL: 7221344
 time: 14 sec.     [TOTAL] excl: 4017169, INCL: 7221344


★ ★ ★  1 	 02/22/17.Wed = [10, 13, 28, 52, 61] =
= SortedNormalizeRule =
    total: 11238513       excl:3616259   INCL: 7622254
 time: 13 sec.     [TOTAL] excl: 3616259, INCL: 7622254


★ ★ ★  2 	 02/18/17.Sat = [3, 7, 9, 31, 33] =
= SortedNormalizeRule =
    total: 11238513       excl:3756243   INCL: 7482270
 time: 15 sec.     [TOTAL] excl: 3756243, INCL: 7482270


★ ★ ★  3 	 02/15/17.Wed = [5, 28, 33, 38, 42] =
= SortedNormalizeRule =
    total: 11238513       excl:3438121   INCL: 7800392
 time: 12 sec.     [TOTAL] excl: 3438121, INCL: 7800392


★ ★ ★  4 	 02/11/17.Sat = [5, 9, 17, 37, 64] =
= SortedNormalizeRule =
    total: 11238513       excl:3551073   INCL: 7687440
 time: 10 sec.     [TOTAL] excl: 3551073, INCL: 7687440


★ ★ ★  5 	 02/08/17.Wed = [14, 20, 42, 49, 66] =
= SortedNormalizeRule =
    total: 11238513       excl:3161916   INCL: 8076597
 time: 12 sec.     [TOTAL] excl: 3161916, INCL: 8076597


★ ★ ★  6 	 02/04/17.Sat = [6, 13, 16, 17, 52] =
= SortedNormalizeRule =
    total: 11238513       excl:4371035   INCL: 6867478
 time: 11 sec.     [TOTAL] excl: 4371035, INCL: 6867478


★ ★ ★  7 	 02/01/17.Wed = [9, 43, 57, 60, 64] =
 >>> BOOOOO <<< : SortedNormalizeRule
= SortedNormalizeRule =
    total: 11238513       excl:3307957   INCL: 7930556
 time: 11 sec.     [TOTAL] excl: 3307957, INCL: 7930556


★ ★ ★  8 	 01/28/17.Sat = [12, 20, 39, 49, 69] =
= SortedNormalizeRule =
    total: 11238513       excl:4197017   INCL: 7041496
 time: 10 sec.     [TOTAL] excl: 4197017, INCL: 7041496


★ ★ ★  9 	 01/25/17.Wed = [18, 28, 62, 66, 68] =
= SortedNormalizeRule =
    total: 11238513       excl:3556905   INCL: 7681608
 time: 11 sec.     [TOTAL] excl: 3556905, INCL: 7681608


★ ★ ★  10 	 01/21/17.Sat = [23, 25, 45, 52, 67] =
= SortedNormalizeRule =
    total: 11238513       excl:2722366   INCL: 8516147
 time: 10 sec.     [TOTAL] excl: 2722366, INCL: 8516147


★ ★ ★  11 	 01/18/17.Wed = [9, 40, 41, 53, 58] =
 >>> BOOOOO <<< : SortedNormalizeRule
= SortedNormalizeRule =
    total: 11238513       excl:3691142   INCL: 7547371
 time: 11 sec.     [TOTAL] excl: 3691142, INCL: 7547371


★ ★ ★  12 	 01/14/17.Sat = [23, 55, 59, 64, 69] =
 >>> BOOOOO <<< : SortedNormalizeRule
= SortedNormalizeRule =
    total: 11238513       excl:4354677   INCL: 6883836
 time: 8 sec.     [TOTAL] excl: 4354677, INCL: 6883836


★ ★ ★  13 	 01/11/17.Wed = [1, 3, 13, 16, 43] =
 >>> BOOOOO <<< : SortedNormalizeRule
= SortedNormalizeRule =
    total: 11238513       excl:3091186   INCL: 8147327
 time: 9 sec.     [TOTAL] excl: 3091186, INCL: 8147327


★ ★ ★  14 	 01/07/17.Sat = [3, 12, 24, 37, 63] =
= SortedNormalizeRule =
    total: 11238513       excl:2989595   INCL: 8248918
 time: 10 sec.     [TOTAL] excl: 2989595, INCL: 8248918


★ ★ ★  15 	 01/04/17.Wed = [16, 17, 29, 41, 42] =
= SortedNormalizeRule =
    total: 11238513       excl:4255543   INCL: 6982970
 time: 11 sec.     [TOTAL] excl: 4255543, INCL: 6982970


★ ★ ★  16 	 12/31/16.Sat = [1, 3, 28, 57, 67] =
= SortedNormalizeRule =
    total: 11238513       excl:4995271   INCL: 6243242
 time: 8 sec.     [TOTAL] excl: 4995271, INCL: 6243242


★ ★ ★  17 	 12/28/16.Wed = [16, 23, 30, 44, 58] =
 >>> BOOOOO <<< : SortedNormalizeRule
= SortedNormalizeRule =
    total: 11238513       excl:3205070   INCL: 8033443
 time: 9 sec.     [TOTAL] excl: 3205070, INCL: 8033443


★ ★ ★  18 	 12/24/16.Sat = [28, 38, 42, 51, 52] =
= SortedNormalizeRule =
    total: 11238513       excl:3457220   INCL: 7781293
 time: 11 sec.     [TOTAL] excl: 3457220, INCL: 7781293


★ ★ ★  19 	 12/21/16.Wed = [25, 33, 40, 54, 68] =
= SortedNormalizeRule =
    total: 11238513       excl:3667317   INCL: 7571196
 time: 8 sec.     [TOTAL] excl: 3667317, INCL: 7571196



 ERRORS: 	 5, #: [7, 11, 12, 13, 17] 		 Denied: 	 3670155;  Accepted: 7568358 		 time: 	 10; 	 total : 229 sec.

 */


/*
 * ERRORS: 	 70, #: [7, 11, 12, 13, 17, 22, 26, 27, 28, 35, 38, 39, 42, 51, 53, 57, 65, 67, 72, 73, 74, 76, 79, 80, 82, 86, 87, 89, 92, 95, 97, 98, 101, 103, 105, 110, 112, 116, 118, 121, 124, 131, 134, 135, 136, 139, 143, 148, 153, 155, 158, 160, 162, 164, 167, 168, 171, 172, 174, 178, 179, 180, 184, 185, 187, 191, 192, 193, 196, 199]
 * 			Denied: 	 3579350;  Accepted: 7659163 		 time: 	 8; 	 total : 1890 sec.
 */