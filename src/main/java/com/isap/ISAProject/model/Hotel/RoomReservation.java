package com.isap.ISAProject.model.Hotel;

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

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "room_reservation")
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = {"beginDate", "endDate"}, 
        allowGetters = true)
public class RoomReservation {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@Column(nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
	private Date beginDate;
	
	@Column(nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date endDate;
	
	@Column(nullable = false)
	private int numberOfNights;

	@Column(nullable = false)
	private int numberOfGuests;
	
	@Column(nullable = false)
	private int numberOfRooms;
	
	@ManyToMany(mappedBy="roomReservation")
	private List<Room> room;
}
