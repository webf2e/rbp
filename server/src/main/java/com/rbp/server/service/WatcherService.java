package com.rbp.server.service;

import java.text.SimpleDateFormat;

import com.rbp.server.util.Constant;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WatcherService {

	@Autowired
	private ZookeeperService zookeeperService;
	
	private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.sss");
	
	public WatcherService(){}
	/**
	 * 节点注册监听
	 * @param zk zookeeper的连接字符串
	 * @param znodeName 监听的节点名称
	 */
	public void watchChild(String znodeName) throws Exception {
		PathChildrenCache cache = new PathChildrenCache(Constant.curatorFramework, znodeName, true);
		PathChildrenCacheListener plis = new PathChildrenCacheListener() {
			/**
			 * 监听处理逻辑
			 */
			@Override
			public void childEvent(CuratorFramework client,
					PathChildrenCacheEvent event) throws Exception {
				if(null == event || null == event.getData()){
					return;
				}
				if(event.getType() == PathChildrenCacheEvent.Type.CHILD_ADDED){
					System.out.println(event.getData().getPath());
					System.out.println(new String(event.getData().getData()));
					System.out.println("child_add");
					zookeeperService.delZnodeContainsChild(event.getData().getPath());
				}else if(event.getType() == PathChildrenCacheEvent.Type.CHILD_UPDATED){
					System.out.println("child_update");
				}else if(event.getType() == PathChildrenCacheEvent.Type.CHILD_REMOVED){
					System.out.println("child_removed");
				}
			}
		};
		//注册并开始监听 
		cache.getListenable().addListener(plis);
		cache.start();
	}

}
