package com.isap.ISAProject.repository.rentacar;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.isap.ISAProject.model.rentacar.BranchOffice;
import com.isap.ISAProject.model.rentacar.VehicleReservation;


@Repository
public interface VehicleReservationRepository extends PagingAndSortingRepository<VehicleReservation, Long>, JpaSpecificationExecutor<VehicleReservation> {
	
	List<VehicleReservation> findAllByBeginDateBetween(Date begin, Date end);
	
	List<VehicleReservation> findAllByEndDateBetween(Date begin, Date end);
}
