package repository.hotel;

import org.springframework.data.jpa.repository.JpaRepository;

import com.isap.ISAProject.model.Hotel.Room;

public interface RoomRepository extends JpaRepository<Room, Long> {

}
