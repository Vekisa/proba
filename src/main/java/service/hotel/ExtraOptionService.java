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

import com.isap.ISAProject.model.hotel.ExtraOption;
import com.isap.ISAProject.repository.hotel.ExtraOptionRepository;

@Service
@Transactional(readOnly = true)
public class ExtraOptionService {
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private ExtraOptionRepository extraOptionRepository;
	
	public Optional<ExtraOption> findById(long id) {	
		logger.info("> findById id:{}", id);
		Optional<ExtraOption> extraOption = extraOptionRepository.findById(id);
		logger.info("< findById id:{}", id);
		return extraOption;
	}
	
	public Page<ExtraOption> findAll(Pageable pageable) {
		logger.info("> findAll");
		Page<ExtraOption> extraOptions = extraOptionRepository.findAll(pageable);
		logger.info("< findAll");
		return extraOptions;
	}
	
	@Transactional(readOnly = false)
	public ExtraOption save(ExtraOption extraOption) {
		logger.info("> create");
		ExtraOption savedExtraOption = extraOptionRepository.save(extraOption);
		logger.info("< create");
		return savedExtraOption;
	}
	
	@Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW)
	public void deleteById(long id) {
		logger.info("> delete");
		extraOptionRepository.deleteById(id);
		logger.info("< delete");
	}

}
