package rv.httpclient.util;

import java.util.Map;

import com.google.gson.JsonObject;

import rv.httpclient.HttpAck;
import rv.httpclient.HttpResponse;

/**
 * @author Ravin Vasudev
 * @version 1.0
 * @since June 2016
 * @category HTTP Client
 * 
 * 	<p>
 * 		
 * 	</p>
 *
 */
public class Validator {
	
	public enum Key {
		
		URL("URL"),
		
		HTTP_METHOD("HTTP_METHOD"),
		
		HEADERS("HEADERS"),
		
		QUERY_PARAM("QUERY_PARAM"),
		
		REQUEST_BODY("REQUEST_BODY");
		
		private String value;
		
		Key(String value) {
			this.value = value;
		}
		
		public String get() {
			return this.value;
		}

	}
	
	private static StringBuilder message = new StringBuilder();
	private static boolean error = Boolean.FALSE;
	
	public static void validateParam(Key paramName, String paramValue) {
		if(paramValue == null || paramValue.isEmpty()) {
			message.append(String.format(" [Parameter<%s> cannot be left empty.] ", paramName.get()));
			error = Boolean.TRUE;
		}
	}
	
	public static void validateParam(Key paramName, JsonObject paramValue) {
		if(paramValue == null || paramValue.isJsonNull()) {
			message.append(String.format(" [Parameter<%s> cannot be left empty.] ", paramName.get()));
			error = Boolean.TRUE;
		}
	}

	public static void validateSpecialParam(Key paramName, HttpMethod paramValue) {
		if(paramName.equals(Key.HTTP_METHOD)) {
			if(paramValue == null) {
				paramValue = HttpMethod.GET;
			} else {
				switch(paramValue) {
					case GET:
					case POST:
					case PUT:
					case DELETE:
					case OPTIONS:
					case HEAD:
						break;
					default:
						message.append(String.format(" [Parameter<%s> is not valid.] ", paramName));
						error = Boolean.TRUE;
				}
			}
		}
	}

	public static void validateHeaders(Key paramName, Map<String, String> headerMap) {
		//do nothing
	}
	
	public static void validateQueryParam(Key queryParam, Map<String, String> queryParams) {
		//do nothing
	}

	public static boolean isError() {
		return error;
	}

	public static HttpResponse getErrors() {
		if(isError()) {
			final HttpResponse httpResponse = new HttpResponse();
			final HttpAck ack = httpResponse.getHttpAck();
			ack.setStatus(Status.FAILURE);
			ack.setErrorType(ResponseCode.VALIDATION_ERROR);
			ack.setErrorCode(ResponseCode.VALIDATION_ERROR.get());
			ack.setErrorMessage(message.toString());
			return httpResponse;
		}
		return null;
	}
}
