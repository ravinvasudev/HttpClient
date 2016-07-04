package rv.httpclient;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonObject;

import rv.httpclient.util.HttpHeader;
import rv.httpclient.util.HttpMethod;
import rv.httpclient.util.Validator;
import rv.httpclient.util.Validator.Key;

/**
 * @author Ravin Vasudev
 * @version 1.0
 * @since June 2016
 * @category HTTP Client
 * 
 *           <p>
 *           A handler for processing HTTP POST request by encapsulating HTTP
 *           connection to remote server.
 *           </p>
 *
 */
public final class HttpPost {

	private String url;
	private Map<HttpHeader, String> headers;
	private Map<String, String> queryParams;
	private String requestBody;

	/**
	 * Set the HTTP Headers.
	 * 
	 * @param headers
	 *            - Map of key-value pair of request headers.
	 */
	public void setHeaders(Map<HttpHeader, String> headers) {
		this.headers = headers;
	}

	/**
	 * Set the HTTP Header.
	 * 
	 * @param key
	 *            - header key
	 * @param value
	 *            - header vaue
	 */
	public void addHeader(HttpHeader key, String value) {
		if (key != null && !key.isEmpty() && value != null && !value.isEmpty()) {
			if (this.headers == null) {
				this.headers = new HashMap<HttpHeader, String>();
			}
			this.headers.put(key, value);
		}
	}

	/**
	 * Append the query parameters to request URL.
	 * 
	 * @param queryParams
	 *            - query parameters to pass in HTTP GET request.
	 */
	public void setQueryParams(Map<String, String> queryParams) {
		this.queryParams = queryParams;
	}

	/**
	 * Append the query parameter to request URL.
	 * 
	 * @param key
	 *            - parameter key
	 * @param value
	 *            - parameter value
	 */
	public void addQueryParam(String key, String value) {
		if (key != null && !key.isEmpty() && value != null && !value.isEmpty()) {
			if (this.queryParams == null) {
				this.queryParams = new HashMap<String, String>();
			}
			this.queryParams.put(key, value);
		}
	}

	/**
	 * Set the request parameters.
	 * 
	 * @param requestBody
	 *            - parameters to pass in HTTP request.
	 */
	public void setBody(String requestBody) {
		this.requestBody = requestBody;
	}

	/**
	 * Set the request parameters.
	 * 
	 * @param requestBody
	 *            - parameters to pass in HTTP request.
	 */
	public void setBody(byte[] requestBody) {
		if (requestBody != null && requestBody.length > 0) {
			this.requestBody = new String(requestBody);
		}
	}

	/**
	 * Set the request parameters.
	 * 
	 * @param requestBody
	 *            - parameters to pass in HTTP request.
	 */
	public void setBody(JsonObject requestBody) {
		if (requestBody != null && !requestBody.isJsonNull()) {
			this.requestBody = requestBody.toString();
		}
	}
	
	private HttpResponse execute() throws HttpClientException {
		final HttpConnection conn = new HttpConnection();
		try {
			if(this.queryParams != null) {
				this.url = conn.constructGetURLWithQueryParams(this.url, this.queryParams);
			}
			conn.open(this.url, HttpMethod.POST);
			conn.addHeaders(this.headers);
			conn.addBody(this.requestBody);
			return conn.execute();
		} finally {
			conn.close();
		}
	}

	/**
	 * Execute intended operation on HTTP URL.
	 * 
	 * @param url
	 *            - HTTP URL to call
	 * @param headers
	 *            - HTTP headers to send along the request
	 * @param requestBody
	 *            - string based json/xml/url-encoded parameters to send along
	 *            the request.
	 * @return RestResponse - response returned by server
	 * @throws HttpClientException
	 */
	public HttpResponse execute(String url, Map<HttpHeader, String> headers, String requestBody)
			throws HttpClientException {
		Validator.validateParam(Key.URL, url);
		Validator.validateHeaders(Key.HEADERS, headers);
		Validator.validateParam(Key.REQUEST_BODY, requestBody);
		Validator.validateQueryParam(Key.QUERY_PARAM, this.queryParams);
		if (Validator.isError()) {
			return Validator.getErrors();
		}
		this.url = url;
		this.headers = headers;
		this.requestBody = requestBody;
		return execute();
	}

	/**
	 * Execute intended operation on HTTP URL.
	 * 
	 * @param url
	 *            - HTTP URL to call
	 * @param headers
	 *            - HTTP headers to send along the request
	 * @param requestBody
	 *            - JSON format parameters to send along the request
	 * @return RestResponse - response returned by server
	 * @throws HttpClientException
	 */
	public HttpResponse execute(String url, Map<HttpHeader, String> headers, JsonObject requestBody)
			throws HttpClientException {
		Validator.validateParam(Key.URL, url);
		Validator.validateHeaders(Key.HEADERS, headers);
		Validator.validateParam(Key.REQUEST_BODY, requestBody);
		Validator.validateQueryParam(Key.QUERY_PARAM, this.queryParams);
		if (Validator.isError()) {
			return Validator.getErrors();
		}
		this.url = url;
		this.headers = headers;
		this.requestBody = requestBody.toString();
		return execute();		
	}

	/**
	 * Execute intended operation on HTTP URL. Use this method when Request
	 * Data, Query Parameters and Headers are set individually in respective
	 * setters.
	 * 
	 * @param url
	 *            - HTTP URL to call
	 * @return RestResponse - response returned by server
	 * @throws HttpClientException
	 */
	public HttpResponse execute(String url) throws HttpClientException {
		return execute(url, this.headers, this.requestBody);
	}

}
