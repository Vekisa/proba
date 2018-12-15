package repository.hotel;

import org.springframework.data.jpa.repository.JpaRepository;

import model.hotel.Hotel;

public interface HotelRepository extends JpaRepository<Hotel, Long> {

}
