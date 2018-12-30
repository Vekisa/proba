package com.isap.ISAProject.repository.user;

import org.springframework.data.repository.PagingAndSortingRepository;

import com.isap.ISAProject.model.user.Friendship;

public interface FriendshipRepository extends PagingAndSortingRepository<Friendship, Long>{

}
