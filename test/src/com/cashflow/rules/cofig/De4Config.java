package com.cashflow.rules.cofig;

import com.cashflow.rules.vertical.DefaultConfig;

/**
 * @author Ruslan Dauhiala
 */
public class De4Config extends DefaultConfig
{
	// ERRORS: 	 6 (0.29)  		 Denied : 	 6613475	;  Accepted : 4625038 t	 time: 	 3; 	 total : 530 sec. #: [4, 5, 6, 7, 8, 18]
// 							 denied%: 	 0.59    	;  errors % : 0.30
/// ★ ★ ★ conf: D4 remove: = = =  -5-30-2-  = = =

	// ERRORS: 	 6 (0.31)  		 Denied : 	 6833559	;  Accepted : 4404954 t	 time: 	 3; 	 total : 605 sec. #: [4, 5, 6, 7, 8, 18]
// 							 denied%: 	 0.61    	;  errors % : 0.30
/// ★ ★ ★ conf: D4 remove: = = =  -5-31-2-  = = =

	//static int kFirstToRemove = 15;
	public static int kInitGap = 1;
	public static int kGap = kInitGap;
	public static int kMaxGap = 5;

	public static int kInitToRemove = 38;
	public static int kToRemove = kInitToRemove;
			// 25;
			// 30; // !!!
	public static int kMaxRemove =
			 kInitToRemove;
			 //31;
			 //35;
			 //40;

	public static boolean kInitOrder = true;
	public static boolean kInOrder = kInitOrder;
			  //false;    // -3-30-2
			  //true;     // -5-31-2-


	public static int kInitCheckNum = 1;
	public static int kClusterCheckNum = kInitCheckNum;
	public static int kMaxClusterCheckNum =
			  2;
			  //kInitCheckNum;

	public static double kDeniedRate = 0;

//	public static int kPartToStay = 10;
//	public static int kPartStep = 0;

	// ====
//	public boolean hasNext()
//	{
//		return ++kPartStep < (100 / kPartToStay);
//	}
//	public void reset()
//	{
//		kPartStep = 0;
//	}
//
//	public String toStr()
//	{
//		return super.toStr() +
//				  //" \t $ D4Rule(gap-remove-cluster) \t " +
//				  kPartStep + "-";
//	}

	// ====

	public boolean hasNext()
	{
		kGap = kGap
				    + 1;
				  //+ 2;

		if (kGap > kMaxGap)
		{
			kGap = kInitGap;
			if (++kToRemove > kMaxRemove)
			{
				kToRemove = kInitToRemove;
				if (++kClusterCheckNum > kMaxClusterCheckNum)
				{
					if (!(kInOrder ^ kInitOrder))
					{
						kGap = kInitGap;
						kToRemove = kInitToRemove;
						kClusterCheckNum = kInitCheckNum;
						kInOrder = !kInOrder;
					} else
					{
						return false;
					}
				}
			}
		}

//		kToRemove += 1;
//		if (kToRemove > kMaxRemove)
//		{
//			return false;
//		}

		return true;

	}

	public void reset()
	{
		kGap = kInitGap;
		kToRemove = kInitToRemove;
		kClusterCheckNum = kInitCheckNum;
		kInOrder = kInitOrder;
	}


	public String toStr()
	{
		return super.toStr() +
				  //" \t $ D4Rule(gap-remove-cluster) \t " +
				  kGap + "-" + kToRemove + "-" + kClusterCheckNum + "-" + (kInOrder ? "o" : "n");
	}

	// order = false;
	// 3 25
	//	3;//  ERRORS: 	 9 (0.04)
	// 4; // ERRORS: 	 8 (0.09)
	// 5;  // ERRORS: 	 7 (0.14)     denied%: 	 0.49    	;  errors % : 0.35
	// 6;  // ERRORS: 	 7 (0.14)
	// 7;   // ERRORS: 	 6 (0.19)   !!!!
	// 8;  / ERRORS: 	 11 (-0.06)
	// 3 40
	//2; // ERRORS: 	 15;
	//3; //// ERRORS: 	 17;  denied%: 	 0.78    	;  errors % : 0.85
	//4; // ERRORS: 	 14; denied%: 	 0.78    	;  errors % : 0.70
	//5; //  ERRORS: 	 15;  denied%: 	 0.78    	;  errors % : 0.75
	//4 25
	//2;  // ERRORS: 	 9 (0.04)
	//3; //ERRORS: 	 9 (0.04)
	//4;   // ERRORS: 	 11 (-0.06)
	//5; // ERRORS: 	 10 (-0.01)

	// 4 40
	//aNextChunkPos.size() / 2;
	//1; // ERRORS: 	 16;   denied%: 	 0.78    	;  errors % : 0.80
	//2; // ERRORS: 	 15;
	//3;   // ERRORS: 	 12;  denied%: 	 0.78    	;  errors % : 0.60
	//4; // ERRORS: 	 17; denied%: 	 0.78    	;  errors % : 0.85
	//6; //ERRORS: 	 15; denied%: 	 0.78    	;  errors % : 0.75
	//10;//ERRORS: 	 16;  denied%: 	 0.78    	;  errors % : 0.80
	// 5 25
	// 2; // ERRORS: 	 8 (0.09)
	// 3; // ERRORS: 	 8 (0.09)
	// 4; // ERRORS: 	 9 (0.04)
	//5; // ERRORS: 	 9 (0.04)
	// 6 40
	//1; 	//    ERRORS: 	 17; denied%: 	 0.78    	;  errors % : 0.85
	//2;//    ERRORS: 	 16; denied%: 	 0.78    	;  errors % : 0.80
	//3;   // ERRORS: 	 17; denied%: 	 0.78    	;  errors % : 0.85
	//4; // ERRORS: 	 16; denied%: 	 0.78    	;  errors % : 0.80
	//6; // ERRORS: 	 15; denied%: 	 0.78    	;  errors % : 0.75
	//10;// ERRORS: 	 16; denied%: 	 0.78    	;  errors % : 0.80
	// order = true
	// 2 26
	// 2; // ERRORS: 	 9 (0.06)
	// 4 26
	//  2; // ERRORS: 	 13 (-0.14)
	//  3; // ERRORS: 	 13 (-0.14)
	//	5 26
	//1; // ERRORS: 	 9 (0.06)
	//2; // ERRORS: 	 5 (0.26) !!! denied%: 	 0.51    	;  errors % : 0.25
	//3; // ERRORS: 	 6 (0.21)
	//4; // ERRORS: 	 7 (0.16)
	//5; // ERRORS: 	 9 (0.06)
	// 5 30
	// 2;   // ERRORS: 	 6 (0.29) !!! denied%: 	 0.59    	;  errors % : 0.30  Denied : 	 6613475	;  Accepted : 4625038 t	 time: 	 5; 	 total : 118 sec. #: [4, 5, 6, 7, 8, 18]
	//3;  // ERRORS: 	 9 (0.14)     denied%: 	 0.59    	;  errors % : 0.45
	//4;  // ERRORS: 	 7 (0.24)     denied%: 	 0.59    	;  errors % : 0.35
	//5;  // ERRORS: 	 10 (0.09)
	// 5 35
	//1;  // ERRORS:   12 (0.09)    denied%: 	 0.69    	;  errors % : 0.60
	//2;  // ERRORS: 	 10 (0.19)    denied%: 	 0.69    	;  errors % : 0.50
	//3;  // ERRORS: 	 11 (0.14)
	//4;  // ERRORS: 	 10 (0.19)    denied%: 	 0.69    	;  errors % : 0.50
	//5;  // ERRORS: 	 11 (0.14)
	//6;  // ERRORS: 	 13 (0.04)
	// 5 40
	//2;   // ERRORS: 	 13 (0.13)     denied%: 	 0.78    	;  errors % : 0.65
	// 3;  // ERRORS: 	 11 (0.23) !!! denied%: 	 0.78    	;  errors % : 0.55
	//4;   // ERRORS: 	 12 (0.18)     denied%: 	 0.78    	;  errors % : 0.60
	//5;   // ERRORS: 	 13 (0.13)
	//5 45
	//2;  // ERRORS: 	 15 (0.13)     denied%: 	 0.88    	;  errors % : 0.75
	//3;  // ERRORS: 	 17 (0.03)
	//4;  // ERRORS: 	 16 (0.08)
	//5;  // ERRORS: 	 14 (0.18)     denied%: 	 0.88    	;  errors % : 0.70
	//6;  // ERRORS: 	 15 (0.13)
	//7;  // ERRORS: 	 16 (0.08)        denied%: 	 0.88    	;  errors % : 0.80
	//8;
	// 6 35
	//2;   // ERRORS: 	 17 (-0.16)
	// 3;  // ERRORS: 	 14 (-0.01)     denied%: 	 0.69    	;  errors % : 0.70
	// 4;  // ERRORS: 	 14 (-0.01)
	// 5;  // ERRORS: 	 14 (-0.01)
// 			=========

}