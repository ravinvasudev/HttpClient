package rv.httpclient;

import rv.httpclient.util.ResponseCode;
import rv.httpclient.util.Status;

/**
 * @author Ravin Vasudev
 * @version 1.0
 * @since June 2016
 * @category HTTP Client
 * 
 *           <p>
 *           Represents acknowledgement object of HTTP Request sent. Error Code
 *           and Error Message represents the values as returned in HTTP server.
 *           </p>
 *
 */
public class HttpAck {

	private Status status;

	private int errorCode;

	private ResponseCode errorType;

	private String errorMessage;

	public HttpAck() {

	}

	/** Represents general request status. It can be SUCCESS or FAILURE. */
	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	/** Error Type represents the general error category. */
	public ResponseCode getErrorType() {
		return errorType;
	}

	public void setErrorType(ResponseCode errorType) {
		this.errorType = errorType;
	}

	/** Code returned by the server. */
	public int getErrorCode() {
		return this.errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	/** Message returned by the server. */
	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	@Override
	public String toString() {
		return String.format("HttpAck [status=%s, errorType=%s, errorCode=%s, errorMessage=%s]", status, errorType,
				errorCode, errorMessage);
	}
}