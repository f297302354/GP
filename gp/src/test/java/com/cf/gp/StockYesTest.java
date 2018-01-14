package com.cf.gp;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.cf.gp.model.StockYesParam;
import com.cf.gp.service.StockYesService;

public class StockYesTest {

	private ClassPathXmlApplicationContext app;
	
	@Before
	public void init() {
		app = new ClassPathXmlApplicationContext(new String[]{"spring/spring.xml","mybatis/spring-mybatis.xml"});
	}
	
	/*@Test
	@Deprecated
	public void queryInfoWithInsertDB() {
		//第一次 exec time = 3134509（52分钟） - 太慢了
		//第二次 exec time = 11596（11秒） 只加了一个code的索引列 50多万的数据
		//第三次 exec time = 24698 加了code_name_date的复合的索引列 100万的数据
		//第四次 exec time = 26710 加了code_name的复合的索引、date的单独索引列的 100万的数据
		long start = System.currentTimeMillis();
		StockYesService stockYesService = (StockYesService)app.getBean("stockYesService");
		boolean result = stockYesService.execYes("2018-01-12");
		long end = System.currentTimeMillis();
		System.out.println(result);
		System.out.println("exec time = " + String.valueOf(end - start));
	}*/
	
	@Test
	public void queryInfoWithInsertDB() {
		//第一次 exec time = 16549 比上面的方法快了10秒左右
		long start = System.currentTimeMillis();
		StockYesService stockYesService = (StockYesService)app.getBean("stockYesService");
		StockYesParam param = new StockYesParam();
		param.setDate("2018-01-12");
		param.setDiffVal(0.03d);
		param.setDownAvgDay(318);
		param.setHavdAvgDay(318);
		param.setCalculdateFlag("==");
		boolean result = stockYesService.execYes(param);
		long end = System.currentTimeMillis();
		System.out.println(result);
		System.out.println("exec time = " + String.valueOf(end - start));
	}
	
}
