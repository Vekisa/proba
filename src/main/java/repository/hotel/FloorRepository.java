package repository.hotel;

import org.springframework.data.jpa.repository.JpaRepository;

import model.hotel.Floor;

public interface FloorRepository extends JpaRepository<Floor, Long> {

}
