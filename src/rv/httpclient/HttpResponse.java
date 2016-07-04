package rv.httpclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

import rv.httpclient.util.HttpHelper;
import rv.httpclient.util.ResponseCode;

/**
 * @author Ravin Vasudev
 * @version 1.0
 * @since June 2016
 * @category HTTP Client
 * 
 *           <p>
 *           A utility handler for processing HTTP response.
 *           </p>
 *
 */
public final class HttpResponse {

	private StringBuilder sb;

	private HttpAck httpAck;

	private int responseCode;

	private String responseMessage;

	private Map<String, List<String>> headers;

	public HttpResponse() {
		httpAck = new HttpAck();
	}

	public JsonObject getJson() throws HttpClientException {
		try {
			return (JsonObject) new JsonParser().parse(new StringReader(sb.toString()));
		} catch (JsonIOException e) {
			throw new HttpClientException(ResponseCode.RUNTIME_ERROR, "Failed to parse response to construct JSON.", e);
		} catch (JsonParseException e) {
			throw new HttpClientException(ResponseCode.RUNTIME_ERROR, "Failed to parse response to construct JSON.", e);
		}
	}

	public <T> T getXML(Class<T> t) throws HttpClientException {
		return HttpHelper.constructXMLResponse(t, getXML());
	}

	public String getXML() throws HttpClientException {
		return sb != null ? sb.toString() : null;
	}

	public HttpAck getHttpAck() {
		return httpAck;
	}

	public void setHttpAcke(HttpAck httpAck) {
		this.httpAck = httpAck;
	}

	public Map<String, List<String>> getHeaders() {
		return headers;
	}

	public void setHeaders(Map<String, List<String>> headers) {
		this.headers = headers;
	}

	public String getResponseMessage() {
		return this.responseMessage;
	}

	public void setResponseMessage(String responseMessage) {
		this.responseMessage = responseMessage;
	}

	public int getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
	}

	public void setResponseDate(final InputStream stream) throws IOException {
		final BufferedReader br = new BufferedReader(new InputStreamReader(stream));
		sb = new StringBuilder();
		String output = null;
		while ((output = br.readLine()) != null) {
			sb.append(output);
		}
	}

	private String printHeaders() {
		if(headers != null && !headers.isEmpty()) {
			final StringBuilder sb = new StringBuilder("Headers [");
			final Set<Entry<String, List<String>>> entrySet = headers.entrySet();
			final Iterator<Entry<String, List<String>>> it = entrySet.iterator();
			while (it.hasNext()) {
				final Entry<String, List<String>> entry = it.next();
				final String key = entry.getKey();
				final List<String> values = entry.getValue();
				for (String value : values) {
					sb.append(String.format("[key=%s, value=%s]", key, value)).append(" ");
				}
			}
			sb.append("]");
			return sb.toString();
		}
		return null;
	}

	@Override
	public String toString() {
		return String.format("HttpResponse [httpAck=%s, responseCode=%s, responseMessage=%s, headers=%s]", httpAck,
				responseCode, responseMessage, printHeaders());
	}

}