package com.isap.ISAProject.unit.service.user;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.junit4.SpringRunner;

import com.isap.ISAProject.model.user.CompanyAdmin;
import com.isap.ISAProject.model.user.RegisteredUser;
import com.isap.ISAProject.model.user.UsersAdmin;
import com.isap.ISAProject.repository.user.CompanyAdminRepository;
import com.isap.ISAProject.repository.user.ConfirmationTokenRepository;
import com.isap.ISAProject.repository.user.RegisteredUserRepository;
import com.isap.ISAProject.repository.user.UsersAdminRepository;
import com.isap.ISAProject.security.TokenUtils;
import com.isap.ISAProject.service.EmailSenderService;
import com.isap.ISAProject.service.user.UserService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {

	@InjectMocks
	private UserService service;
	
	@Mock
	private CompanyAdminRepository companyAdminsRepository;

	@Mock
	private UsersAdminRepository usersAdminsRepository;

	@Mock
	private RegisteredUserRepository registeredUsersRepository;

	@Mock
	private ConfirmationTokenRepository confirmationTokenRepository;

	@Mock
	private EmailSenderService emailService;

	@Value("X-Auth-Token")
	private String tokenHeader;

	@Mock
	private AuthenticationManager authenticationManager;

	@Mock
	private TokenUtils tokenUtils;

	@Mock
	private UserDetailsService userDetailsService;
	
	@Test
	public void testCreateCompanyAdmin() {
		CompanyAdmin mockAdmin = Mockito.mock(CompanyAdmin.class);
		when(mockAdmin.getUsername()).thenReturn(UsersStaticData.COMPANY_ADMIN_1_USERNAME);
		when(mockAdmin.getFirstName()).thenReturn(UsersStaticData.COMPANY_ADMIN_1_FIRSTNAME);
		when(mockAdmin.getLastName()).thenReturn(UsersStaticData.COMPANY_ADMIN_1_LASTNAME);
		when(mockAdmin.getEmail()).thenReturn(UsersStaticData.COMPANY_ADMIN_1_EMAIL);
		when(mockAdmin.getPhoneNumber()).thenReturn(UsersStaticData.COMPANY_ADMIN_1_PHONE_NUMBER);
		when(mockAdmin.getCity()).thenReturn(UsersStaticData.COMPANY_ADMIN_1_CITY);
		when(mockAdmin.getId()).thenReturn(UsersStaticData.COMPANY_ADMIN_1_ID);
		when(mockAdmin.getAuthority()).thenReturn(UsersStaticData.COMPANY_ADMIN_1_AUTHORIZATION);
		when(mockAdmin.getState()).thenReturn(UsersStaticData.COMPANY_ADMIN_1_STATE);
		when(mockAdmin.getPassword()).thenReturn(UsersStaticData.COMPANY_ADMIN_1_PASSWORD);
		when(companyAdminsRepository.save(mockAdmin)).thenReturn(mockAdmin);
		CompanyAdmin admin = service.createCompanyAdmin(mockAdmin);
		assertEquals(admin.getFirstName(), UsersStaticData.COMPANY_ADMIN_1_FIRSTNAME);
		assertEquals(admin.getLastName(), UsersStaticData.COMPANY_ADMIN_1_LASTNAME);
		assertEquals(admin.getEmail(), UsersStaticData.COMPANY_ADMIN_1_EMAIL);
		assertEquals(admin.getPhoneNumber(), UsersStaticData.COMPANY_ADMIN_1_PHONE_NUMBER);
		assertEquals(admin.getCity(), UsersStaticData.COMPANY_ADMIN_1_CITY);
		assertEquals(admin.getId(), UsersStaticData.COMPANY_ADMIN_1_ID);
		assertEquals(admin.getAuthority(), UsersStaticData.COMPANY_ADMIN_1_AUTHORIZATION);
		assertEquals(admin.getState(), UsersStaticData.COMPANY_ADMIN_1_STATE);
		assertEquals(admin.getFirstName(), UsersStaticData.COMPANY_ADMIN_1_FIRSTNAME);
	}
	
	@Test
	public void testCreateUserAdmin() {
		UsersAdmin mockAdmin = Mockito.mock(UsersAdmin.class);
		when(mockAdmin.getUsername()).thenReturn(UsersStaticData.USERS_ADMIN_1_USERNAME);
		when(mockAdmin.getFirstName()).thenReturn(UsersStaticData.USERS_ADMIN_1_FIRSTNAME);
		when(mockAdmin.getLastName()).thenReturn(UsersStaticData.USERS_ADMIN_1_LASTNAME);
		when(mockAdmin.getEmail()).thenReturn(UsersStaticData.USERS_ADMIN_1_EMAIL);
		when(mockAdmin.getPhoneNumber()).thenReturn(UsersStaticData.USERS_ADMIN_1_PHONE_NUMBER);
		when(mockAdmin.getCity()).thenReturn(UsersStaticData.USERS_ADMIN_1_CITY);
		when(mockAdmin.getId()).thenReturn(UsersStaticData.USERS_ADMIN_1_ID);
		when(mockAdmin.getAuthority()).thenReturn(UsersStaticData.USERS_ADMIN_1_AUTHORIZATION);
		when(mockAdmin.getState()).thenReturn(UsersStaticData.USERS_ADMIN_1_STATE);
		when(mockAdmin.getPassword()).thenReturn(UsersStaticData.USERS_ADMIN_1_PASSWORD);
		when(usersAdminsRepository.save(mockAdmin)).thenReturn(mockAdmin);
		UsersAdmin admin = service.createUserAdmin(mockAdmin);
		assertEquals(admin.getFirstName(), UsersStaticData.USERS_ADMIN_1_FIRSTNAME);
		assertEquals(admin.getLastName(), UsersStaticData.USERS_ADMIN_1_LASTNAME);
		assertEquals(admin.getEmail(), UsersStaticData.USERS_ADMIN_1_EMAIL);
		assertEquals(admin.getPhoneNumber(), UsersStaticData.USERS_ADMIN_1_PHONE_NUMBER);
		assertEquals(admin.getCity(), UsersStaticData.USERS_ADMIN_1_CITY);
		assertEquals(admin.getId(), UsersStaticData.USERS_ADMIN_1_ID);
		assertEquals(admin.getAuthority(), UsersStaticData.USERS_ADMIN_1_AUTHORIZATION);
		assertEquals(admin.getState(), UsersStaticData.USERS_ADMIN_1_STATE);
		assertEquals(admin.getFirstName(), UsersStaticData.USERS_ADMIN_1_FIRSTNAME);		
	}
	
	@Test
	public void testCreateRegisteredUser() {
		RegisteredUser mockUser = Mockito.mock(RegisteredUser.class);
		when(mockUser.getUsername()).thenReturn(UsersStaticData.REGISTERED_1_USERNAME);
		when(mockUser.getFirstName()).thenReturn(UsersStaticData.REGISTERED_1_FIRSTNAME);
		when(mockUser.getLastName()).thenReturn(UsersStaticData.REGISTERED_1_LASTNAME);
		when(mockUser.getEmail()).thenReturn(UsersStaticData.REGISTERED_1_EMAIL);
		when(mockUser.getPhoneNumber()).thenReturn(UsersStaticData.REGISTERED_1_PHONE_NUMBER);
		when(mockUser.getCity()).thenReturn(UsersStaticData.REGISTERED_1_CITY);
		when(mockUser.getId()).thenReturn(UsersStaticData.REGISTERED_1_ID);
		when(mockUser.getAuthority()).thenReturn(UsersStaticData.REGISTERED_1_AUTHORIZATION);
		when(mockUser.getState()).thenReturn(UsersStaticData.REGISTERED_1_STATE);
		when(mockUser.getPassword()).thenReturn(UsersStaticData.REGISTERED_1_PASSWORD);
		when(registeredUsersRepository.save(mockUser)).thenReturn(mockUser);
		RegisteredUser admin = service.createRegisteredUser(mockUser);
		assertEquals(admin.getFirstName(), UsersStaticData.REGISTERED_1_FIRSTNAME);
		assertEquals(admin.getLastName(), UsersStaticData.REGISTERED_1_LASTNAME);
		assertEquals(admin.getEmail(), UsersStaticData.REGISTERED_1_EMAIL);
		assertEquals(admin.getPhoneNumber(), UsersStaticData.REGISTERED_1_PHONE_NUMBER);
		assertEquals(admin.getCity(), UsersStaticData.REGISTERED_1_CITY);
		assertEquals(admin.getId(), UsersStaticData.REGISTERED_1_ID);
		assertEquals(admin.getAuthority(), UsersStaticData.REGISTERED_1_AUTHORIZATION);
		assertEquals(admin.getState(), UsersStaticData.REGISTERED_1_STATE);
		assertEquals(admin.getFirstName(), UsersStaticData.REGISTERED_1_FIRSTNAME);		
	}
	
}
