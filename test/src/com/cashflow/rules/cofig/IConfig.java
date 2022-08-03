package com.cashflow.rules.cofig;

import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Ruslan Dauhiala
 */
public interface IConfig
{
	boolean kDetailedMode =
			  true;
			  //false;

	int kGamesNum =
			  // 10;
			  // 5;
			  //3;
			  1;

	boolean kUseMM =
			  false;
	//true;

	boolean kFilePrint = false;

	String kParentFolderPath = "/Users/jet/Documents/pb/";

	String kOutFilePath = Paths.get(
			  kParentFolderPath,
			  "out",
			  new SimpleDateFormat("MM-dd HH:mm:ss").format(new Date()) + ".txt").toString();

	boolean hasNext();

	String toStr();

	void reset();
}
