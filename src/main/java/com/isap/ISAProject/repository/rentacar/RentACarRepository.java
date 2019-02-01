package com.isap.ISAProject.repository.rentacar;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.isap.ISAProject.model.rentacar.RentACar;

@Repository
public interface RentACarRepository extends JpaRepository<RentACar, Long> {
	List<RentACar> findByName(String name);
}
