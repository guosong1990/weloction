/**
 * 2014年10月26日
 * guosong
 */
package com.songzi.welocation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.baidu.platform.comapi.map.m;
import com.songzi.welocation.adapter.FriendListAdapter;
import com.songzi.welocation.adapter.FriendListAdapter.ViewHolder;
import com.songzi.welocation.model.User;
import com.songzi.welocation.server.ConnServer;
import com.songzi.welocation.util.Const;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;



/**
 * @author guosong
 *
 */
public class FriendListActivity extends Activity implements OnClickListener ,OnItemClickListener,Runnable{


	private ViewGroup btnCancle = null;
	private ViewGroup btnAdd = null;
	private Button btnSelectAll = null;
	private ListView lvListView = null;
	private boolean dataEnd = true;
	private FriendListAdapter adpAdapter = null;
	private ConnServer connServer;
	private List<User> friends = new ArrayList<User>();
	private List<User> new_friends;
 	private FriendListActivity instance;
 	private final static String TAG = "FriendListActivity";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.friend_list);
		connServer = new ConnServer();
		initView();
		initData();
		instance = this;
	}

	/**
	 * 初始化控件
	 */
	private void initView() {

		btnCancle = (ViewGroup) findViewById(R.id.btnCancle);
		btnCancle.setOnClickListener(this);

		btnAdd = (ViewGroup) findViewById(R.id.btnAdd);
		btnAdd.setOnClickListener(this);


		btnSelectAll = (Button) findViewById(R.id.btnSelectAll);
		btnSelectAll.setOnClickListener(this);

		lvListView = (ListView) findViewById(R.id.lvListView);
		lvListView.setOnItemClickListener(this);

	}

	/**
	 * 初始化视图
	 */
	private void initData() {
		new Thread(FriendListActivity.this).start();
		while(dataEnd){
		}

		adpAdapter = new FriendListAdapter(this, friends);

		lvListView.setAdapter(adpAdapter);
	}

	/**
	 * 按钮点击事件
	 */
	@Override
	public void onClick(View v) {

		/*
		 * 当点击返回的时候
		 */
		if (v == btnCancle) {
			finish();

		}

		/*
		 * 当点击全选的时候
		 */
		if (v == btnSelectAll) {
			Map<Integer, Boolean> map = adpAdapter.getCheckMap();
			// 获取当前的数据数量
			int count = adpAdapter.getCount();
			// 进行遍历
			String names = "";
			for (int i = 0; i < count; i++) {
				// 因为List的特性,删除了2个item,则3变成2,所以这里要进行这样的换算,才能拿到删除后真正的position
				int position = i - (count - adpAdapter.getCount());
				if (map.get(i) != null && map.get(i)) {
					User bean = (User) adpAdapter.getItem(position);
					names += (bean.getUsername()+":");
		
				}
			}
			adpAdapter.notifyDataSetChanged();
			Const.showUserNames = names;
			Const.isShowUsers = true;
			Log.e("我想显示的用户有：", Const.showUserNames);
			Intent intent = new Intent(FriendListActivity.this,LocationActivity.class);
			FriendListActivity.this.startActivity(intent);	
		}
	}

	/**
	 * 当ListView 子项点击的时候
	 */
	@Override
	public void onItemClick(AdapterView<?> listView, View itemLayout,
			int position, long id) {

		if (itemLayout.getTag() instanceof ViewHolder) {

			ViewHolder holder = (ViewHolder) itemLayout.getTag();

			// 会自动出发CheckBox的checked事件
			holder.cbCheck.toggle();

		}

	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		// TODO Auto-generated method stub
			while(true){
				Log.e(TAG, "刷星列表");
				friends = connServer.getMyFriends("myFrends");
				dataEnd = false;
				Message message = new Message();
				message.what = 1;
				handler.sendMessage(message);
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
			}

	}
	
    //Handler
    Handler handler = new Handler(){
    	public void handleMessage(Message msg)
    	{
    		if(msg.what==1){
    			adpAdapter.notifyDataSetChanged();
    		}
    		
    	}
    };
}
