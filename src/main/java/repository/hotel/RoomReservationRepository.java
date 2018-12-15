package repository.hotel;

import org.springframework.data.jpa.repository.JpaRepository;

import model.hotel.RoomReservation;

public interface RoomReservationRepository extends JpaRepository<RoomReservation, Long> {

}
