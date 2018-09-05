package com.smartlogic.security;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.net.ssl.HttpsURLConnection;

import com.smartlogic.security.api.UserInfo;

/**
 * Methods of this class connect to Oauth2 APIs. <br>
 * Instead of including an external library to handle OAuth2 APIs, and making
 * installation/packaging more difficult, I created a couple small methods to
 * handle it. There is only a couple API calls to parse, so I felt I could by
 * pass an external library for the sake of easier installation.
 *
 * @author pdgreen
 * @author rahlander
 */
public class ApiUtils {
	/*
	 * User Info API
	 */

	public static final String USERINFO_API_PERMISSIONS = "openid email profile";
	/*
	 * parameters
	 */
	public static final String USERINFO_API_ID_PARAMETER = "id";
	public static final String USERINFO_API_ID_ALT_PARAMETER = "sub";
	public static final String USERINFO_API_EMAIL_PARAMETER = "email";
	public static final String USERINFO_API_VERIFIED_EMAIL_PARAMETER = "email_verified";
	public static final String USERINFO_API_NAME_PARAMETER = "name";
	public static final String USERINFO_API_GIVEN_NAME_PARAMETER = "given_name";
	public static final String USERINFO_API_FAMILY_NAME_PARAMETER = "family_name";
	public static final String USERINFO_API_GENDER_PARAMETER = "gender";
	public static final String USERINFO_API_LINK_PARAMETER = "link";
	public static final String USERINFO_API_PICTURE_PARAMETER = "picture";
	public static final String USERINFO_API_LOCALE_PARAMETER = "locale";

	/*
	 * parameters
	 */
	// public static final String TOKEN_API_ACCESS_TYPE_PARAMETER =
	// "access_type";
	public static final String TOKEN_API_ACCESS_TOKEN_PARAMETER = "access_token"; // Verified
	// public static final String TOKEN_API_APPROVAL_PROMPT_PARAMETER =
	// "approval_prompt";
	public static final String TOKEN_API_CLIENT_ID_PARAMETER = "client_id"; // Verified
	public static final String TOKEN_API_CLIENT_SECRET_PARAMETER = "client_secret"; // Verified
	public static final String TOKEN_API_CODE_PARAMETER = "code"; // Verified
	public static final String TOKEN_API_ERROR_PARAMETER = "error"; // Verified
	public static final String TOKEN_API_EXPIRES_IN_PARAMETER = "expires_in"; // Verified
	public static final String TOKEN_API_GRANT_TYPE_PARAMETER = "grant_type"; // verified
	public static final String TOKEN_API_REDIRECT_URI_PARAMETER = "redirect_uri"; // Verified
	public static final String TOKEN_API_RESPONSE_TYPE_PARAMETER = "response_type"; // Verified
	public static final String TOKEN_API_SCOPE_PARAMETER = "scope"; // Verified
	public static final String TOKEN_API_STATE_PARAMETER = "state"; // Verified
	public static final String TOKEN_API_TOKEN_TYPE_PARAMETER = "token_type"; // Verified
	public static final String TOKEN_API_GROUPS_PARAMETER = "groups";
	/*
	 * values
	 */
	public static final String TOKEN_API_AUTHORIZATION_CODE_VALUE = "authorization_code";
	private static final Logger LOGGER = Logger.getLogger(ApiUtils.class.getName());

	public static URI buildOauthAuthorizeUri(final String redirectUri, final URI endpoint, final String clientid,
			final String scopes) {

		final StringBuilder querySb = new StringBuilder();
		querySb.append(TOKEN_API_SCOPE_PARAMETER).append("=").append(scopes);
		querySb.append("&");
		querySb.append(TOKEN_API_REDIRECT_URI_PARAMETER).append("=").append(redirectUri); // yes
		querySb.append("&");
		querySb.append(TOKEN_API_RESPONSE_TYPE_PARAMETER).append("=").append(TOKEN_API_CODE_PARAMETER);
		querySb.append("&");
		querySb.append(TOKEN_API_CLIENT_ID_PARAMETER).append("=").append(clientid); // Yes
		querySb.append("&");
		querySb.append("state=SL");
		querySb.append("&");
		querySb.append("nonce=SL");

		final String totalQuery = endpoint.getQuery() == null ? querySb.toString()
				: endpoint.getQuery() + "&" + querySb.toString();
		try {
			return new URI(endpoint.getScheme(), endpoint.getUserInfo(), endpoint.getHost(), endpoint.getPort(),
					endpoint.getPath(), totalQuery, endpoint.getFragment());
		} catch (URISyntaxException ex) {
			throw new IllegalArgumentException("Unable to build Oauth Uri", ex);
		}
	}

	static Response sendRequest(final String method, final URI destination, final String body, final String token) {
		if (LOGGER.isLoggable(Level.FINER)) {
			LOGGER.log(Level.FINER, "sendRequest({0},{1},{2})",
					new Object[] { method, destination, "hasBody?" + body != null });
		}

		HttpsURLConnection httpsURLConnection;

		try {
			httpsURLConnection = (HttpsURLConnection) destination.toURL().openConnection();
			if (token != null && token.length() > 0)
				httpsURLConnection.setRequestProperty("Authorization", "Bearer " + token);
			httpsURLConnection.setRequestMethod(method);
			if (body != null) {
				httpsURLConnection.setDoOutput(true);
			}
			httpsURLConnection.connect();
		} catch (IOException ex) {
			throw new IllegalStateException("Unable to create connection", ex);
		}
		if (body != null) {
			try {
				final OutputStream out = httpsURLConnection.getOutputStream();
				LOGGER.log(Level.FINER, "body: {0}", new Object[] { body });
				out.write(body.getBytes());
				out.flush();
				out.close();
			} catch (IOException ex) {
				throw new IllegalStateException("Unable to write body", ex);
			}
		}

		try {
			final int status = httpsURLConnection.getResponseCode();
			LOGGER.log(Level.FINER, "response code: {0}", new Object[] { status });

			final String responseBody;
			if (status < 400) {
				final BufferedReader reader = new BufferedReader(
						new InputStreamReader(httpsURLConnection.getInputStream()));
				final StringBuilder stringBuilder = new StringBuilder();
				String line = null;
				while ((line = reader.readLine()) != null) {
					stringBuilder.append(line).append("\n");
				}
				reader.close();
				responseBody = stringBuilder.toString();
			} else {
				responseBody = null;
			}

			return new Response(status, responseBody);
		} catch (IOException ex) {
			throw new IllegalStateException("Unable to read response", ex);
		}
	}

	static class Response {

		private final int status;
		private final String body;

		public Response(int status, String body) {
			this.status = status;
			this.body = body;
		}

		public String getBody() {
			return body;
		}

		public int getStatus() {
			return status;
		}
	}

	static Response GET(final URI destination, final String token) {
		return sendRequest("GET", destination, null, token);
	}

	static Response GET(final URI destination) {
		return sendRequest("GET", destination, null, null);
	}

	static Response POST(final URI destination, final String body) {
		return sendRequest("POST", destination, body, null);
	}

	public static AccessTokenInfo lookupAccessTokenInfo(URI endpoint, String redirectUri, String authorizationCode,
			String clientid, String clientSecret) {
		// FIXME cache URI
		final URI apiUri;
		try {
			apiUri = new URI(endpoint.toString());
		} catch (URISyntaxException ex) {
			throw new IllegalStateException("unable to create uri for " + endpoint, ex);
		}

		final StringBuilder bodySb = new StringBuilder();
		bodySb.append(TOKEN_API_CODE_PARAMETER).append("=").append(authorizationCode);
		bodySb.append("&");
		bodySb.append(TOKEN_API_CLIENT_ID_PARAMETER).append("=").append(clientid);
		bodySb.append("&");
		bodySb.append(TOKEN_API_CLIENT_SECRET_PARAMETER).append("=").append(clientSecret);
		bodySb.append("&");
		bodySb.append(TOKEN_API_REDIRECT_URI_PARAMETER).append("=").append(redirectUri);
		bodySb.append("&");
		bodySb.append(TOKEN_API_GRANT_TYPE_PARAMETER).append("=").append(TOKEN_API_AUTHORIZATION_CODE_VALUE);
		LOGGER.log(Level.FINE, "Lookup Access Token body: {0}", bodySb);

		final Response response = POST(apiUri, bodySb.toString());

		if (response.getStatus() == 200) {
			return ParseUtils.parseAccessTokenJson(response.getBody());
		} else {
			throw new IllegalStateException(String.format("Failed to get access token with URI %s.  Return code %d", apiUri, response.getStatus()));
		}
	}

	public static UserInfo retrieveUserInfo(URI endpoint, AccessTokenInfo accessTokenInfo) {

		final URI apiUri;
		try {
			apiUri = new URI(endpoint.toString());
		} catch (URISyntaxException ex) {
			throw new IllegalStateException("unable to create uri for " + endpoint, ex);
		}

		final Response response = GET(apiUri, accessTokenInfo.getAccessToken().toString());

		if (response.getStatus() == 200) {
			return ParseUtils.parseUserInfoJson(response.getBody());
		} else {
			throw new IllegalStateException(String.format("Failed to get userinfo with URI %s.  Return code %d", apiUri, response.getStatus()));
		}

	}
}
