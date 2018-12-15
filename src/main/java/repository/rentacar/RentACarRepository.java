package repository.rentacar;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import model.rentacar.RentACar;

@Repository
public interface RentACarRepository extends JpaRepository<RentACar, Long> {

}
