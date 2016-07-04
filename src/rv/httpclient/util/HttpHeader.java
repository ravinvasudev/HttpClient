package rv.httpclient.util;

/**
 * @author Ravin Vasudev
 * @version 1.0
 * @since June 2016
 * @category HTTP Client
 * 
 *           <p>
 * 
 *           </p>
 *
 */
public enum HttpHeader {

	ACCEPT("accept"),

	Accept_Language("accept-language"),

	AUTHORIZATION("authorization"),

	CONTENT_TYPE("content-type"),

	CACHE_CONTROL("cache-control");

	private String header;

	HttpHeader(String header) {
		this.header = header;
	}

	public String get() {
		return this.header;
	}

	public boolean isEmpty() {
		return header != null ? header.isEmpty() : true;
	}

}
