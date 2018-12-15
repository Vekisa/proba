package repository.hotel;

import org.springframework.data.jpa.repository.JpaRepository;

import com.isap.ISAProject.model.Hotel.RoomReservation;

public interface RoomReservationRepository extends JpaRepository<RoomReservation, Long> {

}
