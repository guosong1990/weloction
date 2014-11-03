package com.songzi.welocation;


import java.util.Iterator;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.TextOptions;
import com.baidu.mapapi.model.LatLng;
import com.songzi.welocation.model.User;
import com.songzi.welocation.server.ConnServer;
import com.songzi.welocation.util.Const;
import com.songzi.welocation.util.PhoneUtil;
import com.songzi.welocation.view.LoginView;

/**
 * 此demo用来展示如何结合定位SDK实现定位，并使用MyLocationOverlay绘制定位位置 同时展示如何使用自定义图标绘制并点击时弹出泡泡
 * 
 */
public class LocationActivity extends Activity implements Runnable{

	// 定位相关
	LocationClient mLocClient;
	public MyLocationListenner myListener = new MyLocationListenner();
	private LocationMode mCurrentMode;
	BitmapDescriptor mCurrentMarker;

	MapView mMapView;
	BaiduMap mBaiduMap;

	// UI相关
	OnCheckedChangeListener radioButtonListener;
	Button requestLocButton;
	boolean isFirstLoc = true;// 是否首次定位
	
	private ConnServer server;
	private PhoneUtil phoneUtil;
	private TextView locationInfo;
	private TextView myinfo;
	private LoginView mLoginView;
	private View baiduMapView;
	private ImageView btn_login;
	private boolean isFriLocation;
	private LinearLayout login;
	private LinearLayout regite;
	private TextView acitonbar_text;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		initView();
		server = new ConnServer();
		phoneUtil = new PhoneUtil(this);
		Const.IMEI = phoneUtil.getDeviceId();
		requestLocButton = (Button) findViewById(R.id.button1);
		locationInfo = (TextView) findViewById(R.id.textView1);
		myinfo = (TextView) findViewById(R.id.textView2);
		mCurrentMode = LocationMode.NORMAL;
		requestLocButton.setText("普通");
		OnClickListener btnClickListener = new OnClickListener() {
			public void onClick(View v) {
				switch (mCurrentMode) {
				case NORMAL:
					requestLocButton.setText("跟随");
					mCurrentMode = LocationMode.FOLLOWING;
					mBaiduMap
							.setMyLocationConfigeration(new MyLocationConfiguration(
									mCurrentMode, true, mCurrentMarker));
					break;
				case COMPASS:
					requestLocButton.setText("普通");
					mCurrentMode = LocationMode.NORMAL;
					mBaiduMap
							.setMyLocationConfigeration(new MyLocationConfiguration(
									mCurrentMode, true, mCurrentMarker));
					break;
				case FOLLOWING:
					requestLocButton.setText("罗盘");
					mCurrentMode = LocationMode.COMPASS;
					mBaiduMap
							.setMyLocationConfigeration(new MyLocationConfiguration(
									mCurrentMode, true, mCurrentMarker));
					break;
				}
			}
		};
		requestLocButton.setOnClickListener(btnClickListener);

		RadioGroup group = (RadioGroup) this.findViewById(R.id.radioGroup);
		radioButtonListener = new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if (checkedId == R.id.defaulticon) {
					// 传入null则，恢复默认图标
					mCurrentMarker = null;
					mBaiduMap
							.setMyLocationConfigeration(new MyLocationConfiguration(
									mCurrentMode, true, null));
				}
				if (checkedId == R.id.customicon) {
					// 修改为自定义marker
					mCurrentMarker = BitmapDescriptorFactory
							.fromResource(R.drawable.icon_myself);
					mBaiduMap
							.setMyLocationConfigeration(new MyLocationConfiguration(
									mCurrentMode, true, mCurrentMarker));
				}
			}
		};
		group.setOnCheckedChangeListener(radioButtonListener);

		// 地图初始化
		mMapView = (MapView) findViewById(R.id.bmapView);
		mBaiduMap = mMapView.getMap();
		// 开启定位图层
		mBaiduMap.setMyLocationEnabled(true);
		// 定位初始化
		mLocClient = new LocationClient(this);
		mLocClient.registerLocationListener(myListener);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// 打开gps
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setScanSpan(10000);
		mLocClient.setLocOption(option);
		mLocClient.start();
		
		//开启线程画好友的位置
		Thread thread = new Thread(this);
		thread.start();

	}
	/**
	 * 初始化中界面，不包括地图
	 * 2014年10月26日
	 */
    private void initView() {
    	acitonbar_text = (TextView) findViewById(R.id.acitonbar_text);
    	mLoginView = (LoginView)findViewById(R.id.mLoginView);
    	baiduMapView = (View)findViewById(R.id.baiduMap);
    	btn_login = (ImageView)findViewById(R.id.btn_login);
    	login = (LinearLayout) findViewById(R.id.ll_login);
    	regite = (LinearLayout) findViewById(R.id.ll_reg);
    	mLoginView.setEnabled(true);
    	
    	if(Const.isLogin){
    		acitonbar_text.setText(Const.username);
    	}
    	//设置遮罩阴影层点击消失该界面
    	baiduMapView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(mLoginView.isShow()){
					mLoginView.dismiss();
				}
			}
		});
    	//展现登录框监听
    	btn_login.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!Const.isLogin){//是否登录
					if(mLoginView.isShow()){
						mLoginView.dismiss();
					}else{
						mLoginView.show();
					}
				}else{
					Intent intent = new Intent(LocationActivity.this,FriendListActivity.class);
					LocationActivity.this.startActivity(intent);
				}

			}
		});
    	//登录按钮监听
    	login.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				isFriLocation = false;
				Intent intent = new Intent(LocationActivity.this,LoginActivity.class);
				LocationActivity.this.startActivity(intent);
			}
		});
    	regite.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				isFriLocation = false;
				Intent intent = new Intent(LocationActivity.this,RegisterActivity.class);
				LocationActivity.this.startActivity(intent);
			}
		});
	}


	/**
	 * 定位SDK监听函数
	 */
	public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			// map view 销毁后不在处理新接收的位置
			if (location == null || mMapView == null)
				return;
			MyLocationData locData = new MyLocationData.Builder()
					.accuracy(location.getRadius())
					// 此处设置开发者获取到的方向信息，顺时针0-360
					.direction(100).latitude(location.getLatitude())
					.longitude(location.getLongitude()).build();
			//Log.e("我的经度", location.getLongitude()+"");
			//Log.e("我的维度", location.getLatitude()+"");
			mBaiduMap.setMyLocationData(locData);
			Const.myLocation = location.getLatitude()+":"+location.getLongitude();
			Message message = handler.obtainMessage();
			message.what=1;
			handler.sendMessage(message);
			//Log.e("我的位置", Const.myLocation);
			if (isFirstLoc) {
				isFirstLoc = false;
				LatLng ll = new LatLng(location.getLatitude(),
						location.getLongitude());
				MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
				mBaiduMap.animateMapStatus(u);
			}
		}

		public void onReceivePoi(BDLocation poiLocation) {
		}
	}

	@Override
	protected void onPause() {
		mMapView.onPause();
   		if(mLoginView.isShow()){
			mLoginView.dismiss();
		}
		super.onPause();
	}

	@Override
	protected void onResume() {
		mMapView.onResume();
    	if(Const.isLogin){
    		acitonbar_text.setText(Const.username);
    	}
		super.onResume();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
    	if(Const.isLogin){
    		acitonbar_text.setText(Const.username);
    	}
		super.onStart();
	}
	@Override
	protected void onDestroy() {
		// 退出时销毁定位
		mLocClient.stop();
		// 关闭定位图层
		mBaiduMap.setMyLocationEnabled(false);
		mMapView.onDestroy();
		mMapView = null;

		//开启退出线程
		if(!Const.username.equals("请登录")){//代表已登录
			Const.isExit = true;
			Const.isShowUsers = false;
			Thread  thread = new Thread(this);
			thread.start();	
		}
		super.onDestroy();
	}

	@Override
	public void run() {
		while(Const.isShowUsers){
			// TODO Auto-generated method stub
			//以下是在地图上再加一个覆盖物表示好友的位置
			//定义Maker坐标点  
			double lat = 0.0;
			double lng = 0.0;
			//清除上次的图标
			mBaiduMap.clear();
			for (Iterator iterator = Const.myFriendsList.iterator(); iterator.hasNext();) {
				User user = (User) iterator.next();
				if(Const.showUserNames.contains(user.getUsername())){
			    	String latLng = user.getLatlngLately();
					Log.e("好友的位置->>>>>>>>>>>>>>>>>>", latLng);
			    	String[] ll = latLng.split(":");
			    	if(ll[0].equals("")||ll[1].equals("")){//在新启动的一瞬间可能为空排除掉，不然会报错Const的静态变量什么时候才能实例化？？？？
			    		return;
			    	}
			    	lat = Double.parseDouble(ll[0]);
			    	lng = Double.parseDouble(ll[1]);
			    	LatLng point = new LatLng(lat, lng);
			    	
					//Log.e("好友的位置：", Const.lat_friends+":"+Const.lng_friends);
			
			    	OverlayOptions textOption = new TextOptions()  
			        .fontSize(25)  
			        .fontColor(0xFFFF0000)  
			        .text(user.getUsername())  
			        .position(point);
			    	
					//构建Marker图标  
					BitmapDescriptor bitmap = BitmapDescriptorFactory  
					    .fromResource(R.drawable.icon_friend);  
					//构建MarkerOption，用于在地图上添加Marker  
					OverlayOptions bg = new MarkerOptions()  
					    .position(point)  
					    .icon(bitmap);  
					
					
					//在地图上添加Marker，并显示  
					mBaiduMap.addOverlay(textOption);
					mBaiduMap.addOverlay(bg);
				}
			}
	
			//server.getData("getLatLng");
			//server.upMyLatLng("upMyLocation");
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if(Const.isExit){
			Log.e("用户退出", "---------");
			server.logout("exit");
		}
	}
	
	
	
    //Handler
    Handler handler = new Handler(){
    	public void handleMessage(Message msg)
    	{
    		switch(msg.what)
    		{
    		case 0:
    			locationInfo.setText("好友的经纬度："+Const.lat_friends+":"+Const.lng_friends);
    			break;
    		case 1:
    			myinfo.setText("我的位置是是："+Const.myLocation);
    			break;
    		case 2:
    			
    			break;
    		
    		}
    		
    	}
    };
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	// TODO Auto-generated method stub
    	if(keyCode ==  KeyEvent.KEYCODE_BACK){
    		if(mLoginView.isShow()){
    			mLoginView.dismiss();
    			return true;
    		}
    		isFriLocation = false;
    		finish();
    	}
    	return super.onKeyDown(keyCode, event);
    }
    
	/**
	 * 处理后台来的字符串，转换为相应的
	 */
    public void doStringToLatLng(){
    	String latLng = Const.message;
		Log.e("好友的位置->>>>>>>>>>>>>>>>>>", latLng);
    	String[] ll = latLng.split(":");
    	if(ll[0].equals("")||ll[1].equals("")){//在新启动的一瞬间可能为空排除掉，不然会报错Const的静态变量什么时候才能实例化？？？？
    		return;
    	}
    	Const.lat_friends = Double.parseDouble(ll[0]);
    	Const.lng_friends = Double.parseDouble(ll[1]);
    }
    
}
