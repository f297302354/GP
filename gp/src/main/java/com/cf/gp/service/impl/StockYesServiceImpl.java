package com.cf.gp.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cf.gp.dao.StockYesMapper;
import com.cf.gp.model.StockInfo;
import com.cf.gp.model.StockPriceHistory;
import com.cf.gp.model.StockPriceHistoryParamVo;
import com.cf.gp.model.StockYes;
import com.cf.gp.service.StockInfoService;
import com.cf.gp.service.StockPriceHistoryService;
import com.cf.gp.service.StockYesService;

@Service("stockYesService")
public class StockYesServiceImpl implements StockYesService {
	
	@Autowired
	private StockYesMapper stockYesMapper;
	
	@Autowired
	private StockPriceHistoryService stockPriceHistoryService;
	
	@Autowired
	private StockInfoService stockInfoService;
	
	private static final String START_CODE = "300";
	
	private static final int AVG_14 = 14;
	
	private static final int AVG_25 = 25;
	
	private static final int AVG_99 = 99;
	
	@SuppressWarnings("finally")
	@Transactional
	public boolean execYes(String date) {
		boolean resultFlag = false;
		try {
			Date parseDate = new SimpleDateFormat("yyyy-MM-dd").parse(date);
			List<StockInfo> selectAll = stockInfoService.selectAll();
			StockPriceHistoryParamVo param = null;
			for (int i = 0; i < selectAll.size(); i++) {
				StockInfo stockInfo = selectAll.get(i);
				if (stockInfo.getsCode().indexOf(START_CODE) != 0 ) {//创业板暂时不考虑
					try {
						param = new StockPriceHistoryParamVo();
						param.setCode(stockInfo.getsCode());
						param.setDate(date);
						param.setDayAvg(AVG_99);
						if (stockPriceHistoryService.queryAvgValueCount(param) == AVG_99) {//如果有99天的值就可以算99天均值
							double val99 = stockPriceHistoryService.queryAvgValue(param);
							param.setDayAvg(AVG_25);
							double val25 = stockPriceHistoryService.queryAvgValue(param);
							param.setDayAvg(AVG_14);
							double val14 = stockPriceHistoryService.queryAvgValue(param);
							StockPriceHistory current = stockPriceHistoryService.selectByDate(param);
							//TODO:注意条件
//							if (current.getsPrice().doubleValue() >= val25 && current.getsPrice().doubleValue() < val99) {//大于等于25日均线并且小于99日均线
//							if (current.getsPrice().doubleValue() == val25) {//等于25日均线的收盘价
							if (current.getsPrice().doubleValue() >= val14 && current.getsPrice().doubleValue() <= val25) {//大于等于14日均线并且小于等于25日均线
								StockYes y = new StockYes();
								y.setsCode(stockInfo.getsCode());
								y.setsName(stockInfo.getsName());
								y.setsDate(parseDate);
								stockYesMapper.insert(y);
							}
						}
					} catch (Exception e) {
						continue;
					}
				}
			}
			resultFlag = true;
		} catch (Exception e) {
			System.err.println(e.getMessage());
		} finally {
			return resultFlag;
		}
	}
	
	public static void main(String[] args) {
		String s = "300123";
		System.out.println(s.indexOf("300"));
	}
	
}
