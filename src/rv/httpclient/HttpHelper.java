package rv.httpclient.util;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.DatatypeConverter;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.PropertyException;
import javax.xml.bind.Unmarshaller;

import rv.httpclient.HttpClientException;

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
public final class HttpHelper {
	
	private static final String ERROR_MSG = "There was some problem while performing requested operation.";
	
	/**
	 * Method to get encoded Authorization header based on Client ID & Client Secret.
	 * 
	 * @param auth - one of the {@link Auth} type
	 * @param clientId - clientId to use to produce encoded Authorization header
	 * @param clientSecret - clientSecret to use to produce encoded Authorization header
	 * */
	public static String encodeCredentials(final Auth auth, final String clientId, final String clientSecret) throws HttpClientException {
		try {
			final StringBuilder credential = new StringBuilder(clientId).append(":").append(clientSecret);
			/*final byte[] encodedBytes = java.util.Base64.getEncoder().encode(credential.toString().getBytes());*/			
			final byte[] encodedBytes = DatatypeConverter.parseBase64Binary(credential.toString());			
			return new StringBuilder(auth.get()).append(" ").append(new String(encodedBytes)).toString();
		} catch(NullPointerException e) {
			throw new HttpClientException(ResponseCode.VALIDATION_ERROR, ERROR_MSG, e);
		}
	}

	/**
	 * Method to get Authorization header based on Token.
	 * 
	 * @param auth - one of the {@link Auth} type
	 * @param token - token to use to produce encoded Authorization header
	 * @throws HttpClientException 
	 * */
	public static String encodeCredentials(Auth auth, String token) throws HttpClientException {
		try {
			return new StringBuilder(auth.get()).append(" ").append(token).toString();
		} catch(NullPointerException e) {
			throw new HttpClientException(ResponseCode.VALIDATION_ERROR, ERROR_MSG, e);
		}
	}
	
	/**
	 * Use this method to transform Java POJO object to a String.
	 * @param t - Java POJO Object
	 * @return String object
	 * @throws HttpClientException
	 * */
	public static <T> String constructXMLRequest(T t) throws HttpClientException {
		final StringWriter sw = new StringWriter();
		try {
			final JAXBContext requestContext = JAXBContext.newInstance(t.getClass());
			final Marshaller marshaller = requestContext.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);		
			marshaller.marshal(t, sw);
		} catch(PropertyException e) {
			throw new HttpClientException(ResponseCode.RUNTIME_ERROR, ERROR_MSG, e);
		} catch(JAXBException e) {
			throw new HttpClientException(ResponseCode.RUNTIME_ERROR, ERROR_MSG, e);
		}
		return sw.toString();
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T constructXMLResponse(Class<T> t, String response) throws HttpClientException {
		if(response != null && !response.isEmpty()) {
			try {
				JAXBContext respContext = JAXBContext.newInstance(t);
				Unmarshaller unmarshaller = respContext.createUnmarshaller();
				StringReader reader = new StringReader(response.toString());
				return (T) unmarshaller.unmarshal(reader);
			} catch (JAXBException e) {
				throw new HttpClientException(ResponseCode.RUNTIME_ERROR, ERROR_MSG, e);
			}
		}
		return null;
	}

}
