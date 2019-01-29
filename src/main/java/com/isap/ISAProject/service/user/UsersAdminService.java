package com.isap.ISAProject.service.user;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.isap.ISAProject.model.user.UsersAdmin;
import com.isap.ISAProject.repository.user.CompanyAdminRepository;
import com.isap.ISAProject.repository.user.RegisteredUserRepository;
import com.isap.ISAProject.repository.user.UsersAdminRepository;
import com.isap.ISAProject.serviceInterface.user.UsersAdminServiceInterface;

@Service
public class UsersAdminService implements UsersAdminServiceInterface {

	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private CompanyAdminRepository companyAdminsRepository;
	
	@Autowired
	private UsersAdminRepository usersAdminsRepository;
	
	@Autowired
	private RegisteredUserRepository registeredUsersRepository;
	
	@Override
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
	public List<UsersAdmin> findAll(Pageable pageable) {
		logger.info("> fetching users admins");
		Page<UsersAdmin> admins = usersAdminsRepository.findAll(pageable);
		logger.info("< users admins fetched");
		return admins.getContent();
	}

	@Override
	@Transactional(readOnly = true, propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
	public UsersAdmin findById(Long id) {
		logger.info("> fetching users admin with id {}", id);
		Optional<UsersAdmin> admin = usersAdminsRepository.findById(id);
		logger.info("< users admin fetched");
		if(admin.isPresent()) return admin.get();
		throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Requested admin doesn't exist.");
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
	public void delete(Long id) {
		logger.info("> deleting users admin with id {}", id);
		UsersAdmin admin = this.findById(id);
		usersAdminsRepository.delete(admin);
		logger.info("< users admin deleted");
	}

	@Override
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
	public UsersAdmin update(Long id, UsersAdmin newAdmin) {
		logger.info("> updating users admin with id {}", id);
		checkIfUsernameExists(newAdmin.getUsername());
		UsersAdmin oldAdmin = this.findById(id);
		oldAdmin.setCity(newAdmin.getCity());
		oldAdmin.setFirstName(newAdmin.getFirstName());
		oldAdmin.setLastName(newAdmin.getLastName());
		oldAdmin.setPassword(newAdmin.getPassword());
		oldAdmin.setPhoneNumber(newAdmin.getPhoneNumber());
		oldAdmin.setUsername(newAdmin.getUsername());
		logger.info("< users admin updated");
		return oldAdmin;
	}
	
	private void checkIfUsernameExists(String username) {
		if(companyAdminsRepository.findByUsername(username) != null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already in use.");
		if(usersAdminsRepository.findByUsername(username) != null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already in use.");
		if(registeredUsersRepository.findByUsername(username) != null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already in use.");
	}

}
