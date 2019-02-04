package com.isap.ISAProject.repository.hotel;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.isap.ISAProject.model.hotel.Room;

public interface RoomRepository extends PagingAndSortingRepository<Room, Long>, JpaSpecificationExecutor<Room> {

}
