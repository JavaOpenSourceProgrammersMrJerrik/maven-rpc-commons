package com.mangocity.rpc.conn;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.junit.Test;
import com.mangocity.rpc.conn.BaseZookeeper;

//测试zookeeper的联通性
public class Client {

	@Test
	public void testConnection() throws IOException, InterruptedException, KeeperException {
		BaseZookeeper baseZookeeper = new BaseZookeeper();

		String host = "182.61.33.175:2181";

		// 连接zookeeper
		baseZookeeper.connectZookeeper(host);
		System.out.println("--------connect zookeeper ok-----------");

		// 创建节点
		byte[] data = { 1, 2, 3, 4, 5 };
		String result = baseZookeeper.createNode("/test", data);
		System.out.println(result);
		System.out.println("--------create node ok-----------");

		// 获取某路径下所有节点
		List<String> children = baseZookeeper.getChildren("/");
		for (String child : children) {
			System.out.println(child);
		}
		System.out.println("--------get children ok-----------");

		// 获取节点数据
		byte[] nodeData = baseZookeeper.getData("/test");
		System.out.println(Arrays.toString(nodeData));
		System.out.println("--------get node data ok-----------");

		// 更新节点数据
		data = "test data".getBytes();
		baseZookeeper.setData("/test", data, 0);
		System.out.println("--------set node data ok-----------");

		nodeData = baseZookeeper.getData("/test");
		System.out.println(Arrays.toString(nodeData));
		System.out.println("--------get node new data ok-----------");

		baseZookeeper.closeConnect();
		System.out.println("--------close zookeeper ok-----------");
	}

	@Test
	public void testClient() throws IOException, KeeperException, InterruptedException {
		ZooKeeper zooKeeper = new ZooKeeper("182.61.33.175:2181", 2000, new Watcher() {
			@Override
			public void process(WatchedEvent event) {
				System.out.println("first watcher process..." + event.getPath());
			}

		});
		String path = zooKeeper.create("/tester", null, Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		System.out.println("path: " + path);

		System.out.println("=======================");

		path = zooKeeper.create("/tester/goods", null, Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		System.out.println("path: " + path);

		path = zooKeeper.create("/tester/gifts", null, Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		System.out.println("path: " + path);

		/*
		 * zooKeeper.setData("/tester", "135479".getBytes(), -1);
		 * 
		 * byte[] data = zooKeeper.getData("/tester", new Watcher() {
		 * 
		 * @Override public void process(WatchedEvent event) {
		 * System.out.println("second watcher process..." + event.getPath()); }
		 * }, null);
		 * 
		 * System.out.println("data: " + new String(data));
		 */

		List<String> list = zooKeeper.getChildren("/tester", new Watcher() {
			@Override
			public void process(WatchedEvent event) {
				System.out.println("second watcher process..." + event.getPath());
			}
		});

		System.out.println("list: " + list);
	}

	@Test
	public void testCreateMultiPath() throws Exception {
		ZooKeeper zooKeeper = new ZooKeeper("182.61.33.175:2181", 2000, new Watcher() {
			@Override
			public void process(WatchedEvent event) {
				System.out.println("first watcher process..." + event.getPath());
			}
		});
		String path = zooKeeper.create("/person", null, Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		 path = zooKeeper.create("/person/boy", null, Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		System.out.println("path: " + path);
	}

}