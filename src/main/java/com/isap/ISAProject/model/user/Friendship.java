package com.isap.ISAProject.model.user;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "friendship")
@EntityListeners(AuditingEntityListener.class)
public class Friendship {
	
	@JsonIgnore
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@Column(nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date friendsSince;
	
	@JsonIgnore
	@Cascade({CascadeType.PERSIST, CascadeType.MERGE})
	@ManyToMany
	@JoinTable(name = "friendships_between_users",
	   joinColumns = { @JoinColumn(name = "friendship_id") },
    inverseJoinColumns = { @JoinColumn(name = "user_id")} )
	private List<RegisteredUser> friends;
	
	public Friendship() {
		this.friends = new ArrayList<RegisteredUser>();
	}
	
	public Long getId() { return id; }
	
	public Date getFriendsSince() { return friendsSince; }
	
	public void setFriendsSince(Date friendsSince) { this.friendsSince = friendsSince; }

	public List<RegisteredUser> getFriends() { return this.friends; }
	
}
