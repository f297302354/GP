package com.cf.gp;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.cf.gp.service.StockInfoService;

public class StockInfoTest {

	private ClassPathXmlApplicationContext app;
	
	@Before
	public void init() {
		app = new ClassPathXmlApplicationContext(new String[]{"spring/spring.xml","mybatis/spring-mybatis.xml"});
	}
	
	@Test
	public void queryInfoWithInsertDB() {
		//第一次 exec time = 27147 - 3411条记录
		long start = System.currentTimeMillis();
		StockInfoService stockInfoService = (StockInfoService)app.getBean("stockInfoService");
		boolean result = stockInfoService.queryInfoWithInsertDB();
		long end = System.currentTimeMillis();
		System.out.println(result);
		System.out.println("exec time = " + String.valueOf(end - start));
	}
	
}
