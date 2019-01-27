package com.isap.ISAProject.repository.airline;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.isap.ISAProject.model.airline.Destination;

@Transactional(propagation = Propagation.MANDATORY)
public interface DestinationRepository extends JpaRepository<Destination, Long> {

	@Modifying
	@Query(value = "update isap.airline set location_id = null where location_id = ?1", nativeQuery = true)
	public void removeSelfFromAirlines(Long id);

	@Modifying
	@Query(value = "update isap.branch_office set location_id = null where location_id = ?1", nativeQuery = true)
	public void removeSelfFromBranchOffices(Long destinationId);

	@Modifying
	@Query(value = "update isap.hotel set location_id = null where location_id = ?1", nativeQuery = true)
	public void removeSelfFromHotels(Long destinationId);
	
}
