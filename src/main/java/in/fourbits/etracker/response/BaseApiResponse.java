package in.fourbits.etracker.response;

import java.util.Date;
import java.util.List;

public class BaseApiResponse {
	
	private String timeStamp;
	private String code;
	private List<String> messages;
	
	public BaseApiResponse() {		
	}
	
	public BaseApiResponse(String code, List messages) {		
		this.timeStamp = (new Date()).getTime()+"";
		this.code = code;
		this.messages = messages;
	}

	public BaseApiResponse(String timeStamp, String code, List messages) {		
		this.timeStamp = timeStamp;
		this.code = code;
		this.messages = messages;
	}
	
	public String getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}

	public List<String> getMessages() {
		return messages;
	}

	public void setMessage(List<String> messages) {
		this.messages = messages;
	}
	

}
