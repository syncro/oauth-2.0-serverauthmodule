package com.smartlogic.security;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LoginRequestHelper {

  private static final String PARAMS_SEP = "&";

  private String loginRequestParam;

  public LoginRequestHelper(String loginRequestParam) {
    this.loginRequestParam = loginRequestParam;
  }

  public boolean shouldAuthenticate(String query) {
    if (loginRequestParam == null) {
      return true;
    }
    if (query == null) {
      return false;
    }
    return split(query).anyMatch(this::isMatching);
  }

  public String getQueryOmittingLoginParam(String query) {
    if (loginRequestParam == null) {
      return query;
    }
    if (query == null) {
      return null;
    }
    return split(query).filter(x -> !isMatching(x)).collect(Collectors.joining(PARAMS_SEP));
  }

  private boolean isMatching(String x) {
    return x.equals(loginRequestParam);
  }

  private Stream<String> split(String query) {
    return Stream.of(query.split(PARAMS_SEP));
  }

}
