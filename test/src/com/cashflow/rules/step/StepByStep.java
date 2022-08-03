package com.cashflow.rules.step;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Ruslan Dauhiala
 */
public class StepByStep
{
	static final int kLimit = 11_238_513;
	// new -> old
	static final int[] kGamesNums =
			  {3598742, 6028076, 1872508, 3427389, 9590790, 1327383, 5613473, 346925, 9680981, 8913417, 356377, 2395572, 1795166, 4661253, 8274548, 2621515, 5766095, 7202035, 3728756, 9763079, 9726742, 8463772, 611072, 9329944, 1670360, 8390857, 9684895, 6358905, 7662831, 3264084, 9142308, 8385509, 11115405, 5291242, 8396876, 8107354, 1291220, 5137335, 5139591, 3675317, 6171064, 1602931, 9703919, 3800086, 11159599, 3822229, 7134170, 6969029, 9876480, 4442195, 7290919, 11007178, 435226, 9808986, 7901111, 2394281, 8432385, 3094874, 10172903, 2237554, 7617961, 4611166, 6950239, 1325542, 10444754, 1790436, 64488, 602442, 5725487, 1421045, 10106524, 8906809, 10398554, 9882632, 4910460, 10922364, 8529150, 9799184, 2358779, 6690764, 6697782, 1685391, 10336904, 8701090, 7736075, 10657452, 10766366, 64719, 6227206, 9541335, 4880728, 4815864, 10005673, 11089459, 8374478, 8484501, 3987833, 4940483, 4011666, 9015881, 4443021, 3627316, 8404866, 5458108, 9169711, 6963461, 6450502, 521089, 709163, 4519563, 3545526, 11223670, 5753216, 26036, 4823943, 2467457, 9099952, 10542652, 6045452, 7750833, 5856939, 3432963, 9199972, 3419197, 1796260, 2915397, 3148121, 10415457, 3433365, 2549997, 8419890, 10504833, 6452992, 8388464, 9526915, 476387, 9495311, 578701, 3423010, 4948238, 9825893, 5075583, 5705195, 4964143, 8807570, 1080942, 7280538, 8283074, 687515, 9805665, 1310277, 5935497,
		  4142055, 5854032, 1708780, 3505098, 3103764, 7544757, 3817606, 5761789, 6835280, 8788143, 9729845, 5749842, 9866627, 64540, 1891882, 8087817, 82936, 8196192, 10454554, 10089839, 268676, 8760644, 6848753, 11132982, 4821001, 1938734, 8391435, 4697950, 8208839, 10468564, 4999338, 667255, 9451271, 7163723, 8890208, 836495, 705870, 5934475, 9862681, 8279510, 2300152, 5027196, 1184852, 10630483, 4432027, 713093, 5545786, 5791818, 2029627, 9530864, 4739089, 3144598, 2911053, 5339472, 1688586, 10847542, 11055857, 9866975, 9267019, 5341245, 6367303, 6223835, 3040384, 4067872, 6381839, 1975336, 6123053, 1423364, 6175655, 9771367, 2184120, 7732733, 1414277, 2785330, 9213853, 6910659, 8140406, 9777961, 4144150, 6504075, 3020863, 9727840, 7305212, 9264900, 3465605, 10655217, 1882447, 1437533, 9071553, 6906373, 2037756, 10597396, 7565820, 2876174, 5666177, 10003446, 6490149, 3066240, 6490497, 5809124, 6554084, 7588200, 2183064, 6681673, 5789463, 9452375, 6235189, 4484496, 4429176, 856312, 2602353, 10166170, 3194835, 1902782, 9631577, 3584064, 2297811, 2436273, 8130150, 1155027, 2992724, 7005878, 10361744, 8336981, 10403541, 5313739, 1219461, 4288692, 7299394, 7489580, 983654, 8276822, 11028704, 8612272, 7576147, 2847348, 4461254, 1162574};

	public static void main(String args[])
	{
		new StepByStep().go();
	}

	private void go()
	{
		for (int i = kGamesNums.length-2; i > 0; i--)
		{
			int aCur = kGamesNums[i];
			int aPrev = kGamesNums[i+1];
			int aDiff = getDiff(i);
			Set<Integer> aFutureExcludedSet = new HashSet<>();
			aFutureExcludedSet.add(aCur);
			boolean aGo;
			boolean aMeetFuture = false;
			int aForward = aCur;
			System.out.print(i+": ");
			int aLoop = 0;
			int aFutureNum = 0;
			Set<Integer> aBackUpSet = new HashSet<>();
			do
			{
				aForward += aDiff;
				if (aForward > kLimit)
				{
					aForward -= kLimit;
				}

				if (!(aGo = aFutureExcludedSet.add(aForward))
//						  || !(aGo = aFutureExcludedSet.add(aForward+1))
//						  || !(aGo = aFutureExcludedSet.add(aForward-1))
						  )
				{

					if((double)aFutureExcludedSet.size()/kLimit < .5)
					{
						if (aBackUpSet.isEmpty())
						{
							aBackUpSet.addAll(aFutureExcludedSet);
							aFutureExcludedSet.clear();
							aDiff++;
							aGo = true;
						} else
						{
							aFutureExcludedSet.addAll(aBackUpSet);
							if((double)aFutureExcludedSet.size()/kLimit < .5)
							{
								aBackUpSet.clear();
								aBackUpSet.addAll(aFutureExcludedSet);
								aFutureExcludedSet.clear();
								aDiff++;
								aGo = true;
							}
							aLoop = aFutureExcludedSet.size();
						}
					} else
					{
						aLoop = aFutureExcludedSet.size();
					}
				}

				if (!aMeetFuture && (
						  aForward == kGamesNums[i-1]
//									 || aForward+1 == kGamesNums[i-1]
//									 || aForward-1 == kGamesNums[i-1]
				)
						  //|| aBackward == kGamesNums[i-1])
				//aFutureExcludedSet.contains(kGamesNums[i-1]) // speed UP!
				)
				{
					aFutureNum = aFutureExcludedSet.size();
					aMeetFuture = true;
				}
			} while (aGo);

			double aCoverage = (double)aFutureExcludedSet.size()/kLimit;

			System.out.format("\t\t Loop: %10d ", aLoop);
			System.out.format("\t\t Future: %10d ", aFutureNum);
			System.out.format("\t\t Eff*: %.3f ", aFutureNum == 0 ? aCoverage : (double)aFutureNum/kLimit);
			System.out.format("\t\t Cov**: %.3f\n", aCoverage);
		}

		System.out.print("\n\n\n" +
				  " ===============\n" +
				  " *  - how soon it makes a mistake?\n" +
				  " ** - how many games it includes\n" +
				  " peace ");
	}

	int myDiffSum = 0;
	private int getDiff(int theI)
	{
//		int aCur = kGamesNums[theI];
//		int aPrev = kGamesNums[theI+1];
		myDiffSum += Math.abs(kGamesNums[theI] - kGamesNums[theI+1]);
		return myDiffSum / (kGamesNums.length-theI-1);
		//Math.abs(aCur - aPrev);
	}
}
