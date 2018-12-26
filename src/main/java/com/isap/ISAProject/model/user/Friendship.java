package com.isap.ISAProject.model.user;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "friendship")
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = {"friendsSince"}, 
        allowGetters = true)
public class Friendship {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@Column(nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date friendsSince;
	
	@ManyToOne
	private RegisteredUser self;
	
	@OneToOne
	private RegisteredUser friend;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Date getFriendsSince() {
		return friendsSince;
	}
	public void setFriendsSince(Date friendsSince) {
		this.friendsSince = friendsSince;
	}
	public RegisteredUser getFriend() {
		return friend;
	}
	public void setFriend(RegisteredUser friend1) {
		this.friend = friend1;
	}


}
