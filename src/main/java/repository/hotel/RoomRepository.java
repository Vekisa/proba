package repository.hotel;

import org.springframework.data.jpa.repository.JpaRepository;

import model.hotel.Room;

public interface RoomRepository extends JpaRepository<Room, Long> {

}
