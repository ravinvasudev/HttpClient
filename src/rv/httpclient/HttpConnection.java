package rv.httpclient;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gson.JsonObject;

import rv.httpclient.util.HttpMethod;
import rv.httpclient.util.ResponseCode;
import rv.httpclient.util.Status;

/**
 * @author Ravin Vasudev
 * @version 1.0
 * @since June 2016
 * @category HTTP Client
 * 
 *           <p>
 *           HttpURLConnection wrapper class. Handles the intricacies of
 *           communication with remote server.
 *           </p>
 *           <p>
 *           Do not use this class directly to communicate with server but can
 *           be extended to add more functionality.
 *           </p>
 *
 */
public final class HttpConnection {

	private HttpURLConnection conn;

	public HttpConnection() {

	}

	/***
	 * Opens the URL connection for
	 * 
	 * @param url
	 *            - url of open a connection
	 * @param httpMethod
	 *            - http method to use for communication
	 * @throws HttpClientException
	 */
	public void open(final String url, final HttpMethod httpMethod) throws HttpClientException {
		try {
			conn = (HttpURLConnection) new URL(url).openConnection();
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setRequestMethod(httpMethod == null ? HttpMethod.GET.toString() : httpMethod.toString());
		} catch (MalformedURLException e) {
			throw new HttpClientException();
		} catch (IOException e) {
			throw new HttpClientException(ResponseCode.RUNTIME_ERROR, "Failed to create HttpURLConnection.", e);
		}
	}

	/***
	 * Construct HTTP URL appended with query parameters.
	 * 
	 * @param url
	 *            - url of open a connection
	 * @param queryParams
	 *            - parameters to append to the url
	 * @return a url with appended parameters
	 * @throws HttpClientException
	 */
	public String constructGetURLWithQueryParams(String url, Map<String, String> queryParams)
			throws HttpClientException {
		final StringBuilder getURL = new StringBuilder(url);
		boolean flag = Boolean.FALSE;
		if (queryParams != null && !queryParams.isEmpty()) {
			getURL.append("?");
			final Iterator<Entry<String, String>> it = queryParams.entrySet().iterator();
			while (it.hasNext()) {
				final Entry<String, String> entry = it.next();
				try {
					final String key = entry.getKey() != null ? entry.getKey() : null;
					if (key != null) {
						final String value = entry.getValue();
						if (flag == Boolean.FALSE) {
							flag = Boolean.TRUE;
						} else {
							getURL.append("&");
						}
						getURL.append(key).append("=").append(value);
					}
				} catch (IllegalStateException e) {
					throw new HttpClientException(ResponseCode.RUNTIME_ERROR, "Query Parameter has a problem.", e);
				} catch (NullPointerException e) {
					throw new HttpClientException(ResponseCode.VALIDATION_ERROR, "Query Parameter has a null key.", e);
				}
			}
		}
		return getURL.toString();
	}

	/***
	 * Adds HTTP Headers to an open connection.
	 * 
	 * @param headers
	 *            - http headers to add
	 * @throws HttpClientException
	 */
	public void addHeaders(final Map<String, String> headers) throws HttpClientException {
		if (headers != null && !headers.isEmpty()) {
			final Iterator<Entry<String, String>> it = headers.entrySet().iterator();
			while (it.hasNext()) {
				final Entry<String, String> entry = it.next();
				try {
					final String key = entry.getKey() != null ? entry.getKey() : null;
					if (key != null) {
						conn.setRequestProperty(key, entry.getValue());
					}
				} catch (IllegalStateException e) {
					throw new HttpClientException(ResponseCode.RUNTIME_ERROR, "Request Header has a problem.", e);
				} catch (NullPointerException e) {
					throw new HttpClientException(ResponseCode.VALIDATION_ERROR, "Request Header has a null key.", e);
				}
			}
		}
	}

	/***
	 * Executes the operation.
	 * 
	 * @return httpResponse
	 * @throws HttpClientException
	 */
	public HttpResponse execute() throws HttpClientException {
		try {
			final int responseCode = conn.getResponseCode();
			final String responseMessage = conn.getResponseMessage();
			final Map<String, List<String>> headers = conn.getHeaderFields();

			final HttpResponse httpResponse = new HttpResponse();
			httpResponse.setResponseCode(responseCode);
			httpResponse.setResponseMessage(responseMessage);
			httpResponse.setHeaders(headers);
			final HttpAck ack = httpResponse.getHttpAck();

			if (responseCode == HttpURLConnection.HTTP_OK) {
				httpResponse.setResponseDate(conn.getInputStream());
				ack.setStatus(Status.SUCCESS);
			} else {
				ack.setStatus(Status.FAILURE);
				ack.setErrorType(ResponseCode.RUNTIME_ERROR);
				ack.setErrorCode(responseCode);
				ack.setErrorMessage(responseMessage);
			}
			return httpResponse;
		} catch (IOException e) {
			throw new HttpClientException(ResponseCode.RUNTIME_ERROR, "Failed to execute HttpRequest.", e);
		}
	}

	/***
	 * Close HttpURLConnection previously opened.
	 */
	public void close() {
		conn.disconnect();
	}

	/**
	 * Adds request data to HttpURLConnection.
	 * 
	 * @param request
	 *            - string format request data. it can be a xml/json/url-encoded
	 * @throws HttpClientException
	 */
	public void addBody(final String request) throws HttpClientException {
		if (request != null && !request.isEmpty()) {
			try {
				final OutputStream os = conn.getOutputStream();
				os.write(request.getBytes());
				os.flush();
			} catch (IOException e) {
				throw new HttpClientException(ResponseCode.RUNTIME_ERROR,
						"Failed to create output stream for writing request parameters.", e);
			}
		}
	}

	/***
	 * Adds request data to HttpURLConnection.
	 * 
	 * @param request
	 *            - json format request data
	 * @throws HttpClientException
	 */
	public void addBody(JsonObject request) throws HttpClientException {
		if (request != null && !request.isJsonNull()) {
			addBody(request.toString());
		}
	}

	/***
	 * Adds request data to HttpURLConnection.
	 * 
	 * @param request
	 *            - request data
	 * @throws HttpClientException
	 */
	public void addBody(byte[] request) throws HttpClientException {
		if (request != null) {
			addBody(new String(request));
		}
	}

}
