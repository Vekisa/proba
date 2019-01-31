package com.isap.ISAProject.model;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class RatableEntity {
	
	@Column(nullable = false)
	private double rating;

	private long timesRated;
	private long cumulativeRating;
	
	public void addRating(int rating) {
		this.timesRated++;
		this.cumulativeRating += rating;
	}
	
	public double getRating() {
		return (double) cumulativeRating / timesRated;
	}
	
	public void setRating(double rating) { this.rating = rating; }
	
}
