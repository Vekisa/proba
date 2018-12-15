package repository.hotel;

import org.springframework.data.jpa.repository.JpaRepository;

import com.isap.ISAProject.model.Hotel.Hotel;

public interface HotelRepository extends JpaRepository<Hotel, Long> {

}
