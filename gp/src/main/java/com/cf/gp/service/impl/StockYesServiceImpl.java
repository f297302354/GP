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
import com.cf.gp.model.StockPriceHistoryResultCondition;
import com.cf.gp.model.StockYes;
import com.cf.gp.model.StockYesParam;
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
	
	private static final String ST_CODE = "ST";
	
	private static final int AVG_14 = 14;
	
	private static final int AVG_25 = 25;
	
	private static final int AVG_99 = 99;
	
	private static final int AVG_144 = 144;
	
	private static final int AVG_165 = 165;
	
	private static final double DIFF_VAL = 0.03;//相差1分钱
	
	private static final String CALCULATE_AND_FLAG = "<>";
	
	
	/*@SuppressWarnings("finally")
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
							if (current.getsPrice().doubleValue() >= val25 &&  current.getsPrice().doubleValue() <= (val25 + DIFF_VAL)) {
//								if (current.getsPrice().doubleValue() >= (val25 - DIFF_VAL) &&  current.getsPrice().doubleValue() <= (val25 + DIFF_VAL)) {//等于25日均线的收盘价(前后误差值为：DIFF_VAL变量)
//							if (current.getsPrice().doubleValue() >= val14 && current.getsPrice().doubleValue() <= val25) {//大于等于14日均线并且小于等于25日均线
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
	}*/
	
	/**
	 * 考虑的是144 165天的数据
	 */
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
						param.setDayAvg(AVG_165);
						if (stockPriceHistoryService.queryAvgValueCount(param) == AVG_165) {//如果有165天的值就可以算165天均值
							double val165 = stockPriceHistoryService.queryAvgValue(param);
							param.setDayAvg(AVG_144);
							double val144 = stockPriceHistoryService.queryAvgValue(param);
							StockPriceHistory current = stockPriceHistoryService.selectByDate(param);
							//TODO:注意条件
							if (
									(current.getsPrice().doubleValue() >= val165 &&  current.getsPrice().doubleValue() <= (val165 + DIFF_VAL)) //165
										|| 
									(current.getsPrice().doubleValue() >= val144 &&  current.getsPrice().doubleValue() <= (val144 + DIFF_VAL)) //144
										) {//165线上3分波动
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
	
	/**
	 * @param vo 参数对象
	 * @return
	 */
	@SuppressWarnings("finally")
	@Transactional
	public boolean execYes(StockYesParam vo) {
		boolean resultFlag = false;
		try {
			List<StockPriceHistory> selectAll = stockPriceHistoryService.queryOneDayDataByDate(vo.getDate());
			StockPriceHistoryParamVo param = null;
			for (int i = 0; i < selectAll.size(); i++) {
				StockPriceHistory stockInfo = selectAll.get(i);
				if (stockInfo.getsName().indexOf(ST_CODE) == -1) {//ST暂时不考虑，创业板的sql中暂时已经过滤了
					try {
						param = new StockPriceHistoryParamVo();
						param.setCode(stockInfo.getsCode());
						param.setDate(vo.getDate());
						param.setDayAvg(vo.getHavdAvgDay());
						StockPriceHistoryResultCondition avgValAndCount = stockPriceHistoryService.queryAvgValAndCount(param);
						if (avgValAndCount.getCount() == vo.getHavdAvgDay()) {
							Integer downAvgDay = vo.getDownAvgDay();
							Integer upAvgDay = vo.getUpAvgDay();
							double downVal = avgValAndCount.getVal();
							if (downAvgDay.intValue() != vo.getHavdAvgDay()) {
								param.setDayAvg(downAvgDay);
								downVal = stockPriceHistoryService.queryAvgValue(param);
							}
							double currentVal = stockInfo.getsPrice().doubleValue();
							boolean isInsert = false;
							if (upAvgDay == null) {
								if (currentVal >= downVal && currentVal <= (downVal + vo.getDiffVal())) {
									isInsert = true;
								}
							} else {
								param.setDayAvg(upAvgDay);
								double upVal = stockPriceHistoryService.queryAvgValue(param);
								if (CALCULATE_AND_FLAG.equals(vo.getCalculdateFlag())) {// <> 算区间
									if (currentVal >= downVal && currentVal <= upVal) {
										isInsert = true;
									}
								} else {// == 算两个值得都符合情况
									if ((currentVal >= downVal && currentVal <= (downVal + vo.getDiffVal())) ||  (currentVal >= upVal && currentVal <= (upVal + vo.getDiffVal()))) {
										isInsert = true;
									}
								}
							}
							if (isInsert) {
								StockYes y = new StockYes();
								y.setsCode(stockInfo.getsCode());
								y.setsName(stockInfo.getsName());
								y.setsDate(vo.getCurrentDate());
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
	
//	@SuppressWarnings("finally")
//	@Transactional
//	public boolean execYes(StockYesParam vo) {
//		boolean resultFlag = false;
//		try {
//			List<StockInfo> selectAll = stockInfoService.selectAll();
//			StockPriceHistoryParamVo param = null;
//			for (int i = 0; i < selectAll.size(); i++) {
//				StockInfo stockInfo = selectAll.get(i);
//				try {
//					param = new StockPriceHistoryParamVo();
//					param.setCode(stockInfo.getsCode());
//					param.setDate(vo.getDate());
//					param.setDayAvg(vo.getHavdAvgDay());
//					StockPriceHistoryResultCondition avgValAndCount = stockPriceHistoryService.queryAvgValAndCount(param);
//					if (avgValAndCount.getCount() == vo.getHavdAvgDay()) {
//						
//						Integer downAvgDay = vo.getDownAvgDay();
//						Integer upAvgDay = vo.getUpAvgDay();
//						if (upAvgDay == null) {
//							double val = avgValAndCount.getVal();
//							if (downAvgDay.intValue() != vo.getHavdAvgDay()) {
//								param.setDayAvg(downAvgDay);
//								val = stockPriceHistoryService.queryAvgValue(param);
//							}
//							if (stockInfo.getsPrice() >= val && stockInfo.getsPrice() <= (val + vo.getDiffVal())) {
//								StockYes y = new StockYes();
//								y.setsCode(stockInfo.getsCode());
//								y.setsName(stockInfo.getsName());
//								y.setsDate(vo.getCurrentDate());
//								stockYesMapper.insert(y);
//							}
//						} else {
//							
//						}
//					}
//				} catch (Exception e) {
//					continue;
//				}
//			}
//			resultFlag = true;
//		} catch (Exception e) {
//			System.err.println(e.getMessage());
//		} finally {
//			return resultFlag;
//		}
//	}
	
	public static void main(String[] args) {
		String s = "300123";
		System.out.println(s.indexOf("300"));
	}
	
}
