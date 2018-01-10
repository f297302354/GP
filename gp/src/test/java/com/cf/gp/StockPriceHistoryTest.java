package com.cf.gp;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.cf.gp.service.StockPriceHistoryService;

public class StockPriceHistoryTest {

	private ClassPathXmlApplicationContext app;
	
	@Before
	public void init() {
		app = new ClassPathXmlApplicationContext(new String[]{"spring/spring.xml","mybatis/spring-mybatis.xml"});
	}
	
	/**
	 * 区间数据
	 */
//	@Test
	public void queryInfoWithInsertDB() {
		//第一次 exec time = 43536 - 59466条记录
		//第二次 exec time = 373330 - 527369条记录
		long start = System.currentTimeMillis();
		StockPriceHistoryService stockPriceHistoryService = (StockPriceHistoryService)app.getBean("stockPriceHistoryService");
		boolean result = stockPriceHistoryService.queryHistoryDataWithInsertDB("20170501", "20180109");
		long end = System.currentTimeMillis();
		System.out.println(result);
		System.out.println("exec time = " + String.valueOf(end - start));
	}
	
	/**
	 * 实时数据 - 新浪的请求多了会封IP
	 */
	@Test
	public void queryDataWithInsertDB() {
		//第一次 exec time = 31160 - 59466条记录 （30多次http）
		long start = System.currentTimeMillis();
		StockPriceHistoryService stockPriceHistoryService = (StockPriceHistoryService)app.getBean("stockPriceHistoryService");
		boolean result = stockPriceHistoryService.queryDataWithInsertDB();
		long end = System.currentTimeMillis();
		System.out.println(result);
		System.out.println("exec time = " + String.valueOf(end - start));
	}
	
}
