package com.smartlogic.security;

import java.util.Date;

/**
 * Information about access token;
 *
 * @author pdgreen
 * @author rahlander
 */
public class AccessTokenInfo {

  private final String accessToken;
  private final Date expiration;
  private final String type;
  private final String groups;

  public AccessTokenInfo(String accessToken, Date expiration, String type, String groups) {
    this.accessToken = accessToken;
    this.expiration = expiration;
    this.type = type;
    this.groups = groups;
  }

  public String getAccessToken() {
    return accessToken;
  }

  public Date getExpiration() {
    return expiration;
  }

  public String getType() {
    return type;
  }

  @Override
  public String toString() {
    return getAccessToken();
  }

  public String getGroups() {
	return groups;
}
}
