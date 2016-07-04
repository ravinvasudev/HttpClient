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
public enum ResponseCode {

	SUCCESS(00),

	VALIDATION_ERROR(002),

	RUNTIME_ERROR(003);

	private int code;

	private ResponseCode(int code) {
		this.code = code;
	}

	public int get() {
		return this.code;
	}

}
