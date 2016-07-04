package rv.httpclient.util;

/**
 * @author Ravin Vasudev
 * @version 1.0
 * @since June 2016
 * @category HTTP Client
 * 
 *           <p>
 *           Represents the overall general request status.
 *           </p>
 *
 */
public enum Status {

	SUCCESS("SUCCESS"),

	FAILURE("FAILURE");

	private String status;

	private Status(String status) {
		this.status = status;
	}

	public String get() {
		return this.status;
	}

}
