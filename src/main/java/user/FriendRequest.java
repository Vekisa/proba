package user;

import java.util.Date;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class FriendRequest {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	private Date requestTime;
	private RegisteredUser sender;
	private RegisteredUser receiver;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Date getRequestTime() {
		return requestTime;
	}
	public void setRequestTime(Date requestTime) {
		this.requestTime = requestTime;
	}
	public RegisteredUser getSender() {
		return sender;
	}
	public void setSender(RegisteredUser sender) {
		this.sender = sender;
	}
	public RegisteredUser getReceiver() {
		return receiver;
	}
	public void setReceiver(RegisteredUser receiver) {
		this.receiver = receiver;
	}
	
	

}
