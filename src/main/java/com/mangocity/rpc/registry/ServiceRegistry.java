package com.mangocity.rpc.registry;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mangocity.rpc.common.Constant;

//实现服务注册 zookeeper的注册中心就充当了RMI的JNDI
public class ServiceRegistry {

	private static final Logger LOGGER = LoggerFactory.getLogger(ServiceRegistry.class);

	private CountDownLatch latch = new CountDownLatch(1);

	private String registryAddress;

	public ServiceRegistry(String registryAddress) {
		this.registryAddress = registryAddress;
	}

	public void register(String data) {
		if (data != null) {
			ZooKeeper zk = connectServer();
			if (zk != null) {
				try {
					Stat stat = zk.exists(Constant.ZK_DATA_PATH, false);
					if(null == stat){
						createNode(zk, data);
					}else{
						setData(zk,data);
					}
				} catch (KeeperException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
			}
		}
	}


	private ZooKeeper connectServer() {
		LOGGER.info("ServiceRegistry connectServer begin()...");
		ZooKeeper zk = null;
		try {
			zk = new ZooKeeper(registryAddress, Constant.ZK_SESSION_TIMEOUT, new Watcher() {
				@Override
				public void process(WatchedEvent event) {
					if (event.getState() == Event.KeeperState.SyncConnected) {
						latch.countDown();
					}
				}
			});
			latch.await();
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
		return zk;
	}

	private void createNode(ZooKeeper zk, String data) {
		LOGGER.info("ServiceRegistry createNode begin()...data: " + data);
		try {
			byte[] bytes = data.getBytes();
			String path = zk.create(Constant.ZK_REGISTRY_PATH, bytes,Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
			path = zk.create(Constant.ZK_DATA_PATH, bytes, Ids.OPEN_ACL_UNSAFE,
					CreateMode.PERSISTENT);
			LOGGER.debug("create zookeeper node ({} => {})", path, data);
		} catch (KeeperException e) {
			LOGGER.error("ServiceRegistry createNode failure. " + e.getMessage(), e);
		}catch(InterruptedException e){
			LOGGER.error("ServiceRegistry createNode failure. " + e.getMessage(), e);
		}
	}
	
	private void setData(ZooKeeper zk, String data) {
		byte[] bytes = data.getBytes();
		try {
			zk.setData(Constant.ZK_DATA_PATH, bytes, -1);//-1是version
		}catch (KeeperException e) {
			LOGGER.error("ServiceRegistry createNode failure. " + e.getMessage(), e);
		}catch(InterruptedException e){
			LOGGER.error("ServiceRegistry createNode failure. " + e.getMessage(), e);
		}
	}
}
