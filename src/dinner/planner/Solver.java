package dinner.planner;


public class Solver {
	public static int stochasticHillClimbing(Dinner dinner, double constant) {
		int calculations = 0;
		dinner.randomPlacing();
		int n = dinner.size();
		double maxVal = dinner.value();
		double oldVal = maxVal;
		int[] bestT1 = dinner.cloneTable1();
		int[] bestT2 = dinner.cloneTable2();
		int noChange = 0;
		
		do {
			int i = (int) (Math.random() * n);
			int j = (int) (Math.random() * n);
			
			dinner.switchPlaces(i, j);
			double val = dinner.value();
			calculations++;
			noChange++;
			if (val > maxVal) {
				noChange = 0;
				maxVal = val;
				bestT1 = dinner.cloneTable1();
				bestT2 = dinner.cloneTable2();
			}
			
			
			if (keepNewPoint(val, oldVal, constant)) {
				oldVal = val;
			} else {
				dinner.switchPlaces(i, j);
			}
		} while(noChange < n*n*n*0.1);
		
		dinner.setTable1(bestT1);
		dinner.setTable2(bestT2);
		
		return calculations;
	}
	
	private static boolean keepNewPoint(double val, double oldVal, double T) {
		double probability = 1 / (1 + Math.exp((oldVal - val) / T ));
		return probability > Math.random();
	}
	
	public static int hillClimbing(Dinner dinner) {
		int calculations = 0;
		dinner.randomPlacing();
		int n = dinner.size();
		double maxVal = dinner.value();
		double oldVal;
		int[] bestT1 = dinner.cloneTable1();
		int[] bestT2 = dinner.cloneTable2();
		
		do {
			oldVal = maxVal;
			for (int i = 0; i < n ; i++) {
				for (int j = 0; j < n; j++) {
					dinner.switchPlaces(i,j);
					calculations++;
					double val = dinner.value();
					if (val > maxVal) {
						maxVal = val;
						bestT1 = dinner.cloneTable1();
						bestT2 = dinner.cloneTable2();
					}
					dinner.switchPlaces(i, j);
				}
			}
			dinner.setTable1(bestT1);
			dinner.setTable2(bestT2);
		} while (maxVal > oldVal);
		return calculations;
	}
	
	public static int multHC(Dinner dinner, int n) {
		double best = 0;
		int calculations = 0;
		Dinner solution = null;
		for (int i = 0; i < n; i++) {
			calculations += hillClimbing(dinner);
			double val = dinner.value();
			if (val > best) {
				best = val;
				solution = dinner.clone();
			}
		}
		dinner.setTable1(solution.cloneTable1());
		dinner.setTable2(solution.cloneTable2());
		return calculations;
	}
	
	public static void bruteForce(Dinner dinner) {
		int n = dinner.size();
		boolean[] chosen = new boolean[2*n];
		int[] bestT1 = new int[n];
		int[] bestT2 = new int[n];
		double[] bestV = {0};
		
		bruteForce(dinner, chosen, bestT1, bestT2, bestV, 2*n);
		dinner.setTable1(bestT1);
		dinner.setTable2(bestT2);
	}
	
	private static void bruteForce(Dinner dinner, boolean[] chosen, int[] bestT1, 
			int[] bestT2, double[] bestV, int unFilled) {
		int n = bestT1.length;
		if (unFilled == 0) {
			double val = dinner.value();
			if (val > bestV[0]) {
				bestT1 = dinner.cloneTable1();
				bestT2 = dinner.cloneTable2();
				bestV[0] = val;
			}
			return;
		}
		
		for (int i = 0; i < 2*n; i++) {
			if (!chosen[i]) {
				chosen[i] = true;
				
				if (unFilled > n) {
					dinner.setPositionTable1(unFilled - n - 1, i);
				} else {
					dinner.setPositionTable2(unFilled - 1, i);
				}
				bruteForce(dinner, chosen, bestT1, bestT2, bestV, unFilled - 1);
				chosen[i] = false;
			}
		}
	}
	
	
	
	public static void main(String[] args) {
		int n = 30;
		double[][] matrix = new double[2*n][2*n];
		for (int i = 0; i < 2*n; i++) {
			for (int j = 0; j < 2*n; j++) {
				matrix[i][j] = Math.random();
			}
		}
		
		Dinner dinner = new Dinner(n, matrix);
		
		System.out.println("--Random:");
		long time = System.currentTimeMillis();
//		bruteForce(dinner);
		dinner.randomPlacing();
		System.out.println("time: " + (System.currentTimeMillis() - time));
		System.out.println(dinner.value());
		System.out.println("");
		
		System.out.println("--HC 10:");
		time = System.currentTimeMillis();
		System.out.println("Calc: " + multHC(dinner, 10));
		System.out.println("Time: " + (System.currentTimeMillis() - time));
		System.out.println("Val: " + dinner.value());
		System.out.println("");
		
		System.out.println("--HC:");
		time = System.currentTimeMillis();
		System.out.println("Calc: " + hillClimbing(dinner));
		System.out.println("time: " + (System.currentTimeMillis() - time));
		System.out.println("Val: " + dinner.value());
		System.out.println("");
		
		System.out.println("--SHC:");
		time = System.currentTimeMillis();
		System.out.println("Calc: " + stochasticHillClimbing(dinner, 1));
		System.out.println("time: " + (System.currentTimeMillis() - time));
		System.out.println("Val: " + dinner.value());
		System.out.println("");
	}
}
