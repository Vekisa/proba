package repository.hotel;

import org.springframework.data.jpa.repository.JpaRepository;

import com.isap.ISAProject.model.Hotel.Catalogue;

public interface CatalogueRepository extends JpaRepository<Catalogue, Long> {

}
