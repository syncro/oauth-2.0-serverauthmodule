package com.smartlogic.security;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
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


  public static AccessTokenInfo parseAccessTokenJson(final String json) {
    LOGGER.log(Level.FINER, "parse access token json: " + json);

    return ParseUtils.getGsonInstance().fromJson(json, AccessTokenInfo.class);
  }

  public static UserInfo parseUserInfoJson(final String json) {
    LOGGER.log(Level.FINER, "parse user info json: " + json);

    return ParseUtils.getGsonInstance().fromJson(json, UserInfo.class);
  }
}
