package user;

import java.util.List;

public class RegisteredUser extends SystemUser {

	private int bonusPoints;
	
	private List<Reservation> history;
	
	private List<FriendRequest> sentRequests;
	
	private List<FriendRequest> receivedRequests;

	public int getBonusPoints() {
		return bonusPoints;
	}

	public void setBonusPoints(int bonusPoints) {
		this.bonusPoints = bonusPoints;
	}

	public List<Reservation> getHistory() {
		return history;
	}

	public void setHistory(List<Reservation> history) {
		this.history = history;
	}

	public List<FriendRequest> getSentRequests() {
		return sentRequests;
	}

	public void setSentRequests(List<FriendRequest> sentRequests) {
		this.sentRequests = sentRequests;
	}

	public List<FriendRequest> getReceivedRequests() {
		return receivedRequests;
	}

	public void setReceivedRequests(List<FriendRequest> receivedRequests) {
		this.receivedRequests = receivedRequests;
	}
}
