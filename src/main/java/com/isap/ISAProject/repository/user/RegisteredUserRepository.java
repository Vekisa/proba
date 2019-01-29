package com.isap.ISAProject.repository.user;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.isap.ISAProject.model.user.RegisteredUser;

//@Transactional(propagation = Propagation.MANDATORY)
public interface RegisteredUserRepository extends PagingAndSortingRepository<RegisteredUser, Long> {
	
	public RegisteredUser findByUsername(String username);

	public RegisteredUser findByEmail(String email);
	
	@Query(value = "select * from isap.registered_user where id in (select user_id from isap.friendships_between_users where friendship_id in "
			+ "(select friendship_id from isap.friendships_between_users where user_id = ?1) and user_id != ?1)", nativeQuery = true)
	public List<RegisteredUser> findFriendsOfUser(Long id);
	
}
