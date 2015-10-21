package com.trevor.matrixalgebra;
import java.util.Arrays;


public class Vector {

	private double[] vector;
	
	public Vector(double[] vector){
		this.vector = vector;
	}
	
	public Vector(Vector other){
		vector = other.getVector();
	}
	
	public double dotProduct(Vector other){
		double total = 0;
		for(int index = 0; index < vector.length; index++){
			total += vector[index] * other.getVector()[index];
		}
		return total;
	}
	
	public double[] getVector(){
		return vector;
	}
	
	public Vector scaleMatrix(double a){
		double[] newMatrix = new double[vector.length];
		for(int index = 0; index < vector.length; index++){
			newMatrix[index] = a * vector[index];
		}
		return new Vector(newMatrix);
	}
	
	public Vector addVectors(Vector other){
		double[] vectorSum = new double[vector.length];
		for(int i = 0; i < vector.length; i++){
			vectorSum[i] = vector[i] + other.getVector()[i];
		}
		return new Vector(vectorSum);
	}
	
	public Vector subtractVectors(Vector other){
		double[] vectorSum = new double[vector.length];
		for(int i = 0; i < vector.length; i++){
			vectorSum[i] = vector[i] - other.getVector()[i];
		}
		return new Vector(vectorSum);
	}
	
	public String toString(){
		String s = "[" + vector[0];
		for(int index = 1; index < vector.length; index++){
			s += "\t" + (double) Math.round(vector[index] * 10000) / 10000;
		}
		s += "]";
		return s;
	}
	
}
