package com.isap.ISAProject.repository.rentacar;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.isap.ISAProject.model.rentacar.RentACar;

@Repository
public interface RentACarRepository extends PagingAndSortingRepository<RentACar, Long>, JpaSpecificationExecutor<RentACar> {
}
