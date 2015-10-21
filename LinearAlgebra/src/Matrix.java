import java.util.*;


public class Matrix {

	private List<Vector> matrix;
	private int numRows;
	private int numCol;

	public Matrix(int rows, int cols, ArrayList<Double> entries) {
		numRows = rows;
		numCol = cols;
		if(rows * cols != entries.size()){
			throw new IllegalArgumentException();
		}
		matrix = new ArrayList<Vector>();
		int entryIndex = 0;
		while(entryIndex < entries.size()){
			double[] rowVector = new double[numCol];
			for(int col = 0; col < numCol; col++){
				rowVector[col] = entries.get(entryIndex);
				entryIndex++;
			}
			matrix.add(new Vector(rowVector));
		}
	}

	public void rref(){
		int topRow = 0;
		int nonZeroCol = 0;
		while(topRow < numRows){
			//create leading ones
			for(int row = topRow; row < numRows; row++){
				double scalar = matrix.get(row).getVector()[nonZeroCol];
				if(scalar != 0){
					matrix.set(row, matrix.get(row).scaleMatrix(1 / scalar));
				}
			}
			//subtract every row below the first by the first
			for(int row = topRow + 1; row < numRows; row++){
				matrix.set(row, matrix.get(row).subtractVectors(matrix.get(topRow)));
			}
			topRow++;
			nonZeroCol++;
		}

		int botRow = numRows - 1;
		int leadingColumn = findLeadingOne(matrix.get(botRow));
		while(botRow > 0){
			if(leadingColumn > 0){
				for(int row = botRow - 1; row >= 0; row--){
					double scalar = matrix.get(row).getVector()[leadingColumn];
					Vector temp = matrix.get(botRow).scaleMatrix(scalar);
					matrix.set(row, matrix.get(row).subtractVectors(temp));
				}
			}
			botRow--;
			leadingColumn = findLeadingOne(matrix.get(botRow));
		}
	}

	public String toString() {
		String augmentMatrix = "";
		for(Vector v : matrix){
			augmentMatrix += v.toString() + "\n";
		}
		return augmentMatrix;
	}

	public int findLeadingOne(Vector v){
		int index = 0;
		while(index < v.getVector().length){
			if(v.getVector()[index] == 1){
				return index;
			}
			index++;
		}
		return -1;
	}
}
