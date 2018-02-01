package com.smartlogic.security;

import java.util.Date;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
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

  public String getGroups() {
    return decodeGroups();
  }

  private String decodeGroups() {
    String groups = "";
    try {
      DecodedJWT jwt = JWT.decode(accessToken);
      Claim groupClaim = jwt.getClaims().get(ApiUtils.TOKEN_API_GROUPS_PARAMETER);
      if (groupClaim != null) {
        String[] groupsArray = groupClaim.asArray(String.class);
        groups = String.join(",", groupsArray);
      }
    } catch (JWTDecodeException ex) {

    }
    return groups;
  }
}
