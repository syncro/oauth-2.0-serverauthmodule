package com.smartlogic.security;

import java.io.Serializable;
import javax.security.auth.callback.Callback;

import com.smartlogic.security.api.UserInfo;

/**
 * Callback for UserInfo.
 *
 * @author pdgreen
 * @author rahlander
 */
public class UserInfoCallBack implements Callback, Serializable {
	
	private static final long serialVersionUID = 1L;

  private UserInfo userInfo;

  public UserInfo getUserInfo() {
    return userInfo;
  }

  void setUserInfo(UserInfo userInfo) {
    this.userInfo = userInfo;
  }
}
