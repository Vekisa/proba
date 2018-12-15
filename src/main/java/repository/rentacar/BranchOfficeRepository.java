package repository.rentacar;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import model.rentacar.BranchOffice;


@Repository
public interface BranchOfficeRepository extends JpaRepository<BranchOffice, Long> {

}
