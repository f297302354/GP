package com.cf.gp.service.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.cf.gp.dao.StockPriceHistoryMapper;
import com.cf.gp.model.StockInfo;
import com.cf.gp.model.StockPriceHistory;
import com.cf.gp.model.StockPriceHistoryParamVo;
import com.cf.gp.model.StockPriceHistoryResultCondition;
import com.cf.gp.model.StockTranformVo;
import com.cf.gp.service.StockInfoService;
import com.cf.gp.service.StockPriceHistoryService;

@Service("stockPriceHistoryService")
public class StockPriceHistoryServiceImpl implements StockPriceHistoryService {

	@Autowired
	private StockInfoService stockInfoService;
	
	@Autowired
	private StockPriceHistoryMapper stockPriceHistoryMapper;
	
	private static final String STOCK_CODE = "$stock_code";
	
	private static final String START_PAGE_FLAG = "$start_page";
	
	private static final int fault_Count = 3;//失败重试3次
	
	private static final int batch_count = 1000;//批量以1000次为单位插入
	
	/**
	 * 根据股票表中的内容查询当前这个股票的一段时间的历史数据，只执行一次
	 */
	/*@SuppressWarnings("finally")
	@Transactional
	public boolean queryHistoryDataWithInsertDB(String start_date, String end_date) {
		boolean resultFlag = false;
		CloseableHttpClient client = HttpClients.createDefault();
//		String start_date = "20170501";
//		String end_date = "20180109";
		String url = "http://q.stock.sohu.com/hisHq?code=cn_$stock_code&start="+start_date+"&end="+end_date+"&stat=1&order=D&period=d&callback=historySearchHandler&rt=jsonp&r=0.8391495715053367&0.9677250558488026";
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		try {
			List<StockInfo> selectAll = stockInfoService.selectAll();
			for (int i = 0; i < selectAll.size(); i++) {
				System.out.println("----------------  " + i);
				StockInfo stockInfo = selectAll.get(i);
				HttpGet get = new HttpGet(url.replace(STOCK_CODE, stockInfo.getsCode()));
				CloseableHttpResponse resp = client.execute(get);
				HttpEntity entity = resp.getEntity();
				String result = EntityUtils.toString(entity);
				String[] dataArr = null;
				try {
					dataArr = this.stringDataTransformArray(result);
				} catch (Exception e) {
					continue;
				}
				for (int j = 0; j < dataArr.length; j++) {
					try {
						String[] everyDatas = dataArr[j].split(",");
						String everyDate = everyDatas[0];//日期
						String everyPrice = everyDatas[2];//收盘价
						StockPriceHistory his = new StockPriceHistory();
						his.setsCode(stockInfo.getsCode());
						his.setsName(stockInfo.getsName());
						his.setsDate(format.parse(everyDate));
						his.setsPrice(BigDecimal.valueOf(Double.valueOf(everyPrice)));
						stockPriceHistoryMapper.insert(his);
					} catch (Exception e) {
						continue;
					}
				}
			}
			resultFlag = true;
		} catch (Exception e) {
			System.err.println(e.getMessage());
		} finally {
			try {
				client.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return resultFlag;
		}
	}*/
	
	/**
	 * 根据股票表中的内容查询当前这个股票的一段时间的历史数据，只执行一次
	 */
	@SuppressWarnings("finally")
	@Transactional
	public boolean queryHistoryDataWithInsertDB(String start_date, String end_date) {
		boolean resultFlag = false;
		CloseableHttpClient client = HttpClients.createDefault();
//		String start_date = "20170501";
//		String end_date = "20180109";
		String url = "http://q.stock.sohu.com/hisHq?code=cn_$stock_code&start="+start_date+"&end="+end_date+"&stat=1&order=D&period=d&callback=historySearchHandler&rt=jsonp&r=0.8391495715053367&0.9677250558488026";
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		try {
			List<StockInfo> selectAll = stockInfoService.selectAll();
			List<StockPriceHistory> parpareInsertList = new ArrayList<StockPriceHistory>();
			for (int i = 0; i < selectAll.size(); i++) {
				System.out.println("----------------  " + i);
				StockInfo stockInfo = selectAll.get(i);
				HttpGet get = new HttpGet(url.replace(STOCK_CODE, stockInfo.getsCode()));
				CloseableHttpResponse resp = client.execute(get);
				HttpEntity entity = resp.getEntity();
				String result = EntityUtils.toString(entity);
				String[] dataArr = null;
				try {
					dataArr = this.stringDataTransformArray(result);
				} catch (Exception e) {
					continue;
				}
				for (int j = 0; j < dataArr.length; j++) {
					try {
						String[] everyDatas = dataArr[j].split(",");
						String everyDate = everyDatas[0];//日期
						String everyOpen = everyDatas[1];//开盘价
						String everyPrice = everyDatas[2];//收盘价
						String everyLow = everyDatas[5];//最低价
						String everyHigh = everyDatas[6];//最高价
						StockPriceHistory his = new StockPriceHistory();
						his.setsCode(stockInfo.getsCode());
						his.setsName(stockInfo.getsName());
						his.setsDate(format.parse(everyDate));
						his.setsPrice(BigDecimal.valueOf(Double.valueOf(everyPrice)));
						his.setsOpen(BigDecimal.valueOf(Double.valueOf(everyOpen)));
						his.setsHigh(BigDecimal.valueOf(Double.valueOf(everyHigh)));
						his.setsLow(BigDecimal.valueOf(Double.valueOf(everyLow)));
						parpareInsertList.add(his);
//						stockPriceHistoryMapper.insert(his);
					} catch (Exception e) {
						continue;
					}
				}
			}
			this.batchInsert(parpareInsertList);
			resultFlag = true;
		} catch (Exception e) {
			System.err.println(e.getMessage());
		} finally {
			try {
				client.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return resultFlag;
		}
	}
	
	/**
	 * 查询今天实时的股票数据，如果忘记执行了某几天，那么就需要调用查询历史的方法
	 */
	@SuppressWarnings("finally")
	@Transactional
	public boolean queryDataWithInsertDB() {
		boolean resultFlag = false;
		boolean exeFlag = true;
		int startPage = 1;
		int faultCount = 1;
		String url = "http://vip.stock.finance.sina.com.cn/quotes_service/api/json_v2.php/Market_Center.getHQNodeData?page=$start_page&num=100&sort=symbol&asc=1&node=hs_a&symbol=&_s_r_a=init";
		CloseableHttpClient client = HttpClients.createDefault();
		Date d = new Date();
		try {
			List<StockPriceHistory> parpareInsertList = new ArrayList<StockPriceHistory>();
			while (exeFlag) {
				HttpGet get = new HttpGet(url.replace(START_PAGE_FLAG, String.valueOf(startPage)));
				CloseableHttpResponse resp = client.execute(get);
				String result = EntityUtils.toString(resp.getEntity());
				if (result == null) {
					exeFlag = false;
				} else {
					if (faultCount == 3) {
						exeFlag = false;
					} else {
						try {
							List<StockTranformVo> resultList = JSON.parseArray(result, StockTranformVo.class);
							for (int i = 0; i < resultList.size(); i++) {
								StockTranformVo stockTranformVo = resultList.get(i);
								if (stockTranformVo.getOpen() != 0d) {//停牌的开盘价是0
									StockPriceHistory his = new StockPriceHistory();
									his.setsCode(stockTranformVo.getCode());
									his.setsName(stockTranformVo.getName());
									his.setsDate(d);
									his.setsPrice(BigDecimal.valueOf(stockTranformVo.getTrade()));
									his.setsOpen(BigDecimal.valueOf(stockTranformVo.getOpen()));
									his.setsHigh(BigDecimal.valueOf(stockTranformVo.getHigh()));
									his.setsLow(BigDecimal.valueOf(stockTranformVo.getLow()));
									parpareInsertList.add(his);
//									stockPriceHistoryMapper.insert(his);
								}
							}
						} catch (Exception e) {
							faultCount++;
							startPage++;
							continue;
						}
						startPage++;
					}
				}
				System.out.println(">>>>>>>>>>>>>>>>>>>>>    " + startPage);
				System.out.println(result);
			}
			this.batchInsert(parpareInsertList);
			resultFlag = true;
		} catch (Exception e) {
			System.err.println(e.getMessage());
		} finally {
			try {
				client.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return resultFlag;
		}
	}
	
	public String[] stringDataTransformArray(String data) {
		String substring = data.substring(data.indexOf("\"hq\":[")+"\"hq\":[".length(), data.indexOf("],\"code\""));
		return substring.replace("],", "").replace("]", "").replace("\"", "").substring(1).replace("[", "@").split("@");
	}
	
	@Override
	public double queryAvgValue(StockPriceHistoryParamVo vo) {
		return stockPriceHistoryMapper.queryAvgValue(vo);
	}

	@Override
	public StockPriceHistory selectByDate(StockPriceHistoryParamVo vo) {
		return stockPriceHistoryMapper.selectByDate(vo);
	}
	
	@Override
	public int queryAvgValueCount(StockPriceHistoryParamVo vo) {
		return stockPriceHistoryMapper.queryAvgValueCount(vo);
	}

	@Override
	public StockPriceHistoryResultCondition queryAvgValAndCount(StockPriceHistoryParamVo vo) {
		return stockPriceHistoryMapper.queryAvgValAndCount(vo);
	}

	@Override
	@Transactional
	public void batchInsert(List<StockPriceHistory> datas) {
		List<List<StockPriceHistory>> lists = this.transformBigListToSmallList(datas);
		for (int i = 0, size = lists.size(); i < size; i++) {
			List<StockPriceHistory> subList = lists.get(i);
			stockPriceHistoryMapper.batchInsert(subList);
		}
	}
	
	private List<List<StockPriceHistory>> transformBigListToSmallList(List<StockPriceHistory> bigList) {
		int size = bigList.size();
		int subCount = size > batch_count ? ((size%1000)>0?1:0) + (size/1000) : 1;
		List<List<StockPriceHistory>> resultList = new ArrayList<List<StockPriceHistory>>();
		List<StockPriceHistory> subList = null;
		for (int i = 0; i < subCount; i++) {
			subList = new ArrayList<StockPriceHistory>();
			for (int j = i*batch_count, k = (i+1)*batch_count>size?size:(i+1)*batch_count; j < k; j++) {
				subList.add(bigList.get(j));
			}
			resultList.add(subList);
		}
		return resultList;
	}
	
	public static void main(String[] args) {
		int size = 36;
		int c = size > 10 ? ((size%10)>0?1:0) + (size/10) : 1;
		for (int i = 0; i < c; i++) {
			for (int j = i*10, k=(i+1)*10>size?size:(i+1)*10; j < k; j++) {
				System.out.println(" i = " + i + " , j = " + j);
			}
		}
		
	}

	@Override
	public List<StockPriceHistory> queryOneDayDataByDate(String date) {
		return stockPriceHistoryMapper.queryOneDayDataByDate(date);
	}

}
