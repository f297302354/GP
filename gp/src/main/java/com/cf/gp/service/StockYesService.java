package com.cf.gp.service;

import com.cf.gp.model.StockYesParam;

public interface StockYesService {

	public boolean execYes(String date);
	
	public boolean execYes(StockYesParam vo);
	
}
