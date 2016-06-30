package com.mangocity.rpc.service.impl;

import com.mangocity.rpc.enums.RpcService;
import com.mangocity.rpc.service.IHelloService;

@RpcService(IHelloService.class)
public class HelloServiceImpl implements IHelloService {

	@Override
	public String hello(String name) {
		return "The first rpc framework...hello " + name;
	}
}
