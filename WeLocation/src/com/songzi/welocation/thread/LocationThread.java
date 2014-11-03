/**
 * 2014年10月28日
 * guosong
 */
package com.songzi.welocation.thread;

import com.songzi.welocation.server.ConnServer;
import com.songzi.welocation.util.Const;

/**
 * @author guosong
 *
 */
public class LocationThread extends Thread {

	private ConnServer connServer = new ConnServer();
	public void run() {
		while(Const.isloadLocation){
			//上传我的位置
			connServer.upMyLatLng("upMyLocation",Const.username);
			try {
				Thread.sleep(Const.TIME);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}if(Const.isShowUsers){
				connServer.getMyFriends("myFrends");
			}
		}
	}

}
