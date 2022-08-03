package uscis;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

// IT WAS REPORTED TO US THAT YOUR IP ADDRESS OR INTERNET GATEWAY HAS BEEN LOCKED OUT FOR A SELECT PERIOD OF TIME. THIS IS DUE TO AN UNUSUALLY HIGH RATE OF USE. IN ORDER TO AVOID THIS ISSUE, PLEASE CREATE A CUSTOMER ACCOUNT (SINGLE APPLICANT) OR A REPRESENTATIVE ACCOUNT (REPRESENTING MANY INDIVIDUALS).
/**
 * @author Ruslan Dauhiala
 */
public class CheckStatus
{

	String kCase =
			  "I-131";
			  //"I-485";

	String kMyId = "LIN1890494135";

	// last 		LIN1890458571
	int kMy = 	        56174 ;
	int kStart = 54669;
	int kEnd = kMy + 1_300;

	static String kPref =  "LIN19904";
	DateFormat kParseDateFormat = new SimpleDateFormat("MMMM dd, yyyy");
	DateFormat kWriteDateFormat = new SimpleDateFormat("yyyy-MM-dd");

	Path myOutPath = Paths.get("/Users/jet/work/test/src/uscis", kCase, kWriteDateFormat.format(new Date()) + ".csv");

	private static final String kUrl = "https://egov.uscis.gov/casestatus/mycasestatus.do?appReceiptNum=";
			  //+ kPref; //94135";
	// adp https://egov.uscis.gov/casestatus/mycasestatus.do?appReceiptNum=LIN1990456174

	private void grab131Ids()
	{
		List<String> anIdList = new ArrayList<>();
		for (int anId = kStart; anId < kEnd; anId++)
		{
			Document doc = null;
			try
			{
				doc = Jsoup.connect(kUrl + kPref + anId).get();
				Thread.sleep(200);
			} catch (Exception anExc)
			{
				System.out.println(anId + ": " + anExc);
				continue;
			}
			Elements aStatusElements = doc.select(".rows.text-center");
			if (aStatusElements.size() == 0)
			{
				System.out.println("\n\n>>> BLOCKED ON " + (anId-kStart) + " :|");
				//	IT WAS REPORTED TO US THAT YOUR IP ADDRESS
				break;
			}
			for (Element headline : aStatusElements)
			{
				Elements aP = headline.select("p");
				if (aP != null && aP.text() != null)
				{
					String aText = aP.text();
					if (aText != null && aText.contains("I-131"))
					{
						//System.out.println(" - " + kPref + i);
						System.out.println(" - " + anId);
						anIdList.add(kPref+anId);
					}
				}
			}
			if ( (anId-kStart) % 100 == 0)
			{
				System.out.println( " " + (anId-kStart) + "...");
			}
		}

		try  (BufferedOutputStream aStream = new BufferedOutputStream(
				  new FileOutputStream(new File("/Users/jet/work/test/src/uscis/ids I-131 next.txt"))))
		{
			for (String aId : anIdList)
			{
				aStream.write((aId + "\n").getBytes());
			}
		} catch ( IOException e)
		{
			e.printStackTrace();
		}

	}

	public static void main(String args[])
	{
		new CheckStatus().check();
		//new CheckStatus().test();
		//new CheckStatus().getIds();

		//new CheckStatus().grab131Ids();

	}

	private void getIds()
	{
		Set<String> aSortedSet = new TreeSet<>();

		try (DirectoryStream<Path> aPaths = Files.newDirectoryStream(Paths.get("/Users/jet/work/test/src/uscis")))
		{
			for (Path aPath : aPaths)
			{
				if (aPath.toString().endsWith(".csv"))
				{
					try (Scanner aScanner = new Scanner(aPath.toFile()))
					{
						while (aScanner.hasNextLine())
						{
							String aNextLine = aScanner.nextLine();
							if (aNextLine.contains(","))
							{
								String aId = aNextLine.split(",")[0].trim();
								if (aId.startsWith("LIN"))
								{
									aSortedSet.add(aId);
								}
							}
						}
					} catch (FileNotFoundException e)
					{
						e.printStackTrace();
					}
				}
			}
		} catch (IOException e)
		{
			e.printStackTrace();
		}


		try  (BufferedOutputStream aStream = new BufferedOutputStream(new FileOutputStream(new File("/Users/jet/work/test/src/uscis/ids I-485.txt"))))
		{
			for (String aId : aSortedSet)
			{
				aStream.write((aId + "\n").getBytes());
			}
		} catch ( IOException e)
		{
			e.printStackTrace();
		}

	}

	private void test()
	{
		System.out.format("%4d%%  (%4d)  - %s\n", 12, 123, "here we check");
		System.out.format("%4d%%  (%4d)  - %s\n", 2, 2, "just 2");
		System.out.format("%4d%%  (%4d)  - %s\n", 100, 999, "easy!");
	}

	private void check()
	{

		Set<String> anIds = new TreeSet<>();
		try (Scanner aScanner = new Scanner(Paths.get("/Users/jet/work/test/src/uscis/ids " + kCase + ".txt").toFile()))
		{
			while (aScanner.hasNextLine())
			{
				String aNextLine = aScanner.nextLine();
				if (aNextLine.startsWith("LIN"))
				{
					anIds.add(aNextLine.trim());
				}
			}
		} catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}

		try  (BufferedOutputStream aStream = new BufferedOutputStream(new FileOutputStream(myOutPath.toFile())))
		{
			Map<String, Integer> aTotalMap = new HashMap<>();
//			int i = kMy;
			int aC = 0;
			int aCaseNum = 0;
//			for (int i = kStart;
//				  i < kEnd;
//				  i++)
			List<String> aStrings = new ArrayList<>(anIds);
			HashMap<String, Integer> aRepeatMap = new HashMap<>();
			for (int ind = 0; ind < aStrings.size(); ind++)
			{
				String anId = aStrings.get(ind);
				Thread.sleep(300);
				//Document doc = Jsoup.connect(kUrl + String.format("%1$5d", i)).get();
				Document doc = null;
				try
				{
					doc = Jsoup.connect(kUrl + anId).get();
				} catch (Exception anExc)
				{
					System.out.println(anId + ": " + anExc);
					continue;
				}
//				System.out.println(doc.title());
				Elements aStatusElements = doc.select(".rows.text-center");
				if (aStatusElements.size() == 0)
				{
					System.out.println("\n\n>>> BLOCKED ON " + aC + " :|");
					//	IT WAS REPORTED TO US THAT YOUR IP ADDRESS
					break;
				}
				for (Element headline : aStatusElements)
				{
					Elements aP = headline.select("p");
					if (aP != null && aP.text() != null)
					{
						String aText = aP.text();
						if (aText != null
								  && aText.contains(kCase)
								  )
						{
							//System.out.println(" - " + kPref + i);
							System.out.println(" - " + anId);
							String[] anArr = aText.split(",");
							if (anArr.length < 2)
							{

								Integer aV = aRepeatMap.get(anId);
								if (aV == null)
								{
									aV = 0;
								}
								aV += 1;
								if (aV < 3)
								{
									ind--;
									aRepeatMap.put(anId, aV);
								}
								continue;
							}
							String aStringDate = anArr[0].replace("On ", "").replace("As of ", "") + ", " + anArr[1].trim();
							Date aDate;
							String aOutDate = "";
							try
							{
								aDate = kParseDateFormat.parse(aStringDate);
								aOutDate = kWriteDateFormat.format(aDate);
							} catch (ParseException e)
							{
								e.printStackTrace();
								//aDate = new Date();
								aOutDate = "2019-08-xx";
							}
							try
							{

								String aMessage = aStatusElements.select("h1").first().text();
//								String anOutString = kPref + i + ", " + aOutDate + ", " + aMessage + "\n";
//								if (i == kMy)
								String anOutString = anId + ", " + aOutDate + ", " + aMessage + "\n";
								if (kMyId.equals(anId))
								{
									anOutString = "\n\n" + anOutString + "\n\n";
								}
								aStream.write(anOutString.getBytes());
								increment(aMessage, aTotalMap);
								aCaseNum++;
							} catch (Exception e)
							{
								e.printStackTrace();
							}
						}
					} else
					{
						System.out.println("unknown: " + anId);
					}
				}
				if (++aC %100 == 0)
				{
					System.out.println("\t..." + aC);
				}
			}

			aStream.write("\n\n ================================================== \n\n".getBytes());
			aStream.write(("\n Total "+ kCase+ " apps:  " + aCaseNum + "\n\n").getBytes());

			List<Map.Entry<String, Integer>> aList = new ArrayList<>(aTotalMap.entrySet());
			aList.sort(Map.Entry.comparingByValue());

			for (Map.Entry<String, Integer> anEntry : aList)
			{
				int aPercent = (int)(((double)anEntry.getValue() / aCaseNum) * 100);
				String aS = String.format("%4d%%  (%4d)  - %s\n", aPercent, anEntry.getValue(), anEntry.getKey());
				aStream.write(aS.getBytes());
			}
		} catch (IOException | InterruptedException e)
		{
			e.printStackTrace();
		}
	}

	private void increment(String theMessage, Map<String, Integer> theTotalMap)
	{
		Integer anInt = theTotalMap.get(theMessage);
		theTotalMap.put(theMessage, anInt == null ? 1 : ++anInt);
	}
}