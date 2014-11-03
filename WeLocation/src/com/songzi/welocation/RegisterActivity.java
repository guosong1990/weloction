/**
 * 2014年10月26日
 * guosong
 */
package com.songzi.welocation;

import com.songzi.welocation.model.User;
import com.songzi.welocation.server.ConnServer;
import com.songzi.welocation.util.Const;
import com.songzi.welocation.util.DialogFactory;

import android.R.bool;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;


public class RegisterActivity extends Activity implements OnClickListener,Runnable {

	private Button mBtnRegister;
	private Button mRegBack;
	private EditText mUsername, mTelphone, mPasswdEt, mPasswdEt2;
	private ConnServer connServer;
	private User user;
	public RegisterActivity instance;
	private boolean regIng;//正在注册
	private boolean haveConn;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.register);
		instance = this;
		init();
	}
	
	public void init() {
		mBtnRegister = (Button) findViewById(R.id.register_btn);
		mRegBack = (Button) findViewById(R.id.reg_back_btn);
		mBtnRegister.setOnClickListener(this);
		mRegBack.setOnClickListener(this);

		mUsername = (EditText) findViewById(R.id.username);
		mTelphone = (EditText) findViewById(R.id.telphone);
		mPasswdEt = (EditText) findViewById(R.id.reg_password);
		mPasswdEt2 = (EditText) findViewById(R.id.reg_password2);
		
		connServer = new ConnServer();
		user = new User();
		regIng = true;
		haveConn = false;
	}

	private Dialog mDialog = null;

	private void showRequestDialog() {
		if (mDialog != null) {
			mDialog.dismiss();
			mDialog = null;
		}
		mDialog = DialogFactory.creatRequestDialog(instance, "正在注册中...");
		mDialog.show();
	}





	private void estimate() {
		String username = mUsername.getText().toString();
		String telphone = mTelphone.getText().toString();
		String passwd = mPasswdEt.getText().toString();
		String passwd2 = mPasswdEt2.getText().toString();
		user.setUsername(username);
		user.setTelpone(telphone);
		user.setPassword(passwd);
		if (username.equals("") || telphone.equals("") || passwd.equals("")
				|| passwd2.equals("")) {
			DialogFactory.ToastDialog(RegisterActivity.this, "用户注册",
					"亲！带*项是不能为空的哦");
		} else {
			if (passwd.equals(passwd2)) {
				showRequestDialog();
				// 提交注册信息
				
				new Thread(RegisterActivity.this).start();//开始注册
				while (regIng) {//死等,等到服务器连接完成，因为服务器连接不能放到主线程了，只能另放一个线程
					
				}
				if (haveConn) {// 如果已连接上服务器
						if(Const.isRegSucces.equals("success")){
							mDialog.dismiss();
							DialogFactory.ToastDialog(this, "用户注册", "亲！用户注册成功");
						}else {
							DialogFactory.ToastDialog(this, "用户注册", "亲！用户注册失败，用户名或手机已经注册");
						}
				} else {
					if (mDialog.isShowing())
						mDialog.dismiss();
					DialogFactory.ToastDialog(this, "用户注册", "亲！服务器暂未开放哦");
				}

			} else {
				DialogFactory.ToastDialog(RegisterActivity.this, "用户注册",
						"亲！您两次输入的密码不同哦");
			}
		}
	}

	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.reg_back_btn:
			this.finish();
			break;
		case R.id.register_btn:
			estimate();
		default:
			break;
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		// TODO Auto-generated method stub
		haveConn = connServer.regUser("regUser", user);
		regIng = false;//结束死等
	}


	
}
