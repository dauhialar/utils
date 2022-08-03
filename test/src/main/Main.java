package main;

import com.ArrayList;
import com.cashflow.rules.derivative.DeltaRule;
import pb.Meters;
import pb.Utils;
import pb.file.FileReader;
import pb.results.Result;
import pb.statistics.ChunkStatistics;
import sun.misc.Regexp;
//import static java.lang.System.out;

import java.*;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.*;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
//import java.lang.*;
import java.util.stream.IntStream;

public class  Main
{

	public static void main(String[] args) throws IOException
	{
		Main main = new Main();
//		new Main().compareMMandPB();

		//new Main().showArray();
//		new Main().chunkTest();

		main.checkLowFrequencyNumberStrategy();
		//main.toJson();

	}

	public static void writeFile3() throws IOException {
		PrintWriter pw = new PrintWriter(new FileWriter("json.txt"));
	 
		for (int i = 0; i < 10; i++) {
			pw.write("something");
		}
	 
		pw.close();
	}
	
	private void toJson() throws IOException {
		Map<Date, List<Integer>> myMMTheFun = new FileReader().loadFromFile("/Users/rus/code/old/pb/pb_after_update_2015.txt");
		Map<Date, List<Integer>> aMap = new TreeMap<Date, List<Integer>>(new Comparator<Date>()
		{
			@Override
			public int compare(Date o1, Date o2)
			{
				return o1.compareTo(o2);
			}
		});
		aMap.putAll(myMMTheFun);
		//Collections.sort
		
		PrintWriter pw = new PrintWriter(new FileWriter("json.txt"));
	 
		// for (int i = 0; i < 10; i++) {
		// 	pw.write("something");
		// }
	 
		// pw.close();

		//System.out.println("[\n" );
		pw.write("[\n");
		for (Map.Entry<Date, List<Integer>> entry: aMap.entrySet()) {
			// System.out.println("\t{ \"date\" : '" + DateFormat.getDateInstance(DateFormat.SHORT).format(entry.getKey()) + "', \"nums\": " + entry.getValue().subList(0, 5) + " },");
			pw.write("\t{ \"date\" : '" + DateFormat.getDateInstance(DateFormat.SHORT).format(entry.getKey()) + "', \"nums\": " + entry.getValue().subList(0, 5) + " },\n");
		}
		pw.write("]");
		pw.flush();
		pw.close();
		// System.out.println("]");

	}

	private void checkLowFrequencyNumberStrategy() {
		System.out.println("hello!");
		Map<Date, List<Integer>>  myMMTheFun = new TreeMap<>(new Comparator<Date>()
		{
			@Override
			public int compare(Date o1, Date o2)
			{
				return o1.compareTo(o2);
			}
		});

		myMMTheFun.putAll(new FileReader().loadFromFile("/Users/rus/code/old/pb/pb_after_update_2015.txt"));
		
		int maxNum = 69;
		int gameTotalNumber = 5;
		int testBase = 15;
		

		Map<Integer, Integer> counterMap = new HashMap<>(); 
		for (int i = 1; i <= maxNum; i++) {
			counterMap.put(i, 0);
		}

		Map<Integer, Integer> resultIntersactionFrequencyMap = new TreeMap();
		IntStream.range(1, gameTotalNumber + 1).forEach(i -> resultIntersactionFrequencyMap.put(i, 0));
		

		int index = 0;
		List<List<Integer>> myMMTheFunList = new java.util.ArrayList<>(myMMTheFun.values());
		for (int n = 0; n < myMMTheFunList.size() - 1; n++) {	
			List<Integer> numbers = myMMTheFunList.get(n).subList(0, 5);
			
			System.out.println("\n- " + index + "\t game: \t" + numbers);
			

			if (testBase < index) {
				Map<Integer, Integer> sortedMap = com.utils.Utils.sortByValue(counterMap);

				List<Integer> leastFrequentNumbers = new java.util.ArrayList<>();

				List<Integer> frequencyList = new java.util.ArrayList<>(); 
				for (int num : numbers) {
					frequencyList.add(sortedMap.get(num));
				}

				System.out.print("\tFreq. Index: " + frequencyList);
				Collections.sort(frequencyList);
				System.out.println("\t\tsorted: " + frequencyList);

				List<Map.Entry<Integer, Integer>> sortedLeastFrequentPairList = new java.util.ArrayList<>(sortedMap.entrySet());
				for (int i = 0; i < sortedLeastFrequentPairList.size() - 1; i++) {
					leastFrequentNumbers.add(sortedLeastFrequentPairList.get(i).getKey());
					if (i >= gameTotalNumber && sortedLeastFrequentPairList.get(i).getValue() != sortedLeastFrequentPairList.get(i+1).getValue()) {
						System.out.println("\tLeast Frequent Map: \t\t" + sortedMap) ;
						System.out.println("\tLeast Frequent Numbers: \t" + leastFrequentNumbers) ;
						break;
					}
				}

				List<Integer> nextNumbers = myMMTheFunList.get(n+1).subList(0, 5);
				leastFrequentNumbers.retainAll(nextNumbers);
				int match = leastFrequentNumbers.size();
				if (match != 0) {
					resultIntersactionFrequencyMap.put(match, resultIntersactionFrequencyMap.get(match) + 1);
				}
				System.out.println("\t intersaction count: " + match + "; " + leastFrequentNumbers + "\n");
			}

			for (Integer number : numbers) {
				counterMap.put(number, counterMap.get(number) + 1);
			}
			index++;

		}
		

		int intersactionTotal = resultIntersactionFrequencyMap.values().stream().mapToInt(i -> i).sum();
		int gamesViewed = myMMTheFunList.size() - testBase;
		System.out.println("\n ================== \n\n ");
		System.out.println(" Games total: " + myMMTheFunList.size());
		System.out.println(" Games reviewed: " + gamesViewed);
		System.out.println(" Intersactions found in: " + intersactionTotal + " ~ " + ((intersactionTotal*100/gamesViewed)) + "%" );
		System.out.println(" Intersactions breakdown: " + resultIntersactionFrequencyMap);
	}


	private void chunkTest()
	{
		Integer[] array = { 2, 3, 4, 7, 8, 9, 11, 14, 15, 17, 18, 19, 21, 22, 23, 24, 25, 26, 27, 31, 32, 33, 34, 35, 37, 38, 43, 45, 46, 48, 49, 51, 52, 53, 55, 56, 57, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68, 69 };
		List<List<Integer>> aResList = getAllChunk(Arrays.asList(array), 5);
		for (List<Integer> aList : aResList)
		{
			System.out.println(aList);
		}

		System.out.println(aResList.size());

	}

	private void showArray()
	{
		Integer[] array2 = {
				  8,12,15,35,50,
				  7,33,39,52,55,
				  9,33,54,56,57,
				  2, 5,25,26,49,
				  13,19,21,28,49,
				  20,30,38,46,59,
				  14,22,27,32,49,
				  3,19,20,22,47,
				  2,31,35,40,42,
				  9,13,23,42,51,
				  9,12,35,41,51,
				  24,29,33,38,44,
				  13,17,35,45,50,
				  11,31,35,42,45
		};
		Set<Integer> aSet = new TreeSet<>(Arrays.asList(array2));
	}

	private void compareMMandPB()
	{
		Map<Date, List<Integer>> aResMap = new TreeMap<>();

		Map<Date, List<Integer>> myPBTheFun = new FileReader().loadFromFile("/Users/jet/Documents/pb/pb.txt");

		Map<Date, List<Integer>> myMMTheFun = new FileReader().loadFromFile("/Users/jet/Documents/pb/mm.txt");

		List<Date> aPbDates = new java.util.ArrayList<>(myPBTheFun.keySet());
//		List<Date> aMMDates = new java.util.ArrayList<>(myMMTheFun.keySet());
		myMMTheFun = Utils.replaceDates(aPbDates, myMMTheFun);
//		myMMTheFun = myMMTheFunDates;
//		Set<Date> aPbTreeSet = new TreeSet<>(myPBTheFun.keySet());
//		Set<Date> aMnTreeSet = new TreeSet<>(myMMTheFun.keySet());
		List<Date> aPbDate = new java.util.ArrayList<>(myPBTheFun.keySet());
		Map<Date, List<Integer>> aPbDer1 = DeltaRule.getDeltaMap(myPBTheFun);
		Map<Date, List<Integer>> aPbDer2 = DeltaRule.getDeltaMap(aPbDer1);
		Map<Date, List<Integer>> aPbDer3 = DeltaRule.getDeltaMap(aPbDer2);
		Map<Date, List<Integer>> aPbDer4 = DeltaRule.getDeltaMap(aPbDer3);

		List<Date> aMMDate = new java.util.ArrayList<>(myMMTheFun.keySet());
		Map<Date, List<Integer>> aMMDer1 = DeltaRule.getDeltaMap(myMMTheFun);
		Map<Date, List<Integer>> aMMDer2 = DeltaRule.getDeltaMap(aMMDer1);
		Map<Date, List<Integer>> aMMDer3 = DeltaRule.getDeltaMap(aMMDer2);
		Map<Date, List<Integer>> aMMDer4 = DeltaRule.getDeltaMap(aMMDer3);

		Map<Date, Integer> aGapMap = new TreeMap<>();
		int aGap = 0;

		for (int i = 0; i < aMMDate.size()-1; i++ )
		{
			System.out.printf("%1$4d  " + Meters.kOutDateFormat.format(aPbDate.get(i))+ " \t ", i );
			Utils.printf(myPBTheFun.get(aPbDate.get(i)).subList(0, 5), 2, ",", " | ");
			Utils.printf(aPbDer1.get(aPbDate.get(i)), 3, "'", " | ");
			Utils.printf(aPbDer2.get(aPbDate.get(i)), 4, "''", " | ");
			Utils.printf(aPbDer3.get(aPbDate.get(i)), 4, "'''", " | ");
			Utils.printf(aPbDer4.get(aPbDate.get(i)), 4, "`", " \t<|>\t ");

			Utils.printf(myMMTheFun.get(aMMDate.get(i)).subList(0, 5), 2, ",", " | ");
			Utils.printf(aMMDer1.get(aMMDate.get(i)), 3, "'", " | ");
			Utils.printf(aMMDer2.get(aMMDate.get(i)), 4, "''", " | ");
			Utils.printf(aMMDer3.get(aMMDate.get(i)), 4, "'''", " | ");
			Utils.printf(aMMDer4.get(aMMDate.get(i)), 4, "`", " |\n");
			//System.out.println();
			List<Integer> aList = Utils.getIntersection(myMMTheFun.get(aMMDate.get(i)).subList(0,5), myPBTheFun.get(aPbDate.get(i)).subList(0,5));
			if (!aList.isEmpty())
			{
				aGapMap.put(aPbDate.get(i), i - aGap - 1);
				aGap = i;
				aResMap.put(aPbDate.get(i), aList);
			}
		}
		System.out.println("compareMMandPB:\n \tfound: " + aResMap.size() + " out of " + aMMDate.size());

		List<Date> aList = new java.util.ArrayList<>(aResMap.keySet());

		for (int i = 1; i < aList.size(); i++)
		{
			Integer aInteger = aGapMap.get(aList.get(i-1));
			System.out.println( aInteger == null ? "\t" : " " + aInteger + " games no dubs, " + Meters.kOutDateFormat.format(aList.get(i)) + " " + aResMap.get(aList.get(i)));
		}

		System.out.println("\n\n" +
				  "\t\t ====================");
		System.out.println(	"\t\t ======= Main =======");
		System.out.println(	"\t\t ====================");
		ChunkStatistics.getAllChunkStats(Utils.get5SizeMap(myMMTheFun), Utils.get5SizeMap(myPBTheFun));

		System.out.println("\n\n" +
				  "\t\t ====================");
		System.out.println(	"\t\t ======= der1 =======");
		System.out.println(	"\t\t ====================");
		ChunkStatistics.getAllChunkStats(aMMDer1, aPbDer1);

		System.out.println("\n\n" +
				  "\t\t ====================");
		System.out.println(	"\t\t ======= der2 =======");
		System.out.println(	"\t\t ====================");
		ChunkStatistics.getAllChunkStats(aMMDer2, aPbDer2);

		System.out.println("\n\n" +
				  "\t\t ====================");
		System.out.println(	"\t\t ======= der3 =======");
		System.out.println(	"\t\t ====================");
		ChunkStatistics.getAllChunkStats(aMMDer3, aPbDer3);

		System.out.println("\n\n" +
				  "\t\t ====================");
		System.out.println(	"\t\t ======= der4 =======");
		System.out.println(	"\t\t ====================");
		ChunkStatistics.getAllChunkStats(aMMDer4, aPbDer4);


	}

	public static List<List<Integer>> getAllChunk(List<Integer> theSource, int theChunkSize)
	{
		long mask = 0;
		long limit = 1l << (long)theSource.size();
		List<List<Integer>> aResList = new java.util.ArrayList<>();
		while (mask < limit)
		{
			List<Integer> aIntegerList = new java.util.ArrayList<>();
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
				aResList.add(aIntegerList);
			}
			mask++;
		}
		return aResList;
	}


	private static void printPermutation(int mask, int[] array)
	{
		//System.out.print("(");
		List<Integer> aIntegerList = new java.util.ArrayList<>();
		for (int i = 0; i < array.length; i++) {
			//исли бит номер i еденичный, печатаем i-й элемент массива
			if ((mask & (1 << i)) != 0)
			{
				aIntegerList.add(array[i]);
				//System.out.print(array[i] + ",");
			}
		}
		//System.out.println(")");
	}

	{testBlock = 8999;}

	int o, a1 = o, yqw = o, testBlock;

	public Main()
	{
		//o = 0;
	}
	public Main(int d)
	{
		this();
	}

	public enum Season {
		summer(8), fall(9);
		int myInt = 0;

		Season(int i)
		{
			myInt = i;
		}

		public int getInt()
		{
			return myInt;
		}
	}
	Main myInh;
   int yz7$9;
	static int myInt = 3;
	//12
	//int k =0;
	int k =0;

	String reverse(String theString) throws Exception
	{
		if (theString.length() ==0 )
		{
			throw new Exception();
		}
		return theString;
	}

	//public static void main (String... args) throws Exception
	{
		//new Main().switch2();
		//new Main().regExTest();
		//new Main().stringBuilderTest();
		//new Main().resourceBundleTest();
		//new Main().numberFormatTest();
		//new Main().splitTest();
	}

	private void splitTest()
	{
		String s = "1 a2 b 3 c4d 5e";
		String[] aStrings = s.split("\\d");
		//String[] aStrings = s.split("");
		for (String a : aStrings)
		{
			System.out.print(">" + a + "< ");
		}

	}

	private void numberFormatTest()
	{
		String s = "987.123456";
		double d = 987.123456;
		NumberFormat aNumberFormat = NumberFormat.getInstance();
		aNumberFormat.setMaximumFractionDigits(5);
		aNumberFormat.setMinimumFractionDigits(10);
		aNumberFormat.setMinimumIntegerDigits(3);
		aNumberFormat.setMaximumIntegerDigits(2);
		System.out.println(aNumberFormat.format(d));

		System.out.println(aNumberFormat.format(d));
		try
		{
			System.out.println(aNumberFormat.parse(s));
		} catch (ParseException e)
		{


		}
	}

	private void regExTest()
	{
		Pattern aPattern = Pattern.compile("\\W");
		Matcher aMatcher = aPattern.matcher("^23 *$76 bc");
		System.out.print("out: ");
		while (aMatcher.find())
		{
			System.out.print(aMatcher.start() + " ");
		}
	}


	private void stringBuilderTest()
	{
		StringBuilder sb = new StringBuilder(8);
		System.out.print(sb.length() + " " + sb + " ");
		sb.insert(0, "abcdef");
		sb.append("789");
		System.out.println(sb.length() + " " + sb);
	}

	private void resourceBundleTest()
	{
		ResourceBundle rb = ResourceBundle.getBundle("Locale", Locale.getDefault());
		rb.getObject("123");
	}

	void switch2()
	{
		Integer i1 = 10;
		Integer i2 = 10;
		System.out.println("i1 == i2: " + (i1 == i2) + "; i1 != i2: " + (i1 != i2));
	}

	void test()
	{

		int o=0, a1=0b0, yqw=077665, testBlock= 1;
		if (testBlock == 1)
		{

		}
		byte aS = 10_0+27; aS = (byte)(0 - aS);
		byte aE = 3; aE = 9+9; aE+=9; aE = (byte) (aE + aS); aE+=128;
		float aFF  = 1.1f;
		double aD = 1_2_3.1_2_3;
		float aF = (float) 123.123;
		short aFFF = 90_0;
		//new Test().simpleMethod();
		Short aShort = new Short("9");
		if (9 == aShort)
		{
			System.out.println(" a short = 9; testBlock = " +new Main().testBlock);
		}

		ArrayList aArrayList = new ArrayList();
		ArrayList anObject = new ArrayList();
		anObject.testMe(new Main());
		int a [] = {1,2,3,4};
		int [] x = {1,2,3,4};
		{
			int myInt = 4;

			for (myInt = 1; ;)
			{
				break;
			}}
		//System.out.println(Season.summer.getInt());
//		System.out.println("Float.MAX_VALUE : " + Float.MAX_VALUE);
//		System.out.println("Double.MAX_VALUE : " + Double.MAX_VALUE);
//		throw new Exception();

		StringBuilder aBuilder = new StringBuilder(33);
		aBuilder.append("22");
		aBuilder.insert(2, "3");
		System.out.println(aBuilder);
	}
	class A{}
	class D2{}
	class C extends A{}
	interface I{}


	int k(int i)
	{
		A aA = new A();
		C aC = new C();
		D2 aD2 = new D2();
		if (aA instanceof Iterator)

		{

		}
		short as = -32688;
		int k = 9;
		//i++;
		char a = 65535;
		float j = -9.9f;
	   int []n[];
		return ++i;
	}

	int a;

	void test2( int b)
	{
		for (int a = 2; a < 3; a++)
		{}
	}


	void s (int a1)
	{
		double af = 5;
		float aFloat = 8_140_000_000.2342342423243f;

		//new OutOf().simple();
		new In().simple();
		int t;
		int r = t =8 ;
		a1++;
		for (; a1 < 3; a1++)
		{

		}
	}

	class In
	{
		private void simple(){};
	}
}

class OutOf
{
	private void simple(){};
}