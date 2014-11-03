package com.songzi.welocation.util;

import java.util.ArrayList;
import java.util.List;

import com.songzi.welocation.model.User;

import android.R.integer;

public class Const {
	public final static String FRIURL = "http://192.168.1.104:8080/server/FriendServlet";//获取好友信息的URL
	public final static String MYURL = "http://192.168.1.104:8080/server/MyServlet";//处理自己的URL
	public final static String PARAM = "methodtype";
	public static final int REQUEST_TIMEOUT = 5*1000;//设置请求超时10秒钟  
	public static final int SO_TIMEOUT = 10*1000;  //设置等待数据超时时间10秒钟  
	public static String message = "0.0:0.0";
	public static double lat_friends;//朋友的维度
	public static double lng_friends;//朋友的经度
	public static double lat_my;//我的维度
	public static double lng_my;//我的经度
	public static String myLocation = "0.0:0.0";
	public static String IMEI;
	public static String isRegSucces = "false";
	public static String isLogSucces = "false";
	
	public static boolean isLogin = false;
	public static String username = "请登录";
	
	public static String myFriends = "";
	
	public final static int TIME = 4000; //上传位置时间间隙
	public static boolean isloadLocation = false;
	public static String showUserNames = "";//存放我要现实的好友名字表 
	public static boolean isShowUsers = false;
	
	public static boolean isExit = false;
	public static List<User> myFriendsList = new ArrayList<User>(); 
}
