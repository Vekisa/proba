package com.isap.ISAProject.repository.hotel;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.isap.ISAProject.model.hotel.Catalogue;

public interface CatalogueRepository extends PagingAndSortingRepository<Catalogue, Long> {

}
