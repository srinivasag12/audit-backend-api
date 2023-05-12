package com.api.central.audit.delegate;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Component;

import com.api.central.audit.entity.SocketBean;

@Component
public class SocketDelegateImpl implements SocketDelegate{

	@Override
	public List<SocketBean> getMessage(String username) {
		System.out.println("username :"+username);
		Random rand = new Random(); Integer value = rand.nextInt(50000); 
		List<SocketBean> beans = new ArrayList<SocketBean>();
		beans.add(new SocketBean(value.toString()));
		return beans;
	}

}
