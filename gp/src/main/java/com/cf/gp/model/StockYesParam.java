package com.cf.gp.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class StockYesParam {
    
	/**
	 * 查询日期 yyyy-MM-dd 格式，必填
	 */
	private String date;
	
	/**
	 * 至少有多少天均线才会考虑，默认有165日均线的才考虑
	 */
	private Integer havdAvgDay = 165;
	
	/**
	 * 最低值，如果没有填最高值，那么就按着一根均线考虑
	 */
	private Integer downAvgDay;
	
	/**
	 * 最高值
	 */
	private Integer upAvgDay;
	
	/**
	 * 误差值
	 */
	private double diffVal;
	
	private Date currentDate;
	
	private boolean priceGTOpen = true;//true是算所有线，false只算阴线
	
	private boolean calLow = false;//是否按最低价来计算，如果这个属性为true，那么就不会计算收盘价的价格了，并且没有区间以及两均线或者都符合的计算逻辑了
	
	/**
	 * 如果是两个值得情况下，当前字段为计算两个值得规则
	 * <>是计算两个值得区间中间值，也就是符合>=downVal 并且 <=upVal
	 * ==是计算两个值得并且情况，也就是符合>=downVal 或者 >= upVal
	 */
	private String calculdateFlag = "<>";

	public String getDate() {
		return date;
	}

	public StockYesParam() {
		this.currentDate = new Date();
		this.date = new SimpleDateFormat("yyyy-MM-dd").format(this.currentDate);
	}

	public void setDate(String date) {
		try {
			this.currentDate = new SimpleDateFormat("yyyy-MM-dd").parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		this.date = date;
	}

	public Integer getHavdAvgDay() {
		return havdAvgDay;
	}

	public void setHavdAvgDay(Integer havdAvgDay) {
		this.havdAvgDay = havdAvgDay;
	}

	public Integer getDownAvgDay() {
		return downAvgDay;
	}

	public void setDownAvgDay(Integer downAvgDay) {
		this.downAvgDay = downAvgDay;
	}

	public Integer getUpAvgDay() {
		return upAvgDay;
	}

	public void setUpAvgDay(Integer upAvgDay) {
		this.upAvgDay = upAvgDay;
	}

	public double getDiffVal() {
		return diffVal;
	}

	public void setDiffVal(double diffVal) {
		this.diffVal = diffVal;
	}

	public Date getCurrentDate() {
		return currentDate;
	}

	public String getCalculdateFlag() {
		return calculdateFlag;
	}

	public void setCalculdateFlag(String calculdateFlag) {
		this.calculdateFlag = calculdateFlag;
	}

	public boolean isPriceGTOpen() {
		return priceGTOpen;
	}

	public void setPriceGTOpen(boolean priceGTOpen) {
		this.priceGTOpen = priceGTOpen;
	}

	public boolean isCalLow() {
		return calLow;
	}

	public void setCalLow(boolean calLow) {
		this.calLow = calLow;
	}

}