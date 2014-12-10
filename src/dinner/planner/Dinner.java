package dinner.planner;

public class Dinner {
	private int[] table1;
	private int[] table2;
	private double[][] matrix;

	public Dinner(int n, double[][] matrix) {
		table1 = new int[n];
		table2 = new int[n];
		this.matrix = matrix;
	}

	public Dinner clone() {
		Dinner ret = new Dinner(table1.length, matrix.clone());
		ret.table1 = table1.clone();
		ret.table2 = table2.clone();
		return ret;
	}

	public double value() {
		double ret = 0;
		for (int i = 0; i < table1.length; i++) {
			for (int j = 0; j < table1.length; j++) {
				ret += matrix[table1[i]][table1[j]];
				ret += matrix[table2[i]][table2[j]];
			}
		}
		return ret;
	}

	public void printTables() {
		for (int i = 1; i < 3; i++) {
			System.out.println("Table " + i);
			for (int j = 0; j < table1.length; j++) {
				System.out.println((i == 1 ? table1 : table2)[j]);
			}
		}
	}

	public void randomPlacing() {
		int n = table1.length;
		boolean[][] chosen = new boolean[2][n];
		int placed = 0;
		while (placed < n * 2) {
			int table = (Math.random() > 0.5 ? 1 : 2);
			int seat = (int) (Math.random() * n);
			if (!chosen[table - 1][seat]) {
				(table == 1 ? table1 : table2)[seat] = placed;
				chosen[table - 1][seat] = true;
				placed++;
			}
		}
	}
	
	public int size() {
		return table1.length;
	}
	
	public int[] cloneTable1() {
		return table1.clone();
	}
	
	public int[] cloneTable2() {
		return table2.clone();
	}
	
	public void switchPlaces(int i, int j) {
		int tmp = table1[i];
		table1[i] = table2[j];
		table2[j] = tmp;
	}
	
	public void setTable1(int[] table1) {
		this.table1 = table1; 
	}
	
	public void setTable2(int[] table2) {
		this.table2 = table2; 
	}
	
	public void setPositionTable1(int position, int person) {
		if (position >= table1.length || position < 0)
			throw new IllegalArgumentException("position has bad value.");
		if (person >= table1.length*2 || person < 0)
			throw new IllegalArgumentException("person has bad value.");
		table1[position] = person;
	}
	
	public void setPositionTable2(int position, int person) {
		if (position >= table1.length || position < 0)
			throw new IllegalArgumentException("position has bad value.");
		if (person >= table1.length*2 || person < 0)
			throw new IllegalArgumentException("person has bad value.");
		table2[position] = person;
	}
}
