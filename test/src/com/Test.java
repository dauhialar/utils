package com;

import ent.Example;

import java.io.Serializable;
import java.util.*;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

class BigOuther
{
	static class NestStat
	{
		public void hi()
		{
		}

		;
	}

	class Simple
	{

	}
}

/**
 * @author Ruslan Dauhiala
 */
public class Test extends Example implements IExample
{
	String blah = "4";
	final String a = "";

	final String kNums = "0011111111101100001001100010010001010111111111110110100110101101111010101110101001101001001100011011101011101110011111001100000100111010010111001111001100110000101110111010011100011010000001010000011010001111010010000100100111001100000110111111100010111101110000100001110011010010110100111001010010011011110101011101010011101010010100011110101101011100100110010111101001011010000111010111101010101100100011111101101000011111110101001000101110101011011011100110011111000110000101001101101110000101110101110100010001100011101001011000010101000010110101010001000001110100000100101110101000011011101100011010001001100110010110110100111000001100111000001011110100001101111110100101100010000000100000100100001100001010111110100000100111011010101000000111111100011101100011001000010011110011100000101100111100111101001011000001010110101000111100011001100001001111110000011001101001101011111100110110110010001011010001101011000101111100011110110101101000010101101011011101111010101100111011111010111110101110110101101000110111111011110000111101000010010101010011110101001001111001010011101100100011000110011110110100001111010000101101001110100101011100111101100110001010001101011101010110011100110010101011000000110110100010001000111011000111000100011100010110011111110001001010111010011010011000010100111100100011011111010001110010101110110110100011110100100110011000111111100100001101011001010000001101011000001011111001011011010000100010110001011001011001001001101110110001011101101101100110000001101001101111111100100101010011011101010111010100111000101011101110111000011011001111001100011100110011000011010011011110011000010011111011010011111001000101111000000111100010001110000001001101011010011011000000000111110100010000110001011101011001000110111011101101010110111101111100110100010111110100101110110000111001101100010000100000101001011100011101111100001101100001000110000011000000100000001111000101101010100011110000101010000111111000110101101101101010011111100010101001110000110101110010101010010001001011010100001";

	public void m()
	{
//		//a5 = 5;
//		String a ="ind";
//		//an+="k";
//		//a+="k";
//		Test aExample = new Test();
//		aExample.i++;
//		//aExample.simpleMethod();
//
//		Example ex = new Test();
//		//ex.i++;

	}

	static class A implements AutoCloseable
	{
		public void close() throws Exception
		{
			throw new Exception("catch");
		}
	}

	private static void method() throws Exception
	{
		try (A a = new A())
		{
			throw new Exception("try");
		} finally
		{
//			throw new Exception("finally");
		}


	}


	class Simple<T extends Serializable>
	{
		java.util.ArrayList<? extends String> aS = new ArrayList<String>();
		T hi;
	}

	static public class B2
	{
		public void b2m()
		{
		}

		;
	}

	public static void main(String[] args)
	{

		//new Test().fire();
		//System.out.format("%s", new Double("123"));
		try
		{
			method();
		} catch (Exception e)
		{
			System.out.println(e.getMessage());
			System.out.println(e.getSuppressed()[0]);
		}
		Runnable aS2 = new Runnable()
		{
			@Override
			public void run()
			{

			}
		};
		aS2.run();

		Serializable aS = new Serializable()
		{
		};

		BigOuther.NestStat aN = new BigOuther.NestStat();
		BigOuther.Simple aSimple = new BigOuther().new Simple();
		synchronized (Test.class)
		{

		}


		AtomicInteger anInt = new AtomicInteger();
		anInt.set(0);
		System.out.println("orig:" + anInt.get());

		int aGetAndInc = anInt.getAndIncrement();
		System.out.println("aGetAndInc: " + aGetAndInc + "; orig:" + anInt.get());

		int aIncAndGet = anInt.incrementAndGet();
		System.out.println("aIncAndGet: " + aIncAndGet + "; orig:" + anInt.get());

		boolean aB = anInt.compareAndSet(400, 555);
		System.out.println("compare and set 400 - 500: " + aB + " orig:" + anInt.get());

		aB = anInt.compareAndSet(2, 555);
		System.out.println("compare and set   2 - 500: " + aB + " orig:" + anInt.get());


		ReentrantLock aReentrantLock = new ReentrantLock();
		aReentrantLock.lock();
		aReentrantLock.lock();
		aReentrantLock.unlock();
		boolean aLocked = false;
		try
		{
			aLocked = aReentrantLock.tryLock(12, TimeUnit.SECONDS);
			System.out.println("aLocked: " + aLocked);
		} catch (InterruptedException e)
		{

		} finally
		{
			if (aLocked)
			{
				int i = 0;
				aReentrantLock.unlock();
				System.out.println("unlock: " + ++i);
				aReentrantLock.unlock();
				System.out.println("unlock: " + ++i);
				aReentrantLock.unlock();
				System.out.println("unlock: " + ++i);
			}
		}
	}


	private void fire()
	{
		HashMap<Integer, Integer> a0Map = new HashMap<>();
		HashMap<Integer, Integer> a1Map = new HashMap<>();
		//2-10 total: 1016 OK:516; fail: 500
		//3-5 total: 1016 OK:503; fail: 513
		//5-10 total: 1016 OK:490; fail: 526
		//5-15 total: 1016 OK:493; fail: 523
		//5-20 total: 1016 OK:493; fail: 523
		//2-7 total: 1016 OK:519; fail: 497
		//2-25 total: 1016 OK:520; fail: 496; equal: 0
		//6-10 total: 1016 OK:493; fail: 523; equal: 53
		//6-15 total: 1016 OK:485; fail: 531; equal: 28
		//6-20 total: 1016 OK:485; fail: 531; equal: 26

		//1-2 total: 1016 OK:498; fail: 518; equal: 0
		//1-3 total: 1016 OK:512; fail: 504; equal: 0
		//1-4 total: 1016 OK:529; fail: 487; equal: 0    b!
		//1-5 total: 1016 OK:521; fail: 495; equal: 0

		//2-3 total: 1016 OK:508; fail: 508; equal: 0
		//2-4 total: 1016 OK:529; fail: 487; equal: 0    b!
		//2-5 total: 1016 OK:518; fail: 498; equal: 0

		//3-4 total: 1016 OK:512; fail: 504; equal: 50
		//3-5 total: 1016 OK:503; fail: 513; equal: 18
		//4-5 total: 1016 OK:477; fail: 539; equal: 83
		int aMinChunk = 5;
		int aMaxChunk = 10;

		for (int i = kNums.length() - 1; i > 1000; i--)
		{
			a0Map.put(i, 0);
			a1Map.put(i, 0);
			for (int n = aMinChunk; n < aMaxChunk; n++)
			{
				String aChunk = kNums.substring(i - n, i);
				int aLastInd = 0;
				while (aLastInd >= 0 && aLastInd + n < i)
				{
					aLastInd = kNums.indexOf(aChunk, aLastInd);
					if (aLastInd >= 0 && aLastInd + n < i)
					{
						char aNextNum = kNums.charAt(aLastInd + aChunk.length());
						if (aNextNum == '0')
						{
							a0Map.put(i, a0Map.get(i) + 1);
						} else
						{
							a1Map.put(i, a1Map.get(i) + 1);
						}
						aLastInd++;
					}
				}
			}
		}

		int aOkNum = 0;
		int aE = 0;
		for (Map.Entry<Integer, Integer> aEntry : a0Map.entrySet())
		{
			char aTrueChar = kNums.charAt(aEntry.getKey());
			boolean aZero = aEntry.getValue() > a1Map.get(aEntry.getKey());
			boolean isOk = (aTrueChar == '1' ^ aZero);
			if (isOk)
			{
				aOkNum++;
			}
			if (aEntry.getValue() == a1Map.get(aEntry.getKey()))
			{
				aE++;
			}
			System.out.println(aEntry.getKey() + " \t\t " +
					  aTrueChar + " \t\t " +
					  (isOk ? "YES!" : "NO  ") + " \t\t " +
					  "0: " + aEntry.getValue() + " \t\t 1: " + a1Map.get(aEntry.getKey()) + "" +
					  (aEntry.getValue() == a1Map.get(aEntry.getKey()) ? " E" : ""));
		}
		System.out.println("\n//" + aMinChunk + "-" + aMaxChunk + " total: " + a0Map.size() + " OK:" + aOkNum + "; fail: " + (a0Map.size() - aOkNum) + "; equal: " + aE);
	}

}

