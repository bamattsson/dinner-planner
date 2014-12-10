package dinner.planner;


public class Solver {
	public static void hillClimbing(Dinner dinner) {
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
	}
	
	public static void multHC(Dinner dinner, int n) {
		double best = 0;
		Dinner solution = null;
		for (int i = 0; i < n; i++) {
			hillClimbing(dinner);
			double val = dinner.value();
			if (val > best) {
				best = val;
				solution = dinner.clone();
			}
		}
		dinner.setTable1(solution.cloneTable1());
		dinner.setTable2(solution.cloneTable2());
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
		
		
		System.out.println("--HC 10:");
		time = System.currentTimeMillis();
		multHC(dinner, 10);
		System.out.println("time: " + (System.currentTimeMillis() - time));
		System.out.println(dinner.value());
		
		System.out.println("--HC:");
		time = System.currentTimeMillis();
		hillClimbing(dinner);
		System.out.println("time: " + (System.currentTimeMillis() - time));
		System.out.println(dinner.value());
	}
}
