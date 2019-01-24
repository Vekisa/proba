package service.hotel;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.isap.ISAProject.model.hotel.Catalogue;
import com.isap.ISAProject.model.hotel.Hotel;
import com.isap.ISAProject.repository.hotel.CatalogueRepository;

@Service
@Transactional(readOnly = true)
public class CatalogueService {
	
private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private CatalogueRepository catalogueRepository;
	
	public Optional<Catalogue> findById(long id) {
		logger.info("> findById id:{}", id);
		Optional<Catalogue> catalogue = catalogueRepository.findById(id);
		logger.info("< findById id:{}", id);
		return catalogue;
	}
	
	public Page<Catalogue> findAll(Pageable pageable) {
		logger.info("> findAll");
		Page<Catalogue> catalogues = catalogueRepository.findAll(pageable);
		logger.info("< findAll");
		return catalogues;
	}
	
	@Transactional(readOnly = false)
	public Catalogue save(Catalogue catalogue) {
		logger.info("> create");
		Catalogue savedCatalogue = catalogueRepository.save(catalogue);
		logger.info("< create");
		return savedCatalogue;
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void deleteById(long id) {
		logger.info("> delete");
		catalogueRepository.deleteById(id);
		logger.info("< delete");
	}

}
