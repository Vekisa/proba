package com.isap.ISAProject.repository.rentacar;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.isap.ISAProject.model.rentacar.BranchOffice;


@Repository
public interface BranchOfficeRepository extends PagingAndSortingRepository<BranchOffice, Long>, JpaSpecificationExecutor<BranchOffice> {
}
