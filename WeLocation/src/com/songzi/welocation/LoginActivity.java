package com.songzi.welocation;


import com.songzi.welocation.server.ConnServer;
import com.songzi.welocation.thread.LocationThread;
import com.songzi.welocation.util.Const;
import com.songzi.welocation.util.DialogFactory;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;



/**
 * 登录
 * 
 * @author guosong
 * 
 */
public class LoginActivity extends Activity implements OnClickListener ,Runnable{
	private Button mBtnRegister;
	private Button mBtnLogin;
	private EditText mAccounts, mPassword;
	//private CheckBox mAutoSavePassword;
	private ConnServer connServer;
	private MenuInflater mi;// 菜单
	private boolean logIng;
	private boolean haveConn;
	private String username;
	private String password;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		init();
		mi = new MenuInflater(this);
	}

	@Override
	protected void onResume() {// 在onResume方法里面先判断网络是否可用，再启动服务,这样在打开网络连接之后返回当前Activity时，会重新启动服务联网，
		super.onResume();
		
	}

	public void init() {
		//mAutoSavePassword = (CheckBox) findViewById(R.id.auto_save_password);

		mBtnRegister = (Button) findViewById(R.id.regist_btn);
		mBtnRegister.setOnClickListener(this);

		mBtnLogin = (Button) findViewById(R.id.login_btn);
		mBtnLogin.setOnClickListener(this);

		mAccounts = (EditText) findViewById(R.id.lgoin_accounts);
		mPassword = (EditText) findViewById(R.id.login_password);
		connServer = new ConnServer();
		logIng = true;
		haveConn = false;
	}



	/**
	 * 处理点击事件
	 */
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.regist_btn:
			goRegisterActivity();
			break;
		case R.id.login_btn:
			submit();
			break;
		default:
			break;
		}
	}

	/**
	 * 进入注册界面
	 */
	public void goRegisterActivity() {
		Intent intent = new Intent();
		intent.setClass(this, RegisterActivity.class);
		startActivity(intent);
	}

	/**
	 * 点击登录按钮后，弹出验证对话框
	 */
	private Dialog mDialog = null;

	private void showRequestDialog() {
		if (mDialog != null) {
			mDialog.dismiss();
			mDialog = null;
		}
		mDialog = DialogFactory.creatRequestDialog(this, "正在验证账号...");
		mDialog.show();
	}

	/**
	 * 提交账号密码信息到服务器
	 */
	private void submit() {
//		application.setClientStart(true);
		username = mAccounts.getText().toString();
		password = mPassword.getText().toString();
		if (username.length() == 0 || password.length() == 0) {
			DialogFactory.ToastDialog(this, "用户登录", "亲！帐号或密码不能为空哦");
		} else {
			showRequestDialog();
			// 提交注册信息
			
			new Thread(LoginActivity.this).start();//开始注册
			while (logIng) {//死等,等到服务器连接处理完成，因为服务器连接不能放到主线程了，只能另放一个线程，主线程等待服务器处理线程完成后继续
				
			}
			if (haveConn) {// 如果已连接上服务器
					if(Const.isLogSucces.equals("success")){
						mDialog.dismiss();
						//DialogFactory.ToastDialog(this, "用户登录", "亲！用户登录成功");
						Toast.makeText(this, "亲！用户登录成功", 3).show();
						Const.isLogin = true;//标示已经登录
						Const.username = username;
						Intent intent = new Intent(LoginActivity.this,FriendListActivity.class);
						LoginActivity.this.startActivity(intent);
						//开启经纬度上传
						Const.isloadLocation = true;
						LocationThread locationThread = new LocationThread();
						locationThread.start();
					}else {
						mDialog.dismiss();
						Toast.makeText(this, "亲！登录失败，请检查密码！", 3).show();
						//DialogFactory.ToastDialog(this, "用户登录", "亲！登录失败，请检查密码！");
					}
			} else {
				if (mDialog.isShowing())
					mDialog.dismiss();
					Toast.makeText(this, "亲！服务器故障，正在抢修...", 3).show();
					//DialogFactory.ToastDialog(this, "用户登录", "亲！服务器暂未开放哦");
			}

		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		// TODO Auto-generated method stub
		haveConn = connServer.logUser("logUser", username, password);
		logIng = false;
	}













}
