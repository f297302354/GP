package com.cf.gp.service.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
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

import com.cf.gp.dao.StockPriceHistoryMapper;
import com.cf.gp.model.StockInfo;
import com.cf.gp.model.StockPriceHistory;
import com.cf.gp.service.StockInfoService;
import com.cf.gp.service.StockPriceHistoryService;

@Service("stockPriceHistoryService")
public class StockPriceHistoryServiceImpl implements StockPriceHistoryService {

	@Autowired
	private StockInfoService stockInfoService;
	
	@Autowired
	private StockPriceHistoryMapper stockPriceHistoryMapper;
	
	private static final String STOCK_CODE = "$stock_code";
	
	/**
	 * 根据股票表中的内容查询当前这个股票的一段时间的历史数据，只执行一次
	 */
	@SuppressWarnings("finally")
	@Transactional
	public boolean queryHistoryDataWithInsertDB() {
		boolean resultFlag = false;
		CloseableHttpClient client = HttpClients.createDefault();
		String start_date = "20170501";
		String end_date = "20180109";
		String url = "http://q.stock.sohu.com/hisHq?code=cn_$stock_code&start="+start_date+"&end="+end_date+"&stat=1&order=D&period=d&callback=historySearchHandler&rt=jsonp&r=0.8391495715053367&0.9677250558488026";
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		try {
			List<StockInfo> selectAll = stockInfoService.selectAll();
			for (int i = 0; i < selectAll.size(); i++) {
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
	}
	
	public String[] stringDataTransformArray(String data) {
		String substring = data.substring(data.indexOf("\"hq\":[")+"\"hq\":[".length(), data.indexOf("],\"code\""));
		return substring.replace("],", "").replace("]", "").replace("\"", "").substring(1).replace("[", "@").split("@");
	}

}
