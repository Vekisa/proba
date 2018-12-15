package repository.rentacar;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import model.rentacar.VehicleReservation;


@Repository
public interface VehicleReservationRepository extends JpaRepository<VehicleReservation, Long> {

}
