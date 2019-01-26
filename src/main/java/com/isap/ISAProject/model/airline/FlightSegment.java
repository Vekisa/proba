package com.isap.ISAProject.model.airline;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "flight_segment")
public class FlightSegment {
	
	@JsonIgnore
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@Version
	private Long version;
	
	@Column(nullable = false)
	private int startRow;
	
	@Column(nullable = false)
	private int endRow;
	
	@Column(nullable = false)
	private int columns;
	
	@JsonIgnore
	@ManyToOne
	private FlightConfiguration configuration;
	
	@JsonIgnore
	@ManyToOne
	private FlightSeatCategory category;

	public void setConfiguration(FlightConfiguration flightConfiguration) { this.configuration = flightConfiguration; }
	
	public int getStartRow() { return this.startRow; }
	
	public int getEndRow() { return this.endRow; }

	public void setColumns(int columns) { this.columns = columns; }

	public int getColumns() { return this.columns; }

	public void setEndRow(int endRow) { this.endRow = endRow; }

	public void setStartRow(int startRow) { this.startRow = startRow; }

	public Long getId() { return this.id; }

	public FlightConfiguration getConfiguration() { return this.configuration; }
	
	public FlightSeatCategory getCategory() { return this.category; }
	
}
