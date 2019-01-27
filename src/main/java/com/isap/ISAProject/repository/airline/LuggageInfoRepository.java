package com.isap.ISAProject.repository.airline;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.isap.ISAProject.model.airline.LuggageInfo;

@Transactional(propagation = Propagation.MANDATORY)
public interface LuggageInfoRepository extends PagingAndSortingRepository<LuggageInfo, Long> {

}
