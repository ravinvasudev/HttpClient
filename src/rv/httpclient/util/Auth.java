package rv.httpclient.util;

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
public enum Auth {

	BASIC("Basic"),

	BEARER("Bearer");

	private String auth;

	private Auth(String auth) {
		this.auth = auth;
	}

	public String get() {
		return this.auth;
	}
}
