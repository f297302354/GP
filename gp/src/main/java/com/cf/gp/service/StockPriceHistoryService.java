package com.cf.gp.service;

import com.cf.gp.model.StockPriceHistory;
import com.cf.gp.model.StockPriceHistoryParamVo;

public interface StockPriceHistoryService {

	public boolean queryHistoryDataWithInsertDB(String start_date, String end_date);
	
	public double queryAvgValue(StockPriceHistoryParamVo vo);
	
	StockPriceHistory selectByDate(StockPriceHistoryParamVo vo);

	int queryAvgValueCount(StockPriceHistoryParamVo vo);
	
	public boolean queryDataWithInsertDB();
	
}
