package rv.httpclient;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonObject;

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
 *           A handler for processing HTTP requests by encapsulating background
 *           stuff.
 *           </p>
 *
 */
public final class HttpRequest {

	private String url;
	private HttpMethod httpMethod;
	/*private Map<HttpHeader, String> headers;*/
	private Map<String, String> headers;	
	private Map<String, String> queryParams;
	private String requestBody;

	/**
	 * Set the HTTP method for the URL request. It can be one of the
	 * <li>GET</li>
	 * <li>POST</li>
	 * <li>HEAD</li>
	 * <li>OPTIONS</li>
	 * <li>PUT</li>
	 * <li>DELETE</li>
	 * <li>TRACE</li>
	 * 
	 * @param method
	 *            - HTTP method (The default method is GET)
	 */
	public void setHttpMethod(HttpMethod method) {
		if (method == null) {
			this.httpMethod = HttpMethod.GET;
		} else {
			this.httpMethod = method;
		}
	}

	/**
	 * Set the HTTP Headers.
	 * 
	 * @param headers
	 *            - Map of key-value pair of request headers.
	 */
	public void setHeaders(Map<String, String> headers) {
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
	public void addHeader(String key, String value) {
		if (key != null && !key.isEmpty() && value != null && !value.isEmpty()) {
			if (this.headers == null) {
				this.headers = new HashMap<String, String>();
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
			if (this.queryParams != null) {
				this.url = conn.constructGetURLWithQueryParams(this.url, this.queryParams);
			}
			conn.open(this.url, this.httpMethod);
			conn.addHeaders(this.headers);
			conn.addBody(this.requestBody);
			return conn.execute();
		} finally {
			conn.close();
		}
	}

	/**
	 * Execute operation on HTTP URL.
	 * 
	 * @param url
	 *            - HTTP URL to call
	 * @param httpMethod
	 *            - HTTP method to use
	 * @param queryParams
	 *            - query parameters to append to HTTP URL
	 * @param headers
	 *            - HTTP headers to send along the request
	 * @param requestBody
	 *            - parameters to send along the request
	 * @return HttpResponse - response returned by server
	 * @throws HttpClientException
	 */
	public HttpResponse execute(String url, HttpMethod httpMethod, Map<String, String> queryParams,
			Map<String, String> headers, String requestBody) throws HttpClientException {
		Validator.validateParam(Key.URL, url);
		Validator.validateSpecialParam(Key.HTTP_METHOD, httpMethod);
		Validator.validateQueryParam(Key.QUERY_PARAM, queryParams);
		Validator.validateHeaders(Key.HEADERS, headers);
		Validator.validateParam(Key.REQUEST_BODY, requestBody);
		if (Validator.isError()) {
			return Validator.getErrors();
		}
		this.url = url;
		this.httpMethod = httpMethod;
		this.queryParams = queryParams;
		this.headers = headers;
		this.requestBody = requestBody;
		return execute();
	}

	/**
	 * Execute operation on HTTP URL.
	 * 
	 * @param url
	 *            - HTTP URL to call
	 * @param httpMethod
	 *            - HTTP method to use
	 * @param queryParams
	 *            - query parameters to append to HTTP URL
	 * @param headers
	 *            - HTTP headers to send along the request
	 * @param requestBody
	 *            - JSON format parameters to send along the request
	 * @return HttpResponse - response returned by server
	 * @throws HttpClientException
	 */
	public HttpResponse execute(String url, HttpMethod httpMethod, Map<String, String> queryParams,
			Map<String, String> headers, JsonObject requestBody) throws HttpClientException {
		Validator.validateParam(Key.URL, url);
		Validator.validateSpecialParam(Key.HTTP_METHOD, httpMethod);
		Validator.validateQueryParam(Key.QUERY_PARAM, queryParams);
		Validator.validateHeaders(Key.HEADERS, headers);
		Validator.validateParam(Key.REQUEST_BODY, requestBody);
		if (Validator.isError()) {
			return Validator.getErrors();
		}
		this.url = url;
		this.httpMethod = httpMethod;
		this.queryParams = queryParams;
		this.headers = headers;
		this.requestBody = requestBody.toString();
		return execute();
	}

	/**
	 * Execute operation on HTTP URL. Use this method when Method, Request Data,
	 * Query Parameters and Headers are set individually in respective setters.
	 * 
	 * @param url
	 *            - HTTP URL to call
	 * @return HttpResponse - response returned by server
	 * @throws HttpClientException
	 */
	public HttpResponse execute(String url) throws HttpClientException {
		Validator.validateParam(Key.URL, url);
		if (Validator.isError()) {
			return Validator.getErrors();
		}
		this.url = url;
		return execute();
	}

}
