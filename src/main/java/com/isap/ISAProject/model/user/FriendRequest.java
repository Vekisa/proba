package com.isap.ISAProject.model.user;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "friend_request")
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = {"requestTime"}, 
        allowGetters = true)
public class FriendRequest {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@Column(nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date requestTime;
	
	@ManyToOne
	private RegisteredUser sender;
	
	@ManyToOne
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
