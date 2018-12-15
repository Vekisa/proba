package com.isap.ISAProject.repository.rentacar;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.isap.ISAProject.model.rentacar.BranchOffice;


@Repository
public interface BranchOfficeRepository extends JpaRepository<BranchOffice, Long> {

}
