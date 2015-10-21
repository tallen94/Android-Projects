import java.util.*;
import java.io.*;


public class RREF {

	public static void main(String[] args) throws FileNotFoundException {
		// TODO Auto-generated method stub
		File f = new File("matrix.txt");
		Scanner input = new Scanner(f);
		Scanner scan = new Scanner(System.in);
		ArrayList<Double> entries = new ArrayList<Double>();
		while(input.hasNext()){
			entries.add(input.nextDouble());
		}
		System.out.print("Rows = ");
		int rows = scan.nextInt();
		System.out.print("Columns = ");
		int columns = scan.nextInt();
		
		Matrix m = new Matrix(rows, columns, entries);
		System.out.println("Before echelon form:\n" + m);
		m.rref();
		System.out.println("After:\n" + m);	
	}
}
