package repository.hotel;

import org.springframework.data.jpa.repository.JpaRepository;

import model.hotel.Catalogue;

public interface CatalogueRepository extends JpaRepository<Catalogue, Long> {

}
