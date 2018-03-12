package com.smartlogic.security;

import java.net.URI;
import java.net.URISyntaxException;

public class Uris {

  public static URI buildUriWithQueryString(URI uri, String query) {
    try {
      return new URI(uri.getScheme(), uri.getUserInfo(), uri.getHost(), uri.getPort(),
          uri.getPath(), query, uri.getFragment());
    } catch (URISyntaxException e) {
      throw new RuntimeException(e);
    }
  }

}
