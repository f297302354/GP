package com.cf.gp.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cf.gp.dao.TestMapper;
import com.cf.gp.model.TestModel;
import com.cf.gp.service.TestInterface;

@Service("testService")
public class TestServiceimpl implements TestInterface {

	@Autowired
	private TestMapper testMapper;
	
	public List<TestModel> queryModel() {
		return testMapper.queryModel();
	}

}
