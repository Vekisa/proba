package com.isap.ISAProject.repository.rentacar;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.isap.ISAProject.model.airline.Location;
import com.isap.ISAProject.model.rentacar.BranchOffice;


@Repository
public interface BranchOfficeRepository extends JpaRepository<BranchOffice, Long> {
	BranchOffice findByRentACarAndLocation(int rentacar, int location);
	
	List<BranchOffice> findByLocation(Location location);
}
