package com.isap.ISAProject.unit.service.user;

import static com.isap.ISAProject.unit.constants.rentacar.RentACarConstants.PAGE_SIZE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import com.isap.ISAProject.model.user.FriendRequest;
import com.isap.ISAProject.model.user.Friendship;
import com.isap.ISAProject.model.user.RegisteredUser;
import com.isap.ISAProject.repository.user.ConfirmationTokenRepository;
import com.isap.ISAProject.repository.user.FriendRequestRepository;
import com.isap.ISAProject.repository.user.FriendshipRepository;
import com.isap.ISAProject.repository.user.RegisteredUserRepository;
import com.isap.ISAProject.service.user.FriendRequestService;
import com.isap.ISAProject.service.user.RegisteredUserService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RegisteredUserServiceTest {

	@InjectMocks
	private RegisteredUserService service;
	
	@Mock
	private RegisteredUserRepository repository;
	
	@Mock
	private FriendshipRepository friendshipRepository;
	
	@Mock
	private FriendRequestRepository friendRequestRepository;
	
	@Mock 
	private ConfirmationTokenRepository confirmationTokenRepository;
	
	@Mock
	private FriendRequestService friendRequestService;
	
	@Mock
	private RegisteredUser mockUser;
	
	@Mock
	private RegisteredUser mockFriend;
	
	@Before
	public void setup() {
		mockUser = new RegisteredUser(
			UsersStaticData.REGISTERED_1_EMAIL, 
			UsersStaticData.REGISTERED_1_USERNAME, 
			UsersStaticData.REGISTERED_1_PASSWORD, 
			UsersStaticData.REGISTERED_1_FIRSTNAME, 
			UsersStaticData.REGISTERED_1_LASTNAME, 
			UsersStaticData.REGISTERED_1_CITY, 
			UsersStaticData.REGISTERED_1_PHONE_NUMBER);
		mockFriend = new RegisteredUser(
			UsersStaticData.REGISTERED_2_EMAIL, 
			UsersStaticData.REGISTERED_2_USERNAME, 
			UsersStaticData.REGISTERED_2_PASSWORD, 
			UsersStaticData.REGISTERED_2_FIRSTNAME, 
			UsersStaticData.REGISTERED_2_LASTNAME, 
			UsersStaticData.REGISTERED_2_CITY, 
			UsersStaticData.REGISTERED_2_PHONE_NUMBER);
	}
	
	@SuppressWarnings("deprecation")
	@Test
	public void testFindAllRegisteredUsers() {
		PageRequest pageRequest = new PageRequest(1, PAGE_SIZE);
		when(repository.findAll(pageRequest))
		.thenReturn(new PageImpl<RegisteredUser>(Arrays.asList(mockUser)
				.subList(0, 1), pageRequest, 1));
		List<RegisteredUser> users = service.findAll(pageRequest);
		assertThat(users).hasSize(1);
		verify(repository, times(1)).findAll(pageRequest);
		verifyNoMoreInteractions(repository);
	}
	
	@Test
	public void testFindById() {
		when(repository.findById(UsersStaticData.REGISTERED_1_ID)).thenReturn(Optional.of(mockUser));
		RegisteredUser user = service.findById(UsersStaticData.REGISTERED_1_ID);
		assertEquals(UsersStaticData.REGISTERED_1_EMAIL, user.getEmail());
		assertEquals(UsersStaticData.REGISTERED_1_USERNAME, user.getUsername());
		verify(repository, times(1)).findById(UsersStaticData.REGISTERED_1_ID);
		verifyNoMoreInteractions(repository);
	}
	
	@Test
	public void testSaveUser() {
		when(repository.save(mockUser)).thenReturn(mockUser);
		RegisteredUser user = service.save(mockUser);
		assertEquals(UsersStaticData.REGISTERED_1_EMAIL, user.getEmail());
		assertEquals(UsersStaticData.REGISTERED_1_USERNAME, user.getUsername());
		verify(repository, times(1)).save(mockUser);
		verifyNoMoreInteractions(repository);
	}
	
	@Test
	public void testGetFriends() {
		when(repository.findById(UsersStaticData.REGISTERED_1_ID)).thenReturn(Optional.of(mockUser));
		when(repository.findFriendsOfUser(UsersStaticData.REGISTERED_1_ID)).thenReturn(Arrays.asList(mockFriend));
		List<RegisteredUser> friends = service.getFriends(UsersStaticData.REGISTERED_1_ID);
		assertThat(friends).hasSize(1);
		assertEquals(friends.get(0).getUsername(), UsersStaticData.REGISTERED_2_USERNAME);
		verify(repository, times(1)).findFriendsOfUser(UsersStaticData.REGISTERED_1_ID);
		verifyNoMoreInteractions(repository);
	}
	
	@Test
	public void testGetReceivedFriendRequests() {
		when(repository.findById(UsersStaticData.REGISTERED_1_ID)).thenReturn(Optional.of(mockUser));
		FriendRequest mockRequest = new FriendRequest();
		mockRequest.setReceiver(mockUser);
		mockRequest.setSender(mockFriend);
		mockUser.getReceivedRequests().add(mockRequest);
		mockFriend.getSentRequests().add(mockRequest);
		List<FriendRequest> requests = service.getReceivedFriendRequestsOfUser(UsersStaticData.REGISTERED_1_ID);
		assertThat(requests).hasSize(1);
		assertEquals(requests.get(0).getReceiver(), mockUser);
		assertEquals(requests.get(0).getSender(), mockFriend);
		verify(repository, times(1)).findById(UsersStaticData.REGISTERED_1_ID);
		verifyNoMoreInteractions(repository);
	}
	
	@Test
	public void testGetSentFriendRequests() {
		when(repository.findById(UsersStaticData.REGISTERED_2_ID)).thenReturn(Optional.of(mockFriend));
		FriendRequest mockRequest = new FriendRequest();
		mockRequest.setReceiver(mockUser);
		mockRequest.setSender(mockFriend);
		mockUser.getReceivedRequests().add(mockRequest);
		mockFriend.getSentRequests().add(mockRequest);
		List<FriendRequest> requests = service.getSentFriendRequestOfUser(UsersStaticData.REGISTERED_2_ID);
		assertThat(requests).hasSize(1);
		assertEquals(requests.get(0).getReceiver(), mockUser);
		assertEquals(requests.get(0).getSender(), mockFriend);
		verify(repository, times(1)).findById(UsersStaticData.REGISTERED_2_ID);
		verifyNoMoreInteractions(repository);		
	}
	
	@Test
	public void testSendFriendRequest() {
		when(repository.findById(UsersStaticData.REGISTERED_1_ID)).thenReturn(Optional.of(mockUser));
		when(repository.findById(UsersStaticData.REGISTERED_2_ID)).thenReturn(Optional.of(mockFriend));
		when(repository.save(mockUser)).thenReturn(mockUser);
		when(repository.save(mockFriend)).thenReturn(mockFriend);
		FriendRequest request = service.sendFriendRequest(UsersStaticData.REGISTERED_1_ID, UsersStaticData.REGISTERED_2_ID);
		assertEquals(request.getReceiver(), mockFriend);
		assertEquals(request.getSender(), mockUser);
		assertThat(mockUser.getSentRequests()).hasSize(1);
		assertThat(mockFriend.getReceivedRequests()).hasSize(1);
	}
	
	@Test
	public void testGetFriendshipsOfUser() {
		when(repository.findById(UsersStaticData.REGISTERED_1_ID)).thenReturn(Optional.of(mockUser));
		Friendship mockFriendship = new Friendship();
		mockFriendship.getFriends().add(mockFriend);
		mockFriendship.getFriends().add(mockUser);
		mockUser.getFriendships().add(mockFriendship);
		List<Friendship> friendships = service.getFriendshipsOfUser(UsersStaticData.REGISTERED_1_ID);
		assertThat(friendships).hasSize(1);
		assertThat(friendships.get(0).getFriends().contains(mockFriend));
		assertThat(friendships.get(0).getFriends().contains(mockUser));
		verify(repository, times(1)).findById(UsersStaticData.REGISTERED_1_ID);
		verifyNoMoreInteractions(repository);	
	}

	@Test
	public void testDeclineFriendRequest() {
		when(repository.findById(UsersStaticData.REGISTERED_1_ID)).thenReturn(Optional.of(mockUser));
		when(repository.findById(UsersStaticData.REGISTERED_2_ID)).thenReturn(Optional.of(mockFriend));
		FriendRequest mockRequest = new FriendRequest();
		mockRequest.setReceiver(mockUser);
		mockRequest.setSender(mockFriend);
		mockUser.getReceivedRequests().add(mockRequest);
		mockFriend.getSentRequests().add(mockRequest);
		service.declineFriendRequest(UsersStaticData.REGISTERED_1_ID, UsersStaticData.REGISTERED_2_ID);
		assertThat(mockUser.getSentRequests()).hasSize(0);
		assertThat(mockFriend.getReceivedRequests()).hasSize(0);
		assertThat(mockUser.getFriendships()).hasSize(0);
		assertThat(mockFriend.getFriendships()).hasSize(0);
	}
	
}
