package com.isap.ISAProject.repository.user;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.isap.ISAProject.model.user.ConfirmationToken;

public interface ConfirmationTokenRepository extends PagingAndSortingRepository<ConfirmationToken, Long> {
	@Query(value = "select * from isap.confirmation_token where confirmation_token = ?1"
			, nativeQuery = true)
	public ConfirmationToken findByString(String token);
}
