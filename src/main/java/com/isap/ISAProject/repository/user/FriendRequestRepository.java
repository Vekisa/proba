package com.isap.ISAProject.repository.user;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.isap.ISAProject.model.user.FriendRequest;

@Transactional(propagation = Propagation.MANDATORY)
public interface FriendRequestRepository  extends PagingAndSortingRepository<FriendRequest, Long>{

	@Query(value = "select * from friend_request where (receiver_id = ?1 and sender_id = ?2) or (receiver_id = ?2 and sender_id = ?1)", nativeQuery = true)
	FriendRequest getFriendRequestBetween(Long id1, Long id2);
	
}
