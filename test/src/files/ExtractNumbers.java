package files;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * @author Ruslan Dauhiala
 */
public class ExtractNumbers
{
	private static String kPref = "Pi";
	private  Path myPiPath = Paths.get("/Users/jet/work/pi/1M/pi 1m.txt");
	//private  Path myPiPath = Paths.get("/Users/jet/work/pi/1B/pi-billion.txt");
	Map<Integer, Long> myLastIndMap = new HashMap<>();

	public static void main(String[] args)
	{
		new ExtractNumbers().go();
		//new ExtractNumbers().test();


	}

	private void test()
	{


		try (FileReader aFileReader = new FileReader(Paths.get(myPiPath.getParent().toString(), "Pi-dif.txt").toString());
			  BufferedReader aBufferedReader = new BufferedReader(aFileReader);
			  Scanner aScanner = new Scanner(aBufferedReader) )
		{
			int aMax = 2000;
			int aInd = aMax;
			while (aScanner.hasNext() && aInd-- > 0 )
			{
				if (aScanner.hasNextInt())
				{
					System.out.println(aScanner.nextInt() + " ") ;
				} else
				{
					aScanner.next();
				}

			}
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	private void go()
	{
		Map<Integer, Writer> aNumGapWriter = new HashMap<>();
		BufferedWriter aDeltaWriter = null;
		BufferedReader aReader = null;
		try
		{
			File aDeltaFile = new File(myPiPath.getParent().toFile(),  kPref + "-odd-even.txt");
			Files.deleteIfExists(aDeltaFile.toPath());
			aDeltaWriter = new BufferedWriter(new FileWriter(aDeltaFile));
//101001101100101011011000010010
//111001010101001000100101101010
//010101101001000100101010100111
// 1010101001000100101101010
			//aDeltaWriter = new BufferedWriter(new FileWriter(aDeltaFile));
//			for (int i = 0; i < 10; i++)
//			{
//				File aNextPosFile = new File(myPiPath.getParent().toFile(), kPref + i + "-next.txt");
//				Files.deleteIfExists(aNextPosFile.toPath());
//				BufferedWriter aWriter =  new BufferedWriter(new FileWriter(aNextPosFile));
//				aNumGapWriter.put(i, aWriter);
//				myLastIndMap.put(i, 0l);
//			}
			int aPrev = -1;
			long aPointer = 0l;
			long aTime = System.currentTimeMillis();


			aReader = new BufferedReader(new FileReader(myPiPath.toFile()));
			int aNum;
			aReader.read(); // 3
			aReader.read(); // .
			while((aNum = aReader.read()) != -1)
			{
				aNum = aNum - '0';
				aDeltaWriter.write(Integer.toString(aNum % 2));

//				if (aPrev != -1)
//				{
//					if (aPrev < aNum)
//					{
//						aPrev += 10;
//					}
//					int aDelta = Math.abs(aPrev-aNum);
//					aDeltaWriter.write(Integer.toString(aDelta));
//				}
//				aPrev = aNum;
//
//				if (myLastIndMap.get(aNum) != null && myLastIndMap.get(aNum) > 0)
//				{
//					aNumGapWriter.get(aNum).write(aPointer - myLastIndMap.get(aNum) + " ");
//				}
//				myLastIndMap.put(aNum, aPointer);
				aPointer++;
				if (aPointer == 10_000)
				{
					System.out.println("10  \tk...");
				} else if (aPointer == 100_000)
				{
					System.out.println("100 \tk...");
				} else if (aPointer == 1_000_000)
				{
					long a1Mtime = (System.currentTimeMillis() - aTime) ;
					System.out.println("1   \tM... time: " + a1Mtime + "(" + (a1Mtime / 1000) + " sec.); ETA: " + (a1Mtime / 60) + " min.");
				} else if (aPointer == 10_000_000)
				{
					long a1Mtime = (System.currentTimeMillis() - aTime) ;
					System.out.println("10   \tM... time: " + a1Mtime + "(" + (a1Mtime / 1000 ) + " sec.); ");
				} else if (aPointer == 100_000_000)
				{
					long a1Mtime = (System.currentTimeMillis() - aTime) ;
					System.out.println("100  \tM... time: " + a1Mtime + "(" + (a1Mtime / 1000 ) + " sec.); ");
				} else if (aPointer == 1_000_000_000)
				{
					long a1Mtime = (System.currentTimeMillis() - aTime) ;
					System.out.println("1000 \tM... time: " + a1Mtime + "(" + (a1Mtime / 1000 / 60) + " min.); ");
				}
			}
		} catch (IOException e)
		{
			e.printStackTrace();
		} finally
		{
			System.out.println("\n...finally!");
			try
			{
				for (Writer aWriter : aNumGapWriter.values())
				{
					aWriter.flush();
					aWriter.close();
				}
				aDeltaWriter.flush();
				aDeltaWriter.close();
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
		System.out.println("\nDone!");
	}
}
