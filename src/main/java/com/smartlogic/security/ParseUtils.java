package com.smartlogic.security;

import static com.smartlogic.security.ApiUtils.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.smartlogic.security.api.UserInfo;

/**
 * Methods of this class parse JSON responses. <br> Instead of including an external library to handle JSON, and making
 * installation/packaging more difficult, I created a couple small methods to handle it. There is only a couple response
 * to parse, so I felt I could by pass a full JSON parse for the sake of easier installation.
 *
 * @author pdgreen
 * @author rahlander
 */
public class ParseUtils {

  static Map<String, String> parseSimpleJson(final String json) {
    final String[] parts = json.substring(json.indexOf("{") + 1, json.lastIndexOf("}")).split(",");

    final Map<String, String> values = new HashMap<String, String>();
    for (final String part : parts) {
      final String[] vparts = part.replaceAll("\"", "").split(":", 2);
      values.put(vparts[0].trim(), vparts[1].trim());
    }
    return values;
  }

  public static AccessTokenInfo parseAccessTokenJson(final String json) {

    final Map<String, String> values = parseSimpleJson(json);

    final String accessToken = values.get(TOKEN_API_ACCESS_TOKEN_PARAMETER);
    final String expiresInAsString = values.get(TOKEN_API_EXPIRES_IN_PARAMETER);
    final String tokenType = values.get(TOKEN_API_TOKEN_TYPE_PARAMETER);
    
    String groups = "";
    try{
    	DecodedJWT jwt = JWT.decode(accessToken);
    	Claim groupClaim = jwt.getClaims().get(TOKEN_API_GROUPS_PARAMETER);
    	if(groupClaim != null){
    		String[] groupsArray = groupClaim.asArray(String.class);
    		groups = String.join(",", groupsArray);
    	}
    }
    catch(JWTDecodeException ex){
    	
    }


    final int expiresIn = Integer.parseInt(expiresInAsString);

    return new AccessTokenInfo(accessToken, new Date(new Date().getTime() + expiresIn * 1000), tokenType, groups);
  }

  public static UserInfo parseUserInfoJson(final String json) {

    final Map<String, String> values = parseSimpleJson(json);

    String id = values.get(USERINFO_API_ID_PARAMETER);
    if(id == null || id.length() == 0) id = values.get(USERINFO_API_ID_ALT_PARAMETER);
    final String email = values.get(USERINFO_API_EMAIL_PARAMETER);
    final boolean verifiedEmail = values.containsKey(USERINFO_API_VERIFIED_EMAIL_PARAMETER) && Boolean.parseBoolean(values.get(USERINFO_API_VERIFIED_EMAIL_PARAMETER));
    final String name = values.get(USERINFO_API_NAME_PARAMETER);
    final String givenName = values.get(USERINFO_API_GIVEN_NAME_PARAMETER);
    final String familyName = values.get(USERINFO_API_FAMILY_NAME_PARAMETER);
    final String gender = values.get(USERINFO_API_GENDER_PARAMETER);
    final String link = values.get(USERINFO_API_LINK_PARAMETER);
    final String picture = values.get(USERINFO_API_PICTURE_PARAMETER);
    final String locale = values.get(USERINFO_API_LOCALE_PARAMETER);

    return new UserInfo(id, email, verifiedEmail, name, givenName, familyName, gender, link, picture, locale);
  }
}
