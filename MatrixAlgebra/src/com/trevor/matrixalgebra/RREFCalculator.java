package com.trevor.matrixalgebra;

import android.os.Bundle;
import android.widget.*;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import java.util.*;

public class RREFCalculator extends Activity {

	private EditText etMatrix, etRow, etCol;
	private Button b1;
	private TextView tv1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rref);
		initialize();
		b1.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int row = Integer.parseInt(etRow.getText().toString());
				int col = Integer.parseInt(etCol.getText().toString());
				System.out.println(etMatrix.getText().toString());
				Scanner input = new Scanner(etMatrix.getText().toString());
				ArrayList<Double> entries = new ArrayList<Double>();
				while(input.hasNext()){
					System.out.print(input.nextDouble());
					entries.add(input.nextDouble());
				}
				System.out.println("AFTER WHILE LOOP");
				System.out.println("Entries size = " + entries.size());
				Matrix m = new Matrix(row, col, entries);
				m.rref();
				System.out.println("THIS IS M:");
				System.out.println(m.toString());
				//tv1.setText(m);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.rre, menu);
		return true;
	}
	
	private void initialize(){
		etMatrix = (EditText) findViewById(R.id.et1);
		etMatrix.requestFocus();
		etMatrix.setSelection(0);
		etRow = (EditText) findViewById(R.id.etRow);
		etCol = (EditText) findViewById(R.id.etCol);
		b1 = (Button) findViewById(R.id.b1);
		tv1 = (TextView) findViewById(R.id.tv1);
	}
}
