package com.cf.gp.model;

public class StockPriceHistoryResultCondition {

	private int count;
	
	private double val;

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public double getVal() {
		return val;
	}

	public void setVal(double val) {
		this.val = val;
	}

	@Override
	public String toString() {
		return "StockPriceHistoryResultCondition [count=" + count + ", val=" + val + "]";
	}
	
}
