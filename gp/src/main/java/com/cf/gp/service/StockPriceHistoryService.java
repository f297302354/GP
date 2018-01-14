package com.cf.gp.service;

import java.util.List;

import com.cf.gp.model.StockPriceHistory;
import com.cf.gp.model.StockPriceHistoryParamVo;
import com.cf.gp.model.StockPriceHistoryResultCondition;

public interface StockPriceHistoryService {

	public boolean queryHistoryDataWithInsertDB(String start_date, String end_date);
	
	public double queryAvgValue(StockPriceHistoryParamVo vo);
	
	StockPriceHistory selectByDate(StockPriceHistoryParamVo vo);

	int queryAvgValueCount(StockPriceHistoryParamVo vo);
	
	public boolean queryDataWithInsertDB();
	
	StockPriceHistoryResultCondition queryAvgValAndCount(StockPriceHistoryParamVo vo);
	
	void batchInsert(List<StockPriceHistory> datas);
	
	List<StockPriceHistory> queryOneDayDataByDate(String date);
	
}
