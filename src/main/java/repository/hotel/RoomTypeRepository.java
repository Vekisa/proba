package repository.hotel;

import org.springframework.data.jpa.repository.JpaRepository;

import model.hotel.RoomType;

public interface RoomTypeRepository extends JpaRepository<RoomType, Long> {

}
