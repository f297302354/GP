package com.cf.gp;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.cf.gp.service.StockYesService;

public class StockYesTest {

	private ClassPathXmlApplicationContext app;
	
	@Before
	public void init() {
		app = new ClassPathXmlApplicationContext(new String[]{"spring/spring.xml","mybatis/spring-mybatis.xml"});
	}
	
	@Test
	public void queryInfoWithInsertDB() {
		//第一次 exec time = 3134509（52分钟） - 太慢了
		//第二次 exec time = 11596（11秒） 只加了一个code的索引列
		long start = System.currentTimeMillis();
		StockYesService stockYesService = (StockYesService)app.getBean("stockYesService");
		boolean result = stockYesService.execYes("2018-01-10");
		long end = System.currentTimeMillis();
		System.out.println(result);
		System.out.println("exec time = " + String.valueOf(end - start));
	}
	
}
