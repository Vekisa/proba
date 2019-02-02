package com.isap.ISAProject.model;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class RatableEntity {
	
	@Column(nullable = false)
	private double rating;
	
	public double getRating() { return rating; }
	
	public void setRating(double rating) { this.rating = rating; }
	
}
