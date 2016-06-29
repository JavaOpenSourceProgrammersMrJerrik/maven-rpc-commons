package com.mangocity.rpc.bootstrap;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class RpcBootstrap {

	@SuppressWarnings("resource")
	public static void main(String[] args) {
		new ClassPathXmlApplicationContext("applicationContext.xml");
	}
}
