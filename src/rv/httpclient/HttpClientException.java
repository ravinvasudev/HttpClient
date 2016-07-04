package rv.httpclient;

import rv.httpclient.util.ResponseCode;

/**
 * @author Ravin Vasudev
 * @version 1.0
 * @since June 2016
 * @category HTTP Client
 * 
 *           <p>
 *           Exception wrapper class to encapsulate all reasons of communication
 *           failure during execution.
 *           </p>
 */
public final class HttpClientException extends Exception {

	private static final long serialVersionUID = 1L;

	private ResponseCode code;

	public HttpClientException() {
		super();
	}

	public HttpClientException(String message) {
		super(message);
	}

	public HttpClientException(ResponseCode code, String message) {
		super(message);
		this.code = code;
	}

	public HttpClientException(ResponseCode code, String message, Throwable cause) {
		super(message, cause);
		this.code = code;
	}

	public int getErrorCode() {
		return this.code.get();
	}

	public ResponseCode getResponseCode() {
		return this.code;
	}

	@Override
	public String toString() {
		return String.format("HttpClientException [ErrorCode=%s] [ErrorMessage=%s]", code, getMessage());
	}

}
