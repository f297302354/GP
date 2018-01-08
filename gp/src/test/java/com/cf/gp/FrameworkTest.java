package com.cf.gp;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.cf.gp.model.TestModel;
import com.cf.gp.service.TestInterface;

public class FrameworkTest {

	private ClassPathXmlApplicationContext app;
	
	@Before
	public void init() {
		app = new ClassPathXmlApplicationContext(new String[]{"spring/spring.xml","mybatis/spring-mybatis.xml"});
	}
	
	@Test
	public void test() {
		TestInterface bean = (TestInterface)app.getBean("testService");
		List<TestModel> queryModel = bean.queryModel();
		System.out.println(queryModel.size());
	}
	
}
