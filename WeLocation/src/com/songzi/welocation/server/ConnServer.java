package com.songzi.welocation.server;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;









import android.os.Message;
import android.util.Log;

import com.songzi.welocation.model.User;
import com.songzi.welocation.util.Const;
import com.songzi.welocation.util.JsonUtil;

public class ConnServer {
	
	
/*	
	 * 	获取服务器上的好友经纬度
	 
	public void getData(String method) {
		// TODO Auto-generated method stub
		HttpPost request = new HttpPost(Const.FRIURL);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair(Const.PARAM, method));//这样就能重复使用一个servlet
		params.add(new BasicNameValuePair("IMEI",Const.IMEI));
		try {
			request.setEntity(new UrlEncodedFormEntity(params,HTTP.UTF_8));
			HttpClient client = getHttpClient();
			HttpResponse response = client.execute(request);
			if(response.getStatusLine().getStatusCode()==200){//返回成功
				Const.message = EntityUtils.toString(response.getEntity());
				doStringToLatLng();
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/
	
	/**
	 * 上传自己的经纬度
	 */
	
	public void upMyLatLng(String method,String username){
		// TODO Auto-generated method stub
		HttpPost request = new HttpPost(Const.MYURL);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair(Const.PARAM, method));//这样就能重复使用一个servlet
		params.add(new BasicNameValuePair("myLocation",Const.myLocation));
		params.add(new BasicNameValuePair("username",username));
		try {
			request.setEntity(new UrlEncodedFormEntity(params,HTTP.UTF_8));
			HttpClient client = getHttpClient();
			HttpResponse response = client.execute(request);
			if(response.getStatusLine().getStatusCode()==200){//返回成功
				//Const.message = EntityUtils.toString(response.getEntity());
				//doStringToLatLng();
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/*
	 * 用户注册
	 */
	public boolean regUser(String method,User user){
		// TODO Auto-generated method stub
		HttpPost request = new HttpPost(Const.MYURL);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair(Const.PARAM, method));//这样就能重复使用一个servlet
		params.add(new BasicNameValuePair("username",user.getUsername()));
		params.add(new BasicNameValuePair("telphone", user.getPassword()));
		params.add(new BasicNameValuePair("password", user.getPassword()));
		try {
			request.setEntity(new UrlEncodedFormEntity(params,HTTP.UTF_8));
			HttpClient client = getHttpClient();
			HttpResponse response = client.execute(request);
			if(response.getStatusLine().getStatusCode()==200){//返回成功
				Const.isRegSucces = EntityUtils.toString(response.getEntity());
				return true;
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return false;
	}
	
	/**
	 * 用户登录
	 */
	
	public boolean logUser(String method,String username,String password){
		// TODO Auto-generated method stub
		HttpPost request = new HttpPost(Const.MYURL);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair(Const.PARAM, method));//这样就能重复使用一个servlet
		params.add(new BasicNameValuePair("username",username));
		params.add(new BasicNameValuePair("password", password));
		try {
			request.setEntity(new UrlEncodedFormEntity(params,HTTP.UTF_8));
			HttpClient client = getHttpClient();
			HttpResponse response = client.execute(request);
			if(response.getStatusLine().getStatusCode()==200){//返回成功
				Const.isLogSucces = EntityUtils.toString(response.getEntity());
				return true;
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return false;
	}
	
	
	public List<User> getMyFriends(String method){
		// TODO Auto-generated method stub
		HttpPost request = new HttpPost(Const.MYURL);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair(Const.PARAM, method));//这样就能重复使用一个servlet
		params.add(new BasicNameValuePair("myusername",Const.username));
		try {
			request.setEntity(new UrlEncodedFormEntity(params,HTTP.UTF_8));
			HttpClient client = getHttpClient();
			HttpResponse response = client.execute(request);
			if(response.getStatusLine().getStatusCode()==200){//返回成功
				Const.myFriends = EntityUtils.toString(response.getEntity());
				Const.myFriendsList = JsonUtil.json2list(Const.myFriends);
				return Const.myFriendsList;
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	
	}
	/**
	 * 程序退出
	 */
	
	public void  logout(String method){

		// TODO Auto-generated method stub
		HttpPost request = new HttpPost(Const.MYURL);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair(Const.PARAM, method));//这样就能重复使用一个servlet
		params.add(new BasicNameValuePair("myusername",Const.username));
		try {
			request.setEntity(new UrlEncodedFormEntity(params,HTTP.UTF_8));
			HttpClient client = getHttpClient();
			HttpResponse response = client.execute(request);
		
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	
	
	}
	
	/**
	 * 初始化HttpClient，并设置超时
	 */
    
    public HttpClient getHttpClient()
    {
    	BasicHttpParams httpParams = new BasicHttpParams();
    	HttpConnectionParams.setConnectionTimeout(httpParams, Const.REQUEST_TIMEOUT);
    	HttpConnectionParams.setSoTimeout(httpParams, Const.SO_TIMEOUT);
    	HttpClient client = new DefaultHttpClient(httpParams);
    	return client;
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
