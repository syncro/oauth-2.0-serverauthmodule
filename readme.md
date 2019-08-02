Overview
========
OAuth 2.0 ServerAuthModule is a ServerAuthModule (SAM), [JSR-196 (JASPIC) Spec][jsr-196], implementation of [OAuth 2.0]: `com.smartlogic.security.google.OAuthServerAuthModule`.  It optionally supports the [LoginModule Bridge Profile].  This module has been tested with [Google OAuth][google-oauth] and [Okta OAuth][okta-oauth] and [Keycloack][keycloack] integrated with [Tomcat using JASPIC plugin][tomcat85-jaspic].

Installation
============

Copy `oauth-2_0-sam-0.2.x-(SNAPSHOT)-jar-with-dependencies.jar` into the class path of the application server.

Configuration
=============

Before you can authenticate with OAuth, you will need to create a Client ID for your web application at using your provider tools.

Next, the OAuthServerAuthModule needs added to the application server.  See [Configuration]

### OAuthServerAuthModule

The following attributes can be used to configure `com.smartlogic.security.google.OAuthServerAuthModule`.

#### `oauth.clientid` (_REQUIRED_)
`oauth.clientid` must be set to a "`Client ID`" from your OAuth2 providers API console.

#### `oauth.clientsecret` (_REQUIRED_)
`oauth.clientsecret` must be set to the "`Client Secret`" from your OAuth2 providers API console of the "`Client ID`" specified in `oauth.clientid`.


#### `oauth.endpoint` (_OPTIONAL_)
`oauth.endpoint` is the URI that will be connect to for the OAuth authentication.  Use this when you have a single base uri and the authorization endpoint is " authorize ", the token api endpoint is " token " and the userinfo endpoint is " userinfo ".  If this is not the case, you must specify each individual uri.

Okta Example: `https://dev-926840.oktapreview.com/oauth2/ausd9spsyoOsLKc2i0h7/v1`

#### `oauth.auth_uri` (_optional_)
`oauth.auth_uri` is the authorization uri.  

Google Example: `https://accounts.google.com/o/oauth2/v2/auth`

#### `oauth.token_uri` (_optional_)
`oauth.token_uri` is the token uri.  

Google Example: `https://www.googleapis.com/oauth2/v4/token`

#### `oauth.userinfo_uri` (_optional_)
`oauth.userinfo_uri` is the userinfo uri.  

Google Example: `https://www.googleapis.com/oauth2/v3/userinfo`

#### `oauth.callback_uri` (_optional_) 
default: `/j_oauth_callback`

`oauth.callback_uri` is the URI that Google will redirect to after the user responds to the request.  This should correspond to "`Redirect URIs`" value defined in the OAuth2 Client API Console for your provider.

#### `javax.security.auth.login.LoginContext` (_optional_)
default: `"com.smartlogic.security.google.OAuthServerAuthModule"`

With [LoginModule Bridge Profile], `javax.security.auth.login.LoginContext` is where you define the name of the [LoginContext][javadocs-logincontext] to use.

#### `ignore_missing_login_context` (_optional_)
default: `"false"`

`OAuthServerAuthModule` is configured by default to support the [LoginModule Bridge Profile].  If you set `ignore_missing_login_context` to true (in the case when you don't want to use any [LoginModules][javadocs-loginmodule]), there will be no error when a LoginContext isn't found.


#### `add_domain_as_group` (_optional_)
default: `"false"`

If `add_domain_as_group` is `true`, then the domain of the email address of the authenticated user will be added as a group.  IE: "smartlogic.com" will be a principal added as a group for the user "rick.ahlander@smartlogic.com".



#### `default_groups` (_optional_)
default: `""`

`default_groups` is a comma (",") separated list of groups that will be given to the principal upon successful authentication.


#### `groups_jsonpath` (_optional_)
default: `"$.payload.tree.realm_access._children.roles._children[*]._value"`

`groups_jsonpath` jsonPath expression to extract groups, defaulting to keycloack defaults.


#### 'oauth.scope' (_optional_)
default: '"openid email profile"'

'oauth.scope' is a space (" ") separated list of scopes requested from your OAuth2 provider.

#### `login_request_param` (_optional_)
default: '(none)'

'login_request_param' is a the concatenated query parameter key and value query. It used to
determine whether the request requires oauth authentication, if not authenticated yet.

Example usage is not to authenticate automatically all requests with ouath provider. Instead,
only a specific request matching the specified parameter is used to authenticate the user (create the session).
All other requests (if the session is not established yet) are redirected to the landing page,
where the landing page has a button "Log in with outh provider", which sends a request the matching
'login_request_regexp' value.

#### `forward_to_if_not_authenticated` (_optional_)
default: '(none)'

'forward_to_if_not_authenticated' specifies the application relative path that will be used
for landing page for a not authenticated request, which does not match `login_request_param`.

Note that, this parameter is only relevant when `login_request_param` is defined.

### GlassFish Configuration
See [Phillip Green's GlassFish Configuration](https://bitbucket.org/phillip_green_idmworks/google-oauth-2.0-serverauthmodule/wiki/setup/2-configuration).  Note, the name of the class has changed in this fork.  The class name is com.smartlogic.security.OAuthServerAuthmodule.

### Tomcat Configuration
See [Tomcat JASPIC Configuration][tomcat85-jaspic]

###Google Tomcat Config Example

	<jaspic-providers xmlns="http://tomcat.apache.org/xml"
              xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
              xsi:schemaLocation="http://tomcat.apache.org/xml jaspic-providers.xsd"
              version="1.0">
    	<provider name="google-oauth"
        	className="org.apache.catalina.authenticator.jaspic.SimpleAuthConfigProvider"
        	layer="HttpServlet"
        	appContext="Catalina/localhost /OE"
        	description="Google OAuth test">
        
        	<property name="org.apache.catalina.authenticator.jaspic.ServerAuthModule.1"
           	value="com.smartlogic.security.OAuthServerAuthModule" />
        	<property name="oauth.auth_uri"
		    	value="https://accounts.google.com/o/oauth2/v2/auth" />
			<property name="oauth.userinfo_uri"
				value="https://www.googleapis.com/oauth2/v3/userinfo" />
			<property name="oauth.token_uri"
			  	value="https://www.googleapis.com/oauth2/v4/token" />
			<property name="oauth.clientid"
            	value="MY_GOOGLE_CLIENT_ID" />
        	<property name="oauth.clientsecret"
            	value="MY_GOOGLE_CLIENT_SECRET" />
        	<property name="ignore_missing_login_context"
            	value="true" />
        	<property name="login_request_param"
            	value="oauth.loginProvider=google" />
        	<property name='default_groups'
            	value='SemaphoreUsers,SemaphoreAdministrators'/>
        	<property name="forward_to_if_not_authenticated"
            	value="/logon.jsp" />
        </provider>
	</jaspic-providers>

###OKTA Tomcat Config Example

    <jaspic-providers xmlns="http://tomcat.apache.org/xml"
                  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
                  xsi:schemaLocation="http://tomcat.apache.org/xml jaspic-providers.xsd"
                  version="1.0">			  
	    <provider name="okta-oauth"
	        className="org.apache.catalina.authenticator.jaspic.SimpleAuthConfigProvider"
	        layer="HttpServlet"
	        appContext="Catalina/localhost /contextPath"
	        description="OKTA OAUTH">
	        
	        <property name="org.apache.catalina.authenticator.jaspic.ServerAuthModule.1"
	            value="com.smartlogic.security.OAuthServerAuthModule" />
	        <property name="oauth.clientid"
	            value="Obtained-from OKTA-Application-Setup" />
	        <property name="oauth.clientsecret"
	            value="Obtained-from-OKTA-Application-Setup" />
	        <property name="oauth.endpoint"
				  value="https://oktaauthendpoint.oktapreview.com/oauth2/oktapplicationkey/v1" />
	        <property name="ignore_missing_login_context"
	            value="true" />
          <property name="login_request_param"
              value="oauth.loginProvider=okta" />
          <property name="forward_to_if_not_authenticated"
              value="/logon.jsp" />
	        <property name="default_groups"
				  value="SemaphoreUsers" />
	    </provider>
    </jaspic-providers>

Usage
=====

The configured `OAuthServerAuthModule` needs specified in the application server specific configuration for each application.

Common Problems
===============


References
==========
  + [JSR-196][jsr-196]
  + [Google API Console][google-api-console]
  + [Google OAuth][google-oauth]
  + [Google OAuth for Webservers][google-oauth-webserver]
  + [LoginContext Javadocs][javadocs-logincontext]
  + [LoginModule Javadocs][javadocs-loginmodule]
  + [LoginModule Bridge Profile in glassfish][LoginModule Bridge Profile]
  + [LoginContext Configuration][configuration-logincontext]
  + [configuration-logincontext]
  + [openid4java-jsr196]
  + [Project Source Code on Bitbucket][bitbucket-source]
  + [Okta OAuth2][okta-oauth]
  + [Tomcat 8.5 JASPIC][tomcat85-jaspic]
  + [Tomcat 9.0 JASPIC][tomcat90-jaspic]

  [jsr-196]: http://www.jcp.org/en/jsr/detail?id=196
  [google-api-console]: https://code.google.com/apis/console/
  [google-oauth]: https://developers.google.com/accounts/docs/OAuth2  
  [google-oauth-webserver]: https://developers.google.com/accounts/docs/OAuth2WebServer
  [javadocs-logincontext]: http://docs.oracle.com/javase/6/docs/api/javax/security/auth/login/LoginContext.html
  [javadocs-loginmodule]: http://docs.oracle.com/javase/6/docs/api/javax/security/auth/spi/LoginModule.html
  [LoginModule Bridge Profile]: https://blogs.oracle.com/nasradu8/entry/loginmodule_bridge_profile_jaspic_in
  [configuration-logincontext]: http://docs.oracle.com/javase/6/docs/api/javax/security/auth/login/Configuration.html
  [openid4java-jsr196]: http://code.google.com/p/openid4java-jsr196/
  [bitbucket-source]: https://bitbucket.org/phillip_green_smartlogic/oauth-2.0-serverauthmodule
  [okta-oauth]: https://developer.okta.com/docs/api/resources/oauth2
  [keycloack]: https://www.keycloak.org/
  [tomcat85-jaspic]: https://tomcat.apache.org/tomcat-8.5-doc/config/jaspic.html
  [tomcat90-jaspic]: https://tomcat.apache.org/tomcat-9.0-doc/config/jaspic.html
