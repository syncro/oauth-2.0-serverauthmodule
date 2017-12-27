package com.smartlogic.security;

import java.io.IOException;
import javax.security.auth.callback.*;

import com.smartlogic.security.api.UserInfo;

/**
 * Callback handler for {@link OAuthServerAuthModule}.
 *
 * @author pdgreen
 * @author rahlander
 */
public class OAuthCallbackHandler implements CallbackHandler {

  private UserInfo userInfo;

  public OAuthCallbackHandler() {
  }

  public OAuthCallbackHandler(UserInfo userInfo) {
    this.userInfo = userInfo;
  }

  public UserInfo getUserInfo() {
    return userInfo;
  }

  public void setUserInfo(UserInfo userInfo) {
    this.userInfo = userInfo;
  }

  @Override
  public void handle(final Callback[] callbacks) throws IOException, UnsupportedCallbackException {
    for (Callback callback : callbacks) {
      if (callback instanceof NameCallback) {
        ((NameCallback) callback).setName(userInfo.getEmail());
      } else if (callback instanceof PasswordCallback) {
        ((PasswordCallback) callback).setPassword(null);
      } else if (callback instanceof UserInfoCallBack) {
        ((UserInfoCallBack) callback).setUserInfo(userInfo);
      } else {
        throw new UnsupportedCallbackException(callback);
      }
    }
  }
}
