package com.rbp.main.client.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.rbp.main.client.util.Constant;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.CuratorEvent;
import org.apache.curator.framework.api.CuratorListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.data.Stat;
import org.springframework.stereotype.Service;

@Service
public class ZookeeperService {
	
	private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.sss");
	
	public void getConnnection(final String connectString){
		RetryPolicy retryPolicy = new ExponentialBackoffRetry(2000, 3);
		Constant.curatorFramework = CuratorFrameworkFactory.newClient(connectString, retryPolicy);
		//添加监听
		Constant.curatorFramework.getCuratorListenable().addListener(new CuratorListener() {

			@Override
			public void eventReceived(CuratorFramework client, CuratorEvent event) throws Exception {
				String server = client.getZookeeperClient().getCurrentConnectionString();
				System.out.println(server);
				System.out.println(event.getType().toString());
				if(event.getType().toString().equals("WATCHED")){
					WatchedEvent watchedEvent = event.getWatchedEvent();
					if(watchedEvent.getState().toString().equals("SyncConnected")){
						if(watchedEvent.getType().toString().equals("None")){

						}
						if(watchedEvent.getType().toString().equals("NodeDataChanged")){

						}
					}
				}
			}
		});
		Constant.curatorFramework.start();
	}
	
	public void closeConnection(){
		if(Constant.curatorFramework != null){
			if(Constant.curatorFramework.isStarted()){
				Constant.curatorFramework.close();
			}
		}
	}
	
	public List<String> getChildZnode(String parentZnode,boolean watched) throws Exception{
		if(null == Constant.curatorFramework || !Constant.curatorFramework.isStarted()){
			return null;
		}
		if(watched){
			return Constant.curatorFramework.getChildren().watched().forPath(parentZnode);
		}
		return Constant.curatorFramework.getChildren().forPath(parentZnode);
	}
	
	public String getZnodeData(String znode,boolean watcher) throws Exception{
		if(null == Constant.curatorFramework || !Constant.curatorFramework.isStarted()){
			return null;
		}
		byte[] b = null;
		try {
			if(watcher){
				b = Constant.curatorFramework.getData().watched().forPath(znode);
			}else{
				b = Constant.curatorFramework.getData().forPath(znode);
			}
		} catch (Exception e) {
			return null;
		}
		if(null == b){
			return "null";
		}
		return new String(b);
	}
	
	public Stat getZnodeStat(String znode) throws Exception{
		if(null == Constant.curatorFramework || !Constant.curatorFramework.isStarted()){
			return null;
		}
		return Constant.curatorFramework.checkExists().forPath(znode);
	}
	
	/**
	 * 创建子节点逻辑
	 * @param znodeName 子节点名称
	 * @param content 子节点内容
	 * @param znodeType 子节点类型
	 * @return
	 * @throws Exception
	 */
	public String createNewZnode(String znodeName,String content,String znodeType) throws Exception{
		if(null == Constant.curatorFramework || !Constant.curatorFramework.isStarted()){
			return null;
		}
		//创建持久化节点
		if(znodeType.equals("PERSISTENT")){
			try {
				Constant.curatorFramework.create().withMode(CreateMode.PERSISTENT)
				.forPath(znodeName, content.getBytes());
			} catch (Exception e) {
				return e.getMessage();
			}
		}
		//创建持久化序列节点
		if(znodeType.equals("PERSISTENT_SEQUENTIAL")){
			try {
				Constant.curatorFramework.create().withMode(CreateMode.PERSISTENT_SEQUENTIAL)
				.forPath(znodeName, content.getBytes());
			} catch (Exception e) {
				return e.getMessage();
			}
		}
		//创建临时节点
		if(znodeType.equals("EPHEMERAL")){
			try {
				Constant.curatorFramework.create().withMode(CreateMode.EPHEMERAL)
				.forPath(znodeName, content.getBytes());
			} catch (Exception e) {
				return e.getMessage();
			}
		}
		//创建临时序列节点 
		if(znodeType.equals("EPHEMERAL_SEQUENTIAL")){
			try {
				Constant.curatorFramework.create().withMode(CreateMode.EPHEMERAL_SEQUENTIAL)
				.forPath(znodeName, content.getBytes());
			} catch (Exception e) {
				return e.getMessage();
			}
		}
		return "OK";
	}
	
	public String delZnodeContainsChild(String path){
		List<String> childList = new ArrayList<String>();
		try {
			getZnodeContainsChild(path,childList,Constant.curatorFramework);
		} catch (Exception e) {
			return "error";
		}
		int count = 0;
		for (String childZnode : childList) {
			try {
				Constant.curatorFramework.delete().forPath(childZnode);
				count++;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return count + "";
	}
	
	public void getZnodeContainsChild(String path,List<String> childZnodes,CuratorFramework curatorFramework) throws Exception{
		List<String> childList = curatorFramework.getChildren().forPath(path);
		if(childList.size() != 0){
			for (String child : childList) {
				getZnodeContainsChild(path + "/" + child,childZnodes,curatorFramework);
			}
			childZnodes.add(path);
		}else{
			childZnodes.add(path);
		}
	}
	
	public String changeZnodeContent(String znodeName,String content) throws Exception{
		if(null == Constant.curatorFramework || !Constant.curatorFramework.isStarted()){
			return null;
		}
		try {
			Constant.curatorFramework.setData().forPath(znodeName, content.getBytes());
		} catch (Exception e) {
			return null;
		}
		return "OK";
	}
}
