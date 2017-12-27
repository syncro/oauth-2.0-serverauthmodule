package com.smartlogic.security.api;

import java.security.Principal;

/**
 * Principal for user authenticated with OAuth.
 *
 * @author pdgreen
 * @author rahlander
 */
public class OAuthPrincipal implements Principal {

  private final UserInfo userInfo;

  public OAuthPrincipal(UserInfo userInfo) {
    this.userInfo = userInfo;
  }

  @Override
  public String getName() {
    return userInfo.getEmail();
  }

  public UserInfo getUserInfo() {
    return userInfo;
  }

  @Override
  public String toString() {
    return new StringBuilder().append("{").append(OAuthPrincipal.class.getSimpleName()).append(":").append(getName()).append("}").toString();
  }
}
