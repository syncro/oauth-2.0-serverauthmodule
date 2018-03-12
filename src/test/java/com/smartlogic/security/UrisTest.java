package com.smartlogic.security;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.net.URI;

import org.junit.Test;

public class UrisTest {

  @Test
  public void testBuildUriWithQueryString_nullQuery() {
    URI uri = URI.create("http://example.com/logon.jsp");
    URI result = Uris.buildUriWithQueryString(uri, null);

    assertThat(result, is(URI.create("http://example.com/logon.jsp")));
  }

  @Test
  public void testBuildUriWithQueryString_withQuery() {
    URI uri = URI.create("http://example.com/logon.jsp");
    URI result = Uris.buildUriWithQueryString(uri, "p1=v1&p2=v2");

    assertThat(result, is(URI.create("http://example.com/logon.jsp?p1=v1&p2=v2")));
  }
}
