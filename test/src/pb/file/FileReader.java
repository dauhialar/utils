package pb.file;

import jdk.nashorn.internal.parser.DateParser;
import pb.Meters;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.*;

/**
 * @author Ruslan Dauhiala
 */
public class FileReader
{

	public static Map<Date, List<Integer>> loadFromFile(String thePath)
	{
		Map<Date, List<Integer>> aMap = new TreeMap<Date, List<Integer>>(new Comparator<Date>()
		{
			@Override
			public int compare(Date o1, Date o2)
			{
				return o2.compareTo(o1);
			}
		})
			//;
		{{
//			put(new Date(System.currentTimeMillis()), new ArrayList<Integer>(){{
//				add(0);
//				add(0);
//				add(0);
//				add(0);
//				add(0);
//				add(0);
//			}});
		}};

		Path aFile = Paths.get(thePath);
		try (
				  java.io.FileReader aFileReader = new java.io.FileReader(aFile.toFile());
				  BufferedReader aBufferedReader = new BufferedReader(aFileReader)
		)
		{
			String aLine;
			//Pattern aPattern = Pattern.compile(pattern);

			while((aLine = aBufferedReader.readLine()) != null)
			{
				if ("".equals(aLine.trim()))
				{
					continue;
				}
//				Matcher aMatcher = aPattern.matcher(aLine);
				List<Integer> aList = new ArrayList<>();
				String[] anArr = aLine.split("\\s+");
				String aDateStr = anArr[0];
				Date aDate = Meters.kFileDateFormat.parse(aDateStr);
				for (int i = 0; i < 6; i++)
				{
					aList.add(Integer.parseInt(anArr[i + 1].trim()));
				}
				//myAllTheFun.put(aReverceDateFormat.format(aDate), aList);
				aMap.put(aDate, aList);
			}
		} catch (IOException | ParseException e)
		{
			e.printStackTrace();
		}
		//Collections.sort(myAllTheFun.keySet());
		System.out.println("size: " + aMap.size() + " \n");
		//testFileLoad(aMap);
		return aMap;
	}


	private static void testFileLoad(Map<Date, List<Integer>> theMap)
	{
		Set<Date> aSortDate = new TreeSet<>(theMap.keySet());
		//Collections.sort(aSortDate);
		for (Map.Entry aEntry : theMap.entrySet())
		{
			System.out.println(aEntry.getKey() + "\t\t" + aEntry.getValue());
		}

		System.out.println(theMap.keySet().toString().replace(",", " "));
		String aD ="";
//		for (String aDateS : myAllTheFun.keySet())
//		{
//			aD = aDateS + " " + aD;
//		}
		System.out.println(aD);
		int i = 1;
		for (List aList : theMap.values())
		{
			System.out.println(i++ + ": ");
			System.out.println(aList.toString().replace(",", " "));
		}

		for (int j = 0; j < 6; j++)
		{
			System.out.println("\n" + (j + 1) + ": ");
			String aRes = "";
			for (List<Integer> aList : theMap.values())
			{
				aRes = aList.get(j) + " " + aRes;
			}
			System.out.println(aRes);
		}

		//System.out.println("\n" + (j + 1) + ": ");
	}


	public static void main(String args[])
	{
		Map<Date, List<Integer>> aMap = new TreeMap<Date, List<Integer>>(new Comparator<Date>()
		{
			@Override
			public int compare(Date o1, Date o2)
			{
				return o2.compareTo(o1);
			}
		});

		Path aFile = Paths.get("/Users/jet/work/numbers/after.txt");

		Path anOut = Paths.get("/Users/jet/work/numbers/out.txt");
		try (
				  java.io.FileReader aFileReader = new java.io.FileReader(aFile.toFile());
				  BufferedReader aBufferedReader = new BufferedReader(aFileReader);
				  )
		{
//			java.io.FileReader aFileReader = new java.io.FileReader(aFile.toFile());
//			BufferedReader aBufferedReader = new BufferedReader(aFileReader);
			String aLine;
			//Pattern aPattern = Pattern.compile(pattern);

			int aLineNum = 0;
			Date aDate = null;
			List<Integer> aGame = new ArrayList<>();
			while((aLine = aBufferedReader.readLine()) != null)
			{
				if ("".equals(aLine.trim()))
				{
					continue;
				}
				int aPoint = aLineNum % 8;

				switch (aPoint)
				{
					case 0:
						aDate = Meters.kNewFileDateFormat.parse(aLine);
						aLineNum++;
						break;
					case 7:
						aLineNum = 0;
						if(aGame.size()!=6)
						{
							throw new RuntimeException();
						}
						aMap.put(aDate, aGame);
						aGame = new ArrayList<>();
						break;
					default:
						aGame.add(Integer.parseInt(aLine));
						aLineNum++;
				}
			}

		} catch (IOException | ParseException e)
		{
			e.printStackTrace();
		}
		//Collections.sort(myAllTheFun.keySet());
		System.out.println("size: " + aMap.size() + " \n");
		for (Map.Entry<Date, List<Integer>> aEntry : aMap.entrySet())
		{
			System.out.println(Meters.kFileDateFormat.format(aEntry.getKey()) + "\t" +
					  aEntry.getValue().toString().replace("[", "").replace("]", "").replace(","," "));
		}
		//testFileLoad(aMap);

		//return aMap;

	}
}
