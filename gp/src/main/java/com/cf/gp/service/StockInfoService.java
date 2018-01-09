package com.cf.gp.service;

import java.util.List;

import com.cf.gp.model.StockInfo;


public interface StockInfoService {

	boolean queryInfoWithInsertDB();
	
	List<StockInfo> selectAll();

}
