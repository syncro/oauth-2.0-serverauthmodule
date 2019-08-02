package com.smartlogic.security;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.google.gson.annotations.SerializedName;

/**
 * Information about access token;
 *
 * @author pdgreen
 * @author rahlander
 */
public class AccessTokenInfo {

  @SerializedName("access_token")
  private final String accessToken;
  @SerializedName("expires_in")
  private final Date expiration;
  @SerializedName("token_type")
  private final String type;

  private List<String> groups = new ArrayList<>();

  public AccessTokenInfo() {
    this.accessToken = null;
    this.expiration = null;
    this.type = null;
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

  public void setGroups(List<String> groups) {
    this.groups = groups;
  }

  public String getGroups() {
    return groupsAsString();
  }

  public String groupsAsString() {
    return String.join(",", groups);
  }


}
