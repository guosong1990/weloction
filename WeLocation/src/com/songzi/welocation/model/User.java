/**
 * 2014年10月26日
 * guosong
 */
package com.songzi.welocation.model;

import java.util.Date;

import android.R.integer;

/**
 * TbUser entity. @author MyEclipse Persistence Tools
 */

public class User implements java.io.Serializable {

	// Fields

	private Integer id;
	private String username;
	private String password;
	private String telpone;
	private String latlngLately;
	private Date regtime;
	private Integer state;

	// Constructors

	/** default constructor */
	public User() {
	}

	/** full constructor */
	public User(String username, String password, String telpone,
			String latlngLately, Date regtime,Integer state) {
		this.username = username;
		this.password = password;
		this.telpone = telpone;
		this.latlngLately = latlngLately;
		this.regtime = regtime;
		this.state = state;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getTelpone() {
		return this.telpone;
	}

	public void setTelpone(String telpone) {
		this.telpone = telpone;
	}

	public String getLatlngLately() {
		return this.latlngLately;
	}

	public void setLatlngLately(String latlngLately) {
		this.latlngLately = latlngLately;
	}

	public Date getRegtime() {
		return this.regtime;
	}

	public void setRegtime(Date regtime) {
		this.regtime = regtime;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

}