package repository.hotel;

import org.springframework.data.jpa.repository.JpaRepository;

import com.isap.ISAProject.model.Hotel.RoomType;

public interface RoomTypeRepository extends JpaRepository<RoomType, Long> {

}
