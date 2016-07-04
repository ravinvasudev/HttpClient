package rv.httpclient.util;

public enum ContentType {
	
	JSON("application/json"),
	
	XML("application/xml"),
	
	FORM_DATA("multipart/form-data"),
	
	FORM_URL_ENCODED("application/x-www-form-urlencoded");
	
	private String type;
	
	ContentType(String type) {
		this.type = type;
	}
	
	public String get() {
		return this.type;
	}

}
