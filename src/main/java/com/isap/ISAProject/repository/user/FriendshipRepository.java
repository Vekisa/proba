package com.isap.ISAProject.repository.user;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.isap.ISAProject.model.user.Friendship;

@Transactional(propagation = Propagation.MANDATORY)
public interface FriendshipRepository extends PagingAndSortingRepository<Friendship, Long>{

	@Query(value = "select * from friendship where id in (select fbu1.friendship_id from friendships_between_users fbu1"
			+ " join friendships_between_users fbu2 on fbu1.friendship_id = fbu2.friendship_id where fbu1.user_id = ?1 and fbu2.user_id = ?2)", 
			nativeQuery = true)
	Friendship findFriendshipOf(Long self, Long friend);

}
