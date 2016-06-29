package com.mangocity.test;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.mangocity.rpc.proxy.RpcProxy;
import com.mangocity.rpc.service.IHelloService;

public class HelloServiceTest {

	private RpcProxy rpcProxy;

	@Test
	@SuppressWarnings("resource")
	public void testHelloTest() {
		ApplicationContext cxt = new ClassPathXmlApplicationContext("applicationContext-client.xml");
		rpcProxy = cxt.getBean("rpcProxy", RpcProxy.class);
		IHelloService helloService = rpcProxy.create(IHelloService.class);
		String result = helloService.hello("World");
		System.out.println("result: " + result);
	}
}
