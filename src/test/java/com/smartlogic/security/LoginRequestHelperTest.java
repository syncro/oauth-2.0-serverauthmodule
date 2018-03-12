package com.smartlogic.security;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

public class LoginRequestHelperTest {

  private LoginRequestHelper loginRequestHelper;

  @Before
  public void init() {
    loginRequestHelper = new LoginRequestHelper("oauth.loginProvider=x");
  }

  @Test
  public void shouldAuthenticate_nullParam() {
    loginRequestHelper = new LoginRequestHelper(null);
    assertThat(loginRequestHelper.shouldAuthenticate("p1=v1"), is(true));
  }

  @Test
  public void getQueryOmitingSpecialParameters_nullParam() {
    loginRequestHelper = new LoginRequestHelper(null);
    assertThat(loginRequestHelper.getQueryOmittingLoginParam("p1=v1"), is("p1=v1"));
  }

  @Test
  public void getQueryOmitingSpecialParameters_onlyLoginParam() {
    assertThat(loginRequestHelper.getQueryOmittingLoginParam("oauth.loginProvider=x"),
        is(""));
  }

  @Test
  public void getQueryOmitingSpecialParameters_amongOtherParams() {
    assertThat(
        loginRequestHelper.getQueryOmittingLoginParam("p1=v1&oauth.loginProvider=x&p2=v2"),
        is("p1=v1&p2=v2"));
  }

  @Test
  public void getQueryOmitingSpecialParameters_nullQuery() {
    assertThat(loginRequestHelper.getQueryOmittingLoginParam(null), is(nullValue()));
  }

  @Test
  public void shouldAuthenticate_amongOtherParams() {
    assertThat(loginRequestHelper.shouldAuthenticate("p1=v1&oauth.loginProvider=x&p2=v2"),
        is(true));
  }

  @Test
  public void shouldAuthenticate_missing() {
    assertThat(loginRequestHelper.shouldAuthenticate("p1=v1&p2=v2"), is(false));
  }

  @Test
  public void shouldAuthenticate_nullQuery() {
    assertThat(loginRequestHelper.shouldAuthenticate(null), is(false));
  }

}
