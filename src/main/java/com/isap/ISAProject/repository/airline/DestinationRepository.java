package com.isap.ISAProject.repository.airline;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.isap.ISAProject.model.airline.Destination;

@Transactional(propagation = Propagation.MANDATORY)
public interface DestinationRepository extends JpaRepository<Destination, Long> {

}
