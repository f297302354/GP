package com.cf.gp.service.impl;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.cf.gp.dao.StockInfoMapper;
import com.cf.gp.model.StockInfo;
import com.cf.gp.model.StockTranformVo;
import com.cf.gp.service.StockInfoService;

@Service("stockInfoService")
public class StockInfoServiceImpl implements StockInfoService {

	@Autowired
	private StockInfoMapper stockInfoMapper;
	
	private static final String START_PAGE_FLAG = "$start_page";
	
	private static final String SH = "sh";
	
	private static final String SZ = "sz";
	
	private static final String SH_FLAG = "0";
	
	private static final String SZ_FLAG = "1";
	
	/**
	 * 根据事实的股票数据把获取的所有股票插入到股票基础信息表中，只执行一次就行了，未来新股的增量部分还没有考虑进去
	 */
	@SuppressWarnings("finally")
	@Transactional
	public boolean queryInfoWithInsertDB() {
		boolean resultFlag = false;
		boolean exeFlag = true;
		int startPage = 1;
		String url = "http://vip.stock.finance.sina.com.cn/quotes_service/api/json_v2.php/Market_Center.getHQNodeData?page=$start_page&num=100&sort=symbol&asc=1&node=hs_a&symbol=&_s_r_a=init";
		CloseableHttpClient client = HttpClients.createDefault();
		Date d = new Date();
		try {
			while (exeFlag) {
				HttpGet get = new HttpGet(url.replace(START_PAGE_FLAG, String.valueOf(startPage)));
				CloseableHttpResponse resp = client.execute(get);
				String result = EntityUtils.toString(resp.getEntity());
				if (result == null) {
					exeFlag = false;
				} else {
					List<StockTranformVo> resultList = JSON.parseArray(result, StockTranformVo.class);
					for (int i = 0; i < resultList.size(); i++) {
						StockTranformVo stockTranformVo = resultList.get(i);
						StockInfo single = new StockInfo();
						single.setsCode(stockTranformVo.getCode());
						single.setsDate(d);
						single.setsName(stockTranformVo.getName());
						single.setsType(stockTranformVo.getSymbol().contains(SH)?SH_FLAG:SZ_FLAG);
						stockInfoMapper.insert(single);
					}
					startPage++;
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
	
	

}
