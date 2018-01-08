package com.cf.gp;

import java.util.List;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.cf.gp.model.TestModel;
import com.cf.gp.service.TestInterface;

public class FrameworkTest {

	public static void main(String[] args) {
		ClassPathXmlApplicationContext app = new ClassPathXmlApplicationContext(new String[]{"spring/spring.xml","mybatis/spring-mybatis.xml"});
		TestInterface bean = (TestInterface)app.getBean("testService");
		List<TestModel> queryModel = bean.queryModel();
		System.out.println(queryModel.size());
	}
	
}
