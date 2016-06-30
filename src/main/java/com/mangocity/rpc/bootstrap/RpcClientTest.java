package com.mangocity.rpc.bootstrap;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.mangocity.rpc.proxy.RpcProxy;
import com.mangocity.rpc.service.IHelloService;

public class RpcClientTest {
	public static void main(String[] args) {
		ApplicationContext cxt = new ClassPathXmlApplicationContext(
				"applicationContext-client.xml");
		RpcProxy rpcProxy = cxt.getBean("rpcProxy", RpcProxy.class);
		IHelloService helloService = rpcProxy.create(IHelloService.class);
		String result = helloService.hello("World");
		System.out.println("result: " + result);
	}
}
