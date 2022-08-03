package main;

import pb.Chunk;
import pb.Meters;
import pb.Utils;
import pb.file.FileReader;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * @author Ruslan Dauhiala
 */
public class Combination {

	public static void combination(int[] elements, int K, GamePrint theGamePrint)
	{
		int myCol=0;

		// get the length of the array
		// e.g. for {'A','B','C','D'} => N = 4
		int N = elements.length;

		if(K > N){
			System.out.println("Invalid input, K > N");
			return;
		}
		// calculate the possible combinations
		// e.g. c(4,2)
		//c(N,K);

		// get the combination by index
		// e.g. 01 --> AB , 23 --> CD
		int combination[] = new int[K];

		// position of current index
		//  if (r = 1)              r*
		//  index ==>        0   |   1   |   2
		//  element ==>      A   |   B   |   C
		int r = 0;
		int index = 0;

		while(r >= 0){
			// possible indexes for 1st position "r=0" are "0,1,2" --> "A,B,C"
			// possible indexes for 2nd position "r=1" are "1,2,3" --> "B,C,D"

			// for r = 0 ==> index < (4+ (0 - 2)) = 2
			if(index <= (N + (r - K))){
				combination[r] = index;

				// if we are at the last position print and increase the index
				if(r == K-1){

					//do something with the combination e.g. add to list or print

					List<Integer> aList = new ArrayList<>();
					for(int i : combination)
					{
						aList.add(elements[i]);
					}
					theGamePrint.print(aList);
					//print(myBufferedWriter, combination, elements);
					myCol++;

					index++;
				}
				else{
					// select index for next position
					index = combination[r]+1;
					r++;
				}
			}
			else{
				r--;
				if(r > 0)
					index = combination[r]+1;
				else
					index = combination[0]+1;
			}
		}
//		System.out.println("total:" + myCol);
	}

	private void applyCunksRule() throws IOException
	{
		Map<Date, List<Integer>> myAllTheFun = new FileReader().loadFromFile("/Users/jet/Documents/pb/pb.txt");
		List<Date> aDates = new ArrayList<>(myAllTheFun.keySet());

		List<List<Integer>> anAllChunks = Utils.getAllTheChunksTotal(2, myAllTheFun, Utils.kListConfig);
		System.out.println("Chunks size: " + anAllChunks.size() + "\n" + anAllChunks);
//		List<List<Integer>> anAllChunks = getAllChunks(myAllTheFun, 4, 2, 10);
//		anAllChunks.addAll(getAllChunks(myAllTheFun, 4, 3, 10));
//		anAllChunks.addAll(getAllChunks(myAllTheFun, 4, 4, 500));

		try (BufferedWriter aBufferedWriter = new BufferedWriter(new FileWriter(new File("/Users/jet/Documents/pb/" + Meters.kFileNameDateFormat.format(aDates.get(2)) + ".txt"))))
		{
			File aSource = new File("/Users/jet/Documents/pb/combination.txt");
			int aTotal = 0;
			int aSkip = 0;
			try (BufferedReader br = new BufferedReader(new java.io.FileReader(aSource)))
			{
				String aLine;
				while ((aLine = br.readLine()) != null)
				{
					if ("".equals(aLine))
					{
						continue;
					}
					String[] aNums = aLine.split("  ");
					List<Integer> aGame = new ArrayList<>();
					for (String aNum : aNums)
					{
						aGame.add(Integer.parseInt(aNum.trim()));
					}
					boolean aFound = false;
					for (List<Integer> aChunkGame : anAllChunks)
					{
						if (aGame.containsAll(aChunkGame))
						{
							aFound = true;
							aSkip++;
							break;
						}
					}
					if (!aFound)
					{
						aTotal ++;
						String aStr = aGame.toString();
						aBufferedWriter.write(aStr.substring(1, aStr.length() - 1).replace(",", " "));
						aBufferedWriter.newLine();
					}
				}
			}

			aBufferedWriter.flush();

			System.out.println("a total number of all variants: " + aTotal + "; skipped : " + aSkip);
		}

	}

	public static void main(String[] args) throws IOException
	{
		//new Combination().applyCunksRule();
		//new Combination().writeAllToFile();
		new Combination().writeAllToMemoryTest();
	}

	public List<List<Integer>> getList()
	{
		final List<List<Integer>> aLists = new ArrayList<>();
		Combination.combination(getAll69(), 5, new GamePrint()
		{
			@Override
			public void print(List<Integer> theGame)
			{
				aLists.add(theGame);
			}
		});
		return aLists;
	}

	private void writeAllToMemoryTest() throws IOException
	{
		long aS = System.currentTimeMillis();
		final List<List<Integer>> aLists = new ArrayList<>();
		Combination.combination(getAll69(), 5, new GamePrint()
		{
			@Override
			public void print(List<Integer> theGame)
			{
				aLists.add(theGame);
			}
		});

		long aStep1 = System.currentTimeMillis();
		System.out.println("all comb are done: " + (aStep1 - aS));

		for (int i = 0; i < aLists.size(); i++)
		{
			List a = aLists.get(i);
		}
		long aEnd = System.currentTimeMillis();

		System.out.println("iterate time: " + (aEnd - aStep1));

		System.out.println("all tests total: " + (aEnd - aS));
	}


	private static List<List<Integer>> getAllChunks(Map<Date, List<Integer>> theMyAllTheFun, int theStartInd, int theChunkSize, int theGamesCount)
	{
		List<List<Integer>> aRes = new ArrayList<>();
		List<Date> aDateList = new ArrayList<>(theMyAllTheFun.keySet());
		for (int i = theStartInd; i < theStartInd + theGamesCount; i++)
		{
			aRes.addAll(Utils.getAllChunks(theMyAllTheFun.get(aDateList.get(i)).subList(0, 5), theChunkSize));
		}
		return aRes;
	}

	private void writeAllToFile() throws IOException
	{
		Path aPath = Paths.get("/Users/jet/Documents/pb/combination.txt");
		Files.deleteIfExists(aPath);
		final BufferedWriter myBufferedWriter = new BufferedWriter(new FileWriter(aPath.toFile())) ;

		Combination.combination(getAll69(), 5, new GamePrint()
		{
			@Override
			public void print(List<Integer> theGame)
			{
				try
				{
					myBufferedWriter.newLine();
					String aStr = theGame.toString();
					myBufferedWriter.write(aStr.substring(1, aStr.length() - 1).replace(",", " "));
				} catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		});

		myBufferedWriter.flush();

	}

	public static int[] getAll69()
	{
		return new int[] {
//				  aList.toArray()
				 //     3,                    9                             18,            20                 27,             31        34,                                            44                                     56,                                                    67
				 // 1, 2,   4, 5, 6, 7, 8   , 10, 11, 12, 13, 14, 15, 16, 17,     19,   21, 22, 23, 24, 25, 26,    28, 29, 30,    32, 33,    35, 36, 37, 38, 39, 40, 41, 42, 43,    45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55,  57, 58, 59, 60, 61, 62, 63, 64, 65, 66,     68, 69
			 /*all 69=>*/
				  1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68, 69
			  //1, 8, 9, 10, 11, 12, 13, 14, 15, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68
			  //,91, 92, 93, 95, 97, 98, 99, 911, 912, 914, 915, 916, 921, 922, 924, 926, 927, 930, 931, 932, 935, 937, 939, 940, 941, 942, 943, 944, 945, 48
		};
	}

	public interface GamePrint
	{
		void print(List<Integer> theGame);
	}

}