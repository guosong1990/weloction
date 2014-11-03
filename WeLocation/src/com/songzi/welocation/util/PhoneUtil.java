/**
 * 2014年10月25日
 * guosong
 */
package com.songzi.welocation.util;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.Log;

/**
 * @author guosong
 *此类是用来获取手机相关信息的
 */
public class PhoneUtil {
	private TelephonyManager telephonyManager;
	private String IMSI;
	private Context context;
	
	public PhoneUtil(Context context){
		this.context = context;
		telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
	}
	/**
	 * 获取手机号码
	 */
	public String getNativePhoneNumber(){
		String nativePhonenumber = telephonyManager.getLine1Number();
		return nativePhonenumber;
	}
	/*
	 * 获取手机串号
	 */
	public String getDeviceId(){
		String deviceId = telephonyManager.getDeviceId();
		Log.e("这台手机的ID为：",deviceId+"");
		return deviceId;
		
	}
}
