package com.isap.ISAProject.repository.airline;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.isap.ISAProject.model.airline.Location;

@Transactional(propagation = Propagation.MANDATORY)
public interface LocationRepository extends JpaRepository<Location, Long> {

	@Modifying
	@Query(value = "update isap.airline set location_id = null where location_id = ?1", nativeQuery = true)
	public void removeSelfFromAirlines(Long id);

	@Modifying
	@Query(value = "update isap.branch_office set location_id = null where location_id = ?1", nativeQuery = true)
	public void removeSelfFromBranchOffices(Long destinationId);

	@Modifying
	@Query(value = "update isap.hotel set location_id = null where location_id = ?1", nativeQuery = true)
	public void removeSelfFromHotels(Long destinationId);
	
	List<Location> findByName(String name);
}
