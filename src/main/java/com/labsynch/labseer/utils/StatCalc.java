package com.labsynch.labseer.utils;

public class StatCalc {
	private int count;   // Number of numbers that have been entered.
	private Double sum;  // The sum of all the items that have been entered.
	private Double squareSum;  // The sum of the squares of all the items.
	private Double product; // The product of all the items

	public StatCalc() {
		count = 0;
		sum = 0.0;
		squareSum = 0.0;
		product = 1.0;
	}
	public void add(double num) {
		// Add the number to the dataset.
		count++;
		sum += num;
		squareSum += num*num;
		product *= num;
	}

	public int getCount() {   
		// Return number of items that have been entered.
		return count;
	}

	public Double getSum() {
		// Return the sum of all the items that have been entered.
		return sum;
	}
	public Double getProduct() {
		// Return the product of all the items that have been entered.
		return product;
	}

	public Double getArithmeticMean() {
		// Return average of all the items that have been entered.
		// Value is Double.NaN if count == 0.
		return sum / count;  
	}
	public Double getGeometricMean() {
		// Return geometric mean of all the items that have been entered.
		// Value is Double.NaN if count == 0.
		return Math.pow(product,1/count);  
	}

	public Double getStandardDeviation() {  
		// Return standard deviation of all the items that have been entered.
		// Value will be Double.NaN if count == 0.
		Double mean = getArithmeticMean();
		Double stdDev = squareSum/count - mean*mean;
		if(stdDev < 0.000000000001) {
			stdDev = 0.0;
		}
		return Math.sqrt( stdDev );
	}
}