package com.cf.gp.model;

public class StockTranformVo {

	private String symbol;
	
	private String code;
	
	private String name;
	
	private double trade;
	
	private double open;

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getTrade() {
		return trade;
	}

	public void setTrade(double trade) {
		this.trade = trade;
	}

	public double getOpen() {
		return open;
	}

	public void setOpen(double open) {
		this.open = open;
	}
	
}
