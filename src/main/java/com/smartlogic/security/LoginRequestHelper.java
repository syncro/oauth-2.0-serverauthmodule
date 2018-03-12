package com.smartlogic.security;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;

public class LoginRequestHelper {

  private String loginRequestParam;

  public LoginRequestHelper(String loginRequestParam) {
    this.loginRequestParam = loginRequestParam;
  }

  public boolean shouldAuthenticate(String query) {
    if (loginRequestParam == null) {
      return true;
    }
    List<NameValuePair> params = parse(query);
    for (NameValuePair param : params) {
      if (isMatching(param, loginRequestParam)) {
        return true;
      }
    }
    return false;
  }

  public String getQueryOmittingLoginParam(String query) {
    if (loginRequestParam == null) {
      return query;
    }
    if (query == null) {
      return null;
    }
    List<NameValuePair> params = parse(query);
    List<NameValuePair> result = getNonMatching(params, loginRequestParam);
    return URLEncodedUtils.format(result, StandardCharsets.UTF_8);
  }

  private List<NameValuePair> getNonMatching(List<NameValuePair> params, String loginRequestParam) {
    List<NameValuePair> result = new ArrayList<NameValuePair>();
    for (NameValuePair param : params) {
      if (!isMatching(param, loginRequestParam)) {
        result.add(param);
      }
    }
    return result;
  }

  private List<NameValuePair> parse(String query) {
    return URLEncodedUtils.parse(query, StandardCharsets.UTF_8);
  }

  private boolean isMatching(NameValuePair param, String loginRequestParam) {
    return loginRequestParam.equals(param.getName() + "=" + param.getValue());
  }

}
