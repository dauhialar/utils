package com.cashflow.rules.vertical;

import com.cashflow.rules.cofig.IConfig;

/**
 * @author Ruslan Dauhiala
 */
public class DefaultConfig implements IConfig
{

	@Override
	public boolean hasNext()
	{
		return false;
	}

	@Override
	public String toStr()
	{
		return  ""
				  //" \t C > "
				  //+ "\t games: " + kGamesNum
				  ;
	}

	public void reset()
	{

	}
}
