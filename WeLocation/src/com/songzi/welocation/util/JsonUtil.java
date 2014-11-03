/**
 * 2014年10月27日
 * guosong
 */
package com.songzi.welocation.util;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.songzi.welocation.model.User;


/**
 * @author guosong
 *
 */
public class JsonUtil {
	
	
	
	public static List json2list(String json){
		JSONArray jsonarray;
		List<User> friends = new ArrayList<User>();
		User user;
		try {
			jsonarray = new JSONArray(json);
			JSONObject jsonObject;
			for (int i = 0; i < jsonarray.length(); i++) {
				jsonObject = jsonarray.getJSONObject(i);
				user = new User();
				user.setId(jsonObject.getInt("id"));
				user.setUsername(jsonObject.getString("username"));
				user.setLatlngLately(jsonObject.getString("latlngLately"));
				user.setTelpone(jsonObject.getString("telpone"));
				user.setState(jsonObject.getInt("state"));
				friends.add(user);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return friends;
	}
}
