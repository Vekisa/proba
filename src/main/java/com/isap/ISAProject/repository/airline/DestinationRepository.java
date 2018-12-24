package com.isap.ISAProject.repository.airline;

import org.springframework.data.jpa.repository.JpaRepository;

import com.isap.ISAProject.model.airline.Destination;

public interface DestinationRepository extends JpaRepository<Destination, Long> {

}
