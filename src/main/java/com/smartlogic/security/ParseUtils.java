package com.smartlogic.security;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.jayway.jsonpath.JsonPath;
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

  private static final Logger LOGGER = Logger.getLogger(ParseUtils.class.getName());

  private static Gson _gsonInstance;

  public static Gson getGsonInstance() {
    if (_gsonInstance == null) {
      // Creates the json object which will manage the information received
      GsonBuilder builder = new GsonBuilder();

      // Register an adapter to manage the date types as long values
      builder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
        public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws
            JsonParseException {
          return new Date(json.getAsJsonPrimitive().getAsLong());
        }
      });

      _gsonInstance = builder.create();
    }
    return _gsonInstance;
  }

  public static void decodeGroups(AccessTokenInfo accessTokenInfo, String groupsJsonPath) {
    try {
      DecodedJWT jwt = JWT.decode(accessTokenInfo.getAccessToken());
      String tokenString = ParseUtils.getGsonInstance().toJson(jwt);
      accessTokenInfo.setGroups(JsonPath.read(tokenString, groupsJsonPath));
    } catch (JWTDecodeException ex) {
      LOGGER.log(Level.WARNING, "Error decoding groups from token: {0}", ex);
    }
  }

  public static AccessTokenInfo parseAccessTokenJson(final String json) {
    LOGGER.log(Level.FINER, "parse access token json: " + json);

    AccessTokenInfo tokenInfo = ParseUtils.getGsonInstance().fromJson(json, AccessTokenInfo.class);
    return tokenInfo;
  }

  public static AccessTokenInfo parseAccessTokenJson(final String json, String groupsJsonPath) {
    LOGGER.log(Level.FINER, "parse access token json: " + json);

    AccessTokenInfo tokenInfo = ParseUtils.getGsonInstance().fromJson(json, AccessTokenInfo.class);
    decodeGroups(tokenInfo, groupsJsonPath);
    return tokenInfo;
  }

  public static UserInfo parseUserInfoJson(final String json) {
    LOGGER.log(Level.FINER, "parse user info json: " + json);

    return ParseUtils.getGsonInstance().fromJson(json, UserInfo.class);
  }
}
