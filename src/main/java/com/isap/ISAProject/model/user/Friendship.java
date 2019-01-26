package com.isap.ISAProject.model.user;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
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
	
	@ManyToMany(mappedBy = "friendships")
	private List<RegisteredUser> friends;
	
	public Long getId() { return id; }
	
	public Date getFriendsSince() { return friendsSince; }
	
	public void setFriendsSince(Date friendsSince) { this.friendsSince = friendsSince; }

}
