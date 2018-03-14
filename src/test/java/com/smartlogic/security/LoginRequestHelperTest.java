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
  public void getQueryOmittingLoginParam_nullParam() {
    loginRequestHelper = new LoginRequestHelper(null);
    assertThat(loginRequestHelper.getQueryOmittingLoginParam("p1=v1"), is("p1=v1"));
  }

  @Test
  public void getQueryOmittingLoginParam_onlyLoginParam() {
    assertThat(loginRequestHelper.getQueryOmittingLoginParam("oauth.loginProvider=x"), is(""));
  }

  @Test
  public void getQueryOmittingLoginParam_amongOtherParams() {
    assertThat(loginRequestHelper.getQueryOmittingLoginParam("p1=v1&oauth.loginProvider=x&p2=v2"),
        is("p1=v1&p2=v2"));
  }

  @Test
  public void getQueryOmittingLoginParam_repeatedParams() {
    assertThat(loginRequestHelper.getQueryOmittingLoginParam("p1=v1&oauth.loginProvider=x&p1=v2"),
        is("p1=v1&p1=v2"));
  }

  @Test
  public void getQueryOmittingLoginParam_paramWithoutValue() {
    assertThat(loginRequestHelper.getQueryOmittingLoginParam("p1=v1&oauth.loginProvider=x&p2="),
        is("p1=v1&p2="));
  }

  @Test
  public void getQueryOmitingSpecialParameters_nullQuery() {
    assertThat(loginRequestHelper.getQueryOmittingLoginParam(null), is(nullValue()));
  }

  @Test
  public void getQueryOmittingLoginParam_notEncodedValues() {
    assertThat(
        loginRequestHelper.getQueryOmittingLoginParam(
            "oauth.loginProvider=x&some=param&_hash_=/models/model:myExample/tasks/" +
                "model:myExample/concepts/details/concept/edit?itemUri=example:Losec"),
        is("some=param&_hash_=/models/model:myExample/tasks/" +
            "model:myExample/concepts/details/concept/edit?itemUri=example:Losec"));
  }

  @Test
  public void shouldAuthenticate_notEncodedValues() {
    assertThat(loginRequestHelper.shouldAuthenticate(
        "oauth.loginProvider=x&some=param&_hash_=/models/model:myExample/tasks/" +
            "model:myExample/concepts/details/concept/edit?itemUri=example:Losec"),
        is(true));
  }

  @Test
  public void getQueryOmittingLoginParam_encodedValues() {
    assertThat(loginRequestHelper.getQueryOmittingLoginParam(
        "oauth.loginProvider=x&some=param&_hash_=%2Fmodels%2Fmodel%3AmyExample%2Ftasks%2F" +
            "model%3AmyExample%2Fconcepts%2Fdetails%2Fconcept%2Fedit%3FitemUri%3Dexample%3ALosec"),
        is("some=param&_hash_=%2Fmodels%2Fmodel%3AmyExample%2Ftasks%2F" +
            "model%3AmyExample%2Fconcepts%2Fdetails%2Fconcept%2Fedit%3FitemUri%3Dexample%3ALosec"));
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
