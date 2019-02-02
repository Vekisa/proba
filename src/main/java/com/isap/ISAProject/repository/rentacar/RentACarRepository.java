package com.isap.ISAProject.repository.rentacar;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.isap.ISAProject.model.rentacar.RentACar;

@Repository
public interface RentACarRepository extends JpaRepository<RentACar, Long> {
	List<RentACar> findByName(String name);

	@Modifying
	@Query(value = "update isap.vehicle set rating = (select avg(rating) from isap.vehicle_rating where rating > 0 && vehicle_id = ?1) where id = ?1", nativeQuery = true)
	void updateVehicleRating(Long vehicleId);

	@Modifying
	@Query(value = "update isap.rent_a_car set rating = (select avg(rating) from isap.rentacar_rating where rating > 0 && rentacar_id = ?1) where id = ?1", nativeQuery = true)
	void updateRentACarRating(Long rentACarId);

}
